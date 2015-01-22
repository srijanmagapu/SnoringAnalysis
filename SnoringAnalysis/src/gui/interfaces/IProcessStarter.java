package gui.interfaces;

import controllers.IStartProcessingHandler;

public interface IProcessStarter
{
	public void registerStartStopHandler(IStartProcessingHandler startHandler);
	public void unRegisterStartStopHandler(IStartProcessingHandler startHandler);
}
