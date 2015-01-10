package gui;

import gui.interfaces.ISourcePanel;

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
import java.nio.file.Paths;
import java.util.ArrayList;
import java.awt.event.ComponentEvent;

import javax.swing.ButtonGroup;

import java.awt.event.MouseListener;

import javax.swing.event.AncestorListener;
import javax.swing.event.AncestorEvent;

import java.awt.event.ComponentListener;

import javax.swing.Box;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

import controllers.IStartProcessingHandler;

import java.awt.Component;

public class SoundSourcePanel extends JPanel implements ISourcePanel, AncestorListener, ComponentListener, ActionListener, MouseListener
{
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
	private Box horizontalBox;
	private Component horizontalStrut;
	private Component horizontalStrut_1;

	ArrayList<IStartProcessingHandler> startHandlers;

	/**
	 * Create the panel.
	 */
	public SoundSourcePanel()
	{
		addComponentListener(this);
		addAncestorListener(this);

		setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Source", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));

		rdbtnMic = new JRadioButton("Microphone");
		rdbtnMic.addActionListener(this);
		buttonGroup.add(rdbtnMic);

		rdbtnFile = new JRadioButton("File");
		rdbtnFile.addActionListener(this);
		rdbtnFile.setSelected(true);
		buttonGroup.add(rdbtnFile);

		horizontalBox = Box.createHorizontalBox();

		btnBrowse = new JButton("Browse");
		btnBrowse.addActionListener(this);
		horizontalBox.add(btnBrowse);
		btnBrowse.addMouseListener(this);

		horizontalStrut = Box.createHorizontalStrut(10);
		horizontalBox.add(horizontalStrut);

		filePathTextField = new JTextField();
		horizontalBox.add(filePathTextField);
		filePathTextField.setEditable(false);
		filePathTextField.setColumns(10);

		horizontalStrut_1 = Box.createHorizontalStrut(20);
		horizontalBox.add(horizontalStrut_1);

		btnStartStop = new JButton("Start");
		btnStartStop.addMouseListener(this);
		horizontalBox.add(btnStartStop);
		setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		add(rdbtnMic);
		add(rdbtnFile);
		add(horizontalBox);
	}

	@Override
	public void registerStartStopHandler(IStartProcessingHandler startHandler)
	{
		if (startHandlers == null)
			startHandlers = new ArrayList<>();

		startHandlers.add(startHandler);
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

	public void ancestorAdded(AncestorEvent event)
	{
		horizontalBox.setSize((int) event.getComponent().getWidth() - horizontalBox.getX() - 20, horizontalBox.getHeight());
	}

	public void componentResized(ComponentEvent arg0)
	{
		horizontalBox.setSize((int) arg0.getComponent().getWidth() - horizontalBox.getX() - 20, horizontalBox.getHeight());
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

	public void mouseClicked(MouseEvent event)
	{
		JButton button = (JButton) event.getSource();
		//start/stop
		if (button == btnStartStop)
		{
			String btnText = button.getText();
			switch (btnText)
			{
			case "Start":
				if (startHandlers != null){
					button.setText("Stop");
					for (IStartProcessingHandler handler : startHandlers)
						handler.startProcessing(getSoundSource(), getChosenFileName());
					
				}
				break;

			case "Stop":
				if (startHandlers != null){
					button.setText("Start");
					for (IStartProcessingHandler handler : startHandlers)
						handler.stopProcessing();
				}
				break;
			}
		}
		//browse
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
