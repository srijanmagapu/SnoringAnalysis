package gui.settingsWindow;

import gui.dbCreatorWindow.DBCreatorWindow;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

public class SettingsPanel extends JPanel implements MouseListener
{
	private static final long serialVersionUID = -3585563240704105519L;
	private JPanel panel;
	private JLabel lblDataBaseFile;
	private JTextField dbPath;
	private JButton btnBrawse;
	private JButton btnCreateDb;

	/**
	 * Create the panel.
	 */
	public SettingsPanel()
	{
		setLayout(null);
		
		panel = new JPanel();
		panel.setToolTipText("");
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Training Data", TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLUE));
		panel.setBounds(10, 11, 591, 207);
		add(panel);
		panel.setLayout(null);
		
		lblDataBaseFile = new JLabel("Data base file:");
		lblDataBaseFile.setBounds(27, 38, 71, 14);
		panel.add(lblDataBaseFile);
		
		dbPath = new JTextField();
		dbPath.setToolTipText("");
		dbPath.setBounds(108, 35, 374, 20);
		panel.add(dbPath);
		dbPath.setColumns(10);
		
		btnBrawse = new JButton("Brawse");
		btnBrawse.setBounds(492, 34, 89, 23);
		panel.add(btnBrawse);
		
		btnCreateDb = new JButton("Create DB");
		btnCreateDb.addMouseListener(this);
		btnCreateDb.setBounds(492, 68, 89, 23);
		panel.add(btnCreateDb);

	}
	public void mouseClicked(MouseEvent e) {
		JButton button = (JButton)e.getSource();
		
		if(button == btnCreateDb)
		{
			DBCreatorWindow dbCreator = new DBCreatorWindow();
			dbCreator.setVisible(true);
		}
	}
	public void mouseEntered(MouseEvent e) {
	}
	public void mouseExited(MouseEvent e) {
	}
	public void mousePressed(MouseEvent e) {
	}
	public void mouseReleased(MouseEvent e) {
	}
}
