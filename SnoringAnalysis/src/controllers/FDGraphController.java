package controllers;

import java.util.Observable;
import java.util.Observer;

import javax.swing.SwingUtilities;

import be.tarsos.dsp.util.fft.FFT;
import gui.interfaces.IAreaSignalGraph;
import model.SignalBuffer;

public class FDGraphController implements Observer
{
	private IAreaSignalGraph view;
	private SignalBuffer buffer;
	private FFT fft;
	
	public void setFFT(FFT fft)
	{
		this.fft = fft;
	}
	
	public FDGraphController(IAreaSignalGraph view, SignalBuffer buffer)
	{
		this.view = view;
		this.buffer = buffer;
		
		this.buffer.addObserver(this);
		
		this.view.setBottomLine(-70);
	}

	@Override
	public void update(Observable sender, Object params)
	{
		SwingUtilities.invokeLater(new Runnable(){
            public void run()
			{
            	float[] buffer = ((SignalBuffer)sender).getBuffer();
            	view.setData(createXData(buffer, (float)params), createYData(buffer));
			}
		});
	}
	
	private double[] createXData(float[] data, float sampleRate)
	{
		double[] xData = new double[data.length];
		for(int i=0 ; i < data.length; i++)
			xData[i] = fft.binToHz(i, sampleRate);
		
		return xData;
	}
	
	
	private double[] createYData(float[] data)
	{
		//find max value
		double maxY = data[0];
		for(int i = 1; i < data.length; i++)
			if(data[i] > maxY)
				maxY = data[i];

		//convert to dB and normalize
		double[] yData = new double[data.length];
		for(int i = 0; i < data.length; i++)
			yData[i] = 20 * Math.log10(data[i] / maxY);
		
		return yData;
	}
}
