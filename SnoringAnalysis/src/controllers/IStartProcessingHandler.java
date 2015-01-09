package controllers;

import gui.ISourcePanel;

public interface IStartProcessingHandler
{
	public void startProcessing(ISourcePanel.SoundSource source, String path);
	public void stopProcessing();
}