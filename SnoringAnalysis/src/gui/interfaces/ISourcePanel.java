package gui.interfaces;

public interface ISourcePanel
{
	public enum SoundSource
	{
		File, Mic
	}
	
	public void registerPlaySoundSwither(IPlaySoundSwitcher switcher);
}
