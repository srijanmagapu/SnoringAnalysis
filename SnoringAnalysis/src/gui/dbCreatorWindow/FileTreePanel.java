package gui.dbCreatorWindow;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.File;
import java.io.FileFilter;
import java.util.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreeSelectionModel;

/**
 * @author Kirill Grouchnikov
 */
public class FileTreePanel extends JPanel
{
	/**
	 * File system view.
	 */
	protected static FileSystemView fsv = FileSystemView.getFileSystemView();

	/**
	 * Renderer for the file tree.
	 * 
	 * @author Kirill Grouchnikov
	 */
	private static class FileTreeCellRenderer extends DefaultTreeCellRenderer
	{
		/**
		 * Icon cache to speed the rendering.
		 */
		private Map<String, Icon> iconCache = new HashMap<String, Icon>();

		/**
		 * Root name cache to speed the rendering.
		 */
		private Map<File, String> rootNameCache = new HashMap<File, String>();

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * javax.swing.tree.DefaultTreeCellRenderer#getTreeCellRendererComponent
		 * (javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int,
		 * boolean)
		 */
		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
		{
			/*if(leaf)
				return null;*/
			
			FileTreeNode ftn = (FileTreeNode) value;
			File file = ftn.getFile();
			String filename = "";
			if (file != null)
			{
				if (ftn.isFileSystemRoot())
				{
					// long start = System.currentTimeMillis();
					filename = this.rootNameCache.get(file);
					if (filename == null)
					{
						filename = fsv.getSystemDisplayName(file);
						this.rootNameCache.put(file, filename);
					}
					// long end = System.currentTimeMillis();
					// System.out.println(filename + ":" + (end - start));
				}
				else
				{
					filename = file.getName();
				}
			}
			
			JLabel result = (JLabel) super.getTreeCellRendererComponent(tree, filename, sel, expanded, leaf, row, hasFocus);
			if (file != null)
			{
				Icon icon = this.iconCache.get(filename);
				if (icon == null)
				{
					// System.out.println("Getting icon of " + filename);
					icon = fsv.getSystemIcon(file);
					this.iconCache.put(filename, icon);
				}
				result.setIcon(icon);
			}
			return result;
		}
	}

	

	/**
	 * The file tree.
	 */
	private JTree tree;

	/**
	 * Creates the file tree panel.
	 */
	public FileTreePanel()
	{
		this.setLayout(new BorderLayout());
		File[] roots = File.listRoots();
		FileTreeNode rootTreeNode = new FileTreeNode(roots);
		this.tree = new JTree(rootTreeNode);
		this.tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		this.tree.setCellRenderer(new FileTreeCellRenderer());
		this.tree.setRootVisible(false);
		final JScrollPane jsp = new JScrollPane(this.tree);
		jsp.setBorder(new EmptyBorder(0, 0, 0, 0));
		this.add(jsp, BorderLayout.CENTER);
	}
	
	public void setSelectionListenr(TreeSelectionListener listener)
	{
		tree.addTreeSelectionListener(listener);
	}

/*	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				JFrame frame = new JFrame("File tree");
				frame.setSize(500, 400);
				frame.setLocationRelativeTo(null);
				frame.add(new FileTreePanel());
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
		});
	}*/
}
