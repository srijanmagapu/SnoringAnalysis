package businessLayer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import model.AudioFeature;

public class FeatureQueue
{
	private BlockingQueue<AudioFeature> energyBuffer = new LinkedBlockingQueue<AudioFeature>();
	private BlockingQueue<AudioFeature> mfccBuffer = new LinkedBlockingQueue<AudioFeature>();
	
	private static FeatureQueue instance;
	private FeatureQueue(){}
	
	public static FeatureQueue getInstance()
	{
		if(instance == null)
			instance = new FeatureQueue();
		return instance;
	}
	
	public boolean addEnergyBuffer(AudioFeature e)
	{
		return energyBuffer.add(e);
	}
	
	public boolean addMFCCBuffer(AudioFeature e)
	{
		return mfccBuffer.add(e);
	}
	
	public AudioFeature takeEnergyBuffer() throws InterruptedException
	{
		return energyBuffer.take();
	}
	
	public AudioFeature takeMFCCBuffer() throws InterruptedException
	{
		return mfccBuffer.take();
	}
}
