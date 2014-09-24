package audioProcessors.EnergyProcessors;

import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;

public class TDEnergyProcessor implements AudioProcessor
{
	private double[] energyBuffer = new double[1024 * 256];
	private int actualBufferSize = 0;
	private int factor = 64;

	public double[] getEnergyBuffer()
	{
		return energyBuffer;
	}

	@Override
	public boolean process(AudioEvent audioEvent)
	{
		float[] data = audioEvent.getFloatBuffer();

		if (actualBufferSize + data.length > energyBuffer.length)
		{
			double[] temp = new double[energyBuffer.length + (data.length * factor)];
			System.arraycopy(energyBuffer, 0, temp, 0, actualBufferSize);
			energyBuffer = temp;
		}

		energyBuffer[actualBufferSize] = calculateEnergy(data);

		actualBufferSize++;
		return true;
	}

	@Override
	public void processingFinished()
	{
		if (actualBufferSize < energyBuffer.length)
		{
			double[] temp = new double[actualBufferSize];
			System.arraycopy(energyBuffer, 0, temp, 0, actualBufferSize);
			energyBuffer = temp;
		}

	}

	private double calculateEnergy(float[] buff)
	{
		double energy = 0;

		for (double e : buff)
			energy += e * e;

		 double _energy = Math.pow(energy, 0.5) / buff.length;
		 return 20 * Math.log10(_energy);
	}

}
