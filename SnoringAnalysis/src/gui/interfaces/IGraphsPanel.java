package gui.interfaces;

import gui.graphs.AreaGraph;
import gui.graphs.LineGraph;

public interface IGraphsPanel
{
	public LineGraph getTDGraphPanel();
	public LineGraph getMFCCGraphPanel();
	public AreaGraph getFDGraphPanel();
	public AreaGraph getEnergyGraphPanel();
}
