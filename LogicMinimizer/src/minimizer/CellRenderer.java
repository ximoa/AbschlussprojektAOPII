package minimizer;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;

import javax.management.modelmbean.ModelMBean;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class CellRenderer extends DefaultTableCellRenderer implements Prime{


		public Component getTableCellRendererComponent(JTable kTable, Object obj, boolean isSelected, boolean hasFocus, int row, int column) {
		    Component c = super.getTableCellRendererComponent(kTable, obj, isSelected, hasFocus, row, column);
		     
		    	Color[] colors = new Color[6];
		    	colors[0] = Color.red;
		    	colors[1] = Color.green;
		    	colors[2] = Color.blue;
		    	colors[3] = Color.orange;
		    	colors[4] = Color.magenta;
		    	colors[5] = Color.cyan;
		    	
		    	System.out.println(row + "|" + column + ">" + obj +"<" + primes[row][column]);
		    	
		    	if (row > 0 && column > 0)
		    	{
		    		
		    		if (primes[row][column]>0)
			    	{
			    		System.out.println(column +"|" + row);
			    		setBackground(Color.gray);
		    	}
		    	}
		    	
		    return c;
		}
}
