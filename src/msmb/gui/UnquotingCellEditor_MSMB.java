package msmb.gui;

import java.awt.Component;
import java.awt.Point;
import java.util.EventObject;

import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;

import msmb.commonUtilities.tables.CustomTableModel;
import msmb.commonUtilities.tables.UnquotingCellEditor;
import msmb.model.MultiModel;
import msmb.utility.Constants;

public class UnquotingCellEditor_MSMB extends  UnquotingCellEditor{
	
	private static final long serialVersionUID = 1;

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
	    
	  String tableModelName = ((CustomTableModel)(table.getModel())).getTableName();
       if(MainGui.cellTableEdited.compareTo(tableModelName) !=0) MainGui.resetViewsInExpressions();
        
        MainGui.cellValueBeforeChange = value.toString();
     	
     	MainGui.cellSelectedRow=row;
     	MainGui.cellSelectedCol=column;
     	MainGui.cellTableEdited = tableModelName;
     
     	super.getTableCellEditorComponent(table,value,isSelected, row, column);
		
     	this.setText(value.toString().trim());
     	MainGui.validationsOn=true;
     	MainGui.validateOnce=false;
		
		return this;
		  
	}


}
