package gui;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import java.awt.Color;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.EtchedBorder;

public class FrameSignalAnalysisPanel extends JPanel
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5548810346486560728L;

	/**
	 * Create the panel.
	 */
	public FrameSignalAnalysisPanel()
	{
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		setLayout(null);
		setBounds(0, 0, 661, 473);
		
		JPanel tdPanel = new JPanel();
		tdPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Time Domain", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 255)));
		tdPanel.setBounds(10, 11, 315, 221);
		add(tdPanel);
		
		JPanel fdPanel = new JPanel();
		fdPanel.setBorder(new TitledBorder(null, "Frequency Domain", TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLUE));
		fdPanel.setBounds(333, 11, 315, 221);
		add(fdPanel);
		
		JPanel energyPanel = new JPanel();
		energyPanel.setBorder(new TitledBorder(null, "Energy", TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLUE));
		energyPanel.setBounds(333, 243, 315, 221);
		add(energyPanel);
		
		JPanel melPanel = new JPanel();
		melPanel.setBorder(new TitledBorder(null, "Mel coefficients", TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLUE));
		melPanel.setBounds(10, 243, 315, 221);
		add(melPanel);

	}

}
