package gui;

import gui.interfaces.IPlaySoundSwitcher;
import gui.interfaces.ISourcePanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.AncestorEvent;

import controllers.IStartProcessingHandler;

public class SoundSourcePanel extends JPanel implements ISourcePanel, ActionListener, MouseListener
{
	private static final long serialVersionUID = -8976814604884193624L;
	private SoundSource DEFAULT_SOUNDSOURCE = SoundSource.File;
	private String DEFAULT_FOLDER = "sounds";

	private JTextField filePathTextField;
	private File chosenFile;
	private SoundSource soundSource = DEFAULT_SOUNDSOURCE;
	private JRadioButton rdbtnMic;
	private JRadioButton rdbtnFile;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JButton btnBrowse;
	private JButton btnStartStop;

	ArrayList<IStartProcessingHandler> startHandlers;
	ArrayList<IPlaySoundSwitcher> playSoundSwitcher;
	private JPanel leftPanel;
	private JPanel rightPanel;
	private JPanel centerPanel;
	private Component verticalStrut;
	private Component verticalStrut_1;
	private JCheckBox chckBoxPlaySound;

	/**
	 * Create the panel.
	 */
	public SoundSourcePanel()
	{
		setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Source", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		setLayout(new BorderLayout(0, 0));

		leftPanel = new JPanel();
		add(leftPanel, BorderLayout.WEST);

		rdbtnMic = new JRadioButton("Microphone");
		leftPanel.add(rdbtnMic);
		rdbtnMic.addActionListener(this);
		buttonGroup.add(rdbtnMic);

		rdbtnFile = new JRadioButton("File");
		leftPanel.add(rdbtnFile);
		rdbtnFile.addActionListener(this);
		rdbtnFile.setSelected(true);
		buttonGroup.add(rdbtnFile);

		btnBrowse = new JButton("Browse");
		leftPanel.add(btnBrowse);
		btnBrowse.addActionListener(this);
		btnBrowse.addMouseListener(this);

		rightPanel = new JPanel();
		add(rightPanel, BorderLayout.EAST);

		btnStartStop = new JButton("Start");
		rightPanel.add(btnStartStop);

		chckBoxPlaySound = new JCheckBox("Play sound");
		chckBoxPlaySound.addActionListener(this);
		rightPanel.add(chckBoxPlaySound);
		btnStartStop.addMouseListener(this);

		centerPanel = new JPanel();
		add(centerPanel, BorderLayout.CENTER);
		centerPanel.setLayout(new BorderLayout(0, 0));

		filePathTextField = new JTextField();
		centerPanel.add(filePathTextField);
		filePathTextField.setMaximumSize(new Dimension(2147483647, 20));
		filePathTextField.setEditable(false);
		filePathTextField.setColumns(10);

		verticalStrut = Box.createVerticalStrut(7);
		centerPanel.add(verticalStrut, BorderLayout.NORTH);

		verticalStrut_1 = Box.createVerticalStrut(7);
		centerPanel.add(verticalStrut_1, BorderLayout.SOUTH);
	}

	@Override
	public void registerStartStopHandler(IStartProcessingHandler startHandler)
	{
		if (startHandlers == null) startHandlers = new ArrayList<>();

		startHandlers.add(startHandler);
	}
	

	@Override
	public void registerPlaySoundSwither(IPlaySoundSwitcher switcher)
	{
		if(playSoundSwitcher == null)
			playSoundSwitcher = new ArrayList<>();
		
		playSoundSwitcher.add(switcher);
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
		else if(source == chckBoxPlaySound)
		{
			for(IPlaySoundSwitcher sw : playSoundSwitcher)
			{
				sw.switchSound(chckBoxPlaySound.isSelected());
			}
		}
	}

	public void mouseClicked(MouseEvent event)
	{
		JButton button = (JButton) event.getSource();
		// start/stop
		if (button == btnStartStop)
		{
			String btnText = button.getText();
			switch (btnText)
			{
			case "Start":
				if (startHandlers != null)
				{
					button.setText("Stop");
					for (IStartProcessingHandler handler : startHandlers)
						handler.startProcessing(getSoundSource(), getChosenFileName());

				}
				break;

			case "Stop":
				if (startHandlers != null)
				{
					button.setText("Start");
					for (IStartProcessingHandler handler : startHandlers)
						handler.stopProcessing();
				}
				break;
			}
		}
		// browse
		else if (button == btnBrowse)
		{
			final JFileChooser fc = new JFileChooser();
			fc.setCurrentDirectory(new File(Paths.get("").toAbsolutePath().toFile(), DEFAULT_FOLDER));
			// new java.awt.FileDialog((java.awt.Frame)null).setVisible(true);
			int returnVal = fc.showOpenDialog(event.getComponent());

			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				chosenFile = fc.getSelectedFile();
				filePathTextField.setText(fc.getSelectedFile().getAbsolutePath());
			}
		}
	}

	public void ancestorMoved(AncestorEvent event)
	{
	}

	public void ancestorRemoved(AncestorEvent event)
	{
	}

	public void componentHidden(ComponentEvent arg0)
	{
	}

	public void componentMoved(ComponentEvent arg0)
	{
	}

	public void componentShown(ComponentEvent arg0)
	{
	}

	public void mouseEntered(MouseEvent arg0)
	{
	}

	public void mouseExited(MouseEvent arg0)
	{
	}

	public void mousePressed(MouseEvent arg0)
	{
	}

	public void mouseReleased(MouseEvent arg0)
	{
	}

}
