package gui.settingsWindow;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class SettingsWindow extends JDialog
{
	private static final long serialVersionUID = -8120511484901187729L;
	private final JPanel contentPanel = new JPanel();
	private JButton saveButton;
	private JButton cancelButton;

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
			SettingsWindow dialog = new SettingsWindow();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			//dialog.pack();
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
	public SettingsWindow()
	{
		setBounds(100, 100, 639, 408);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		
		SettingsPanel settingsPanel = new SettingsPanel();
		contentPanel.add(settingsPanel, BorderLayout.CENTER);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		saveButton = new JButton("Save");
		saveButton.setActionCommand("OK");
		buttonPane.add(saveButton);
		getRootPane().setDefaultButton(saveButton);

		cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);

	}

}
