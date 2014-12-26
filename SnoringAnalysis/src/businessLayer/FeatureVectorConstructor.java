package businessLayer;

import java.util.ArrayList;
import java.util.List;

import math.DwVector;
import math.PCA;
//import math.PCA;
import math.PCA2;

public class FeatureVectorConstructor implements IConsumer, Runnable
{
	private final int requiredDimension;
	private List<float[]> data = new ArrayList<>();
	private double[][] redicedData;
	
	public FeatureVectorConstructor(int dim)
	{
		this.requiredDimension = dim;
	}
	
	private void constuctFeatureVector(float[] energy, float[] mfcc)
	{
		//merge vectors
		float[] mergedVector = new float[energy.length + mfcc.length];
		System.arraycopy(energy, 0, mergedVector, 0, energy.length);
		System.arraycopy(mfcc, 0, mergedVector, energy.length, mfcc.length);
		
		data.add(mergedVector);
	}
	
	public void run()
	{
		DwVector[] vec = new DwVector[data.size()];
		for(int i=0; i<data.size(); i++)
			vec[i] = new DwVector(data.get(i));
		//reduce dims
		PCA pca = new PCA(vec);
		pca.compute();
		pca.setTransformDimension(requiredDimension);
		redicedData =  pca.getArray();
	}
	


	@Override
	public void consume(float[] arg1, float[] arg2)
	{
		constuctFeatureVector(arg1, (arg2));
	}

}
