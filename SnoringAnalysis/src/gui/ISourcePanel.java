package gui;

import controllers.IStartProcessingHandler;

public interface ISourcePanel
{
	public enum SoundSource
	{
		File, Mic
	}
	
	public void registerStartStopHandler(IStartProcessingHandler startHandler);
}
