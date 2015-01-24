package model;

import org.battelle.clodhopper.distance.DistanceMetric;
import org.battelle.clodhopper.fuzzycmeans.FuzzyCMeansParams;

public class FCMGraphData
{
	private DistanceMetric distMetric;
	private EventPointList centers;
	private EventPointList points;
	
	
	private static volatile FCMGraphData instance;
	private FCMGraphData()
	{
		this.distMetric = new FuzzyCMeansParams().getDistanceMetric();
		this.centers = new EventPointList();
		this.points = new EventPointList();
	}
	
	public static FCMGraphData getInstance()
	{
		if(instance == null)
			instance = new FCMGraphData();
		
		return instance;
	}
	
	
	public void clearAll()
	{
		clearCenters();
		clearPoints();
	}
	
	public void clearCenters()
	{
		this.centers = new EventPointList();
	}
	
	public void clearPoints()
	{
		this.points = new EventPointList();
	}
	
	
	public void addClusterCenters(EventPointList centers)
	{
		this.centers.add(centers);
	}
	
	public void addClusterCenter(EventPoint center)
	{
		this.centers.add(center);
	}
	
	public void setClusterCenters(double[][] centers)
	{
		clearCenters();
		
		for(int i = 0; i < centers.length; i++)
			addClusterCenter(new EventPoint(centers[i], i));
	}

	
	public void addPoints(EventPointList points)
	{
		this.points.add(points);
	}
	
	public void addPoint(EventPoint point)
	{
		this.points.add(point);
	}
	
	public void addPoints(EventPoint[] points)
	{
		this.points.add(points);
	}
	
	public void addPoints(double[][] points)
	{
		for(int i = 0; i < points.length; i++)
			addPoint(new EventPoint(points[i], getClosestCluster(points[i])));
	}
	
	
	private int getClosestCluster(double[] point)
	{
		double minDist = Double.MAX_VALUE;
		int closestCluster = 0;
		
		for(int i = 0; i < centers.size(); i++ )
		{
			double dist = distMetric.distance(point, centers.get(i).getCoordinates());
			if(dist < minDist)
			{
				minDist = dist;
				closestCluster = i;
			}
		}
			
		return closestCluster;
	}

	public EventPoint[] getCenters()
	{
		return centers.getPointsArray();
	}
	
	public EventPoint[] getPoints()
	{
		return points.getPointsArray();
	}
	
	public EventPoint[] getPointsFromCluster(int cluster)
	{
		return points.getPointsFromCluster(cluster);
	}
}

