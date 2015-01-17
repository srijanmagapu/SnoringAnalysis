package controllers;

import java.util.Observable;
import java.util.Observer;

import javax.swing.SwingUtilities;

import gui.interfaces.IAreaSignalGraph;
import model.SignalBuffer;

public class EnergyGraphController implements Observer
{
	private IAreaSignalGraph view;
	private SignalBuffer buffer;
	
	public EnergyGraphController(IAreaSignalGraph view, SignalBuffer buffer)
	{
		this.view = view;
		this.buffer = buffer;
		
		this.buffer.addObserver(this);
		
		this.view.setBottomLine(0);
	}

	
	@Override
	public void update(Observable sender, Object params)
	{
		SwingUtilities.invokeLater(new Runnable(){
            public void run()
			{
            	float[] buffer = ((SignalBuffer)sender).getBuffer();
            	view.setData(createXData(buffer), createYData(buffer));
			}
		});
	}
	
	private double[] createXData(float[] data)
	{
		double[] xData = new double[data.length];
		
		for(int i = 0; i < data.length; i++)
			xData[i] = i + 1;
		
		return xData;
	}
	
	
	private double[] createYData(float[] data)
	{
		double[] yData = new double[data.length];
		for(int i = 0; i < data.length; i++)
			yData[i] = data[i];
		
		return yData;
	}

}
