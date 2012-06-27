package gui;


import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.table.*;
import javax.swing.undo.*;

import debugTab.DebugConstants;
import debugTab.DebugMessage;

import utility.Constants;

public class CustomTableModel extends DefaultTableModel {  
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Vector<String> columnNames = new Vector<String>();
	private String tableName = new String();
    private Vector data = new Vector();
    private transient CustomTableModelListener addEmptyRow_CustomTableModelListener;
    public boolean modified = false;
    public boolean isActive_AddEmptyRow;
    private transient MainGui gui;
	private transient  MultistateBuilderFrame multiStateDialog;
	protected HashSet<String> disabledCell = new HashSet<String>();
	
	private boolean alwaysEmptyRow = true;
	protected boolean column0_referenceIndex = true;
	
	
 
	public void setColumnNames(Vector<String> n) {
		this.columnNames.clear();
		this.columnNames.addAll(n);	
	}
	
	public void clearData() {
		this.data.clear();
		this.initializeTableModel();
		this.modified = false;
	}
	
	public CustomTableModel(String name) {
		this.columnNames.clear();
		this.columnNames.add("#");
	   	this.columnNames.add(" ");
	   
		this.initializeTableModel();
		this.tableName = new String(name);
	}
	
		
	public CustomTableModel(String name, Vector<String> colNames, MainGui w){
		this.columnNames.clear();
		this.columnNames.add("#");
		this.columnNames.addAll(colNames);
		this.gui = w;
		column0_referenceIndex = true;
		this.initializeTableModel();
		this.tableName = new String(name);
	}
	
	public CustomTableModel(String name, Vector<String> colNames, MultistateBuilderFrame w){
		this.columnNames.clear();
		this.columnNames.add("#");
		this.columnNames.addAll(colNames);
		this.multiStateDialog = w;
		column0_referenceIndex = true;
		this.initializeTableModel();
		this.tableName = new String(name);
	}
	
	/*public CustomTableModel(String name, Vector colNames, Vector rowData, MainGui w) {
		this.columnNames.clear();
		this.columnNames.add("#");
		this.columnNames.addAll(colNames);
		this.gui = w;   
		column0_referenceIndex = true;
    	Iterator it = rowData.iterator();
    	while(it.hasNext()) {
    		Vector row = new Vector();
    		row.add(data.size()+1);
    		row.addAll((Vector)it.next());
    		data.add(row);
    	}
    	this.initializeTableModel();
    	this.tableName = new String(name);
   }*/
	
	
	public CustomTableModel(String name, Vector<String> colNames, MainGui w, boolean alwaysEmptyRow, boolean column0_referenceIndex){
		this.columnNames.clear();
		this.alwaysEmptyRow = alwaysEmptyRow;
		this.column0_referenceIndex = column0_referenceIndex;
		if(column0_referenceIndex)this.columnNames.add("#");
		this.columnNames.addAll(colNames);
		this.gui = w;
		this.initializeTableModel();
		this.tableName = new String(name);
	}
	
	public CustomTableModel(String name, Vector<String> colNames, MultistateBuilderFrame w, boolean alwaysEmptyRow, boolean column0_referenceIndex){
		this.columnNames.clear();
		this.alwaysEmptyRow = alwaysEmptyRow;
		this.column0_referenceIndex = column0_referenceIndex;
		if(column0_referenceIndex) this.columnNames.add("#");
		this.columnNames.addAll(colNames);
		this.multiStateDialog = w;
		this.initializeTableModel();
		this.tableName = new String(name);
	}
	
	private void initializeTableModel(){
		   Vector<Object> newR = new Vector<Object>();
	   	   if(column0_referenceIndex) {
	   		   newR.add(1);
	   		   for(int i=1; i < columnNames.size(); i++) {
	   			   if(((String)columnNames.get(i)).compareTo(Constants.EventsColumns.DELAYCALC.description)==0 ||
	   					((String)columnNames.get(i)).compareTo(Constants.EventsColumns.EXPAND_ACTION_ONVOLUME_TOSPECIES_C.description)==0) 
	   			   {
	   				 newR.add(new Boolean(false));
	   			   }
	   			   else { newR.add(new String("")); }
	   		   }
	   	   } else {
	   		   for(int i=0; i < columnNames.size(); i++)  {
	   			   if(((String)columnNames.get(i)).compareTo(Constants.EventsColumns.DELAYCALC.description)==0 ||
	   					((String)columnNames.get(i)).compareTo(Constants.EventsColumns.EXPAND_ACTION_ONVOLUME_TOSPECIES_C.description)==0 ) 
   			   {
	   				newR.add(new Boolean(false));
	   			   } else {	  newR.add(new String("")); }
	   		   }
	   	   }
	   	   
	   	   data.add(newR);
	   	   if(alwaysEmptyRow) {
	   		   addEmptyRow_CustomTableModelListener = new CustomTableModelListener(this);
	   		  this.addTableModelListener(addEmptyRow_CustomTableModelListener);
	   	   }
	   	   
	
	}
	
	
	@Override
	public void insertRow(int row, Vector rowData) {
		if(rowData.size() > 0) {
     		Vector newR = new Vector();
     		newR.add(row+1);
        	newR.addAll(rowData);
        	for(int i=newR.size(); i < this.getColumnCount(); i++) {
        		newR.add(new String(""));
        	}
        	data.insertElementAt(newR, row+1);
        	
        	changeNumerationBelow(row+1);
        	fireTableDataChanged(); 
		}	
	}
	
	private void changeNumerationBelow(int i) {
		for(;i < data.size();i++) {
			((Vector)(data.get(i))).setElementAt(i+1, 0);
		}
	}

	public void removeAddEmptyRow_Listener() {
			isActive_AddEmptyRow = false;
			this.removeTableModelListener(addEmptyRow_CustomTableModelListener);
	}

	public void addAddEmptyRow_Listener() {
		isActive_AddEmptyRow = true;
		this.addTableModelListener(addEmptyRow_CustomTableModelListener);
}

    public int getColumnCount() {
        return columnNames.size();
    }

    public int getRowCount() {
    	if(data!=null)  	return data.size();
    	else return 0;
    }
     

    public String getColumnName(int col) {
        return columnNames.get(col).toString();
    }

    public Object getValueAt(int row, int col) {
    	Vector r = (Vector)data.get(row);
    	if(r != null && r.size() > 1) {
    		return r.get(col);
    	}
    	else return null;
		
    }

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
   }

    public boolean isCellEditable(int row, int col) {
        if (col < 1) {
            return false;
        } else {
        	if(disabledCell.contains(row+"_"+col)) {
        		return false;
        	}
        	return true;
        }
    }
    
   
    public void addRow(Vector v) {
     	if(v.size() > 0) {
     		Vector newR = new Vector();
     		if(this.column0_referenceIndex && !this.alwaysEmptyRow) newR.add(this.getRowCount()+1);
     		else if(this.column0_referenceIndex) newR.add(this.getRowCount());
        	newR.addAll(v);
        	
        	
        	for(int i=0; i < newR.size(); i++) {
      		  if(((String)columnNames.get(i)).compareTo(Constants.EventsColumns.DELAYCALC.description)==0 ||
      		      ((String)columnNames.get(i)).compareTo(Constants.EventsColumns.EXPAND_ACTION_ONVOLUME_TOSPECIES_C.description)==0 )
	   			   {
      			  newR.set(i, Boolean.parseBoolean((String) newR.get(i)));
	   			   }
	   		  	}	
        	
        	for(int i=newR.size(); i < this.getColumnCount(); i++) {
        		  if(((String)columnNames.get(i)).compareTo(Constants.EventsColumns.DELAYCALC.description)==0 ||
        		      ((String)columnNames.get(i)).compareTo(Constants.EventsColumns.EXPAND_ACTION_ONVOLUME_TOSPECIES_C.description)==0 )
	   			   {
        			  newR.add(new Boolean(false));
	   			   }
	   			   else { newR.add(new String("")); }


        	}
        	
        	
        	 if(this.alwaysEmptyRow) {
        		 data.set(this.getRowCount()-1, newR);
        	     addEmptyRow_CustomTableModelListener.tableChanged(new TableModelEvent(this, this.getRowCount()-1));
        	 } else{
        		 if(((String)((Vector)data.get(0)).get(1)).length()==0) {
        			 if(this.column0_referenceIndex) newR.set(0, 1);
        			 data.set(0, newR);
        		 } else {
        			 data.add(newR);
        		 }
        	 }
            fireTableDataChanged();
        } else {
        	Vector newR = new Vector();
        	Vector lastRow = (Vector)data.get(data.size()-1);
        	if(isEmpty(lastRow)) {	
        		for (int i = 0; i < lastRow.size(); i++) {
        			enableCell(data.size()-1, i);
        		}
        		return;   	
        	}
        	int i = 0;
        	if(this.column0_referenceIndex) {
        		newR.add(this.getRowCount()+1);
        		i = 1;
        	}
   	   	    for(;i < columnNames.size();i++) {
   	   	   if(((String)columnNames.get(i)).compareTo(Constants.EventsColumns.DELAYCALC.description)==0 	 ||
   	   	   		((String)columnNames.get(i)).compareTo(Constants.EventsColumns.EXPAND_ACTION_ONVOLUME_TOSPECIES_C.description)==0 )
	   		
			   {
   	   		     		newR.add(new Boolean(false));
   	   		   }
			   else { newR.add(new String("")); }
   	   	    }
   	   	    data.add(newR);
        }
     	

     }
    
    
    private boolean isEmpty(Vector row) {
		for (Object object : row) {
			if(object instanceof String) {
				if(((String)object).trim().length() > 0) return false;
			}
		}
		return true;
	}
    
    
    boolean isEmpty(int index) {
    	Vector row = new Vector();
    	if(column0_referenceIndex) { row.addAll((Vector)data.get(index)); row.remove(0); }
    	else row = (Vector)data.get(index);
    	return isEmpty(row);
	}

	public void removeRow(int nrow) {
     	//SE RIMUOVO MULTISPECIES, AGGIORNARE GLI INDICIIIIIIIIIIIIIIIIIIIIII NELL'ALTRA HASHMAP DI MAINGUI
    	//TOOOOOOOOOOOO BEEEEEEEEEEEEE FIXED
    	data.removeElementAt(nrow);
     	
     	for(int i = 0; i < data.size(); i++) {
    		Vector row = new Vector();
    		Vector current = (Vector)data.get(i);
    		row.add(i+1);
    		for(int j = 1; j < current.size(); j++){
    				row.add(current.get(j));
     		}
    		data.set(i,row);
    	}
     }
    
    
    
    public void setRow(int index, Vector v) {
    	Vector newR = new Vector();
    	newR.add(index+1);
    	newR.addAll(v);
    	for(int i=newR.size(); i < this.getColumnCount(); i++) {		 
    		if(((String)columnNames.get(i)).compareTo(Constants.EventsColumns.DELAYCALC.description)==0 ||
    				((String)columnNames.get(i)).compareTo(Constants.EventsColumns.EXPAND_ACTION_ONVOLUME_TOSPECIES_C.description)==0 )  		{
    			newR.add(new Boolean(false));	  
    			}
    		 else {newR.add(new String(""));}  
    	}
     	data.set(index, newR);
     }

    


    public void setValueAt_old(Object value, int row, int col) {
    	MainGui.cell_to_highlight = null;
    	if(row < 0) return;
      Vector r = (Vector)data.get(row);
	   Object old = r.get(col);
	   r.set(col, value);
	   data.set(row, r);
	   try{
		   if(gui != null) {
			   int tabSelected = -1;
			   if(tableName.compareTo(Constants.TitlesTabs.COMPARTMENTS.description)==0) tabSelected = Constants.TitlesTabs.COMPARTMENTS.index;
			   else if(tableName.compareTo(Constants.TitlesTabs.DEBUG.description)==0) tabSelected = Constants.TitlesTabs.DEBUG.index;
			   else if(tableName.compareTo(Constants.TitlesTabs.EQUATIONS.description)==0) tabSelected = Constants.TitlesTabs.EQUATIONS.index;
			   else if(tableName.compareTo(Constants.TitlesTabs.EVENTS.description)==0) tabSelected = Constants.TitlesTabs.EVENTS.index;
			   else if(tableName.compareTo(Constants.TitlesTabs.FUNCTIONS.description)==0) tabSelected = Constants.TitlesTabs.FUNCTIONS.index;
			   else if(tableName.compareTo(Constants.TitlesTabs.GLOBALQ.description)==0) tabSelected = Constants.TitlesTabs.GLOBALQ.index;
			   else if(tableName.compareTo(Constants.TitlesTabs.SPECIES.description)==0) tabSelected = Constants.TitlesTabs.SPECIES.index;
			   else if(tableName.compareTo(Constants.TitlesTabs.REACTIONS.description)==0) { 
				   MainGui.addedByReaction = true; tabSelected = Constants.TitlesTabs.REACTIONS.index;
			   }
			   
			   gui.jTabGeneral.setSelectedIndex(tabSelected);
 			   gui.updateModelFromTable(row, col);
		   }
		   else if(this.multiStateDialog != null) multiStateDialog.updateMultisiteSpeciesConcentrationFromTable(row);
	   } catch(Exception ex) {
		   if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) ex.printStackTrace();
		   r.set(col, old);
		   data.set(row, r);
		   return;
	   }
	   fireTableDataChanged();
	   fireTableCellUpdated(row, col);
	   try {
		gui.validateMultiModel(false, false);
		} catch (Exception e) {
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
		}
    }
    
    public void setValueAt_old_withoutUpdate(Object value, int row, int col) {
    	MainGui.cell_to_highlight = null;
 	   Vector r = (Vector)data.get(row);
 	   Object old = r.get(col);
 	   r.set(col, value);
 	   data.set(row, r);
 	   fireTableDataChanged();
 	   fireTableCellUpdated(row, col);
     }

	public void disableCell(int row, int col) {
		this.disabledCell.add(row+"_"+col);
	}
	
	public void enableCell(int row, int col) {
		this.disabledCell.remove(row+"_"+col);
	}

	public String getTableName() {
		return this.tableName;
	}

	@Override
    public void setValueAt(Object value, int row, int column)
    {
		MainGui.modelHasBeenModified = true;
        setValueAt(value, row, column, true);
    }

	  public void setValueAt_withoutUpdate(Object value, int row, int col) {
		  setValueAt_withoutUpdate(value, row, col, true);
	  }
	  
	  public void setValueAt_withoutUpdate(Object value, int row, int col, boolean undoable) {
	        if(this.tableName.compareTo(MainGui.cellTableEdited) != 0 || 
		    		row != MainGui.cellSelectedRow || col != MainGui.cellSelectedCol) {
		    	undoable = false;
		    }
		    UndoableEditListener listeners[] = getListeners(UndoableEditListener.class);
	        if (undoable == false || listeners == null)
	        {
	            this.setValueAt_old_withoutUpdate(value, row, col);
	             return;
	        }
	        
	    
	        Object oldValue = getValueAt(row, col);
	        setValueAt_old_withoutUpdate(value, row, col);
	        /*JvCellEdit cellEdit = new JvCellEdit(this, oldValue, value, row, col);
	        UndoableEditEvent editEvent = new UndoableEditEvent(this, cellEdit);
	        for (UndoableEditListener listener : listeners)
	            listener.undoableEditHappened(editEvent);*/
	  }
	    public void setValueAt(Object value, int row, int column, boolean undoable)
	    {
		    if(this.tableName.compareTo(MainGui.cellTableEdited) != 0 || 
		    		row != MainGui.cellSelectedRow || column != MainGui.cellSelectedCol) {
		    	undoable = false;
		    }
	        UndoableEditListener listeners[] = getListeners(UndoableEditListener.class);
	        if (undoable == false || listeners == null)
	        {
	            this.setValueAt_old(value, row, column);
	            return;
	        }
	        
	     
	        Object oldValue = getValueAt(row, column);
	        setValueAt_old(value, row, column);
	        /*JvCellEdit cellEdit = new JvCellEdit(this, oldValue, value, row, column);
	        UndoableEditEvent editEvent = new UndoableEditEvent(this, cellEdit);
	        for (UndoableEditListener listener : listeners)
	            listener.undoableEditHappened(editEvent);*/
	    }


	    public void addUndoableEditListener(UndoableEditListener listener)
	    {
	        listenerList.add(UndoableEditListener.class, listener);
	    }

		public void enableAllCells() {
			disabledCell.clear();
		}
	
	   
   
}

/*
class JvCellEdit extends AbstractUndoableEdit
{
    protected CustomTableModel tableModel;
    protected Object oldValue;
    protected Object newValue;
    protected int row;
    protected int column;


    public JvCellEdit(CustomTableModel tableModel, Object oldValue, Object newValue, int row, int column)
    {
        this.tableModel = tableModel;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.row = row;
        this.column = column;
    }


    @Override
    public String getPresentationName()
    {
        return "Edit "+tableModel.getTableName();
    }


    @Override
    public void undo() throws CannotUndoException
    {
        super.undo();

        tableModel.setValueAt(oldValue, row, column, false);
    }


    @Override
    public void redo() throws CannotUndoException
    {
        super.redo();

        tableModel.setValueAt(newValue, row, column, false);
    }
    

    @Override
    public String toString() {
    	return new String(row+","+column+" old: "+oldValue+"; new: "+newValue +"\n");
    }
}

*/

class CustomColumn0Renderer extends DefaultTableCellRenderer  {  
    private static final long serialVersionUID = 1L;
	int selectedRow;  
    Font customFont = null;
    public Font getCustomFont() {return customFont;	}
	public void setCustomFont(Font customFont) {	this.customFont = customFont;	}

	public CustomColumn0Renderer()      {  
        setHorizontalAlignment(JLabel.CENTER);  
        setBackground(UIManager.getColor("TableHeader.background"));  
        selectedRow = -1;  
    }  
   
    public Component getTableCellRendererComponent(JTable table,  Object value,  boolean isSelected,  boolean hasFocus,  int row, int column)  {  
        super.getTableCellRendererComponent(table, value, isSelected,  hasFocus, row, column);  
        setBorder(BorderFactory.createRaisedBevelBorder());
        
      
        if(customFont!=null)  setFont(customFont);
              return this;  
    }  
   
   
    public void setSelectedRow(int selected)     {  
        selectedRow = selected;  
        repaint();  
    }
    

    
}  


class CustomTableModelListener implements TableModelListener{
	 CustomTableModel mod;
	 
	 public CustomTableModelListener(CustomTableModel m) {
		 mod = m;
	 }
	 
	 public void tableChanged(TableModelEvent e) {
		int rowChanged = e.getFirstRow();
	    mod.modified = true;
        if(rowChanged == mod.getRowCount()-1) {
        	  
        	   mod.addRow(new Vector());
        } else {
        	if(e.getColumn()!=-1) {
        		//MainGui.clear_debugMessages_relatedWith(rowChanged+1);
        		MainGui.addRemoveEmptyFields_inConsistencyChecks(mod, mod.getTableName());
        	}
        }
     }
	 
   }

class CustomJTable extends JTable  
//implements DragGestureListener, DragSourceListener 
{
	private static final long serialVersionUID = 1L;

	CustomTableModel model ;
	
    Font customFont = null;
    public Font getCustomFont() {return customFont;	}
	public void setCustomFont(Font customFont) {	
		this.customFont = customFont; 	
		if(customFont!= null) {
			FontMetrics metrics = this.getFontMetrics(customFont);
			int fontHeight = metrics.getHeight();
		    this.setRowHeight(fontHeight+5);
		} else {
			this.setRowHeight(20);
		}
	}
	JPopupMenu popupMenu = new JPopupMenu();
	private PopUpViewActionListener editableListener;
	private PopUpViewActionListener expandedListener;
	private PopUpViewActionListener expandedAllListener;
	private PopUpViewActionListener compressedListener;
	private PopUpViewActionListener customListener;
	private PopUpViewActionListener currentAsEditable;
 
	/*private DragSource dragSource;
	
	public CustomJTable() {
		InputMap inputMap = this.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK);
        inputMap.put(keyStroke, "none");
        setDragEnabled(true);
     // Setup our drag-and-drop
        dragSource = new DragSource();
        dragSource.createDefaultDragGestureRecognizer( this, DnDConstants.ACTION_COPY, this );
       
   }
	  // ----------------------------------------------------
	  // Drag Gesture Listener Events 
	  // ----------------------------------------------------
	  public void dragGestureRecognized(DragGestureEvent dge)
	  {
	   System.out.println( "Drag Gesture Recognized..." );

	  
	   int[] rows = this.getSelectedRows();
	   if( rows.length > 0 )
	   {
	     // Here is an array of the selected rows that you need to extract from
	     // and build your selected transport; we'll build an XML document to pass
	     // in the transferable object

	    
	     // Build a StringSelection (Transferable) object to
	     // hold the XML string
	     StringSelection ss = new StringSelection( rows.toString() );

	     // Start the drag
	     this.dragSource.startDrag( dge, DragSource.DefaultCopyDrop, ss, this );
	   }
	  }

	  // ----------------------------------------------------
	  // Drag Source Listener events
	  // ----------------------------------------------------
	  public void dragEnter(DragSourceDragEvent dsde)
	  {
	  }
	  public void dragDropEnd(DragSourceDropEvent dsde)
	  {
	    if( dsde.getDropSuccess() == true )
	    {
	      System.out.println( "Drop successful!" );

	      if( dsde.getDropAction() == DnDConstants.ACTION_MOVE )
	      {
	        // Remove the objects from our table: you might want to save the
	        // selected indices
	      }
	      else if( dsde.getDropAction() == DnDConstants.ACTION_COPY )
	      {
	        System.out.println( "Copy complete" );
	      }
	      else if( dsde.getDropAction() == DnDConstants.ACTION_LINK )
	      {
	        System.out.println( "Link" );
	      }
	      else
	      {
	        System.out.println( "Unsupported Action: " + dsde.getDropAction() );
	      }
	    }
	    else
	    {
	      System.out.println( "Drop failed..." );
	    }
	  }
	  public void dragOver(DragSourceDragEvent dsde)
	  {
	  }
	  public void dropActionChanged(DragSourceDragEvent dsde)
	  {
	  }
	  public void dragExit(DragSourceEvent dse)
	  {
	  }*/
	
    
	public void initializeCustomTable(CustomTableModel m) {
		if(customFont!= null) {
			FontMetrics metrics = this.getFontMetrics(customFont);
			int fontHeight = metrics.getHeight();
		    this.setRowHeight(fontHeight);
		} else {
			this.setRowHeight(20);
		}
	    
	    model = m;    
	  
	    setBackground(UIManager.getColor("Button.background"));
	    
	  
		TableColumnModel colModel = this.getColumnModel();
		if(model != null && model.column0_referenceIndex) {
			TableColumn col = colModel.getColumn(0);  
			col.setCellRenderer(new CustomColumn0Renderer());  
			col.setPreferredWidth(40);
			col.setMaxWidth(100);
			col.setMinWidth(40);
			col.setWidth(40);
		} 
		
		TableColumn colLast = colModel.getColumn(colModel.getColumnCount()-1);  
		colLast.setPreferredWidth(40);
		colLast.setMinWidth(40);
		colLast.setWidth(40);
		
		
		
	    if(model.getTableName().compareTo(Constants.MultistateBuilder_QUANTITIES_description)==0)this.setAutoCreateRowSorter(true); 
	    
	    putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		
	    
	    
	    addMouseListener(new MouseAdapter()
	    {
	    	private JMenuItem ackMenuItem;
	    	private JMenuItem copySignature;
			public void mousePressed(MouseEvent e) {
	    	      showPopup(e);
	    	    }
	    	    public void mouseReleased(MouseEvent e) {
	    	      showPopup(e);
	    	    }
	    	    
	        public void mouseClicked(MouseEvent e)
	        {
	          //  if (e.getComponent().isEnabled() && e.getButton() == MouseEvent.BUTTON1)
	           	 
	        	  if (e.getButton() == MouseEvent.BUTTON1)
	            {
	                Point p = e.getPoint();
	                int row = rowAtPoint(p); 
	                int col = columnAtPoint(p); 
	                if(row == -1 || col == -1) return;
	             
	               
	                
	                MainGui.cellValueBeforeChange = model.getValueAt(row,col).toString();
	             	
	             	MainGui.cellSelectedRow=row;
	             	MainGui.cellSelectedCol=col;
	             	MainGui.cellTableEdited = model.getTableName();
	             	/*if( model.getTableName().compareTo(Constants.TitlesTabs.FUNCTIONS.description)==0 && col == Constants.FunctionsColumns.SIGNATURE.index) {
	             		  CustomDialogSignature d = new CustomDialogSignature(null, (String) MainGui.tableFunctionsmodel.getValueAt(MainGui.cellSelectedRow, Constants.FunctionsColumns.NAME.index));
						  d.pack();
						  d.setLocationRelativeTo(null);
						  d.setModal(true);
							d.setVisible(true);
	             	}*/
	             	
	             	
	            } else {
	            	showPopup(e);
	            }
	        }
	        
	        private void showPopup(MouseEvent e) {
	        	if (e.isPopupTrigger()) {
	              	boolean showPopup = false;
	            	popupMenu.removeAll();
		            Point p = e.getPoint();
		             final int row = rowAtPoint(p); 
		             final int col = columnAtPoint(p); 
		             if(row == -1 || col == -1) return;
		             if(MainGui.isCellWithDefaultValue(model.getTableName(), row, col)) {
		            	 showPopup = true;
		            	 DebugMessage dm = new DebugMessage();
			             dm.setOrigin_table(model.getTableName());
			             dm.setProblem("");
			             dm.setPriority(DebugConstants.PriorityType.DEFAULTS.priorityCode);
			             dm.setOrigin_col(col);
			             dm.setOrigin_row(row+1);
						 MainGui.toBeAck_debugMessage =dm;
		            	 ackMenuItem = new JMenuItem("Acknowledge default value");
		    			 ackMenuItem.addActionListener(new ActionListener() {
		    				  public void actionPerformed(ActionEvent e) {
		    					  try {
		    						MainGui.ackSelectedDebugMessage();
		    					} catch (Exception e1) {
		    						if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e1.printStackTrace();
		    					}
		    				  }
		    			  });
		    		    popupMenu.insert(ackMenuItem,0);
		    		 } else {
		    			 if(ackMenuItem!= null) popupMenu.remove(ackMenuItem);
		    		 }
		             if(MainGui.isCellWithMultipleView(model.getTableName(), row, col)) {
		            	 JMenuItem editableView = new JMenuItem(Constants.Views.EDITABLE.description);
		         	    editableListener = new PopUpViewActionListener(Constants.Views.EDITABLE.index);
		         		editableView.addActionListener(editableListener);
		         		popupMenu.add(editableView);
		         	    
		         		JMenuItem expandedView = new JMenuItem(Constants.Views.EXPANDED.description);
		         	    expandedListener = new PopUpViewActionListener(Constants.Views.EXPANDED.index);
		         		expandedView.addActionListener(expandedListener);
		         		popupMenu.add(expandedView);
		         	
		         		popupMenu.addSeparator();
		         		JMenuItem expandedAllView = new JMenuItem(Constants.Views.EXPANDED_ALL.description);
		         	    expandedAllListener = new PopUpViewActionListener(Constants.Views.EXPANDED_ALL.index);
		         		expandedAllView.addActionListener(expandedAllListener);
		         		popupMenu.add(expandedAllView);
		         		
		         		JMenuItem compressedView = new JMenuItem(Constants.Views.COMPRESSED.description);
		         	    compressedListener = new PopUpViewActionListener(Constants.Views.COMPRESSED.index);
		         		compressedView.addActionListener(compressedListener);
		         		//popupMenu.add(compressedView); // compressed is the editable
		         		
		         	    JMenuItem customView = new JMenuItem(Constants.Views.CUSTOM.description);
		         	    customListener = new PopUpViewActionListener(Constants.Views.CUSTOM.index);
		         	    customView.addActionListener(customListener);
		         	    // popupMenu.addSeparator();
			         	//popupMenu.add(customView);
		         		
		         	    popupMenu.addSeparator();
		         	    JMenuItem setCurrentAsEditable = new JMenuItem(Constants.Views.CURRENT_AS_EDITABLE.description);
		         	    currentAsEditable = new PopUpViewActionListener(Constants.Views.CURRENT_AS_EDITABLE.index);
		         	    setCurrentAsEditable.addActionListener(currentAsEditable);
		         		popupMenu.add(setCurrentAsEditable);
		         		
		         		
		         		 editableListener.setTable(model.getTableName());
			             editableListener.setRow(row);
			             editableListener.setColumn(col);
			             expandedListener.setTable(model.getTableName());
			             expandedListener.setRow(row);
			             expandedListener.setColumn(col);
			             expandedAllListener.setTable(model.getTableName());
			             expandedAllListener.setRow(row);
			             expandedAllListener.setColumn(col);
			             compressedListener.setTable(model.getTableName());
			             compressedListener.setRow(row);
			             compressedListener.setColumn(col);
			             customListener.setTable(model.getTableName());
			             customListener.setRow(row);
			             customListener.setColumn(col);
			             currentAsEditable.setTable(model.getTableName());
			             currentAsEditable.setRow(row);
			             currentAsEditable.setColumn(col);
			             showPopup = true;
			         }
		             
		             if(model.getTableName().compareTo(Constants.TitlesTabs.FUNCTIONS.description)==0) {
		            	 copySignature = new JMenuItem("Copy signature to Clipboard");
		            	 copySignature.addActionListener(new ActionListener() {
		    				  public void actionPerformed(ActionEvent e) {
		    					  try {
		    						  String typedText = (String) model.getValueAt(row, Constants.FunctionsColumns.NAME.index);
		    						  Clipboard clipboard = getToolkit().getSystemClipboard();
		    					  clipboard.setContents(new StringSelection(typedText), null );
		    					  MainGui.copiedSignature=true;
		    					} catch (Exception e1) {
		    						if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e1.printStackTrace();
		    					}
		    				  }

							
		    			  });
		    		    popupMenu.insert(copySignature,0);
		    		    showPopup = true;
		             } else {
		    			 if(copySignature!= null) popupMenu.remove(copySignature);
		    		 }
		              if(showPopup) {
		            	 popupMenu.show(e.getComponent(), e.getX(), e.getY());
			         }
				
	            }
	          }
	    });
	    
	   
	    
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION );
		
		//this.addHighlighter(HighlighterFactory.createAlternateStriping(Color.white,Constants.color_shading_table));
		this.setColumnSelectionAllowed(false);
		//this.setSortable(false);

		this.getTableHeader().setReorderingAllowed(false);
		/*KeyStroke paste = KeyStroke.getKeyStrokeKeyEvent.VK_V,ActionEvent.CTRL_MASK,false);
	    this.registerKeyboardAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				 if (e.getActionCommand().compareTo("Paste")==0)
			      {
				System.out.println(MainGui.copiedSignature = false);
			      }
				
			}},"Paste",paste,JComponent.WHEN_FOCUSED);
*/
		
		/*TableColumnModel model = super.getColumnModel();
		  for(int i=0; i<super.getColumnCount(); i++) {
		      TableColumn tc = model.getColumn(i);
		      tc.setCellEditor(new ExpressionsCellEditor(new JTextField()));

		  }*/
				
	}
	

	 public boolean getScrollableTracksViewportHeight()
	    {
	        Component parent = getParent();

	        if (parent instanceof JViewport) {
	        	  return parent.getHeight() > getPreferredSize().height;
	        }
	          

	        return false;
	    }
	
	
	@Override
	public void editingStopped(ChangeEvent e) {
	  	TableColumn nameColumn = getColumnModel().getColumn(editingColumn);
        TableCellRenderer cellRender = nameColumn.getCellRenderer();
     	if(cellRender instanceof EditableCellRenderer) {
     		((EditableCellRenderer) cellRender).cell_no_defaults(editingRow);
     	};
  
		super.editingStopped(e);
    }
	
	
	
	

	 
	public Component prepareRenderer(TableCellRenderer renderer, int rowIndex, int vColIndex) {
		Component c = null;
		try{
			c = super.prepareRenderer(renderer, rowIndex, vColIndex);
			
			
			if(MainGui.cell_to_highlight != null) {
				if(MainGui.cell_to_highlight.getLeft().intValue()-1 == rowIndex && MainGui.cell_to_highlight.getRight().intValue() == vColIndex){
					c.setBackground(MainGui.color_cell_to_highlight);
					return c;
				}
			}
			
			
			if(renderer instanceof EditableCellRenderer) {
				if(this.model.disabledCell.contains(rowIndex+"_"+vColIndex)) {
					c.setBackground(Constants.vt_blues_1);
					if(this.model.getTableName().compareTo(Constants.TitlesTabs.FUNCTIONS.description)==0) c.setForeground(Color.BLACK);
					else if(this.model.getTableName().compareTo(Constants.MultistateBuilder_QUANTITIES_description)==0) c.setForeground(Color.BLACK);
					else if(this.model.getTableName().compareTo(Constants.TitlesTabs.SPECIES.description)==0 && vColIndex==Constants.SpeciesColumns.EXPRESSION.index) c.setForeground(Color.BLACK);
					else if(this.model.getTableName().compareTo(Constants.TitlesTabs.SPECIES.description)==0 && vColIndex==Constants.SpeciesColumns.INITIAL_QUANTITY.index) {
						if(this.model.getValueAt(rowIndex, Constants.SpeciesColumns.TYPE.index).toString().compareTo(Constants.SpeciesType.MULTISTATE.description)==0)
						c.setForeground(Constants.vt_blues_1);
						else c.setForeground(Color.BLACK);
					}
					else c.setForeground(Constants.vt_blues_1);
				} else {
					c.setForeground(getForeground());
				}
				if(rowIndex== getRowCount()-1 && this.getValueAt(rowIndex, vColIndex).toString().trim().length() ==0) {
					if(c.getBackground() != MainGui.color_cell_with_errors)
					{
						if (rowIndex % 2 != 0) {
							c.setBackground(MainGui.color_shading_table);
						} else {
							c.setBackground(Color.white);
						}
					}
				}
				
				if(customFont!=null) c.setFont(customFont);
				if(this.isRowSelected(rowIndex)) {
					c.setBackground(MainGui.color_cell_to_highlight);
					c.setFont(c.getFont().deriveFont(Font.BOLD));
					if(vColIndex== 0) {
						c.setForeground(Color.BLACK);
					}
				} else {
					if(vColIndex== 0) {
						c.setBackground(MainGui.color_shading_table);
						c.setForeground(Color.BLACK);
					}
					else {
						if(c.getBackground() != MainGui.color_cell_with_errors)
						{
						if (rowIndex % 2 != 0) {
							c.setBackground(MainGui.color_shading_table);
						} else {
							c.setBackground(Color.white);
						}
					 }
					}
				}
				
				return c;
			}
			
		if(this.model.disabledCell.contains(rowIndex+"_"+vColIndex)) {
			c.setBackground(Constants.vt_blues_1);
		}else {
			if(c.getBackground() != MainGui.color_cell_with_errors)
			{
			if (rowIndex % 2 != 0) {
				c.setBackground(MainGui.color_shading_table);
			} else {
				c.setBackground(Color.white);
			}
			}
		}
		
		
		
		if( vColIndex == 0 && model.column0_referenceIndex) {
			c.setBackground(UIManager.getColor("TableHeader.background"));
		}
		
		if(customFont!=null) c.setFont(customFont);
		
		if(this.isRowSelected(rowIndex)) {
			c.setBackground(MainGui.color_cell_to_highlight);
			c.setFont(c.getFont().deriveFont(Font.BOLD));
			c.setForeground(Color.BLACK);
		} else {
			if(vColIndex== 0) {
				c.setBackground(MainGui.color_shading_table);
			}
			else {
				if(c.getBackground() != MainGui.color_cell_with_errors)
				{
				if (rowIndex % 2 != 0) {
					c.setBackground(MainGui.color_shading_table);
				} else {
					c.setBackground(Color.white);
				}
				}
			 }
				
		}
		
		return c;
		
		
		} catch(Exception ex) {
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) ex.printStackTrace();
				System.out.println("Problems rendering column: "+this.model.getColumnName(vColIndex));
				if(customFont!=null)  c.setFont(customFont);
				return c;
		}
		
		}

	
};



class UnquotingCellEditor extends JTextField implements TableCellEditor{  
		private static final long serialVersionUID = 1L;
	Font customFont = null;
	    public Font getCustomFont() {return customFont;	}
		public void setCustomFont(Font customFont) {	this.customFont = customFont;	}

	protected Vector<CellEditorListener> listeners;  

	public UnquotingCellEditor(){  
		listeners = new Vector<CellEditorListener>();  
		}  


	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,  
			int row, int column) {  
	    
		MainGui.cellValueBeforeChange = table.getModel().getValueAt(row,column).toString();
     	MainGui.cellSelectedRow=row;
     	MainGui.cellSelectedCol=column;
     	MainGui.cellTableEdited = ((CustomTableModel) table.getModel()).getTableName();
     	
		if(value.toString().trim().startsWith("\"") &&  value.toString().trim().endsWith("\"")) {
			this.setText(value.toString().trim().substring(1,value.toString().trim().length()-1));
		}   else {
			this.setText(value.toString().trim());
		}
		
		if(customFont!=null) this.setFont(customFont);
		return this;
		
	}  

	public void cancelCellEditing(){  
		fireEditingCanceled();  
	}  


	public boolean stopCellEditing(){  
		fireEditingStopped();  
		return true;  
	}  

	public Object getCellEditorValue(){  
		return this.getText();  
	}  

	@Override
	public boolean isCellEditable(EventObject oe){
		  try { 
			  MouseEvent me = (MouseEvent) oe;
			  if (me.getClickCount() == 2) return true;
	      	 else return false;
		  } catch (Exception e) { // is not a mouse event, can be a tab or some other key events
			 return false;
		}
	 }  

	@Override
	public boolean shouldSelectCell(EventObject ev) { return true;}  
	
	@Override
	public void addCellEditorListener(CellEditorListener cel){  
		listeners.addElement(cel);  
	}  

	@Override
	public void removeCellEditorListener(CellEditorListener cel){  
		listeners.removeElement(cel);  
	}  

	protected void fireEditingCanceled(){  
		ChangeEvent ce = new ChangeEvent(this);  
		for(int i=0; i<listeners.size(); i++){  
			((CellEditorListener)listeners.elementAt(i)).editingCanceled(ce);  
		}  
	}  

	protected void fireEditingStopped(){  
		ChangeEvent ce = new ChangeEvent(this);  
		for(int i=0; i<listeners.size(); i++){  
			((CellEditorListener)listeners.elementAt(i)).editingStopped(ce);  
		}  
	}
}  

class EditableCellRenderer extends DefaultTableCellRenderer {
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Font customFont = null;
	    public Font getCustomFont() {return customFont;	}
		public void setCustomFont(Font customFont) {	this.customFont = customFont;	}

	HashSet<Integer> cells_with_errors = new HashSet<Integer>();
	HashSet<Integer> cells_with_defaults = new HashSet<Integer>();
	
	private boolean row_with_default;

	public void cell_has_defaults(int row) { cells_with_defaults.add(row); }
	public void cell_no_defaults(int row) { cells_with_defaults.remove(row); }
	public boolean isCellWithDefaults(int row) { return cells_with_defaults.contains(row);}
	
	public void cell_has_errors(int row) { 
		cells_with_errors.add(row); }
	public void cell_no_errors(int row) { cells_with_errors.remove(row); }
	public boolean isCellWithError(int row) {	return cells_with_errors.contains(row);	}
	
	/*   protected void paintComponent(Graphics g) {
		      super.paintComponent(g);
		    
		  * if(row_with_default){
				Color COLOR_1 = new Color(255, 0, 0, 200);
			
				Color COLOR_2 = new Color(0, 0, 255, 200);
				float SIDE = 50;

			GradientPaint gradientPaint = new GradientPaint(0, 0, COLOR_1, SIDE,SIDE, COLOR_2, true);
		      Graphics2D g2 = (Graphics2D) g;
		      g2.setPaint(gradientPaint);
		      g2.fillRect(0, 0, getWidth(), getHeight());
		   }*
	   }*/


	
	public void printCellsWithErrors() {
		System.out.println(cells_with_errors);
	}
	
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		
	  super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	
			if(cells_with_errors.contains(row)) {
				setBackground(MainGui.color_cell_with_errors);
			} else {
			
				if (row % 2 != 0) {
						setBackground(MainGui.color_shading_table);
					} else {
						setBackground(Color.white);
					}
			}
				CustomTableModel mod = (CustomTableModel)(((CustomJTable)table).getModel());
				String descr = mod.getTableName();
				String def = null;
				if(descr.compareTo(Constants.TitlesTabs.SPECIES.description)==0) {	mod = MainGui.tableSpeciesmodel;
					if(column == Constants.SpeciesColumns.INITIAL_QUANTITY.index) def = MainGui.species_defaultInitialValue;
					if(column == Constants.SpeciesColumns.COMPARTMENT.index) def = MainGui.compartment_default_for_dialog_window;
				}
				else if(descr.compareTo(Constants.TitlesTabs.COMPARTMENTS.description)==0) {	mod = MainGui.tableCompartmentsmodel;	
				if(column == Constants.CompartmentsColumns.INITIAL_SIZE.index) def = MainGui.compartment_defaultInitialValue;
				if(column == Constants.CompartmentsColumns.NAME.index) def = MainGui.compartment_default_for_dialog_window;
				}
				else if(descr.compareTo(Constants.TitlesTabs.EVENTS.description)==0) {	mod = MainGui.tableEventsmodel;	}
				else if(descr.compareTo(Constants.TitlesTabs.GLOBALQ.description)==0) {	
					mod = MainGui.tableGlobalQmodel;	
					if(column == Constants.GlobalQColumns.VALUE.index) def = MainGui.globalQ_defaultValue_for_dialog_window;
				}
				else if(descr.compareTo(Constants.TitlesTabs.REACTIONS.description)==0) {	mod = MainGui.tableReactionmodel;	}

				/*if(def != null) {
					if(mod.getValueAt(row, column).toString().compareTo(def)==0)	row_with_default = this.cells_with_defaults.contains(row);
					else row_with_default = false;
				} else {
					row_with_default = false;
				}*/
			
			
			
			//if(row_with_default) {
			if(this.cells_with_defaults.contains(row)){
				//setBorder(BorderFactory.createSoftBevelBorder(BevelBorder.RAISED, Constants.color_cell_with_errors, Constants.color_cell_with_errors));
				Border compound = null;
				Border redline = BorderFactory.createLineBorder(MainGui.color_border_defaults,3);
				compound = BorderFactory.createCompoundBorder(redline, compound);
				setBorder(compound);
			} 
			
			if(customFont!=null)  setFont(customFont);
			return this;

	}
	
	public void clearCellsWithErrors() { cells_with_errors.clear();	}
	public void clearCellsWithDefaults() { cells_with_defaults.clear();	}


}





class PopUpViewActionListener implements ActionListener {

	int whichView = Constants.Views.EDITABLE.index;
	int column = 0;
	int row = 0;
	String table = new String();
	
	public PopUpViewActionListener(int view) {
		super();
		this.whichView = view;
		
	}
	
	public int getColumn() {return column;}
	public void setColumn(int column) {		this.column = column;	}
	public int getRow() {		return row;	}
	public void setRow(int row) {		this.row = row;	}
	public String getTable() {		return table;	}
	public void setTable(String table) {		this.table = table;	}

	public void actionPerformed(ActionEvent e) {
			 try {
				 MainGui.setView(whichView,table,row,column);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		  }
  }


class ScientificFormatCellRenderer extends EditableCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	DecimalFormat sci_decimalFormat;
	DecimalFormat basic_decimalFormat;


	
	

	public ScientificFormatCellRenderer() {
		Locale currentLocale = new Locale("en", "US");

		DecimalFormatSymbols symbols =   new DecimalFormatSymbols(currentLocale);
		
		sci_decimalFormat = new DecimalFormat("0.###E00",symbols);
		basic_decimalFormat = new DecimalFormat("#0.0###",symbols);
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		try{
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			 
					Double parsed = Double.parseDouble(value.toString());
			if ((parsed < 10000.0 && parsed > 0.0001) || parsed == 0.0 ) this.setText(basic_decimalFormat.format(value));
			else this.setText(sci_decimalFormat.format(value));
		} catch(Exception ex) {
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) ex.printStackTrace();
			this.setText(value.toString());
		}
		if(customFont!=null)   setFont(customFont);
		return this;

	}
	
	
}


/*

class KeyEventListener  implements KeyListener {

    public void keyTyped(KeyEvent e) {
    	displayInfo(e, "KEY TYPED: ");
    
    }

    public void keyPressed(KeyEvent e) {
    	displayInfo(e, "KEY PRESSED: ");
    }

    public void keyReleased(KeyEvent e) {
    	displayInfo(e, "KEY RELEASED: ");
    }
   
    private void displayInfo(KeyEvent e, String keyStatus){
        
        //You should only rely on the key char if the event
        //is a key typed event.
        int id = e.getID();
        System.out.println(keyStatus);
        String keyString;
        int keyCode = e.getKeyCode();
            keyString = "key code = " + keyCode
                    + " ("
                    + KeyEvent.getKeyText(keyCode)
                    + ")";
            if(e.getKeyCode() != 10) {
            	//System.out.println(keyString);
            	return;
            } else {
                System.out.println(((gui.CustomJTable)e.getComponent()).getSelectedRow());
                System.out.println(((gui.CustomJTable)e.getComponent()).getSelectedColumn());
            }
        System.out.println(keyString);
        
        int modifiersEx = e.getModifiersEx();
        String modString = "extended modifiers = " + modifiersEx;
        String tmpString = KeyEvent.getModifiersExText(modifiersEx);
        if (tmpString.length() > 0) {
            modString += " (" + tmpString + ")";
        } else {
            modString += " (no extended modifiers)";
        }
        
        String actionString = "action key? ";
        if (e.isActionKey()) {
            actionString += "YES";
        } else {
            actionString += "NO";
        }
        
        String locationString = "key location: ";
        int location = e.getKeyLocation();
        if (location == KeyEvent.KEY_LOCATION_STANDARD) {
            locationString += "standard";
        } else if (location == KeyEvent.KEY_LOCATION_LEFT) {
            locationString += "left";
        } else if (location == KeyEvent.KEY_LOCATION_RIGHT) {
            locationString += "right";
        } else if (location == KeyEvent.KEY_LOCATION_NUMPAD) {
            locationString += "numpad";
        } else { // (location == KeyEvent.KEY_LOCATION_UNKNOWN)
            locationString += "unknown";
        }
        	System.out.println(keyString);
                System.out.println(modString);
                System.out.println(locationString);
        
       
    }
}*/

