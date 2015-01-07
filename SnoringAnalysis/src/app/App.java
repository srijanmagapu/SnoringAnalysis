package app;

import gui.MainFrame;

import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import utils.Constants;
import be.tarsos.dsp.io.jvm.JVMAudioInputStream;
import businessLayer.DBCreator;
import businessLayer.FeatureQueue;
import businessLayer.FeatureProcessor;
import businessLayer.FeatureWorker;
import audioProcessors.GraphBuilder;
import audioProcessors.EnergyProcessors.EnergyDispatcher;

public class App {
	
	public static void main(String[] args) throws UnsupportedAudioFileException, IOException
	{
		
		FeatureWorker worker = new FeatureWorker(FeatureQueue.getInstance());
		worker.setIConsumer(new DBCreator(Constants.numOfDimensions, Constants.numOfClusters));
		(new Thread(worker, "Worker")).start();
		
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					MainFrame frame = new MainFrame();
					DispatchManager dm = new DispatchManager(frame);
					frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
		
		//app.dm.initDispatcher( new JVMAudioInputStream(AudioSystem.getAudioInputStream(new File(args[0])) ));
	}

}
