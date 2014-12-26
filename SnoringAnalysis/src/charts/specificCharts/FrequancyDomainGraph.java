package charts.specificCharts;

import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import charts.baseCharts.AreaChart;

public class FrequancyDomainGraph extends AreaChart{

	public FrequancyDomainGraph()
	{
		super();
	}
	
	public FrequancyDomainGraph(String chartName, String xAxisName, String yAxisName)
	{
		super(chartName, xAxisName, yAxisName);
	}
	
	@Override
	protected XYDataset createDataset(double[] xData, double[] yData)
	{
		XYSeries xySeries = new XYSeries("");
		XYSeries bottom = new XYSeries(" ");
		double min = yData[0];

		for (int i = 0; i < xData.length; i++)
		{
			xySeries.add(xData[i], yData[i]);
			if (yData[i] < min) min = yData[i];
		}

		for (int i = 0; i < xData.length; i++)
		{
			bottom.add(xData[i], min);
		}

		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(xySeries);
		dataset.addSeries(bottom);
		return dataset;
	}
}
