package charts.specificCharts;


import charts.baseCharts.LineChart;

public class TimeDomainGraph extends LineChart
{
	public TimeDomainGraph()
	{
		super();
	}
	
	public TimeDomainGraph(String chartName, String xAxisName, String yAxisName)
	{
		super(chartName, xAxisName, yAxisName);
	}

	public void setData(float[] yData, float sampleRate)
	{
		double[] doubleData = new double[yData.length];
		for (int i = 0; i < yData.length; i++)
			doubleData[i] = yData[i];

		setData(doubleData, sampleRate);
	}

	public void setData(double[] yData, float sampleRate)
	{
		this.yData = yData;
		double[] xData = createXData(yData.length, sampleRate);
		setData(xData, yData);
	}

	private double[] createXData(int size, float sampleRate)
	{
		double[] data = new double[size];
		for (int i = 0; i < size; i++)
			data[i] = i / sampleRate;

		return data;
	}
}
