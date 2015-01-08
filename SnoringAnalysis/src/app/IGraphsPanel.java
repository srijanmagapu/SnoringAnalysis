package app;

import gui.graphs.AreaGraph;
import gui.graphs.TDGraph;

public interface IGraphsPanel
{
	public TDGraph getTDGraphPanel();
	public AreaGraph getFDGraphPanel();
	public TDGraph getMFCCGraphPanel();
	public TDGraph getEnergyGraphPanel();
}
