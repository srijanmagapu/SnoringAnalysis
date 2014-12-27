package businessLayer;

import java.util.ArrayList;
import java.util.List;

import math.DwVector;
import math.PCA;
import utils.DBUtils;

public class DBCreator extends FeatureConsumer
{	
	public DBCreator(int dim)
	{
		super(dim);
	}
	
	@Override
	protected void finishConstuction(float[] vector)
	{
		data.add(vector);
	}
	
	@Override
	public void run()
	{
		DwVector[] vectors = new DwVector[data.size()];
		for(int i=0; i<data.size(); i++)
			vectors[i] = new DwVector(data.get(i));
		//reduce dims
		PCA pca = new PCA(vectors);
		pca.compute();
		pca.setTransformDimension(requiredDimension);
		pcaMatrix =  pca.getArray();
		
		//store PCA matrix for vectors projection
		DBUtils.storePCAMatrix(pcaMatrix);
		
		List<double[]> reducedVectors = new ArrayList<double[]>();
		
		for(DwVector vector : vectors)
			reducedVectors.add(pca.transformVector(vector).getVector());
		
	}
	
}
