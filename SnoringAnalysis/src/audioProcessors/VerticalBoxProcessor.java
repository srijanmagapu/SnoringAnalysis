package audioProcessors;

import model.SignalBuffer;
import be.tarsos.dsp.AudioEvent;

public class VerticalBoxProcessor implements IVerticalBoxProcessor
{
	/**
	 * height of the vbox, should be adaptive at some point...
	 */
	private float boxHeight = 0.01f;
	
	/**
	 *  percentage of out of bounds samples in a window that count as out of control
	 */
	private double threshold = 0.05;
	
	/**
	 * was last frame out of control?
	 */
	private boolean outOfControl = false;
	
	private final SignalBuffer signalBuffer;
	
	public SignalBuffer getSignalBuffer()
	{
		return signalBuffer;
	}
	
	public VerticalBoxProcessor()
	{
		this.signalBuffer = new SignalBuffer();
	}
	
	@Override
	public boolean process(AudioEvent audioEvent)
	{
		float[] data = audioEvent.getFloatBuffer();
		int outOfBounds = 0;
		
		for( int i=0 ; i<data.length ; i++ )
		{
			outOfBounds += Math.abs(data[i]) < boxHeight ? 0 : 1;
		}
		
		outOfControl = (double)outOfBounds / (double)data.length > threshold;
		
		if(outOfControl)
			signalBuffer.setBuffer(data);
		
		return true;
	}

	@Override
	public void processingFinished()
	{
	}

	@Override
	public boolean getOutOfControl()
	{
		if(outOfControl)
			return outOfControl;
		else
			return outOfControl;
	}
	
}