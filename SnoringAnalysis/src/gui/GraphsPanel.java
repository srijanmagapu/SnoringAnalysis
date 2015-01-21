package gui;

import javax.swing.JPanel;
import java.awt.GridLayout;
import gui.graphs.LineGraph;
import org.jfree.chart.JFreeChart;
import gui.graphs.AreaGraph;
import gui.interfaces.IGraphsPanel;

public class GraphsPanel extends JPanel implements IGraphsPanel
{
	private static final long serialVersionUID = -8984907739137695393L;
	
	private LineGraph tdGraph;
	private LineGraph mfccGraph;
	private AreaGraph energyGraph;
	private AreaGraph fdGraph;
	
	public LineGraph getTDGraph()
	{
		return tdGraph;
	}

	/**
	 * Create the panel.
	 */
	public GraphsPanel()
	{
		setLayout(new GridLayout(0, 2, 5, 0));
		
		tdGraph = new LineGraph((JFreeChart) null, "Time Domain");
		add(tdGraph);
		
		fdGraph = new AreaGraph((JFreeChart) null, "Frequency Domain");
		add(fdGraph);
		
		mfccGraph = new LineGraph((JFreeChart) null, "MFCC");
		add(mfccGraph);
		
		energyGraph = new AreaGraph((JFreeChart) null, "Energy");
		add(energyGraph);

	}

	@Override
	public LineGraph getTDGraphPanel()
	{
		return tdGraph;
	}

	@Override
	public LineGraph getMFCCGraphPanel()
	{
		return mfccGraph;
	}

	@Override
	public AreaGraph getEnergyGraphPanel()
	{
		return energyGraph;
	}

	@Override
	public AreaGraph getFDGraphPanel()
	{
		return fdGraph;
	}

}
