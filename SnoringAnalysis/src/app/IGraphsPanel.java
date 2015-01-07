package app;

import gui.graphs.TDGraph;

public interface IGraphsPanel
{
	public TDGraph getTDGraphPanel();
	public TDGraph getMFCCGraphPanel();
	public TDGraph getEnergyGraphPanel();
}
