package charts.specificCharts;

import charts.baseCharts.LineChart;

public class TimeDomainEnergyGraph extends LineChart
{
	public TimeDomainEnergyGraph()
	{
		super();
	}
	
	public TimeDomainEnergyGraph(String chartName, String xAxisName, String yAxisName)
	{
		super(chartName, xAxisName, yAxisName);
	}
	
	public void setData(double[] yData, float timeScale)
	{
		double[] data = createXData(yData, timeScale);
		setData(data, yData);
	}
	
	public void setData(float[] yData, float timeScale)
	{
		double[] doubleYData = new double[yData.length];
		for(int i = 0; i < doubleYData.length; i++)
			doubleYData[i] = yData[i];
		
		double[] data = createXData(doubleYData, timeScale);
		setData(data, doubleYData);
	}

	private double[] createXData(double[] yData, float timeScale){
		double[] data = new double[yData.length];
		for(int i=0; i < yData.length; i++)
			data[i] = i / timeScale;
		
		return data;
	}
}
