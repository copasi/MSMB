package msmb.utility;

import java.awt.Color;
import java.awt.Font;

import javax.swing.UIManager;

public class GraphicalProperties {
	public static Color color_border_defaults = Color.MAGENTA;
	
	public static Color color_shading_table = Constants.vt_cream_2;  
	public static Color color_selected_row = Constants.vt_gray_4;
	
	public static Color color_cell_with_errors = Constants.vt_orange;
	public static Color color_cell_with_minorIssues = null;

	public static Color color_cell_to_highlight = Color.YELLOW;
	
	public static Font customFont = new Font(UIManager.getLookAndFeelDefaults().getFont("Label.font").getName(), Font.PLAIN, 12);


}
