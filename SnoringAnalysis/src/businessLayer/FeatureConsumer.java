package businessLayer;

import java.util.ArrayList;
import java.util.List;

public abstract class FeatureConsumer implements IFeatureConsumer
{
	protected final int requiredDimension;
	protected List<float[]> data = new ArrayList<>();
	protected double[][] pcaMatrix;
	
	public FeatureConsumer(int dim)
	{
		this.requiredDimension = dim;
	}
	
	@Override
	public void consume(float[] arg1, float[] arg2)
	{
		constuctFeatureVector(arg1, arg2);
	}
	
	private void constuctFeatureVector(float[] energy, float[] mfcc)
	{
		//merge vectors
		float[] mergedVector = new float[energy.length + mfcc.length];
		System.arraycopy(energy, 0, mergedVector, 0, energy.length);
		System.arraycopy(mfcc, 0, mergedVector, energy.length, mfcc.length);
		
		finishConstuction(mergedVector);
	}
	
	protected abstract void finishConstuction(float[] vector);
	
	@Override
	public abstract void run();
	
}
