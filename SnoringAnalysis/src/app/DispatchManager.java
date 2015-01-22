package app;

import java.io.File;
import java.io.IOException;

import gui.graphs.AreaGraph;
import gui.interfaces.IMainFrame;
import gui.interfaces.IPlaySoundSwitcher;
import gui.interfaces.IProgressBar;
import gui.interfaces.ISignalGraph;
import gui.interfaces.ISourcePanel.SoundSource;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import utils.Constants;
import model.ProgressData;
import model.SignalBuffer;
import controllers.EnergyGraphController;
import controllers.FDGraphController;
import controllers.IStartProcessingHandler;
import controllers.ProcessProgressController;
import controllers.SignalGraphController;
import audioProcessors.DummyProcessor;
import audioProcessors.MFCCProcessor;
import audioProcessors.PreProcessor;
import audioProcessors.STFTEnergyProcssor;
import audioProcessors.SwitchableAudioPlayer;
import audioProcessors.VerticalBoxProcessor;
import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.filters.BandPass;
import be.tarsos.dsp.io.jvm.JVMAudioInputStream;
import businessLayer.DBCreator;
import businessLayer.FeatureProcessor;
import businessLayer.FeatureQueue;
import businessLayer.FeatureWorker;
import businessLayer.IFeatureConsumer;
import businessLayer.IModeSwitcher;

public class DispatchManager implements IStartProcessingHandler, Runnable, IModeSwitcher, IPlaySoundSwitcher
{
	private static final int windowSizeInMs = 50;
	private static final int sampleRate = 10240;
	private static final int audioBufferSize = sampleRate / 1000 * windowSizeInMs;	//512
	private static final int bufferOverlap = audioBufferSize / 2; 	//256
	
	private static final int minFreq = 150;
	private static final int maxFreq = 5000;
	
	private static final int freqWidth = (maxFreq - minFreq)/2;
	private static final int centerFreq = freqWidth + minFreq;
	
	private static final int energyFreqBand = 500;
	private static final int numOfEnergyBands = maxFreq / energyFreqBand;	//10
	
	private static final int amountOfCepstrumCoef = 10;
	private static final int amountOfMelFilters = 10;
	
	private AudioDispatcher mainDispatcher;
	private DummyProcessor dummyProcessor;
	
	private Mode processingMode = Mode.Training;
	
	private boolean playAudio;
	private SwitchableAudioPlayer audioPlayer = null;
	
	private IMainFrame mainFrame;
	
	private String[] filesToProcess;
	private SoundSource soundSource = SoundSource.File;
	
	public DispatchManager(IMainFrame frame)
	{
		this.mainFrame = frame;
		frame.setStartStopProcessingHandler(this);
		frame.getSourcePanel().registerPlaySoundSwither(this);
	}
	
	
	@Override
	public void startProcessing(SoundSource source, String path)
	{
		startProcessing(source, new String[]{path});
	}
	
	@Override
	public void startProcessing(SoundSource source, String[] pathes)
	{
		this.filesToProcess = pathes.clone();
		this.soundSource = source;
		
		Thread t = new Thread(this, "DispatchManager");
		System.out.println(t.getName() + " : " + t.getId());
		t.start();
	}
	
	@Override
	public void run()
	{
		switch(soundSource)
		{
		case File:
			processFiles(filesToProcess);
			break;
			
		case Mic:
			processMic();
			break;
		}
	}
	
	private void processFiles(String[] pathes)
	{
		//set FeatureWorker
		IFeatureConsumer iConsumer = null;
		switch (processingMode)
		{
		case Training:
			iConsumer = new DBCreator(Constants.numOfDimensions, Constants.numOfClusters);
			break;
		case Analyzing:
			iConsumer = new FeatureProcessor(Constants.numOfDimensions, Constants.numOfClusters);
			break;
		}

		FeatureWorker worker = new FeatureWorker(FeatureQueue.getInstance());
		worker.setIConsumer(iConsumer);
		(new Thread(worker, "FeatureWorker")).start();
		
		//process files
		for(String path : pathes)
		{
			try
			{
				double vboxHeight = preProcessAudio(path);
				processAudio( vboxHeight, path );
			}
			catch (UnsupportedAudioFileException e)
			{
				System.err.println("Exception while processing " + path);
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		//this will finish processing
		FeatureWorker.stopAllWorkers();
	}
	
	private void processMic()
	{
		
	}

	
	private double preProcessAudio(String path) throws UnsupportedAudioFileException, IOException
	{
		JVMAudioInputStream preProcessorStream = new JVMAudioInputStream(AudioSystem.getAudioInputStream(new File(path)) );
		AudioDispatcher preProcessorDispatcher = new AudioDispatcher(preProcessorStream, audioBufferSize, 0);

		BandPass bandPass = new BandPass(centerFreq, freqWidth, sampleRate);
		PreProcessor preProessor = new PreProcessor();
		
		preProcessorDispatcher.addAudioProcessor(bandPass);
		preProcessorDispatcher.addAudioProcessor(preProessor);
		preProcessorDispatcher.run();
		
		return preProessor.getCaculatedheight();
	}
	
	public void processAudio( double vboxHeight, String path ) throws UnsupportedAudioFileException, IOException
	{
		JVMAudioInputStream stream = new JVMAudioInputStream(AudioSystem.getAudioInputStream(new File(path)) );
		mainDispatcher = new AudioDispatcher(stream, audioBufferSize, bufferOverlap);

		//============  Filter  ===========
		BandPass bandPass = new BandPass(centerFreq, freqWidth, sampleRate);
		
		//============  V-Box  ============
		VerticalBoxProcessor vboxProcessor = new VerticalBoxProcessor((float) vboxHeight);
		
		//============  Energy  ============
		STFTEnergyProcssor energyProcessor = new STFTEnergyProcssor(audioBufferSize, numOfEnergyBands);
		energyProcessor.setIVerticalBoxProcessor(vboxProcessor);
		
		//============  MFCC  ============
		MFCCProcessor mfccProcessor = new MFCCProcessor(audioBufferSize, sampleRate, amountOfCepstrumCoef, amountOfMelFilters, minFreq, maxFreq);
		mfccProcessor.setIVerticalBoxProcessor(vboxProcessor);
		
		//============  Dummy processor  ============
		dummyProcessor = new DummyProcessor();
		dummyProcessor.setInterruptProcessing(false);
		
		// Progress bar
		final IProgressBar progressBarView = mainFrame.getIProgressBar();
		final ProgressData progressData = dummyProcessor.getProgressData();
		//ProcessProgressController progressController = new ProcessProgressController(progressBarView, progressData);
		new ProcessProgressController(progressBarView, progressData);
		
		if (processingMode == Mode.Analyzing)
		{
			// TD graph
			final ISignalGraph tdView = mainFrame.getIGraphsPanel().getTDGraphPanel();
			final SignalBuffer tdBuffer = energyProcessor.getSignalTDBuffer();
			//SignalGraphController tdControler = new SignalGraphController(tdView, tdBuffer);
			new SignalGraphController(tdView, tdBuffer);

			// FD graph
			final AreaGraph fdView = mainFrame.getIGraphsPanel().getFDGraphPanel();
			final SignalBuffer fdBuffer = energyProcessor.getSignalFFTBuffer();
			FDGraphController fdControler = new FDGraphController(fdView, fdBuffer);
			fdControler.setFFT(energyProcessor.getFFT());

			// Energy graph
			final AreaGraph energyView = mainFrame.getIGraphsPanel().getEnergyGraphPanel();
			final SignalBuffer energyBuffer = energyProcessor.getSignalEnergyBuffer();
			//EnergyGraphController energyControler = new EnergyGraphController(energyView, energyBuffer);
			new EnergyGraphController(energyView, energyBuffer);
			
			//MFCC graph
			final ISignalGraph mfccView = mainFrame.getIGraphsPanel().getMFCCGraphPanel();
			final SignalBuffer mfccBuffer = mfccProcessor.getSignalBuffer();
			//SignalGraphController mfccControler = new SignalGraphController(mfccView, mfccBuffer);
			new SignalGraphController(mfccView, mfccBuffer);
			
		}
		
		//Audio Player
		try
		{
			audioPlayer = new SwitchableAudioPlayer(JVMAudioInputStream.toAudioFormat(mainDispatcher.getFormat()));
			audioPlayer.setPlayAudio(playAudio);
		}
		catch (LineUnavailableException e)
		{
			System.err.println("Can't create audio player for " + path);
			e.printStackTrace();
		}

		//============  add processors  ============
		if(audioPlayer != null)
			mainDispatcher.addAudioProcessor(audioPlayer);
		
		mainDispatcher.addAudioProcessor(bandPass);
		mainDispatcher.addAudioProcessor(vboxProcessor);
		mainDispatcher.addAudioProcessor(energyProcessor);
		mainDispatcher.addAudioProcessor(mfccProcessor);
		mainDispatcher.addAudioProcessor(dummyProcessor);
		
		mainDispatcher.run();
	}

	
	@Override
	public void stopProcessing()
	{
		dummyProcessor.setInterruptProcessing(true);
	}


	@Override
	public void switchMode(Mode mode)
	{
		this.processingMode = mode;
	}


	@Override
	public void switchSound(boolean enable)
	{
		playAudio = enable;
		
		if(audioPlayer != null)
			audioPlayer.setPlayAudio(enable);
	}

}
