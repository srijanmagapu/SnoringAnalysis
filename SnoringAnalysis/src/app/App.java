package app;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import be.tarsos.dsp.io.jvm.JVMAudioInputStream;
import businessLayer.FeatureQueue;
import businessLayer.FeatureVectorConstructor;
import businessLayer.FeatureWorker;
import audioProcessors.GraphBuilder;
import audioProcessors.EnergyProcessors.EnergyDispatcher;

public class App {

	public static void main(String[] args) throws IOException, UnsupportedAudioFileException
	{
		if (args.length < 1)
		{
			System.out.println("Where is the input file??????");
		}

		/*
		GraphBuilder tdp = new GraphBuilder(args[2]);
		tdp.processAudioFile();
		
		tdp.drawTDGraph("Time Domain");
		//tdp.drawMagnitudeGraph("Magnitude");
		tdp.drawPowerGraph("Power");
		
		
		//EnergyDispatcher ed = new EnergyDispatcher(args[2]);
		//ed.dispatchSound();
		//ed.drawTDEnergyGraph();
		//ed.drawFDEnergyGraph();
		//ed.drawFDEnergyGraph2();
		*/
		
		FeatureWorker worker = new FeatureWorker(FeatureQueue.getInstance());
		worker.setIConsumer(new FeatureVectorConstructor(3));
		//(new Thread(worker, "Worker")).start();
		
		DispatchManager dispatchManager = new DispatchManager();
		dispatchManager.initDispatcher( new JVMAudioInputStream(AudioSystem.getAudioInputStream(new File(args[0])) ));
		
		FeatureQueue.getInstance().addEnergyBuffer(new float[0]);
		FeatureQueue.getInstance().addMFCCBuffer(new float[0]);
		worker.stopWorker();
	}

}
