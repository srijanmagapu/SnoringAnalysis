package app;

import java.io.File;
import java.io.IOException;

import gui.ISourcePanel.SoundSource;
import gui.SignalGraph;
import gui.interfaces.ISignalGraph;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import model.SignalBuffer;
import controllers.IStartProcessingHandler;
import controllers.SignalGraphController;
import audioProcessors.DummyProcessor;
import audioProcessors.MFCCProcessor;
import audioProcessors.STFTEnergyProcssor;
import audioProcessors.VerticalBoxProcessor;
import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.filters.BandPass;
import be.tarsos.dsp.io.TarsosDSPAudioInputStream;
import be.tarsos.dsp.io.jvm.JVMAudioInputStream;

public class DispatchManager implements IStartProcessingHandler
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
	
	private IMainFrame mainFrame;
	
	public DispatchManager(IMainFrame frame)
	{
		this.mainFrame = frame;
		frame.getSourcePanel().registerStartStopHandler(this);
	}
	
	public void initDispatcher(TarsosDSPAudioInputStream stream) throws UnsupportedAudioFileException
	{
		
		mainDispatcher = new AudioDispatcher(stream, audioBufferSize, bufferOverlap);
		
		//filter
		BandPass bandPass = new BandPass(centerFreq, freqWidth, sampleRate);
		
		//V-Box
		VerticalBoxProcessor vboxProcessor = new VerticalBoxProcessor();
		
		final ISignalGraph tdView = mainFrame.getIGraphsPanel().getTDGraphPanel();
		final SignalBuffer tdBuffer = vboxProcessor.getSignalBuffer();
		SignalGraphController tdControler = new SignalGraphController(tdView, tdBuffer);
		
		//Energy
		STFTEnergyProcssor energyProcessor = new STFTEnergyProcssor(audioBufferSize, energyFreqBand, numOfEnergyBands);
		energyProcessor.setIVerticalBoxProcessor(vboxProcessor);
		
		//MFCC
		MFCCProcessor mfccProcessor = new MFCCProcessor(audioBufferSize, sampleRate, amountOfCepstrumCoef, amountOfMelFilters, minFreq, maxFreq);
		mfccProcessor.setIVerticalBoxProcessor(vboxProcessor);
		
		//Dummy processor
		dummyProcessor = new DummyProcessor();
		dummyProcessor.setInterruptProcessing(false);
		
		
		mainDispatcher.addAudioProcessor(bandPass);
		mainDispatcher.addAudioProcessor(vboxProcessor);
		mainDispatcher.addAudioProcessor(energyProcessor);
		mainDispatcher.addAudioProcessor(mfccProcessor);
		mainDispatcher.addAudioProcessor(dummyProcessor);
		
		mainDispatcherThread = new Thread(mainDispatcher);
		mainDispatcherThread.start();
	}

	@Override
	public void startProcessing(SoundSource source, String path)
	{
		try
		{
			switch(source)
			{
			case File:
				initDispatcher(new JVMAudioInputStream(AudioSystem.getAudioInputStream(new File(path)) ));
				break;
				
			case Mic:
				initDispatcher(new JVMAudioInputStream(AudioSystem.getAudioInputStream(new File(path)) ));
				break;
			}
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
}
