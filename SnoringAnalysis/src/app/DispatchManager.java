package app;

import javax.sound.sampled.UnsupportedAudioFileException;
import audioProcessors.MFCCProcessor;
import audioProcessors.STFTEnergyProcssor;
import audioProcessors.VerticalBoxProcessor;
import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.filters.BandPass;
import be.tarsos.dsp.io.TarsosDSPAudioInputStream;

public class DispatchManager
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
	
	public DispatchManager()
	{
	}
	
	public void initDispatcher(TarsosDSPAudioInputStream stream) throws UnsupportedAudioFileException
	{
		
		mainDispatcher = new AudioDispatcher(stream, audioBufferSize, bufferOverlap);
		
		BandPass bandPass = new BandPass(centerFreq, freqWidth, sampleRate);
		VerticalBoxProcessor vboxProcessor = new VerticalBoxProcessor();
		
		STFTEnergyProcssor energyProcessor = new STFTEnergyProcssor(audioBufferSize, energyFreqBand, numOfEnergyBands);
		energyProcessor.setIVerticalBoxProcessor(vboxProcessor);
		
		MFCCProcessor mfccProcessor = new MFCCProcessor(audioBufferSize, sampleRate, amountOfCepstrumCoef, amountOfMelFilters, minFreq, maxFreq);
		mfccProcessor.setIVerticalBoxProcessor(vboxProcessor);
		
		
		mainDispatcher.addAudioProcessor(bandPass);
		mainDispatcher.addAudioProcessor(vboxProcessor);
		mainDispatcher.addAudioProcessor(energyProcessor);
		mainDispatcher.addAudioProcessor(mfccProcessor);
		
		mainDispatcher.run();
	}
}
