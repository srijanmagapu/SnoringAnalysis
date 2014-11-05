package gui;

import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

public class SnoreDetectionPanel extends JPanel
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public SnoreDetectionPanel()
	{
		setLayout(null);
		
		JPanel tdPanel = new JPanel();
		tdPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		tdPanel.setBounds(10, 11, 566, 154);
		add(tdPanel);
		
		JPanel detectionPanel = new JPanel();
		detectionPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		detectionPanel.setBounds(10, 163, 566, 30);
		add(detectionPanel);

	}
}
