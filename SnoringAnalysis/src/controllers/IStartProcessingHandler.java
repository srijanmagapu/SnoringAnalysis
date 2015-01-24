package controllers;

import businessLayer.IModeSwitcher.Mode;
import gui.interfaces.ISourcePanel;

public interface IStartProcessingHandler
{
	public void startProcessing(ISourcePanel.SoundSource source, String path, Mode mode);
	public void startProcessing(ISourcePanel.SoundSource source, String[] path, Mode mode);
	public void stopProcessing();
}
