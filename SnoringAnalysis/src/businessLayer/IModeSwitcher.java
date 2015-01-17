package businessLayer;

public interface IModeSwitcher
{
	public enum Mode
	{
		Training,
		Analyzing
	}
	
	public void switchMode(Mode mode);
}
