package businessLayer;

public class FeatureWorker implements Runnable
{
	private FeatureQueue queue;
	private IConsumer consumer;
	
	private boolean stopped;
	
	public FeatureWorker(FeatureQueue featureQueue)
	{
		this.queue = (FeatureQueue) featureQueue;
	}
	
	public void setIConsumer(IConsumer consumer)
	{
		this.consumer = consumer;
	}
	
	public void stopWorker()
	{
		stopped = true;
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
					
				consumer.consume(energy, mfcc);
			}
			catch (InterruptedException e)
			{
				System.err.println("Error occured while taking buffer from queue");
				e.printStackTrace();
			}
		}
		
		((Runnable)consumer).run();
	}
	
	
}
