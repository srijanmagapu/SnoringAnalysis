package model;

public class EventPoint
{
	private double[] coordinates;
	private int cluster;
	private double startTime;
	private double endTime;

	public EventPoint(double[] coordinates, int cluster)
	{
		this.coordinates = coordinates;
		this.cluster = cluster;
	}

	public void setCoordinates(double[] coordinates)
	{
		this.coordinates = coordinates;
	}

	public double[] getCoordinates()
	{
		return this.coordinates;
	}

	public void setCluster(int cluster)
	{
		this.cluster = cluster;
	}
	
	public int getCluster()
	{
		return this.cluster;
	}

	public double getStartTime()
	{
		return startTime;
	}

	public void setStartTime(double startTime)
	{
		this.startTime = startTime;
	}

	public double getEndTime()
	{
		return endTime;
	}

	public void setEndTime(double endTime)
	{
		this.endTime = endTime;
	}
}
