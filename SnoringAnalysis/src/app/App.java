package app;

import gui.MainFrame;

import java.awt.EventQueue;
import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.UIManager;

import utils.Constants;
import businessLayer.DBCreator;
import businessLayer.FeatureQueue;
import businessLayer.FeatureWorker;

public class App {
	
	public static void main(String[] args) throws UnsupportedAudioFileException, IOException
	{
		try
		{
		  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception e){}
		
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
