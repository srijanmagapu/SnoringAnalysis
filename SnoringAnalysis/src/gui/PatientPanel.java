package gui;

import gui.graphs.MulticolorLineRenderer;
import gui.interfaces.IPatientView;

import java.awt.EventQueue;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import model.EventPoint;
import model.EventPointList;
import model.TimeStamp;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class PatientPanel extends ChartPanel implements IPatientView
{
	private static final long serialVersionUID = 7803149856906063368L;

	private XYSeries xySeries;
	private JFreeChart chart;
	DefaultXYDataset dataset;
	
	MulticolorLineRenderer renderer;
	
	public PatientPanel(JFreeChart dummyChart, String name)
	{
		super(dummyChart);
		
		xySeries = new XYSeries("", false);
		//XYSeriesCollection dataset = new XYSeriesCollection(xySeries);
		dataset = new DefaultXYDataset();
		
		chart = ChartFactory.createTimeSeriesChart("", "", "", dataset, false, true, true);
		//chart.removeLegend();
		
		renderer = new MulticolorLineRenderer();
		
		chart.getXYPlot().setRenderer(renderer);
		NumberAxis axis = (NumberAxis) chart.getXYPlot().getRangeAxis();
		axis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		axis.setAutoRangeIncludesZero(false);
		
		//axis.setDateFormatOverride(new SimpleDateFormat("ss:SSS"));
		
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

		(new Thread(new Runnable()
		{
			public void run()
			{
				System.out.println("Start setting data...");
				long s = System.currentTimeMillis();
				//very bed
				/*for(int i = 0; i < yData.length; i++)
					xySeries.add(xData[i], yData[i]);*/
				
				//out of heap
				/*double[][] series = new double[][]{xData, yData};
				dataset.addSeries("", series);*/
				
				double[] newx = new double[xData.length];
				double[] newy = new double[xData.length];
				newx[0] = xData[0];
				newy[0] = yData[0];
				int cnt = 1;
				
				final double eps = 0.000001;
				double prev = xData[0];
				
				for(int i = 1; i < xData.length; i++)
				{
					if((xData[i] - prev) > eps)
					{
						newx[cnt] = xData[i];
						newy[cnt] = yData[i];
						prev = xData[i];
						cnt++;
					}
				}
				
				double[] xToSet = new double[cnt];
				double[] yToSet = new double[cnt];
				System.arraycopy(newx, 0, xToSet, 0, cnt);
				System.arraycopy(newy, 0, yToSet, 0, cnt);
				
				dataset.addSeries("", new double[][]{xToSet,yToSet});
				
				long f = System.currentTimeMillis();
				System.out.println("Time to set data: " + (f - s) + " ms");
				System.out.println("New data size: " + cnt);
				
				
				EventQueue.invokeLater(new Runnable()
				{
					public void run()
					{
						xySeries.setNotify(true);
					}
				});
			}
		})).start();
		/*long s = System.currentTimeMillis();
		for(int i = 0; i < yData.length; i++)
			xySeries.add(xData[i], yData[i]);

		long f = System.currentTimeMillis();
		System.out.println("Time to set data: " + (f - s) + " ms");
		xySeries.setNotify(true);*/
	}
	
	@Override
	public void setEventPoints(EventPoint[] points)
	{
		renderer.setEventPoints(points);
	}

}
