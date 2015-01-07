package gui;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

public class SWTFileOpenSnippet
{

	public static void main(String[] args)
	{
		Display display = new Display();
		Shell shell = new Shell(display);
		// Don't show the shell.
		// shell.open ();
		FileDialog dialog = new FileDialog(shell, SWT.OPEN | SWT.MULTI);
		String[] filterNames = new String[] { "All Files (*)" };
		String[] filterExtensions = new String[] { "*" };
		String filterPath = "c:\\";
		dialog.setFilterNames(filterNames);
		dialog.setFilterExtensions(filterExtensions);
		dialog.setFilterPath(filterPath);
		dialog.open();
		System.out.println("Selected files: ");
		String[] selectedFileNames = dialog.getFileNames();
		for (String fileName : selectedFileNames)
		{
			System.out.println("  " + fileName);
		}
		shell.close();
		while (!shell.isDisposed())
		{
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
	}
}
