package model;

import java.util.Observable;

public class SignalBuffer extends Observable
{
	private float[] buffer;
	
	public void setByffer(float[] buffer)
	{
		this.buffer = buffer.clone();
		setChanged();
		notifyObservers();
	}
	
	public float[] getBuffer()
	{
		return this.buffer.clone();
	}
}
