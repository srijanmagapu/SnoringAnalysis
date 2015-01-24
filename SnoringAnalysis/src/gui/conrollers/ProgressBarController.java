package gui.conrollers;

import gui.interfaces.IProgressBar;

import java.util.Deque;
import java.util.LinkedList;

import javax.swing.JProgressBar;

public class ProgressBarController implements IProgressBar
{
	private static ProgressBarController instance;
	public static ProgressBarController getInstance()
	{
		if(instance == null)
			instance = new ProgressBarController();
		
		return instance;
	}

	private ProgressBarController()
	{
		stack = new LinkedList<>();
	}
	
	private  Deque<JProgressBar> stack;

	public JProgressBar getProgressBar()
	{
		return stack.peek();
	}

	public void setProgressBar(JProgressBar bar)
	{
		stack.offerFirst(bar);
	}
	
	public void removeProgressBar()
	{
		stack.poll();
	}

	@Override
	public void setProgressValue(int percent)
	{
		JProgressBar bar = getProgressBar();
		if(bar != null)
			bar.setValue(percent);
	}
	
}
