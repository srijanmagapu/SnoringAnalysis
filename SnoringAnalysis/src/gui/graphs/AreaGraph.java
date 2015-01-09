package gui.graphs;

import java.awt.Color;

import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import gui.interfaces.ISignalGraph;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYDifferenceRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class AreaGraph extends ChartPanel implements ISignalGraph
{
	private XYSeries xySeries;
	private XYSeries bottom;
	private JFreeChart chart;
	
	
	public AreaGraph(JFreeChart dummyChart, String name)
	{
		super(dummyChart);
		
		setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), name, TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLUE));

		xySeries = new XYSeries("");
		bottom = new XYSeries(" ");
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(xySeries);
		dataset.addSeries(bottom);
		
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
	public void setData(float[] yData, float sampleRate)
	{
		sampleRate = 1;
		double[] doubleData = new double[yData.length];
		for (int i = 0; i < yData.length; i++)
			doubleData[i] = yData[i];

		setData(doubleData, sampleRate);		
	}

	private void setData(double[] yData, float sampleRate)
	{
		double[] xData = createXData(yData.length, sampleRate);
		
		//find max value
		double maxY = yData[0];
		for(int i = 1; i < yData.length; i++)
			if(yData[i] > maxY)
				maxY = yData[i];
		
		//normalize yData
		double minY = yData[0];
		for(int i = 0; i < yData.length; i++)
		{
			yData[i] = 20*Math.log10(yData[i] / maxY);
			//find min y value
			if(yData[i] < minY)
				minY = yData[i];
		}
		
		
		/**set min to const value**/
				minY = -70;
		xySeries.clear();
		bottom.clear();
		xySeries.setNotify(false);
		bottom.setNotify(false);
		
		for(int i=0; i< yData.length; i++)
		{
			xySeries.add(xData[i], yData[i]);
			bottom.add(xData[i], minY);
		}
		
		((NumberAxis)chart.getXYPlot().getRangeAxis()).setLowerBound(minY);
		
		xySeries.setNotify(true);
		bottom.setNotify(true);
	}
	
	private double[] createXData(int size, float sampleRate)
	{
		double[] data = new double[size];
		for (int i = 0; i < size; i++)
			data[i] = i / sampleRate;

		return data;
	}
}
