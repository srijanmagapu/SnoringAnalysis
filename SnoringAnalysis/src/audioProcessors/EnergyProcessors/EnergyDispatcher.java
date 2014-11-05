package audioProcessors.EnergyProcessors;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.UnsupportedAudioFileException;

import charts.specificCharts.FrequencyDomainEnergyGraph;
import charts.specificCharts.MFCCGraph;
import charts.specificCharts.TimeDomainEnergyGraph;
import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.mfcc.MFCC;
import be.tarsos.dsp.util.fft.FFT;

public class EnergyDispatcher
{
	private File source;
	private int bufferSize = 1024*2;
	private int energyBlockSize = 512;
	private FDEnergyProcessor fdEnergyProcessor;
	private FDRelativeEnergyProcessor fdRelativeEnergyProcessor;
	private TDEnergyProcessor tdEnergyProcessor;
	private MFCC mfccProcessor;
	private double[] tdEnergyBuffer;
	private double[] fdEnergyBuffer;
	private double[] fdRelativeEnergyBuffer;
	private float[] mfccBuffer;
	private AudioFormat format;
	
	
	public EnergyDispatcher(String source)
	{
		this.source = new File(source);
	}
	
	
	public void dispatchSound() throws UnsupportedAudioFileException, IOException{
		AudioDispatcher fdd = AudioDispatcher.fromFile(source, bufferSize, bufferSize / 2);
		AudioDispatcher fdRelatived = AudioDispatcher.fromFile(source, bufferSize, 0);
		AudioDispatcher tdd = AudioDispatcher.fromFile(source, bufferSize, 0);
		AudioDispatcher mfccd = AudioDispatcher.fromFile(source, bufferSize, 0);
		
		format = fdd.getFormat();

		fdEnergyProcessor = new FDEnergyProcessor(bufferSize, energyBlockSize);
		fdRelativeEnergyProcessor = new FDRelativeEnergyProcessor(bufferSize, energyBlockSize);
		tdEnergyProcessor = new TDEnergyProcessor();
		mfccProcessor = new MFCC((int)format.getSampleRate()/bufferSize, (int)format.getSampleRate());
		
		tdd.addAudioProcessor(tdEnergyProcessor);
		fdd.addAudioProcessor(fdEnergyProcessor);
		fdRelatived.addAudioProcessor(fdRelativeEnergyProcessor);
		mfccd.addAudioProcessor(mfccProcessor);
		
		tdd.run();
		fdd.run();
		fdRelatived.run();
		mfccd.run();
		
		tdEnergyBuffer = tdEnergyProcessor.getEnergyBuffer();
		fdEnergyBuffer = fdEnergyProcessor.getEnergyBuffer();
		fdRelativeEnergyBuffer = fdRelativeEnergyProcessor.getEnergyBuffer();
		mfccBuffer = mfccProcessor.getMFCC();
		
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
	
	public void drawFDRelativeEnergyGraph()
	{
		TimeDomainEnergyGraph fdg = new TimeDomainEnergyGraph("FD Relative Energy", "Sec", "yyy");
		//fdg.setData(xData, fdEnergyBuffer);
		//fdg.setData(xData, convertAmplitudeToDB(fdEnergyBuffer));
		fdg.setData(fdRelativeEnergyBuffer, format.getSampleRate() / bufferSize);
		fdg.drawChart();
	}
	
	public void drawMFCC()
	{
		MFCCGraph mfccg = new MFCCGraph("MFCC", "xxx", "yyy");
		mfccg.setData(mfccBuffer);
		mfccg.drawChart();
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
	
	private double[] createFDRelativexData(FFT fft, float sampleRate)
	{
		double[] data = new double[bufferSize / (energyBlockSize * 2 * 2)];
		for(int i = 0; i < data.length / 2; i++)
			data[i] = fft.binToHz(i*energyBlockSize, sampleRate);
		
		return data;
	}
	
}
