package audioProcessors;

import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.util.fft.FFT;
import be.tarsos.dsp.util.fft.HammingWindow;
import businessLayer.FeatureQueue;

public class STFTEnergyProcssor implements AudioProcessor
{
	/**
	 * The width of single frequency band
	 */
	//private final int freqBand = 500;
	private final int freqBand;
	
	/**
	 * Number of bands in the buffer
	 */
	//private final int numOfBands = 10;
	private final int numOfBands;

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

	public STFTEnergyProcssor(int bufferSize, int freqBand, int numOfBands)
	{
		this.bufferSize = bufferSize;
		this.freqBand = freqBand;
		this.numOfBands = numOfBands;
		this.fft = new FFT(bufferSize, new HammingWindow());

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
				resetEnergyComputations();
			
			// event floatBuffer is reused so copy it to another array
			float[] eventFloatBuffer = audioEvent.getFloatBuffer();
			float[] transformBuffer = new float[bufferSize];
			System.arraycopy(eventFloatBuffer, 0, transformBuffer, 0, eventFloatBuffer.length);

			// apply fft
			fft.forwardTransform(transformBuffer);

			// get square amplitudes
			float[] abs = new float[bufferSize / 2];
			squareModulus(transformBuffer, abs);

			// compute local energy
			computeEnergy(abs);
		}
		else if(previousOutOfControlState)
		{
			//compute final energy for the acoustical event detected by v-box
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
		
		count++;
		System.out.println("Adding Energy vector " + count);
		FeatureQueue.getInstance().addEnergyBuffer(energy);
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

	@Override
	public void processingFinished()
	{
		if(overallEnergy != 0)
			finishEnergyComputation();
	}
}
