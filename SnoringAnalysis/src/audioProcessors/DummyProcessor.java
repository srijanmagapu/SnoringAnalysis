package audioProcessors;

import model.ProgressData;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;

public class DummyProcessor implements AudioProcessor
{

	private boolean interruptProcessing;
	private ProgressData progress;
	
	public void setInterruptProcessing(boolean b)
	{
		interruptProcessing = b;
	}
	
	public ProgressData getProgressData()
	{
		return progress;
	}
	
	public DummyProcessor()
	{
		progress = new ProgressData();
	}
	
	@Override
	public boolean process(AudioEvent audioEvent)
	{
		progress.setProgress((int)Math.ceil(100 * audioEvent.getProgress()));
		// return false to interrupt processing
		return !interruptProcessing;
	}

	@Override
	public void processingFinished()
	{
	}

}
