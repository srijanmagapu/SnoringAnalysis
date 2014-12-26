package gui;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JButton;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.awt.event.ComponentEvent;
import javax.swing.ButtonGroup;
import java.awt.event.MouseListener;
import javax.swing.event.AncestorListener;
import javax.swing.event.AncestorEvent;
import java.awt.event.ComponentListener;

public class SoundSourcePanel extends JPanel implements AncestorListener, ComponentListener, ActionListener, MouseListener
{
	private JTextField filePathTextField;
	private File chosenFile;
	private SoundSource soundSource;
	private JRadioButton rdbtnMic;
	private JRadioButton rdbtnFile;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JButton btnBrowse;

	/**
	 * Create the panel.
	 */
	public SoundSourcePanel()
	{
		addComponentListener(this);
		addAncestorListener(this);

		setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Source", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		rdbtnMic = new JRadioButton("Microphone");
		rdbtnMic.addActionListener(this);
		buttonGroup.add(rdbtnMic);
		add(rdbtnMic);

		rdbtnFile = new JRadioButton("File");
		rdbtnFile.addActionListener(this);
		rdbtnFile.setSelected(true);
		buttonGroup.add(rdbtnFile);
		add(rdbtnFile);

		btnBrowse = new JButton("Browse");
		btnBrowse.addMouseListener(this);

		add(btnBrowse);

		filePathTextField = new JTextField();
		filePathTextField.setEditable(false);
		add(filePathTextField);
		filePathTextField.setColumns(10);
	}

	public void resetChosenFile()
	{
		chosenFile = null;
		filePathTextField.setText("");
	}

	public File getChosenFile()
	{
		return this.chosenFile;
	}

	public String getChosenFileName()
	{
		return this.chosenFile.getPath();
	}

	public SoundSource getSoundSource()
	{
		return this.soundSource;
	}

	public enum SoundSource
	{
		File, Mic
	}

	public void ancestorAdded(AncestorEvent event)
	{
		filePathTextField.setSize((int) event.getComponent().getWidth() - filePathTextField.getX() - 20, filePathTextField.getHeight());
	}

	public void componentResized(ComponentEvent arg0)
	{
		filePathTextField.setSize((int) arg0.getComponent().getWidth() - filePathTextField.getX() - 20, filePathTextField.getHeight());
	}

	public void actionPerformed(ActionEvent e)
	{
		Object source = e.getSource();
		// Mic
		if (source == rdbtnMic)
		{
			soundSource = SoundSource.Mic;
			btnBrowse.setEnabled(false);
			btnBrowse.removeMouseListener(this);
			chosenFile = null;
			
		}
		// File
		else if (source == rdbtnFile)
		{
			soundSource = SoundSource.File;
			btnBrowse.setEnabled(true);
			btnBrowse.addMouseListener(this);
			if (filePathTextField.getText() != "" || filePathTextField.getText() != null)
			{
				try
				{
					chosenFile = new File(filePathTextField.getText());
				}
				catch (Exception ex)
				{
					System.err.println("Cant open file" + filePathTextField.getText());
				}
			}
		}
	}

	public void mouseClicked(MouseEvent arg0)
	{
		final JFileChooser fc = new JFileChooser();
		// new java.awt.FileDialog((java.awt.Frame)
		// null).setVisible(true);
		int returnVal = fc.showOpenDialog(arg0.getComponent());

		if (returnVal == JFileChooser.APPROVE_OPTION)
		{
			chosenFile = fc.getSelectedFile();
			filePathTextField.setText(fc.getSelectedFile().getAbsolutePath());
		}
	}

	public void ancestorMoved(AncestorEvent event){}
	public void ancestorRemoved(AncestorEvent event){}
	public void componentHidden(ComponentEvent arg0){}
	public void componentMoved(ComponentEvent arg0){}
	public void componentShown(ComponentEvent arg0){}
	public void mouseEntered(MouseEvent arg0){}
	public void mouseExited(MouseEvent arg0){}
	public void mousePressed(MouseEvent arg0){}
	public void mouseReleased(MouseEvent arg0){}
}
