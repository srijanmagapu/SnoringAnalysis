package gui.interfaces;

import controllers.IStartProcessingHandler;


public interface IMainFrame
{
	public void setStartStopProcessingHandler(IStartProcessingHandler handler);
	public IGraphsPanel getIGraphsPanel();
	public ISourcePanel getSourcePanel();
	public IProgressBar getIProgressBar();
}
