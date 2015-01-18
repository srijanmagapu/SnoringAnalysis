package model;

public class AudioFeature
{
	private double startTime;
	private double endTime;
	private float[] data;
	
	public AudioFeature()
	{
		startTime = -1;
		endTime = -1;
		data = null;
	}
	
	public float[] getData()
	{
		return data;
	}
	public void setData(float[] data)
	{
		this.data = data;
	}
	public double getEndTime()
	{
		return endTime;
	}
	public void setEndTime(double endTime)
	{
		this.endTime = endTime;
	}
	public double getStartTime()
	{
		return startTime;
	}
	public void setStartTime(double startTime)
	{
		this.startTime = startTime;
	}
}
