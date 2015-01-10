package model;

import java.util.Observable;

public class ProgressData extends Observable
{
	private int progress;
	
	public void setProgress(int percent)
	{
		int oldProgress = this.progress;
		this.progress = percent;
		if(this.progress != oldProgress)
		{
			setChanged();
			notifyObservers();
		}
	}
	
	public int getProgress()
	{
		return progress;
	}
}
