package audioProcessors;

import model.SignalBuffer;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.mfcc.MFCC;
import businessLayer.FeatureQueue;

public class MFCCProcessor implements AudioProcessor
{
	/**
	 * The implementor of V-Box algorithm. Will signal when acoustical event detected.
	 */
	private IVerticalBoxProcessor vboxProcessor;
	private boolean previousOutOfControlState;

	MFCC mfccProcessor;
	int amountOfCepstrumCoef;

	float[] mfcc;
	float overallMfcc;
	
	public IVerticalBoxProcessor getIVerticalBoxProcessor()
	{
		return vboxProcessor;
	}
	
	public void setIVerticalBoxProcessor(IVerticalBoxProcessor vbox)
	{
		this.vboxProcessor = vbox;
	}
	
	private final SignalBuffer signalBuffer;
	
	public SignalBuffer getSignalBuffer()
	{
		return signalBuffer;
	}
	
	
	//								512				  10240					10						10						150						5000
	public MFCCProcessor(int samplesPerFrame, float sampleRate, int amountOfCepstrumCoef, int amountOfMelFilters, float lowerFilterFreq, float upperFilterFreq)
	{
		this.mfccProcessor = new MFCC(samplesPerFrame, sampleRate, amountOfCepstrumCoef, amountOfMelFilters, lowerFilterFreq, upperFilterFreq);
		this.amountOfCepstrumCoef = amountOfCepstrumCoef;
		this.signalBuffer = new SignalBuffer();
		resetMFCCComputations();
	}
	
	@Override
	public boolean process(AudioEvent audioEvent)
	{
		boolean newOutOfControlState = false;
		
		if (vboxProcessor != null)
			newOutOfControlState = vboxProcessor.getOutOfControl();
		
		if(newOutOfControlState)
		{
			if(!previousOutOfControlState)
				resetMFCCComputations();
			
			mfccProcessor.process(audioEvent);
			float[] localMfcc = mfccProcessor.getMFCC();
			
			for(int i = 0; i < localMfcc.length; i++)
			{
				mfcc[i] += localMfcc[i];
				overallMfcc += localMfcc[i];
			}
		}
		else if(previousOutOfControlState)
		{
			//compute final MFCC for the acoustical event detected by v-box
			finishMfccComputng();
		}
		
		previousOutOfControlState = newOutOfControlState;
		
		return true;
	}
	
	int count = 0;
	
	private void finishMfccComputng()
	{
		for(int i = 0; i < mfcc.length; i++)
			mfcc[i] /= overallMfcc;

		count++;
		System.out.println("Adding MFCC vector " + count);
		FeatureQueue.getInstance().addMFCCBuffer(mfcc);
		
		signalBuffer.setBuffer(mfcc);
	}
	
	private void resetMFCCComputations()
	{
		this.mfcc = new float[amountOfCepstrumCoef];
		this.overallMfcc = 0;
	}
	

	@Override
	public void processingFinished()
	{
		if(overallMfcc != 0)
			finishMfccComputng();
	}

}
