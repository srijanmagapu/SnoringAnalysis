package gui.dbCreatorWindow;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FilenameFilter;

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

public class DBCreatorWindow extends JDialog implements TreeSelectionListener, MouseListener
{
	private static final long serialVersionUID = -6116927081135838672L;
	private final JPanel contentPanel = new JPanel();
	private JSplitPane splitPane;
	private JTable table;
	private FileTreePanel fileTreePanel;
	private JScrollPane fileSelectionScrollPanel;
	private DefaultTableModel fileSelectionTableModel;

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
		setBounds(100, 100, 611, 393);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));

		splitPane = new JSplitPane();
		splitPane.setDividerLocation(200);
		contentPanel.add(splitPane, BorderLayout.CENTER);

		fileSelectionScrollPanel = new JScrollPane();
		splitPane.setRightComponent(fileSelectionScrollPanel);

		table = new JTable();
		fileSelectionTableModel = new DefaultTableModel(new Object[][] {}, new String[] { "", "File" }) {
			private static final long serialVersionUID = 1329796869192942072L;

			@Override
		    public Class<?> getColumnClass(int columnIndex) {
		        return columnIndex == 0 ? Boolean.class : super.getColumnClass(columnIndex);
		    }
		};
		//fileSelectionTableModel = new DefaultTableModel(new Object[][] {}, new String[] { "", "File" });
		table.setModel(fileSelectionTableModel);
		table.getColumnModel().getColumn(0).setMaxWidth(30);
		fileSelectionScrollPanel.setViewportView(table);
		
		fileTreePanel = new FileTreePanel();
		fileTreePanel.setSelectionListenr(this);
		splitPane.setLeftComponent(fileTreePanel);

		JPanel controlPane = new JPanel();
		getContentPane().add(controlPane, BorderLayout.SOUTH);
		controlPane.setLayout(new BorderLayout(0, 0));

		JProgressBar progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		controlPane.add(progressBar, BorderLayout.SOUTH);

		JPanel buttonPanel = new JPanel();
		controlPane.add(buttonPanel, BorderLayout.EAST);
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JButton btnStart = new JButton("Start");
		buttonPanel.add(btnStart);

		JButton btnClose = new JButton("Close");
		btnClose.addMouseListener(this);
		buttonPanel.add(btnClose);

	}

	@Override
	public void valueChanged(TreeSelectionEvent e)
	{
		JTree tree = (JTree)e.getSource();
		FileTreeNode ftn = (FileTreeNode)(tree.getLastSelectedPathComponent());
		File[] wavFiles = ftn.getFile().listFiles(new FilenameFilter()
		{
			@Override
			public boolean accept(File dir, String name)
			{
				return name.toLowerCase().endsWith(".wav");
			}
		});

		fileSelectionTableModel.setRowCount(0);
		
		for(File f : wavFiles)
			fileSelectionTableModel.addRow(new Object[]{false, f.getName()});
	}
	
	public void mouseClicked(MouseEvent arg0)
	{
		this.dispose();
	}
	public void mouseEntered(MouseEvent arg0) {
	}
	public void mouseExited(MouseEvent arg0) {
	}
	public void mousePressed(MouseEvent arg0) {
	}
	public void mouseReleased(MouseEvent arg0) {
	}
}
