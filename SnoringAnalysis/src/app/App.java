package app;

import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;

import be.tarsos.dsp.mfcc.MFCC;
import audioProcessors.GraphBuilder;
import audioProcessors.EnergyProcessors.EnergyDispatcher;

public class App {

	public static void main(String[] args) throws IOException, UnsupportedAudioFileException
	{
		if (args.length < 1)
		{
			System.out.println("Where is the input file??????");
		}

		
		GraphBuilder tdp = new GraphBuilder(args[1]);
		tdp.processAudioFile();
		
		tdp.drawTDGraph("Time Domain");
		//tdp.drawMagnitudeGraph("Magnitude");
		//tdp.drawPowerGraph("Power");
		
		
		EnergyDispatcher ed = new EnergyDispatcher(args[1]);
		ed.dispatchSound();
		//ed.drawTDEnergyGraph();
		//ed.drawFDEnergyGraph();
		//ed.drawFDRelativeEnergyGraph();
		//ed.drawMFCC();
		
	}

}
