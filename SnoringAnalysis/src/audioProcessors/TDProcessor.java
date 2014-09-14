package audioProcessors;

import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;

public class TDProcessor implements AudioProcessor
{

	private int overlapping;
	private int frames = 0;
	private float[] originalData = new float[0];
	
	public float[] getFloatBuffer(){
		return originalData;
	}

	
	public TDProcessor(int overlapping)
	{
		this.overlapping = overlapping;
	}
	
	@Override
	public boolean process(AudioEvent audioEvent)
	{

		frames++;
		float[] eventFloatBuffer = audioEvent.getFloatBuffer();
		// copy original data
		if (frames == 1)
		{
			originalData = new float[eventFloatBuffer.length];
			System.arraycopy(eventFloatBuffer, 0, originalData, 0, eventFloatBuffer.length);
		}
		else
		{
			float[] temp = new float[originalData.length + eventFloatBuffer.length - overlapping];
			System.arraycopy(originalData, 0, temp, 0, originalData.length);
			System.arraycopy(eventFloatBuffer, overlapping, temp, originalData.length, eventFloatBuffer.length - overlapping);
			originalData = temp;
		}
		return true;
	}

	@Override
	public void processingFinished()
	{
		System.out.println("TDProcessor finished");
	}

}
