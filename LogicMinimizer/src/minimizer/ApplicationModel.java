package minimizer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class ApplicationModel
{
	private DefaultTableModel truthTable = new DefaultTableModel();

	public DefaultTableModel getTruthTable()
	{
		return truthTable;
	}

	private int variablesCount = 0;

	public void setVariablesCount(int value)
	{
		variablesCount = value;
		updateTruthTable();
	}

	public int getVariablesCount()
	{
		return variablesCount;
	}

	private int functionsCount = 0;

	public void setFunctionsCount(int value)
	{
		functionsCount = value;
		updateTruthTable();
	}

	public int getFunctionsCount()
	{
		return functionsCount;
	}

	public void updateTruthTable()
	{
		truthTable.setRowCount(0);
		truthTable.setColumnCount(0);

		for (int i = 0; i < variablesCount; i++)
		{
			truthTable.addColumn("<html>X<sub>" + (variablesCount - i - 1) + "</sub></html>");
		}

		for (int i = 0; i < functionsCount; i++)
		{
			truthTable.addColumn("<html>Y<sub>" + (functionsCount - i - 1) + "</sub></html>");
		}
	}

	private void loadData(String filename)
	{
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			
			String line;
			while((line = reader.readLine()) != null)
			{
				parseData(line);
			}
			reader.close();
		} 
		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}
	}

	private void parseData(String line)
	{
		line = line.toLowerCase().replace(" ", "");
		if (line.startsWith(".i"))
		{
			setVariablesCount(Integer.parseInt(line.substring(2)));
		}
		else if (line.startsWith(".o"))
		{
			setFunctionsCount(Integer.parseInt(line.substring(2)));
		}
		else if (!line.startsWith("."))
		{
			truthTable.addRow(line.split("(?!^)"));
		}
	}

	private void saveData(String filename)
	{
		try
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
			writer.write(serialize());
			writer.close();
		} 
		catch (IOException e)
		{
			System.out.println("Error writing to file");
		}
	}

	private String serialize()
	{
		String data = ".i " + variablesCount + System.lineSeparator() + ".o "
				+ functionsCount;

		Vector<Vector<String>> vec = truthTable.getDataVector();
		Vector<String> elements;
		int n = vec.size();
		for (int i = 0; i < n; i++)
		{
			data += System.lineSeparator();
			elements = vec.get(i);
			for (int j = 0; j < variablesCount; j++)
			{
				data += elements.get(j);
			}

			data += " ";

			for (int j = variablesCount; j < variablesCount + functionsCount; j++)
			{
				data += elements.get(j);
			}
		}
		data += System.lineSeparator() + ".e";

		return data;
	}

	public void minimize(String options, String outputType)
	{
		String inputFile = "usr.pla";
		String outputFile = "min.pla";
		
		saveData(inputFile);
		
		try
		{
			String optionsStr = "";
			if (!options.isEmpty()) optionsStr += options;
			if (!outputType.isEmpty()) optionsStr += " -o" + outputType;
			String command = "cmd /c bin\\espresso " + optionsStr + " " + inputFile + " > " + outputFile;
			//System.out.println(command); //Test
			Process espresso = Runtime.getRuntime().exec(command);
			int errorCode;
			try
			{
				errorCode = espresso.waitFor();
			}
			catch (InterruptedException e)
			{
				System.out.println(e);
			}
		}
		catch (IOException e)
		{
			System.out.println(e);
		}

		loadData(outputFile);
	}
}
