package minimizer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.EventObject;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.CellEditorListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

public class ApplicationView extends JFrame
{
	public JTable table;
	public JSpinner varsCountSpinner;
	public JSpinner functionsCountSpinner;
	public JButton addRowButton;
	public JButton fillButton;
	public JButton minimizeButton;
	public JRadioButton exactRadioButton;
	public JRadioButton heuristicRadioButton;
	public JRadioButton onSetRadioButton;
	public JRadioButton dcSetRadioButton;
	public JRadioButton offSetRadioButton;

	public ApplicationView(int width, int height)
	{
		setSize(width, height);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Logic Minimizer");
		setVisible(true);
		
		initGUILayout();
		
		revalidate();
	}

	private void initGUILayout()
	{
		//Top buttons
		JPanel topPanel = new JPanel();
		add(topPanel, BorderLayout.NORTH);
		
		JLabel varsCountLabel = new JLabel("Variablen");
		topPanel.add(varsCountLabel);
		
		SpinnerNumberModel varsCountSpinnerModel = new SpinnerNumberModel(0, 0, 6, 1);
		varsCountSpinner = new JSpinner(varsCountSpinnerModel);
		varsCountSpinner.setPreferredSize(new Dimension(40, 20));
		topPanel.add(varsCountSpinner);

		
		JLabel functionsCountLabel = new JLabel("Funktionen");
		topPanel.add(functionsCountLabel);
		
		SpinnerNumberModel functionsCountSpinnerModel = new SpinnerNumberModel(0, 0, 4, 1);
		functionsCountSpinner = new JSpinner(functionsCountSpinnerModel);
		functionsCountSpinner.setPreferredSize(new Dimension(40, 20));
		topPanel.add(functionsCountSpinner);
		
		addRowButton = new JButton("Add row");
		topPanel.add(addRowButton);
		
		fillButton = new JButton("Fill");
		topPanel.add(fillButton);
		
		table = new JTable(new DefaultTableModel())
		{
			DefaultCellEditor myCellEditor = new DefaultCellEditor(new JComboBox<String>(new String[] {"0", "1", "-"}));
			
			@Override
			public TableCellEditor getCellEditor(int row, int column) {
				return myCellEditor;
			}
		};
		table.setRowHeight(20);
		add(new JScrollPane(table), BorderLayout.CENTER);
		
		minimizeButton = new JButton("Minimize");
		add(minimizeButton, BorderLayout.SOUTH);
		
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		add(rightPanel, BorderLayout.EAST);
		
		JLabel options = new JLabel("Options");
		rightPanel.add(options);
		
		heuristicRadioButton = new JRadioButton("Heuristic");
		heuristicRadioButton.setSelected(true);
		rightPanel.add(heuristicRadioButton);
		
		exactRadioButton = new JRadioButton("Exact");
		rightPanel.add(exactRadioButton);
		
	    ButtonGroup optionsGroup = new ButtonGroup();
	    optionsGroup.add(heuristicRadioButton);
	    optionsGroup.add(exactRadioButton);

		JLabel outputType = new JLabel("Output type");
		rightPanel.add(outputType);
		
		onSetRadioButton = new JRadioButton("ON-Set");
		onSetRadioButton.setSelected(true);
		rightPanel.add(onSetRadioButton);

		dcSetRadioButton = new JRadioButton("DC-Set");
		dcSetRadioButton.setSelected(false);
		rightPanel.add(dcSetRadioButton);
		
		offSetRadioButton = new JRadioButton("OFF-Set");
		offSetRadioButton.setSelected(false);
		rightPanel.add(offSetRadioButton);
	}
}
