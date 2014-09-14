package audioProcessors;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.UnsupportedAudioFileException;

import be.tarsos.dsp.AudioDispatcher;
import charts.FrequancyDomainGraph;
import charts.TimeDomainGraph;

public class GraphBuilder
{
	private File source;
	private float[] sourceData;
	private float[] amplitudes;

	private int bufferSize = 1024;
	private int overlapping = 512;

	private FFTProcessor fdProcessor;
	private TDProcessor tdProcessor;
	private AudioFormat format;

	public GraphBuilder(String fileName)
	{
		source = new File(fileName);
	}

	public void readFileByAudioDispatcher() throws UnsupportedAudioFileException, IOException
	{
		AudioDispatcher ad = AudioDispatcher.fromFile(source, bufferSize, overlapping);
		format = ad.getFormat();

		fdProcessor = new FFTProcessor(bufferSize);
		tdProcessor = new TDProcessor(overlapping);

		ad.addAudioProcessor(fdProcessor);
		ad.addAudioProcessor(tdProcessor);
		ad.run();

		amplitudes = fdProcessor.getAmplitudes();
		sourceData = tdProcessor.getFloatBuffer();
	}

	public void drawMagnitudeGraph(String name)
	{
		FrequancyDomainGraph xyChart = new FrequancyDomainGraph(name, "Hz", "Magnitude");

		// find maximal amplitude
		float max = Math.abs(amplitudes[1]);
		for (int i = 2; i < amplitudes.length; i++)
		{
			if (Math.abs(amplitudes[i]) > max) max = Math.abs(amplitudes[i]);
		}

		System.out.println("max: " + max);

		double[] xData = new double[bufferSize / 2];
		double[] yData = new double[bufferSize / 2];
		for (int i = 0; i < bufferSize / 2; i++)
		{
			xData[i] = fdProcessor.getFFT().binToHz(i, format.getSampleRate());
			yData[i] = amplitudes[i];
		}

		xyChart.setXYData(xData, yData);
		xyChart.drawChart();
	}

	public void drawPowerGraph(String name)
	{
		FrequancyDomainGraph xyChart = new FrequancyDomainGraph(name, "Hz", "dB");

		// find maximal amplitude
		float max = Math.abs(amplitudes[5]);
		for (int i = 6; i < amplitudes.length; i++)
		{
			if (Math.abs(amplitudes[i]) > max) max = Math.abs(amplitudes[i]);
		}

		System.out.println("max: " + max);

		double[] xData = new double[bufferSize / 2];
		double[] yData = new double[bufferSize / 2];
		for (int i = 0; i < bufferSize / 2; i++)
		{
			xData[i] = fdProcessor.getFFT().binToHz(i, format.getSampleRate());
			yData[i] = 20 * Math.log10(amplitudes[i] / max);
		}

		xyChart.setXYData(xData, yData);
		xyChart.drawChart();
	}

	public void drawTDGraph(String name)
	{
		TimeDomainGraph xyChart = new TimeDomainGraph(name, "sec", "Power");

		double[] yData = new double[sourceData.length];
		for (int i = 0; i < sourceData.length; i++)
			yData[i] = sourceData[i];

		xyChart.setData(yData, format.getSampleRate());
		xyChart.drawChart();

	}

}
