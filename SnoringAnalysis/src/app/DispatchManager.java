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
	private Thread mainDispatcherThread;
	private DummyProcessor dummyProcessor;
	
	private Mode processingMode = Mode.Training;
	
	private boolean playAudio;
	private SwitchableAudioPlayer audioPlayer = null;
	
	private IMainFrame mainFrame;
	
	private JVMAudioInputStream stream;
	private JVMAudioInputStream preProcessorStream;
	
	public DispatchManager(IMainFrame frame)
	{
		this.mainFrame = frame;
		frame.getSourcePanel().registerStartStopHandler(this);
		frame.getSourcePanel().registerPlaySoundSwither(this);
	}
	

	@Override
	public void run()
	{
		try
		{
			double vboxHeight = preProcessAudio();
			processAudio( vboxHeight );
		}
		catch (UnsupportedAudioFileException e)
		{
			e.printStackTrace();
		}
	}
	
	private double preProcessAudio()
	{
		AudioDispatcher preProcessorDispatcher = new AudioDispatcher(preProcessorStream, audioBufferSize, 0);

		BandPass bandPass = new BandPass(centerFreq, freqWidth, sampleRate);
		PreProcessor preProessor = new PreProcessor();
		
		preProcessorDispatcher.addAudioProcessor(bandPass);
		preProcessorDispatcher.addAudioProcessor(preProessor);
		preProcessorDispatcher.run();
		
		preProcessorDispatcher.stop();
		return preProessor.getCaculatedheight();
	}
	
	public void processAudio( double vboxHeight ) throws UnsupportedAudioFileException
	{
		mainDispatcher = new AudioDispatcher(stream, audioBufferSize, bufferOverlap);

		//============  Filter  ===========
		BandPass bandPass = new BandPass(centerFreq, freqWidth, sampleRate);
		
		//============  V-Box  ============
		VerticalBoxProcessor vboxProcessor = new VerticalBoxProcessor((float) vboxHeight);
		
		//============  Energy  ============
		STFTEnergyProcssor energyProcessor = new STFTEnergyProcssor(audioBufferSize, numOfEnergyBands);
		energyProcessor.setIVerticalBoxProcessor(vboxProcessor);
		
		// TD graph
		final ISignalGraph tdView = mainFrame.getIGraphsPanel().getTDGraphPanel();
		final SignalBuffer tdBuffer = energyProcessor.getSignalTDBuffer();
		SignalGraphController tdControler = new SignalGraphController(tdView, tdBuffer);
		
		// FD graph
		final AreaGraph fdView = mainFrame.getIGraphsPanel().getFDGraphPanel();
		final SignalBuffer fdBuffer = energyProcessor.getSignalFFTBuffer();
		FDGraphController fdControler = new FDGraphController(fdView, fdBuffer);
		fdControler.setFFT(energyProcessor.getFFT());
		
		// Energy graph
		final AreaGraph energyView = mainFrame.getIGraphsPanel().getEnergyGraphPanel();
		final SignalBuffer energyBuffer = energyProcessor.getSignalEnergyBuffer();
		EnergyGraphController energyControler = new EnergyGraphController(energyView, energyBuffer);
		
		//============  MFCC  ============
		MFCCProcessor mfccProcessor = new MFCCProcessor(audioBufferSize, sampleRate, amountOfCepstrumCoef, amountOfMelFilters, minFreq, maxFreq);
		mfccProcessor.setIVerticalBoxProcessor(vboxProcessor);
		
		//MFCC graph
		final ISignalGraph mfccView = mainFrame.getIGraphsPanel().getMFCCGraphPanel();
		final SignalBuffer mfccBuffer = mfccProcessor.getSignalBuffer();
		SignalGraphController mfccControler = new SignalGraphController(mfccView, mfccBuffer);
		
		//Dummy processor
		dummyProcessor = new DummyProcessor();
		dummyProcessor.setInterruptProcessing(false);
		
		final IProgressBar progressBarView = mainFrame.getIProgressBar();
		final ProgressData progressData = dummyProcessor.getProgressData();
		ProcessProgressController progressController = new ProcessProgressController(progressBarView, progressData);
		
		//Audio Player
		try
		{
			audioPlayer = new SwitchableAudioPlayer(JVMAudioInputStream.toAudioFormat(mainDispatcher.getFormat()));
			audioPlayer.setPlayAudio(playAudio);
		}
		catch (LineUnavailableException e)
		{
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
		mainDispatcher.stop();
		
	}

	@Override
	public void startProcessing(SoundSource source, String path)
	{
		try
		{
			switch(source)
			{
			case File:
				preProcessorStream = new JVMAudioInputStream(AudioSystem.getAudioInputStream(new File(path)) );
				stream = new JVMAudioInputStream(AudioSystem.getAudioInputStream(new File(path)) );
				
				break;
				
			case Mic:
				break;
			}
			
			FeatureWorker worker = new FeatureWorker(FeatureQueue.getInstance());
			
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
			
			worker.setIConsumer(iConsumer);
			(new Thread(worker, "FeatureWorker")).start();
			
			/*SwingUtilities.invokeLater(new SwingWorker(){

				@Override
				protected Object doInBackground() throws Exception
				{
					// TODO Auto-generated method stub
					return null;
				}
				
			});*/
			Thread t = new Thread(this, "DispatchManager");
			System.out.println(t.getName() + " : " + t.getId());
			t.start();
		}
		catch(UnsupportedAudioFileException ex)
		{
			ex.printStackTrace();
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
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
