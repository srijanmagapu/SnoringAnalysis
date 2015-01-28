package gui;

import gui.graphs.MulticolorLineRenderer;
import gui.interfaces.IPatientView;
import model.EventPoint;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYSeries;

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
		dataset = new DefaultXYDataset();

		chart = ChartFactory.createTimeSeriesChart("", "", "", dataset, false, true, true);

		renderer = new MulticolorLineRenderer();

		chart.getXYPlot().setRenderer(renderer);
		NumberAxis axis = (NumberAxis) chart.getXYPlot().getRangeAxis();
		axis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		axis.setAutoRangeIncludesZero(false);

		this.setChart(chart);
	}

	@Override
	public void setData(double[] xData, double[] yData)
	{
		xySeries.clear();

		double[] newx = new double[xData.length];
		double[] newy = new double[xData.length];
		newx[0] = xData[0];
		newy[0] = yData[0];
		int cnt = 1;

		final double eps = 0.000001;
		double prev = xData[0];

		for (int i = 1; i < xData.length; i++)
		{
			if ((xData[i] - prev) > eps)
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

		dataset.addSeries("", new double[][] { xToSet, yToSet });
	}

	@Override
	public void setEventPoints(EventPoint[] points)
	{
		renderer.setEventPoints(points);
	}

}
