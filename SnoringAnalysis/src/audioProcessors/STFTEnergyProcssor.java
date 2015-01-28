package audioProcessors;

import java.util.ArrayList;

import model.AudioFeature;
import model.SignalBuffer;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.util.fft.FFT;
import be.tarsos.dsp.util.fft.HammingWindow;
import businessLayer.FeatureQueue;

public class STFTEnergyProcssor implements AudioProcessor
{
	
	/**
	 * Number of bands in the buffer
	 */
	//private final int numOfBands = 10;
	private final int numOfBands;
	
	private int overlap;
	
	private final int MAX_NUMBER_OF_SAMPLES = 3000;
	private ArrayList<float[]> tdCumulativeBuffer;
	private ArrayList<float[]> fdCumulativeBuffer;
	
	double eventStart;
	double eventEnd;
	
	private final SignalBuffer signalEnergyBuffer;
	public SignalBuffer getSignalEnergyBuffer()
	{
		return signalEnergyBuffer;
	}
	
	private final SignalBuffer signalTDBuffer;
	public SignalBuffer getSignalTDBuffer()
	{
		return signalTDBuffer;
	}
	
	private final SignalBuffer signalFFTBuffer;
	public SignalBuffer getSignalFFTBuffer()
	{
		return signalFFTBuffer;
	}
	
	/**
	 * The implementor of V-Box algorithm. Will signal when acoustical event detected.
	 */
	private IVerticalBoxProcessor vboxProcessor;
	
	/**
	 * 
	 * The previous outOfControl state.
	 * The previousOutOfControlState-newOutOfControlState policy is as following:
	 * <pre>
	 * newOutOfControlState = true, previousOutOfControlState = false
	 * 		new acoustical event starts - reset energy computations and compute new energy
	 * 
	 * newOutOfControlState = true, previousOutOfControlState = true
	 * 		the acoustical event continues - compute energy
	 * 
	 * newOutOfControlState = false, previousOutOfControlState = true
	 * 		the acoustical event ended - finalize energy computations
	 * 
	 * newOutOfControlState = false, previousOutOfControlState = false
	 * 		do nothing
	 * </pre>
	 */
	private boolean previousOutOfControlState;

	
	private FFT fft;
	/**
	 * Buffer size for FFT
	 */
	private int bufferSize;

	/**
	 * Energy of the acoustical event computed by:
	 * 
	 * <pre>
	 *      Sum(n=1-->N){ Sum(f=500(i-1)-->500i) {|S(n,f)|^2 }}
	 * E_i= --------------------------------------------------
	 *          Sum(n=1-->N){ Sum(f=0-->5000){ |S(n,f|^2 }}
	 * 
	 * Where:
	 * 	N - number of windows in event
	 * 	i - number of frequency band
	 * 	S - STFT of the window
	 * </pre>
	 */
	private float[] energy;
	private float overallEnergy;

	/**
	 * Get the energy calculated by the processor
	 * @return energy
	 */
	public float[] getEnergy()
	{
		return this.energy;
	}
	
	public FFT getFFT()
	{
		return fft;
	}

	public IVerticalBoxProcessor getIVerticalBoxProcessor()
	{
		return this.vboxProcessor;
	}

	public void setIVerticalBoxProcessor(IVerticalBoxProcessor vboxProcessor)
	{
		this.vboxProcessor = vboxProcessor;
	}

	public STFTEnergyProcssor(int bufferSize, int numOfBands)
	{
		this.bufferSize = bufferSize;
		this.numOfBands = numOfBands;
		
		this.fft = new FFT(bufferSize, new HammingWindow());

		this.signalEnergyBuffer = new SignalBuffer();
		this.signalTDBuffer = new SignalBuffer();
		this.signalFFTBuffer = new SignalBuffer();

		this.previousOutOfControlState = false;
		resetEnergyComputations();
	}

	/*
	 * (non-Javadoc)
	 * @see be.tarsos.dsp.AudioProcessor#process(be.tarsos.dsp.AudioEvent)
	 * 
	 */
	@Override
	public boolean process(AudioEvent audioEvent)
	{
		boolean newOutOfControlState = false;

		if (vboxProcessor != null)
			newOutOfControlState = vboxProcessor.getOutOfControl();
		
		if (newOutOfControlState)
		{
			if(!previousOutOfControlState)
			{
				eventStart = audioEvent.getTimeStamp();
				resetEnergyComputations();
			}
			overlap = audioEvent.getOverlap();
			// event floatBuffer is reused so copy it to another array
			float[] eventFloatBuffer = audioEvent.getFloatBuffer();
			float[] transformBuffer = new float[bufferSize];
			System.arraycopy(eventFloatBuffer, 0, transformBuffer, 0, eventFloatBuffer.length);
			
			//store copy of TD signal for following computations
			tdCumulativeBuffer.add(transformBuffer.clone());

			// apply fft
			fft.forwardTransform(transformBuffer);

			// get square amplitudes for energy
			float[] abs = new float[bufferSize / 2];
			squareModulus(transformBuffer, abs);

			// compute local energy
			computeEnergy(abs);
			
			// get regular amplitudes for FD buffer
			arraySqrt(abs);
			fdCumulativeBuffer.add(abs);
		}
		else if(previousOutOfControlState)
		{
			//compute final energy for the acoustical event detected by v-box
			eventEnd = audioEvent.getTimeStamp();
			finishEnergyComputation();
		}
		
		previousOutOfControlState = newOutOfControlState;
		
		return true;
	}


	/**
	 * Reset energy computations
	 */
	private void resetEnergyComputations()
	{
		this.energy = new float[numOfBands];
		this.overallEnergy = 0;
		
		this.tdCumulativeBuffer = new ArrayList<>();
		this.fdCumulativeBuffer = new ArrayList<>();
	}

	/**
	 * Compute current window energy and sum it with previous windows
	 * @param stft current window square amplitudes
	 */
	private void computeEnergy(float[] stft)
	{
		//if (stft.length != numOfBands * freqBand) throw new IllegalArgumentException("The size of buffer should be " + numOfBands * freqBand);

		int range = stft.length / numOfBands;
		// TODO: start freq and maxFreq from 149? 150?
		for (int i = 0, freq = 0, maxFreq = 0; i < numOfBands; i++)
		{
			maxFreq = freq + range;

			for (; freq < maxFreq; freq++)
				energy[i] += stft[freq];

			overallEnergy += energy[i];
		}
	}

	int count = 0;
	
	/**
	 * Will be called after the acoustical event detected by vbox finished.
	 * Finalizes the energy computations.
	 */
	private void finishEnergyComputation()
	{
		for (int i = 0; i < numOfBands; i++)
			energy[i] /= overallEnergy;
		
		AudioFeature feature = new AudioFeature();
		feature.setData(energy);
		feature.setStartTime(eventStart * 1000);
		feature.setEndTime(eventEnd * 1000);
		
		count++;
		FeatureQueue.getInstance().addEnergyBuffer(feature);

		//create signalTDBuffer
		createTDBuffer();
		
		//create signalFFTBuffer
		createFDBuffer();
		
		//create energyBuffer
		signalEnergyBuffer.setBuffer(energy);
	}
	
	private void createFDBuffer()
	{
		float[] fdArray = new float[fdCumulativeBuffer.get(0).length];
		
		//sum elements
		for(float[] arr : fdCumulativeBuffer)
			for(int i = 0; i < arr.length; i++)
				fdArray[i] += arr[i];
		
		int listSize = fdCumulativeBuffer.size();
		//compute mean
		for(int i = 0; i < fdArray.length; i++)
			fdArray[i] /= listSize;
		
		signalFFTBuffer.setBuffer(fdArray);
	}
	
	private void createTDBuffer()
	{
		int arraySize = tdCumulativeBuffer.get(0).length;
		double step = 1.;
		int numOfSamples = (arraySize - overlap) * (tdCumulativeBuffer.size())  + overlap;
		
		if(numOfSamples > MAX_NUMBER_OF_SAMPLES)
			step = (int)Math.ceil(numOfSamples / (double)MAX_NUMBER_OF_SAMPLES);
		
		float[] cumulativeArray = new float[(int)Math.ceil(numOfSamples / step)];
		int cumulativeIndex = 0;
		double next = 0;
		
		//copy the array part that not overlapped by next array
		for(float[] arr : tdCumulativeBuffer)
		{
			for(int i = 0; i < arraySize - overlap; next += step, i = (int)next)
				cumulativeArray[cumulativeIndex++] = arr[i];
			
			next -= arraySize - overlap;
		}
		
		next = arraySize - overlap + next;
		//copy the last part of last array
		float[] lastArray = tdCumulativeBuffer.get(tdCumulativeBuffer.size()-1);
		for(int i = (int)next; i < arraySize; next += step, i = (int)next)
			cumulativeArray[cumulativeIndex++] = lastArray[i];
		
		signalTDBuffer.setBuffer(cumulativeArray);
	}

	/**
	 * Use this implementation of modulus instead of FFT.modulus for performance reasons.
	 * @param data - float data buffer (FFT result)
	 * @param amplitudes - square amplitudes
	 */
	private void squareModulus(final float[] data, final float[] amplitudes)
	{
		assert data.length / 2 == amplitudes.length;
		for (int i = 0; i < amplitudes.length; i++)
		{
			amplitudes[i] = squareModulus(data, i);
		}
	}

	/**
	 * Use this implementation of modulus instead of FFT.modulus for performance reasons.
	 * @param data
	 * @param index
	 * @return
	 */
	private float squareModulus(final float[] data, final int index)
	{
		final int realIndex = 2 * index;
		final int imgIndex = 2 * index + 1;
		final float modulus = data[realIndex] * data[realIndex] + data[imgIndex] * data[imgIndex];
		return modulus;
	}
	
	private void arraySqrt(float[] data)
	{
		for(int i = 0; i < data.length; i++)
			data[i] = (float)Math.sqrt(data[i]);
	}

	@Override
	public void processingFinished()
	{
		if(overallEnergy != 0)
			finishEnergyComputation();
	}
}
