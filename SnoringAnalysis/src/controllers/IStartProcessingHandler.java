package controllers;

import gui.interfaces.ISourcePanel;

public interface IStartProcessingHandler
{
	public void startProcessing(ISourcePanel.SoundSource source, String path);
	public void startProcessing(ISourcePanel.SoundSource source, String[] path);
	public void stopProcessing();
}
