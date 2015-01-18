package audioProcessors;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;

public class SwitchableAudioPlayer implements AudioProcessor 
{
	/**
	 * The line to send sound to. Is also used to keep everything in sync.
	 */
	private SourceDataLine line;

	
	private final AudioFormat format;
	
	
	private boolean playAudio;
	

	/**
	 * Creates a new audio player.
	 * 
	 * @param format
	 *            The AudioFormat of the buffer.
	 * @throws LineUnavailableException
	 *             If no output line is available.
	 */
	public SwitchableAudioPlayer(final AudioFormat format)	throws LineUnavailableException {
		final DataLine.Info info = new DataLine.Info(SourceDataLine.class,format);
		this.format = format;
		line = (SourceDataLine) AudioSystem.getLine(info);
		line.open();
		line.start();
	}
	
	public SwitchableAudioPlayer(final AudioFormat format, int bufferSize) throws LineUnavailableException {
		final DataLine.Info info = new DataLine.Info(SourceDataLine.class,format,bufferSize);
		this.format = format;
		line = (SourceDataLine) AudioSystem.getLine(info);
		line.open();
		line.start();
	}
	
	
	@Override
	public boolean process(AudioEvent audioEvent) {
		
		if (playAudio)
		{
			// overlap in samples * nr of bytes / sample = bytes overlap
			int byteOverlap = audioEvent.getOverlap() * format.getFrameSize();
			int byteStepSize = audioEvent.getBufferSize() * format.getFrameSize() - byteOverlap;
			line.write(audioEvent.getByteBuffer(), byteOverlap, byteStepSize);
		}
		
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see be.hogent.tarsos.util.RealTimeAudioProcessor.AudioProcessor#
	 * processingFinished()
	 */
	public void processingFinished() {
		// cleanup
		line.drain();//drain takes too long..
		line.stop();
		line.close();
	}

	public void setPlayAudio(boolean playAudio)
	{
		this.playAudio = playAudio;
	}
}
