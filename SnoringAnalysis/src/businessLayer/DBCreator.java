package businessLayer;

import java.util.Arrays;
import java.util.HashMap;

import math.DwVector;
import math.PCA;
import model.FCMGraphData;
import model.TimeStamp;

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
	protected void finishConstuction(double[] vector, TimeStamp timeStamp)
	{
		data.add(vector);
		timeStamps.add(timeStamp);
	}

	@Override
	public void run()
	{
		try
		{
			if (data.size() == 0)
				throw new Exception("DBCreator: No data to preceed.");

			System.out.println("BDCreator: number of vectros: " + data.size());
			
			DBUtils.storeRawFeatures(data);

			// create array of vectors from raw data
			DwVector[] vectors = new DwVector[data.size()];
			for (int i = 0; i < data.size(); i++)
				vectors[i] = new DwVector(data.get(i));

			// compute transform matrix
			PCA pca = new PCA(vectors);
			pca.compute();
			pca.setTransformDimension(requiredDimension);

			pcaMatrix = pca.getTrasformMatrix();

			// store PCA matrix for vectors projection
			DBUtils.storePCAMatrix(pcaMatrix);

			// reduced dimensions
			DwVector[] reduced = pca.transformData(vectors, false);

			// generate raw data from reduced vectors
			double[][] reducedRaw = new double[reduced.length][requiredDimension];
			for (int i = 0; i < reduced.length; i++)
			{
				reducedRaw[i] = reduced[i].getVector();
			}

			// fcm classes
			FuzzyCMeansParams fcmParams = new FuzzyCMeansParams();
			fcmParams.setClusterCount(numberOfClusters);

			// prep data for fcm
			TupleList fcmData = new ArrayTupleList(requiredDimension, reduced.length);
			for (int i = 0; i < reducedRaw.length; i++)
			{
				fcmData.setTuple(i, reducedRaw[i]);
			}
			FuzzyCMeansClusterer fcmClusterer = new FuzzyCMeansClusterer(fcmData, fcmParams);
			fcmClusterer.run();

			double[][] centers = new double[numberOfClusters][requiredDimension];

			for (int i = 0; i < numberOfClusters; i++)
			{
				centers[i] = fcmClusterer.getClusterCenter(i);
			}
			
			centers = sortCenters(centers);
			
			// store cluster centers
			DBUtils.storeClusterCenters(centers);
			
			// set centers for fcm graph
			FCMGraphData.getInstance().clearAll();
			FCMGraphData.getInstance().setClusterCenters(centers);
			
			
			// prep point membership for storing
			double[][] membership = new double[reducedRaw.length][numberOfClusters];
			for (int i = 0; i < reducedRaw.length; i++)
			{
				membership[i] = fcmClusterer.getDegreesOfMembership(i);
			}
			
			// store membership and points
			DBUtils.storePointsWithMembership(reducedRaw, membership);

			// set points for fcm graph
			FCMGraphData.getInstance().addPoints(reducedRaw, timeStamps);
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			// e.printStackTrace();
		}
	}

	private double[][] sortCenters(double[][] centers)
	{
		double zeroPoint[] = new double[]{0,0,0};
		double[] dists = new double[centers.length];
		
		for(int i = 0; i < centers.length; i++)
		{
			dists[i] = distMetric.distance(zeroPoint, centers[i]);
		}
		
		HashMap<Double, double[]> distToPoint = new HashMap<>();
		for(int i = 0; i < centers.length; i++)
		{
			distToPoint.put(new Double(dists[i]), centers[i]);
		}
		
		Arrays.sort(dists);

		double sorted[][] = new double[centers.length][];
		try
		{
			for (int i = 0; i < sorted.length; i++)
			{
				sorted[i] = distToPoint.get(new Double(dists[i]));
			}

		}
		catch (Exception ex)
		{
			System.out.println();
		}
		
		return sorted;
	}

}
