package gui.settingsWindow;

import gui.conrollers.ProgressBarController;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class SettingsWindow extends JDialog implements ActionListener
{
	private static final long serialVersionUID = -8120511484901187729L;
	private final JPanel contentPanel = new JPanel();
	private JButton saveButton;
	private JButton cancelButton;
	private SettingsPanel settingsPanel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{		
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
		}
		
		try
		{
			SettingsWindow dialog = new SettingsWindow(null, true);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public SettingsWindow(JFrame parent, boolean modal)
	{
		super(parent, modal);
		setName("Settings");
		setBounds(100, 100, 639, 408);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		
		settingsPanel = new SettingsPanel();
		contentPanel.add(settingsPanel, BorderLayout.CENTER);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		saveButton = new JButton("Save");
		saveButton.setActionCommand("OK");
		buttonPane.add(saveButton);
		getRootPane().setDefaultButton(saveButton);

		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);

	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if(source == cancelButton)
			this.dispose();
		
	}
}
