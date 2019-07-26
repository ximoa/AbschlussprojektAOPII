package minimizer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Stack;

import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

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
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object source = e.getSource();
		if (source == view.fillButton) doFillTable();
		else if (source == view.addRowButton) doAddRow();
		else if (source == view.minimizeButton) doMinimize();
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
