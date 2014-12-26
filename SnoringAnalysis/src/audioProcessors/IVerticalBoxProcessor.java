package audioProcessors;

import be.tarsos.dsp.AudioProcessor;

public interface IVerticalBoxProcessor extends AudioProcessor 
{
	/**
	 * vbox algortihm would label a window as "out of control" if a certain percentage of the
	 * observations were outside of the box <br/>
	 * 
	 * @return <b>true</b> if the last window processed was marked out of control, <b>false</b> otherwise.
	 */
	public boolean getOutOfControl();
}
