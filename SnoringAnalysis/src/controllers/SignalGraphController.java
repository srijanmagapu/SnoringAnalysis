package controllers;

import java.util.Observable;
import java.util.Observer;

import javax.swing.SwingUtilities;

import model.SignalBuffer;
import gui.interfaces.ISignalGraph;

public class SignalGraphController implements Observer
{
	protected ISignalGraph view;
	protected SignalBuffer buffer;
	
	public SignalGraphController(ISignalGraph view, SignalBuffer buffer)
	{
		this.view = view;
		this.buffer = buffer;
		
		this.buffer.addObserver(this);
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
	
	protected double[] createXData(float[] data)
	{
		double[] xData = new double[data.length];
		
		for(int i = 0; i < data.length; i++)
			xData[i] = i + 1;

		return xData;
	}
	
	
	protected double[] createYData(float[] data)
	{
		double[] yData = new double[data.length];
		for(int i = 0; i < data.length; i++)
			yData[i] = data[i];
		
		return yData;
	}
}
