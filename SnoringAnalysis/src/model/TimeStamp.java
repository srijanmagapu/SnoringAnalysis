package model;

public class TimeStamp
{
	private double start;
	private double end;
	
	public TimeStamp(double start, double end)
	{
		this.start = start;
		this.end = end;
	}
	
	public double getStart()
	{
		return start;
	}
	public void setStart(double start)
	{
		this.start = start;
	}
	public double getEnd()
	{
		return end;
	}
	public void setEnd(double end)
	{
		this.end = end;
	}
}
