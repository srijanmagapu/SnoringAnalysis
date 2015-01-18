package businessLayer;

import java.util.ArrayList;

import model.AudioEventDescriptor;
import model.AudioFeature;

public class FeatureWorker implements Runnable
{
	private static ArrayList<FeatureWorker> workerList = new ArrayList<>();
	
	private FeatureQueue queue;
	private IFeatureConsumer consumer;
	
	private boolean stopped;
	
	double eventStart;
	double eventEnd;
	
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
		queue.addEnergyBuffer(new AudioFeature());
		queue.addMFCCBuffer(new AudioFeature());
	}

	@Override
	public void run()
	{
		AudioFeature energy;
		AudioFeature mfcc;
		
		while(!stopped)
		{
			try
			{
				energy = queue.takeEnergyBuffer();
				mfcc = queue.takeMFCCBuffer();
				
				if(energy.getData()==null || mfcc.getData()==null)
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
