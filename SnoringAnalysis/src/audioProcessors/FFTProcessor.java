package audioProcessors;

import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.util.fft.FFT;
import be.tarsos.dsp.util.fft.HannWindow;

public class FFTProcessor implements AudioProcessor
{
	int frames = 0;
	FFT fft;
	float[] amplitudes;
	private int bufferSize;

	
	public FFT getFFT()
	{
		return fft;
	}
	
	public float[] getAmplitudes()
	{
		return amplitudes;
	}


	public FFTProcessor(int bufferSize)
	{
		this.bufferSize = bufferSize;
		this.fft = new FFT(bufferSize, new HannWindow());
		this.amplitudes = new float[bufferSize / 2];
	}

	@Override
	public boolean process(AudioEvent audioEvent)
	{
		frames++;
		// event floatBuffer is reused so copy it to another array
		float[] eventFloatBuffer = audioEvent.getFloatBuffer();
		float[] transformBuffer = new float[bufferSize ];
		System.arraycopy(eventFloatBuffer, 0, transformBuffer, 0, eventFloatBuffer.length);

		// apply fft
		fft.forwardTransform(transformBuffer);

		// get amplitudes
		float[] abs = new float[bufferSize / 2];
		fft.modulus(transformBuffer, abs);

		//sum amplitudes
		for(int i = 0; i < abs.length; i++)
			amplitudes[i] += abs[i];

		return true;
	}

	@Override
	public void processingFinished()
	{
		System.out.println("TarsosFFTProcessor finished");
		// Printer.printToFile(amplitudes, "TarsosAmplitudes.txt");
		
		System.out.println("Frames "+ frames);
		
		//average amplitudes
		for (int i = 0; i < amplitudes.length; i++)
			amplitudes[i] /= frames;
	}
}
