package controllers;

import java.util.Observable;
import java.util.Observer;

import javax.swing.SwingUtilities;

import model.ProgressData;
import gui.interfaces.IProgressBar;

public class ProcessProgressController implements Observer
{
	IProgressBar progressBar;
	ProgressData progressData;
	
	public ProcessProgressController(IProgressBar view, ProgressData data)
	{
		this.progressBar = view;
		this.progressData = data;
		data.addObserver(this);
	}

	@Override
	public void update(Observable o, Object arg)
	{
		SwingUtilities.invokeLater(new Runnable(){
            public void run()
			{
            	progressBar.setProgressValue(progressData.getProgress());
			}
		});
	}
}
