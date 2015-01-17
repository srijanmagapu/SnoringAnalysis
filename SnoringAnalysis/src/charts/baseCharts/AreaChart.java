package charts.baseCharts;

import java.awt.Color;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYDifferenceRenderer;
import org.jfree.data.xy.XYDataset;

public abstract class AreaChart extends BaseChart
{

	public AreaChart()
	{
		super();
	}
	
	public AreaChart(String chartName, String xAxisName, String yAxisName)
	{
		super(xAxisName, yAxisName);
	}
	
	@Override
	protected JFreeChart createChart(final XYDataset dataset)
	{
		JFreeChart ch = ChartFactory.createXYAreaChart("",xAxisName, yAxisName, dataset, PlotOrientation.VERTICAL, false, true, false);

		// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
		ch.setBackgroundPaint(Color.white);

		XYPlot areaPlot = ch.getXYPlot();
		areaPlot.setBackgroundPaint(Color.lightGray);
		areaPlot.setDomainGridlinePaint(Color.white);
		areaPlot.setRangeGridlinePaint(Color.white);

		XYDifferenceRenderer xyDifferenceRenderer = new XYDifferenceRenderer(Color.blue, Color.GREEN, false);
		xyDifferenceRenderer.setSeriesPaint(0, Color.blue);
		xyDifferenceRenderer.setSeriesPaint(1, Color.blue);
		areaPlot.setRenderer(xyDifferenceRenderer);

		NumberAxis areaRangeAxis = (NumberAxis) areaPlot.getRangeAxis();
		areaRangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		areaRangeAxis.setLowerBound(findMinValue(yData));
		areaRangeAxis.setAutoRangeIncludesZero(false);

		return ch;

	}
	
	private double findMinValue(double[] data){
		double min = data[0];
		for(double i : data)
			if(i < min)
				min = i;
		
		return min;
	}
}
