package minimizer;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public class KarnaughView extends JFrame
{
	public JTable kTable;
	public KarnaughView(ApplicationModel model) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Karnaugh-Veitsch-Diagramm");
		setLayout(new GridLayout(model.getFunctionsCount()/2, model.getFunctionsCount()-(model.getFunctionsCount()/2), 30, 30));
		for(int i = 0; i < model.getFunctionsCount(); i++) {
			createTable(model, i);
		}
		pack();
		setVisible(true);
	}
	
	private void createTable(ApplicationModel model, int funcCount) {
		
		//Array for first row and column
		String[][] grayCode = new String[3][];
		grayCode[0] = new String[]{"0","1"};
		grayCode[1] = new String[]{"00", "01", "11", "10"};
		grayCode[2] = new String[]{"000", "001", "011", "010","110","111","101","100"};
		
		int varCount = model.getVariablesCount();	
		int rowVar = varCount/2;
		int colVar = varCount - (varCount/2);
		int rowCount = (int) Math.pow(2, rowVar) + 1;
		int colCount = (int) Math.pow(2, colVar) + 1;
		
		kTable = new JTable(rowCount ,colCount);
		kTable.setCellSelectionEnabled(false);
		kTable.setRowHeight(60);
		kTable.setEnabled(false);
		
		//create String for corner of table
		String x = "<html>"; 
		for(int i = varCount-1; i >= 0 ; i--) {
			x+="X<sub>" + i + "</sub>";
			if(i == varCount/2 + 1) {
				x+= "/";
			}
		}
		x+= "</html>";
		kTable.setValueAt(x, 0, 0);
		
		for(int i = 0; i < rowCount - 1 ; i++) {
			kTable.setValueAt(grayCode[rowVar - 1][i], i+1, 0);
		}
		
		for(int i = 0; i < colCount- 1 ; i++) {
			kTable.setValueAt(grayCode[colVar - 1][i], 0, i+1);
		}
		
		Vector<Vector<String>> vec = model.getTruthTable().getDataVector();
		ArrayList<String> varIn = new ArrayList<>();
		ArrayList<String> funcIn = new ArrayList<>();
		for(int i = 0; i < vec.size(); i++) {
			varIn.add(i, "");
			for(int j = 0; j < varCount; j++) {
				varIn.set(i,varIn.get(i) + vec.get(i).get(j));					
			}
			funcIn.add(i, vec.get(i).get(varCount+funcCount));
		}
		
		fillTable(varIn, funcIn, model);
		//ex.: varIn[3] = "0011" -> look in Row for "00" + look in Colums for "11"
		
		
		this.add(kTable);
	}
	
	private void fillTable(ArrayList<String> var, ArrayList<String> func, ApplicationModel model ) {
		int varCount = model.getVariablesCount();	
		int rowVar = varCount/2;
		int colVar = varCount - (varCount/2);
		int rowCount = (int) Math.pow(2, rowVar) + 1;
		int colCount = (int) Math.pow(2, colVar) + 1;
		
		int rowIndex = 0;
		int colIndex = 0;
		
		String rowSearch;
		String colSearch;
		
		//boolean found = false;
		
		for(int i = 0; i < var.size() ; i++){
			if(var.get(i).indexOf('-') < 0) {
				rowSearch = var.get(i).substring(0, rowVar);
				colSearch = var.get(i).substring(rowVar);
				rowIndex = 1; 
				colIndex = 1;
				while(!rowSearch.equals(kTable.getValueAt(rowIndex, 0)) && rowIndex <= rowCount) {
					rowIndex++;
				}
				while(!colSearch.equals(kTable.getValueAt(0, colIndex)) && colIndex <= colCount) {
					colIndex++;
				}
				if(colIndex <= colCount && rowIndex <= rowCount) {
					kTable.setValueAt(func.get(i), rowIndex, colIndex);
				}
			}
			else {
				var.add(var.get(i).replaceFirst("-", "1"));	
				var.set(i, var.get(i).replaceFirst("-", "0"));
				func.add(func.get(i));
				i--;
			}
			
		}
	}
}
