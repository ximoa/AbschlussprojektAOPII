package minimizer;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

public class KarnaughView extends JFrame
{
	public JTable kTable;
	public JPanel pI, pG;
	public JLabel l;
	public JScrollPane sp;
	private ApplicationModel model;
	private Variables v;
	
	public KarnaughView(ApplicationModel model) 
	{
		this.model = model;
		if(model.getFunctionsCount() == 0) {
			
		}
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Karnaugh-Veitsch-Diagramm");
		pG = new JPanel(new GridLayout(0, model.getFunctionsCount() - model.getFunctionsCount()/2, 10, 10));
		pG.setBorder(new EmptyBorder(20, 20, 20, 20));
		for(int i = 0; i < model.getFunctionsCount(); i++) 
		{
			v = new Variables(i);
			v.setVariablesFromModel(model);
			makeKV();
			pI = new JPanel(new BorderLayout());
			l = new JLabel("<html>Funktion Y<sub>"+i+":</sub></html>");
			pI.add(l,BorderLayout.NORTH);
			pI.add(kTable,BorderLayout.CENTER);
			pG.add(pI);
		}
		sp = new JScrollPane(pG);
		add(sp);
		pack();
		if(getSize().height > Toolkit.getDefaultToolkit().getScreenSize().height) {
			setSize(getSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
		}
		setVisible(true);
	}
	
	private void makeKV() 
	{
		fillHead();
		if(compareFiles()) 
		{
			v.setVariablesFromModel(model);
		}
		else 
		{
			v.setVariablesFromFile("usr.pla");
		}
		
		fillOutput();
		fillRest();
	}

	private void fillHead() 
	{
		//Array for first row and column
		final String[][] grayCode = new String[3][];
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
		for(int i = varCount-1; i >= 0 ; i--) 
		{
			x+="X<sub>" + i + "</sub>";
			if(i == varCount - varCount/2) 
			{
				x+= "/";
			}
		}
		x+= "</html>";
		kTable.setValueAt(x, 0, 0);

		for(int i = 0; i < rowCount - 1 ; i++) 
		{
			if(rowVar-1 >= 0) {
				kTable.setValueAt(grayCode[rowVar - 1][i], i+1, 0);
			}
		}

		for(int i = 0; i < colCount- 1 ; i++) 
		{
			kTable.setValueAt(grayCode[colVar - 1][i], 0, i+1);
		}
	}

	private void fillOutput() 
	{
		final int varCount = model.getVariablesCount();
		final int rowVar = varCount/2;
		final int rowCount = (int) Math.pow(2, rowVar) + 1;
		final int colCount = (int) Math.pow(2, varCount - (varCount/2)) + 1;
		
		int rowIndex = 0;
		int colIndex = 0;
		
		String rowSearch;
		String colSearch;
		String x;
		
		ArrayList<String> var = v.getVarIn();
		ArrayList<String> func = v.getFuncIn();		
		
		for(int i = 0; i < var.size() ; i++)
		{
			if(var.get(i).indexOf('-') < 0) 
			{
				rowSearch = var.get(i).substring(0, rowVar);
				colSearch = var.get(i).substring(rowVar);
				rowIndex = 1; 
				colIndex = 1;
				while(rowIndex < rowCount)
				{
					if(!rowSearch.equals(kTable.getValueAt(rowIndex, 0))) {
						rowIndex++;
					}
					else break;
				}
				while(colIndex < colCount) 
				{
					if(!colSearch.equals(kTable.getValueAt(0, colIndex))) {
						colIndex++;
					}
					else break;
				}
				
				if(colIndex <= colCount && rowIndex <= rowCount) 
				{
					x = (String) kTable.getValueAt(rowIndex, colIndex);
					if(kTable.getValueAt(rowIndex, colIndex) == null) 
					{
						kTable.setValueAt(func.get(i), rowIndex, colIndex);
					}
					else if(!x.equals(func.get(i))) 
					{
						kTable.setValueAt("-", rowIndex, colIndex);
					}
				}
			}
			else 
			{
				var.add(var.get(i).replaceFirst("-", "1"));	
				var.set(i, var.get(i).replaceFirst("-", "0"));
				func.add(func.get(i));
				i--;
			}
			
		}
	}
	
	private boolean compareFiles() 
	{
		ArrayList<String> lines1 = new ArrayList<>();
		ArrayList<String> lines2 = new ArrayList<>();
		BufferedReader reader1;
		BufferedReader reader2;
		File f1 = new File("usr.pla");
		File f2 = new File("min.pla");
		int lineCount1 = 0;
		int lineCount2 = 0;
		try 
		{
			reader1 = new BufferedReader(new FileReader(f1));
			reader2 = new BufferedReader(new FileReader(f2));
			if(reader1.readLine() == null) 
			{
				reader1.close();
				reader2.close();
				return true;
			}
			else 
			{
				lineCount1++;
			}
			while(!(reader1.readLine() == null))
			{
				lineCount1++;
			}
			while(!(reader2.readLine() == null))
			{
				lineCount2++;
			}
			reader1.close();
			reader2.close();
			if(lineCount1 == lineCount2 - 1) 
			{
				return true;
//				if(lineCount2 - 4 >= Math.pow(2, model.getVariablesCount())) 
//				{
//					return true;
//				}
//				else 
//				{
//					JOptionPane.showMessageDialog(this, "Nach mehrfachem minimieren ohne Änderung der Tabelle ist die Anzeige eines vollständigen, korrektem KV-Diagramms nicht möglich.");
//					return true;
//				}
			}
			else 
			{
				String x;
				reader2 = new BufferedReader(new FileReader(f2));
				for(int i = 0; i < lineCount2; i++) 
				{
					x = reader2.readLine().replace(" ",	"");
					if(x.startsWith(".")) {}
					else if(!v.getVarIn().contains(x.substring(0,model.getVariablesCount()))) 
					{
						reader2.close();
						return true;
					}
				}
				reader2.close();
				return false;
			}
		}catch (IOException e) 
		{
			e.printStackTrace();
		}
		return false;
	}
	
	private void fillRest()
	{
		int rowCount = (int) Math.pow(2, model.getVariablesCount()/2) + 1;
		int colCount = (int) Math.pow(2, model.getVariablesCount() - (model.getVariablesCount()/2)) + 1;
		for(int i = 1; i < rowCount; i++) 
		{
			for(int j = 1; j < colCount; j++) 
			{
				if(kTable.getValueAt(i, j) == null)
				{
					kTable.setValueAt("-", i, j);
				}
			}
		}
	}
}
