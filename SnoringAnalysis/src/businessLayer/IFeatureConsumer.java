package businessLayer;

public interface IFeatureConsumer extends Runnable
{
	public void consume(float[] energy, float[] mfcc);
}
