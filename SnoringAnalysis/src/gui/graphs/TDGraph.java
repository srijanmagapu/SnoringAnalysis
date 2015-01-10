package gui.graphs;

import gui.interfaces.ISignalGraph;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;

import java.awt.Color;

public class TDGraph extends ChartPanel implements ISignalGraph
{
	private XYSeries xySeries;
	private JFreeChart chart;
	
	public TDGraph(JFreeChart dummyChart, String name)
	{
		super(dummyChart);
		setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), name, TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLUE));

		xySeries = new XYSeries("");
		XYSeriesCollection dataset = new XYSeriesCollection(xySeries);
		chart = ChartFactory.createTimeSeriesChart("", "", "", dataset, false, true, true);
		//chart.removeLegend();
		
		//NumberAxis areaRangeAxis = (NumberAxis) chart.getXYPlot().getRangeAxis();
		//areaRangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		//areaRangeAxis.setAutoRangeIncludesZero(false);
		
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
	/*public void setData(float[] yData, float sampleRate)
	{
		double[] doubleData = new double[yData.length];
		for (int i = 0; i < yData.length; i++)
			doubleData[i] = yData[i];

		setData(doubleData, sampleRate);
	}*/
	
	private void setData(double[] yData, float sampleRate)
	{
		double[] xData = createXData(yData.length, sampleRate);
		
		xySeries.clear();
		xySeries.setNotify(false);
		for(int i=0; i< yData.length; i++)
			xySeries.add(xData[i], yData[i]);

		xySeries.setNotify(true);
	}
	
	private double[] createXData(int size, float sampleRate)
	{
		double[] data = new double[size];
		for (int i = 0; i < size; i++)
			data[i] = i / sampleRate;

		return data;
	}


}
