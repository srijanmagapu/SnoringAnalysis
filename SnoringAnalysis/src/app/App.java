package app;

import java.io.IOException;
import javax.sound.sampled.UnsupportedAudioFileException;
import audioProcessors.GraphBuilder;

public class App {

	public static void main(String[] args) throws IOException, UnsupportedAudioFileException
	{
		if (args.length < 1)
		{
			System.out.println("Where is the input file??????");
		}


		GraphBuilder tdp = new GraphBuilder(args[0]);
		tdp.readFileByAudioDispatcher();
		
		tdp.drawTDGraph("Time Domain");
		tdp.drawMagnitudeGraph("Magnitude");
		tdp.drawPowerGraph("Power");
		
	}

}
