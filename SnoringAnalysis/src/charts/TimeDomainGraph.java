package charts;

import java.awt.Color;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYDifferenceRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class TimeDomainGraph
{
	private String chartName;
	private String xAxisName;
	private String yAxisName;
	private String frameName = "";
	
	private double[] xData;
	private double[] yData;
	private float sampleRate;
	
	public void setFrameName(String frameName){
		this.frameName = frameName;
	}
	
	public void setChartName(String chartName) {
		this.chartName = chartName;
	}

	public void setxAxisName(String xAxisName) {
		this.xAxisName = xAxisName;
	}

	public void setyAxisName(String yAxisName) {
		this.yAxisName = yAxisName;
	}
	
	public TimeDomainGraph(String chartName, String xAxisName, String yAxisName){
		this.chartName = chartName;
		this.xAxisName = xAxisName;
		this.yAxisName = yAxisName;
	}
	
	
	public void setData(double[] yData, float frequency){
		this.sampleRate = frequency;
		this.yData = yData;
		createXData();
	}
	
	private void createXData(){
		xData = new double[yData.length];
		for(int i=0; i < yData.length; i++)
			xData[i] = i / sampleRate;
	}
	
	
	public void drawChart()
	{
		XYDataset dataSet = createDataset(xData, yData);
		JFreeChart chart = createChart(dataSet);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(590, 420));
		
		ApplicationFrame frame = new ApplicationFrame(frameName);
		frame.setContentPane(chartPanel);

		frame.pack();
		RefineryUtilities.centerFrameOnScreen(frame);
		frame.setVisible(true);
	}
	
	
	private XYDataset createDataset(double[] xData, double[] yData)
	{
		XYSeries xySeries = new XYSeries("");
		
		for (int i = 0; i < xData.length; i ++)
			xySeries.add(xData[i], yData[i]);
		
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(xySeries);
		return dataset;
	}
	
	

	private JFreeChart createChart(final XYDataset dataset)
	{
		JFreeChart ch = ChartFactory.createXYAreaChart(chartName, xAxisName, yAxisName, dataset, PlotOrientation.VERTICAL, false, true, false);
		
		// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
		ch.setBackgroundPaint(Color.white);

		XYPlot areaPlot = ch.getXYPlot();
		areaPlot.setBackgroundPaint(Color.lightGray);
		areaPlot.setDomainGridlinePaint(Color.white);
		areaPlot.setRangeGridlinePaint(Color.white);
		
		
		final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setSeriesLinesVisible(0, true);
		renderer.setSeriesShapesVisible(0, false);
		
		XYDifferenceRenderer xyDifferenceRenderer = new XYDifferenceRenderer (Color.blue, Color.GREEN, false);
		xyDifferenceRenderer.setSeriesPaint(0, Color.blue);
		xyDifferenceRenderer.setSeriesPaint(1, Color.blue);
		areaPlot.setRenderer(xyDifferenceRenderer);

		NumberAxis areaRangeAxis = (NumberAxis) areaPlot.getRangeAxis();
		areaRangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		areaRangeAxis.setAutoRangeIncludesZero(false);
		
		return ch;

	}
}
