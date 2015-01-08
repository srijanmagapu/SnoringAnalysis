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
import gui.graphs.AreaGraph;

public class GraphsPanel extends JPanel implements IGraphsPanel
{
	private TDGraph tdGraph;
	private TDGraph mfccGraph;
	private TDGraph energyGraph;
	private AreaGraph fdGraph;
	
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
		
		tdGraph = new TDGraph((JFreeChart) null, "Time Domain");
		add(tdGraph);
		
		fdGraph = new AreaGraph((JFreeChart) null, "Frequency Domain");
		add(fdGraph);
		
		mfccGraph = new TDGraph((JFreeChart) null, "MFCC");
		add(mfccGraph);
		
		energyGraph = new TDGraph((JFreeChart) null, "Energy");
		add(energyGraph);

	}

	@Override
	public TDGraph getTDGraphPanel()
	{
		return tdGraph;
	}

	@Override
	public TDGraph getMFCCGraphPanel()
	{
		return mfccGraph;
	}

	@Override
	public TDGraph getEnergyGraphPanel()
	{
		return energyGraph;
	}

	@Override
	public AreaGraph getFDGraphPanel()
	{
		return fdGraph;
	}

}
