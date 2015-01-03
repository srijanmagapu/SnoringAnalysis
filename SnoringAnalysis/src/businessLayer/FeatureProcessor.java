package businessLayer;

import org.battelle.clodhopper.distance.DistanceMetric;
import org.battelle.clodhopper.fuzzycmeans.FuzzyCMeansParams;

import math.DwVector;
import math.PCA;
import model.EventPoint;
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
		
		// load FCA cluster centers
		this.clusterCenters = DBUtils.loadClusterCenters();
		
		this.distMetric = new FuzzyCMeansParams().getDistanceMetric();
	}
	
	@Override
	protected void finishConstuction(double[] vector)
	{
		double[] reducedVector = pca.transformVector(new DwVector(vector)).getVector();
		int closestCluster = getClosestCluster(reducedVector);
		
		EventPoint ep = new EventPoint(reducedVector, closestCluster);
		
	}
	
	private int getClosestCluster(double[] point)
	{
		double minDist = 0;
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
