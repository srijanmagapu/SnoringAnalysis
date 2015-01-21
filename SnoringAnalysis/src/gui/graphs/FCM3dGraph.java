package gui.graphs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jzy3d.chart.Chart;
import org.jzy3d.chart.Settings;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.chart.factories.IChartComponentFactory.Toolkit;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Scatter;
import org.jzy3d.plot3d.rendering.canvas.Quality;

public class FCM3dGraph extends JPanel
{
	private static final long serialVersionUID = 2717233264552263693L;
	private Coord3d[] points;
	private Color[] colors;
	private Scatter scatter;
	private Chart chart;


	public FCM3dGraph() {
		this.setPreferredSize(new Dimension(800, 600));
		setLayout(new BorderLayout(0, 0));
        
        scatter = new Scatter();
        
        /*int size = 5000;
		float x;
		float y;
		float z;
		float a;

		points = new Coord3d[size];
		colors = new Color[size];
		
		for (int i = 0; i < size; i++)
		{
			x = (float) Math.random() - 0.5f;
			y = (float) Math.random() - 0.5f;
			z = (float) Math.random() - 0.5f;
			points[i] = new Coord3d(x, y, z);
			a = 0.25f;
			colors[i] = new Color(x, y, z, a);
		}

		scatter.setData(points);
		scatter.setColors(colors);
        */
        chart = AWTChartComponentFactory.chart(Quality.Advanced, Toolkit.newt.toString());
        chart.getScene().add(scatter);
        
        this.add((Component)chart.getCanvas(), BorderLayout.CENTER);
	}

	private void init()
	{
		int size = 50;
		float x;
		float y;
		float z;
		float a;

		points = new Coord3d[size];
		colors = new Color[size];
		
		for (int i = 0; i < size; i++)
		{
			x = (float) Math.random() - 0.5f;
			y = (float) Math.random() - 0.5f;
			z = (float) Math.random() - 0.5f;
			points[i] = new Coord3d(x, y, z);
			a = 0.25f;
			colors[i] = new Color(x, y, z, a);
		}

		scatter.setData(points);
		scatter.setColors(colors);
	}
	
	public static void main(String[] arg)
	{
		Settings.getInstance().setHardwareAccelerated(true);
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setBounds(100, 100, 680, 470);
        
        FCM3dGraph graph = new FCM3dGraph();
        graph.init();
        //graph.setSize(frame.getSize());
        //graph.revalidate();
        //frame.setContentPane(graph);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(graph, BorderLayout.CENTER);
        
        frame.setContentPane(panel);
        frame.pack();
        System.out.println(graph.getSize());
        frame.setVisible(true);
    }
}
