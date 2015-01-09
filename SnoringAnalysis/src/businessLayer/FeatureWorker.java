package businessLayer;

import java.util.ArrayList;

public class FeatureWorker implements Runnable
{
	private static ArrayList<FeatureWorker> workerList = new ArrayList<>();
	
	private FeatureQueue queue;
	private IFeatureConsumer consumer;
	
	private boolean stopped;
	
	public FeatureWorker(FeatureQueue featureQueue)
	{
		this.queue = (FeatureQueue) featureQueue;
		workerList.add(this);
	}
	
	public void setIConsumer(IFeatureConsumer consumer)
	{
		this.consumer = consumer;
	}
	
	public static void stopAllWorkers()
	{
		for(FeatureWorker worker : workerList)
			worker.stopWorker();
	}
	
	public void stopWorker()
	{
		stopped = true;
		queue.addEnergyBuffer(new float[0]);
		queue.addMFCCBuffer(new float[0]);
	}

	@Override
	public void run()
	{
		float[] energy;
		float[] mfcc;
		
		while(!stopped)
		{
			try
			{
				energy = queue.takeEnergyBuffer();
				mfcc = queue.takeMFCCBuffer();

				if(energy.length==0 || mfcc.length==0)
					break;
					
				if(consumer != null)
					consumer.consume(energy, mfcc);
				
				
			}
			catch (InterruptedException e)
			{
				System.err.println("Error occured while taking buffer from queue");
				e.printStackTrace();
			}
		}
		
		if(consumer != null)
			consumer.run();
	}
	
	
}