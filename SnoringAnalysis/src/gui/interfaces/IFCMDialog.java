package gui.interfaces;

import model.EventPoint;

public interface IFCMDialog
{
	public enum Category
	{
		Snoring,
		Breathing,
		Noise
	}
	
	public void setCenters(EventPoint center1, EventPoint center2, EventPoint center3);
	public void setCenter(EventPoint center);
	
	public void setGroups(EventPoint[] group1, EventPoint[] group2, EventPoint[] group3);
	public void setGroup(EventPoint[] group);
	
	public void refreshGraph();
}
