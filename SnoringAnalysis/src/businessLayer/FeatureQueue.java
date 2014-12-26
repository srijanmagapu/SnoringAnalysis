package businessLayer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class FeatureQueue
{
	private BlockingQueue<float[]> energyBuffer = new LinkedBlockingQueue<>();
	private BlockingQueue<float[]> mfccBuffer = new LinkedBlockingQueue<>();
	
	private static FeatureQueue instance;
	private FeatureQueue(){}
	
	public static FeatureQueue getInstance()
	{
		if(instance == null)
			instance = new FeatureQueue();
		return instance;
	}
	
	public boolean addEnergyBuffer(float[] energy)
	{
		return energyBuffer.add(energy);
	}
	
	public boolean addMFCCBuffer(float[] mfcc)
	{
		return mfccBuffer.add(mfcc);
	}
	
	public float[] takeEnergyBuffer() throws InterruptedException
	{
		return energyBuffer.take();
	}
	
	public float[] takeMFCCBuffer() throws InterruptedException
	{
		return mfccBuffer.take();
	}
}
