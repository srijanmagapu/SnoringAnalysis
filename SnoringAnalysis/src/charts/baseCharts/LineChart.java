package charts.baseCharts;

import java.awt.Color;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public abstract class LineChart extends BaseChart
{
	public LineChart(String chartName, String xAxisName, String yAxisName)
	{
		super(xAxisName, yAxisName);
	}

	public LineChart()
	{
		super();
	}

	public ChartPanel getPanel()
	{
		XYDataset dataSet = createDataset(xData, yData);
		JFreeChart chart = createChart(dataSet);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(590, 420));

		//ApplicationFrame frame = new ApplicationFrame(frameName);
		//frame.setContentPane(chartPanel);

		//frame.pack();
		//RefineryUtilities.centerFrameOnScreen(frame);
		//frame.setVisible(true);
		
		return chartPanel;
	}

	@Override
	protected XYDataset createDataset(double[] xData, double[] yData)
	{
		XYSeries xySeries = new XYSeries("");

		for (int i = 0; i < xData.length; i++)
			xySeries.add(xData[i], yData[i]);

		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(xySeries);
		return dataset;
	}

	@Override
	protected JFreeChart createChart(final XYDataset dataset)
	{
		JFreeChart ch = ChartFactory.createXYLineChart("", xAxisName, yAxisName, dataset, PlotOrientation.VERTICAL, false, true, false);

		// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
		ch.setBackgroundPaint(Color.white);

		XYPlot xyPlot = ch.getXYPlot();
		xyPlot.setBackgroundPaint(Color.lightGray);
		xyPlot.setDomainGridlinePaint(Color.white);

		final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setSeriesLinesVisible(0, true);
		renderer.setSeriesShapesVisible(0, false);
		
		xyPlot.setRenderer(renderer);

		NumberAxis areaRangeAxis = (NumberAxis) xyPlot.getRangeAxis();
		areaRangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		areaRangeAxis.setAutoRangeIncludesZero(false);

		return ch;

	}
}
