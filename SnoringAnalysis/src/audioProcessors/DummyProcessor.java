package audioProcessors;

import java.util.ArrayList;

import model.ProgressData;
import model.SignalBuffer;
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
	
	private SignalBuffer signalTimeDomain;
	private ArrayList<float[]> tdList;
	private int overlap;
	
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
		signalTimeDomain = new SignalBuffer();
		tdList = new ArrayList<>();
	}
	
	@Override
	public boolean process(AudioEvent audioEvent)
	{
		overlap = audioEvent.getOverlap();
		tdList.add(audioEvent.getFloatBuffer().clone());
		
		progress.setProgress((int)Math.ceil(100 * audioEvent.getProgress()));
		// return false to interrupt processing
		return !interruptProcessing;
	}

	@Override
	public void processingFinished()
	{
		createTDBuffer();
	}
	
	private void createTDBuffer()
	{
		int arraySize = tdList.get(0).length;
		int numOfSamples = (arraySize - overlap) * (tdList.size())  + overlap;
		
		float[] cumulativeArray = new float[numOfSamples];
		int cumulativeIndex = 0;
		
		//copy the array part that not overlapped by next array
		for(float[] arr : tdList)
		{
			System.arraycopy(arr, 0, cumulativeArray, cumulativeIndex, arr.length - overlap);
			cumulativeIndex += arr.length - overlap;
		}
		
		//copy the last part of last array
		float[] lastArray = tdList.get(tdList.size()-1);
		System.arraycopy(lastArray, lastArray.length - overlap, cumulativeArray, cumulativeIndex, overlap);
		
		signalTimeDomain.setBuffer(cumulativeArray);
		
		System.out.println("Overall TD samples: " + cumulativeArray.length);
	}
	
	public SignalBuffer getSignalBuffer()
	{
		return signalTimeDomain;
	}

}
