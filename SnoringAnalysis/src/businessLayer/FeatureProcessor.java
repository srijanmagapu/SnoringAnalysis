package businessLayer;

import math.DwVector;
import math.PCA;
import model.EventPoint;
import model.FCMGraphData;
import model.TimeStamp;
import utils.DBUtils;

public class FeatureProcessor extends FeatureConsumer
{
	private PCA pca;
	private double[][] clusterCenters;
	private int vectorsCounter;
	
	public FeatureProcessor(int dim, int numberOfClusters)
	{
		super(dim, numberOfClusters);
		
		//set PCA transform matrix
		double[][] pcaMatrix = DBUtils.loadPCAMatrix();
		this.pca = new PCA();
		this.pca.setTransformMatrix(pcaMatrix);
		
		// load FCA cluster centers
		this.clusterCenters = DBUtils.loadClusterCenters();
		
		FCMGraphData.getInstance().clearAll();
		FCMGraphData.getInstance().setClusterCenters(clusterCenters);
		
	}
	
	@Override
	protected void finishConstuction(double[] vector, TimeStamp timeStamp)
	{
		double[] reducedVector = pca.transformVector(new DwVector(vector)).getVector();
		int closestCluster = getClosestCluster(reducedVector);
		
		// add new point to fcmData
		EventPoint point = new EventPoint(reducedVector, closestCluster);
		point.setTimeStamp(timeStamp);
		FCMGraphData.getInstance().addPoint(point);
		vectorsCounter++;
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
		System.out.println("FeatureProcessor: number of vectors " + vectorsCounter);
	}
	


}
