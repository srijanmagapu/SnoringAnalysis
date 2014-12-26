package math;

/*
 * Part of the Java Image Processing Cookbook, please see
 * http://www.lac.inpe.br/JIPCookbook/index.jsp
 * for information on usage and distribution.
 * Rafael Santos (rafael.santos@lac.inpe.br)
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.TiledImage;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.Timer;

/**
 * This applications shows a rather complex usage of the
 * FuzzyCMeansImageClustering class, creating a complex user interface that
 * allows the user to select some clustering parameters and rerun the cluster
 * task. The original and clustered image are shown, side-by-side and
 * synchronized. Input data is read from a file and at the end of the task the
 * different-ordered clustering results can be displayed.
 */
public class FuzzyCMeansImageClusteringApp extends JFrame implements ActionListener
{
	// The input and output images.
	private PlanarImage input, fuzzy, output0, output1;
	// The display for the input and output images.
	private DisplayFourSynchronizedImages display;
	// The start (clustering) button.
	private JButton start;
	// A JSlider for the number of clusters.
	private JSlider numClustersSlider;
	// A JSlider of the fuzziness factor.
	private JSlider fuzzinessSlider;
	// The possible values for the fuzziness factor.
	private float[] fuzzinessValues = { 1, 1.2f, 1.5f, 2, 2.5f, 3, 5, 10, 25 };
	// A JSlider of the maximum number of iterations.
	private JSlider maxIterationsSlider;
	// The possible values for the maximum number of iterations.
	private int[] maxIterationsValues = { 2, 5, 10, 20, 50, 100, 200, 500, 1000 };
	// A JSlider of the final epsilon value.
	private JSlider epsilonSlider;
	// The possible values for the epsilon value.
	private float[] epsilonValues = { 1f, 0.1f, 0.01f, 0.001f, 0.0001f, 0.00001f };
	// A smaller font for the sliders.
	private Font labelsFont = new Font("Dialog", 0, 9);
	// A progress bar to indicate the state of the task.
	private JProgressBar progressBar;
	// A JLabel with information about the process.
	private JLabel infoLabel;
	// A timer, which will act on the user interface.
	private Timer monitor;
	// The clustering task instance.
	private FuzzyCMeansImageClustering task;

	/**
	 * The constructor for the application, which will set its user interface.
	 * 
	 * @param ifile
	 *            the input file name.
	 * @param ofile
	 *            the output file name.
	 */
	public FuzzyCMeansImageClusteringApp(String ifile)
	{
		super("Fuzzy C-Means Clustering Application");
		// Read the input image from the file.
		input = JAI.create("fileload", ifile);
		// Create compatible (empty) output images.
		fuzzy = new TiledImage(input, false);
		output0 = new TiledImage(input, false);
		output1 = new TiledImage(input, false);
		// Create an instance of DisplayTwoSynchronizedImages and add it to the
		// center of the JFrame.
		display = new DisplayFourSynchronizedImages(input, fuzzy, output0, output1);
		getContentPane().add(display, BorderLayout.CENTER);
		// Let's create the start button and set its maximum size so it will
		// appear wide enough in the control panel.
		start = new JButton("Start Clustering");
		start.addActionListener(this);
		start.setAlignmentX(Component.CENTER_ALIGNMENT);
		start.setMaximumSize(new Dimension(250, 25));
		// A JSlider for the number of clusters.
		numClustersSlider = new JSlider(2, 30, 6);
		Hashtable<Integer, JLabel> labels = new Hashtable<Integer, JLabel>();
		for (int label = 2; label <= 30; label += 4)
		{
			JLabel aLabel = new JLabel("" + label);
			aLabel.setFont(labelsFont);
			labels.put(new Integer(label), aLabel);
		}
		numClustersSlider.setMajorTickSpacing(4);
		numClustersSlider.setMinorTickSpacing(1);
		numClustersSlider.setSnapToTicks(true);
		numClustersSlider.setPaintTicks(true);
		numClustersSlider.setLabelTable(labels);
		numClustersSlider.setPaintLabels(true);
		numClustersSlider.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE), "Number of clusters"));
		// A JSlider for the fuzziness factor.
		fuzzinessSlider = new JSlider(0, fuzzinessValues.length - 1, 3);
		labels = new Hashtable<Integer, JLabel>();
		for (int label = 0; label < fuzzinessValues.length; label++)
		{
			JLabel aLabel = new JLabel("" + fuzzinessValues[label]);
			aLabel.setFont(labelsFont);
			labels.put(new Integer(label), aLabel);
		}
		fuzzinessSlider.setMajorTickSpacing(1);
		fuzzinessSlider.setSnapToTicks(true);
		fuzzinessSlider.setPaintTicks(true);
		fuzzinessSlider.setLabelTable(labels);
		fuzzinessSlider.setPaintLabels(true);
		fuzzinessSlider.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE), "Fuzziness"));
		// A JSlider for the maximum number of iterations.
		maxIterationsSlider = new JSlider(0, maxIterationsValues.length - 1, 5);
		labels = new Hashtable<Integer, JLabel>();
		for (int label = 0; label < maxIterationsValues.length; label++)
		{
			JLabel aLabel = new JLabel("" + maxIterationsValues[label]);
			aLabel.setFont(labelsFont);
			labels.put(new Integer(label), aLabel);
		}
		maxIterationsSlider.setMajorTickSpacing(1);
		maxIterationsSlider.setSnapToTicks(true);
		maxIterationsSlider.setPaintTicks(true);
		maxIterationsSlider.setLabelTable(labels);
		maxIterationsSlider.setPaintLabels(true);
		maxIterationsSlider.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE), "Max. iterations (approx.)"));
		// A JSlider for the epsilon values.
		epsilonSlider = new JSlider(0, epsilonValues.length - 1, 4);
		labels = new Hashtable<Integer, JLabel>();
		for (int label = 0; label < epsilonValues.length; label++)
		{
			JLabel aLabel = new JLabel("" + epsilonValues[label]);
			aLabel.setFont(labelsFont);
			labels.put(new Integer(label), aLabel);
		}
		epsilonSlider.setMajorTickSpacing(1);
		epsilonSlider.setSnapToTicks(true);
		epsilonSlider.setPaintTicks(true);
		epsilonSlider.setLabelTable(labels);
		epsilonSlider.setPaintLabels(true);
		epsilonSlider.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE), "Epsilon"));
		// A JProgressBar for the progress bar (duh!).
		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		// The information label.
		infoLabel = new JLabel(" ");
		infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		infoLabel.setPreferredSize(new Dimension(250, 25));
		// Add the control components to the control panel.
		Box controlPanel = Box.createVerticalBox();
		controlPanel.add(numClustersSlider);
		controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		controlPanel.add(fuzzinessSlider);
		controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		controlPanel.add(maxIterationsSlider);
		controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		controlPanel.add(epsilonSlider);
		controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		controlPanel.add(start);
		controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		controlPanel.add(progressBar);
		controlPanel.add(infoLabel);
		// Add the control panel to the content pane.
		getContentPane().add(controlPanel, BorderLayout.EAST);
		// Create a timer monitor, which will cause an ActionEvent every 2
		// seconds.
		monitor = new Timer(2000, this);
		// Set the closing operation so the application is finished.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack(); // Adjust the frame size using preferred dimensions.
		setVisible(true); // Show the frame.
	}

	/**
	 * This method will be called when an action event occurs. In this class, it
	 * either means that the user has clicked on the Start button or that the
	 * timer has fired.
	 */
	public void actionPerformed(ActionEvent e)
	{
		// If the user clicks on the start button...
		if (e.getSource() == start)
		{
			// Start the monitor, which will cause another action event every
			// two seconds.
			monitor.start();
			// Gets the clustering task arguments from the user interface.
			int nClusters = numClustersSlider.getValue();
			float fuzziness = fuzzinessValues[fuzzinessSlider.getValue()];
			int maxIter = maxIterationsValues[maxIterationsSlider.getValue()];
			float epsilon = epsilonValues[epsilonSlider.getValue()];
			// Create the clustering task and starts it.
			task = new FuzzyCMeansImageClustering(input, nClusters, maxIter, fuzziness, epsilon);
			progressBar.setMaximum(100); // maximum value is 100 percent.
			// Start the clustering task.
			task.start();
			// Turn "off" the Start button.
			start.setEnabled(false);
			start.setText("Clustering, please wait...");
			// Turn "off" some UI components.
			numClustersSlider.setEnabled(false);
			maxIterationsSlider.setEnabled(false);
			fuzzinessSlider.setEnabled(false);
			epsilonSlider.setEnabled(false);
		}
		// This will happen when the monitor fires.
		else if (e.getSource() == monitor)
		{
			// Change the images on the display to be the resulting images so
			// far.
			display.setImage2(task.getRankedMFImage(0));
			display.setImage3(task.getRankedImage(0));
			display.setImage4(task.getRankedImage(1));
			// Which percentage of the task is completed ?
			int percentage = (int) (100 * task.getPosition() / task.getSize());
			progressBar.setValue(percentage);
			// Update the information about the task.
			infoLabel.setText("Step " + task.getPosition() + " of " + task.getSize());
			// If the task has finished...
			if (task.isFinished())
			{
				// Enable the start button and the cluster number/max iterations
				// selection slider.
				start.setEnabled(true);
				start.setText("Start clustering");
				numClustersSlider.setEnabled(true);
				maxIterationsSlider.setEnabled(true);
				fuzzinessSlider.setEnabled(true);
				epsilonSlider.setEnabled(true);
				// Stops the monitor.
				monitor.stop();
				// This block stores the ranked MF image and/or the first and
				// second ranked image.
				try
				{
					ImageIO.write(task.getRankedMFImage(0), "PNG", new File("ranked0.png"));
					ImageIO.write(task.getRankedImage(0), "PNG", new File("res1.png"));
					ImageIO.write(task.getRankedImage(1), "PNG", new File("res2.png"));
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
				}
			}
		}
	}

	/**
	 * The application entry point, which will need an input image file name and
	 * an output image file name.
	 */
	public static void main(String[] args)
	{
		FuzzyCMeansImageClusteringApp a = new FuzzyCMeansImageClusteringApp(args[0]);
	}

}