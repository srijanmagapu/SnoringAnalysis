package audioProcessors.EnergyProcessors;

import audioProcessors.FFTProcessor;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.util.fft.FFT;

public class FDEnergyProcessor2 implements AudioProcessor
{
	int bufferSize;
	int energyBlockSize;
	FFTProcessor fftProcessor;
	double[] energyBuffer;
	
	public double[] getEnergyBuffer()
	{
		return energyBuffer;
	}
	
	public FFT getFFT()
	{
		return fftProcessor.getFFT();
	}
	
	public FDEnergyProcessor2(int bufferSize, int energyBlockSize)
	{
		this.bufferSize = bufferSize;
		this.energyBlockSize = energyBlockSize;
		this.fftProcessor = new FFTProcessor(bufferSize);
	}

	@Override
	public boolean process(AudioEvent audioEvent)
	{
		this.fftProcessor.process(audioEvent);
		return true;
	}

	@Override
	public void processingFinished()
	{
		energyBuffer = computeEnergy(fftProcessor.getAmplitudes(), energyBlockSize);
	}
	
	double[] computeEnergy(float[] amplitudes, int blockSize)
	{
		double[] energy = new double[amplitudes.length / blockSize];
		for(int i = 0; i < amplitudes.length / blockSize; i++)
		{
			for(int j = 0; j < blockSize; j++)
			{
				float amp = amplitudes[i*blockSize + j];
				energy[i] += amp * amp;
			}
			
			energy[i] /= blockSize;
		}
		
		int midPoint = energy.length / 2;
		double[] energy2 = new double[midPoint];
		for(int i = 0; i < midPoint; i++)
			energy2[i] = energy[i] / energy[midPoint + i];
		
		
		return energy2;
		
	}
}
