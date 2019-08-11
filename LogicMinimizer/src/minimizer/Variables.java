package minimizer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

public class Variables
{
	private ArrayList<String> varIn;
	private ArrayList<String> funcIn;
	private int funcNumb;
	
	public Variables(int funcNumb) 
	{
		this.funcNumb = funcNumb;
		varIn = new ArrayList<>();
		funcIn = new ArrayList<>();
	}
	public ArrayList<String> getVarIn()
	{
		return varIn;
	}

	public ArrayList<String> getFuncIn()
	{
		return funcIn;
	}
	
	public void setVariablesFromModel(ApplicationModel model) 
	{
		funcIn.clear();
		varIn.clear();
		Vector<Vector<String>> vec = model.getTruthTable().getDataVector();
		int varCount = model.getVariablesCount();
		int funcCount = model.getFunctionsCount();
		for(int i = 0; i < vec.size(); i++) 
		{
			this.varIn.add(i, "");
			for(int j = 0; j < varCount; j++) 
			{
				this.varIn.set(i,this.varIn.get(i) + vec.get(i).get(j));					
			}
			funcIn.add(i, vec.get(i).get(varCount + funcCount - funcNumb - 1));
		}
	}
	public void setVariablesFromFile(String filename) 
	{
		
		funcIn.clear();
		varIn.clear();
		String x = "";
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			int lineCount = 0;
//			x = reader.readLine();
//			while(x != null) 
//			{
//				lineCount++;
//				x = reader.readLine();
//			}
//			reader.close();
//			reader = new BufferedReader(new FileReader(filename));
			int varCount = Integer.parseInt(reader.readLine().replace(" ", "").substring(2));
			int funcCount = Integer.parseInt(reader.readLine().replace(" ", "").substring(2));
			int i = 0;
			x = reader.readLine();
			while(x != null)
			{
				if(x.startsWith(".")) {}
				else 
				{
					x = x.replace(" ","");
					this.varIn.add(i, x.substring(0,varCount));
					this.funcIn.add(i, x.substring(varCount + funcCount - funcNumb - 1, varCount + funcCount - funcNumb));
					i++;
				}
				x = reader.readLine();
			}
			reader.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
