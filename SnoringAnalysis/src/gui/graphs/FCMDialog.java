package gui.graphs;

import gui.interfaces.IFCMDialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.EventPoint;

public class FCMDialog extends JDialog implements IFCMDialog, ActionListener, ChangeListener
{
	private static final long serialVersionUID = 1475528859878123947L;

	private final JPanel contentPanel = new JPanel();
	private FCM3DScatterPanel m3DScatterPanel;
	private JButton randomButton;
	private JButton cancelButton;
	private JButton btnReset;
	private JPanel buttonPanel;
	private JSlider horizontSlider;
	private JSlider verticalSlider;
	private JPanel dControlPanel;
	private JPanel rControlPanel;
	private JCheckBox chckbxCat1;
	private JCheckBox chckbxCat2;
	private JCheckBox chckbxCat3;
	private JPanel chckPanel;
	private Component horizontalStrut;
	private Component horizontalStrut_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			FCMDialog dialog = new FCMDialog(null, true);
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
	public FCMDialog(JFrame parent, boolean modal)
	{
		super(parent, modal);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 745, 602);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));

		m3DScatterPanel = new FCM3DScatterPanel();
		contentPanel.add(m3DScatterPanel);
		m3DScatterPanel.setLayout(new BoxLayout(m3DScatterPanel, BoxLayout.X_AXIS));

		verticalSlider = new JSlider();
		verticalSlider.addChangeListener(this);
		verticalSlider.setMinorTickSpacing(10);
		verticalSlider.setValue(0);
		verticalSlider.setPaintTicks(true);
		verticalSlider.setMinimum(-180);
		verticalSlider.setMaximum(180);
		verticalSlider.setOrientation(SwingConstants.VERTICAL);
		contentPanel.add(verticalSlider, BorderLayout.WEST);
		
		rControlPanel = new JPanel();
		rControlPanel.setBackground(Color.WHITE);
		contentPanel.add(rControlPanel, BorderLayout.EAST);
		rControlPanel.setLayout(new BoxLayout(rControlPanel, BoxLayout.X_AXIS));
		
		horizontalStrut_1 = Box.createHorizontalStrut(5);
		rControlPanel.add(horizontalStrut_1);
		
		chckPanel = new JPanel();
		chckPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		rControlPanel.add(chckPanel);
		chckPanel.setLayout(new BoxLayout(chckPanel, BoxLayout.Y_AXIS));
		
		chckbxCat1 = new JCheckBox("Cat1");
		chckbxCat1.setSelected(true);
		chckbxCat1.addChangeListener(this);
		chckPanel.add(chckbxCat1);
		
		chckbxCat2 = new JCheckBox("Cat2");
		chckbxCat2.setSelected(true);
		chckbxCat2.addChangeListener(this);
		chckPanel.add(chckbxCat2);
		
		chckbxCat3 = new JCheckBox("Cat3");
		chckbxCat3.setSelected(true);
		chckbxCat3.addChangeListener(this);
		chckPanel.add(chckbxCat3);
		
		horizontalStrut = Box.createHorizontalStrut(5);
		rControlPanel.add(horizontalStrut);

		dControlPanel = new JPanel();
		getContentPane().add(dControlPanel, BorderLayout.SOUTH);

		dControlPanel.setLayout(new BorderLayout(0, 0));

		buttonPanel = new JPanel();
		dControlPanel.add(buttonPanel, BorderLayout.EAST);

		btnReset = new JButton("Reset");
		buttonPanel.add(btnReset);

		randomButton = new JButton("Random");
		buttonPanel.add(randomButton);
		randomButton.addActionListener(this);
		randomButton.setActionCommand("random");
		getRootPane().setDefaultButton(randomButton);

		cancelButton = new JButton("Cancel");
		buttonPanel.add(cancelButton);
		cancelButton.addActionListener(this);
		cancelButton.setActionCommand("Cancel");

		horizontSlider = new JSlider();
		horizontSlider.addChangeListener(this);
		horizontSlider.setValue(0);
		horizontSlider.setPaintTicks(true);
		horizontSlider.setMinorTickSpacing(10);
		horizontSlider.setMinimum(-180);
		horizontSlider.setMaximum(180);
		dControlPanel.add(horizontSlider, BorderLayout.CENTER);
		btnReset.addActionListener(this);

		this.pack();
	}

	public void actionPerformed(ActionEvent e)
	{

		Object source = e.getSource();
		if (source == randomButton)
		{
			m3DScatterPanel.randomChart();
			this.revalidate();
			this.repaint();
		}
		else if (source == btnReset)
		{
			horizontSlider.setValue(0);
			verticalSlider.setValue(0);
			this.revalidate();
			this.repaint();
		}
		else if (source == cancelButton)
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

	public void stateChanged(ChangeEvent e)
	{
		Object source = e.getSource();
		if (source == horizontSlider)
		{
			m3DScatterPanel.rotateHorizontal(horizontSlider.getValue());
			
		}
		else if (source == verticalSlider)
		{
			m3DScatterPanel.rotateVertical(verticalSlider.getValue());
		}
		else if(source == chckbxCat1)
		{
			m3DScatterPanel.showCat1Points(chckbxCat1.isSelected());
		}
		else if(source == chckbxCat2)
		{
			m3DScatterPanel.showCat2Points(chckbxCat2.isSelected());
		}
		else if(source == chckbxCat3)
		{
			m3DScatterPanel.showCat3Points(chckbxCat3.isSelected());
		}
		
		this.revalidate();
	}
}
