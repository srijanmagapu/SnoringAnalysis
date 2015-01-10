package gui;

import gui.interfaces.IGraphsPanel;
import gui.interfaces.IMainFrame;
import gui.interfaces.IProgressBar;
import gui.interfaces.ISourcePanel;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.JProgressBar;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

import java.awt.CardLayout;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.ButtonGroup;

import org.eclipse.swt.widgets.ProgressBar;

import app.DispatchManager;

public class MainFrame extends JFrame implements ActionListener, IMainFrame, IProgressBar
{

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
		menuBar.add(mnFile);
		
		mntmOpen = new JMenuItem("Open");
		mnFile.add(mntmOpen);
		
		mntmClose = new JMenuItem("Close");
		mnFile.add(mntmClose);
		
		mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);
		
		mnView = new JMenu("View");
		menuBar.add(mnView);
		
		rdbtnmntmPatient = new JRadioButtonMenuItem("Patient");
		buttonGroup.add(rdbtnmntmPatient);
		mnView.add(rdbtnmntmPatient);
		
		rdbtnmntmResearcher = new JRadioButtonMenuItem("Researcher");
		buttonGroup.add(rdbtnmntmResearcher);
		mnView.add(rdbtnmntmResearcher);
		
		mnTools = new JMenu("Tools");
		menuBar.add(mnTools);
		
		mntmClusterring = new JMenuItem("Clusterring");
		mnTools.add(mntmClusterring);
		
		mntmPreferences = new JMenuItem("Preferences");
		mnTools.add(mntmPreferences);
		
		mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(this);
		mnHelp.add(mntmAbout);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
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

	public void actionPerformed(ActionEvent arg0) {
		About about = new About();
		about.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		about.setLocationRelativeTo(this);
		about.setVisible(true);
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
		return this;
	}
	
	@Override
	public void setProgressValue(int percent)
	{
		progressBar.setValue(percent);
	}
}
