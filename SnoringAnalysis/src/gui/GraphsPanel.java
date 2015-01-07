package gui;

import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;

import org.jfree.chart.ChartPanel;

import java.awt.Color;

import gui.graphs.TDGraph;

import org.jfree.chart.JFreeChart;

import app.IGraphsPanel;

public class GraphsPanel extends JPanel implements IGraphsPanel
{
	private ChartPanel frequencyDomainPanel;
	private ChartPanel mfccPanel;
	private ChartPanel energyPanel;
	private TDGraph tdGraph;
	
	public TDGraph getTDGraph()
	{
		return tdGraph;
	}

	/**
	 * Create the panel.
	 */
	public GraphsPanel()
	{
		setLayout(new GridLayout(0, 2, 5, 0));
		
		tdGraph = new TDGraph((JFreeChart) null);
		add(tdGraph);

		frequencyDomainPanel = new ChartPanel(null);
		frequencyDomainPanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Frequency Domain", TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLUE));
		add(frequencyDomainPanel);
		frequencyDomainPanel.setLayout(new BorderLayout(0, 0));

		mfccPanel = new ChartPanel(null);
		mfccPanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "MFCC", TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLUE));
		add(mfccPanel);
		mfccPanel.setLayout(new BorderLayout(0, 0));

		energyPanel = new ChartPanel(null);
		energyPanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Energy", TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLUE));
		add(energyPanel);
		energyPanel.setLayout(new BorderLayout(0, 0));

	}

	@Override
	public TDGraph getTDGraphPanel()
	{
		return tdGraph;
	}

}
