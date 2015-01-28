package model;

import java.util.Observable;

public class SignalBuffer extends Observable
{
	private final float DEFAULT_SAMPLE_RATE = 10240;
	private float sampleRate = DEFAULT_SAMPLE_RATE;
	private float[] buffer;
	
	public void setSampleRate(float sampleRate)
	{
		this.sampleRate = sampleRate;
	}
	
	public float getSampleRate()
	{
		return this.sampleRate;
	}
	
	public void setBuffer(float[] buffer)
	{
		this.buffer = buffer.clone();
		setChanged();
		notifyObservers(sampleRate);
	}
	
	public float[] getBuffer()
	{
		return this.buffer.clone();
	}
}
