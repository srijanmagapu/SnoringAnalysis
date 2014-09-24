package audioProcessors;

import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;

public class TDProcessor implements AudioProcessor
{

	private int overlapping;
	private int actualOriginalDataSize;
	private int factor = 16;
	private float[] originalData;

	public float[] getFloatBuffer()
	{
		return originalData;
	}

	public TDProcessor(int overlapping)
	{
		this.overlapping = overlapping;
		this.originalData = new float[1024 * factor];
	}

	@Override
	public boolean process(AudioEvent audioEvent)
	{

		float[] eventFloatBuffer = audioEvent.getFloatBuffer();

		if (actualOriginalDataSize + eventFloatBuffer.length > originalData.length)
		{
			float[] temp = new float[originalData.length + (factor * eventFloatBuffer.length)];
			System.arraycopy(originalData, 0, temp, 0, originalData.length);
			originalData = temp;
		}

		System.arraycopy(eventFloatBuffer, overlapping, originalData, actualOriginalDataSize, eventFloatBuffer.length - overlapping);
		actualOriginalDataSize += eventFloatBuffer.length - overlapping;

		return true;
	}

	@Override
	public void processingFinished()
	{
		System.out.println("TDProcessor finished");

		if (originalData.length > actualOriginalDataSize)
		{
			float[] temp = new float[actualOriginalDataSize];
			System.arraycopy(originalData, 0, temp, 0, actualOriginalDataSize);
			originalData = temp;
		}
	}

}
