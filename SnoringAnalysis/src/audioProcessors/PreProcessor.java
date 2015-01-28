package audioProcessors;

import java.util.ArrayList;
import java.util.List;
import org.battelle.clodhopper.fuzzycmeans.FuzzyCMeansClusterer;
import org.battelle.clodhopper.fuzzycmeans.FuzzyCMeansParams;
import org.battelle.clodhopper.tuple.ArrayTupleList;
import org.battelle.clodhopper.tuple.TupleList;

import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;

public class PreProcessor implements AudioProcessor
{
	private double arbitraryScaling = 4;
	private double calculatedHeight = 0.1;
	private List<double[]> amplitudes = new ArrayList<double[]>();
	
	public double getCaculatedheight()
	{
		return calculatedHeight;
	}

	@Override
	public boolean process(AudioEvent audioEvent)
	{
		float[] eventFloatBuffer = audioEvent.getFloatBuffer();
		double[] transformBuffer = new double[audioEvent.getBufferSize()];
		
		for( int i=0 ; i<audioEvent.getBufferSize() ; i++ )
		{
			transformBuffer[i] = (double) Math.abs(eventFloatBuffer[i]);
		}
		
		amplitudes.add(transformBuffer);
		
		return true;
	}

	@Override
	public void processingFinished()
	{
		double[] buff = getSomething();
		
		TupleList tuples = new ArrayTupleList(1, buff.length, buff);
		FuzzyCMeansParams params = new FuzzyCMeansParams();
		params.setClusterCount(3);
		FuzzyCMeansClusterer clusterer = new FuzzyCMeansClusterer(tuples, params);
		clusterer.run();
		
		
		double c1 = clusterer.getClusterCenter(0)[0];
		double c2 = clusterer.getClusterCenter(1)[0];
		double c3 = clusterer.getClusterCenter(2)[0];
		
		double highAmp = Math.max(Math.max(c1, c2), c3);
		double lowAmp  = Math.min(Math.min(c1, c2), c3);
		
		calculatedHeight = (highAmp + lowAmp) / arbitraryScaling;
		System.out.println(calculatedHeight);
	}
	
	private double[] mergeBuffers()
	{
		int count = 0;
		for (double[] array: amplitudes)
		{
		  count += array.length;
		}
		
		double[] mergedArray = new double[count];
		int start = 0;
		for( double[] array: amplitudes )
		{
		  System.arraycopy(array, 0, mergedArray, start, array.length);
		  start += array.length;
		}
		
		return mergedArray;
	}
	
	
	private double[] getSomething()
	{
		double[] mergedArray = new double[amplitudes.size()];
		for( int i=0 ; i<amplitudes.size() ; i++ )
		{
			double max = 0;
			
			for( double value : amplitudes.get(i) )
				max = value>max ? value : max;
			
			mergedArray[i] = max;
		}
		
		return mergedArray;
	}
}
