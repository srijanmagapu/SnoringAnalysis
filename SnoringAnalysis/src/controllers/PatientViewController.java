package controllers;

import java.util.Observable;

import javax.swing.SwingUtilities;

import gui.interfaces.IPatientView;
import model.FCMGraphData;
import model.SignalBuffer;

public class PatientViewController extends SignalGraphController
{

	public PatientViewController(IPatientView view, SignalBuffer buffer)
	{
		super(view, buffer);
	}
	
	
	
	@Override
	public void update(Observable sender, Object params)
	{
		double[] x;
		double[] y;
		x = createXData(((SignalBuffer)sender).getBuffer());
		y = createYData(((SignalBuffer)sender).getBuffer());
		
		SwingUtilities.invokeLater(new Runnable(){
            public void run()
			{
            	((IPatientView)view).setEventPoints(FCMGraphData.getInstance().getPoints());
            	view.setData(x, y);
			}
		});
	}
	
	@Override
	protected double[] createXData(float[] data)
	{
		double[] xData = new double[data.length];
		for(int i = 0; i < data.length; i++)
			xData[i] = (i*1000) / 44100;
		
		return xData;
	}
}
