package gui.graphs;

import java.awt.BorderLayout;
import java.sql.Date;
import java.util.Random;

import gui.interfaces.IFCMDialog;

import javax.swing.JPanel;

import model.EventPoint;
import ChartDirector.Chart;
import ChartDirector.ChartViewer;
import ChartDirector.RanSeries;
import ChartDirector.ThreeDScatterChart;
import ChartDirector.ThreeDScatterGroup;

public class FCM3DScatterPanel extends JPanel implements IFCMDialog
{
	private static final long serialVersionUID = -5193067788203905059L;

	// colors
	private static final int breathingCenterColor = 0xff0000;	//red
	private static final int snoringCenterColor = 0x00ff00;		//green
	private static final int noiseCenterColor = 0x0000ff;		//blue
	
	private static final int breathingPointColor = 0xff0000;
	private static final int snoringPointColor = 0x00ff00;
	private static final int noisePointColor = 0x0000ff;
	
	// shapes
	private static final int breathingCenterShape = Chart.StarShape(5);
	private static final int snoringCenterShape = Chart.StarShape(5);
	private static final int noiseCenterShape = Chart.StarShape(5);
	
	private static final int breathingPointShape = Chart.GlassSphereShape;
	private static final int snoringPointShape = Chart.GlassSphereShape;
	private static final int noisePointShape = Chart.GlassSphereShape;
	
	// sizes
	private static final int breathingCenterSize = 20;
	private static final int snoringCenterSize = 20;
	private static final int noiseCenterSize = 20;
	
	private static final int breathingPointSize = 9;
	private static final int snoringPointSize = 9;
	private static final int noisePointSize = 9;
	
	
	ChartViewer viewer;
	ThreeDScatterChart chart;
	
	private EventPoint[] centers;
	private EventPoint[][] groups;
	
	public FCM3DScatterPanel()
	{
		setLayout(new BorderLayout(0, 0));
		
		centers = new EventPoint[3];
		groups = new EventPoint[3][];
		for(int i = 0; i < groups.length; i++)
			groups[i] = new EventPoint[0];
		
		init();
	}
	
	private void init()
	{
		chart = createChart();
		viewer = createViewer(chart);
		
        // Add viewer to the JPanel
        this.add(viewer, BorderLayout.CENTER);
        
	}
	
	private ThreeDScatterChart createChart()
	{
		// Create a ThreeDScatterChart object of size 800 x 520 pixels
		ThreeDScatterChart newChart = new ThreeDScatterChart(800, 520);
        
        // Add a title to the chart using 20 points Times New Roman Italic font
        newChart.addTitle("FCM 3D Scatter Plot                    ", "Times New Roman Italic",20);

        // Set the center of the plot region at (350, 240), and set width x depth x
        // height to 360 x 360 x 270 pixels
        newChart.setPlotRegion(350, 240, 360, 360, 270);

        // Set the elevation and rotation angles to 15 and 30 degrees
        newChart.setViewAngle(15, 30);
        
        // Add Legend
        newChart.addLegend(640, 180);
        
        return newChart;
	}
	
	private ChartViewer createViewer(ThreeDScatterChart chart)
	{
		// create new chart view
		ChartViewer newViewer = new ChartViewer();

		// Output the chart
		newViewer.setChart(chart);
		
		return newViewer;
	}

	@Override
	public void setCenters(EventPoint center1, EventPoint center2, EventPoint center3)
	{
		setCenter(center1);
		setCenter(center2);
		setCenter(center3);
	}

	@Override
	public void setCenter(EventPoint center)
	{
		centers[center.getCluster()] = center;
	}

	@Override
	public void setGroups(EventPoint[] group1, EventPoint[] group2, EventPoint[] group3)
	{
		setGroup(group1);
		setGroup(group2);
		setGroup(group3);
	}

	@Override
	public void setGroup(EventPoint[] group)
	{
		System.out.println("-------------------------------");
		System.out.println("groups size " + groups.length);
		System.out.println("group size " + group.length);
		if(group != null && group.length > 0)
			groups[group[0].getCluster()] = group;
	}

	@Override
	public void refreshGraph()
	{
		chart = createChart();
		for(int i = 0; i < centers.length; i++)
			addCenterToChart(chart, centers[i]);
		
		for(int i = 0; i < groups.length; i++)
			addGroupToChart(chart, groups[i]);
		
		this.remove(viewer);
		viewer = createViewer(chart);
		
        this.add(viewer, BorderLayout.CENTER);
        viewer.updateDisplay();
	}
	
	
	private void addCenterToChart(ThreeDScatterChart chart, EventPoint center)
	{
		double[] xData = new double[1];
		double[] yData = new double[1];
		double[] zData = new double[1];
		
		double[] pointCoord = center.getCoordinates();
		xData[0] = pointCoord[0];
		yData[0] = pointCoord[1];
		zData[0] = pointCoord[2];
		
		switch(center.getCluster())
		{
		case 0:
			chart.addScatterGroup(xData, yData, zData, "Center1", breathingCenterShape, breathingCenterSize, breathingCenterColor);
			break;
		case 1:
			chart.addScatterGroup(xData, yData, zData, "Center2", snoringCenterShape, snoringCenterSize, snoringCenterColor);
			break;
		case 2:
			chart.addScatterGroup(xData, yData, zData, "Center3", noiseCenterShape, noiseCenterSize, noiseCenterColor);
			break;
		}
	}
	
	
	private void addGroupToChart(ThreeDScatterChart chart, EventPoint[] group)
	{
		double[] xData = new double[group.length];
		double[] yData = new double[group.length];
		double[] zData = new double[group.length];
		
		for(int i = 0; i < group.length; i++)
		{
			double[] pointCoord = group[i].getCoordinates();
			xData[i] = pointCoord[0];
			yData[i] = pointCoord[1];
			zData[i] = pointCoord[2];
		}
		
		if(group != null && group.length > 0)
		{
			switch(group[0].getCluster())
			{
			case 0:
				chart.addScatterGroup(xData, yData, zData, "Cat1", breathingPointShape, breathingPointSize, breathingPointColor);
				break;
			case 1:
				chart.addScatterGroup(xData, yData, zData, "Cat2", snoringPointShape, snoringPointSize, snoringPointColor);
				break;
			case 2:
				chart.addScatterGroup(xData, yData, zData, "Cat3", noisePointShape, noisePointSize, noisePointColor);
				break;
			}
		}
	}

	/*********************************************************************
	 * 
	 * 
	 ********************************************************************/
	
	public void randomChart()
	{
		setCenters(createRundomPoint(0), createRundomPoint(1), createRundomPoint(2));
		setGroups(createRandomGroup(0, 50), createRandomGroup(1, 50), createRandomGroup(2, 50));
		refreshGraph();
		
		/*chart = createChart();
		addRandomPointsToChart(chart);
		viewer.setChart(chart);
		viewer.updateDisplay();*/
	}
	
	private EventPoint[] createRandomGroup(int cluster, int size)
	{
		EventPoint[] group = new EventPoint[size];
		for(int i = 0; i < size ; i++)
			group[i] = createRundomPoint(cluster);
		
		return group;
	}
	

	Random rand = new Random(System.currentTimeMillis());
	private EventPoint createRundomPoint(int cluster)
	{
		double[] coord = new double[3];
		
		coord[0] = rand.nextDouble() * 100;
		coord[1] = rand.nextDouble() * 100;
		coord[2] = rand.nextDouble() * 100;
		
		return new EventPoint(coord, cluster);
	}
	
	private void addRandomPointsToChart(ThreeDScatterChart chart)
	{
		// The random XYZ data for the first 3D scatter group
        RanSeries r0 = new RanSeries(7);
        double[] xData0 = r0.getSeries2(100, 100, -10, 10);
        double[] yData0 = r0.getSeries2(100, 0, 0, 20);
        double[] zData0 = r0.getSeries2(100, 100, -10, 10);

        // The random XYZ data for the second 3D scatter group
        RanSeries r1 = new RanSeries(4);
        double[] xData1 = r1.getSeries2(100, 100, -10, 10);
        double[] yData1 = r1.getSeries2(100, 0, 0, 20);
        double[] zData1 = r1.getSeries2(100, 100, -10, 10);

        // The random XYZ data for the third 3D scatter group
        RanSeries r2 = new RanSeries(8);
        double[] xData2 = r2.getSeries2(100, 100, -10, 10);
        double[] yData2 = r2.getSeries2(100, 0, 0, 20);
        double[] zData2 = r2.getSeries2(100, 100, -10, 10);


        // Add 3 scatter groups to the chart with 9 pixels glass sphere symbols of
        // red (ff0000), green (00ff00) and blue (0000ff) colors
        chart.addScatterGroup(xData0, yData0, zData0, "Alpha", Chart.GlassSphereShape, 9, 0x3ae705);
        chart.addScatterGroup(xData1, yData1, zData1, "Beta", Chart.StarShape(4), 9, 0x0564e7);
        chart.addScatterGroup(xData2, yData2, zData2, "Gamma", Chart.StarShape(5), 9, 0xff0040);
        
        chart.addLegend(640, 180);
	}

}
