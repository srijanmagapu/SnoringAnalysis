package businessLayer;

import math.DwVector;
import math.PCA;

import org.battelle.clodhopper.fuzzycmeans.FuzzyCMeansClusterer;
import org.battelle.clodhopper.fuzzycmeans.FuzzyCMeansParams;
import org.battelle.clodhopper.tuple.ArrayTupleList;
import org.battelle.clodhopper.tuple.TupleList;

import utils.DBUtils;

public class DBCreator extends FeatureConsumer
{
	
	public DBCreator(int dim, int numberOfClusters)
	{
		super(dim, numberOfClusters);
	}
	
	@Override
	protected void finishConstuction(double[] vector)
	{
		data.add(vector);
	}
	
	@Override
	public void run()
	{
		DBUtils.storeRawFeatures(data);
		
		// create array of vectors from raw data
		DwVector[] vectors = new DwVector[data.size()];
		for(int i=0; i<data.size(); i++)
			vectors[i] = new DwVector(data.get(i));
		
		// compute transform matrix
		PCA pca = new PCA(vectors);
		pca.compute();
		pca.setTransformDimension(requiredDimension);
		
		pcaMatrix =  pca.getTrasformMatrix();
		
		//store PCA matrix for vectors projection
		DBUtils.storePCAMatrix(pcaMatrix);
		
		// reduced dimensions
		DwVector[] reduced = pca.transformData(vectors, false);
		
		// generate raw data from reduced vectors
		double[][] reducedRaw = new double[reduced.length][requiredDimension];
		for( int i=0 ; i<reduced.length ; i++ )
		{
			reducedRaw[i] = reduced[i].getVector();
		}
		
		// fcm classes
		FuzzyCMeansParams fcmParams = new FuzzyCMeansParams();
		fcmParams.setClusterCount(numberOfClusters);
		
		// prep data for fcm
		TupleList fcmData = new ArrayTupleList(requiredDimension, reduced.length);
		for( int i=0 ; i<reducedRaw.length ; i++ )
		{
			fcmData.setTuple(i, reducedRaw[i]);
		}
		FuzzyCMeansClusterer fcmClusterer = new FuzzyCMeansClusterer(fcmData, fcmParams);
		fcmClusterer.run();
		
		// prep point membership for storing
		double[][] membership = new double[reducedRaw.length][numberOfClusters];
		for( int i=0 ; i<reducedRaw.length ; i++ )
		{
			membership[i] = fcmClusterer.getDegreesOfMembership(i);
		}
		
		// store membership and points
		DBUtils.storePointsWithMembership(reducedRaw, membership);
		
		double[][] centers = new double[numberOfClusters][requiredDimension];
		
		for( int i=0 ; i<numberOfClusters ; i++ )
		{
			centers[i] = fcmClusterer.getClusterCenter(i);
		}
		
		DBUtils.storeClusterCenters(centers);
	}
	
}
