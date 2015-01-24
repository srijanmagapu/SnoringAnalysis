package businessLayer;

import org.battelle.clodhopper.distance.DistanceMetric;
import org.battelle.clodhopper.fuzzycmeans.FuzzyCMeansParams;

import math.DwVector;
import math.PCA;
import model.EventPoint;
import model.FCMGraphData;
import utils.Constants;
import utils.DBUtils;

public class FeatureProcessor extends FeatureConsumer
{
	private PCA pca;
	private double[][] clusterCenters;
	private DistanceMetric distMetric;
	
	
	public FeatureProcessor(int dim, int numberOfClusters)
	{
		super(dim, numberOfClusters);
		
		//set PCA transform matrix
		double[][] pcaMatrix = DBUtils.loadPCAMatrix();
		this.pca = new PCA();
		this.pca.setTransformMatrix(pcaMatrix);
		//this.pca.setTransformDimension(Constants.numOfDimensions);
		
		// load FCA cluster centers
		this.clusterCenters = DBUtils.loadClusterCenters();
		
		FCMGraphData.getInstance().clearAll();
		FCMGraphData.getInstance().setClusterCenters(clusterCenters);
		
		this.distMetric = new FuzzyCMeansParams().getDistanceMetric();
	}
	
	@Override
	protected void finishConstuction(double[] vector)
	{
		double[] reducedVector = pca.transformVector(new DwVector(vector)).getVector();
		int closestCluster = getClosestCluster(reducedVector);
		
		// add new point to fcmData
		FCMGraphData.getInstance().addPoint(new EventPoint(reducedVector, closestCluster));
	}
	
	private int getClosestCluster(double[] point)
	{
		double minDist = Double.MAX_VALUE;
		int closestCluster = 0;
		
		for(int i = 0; i < clusterCenters.length; i++ )
		{
			double dist = distMetric.distance(point, clusterCenters[i]);
			if(dist < minDist)
			{
				minDist = dist;
				closestCluster = i;
			}
		}
			
		return closestCluster;
	}
	
	@Override
	public void run()
	{
		
	}
	


}
