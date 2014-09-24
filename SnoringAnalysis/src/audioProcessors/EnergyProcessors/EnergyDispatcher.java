package audioProcessors.EnergyProcessors;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.UnsupportedAudioFileException;

import charts.specificCharts.FrequencyDomainEnergyGraph;
import charts.specificCharts.TimeDomainEnergyGraph;
import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.util.fft.FFT;

public class EnergyDispatcher
{
	private File source;
	private int bufferSize = 1024;
	private int energyBlockSize = 8;
	private FDEnergyProcessor fdEnergyProcessor;
	private FDEnergyProcessor2 fdEnergyProcessor2;
	private TDEnergyProcessor tdEnergyProcessor;
	private double[] tdEnergyBuffer;
	private double[] fdEnergyBuffer;
	private double[] fdEnergyBuffer2;
	private AudioFormat format;
	
	
	public EnergyDispatcher(String source)
	{
		this.source = new File(source);
	}
	
	
	public void dispatchSound() throws UnsupportedAudioFileException, IOException{
		AudioDispatcher fdd = AudioDispatcher.fromFile(source, bufferSize, bufferSize / 2);
		AudioDispatcher fdd2 = AudioDispatcher.fromFile(source, bufferSize, bufferSize / 2);
		AudioDispatcher tdd = AudioDispatcher.fromFile(source, bufferSize, 0);
		format = fdd.getFormat();

		fdEnergyProcessor = new FDEnergyProcessor(bufferSize, energyBlockSize);
		fdEnergyProcessor2 = new FDEnergyProcessor2(bufferSize, energyBlockSize);
		tdEnergyProcessor = new TDEnergyProcessor();
		
		tdd.addAudioProcessor(tdEnergyProcessor);
		fdd.addAudioProcessor(fdEnergyProcessor);
		fdd2.addAudioProcessor(fdEnergyProcessor2);
		
		tdd.run();
		fdd.run();
		fdd2.run();
		tdEnergyBuffer = tdEnergyProcessor.getEnergyBuffer();
		fdEnergyBuffer = fdEnergyProcessor.getEnergyBuffer();
		fdEnergyBuffer2 = fdEnergyProcessor2.getEnergyBuffer();
	}
	
	public void drawTDEnergyGraph(){
		TimeDomainEnergyGraph tdg = new TimeDomainEnergyGraph("TD Energy", "sec", "dBSPL");
		tdg.setData(tdEnergyBuffer,  format.getSampleRate() / bufferSize);
		tdg.drawChart();
	}
	
	public void drawFDEnergyGraph()
	{
		FrequencyDomainEnergyGraph fdg = new FrequencyDomainEnergyGraph("FD Energy", "Hz", "yyy");
		double[] xData = createFDxData(fdEnergyProcessor.getFFT(), format.getSampleRate());
		//fdg.setData(xData, fdEnergyBuffer);
		//fdg.setData(xData, convertAmplitudeToDB(fdEnergyBuffer));
		fdg.setData(xData, convertAmplitudeToDB(normalizeAmplitudes(fdEnergyBuffer)));
		fdg.drawChart();
	}
	
	public void drawFDEnergyGraph2()
	{
		FrequencyDomainEnergyGraph fdg = new FrequencyDomainEnergyGraph("FD Energy2", "Hz", "yyy");
		double[] xData = createFD2xData(fdEnergyProcessor2.getFFT(), format.getSampleRate());
		//fdg.setData(xData, fdEnergyBuffer);
		//fdg.setData(xData, convertAmplitudeToDB(fdEnergyBuffer));
		fdg.setData(xData, convertAmplitudeToDB(normalizeAmplitudes(fdEnergyBuffer2)));
		fdg.drawChart();
	}
	
	private double[] normalizeAmplitudes(double[] ampl)
	{
		double[] temp = new double[ampl.length];
		double max = ampl[0];
		for(double i : ampl)
			if(i > max)
				max = i;
		
		for(int i = 0; i < ampl.length; i++)
			temp[i] = ampl[i] / max;
		
		return temp;
	}
	
	private double[] convertAmplitudeToDB(double[] buff)
	{
		double[] data = new double[buff.length];
		for(int i = 0; i < data.length; i++)
			data[i] = 20 * Math.log10(buff[i]);
		
		return data;
	}
	
	private double[] createFDxData(FFT fft, float sampleRate)
	{
		double[] data = new double[bufferSize / (energyBlockSize * 2)];
		for(int i = 0; i < data.length; i++)
			data[i] = fft.binToHz(i*energyBlockSize, sampleRate);
		
		return data;
	}
	
	private double[] createFD2xData(FFT fft, float sampleRate)
	{
		double[] data = new double[bufferSize / (energyBlockSize * 2 * 2)];
		for(int i = 0; i < data.length / 2; i++)
			data[i] = fft.binToHz(i*energyBlockSize, sampleRate);
		
		return data;
	}
	
}
