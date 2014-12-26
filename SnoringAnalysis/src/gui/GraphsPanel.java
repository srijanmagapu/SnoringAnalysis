package gui;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;

public class GraphsPanel extends JPanel
{
	private JPanel timeDomainPanel;
	private JPanel frequencyDomainPanel;
	private JPanel mfccPanel;
	private JPanel energyPanel;

	/**
	 * Create the panel.
	 */
	public GraphsPanel()
	{
		setLayout(new GridLayout(0, 2, 5, 0));

		timeDomainPanel = new JPanel();
		timeDomainPanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Time Domain", TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLUE));
		add(timeDomainPanel);
		timeDomainPanel.setLayout(new BorderLayout(0, 0));

		frequencyDomainPanel = new JPanel();
		frequencyDomainPanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Frequency Domain", TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLUE));
		add(frequencyDomainPanel);
		frequencyDomainPanel.setLayout(new BorderLayout(0, 0));

		mfccPanel = new JPanel();
		mfccPanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "MFCC", TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLUE));
		add(mfccPanel);
		mfccPanel.setLayout(new BorderLayout(0, 0));

		energyPanel = new JPanel();
		energyPanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Energy", TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLUE));
		add(energyPanel);
		energyPanel.setLayout(new BorderLayout(0, 0));

	}

	public void setTDPanel(JPanel panel)
	{
		timeDomainPanel.removeAll();
		timeDomainPanel.add(panel, BorderLayout.CENTER);
	}
	
	public void setFDPanel(JPanel panel)
	{
		frequencyDomainPanel.removeAll();
		frequencyDomainPanel.add(panel, BorderLayout.CENTER);
	}
	
	public void setMfccPanel(JPanel panel)
	{
		mfccPanel.removeAll();
		mfccPanel.add(panel, BorderLayout.CENTER);
	}
	
	public void setEnergyPanel(JPanel panel)
	{
		energyPanel.removeAll();
		energyPanel.add(panel, BorderLayout.CENTER);
	}
	
	public void setPanels(JPanel td, JPanel fd, JPanel mfcc, JPanel energy)
	{
		timeDomainPanel.removeAll();
		frequencyDomainPanel.removeAll();
		mfccPanel.removeAll();
		energyPanel.removeAll();
		
		timeDomainPanel.add(td, BorderLayout.CENTER);
		frequencyDomainPanel.add(fd, BorderLayout.CENTER);
		mfccPanel.add(mfcc, BorderLayout.CENTER);
		energyPanel.add(energy, BorderLayout.CENTER);
	}
	

}
