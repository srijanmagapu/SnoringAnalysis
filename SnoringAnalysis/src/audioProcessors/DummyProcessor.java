package audioProcessors;

import model.ProgressData;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import businessLayer.FeatureWorker;

public class DummyProcessor implements AudioProcessor
{

	private boolean interruptProcessing;
	private ProgressData progress;
	
	public DummyProcessor()
	{
		progress = new ProgressData();
	}
	
	public void setInterruptProcessing(boolean b)
	{
		interruptProcessing = b;
	}
	
	public ProgressData getProgressData()
	{
		return progress;
	}
	double p;
	@Override
	public boolean process(AudioEvent audioEvent)
	{
		p = audioEvent.getProgress();
		progress.setProgress((int)Math.ceil(100 * audioEvent.getProgress()));
		// return false to interrupt processing
		return !interruptProcessing;
	}

	@Override
	public void processingFinished()
	{
		FeatureWorker.stopAllWorkers();
	}

}
