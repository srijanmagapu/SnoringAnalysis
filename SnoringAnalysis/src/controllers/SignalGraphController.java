package controllers;

import java.util.Observable;
import java.util.Observer;

import javax.swing.SwingUtilities;

import model.SignalBuffer;
import gui.interfaces.ISignalGraph;

public class SignalGraphController implements Observer
{
	private ISignalGraph view;
	private SignalBuffer buffer;
	
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
				view.setData(((SignalBuffer)sender).getBuffer(), (float)params);
			}
		});
	}
}
