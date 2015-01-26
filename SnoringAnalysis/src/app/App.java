package app;

import gui.MainFrame;

import java.awt.EventQueue;
import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.UIManager;

public class App {
	/**
	 * Entry point
	 * @param args
	 * @throws UnsupportedAudioFileException
	 * @throws IOException
	 */
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
					new DispatchManager(frame);
					frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

}
