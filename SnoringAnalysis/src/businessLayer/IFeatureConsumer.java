package businessLayer;

import model.AudioFeature;

public interface IFeatureConsumer extends Runnable
{
	public void consume(AudioFeature energy, AudioFeature mfcc);
}
