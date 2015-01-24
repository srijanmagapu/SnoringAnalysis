package gui.dbCreatorWindow;

import gui.conrollers.ProcessHandlerController;
import gui.conrollers.ProgressBarController;
import gui.interfaces.ISourcePanel.SoundSource;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;

import controllers.IStartProcessingHandler;

public class DBCreatorWindow extends JDialog implements TreeSelectionListener, ActionListener
{
	private static final long serialVersionUID = -6116927081135838672L;
	private final JPanel contentPanel = new JPanel();
	private JSplitPane splitPane;
	private JTable table;
	private FileTreePanel fileTreePanel;
	private JScrollPane fileSelectionPanel;
	private DefaultTableModel fileSelectionTableModel;
	private JButton btnStart;
	private JButton btnClose;
	private JProgressBar progressBar;
	private Map<String, String> fileMap;

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
			DBCreatorWindow dialog = new DBCreatorWindow();
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
	public DBCreatorWindow()
	{
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Create Data Base");
		this.setModal(true);
		setBounds(100, 100, 611, 393);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));

		splitPane = new JSplitPane();
		splitPane.setDividerLocation(200);
		contentPanel.add(splitPane, BorderLayout.CENTER);

		fileSelectionPanel = new JScrollPane();
		splitPane.setRightComponent(fileSelectionPanel);

		table = new JTable();
		fileSelectionTableModel = new DefaultTableModel(new Object[][] {}, new String[] { "", "File" })
		{
			private static final long serialVersionUID = 1329796869192942072L;

			@Override
			public Class<?> getColumnClass(int columnIndex)
			{
				return columnIndex == 0 ? Boolean.class : super.getColumnClass(columnIndex);
			}
			
			@Override
			public boolean isCellEditable(int row, int col)
			{
				if(col == 1)
					return false;
				else
					return true;
			}
		};

		table.setModel(fileSelectionTableModel);
		table.getColumnModel().getColumn(0).setMaxWidth(30);
		fileSelectionPanel.setViewportView(table);

		fileTreePanel = new FileTreePanel();
		fileTreePanel.setSelectionListenr(this);
		splitPane.setLeftComponent(fileTreePanel);

		JPanel controlPane = new JPanel();
		getContentPane().add(controlPane, BorderLayout.SOUTH);
		controlPane.setLayout(new BorderLayout(0, 0));

		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		ProgressBarController.getInstance().setProgressBar(progressBar);
		controlPane.add(progressBar, BorderLayout.SOUTH);

		JPanel buttonPanel = new JPanel();
		controlPane.add(buttonPanel, BorderLayout.EAST);
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		btnStart = new JButton("Start");
		btnStart.addActionListener(this);
		buttonPanel.add(btnStart);

		btnClose = new JButton("Close");
		btnClose.addActionListener(this);
		buttonPanel.add(btnClose);

	}

	@Override
	public void valueChanged(TreeSelectionEvent e)
	{
		JTree tree = (JTree) e.getSource();
		FileTreeNode ftn = (FileTreeNode) (tree.getLastSelectedPathComponent());
		File[] wavFiles = ftn.getFile().listFiles(new FilenameFilter()
		{
			@Override
			public boolean accept(File dir, String name)
			{
				return name.toLowerCase().endsWith(".wav");
			}
		});

		fileSelectionTableModel.setRowCount(0);
		fileMap = new Hashtable<>();
		for (File f : wavFiles)
		{
			fileSelectionTableModel.addRow(new Object[] { false, f.getName() });
			fileMap.put(f.getName(), f.toString());
		}
	}

	public void actionPerformed(ActionEvent e)
	{
		JButton source = null;
		if (e.getSource() instanceof JButton)
		{
			source = (JButton) e.getSource();

			if (source == btnStart)
			{
				switch(source.getText())
				{
				case "Start":
					source.setText("Stop");
					sendFilesToProcessing();
					break;
					
				case "Stop":
					source.setText("Start");
					stopProcessing();
					break;
				}
				
			}
			else if (source == btnClose)
			{
				this.dispose();
			}
		}
	}

	private void sendFilesToProcessing()
	{
		IStartProcessingHandler handler = ProcessHandlerController.getStartStopProcessingHandler();
		if (handler != null)
		{
			ArrayList<String> selectedFiles = new ArrayList<>();
			@SuppressWarnings("unchecked")
			Vector<Vector<Object>> data = fileSelectionTableModel.getDataVector();
			for (Vector<Object> row : data)
			{
				if ((boolean) row.elementAt(0) == true)
					selectedFiles.add(fileMap.get((String) row.elementAt(1)));
			}

			String[] pathes = new String[selectedFiles.size()];
			pathes = selectedFiles.toArray(new String[0]);
			
			handler.startProcessing(SoundSource.File, pathes);
		}
	}
	
	private void stopProcessing()
	{
		IStartProcessingHandler handler = ProcessHandlerController.getStartStopProcessingHandler();
		if (handler != null)
			handler.stopProcessing();
	}

	@Override
	public void dispose()
	{
		ProgressBarController.getInstance().removeProgressBar();
		super.dispose();
	}

}
