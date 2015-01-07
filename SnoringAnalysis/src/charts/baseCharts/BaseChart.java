package charts.baseCharts;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public abstract class BaseChart
{
	protected String xAxisName = "";
	protected String yAxisName = "";

	protected double[] xData;
	protected double[] yData;


	public void setxAxisName(String xAxisName)
	{
		this.xAxisName = xAxisName;
	}

	public void setyAxisName(String yAxisName)
	{
		this.yAxisName = yAxisName;
	}

	public BaseChart(String xAxisName, String yAxisName)
	{
		this.xAxisName = xAxisName;
		this.yAxisName = yAxisName;
	}

	public BaseChart()
	{
	}

	/**
	 * Set data for chart.
	 * The arrays must have same length
	 * @param xData data for x axis
	 * @param yData data for y axis
	 */
	public void setData(double[] xData, double[] yData)
	{
		if(xData.length != yData.length){
			String msg = "The xData and yData arrays must have same length";
			throw new IllegalArgumentException(msg);
		}
		
		this.xData = xData;
		this.yData = yData;
	}

	/**
	 * Set data for chart.
	 * The arrays must have same length
	 * @param xData data for x axis
	 * @param yData data for y axis
	 */
	public void setData(float[] xData, float[] yData)
	{
		if(xData.length != yData.length){
			String msg = "The xData and yData arrays must have same length";
			throw new IllegalArgumentException(msg);
		}
		
		this.xData = new double[xData.length];
		this.yData = new double[yData.length];

		for (int i = 0; i < xData.length; i++)
		{
			this.xData[i] = xData[i];
			this.yData[i] = yData[i];
		}
	}
	
	/**
	 * Set the data for chart.
	 * yData indexes will be used for xData
	 * @param yData data to set
	 */
	public void setData(double[] yData)
	{
		double[] data = createXData(yData.length);
		setData(data, yData);
	}
	
	/**
	 * Set the data for chart.
	 * yData indexes will be used for xData
	 * @param yData data to set
	 */
	public void setData(float[] yData)
	{
		double[] data = new double[yData.length];
		for(int i = 0; i < data.length; i++)
			data[i] = yData[i];
		
		setData(data);
	}
	
	private double[] createXData(int size)
	{
		double[] data = new double[size];
		for(int i = 0; i < size; i++)
			data[i] = i;
		
		return data;
	}

	/**
	 * Show the chart
	 */
	public ChartPanel getPanel()
	{
		XYDataset dataSet = createDataset(xData, yData);
		JFreeChart chart = createChart(dataSet);
		ChartPanel chartPanel = new ChartPanel(chart);
		//chartPanel.setPreferredSize(new java.awt.Dimension(590, 420));

		//ApplicationFrame frame = new ApplicationFrame(frameName);
		//frame.setContentPane(chartPanel);

		//frame.pack();
		//RefineryUtilities.centerFrameOnScreen(frame);
		//frame.setVisible(true);
		
		return chartPanel;
	}

	/**
	 * Create the data set to be drawn on the chart
	 * @param xData the data for x axis
	 * @param yData the data for y axis
	 * @return
	 */
	protected abstract XYDataset createDataset(double[] xData, double[] yData);
	
	/**
	 * Create the chart
	 * @param dataSet dataSet to be shown on chart
	 * @return
	 */
	protected abstract JFreeChart createChart(XYDataset dataSet);
}
