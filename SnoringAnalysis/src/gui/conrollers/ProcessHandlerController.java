package gui.conrollers;

import controllers.IStartProcessingHandler;

public class ProcessHandlerController
{
	private static IStartProcessingHandler startStopProcessingHandler;
	
	public static IStartProcessingHandler getStartStopProcessingHandler()
	{
		return startStopProcessingHandler;
	}
	public static void setStartStopProcessingHandler(IStartProcessingHandler handler)
	{
		startStopProcessingHandler = handler;
	}
	

}
