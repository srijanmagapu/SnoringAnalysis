package audioProcessors;

import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import businessLayer.FeatureWorker;

public class DummyProcessor implements AudioProcessor
{

	private boolean interruptProcessing;
	
	public void setInterruptProcessing(boolean b)
	{
		interruptProcessing = b;
	}
	
	@Override
	public boolean process(AudioEvent audioEvent)
	{
		// return false to interrupt processing
		return !interruptProcessing;
	}

	@Override
	public void processingFinished()
	{
		FeatureWorker.stopAllWorkers();
	}

}
