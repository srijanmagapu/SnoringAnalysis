package gui.graphs;

import gui.interfaces.IFCMDialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;

import model.EventPoint;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class FCMDialog extends JDialog implements IFCMDialog, ActionListener
{
	private static final long serialVersionUID = 1475528859878123947L;
	
	private final JPanel contentPanel = new JPanel();
	private FCM3DScatterPanel m3DScatterPanel;
	private JButton randomButton;
	private JButton cancelButton;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			FCMDialog dialog = new FCMDialog();
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
	public FCMDialog()
	{
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));

		m3DScatterPanel = new FCM3DScatterPanel();
		contentPanel.add(m3DScatterPanel);
		m3DScatterPanel.setLayout(new BoxLayout(m3DScatterPanel, BoxLayout.X_AXIS));

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		randomButton = new JButton("Random");
		randomButton.addActionListener(this);
		randomButton.setActionCommand("random");
		buttonPane.add(randomButton);
		getRootPane().setDefaultButton(randomButton);

		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);

		this.pack();
	}


	public void actionPerformed(ActionEvent e) {
		
		Object source = e.getSource();
		if(source == randomButton)
		{
			m3DScatterPanel.randomChart();
			this.revalidate();
			this.repaint();
		}
		else if(source == cancelButton)
		{
			this.dispose();
		}
	}

	@Override
	public void setCenters(EventPoint center1, EventPoint center2, EventPoint center3)
	{
		m3DScatterPanel.setCenters(center1, center2, center3);
	}

	@Override
	public void setCenter(EventPoint center)
	{
		m3DScatterPanel.setCenter(center);
	}

	@Override
	public void setGroups(EventPoint[] group1, EventPoint[] group2, EventPoint[] group3)
	{
		m3DScatterPanel.setGroups(group1, group2, group3);
	}

	@Override
	public void setGroup(EventPoint[] group)
	{
		m3DScatterPanel.setGroup(group);
	}


	@Override
	public void refreshGraph()
	{
		m3DScatterPanel.refreshGraph();
	}
}
