package businessLayer;

public class FeatureWorker implements Runnable
{
	private FeatureQueue queue;
	private IFeatureConsumer consumer;
	
	private boolean stopped;
	
	public FeatureWorker(FeatureQueue featureQueue)
	{
		this.queue = (FeatureQueue) featureQueue;
	}
	
	public void setIConsumer(IFeatureConsumer consumer)
	{
		this.consumer = consumer;
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
