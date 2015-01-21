package gui.graphs;

import gui.interfaces.ISignalGraph;

import java.awt.Color;
import java.text.SimpleDateFormat;

import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class LineGraph extends ChartPanel implements ISignalGraph
{
	private static final long serialVersionUID = 8215031469382849566L;
	
	private XYSeries xySeries;
	private JFreeChart chart;
	
	public LineGraph(JFreeChart dummyChart, String name)
	{
		super(dummyChart);
		setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), name, TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLUE));

		xySeries = new XYSeries("");
		XYSeriesCollection dataset = new XYSeriesCollection(xySeries);
		chart = ChartFactory.createTimeSeriesChart("", "", "", dataset, false, true, true);
		//chart.removeLegend();
		
		DateAxis axis = (DateAxis) chart.getXYPlot().getDomainAxis();
		axis.setDateFormatOverride(new SimpleDateFormat(""));
		
		//NumberAxis areaRangeAxis = (NumberAxis) chart.getXYPlot().getRangeAxis();
		//areaRangeAxis.setAutoRangeIncludesZero(false);
		//areaRangeAxis.setNumberFormatOverride(new MillisecondsSpendNumberFormat());
		//areaRangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits(locale));
		
		this.setChart(chart);
	}

	@Override
	public void setData(double[] xData, double[] yData)
	{
		xySeries.clear();
		xySeries.setNotify(false);
		
		for(int i = 0; i < yData.length; i++)
			xySeries.add(xData[i], yData[i]);

		xySeries.setNotify(true);
	}

}
