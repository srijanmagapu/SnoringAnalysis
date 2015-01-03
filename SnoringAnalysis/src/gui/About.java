package gui;

import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.Color;
import javax.swing.ImageIcon;
import java.awt.Toolkit;
import java.awt.Dialog.ModalityType;

public class About extends JDialog
{
	private JLabel lblSnoringAnalyzer;
	private JLabel lblVersion;
	private JLabel lblDevelopedBy;
	private JLabel lblNirBarZvi;
	private JLabel lblGordashnikovArtur;
	private JLabel lblSupervisor;
	private JLabel lblAlexFrid;
	private JLabel lblBraude;
	private JLabel lblIcon;

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
					About dialog = new About();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the dialog.
	 */
	public About()
	{
		setModalityType(ModalityType.APPLICATION_MODAL);
		setModal(true);
		setTitle("About");
		setIconImage(Toolkit.getDefaultToolkit().getImage(About.class.getResource("/gui/icon.png")));
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(null);
		
		lblSnoringAnalyzer = new JLabel("Snoring Analyzer");
		lblSnoringAnalyzer.setForeground(new Color(0, 0, 255));
		lblSnoringAnalyzer.setHorizontalAlignment(SwingConstants.CENTER);
		lblSnoringAnalyzer.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblSnoringAnalyzer.setBounds(130, 29, 173, 31);
		getContentPane().add(lblSnoringAnalyzer);
		
		lblVersion = new JLabel("Version 1.0");
		lblVersion.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblVersion.setHorizontalAlignment(SwingConstants.CENTER);
		lblVersion.setBounds(180, 71, 69, 14);
		getContentPane().add(lblVersion);
		
		lblDevelopedBy = new JLabel("Developed by:");
		lblDevelopedBy.setFont(new Font("Dialog", Font.PLAIN, 15));
		lblDevelopedBy.setBounds(51, 153, 98, 20);
		getContentPane().add(lblDevelopedBy);
		
		lblNirBarZvi = new JLabel("Nir Bar Zvi");
		lblNirBarZvi.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNirBarZvi.setBounds(81, 184, 76, 14);
		getContentPane().add(lblNirBarZvi);
		
		lblGordashnikovArtur = new JLabel("Gordashnikov Artur");
		lblGordashnikovArtur.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblGordashnikovArtur.setBounds(81, 209, 138, 14);
		getContentPane().add(lblGordashnikovArtur);
		
		lblSupervisor = new JLabel("Supervisor:");
		lblSupervisor.setFont(new Font("Dialog", Font.PLAIN, 15));
		lblSupervisor.setBounds(264, 153, 84, 20);
		getContentPane().add(lblSupervisor);
		
		lblAlexFrid = new JLabel("Alex Frid");
		lblAlexFrid.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblAlexFrid.setBounds(294, 178, 74, 14);
		getContentPane().add(lblAlexFrid);
		
		lblBraude = new JLabel("");
		lblBraude.setIcon(new ImageIcon(About.class.getResource("/gui/braude.png")));
		lblBraude.setHorizontalAlignment(SwingConstants.CENTER);
		lblBraude.setBounds(10, 10, 100, 90);
		getContentPane().add(lblBraude);
		
		lblIcon = new JLabel("");
		lblIcon.setHorizontalAlignment(SwingConstants.CENTER);
		lblIcon.setIcon(new ImageIcon(About.class.getResource("/gui/icon.png")));
		lblIcon.setBounds(324, 10, 100, 100);
		getContentPane().add(lblIcon);

	}
}
