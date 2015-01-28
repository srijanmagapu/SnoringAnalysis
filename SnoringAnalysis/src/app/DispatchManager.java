package app;

import gui.graphs.AreaGraph;
import gui.interfaces.IMainFrame;
import gui.interfaces.IPatientView;
import gui.interfaces.IPlaySoundSwitcher;
import gui.interfaces.IProgressBar;
import gui.interfaces.ISignalGraph;
import gui.interfaces.ISourcePanel.SoundSource;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import model.ProgressData;
import model.SignalBuffer;
import utils.Constants;
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
import controllers.EnergyGraphController;
import controllers.FDGraphController;
import controllers.IStartProcessingHandler;
import controllers.PatientViewController;
import controllers.ProcessProgressController;
import controllers.SignalGraphController;

/**
 * This class is the heart of the system. Here is the sound processing is initialized and started.
 * It uses TarsosDSP framework for reading and processing files.
 * It can work in two modes: Training and Analyzing - the first used for creating data base of
 * features, when the later analyzes the sound record, extracts sound events and categorizes it
 * based on data base created by Training mode.
 * @author Gao
 *
 */
public class DispatchManager implements IStartProcessingHandler, Runnable, IModeSwitcher, IPlaySoundSwitcher
{
	/**
	 * The size of window in ms that shall be dispatched by AudioDispatcher
	 */
	private static final int windowSizeInMs = 50;
	/**
	 * The required stream sample rate
	 */
	private static final int sampleRate = 10240;
	/**
	 * The size of window in samples that shall be dispatched by AudioDispatcher
	 */
	private static final int audioBufferSize = sampleRate / 1000 * windowSizeInMs;	//512
	/**
	 * The required overlap between two sequential windows dispatched by AudioDispatcher
	 */
	private static final int bufferOverlap = audioBufferSize / 2; 	//256
	
	/**
	 * Minimal frequency band. All data below this value shall be filtered out before
	 * feature extraction is started
	 */
	private static final int minFreq = 150;
	/**
	 * Maximum frequency band. All data above this value shall be filtered out before
	 * feature extraction is started
	 */
	private static final int maxFreq = 5000;
	
	/**
	 * The working frequency range
	 */
	private static final int freqWidth = (maxFreq - minFreq)/2;
	/**
	 * The central point of working frequency range
	 */
	private static final int centerFreq = freqWidth + minFreq;
	
	/**
	 * The frequency band size that shall be used for computation of each energy value
	 */
	private static final int energyFreqBand = 500;
	private static final int numOfEnergyBands = maxFreq / energyFreqBand;	//10
	
	/**
	 * Required amount of MFCC coefs
	 */
	private static final int amountOfCepstrumCoef = 10;
	/**
	 * Required amount of MFCC filters
	 */
	private static final int amountOfMelFilters = 10;
	
	/**
	 * The main AudioDispatcher used for actual sound processing
	 */
	private AudioDispatcher mainDispatcher;
	/**
	 * The dummy processor used to interrupt sound processing
	 */
	private DummyProcessor dummyProcessor;
	
	/**
	 * The processing mode
	 */
	private Mode processingMode = Mode.Training;
	
	/**
	 * Indicates if the processed sound shall be played
	 */
	private boolean playAudio;
	/**
	 * Audio player used to play audio during processing
	 */
	private SwitchableAudioPlayer audioPlayer = null;
	
	/**
	 * The main GUI frame
	 */
	private IMainFrame mainFrame;
	
	/**
	 * Array of files that shall be processed
	 */
	private String[] filesToProcess;
	
	/**
	 * The sound source to get the data to process
	 */
	private SoundSource soundSource = SoundSource.File;
	
	/**
	 * Create DispatchManager
	 * @param frame - the main frame used for sending commands 
	 * to dispatcher and receiving results.
	 */
	public DispatchManager(IMainFrame frame)
	{
		this.mainFrame = frame;
		frame.setStartStopProcessingHandler(this);
		frame.getSourcePanel().registerPlaySoundSwither(this);
	}
	
	
	@Override
	public void startProcessing(SoundSource source, String path, Mode mode)
	{
		startProcessing(source, new String[]{path}, mode);
	}
	
	@Override
	public void startProcessing(SoundSource source, String[] pathes, Mode mode)
	{
		switchMode(mode);
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
	
	/**
	 * Process files from array
	 * @param pathes - array of full paths to files that shall be processed
	 */
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

		FeatureWorker worker = new FeatureWorker(FeatureQueue.getInstance().initQueue());
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

	/**
	 * Performed before actual processing is started.
	 * It used for analyzing the sound file and 
	 * calculating the height of V-BOX. 
	 * 
	 * @param path - full path to the file
	 * @return V-BOX height
	 * @throws UnsupportedAudioFileException
	 * @throws IOException
	 */
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
	
	/**
	 * The actual processing of the file. It creates the TarsosDSP AudioDispatcher
	 * based on file, that it's full path is passed as a parameter.
	 * It uses following processors:
	 * 	1. BandPass filter to filter the signal in range [150, 5000].
	 * 	2. VerticalBoxProcessor that recognizes the sound events.
	 * 	3. STFTEnergyProcssor that calculates the signal Temporal Energy.
	 * 	4. MFCCProcessor that computes the MFCC.
	 * @param vboxHeight - the V-Box H parameter to use
	 * @param path - the full path to the file to process
	 * @throws UnsupportedAudioFileException
	 * @throws IOException
	 */
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
			
			// MFCC graph
			final ISignalGraph mfccView = mainFrame.getIGraphsPanel().getMFCCGraphPanel();
			final SignalBuffer mfccBuffer = mfccProcessor.getSignalBuffer();
			//SignalGraphController mfccControler = new SignalGraphController(mfccView, mfccBuffer);
			new SignalGraphController(mfccView, mfccBuffer);
			
			// Patient Panel
			final IPatientView patientView = mainFrame.getPatientView();
			final SignalBuffer patientBuffer = dummyProcessor.getSignalBuffer();
			new PatientViewController(patientView, patientBuffer);
			
		}
		else if(processingMode == Mode.Training)
		{
			switchSound(false);
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
		if(dummyProcessor != null)
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
