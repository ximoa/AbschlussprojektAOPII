package minimizer;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class CellRenderer extends DefaultTableCellRenderer{
		@Override
		public Component getTableCellRendererComponent(JTable kTable, Object obj, boolean isSelected, boolean hasFocus, int row, int column) {
		    Component c = super.getTableCellRendererComponent(kTable, obj, isSelected, hasFocus, row, column);
		     
		    	int ColorIndex = 0; 
		    	Color[] colors = new Color[6];
		    	colors[0] = Color.red;
		    	colors[1] = Color.green;
		    	colors[2] = Color.blue;
		    	colors[3] = Color.orange;
		    	colors[4] = Color.magenta;
		    	colors[5] = Color.cyan;
		    	
		        c.setBackground(colors[ColorIndex]);
		        ColorIndex += 1;

		    return c;
		}
}
