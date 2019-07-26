package minimizer;

import javax.swing.JFrame;
import javax.swing.JTable;

public class KarnaughView extends JFrame
{
	public JTable kTable;
	public KarnaughView() {
		setSize(640,480);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Karnaugh-Veitsch-Diagramm");
		setVisible(true);		
	}
}
