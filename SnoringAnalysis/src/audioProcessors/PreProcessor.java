package audioProcessors;

import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;

public class PreProcessor implements AudioProcessor
{
	private double calculatedHeight = 0.1;
	
	public double getCaculatedheight()
	{
		return calculatedHeight;
	}

	@Override
	public boolean process(AudioEvent audioEvent)
	{
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void processingFinished()
	{
		// TODO Auto-generated method stub
		
	}

}
