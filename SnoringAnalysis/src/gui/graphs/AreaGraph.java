package gui.graphs;

import java.awt.Color;

import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import gui.interfaces.ISignalGraph;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class AreaGraph extends ChartPanel implements ISignalGraph
{
	private XYSeries xySeries;
	private JFreeChart chart;
	
	
	public AreaGraph(JFreeChart chart, String name)
	{
		super(chart);
		setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), name, TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLUE));

		xySeries = new XYSeries("");
		XYSeriesCollection dataset = new XYSeriesCollection(xySeries);
		
		chart = ChartFactory.createXYAreaChart("", "", "", dataset);
		chart.removeLegend();
		chart.getXYPlot().getDomainAxis().setAxisLineVisible(false);
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
