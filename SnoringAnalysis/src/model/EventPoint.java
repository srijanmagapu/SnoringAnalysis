package model;

public class EventPoint implements Comparable<EventPoint>
{
	private double[] coordinates;
	private int cluster;
	private TimeStamp timeStamp;

	public EventPoint(double[] coordinates, int cluster)
	{
		this.coordinates = coordinates;
		this.cluster = cluster;
		this.timeStamp = new TimeStamp(0, 0);
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

	
	public TimeStamp getTimeStamp()
	{
		return timeStamp;
	}

	public void setTimeStamp(TimeStamp timeStamp)
	{
		this.timeStamp = timeStamp;
	}

	@Override
	public int compareTo(EventPoint point)
	{
		return new Double(timeStamp.getStart()).compareTo(new Double(point.getTimeStamp().getStart())); 
	}
}
