package businessLayer;

import java.util.ArrayList;
import java.util.List;

public abstract class FeatureConsumer implements IFeatureConsumer
{
	protected final int requiredDimension;
	protected final int numberOfClusters;
	protected List<double[]> data = new ArrayList<>();
	protected double[][] pcaMatrix;
	
	public FeatureConsumer(int dim, int numberOfClusters)
	{
		this.requiredDimension = dim;
		this.numberOfClusters = numberOfClusters;
	}
	
	@Override
	public void consume(float[] arg1, float[] arg2)
	{
		constuctFeatureVector(arg1, arg2);
	}
	
	private void constuctFeatureVector(float[] energy, float[] mfcc)
	{
		//merge vectors
		double[] mergedVector = new double[energy.length + mfcc.length];
		for( int i=0 ; i<energy.length ; i++ )
		{
			mergedVector[i] = energy[i];
		}

		for( int i=energy.length ; i<energy.length+mfcc.length ; i++ )
		{
			mergedVector[i] = energy[i];
		}
		
		finishConstuction(mergedVector);
	}
	
	protected abstract void finishConstuction(double[] vector);
	
	@Override
	public abstract void run();
	
}
