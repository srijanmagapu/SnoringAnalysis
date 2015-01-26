package audioProcessors;

import model.ProgressData;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;

/**
 * This processor used to perform service task that doesn't have
 * logical connection to any other processor. For example it used
 * interrupt the input dispatching by returning false value from
 * process method.
 * @author Gao
 *
 */
public class DummyProcessor implements AudioProcessor
{

	/**
	 * Indicates if process shall be interrupt
	 */
	private boolean interruptProcessing;
	/**
	 * The container to store information about processing progress
	 */
	private ProgressData progress;
	
	/**
	 * If true passed to this method, process method will return false
	 * that will stop the dispatcher and interrupt processing of current file
	 * @param b
	 */
	public void setInterruptProcessing(boolean b)
	{
		interruptProcessing = b;
	}
	
	/**
	 * Returns ProgressData container
	 * @return ProgressData container
	 */
	public ProgressData getProgressData()
	{
		return progress;
	}
	
	/**
	 * Create new DummyProcessor
	 */
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
