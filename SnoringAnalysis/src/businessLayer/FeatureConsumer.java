package businessLayer;

import java.util.ArrayList;
import java.util.List;

import org.battelle.clodhopper.distance.DistanceMetric;
import org.battelle.clodhopper.fuzzycmeans.FuzzyCMeansParams;

import model.AudioFeature;
import model.TimeStamp;

public abstract class FeatureConsumer implements IFeatureConsumer
{
	protected final int requiredDimension;
	protected final int numberOfClusters;
	protected List<double[]> data = new ArrayList<>();
	protected List<TimeStamp> timeStamps = new ArrayList<>();
	protected double[][] pcaMatrix;
	protected DistanceMetric distMetric;
	
	public FeatureConsumer(int dim, int numberOfClusters)
	{
		this.requiredDimension = dim;
		this.numberOfClusters = numberOfClusters;
		this.distMetric = new FuzzyCMeansParams().getDistanceMetric();
	}
	
	@Override
	public void consume(AudioFeature arg1, AudioFeature arg2)
	{
		constuctFeatureVector(arg1, arg2);
	}
	
	private void constuctFeatureVector(AudioFeature energy, AudioFeature mfcc)
	{
		float[] energyBuff = energy.getData();
		float[] mfccBuff = mfcc.getData();
		
		//merge vectors
		double[] mergedVector = new double[energyBuff.length + mfccBuff.length];
		for( int i=0 ; i<energyBuff.length ; i++ )
		{
			mergedVector[i] = energyBuff[i];
		}

		for( int i=energyBuff.length ; i<energyBuff.length+mfccBuff.length ; i++ )
		{
			mergedVector[i] = energyBuff[i - energyBuff.length];
		}
		
		TimeStamp ts = new TimeStamp(energy.getStartTime(), energy.getEndTime());
		finishConstuction(mergedVector, ts);
	}
	
	protected abstract void finishConstuction(double[] vector, TimeStamp timeStamp);
	
	@Override
	public abstract void run();
	
}
