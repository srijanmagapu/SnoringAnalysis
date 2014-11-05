package audioProcessors.EnergyProcessors;

import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.util.fft.FFT;
import be.tarsos.dsp.util.fft.HannWindow;

public class FDRelativeEnergyProcessor implements AudioProcessor
{
	int bufferSize;
	int energyBlockSize;
	double[] energyBuffer;
	int factor = 8;
	FFT fft;
	int frames;

	public double[] getEnergyBuffer()
	{
		return energyBuffer;
	}

	public FFT getFFT()
	{
		return fft;
	}

	public FDRelativeEnergyProcessor(int bufferSize, int energyBlockSize)
	{
		this.bufferSize = bufferSize;
		this.energyBlockSize = energyBlockSize;
		this.energyBuffer = new double[1024 * factor];
		this.fft = new FFT(bufferSize, new HannWindow());
	}

	@Override
	public boolean process(AudioEvent audioEvent)
	{
		float[] floatData = audioEvent.getFloatBuffer();

		// copy float data
		float[] transformBuffer = new float[floatData.length];
		System.arraycopy(floatData, 0, transformBuffer, 0, floatData.length);

		// apply fft
		float[] abs = new float[transformBuffer.length / 2];
		fft.forwardTransform(transformBuffer);
		fft.modulus(transformBuffer, abs);
		if (frames == energyBuffer.length)
		{
			double[] temp = new double[energyBuffer.length + 1024 * factor];
			System.arraycopy(energyBuffer, 0, temp, 0, energyBuffer.length);
			energyBuffer = temp;
		}
		energyBuffer[frames] = computeEnergy(abs);

		frames++;
		return true;
	}

	@Override
	public void processingFinished()
	{
		double[] temp = new double[frames];
		System.arraycopy(energyBuffer, 0, temp, 0, frames);
		energyBuffer = temp;
	}

	double computeEnergy(float[] amplitudes)
	{
		float lowEnergy = 0;
		float hiEnergy = 0;

		int midPoit = amplitudes.length / 2;
		for (int i = 0; i < midPoit; i++)
		{
			lowEnergy += amplitudes[i] * amplitudes[i];
			hiEnergy += amplitudes[i + midPoit] * amplitudes[i + midPoit];

		}

		//return lowEnergy == 0 || hiEnergy == 0 ? 0 : 20*Math.log10((lowEnergy / hiEnergy) / amplitudes.length);
		return lowEnergy == 0 || hiEnergy == 0 ? 0 : ((lowEnergy / hiEnergy) / amplitudes.length);
	}
}
