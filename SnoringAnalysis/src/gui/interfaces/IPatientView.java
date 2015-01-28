package gui.interfaces;

import model.EventPoint;

public interface IPatientView extends ISignalGraph
{
	public void setEventPoints(EventPoint[] list);
}
