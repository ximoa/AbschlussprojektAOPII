package minimizer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

public class ApplicationController implements ActionListener, ChangeListener
{
	private ApplicationModel model;
	private ApplicationView view;

	public ApplicationController(ApplicationModel model, ApplicationView view)
	{
		this.model = model;
		this.view = view;
		
		view.table.setModel(model.getTruthTable());
		
		view.fillButton.addActionListener(this);
		view.addRowButton.addActionListener(this);
		view.minimizeButton.addActionListener(this);
		view.varsCountSpinner.addChangeListener(this);
		view.functionsCountSpinner.addChangeListener(this);	
		//
		view.karnaughButton.addActionListener(this);
		view.addWindowListener(new WindowAdapter() {
			@Override
		    public void windowClosing(WindowEvent e) 
		    {
				try
				{
					PrintWriter pwriter = new PrintWriter(new FileWriter("usr.pla", false), false);
			        pwriter.flush();
			        pwriter.close();
			        
					pwriter = new PrintWriter(new FileWriter("min.pla", false), false);
			        pwriter.flush();
			        pwriter.close();
				} catch (IOException e1)
				{
					e1.printStackTrace();
				} 
		    }
		});

		//
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object source = e.getSource();
		if (source == view.fillButton) doFillTable();
		else if (source == view.addRowButton) doAddRow();
		else if (source == view.minimizeButton) doMinimize();
		//
		else if (source == view.karnaughButton) new KarnaughView(model);		
		//
	}

	@Override
	public void stateChanged(ChangeEvent e)
	{
		Object source = e.getSource();
		if (source == view.functionsCountSpinner)
		{
			model.setFunctionsCount(((SpinnerNumberModel)view.functionsCountSpinner.getModel()).getNumber().intValue());
		}
		else if (source == view.varsCountSpinner)
		{
			model.setVariablesCount(((SpinnerNumberModel)view.varsCountSpinner.getModel()).getNumber().intValue());
		}
	}

	private void doFillTable()
	{
		DefaultTableModel tableModel = model.getTruthTable();
		tableModel.setRowCount(0);
		int varsCount = model.getVariablesCount();
		int functionsCount =  model.getFunctionsCount();
		int totalRows = (int)Math.pow(2, varsCount);
		
		for (int i = 0; i < totalRows; i++)
		{
			String data = strFormat(Integer.toBinaryString(i), varsCount);
			for (int j = 0; j < functionsCount; j++)
			{
				data += "0";
			}
			tableModel.addRow(data.split("(?!^)"));
		}
	}

	private String strFormat(String binaryString, int varsCount)
	{
		int n = varsCount - binaryString.length();
		for (int i = 0; i < n; i++)
		{
			binaryString = "0" + binaryString;
		}
		return binaryString;
	}

	private void doAddRow()
	{
		DefaultTableModel tableModel = model.getTruthTable();
		tableModel.addRow(strFormat(String.valueOf(0), tableModel.getColumnCount()).split("(?!^)"));
	}

	private void doMinimize()
	{
		String options = view.heuristicRadioButton.isSelected() ? "" : "-Dexact";
		String outputType = "";
		if (view.onSetRadioButton.isSelected()) outputType += "f";
		if (view.dcSetRadioButton.isSelected()) outputType += "d";
		if (view.offSetRadioButton.isSelected()) outputType += "r";
		
		model.minimize(options, outputType);
		view.varsCountSpinner.setValue(model.getVariablesCount());
		view.functionsCountSpinner.setValue(model.getFunctionsCount());
	}
}
