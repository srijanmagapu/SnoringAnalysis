package gui.graphs;

import java.awt.Color;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import gui.interfaces.IAreaSignalGraph;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYDifferenceRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class AreaGraph extends ChartPanel implements IAreaSignalGraph
{

	private static final long serialVersionUID = 5841122772104129834L;
	
	private XYSeries xySeries;
	private XYSeries bottom;
	private double bottomLine = -70;
	
	private JFreeChart chart;
	
	protected XYSeriesCollection dataset;
	
	public AreaGraph(JFreeChart dummyChart, String name)
	{
		super(dummyChart);
		
		setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), name, TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLUE));

		xySeries = new XYSeries("");
		bottom = new XYSeries(" ");
		
		dataset = new XYSeriesCollection();
		dataset.addSeries(bottom);
		dataset.addSeries(xySeries);
		
		chart = ChartFactory.createXYAreaChart("", "", "", dataset);
		chart.removeLegend();
		
		
		//
		XYPlot areaPlot = chart.getXYPlot();
		areaPlot.setBackgroundPaint(Color.lightGray);
		areaPlot.setDomainGridlinePaint(Color.white);
		areaPlot.setRangeGridlinePaint(Color.white);

		XYDifferenceRenderer xyDifferenceRenderer = new XYDifferenceRenderer(Color.blue, Color.GREEN, false);
		xyDifferenceRenderer.setSeriesPaint(0, Color.blue);
		xyDifferenceRenderer.setSeriesPaint(1, Color.blue);
		areaPlot.setRenderer(xyDifferenceRenderer);

		NumberAxis areaRangeAxis = (NumberAxis) chart.getXYPlot().getRangeAxis();
		areaRangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		areaRangeAxis.setAutoRangeIncludesZero(false);
		
		this.setChart(chart);
	}


	@Override
	public void setData(double[] xData, double[] yData)
	{	
		xySeries.clear();
		bottom.clear();
		
		xySeries.setNotify(false);
		bottom.setNotify(false);
		
		for(int i=0; i< yData.length; i++)
		{
			xySeries.add(xData[i], yData[i]);
			bottom.add(xData[i], bottomLine);
		}
		
		((NumberAxis)chart.getXYPlot().getRangeAxis()).setLowerBound(bottomLine);
		
		xySeries.setNotify(true);
		bottom.setNotify(true);
	}
	
	@Override
	public void setBottomLine(double value)
	{
		this.bottomLine = value;
	}
	
	@Override
	public double getBottomLine()
	{
		return this.bottomLine;
	}

}
