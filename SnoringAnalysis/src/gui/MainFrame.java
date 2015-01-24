package gui;

import gui.conrollers.ProcessHandlerController;
import gui.conrollers.ProgressBarController;
import gui.graphs.FCMDialog;
import gui.interfaces.IGraphsPanel;
import gui.interfaces.IMainFrame;
import gui.interfaces.IProgressBar;
import gui.interfaces.ISourcePanel;
import gui.settingsWindow.SettingsWindow;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import model.EventPoint;
import model.FCMGraphData;
import controllers.IStartProcessingHandler;

public class MainFrame extends JFrame implements ActionListener, IMainFrame
{

	private static final long serialVersionUID = -3268929758235757177L;
	
	private JPanel contentPane;
	private JPanel mainPanel;
	private SoundSourcePanel soundSourcePanel;
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenu mnView;
	private JMenu mnTools;
	private JMenu mnHelp;
	private JMenuItem mntmAbout;
	private JMenuItem mntmOpen;
	private JMenuItem mntmClose;
	private JMenuItem mntmExit;
	private JRadioButtonMenuItem rdbtnmntmPatient;
	private JRadioButtonMenuItem rdbtnmntmResearcher;
	private JMenuItem mntmClusterring;
	private JMenuItem mntmPreferences;
	private GraphsPanel graphsPanel;
	private JPanel cardPanel;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JProgressBar progressBar;


	/**
	 * Create the frame.
	 */
	public MainFrame()
	{
		setIconImage(Toolkit.getDefaultToolkit().getImage(MainFrame.class.getResource("/gui/icon.png")));
		setTitle("Snoring Analyzer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 680, 470);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnFile = new JMenu("File");
		mnFile.setMnemonic('F');
		menuBar.add(mnFile);
		
		mntmOpen = new JMenuItem("Open");
		mnFile.add(mntmOpen);
		
		mntmClose = new JMenuItem("Close");
		mnFile.add(mntmClose);
		
		mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);
		
		mnView = new JMenu("View");
		mnView.setMnemonic('V');
		menuBar.add(mnView);
		
		rdbtnmntmPatient = new JRadioButtonMenuItem("Patient");
		rdbtnmntmPatient.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
		buttonGroup.add(rdbtnmntmPatient);
		mnView.add(rdbtnmntmPatient);
		
		rdbtnmntmResearcher = new JRadioButtonMenuItem("Researcher");
		rdbtnmntmResearcher.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
		buttonGroup.add(rdbtnmntmResearcher);
		mnView.add(rdbtnmntmResearcher);
		
		mnTools = new JMenu("Tools");
		mnTools.setMnemonic('T');
		menuBar.add(mnTools);
		
		mntmClusterring = new JMenuItem("Clusterring");
		mntmClusterring.addActionListener(this);
		mntmClusterring.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
		mnTools.add(mntmClusterring);
		
		mntmPreferences = new JMenuItem("Preferences");
		mntmPreferences.addActionListener(this);
		mntmPreferences.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mnTools.add(mntmPreferences);
		
		mnHelp = new JMenu("Help");
		mnHelp.setMnemonic('H');
		menuBar.add(mnHelp);
		
		mntmAbout = new JMenuItem("About");
		mntmAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
		mntmAbout.setActionCommand(mntmAbout.getText());
		mntmAbout.addActionListener(this);
		mnHelp.add(mntmAbout);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		ProgressBarController.getInstance().setProgressBar(progressBar);
		contentPane.add(progressBar, BorderLayout.SOUTH);
		
		mainPanel = new JPanel();
		contentPane.add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(new BorderLayout(0, 0));
		
		cardPanel = new JPanel();
		mainPanel.add(cardPanel, BorderLayout.CENTER);
		cardPanel.setLayout(new CardLayout(0, 0));
		
		graphsPanel = new GraphsPanel();
		cardPanel.add(graphsPanel, "name_9808530924142");
		
		soundSourcePanel = new SoundSourcePanel();
		mainPanel.add(soundSourcePanel, BorderLayout.NORTH);

	}

	public void actionPerformed(ActionEvent e) {
		
		JMenuItem button = (JMenuItem)e.getSource();
		String action = button.getActionCommand();
		
		if(action.equals(mntmPreferences.getText()))
		{
			SettingsWindow settingsWindow  = new SettingsWindow(this, true);
			settingsWindow.setVisible(true);
		}
		else if(action.equals(mntmAbout.getText()))
		{
			About about = new About();
			about.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			about.setLocationRelativeTo(this);
			about.setVisible(true);
		}
		else if(action.equals(mntmClusterring.getText()))
		{
			FCMDialog dialog = new FCMDialog();
			FCMGraphData data = FCMGraphData.getInstance();
			EventPoint[] centers = data.getCenters();
			if(centers.length == 3){
				dialog.setCenters(centers[0], centers[1], centers[2]);
				dialog.setGroups(data.getPointsFromCluster(0), data.getPointsFromCluster(1), data.getPointsFromCluster(2));
			}
			
			dialog.refreshGraph();
			dialog.setVisible(true);
		}
	}

	@Override
	public IGraphsPanel getIGraphsPanel()
	{
		return graphsPanel;
	}

	@Override
	public ISourcePanel getSourcePanel()
	{
		return soundSourcePanel;
	}

	@Override
	public IProgressBar getIProgressBar()
	{
		return ProgressBarController.getInstance();
	}

	@Override
	public void setStartStopProcessingHandler(IStartProcessingHandler handler)
	{
		ProcessHandlerController.setStartStopProcessingHandler(handler);
	}
	
	@Override
	public void dispose()
	{
		ProgressBarController.getInstance().removeProgressBar();
		super.dispose();
	}
}
