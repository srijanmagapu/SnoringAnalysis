package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.FlowLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.JRadioButton;
import javax.swing.JButton;
import java.awt.CardLayout;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import java.awt.Color;
import javax.swing.UIManager;
import javax.swing.JLabel;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.MatteBorder;

public class MainWindow
{

	private JFrame frmSnoringAnalyzer;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					MainWindow window = new MainWindow();
					window.frmSnoringAnalyzer.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow()
	{
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frmSnoringAnalyzer = new JFrame();
		frmSnoringAnalyzer.setResizable(false);
		frmSnoringAnalyzer.setTitle("Snoring Analyzer");
		frmSnoringAnalyzer.setBounds(100, 100, 680, 608);
		frmSnoringAnalyzer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSnoringAnalyzer.getContentPane().setLayout(null);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 674, 21);
		frmSnoringAnalyzer.getContentPane().add(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmOpenFile = new JMenuItem("Open file...");
		mnFile.add(mntmOpenFile);
		
		JMenuItem mntmCloseFile = new JMenuItem("Close file");
		mnFile.add(mntmCloseFile);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);
		
		JMenu mnView = new JMenu("View");
		menuBar.add(mnView);
		
		JRadioButtonMenuItem rdbtnmntmFile = new JRadioButtonMenuItem("Frame signal analysis");
		mnView.add(rdbtnmntmFile);
		
		JRadioButtonMenuItem rdbtnmntmMic = new JRadioButtonMenuItem("Snore detection");
		mnView.add(rdbtnmntmMic);
		
		JMenu mnTools = new JMenu("Tools");
		menuBar.add(mnTools);
		
		JMenuItem mntmFcm = new JMenuItem("FCM");
		mnTools.add(mntmFcm);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mnHelp.add(mntmAbout);
		
		JPanel sourcePanel = new JPanel();
		sourcePanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Source", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 255)));
		sourcePanel.setBounds(10, 32, 654, 53);
		frmSnoringAnalyzer.getContentPane().add(sourcePanel);
		sourcePanel.setLayout(null);
		
		JRadioButton defaultMicRB = new JRadioButton("Default Microphone");
		defaultMicRB.setBounds(16, 15, 119, 23);
		sourcePanel.add(defaultMicRB);
		
		JRadioButton fileRB = new JRadioButton("File");
		fileRB.setBounds(156, 15, 55, 23);
		sourcePanel.add(fileRB);
		
		JButton btnBrowse = new JButton("Browse...");
		btnBrowse.setBounds(213, 15, 95, 23);
		sourcePanel.add(btnBrowse);
		
		JLabel sourceFilePathLbl = new JLabel("C:\\record1.wav");
		sourceFilePathLbl.setBounds(318, 19, 326, 14);
		sourcePanel.add(sourceFilePathLbl);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Snore Detection", TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLUE));
		panel.setBounds(10, 96, 654, 472);
		frmSnoringAnalyzer.getContentPane().add(panel);
		panel.setLayout(null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_1.setBounds(10, 22, 634, 135);
		panel.add(panel_1);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_2.setBounds(10, 168, 634, 39);
		panel.add(panel_2);
	}
}
