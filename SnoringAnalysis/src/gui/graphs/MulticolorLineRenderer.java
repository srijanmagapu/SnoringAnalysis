package gui.graphs;

import java.awt.Color;
import java.awt.Paint;
import java.util.Arrays;

import model.EventPoint;

import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;

public class MulticolorLineRenderer extends XYLineAndShapeRenderer
{
	private static final long serialVersionUID = 4439401895209254083L;

	Paint[] paints = new Paint[] { Color.RED, Color.GREEN, Color.BLUE };

	Paint defaultPaint = Color.DARK_GRAY;

	EventPoint[] points;

	public MulticolorLineRenderer()
	{
		super(true, false);
	}

	@Override
	public Paint getItemPaint(int series, int item)
	{
		XYDataset data = this.getPlot().getDataset(series);
		double x = data.getX(series, item).doubleValue();

		Paint p = null;

		EventPoint point = findPoint(x);
		if (point == null)
			p = defaultPaint;
		else 
			p = paints[point.getCluster()];

		return p;
	}

	public void setEventPoints(EventPoint[] points)
	{
		if (points != null && points.length > 0)
		{
			this.points = points.clone();;
			Arrays.sort(this.points);
		}
	}

	private EventPoint findPoint(double x)
	{
		EventPoint p = null;
		if (points != null)
		{
			for (EventPoint point : points)
			{
				if (x < point.getTimeStamp().getStart()) break;
				else if (x >= point.getTimeStamp().getStart() && x <= point.getTimeStamp().getEnd())
				{
					p = point;
					break;
				}
			}
		}

		return p;
	}
}
