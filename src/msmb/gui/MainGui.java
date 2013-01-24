//VM ARGUMENTS -Djava.library.path="${project_loc:MSMB}\libs"
//-Xms1024m -Xmx1024m
//-splash:"${project_loc:MSMB}\bin\gui\images\splashScreen.png"
//-----------------------------------------------------------------------------------
		
package  msmb.gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.io.*;
import java.lang.reflect.Field;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.tree.*;
import org.apache.commons.lang3.tuple.MutablePair;
import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.Completion;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.TreeTableNode;
import org.COPASI.*;

import examples.Main;

//import com.sun.tools.apt.Main;

import  msmb.parsers.chemicalReaction.MR_ChemicalReaction_Parser;
import  msmb.parsers.chemicalReaction.syntaxtree.CompleteReaction;
import  msmb.parsers.chemicalReaction.visitor.ExtractNamesSpeciesUsedVisitor;
import  msmb.parsers.mathExpression.MR_Expression_Parser;
import  msmb.parsers.mathExpression.MR_Expression_ParserConstants;
import msmb.parsers.mathExpression.MR_Expression_ParserConstantsNOQUOTES;
import  msmb.parsers.mathExpression.MR_Expression_Parser_ReducedParserException;
import  msmb.parsers.mathExpression.syntaxtree.CompleteExpression;
import  msmb.parsers.mathExpression.syntaxtree.SingleFunctionCall;
import  msmb.parsers.mathExpression.visitor.ExpressionVisitor;
import  msmb.parsers.mathExpression.visitor.ExtractNamesUsedVisitor;
import msmb.parsers.mathExpression.visitor.GetElementBeforeSpecialExtension;
import msmb.parsers.mathExpression.visitor.GetFunctionCallsInEquation;
import  msmb.parsers.mathExpression.visitor.GetFunctionNameVisitor;
import  msmb.parsers.mathExpression.visitor.GetFunctionParametersVisitor;
import msmb.parsers.mathExpression.visitor.GetUsedVariablesInEquation;
import msmb.parsers.mathExpression.visitor.MR_SubstitutionVisitor;
import msmb.parsers.mathExpression.visitor.SwapParametersVisitor;
import msmb.parsers.mathExpression.visitor.ToStringVisitor;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.awt.print.*;

import msmb.model.*;
import msmb.utility.*;
import msmb.utility.Constants.FunctionParamType;
import msmb.utility.Constants.TitlesTabs;
import msmb.commonUtilities.MSMB_Interface;
import msmb.commonUtilities.MSMB_InterfaceChange;
import msmb.commonUtilities.tables.CustomColumn0Renderer;
import msmb.commonUtilities.tables.ScientificFormatCellRenderer;
import msmb.commonUtilities.tables.UnquotingCellEditor;
import msmb.debugTab.*;


public class MainGui extends JFrame implements MSMB_Interface {
	
	public JTabbedPane getMSMB_MainTabPanel() {	return jTabGeneral; }
	public JMenuBar getMSMB_MenuBar() {	return jMenuBar;}
	public void importFromCopasiKey(String copasiKey) throws Exception {loadCopasiDataModel_fromKey(copasiKey);}
	public Vector<String> getMSMB_listOfSpecies() { return multiModel.getAllSpecies_names();}
	public Vector<String> getMSMB_listOfGlobalQuantities() { return multiModel.getAllGlobalQuantities_names();}
	public Vector<String> getMSMB_listOfCompartments() { return multiModel.getAllCompartments_names();}
	
	static HashMap<Class, ChangeListener> changeListeners = new HashMap<Class, ChangeListener>();
	
	public void addChangeListener(ChangeListener c, Class cl) {
		changeListeners.put(cl, c); 
	}
	public static void setChangeToReport(MSMB_InterfaceChange changeToReport) {
		if(changeListeners.get(changeToReport.getType())!= null) changeListeners.get(changeToReport.getType()).stateChanged(new ChangeEvent(changeToReport));
	}
	
	public static Font customFont = GraphicalProperties.customFont;
	private static final long serialVersionUID = 8L;
	
	private static final boolean DEBUG_SAVE_AT_EACH_STEP = false;
	private static int DEBUG_STEP = 0;
	public static final boolean DEBUG_SHOW_PRINTSTACKTRACES = false;
	public static boolean FULL_BRACKET_EXPRESSION = false;
	public Font getCustomFont() {return GraphicalProperties.customFont;}
	
	public void setCustomFont(int size) {
		if(customFont!=null) {
			
			MSMB_InterfaceChange changeToReport = new MSMB_InterfaceChange(Font.class);
			changeToReport.setElementBefore(new Font(customFont.getName(), customFont.getStyle(), customFont.getSize()));
			
			GraphicalProperties.customFont = new Font(GraphicalProperties.customFont.getName(), Font.PLAIN, size);
			customFont = GraphicalProperties.customFont;
			changeToReport.setElementAfter(new Font(customFont.getName(), customFont.getStyle(), customFont.getSize()));
				
			setChangeToReport(changeToReport);
			//if(changeListeners.get(Font.class)!= null) changeListeners.get(Font.class).stateChanged(new ChangeEvent(changeToReport));
			
			/*for(int i = 0; i < jMenuBar.getComponentCount();i++) {
				Component c = jMenuBar.getComponent(i);
				if(c instanceof JMenu) {
					JMenu menu = ((JMenu)c);
					menu.setFont(customFont);
					for(int i1 = 0; i1 < menu.getMenuComponentCount();i1++) {
						Component c1 = menu.getMenuComponent(i1);
						if(c1 instanceof JMenuItem) {
							JMenuItem menu1 = ((JMenuItem)c1);
							menu1.setFont(customFont);
						}
					}
				}
			}*/
			

			
			jTabGeneral.setFont(customFont);

			statusBar.setFont(customFont);
			jTableCompartments.getTableHeader().setFont(customFont);
			jTableEvents.getTableHeader().setFont(customFont);
			jTableFunctions.getTableHeader().setFont(customFont);
			jTableBuiltInFunctions.getTableHeader().setFont(customFont);
			jTableGlobalQ.getTableHeader().setFont(customFont);
			jTableReactions.getTableHeader().setFont(customFont);
			jTableSpecies.getTableHeader().setFont(customFont);
			
			jTableCompartments.setCustomFont(customFont);
			jTableEvents.setCustomFont(customFont);
			jTableFunctions.setCustomFont(customFont);
			jTableGlobalQ.setCustomFont(customFont);
			jTableReactions.setCustomFont(customFont);
			jTableSpecies.setCustomFont(customFont);
			jTableBuiltInFunctions.setCustomFont(customFont);
			

			Vector<Object> tables = new Vector<>();
			tables.add(jTableCompartments);
			tables.add(jTableEvents);
			tables.add(jTableFunctions);
			tables.add(jTableBuiltInFunctions);
			tables.add(jTableGlobalQ);
			tables.add(jTableReactions);
			tables.add(jTableSpecies);
			for (Object t : tables) {
				
				if(t instanceof CustomJTable_MSMB) {
					CustomJTable_MSMB table = (CustomJTable_MSMB) t;

					for(int i = 0; i < table.getColumnCount(); i++) {
						TableColumnModel colModel = table.getColumnModel();
						TableCellEditor e = colModel.getColumn(i).getCellEditor();
						if(e!= null){
							if(e instanceof UnquotingCellEditor) {	((UnquotingCellEditor)e).setCustomFont(customFont); }
							else {
								if(e instanceof ExpressionsCellEditor) {	((ExpressionsCellEditor)e).setCustomFont(customFont); }
								//else {	System.out.println("editor " + e.getClass()); }
							}
						}
						TableCellRenderer r = colModel.getColumn(i).getCellRenderer();
						if(r!=null) {
							if(r instanceof EditableCellRenderer_MSMB) {	((EditableCellRenderer_MSMB)r).setCustomFont(customFont); }
							else {
								if(r instanceof ScientificFormatCellRenderer) {	((ScientificFormatCellRenderer)r).setCustomFont(customFont); }
								else if(r instanceof CustomColumn0Renderer) {	((CustomColumn0Renderer)r).setCustomFont(customFont); }
								//else { 	System.out.println("renderer " + r.getClass()); }
							}
						}
					}
				} 
			}


			this.revalidate();
			this.repaint();
		}
	}
	
	
	static boolean modelHasBeenModified = false;
	
	public static String globalQ_defaultValue_for_dialog_window = Constants.DEFAULT_GLOBALQ_INITIAL_VALUE;
	public static String compartment_default_for_dialog_window = new String(Constants.DEFAULT_COMPARTMENT_NAME);
	public static String species_defaultInitialValue = Constants.DEFAULT_SPECIES_INITIAL_VALUE;
	public static String compartment_defaultInitialValue = Constants.DEFAULT_COMPARTMENT_INITIAL_VALUE;
	public static Vector<String> species_default_for_dialog_window  = new Vector<String>();
	public static boolean autocompleteWithDefaults = true;
	public static boolean show_defaults_dialog_window = true;
	public static int renamingOption = Constants.RENAMING_OPTION_ALL;
	public static boolean showAllAvailableFunctions = true;
	
	private String autosavePath = new String();
	private int autosaveTimeMilliseconds = 5*60*1000;
	private boolean isAutosaveActive = true;

	public boolean isAutosaveActive() { return isAutosaveActive;}
	public void setAutosaveActive(boolean value) {isAutosaveActive = value;}
	public void setAutosaveTimeMin(Integer value) {
		recordAutosave.stopAutosave();
		autosaveTimeMilliseconds = value*60*1000;
		recordAutosave.startAutosave();
	}
	public int getAutosaveTimeMin() {	return autosaveTimeMilliseconds/(1000*60);}
	public int getAutosaveTime() {	return autosaveTimeMilliseconds;}
	public String getAutosaveDirectory() { return autosavePath;	}
	public void setAutosaveDirectory(String s ) {  
		recordAutosave.stopAutosave();
		autosavePath = new String(s.trim());
		if(autosavePath.length()!=0 && !autosavePath.endsWith("/")) autosavePath +="/";
		recordAutosave.startAutosave();
		return;
	}
	
	
	static StatusBar statusBar = null;
	private String file_RecentFiles = new String();
	static String file_preferences = new String();
	private Vector<String> recentFiles = new Vector<String>(); 
	
	
	
	boolean modify_multistate_species_from_builder = false;
	public static boolean loadedExisting = false;
	public static boolean fromMainGuiTest = false; 
	private boolean actionInColumnName = false;
	static String cellValueBeforeChange = new String();
	static int cellSelectedRow= 0;
	static int cellSelectedCol = 0;
	static String cellTableEdited = new String();
	public static boolean quantityIsConc = true;
	public static MutablePair<Integer, Integer> cell_to_highlight;
	public static Vector<Integer> row_to_highlight; //one integer index for each table (same order as in the TitleTabs)
	private static File fileCurrentlyLoaded = null;
	protected static DebugMessage toBeAck_debugMessage;
	private static HashSet<String> old_acknowledged = new HashSet<String>();
	static boolean addedByReaction = false;
	static boolean hidePopupWarning = false;
	public static HashMap<String, DebugMessage> debugMessages = new HashMap<String, DebugMessage>();

	public static HashMap<String, String> cleanNamesPredefinedFunctions = null;

	Vector<Component> addedTabs = new Vector<Component>();
	private static RenamingFrame renamingFrame;
	private MultistateBuilderFrame multiBuilderFrame;
	private MainGui frame;
	private FunctionParameterFrame functionParameterFrame;
	private PreferencesFrame preferenceFrame;
	
	protected static MultiModel multiModel;
	
	public static int timeUnit = 0;
	public static int volumeUnit = 0;
	public static int quantityUnit = 0;
	public static String modelName = new String(Constants.DEFAULT_MODEL_NAME);
	public static boolean exportConcentration = true;
		
	
	private JComboBox<String> comboBoxReactionType = new JComboBox<String>();
	private JComboBox<String> comboBoxSpeciesType = new JComboBox<String>();
	private JComboBox<String> comboBoxCompartmentsType = new JComboBox<String>();
	private JComboBox<String> comboBoxGlobalQType = new JComboBox<String>();
	private JComboBox<String> comboBox_unitVolume;
	private JComboBox<String> comboBox_unitTime;
	private JComboBox<String> comboBox_unitQuantity;
	
	private JCheckBox jCheckBoxExportConcentration;
	private JCheckBox jCheckBoxQuantityIsConcentration;
	
	static JTabbedPane jTabGeneral = null;
	
	static JPopupMenu popupDebugMessageActions;
	
	private JPanel jContentPane = null;
	private JPanel jPanelReactions = null;
	private JPanel jPanelSpecies = null;
	private JPanel jPanelGlobalQ = null;
	private JPanel jPanelFunctions = null;
	private JPanel jPanelCompartments = null;
	private JPanel jPanelEquations = null;
	private JPanel jPanelDebug = null;
	private JPanel jPanelModelProperties_1;
	private JPanel jPanelEvents = null;
	private JPanel jPanelUnitMeasure;
	private JPanel jPanelODEs;
	private JPanel jPanelListFunctionToCompact;
	private JPanel jPanelModelName;
	private static TreeDebugMessages jPanelTreeDebugMessages = null;
	
	private JScrollPane jScrollPaneTableReactions = null;
	private JScrollPane jScrollPaneTableSpecies = null;
	private JScrollPane jScrollPaneTableGlobalQ = null;
	private JScrollPane jScrollPaneTableFunctions = null;
	private JScrollPane jScrollPaneTableCompartments = null;
	private JScrollPane jScrollPane1 = null;
	private JScrollPane jScrollPaneTableEvents;
	private JScrollPane scrollPaneTextAreaODEs;
	private static JScrollPane scrollPaneListFunctionToCompact;
	
	private JMenuBar jMenuBar = null;
	private JMenu recentMenu;
	public static JMenuItem ackMenuItem;
	private JMenuItem preferencesMenu;
	
	private Thread progressBarThread;
	private ProgressBarFrame progressBarFrame;
	
	private JFileChooser cpsFileChooser = new JFileChooser();
	private JFileChooser sbmlFileChooser = new JFileChooser();
	private JFileChooser xppFileChooser = new JFileChooser();
	
	private JButton jButtonDelectAllFunctionToCompact;
	private JButton jButtonSelectAllFunctionToCompact;
	
	private static JTextArea jTextAreaDebugComplete = null;
	private JTextArea jTextAreaODEs;
	
	private JTextField textFieldModelName;

	private boolean autoMergeSpecies = false;
	private RecordAutosave recordAutosave;
	private JMenuItem menu_reverse;
	private JMenuItem itemPrintPDF;
	private JMenu menuFile;
	private JMenu menuEdit;
	private String inputFile = new String(Constants.AUTOSAVE_UNTITLED);
	private JMenu submenuShowExpandedExpression;
	private JCheckBoxMenuItem menu_expandedExpr_Reactions;
	private JMenuItem menu_expandedExpr_Species;
	private JCheckBoxMenuItem menu_expandedExpr_GlobalQ;
	private JCheckBoxMenuItem menu_expandedExpr_Compartments;
	private JCheckBoxMenuItem menu_expandedExpr_Events;
	private JSplitPane splitPaneFunctions;
	static CustomJTable_MSMB jTableBuiltInFunctions;
	private JScrollPane jScrollPaneTableBuiltInFunctions;

	public static boolean importFromTables = false;
	public static boolean importFromSBMLorCPS = false;
	
	static CustomJTable_MSMB jTableReactions = null;
	public static CustomTableModel_MSMB tableReactionmodel = null;
	static CustomJTable_MSMB jTableSpecies = null;
	static CustomTableModel_MSMB tableSpeciesmodel = null;
		
	static CustomJTable_MSMB jTableGlobalQ = null;
	public static CustomTableModel_MSMB tableGlobalQmodel = null;
	static CustomJTable_MSMB jTableFunctions = null;
	public static CustomTableModel_MSMB tableFunctionsmodel = null;
	public static CustomTableModel_MSMB tableBuiltInFunctionsmodel = null;
	public static CustomJTable_MSMB jTableCompartments = null;
	public static CustomTableModel_MSMB tableCompartmentsmodel = null;
	static CustomJTable_MSMB jTableEvents;
	public static CustomTableModel_MSMB tableEventsmodel = null;
	
	public static JList<String> jListFunctionToCompact;
	public static DefaultListModel<String> listModel_FunctionToCompact;

	public static boolean donotCleanDebugMessages = false;
	private static int tabSelected;
	
		
	/*//UndoManager generalUndoManager = new UndoManager();
	   UndoManager undoManagerReactions = new UndoManager();
	    UndoManager undoManagerSpecies = new UndoManager();
	     UndoManager undoManagerCompartment = new UndoManager();
	     UndoManager undoManagerGlobalQ = new UndoManager();
	     UndoManager undoManagerEvents = new UndoManager();
	     UndoManager undoManagerFunctions = new UndoManager();*/
		

	
	public void addNewTab(String title, Component component)
	{
	   jTabGeneral.addTab(title, null, component, null);
	   addedTabs.add(component);
	} 
	
	public void focusAddedTab(int index) {
		jTabGeneral.setSelectedIndex(TitlesTabs.getNumTabs() + index);
	}
		
	private void getJTabGeneral() {
			jTabGeneral = new JTabbedPane();
			
			jTabGeneral.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			jTabGeneral.addTab(Constants.TitlesTabs.REACTIONS.description, null, getJPanelReactions(), null);
			jTabGeneral.addTab(Constants.TitlesTabs.SPECIES.description, null, getJPanelSpecies(), null);
			jTabGeneral.addTab(Constants.TitlesTabs.GLOBALQ.description, null, getJPanelGlobalQ(), null);
			jTabGeneral.addTab(Constants.TitlesTabs.FUNCTIONS.description, null, getJPanelFunctions(), null);
			jTabGeneral.addTab(Constants.TitlesTabs.EVENTS.description, null, getJPanelEvents(), null);
			jTabGeneral.addTab(Constants.TitlesTabs.COMPARTMENTS.description, null, getJPanelCompartments(), null);
			jTabGeneral.addTab(Constants.TitlesTabs.EQUATIONS.description, null, getJPanelEquations(), null);
			jTabGeneral.setEnabledAt(Constants.TitlesTabs.EQUATIONS.index,true);
			jTabGeneral.addTab(Constants.TitlesTabs.DEBUG.description, null, getJPanelDebug(), null);
			//jTabGeneral.setBackgroundAt(6, GraphicalProperties.color_debug_none);
			
			
			
			jTabGeneral.addChangeListener(new ChangeListener() {
			    // This method is called whenever the selected tab changes
			    public void stateChanged(ChangeEvent evt) {
			    	MainGui.cell_to_highlight = null;
			    	JTabbedPane pane = (JTabbedPane)evt.getSource();
					int sel = pane.getSelectedIndex();
					
					if(Constants.TitlesTabs.isMSMBtab(sel)){
						MainGui.tabSelected = -1;
						return;
					}
					MainGui.tabSelected = sel;
			        if(sel == Constants.TitlesTabs.EQUATIONS.index) {
			        	try {
			        		validateMultiModel(false,true);
							jTextAreaODEs.setText(
									multiModel.getDifferentialEquations(new Vector<String>())
							);
							scrollPaneTextAreaODEs.revalidate();
						} catch (Exception e1) {
							if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e1.printStackTrace();
							jTextAreaODEs.setText("");
							scrollPaneTextAreaODEs.revalidate();
						}
			        } else {
			        
			        	int selectedRow = MainGui.row_to_highlight.get(sel);
			        	if(selectedRow != -1) {
			        		if(sel == Constants.TitlesTabs.SPECIES.index) { 
			        			jTableSpecies.setRowSelectionInterval(selectedRow, selectedRow);  
			        			jTableSpecies.scrollRectToVisible(new Rectangle(jTableSpecies.getCellRect(selectedRow, 0, true)));  
			        			jTableSpecies.revalidate(); 	
			        		}
			        		else if(sel == Constants.TitlesTabs.REACTIONS.index) { 
			        			jTableReactions.setRowSelectionInterval(selectedRow, selectedRow);  
			        			jTableReactions.scrollRectToVisible(new Rectangle(jTableReactions.getCellRect(selectedRow, 0, true)));  
					        	jTableReactions.revalidate(); 	
					        }
			        		else if(sel == Constants.TitlesTabs.COMPARTMENTS.index) {
			        			jTableCompartments.setRowSelectionInterval(selectedRow, selectedRow);  
			        			jTableCompartments.scrollRectToVisible(new Rectangle(jTableCompartments.getCellRect(selectedRow, 0, true)));  
			        			jTableCompartments.revalidate(); 	
			        		}
			        		else if(sel == Constants.TitlesTabs.EVENTS.index) { 
			        			jTableEvents.setRowSelectionInterval(selectedRow, selectedRow);   
			        			jTableEvents.scrollRectToVisible(new Rectangle(jTableEvents.getCellRect(selectedRow, 0, true)));  
			        			jTableEvents.revalidate(); 
			        		}
			        		else if(sel == Constants.TitlesTabs.FUNCTIONS.index) { 
			        			jTableFunctions.setRowSelectionInterval(selectedRow, selectedRow);   
			        			jTableFunctions.scrollRectToVisible(new Rectangle(jTableFunctions.getCellRect(selectedRow, 0, true)));  
			        			jTableFunctions.revalidate(); 	
			        		}
			        		else if(sel == Constants.TitlesTabs.GLOBALQ.index) {
			        			jTableGlobalQ.setRowSelectionInterval(selectedRow, selectedRow); 
			        			jTableGlobalQ.scrollRectToVisible(new Rectangle(jTableGlobalQ.getCellRect(selectedRow, 0, true)));  
			        			jTableGlobalQ.revalidate(); 	
			        		}
			        	}
			        }
			        if(menu_reverse!= null) {
				        if(sel != Constants.TitlesTabs.REACTIONS.index) {
				        	 menu_reverse.setEnabled(false);
				        } else {
				        	menu_reverse.setEnabled(true);
				        }
			        }
			    }
			});
	}

	protected static void resetViewsInExpressions() {
	
		try{
			int tabSelected = jTabGeneral.getSelectedIndex();
		
			if(MainGui.cellTableEdited.compareTo(Constants.TitlesTabs.SPECIES.description) ==0) {	
				//CustomTableModel table = tableSpeciesmodel;
				CustomTableModel_MSMB table = tableSpeciesmodel;
				for(int i = 0; i < table.getRowCount()-1; i++) {
					MainGui.donotCleanDebugMessages = true;
					setView(Constants.Views.EDITABLE.index,table.getTableName(),i,Constants.SpeciesColumns.INITIAL_QUANTITY.index);
					MainGui.donotCleanDebugMessages = true;
					setView(Constants.Views.EDITABLE.index,table.getTableName(),i,Constants.SpeciesColumns.EXPRESSION.index);
				}

			} 
			else if(MainGui.cellTableEdited.compareTo(Constants.TitlesTabs.REACTIONS.description) ==0) {	
				CustomTableModel_MSMB table = tableReactionmodel;
				for(int i = 0; i < table.getRowCount()-1; i++) {
					MainGui.donotCleanDebugMessages = true;
					setView(Constants.Views.EDITABLE.index,table.getTableName(),i,Constants.ReactionsColumns.KINETIC_LAW.index);
				}
			}
			else if(MainGui.cellTableEdited.compareTo(Constants.TitlesTabs.COMPARTMENTS.description) ==0) {	
				CustomTableModel_MSMB table = tableCompartmentsmodel;
				for(int i = 0; i < table.getRowCount()-1; i++) {
					MainGui.donotCleanDebugMessages = true;
					setView(Constants.Views.EDITABLE.index,table.getTableName(),i,Constants.CompartmentsColumns.INITIAL_SIZE.index);
					MainGui.donotCleanDebugMessages = true;
					setView(Constants.Views.EDITABLE.index,table.getTableName(),i,Constants.CompartmentsColumns.EXPRESSION.index);
				}
			}
			else if(MainGui.cellTableEdited.compareTo(Constants.TitlesTabs.EVENTS.description) ==0) {	
				CustomTableModel_MSMB table = tableEventsmodel;
				for(int i = 0; i < table.getRowCount()-1; i++) {
					MainGui.donotCleanDebugMessages = true;
					setView(Constants.Views.EDITABLE.index,table.getTableName(),i,Constants.EventsColumns.ACTIONS.index);
					MainGui.donotCleanDebugMessages = true;
					setView(Constants.Views.EDITABLE.index,table.getTableName(),i,Constants.EventsColumns.TRIGGER.index);
					MainGui.donotCleanDebugMessages = true;
					setView(Constants.Views.EDITABLE.index,table.getTableName(),i,Constants.EventsColumns.DELAY.index);
				}
			}
			else if(MainGui.cellTableEdited.compareTo(Constants.TitlesTabs.GLOBALQ.description) ==0) {	
				CustomTableModel_MSMB table = tableGlobalQmodel;
				for(int i = 0; i < table.getRowCount()-1; i++) {
					MainGui.donotCleanDebugMessages = true;
					setView(Constants.Views.EDITABLE.index,table.getTableName(),i,Constants.GlobalQColumns.VALUE.index);
					MainGui.donotCleanDebugMessages = true;
					setView(Constants.Views.EDITABLE.index,table.getTableName(),i,Constants.GlobalQColumns.EXPRESSION.index);
				}
			}
		
			jTabGeneral.setSelectedIndex(tabSelected);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			MainGui.donotCleanDebugMessages = false;
		}
	}

	private JPanel getJPanelCompartments() {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.gridx = 0;
			jPanelCompartments = new JPanel();
			jPanelCompartments.setLayout(new GridBagLayout());
			jPanelCompartments.add(getJScrollPaneTableCompartments(), gridBagConstraints);
		return jPanelCompartments;
	}
	
	private JPanel getJPanelEquations() {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.gridx = 0;
			jPanelEquations = new JPanel();
			jPanelEquations.setLayout(new BorderLayout());
			
			listModel_FunctionToCompact = new DefaultListModel<String>();
	    	jListFunctionToCompact = new JList<String>(listModel_FunctionToCompact);
	    	jListFunctionToCompact.setVisibleRowCount(-1);
	    	
	    	jTextAreaODEs = new JTextArea();
	    	
	    	jTextAreaODEs.setEditable(false);
	    	jTextAreaODEs.setFont(new Font("Dialog", Font.PLAIN, 12));
	    	jTextAreaODEs.setWrapStyleWord(false);
	    	
	    	scrollPaneTextAreaODEs = new JScrollPane(jTextAreaODEs);
	    	
	    	scrollPaneListFunctionToCompact = new JScrollPane(jListFunctionToCompact);
	    	scrollPaneListFunctionToCompact.setMinimumSize(new Dimension(30,30));
	    	scrollPaneListFunctionToCompact.setPreferredSize(new Dimension(30,30));
	 		
			jListFunctionToCompact.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			
			jListFunctionToCompact.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
				public void valueChanged(javax.swing.event.ListSelectionEvent e) {
					try {
						jTextAreaODEs.setText(
								multiModel.getDifferentialEquations(jListFunctionToCompact.getSelectedValuesList())
						);
						scrollPaneTextAreaODEs.revalidate();
					} catch (Exception e1) {
						if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e1.printStackTrace();
						
					}
				}
			});
			
					
	    	jPanelListFunctionToCompact = new JPanel();
	    	jPanelListFunctionToCompact.setLayout(new BorderLayout());
	    	JPanel panButton = new JPanel();
	    	panButton.setLayout(new FlowLayout());
	    	
	    	jButtonSelectAllFunctionToCompact = new JButton("Select All");
	    	jButtonDelectAllFunctionToCompact = new JButton("Deselect All");
	    	
	    	jButtonSelectAllFunctionToCompact.addActionListener( new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					jListFunctionToCompact.setSelectionInterval(0, listModel_FunctionToCompact.size()-1);
					scrollPaneListFunctionToCompact.revalidate();
				}
			});
	    	
	    	jButtonDelectAllFunctionToCompact.addActionListener( new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					jListFunctionToCompact.clearSelection();
					scrollPaneListFunctionToCompact.revalidate();
				}
			});
	    	
	    	panButton.add(jButtonSelectAllFunctionToCompact);
	    	panButton.add(jButtonDelectAllFunctionToCompact);
	    	
	    	JPanel panButton2 = new JPanel();
	    	panButton2.setLayout(new FlowLayout());    	
	    	
	    	
			jPanelListFunctionToCompact.add(scrollPaneListFunctionToCompact, BorderLayout.CENTER);
			jPanelListFunctionToCompact.add(panButton, BorderLayout.NORTH);
			
			jPanelODEs = new JPanel();
			jPanelODEs.setLayout(new BorderLayout());
			
			jPanelODEs.add(scrollPaneTextAreaODEs, BorderLayout.CENTER);
			jPanelODEs.add(panButton2, BorderLayout.SOUTH);
			
			JSplitPane jSplitPane = new JSplitPane();
			jSplitPane.setLeftComponent(jPanelListFunctionToCompact);
			jSplitPane.setRightComponent(jPanelODEs);
			jSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
			jSplitPane.setPreferredSize(new Dimension(577, 260));
			jSplitPane.setDividerLocation(0.25);
			jSplitPane.setOneTouchExpandable(true);

			jPanelEquations.add(jSplitPane);
	       
			
		//}
		
		return jPanelEquations;
	}
	
	private JPanel getJPanelDebug() {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.gridx = 0;
			jPanelDebug = new JPanel();
			jPanelDebug.setLayout(new BorderLayout());
			jPanelDebug.add(getJPanelModelProperties(), BorderLayout.NORTH);
			jPanelModelProperties_1.add(getJCheckBoxQuantityIsConcentration());
			jPanelDebug.add(getJPaneTreeDebugMessages(), BorderLayout.CENTER);
		
			preferenceFrame.updateStatusAutocomplete();
			preferenceFrame.updateStatusDialogWindow();
		
		return jPanelDebug;
	}
	
	private JPanel getJPanelFunctions() {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.gridx = 0;
			jPanelFunctions = new JPanel();
			jPanelFunctions.setLayout(new GridBagLayout());
			jPanelFunctions.add(getJSplitPaneTableFunctions(), gridBagConstraints);
		return jPanelFunctions;
	}
	
	private JPanel getJPanelEvents() {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.gridx = 0;
			jPanelEvents = new JPanel();
			jPanelEvents.setLayout(new GridBagLayout());
			jPanelEvents.add(getJScrollPaneTableEvents(), gridBagConstraints);
		return jPanelEvents;
	}
	
	
	private JPanel getJPanelGlobalQ() {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.gridx = 0;
			jPanelGlobalQ = new JPanel();
			jPanelGlobalQ.setLayout(new GridBagLayout());
			jPanelGlobalQ.add(getJScrollPaneTableGlobalQ(), gridBagConstraints);
		return jPanelGlobalQ;
	}
	
	private JPanel getJPanelReactions() {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.gridx = 0;
			jPanelReactions = new JPanel();
			jPanelReactions.setLayout(new GridBagLayout());
			jPanelReactions.add(getJScrollPaneTableReactions(), gridBagConstraints);
			return jPanelReactions;
	}

	private JPanel getJPanelSpecies() {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.gridx = 0;
			jPanelSpecies = new JPanel();
			jPanelSpecies.setLayout(new GridBagLayout());
			jPanelSpecies.add(getJScrollPaneTableSpecies(), gridBagConstraints);
		return jPanelSpecies;
	}

	private JScrollPane getJScrollPaneTableCompartments() {
		jScrollPaneTableCompartments = new JScrollPane();
		jScrollPaneTableCompartments.setViewportView(getJTableCompartments());
		return jScrollPaneTableCompartments;
	}
	
	private JScrollPane getJScrollPaneTableReactions() {
			jScrollPaneTableReactions = new JScrollPane();
			try {
				jScrollPaneTableReactions.setViewportView(getJTableReactions());
			} catch (IOException e) {
				if(DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
			}
			jTableReactions.revalidate();
			jScrollPaneTableReactions.revalidate();
		return jScrollPaneTableReactions;
	}
	
	private JSplitPane getJSplitPaneTableFunctions() {
			jScrollPaneTableFunctions = new JScrollPane();
			jScrollPaneTableBuiltInFunctions = new JScrollPane();
			try {
				jScrollPaneTableFunctions.setViewportView(getJTableFunctions());
				jScrollPaneTableBuiltInFunctions.setViewportView(getJTableBuiltInFunctions());
					
				//Create a split pane with the two scroll panes in it.
				splitPaneFunctions = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
				splitPaneFunctions.setLeftComponent(jScrollPaneTableFunctions);
				splitPaneFunctions.setRightComponent(jScrollPaneTableBuiltInFunctions);
				
				splitPaneFunctions.setOneTouchExpandable(true);
				splitPaneFunctions.setDividerLocation(150);
				
				//Provide minimum sizes for the two components in the split pane
				Dimension minimumSize = new Dimension(100, 50);
				jScrollPaneTableFunctions.setMinimumSize(minimumSize);
				jScrollPaneTableFunctions.setMinimumSize(minimumSize);
				
				
			} catch (IOException e) {
				if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
			}
		return splitPaneFunctions;
	}
	
	private JScrollPane getJScrollPaneTableEvents() {
			jScrollPaneTableEvents = new JScrollPane();
			jScrollPaneTableEvents.setViewportView(getJTableEvents());
		return jScrollPaneTableEvents;
	}
	
	private JScrollPane getJScrollPaneTableGlobalQ() {
			jScrollPaneTableGlobalQ = new JScrollPane();
			jScrollPaneTableGlobalQ.setViewportView(getJTableGlobalQ());
		return jScrollPaneTableGlobalQ;
	}
	
	private JScrollPane getJScrollPaneTableSpecies() {
		jScrollPaneTableSpecies = new JScrollPane();
		jScrollPaneTableSpecies.setViewportView(getJTableSpecies());
		return jScrollPaneTableSpecies;
	}
	
	private JTable getJTableCompartments() {
			Vector col = new Vector(Constants.compartments_columns);
					
			if(tableCompartmentsmodel == null) tableCompartmentsmodel = new CustomTableModel_MSMB(Constants.TitlesTabs.COMPARTMENTS.description,col,new Vector(),this);
			jTableCompartments = new CustomJTable_MSMB();
			
		       jTableCompartments.getActionMap().put("Delete",
					    new AbstractAction() {
			        public void actionPerformed(ActionEvent evt) {
			          deleteSelected();
			        }
			   }); 
		          
	        jTableCompartments.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "Delete");
				
			jTableCompartments.setModel(tableCompartmentsmodel);
			
			jTableCompartments.initializeCustomTable(tableCompartmentsmodel);
			
			TableColumn typeColumn = jTableCompartments.getColumnModel().getColumn(Constants.CompartmentsColumns.TYPE.index);
			if(comboBoxCompartmentsType.getItemCount()==0) {
				for(int i = 0; i < Constants.compartmentsTypes.size(); i++) { 
					comboBoxCompartmentsType.addItem((String)Constants.compartmentsTypes.get(i));
				}
			}
	        typeColumn.setCellEditor(new DefaultCellEditor(comboBoxCompartmentsType));
	        typeColumn.setCellRenderer(new EditableCellRenderer_MSMB());
	    	
	        
	        TableColumn nameColumn = jTableCompartments.getColumnModel().getColumn(Constants.CompartmentsColumns.NAME.index);
			 nameColumn.setCellRenderer(new EditableCellRenderer_MSMB());
			 nameColumn.setCellEditor(new UnquotingCellEditor());
				
	        
	        TableColumn column = jTableCompartments.getColumnModel().getColumn(Constants.CompartmentsColumns.INITIAL_SIZE.index);  
	        column.setCellRenderer(new EditableCellRenderer_MSMB());
	        
	        TableColumn column2 = jTableCompartments.getColumnModel().getColumn(Constants.CompartmentsColumns.EXPRESSION.index);  
	        column2.setCellRenderer(new EditableCellRenderer_MSMB());
	        
	        JTextField withAutoComplete = new JTextField();
			AutoCompletion autoCompletion = new AutoCompletion(Constants.provider);
			 autoCompletion.setShowDescWindow(true);
			 autoCompletion.setAutoActivationEnabled(true);
			 autoCompletion.setAutoCompleteSingleChoices(false);
			 autoCompletion.setAutoCompleteEnabled(false);
			autoCompletion.install(withAutoComplete);
			withAutoComplete.addKeyListener(new AutoCompleteKeyLister(autoCompletion,multiModel,false));
			jTableCompartments.getColumnModel().getColumn(Constants.CompartmentsColumns.EXPRESSION.index).setCellEditor(new ExpressionsCellEditor(withAutoComplete));

			JTextField withAutoComplete2 = new JTextField();
			AutoCompletion autoCompletion2 = new AutoCompletion(Constants.provider);
			autoCompletion2.setShowDescWindow(true);
			autoCompletion2.setAutoActivationEnabled(true);
			autoCompletion2.setAutoCompleteSingleChoices(false);
			autoCompletion2.setAutoCompleteEnabled(false);
			autoCompletion2.install(withAutoComplete);
			withAutoComplete2.addKeyListener(new AutoCompleteKeyLister(autoCompletion,multiModel,false));
			jTableCompartments.getColumnModel().getColumn(Constants.CompartmentsColumns.INITIAL_SIZE.index).setCellEditor(new ExpressionsCellEditor(withAutoComplete2));
	
	   	 jTableCompartments.getColumnModel().getColumn(Constants.CompartmentsColumns.NAME.index).setCellEditor(new UnquotingCellEditor());
		 jTableCompartments.getColumnModel().getColumn(Constants.CompartmentsColumns.NOTES.index).setCellEditor(new UnquotingCellEditor());
			
	        return jTableCompartments;
	}
	
	private JTable getJTableGlobalQ() {
			Vector col = new Vector(Constants.globalQ_columns);
			
			if(tableGlobalQmodel == null) tableGlobalQmodel = new CustomTableModel_MSMB(Constants.TitlesTabs.GLOBALQ.description,col,new Vector(),this);
			jTableGlobalQ = new CustomJTable_MSMB();
			jTableGlobalQ.setModel(tableGlobalQmodel);
			
			jTableGlobalQ.initializeCustomTable(tableGlobalQmodel);
			
			TableColumn typeColumn = jTableGlobalQ.getColumnModel().getColumn(Constants.GlobalQColumns.TYPE.index);
			if(comboBoxGlobalQType.getItemCount()==0) {
				for(int i = 0; i < Constants.globalQTypes.size(); i++) { 
					comboBoxGlobalQType.addItem((String)Constants.globalQTypes.get(i));
				}
			}
		    typeColumn.setCellEditor(new DefaultCellEditor(comboBoxGlobalQType));
		    jTableGlobalQ.getColumnModel().getColumn(Constants.GlobalQColumns.TYPE.index).setCellRenderer(new EditableCellRenderer_MSMB());
		    
		    TableColumn column = jTableGlobalQ.getColumnModel().getColumn(Constants.GlobalQColumns.VALUE.index);  
	        column.setCellRenderer(new EditableCellRenderer_MSMB());
	        
	    	JTextField withAutoCompleteInitial = new JTextField();
	    	AutoCompletion autoCompletion = new AutoCompletion(Constants.provider);
			autoCompletion = new AutoCompletion(Constants.provider);
			autoCompletion.setShowDescWindow(true);
			autoCompletion.setAutoActivationEnabled(true);
			autoCompletion.setAutoCompleteSingleChoices(false);
			autoCompletion.setAutoCompleteEnabled(false);
			autoCompletion.install(withAutoCompleteInitial);
			withAutoCompleteInitial.addKeyListener(new AutoCompleteKeyLister(autoCompletion,multiModel,true));
			jTableGlobalQ.getColumnModel().getColumn(Constants.GlobalQColumns.VALUE.index).setCellEditor(new ExpressionsCellEditor(withAutoCompleteInitial));

			
	        jTableGlobalQ.getColumnModel().getColumn(Constants.GlobalQColumns.NAME.index).setCellRenderer(new EditableCellRenderer_MSMB());
	        jTableGlobalQ.getColumnModel().getColumn(Constants.GlobalQColumns.NAME.index).setCellEditor(new UnquotingCellEditor());
				  
		    TableColumn column2 = jTableGlobalQ.getColumnModel().getColumn(Constants.GlobalQColumns.EXPRESSION.index);  
	        column2.setCellRenderer(new EditableCellRenderer_MSMB());
			return jTableGlobalQ;
	}
	
	private JTable getJTableBuiltInFunctions() throws IOException {
		Vector col = new Vector(Constants.functions_columns);
		
		
		if(tableBuiltInFunctionsmodel == null)tableBuiltInFunctionsmodel = new CustomTableModel_MSMB(Constants.TitlesTabs.BUILTINFUNCTIONS.description,col,new Vector(),this);
		jTableBuiltInFunctions = new CustomJTable_MSMB();
		jTableBuiltInFunctions.setModel(tableBuiltInFunctionsmodel);
		
		jTableBuiltInFunctions.initializeCustomTable(tableBuiltInFunctionsmodel);
		
		 TableColumn nameColumn = jTableBuiltInFunctions.getColumnModel().getColumn(Constants.FunctionsColumns.NAME.index);
		 nameColumn.setCellRenderer(new EditableCellRenderer_MSMB());
		 nameColumn.setCellEditor(new UnquotingCellEditor());
		 
			
		 
		 TableColumn equationColumn = jTableBuiltInFunctions.getColumnModel().getColumn(Constants.FunctionsColumns.EQUATION.index);
		 equationColumn.setCellRenderer(new EditableCellRenderer_MSMB());
		 equationColumn.setCellEditor(new UnquotingCellEditor());
			
		  TableColumn expandedColumn = jTableBuiltInFunctions.getColumnModel().getColumn(Constants.FunctionsColumns.PARAMETER_ROLES.index);
	       
		  InputStream is = MainGui.class.getResourceAsStream("images/roles.png");
		  ImageIcon icon = new ImageIcon( ImageIO.read(is));
		  Image image = icon.getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH);
		  icon.setImage(image);
		  
	        expandedColumn.setCellRenderer(new ButtonRenderer(icon));
	        
	        ActionListener action = new ActionListener() {
				  public void actionPerformed(ActionEvent e) {
					
					  openFunctionParameterFrame();
				  }
			  };
	        
	        expandedColumn.setCellEditor(new ButtonEditor_ParameterRoles(icon, action));
	       
	        expandedColumn.setMaxWidth(22);
	        expandedColumn.setMinWidth(22);
	        expandedColumn.setWidth(22);
	        
		return jTableBuiltInFunctions;
}

	private JTable getJTableFunctions() throws IOException {
			Vector col = new Vector(Constants.functions_columns);
			
			if(tableFunctionsmodel == null)tableFunctionsmodel = new CustomTableModel_MSMB(Constants.TitlesTabs.FUNCTIONS.description,col,new Vector(),this);
			jTableFunctions = new CustomJTable_MSMB();
			jTableFunctions.setModel(tableFunctionsmodel);
			
			jTableFunctions.initializeCustomTable(tableFunctionsmodel);
			
			
			 TableColumn nameColumn = jTableFunctions.getColumnModel().getColumn(Constants.FunctionsColumns.NAME.index);
			 nameColumn.setCellRenderer(new EditableCellRenderer_MSMB());
			 nameColumn.setCellEditor(new UnquotingCellEditor());
				
			 TableColumn equationColumn = jTableFunctions.getColumnModel().getColumn(Constants.FunctionsColumns.EQUATION.index);
			 equationColumn.setCellRenderer(new EditableCellRenderer_MSMB());
			 equationColumn.setCellEditor(new UnquotingCellEditor());
				
			  TableColumn expandedColumn = jTableFunctions.getColumnModel().getColumn(Constants.FunctionsColumns.PARAMETER_ROLES.index);
		       
			  InputStream is = MainGui.class.getResourceAsStream("images/roles.png");
			  ImageIcon icon = new ImageIcon( ImageIO.read(is));
			  Image image = icon.getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH);
			  icon.setImage(image);
			  
		        expandedColumn.setCellRenderer(new ButtonRenderer(icon));
		        
		        ActionListener action = new ActionListener() {
					  public void actionPerformed(ActionEvent e) {
						  
						  openFunctionParameterFrame();
					  }
				  };
		       
		        expandedColumn.setCellEditor(new ButtonEditor_ParameterRoles(icon, action));
		       
		        expandedColumn.setMaxWidth(22);
		        expandedColumn.setMinWidth(22);
		        expandedColumn.setWidth(22);
		        
			return jTableFunctions;
	}

	
	private JTable getJTableEvents() {
			Vector col = new Vector(Constants.events_columns);
			
			  Vector booleanCol = new Vector();
			  booleanCol.add(Constants.EventsColumns.DELAYCALC.index);
			  booleanCol.add(Constants.EventsColumns.EXPAND_ACTION_ONVOLUME_TOSPECIES_C.index);
			  
			if(tableEventsmodel == null)tableEventsmodel = new CustomTableModel_MSMB(Constants.TitlesTabs.EVENTS.description,col,booleanCol,this);
			jTableEvents = new CustomJTable_MSMB();
			jTableEvents.setModel(tableEventsmodel);
			
			jTableEvents.initializeCustomTable(tableEventsmodel);
			
			TableColumn afterCalcColumn = jTableEvents.getColumnModel().getColumn(Constants.EventsColumns.DELAYCALC.index);
		        
			afterCalcColumn.setMaxWidth(50);
			afterCalcColumn.setMinWidth(50);
			afterCalcColumn.setWidth(50);
		    
			TableColumn expandActionColumn = jTableEvents.getColumnModel().getColumn(Constants.EventsColumns.EXPAND_ACTION_ONVOLUME_TOSPECIES_C.index);
	        
			expandActionColumn.setMaxWidth(60);
			expandActionColumn.setMinWidth(60);
			expandActionColumn.setWidth(60);
			
			
		    jTableEvents.getColumnModel().getColumn(Constants.EventsColumns.NAME.index).setCellEditor(new UnquotingCellEditor());
		    jTableEvents.getColumnModel().getColumn(Constants.EventsColumns.ACTIONS.index).setCellEditor(new UnquotingCellEditor());
		    jTableEvents.getColumnModel().getColumn(Constants.EventsColumns.DELAY.index).setCellEditor(new UnquotingCellEditor());
		    jTableEvents.getColumnModel().getColumn(Constants.EventsColumns.NAME.index).setCellEditor(new UnquotingCellEditor());
		    jTableEvents.getColumnModel().getColumn(Constants.EventsColumns.NOTES.index).setCellEditor(new UnquotingCellEditor());
		    jTableEvents.getColumnModel().getColumn(Constants.EventsColumns.TRIGGER.index).setCellEditor(new UnquotingCellEditor());
						

		return jTableEvents;
	}

	
	public void openFunctionParameterFrame() {
		functionParameterFrame.clearAll();
	    
		if(jTabGeneral.getSelectedIndex() == Constants.TitlesTabs.FUNCTIONS.index) {
			int sel = MainGui.row_to_highlight.get(Constants.TitlesTabs.FUNCTIONS.index); // jTableFunctions.getSelectedRow();
			if(sel > -1) {
				
				if(!MainGui.containsKey_debugMessages_relatedWith(Constants.TitlesTabs.FUNCTIONS.description,  DebugConstants.PriorityType.PARSING.priorityCode, sel,Constants.FunctionsColumns.NAME.index)&&
					!MainGui.containsKey_debugMessages_relatedWith(Constants.TitlesTabs.FUNCTIONS.description,  DebugConstants.PriorityType.PARSING.priorityCode, sel,Constants.FunctionsColumns.EQUATION.index)
					) {
					String name = (String)tableFunctionsmodel.getValueAt(sel, Constants.FunctionsColumns.NAME.index);
					try {
						functionParameterFrame.setFunction((Integer)tableFunctionsmodel.getValueAt(sel, 0), multiModel.funDB.getFunctionByName(name));
					} catch (Exception e) {
						if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) 	e.printStackTrace();
					}
					functionParameterFrame.setVisible(true);
				} 
			}
		}
		
		
		
	}
	
	public void resetJTable(Vector<Vector<String>> data, String description) throws IOException {
		if(description.compareTo(Constants.TitlesTabs.REACTIONS.description) == 0) {
			Vector col =new Vector(Constants.reactions_columns);
			tableReactionmodel = new CustomTableModel_MSMB(Constants.TitlesTabs.REACTIONS.description,col,new Vector(),this);
			tableReactionmodel.setDataVector(new Vector(data), col);
			for(int i = 0; i < data.size(); i++) {
				Vector row = new Vector((Vector<String>)data.get(i));
	    		tableReactionmodel.addRow(new Vector(row.subList(1,row.size())));
	    	}
			jScrollPaneTableReactions.setViewportView(getJTableReactions());
			jTableReactions.revalidate();
			jScrollPaneTableReactions.revalidate();
			return;
		} else if(description.compareTo(Constants.TitlesTabs.SPECIES.description) == 0) {
			Vector col =new Vector(Constants.species_columns);
			//tableSpeciesmodel = new CustomTableModel(Constants.TitlesTabs.SPECIES.description,col,this);
			tableSpeciesmodel = new CustomTableModel_MSMB(Constants.TitlesTabs.SPECIES.description,col,new Vector(),this);
			tableSpeciesmodel.setDataVector(new Vector(data),col);
			for(int i = 0; i < data.size(); i++) {
				Vector row = new Vector((Vector<String>)data.get(i));
				tableSpeciesmodel.addRow(new Vector(row.subList(1,row.size())));
	    	}
			jScrollPaneTableSpecies.setViewportView(getJTableSpecies());
			jTableSpecies.revalidate();
			jScrollPaneTableSpecies.revalidate();
			return;
		} else if(description.compareTo(Constants.TitlesTabs.COMPARTMENTS.description) == 0) {
			Vector col =new Vector(Constants.compartments_columns);
			tableCompartmentsmodel = new CustomTableModel_MSMB(Constants.TitlesTabs.COMPARTMENTS.description,col,new Vector(),this);
			tableCompartmentsmodel.setDataVector(new Vector(data),col);
			for(int i = 0; i < data.size(); i++) {
				Vector row = new Vector((Vector<String>)data.get(i));
				tableCompartmentsmodel.addRow(new Vector(row.subList(1,row.size())));
	    	}
			jScrollPaneTableCompartments.setViewportView(getJTableCompartments());
			jTableCompartments.revalidate();
			jScrollPaneTableCompartments.revalidate();
			return;
		} else if(description.compareTo(Constants.TitlesTabs.EVENTS.description) == 0) {
			Vector col =new Vector(Constants.events_columns);
			tableEventsmodel = new CustomTableModel_MSMB(Constants.TitlesTabs.EVENTS.description,col,new Vector(),this);
			tableEventsmodel.setDataVector(new Vector(data),col);
			for(int i = 0; i < data.size(); i++) {
				Vector row = new Vector((Vector<String>)data.get(i));
				tableEventsmodel.addRow(new Vector(row.subList(1,row.size())));
	    	}
			jScrollPaneTableEvents.setViewportView(getJTableEvents());
			jTableEvents.revalidate();
			jScrollPaneTableEvents.revalidate();
			return;
		} else if(description.compareTo(Constants.TitlesTabs.FUNCTIONS.description) == 0) {
			Vector col =new Vector(Constants.functions_columns);
			tableFunctionsmodel = new CustomTableModel_MSMB(Constants.TitlesTabs.FUNCTIONS.description,col,new Vector(),this);
			tableFunctionsmodel.setDataVector(new Vector(data),col);
			for(int i = 0; i < data.size(); i++) {
				Vector row = new Vector((Vector<String>)data.get(i));
				tableFunctionsmodel.addRow(new Vector(row.subList(1,row.size())));
	    	}
			jScrollPaneTableFunctions.setViewportView(getJTableFunctions());
			jTableFunctions.revalidate();
			jScrollPaneTableFunctions.revalidate();
			return;
		} else if(description.compareTo(Constants.TitlesTabs.GLOBALQ.description) == 0) {
			Vector col =new Vector(Constants.globalQ_columns);
			tableGlobalQmodel = new CustomTableModel_MSMB(Constants.TitlesTabs.GLOBALQ.description,col,new Vector(),this);
			tableGlobalQmodel.setDataVector(new Vector(data),col);
			for(int i = 0; i < data.size(); i++) {
				Vector row = new Vector((Vector<String>)data.get(i));
				tableGlobalQmodel.addRow(new Vector(row.subList(1,row.size())));
	    	}
			jScrollPaneTableGlobalQ.setViewportView(getJTableGlobalQ());
			jTableGlobalQ.revalidate();
			jScrollPaneTableGlobalQ.revalidate();
			return;
		}
	}
	
	private JTable getJTableReactions() throws IOException {
			Vector col = new Vector(Constants.reactions_columns);
						
			if(tableReactionmodel==null) tableReactionmodel = new CustomTableModel_MSMB(Constants.TitlesTabs.REACTIONS.description,col,new Vector(),this);
			jTableReactions = new CustomJTable_MSMB();
			jTableReactions.setModel(tableReactionmodel);
		
			jTableReactions.initializeCustomTable(tableReactionmodel);
	      
			TableColumn typeColumn = jTableReactions.getColumnModel().getColumn(Constants.ReactionsColumns.TYPE.index);
			if(comboBoxReactionType.getItemCount()==0) {
				for(int i = 0; i < Constants.reactionTypes.size(); i++) { 
					comboBoxReactionType.addItem((String)Constants.reactionTypes.get(i));
				}
			}
	        typeColumn.setCellEditor(new DefaultCellEditor(comboBoxReactionType));
	        
	        TableColumn reactionStringColumn = jTableReactions.getColumnModel().getColumn(Constants.ReactionsColumns.REACTION.index);
	        reactionStringColumn.setCellRenderer(new EditableCellRenderer_MSMB());
	        reactionStringColumn.setCellEditor(new UnquotingCellEditor());
			
	        
	        jTableReactions.getColumnModel().getColumn(Constants.ReactionsColumns.NAME.index).setCellEditor(new UnquotingCellEditor());
			
	    
	        
	   
	        reactionStringColumn = jTableReactions.getColumnModel().getColumn(Constants.ReactionsColumns.KINETIC_LAW.index);
	        reactionStringColumn.setCellRenderer(new EditableCellRenderer_MSMB());
	       // reactionStringColumn.setCellEditor(new UnquotingCellEditor());
	        JTextField withAutoComplete = new JTextField();
	   		AutoCompletion autoCompletion = new AutoCompletion(Constants.provider) {
	   			
	   			
	   			
	   			@Override
	   			protected String getReplacementText(Completion c,
	   					Document doc, int start, int len) {
	   				    
	   				    String text = super.getReplacementText(c, doc, start, len);
	   				    text = CellParsers.cleanName(text);
	   				
	   				    try {
							Function f = multiModel.funDB.getFunctionByName(text);
							if(f == null) {
								f = multiModel.funDB.getBuiltInFunctionByName(text);
								if(f!= null) text = f.printCompleteSignature();
							}
							if(f!= null) text = f.printCompleteSignature();
							
							if(start > 0) {
							    try {
									String maybeQuote = doc.getText(start-1, 1);
									if(maybeQuote.compareTo("\"")==0 && text.startsWith("\"")) text = text.substring(1);
								} catch (BadLocationException e1) {
									if (MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e1.printStackTrace();
								}
							}
		   				
						} catch (Exception e) {
							if (MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
						}
		                return text;
		                 
		            
	   			}
	   		};
	   		
	   		autoCompletion.setShowDescWindow(true);
	   		autoCompletion.setAutoActivationEnabled(true);
	   		autoCompletion.setAutoCompleteSingleChoices(false);
	   		autoCompletion.setAutoCompleteEnabled(false);
	   		autoCompletion.install(withAutoComplete);
	   		withAutoComplete.addKeyListener(new AutoCompleteKeyLister(autoCompletion,multiModel,false));
	   		reactionStringColumn.setCellEditor(new ExpressionsCellEditor(withAutoComplete));
	
	        
	        TableColumn expandedColumn = jTableReactions.getColumnModel().getColumn(Constants.ReactionsColumns.EXPANDED.index);
	        
			  InputStream is = MainGui.class.getResourceAsStream("images/row.jpg");
			  ImageIcon icon = null;
				icon = new ImageIcon( ImageIO.read(is));
			
			  
	        expandedColumn.setCellRenderer(new ButtonRenderer(icon));
	        expandedColumn.setCellEditor(new ButtonEditor_ExpandReactions(this));
	       
	        expandedColumn.setMaxWidth(22);
	        expandedColumn.setMinWidth(22);
	        expandedColumn.setWidth(22);
	  
		return jTableReactions;
	}
	
	
	
	
	private JTable getJTableSpecies() {
			Vector col = new Vector(Constants.species_columns);
			
			//if(tableSpeciesmodel == null) tableSpeciesmodel = new CustomTableModel(Constants.TitlesTabs.SPECIES.description,col,this);
			//jTableSpecies = new CustomJTable();
			if(tableSpeciesmodel == null) tableSpeciesmodel = new CustomTableModel_MSMB(Constants.TitlesTabs.SPECIES.description,col,new Vector(),this);
			jTableSpecies = new CustomJTable_MSMB();
			jTableSpecies.setModel(tableSpeciesmodel);
			
			jTableSpecies.initializeCustomTable(tableSpeciesmodel);
			
			TableColumnModel colModel = jTableSpecies.getColumnModel();
			colModel.getColumn(Constants.SpeciesColumns.NAME.index).setCellRenderer(new EditableCellRenderer_MSMB());
			colModel.getColumn(Constants.SpeciesColumns.NAME.index).setCellEditor(new UnquotingCellEditor());
			colModel.getColumn(Constants.SpeciesColumns.EXPRESSION.index).setCellRenderer(new EditableCellRenderer_MSMB());
				
			
			JTextField withAutoComplete = new JTextField();
			AutoCompletion autoCompletion = new AutoCompletion(Constants.provider);
			autoCompletion.setShowDescWindow(true);
			autoCompletion.setAutoActivationEnabled(true);
			autoCompletion.setAutoCompleteSingleChoices(false);
			autoCompletion.setAutoCompleteEnabled(false);
			autoCompletion.install(withAutoComplete);
			withAutoComplete.addKeyListener(new AutoCompleteKeyLister(autoCompletion,multiModel,false));
			colModel.getColumn(Constants.SpeciesColumns.EXPRESSION.index).setCellEditor(new ExpressionsCellEditor(withAutoComplete));

			JTextField withAutoCompleteInitial = new JTextField();
			autoCompletion = new AutoCompletion(Constants.provider);
			 autoCompletion.setShowDescWindow(true);
			 autoCompletion.setAutoActivationEnabled(true);
			 autoCompletion.setAutoCompleteSingleChoices(false);
			 autoCompletion.setAutoCompleteEnabled(false);
			autoCompletion.install(withAutoCompleteInitial);
			withAutoCompleteInitial.addKeyListener(new AutoCompleteKeyLister(autoCompletion,multiModel,true));
			colModel.getColumn(Constants.SpeciesColumns.INITIAL_QUANTITY.index).setCellEditor(new ExpressionsCellEditor(withAutoCompleteInitial));

			
			TableColumn typeColumn = colModel.getColumn(Constants.SpeciesColumns.TYPE.index);
			if(comboBoxSpeciesType.getItemCount()==0) {
				for(int i = 0; i < Constants.speciesTypes.size(); i++) { 
					comboBoxSpeciesType.addItem((String)Constants.speciesTypes.get(i));
				}
			}
	        typeColumn.setCellEditor(new DefaultCellEditor(comboBoxSpeciesType));
	        colModel.getColumn(Constants.SpeciesColumns.TYPE.index).setCellRenderer(new EditableCellRenderer_MSMB());
		        
	        TableColumn column = colModel.getColumn(Constants.SpeciesColumns.INITIAL_QUANTITY.index);  
	        column.setCellRenderer(new ScientificFormatCellRenderer());
	         
	         column = colModel.getColumn(Constants.SpeciesColumns.COMPARTMENT.index);  
	        column.setCellRenderer(new EditableCellRenderer_MSMB());
	        column.setCellEditor(new UnquotingCellEditor());
			
	        jTableSpecies.addMouseListener(new MouseAdapter()
	        {
	            public void mouseClicked(MouseEvent e)
	            {
	                if (e.getComponent().isEnabled() && e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2)
	                {
	                    Point p = e.getPoint();
	                    int row = jTableSpecies.rowAtPoint(p); 
	                    int col = jTableSpecies.columnAtPoint(p); 
	                    if(tableSpeciesmodel.disabledCell.contains(row+"_"+col) 
	                    		&& (col == Constants.SpeciesColumns.TYPE.index || col == Constants.SpeciesColumns.INITIAL_QUANTITY.index)
	                    		// ((String)tableSpeciesmodel.getValueAt(row, col)).compareTo(Constants.SpeciesType.MULTISTATE.description)==0
	                    		) {
	                    	try {
	                    		multiBuilderFrame.clearAll();
	                    	} catch (Exception e1) {
	                    		if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e1.printStackTrace();
	                    	}

	                    	if(jTabGeneral.getSelectedIndex() == 1) {
	                    		int sel = jTableSpecies.getSelectedRow();
	                    		if(sel > -1) {
	                    			String name = (String)tableSpeciesmodel.getValueAt(sel, Constants.SpeciesColumns.NAME.index);

	                    			if(CellParsers.isMultistateSpeciesName(name)) {
	                    				multiBuilderFrame.setMultistateSpecies(multiModel.getMultistateSpecies(name));
	                    				modify_multistate_species_from_builder = true;
	                    				if(col == Constants.SpeciesColumns.INITIAL_QUANTITY.index) multiBuilderFrame.selectInitialQuantityTab();
	        	                    	multiBuilderFrame.setVisible(true);
	        	                    	jTableSpecies.revalidate();
	                    			} 
	                    		}
	                    	}
	                    	
	                    } 
	                } else {
	                	 Point p = e.getPoint();
	                	 int row = jTableSpecies.rowAtPoint(p); 
		                    int col = jTableSpecies.columnAtPoint(p); 
                    	jTableSpecies.setColumnSelectionInterval(col, col);
                    	if(row > 0 && row < jTableSpecies.getRowCount())jTableSpecies.setRowSelectionInterval(row, row);
                    }
	         
	            }
	        });
	        
	        
	     
		return jTableSpecies;
	}

	public void clearAllTables() {
		MainGui.row_to_highlight.clear();
		for(int i = 0; i < Constants.TitlesTabs.getNumTabs()+1; i++) {	row_to_highlight.add(new Integer(-1));	} //last entry is the index of the sub builtin table
		MainGui.cellSelectedCol = -1;
		 MainGui.cellSelectedRow = -1;
		 MainGui.cellTableEdited = "";
		 MainGui.cellValueBeforeChange = "";
		 MainGui.indexToDelete.clear();
		 
			clearTable(tableCompartmentsmodel);
		clearTable(tableFunctionsmodel);
		clearTable(tableBuiltInFunctionsmodel);
		clearTable(tableGlobalQmodel);
		clearTable(tableReactionmodel);
		clearTable(tableSpeciesmodel);
		clearTable(tableEventsmodel);
		if(jScrollPaneTableCompartments==null) {		try {
			initialize();
		} catch (Exception e) {
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
		}	}	   
	     jScrollPaneTableCompartments.revalidate();
	     jScrollPaneTableFunctions.revalidate();
	     jScrollPaneTableGlobalQ.revalidate();
	     jScrollPaneTableReactions.revalidate();
	     jScrollPaneTableSpecies.revalidate();
	     modify_multistate_species_from_builder = false;
	     debugMessages.clear();
	     updateDebugTab();
	      multiModel.clear();
	      
	      menu_expandedExpr_GlobalQ.setSelected(false);
	      menu_expandedExpr_Reactions.setSelected(false);
	      menu_expandedExpr_Species.setSelected(false);
	      menu_expandedExpr_Compartments.setSelected(false);
	      menu_expandedExpr_Events.setSelected(false);
	      
	     
	     //clearCFunctionDB();
	     
	     listModel_FunctionToCompact.clear();
	     scrollPaneListFunctionToCompact.revalidate();
	     
	     
	    
	     
	     ConsistencyChecks.emptyFields.clear();
	     
	}
	
	private static void clearCFunctionDB() {
		
		CFunctionDB funDB_copasi = CCopasiRootContainer.getFunctionList();
		CFunctionVectorN funcs = funDB_copasi.loadedFunctions();
		
		/*System.out.println("      $$$ loadedFunctions size before $$$: "+ funcs.size());
		System.out.println("CFunction.UserDefined = "+CFunction.UserDefined);
		System.out.println("CFunction.MassAction = "+CFunction.MassAction);
		System.out.println("CFunction.Expression = "+CFunction.Expression);
		System.out.println("CFunction.Function = "+CFunction.Function);
		System.out.println("CFunction.PreDefined = "+CFunction.PreDefined);
	
		for (int i = 0; i < funcs.size();i++)
		{
			CFunction val = (CFunction)funDB_copasi.findFunction(funcs.get(i).getObjectName());
			System.out.println(val.getKey()+" "+val.getObjectName() + " " + val.getType());
		}
		*/
		
		
		long funcs_size_before = funcs.size();
		System.err.println("--- FunctionsDB size before cleaning --- "+funcs_size_before);
		
		/*while(true)
		{
			CFunction val = (CFunction)funDB_copasi.findFunction(funcs.get(funcs.size()-1).getObjectName());
			if(val!= null && (val.getType() == CFunction.UserDefined || val.getType() == CFunction.Function)) {
				funDB_copasi.removeFunction(val.getKey()); // many API crashes happen here
				funDB_copasi.clearRefresh();
			} else {
				break;
			}
		}
		*/
		/*funcs = funDB_copasi.loadedFunctions();
		System.out.println("-----$$$ loadedFunctions size after $$$: "+ funcs.size());
		System.out.flush();
		for (int i = 0; i < funcs.size();i++)
		{
			CFunction val = (CFunction)funDB_copasi.findFunction(funcs.get(i).getObjectName());
			System.out.println(val.getObjectName() + " " + val.getType());
		}
		*/
		
		  CCopasiRootContainer.getFunctionList().cleanup();
          CCopasiRootContainer.getFunctionList().load();
          
		funcs = funDB_copasi.loadedFunctions();
		long funcs_size_after = funcs.size();
		System.err.println("--- FunctionsDB size after cleaning --- "+funcs_size_after);
			
		if(funcs_size_after != 38) 	System.err.println("--- FunctionsDB size after cleaning --- "+funcs_size_after);
		
	}

	/*private static void clearTable(CustomTableModel_MSMB tableModel) {
		tableModel.clearData();
		tableModel.removeAddEmptyRow_Listener();
		tableModel.addRow(new Vector());
		tableModel.addAddEmptyRow_Listener();
		tableModel.fireTableDataChanged();
		String description = tableModel.getTableName();
	    
		CustomJTable table = null;
		//if(description.compareTo(Constants.TitlesTabs.SPECIES.description)==0) {			table  = jTableSpecies;	}
		//else 
			if(description.compareTo(Constants.TitlesTabs.COMPARTMENTS.description)==0) {	table = jTableCompartments;	}
		else if(description.compareTo(Constants.TitlesTabs.EVENTS.description)==0) {		table = jTableEvents;	}
		else if(description.compareTo(Constants.TitlesTabs.GLOBALQ.description)==0) {		table = jTableGlobalQ;		}
		else if(description.compareTo(Constants.TitlesTabs.REACTIONS.description)==0) {		table = jTableReactions;		}
		if(table!= null) {
			for(int indexColumn = 0; indexColumn < table.getColumnCount(); indexColumn++) {
				TableColumn nameColumn = table.getColumnModel().getColumn(indexColumn);
				TableCellRenderer cellRender = nameColumn.getCellRenderer();
				if(cellRender instanceof EditableCellRenderer) {
						EditableCellRenderer edi = ((EditableCellRenderer) cellRender);
						edi.clearCellsWithErrors();
						edi.clearCellsWithDefaults();
				}
			}
	     	table.revalidate();
		}
     }
	*/
	
	private static void clearTable(CustomTableModel_MSMB tableModel) {
		tableModel.clearData();
		tableModel.removeAddEmptyRow_Listener();
		tableModel.addRow(new Vector());
		tableModel.addAddEmptyRow_Listener();
		tableModel.fireTableDataChanged();
		String description = tableModel.getTableName();
	    
		CustomJTable_MSMB table = null;
		if(description.compareTo(Constants.TitlesTabs.SPECIES.description)==0) {			table  = jTableSpecies;	}
		else if(description.compareTo(Constants.TitlesTabs.COMPARTMENTS.description)==0) {	table = jTableCompartments;	}
		else if(description.compareTo(Constants.TitlesTabs.EVENTS.description)==0) {		table = jTableEvents;	}
		else if(description.compareTo(Constants.TitlesTabs.GLOBALQ.description)==0) {		table = jTableGlobalQ;		}
		else if(description.compareTo(Constants.TitlesTabs.REACTIONS.description)==0) {		table = jTableReactions;		}
		if(table!= null) {
			table.clearCellsWithErrors();
			table.clearCellsWithDefaults();
		/*	for(int indexColumn = 0; indexColumn < table.getColumnCount(); indexColumn++) {
				TableColumn nameColumn = table.getColumnModel().getColumn(indexColumn);
				TableCellRenderer cellRender = nameColumn.getCellRenderer();
				if(cellRender instanceof EditableCellRenderer_MSMB) {
						EditableCellRenderer_MSMB edi = ((EditableCellRenderer_MSMB) cellRender);
						edi.clearCellsWithErrors();
						edi.clearCellsWithDefaults();
						
				}
			}*/
	     	table.revalidate();
		}
     }
	
	private JMenuBar getJJMenuBar() throws Exception {
			jMenuBar = new JMenuBar();
			menuFile = new JMenu("File");
			jMenuBar.add(menuFile);

			JMenuItem item = new JMenuItem("New");
			item.addActionListener(new ActionListener(){
	            public void actionPerformed(ActionEvent arg0) {
	            	//save recents
	        		BufferedWriter out; 
	        			try {
	        				out = new BufferedWriter(new FileWriter(file_RecentFiles));
	        				
	        				for(int i = 0; i < recentFiles.size(); i++) {
	        						out.write(recentFiles.get(i).toString());
	        						out.write(System.getProperty("line.separator"));
	        				}
	        				out.flush();
	        				out.close();
	        			}
	        			catch (Exception e) {
	        				System.err.println("Trouble writing recentFiles directories: "+ e);
	        				if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
	        			}
	        		
	        		if(modelHasBeenModified) {
	        			//Custom button text
	        			Object[] options = {"Yes",
	        			                    "No",
	        			                    "Cancel"};
	        			int n = JOptionPane.showOptionDialog(frame,
	        			    "Model \""+inputFile+"\" has been modified. Do you want to save the changes?",
	        			    "Question",
	        			    JOptionPane.YES_NO_CANCEL_OPTION,
	        			    JOptionPane.QUESTION_MESSAGE,
	        			    null,
	        			    options,
	        			    options[0]);
	        			if(n==0) {
	        				File file = MainGui.fileCurrentlyLoaded;
	        				if(file == null) {
	        					int returnVal = cpsFileChooser.showSaveDialog(null);
	        					if (returnVal == JFileChooser.APPROVE_OPTION) {
	        						file = cpsFileChooser.getSelectedFile();
	        						addRecents(file);
	        					} 
	        				}
	        				
	        				try {
	        					saveCPS(file,true);
	        					clearAllTables();
	        				} catch (Exception e) {
	        					if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
	        				}
	        			} else if(n==1) {
	        				clearAllTables();
	        			} else if(n == 2) {
	    				return;
	        			}
	    			
	        			
	        		}
	        		clearAllTables();
	        		modelHasBeenModified = false;
	            	
	            }
			});
			menuFile.add(item);

			JMenuItem importMultistateFormat = new JMenuItem("Open ("+Constants.MULTISTATE_FILE_EXTENSION+") ...");
			importMultistateFormat.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent arg0) {
	            	JFileChooser fileChooser = new JFileChooser();
	        		
	        		int returnVal = fileChooser.showOpenDialog(null);
	                if (returnVal == JFileChooser.APPROVE_OPTION) {
	                	deleteTempAutosave();
		            	recordAutosave.stopAutosave();
		            	File file = fileChooser.getSelectedFile();
	                    inputFile = file.getName().substring(0,file.getName().lastIndexOf("."));
	                    donotCleanDebugMessages = true;
	                    importTablesMultistateFormat(file);
	                    donotCleanDebugMessages = false;
	                   
	                    startAutosave();
	                    jTabGeneral.setSelectedIndex(Constants.TitlesTabs.REACTIONS.index);
	                 }
		            
	            }
			}
			);
			menuFile.add(importMultistateFormat);

			JMenuItem saveMultistateFormat = new JMenuItem("Save as ("+Constants.MULTISTATE_FILE_EXTENSION+") ...");
			saveMultistateFormat.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent arg0) {
	            	 exportTables(true);
	            	 modelHasBeenModified = false;
	            }
			}
			);
			menuFile.add(saveMultistateFormat);
			menuFile.addSeparator();
			
			JMenuItem item2 = new JMenuItem("Import CPS...");
			Vector ext = new Vector();
			ext.add("cps");
			cpsFileChooser.addChoosableFileFilter(new CustomFileFilter(ext, "Copasi files"));
			item2.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent arg0) {
	            	  int returnVal = cpsFileChooser.showOpenDialog(null);
	                    if (returnVal == JFileChooser.APPROVE_OPTION) {
	                        File file = cpsFileChooser.getSelectedFile();
	                        addRecents(file);
	                        try {
								loadCPS(file);
							} catch (Exception e) {
								if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
							}
	                    } 
	            }
			}
			);
			menuFile.add(item2);

			JMenuItem item3 = new JMenuItem("Import SBML...");
			Vector sbml = new Vector();
			sbml.add("sbml");
			sbml.add("xml");
			sbmlFileChooser.addChoosableFileFilter(new CustomFileFilter(sbml, "SBML files"));
			item3.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent arg0) {
	            	  int returnVal = sbmlFileChooser.showOpenDialog(null);
	            	    if (returnVal == JFileChooser.APPROVE_OPTION) {
	                        File file = sbmlFileChooser.getSelectedFile();
	                        addRecents(file);

	                        try {
								loadSBML(file);
							} catch (Exception e) {
								if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
							}
	                    } 
	            }
			}
			);
			menuFile.add(item3);
			
			JMenuItem itemSaveCPS = new JMenuItem("Export CPS...");
			itemSaveCPS.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent arg0) {
	            	int returnVal = cpsFileChooser.showSaveDialog(null);
	                if (returnVal == JFileChooser.APPROVE_OPTION) {
	                        File file = cpsFileChooser.getSelectedFile();
	                        addRecents(file);
	                        try {
	        	           			saveCPS(file,true);
							} catch (Exception e) {
								if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
							}
	                    } 
	            }
			}
			);
			
			menuFile.add(itemSaveCPS);
			Vector xpp = new Vector();
			xpp.add("xpp");
			xppFileChooser.addChoosableFileFilter(new CustomFileFilter(xpp, "XPP files"));
			JMenuItem itemExportXPP = new JMenuItem("Export XPP...");
			itemExportXPP.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent arg0) {
	            	int returnVal = xppFileChooser.showSaveDialog(null);
	                if (returnVal == JFileChooser.APPROVE_OPTION) {
	                        File file = xppFileChooser.getSelectedFile();
	                        addRecents(file);
	                        try {
	                        	exportXPP(file,true);
							} catch (Exception e) {
								if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
							}
	                    } 
	            }
			}
			);
			
			menuFile.add(itemExportXPP);
			menuFile.addSeparator();
			
			
			
			itemPrintPDF = new JMenuItem("Print tables to PDF...");
			itemPrintPDF.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
			itemPrintPDF.addActionListener(new ActionListener() {
		        public void actionPerformed(ActionEvent arg0) {
		        	JFileChooser fileChooser = new JFileChooser();
	        		int returnVal = fileChooser.showOpenDialog(null);
	                if (returnVal == JFileChooser.APPROVE_OPTION) {
	                	setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	                	
	                    File file = fileChooser.getSelectedFile();
		        			Vector<Object> all = new Vector<Object>();
		        			all.add(tableReactionmodel);
		        			all.add(tableSpeciesmodel);
		        			all.add(createMultistateTableModel());
			        		all.add(tableGlobalQmodel);
		        			all.add(tableFunctionsmodel);
		        			all.add(tableCompartmentsmodel);
		        			all.add(tableEventsmodel);
		        			
		        			
		        			all.add(comboBox_unitVolume.getSelectedItem().toString());
		        			all.add(comboBox_unitTime.getSelectedItem().toString());
		        			all.add(comboBox_unitQuantity.getSelectedItem().toString());
		        			all.add(exportConcentration);
		        			all.add(quantityIsConc);
		        			
		        			all.add(debugMessages);
		            			
		                	PrintTablesToPDF pdf = new PrintTablesToPDF();
		                	pdf.setModelName(modelName);
		                	try {
		                		pdf.createPdf(file, all);
							} catch (Exception e) {
								JOptionPane.showMessageDialog(null,"Error producing the pdf.\n"+e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
							}
		                	finally { setCursor(null); }
		     

		        }
		        }
			}
			);
			menuFile.add(itemPrintPDF);
			
			menuFile.addSeparator();
			
			preferencesMenu = new JMenuItem("Preferences...");
			preferencesMenu.addActionListener(new ActionListener() {
				 public void actionPerformed(ActionEvent arg0) {
						preferenceFrame.setVisible(true);
				 }

			});
			menuFile.add(preferencesMenu);
			menuFile.addSeparator();
			
			recentMenu = new JMenu("Recent Files");
			menuFile.add(recentMenu);
			loadRecentFiles();
			
			menuFile.addSeparator();
			JMenuItem itemN = new JMenuItem("Exit");
			itemN.addActionListener(new ActionListener() {
				 public void actionPerformed(ActionEvent arg0) {
						exit();
				 }
			});
			menuFile.add(itemN);
			
			
			menuEdit = new JMenu("Edit");
			jMenuBar.add(menuEdit);

	

			submenuShowExpandedExpression = new JMenu("Show expanded expression in table...");
			
			menu_expandedExpr_Reactions = new JCheckBoxMenuItem("Reactions");
			submenuShowExpandedExpression.add(menu_expandedExpr_Reactions);
			menu_expandedExpr_Reactions.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					changeViewAllExpression(Constants.TitlesTabs.REACTIONS.description, menu_expandedExpr_Reactions.isSelected());
				}
			});
		
			menu_expandedExpr_Species = new JCheckBoxMenuItem("Species");
			submenuShowExpandedExpression.add(menu_expandedExpr_Species);
			menu_expandedExpr_Species.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					changeViewAllExpression(Constants.TitlesTabs.SPECIES.description, menu_expandedExpr_Species.isSelected());
				}
			});
			
			menu_expandedExpr_GlobalQ = new JCheckBoxMenuItem("Global Quantities");
			submenuShowExpandedExpression.add(menu_expandedExpr_GlobalQ);
			menu_expandedExpr_GlobalQ.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					changeViewAllExpression(Constants.TitlesTabs.GLOBALQ.description, menu_expandedExpr_GlobalQ.isSelected());
				}
			});
			
			menu_expandedExpr_Compartments = new JCheckBoxMenuItem("Compartments");
			submenuShowExpandedExpression.add(menu_expandedExpr_Compartments);
			menu_expandedExpr_Compartments.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					changeViewAllExpression(Constants.TitlesTabs.COMPARTMENTS.description, menu_expandedExpr_Compartments.isSelected());
				}
			});
			
			menu_expandedExpr_Events = new JCheckBoxMenuItem("Events");
			submenuShowExpandedExpression.add(menu_expandedExpr_Events);
			menu_expandedExpr_Events.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					changeViewAllExpression(Constants.TitlesTabs.EVENTS.description, menu_expandedExpr_Events.isSelected());
				}
			});
			
			menuEdit.add(submenuShowExpandedExpression);
			
			menuEdit.addSeparator();
				
			
			menu_reverse = new JMenuItem("Add reverse reaction...");
			menu_reverse.setEnabled(true);
			menu_reverse.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
			menu_reverse.addActionListener(new ActionListener(){
	            public void actionPerformed(ActionEvent arg0) {
	            	addReverseReaction();
	            }
			});
			menuEdit.add(menu_reverse);
			
			
			JMenuItem del = new JMenuItem("Delete element...");
			del.setEnabled(true);
			del.addActionListener(new ActionListener(){
	            public void actionPerformed(ActionEvent arg0) {
	            	MutablePair<FoundElementToDelete, DefaultMutableTreeTableNode> elementToDelete = collectDeleteTreeTableModel();
	            	if(elementToDelete == null) {
	        			JOptionPane.showMessageDialog(null,"The selected element cannot be deleted!", "Error", JOptionPane.ERROR_MESSAGE);
	        			return;
	        		} else {
	        			if(elementToDelete.left == null) { //empty selection found in the collectDeleteTreeTableModel() method
	        				JOptionPane.showMessageDialog(null,"No element is selected!", "Error", JOptionPane.ERROR_MESSAGE);
	        				return;
	        			}
	        		}
	            	
	            	try {
	            	DeleteFrame df = new DeleteFrame(elementToDelete);
	            	df.setSize(600,300);
	            	df.setLocationRelativeTo(MainGui.this);
	            	df.show();} catch (Exception e) {
						e.printStackTrace();
					}
	            }
			});
			menuEdit.add(del);
			
			
			menuEdit.addSeparator();
			JMenuItem itemMultiValidate =  new JMenuItem("Validate", KeyEvent.VK_A);
			KeyStroke ctrlAKeyStroke = KeyStroke.getKeyStroke("control A");
		    itemMultiValidate.setAccelerator(ctrlAKeyStroke);
		    
			itemMultiValidate.addActionListener(new ActionListener(){
	            public void actionPerformed(ActionEvent arg0) {
	            	try {
						validateMultiModel(true,true);
					} catch (Exception e) {
						if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
					}            	
	            }

			});
		
			menuEdit.add(itemMultiValidate);
			
			multiBuilderFrame = new MultistateBuilderFrame(this);
        	multiBuilderFrame.setModal(true);
            multiBuilderFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        	JMenuItem itemMultiBuilder = new JMenuItem("Multistate Builder");
			itemMultiBuilder.addActionListener(new ActionListener(){
	            public void actionPerformed(ActionEvent arg0) {
	            	try {
						multiBuilderFrame.clearAll();
					} catch (Exception e) {
						if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
					}
	            
	            	if(jTabGeneral.getSelectedIndex() == 1) {
	            		int sel = jTableSpecies.getSelectedRow();
	            		if(sel > -1) {
	            			String name = (String)tableSpeciesmodel.getValueAt(sel, 1);
	            			if(CellParsers.isMultistateSpeciesName(name)) {
		            			multiBuilderFrame.setMultistateSpecies(multiModel.getMultistateSpecies(name));
		            		 	modify_multistate_species_from_builder = true;
		         	        } 
	            		}
	            	}
	            	
	            	multiBuilderFrame.setVisible(true);
	            	jTableSpecies.revalidate();
	            	jTabGeneral.setSelectedIndex(Constants.TitlesTabs.REACTIONS.index);
	                jTableSpecies.revalidate();
	                jTabGeneral.setSelectedIndex(Constants.TitlesTabs.SPECIES.index);
	            }
			});
			menuEdit.add(itemMultiBuilder);
			
			
			
			//menuEdit.addSeparator();
			
			JMenuItem itemMU = new JMenuItem("Move row up");
			itemMU.setEnabled(false);
			itemMU.addActionListener(new ActionListener(){
	            public void actionPerformed(ActionEvent arg0) {
	            	//TO DO
	            	System.out.println("Move up row");
	            }
			});
		//	menuEdit.add(itemMU);
			
			JMenuItem itemMD = new JMenuItem("Move row down");
			itemMD.setEnabled(false);
			itemMD.addActionListener(new ActionListener(){
	            public void actionPerformed(ActionEvent arg0) {
	            	//TO DO
	            	System.out.println("Move down row");
	            }
			});
		//	menuEdit.add(itemMD);
			
		
			
			functionParameterFrame = new FunctionParameterFrame(this, null, 0);
			functionParameterFrame.setModal(true);
			functionParameterFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			ext = new Vector();
			ext.add("csv");
			final JFileChooser csvFileChooser = new JFileChooser();
			csvFileChooser.addChoosableFileFilter(new CustomFileFilter(ext, "Excel TABULAR reactions"));
			
			renamingFrame = new RenamingFrame(this);
			renamingFrame.setModal(true);
			renamingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);			
		
		
		return jMenuBar;
	}
	
	
	
	
	
	
	protected void changeViewAllExpression(String description, boolean selected) {
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		CustomTableModel_MSMB tableModel = null;
		Vector<Integer> columnWithExpressions = new Vector<Integer>();
		MainGui.importFromSBMLorCPS = true;
		if(description.compareTo(Constants.TitlesTabs.REACTIONS.description)==0) {
			tableModel = tableReactionmodel;
			columnWithExpressions.add(Constants.ReactionsColumns.KINETIC_LAW.index);
		} else if(description.compareTo(Constants.TitlesTabs.SPECIES.description)==0) {
			tableModel = tableSpeciesmodel;
			columnWithExpressions.add(Constants.SpeciesColumns.EXPRESSION.index);
			columnWithExpressions.add(Constants.SpeciesColumns.INITIAL_QUANTITY.index);
		} else if(description.compareTo(Constants.TitlesTabs.GLOBALQ.description)==0) {
			tableModel = tableGlobalQmodel;
			columnWithExpressions.add(Constants.GlobalQColumns.EXPRESSION.index);
			columnWithExpressions.add(Constants.GlobalQColumns.VALUE.index);
		} else if(description.compareTo(Constants.TitlesTabs.COMPARTMENTS.description)==0) {
			tableModel = tableCompartmentsmodel;
			columnWithExpressions.add(Constants.CompartmentsColumns.EXPRESSION.index);
			columnWithExpressions.add(Constants.CompartmentsColumns.INITIAL_SIZE.index);
		}  else if(description.compareTo(Constants.TitlesTabs.EVENTS.description)==0) {
			tableModel = tableEventsmodel;
			columnWithExpressions.add(Constants.EventsColumns.ACTIONS.index);
			columnWithExpressions.add(Constants.EventsColumns.TRIGGER.index);
		}
		
		int whichView = -1;
		if(selected) whichView = Constants.Views.EXPANDED.index;
		else  whichView = Constants.Views.COMPRESSED.index;
		if(tableModel!=null) {
			for(int i = 0; i < tableModel.getRowCount()-1; i++) {
				for(int j = 0; j < columnWithExpressions.size(); j++) {
					 try {
						setView(whichView,description,i,columnWithExpressions.get(j));
					} catch (MySyntaxException e) {
						if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES)	e.printStackTrace();
						continue;
					}
				}
			}
		} 
		MainGui.importFromSBMLorCPS = false;
		setCursor(null);
		
	}

	protected CustomTableModel_MSMB createMultistateTableModel() {
		Vector col = new Vector();
		col.add(Constants.SpeciesColumns.NAME.description);
		col.add(Constants.SpeciesColumns.INITIAL_QUANTITY.description);
			
		CustomTableModel_MSMB tableMultistateSpeciesmodel = new CustomTableModel_MSMB(Constants.MULTISTATE_TITLE_TABLE_PDF,col,new Vector(),this);

		Vector<MultistateSpecies> m = multiModel.getAllMultistateSpecies();
		for(int i = 0; i < m.size(); i++) {
			MultistateSpecies ms = m.get(i);
			Vector<Species> single = null;
			try {
				single = ms.getExpandedSpecies(multiModel);
			} catch (Exception e) {
				if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) 	e.printStackTrace();
			}
			for(int j = 0; j < single.size(); j++) {
				Vector row = new Vector();
				Species s = single.get(j);
				row.add(s.getDisplayedName());
				row.add(ms.getInitial_singleConfiguration(s));
				tableMultistateSpeciesmodel.addRow(row);
			}
			
		}
		return tableMultistateSpeciesmodel;
	}

	protected void startAutosave() {
		File outputfile = new File(getAutosaveDirectory()+"_start_session_"+getAutosaveBaseName()+".multis");
		ExportMultistateFormat.setFile(outputfile);
        ExportMultistateFormat.exportMultistateFormat(false);
		recordAutosave.startAutosave();
		setCustomFont(customFont.getSize());
	}

	protected void addReverseReaction() {
		int sel = jTableReactions.getSelectedRow();
		if(sel == -1) {
			JOptionPane.showMessageDialog(this,"Automatic reverse reaction definition: \nYou must select the row containig the reaction for which you want to define the reverse!", "No reaction selected!", JOptionPane.WARNING_MESSAGE);
			return;
		}
		String current = (String) tableReactionmodel.getValueAt(sel, Constants.ReactionsColumns.REACTION.index);
		current = current.trim();
		//I don't have a data structure to store reaction
		//once I will have that I will define a method "getReverse" but now I have to parse the string and reverse it manually
		
		if(current.length() ==0) {
			JOptionPane.showMessageDialog(this,"Automatic reverse reaction definition: \nThe reaction you want to reverse is empty!", "Empty reaction selected!", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		boolean parseErrors = false;
		Vector metabolites = new Vector();
		try{ 
			metabolites = CellParsers.parseReaction(multiModel,current,sel);
		} catch(Exception ex) {
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) ex.printStackTrace();
			parseErrors = true;
			metabolites.add(new Vector());
			metabolites.add(new Vector());
			metabolites.add(new Vector());
		}
		if(parseErrors) {
			JOptionPane.showMessageDialog(this,"Automatic reverse reaction definition: \nParse error in "+current, "Parse error!", JOptionPane.WARNING_MESSAGE);
			return;
		}

		Vector subs = (Vector)metabolites.get(0);
		Vector prod =(Vector)metabolites.get(1);
		Vector mod = (Vector)metabolites.get(2);
	
		String reverse = new String();
		
		for(int i = 0; i < prod.size()-1; i++) {
			String s = (String)prod.get(i);
			reverse += s + " + ";
		}
		if(prod.size()>=1) reverse += prod.get(prod.size()-1);
		
		reverse += " -> ";
		for(int i = 0; i < subs.size()-1; i++) {
			String s = (String)subs.get(i);
			reverse += s + " + ";
		}
		if(subs.size()>=1) reverse += subs.get(subs.size()-1);
		
		
		if(mod.size() >0) {
			reverse = reverse.trim();
			reverse += "; ";
			for(int i = 0; i < mod.size()-1; i++) {
				String s = (String)mod.get(i);
				reverse += s + " ";
			}
			if(mod.size()>=1) reverse += mod.get(mod.size()-1);
		}
		reverse = reverse.trim();
		
		addReaction(sel, reverse);
	}

	private void addReaction(int selRow, String reverse) {
		Vector<String> newReaction = new Vector<String>();
		newReaction.add("reverse_"+new Integer(selRow+1).toString());
		newReaction.add(reverse);
		tableReactionmodel.insertRow(selRow, newReaction.toArray());
		jTableReactions.revalidate();
		try {
			updateModelFromTable(selRow, Constants.ReactionsColumns.REACTION.index);
			 DebugMessage dm = new DebugMessage();
			 dm.setOrigin_table(Constants.TitlesTabs.REACTIONS.description);
			 dm.setProblem("Reverse reaction "+reverse+ " has been added by default");
			 dm.setPriority(DebugConstants.PriorityType.DEFAULTS.priorityCode);
			 dm.setOrigin_col(Constants.ReactionsColumns.REACTION.index);
			 dm.setOrigin_row(selRow+2);
			 MainGui.addDebugMessage_ifNotPresent(dm);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		
	}

	protected MutablePair<FoundElementToDelete, DefaultMutableTreeTableNode> collectDeleteTreeTableModel() {
	     int whichTab = -1;
			CustomJTable_MSMB table = null;
			CustomTableModel_MSMB tableModel = null;
			int indexName = -1;
			String nameTable = null;
			Integer sel_idx = null;
			DefaultMutableTreeTableNode invisibleRoot = new DefaultMutableTreeTableNode("invisibleRoot");
			MutablePair<FoundElementToDelete, DefaultMutableTreeTableNode> ret = null;
			whichTab = jTabGeneral.getSelectedIndex();
			sel_idx = MainGui.row_to_highlight.get(whichTab);
				switch(whichTab) {
					case 0: 
							table = jTableReactions; tableModel = tableReactionmodel;
							indexName = Constants.ReactionsColumns.NAME.index; nameTable = Constants.TitlesTabs.REACTIONS.description;
							break;
					case 1: table = jTableSpecies; tableModel = tableSpeciesmodel;
							indexName = Constants.SpeciesColumns.NAME.index; nameTable = Constants.TitlesTabs.SPECIES.description;
							break;
					case 2: 
							table = jTableGlobalQ; tableModel = tableGlobalQmodel;
							indexName = Constants.GlobalQColumns.NAME.index; nameTable = Constants.TitlesTabs.GLOBALQ.description;
							break;
					case 3: 
							table = jTableFunctions; tableModel = tableFunctionsmodel;
							indexName = Constants.FunctionsColumns.NAME.index; nameTable = Constants.TitlesTabs.FUNCTIONS.description;
							if(jTableBuiltInFunctions.getSelectedRow()!= -1) return null;
							break;
					case 4: 
							table = jTableEvents; tableModel = tableEventsmodel;
							indexName = Constants.EventsColumns.NAME.index; nameTable = Constants.TitlesTabs.EVENTS.description;
							break;
					case 5:  
							table = jTableCompartments; tableModel = tableCompartmentsmodel; 
							indexName = Constants.CompartmentsColumns.NAME.index; nameTable = Constants.TitlesTabs.COMPARTMENTS.description;
							break;
					default: System.out.println("Error???");  sel_idx = null; break;
				}
			try {
				int[] sel = {sel_idx} ; //table.getSelectedRows();
				if(sel.length > 0) {
				
					String message = new String();
					boolean deleteToConfim = false;
					Vector<DefaultMutableTreeTableNode> roots = new Vector<DefaultMutableTreeTableNode>();
					int i = 0;
					//for(int i = 0; i < sel.length; i++) {
						if(printCompleteRowContent(new FoundElement(nameTable, sel[i], 1))==null) return null;
					
						Vector<DefaultMutableTreeTableNode> firstGen = new Vector<DefaultMutableTreeTableNode>();
						String name = (String) tableModel.getValueAt(sel[i], indexName);
						if(nameTable.compareTo(Constants.TitlesTabs.FUNCTIONS.description)==0 && indexName == Constants.FunctionsColumns.NAME.index) {
							name = FunctionsDB.extractJustName(name);
						}
						FoundElementToDelete rootElementToDelete = null;
						Vector<FoundElement> found = search(name);
						Vector<FoundElementToDelete> firstGenToRecChec = new Vector<FoundElementToDelete>();
						DefaultMutableTreeTableNode rootToDelete = null;
						if(found.size() > 0) {
							deleteToConfim = true;
							message += "\""+name +"\" found in: "+ System.getProperty("line.separator");
							Set<FoundElementToDelete> totalFound = new HashSet<FoundElementToDelete>();
							
							for (FoundElement curr_el : found) {
								totalFound.add(new FoundElementToDelete(curr_el));
								if(curr_el.getTableDescription().compareTo(nameTable)==0 && curr_el.getRow() == sel[i] && curr_el.getCol() == indexName) {
									rootElementToDelete = new FoundElementToDelete(curr_el);
									rootElementToDelete.setActionToTake(Constants.DeleteActions.DELETE.description);
									rootToDelete = new DefaultMutableTreeTableNode(rootElementToDelete);
								} else {
									continue;
								}
							}
							
							for (FoundElement curr_el : found) {
								FoundElementToDelete curr_ell = new FoundElementToDelete(curr_el);
								if(curr_el.getTableDescription().compareTo(nameTable)==0 && curr_el.getRow() == sel[i] && curr_el.getCol() == indexName) {
									continue;
								} else {
									if(rootElementToDelete.getTableDescription().compareTo(Constants.TitlesTabs.COMPARTMENTS.description)==0) {
										if(curr_ell.getTableDescription().compareTo(Constants.TitlesTabs.SPECIES.description)==0 &&
												curr_ell.getCol() == Constants.SpeciesColumns.COMPARTMENT.index)
											curr_ell.setActionToTake(Constants.DeleteActions.DELETE.description);
										
									}
									if(curr_ell.getTableDescription().compareTo(Constants.TitlesTabs.EVENTS.description)==0 &&
											curr_ell.getCol() == Constants.EventsColumns.ACTIONS.index &&
											isOnLeftHandSide(curr_ell))  {
										curr_ell.setActionToTake(Constants.DeleteActions.DELETE.description);
									}
									DefaultMutableTreeTableNode firstGenElement = new DefaultMutableTreeTableNode(curr_ell);
									firstGen.add(firstGenElement);
									firstGenToRecChec.add(new FoundElementToDelete(curr_el));
								}
								totalFound.add(curr_ell);
							}
							
							
							for (int i1 = 0; i1 < firstGenToRecChec.size(); i1++) {
								FoundElement el = firstGenToRecChec.get(i1);
								totalFound.addAll(recursiveSearch(el,firstGen.get(i1),totalFound));
							}
							
							for (FoundElement el : totalFound) {
									message += el + ": "+ printCompleteRowContent(el) + System.getProperty("line.separator");
							}
							message += "---";
							if(i!= sel.length-1) message +=System.getProperty("line.separator");
						//}
						
						for(DefaultMutableTreeTableNode child : firstGen) {
							rootToDelete.add(child);
						}
						roots.add(rootToDelete);
					} else { //found is empty, but it can be the reaction with optional name
						
						rootElementToDelete = new FoundElementToDelete(nameTable, sel[i], 1);
						rootElementToDelete.setActionToTake(Constants.DeleteActions.DELETE.description);
						rootToDelete = new DefaultMutableTreeTableNode(rootElementToDelete);
						roots.add(rootToDelete);
						
					}
			
					//System.out.println(message);
			
						
					for(DefaultMutableTreeTableNode child : roots) {
						invisibleRoot.add(child);
					}
					ret = new MutablePair<FoundElementToDelete, DefaultMutableTreeTableNode>(rootElementToDelete, invisibleRoot);
				} else {
					
        			return new MutablePair<FoundElementToDelete, DefaultMutableTreeTableNode>(null, null);	
				}
			} catch (Exception e) {
				if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) 
					e.printStackTrace();
			}
			return ret;
	
	}

	public static boolean isOnLeftHandSide(FoundElementToDelete curr_ell) {
		//printCompleteRowContent(el);
		return true;
	}

	private Set<FoundElementToDelete> recursiveSearch(FoundElement el, DefaultMutableTreeTableNode parent,	Set<FoundElementToDelete> alreadyFound) {
		
		String descr = el.getTableDescription();
		if(descr.compareTo(Constants.TitlesTabs.REACTIONS.description)==0) {	return new HashSet();	}
		else if(descr.compareTo(Constants.TitlesTabs.EVENTS.description)==0) {	return new HashSet();	}
		
		CustomTableModel_MSMB mod = null;
		int nameCol = -1;
		if(descr.compareTo(Constants.TitlesTabs.SPECIES.description)==0) {	mod = tableSpeciesmodel;	nameCol = Constants.SpeciesColumns.NAME.index; }
		else if(descr.compareTo(Constants.TitlesTabs.COMPARTMENTS.description)==0) {	mod = tableCompartmentsmodel;	 nameCol = Constants.CompartmentsColumns.NAME.index;}
		else if(descr.compareTo(Constants.TitlesTabs.GLOBALQ.description)==0) {	mod = tableGlobalQmodel;	nameCol = Constants.GlobalQColumns.NAME.index;}
		else if(descr.compareTo(Constants.TitlesTabs.FUNCTIONS.description)==0) {	mod = tableFunctionsmodel;	nameCol = Constants.FunctionsColumns.NAME.index; }
				
		String name = (String) mod.getValueAt(el.getRow(),nameCol);
						
		Vector<FoundElement> found = search(name);
		
		Vector<DefaultMutableTreeTableNode> currentLevel = new Vector<DefaultMutableTreeTableNode>();
		
		for (FoundElement curr_ell : found) {
			FoundElementToDelete curr_el = new FoundElementToDelete(curr_ell);
			if(((FoundElementToDelete)parent.getUserObject()).getTableDescription().compareTo(Constants.TitlesTabs.COMPARTMENTS.description)==0) {
				if(curr_el.getTableDescription().compareTo(Constants.TitlesTabs.SPECIES.description)==0 &&
						curr_el.getCol() == Constants.SpeciesColumns.COMPARTMENT.index)
					curr_el.setActionToTake(Constants.DeleteActions.DELETE.description);
				
			}
			if(curr_el.getTableDescription().compareTo(Constants.TitlesTabs.EVENTS.description)==0 &&
					curr_el.getCol() == Constants.EventsColumns.ACTIONS.index &&
					isOnLeftHandSide(curr_el))  {
				curr_el.setActionToTake(Constants.DeleteActions.DELETE.description);
			}
			if(alreadyFound.contains(curr_el)) continue;
			alreadyFound.add(curr_el);
			DefaultMutableTreeTableNode curr_node = new DefaultMutableTreeTableNode(curr_el);
			currentLevel.add(curr_node);
			
			parent.add(curr_node);
		}
	
		for (int i = 0; i <found.size();i++ ) {
			FoundElement curr_ell = found.get(i);
			FoundElementToDelete curr_el = new FoundElementToDelete(curr_ell);
			if(alreadyFound.contains(curr_el)) continue;
			if(((FoundElementToDelete)parent.getUserObject()).getTableDescription().compareTo(Constants.TitlesTabs.COMPARTMENTS.description)==0) {
				if(curr_el.getTableDescription().compareTo(Constants.TitlesTabs.SPECIES.description)==0 &&
						curr_el.getCol() == Constants.SpeciesColumns.COMPARTMENT.index)
					curr_el.setActionToTake(Constants.DeleteActions.DELETE.description);
			
			}
			if(curr_el.getTableDescription().compareTo(Constants.TitlesTabs.EVENTS.description)==0 &&
					curr_el.getCol() == Constants.EventsColumns.ACTIONS.index &&
					isOnLeftHandSide(curr_el))  {
				curr_el.setActionToTake(Constants.DeleteActions.DELETE.description);
			}
			alreadyFound.addAll(recursiveSearch(curr_el,currentLevel.get(i),alreadyFound));
		}
		
		return alreadyFound;
	}

	private void deleteSelected() {
		int whichTab = -1;
		CustomJTable_MSMB table = null;
		CustomTableModel_MSMB tableModel = null;
		int indexName = -1;
		String nameTable = null;
		whichTab = jTabGeneral.getSelectedIndex();
			switch(whichTab) {
				case 0: 
						table = jTableReactions; tableModel = tableReactionmodel;
						indexName = Constants.ReactionsColumns.NAME.index; nameTable = Constants.TitlesTabs.REACTIONS.description;
						break;
				case 1: table = jTableSpecies; tableModel = tableSpeciesmodel;
						indexName = Constants.SpeciesColumns.NAME.index; nameTable = Constants.TitlesTabs.SPECIES.description;
						break;
				case 2: 
						table = jTableGlobalQ; tableModel = tableGlobalQmodel;
						indexName = Constants.GlobalQColumns.NAME.index; nameTable = Constants.TitlesTabs.GLOBALQ.description;
						break;
				case 3: 
						table = jTableFunctions; tableModel = tableFunctionsmodel;
						indexName = Constants.FunctionsColumns.NAME.index; nameTable = Constants.TitlesTabs.FUNCTIONS.description;
						break;
				case 4: 
						table = jTableEvents; tableModel = tableEventsmodel;
						indexName = Constants.EventsColumns.NAME.index; nameTable = Constants.TitlesTabs.EVENTS.description;
						break;
				case 5:  
						table = jTableCompartments; tableModel = tableCompartmentsmodel; 
						indexName = Constants.CompartmentsColumns.NAME.index; nameTable = Constants.TitlesTabs.COMPARTMENTS.description;
						break;
				default: System.out.println("Error?"); break;
			}
		try {
			int[] sel = table.getSelectedRows();
			if(sel.length > 0) {
				String message = new String();
				boolean deleteToConfim = false;
				for(int i = 0; i < sel.length; i++) {
					String name = (String) tableModel.getValueAt(sel[i], indexName);
					Vector<FoundElement> found = search(name);
					if(found.size() > 1) {
						deleteToConfim = true;
						message += "\""+name +"\" found in: "+ System.getProperty("line.separator");
						for (FoundElement el : found) {
							
							if(el.getTableDescription().compareTo(nameTable)==0) {
								if(el.getCol() != indexName) {
									message += printCompleteRowContent(el) + System.getProperty("line.separator");
								}
							} else {
								message += printCompleteRowContent(el) + System.getProperty("line.separator");
							}
						}
						message += "---";
						if(i!= sel.length-1) message +=System.getProperty("line.separator");
					}
				}
		
				int option = Constants.DELETE_JUST_ENTITIES;
				if(deleteToConfim) {
					DeletingDialog dialog = new DeletingDialog(message);
					dialog.setVisible(true);
					option = dialog.getReturnOption();
					dialog.dispose();
				}
				
				if(option == Constants.DELETE_JUST_ENTITIES) {
					for(int i = 0; i < sel.length; i++) {
						clear_debugMessages_relatedWith_row(sel[i]-i+1);
						moveUpRow_references_debugMessages(sel[i]-i+1);
					}
				}
				
				MainGui.donotCleanDebugMessages = true;
				switch(whichTab) {
					case 0: break;
					case 1: multiModel.removeSpecies(sel); updateSpeciesTableFromMultiModel(); 
							break;
					case 2: break;
					case 3: break;
					case 4: break;
					case 5: multiModel.removeComp(sel); updateCompartmentsTableFromMultiModel();
							break;
					default: System.out.println("Error?"); break;
				}
				
				MainGui.donotCleanDebugMessages = false;
				
			}
		} catch (Exception e) {
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
		}
	}
	
	public static String printCompleteRowContent(FoundElement el) {
		CustomTableModel_MSMB mod = null;
		if(el==null) return new String();
		String descr = el.getTableDescription();
		String rowContent = new String();
		if(descr.compareTo(Constants.TitlesTabs.SPECIES.description)==0) {	mod = tableSpeciesmodel;	}
		else if(descr.compareTo(Constants.TitlesTabs.COMPARTMENTS.description)==0) {	mod = tableCompartmentsmodel;	}
		else if(descr.compareTo(Constants.TitlesTabs.EVENTS.description)==0) {	mod = tableEventsmodel;	}
		else if(descr.compareTo(Constants.TitlesTabs.GLOBALQ.description)==0) {	mod = tableGlobalQmodel;	}
		else if(descr.compareTo(Constants.TitlesTabs.REACTIONS.description)==0) {	mod = tableReactionmodel;	}
		else if(descr.compareTo(Constants.TitlesTabs.FUNCTIONS.description)==0) {	mod = tableFunctionsmodel;	}
		rowContent += descr + " -> ";
		boolean allEmpty = true;
		for(int i = 0; i <mod.getColumnCount(); i++ ) {
			if(i > 0 && mod.getValueAt(el.getRow(),i).toString().trim().length()!=0) allEmpty = false;
			rowContent += mod.getValueAt(el.getRow(),i)+" | ";
		}
		rowContent = rowContent.substring(0, rowContent.length()-3);
		if(allEmpty == true) return null;
		return rowContent;
	}
	
	public static String printCellContent(FoundElement el) {
		CustomTableModel_MSMB mod = null;
		String descr = el.getTableDescription();
		String cellContent = new String();
		if(descr.compareTo(Constants.TitlesTabs.SPECIES.description)==0) {	mod = tableSpeciesmodel;	}
		else if(descr.compareTo(Constants.TitlesTabs.COMPARTMENTS.description)==0) {	mod = tableCompartmentsmodel;	}
		else if(descr.compareTo(Constants.TitlesTabs.EVENTS.description)==0) {	mod = tableEventsmodel;	}
		else if(descr.compareTo(Constants.TitlesTabs.GLOBALQ.description)==0) {	mod = tableGlobalQmodel;	}
		else if(descr.compareTo(Constants.TitlesTabs.REACTIONS.description)==0) {	mod = tableReactionmodel;	}
		else if(descr.compareTo(Constants.TitlesTabs.FUNCTIONS.description)==0) {	mod = tableFunctionsmodel;	}
			cellContent = (String) mod.getValueAt(el.getRow(),el.getCol());
		return cellContent;
	}
	
	public static String printCellHeader(FoundElement el) {
		String descr = el.getTableDescription();
		String cellHeader = new String();
		if(descr.compareTo(Constants.TitlesTabs.SPECIES.description)==0) {	cellHeader = Constants.SpeciesColumns.getDescriptionFromIndex(el.getCol());	}
		else if(descr.compareTo(Constants.TitlesTabs.COMPARTMENTS.description)==0) {	cellHeader = Constants.CompartmentsColumns.getDescriptionFromIndex(el.getCol());	}
		else if(descr.compareTo(Constants.TitlesTabs.EVENTS.description)==0) {	cellHeader = Constants.EventsColumns.getDescriptionFromIndex(el.getCol());	}
		else if(descr.compareTo(Constants.TitlesTabs.GLOBALQ.description)==0) {	cellHeader = Constants.GlobalQColumns.getDescriptionFromIndex(el.getCol());	}
		else if(descr.compareTo(Constants.TitlesTabs.REACTIONS.description)==0) {	cellHeader = Constants.ReactionsColumns.getDescriptionFromIndex(el.getCol());	}
		else if(descr.compareTo(Constants.TitlesTabs.FUNCTIONS.description)==0) {	cellHeader = Constants.FunctionsColumns.getDescriptionFromIndex(el.getCol());	}
		return cellHeader;
	}

	public static String printMainElementRow(FoundElement el) {
		int mainElementColumn = -1;
		String descr = el.getTableDescription();
		String cellContent = new String();
		CustomTableModel_MSMB mod = null;
		if(descr.compareTo(Constants.TitlesTabs.SPECIES.description)==0) {	mod = tableSpeciesmodel; mainElementColumn = Constants.SpeciesColumns.NAME.index;	}
		else if(descr.compareTo(Constants.TitlesTabs.COMPARTMENTS.description)==0) {	mod = tableCompartmentsmodel; mainElementColumn = Constants.CompartmentsColumns.NAME.index;		}
		else if(descr.compareTo(Constants.TitlesTabs.EVENTS.description)==0) {	mod = tableEventsmodel;	 mainElementColumn = Constants.EventsColumns.NAME.index;	}
		else if(descr.compareTo(Constants.TitlesTabs.GLOBALQ.description)==0) {	mod = tableGlobalQmodel; mainElementColumn = Constants.GlobalQColumns.NAME.index;		}
		else if(descr.compareTo(Constants.TitlesTabs.REACTIONS.description)==0) {	mod = tableReactionmodel; mainElementColumn = Constants.ReactionsColumns.NAME.index;		}
		else if(descr.compareTo(Constants.TitlesTabs.FUNCTIONS.description)==0) {	mod = tableFunctionsmodel; mainElementColumn = Constants.FunctionsColumns.NAME.index;		}
		cellContent = (String) mod.getValueAt(el.getRow(),mainElementColumn);
		return cellContent;
	}
	
	
	
	public static Vector<FoundElement> search_excludeTableColumn(String s, String tableToExclude, Integer columnToExclude)
    {  
		return search(s,tableToExclude,columnToExclude,false);
    }

	public static Vector<FoundElement> search(String s)
    {  
		return search(s,null,null,false);
    }
	
	public static Vector<FoundElement> search(String s , String tableToExclude, Integer columnToExclude, boolean alsoInFunctionEquation)
    {  
		if(s.trim().length() ==0) return new Vector<>();
		renamingFrame.setRenamingString(s,"",null,-1);
		Vector<Vector> tablesAndColumns = new Vector<Vector>();
		
		Vector element = null;
		
		
		if(tableToExclude==null || tableToExclude.compareTo(Constants.TitlesTabs.REACTIONS.description) != 0) {
			element = new Vector();
			element.add(tableReactionmodel);
			element.add(Constants.TitlesTabs.REACTIONS.description);
			if(columnToExclude==null || columnToExclude!=Constants.ReactionsColumns.REACTION.index) element.add(Constants.ReactionsColumns.REACTION.index);
			if(columnToExclude==null || columnToExclude!=Constants.ReactionsColumns.KINETIC_LAW.index) element.add(Constants.ReactionsColumns.KINETIC_LAW.index);
			tablesAndColumns.add(element);
		}
		
		if(tableToExclude==null || tableToExclude.compareTo(Constants.TitlesTabs.SPECIES.description) != 0) {
			element = new Vector();
			element.add(tableSpeciesmodel);
			element.add(Constants.TitlesTabs.SPECIES.description);
			if(columnToExclude==null || columnToExclude!=Constants.SpeciesColumns.NAME.index )element.add(Constants.SpeciesColumns.NAME.index);
			if(columnToExclude==null || columnToExclude!=Constants.SpeciesColumns.INITIAL_QUANTITY.index )element.add(Constants.SpeciesColumns.INITIAL_QUANTITY.index);
			if(columnToExclude==null ||  columnToExclude!=Constants.SpeciesColumns.EXPRESSION.index) element.add(Constants.SpeciesColumns.EXPRESSION.index);
			if(columnToExclude==null ||  columnToExclude!=Constants.SpeciesColumns.COMPARTMENT.index) element.add(Constants.SpeciesColumns.COMPARTMENT.index);
			tablesAndColumns.add(element);
		}
	

		if(tableToExclude==null || tableToExclude.compareTo(Constants.TitlesTabs.GLOBALQ.description) != 0) {
				element = new Vector();
			element.add(tableGlobalQmodel);
			element.add(Constants.TitlesTabs.GLOBALQ.description);
			if(columnToExclude==null || columnToExclude!=Constants.GlobalQColumns.NAME.index) element.add(Constants.GlobalQColumns.NAME.index);
			if(columnToExclude==null || columnToExclude!=Constants.GlobalQColumns.VALUE.index) element.add(Constants.GlobalQColumns.VALUE.index);
			if(columnToExclude==null || columnToExclude!=Constants.GlobalQColumns.EXPRESSION.index) element.add(Constants.GlobalQColumns.EXPRESSION.index);
			tablesAndColumns.add(element);
		}
		
		
		if(tableToExclude==null || tableToExclude.compareTo(Constants.TitlesTabs.EVENTS.description) != 0) {
			element = new Vector();
			element.add(tableEventsmodel);
			element.add(Constants.TitlesTabs.EVENTS.description);
			if(columnToExclude==null || columnToExclude!=Constants.EventsColumns.TRIGGER.index) element.add(Constants.EventsColumns.TRIGGER.index);
			if(columnToExclude==null || columnToExclude!=Constants.EventsColumns.ACTIONS.index) element.add(Constants.EventsColumns.ACTIONS.index);
			if(columnToExclude==null || columnToExclude!=Constants.EventsColumns.DELAY.index) element.add(Constants.EventsColumns.DELAY.index);
			tablesAndColumns.add(element);
		}

		if(tableToExclude==null || tableToExclude.compareTo(Constants.TitlesTabs.COMPARTMENTS.description) != 0) {
			element = new Vector();
			element.add(tableCompartmentsmodel);
			element.add(Constants.TitlesTabs.COMPARTMENTS.description);
			if(columnToExclude==null || columnToExclude!=Constants.CompartmentsColumns.NAME.index) element.add(Constants.CompartmentsColumns.NAME.index);
			if(columnToExclude==null || columnToExclude!=Constants.CompartmentsColumns.INITIAL_SIZE.index) element.add(Constants.CompartmentsColumns.INITIAL_SIZE.index);
			if(columnToExclude==null || columnToExclude!=Constants.CompartmentsColumns.EXPRESSION.index) element.add(Constants.CompartmentsColumns.EXPRESSION.index);
			tablesAndColumns.add(element);
		}

		if(tableToExclude==null || tableToExclude.compareTo(Constants.TitlesTabs.FUNCTIONS.description) != 0) {
			element = new Vector();
			element.add(tableFunctionsmodel);
			element.add(Constants.TitlesTabs.FUNCTIONS.description);
			if(columnToExclude==null || columnToExclude!=Constants.FunctionsColumns.NAME.index)  element.add(Constants.FunctionsColumns.NAME.index);
			if(alsoInFunctionEquation) {
				if(columnToExclude==null || columnToExclude!=Constants.FunctionsColumns.EQUATION.index) element.add(Constants.FunctionsColumns.EQUATION.index);
			}
			tablesAndColumns.add(element);
		}
		
		Vector<FoundElement> found = new Vector<FoundElement>();
		for(Vector el : tablesAndColumns) {
			if(el.get(0) instanceof CustomTableModel_MSMB) {
				CustomTableModel_MSMB tModel = (CustomTableModel_MSMB) el.get(0);
				String descr = (String) el.get(1);
				for(int row = 0; row < (tModel).getRowCount(); row++){
					for(int i = 2;  i < el.size(); i++)
					{
						int col = (int) el.get(i);
						String next = (tModel.getValueAt(row, col).toString());
						if(next.trim().length() ==0) continue;
						if(descr.compareTo(Constants.TitlesTabs.REACTIONS.description)==0 && col==Constants.ReactionsColumns.REACTION.index) { 
							try{
								InputStream is = new ByteArrayInputStream(next.getBytes("UTF-8"));
								MR_ChemicalReaction_Parser react = new MR_ChemicalReaction_Parser(is,"UTF-8");
								CompleteReaction start = react.CompleteReaction();
								ExtractNamesSpeciesUsedVisitor v = new ExtractNamesSpeciesUsedVisitor(multiModel);
								start.accept(v);

								if(v.getExceptions().size() != 0) {	continue;	}

								Vector<String> names = v.getNamesSpeciesUsed();
								for(String n : names) {
									if(n.compareTo(s)==0)
									{
										found.add(new FoundElement(descr, row, col));
										break;
									}
								}
							} catch (Exception e) {
								continue;
							}
						} else {
							try{
								Vector<String> names = new Vector<String>();
								if(descr.compareTo(Constants.TitlesTabs.FUNCTIONS.description)==0 && col == Constants.FunctionsColumns.NAME.index) {
									names.add(FunctionsDB.extractJustName(next));
								} else {
									InputStream is = new ByteArrayInputStream(next.getBytes("UTF-8"));
									MR_Expression_Parser_ReducedParserException parser = new MR_Expression_Parser_ReducedParserException(is,"UTF-8");
									ExtractNamesUsedVisitor v = new ExtractNamesUsedVisitor(multiModel);
									CompleteExpression root = parser.CompleteExpression();
									root.accept(v);
									names.addAll(v.getNamesUsed());
								}



								for(String n : names) {
									if(n.compareTo(s)==0)
									{
										found.add(new FoundElement(descr, row, col));
										break;
									}
								}
							} catch (Throwable e) {

								continue;
							}

						}

					}
				}
			}
    	}
		
        return found;
        
    }

	

	void validateMultiModel(boolean withOptionPaneModelValid, boolean withOptionPaneModelErrors) throws Exception {
	
			try {
				
			ConsistencyChecks.all_elements_in_reaction_exist(multiModel, tableReactionmodel);
			
			
			//ConsistencyChecks.all_elements_in_speciesExpressions_exist(multiModel);
			
			
			ConsistencyChecks.missing_nonMandatory_fields();
			
			Vector missing_fields = ConsistencyChecks.missing_fields();
			if(missing_fields.size() >0) throw new Exception("Error during validation: some mandatory fields are missing (" + missing_fields +")");
				
		} catch (Exception e) {
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES)	e.printStackTrace();
			if(withOptionPaneModelErrors) JOptionPane.showMessageDialog(new JButton(),e.getMessage(), "Model not valid!", JOptionPane.ERROR_MESSAGE);
		    updateDebugTab();
			throw e;
		} 
			Vector<DebugMessage> major = getDebugMessages(DebugConstants.PriorityType.MAJOR.priorityCode);
			if(major.size() > 0) {
				if(withOptionPaneModelErrors) JOptionPane.showMessageDialog(new JButton(),"Major issues in the model!", "Model not valid!", JOptionPane.ERROR_MESSAGE);
			}
			else {
				if(withOptionPaneModelValid) JOptionPane.showMessageDialog(new JButton(),"The model is valid!", "OK!", JOptionPane.INFORMATION_MESSAGE);
			}	
		
	}
	
	
	
	
	
	void createAndShowProgressBarFrame(String fileName) throws Exception {
		progressBarFrame = new ProgressBarFrame(this, "Exporting model to "+ fileName);
		progressBarThread = new Thread(progressBarFrame);
		progressBarThread.start();
    }
	
	void progress(int i) throws InterruptedException {
		if(progressBarFrame !=null) {
				synchronized (progressBarFrame) {
		 			progressBarFrame.progress(i);
		 			progressBarFrame.notifyAll();
			 	}
		 	}
		 return;
	}
	
	public String exportXPP(File file, boolean withProgressBar) throws Exception {
			try {
				validateMultiModel(false,false);
				
				Vector<DebugMessage> major = getDebugMessages(DebugConstants.PriorityType.MAJOR.priorityCode);
				if(major.size() > 0) {
					throw new Exception("Major issues in the model! It cannot be exported to Copasi-SBML");
				}
				
				if(withProgressBar && file!= null) createAndShowProgressBarFrame(file.getName());
				String sbmlID =  multiModel.exportXPP(file, tableReactionmodel,progressBarFrame);
				modelHasBeenModified = false;
				
				return sbmlID;
			} catch (Exception e) {
				if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
				if(MainGui.fromMainGuiTest) throw e;
				Object[] options = {"Save as .msmb", "Cancel"};
				final JOptionPane optionPane = new JOptionPane(
						e.getMessage(),
						JOptionPane.QUESTION_MESSAGE,
						JOptionPane.YES_NO_OPTION,
						null,     //do not use a custom Icon
						options,  //the titles of buttons
						options[0]); //default button title

				final JDialog dialog = new JDialog(this, 
						"Model not valid!",
						true);
				dialog.setContentPane(optionPane);
				
				setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
				
				optionPane.addPropertyChangeListener(
						new PropertyChangeListener() {
							public void propertyChange(PropertyChangeEvent e) {
								String prop = e.getPropertyName();

								if (dialog.isVisible() 
										&& (e.getSource() == optionPane)
										&& (prop.equals(JOptionPane.VALUE_PROPERTY))) {
									dialog.setVisible(false);
								}
							}
						});
				dialog.pack();
				dialog.setLocationRelativeTo(null);
				dialog.setVisible(true);
				
				if(optionPane.getValue() == options[0]) {//save as .multis
					ExportMultistateFormat.setFile(file);
		            ExportMultistateFormat.exportMultistateFormat(withProgressBar);
		            return null;
				} 
				
				if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES)	e.printStackTrace();
				return null;
			}
			
			
		}
		
	//if file=null then the copasiDataModelID is returned (after validation/compilation)
	//otherwise the model is saved in file, the model is cleared and null is returned
	public String saveCPS(File file, boolean withProgressBar) throws Exception {
		try {
			//clearCopasiFunctions();
			//System.out.println("....validation");
			validateMultiModel(false,false);
			//System.out.println("....validated");
			Vector<DebugMessage> major = getDebugMessages(DebugConstants.PriorityType.MAJOR.priorityCode);
			if(major.size() > 0) {
				throw new Exception("Major issues in the model! It cannot be exported to Copasi-SBML");
			}
			
			Vector<DebugMessage> minor = getDebugMessages(DebugConstants.PriorityType.MINOR.priorityCode);
			if(minor.size() > 0 && !MainGui.fromMainGuiTest) {
				Object[] options = {"Continue with Export",
					                "Go back"};
					int n = JOptionPane.showOptionDialog(frame,
						    "There are some minor issues in the model.\n"
								    + "The export can generate a model with issues for simulation/analysis.\n"
								    + "Do you want to continue with the export or go back and fix the issues?",
					"Minor issues in the model",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE,
					null,     //do not use a custom Icon
					options,  //the titles of buttons
					options[0]); //default button title
					if(n == 1) { //back
					     return new String();
					} 
			}
			
			
			if(withProgressBar && file!= null) createAndShowProgressBarFrame(file.getName());
			String sbmlID =  multiModel.saveCPS(file, tableReactionmodel,progressBarFrame);
			modelHasBeenModified = false;
			
			return sbmlID;
		} catch (Exception e) {
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
			if(MainGui.fromMainGuiTest) throw e;
			Object[] options = {"Save as "+Constants.MULTISTATE_FILE_EXTENSION, "Go back"};
			final JOptionPane optionPane = new JOptionPane(
					 "There are some major issues in the model.\n"
							   // + e.getMessage()+"\n" 
							    +"The model cannot be exported to Copasi-SBML-XPP\n"
							    +"Do you want to save it in "+Constants.TOOL_NAME+" format or go back and fix the issues?",
					JOptionPane.ERROR_MESSAGE,
					JOptionPane.YES_NO_OPTION,
					null,     //do not use a custom Icon
					options,  //the titles of buttons
					options[0]); //default button title

			final JDialog dialog = new JDialog(this, 
					"Model not valid!",
					true);
			dialog.setContentPane(optionPane);
			
			setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			
			optionPane.addPropertyChangeListener(
					new PropertyChangeListener() {
						public void propertyChange(PropertyChangeEvent e) {
							String prop = e.getPropertyName();

							if (dialog.isVisible() 
									&& (e.getSource() == optionPane)
									&& (prop.equals(JOptionPane.VALUE_PROPERTY))) {
								dialog.setVisible(false);
							}
						}
					});
			dialog.pack();
			dialog.setLocationRelativeTo(null);
			dialog.setVisible(true);
			
			if(optionPane.getValue() == options[0]) {//save as .multis
				ExportMultistateFormat.setFile(file);
	            ExportMultistateFormat.exportMultistateFormat(withProgressBar);
	            return null;
			} 
			
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES)	e.printStackTrace();
			return null;
		}
		
		
	}
	
	//the copasiDataModelID is returned (after validation/compilation)
	public String saveCPS() throws Exception {
		String id = saveCPS(null,true);
		return id;
	}
		
	

	private String addChangeFunction(int nrow, String name, String equation) throws Exception{
		try{
			int index = multiModel.funDB.getIndex(multiModel.funDB.getFunctionByName(name));
	
			Function f = new Function(name);
			f.setEquation(equation, CFunction.UserDefined, 0);
			Vector<String> undefinedFunCalls = multiModel.funDB.correctRoles(f);

			if(undefinedFunCalls.size() > 0 ) {
				 DebugMessage dm = new DebugMessage();
				 dm.setOrigin_table(Constants.TitlesTabs.FUNCTIONS.description);
				 dm.setProblem("Missing definition of subfunctions: " + undefinedFunCalls.toString());
				 dm.setPriority(DebugConstants.PriorityType.MISSING.priorityCode);
				 dm.setOrigin_col(Constants.FunctionsColumns.EQUATION.index);
				 dm.setOrigin_row(nrow);
				 MainGui.addDebugMessage_ifNotPresent(dm);
			}
			if((MainGui.showAllAvailableFunctions || f.toShow()) && !listModel_FunctionToCompact.contains(f.getName())) listModel_FunctionToCompact.addElement(f.getName());
			jListFunctionToCompact.revalidate();
			scrollPaneListFunctionToCompact.revalidate();
			multiModel.funDB.addChangeFunction(nrow, f);
			functionParameterFrame.setFunction(nrow, f);
			return f.printCompleteSignature();
		} catch(MyChangeNotAllowedException ex) {
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES)			ex.printStackTrace();
			 ex.setPreviousValue(cellValueBeforeChange);
			 throw ex;
		} catch (Exception e) {
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) 				e.printStackTrace();
			throw e;
		}
		
		
	}

	private void updateGlobalQ(int row) throws Exception{
		Integer nrow = (Integer)tableGlobalQmodel.getValueAt(row, 0);
		String name = (String)tableGlobalQmodel.getValueAt(row,Constants.GlobalQColumns.NAME.index);
		String value = tableGlobalQmodel.getValueAt(row,Constants.GlobalQColumns.VALUE.index).toString();
		String type = (String)tableGlobalQmodel.getValueAt(row, Constants.GlobalQColumns.TYPE.index);
		String expression = (String)tableGlobalQmodel.getValueAt(row, Constants.GlobalQColumns.EXPRESSION.index);
		String notes = (String)tableGlobalQmodel.getValueAt(row, Constants.GlobalQColumns.NOTES.index);
		
		
		String cleanName = CellParsers.cleanName(name, false);
		if(cleanName.compareTo(name)!=0) {
			tableGlobalQmodel.setValueAt(cleanName, row,Constants.GlobalQColumns.NAME.index);
			return;
		}
	
		
		 EditableCellRenderer_MSMB edi = (EditableCellRenderer_MSMB)(jTableGlobalQ.getCellRenderer(row, Constants.GlobalQColumns.EXPRESSION.index));
			
		 try {
			boolean added = multiModel.updateGlobalQ(nrow, CellParsers.cleanName(name), value, type, expression, notes);
			
			if(actionInColumnName) {
				if(renamingOption != Constants.RENAMING_OPTION_NONE) {
					MainGui.donotCleanDebugMessages = true;
					name = findAndReplace(cellValueBeforeChange, row, name,Constants.TitlesTabs.GLOBALQ.description, Constants.GlobalQColumns.NAME.index);
					MainGui.donotCleanDebugMessages = false;
				} else {
					Vector<FoundElement> found = search(cellValueBeforeChange);
					revalidateExpressions(found);
				}
			}
			
			statusBar.setMessage("...");
				
			jTableGlobalQ.cell_no_errors(row, Constants.GlobalQColumns.VALUE.index);
			jTableGlobalQ.cell_no_errors(row, Constants.GlobalQColumns.EXPRESSION.index);
				if(type.length()==0 && autocompleteWithDefaults) {
					tableGlobalQmodel.setValueAt(Constants.GlobalQType.FIXED.description, row, Constants.GlobalQColumns.TYPE.index);
				}
				if(value.length()==0 && autocompleteWithDefaults) {
					jTableGlobalQ.cell_has_defaults(row, Constants.GlobalQColumns.VALUE.index);
					tableGlobalQmodel.setValueAt(MainGui.globalQ_defaultValue_for_dialog_window, row, Constants.GlobalQColumns.VALUE.index);
					
					DebugMessage dm = new DebugMessage();
					 dm.setOrigin_table(Constants.TitlesTabs.GLOBALQ.description);
					 dm.setOrigin_col(Constants.GlobalQColumns.VALUE.index);
					 dm.setOrigin_row(nrow);
					 dm.setProblem("Defaut addition");
					 dm.setPriority(DebugConstants.PriorityType.DEFAULTS.priorityCode);
					 MainGui.addDebugMessage_ifNotPresent(dm);
					 recolorCellsWithErrors();
				} else {
					 if(value.trim().length()==0) {
						 DebugMessage dm = new DebugMessage();
						 dm.setOrigin_table(Constants.TitlesTabs.GLOBALQ.description);
						 dm.setOrigin_col(Constants.GlobalQColumns.VALUE.index);
						 dm.setOrigin_row(nrow);
						 dm.setProblem("Missing global quantity value");
						 dm.setPriority(DebugConstants.PriorityType.MINOR_EMPTY.priorityCode);
						 MainGui.addDebugMessage_ifNotPresent(dm);

						 statusBar.setMessage("System unable to parse part of the model. See Debug tab for details");
						 recolorCellsWithErrors();
					 } else {
						 clear_debugMessages_relatedWith(Constants.TitlesTabs.GLOBALQ.description, DebugConstants.PriorityType.MISSING.priorityCode, nrow, Constants.GlobalQColumns.VALUE.index);
						 clear_debugMessages_relatedWith(Constants.TitlesTabs.GLOBALQ.description, DebugConstants.PriorityType.MINOR_EMPTY.priorityCode, nrow, Constants.GlobalQColumns.VALUE.index);
					 }
				}
			
			statusBar.setMessage("...");
			 clear_debugMessages_relatedWith(Constants.TitlesTabs.GLOBALQ.description, DebugConstants.PriorityType.DEFAULTS.priorityCode, nrow, Constants.GlobalQColumns.NAME.index);
			 jTableGlobalQ.revalidate();
		 } catch(MySyntaxException ex ) {
			 	jTableGlobalQ.cell_has_errors(row, ex.getColumn());
				if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) ex.printStackTrace();
				if(type.length()==0 && autocompleteWithDefaults) tableGlobalQmodel.setValueAt_withoutUpdate(Constants.GlobalQType.FIXED.description, row, Constants.GlobalQColumns.TYPE.index);
				if(value.length()==0 && autocompleteWithDefaults) {
					jTableGlobalQ.cell_has_defaults(row, Constants.GlobalQColumns.VALUE.index);
					tableGlobalQmodel.setValueAt_withoutUpdate(MainGui.globalQ_defaultValue_for_dialog_window, row, Constants.GlobalQColumns.VALUE.index);
				}
			
		 }/*	catch (Exception ex) {
			 if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) ex.printStackTrace();
			if(type.length() != 0){
			if(type.compareTo(Constants.GlobalQType.FIXED.description)!=0) {
				boolean parsed = parseExpression(nrow,((String)tableGlobalQmodel.getValueAt(row, Constants.GlobalQColumns.EXPRESSION.index)));
				if(!parsed) {
					ediExpr.cell_has_errors(row);
					statusBar.setMessage("Global quantity expression contains some errors");
					tableGlobalQmodel.setValueAt_withoutUpdate("", row, Constants.GlobalQColumns.EXPRESSION.index);
					return;
				} else {
					ediExpr.cell_no_errors(row);
					 clear_debugMessages_relatedWith(Constants.TitlesTabs.GLOBALQ.description, DebugConstants.PriorityType.DEFAULTS.priorityCode, nrow, Constants.SpeciesColumns.NAME.index);
						
					multiModel.updateGlobalQ(nrow, name, value, type, expression, notes);
					statusBar.setMessage("...");
					return;
				}
			}
			}
			tableGlobalQmodel.setValueAt_withoutUpdate(MainGui.globalQ_defaultValue_for_dialog_window, row, Constants.GlobalQColumns.VALUE.index);
			tableGlobalQmodel.setValueAt_withoutUpdate("Fixed", row, Constants.GlobalQColumns.TYPE.index);
			updateGlobalQ(row);
		}*/
	
	 	
	 	
	 	
		 //addRemoveEmptyFields_inConsistencyChecks(tableGlobalQmodel, Constants.TitlesTabs.GLOBALQ.description);

	 }
	

	private void updateFunctions(int row) throws Exception{

		Integer nrow = (Integer)tableFunctionsmodel.getValueAt(row, 0);
		String name = ((String)tableFunctionsmodel.getValueAt(row,  Constants.FunctionsColumns.NAME.index)).trim();
		String equation = ((String)tableFunctionsmodel.getValueAt(row,  Constants.FunctionsColumns.EQUATION.index)).trim();
				
		if(name.trim().length() ==0){
			ConsistencyChecks.put_EmptyFields(Constants.TitlesTabs.FUNCTIONS.description, nrow,Constants.FunctionsColumns.NAME.index);
		} else {
			ConsistencyChecks.remove_EmptyFields(Constants.TitlesTabs.FUNCTIONS.description, nrow,Constants.FunctionsColumns.NAME.index);
		}
		if(equation.trim().length() ==0) {
			ConsistencyChecks.put_EmptyFields(Constants.TitlesTabs.FUNCTIONS.description, nrow,Constants.FunctionsColumns.EQUATION.index);
		} else {
			ConsistencyChecks.remove_EmptyFields(Constants.TitlesTabs.FUNCTIONS.description, nrow,Constants.FunctionsColumns.EQUATION.index);
		}
		
		
		
		String funName = FunctionsDB.extractJustName(name.trim());
		Function alreadyExist = multiModel.funDB.getFunctionByName(funName);
		int alreadyExIndex = multiModel.funDB.getFunctionIndex(alreadyExist);
		if(alreadyExIndex!=-1 && alreadyExIndex!=nrow) {
			  JOptionPane.showMessageDialog(new JButton(),"The new name you chose already exists!", "Invalid name!", JOptionPane.ERROR_MESSAGE);
			  tableFunctionsmodel.setValueAt( cellValueBeforeChange, row, Constants.FunctionsColumns.NAME.index);
			  return;
		}
	
		clear_debugMessages_relatedWith(Constants.TitlesTabs.FUNCTIONS.description, DebugConstants.PriorityType.MISSING.priorityCode, nrow, Constants.FunctionsColumns.EQUATION.index);
		clear_debugMessages_relatedWith(Constants.TitlesTabs.FUNCTIONS.description, DebugConstants.PriorityType.PARSING.priorityCode, nrow, Constants.FunctionsColumns.EQUATION.index);
		
		String complete_signature = null;
		try {
			complete_signature = addChangeFunction(nrow,name, equation);
			tableFunctionsmodel.setValueAt_withoutUpdate(complete_signature, row, Constants.FunctionsColumns.NAME.index);
			jTableFunctions.cell_no_errors(row, Constants.FunctionsColumns.NAME.index);
			jTableFunctions.cell_no_errors(row, Constants.FunctionsColumns.EQUATION.index);
			 MainGui.clear_debugMessages_relatedWith(Constants.TitlesTabs.FUNCTIONS.description, DebugConstants.PriorityType.PARSING.priorityCode, nrow,Constants.FunctionsColumns.NAME.index);
			 MainGui.clear_debugMessages_relatedWith(Constants.TitlesTabs.FUNCTIONS.description, DebugConstants.PriorityType.PARSING.priorityCode, nrow,Constants.FunctionsColumns.EQUATION.index);
			 MainGui.clear_debugMessages_relatedWith(Constants.TitlesTabs.FUNCTIONS.description, DebugConstants.PriorityType.INCONSISTENCIES.priorityCode, nrow,Constants.FunctionsColumns.EQUATION.index);
		
			 if(actionInColumnName  == true) {
				 	Function f = multiModel.funDB.getUserDefinedFunctionByIndex(nrow);
				 	/*InputStream is = new ByteArrayInputStream(cellValueBeforeChange.getBytes("UTF-8"));
					MR_Expression_Parser parser = new MR_Expression_Parser(is,"UTF-8");
					CompleteFunctionDeclaration root = parser.CompleteFunctionDeclaration();
					GetFunctionNameVisitor nameV = new GetFunctionNameVisitor();
					root.accept(nameV);
					String funNameInCellBefore =  nameV.getFunctionName();*/
				 	
				 	String funNameInCellBefore = FunctionsDB.extractJustName(cellValueBeforeChange.trim());
				 	
					if(cellValueBeforeChange.trim().length() > 0 
							&& funNameInCellBefore.compareTo(f.getName()) != 0 && 
							renamingOption != Constants.RENAMING_OPTION_NONE) {
						renameFunction_fromCellOrfromFunctionParameterFrame(f, nrow,funNameInCellBefore, null);
						funName = f.getName();
					}
			 }
		} catch (Exception ex) {
			 if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) ex.printStackTrace();
			 functionParameterFrame.setFunction(row, null);
			 if(ex instanceof MySyntaxException) {
				 MySyntaxException s = (MySyntaxException)ex;
				 if(s.getColumn() == Constants.FunctionsColumns.NAME.index) {
					 jTableFunctions.cell_has_errors(row, Constants.FunctionsColumns.NAME.index);
						
					 DebugMessage dm = new DebugMessage();
					 dm.setOrigin_table(Constants.TitlesTabs.FUNCTIONS.description);
					 dm.setProblem("Parsing error: " + ex.getMessage());
					 dm.setPriority(DebugConstants.PriorityType.PARSING.priorityCode);
					 dm.setOrigin_col(Constants.FunctionsColumns.NAME.index);
					 dm.setOrigin_row(nrow);
					 MainGui.addDebugMessage_ifNotPresent(dm);
				 } else {
					 if(s.getColumn() == Constants.FunctionsColumns.EQUATION.index) {
						 jTableFunctions.cell_has_errors(row, Constants.FunctionsColumns.EQUATION.index);
							
						 DebugMessage dm = new DebugMessage();
						 dm.setOrigin_table(Constants.TitlesTabs.FUNCTIONS.description);
						 dm.setProblem("Parsing error: " + ex.getMessage());
						 dm.setPriority(DebugConstants.PriorityType.PARSING.priorityCode);
						 dm.setOrigin_col(Constants.FunctionsColumns.EQUATION.index);
						 dm.setOrigin_row(nrow);
						 MainGui.addDebugMessage_ifNotPresent(dm);
					 }
				}
			    if(!MainGui.hidePopupWarning) JOptionPane.showMessageDialog(new JButton(),ex.getMessage(), "Syntax error!", JOptionPane.WARNING_MESSAGE);
						
			 } else if(ex instanceof MyChangeNotAllowedException) {
				 MyChangeNotAllowedException s = (MyChangeNotAllowedException)ex;
				 JOptionPane.showMessageDialog(new JButton(),s.getMessage(), "Syntax error!", JOptionPane.ERROR_MESSAGE);
				 tableFunctionsmodel.setValueAt_withoutUpdate(s.getPreviousValue(), row, Constants.FunctionsColumns.NAME.index);
			 } else  if(ex instanceof MyInconsistencyException) {
				 	 MyInconsistencyException s = (MyInconsistencyException)ex;
					 if(s.getColumn() == Constants.FunctionsColumns.EQUATION.index) {
							 jTableFunctions.cell_has_errors(row, Constants.FunctionsColumns.EQUATION.index);
								
							 DebugMessage dm = new DebugMessage();
							 dm.setOrigin_table(Constants.TitlesTabs.FUNCTIONS.description);
							 dm.setProblem("Inconsistency found: " + ex.getMessage());
							 dm.setPriority(DebugConstants.PriorityType.INCONSISTENCIES.priorityCode);
							 dm.setOrigin_col(Constants.FunctionsColumns.EQUATION.index);
							 dm.setOrigin_row(nrow);
							 MainGui.addDebugMessage_ifNotPresent(dm);
						 }
					
					 if(!MainGui.hidePopupWarning) JOptionPane.showMessageDialog(new JButton(),ex.getMessage(), "Syntax error!", JOptionPane.WARNING_MESSAGE);
				 }
			
			statusBar.setMessage("Problem in updating Function. See Debug tab for details.");
		}
		
		
		Vector usage = search(funName);
		Iterator it = usage.iterator();
		while(it.hasNext()) {
			FoundElement f = (FoundElement) it.next();
			if(f.getTableDescription().compareTo(Constants.TitlesTabs.FUNCTIONS.description)!= 0) continue;
			if(f.getCol() != Constants.FunctionsColumns.NAME.index) continue;
			if(f.getRow() != row) continue;
			usage.removeElement(f);
			break;
		}
		revalidateExpressions(usage);
		
		 jTableFunctions.revalidate();
	//	 addRemoveEmptyFields_inConsistencyChecks(tableFunctionsmodel, Constants.TitlesTabs.FUNCTIONS.description);

	 }
	
	private void updateCompartment(int row) throws Exception{
		
		Integer nrow = (Integer)tableCompartmentsmodel.getValueAt(row, 0);
		String name = (String)tableCompartmentsmodel.getValueAt(row, Constants.CompartmentsColumns.NAME.index);
		String type = (String)tableCompartmentsmodel.getValueAt(row, Constants.CompartmentsColumns.TYPE.index);
		String initial = (String)tableCompartmentsmodel.getValueAt(row, Constants.CompartmentsColumns.INITIAL_SIZE.index);
		String expression = (String)tableCompartmentsmodel.getValueAt(row, Constants.CompartmentsColumns.EXPRESSION.index);
		String notes = (String)tableCompartmentsmodel.getValueAt(row, Constants.CompartmentsColumns.NOTES.index);
		
		
		try {
			if(name.trim().length() ==0) return;
			int rowUpdatated = multiModel.updateCompartment(nrow, CellParsers.cleanName(name), type, initial, expression, notes);
			
			if(actionInColumnName && cellValueBeforeChange.trim().length()>0) {
				if(renamingOption != Constants.RENAMING_OPTION_NONE) {
					MainGui.donotCleanDebugMessages = true;
					name = findAndReplace(cellValueBeforeChange, row, name,Constants.TitlesTabs.GLOBALQ.description, Constants.GlobalQColumns.NAME.index);
					MainGui.donotCleanDebugMessages = false;
				} else {
					Vector<FoundElement> found = search(cellValueBeforeChange);
					revalidateExpressions(found);
				}
				
			}
			
			if(rowUpdatated==-1) {
				statusBar.setMessage("System unable to parse assignment expression for the compartment. See Debug tab for details");
				jTableCompartments.cell_has_errors(row, Constants.CompartmentsColumns.NAME.index);
			} else {
				statusBar.setMessage("...");
				tableCompartmentsmodel.setValueAt_withoutUpdate(CellParsers.cleanName(name), row, Constants.CompartmentsColumns.NAME.index);
				
				jTableCompartments.cell_no_errors(row, Constants.CompartmentsColumns.INITIAL_SIZE.index);
				jTableCompartments.cell_no_errors(row, Constants.CompartmentsColumns.EXPRESSION.index);
				if(type.length()==0 && autocompleteWithDefaults) {
					tableCompartmentsmodel.setValueAt_withoutUpdate(Constants.CompartmentsType.FIXED.description, row, Constants.CompartmentsColumns.TYPE.index);
				}  else {
					 if(type.length()==0) {
						 DebugMessage dm = new DebugMessage();
						 dm.setOrigin_table(Constants.TitlesTabs.COMPARTMENTS.description);
						 dm.setOrigin_col(Constants.CompartmentsColumns.TYPE.index);
						 dm.setOrigin_row(nrow);
						 dm.setProblem("Missing compartment type");
						 dm.setPriority(DebugConstants.PriorityType.MISSING.priorityCode);
						 MainGui.addDebugMessage_ifNotPresent(dm);

						 statusBar.setMessage("System unable to parse part of the model. See Debug tab for details");
						 recolorCellsWithErrors();
					 } else {
						 clear_debugMessages_relatedWith(Constants.TitlesTabs.COMPARTMENTS.description, DebugConstants.PriorityType.MISSING.priorityCode, nrow, Constants.CompartmentsColumns.TYPE.index);
					 }
				 }
				
				if(initial.length()==0 && autocompleteWithDefaults) {
					jTableCompartments.cell_has_defaults(row, Constants.CompartmentsColumns.INITIAL_SIZE.index);
					tableCompartmentsmodel.setValueAt_withoutUpdate(MainGui.compartment_defaultInitialValue, row, Constants.CompartmentsColumns.INITIAL_SIZE.index);
				} else {
					 if(initial.length()==0) {
						 DebugMessage dm = new DebugMessage();
						 dm.setOrigin_table(Constants.TitlesTabs.COMPARTMENTS.description);
						 dm.setOrigin_col(Constants.CompartmentsColumns.INITIAL_SIZE.index);
						 dm.setOrigin_row(nrow);
						 dm.setProblem("Missing compartment initial size");
						 dm.setPriority(DebugConstants.PriorityType.MINOR_EMPTY.priorityCode);
						 MainGui.addDebugMessage_ifNotPresent(dm);

						 statusBar.setMessage("System unable to parse part of the model. See Debug tab for details");
						 recolorCellsWithErrors();
					 } else {
						 clear_debugMessages_relatedWith(Constants.TitlesTabs.COMPARTMENTS.description, DebugConstants.PriorityType.MISSING.priorityCode, nrow, Constants.CompartmentsColumns.INITIAL_SIZE.index);
						 clear_debugMessages_relatedWith(Constants.TitlesTabs.COMPARTMENTS.description, DebugConstants.PriorityType.MINOR_EMPTY.priorityCode, nrow, Constants.CompartmentsColumns.INITIAL_SIZE.index);
					}
				 }
			}

			if(row == tableCompartmentsmodel.getRowCount()-1) { 
				tableCompartmentsmodel.removeAddEmptyRow_Listener();
				tableCompartmentsmodel.addRow(new Vector());
				tableCompartmentsmodel.addAddEmptyRow_Listener();
			}
		} catch(MySyntaxException ex ) {
			
			
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) ex.printStackTrace();
			jTableCompartments.cell_has_errors(row, ex.getColumn());
			
		}
		
	   finally{
		
		   jTableCompartments.highlightCellSelectedRow();
		   jTableCompartments.revalidate();
	   }
	    
	 }
	
	private boolean isNewNameDifferentFromOld(String name) {
		if(cellSelectedCol == Constants.SpeciesColumns.NAME.index && cellTableEdited.compareTo(Constants.TitlesTabs.SPECIES.description)==0) {
			if(!CellParsers.isMultistateSpeciesName(name)) {
				return (cellValueBeforeChange.compareTo(name) != 0);
			}
			try{
				MultistateSpecies oldS = new MultistateSpecies(multiModel,cellValueBeforeChange);
				MultistateSpecies newS = new MultistateSpecies(multiModel,name);
				return (newS.printCompleteDefinition(true).compareTo(oldS.printCompleteDefinition(true)) != 0);
			} catch (Exception e) {
				if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
			}
		}
		return false;
		
	}
	
	
	static boolean showedMessageEmptyNameField = false;
	
	private void updateSpecies(int row) throws Exception{
		if(tableSpeciesmodel.isEmpty(row)) return;
		Integer nrow = (Integer)tableSpeciesmodel.getValueAt(row, 0);
		String name = ((String)tableSpeciesmodel.getValueAt(row, Constants.SpeciesColumns.NAME.index)).trim();
		String initialQuantity = (String)tableSpeciesmodel.getValueAt(row, Constants.SpeciesColumns.INITIAL_QUANTITY.index);
		String type = (String)tableSpeciesmodel.getValueAt(row, Constants.SpeciesColumns.TYPE.index);
		String comp = (String)tableSpeciesmodel.getValueAt(row, Constants.SpeciesColumns.COMPARTMENT.index);
		 
		 
		String cleanName = CellParsers.cleanName(name, true);
		if(cleanName.compareTo(name)!=0) {
			tableSpeciesmodel.setValueAt(cleanName, row,Constants.SpeciesColumns.NAME.index);
			return;
		}
		if(actionInColumnName) clear_debugMessages_relatedWith(Constants.TitlesTabs.SPECIES.description, DebugConstants.PriorityType.PARSING.priorityCode, nrow, Constants.SpeciesColumns.NAME.index);
    	
		String expression = ((String)tableSpeciesmodel.getValueAt(row, Constants.SpeciesColumns.EXPRESSION.index)).trim();
		String notes = (String)tableSpeciesmodel.getValueAt(row, Constants.SpeciesColumns.NOTES.index);
		  
		//String allRow = new String(name+initialQuantity+type+comp+expression+notes);
		
		if(type.compareTo(Constants.SpeciesType.MULTISTATE.description)==0) {
			if(!CellParsers.isMultistateSpeciesName(name)) {
				DebugMessage dm = new DebugMessage();
				dm.setOrigin_table(Constants.TitlesTabs.SPECIES.description);
				dm.setProblem("Species "+ name + " has type "+Constants.SpeciesType.MULTISTATE.description + " but it does not follow the Multistate syntax. \nChange the Type to Reactions, Fixed or Assignment.");
				dm.setPriority(DebugConstants.PriorityType.INCONSISTENCIES.priorityCode);
				dm.setOrigin_col(Constants.SpeciesColumns.TYPE.index);
				dm.setOrigin_row(nrow);
				MainGui.addDebugMessage_ifNotPresent(dm);
			} else {
				clear_debugMessages_relatedWith(Constants.TitlesTabs.SPECIES.description, DebugConstants.PriorityType.INCONSISTENCIES.priorityCode, nrow, Constants.SpeciesColumns.TYPE.index);
			}
		} else {
			clear_debugMessages_relatedWith(Constants.TitlesTabs.SPECIES.description, DebugConstants.PriorityType.INCONSISTENCIES.priorityCode, nrow, Constants.SpeciesColumns.TYPE.index);
		}
		
		
		if(CellParsers.isMultistateSpeciesName(name)) {
			if(type.compareTo(Constants.SpeciesType.MULTISTATE.description)!=0) {
				DebugMessage dm = new DebugMessage();
				dm.setOrigin_table(Constants.TitlesTabs.SPECIES.description);
				dm.setProblem("Species "+ name + " has a name that fulfills the multistate syntax but the type is "+type +". \nChange the Type to Multistate or modify the name.");
				dm.setPriority(DebugConstants.PriorityType.INCONSISTENCIES.priorityCode);
				dm.setOrigin_col(Constants.SpeciesColumns.TYPE.index);
				dm.setOrigin_row(nrow);
				MainGui.addDebugMessage_ifNotPresent(dm);
			} else {
				clear_debugMessages_relatedWith(Constants.TitlesTabs.SPECIES.description, DebugConstants.PriorityType.INCONSISTENCIES.priorityCode, nrow, Constants.SpeciesColumns.TYPE.index);
			}
		
		}
		recolorCellsWithErrors();
		
		
		if(tableSpeciesmodel.disabledCell.contains(row+"_"+Constants.SpeciesColumns.EXPRESSION.index)) {
			expression = Constants.NOT_EDITABLE_VIEW;
		}
		
		if(tableSpeciesmodel.disabledCell.contains(row+"_"+Constants.SpeciesColumns.INITIAL_QUANTITY.index)) {
			initialQuantity = Constants.NOT_EDITABLE_VIEW;
		}
		
		if(name.length() == 0 && !tableSpeciesmodel.isEmpty(nrow-1)
			//	&& nrow!=tableSpeciesmodel.getRowCount()
				) {
			 if(!showedMessageEmptyNameField) JOptionPane.showMessageDialog(new JButton(),"Empty name field: not allowed", "Syntax error!", JOptionPane.ERROR_MESSAGE);
			 showedMessageEmptyNameField = true;
			 if(actionInColumnName) {
				 if(cellValueBeforeChange.length() > 0) {
					 tableSpeciesmodel.setValueAt_withoutUpdate(cellValueBeforeChange, row, Constants.FunctionsColumns.NAME.index);
				 }
			 }
			 jTableSpecies.revalidate();
			 return;
		}
		showedMessageEmptyNameField = false;
		
		if(actionInColumnName  == true) {
			int oldRenamingOption = renamingOption;
			if(cellValueBeforeChange.trim().length() > 0 
					&& isNewNameDifferentFromOld(name)
					&& multiModel.containsSpecies(name) 
					&& !addedByReaction
					&& multiModel.getSpeciesIndex(name)!=nrow
					&& !CellParsers.isMultistateSpeciesName(name)
					&& !CellParsers.isMultistateSpeciesName(cellValueBeforeChange)
					) {
				String rowContent = new String();
				for(int i = 0; i <tableSpeciesmodel.getColumnCount(); i++ ) {
					if(i!= Constants.SpeciesColumns.NAME.index) 	rowContent += tableSpeciesmodel.getValueAt(row,i)+" | ";
					else rowContent += cellValueBeforeChange +" | ";
				}
				
				String cmp1 = (String) tableSpeciesmodel.getValueAt(multiModel.getSpeciesIndex(name)-1, Constants.SpeciesColumns.COMPARTMENT.index);
				String cmp2 = (String) tableSpeciesmodel.getValueAt(row, Constants.SpeciesColumns.COMPARTMENT.index);
				if(cmp1.length() > 0 && cmp2.length() > 0  && cmp1.compareTo(cmp2)!=0) {
					//System.out.println("DIFFERENT COMPARTMENT SO OK");
					tableSpeciesmodel.setValueAt_withoutUpdate(name, row, Constants.SpeciesColumns.NAME.index);
				} 
				else {
				
					RenamingDialog dialog = new RenamingDialog(multiModel,name, rowContent, cellValueBeforeChange, Constants.DELETE_SPECIES_AND_REDIRECT);
					dialog.setVisible(true);
					String newName = dialog.getReturnString();
					dialog.dispose();
					if ((newName != null) && (newName.length() > 0) ) {
						renamingOption = Constants.RENAMING_OPTION_ALL;
						if(newName.compareTo("TO_BE_DELETED")==0) {
							tableSpeciesmodel.setValueAt_withoutUpdate(newName, row, Constants.SpeciesColumns.NAME.index);
							jTableSpecies.setRowSelectionInterval(row, row);
							jTabGeneral.setSelectedIndex(Constants.TitlesTabs.SPECIES.index);
							addedByReaction = true;
							deleteSelected();
						}
						else {
							if(newName.compareTo("NEW_NAME")==0) {
								String spcName = dialog.getNewSpeciesName();
						
								String cmpName = dialog.getNewCompartmentName();
								if(spcName!= null && spcName.length() >0) {
									tableSpeciesmodel.setValueAt(spcName, row, Constants.SpeciesColumns.NAME.index);
									name = spcName;
								} else {
									if(cmpName!= null&& cmpName.length() > 0) {
										tableSpeciesmodel.setValueAt_withoutUpdate("TO_BE_DELETED", row, Constants.SpeciesColumns.NAME.index);
										jTableSpecies.setRowSelectionInterval(row, row);
										jTabGeneral.setSelectedIndex(Constants.TitlesTabs.SPECIES.index);
										addedByReaction = true;
										deleteSelected();
										Species existingSp = multiModel.getSpecies(name);
										if(!existingSp.alreadyInComp(cmpName)) {
											//change each reference that before didn't have a compartment, to the complex string with the original compartment
											if(existingSp.getCompartments().size()==1) {
												String originalCmp = existingSp.getCompartments().get(0);
												String newSpeciesString = CellParsers.addCompartmentLabel(name, originalCmp);
												MainGui.donotCleanDebugMessages = true;
												boolean oldAutocompleteOption = autocompleteWithDefaults;
												boolean oldShowMessage = show_defaults_dialog_window;
												autocompleteWithDefaults = false;
												show_defaults_dialog_window = false;
												findAndReplace_excludeTableColumn(name, row, newSpeciesString,Constants.TitlesTabs.SPECIES.description, Constants.SpeciesColumns.NAME.index,Constants.TitlesTabs.SPECIES.description, Constants.SpeciesColumns.NAME.index);
												MainGui.donotCleanDebugMessages = false;
												autocompleteWithDefaults = oldAutocompleteOption;
												show_defaults_dialog_window = oldShowMessage;
											}
											//change the variable name so that the following findAndReplace will replace the right element with the new complex string
											int existing = multiModel.getSpeciesIndex(name);
											String compList = new String();
											compList = existingSp.getCompartment_listString();
											compList += ", " + cmpName; 
											tableSpeciesmodel.setValueAt(compList, existing-1, Constants.SpeciesColumns.COMPARTMENT.index);
											name = CellParsers.addCompartmentLabel(name, cmpName);
										}
								
									}
									else {
										tableSpeciesmodel.setValueAt_withoutUpdate(cellValueBeforeChange, row, Constants.SpeciesColumns.NAME.index);
									}
								} 
							} else 	tableSpeciesmodel.setValueAt(newName, row, Constants.SpeciesColumns.NAME.index);
						}
						MainGui.donotCleanDebugMessages = true;
						findAndReplace(cellValueBeforeChange, row, name,Constants.TitlesTabs.SPECIES.description, Constants.SpeciesColumns.NAME.index);
						renamingOption = oldRenamingOption;
						MainGui.donotCleanDebugMessages = false;
						return;
					}
	
					if(newName == null  || newName.length() == 0){ // user select cancel 
						addedByReaction = true;
						renamingOption = Constants.RENAMING_OPTION_NONE;
						tableSpeciesmodel.setValueAt(cellValueBeforeChange, row, Constants.SpeciesColumns.NAME.index);
						renamingOption = oldRenamingOption;
						addedByReaction = false;
					}
	
					if(CellParsers.isMultistateSpeciesName(name)) {
						tableSpeciesmodel.disableCell(row,Constants.SpeciesColumns.INITIAL_QUANTITY.index);
						tableSpeciesmodel.disableCell(row,Constants.SpeciesColumns.TYPE.index);
					} else {
						if(!tableSpeciesmodel.disabledCell.contains(row+"_"+Constants.SpeciesColumns.INITIAL_QUANTITY.index)) {
							tableSpeciesmodel.enableCell(row,Constants.SpeciesColumns.INITIAL_QUANTITY.index);
						}
						tableSpeciesmodel.enableCell(row,Constants.SpeciesColumns.TYPE.index);
					}
	
	
					if(row == jTableSpecies.getRowCount()-1) { 
						tableSpeciesmodel.removeAddEmptyRow_Listener();
						tableSpeciesmodel.addRow(new Vector());
						tableSpeciesmodel.addAddEmptyRow_Listener();
					}
	
					jTableSpecies.revalidate();
					return;
				}
			} else {
				if(CellParsers.isMultistateSpeciesName(name) &&
					(cellValueBeforeChange.length()  >0 && !CellParsers.isMultistateSpeciesName(cellValueBeforeChange)) ){
		
					JOptionPane.showMessageDialog(new JButton(),"You cannot rename a single state species to a multistate", "Syntax error!", JOptionPane.ERROR_MESSAGE);
					 
					addedByReaction = true;
					renamingOption = Constants.RENAMING_OPTION_NONE;
					tableSpeciesmodel.setValueAt(cellValueBeforeChange, row, Constants.SpeciesColumns.NAME.index);
					renamingOption = oldRenamingOption;
					tableSpeciesmodel.enableCell(row,Constants.SpeciesColumns.INITIAL_QUANTITY.index);
					tableSpeciesmodel.enableCell(row,Constants.SpeciesColumns.TYPE.index);
					addedByReaction = false;
				}
		
			}

			if(cellTableEdited.compareTo(Constants.TitlesTabs.SPECIES.description)==0 &&
				cellValueBeforeChange.length() > 0 && isNewNameDifferentFromOld(name) && renamingOption != Constants.RENAMING_OPTION_NONE && actionInColumnName  == true) {
				String n = findAndReplace(cellValueBeforeChange,row, name, Constants.TitlesTabs.SPECIES.description, Constants.SpeciesColumns.NAME.index);
				if(n!=null) name = n;
			}
		}
		
		addDefaultCompartment_ifNecessary(comp);
		
		Vector updated_rows = null;
		try{ 
			updated_rows = multiModel.updateSpecies(nrow, name, initialQuantity, type, comp, expression, autocompleteWithDefaults,notes,autoMergeSpecies);
		if(updated_rows == null) {
			jTableSpecies.cell_has_errors(row, Constants.SpeciesColumns.NAME.index);
	    	 statusBar.setMessage("System unable to parse part of the model. See Debug tab for details");
		 } else {
				jTableSpecies.cell_no_errors(row, Constants.SpeciesColumns.NAME.index);
				jTableSpecies.cell_no_errors(row, Constants.SpeciesColumns.EXPRESSION.index);
				jTableSpecies.cell_no_errors(row, Constants.SpeciesColumns.INITIAL_QUANTITY.index);
			 
			 
	    	 statusBar.setMessage("...");
			 updateCompartmentTable(updated_rows); 
			 addedByReaction = true;
		
			 
			 if(type.length()==0 && autocompleteWithDefaults) {
				// if(!name.contains("(")) tableSpeciesmodel.setValueAt_withoutUpdate(Constants.SpeciesType.REACTIONS.description, row, Constants.SpeciesColumns.TYPE.index);
				// else tableSpeciesmodel.setValueAt_withoutUpdate(Constants.SpeciesType.MULTISTATE.description, row, Constants.SpeciesColumns.TYPE.index);
				 
				 if(!CellParsers.isMultistateSpeciesName(name)) tableSpeciesmodel.setValueAt(Constants.SpeciesType.REACTIONS.description, row, Constants.SpeciesColumns.TYPE.index);
				 else tableSpeciesmodel.setValueAt(Constants.SpeciesType.MULTISTATE.description, row, Constants.SpeciesColumns.TYPE.index);
			 } else {
				 if(type.length()==0) {
					 DebugMessage dm = new DebugMessage();
					 dm.setOrigin_table(Constants.TitlesTabs.SPECIES.description);
					 dm.setOrigin_col(Constants.SpeciesColumns.TYPE.index);
					 dm.setOrigin_row(nrow);
					 dm.setProblem("Missing species type");
					 dm.setPriority(DebugConstants.PriorityType.MISSING.priorityCode);
					 MainGui.addDebugMessage_ifNotPresent(dm);

					 statusBar.setMessage("System unable to parse part of the model. See Debug tab for details");
					 recolorCellsWithErrors();
				 } else {
						clear_debugMessages_relatedWith(Constants.TitlesTabs.SPECIES.description, DebugConstants.PriorityType.MISSING.priorityCode, nrow, Constants.SpeciesColumns.TYPE.index);
				 }
			 }
			 //if(comp.length()==0 && autocompleteWithDefaults) tableSpeciesmodel.setValueAt_withoutUpdate("cell", row, Constants.SpeciesColumns.COMPARTMENT.index);
			 //if(initialQuantity.trim().length() ==0)tableSpeciesmodel.setValueAt_withoutUpdate(MainGui.species_defaultInitialValue, row, Constants.SpeciesColumns.INITIAL_QUANTITY.index);
			 if(comp.length()==0 && autocompleteWithDefaults) {
				 jTableSpecies.cell_has_defaults(row,Constants.SpeciesColumns.COMPARTMENT.index);
				 tableSpeciesmodel.setValueAt(MainGui.compartment_default_for_dialog_window, row, Constants.SpeciesColumns.COMPARTMENT.index);
				 DebugMessage dm = new DebugMessage();
				 dm.setOrigin_table(Constants.TitlesTabs.SPECIES.description);
				 dm.setOrigin_col(Constants.SpeciesColumns.COMPARTMENT.index);
				 dm.setOrigin_row(nrow);
				 dm.setProblem("Defaut addition");
				 dm.setPriority(DebugConstants.PriorityType.DEFAULTS.priorityCode);
				 MainGui.addDebugMessage_ifNotPresent(dm);
				 recolorCellsWithErrors();
			 } else {
				 if(comp.length()==0) {
					 DebugMessage dm = new DebugMessage();
					 dm.setOrigin_table(Constants.TitlesTabs.SPECIES.description);
					 dm.setOrigin_col(Constants.SpeciesColumns.COMPARTMENT.index);
					 dm.setOrigin_row(nrow);
					 dm.setProblem("Missing species compartment");
					 dm.setPriority(DebugConstants.PriorityType.MISSING.priorityCode);
					 MainGui.addDebugMessage_ifNotPresent(dm);

					 statusBar.setMessage("System unable to parse part of the model. See Debug tab for details");
					 recolorCellsWithErrors();
				 } else {
					 clear_debugMessages_relatedWith(Constants.TitlesTabs.SPECIES.description, DebugConstants.PriorityType.MISSING.priorityCode, nrow, Constants.SpeciesColumns.COMPARTMENT.index);
				 }
			 }
			 
			 if(initialQuantity.trim().length()==0 && autocompleteWithDefaults){
				 jTableSpecies.cell_has_defaults(row,Constants.SpeciesColumns.INITIAL_QUANTITY.index);
				 tableSpeciesmodel.setValueAt(MainGui.species_defaultInitialValue, row, Constants.SpeciesColumns.INITIAL_QUANTITY.index);
				 DebugMessage dm = new DebugMessage();
				 dm.setOrigin_table(Constants.TitlesTabs.SPECIES.description);
				 dm.setOrigin_col(Constants.SpeciesColumns.INITIAL_QUANTITY.index);
				 dm.setOrigin_row(nrow);
				 dm.setProblem("Defaut addition");
				 dm.setPriority(DebugConstants.PriorityType.DEFAULTS.priorityCode);
				 MainGui.addDebugMessage_ifNotPresent(dm);
				 recolorCellsWithErrors();
			 } else {
				 if(initialQuantity.trim().length()==0) {
					 DebugMessage dm = new DebugMessage();
					 dm.setOrigin_table(Constants.TitlesTabs.SPECIES.description);
					 dm.setOrigin_col(Constants.SpeciesColumns.INITIAL_QUANTITY.index);
					 dm.setOrigin_row(nrow);
					 dm.setProblem("Missing species initial quantity");
					 dm.setPriority(DebugConstants.PriorityType.MINOR_EMPTY.priorityCode);
					 MainGui.addDebugMessage_ifNotPresent(dm);

					 statusBar.setMessage("System unable to parse part of the model. See Debug tab for details");
					 recolorCellsWithErrors();
				 } else {
					 clear_debugMessages_relatedWith(Constants.TitlesTabs.SPECIES.description, DebugConstants.PriorityType.MISSING.priorityCode, nrow, Constants.SpeciesColumns.INITIAL_QUANTITY.index);
					 clear_debugMessages_relatedWith(Constants.TitlesTabs.SPECIES.description, DebugConstants.PriorityType.MINOR_EMPTY.priorityCode, nrow, Constants.SpeciesColumns.INITIAL_QUANTITY.index);
				 }
			 }
			 addedByReaction = false;
	 	 }
		} catch(MySyntaxException ex ) {
			
			
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) ex.printStackTrace();
			jTableSpecies.cell_has_errors(row, ex.getColumn());
		
			 tableSpeciesmodel.setValueAt_withoutUpdate(name, row, Constants.SpeciesColumns.NAME.index);
	    	 statusBar.setMessage("System unable to parse part of the model. See Debug tab for details");
	    	 addedByReaction = true;
	    	 if(type.length()==0 && autocompleteWithDefaults&&!tableSpeciesmodel.isEmpty(nrow-1)) {
	    		 if(!CellParsers.isMultistateSpeciesName(name)) tableSpeciesmodel.setValueAt_withoutUpdate(Constants.SpeciesType.REACTIONS.description, row, Constants.SpeciesColumns.TYPE.index);
	    		 else tableSpeciesmodel.setValueAt_withoutUpdate(Constants.SpeciesType.MULTISTATE.description, row, Constants.SpeciesColumns.TYPE.index);
	    	 }
	    	 if(comp.length()==0 && autocompleteWithDefaults&&!tableSpeciesmodel.isEmpty(nrow-1)) tableSpeciesmodel.setValueAt_withoutUpdate(MainGui.compartment_default_for_dialog_window, row, Constants.SpeciesColumns.COMPARTMENT.index);
	    	 if(initialQuantity.trim().length() ==0 && autocompleteWithDefaults&&!tableSpeciesmodel.isEmpty(nrow-1))tableSpeciesmodel.setValueAt_withoutUpdate(MainGui.species_defaultInitialValue, row, Constants.SpeciesColumns.INITIAL_QUANTITY.index);
	    	 addedByReaction = false;
			
		} catch(MyInconsistencyException ex) { 
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) ex.printStackTrace();
			MultistateSpecies ms = (new MultistateSpecies(multiModel,name));
			if(isNewNameDifferentFromOld(name)){
				RenamingDialog dialog = new RenamingDialog(ms.getSpeciesName(),ms.printCompleteDefinition(),ex.getCause().getMessage());
				dialog.setVisible(true);
				String newName = dialog.getReturnString();
				dialog.dispose();
				
					
				if ((newName != null) && (newName.length() > 0) ) {
					int oldRenamingOption = renamingOption;
					//String old_value = ((String)tableSpeciesmodel.getValueAt(row, Constants.SpeciesColumns.NAME.index)).trim();
					//old_value = old_value.replaceAll(ex.getCause().getMessage(), newName);
					//tableSpeciesmodel.setValueAt(old_value, row, Constants.SpeciesColumns.NAME.index);
					renamingOption = Constants.RENAMING_OPTION_ALL;
					if(newName.compareTo("MERGED_SPECIES")==0) {
						tableSpeciesmodel.setValueAt_withoutUpdate(newName, row, Constants.SpeciesColumns.NAME.index);
						jTableSpecies.setRowSelectionInterval(row, row);
						jTabGeneral.setSelectedIndex(Constants.TitlesTabs.SPECIES.index);
						addedByReaction = true;
						deleteSelected();

						String newCompleteName = multiModel.mergeMultistateSpecies(ex.getRow(),ms);
						renamingOption = oldRenamingOption;

						tableSpeciesmodel.setValueAt(newCompleteName, ex.getRow()-1, Constants.SpeciesColumns.NAME.index);
					}
					else {
						
						if(newName.compareTo("NEW_NAME")==0) {
							String spcName = dialog.getNewSpeciesName();
							String cmpName = dialog.getNewCompartmentName();
							if(spcName.length() >0) {
								tableSpeciesmodel.setValueAt(spcName, row, Constants.SpeciesColumns.NAME.index);
							} else {
								if(cmpName.length() > 0) {
									tableSpeciesmodel.setValueAt_withoutUpdate("TO_BE_DELETED", row, Constants.SpeciesColumns.NAME.index);
									jTableSpecies.setRowSelectionInterval(row, row);
									jTabGeneral.setSelectedIndex(Constants.TitlesTabs.SPECIES.index);
									addedByReaction = true;
									deleteSelected();
									multiModel.addCompartmentToSpecies(name, cmpName);
								}
								else {
									tableSpeciesmodel.setValueAt_withoutUpdate(cellValueBeforeChange, row, Constants.SpeciesColumns.NAME.index);
								}
							} 
						} else 	tableSpeciesmodel.setValueAt(newName, row, Constants.SpeciesColumns.NAME.index);
					}

					//return;
				}

				if(newName == null  || newName.length() == 0){ // user select cancel 
					tableSpeciesmodel.setValueAt_withoutUpdate(cellValueBeforeChange, row, Constants.SpeciesColumns.NAME.index);
				}
			}
		} catch (ClassNotFoundException ex){
		//	if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) 
				ex.printStackTrace();
			if(isNewNameDifferentFromOld(name)){
				String rowContent = new String();
				for(int i = 0; i <tableSpeciesmodel.getColumnCount(); i++ ) {
					rowContent += tableSpeciesmodel.getValueAt(row,i)+" | ";
				}
				RenamingDialog dialog = new RenamingDialog(ex.getCause().getMessage());
				//RenamingDialog dialog = new RenamingDialog(multiModel,name, rowContent, cellValueBeforeChange, Constants.DELETE_SPECIES_AND_REDIRECT);
				
				dialog.setVisible(true);
				String newName = dialog.getReturnString();
				dialog.dispose();
				if(newName == null  || newName.length() == 0){ // user select cancel 
					tableSpeciesmodel.setValueAt_withoutUpdate(cellValueBeforeChange, row, Constants.SpeciesColumns.NAME.index);
					//			return;
				}
				else if ((newName != null) && (newName.length() > 0) ) {
					//String old_value = ((String)tableSpeciesmodel.getValueAt(row, Constants.SpeciesColumns.NAME.index)).trim();
					//old_value = old_value.replaceAll(ex.getCause().getMessage(), newName);
					//tableSpeciesmodel.setValueAt(old_value, row, Constants.SpeciesColumns.NAME.index);
					
					if(newName.compareTo("NEW_NAME")==0) {
						String spcName = dialog.getNewSpeciesName();
						String cmpName = dialog.getNewCompartmentName();
						if(spcName.length() >0) {
							tableSpeciesmodel.setValueAt(spcName, row, Constants.SpeciesColumns.NAME.index);
						} else {
							if(cmpName.length() > 0) {
								
								tableSpeciesmodel.setValueAt_withoutUpdate("TO_BE_DELETED", row, Constants.SpeciesColumns.NAME.index);
								jTableSpecies.setRowSelectionInterval(row, row);
								jTabGeneral.setSelectedIndex(Constants.TitlesTabs.SPECIES.index);
								addedByReaction = true;
								deleteSelected();
								System.out.println("AGGIUNGERE 3 "+cmpName +" nella lista dei cmp della specie");
								multiModel.addCompartmentToSpecies(name, cmpName);
								
							}
							else {
								tableSpeciesmodel.setValueAt_withoutUpdate(cellValueBeforeChange, row, Constants.SpeciesColumns.NAME.index);
							}
						} 
					}
					
					
					//return;
				}
				
				
				
			
			}
			
	} finally{
	
		if(tableSpeciesmodel.getRowCount() < row) {
			name = ((String)tableSpeciesmodel.getValueAt(row, Constants.SpeciesColumns.NAME.index)).trim();
		
			if(CellParsers.isMultistateSpeciesName(name)) {
				 tableSpeciesmodel.disableCell(row,Constants.SpeciesColumns.INITIAL_QUANTITY.index);
			     tableSpeciesmodel.disableCell(row,Constants.SpeciesColumns.TYPE.index);
			 } else {
					if(!tableSpeciesmodel.disabledCell.contains(row+"_"+Constants.SpeciesColumns.INITIAL_QUANTITY.index)) {
						tableSpeciesmodel.enableCell(row,Constants.SpeciesColumns.INITIAL_QUANTITY.index);
					}
			     tableSpeciesmodel.enableCell(row,Constants.SpeciesColumns.TYPE.index);
			}
			
		}	
		
		
			if(row >= jTableSpecies.getRowCount()-1) { 
				tableSpeciesmodel.removeAddEmptyRow_Listener();
				tableSpeciesmodel.addRow(new Vector());
				tableSpeciesmodel.addAddEmptyRow_Listener();
			}
			
		
			jTableSpecies.setRowSelectionInterval(row,row);
			 jTableSpecies.revalidate();
			 
		}
	    // jListDebugShort.revalidate();
	     
	//	 addRemoveEmptyFields_inConsistencyChecks(tableSpeciesmodel, Constants.TitlesTabs.SPECIES.description);
			
	 }

	private void addDefaultCompartment_ifNecessary(String compList) {
		if(autocompleteWithDefaults) {
			Compartment cp =  null;
			Integer indexes_row_comp = 0;
			try{
				Vector<String> splittedList = CellParsers.extractElementsInList(multiModel, compList, Constants.TitlesTabs.SPECIES.description, Constants.SpeciesColumns.COMPARTMENT.description);
					
				for(int i = 0; i < splittedList.size(); i++) {
					String comp = splittedList.get(i);
					if (comp.length()>0) {
						if(multiModel.getComp(comp)==null) {
							indexes_row_comp = multiModel.updateCompartment(-1, 
									comp, 
									Constants.CompartmentsType.FIXED.description, 
									MainGui.compartment_defaultInitialValue, 
									new String(), 
									new String());
								DebugMessage dm = new DebugMessage();
								//dm.setOrigin_cause("Default Element");
								dm.setOrigin_table(Constants.TitlesTabs.COMPARTMENTS.description);
								dm.setProblem("Compartment "+ comp + " has been added by default.");
								dm.setPriority(DebugConstants.PriorityType.DEFAULTS.priorityCode);
								dm.setOrigin_col(Constants.CompartmentsColumns.NAME.index);
								dm.setOrigin_row(indexes_row_comp);
								MainGui.addDebugMessage_ifNotPresent(dm);
								updateCompartmentsTableFromMultiModel();
							}
						}
			}
				
				
				
				} catch (Exception ex) {
					ex.printStackTrace();
				}
		}
		
	}

	
	private String findAndReplace(String toSearch, int row, String replace, String fromTableDescription, int fromColumnNameIndex) throws Exception {
		return findAndReplace_excludeTableColumn(toSearch,row,replace,fromTableDescription, fromColumnNameIndex, null, null);
	}
	
	private String findAndReplace_excludeTableColumn(String toSearch, int row, String replace, String fromTableDescription, int fromColumnNameIndex, String excludeTableDescription, Integer excludeColumn) throws Exception {
		//if(cellValueBeforeChange.trim().length() == 0) return null;
		//if(cellValueBeforeChange.compareTo(replace) == 0) return null;
		
		if(toSearch.trim().length() == 0) return null;
		if(toSearch.compareTo(replace) == 0) return null;
		
		Vector<FoundElement> found = search_excludeTableColumn(toSearch, excludeTableDescription, excludeColumn);
		int minSize = 0;
		/*if(cellValueBeforeChange.compareTo(replace) != 0 
				//&& fromTableDescription.compareTo(Constants.TitlesTabs.SPECIES.description)==0
				) {*/
			found.insertElementAt(new FoundElement(fromTableDescription, row, fromColumnNameIndex),0);
			minSize = 1;
//		}
			
			
			
		if(found.size() > minSize ) {
			renamingFrame.setRenamingString(toSearch, replace, fromTableDescription, row);
			renamingFrame.fillFrameFields(found);
			if(renamingOption == Constants.RENAMING_OPTION_CUSTOM) {
			//	renamingFrame.setSelected(new FoundElement(Constants.TitlesTabs.SPECIES.description, row, Constants.SpeciesColumns.NAME.index));
				renamingFrame.setSelected(new FoundElement(fromTableDescription, row, fromColumnNameIndex));
				renamingFrame.setVisible(true);
			} else if(renamingOption == Constants.RENAMING_OPTION_ALL){
				renamingOption = Constants.RENAMING_OPTION_NONE;
				try{
					renamingFrame.renameAll();
				}catch(Exception ex) {
					if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) ex.printStackTrace();
				}
				renamingOption = Constants.RENAMING_OPTION_ALL;
			}
		}
		String name = replace;
		if(renamingFrame.getClosingOperation()=="Cancel") { name = cellValueBeforeChange; }
		return name;
	}
	
	
	private void replace(Vector<FoundElement> found, String toSearch, int row, String replace, String fromTableDescription, int fromColumnNameIndex) throws Exception {
		int oldRenaming = renamingOption;
		if(renamingOption == Constants.RENAMING_OPTION_NONE) { // it can be the renaming from the properties function frame, so if the name of the function is changed, I have to change that field
				Iterator it = found.iterator();
				FoundElement fDecl = null;
				while(it.hasNext()) {
					FoundElement f1 = (FoundElement) it.next();
					if(f1.getTableDescription().compareTo(Constants.TitlesTabs.FUNCTIONS.description)!= 0) continue;
					if(f1.getCol() != Constants.FunctionsColumns.NAME.index) continue;
					if(f1.getRow() != row) continue;
					fDecl = f1;
					break;
				}
				found.clear();
				found.add(fDecl);
				renamingOption = Constants.RENAMING_OPTION_ALL;
		}
		
		
		if(found.size() > 0 ) {
			renamingFrame.setRenamingString(toSearch, replace, fromTableDescription, row);
			renamingFrame.fillFrameFields(found);
			if(renamingOption == Constants.RENAMING_OPTION_CUSTOM) {
				renamingFrame.setSelected(new FoundElement(fromTableDescription, row, fromColumnNameIndex));
				renamingOption = Constants.RENAMING_OPTION_NONE;
				renamingFrame.setVisible(true);
				
			} else if(renamingOption == Constants.RENAMING_OPTION_ALL){
				renamingOption = Constants.RENAMING_OPTION_NONE;
				try{
					renamingFrame.renameAll();
				}catch(Exception ex) {
					if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) ex.printStackTrace();
				}
			}
		}
		renamingOption = oldRenaming;
	}
	
	private void updateReaction(int row) throws Exception{
		
		Integer nrow = (Integer)tableReactionmodel.getValueAt(row, 0);
		String name = ((String)tableReactionmodel.getValueAt(row, Constants.ReactionsColumns.NAME.index)).trim();
		String reaction_string = ((String)tableReactionmodel.getValueAt(row, Constants.ReactionsColumns.REACTION.index)).trim();
		String type = ((String)tableReactionmodel.getValueAt(row,  Constants.ReactionsColumns.TYPE.index)).trim();
		String equation = ((String)tableReactionmodel.getValueAt(row,  Constants.ReactionsColumns.KINETIC_LAW.index)).trim();
		String exp = ((String)tableReactionmodel.getValueAt(row,  Constants.ReactionsColumns.EXPANDED.index)).trim();
		String notes = ((String)tableReactionmodel.getValueAt(row,  Constants.ReactionsColumns.NOTES.index)).trim();
		
		if(!MainGui.donotCleanDebugMessages) MainGui.clear_debugMessages_defaults_relatedWith(Constants.TitlesTabs.REACTIONS.description, nrow);
	
		if(reaction_string.trim().length() == 0) return;
		Vector metabolites = new Vector<>();
		try{ 
			 metabolites = CellParsers.parseReaction(this.multiModel,reaction_string,nrow-1);
		} catch(Exception ex) { //stop strings like 2*a -> which are wrong formatted
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) ex.printStackTrace();
			EditableCellRenderer_MSMB edicr = (EditableCellRenderer_MSMB)(jTableReactions.getCellRenderer(row, Constants.ReactionsColumns.REACTION.index));
			//edicr.cell_has_errors(row);
			tableReactionmodel.setValueAt_withoutUpdate(reaction_string, row, Constants.ReactionsColumns.REACTION.index);
			statusBar.setMessage("System unable to parse part of the model. See Debug tab for details");
			jTableReactions.revalidate();
			recolorCellsWithErrors();
			return;
		}
		
		

		
		boolean oldAutocompleteWithDefaults = autocompleteWithDefaults;
		if(show_defaults_dialog_window && actionInColumnName) {
				species_default_for_dialog_window = multiModel.getUndefinedSpeciesInReaction(nrow, reaction_string);
				if(species_default_for_dialog_window.size() >0) {
					String string_list = new String();
					for(int i = 0; i < species_default_for_dialog_window.size(); i++) {
						string_list += " " +(i+1)+". " + species_default_for_dialog_window.get(i);
						string_list += System.getProperty("line.separator");
					}
					string_list += "with the initial value of \"" + species_defaultInitialValue + "\" and " +System.getProperty("line.separator");
					string_list += "in the compartment \"" + compartment_default_for_dialog_window + " (size "+compartment_defaultInitialValue+")\" (created if not existing)" ;
					final JCheckBox checkBox_dialog_window =  new JCheckBox("Don't show this message again");
				//	multiModel.getAllCompartments();

					Object[] options = {"Accept", "Reject"};
					Object[] array = {"The system is about to define the following "
							+ species_default_for_dialog_window.size() + " new species: "
							+ System.getProperty("line.separator")
							+ string_list + System.getProperty("line.separator"),
							checkBox_dialog_window};

					final JOptionPane optionPane = new JOptionPane(
							array,
							JOptionPane.QUESTION_MESSAGE,
							JOptionPane.YES_NO_OPTION,
							null,     //do not use a custom Icon
							options,  //the titles of buttons
							options[0]); //default button title

					final JDialog dialog = new JDialog(this, 
							"Default species definition",
							true);
					dialog.setContentPane(optionPane);

					optionPane.addPropertyChangeListener(
							new PropertyChangeListener() {
								public void propertyChange(PropertyChangeEvent e) {
									String prop = e.getPropertyName();

									if (dialog.isVisible() 
											&& (e.getSource() == optionPane)
											&& (prop.equals(JOptionPane.VALUE_PROPERTY))) {
										if(checkBox_dialog_window.isSelected()) {
											preferenceFrame.setCheckboxDialogWindowForDefaults(false);
										}
										dialog.setVisible(false);
									}
								}
							});

					dialog.pack();
					dialog.setLocationRelativeTo(null);
					dialog.setVisible(true);

					if(optionPane.getValue() == options[1]) {
					    //EditableCellRenderer edi = (EditableCellRenderer)(jTableReactions.getCellRenderer(row, Constants.ReactionsColumns.REACTION.index));
						//edi.cell_has_errors(row);
						//multiModel.removeSpecies(species_default_for_dialog_window);
						DebugMessage dm = new DebugMessage();
						dm.setOrigin_table(Constants.TitlesTabs.REACTIONS.description);
						dm.setOrigin_col(Constants.ReactionsColumns.REACTION.index);
						dm.setOrigin_row(nrow);
						dm.setProblem("The following species are not defined: "+species_default_for_dialog_window);
						dm.setPriority(DebugConstants.PriorityType.MISSING.priorityCode);
						MainGui.addDebugMessage_ifNotPresent(dm);

						statusBar.setMessage("System unable to parse part of the model. See Debug tab for details");
						jTableReactions.revalidate();
						recolorCellsWithErrors();
						return;
					} else {
						autocompleteWithDefaults = true; // in this way compartments and species are added automatically
						
					}
				}
	        }
		
		
		
	if(autocompleteWithDefaults && actionInColumnName) addDefaultCompartment_ifNecessary(compartment_default_for_dialog_window);
	
	
	if(type.compareTo(Constants.ReactionType.MASS_ACTION.description)==0) {
		Vector subs = (Vector)metabolites.get(0);
	    Vector prod =(Vector)metabolites.get(1);
		Vector mod = (Vector)metabolites.get(2);
		if(subs.size() == 0) {
			DebugMessage dm = new DebugMessage();
			dm.setOrigin_table(Constants.TitlesTabs.REACTIONS.description);
			dm.setOrigin_col(Constants.ReactionsColumns.REACTION.index);
			dm.setOrigin_row(nrow);
			dm.setProblem("Mass Action kinetic cannot be applied because the reaction does not have substrates!");
			dm.setPriority(DebugConstants.PriorityType.INCONSISTENCIES.priorityCode);
			MainGui.addDebugMessage_ifNotPresent(dm);

			statusBar.setMessage("System unable to parse part of the model. See Debug tab for details");
			jTableReactions.revalidate();
			recolorCellsWithErrors();
			return;
		} else {
			clear_debugMessages_relatedWith(Constants.TitlesTabs.REACTIONS.description, DebugConstants.PriorityType.INCONSISTENCIES.priorityCode, nrow, Constants.ReactionsColumns.REACTION.index);
		}
	} else {
		clear_debugMessages_relatedWith(Constants.TitlesTabs.REACTIONS.description, DebugConstants.PriorityType.INCONSISTENCIES.priorityCode, nrow, Constants.ReactionsColumns.REACTION.index);
	}
	
	
	
	if(tableReactionmodel.disabledCell.contains(row+"_"+Constants.ReactionsColumns.KINETIC_LAW.index)) {
		equation = Constants.NOT_EDITABLE_VIEW;
	}
	
	try{
		Vector updated_rows = multiModel.updateReaction(nrow, name, reaction_string, type, equation, exp, notes, autocompleteWithDefaults,actionInColumnName);
		boolean parseErrors = false;
		autocompleteWithDefaults = oldAutocompleteWithDefaults;
		
	
		if(equation.length() > 0 && !tableReactionmodel.disabledCell.contains(row+"_"+Constants.ReactionsColumns.KINETIC_LAW.index)
				//&& type.compareTo(Constants.ReactionType.USER_DEFINED.description)==0
				//|| type.compareTo(Constants.ReactionType.PRE_DEFINED.description)==0
				) {
			try{
				boolean massAction = false; 
				if(type.compareTo(Constants.ReactionType.MASS_ACTION.description)==0) massAction = true;
				
			/*	Vector newEquation_listOfNewNames = multiModel.check_ifSingleFunctionCallAndSingleParameters(massAction,nrow,name,equation);
				if(newEquation_listOfNewNames!= null) {
					Vector listOfNewNames = (Vector) newEquation_listOfNewNames.get(1);
					if(listOfNewNames.size() > 0 && !dialogAcceptRejectShowed && cellSelectedRow == row) {
				*/		
				Vector<Vector<String>> elementsToDefine = multiModel.check_ifSingleFunctionCallOrSingleGlobalQ(massAction,nrow,name,equation);
				if(elementsToDefine.size()!=0 && !dialogAcceptRejectShowed && cellSelectedRow == row) {
						if(!oldAutocompleteWithDefaults) {
							tableReactionmodel.setValueAt_withoutUpdate(equation, row, Constants.ReactionsColumns.KINETIC_LAW.index);
							multiModel.updateReactionEditableView(row,equation);
							updateFunctionsTableFromMultiModel();
							updateGlobalQTableFromMultiModel();
							autocompleteWithDefaults = oldAutocompleteWithDefaults;
							DebugMessage dm = new DebugMessage();
							dm.setOrigin_table(Constants.TitlesTabs.REACTIONS.description);
							dm.setOrigin_col(Constants.ReactionsColumns.KINETIC_LAW.index);
							dm.setOrigin_row(nrow);
							dm.setProblem("Problem parsing Kinetic law.\nMake sure the expression is a single (properly defined) function call with single parameters names as actual parameters.");
							dm.setPriority(DebugConstants.PriorityType.PARSING.priorityCode);
							MainGui.addDebugMessage_ifNotPresent(dm);

							statusBar.setMessage("System unable to parse part of the model. See Debug tab for details");
							jTableReactions.revalidate();
							recolorCellsWithErrors();
							return;
						}
					
						FunctionFromExpressionDialog newFunctionDialog = new FunctionFromExpressionDialog(elementsToDefine, equation,massAction);
						newFunctionDialog.pack();
						newFunctionDialog.setLocationRelativeTo(null);
						newFunctionDialog.setVisible(true);
						dialogAcceptRejectShowed = true;
						Vector<Vector<String>> returnedElements = newFunctionDialog.getElementsToReturn();
						Vector<Vector<String>> noDuplicatesReturnedElements = new Vector<>();
						newFunctionDialog.dispose();
						
						for(int j = 0; j <returnedElements.size(); j++ ) {
							String table = returnedElements.get(j).get(0);
							String returnedName = returnedElements.get(j).get(1);
							String nameToCheck = new String(returnedName);
							
							if(table.compareTo(Constants.TitlesTabs.FUNCTIONS.description)==0) {
								int i = 1;
								while(multiModel.funDB.getFunctionByName(nameToCheck) != null) {
									nameToCheck = returnedName + "_"+i;
									i++;
								}
							} else {
								int i = 1;
								while(multiModel.getGlobalQ(nameToCheck) != null) {
									nameToCheck = returnedName + "_"+i;
									i++;
								}
							}
							
							Vector noDuplicatesElement = new Vector<>();
							noDuplicatesElement.add(returnedElements.get(j).get(0));
							noDuplicatesElement.add(nameToCheck);
							
							String newEquation = CellParsers.replaceVariableInExpression(returnedElements.get(j).get(2), returnedElements.get(j).get(1), nameToCheck);
							noDuplicatesElement.add(newEquation);
							if(returnedElements.get(j).size() > 3) noDuplicatesElement.add(returnedElements.get(j).get(3));
							noDuplicatesReturnedElements.add(noDuplicatesElement);
						}
						
						String newKineticLaw = new String(equation);
						
						/*if(!massAction) {
							MutablePair<String, String> pair = multiModel.add_function_4_reaction_funDB(name, nrow, nameToCheck,equation);
							newKineticLaw = pair.left;
						} else {
							MutablePair<String, String> pair = multiModel.add_globalQ_4_reaction_globalQDB(name, nrow, nameToCheck,equation);
							newKineticLaw = pair.left;
						}*/
						
						
						for(int i = 0; i < noDuplicatesReturnedElements.size(); i++) {
							Vector element = noDuplicatesReturnedElements.get(i);
							if(element.get(0).toString().compareTo(Constants.TitlesTabs.GLOBALQ.description) ==0) {
								MutablePair<String, String> pair = multiModel.add_globalQ_4_reaction_globalQDB(name, nrow, element.get(1).toString(),element.get(2).toString());
								if(massAction){
									if(noDuplicatesReturnedElements.size() ==1) { newKineticLaw = pair.left; break;}
								} else {
									newKineticLaw = replaceExpressionInActualParameter(newKineticLaw,new Integer(element.get(3).toString()),element.get(1).toString());
								}
							} else {
								MutablePair<String, String> pair = multiModel.add_function_4_reaction_funDB(name, nrow, element.get(1).toString(),element.get(2).toString());
								if(noDuplicatesReturnedElements.size() ==1) { newKineticLaw = pair.left; break;}
								else newKineticLaw = "TO_CHECK";
							}
						}
						
						oldAutocompleteWithDefaults = autocompleteWithDefaults;

						tableReactionmodel.setValueAt_withoutUpdate(newKineticLaw, row, Constants.ReactionsColumns.KINETIC_LAW.index);

						updateReaction(row);
						multiModel.updateReactionEditableView(row,newKineticLaw);
						updateFunctionsTableFromMultiModel();
						updateGlobalQTableFromMultiModel();
						autocompleteWithDefaults = oldAutocompleteWithDefaults;
						
						
						/*
						String string_list = new String();
						for(int i = 0; i <listOfNewNames.size(); i++) {
							string_list += " " +(i+1)+". " + listOfNewNames.get(i);
							string_list += System.getProperty("line.separator");
						}
						final JCheckBox checkBox_dialog_window =  new JCheckBox("Don't show this message again");

						Object[] options = {"Accept", "Reject"};
						Object[] array = {"The system is about to define the following "
								+ listOfNewNames.size() + " new elements: "
								+ System.getProperty("line.separator")
								+ string_list + System.getProperty("line.separator"),
								checkBox_dialog_window};

						final JOptionPane optionPane = new JOptionPane(
								array,
								JOptionPane.QUESTION_MESSAGE,
								JOptionPane.YES_NO_OPTION,
								null,     //do not use a custom Icon
								options,  //the titles of buttons
								options[0]); //default button title

						final JDialog dialog = new JDialog(this, 
								"Default function/parameter definition",
								true);
						dialog.setContentPane(optionPane);

						optionPane.addPropertyChangeListener(
								new PropertyChangeListener() {
									public void propertyChange(PropertyChangeEvent e) {
										String prop = e.getPropertyName();

										if (dialog.isVisible() 
												&& (e.getSource() == optionPane)
												&& (prop.equals(JOptionPane.VALUE_PROPERTY))) {
											if(checkBox_dialog_window.isSelected()) {
												preferenceFrame.setCheckboxDialogWindowForDefaults(false);
											}
											dialog.setVisible(false);
										}
									}
								});

						dialog.pack();
						dialog.setLocationRelativeTo(null);
						dialog.setVisible(true);
						dialogAcceptRejectShowed = true;
						if(optionPane.getValue() == options[1]) { //reject
							EditableCellRenderer edi = (EditableCellRenderer)(jTableReactions.getCellRenderer(row, Constants.ReactionsColumns.KINETIC_LAW.index));
							//edi.cell_has_errors(row);
							//multiModel.removeSpecies(species_default_for_dialog_window);
							DebugMessage dm = new DebugMessage();
							dm.setOrigin_table(Constants.TitlesTabs.REACTIONS.description);
							dm.setOrigin_col(Constants.ReactionsColumns.KINETIC_LAW.index);
							dm.setOrigin_row(nrow);
							if( type.compareTo(Constants.ReactionType.USER_DEFINED.description)==0) dm.setProblem("Free expressions in kinetic law are not allowed. \nThe kinetic law should be a single function call with single parameters names as actual parameters.");
							else if( type.compareTo(Constants.ReactionType.MASS_ACTION.description)==0) dm.setProblem("Free expressions in kinetic law are not allowed. \nThe kinetic law should be a single parameters name.");
							dm.setPriority(DebugConstants.PriorityType.PARSING.priorityCode);
							MainGui.addDebugMessage_ifNotPresent(dm);

							statusBar.setMessage("System unable to parse part of the model. See Debug tab for details");
							jTableReactions.revalidate();
							recolorCellsWithErrors();
							return;
						} else { //accept
							oldAutocompleteWithDefaults = autocompleteWithDefaults;

							tableReactionmodel.setValueAt_withoutUpdate(newEquation_listOfNewNames.get(0), row, Constants.ReactionsColumns.KINETIC_LAW.index);

							updateReaction(row);
							multiModel.updateReactionEditableView(row,newEquation_listOfNewNames.get(0).toString());
							updateFunctionsView();
							updateGlobalQTableFromMultiModel();
							//updateGUIFromMultimodel();
							autocompleteWithDefaults = oldAutocompleteWithDefaults;
						}
						*/
					//} else if(listOfNewNames.size() > 0) { //dialog already shown but something is still wrong
				} else if (elementsToDefine.size()!=0) { //dialog already shown but something is still wrong
						//EditableCellRenderer edi = (EditableCellRenderer)(jTableReactions.getCellRenderer(row, Constants.ReactionsColumns.KINETIC_LAW.index));
						//edi.cell_has_errors(row);
						//multiModel.removeSpecies(species_default_for_dialog_window);
						
						
						DebugMessage dm = new DebugMessage();
						dm.setOrigin_table(Constants.TitlesTabs.REACTIONS.description);
						dm.setOrigin_col(Constants.ReactionsColumns.KINETIC_LAW.index);
						dm.setOrigin_row(nrow);
						dm.setProblem("Problem parsing Kinetic law.\nMake sure the expression is a single (properly defined) function call with single parameters names as actual parameters.");
						dm.setPriority(DebugConstants.PriorityType.PARSING.priorityCode);
						MainGui.addDebugMessage_ifNotPresent(dm);

						statusBar.setMessage("System unable to parse part of the model. See Debug tab for details");
						jTableReactions.revalidate();
						recolorCellsWithErrors();
						return;

					}
				//} 
				
				autocompleteWithDefaults = oldAutocompleteWithDefaults;
				
				clear_debugMessages_relatedWith(Constants.TitlesTabs.REACTIONS.description, DebugConstants.PriorityType.MISSING.priorityCode, nrow, Constants.ReactionsColumns.KINETIC_LAW.index);
				Vector paramInEquation = multiModel.funDB.addMapping(row, equation, type);
				
				if(autocompleteWithDefaults) {
					addUndefinedGlobalQ(paramInEquation);
				} else {
					if(paramInEquation.size() > 0) {
						String message = new String("The following elements are used but never declared: " );
						for(int i = 0; i <paramInEquation.size();i++ ) {
							    message += paramInEquation.get(i) +" ";
						}
						//throw new MySyntaxException(Constants.ReactionsColumns.KINETIC_LAW.index, message,Constants.TitlesTabs.REACTIONS.description);
					    DebugMessage dm = new DebugMessage();
						dm.setOrigin_table(Constants.TitlesTabs.REACTIONS.description);
					    dm.setOrigin_col(Constants.ReactionsColumns.KINETIC_LAW.index);
					    dm.setOrigin_row(nrow);
						dm.setProblem(message);
					    dm.setPriority(DebugConstants.PriorityType.MISSING.priorityCode);
						MainGui.addDebugMessage_ifNotPresent(dm);
						parseErrors = true;
					}
				}
			/*} catch(ParseException ex) {
				String message = new String(ex.getErrorInfo());
				if(ex.getErrorInfo().contains("Unexpected \",\"") || ex.getErrorInfo().contains("Encountered \",\"") || ex.getErrorInfo().contains("implicit multiplication")) {
					message += System.lineSeparator() + "Are you maybe trying to use a function that has not been defined?";
				}
				
				if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) ex.printStackTrace();
				clear_debugMessages_relatedWith(Constants.TitlesTabs.REACTIONS.description, DebugConstants.PriorityType.PARSING.priorityCode, nrow, Constants.ReactionsColumns.KINETIC_LAW.index);
				 
			    DebugMessage dm = new DebugMessage();
				dm.setOrigin_table(Constants.TitlesTabs.REACTIONS.description);
			    dm.setOrigin_col(Constants.ReactionsColumns.KINETIC_LAW.index);
			    dm.setOrigin_row(nrow);
				dm.setProblem(message);
			    dm.setPriority(DebugConstants.PriorityType.PARSING.priorityCode);
				MainGui.addDebugMessage_ifNotPresent(dm);
				parseErrors = true;
				//throw new MySyntaxException(Constants.ReactionsColumns.KINETIC_LAW.index, message,Constants.TitlesTabs.REACTIONS.description);;
			*/
			} catch(MySyntaxException ex) {
				
				String message = new String(ex.getMessage());
				if(message.contains("Unexpected \",\"") || message.contains("Encountered \",\"") || message.contains("implicit multiplication")) {
					message += System.lineSeparator() + "Are you maybe trying to use a function that has not been defined?";
				}
				
				if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) ex.printStackTrace();
				clear_debugMessages_relatedWith(Constants.TitlesTabs.REACTIONS.description, DebugConstants.PriorityType.PARSING.priorityCode, nrow, Constants.ReactionsColumns.KINETIC_LAW.index);
				 
			    DebugMessage dm = new DebugMessage();
				dm.setOrigin_table(Constants.TitlesTabs.REACTIONS.description);
			    dm.setOrigin_col(Constants.ReactionsColumns.KINETIC_LAW.index);
			    dm.setOrigin_row(nrow);
				dm.setProblem(message);
			    dm.setPriority(DebugConstants.PriorityType.PARSING.priorityCode);
				MainGui.addDebugMessage_ifNotPresent(dm);
				parseErrors = true;
				//throw new MySyntaxException(Constants.ReactionsColumns.KINETIC_LAW.index, message,Constants.TitlesTabs.REACTIONS.description);
			
			}catch(Exception ex) {
				//if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) 
					ex.printStackTrace();
				//System.out.println("PROBLEMS IN EQUATION:" + equation);
				//System.out.println("-------------------------mapping of a predefined not yet in the db");
			} 
			
		}
		
	  
		if(updated_rows == null) {
	    	// edi.cell_has_errors(row);
	    	 statusBar.setMessage("System unable to parse part of the model. See Debug tab for details");
			 
		 } else {
			 jTableReactions.cell_no_errors(row,Constants.ReactionsColumns.REACTION.index);
			 if(!parseErrors) {
				 jTableReactions.cell_no_errors(row,Constants.ReactionsColumns.KINETIC_LAW.index);
				 clear_debugMessages_relatedWith(Constants.TitlesTabs.REACTIONS.description, DebugConstants.PriorityType.PARSING.priorityCode, nrow, Constants.ReactionsColumns.KINETIC_LAW.index);
				 statusBar.setMessage("...");
				 clear_debugMessages_relatedWith(Constants.TitlesTabs.REACTIONS.description, DebugConstants.PriorityType.PARSING.priorityCode, nrow, Constants.ReactionsColumns.REACTION.index);
			} else {
				 jTableReactions.cell_has_errors(row,Constants.ReactionsColumns.KINETIC_LAW.index);
			 }
			 addedByReaction = true;
			 updateSpeciesTable_defaults(updated_rows);
			 addedByReaction = false;
		 }
		 
		 
		 
	} catch(MySyntaxException ex) {
		if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES)	ex.printStackTrace();
		clear_debugMessages_relatedWith(Constants.TitlesTabs.REACTIONS.description, DebugConstants.PriorityType.PARSING.priorityCode, nrow, Constants.ReactionsColumns.REACTION.index);
		 
	    DebugMessage dm = new DebugMessage();
		dm.setOrigin_table(Constants.TitlesTabs.REACTIONS.description);
	    dm.setOrigin_col(Constants.ReactionsColumns.REACTION.index);
	    dm.setOrigin_row(nrow);
		dm.setProblem(ex.getMessage());
	    dm.setPriority(DebugConstants.PriorityType.PARSING.priorityCode);
		MainGui.addDebugMessage_ifNotPresent(dm);
		
		EditableCellRenderer_MSMB edi = (EditableCellRenderer_MSMB)(jTableReactions.getCellRenderer(row, Constants.ReactionsColumns.REACTION.index));
		// edi.cell_has_errors(row);	
		
		//throw new MySyntaxException(Constants.ReactionsColumns.KINETIC_LAW.index, message,Constants.TitlesTabs.REACTIONS.description);
	
	}
		 
	// addRemoveEmptyFields_inConsistencyChecks(tableReactionmodel, Constants.TitlesTabs.REACTIONS.description);
	
	jTableReactions.revalidate();
		 
	 recolorCellsWithErrors();
		
	 }
	
	
	



	private String replaceExpressionInActualParameter(String equation, int index, String newParameterName) throws Exception {
		InputStream is = new ByteArrayInputStream(equation.getBytes("UTF-8"));
		MR_Expression_Parser parser = new MR_Expression_Parser(is,"UTF-8");
		SingleFunctionCall root = parser.SingleFunctionCall();
		GetFunctionNameVisitor vis = new GetFunctionNameVisitor();
		root.accept(vis);
		String funName  = vis.getFunctionName();
		Function f = multiModel.getFunctionByName(funName);
		if(f==null) {
			throw new Exception();
		}
		
		String ret = new String();
		ret += funName + "(";
		GetFunctionParametersVisitor vis2 = new GetFunctionParametersVisitor();
		root.accept(vis2);
		Vector<String> param = vis2.getActualParameters();
		if(param.size() != f.getNumParam()) {
			throw new Exception(); //WRITE PARSE ERROR AND STOP
		}
		
		for(int i = 0; i < param.size(); i++) {
			if(i == index) {
				ret += newParameterName + ",";
			} else {
				ret += param.get(i) + ",";
			}
		}
		if(param.size() > 0) ret = ret.substring(0,ret.length()-1);
  				
		ret += ")";
		return ret;
	}

	public static void addRemoveEmptyFields_inConsistencyChecks(CustomTableModel_MSMB tableModel, String table_name) {
		try {
			int nrow = tableModel.getRowCount();
			int ncol = tableModel.getColumnCount();
			Vector to_be_added = new Vector();
			Vector to_be_removed = new Vector();
			for(int i = 0; i < nrow; i++) {
				boolean all_empty = true;
				to_be_added= new Vector();
				to_be_removed= new Vector();
				for(int j = 1; j < ncol; j++) {
					String element = tableModel.getValueAt(i, j).toString().trim();
					if(element.length() == 0) {
						if(table_name.compareTo(Constants.TitlesTabs.SPECIES.description)==0 &&
							j==Constants.SpeciesColumns.INITIAL_QUANTITY.index) {
							String type = tableModel.getValueAt(i, Constants.SpeciesColumns.TYPE.index).toString().trim();
							if(type.compareTo(Constants.SpeciesType.MULTISTATE.description)==0){
								to_be_removed.add(table_name);
								to_be_removed.add((i+1));
								to_be_removed.add((j));
							}
							else { 
								to_be_added.add(table_name);
								to_be_added.add((i+1));
								to_be_added.add((j));
							}
						}
						else if(table_name.compareTo(Constants.TitlesTabs.GLOBALQ.description)==0 &&
								j==Constants.GlobalQColumns.VALUE.index) {
								String type = tableModel.getValueAt(i, Constants.GlobalQColumns.TYPE.index).toString().trim();
								if(type.compareTo(Constants.GlobalQType.ASSIGNMENT.description)==0){
									to_be_removed.add(table_name);
									to_be_removed.add((i+1));
									to_be_removed.add((j));
								}
								else { 
									to_be_added.add(table_name);
									to_be_added.add((i+1));
									to_be_added.add((j));
								}
						} 
						
					} else {
						if(!(tableModel.getValueAt(i, j) instanceof Boolean)) {
							if(!CellParsers.isNaN(element)) {
								to_be_removed.add(table_name);
								to_be_removed.add((i+1));
								to_be_removed.add((j));
								all_empty = false;
							} else {
								DebugMessage dm = new DebugMessage();
								dm.setOrigin_table(table_name);
								dm.setProblem("The field contains NaN. If you want to simulate the model in the future this field has to be otherwise defined.");
								dm.setPriority(DebugConstants.PriorityType.MINOR_EMPTY.priorityCode);
								dm.setOrigin_col(j);
								dm.setOrigin_row(i+1);
								MainGui.addDebugMessage_ifNotPresent(dm);
							}
						}
					}
				}
				if(!all_empty) {
					for(int ii = 0; ii < to_be_added.size(); ii=ii+3) {
						ConsistencyChecks.put_EmptyFields((String)to_be_added.get(ii),(Integer)to_be_added.get(ii+1),(Integer)to_be_added.get(ii+2));
					}
					for(int ii = 0; ii < to_be_removed.size(); ii=ii+3) {
						ConsistencyChecks.remove_EmptyFields((String)to_be_removed.get(ii),(Integer)to_be_removed.get(ii+1),(Integer)to_be_removed.get(ii+2));
					}

				}
			}
			updateDebugTab();
		} catch (Exception e) {
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
			return;
		}
	}
	/*public static void addRemoveEmptyFields_inConsistencyChecks(CustomTableModel_MSMB tableModel, String table_name) {
		try {
			int nrow = tableModel.getRowCount();
			int ncol = tableModel.getColumnCount();
			Vector to_be_added = new Vector();
			Vector to_be_removed = new Vector();
			for(int i = 0; i < nrow; i++) {
				boolean all_empty = true;
				to_be_added= new Vector();
				to_be_removed= new Vector();
				for(int j = 1; j < ncol; j++) {
					String element = tableModel.getValueAt(i, j).toString().trim();
					if(element.length() == 0) {
						if(table_name.compareTo(Constants.TitlesTabs.SPECIES.description)==0 &&
							j==Constants.SpeciesColumns.INITIAL_QUANTITY.index) {
							String type = tableModel.getValueAt(i, Constants.SpeciesColumns.TYPE.index).toString().trim();
							if(type.compareTo(Constants.SpeciesType.MULTISTATE.description)==0){
								to_be_removed.add(table_name);
								to_be_removed.add((i+1));
								to_be_removed.add((j));
							}
							else { 
								to_be_added.add(table_name);
								to_be_added.add((i+1));
								to_be_added.add((j));
							}
						}
						else if(table_name.compareTo(Constants.TitlesTabs.GLOBALQ.description)==0 &&
								j==Constants.GlobalQColumns.VALUE.index) {
								String type = tableModel.getValueAt(i, Constants.GlobalQColumns.TYPE.index).toString().trim();
								if(type.compareTo(Constants.GlobalQType.ASSIGNMENT.description)==0){
									to_be_removed.add(table_name);
									to_be_removed.add((i+1));
									to_be_removed.add((j));
								}
								else { 
									to_be_added.add(table_name);
									to_be_added.add((i+1));
									to_be_added.add((j));
								}
						} 
						
					} else {
						if(!(tableModel.getValueAt(i, j) instanceof Boolean)) {
							if(!CellParsers.isNaN(element)) {
								to_be_removed.add(table_name);
								to_be_removed.add((i+1));
								to_be_removed.add((j));
								all_empty = false;
							} else {
								DebugMessage dm = new DebugMessage();
								dm.setOrigin_table(table_name);
								dm.setProblem("The field contains NaN. If you want to simulate the model in the future this field has to be otherwise defined.");
								dm.setPriority(DebugConstants.PriorityType.MINOR_EMPTY.priorityCode);
								dm.setOrigin_col(j);
								dm.setOrigin_row(i+1);
								MainGui.addDebugMessage_ifNotPresent(dm);
							}
						}
					}
				}
				if(!all_empty) {
					for(int ii = 0; ii < to_be_added.size(); ii=ii+3) {
						ConsistencyChecks.put_EmptyFields((String)to_be_added.get(ii),(Integer)to_be_added.get(ii+1),(Integer)to_be_added.get(ii+2));
					}
					for(int ii = 0; ii < to_be_removed.size(); ii=ii+3) {
						ConsistencyChecks.remove_EmptyFields((String)to_be_removed.get(ii),(Integer)to_be_removed.get(ii+1),(Integer)to_be_removed.get(ii+2));
					}

				}
			}
			updateDebugTab();
		} catch (Exception e) {
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
			return;
		}
	}*/

	private void updateEvents(int row) {
		Integer nrow = (Integer)tableEventsmodel.getValueAt(row, 0);
		String name = (String)tableEventsmodel.getValueAt(row, Constants.EventsColumns.NAME.index);
		String trigger = (String)tableEventsmodel.getValueAt(row,Constants.EventsColumns.TRIGGER.index);
		String actions = (String)tableEventsmodel.getValueAt(row, Constants.EventsColumns.ACTIONS.index);
		String notes = (String)tableEventsmodel.getValueAt(row, Constants.EventsColumns.NOTES.index);
		String delay = (String)tableEventsmodel.getValueAt(row, Constants.EventsColumns.DELAY.index);
		Boolean delayAfterCalc = (Boolean)tableEventsmodel.getValueAt(row, Constants.EventsColumns.DELAYCALC.index);
		Boolean expandActionVolume_c = (Boolean)tableEventsmodel.getValueAt(row, Constants.EventsColumns.EXPAND_ACTION_ONVOLUME_TOSPECIES_C.index);
	//	Boolean expandActionVolume_p = (Boolean)tableEventsmodel.getValueAt(row, Constants.EventsColumns.EXPAND_ACTION_ONVOLUME_TOSPECIES_P.index);
			
		
		int expandActionVolume_extension = -1;
		
		if(expandActionVolume_c) {
			expandActionVolume_extension = MR_Expression_ParserConstants.EXTENSION_CONC;
			
		} 
		
		
		boolean trigger_actions_OK = multiModel.updateEvent(nrow, name, trigger, actions, delay, delayAfterCalc, notes,expandActionVolume_extension);
		
		if(!trigger_actions_OK) {
			 
	    	 statusBar.setMessage("System unable to parse trigger or action expressions for the event. See Debug tab for details");
			 
		 } else {
			 statusBar.setMessage("...");
		 }
		
		if(row == tableEventsmodel.getRowCount()-1) { 
			tableEventsmodel.removeAddEmptyRow_Listener();
			tableEventsmodel.addRow(new Vector());
			tableEventsmodel.addAddEmptyRow_Listener();
		}
		
		 jTableEvents.revalidate();
	    // jListDebugShort.revalidate();
	//	 addRemoveEmptyFields_inConsistencyChecks(tableEventsmodel, Constants.TitlesTabs.EVENTS.description);

	}

	public void updateModel_fromMultiBuilder(MultistateSpecies sp, Vector<String> reactions) throws Exception{
		
		int nrow = tableSpeciesmodel.getRowCount()-1;
		if(modify_multistate_species_from_builder==true) {
			nrow = row_to_highlight.get(Constants.TitlesTabs.SPECIES.index);
		}

		multiModel.clearDataOldMultistateSpecies((String)tableSpeciesmodel.getValueAt(nrow, Constants.SpeciesColumns.NAME.index));
		multiModel.modifyMultistateSpecies(sp,true, nrow,true);
		
		//THE ABOVE METHOD ADD THE SET OF SPECIES WITH EMPTY SBML ID... SAVE THEIR ORIGINAL ONE (IF ANY) SOMEWHERE
		
	     tableSpeciesmodel.removeAddEmptyRow_Listener();
	        
	     Vector row = new Vector();      
	     row.add(sp.printCompleteDefinition());
	      row.add(sp.getInitialQuantity_listString());
	     row.add(Constants.SpeciesType.getDescriptionFromCopasiType(sp.getType()));
	     row.add(sp.getCompartment_listString());
	     tableSpeciesmodel.setRow(nrow, row);
         
	     if(modify_multistate_species_from_builder==false) {
            	tableSpeciesmodel.addRow(new Vector());
            }
	        
	     tableReactionmodel.removeAddEmptyRow_Listener();
	        for(int i = 0; i < reactions.size(); i++) {
	        	Vector r = new Vector();
	        	r.add(new String(""));
	        	r.add(reactions.get(i));
	        	tableReactionmodel.addRow(r);
	        	
	        	jTableReactions.setColumnSelectionInterval(2, 2);
	        	updateReaction(tableReactionmodel.getRowCount()-2);
			}
	        
	        tableReactionmodel.addAddEmptyRow_Listener();
	        tableSpeciesmodel.addAddEmptyRow_Listener();
	        modify_multistate_species_from_builder = false;
	        
	        jTableSpecies.revalidate();
	}
	
	
	public void renameFunction_fromCellOrfromFunctionParameterFrame(Function f, int nrow, String oldName, HashMap<String, MutablePair<Integer, Integer>> changedOrders){
	
		Vector<FoundElement> found = search(oldName,null,null,true);

		Iterator it = found.iterator();
		FoundElement fDecl = null;
		while(it.hasNext()) {
			FoundElement f1 = (FoundElement) it.next();
			if(f1.getTableDescription().compareTo(Constants.TitlesTabs.FUNCTIONS.description)!= 0) continue;
			if(f1.getCol() != Constants.FunctionsColumns.NAME.index) continue;
			if(f1.getRow() != nrow-1) continue;
			fDecl = f1;
			found.removeElement(f1);
			break;
		}


		if(fDecl!=null) found.insertElementAt(fDecl,0);

		MainGui.cellValueBeforeChange = null;

		MainGui.donotCleanDebugMessages = true;
		try {
			if(f.getName().compareTo(oldName) != 0) {
				if(changedOrders== null || changedOrders.size() == 0 ) {
					replace(found,oldName, nrow-1, f.getName(),Constants.TitlesTabs.FUNCTIONS.description, Constants.FunctionsColumns.NAME.index);
					if(renamingFrame.getClosingOperation()=="Cancel") { f.setName(oldName); }
				}
			}
		} catch (Exception e) {
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
		}
		MainGui.donotCleanDebugMessages = false;

		try {

			if(changedOrders!= null && changedOrders.size() > 0 && renamingOption != Constants.RENAMING_OPTION_NONE) {
				int original_focus = jTabGeneral.getSelectedIndex();
				Vector<MutablePair<String, String>> from_to = new Vector<MutablePair<String, String>>();
				String newSignature = f.printCompleteSignature();
				if(f.getName().compareTo(oldName) != 0) {
					newSignature = CellParsers.replaceVariableInExpression(newSignature, oldName, f.getName());
				}
				from_to.add(new MutablePair<String, String>(new String(), newSignature));
				for(int i = 1; i < found.size(); i++ ) {
					FoundElement where = (FoundElement) found.get(i);
					CustomTableModel_MSMB tModel = getTableModelFromDescription(where.getTableDescription());
					if(tModel != null) {
						String currentString = tModel.getValueAt(where.getRow(), where.getCol()).toString();

						InputStream is = new ByteArrayInputStream(currentString.getBytes("UTF-8"));
						MR_Expression_Parser_ReducedParserException parser = new MR_Expression_Parser_ReducedParserException(is,"UTF-8");
						CompleteExpression root = parser.CompleteExpression();
						SwapParametersVisitor mySW= new SwapParametersVisitor(changedOrders);
						root.accept(mySW);
						String newString = mySW.getNewExpression();

						if(renamingOption == Constants.RENAMING_OPTION_ALL	) {
							jTabGeneral.setSelectedIndex(Constants.TitlesTabs.getIndexFromDescription(where.getTableDescription()));
							tModel.setValueAt(newString, where.getRow(), where.getCol());
						} else { //custom renaming
							String toSearch = currentString;
							String replace = newString;
							
							if(f.getName().compareTo(oldName) != 0) {
								replace = CellParsers.replaceVariableInExpression(replace, oldName, f.getName());
							}
							from_to.add(new MutablePair<String,String>(toSearch, replace)); 

						}
					} 
				}


				if(from_to.size() > 0 ) {
					renamingFrame.setRenamingStringVectors(from_to, Constants.TitlesTabs.FUNCTIONS.description, nrow-1);
					renamingFrame.fillFrameFields(found);
					if(renamingOption == Constants.RENAMING_OPTION_CUSTOM) {
						renamingFrame.setSelected(new FoundElement(Constants.TitlesTabs.FUNCTIONS.description, nrow-1, Constants.FunctionsColumns.NAME.index));
						int oldRenaming = renamingOption;
						renamingOption = Constants.RENAMING_OPTION_NONE;
						renamingFrame.setVisible(true);
						renamingOption = oldRenaming;
						if(renamingFrame.getClosingOperation()=="Cancel") { 
							f = multiModel.getFunctionByName(oldName);
						}
					} 
				}
				jTabGeneral.setSelectedIndex(original_focus);


			}
		} catch (Throwable e) {
			e.printStackTrace();
		}

		
		if(f!= null) {
			try {
				multiModel.funDB.addChangeFunction(nrow, f);
			} catch (Exception e) {
				e.printStackTrace();
			}
			tableFunctionsmodel.setValueAt(f.printCompleteSignature(), nrow-1, 1);
			
		
		}

		if(fDecl!=null) found.remove(0);
		revalidateExpressions(found);
		
	}
	
	private void updateCompartmentTable(Vector updated_rows) {
		if(updated_rows==null) return;
		
		for(int i = 0; i < updated_rows.size(); i++) {
			Vector row = (Vector) updated_rows.get(i);
			if(tableCompartmentsmodel.getRowCount() <= (Integer)row.get(0)-1) {
				tableCompartmentsmodel.removeAddEmptyRow_Listener();
				for(int ii = tableCompartmentsmodel.getRowCount(); ii <= (Integer)row.get(0)-1; ii++) {
					tableCompartmentsmodel.addRow(new Vector());
				}
				tableCompartmentsmodel.addAddEmptyRow_Listener();
			}
			Vector row_content = new Vector(row.subList(1, row.size()));
			int index_type_column = 1;
			int type = ((Integer)row_content.get(index_type_column));
			
			row_content.set(index_type_column, Constants.CompartmentsType.getDescriptionFromCopasiType(type));
		/*	if(type == CCompartment.ASSIGNMENT) row_content.set(index_type_column, Constants.compartmentsTypes.get(1)); 
			else if(type == CCompartment.FIXED) row_content.set(index_type_column, Constants.compartmentsTypes.get(0));
			else if(type== CCompartment.ODE) row_content.set(index_type_column, Constants.compartmentsTypes.get(2));
			*/
			int modified_row = ((Integer)row.get(0)-1);
			tableCompartmentsmodel.setRow(modified_row, row_content);
			
			String key = Constants.TitlesTabs.COMPARTMENTS.description+"@"+DebugConstants.PriorityType.PARSING.priorityCode+"_"+(modified_row+1)+"_"+Constants.CompartmentsColumns.INITIAL_SIZE.index;
			if(MainGui.debugMessages.containsKey(key)) {
				jTableCompartments.cell_has_errors(modified_row, Constants.CompartmentsColumns.INITIAL_SIZE.index);
			}
			key = Constants.TitlesTabs.COMPARTMENTS.description+"@"+DebugConstants.PriorityType.PARSING.priorityCode+"_"+(modified_row+1)+"_"+Constants.CompartmentsColumns.EXPRESSION.index;
			if(MainGui.debugMessages.containsKey(key)) {
				jTableCompartments.cell_has_errors(modified_row, Constants.CompartmentsColumns.EXPRESSION.index);
			}
			
		}
		if(updated_rows.size()> 0) tableCompartmentsmodel.addRow(new Vector());
		jTableCompartments.revalidate();
		
	}

	private void updateSpeciesTable_defaults(Vector<Vector> updated_species) throws Exception {
		for(int i = 0; i < updated_species.size(); i++) {
			Vector row = updated_species.get(i);
			if(tableSpeciesmodel.getRowCount() <= (Integer)row.get(0)-1) {
				tableSpeciesmodel.removeAddEmptyRow_Listener();
				for(int ii = tableSpeciesmodel.getRowCount(); ii <= (Integer)row.get(0)-1; ii++) {
					tableSpeciesmodel.addRow(new Vector());
				}
				tableSpeciesmodel.addAddEmptyRow_Listener();
				
			}
			Vector row_content = new Vector(row.subList(1, row.size()));
			int index_type_column = Constants.SpeciesColumns.TYPE.index-1; //because the previous line discards the first element
			int type = ((Integer)row_content.get(index_type_column));
			row_content.set(index_type_column, Constants.SpeciesType.getDescriptionFromCopasiType(type));
			
			tableSpeciesmodel.setRow((Integer)row.get(0)-1, row_content);
			 addedByReaction = true;
			updateSpecies((Integer)row.get(0)-1);
			 addedByReaction = false;
			DebugMessage dm = new DebugMessage();
			//dm.setOrigin_cause("Default Element");
			dm.setOrigin_table(Constants.TitlesTabs.SPECIES.description);
			dm.setProblem("Species "+ row_content.get(Constants.SpeciesColumns.NAME.index-1) + " has been added by default.");
			dm.setPriority(DebugConstants.PriorityType.DEFAULTS.priorityCode);
			dm.setOrigin_col(1);
			dm.setOrigin_row((Integer)row.get(0));
			MainGui.addDebugMessage_ifNotPresent(dm);
			
          	
			MainGui.species_default_for_dialog_window.add((String) row_content.get(Constants.SpeciesColumns.NAME.index-1));
		}
		if(updated_species.size()> 0) tableSpeciesmodel.addRow(new Vector());
		
		
		
		jTableSpecies.revalidate(); 
	}
	
	
	private void exportTables(boolean withProgressBar) {
		JFileChooser fileChooser = new JFileChooser();
		int returnVal = fileChooser.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            ExportMultistateFormat.setFile(file);
            /*ExportMultistateFormat.exportTxtTables(tableReactionmodel,tableSpeciesmodel, tableFunctionsmodel,
        											tableGlobalQmodel, tableEventsmodel, tableCompartmentsmodel);*/
            ExportMultistateFormat.exportMultistateFormat(withProgressBar);
        } 
	}
	
	private void exportTablesTxt() {
		JFileChooser fileChooser = new JFileChooser();
		int returnVal = fileChooser.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            ExportMultistateFormat.exportTxtTables(tableReactionmodel,tableSpeciesmodel, tableFunctionsmodel,
        											tableGlobalQmodel, tableEventsmodel, tableCompartmentsmodel);
            
        } 
	}
	
	private int readTable(CustomTableModel_MSMB tablemodel, String file) throws IOException{
		clearTable(tablemodel);
		
		BufferedReader buffin= new BufferedReader(new FileReader(file));
		
		String line = null;
		int row = 0;
		int col = 0; 
		
		while((line = buffin.readLine()) != null) {
			StringTokenizer st = new StringTokenizer(line,"\t");
				while(st.hasMoreTokens()) {
					String elem = st.nextToken();
					if(col == 0) tablemodel.setValueAt_withoutUpdate(new Integer(elem), row, col);
					else	{
						if(elem.compareTo(Boolean.TRUE.toString())==0 || elem.compareTo(Boolean.FALSE.toString())==0) {
							tablemodel.setValueAt_withoutUpdate(new Boolean(elem), row, col);
						}
						else tablemodel.setValueAt_withoutUpdate(elem, row, col);
					}
					col++;
				}
				col = 0;
			row++;
    	}
    	buffin.close();
    	
    	return row;
		
	}
	
	
		
	static boolean validationsOn = true;
	
	public void importTablesMultistateFormat(File file) {
		autocompleteWithDefaults = false;
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		clearAllTables();
		validationsOn = false;
		  try {
            	String nameFile = file.getAbsolutePath();
            	multiModel.createNewModel(nameFile.substring(nameFile.lastIndexOf(System.getProperty("file.separator"))+1));
            	
            	
            	ExportMultistateFormat.setFile(file);
            	HashMap<String, HashMap<String, String>> multistateInitials = ExportMultistateFormat.importMultistateFormat();
        		
            	
              	jTableFunctions.revalidate(); 
            	int imported_rows = tableFunctionsmodel.getRowCount();
             	tableFunctionsmodel.fireTableDataChanged();
            	jTableFunctions.validate();
            	jTabGeneral.setSelectedIndex(Constants.TitlesTabs.FUNCTIONS.index);
            	for(int i = 0; i < imported_rows; i++) {	if(!tableFunctionsmodel.isEmpty(i))  updateFunctions(i); 		}
            
            	 loadBuiltInFunctionsTable();
        		
            	jTableCompartments.revalidate();
        		 imported_rows= tableCompartmentsmodel.getRowCount();
             	tableCompartmentsmodel.fireTableDataChanged();
            	jTableCompartments.validate();
            	jTabGeneral.setSelectedIndex(Constants.TitlesTabs.COMPARTMENTS.index);
            	for(int i = 0; i < imported_rows; i++) {	if(!tableCompartmentsmodel.isEmpty(i))  updateCompartment(i); 		}
            
            	jTableSpecies.revalidate();
        		imported_rows= tableSpeciesmodel.getRowCount();
             	tableSpeciesmodel.fireTableDataChanged();
            	jTableSpecies.validate();
            	jTabGeneral.setSelectedIndex(Constants.TitlesTabs.SPECIES.index);
            	for(int i = 0; i < imported_rows; i++) {	
            		if(!tableSpeciesmodel.isEmpty(i)) updateSpecies(i); 		
            	}
            	multiModel.compressSpecies();
            	updateSpeciesTableFromMultiModel();
            	
        		jTableGlobalQ.revalidate();
        		imported_rows= tableGlobalQmodel.getRowCount();
             	tableGlobalQmodel.fireTableDataChanged();
            	jTableGlobalQ.validate();
            	jTabGeneral.setSelectedIndex(Constants.TitlesTabs.GLOBALQ.index);
                for(int i = 0; i < imported_rows; i++) {	if(!tableGlobalQmodel.isEmpty(i))  updateGlobalQ(i); 		}
            	
            	
            	jTableReactions.revalidate();
        		imported_rows = tableReactionmodel.getRowCount();
             	tableReactionmodel.fireTableDataChanged();
            	jTableReactions.validate();
            	jTabGeneral.setSelectedIndex(Constants.TitlesTabs.REACTIONS.index);
                for(int i = 0; i < imported_rows; i++) {if(!tableReactionmodel.isEmpty(i))	updateReaction(i); 		}
            	
           	
            	jTableEvents.revalidate();
        		imported_rows = tableEventsmodel.getRowCount();
             	tableEventsmodel.fireTableDataChanged();
            	jTableEvents.validate();
            	jTabGeneral.setSelectedIndex(Constants.TitlesTabs.EVENTS.index);
                
            	for(int i = 0; i < imported_rows; i++) {	if(!tableEventsmodel.isEmpty(i)) updateEvents(i); 		}
            	    
            	
            	validationsOn = true;
            	
            	revalidateExpressions();
            	recolorCellsWithErrors();
            	
            	multiModel.setMultistateInitials(multistateInitials);
            	
            	
            	updateModelProperties();
            	multiModel.setTableReactionModel(tableReactionmodel);
			} catch (Exception e) {
				if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
			}
			
			loadedExisting = true;
			//modelName = new String(multiModel.getSBMLid());
			preferenceFrame.updateStatusAutocomplete();
			try {
				validateMultiModel(false,false);
			} catch (Exception e) {
				if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
			}
			finally { setCursor(null); }
			
	}
	
	public void importTablesTxt(File file) {
		//clearAllTables(false);
		autocompleteWithDefaults = false;
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		clearAllTables();
    	autoMergeSpecies = true;
    	importFromTables = true;
		    try {
            	String nameFile = file.getAbsolutePath();
            	nameFile = nameFile.substring(0, nameFile.lastIndexOf(".",nameFile.lastIndexOf(".")-1));
            	
            	multiModel.createNewModel(nameFile.substring(nameFile.lastIndexOf(System.getProperty("file.separator"))+1));
            	
            	
            	int imported_rows= readTable(tableFunctionsmodel, nameFile+".functions.txt");
            	jTableFunctions.revalidate();
            	for(int i = 0; i < imported_rows; i++) {	updateFunctions(i); 		}
            	
            	imported_rows= readTable(tableCompartmentsmodel, nameFile+".compartments.txt");
            	jTableCompartments.revalidate();
            	for(int i = 0; i < imported_rows; i++) {	updateCompartment(i); 		}

            	

            	imported_rows = readTable(tableSpeciesmodel,nameFile+".species.txt");
            	jTableSpecies.revalidate();
            	for(int i = 0; i < imported_rows; i++) {	updateSpecies(i); 		}
            	multiModel.compressSpecies();
        		updateSpeciesTableFromMultiModel();
            	
            	imported_rows = readTable(tableGlobalQmodel, nameFile+".globalQ.txt");
            	jTableGlobalQ.revalidate();
            	for(int i = 0; i < imported_rows; i++) {	updateGlobalQ(i); 		}
            	
            	imported_rows = readTable(tableReactionmodel, nameFile+".reactions.txt");
            	jTableReactions.setModel(tableReactionmodel);
            	for(int i = 0; i < imported_rows; i++) {	updateReaction(i); 		}
            	
            	imported_rows = readTable(tableEventsmodel, nameFile+".events.txt");
            	jTableEvents.revalidate();
            	for(int i = 0; i < imported_rows; i++) {	updateEvents(i); 		}
            	
            	imported_rows= readTable(tableCompartmentsmodel, nameFile+".compartments.txt");
            	jTableCompartments.revalidate();
            	for(int i = 0; i < imported_rows; i++) {	updateCompartment(i); 		}

            
                
			} catch (Exception e) {
				if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) 
					e.printStackTrace();
			}
			
			loadedExisting = true;
			modelName = new String(multiModel.copasiDataModel.getKey());
			preferenceFrame.updateStatusAutocomplete();
			setCursor(null);
			importFromTables  = false;
	}
	
	private void addUndefinedGlobalQ(Vector paramInEquation) throws Exception {
		if(paramInEquation.size()==0) return;
		Vector<String> globalQ_default_for_dialog_window = new Vector();
		for(int i = 0; i < paramInEquation.size(); i++) {
			try{
				Double.parseDouble((String) paramInEquation.get(i));
				continue; // if is a number I don't have to add any undefined param
			} catch(NumberFormatException ex) {
				if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) ex.printStackTrace();
				if(!multiModel.isGlobalQAlreadyDefined((String) paramInEquation.get(i))) {
					globalQ_default_for_dialog_window.add((String)paramInEquation.get(i));
				}
			}
		}
		  if(show_defaults_dialog_window && globalQ_default_for_dialog_window.size() > 0) {
		    	String string_list = new String();
		    	for(int i = 0; i < globalQ_default_for_dialog_window.size(); i++) {
		    		string_list += " " +(i+1)+". " + globalQ_default_for_dialog_window.get(i);
		    		string_list += System.getProperty("line.separator");
		    	}
		    	string_list += "with the default initial value of \"" + globalQ_defaultValue_for_dialog_window + "\"";
		    	final JCheckBox checkBox_dialog_window =  new JCheckBox("Don't show this message again");
		    
		    	Object[] options = {"Accept", "Reject"};
		    	Object[] array = {"The system is about to define the following "
		    			+ globalQ_default_for_dialog_window.size() + " new global quantities: "
		    			+ System.getProperty("line.separator")
		    			+ string_list + System.getProperty("line.separator"),
		    			checkBox_dialog_window};
		    	
		    	final JOptionPane optionPane = new JOptionPane(
		    			array,
		    			JOptionPane.QUESTION_MESSAGE,
		                JOptionPane.YES_NO_OPTION,
		                null,     //do not use a custom Icon
		    		    options,  //the titles of buttons
		    		    options[0]); //default button title

		    	final JDialog dialog = new JDialog(this, 
		                             "Default species definition",
		                             true);
		    	dialog.setContentPane(optionPane);
		    	
		    	optionPane.addPropertyChangeListener(
		    		    new PropertyChangeListener() {
		    		        public void propertyChange(PropertyChangeEvent e) {
		    		            String prop = e.getPropertyName();

		    		            if (dialog.isVisible() 
		    		             && (e.getSource() == optionPane)
		    		             && (prop.equals(JOptionPane.VALUE_PROPERTY))) {
		    		                if(checkBox_dialog_window.isSelected()) {
		    		                	preferenceFrame.setCheckboxDialogWindowForDefaults(false);
		    		                }
		    		                dialog.setVisible(false);
		    		            }
		    		        }
		    		    });
		    		
		    		dialog.pack();
		    		dialog.setLocationRelativeTo(null);
			    	dialog.setVisible(true);
	  	    	    	
		    	 if(optionPane.getValue() != options[0]) {
		    		 globalQ_default_for_dialog_window.clear();
		    	  }
		    	
	        }
		  
		    for(String param : globalQ_default_for_dialog_window) {
			  	Vector row_content = new Vector();
				row_content.add(param);
				row_content.add(globalQ_defaultValue_for_dialog_window);
				row_content.add("Fixed");
				row_content.add("");
				tableGlobalQmodel.addRow(row_content);
				updateGlobalQ(tableGlobalQmodel.getRowCount()-2);
				DebugMessage dm = new DebugMessage();
				dm.setOrigin_table(Constants.TitlesTabs.GLOBALQ.description);
				dm.setProblem("Global quantity "+ param + " has been added by default.");
				dm.setPriority(DebugConstants.PriorityType.DEFAULTS.priorityCode);
				dm.setOrigin_col(1);
				dm.setOrigin_row(tableGlobalQmodel.getRowCount()-1);
				MainGui.addDebugMessage_ifNotPresent(dm);
				
				
	          
		    }
			MainGui.donotCleanDebugMessages = true;
	  		validateMultiModel(false,false);
		jTableGlobalQ.revalidate();
		MainGui.donotCleanDebugMessages = false;
	}

	
	
	
	
	public static String generateKey(DebugMessage dm) {
		return dm.getOrigin_table()+"@"+dm.getPriority()+"_"+dm.getOrigin_row()+"_"+dm.getOrigin_col();
	}
	
	public static void clear_debugMessages_relatedWith(String table, double priority, int row, int col) throws Exception {
		String key = table+"@"+priority+"_"+row+"_"+col;
		if(debugMessages.get(key)==null) {
			decolorCell(row,col,table);
		}
		recolorCell(debugMessages.get(key),false);
		debugMessages.remove(key);
		updateDebugTab();
		
	}
	
	
	private static void decolorRow(int row, String table) throws Exception {
		if(table.compareTo(Constants.TitlesTabs.SPECIES.description)==0) {
			for(int col = 1; col < Constants.species_columns.size(); col++) {
				jTableSpecies.cell_no_defaults(row,col);
				jTableSpecies.cell_no_errors(row,col);
				jTableSpecies.revalidate();
				}
			}
		else if(table.compareTo(Constants.TitlesTabs.REACTIONS.description)==0) {
			for(int col = 1; col < Constants.reactions_columns.size(); col++) {
				jTableReactions.cell_no_defaults(row,col);
				jTableReactions.cell_no_errors(row,col);
				jTableReactions.revalidate();
	    		}
			}	
		else if(table.compareTo(Constants.TitlesTabs.GLOBALQ.description)==0) {
			for(int col = 1; col < Constants.globalQ_columns.size(); col++) {
				jTableGlobalQ.cell_no_defaults(row,col);
				jTableGlobalQ.cell_no_errors(row,col);
				jTableGlobalQ.revalidate();
				}
			}	
		
		else if(table.compareTo(Constants.TitlesTabs.COMPARTMENTS.description)==0) {
			for(int col = 1; col < Constants.compartments_columns.size(); col++) {
				jTableCompartments.cell_no_defaults(row,col);
				jTableCompartments.cell_no_errors(row,col);
				jTableCompartments.revalidate();
				}
			}	
		
		else if(table.compareTo(Constants.TitlesTabs.EVENTS.description)==0) {
			for(int col = 1; col < Constants.events_columns.size(); col++) {
				jTableEvents.cell_no_defaults(row,col);
				jTableEvents.cell_no_errors(row,col);
				jTableEvents.revalidate(); 
				}
			}	
		
		else if(table.compareTo(Constants.TitlesTabs.FUNCTIONS.description)==0) {
			for(int col = 1; col < Constants.functions_columns.size(); col++) {
				jTableFunctions.cell_no_defaults(row,col);
				jTableFunctions.cell_no_errors(row,col);
				jTableFunctions.revalidate();  
				}
			}	
	}
	
	
	private static void decolorCell(int row, int col, String table) throws Exception {
		if(table.compareTo(Constants.TitlesTabs.SPECIES.description)==0) {
			jTableSpecies.cell_no_defaults(row,col);
			jTableSpecies.cell_no_errors(row,col);
			jTableSpecies.revalidate(); 
		}
		else if(table.compareTo(Constants.TitlesTabs.REACTIONS.description)==0) {
			jTableReactions.cell_no_defaults(row,col);
			jTableReactions.cell_no_errors(row,col);
			jTableReactions.revalidate(); 
			}	
		else if(table.compareTo(Constants.TitlesTabs.GLOBALQ.description)==0) {
			jTableGlobalQ.cell_no_defaults(row,col);
			jTableGlobalQ.cell_no_errors(row,col);
			jTableGlobalQ.revalidate();  
			}	
		
		else if(table.compareTo(Constants.TitlesTabs.COMPARTMENTS.description)==0) {
			jTableCompartments.cell_no_defaults(row,col);
			jTableCompartments.cell_no_errors(row,col);
			jTableCompartments.revalidate();  
			}	
		
		else if(table.compareTo(Constants.TitlesTabs.EVENTS.description)==0) {
			jTableEvents.cell_no_defaults(row,col);
			jTableEvents.cell_no_errors(row,col);
			jTableEvents.revalidate();  
			}	
		
		else if(table.compareTo(Constants.TitlesTabs.FUNCTIONS.description)==0) {
			jTableFunctions.cell_no_defaults(row, col);
			jTableFunctions.cell_no_errors(row, col);
			jTableFunctions.revalidate();
			
			}	
	}
	
	private static void recolorCell(DebugMessage dm, boolean asError) {
		if(dm==null) return;
		if(importFromSBMLorCPS) return; 
		String tableDescr = dm.getOrigin_table();
		CustomJTable_MSMB table = getTableFromDescription(tableDescr);
		if(dm.getPriority()==DebugConstants.PriorityType.PARSING.priorityCode 
				|| dm.getPriority()==DebugConstants.PriorityType.INCONSISTENCIES.priorityCode
				|| dm.getPriority()==DebugConstants.PriorityType.MISSING.priorityCode
				|| dm.getPriority()== DebugConstants.PriorityType.EMPTY.priorityCode) {
	
				if(asError) table.cell_has_errors(dm.getOrigin_row()-1,dm.getOrigin_col());
				else  table.cell_no_errors(dm.getOrigin_row()-1,dm.getOrigin_col());
				table.revalidate();
		}
		else if(dm.getPriority()==DebugConstants.PriorityType.DEFAULTS.priorityCode) {
			if(asError) table.cell_has_defaults(dm.getOrigin_row()-1,dm.getOrigin_col());
			else  table.cell_no_defaults(dm.getOrigin_row()-1,dm.getOrigin_col());
			table.revalidate();
		}else if(dm.getPriority()==DebugConstants.PriorityType.MINOR_EMPTY.priorityCode
				||dm.getPriority()==DebugConstants.PriorityType.MINOR_IMPORT_ISSUES.priorityCode
				) {
			if(asError) table.cell_has_minorIssue(dm.getOrigin_row()-1,dm.getOrigin_col());
			else  table.cell_no_minorIssue(dm.getOrigin_row()-1,dm.getOrigin_col());
			table.revalidate();
		}
	}
	
	
	/*private static void recolorCell_2(DebugMessage dm, boolean asError) throws Exception {
		if(dm==null) return;
		if(importFromSBMLorCPS) return;
		if(dm.getOrigin_table().compareTo(Constants.TitlesTabs.SPECIES.description)==0) {
    		if(dm.getPriority()==DebugConstants.PriorityType.PARSING.priorityCode 
    				|| dm.getPriority()==DebugConstants.PriorityType.INCONSISTENCIES.priorityCode
    				|| dm.getPriority()==DebugConstants.PriorityType.MISSING.priorityCode
    				|| dm.getPriority()== DebugConstants.PriorityType.EMPTY.priorityCode) {
    			if(asError) jTableSpecies.cell_has_errors(dm.getOrigin_row()-1,dm.getOrigin_col());
    			else  jTableSpecies.cell_no_errors(dm.getOrigin_row()-1,dm.getOrigin_col());
    			jTableSpecies.revalidate();
        	 
			}
    		
    		if(dm.getPriority()==DebugConstants.PriorityType.DEFAULTS.priorityCode) {
    			if(asError) jTableSpecies.cell_has_defaults(dm.getOrigin_row()-1,dm.getOrigin_col());
    			else  jTableSpecies.cell_no_defaults(dm.getOrigin_row()-1,dm.getOrigin_col());
    			jTableSpecies.revalidate();
    		}
    		
    
    		
    	} else if(dm.getOrigin_table().compareTo(Constants.TitlesTabs.GLOBALQ.description)==0) {
    		if(dm.getPriority()==DebugConstants.PriorityType.PARSING.priorityCode 
    				|| dm.getPriority()==DebugConstants.PriorityType.MISSING.priorityCode
    				|| dm.getPriority()== DebugConstants.PriorityType.EMPTY.priorityCode) {
    			if(asError) jTableGlobalQ.cell_has_errors(dm.getOrigin_row()-1,dm.getOrigin_col());
    			else  jTableGlobalQ.cell_no_errors(dm.getOrigin_row()-1,dm.getOrigin_col());
    			jTableGlobalQ.revalidate();
    		}
    		if(dm.getPriority()==DebugConstants.PriorityType.DEFAULTS.priorityCode) {
    			if(asError) jTableGlobalQ.cell_has_defaults(dm.getOrigin_row()-1,dm.getOrigin_col());
    			else  jTableGlobalQ.cell_no_defaults(dm.getOrigin_row()-1,dm.getOrigin_col());
    			jTableGlobalQ.revalidate();
    		}
	    } else if(dm.getOrigin_table().compareTo(Constants.TitlesTabs.COMPARTMENTS.description)==0) {
	    	if(dm.getPriority()==DebugConstants.PriorityType.PARSING.priorityCode
	    			|| dm.getPriority()==DebugConstants.PriorityType.MISSING.priorityCode
	    			|| dm.getPriority()== DebugConstants.PriorityType.EMPTY.priorityCode) {
	    		if(asError) jTableCompartments.cell_has_errors(dm.getOrigin_row()-1,dm.getOrigin_col());
    			else  jTableCompartments.cell_no_errors(dm.getOrigin_row()-1,dm.getOrigin_col());
	    		jTableCompartments.revalidate();
    		}
	    	if(dm.getPriority()==DebugConstants.PriorityType.DEFAULTS.priorityCode) {
	    		if(asError) jTableCompartments.cell_has_defaults(dm.getOrigin_row()-1,dm.getOrigin_col());
    			else  jTableCompartments.cell_no_defaults(dm.getOrigin_row()-1,dm.getOrigin_col());
	    		jTableCompartments.revalidate();
  		
    		}
	    } else if(dm.getOrigin_table().compareTo(Constants.TitlesTabs.REACTIONS.description)==0) {
	     	if(dm.getPriority()==DebugConstants.PriorityType.PARSING.priorityCode
	     			|| dm.getPriority()==DebugConstants.PriorityType.MISSING.priorityCode
	     			|| dm.getPriority()==DebugConstants.PriorityType.INCONSISTENCIES.priorityCode) {
	     		if(asError) jTableReactions.cell_has_errors(dm.getOrigin_row()-1,dm.getOrigin_col());
    			else  jTableReactions.cell_no_errors(dm.getOrigin_row()-1,dm.getOrigin_col());
	     		jTableReactions.revalidate();
    		}
	     	if(dm.getPriority()==DebugConstants.PriorityType.DEFAULTS.priorityCode) {
	     		if(asError) jTableReactions.cell_has_defaults(dm.getOrigin_row()-1,dm.getOrigin_col());
    			else  jTableReactions.cell_no_defaults(dm.getOrigin_row()-1,dm.getOrigin_col());
	     		jTableReactions.revalidate();
			
    		}
	    }
	   
		
	}
*/
	public static boolean containsKey_debugMessages_relatedWith(String table, double priority, int row, int col) {
		String key = table+"@"+priority+"_"+row+"_"+col;
		return debugMessages.containsKey(key);
	}
	
	
	public static void clear_debugMessages_defaults_relatedWith(String table, int nrow) throws Exception {
		if(MainGui.donotCleanDebugMessages) return;
		
		HashMap<String, DebugMessage> remaining_messages = new HashMap<String, DebugMessage>();
		Iterator it = debugMessages.keySet().iterator();
		while(it.hasNext()){
			String key = (String) it.next();
			DebugMessage dm = (DebugMessage)(debugMessages.get(key));
	    	int origin_row = dm.getOrigin_row();
	    	String origin_table= dm.getOrigin_table();
	    	if(origin_row != nrow || origin_table.compareTo(table)!=0 || 
	    			dm.getPriority() !=DebugConstants.PriorityType.DEFAULTS.priorityCode) {
	    			remaining_messages.put(key,dm);
	    	} else {
	    		
	    		recolorCell(debugMessages.get(key),false);
	       	}
		}
		
		debugMessages.clear();
		debugMessages.putAll(remaining_messages);
		//printDebugMessages();
		updateDebugTab();
		
	}
	
	
	public static void clear_debugMessages_relatedWith_row(int nrow) throws Exception {
		String table =  jTabGeneral.getTitleAt(jTabGeneral.getSelectedIndex());
		HashMap<String, DebugMessage> remaining_messages = new HashMap<String, DebugMessage>();
		Iterator it = debugMessages.keySet().iterator();
		while(it.hasNext()){
			String key = (String) it.next();
			DebugMessage dm = (DebugMessage)(debugMessages.get(key));
	    	int origin_row = dm.getOrigin_row();
	    	String origin_table= dm.getOrigin_table();
	    	if(origin_row != nrow || origin_table.compareTo(table)!=0 || dm.getPriority()==DebugConstants.PriorityType.MINOR_IMPORT_ISSUES.priorityCode) {
	    			remaining_messages.put(key,dm);
	    	} else {
	    		recolorCell(debugMessages.get(key),false);
	       	}
		}
		
		debugMessages.clear();
		debugMessages.putAll(remaining_messages);
		//printDebugMessages();
		updateDebugTab();
	}
	
	public static void moveUpRow_references_debugMessages(int nrow) {
		String table =  jTabGeneral.getTitleAt(jTabGeneral.getSelectedIndex());
		HashMap<String, DebugMessage> remaining_messages = new HashMap<String, DebugMessage>();
		Iterator it = debugMessages.keySet().iterator();
		Vector<DebugMessage> toclear = new Vector<DebugMessage>();
		while(it.hasNext()){
			String key = (String) it.next();
			DebugMessage dm = (DebugMessage)(debugMessages.get(key));
	    	int origin_row = dm.getOrigin_row();
	    	String origin_table= dm.getOrigin_table();
	    	if(origin_row < nrow || origin_table.compareTo(table)!=0) {
	    			remaining_messages.put(key,dm);
	    	} else {
	    		DebugMessage dmNew = new DebugMessage();
	    		dmNew.setOrigin_col(dm.getOrigin_col());
	    		dmNew.setOrigin_row(origin_row-1);
	    		dmNew.setOrigin_table(dm.getOrigin_table());
	    		dmNew.setPriority(dm.getPriority());
	    		dmNew.setProblem(dm.getProblem());
	    		dmNew.setStatus(dm.getStatus());
	    		toclear.add(dm);
	    		remaining_messages.put(generateKey(dmNew),dmNew);
	    	}
		}
		
		for(int i = 0; i < toclear.size(); i++){
			DebugMessage element = toclear.get(i);
			recolorCell(element, false);
		}
		
		debugMessages.clear();
		debugMessages.putAll(remaining_messages);
		//printDebugMessages();
		updateDebugTab();
		
	}
	
	public static void clear_debugMessages_relatedWith_table_col_priority(String table, int col, double priority) throws Exception {
		HashMap<String, DebugMessage> remaining_messages = new HashMap<String, DebugMessage>();
		Iterator it = debugMessages.keySet().iterator();
		while(it.hasNext()){
			String key = (String) it.next();
			DebugMessage dm = (DebugMessage)(debugMessages.get(key));
	    	int origin_col = dm.getOrigin_col();
	    	String origin_table= dm.getOrigin_table();
	    	if(origin_col != col || origin_table.compareTo(table)!=0 || dm.getPriority() != priority) {
	    			remaining_messages.put(key,dm);
	    	} else {
	    		
	    		recolorCell(debugMessages.get(key),false);
	       	}
		}
		
		debugMessages.clear();
		debugMessages.putAll(remaining_messages);
		//printDebugMessages();
		updateDebugTab();
	}
	
	public static void printDebugMessages() {
		Iterator iterator = debugMessages.keySet().iterator();  
	    System.out.println("_________________");
	     System.out.println("DEBUG VECTOR:");
	    while (iterator.hasNext()) {  
	    	DebugMessage dm = (DebugMessage)debugMessages.get(iterator.next());
	 		System.out.println(dm.getShortDescription());
	    } 
	     System.out.println("^^^^^^^^^^^^^^^^^");
	}
	
	
	public void revalidateReactionTable(){
		try {
			for(int i = 0; i < tableReactionmodel.getRowCount(); i++){

				updateReaction(i);


			}
			revalidateExpressions();
		} catch (Exception e) {
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) 	e.printStackTrace();
		}
	}
	
	
	public void revalidateExpressions() throws Exception {
		if(!validationsOn) return;
		boolean old = autocompleteWithDefaults;
		MainGui.autocompleteWithDefaults = false;
		
		Iterator iterator = debugMessages.keySet().iterator(); 
	    Vector to_be_deleted = new Vector();
	    while (iterator.hasNext()) {  
	    	DebugMessage dm = (DebugMessage)debugMessages.get(iterator.next());
	    	if(dm.getPriority()!=DebugConstants.PriorityType.PARSING.priorityCode 
	    			&& dm.getPriority()!=DebugConstants.PriorityType.MISSING.priorityCode
	    			&& dm.getPriority()!=DebugConstants.PriorityType.MINOR_EMPTY.priorityCode) {
	    		continue;
	    	}
	    	if(dm.getOrigin_table().compareTo(Constants.TitlesTabs.SPECIES.description)==0) {
	    		if(dm.getOrigin_col()==Constants.SpeciesColumns.EXPRESSION.index ||
	    				dm.getOrigin_col()==Constants.SpeciesColumns.INITIAL_QUANTITY.index	 ||
	    				dm.getOrigin_col()==Constants.SpeciesColumns.COMPARTMENT.index	) {
	    			to_be_deleted.add(dm);			
	    		}
	    	} else if(dm.getOrigin_table().compareTo(Constants.TitlesTabs.GLOBALQ.description)==0) {
	    		if(dm.getOrigin_col()==Constants.GlobalQColumns.EXPRESSION.index ||
	    				dm.getOrigin_col()==Constants.GlobalQColumns.VALUE.index	) {
	    			to_be_deleted.add(dm);			
	    		}
	    	} else if(dm.getOrigin_table().compareTo(Constants.TitlesTabs.COMPARTMENTS.description)==0) {
	    		if(dm.getOrigin_col()==Constants.CompartmentsColumns.EXPRESSION.index ||
	    				dm.getOrigin_col()==Constants.CompartmentsColumns.INITIAL_SIZE.index	) {
	    			to_be_deleted.add(dm);			
	    		}
	    	} else if(dm.getOrigin_table().compareTo(Constants.TitlesTabs.REACTIONS.description)==0) {
	    		if(dm.getOrigin_col()==Constants.ReactionsColumns.KINETIC_LAW.index || dm.getOrigin_col()==Constants.ReactionsColumns.REACTION.index  ) {
	    			to_be_deleted.add(dm);			
	    		}
	    	}
	    }
	    addedByReaction = true;
		actionInColumnName = false;
		MainGui.donotCleanDebugMessages = true; // so it won't delete the defaults messages added before
	    for(int j = 0; j < to_be_deleted.size(); j++) {
	    	DebugMessage dm = (DebugMessage) to_be_deleted.get(j);
	    	if(dm.getPriority()!=DebugConstants.PriorityType.PARSING.priorityCode && dm.getPriority()!=DebugConstants.PriorityType.MISSING.priorityCode) continue;
	    	clear_debugMessages_relatedWith(dm.getOrigin_table(), dm.getPriority(), dm.getOrigin_row(), dm.getOrigin_col());
	    	try {
				if(dm.getOrigin_table().compareTo(Constants.TitlesTabs.SPECIES.description)==0) updateSpecies(dm.getOrigin_row()-1);
				else if(dm.getOrigin_table().compareTo(Constants.TitlesTabs.GLOBALQ.description)==0) updateGlobalQ(dm.getOrigin_row()-1);
				else if(dm.getOrigin_table().compareTo(Constants.TitlesTabs.COMPARTMENTS.description)==0) updateCompartment(dm.getOrigin_row()-1);
				else if(dm.getOrigin_table().compareTo(Constants.TitlesTabs.REACTIONS.description)==0) updateReaction(dm.getOrigin_row()-1);
					} catch (Exception e) {
						if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
			}
	    }
	
	    addedByReaction = false;
	    recolorCellsWithErrors();
	    MainGui.donotCleanDebugMessages = false;  // set back as original value
	    MainGui.autocompleteWithDefaults = old;
	}
	
	private static void recolorCellsWithErrors() {
		if(importFromSBMLorCPS) return;
		try{
		Iterator iterator = debugMessages.keySet().iterator(); 
	    while (iterator.hasNext()) {  
	    	DebugMessage dm = (DebugMessage)debugMessages.get(iterator.next());
	    	recolorCell(dm, true);
		   }
		} catch(Exception ex) {
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) ex.printStackTrace();
		}
	}
	
	
	
	public static void updateDebugTab() {
		/*DefaultListModel model = (DefaultListModel)jListDebugShort.getModel();
		/model.clear();
		*/
	  
		jPanelTreeDebugMessages.updateTreeView();
		
	    if(debugMessages.size() == 0) {
	    	statusBar.setMessage("...");
	    }
	    
	    if(debugMessages.size() != 0 && statusBar.getMessage().compareTo("...")==0) {
	    	statusBar.setMessage("Open issues in the model definition. See Debug tab for details.");
	    }

	    recolorCellsWithErrors();
	}
	
	
	
	static boolean dialogAcceptRejectShowed = false;
	public  void updateModelFromTable(int row, int col) throws Exception {
		int whichTab = -1;
		try {
			whichTab = jTabGeneral.getSelectedIndex();
			dialogAcceptRejectShowed = false;
			switch(whichTab) {
				case 0: if(col == Constants.ReactionsColumns.REACTION.index) actionInColumnName = true; //I use it to check if the autocompletion of the other table should be done or not
						else actionInColumnName = false; 
						if(col == Constants.ReactionsColumns.EXPANDED.index) return;
						updateReaction(row); 	break;
				case 1: if(col == Constants.SpeciesColumns.NAME.index) {
							actionInColumnName = true;
							
						}
						else actionInColumnName = false;
						updateSpecies(row); break;
				case 2: if(col == Constants.GlobalQColumns.NAME.index) actionInColumnName = true;
						else actionInColumnName = false;
						updateGlobalQ(row); break;
				case 3: if(col == Constants.FunctionsColumns.NAME.index) actionInColumnName = true;
						else actionInColumnName = false;
						//if(col == Constants.FunctionsColumns.SIGNATURE.index) return;
						updateFunctions(row); break;
				case 4: if(col == Constants.EventsColumns.NAME.index) actionInColumnName = true;
						else actionInColumnName = false;
						updateEvents(row); break;
				case 5: if(col == Constants.CompartmentsColumns.NAME.index) actionInColumnName = true;
						else actionInColumnName = false;
						updateCompartment(row); break;
				default: System.out.println("Error?"+row); break;
			}
			
			
			updateDebugTab(); 
			 
		
			
			jTableReactions.revalidate();
			
			if(DEBUG_SAVE_AT_EACH_STEP) {
				String first_part = multiModel.copasiDataModel.getKey();
				if(first_part.length()==0) first_part = modelName;
				saveCPS(new File(first_part+"_"+DEBUG_STEP+".cps"),false);
				DEBUG_STEP++;
			}
			
			 
			
			revalidateExpressions();
			recolorCellsWithErrors();
			updateDebugTab(); 
			if(!MainGui.importFromSBMLorCPS) {
				int ind = Constants.TitlesTabs.getIndexFromDescription(cellTableEdited);
				if(ind!= -1)jTabGeneral.setSelectedIndex(ind);
			}
			dialogAcceptRejectShowed = false;
			
		} catch(Exception ex) {
			//if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) 
				ex.printStackTrace();
			System.out.println("Something wrong in the updateModelFromTable: ");
			System.out.println("--> "+ex.getMessage());
			throw ex;
		}
	
	}
	
	private void exit() {
		//save recents
		BufferedWriter out; 
		
			try {
				out = new BufferedWriter(new FileWriter(file_RecentFiles));
				
				for(int i = 0; i < recentFiles.size(); i++) {
						out.write(recentFiles.get(i).toString());
						out.write(System.getProperty("line.separator"));
				}
				out.flush();
				out.close();
				
			}
			catch (Exception e) {
				System.err.println("Trouble writing recentFiles directories: "+ e);
				if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
			}
			
			preferenceFrame.savePreferencesToFile();
		
		if(modelHasBeenModified) {
			//Custom button text
			Object[] options = {"Yes",
			                    "No",
			                    "Cancel"};
			int n = JOptionPane.showOptionDialog(frame,
			    "Model \""+inputFile+"\" has been modified. Do you want to save the changes?",
			    "Question",
			    JOptionPane.YES_NO_CANCEL_OPTION,
			    JOptionPane.QUESTION_MESSAGE,
			    null,
			    options,
			    options[0]);
			if(n==0) {
				File file = MainGui.fileCurrentlyLoaded;
				if(file == null) {
					int returnVal = cpsFileChooser.showSaveDialog(null);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						file = cpsFileChooser.getSelectedFile();
						addRecents(file);
					} 
				}
				
				try {
					saveCPS(file,true);
					deleteTempAutosave();
					recordAutosave.stopAutosave();
					this.dispose();
					System.gc();
					System.exit(0);
				} catch (Exception e) {
					if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
				}
			} else if(n==1) {
				try{
					deleteTempAutosave();

					recordAutosave.stopAutosave();
					this.dispose();
					System.gc();
					System.exit(0);
				} catch (Exception e) {
					if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) 	e.printStackTrace();
				}
			} else if(n == 2) {
				return;
			}
			
		} else {
			try{
				System.setErr(null);
				deleteTempAutosave();
				recordAutosave.stopAutosave();
				this.dispose();
				System.gc();
				System.exit(0);
				
			} catch (Exception e) {
				if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) 	e.printStackTrace();
			}
			
		
		}
		
		
	}

	private void deleteTempAutosave() {
		recordAutosave.stopAutosave();
		System.gc();
		
		try{
		String fileName = recordAutosave.getOutputFileCompleteName();
	    
	    File f = new File(fileName);

	    // Make sure the file or directory exists and isn't write protected
	    if (!f.exists()) throw new IllegalArgumentException("Delete: no such file or directory: " + fileName);
	    if (!f.canWrite())  throw new IllegalArgumentException("Delete: write protected: " + fileName);

	    // If it is a directory, make sure it is empty
	    if (f.isDirectory()) {
	    	String[] files = f.list();
	      if (files.length > 0)     throw new IllegalArgumentException(    "Delete: directory not empty: " + fileName);
	    }

	    // Attempt to delete it
	    boolean success = f.delete();

	    if (!success)
	      throw new IllegalArgumentException("Delete: deletion failed");
		} catch (Exception e) {
			//nothing to do, not successful in deleting the temp but I can go on
			//e.printStackTrace();
		}
	}

	private void addRecents(File f) {
		
		int index = recentFiles.indexOf(f.getAbsolutePath());
		if(index!= -1) {
			recentFiles.remove(index);
			recentMenu.remove(recentMenu.getItemCount()-index-1);
		}
		
		if(recentFiles.size() > 11) {
				recentFiles.remove(0);
				recentMenu.remove(0);
		}
		
		recentFiles.add(f.getAbsolutePath());
		JMenuItem item = new JMenuItem(f.getName());
		item.addActionListener(new RecentItemActionListener(f));
     
		recentMenu.add(item,0);
		recentMenu.validate();
		
	}
	
	public static void cleanUpModel() {
		multiModel.cleanUpModel();
		tableSpeciesmodel.clearData();
		tableCompartmentsmodel.clearData();
		tableEventsmodel.clearData();
		tableFunctionsmodel.clearData();
		tableReactionmodel.clearData();
		
		System.gc();
	}
	
/*	public void modifySBMLid(String copasiDataModelID) {
		multiModel.modifySBMLid(copasiDataModelID);
	}*/
	
	
	public void loadSBML(File f) throws Exception{
	  
		try{
			importFromSBMLorCPS = true;
		MainGui.autocompleteWithDefaults = false;
		 setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	   clearAllTables();
	   multiModel.loadSBML(f);
	   updateGUIFromMultimodel();
	   multiModel.setTableReactionModel(tableReactionmodel);
	   
		validateMultiModel(false,false);
		
	   jTabGeneral.setSelectedIndex(Constants.TitlesTabs.REACTIONS.index);
	
		} catch (Exception e) {
			//if(MainGui.fromMainGuiTest) throw e;
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) 
				e.printStackTrace();
		}
		finally { 
			importFromSBMLorCPS = false;
			revalidateReactionTable();

				setCursor(null); 
			loadPreferencesFile();
			modelHasBeenModified = false;
			if(!MainGui.fromMainGuiTest){
				Vector<DebugMessage> minor_import = getDebugMessages(DebugConstants.PriorityType.MINOR_IMPORT_ISSUES.priorityCode);
					if(minor_import.size() > 0) {
						JOptionPane.showMessageDialog(new JButton(),"Issues has been found in the original SBML and \nchanges were applied to make it compliant with "+Constants.TOOL_NAME+". \nSee "+Constants.TitlesTabs.DEBUG.description+" tab for details about the changes.", "Issues at import", JOptionPane.WARNING_MESSAGE);
					}
			}
				
		}
	}
	
	public void loadCopasiDataModel_fromKey(String copasiDataModel_key) throws Exception {
		MainGui.autocompleteWithDefaults = false;
		importFromSBMLorCPS = true;
		try{
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			clearAllTables();
			multiModel.loadCopasiDataModel_fromKey(copasiDataModel_key);
			updateGUIFromMultimodel();
			multiModel.setTableReactionModel(tableReactionmodel);
			validateMultiModel(false,false);
			jTabGeneral.setSelectedIndex(Constants.TitlesTabs.REACTIONS.index);
		
		} catch (Exception e) {
			if(MainGui.fromMainGuiTest) throw e;
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
			
		}
		finally { 
			importFromSBMLorCPS = false;
			revalidateReactionTable();
			setCursor(null); 
			loadPreferencesFile();
			modelHasBeenModified = false;
			
		}
	}
	
	public void loadCPS(File f) throws Exception {
		//clearAllTables(false);
		MainGui.autocompleteWithDefaults = false;
		try{
		MainGui.fileCurrentlyLoaded = f;
		 setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		clearAllTables();
		multiModel.loadCPS(f);
		updateGUIFromMultimodel();
		validateMultiModel(false,false);
		} catch (Exception e) {
			if(MainGui.fromMainGuiTest) throw e;
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
			
		}
		finally { 
			setCursor(null); 
			loadPreferencesFile();
			setCursor(null);
			modelHasBeenModified = false;
		}
		
 	}
	
	private void updateGUIFromMultimodel() throws Exception {
		int oldRenamingOption = renamingOption;
		renamingOption = Constants.RENAMING_OPTION_NONE;
		
		if(!MainGui.fromMainGuiTest ) exportConcentration = true;
		
		Vector<String> reactionWithProblems = multiModel.convert2nonReversible();
		
		if(reactionWithProblems== null) {
			JOptionPane.showMessageDialog(new JButton(),"Model with reversible reactions that cannot be converted to irreversible.\nPlease submit the model to the developer to have more information.", "Problem with conversion", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		System.out.println("-- Loading functions...");
		System.out.flush();
		loadFunctionsTable();
		System.out.println("-- done...");
		System.out.flush();
	
		
		System.out.println("-- Loading compartments...");
		System.out.flush();
		loadCompartmentsTable();
		System.out.println("-- done...");
		System.out.flush();
		
		try{ 
			System.out.println("-- Loading species...");
			System.out.flush();
			loadSpeciesTable(); 
			System.out.println("-- done...");
			System.out.flush();
		} 
		catch(Exception ex) {
			//ex.printStackTrace();
			//some species can refer to globalQ not yet loaded... it's ok to go on with the exceptions
			//it will be revalidated later
		}
		
		System.out.println("-- Loading global quantities...");
		System.out.flush();
		loadGlobalQTable();
		System.out.println("-- done...");
		System.out.flush();
		
		System.out.println("-- Loading reactions...");
		System.out.flush();
		Vector<CFunction> predefined_to_be_loaded = loadReactionTable(reactionWithProblems);
		System.out.println("-- done...");
		System.out.flush();
		
		
		
		
		/*if(!MainGui.showAllAvailableFunctions) {
			int row = tableFunctionsmodel.getRowCount()-1;
				
			for(int i = 0; i < predefined_to_be_loaded.size(); i++ ) {
				CFunction curr = predefined_to_be_loaded.get(i);
				String pureNameFunc = CellParsers.cleanName(curr.getObjectName());
				
				multiModel.funDB.getFunctionByName(pureNameFunc).setShow(true);
				Function f = multiModel.funDB.getFunctionByName(pureNameFunc);
			
				if((MainGui.showAllAvailableFunctions || f.toShow()) && !listModel_FunctionToCompact.contains(f.getName())) listModel_FunctionToCompact.addElement(f.getName());
				jListFunctionToCompact.revalidate();
				scrollPaneListFunctionToCompact.revalidate();
				tableFunctionsmodel.addRow(new Vector());
				tableFunctionsmodel.setValueAt_withoutUpdate(f.printCompleteSignature(), row, 1);
				tableFunctionsmodel.setValueAt_withoutUpdate(f.getExpandedEquation(new Vector()), row, 2);
				row++;
			}
		}
		
		if(!MainGui.importFromSBMLorCPS) updateFunctionsView();
		*/
		System.out.println("-- Loading events...");
		System.out.flush();
		
		loadEventsTable();
		System.out.println("-- done...");
		System.out.flush();
		
		//expandAllExpressions();
		//to revalidate lines containing temporary parsing errors in expressions
		//-----
		revalidateExpressions();
		//-----

		updateDebugTab();
		
		
	    
	    comboBox_unitVolume.setSelectedItem(Constants.UnitTypeVolume.getDescriptionFromCopasiType(MainGui.volumeUnit));
	    comboBox_unitQuantity.setSelectedItem(Constants.UnitTypeQuantity.getDescriptionFromCopasiType(MainGui.quantityUnit));
	    comboBox_unitTime.setSelectedItem(Constants.UnitTypeTime.getDescriptionFromCopasiType(MainGui.timeUnit));
	    textFieldModelName.setText(MainGui.modelName);
	    
		//because I don't want to keep it updated when the user is using MultiReMI
		//I just need it to fill in the proper way the tables
		
		multiModel.clearCopasiDataModel();
		multiModel.compressSpecies();
		updateSpeciesTableFromMultiModel();
		jTabGeneral.setSelectedIndex(0);
		
		renamingOption = oldRenamingOption;
		
	}
	
	
	private void expandAllExpressions() {
	/*	try {
		CModel model = multiModel.copasiDataModel.getModel();
		int numRows = tableSpeciesmodel.getRowCount();
		jTabGeneral.setSelectedIndex(Constants.TitlesTabs.SPECIES.index);
		 for (int i = 0;i < numRows;i++)
	        {
			  String name = tableSpeciesmodel.getValueAt(i, Constants.SpeciesColumns.NAME.index).toString();
			  long index = model.findMetabByName(name);
			  CMetab el = model.getMetabolite(index);
			  String copasiExpression = el.getExpression();
		      String expr = tableSpeciesmodel.getValueAt(i, Constants.SpeciesColumns.EXPRESSION.index).toString();
		      if(expr.trim().length() ==0) continue;
		      
		      String newExpr = multiModel.reprintExpressionCopasiExpanded(expr);
		      System.out.println("newExpr: " + newExpr);
		      
		      
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}

	private static void updateSpeciesTableFromMultiModel() {
		Vector<Species> species = multiModel.getAllSpecies();
		tableSpeciesmodel.clearData();
		  
		for(int i = 0; i < species.size(); i++) {
			Species sp = species.get(i);
			if(sp==null) continue;
			//System.out.println(i+" out of "+species.size()+" SPECIES "+sp.getDisplayedName());
			//System.out.flush();
			tableSpeciesmodel.setValueAt_withoutUpdate(sp.getDisplayedName(), i-1, Constants.SpeciesColumns.NAME.index);
			//tableSpeciesmodel.setValueAt_withoutUpdate(sp.getInitialConcentration(), i-1, 2);
			//tableSpeciesmodel.setValueAt_withoutUpdate(sp.getInitialAmount(), i-1, 3);
			tableSpeciesmodel.setValueAt_withoutUpdate(sp.getInitialQuantity_listString(), i-1,  Constants.SpeciesColumns.INITIAL_QUANTITY.index);
			tableSpeciesmodel.setValueAt_withoutUpdate(Constants.SpeciesType.getDescriptionFromCopasiType(sp.getType()), i-1,  Constants.SpeciesColumns.TYPE.index);
			tableSpeciesmodel.setValueAt_withoutUpdate(sp.getCompartment_listString(), i-1,  Constants.SpeciesColumns.COMPARTMENT.index);
			tableSpeciesmodel.setValueAt_withoutUpdate(sp.getExpression(), i-1,  Constants.SpeciesColumns.EXPRESSION.index);
			tableSpeciesmodel.setValueAt_withoutUpdate(sp.getNotes(), i-1,  Constants.SpeciesColumns.NOTES.index);
			if(CellParsers.isMultistateSpeciesName(sp.getDisplayedName())) {
				 tableSpeciesmodel.disableCell(i-1,Constants.SpeciesColumns.INITIAL_QUANTITY.index);
			     tableSpeciesmodel.disableCell(i-1,Constants.SpeciesColumns.TYPE.index);
			} else {
				 tableSpeciesmodel.enableCell(i-1,Constants.SpeciesColumns.INITIAL_QUANTITY.index);
			     tableSpeciesmodel.enableCell(i-1,Constants.SpeciesColumns.TYPE.index);
			}
			
		}
		
		for(int i =  species.size(); i < tableSpeciesmodel.getColumnCount(); i++) {
			tableSpeciesmodel.enableCell(i-1,Constants.SpeciesColumns.INITIAL_QUANTITY.index);
		     tableSpeciesmodel.enableCell(i-1,Constants.SpeciesColumns.TYPE.index);
		}
		

		tableSpeciesmodel.addAddEmptyRow_Listener();
		tableSpeciesmodel.addRow(new Vector());
		jTableSpecies.clearSelection();
		jTableSpecies.revalidate();
	}


	private static void updateGlobalQTableFromMultiModel() {
		Vector<GlobalQ> elements = multiModel.getAllGlobalQ();
		tableGlobalQmodel.clearData();
		for(int i = 0; i < elements.size(); i++) {
			GlobalQ sp = elements.get(i);
			if(sp==null) continue;
			tableGlobalQmodel.setValueAt_withoutUpdate(sp.getName(), i-1, Constants.GlobalQColumns.NAME.index);
			tableGlobalQmodel.setValueAt_withoutUpdate(sp.getInitialValue(), i-1,  Constants.GlobalQColumns.VALUE.index);
			tableGlobalQmodel.setValueAt_withoutUpdate(Constants.GlobalQType.getDescriptionFromCopasiType(sp.getType()), i-1,  Constants.GlobalQColumns.TYPE.index);
			tableGlobalQmodel.setValueAt_withoutUpdate(sp.getExpression(), i-1,  Constants.GlobalQColumns.EXPRESSION.index);
			tableGlobalQmodel.setValueAt_withoutUpdate(sp.getNotes(), i-1,  Constants.GlobalQColumns.NOTES.index);
		}
		jTableGlobalQ.revalidate();
	
	}
	
	private static void updateFunctionsTableFromMultiModel() {
		Vector<Function> elements = multiModel.getAllUserDefinedFunctins();
		tableFunctionsmodel.clearData();
		for(int i = 0; i < elements.size(); i++) {
			Function el = elements.get(i);
			if(el==null) continue;
			tableFunctionsmodel.setValueAt_withoutUpdate(el.printCompleteSignature(), i-1, Constants.FunctionsColumns.NAME.index);
			try {
				tableFunctionsmodel.setValueAt_withoutUpdate(el.getExpandedEquation(new Vector()), i-1, Constants.FunctionsColumns.EQUATION.index);
			} catch (Exception e) {
				if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) 	e.printStackTrace();
			}
		}
		jTableFunctions.revalidate();
	}

	
	private static void updateEventTableFromMultiModel() {
		Vector<msmb.model.Event> elements = multiModel.getAllEvents();
		tableEventsmodel.clearData();
		for(int i = 0; i < elements.size(); i++) {
			msmb.model.Event sp = elements.get(i);
			if(sp==null) continue;
			tableEventsmodel.setValueAt_withoutUpdate(sp.getName(), i-1, Constants.EventsColumns.NAME.index);
			tableEventsmodel.setValueAt_withoutUpdate(sp.getActions(), i-1,  Constants.EventsColumns.ACTIONS.index);
			tableEventsmodel.setValueAt_withoutUpdate(sp.getTrigger(), i-1,  Constants.EventsColumns.TRIGGER.index);
			tableEventsmodel.setValueAt_withoutUpdate(sp.getDelay(), i-1,  Constants.EventsColumns.DELAY.index);
			tableEventsmodel.setValueAt_withoutUpdate(sp.getNotes(), i-1,  Constants.EventsColumns.NOTES.index);
		}
		jTableEvents.revalidate();
	}
	
	
	private static void updateCompartmentsTableFromMultiModel() {
		Vector<Compartment> comps = multiModel.getAllCompartments();
		
		tableCompartmentsmodel.clearData();
		tableCompartmentsmodel.removeAddEmptyRow_Listener();
		tableCompartmentsmodel.addRow(new Vector());
		tableCompartmentsmodel.addAddEmptyRow_Listener();
		jTableCompartments.revalidate();
		
		for(int i = 0; i < comps.size(); i++) {
			Compartment c = comps.get(i);
			if(c==null) continue;
			tableCompartmentsmodel.setValueAt_withoutUpdate(c.getName(), i-1, Constants.CompartmentsColumns.NAME.index);
			tableCompartmentsmodel.setValueAt_withoutUpdate(c.getExpression(), i-1,  Constants.CompartmentsColumns.EXPRESSION.index);
			tableCompartmentsmodel.setValueAt_withoutUpdate(Constants.CompartmentsType.getDescriptionFromCopasiType(c.getType()), i-1,  Constants.CompartmentsColumns.TYPE.index);
			tableCompartmentsmodel.setValueAt_withoutUpdate(c.getInitialVolume(), i-1,  Constants.CompartmentsColumns.INITIAL_SIZE.index);
			tableCompartmentsmodel.setValueAt_withoutUpdate(c.getNotes(), i-1,  Constants.CompartmentsColumns.NOTES.index);
		}
						
		jTableCompartments.revalidate();
	}
	
	
	private void loadCompartmentsTable() throws Exception {
		tableCompartmentsmodel.clearData();
		tableCompartmentsmodel.removeAddEmptyRow_Listener();
        
		Vector rows = multiModel.loadCompartmentsTable();
		
       for (int i = 0;i < rows.size();i++) {
			jTabGeneral.setSelectedIndex(Constants.TitlesTabs.COMPARTMENTS.index);
            tableCompartmentsmodel.addRow((Vector)rows.get(i));
            updateModelFromTable(i, Constants.CompartmentsColumns.NAME.index);
         }
        
        tableCompartmentsmodel.addAddEmptyRow_Listener();
	}
	
	private static Vector<Function> loadFunctionsTable() throws Exception {
		if(tableFunctionsmodel== null) return null;
		tableFunctionsmodel.clearData();
		tableFunctionsmodel.removeAddEmptyRow_Listener();
		tableBuiltInFunctionsmodel.clearData();
		tableBuiltInFunctionsmodel.removeAddEmptyRow_Listener();
		listModel_FunctionToCompact.clear();
		Vector<Function> predefined_not_loaded = new Vector<Function>();
     	CFunctionDB db = CCopasiRootContainer.getFunctionList();
		
     	Vector printedFunctions = new Vector();
		CFunctionVectorN funcs = db.loadedFunctions();
		
		
		int row = 0;
		int indexFun = 0;
		//System.out.println("tot function:" + funcs.size());
		for (int i = 0; i < funcs.size();i++)
		{
			//System.out.println("function "+i + " of "+funcs.size());	System.out.flush();
			CEvaluationTree val = null;
			val = db.findFunction(funcs.get(i).getObjectName());
					
			String pureNameFunc = CellParsers.cleanName(val.getObjectName());
			CFunction cfun = (CFunction)val;
			try {
				Function f = new Function(pureNameFunc); 
				f.setCompleteFunSignatureInTable(false);
				if(!MainGui.showAllAvailableFunctions) f.setShow(false);
				/*System.out.println("Analyzing... "+pureNameFunc);
				System.out.flush();
				*/
				if(cfun.getType() == CFunction.PreDefined) {
					f.setEquation(val.getInfix(), CFunction.PreDefined, 0);
				} else {
					f.setEquation(val.getInfix(), CFunction.UserDefined, 0);
				}
				Vector<String> names = f.getParametersNames();
				CFunctionParameters variables = cfun.getVariables();
				for(int n = 0; n < names.size(); n++) {
					//String unquoted = names.get(n);
					//if(unquoted.startsWith("\"")&&unquoted.endsWith("\"")) unquoted = unquoted.substring(1, unquoted.length()-1);
					
					String quoted = names.get(n);
					long index = cfun.getVariableIndex(quoted);
					if(index ==-1) {
						String unquoted = quoted;
						if(unquoted.startsWith("\"")&&unquoted.endsWith("\"")) unquoted = unquoted.substring(1, unquoted.length()-1);
						index = cfun.getVariableIndex(unquoted);
					}
					CFunctionParameter param = (variables.getParameter(index));
					
					int type = param.getUsage(); 
					//PAR in native function can be a number --> VAR in my interpretation
					if(type==CFunctionParameter.PARAMETER) type = CFunctionParameter.VARIABLE;
					f.setParameterRole(names.get(n), type  );
				}
				for(int n = 0; n < names.size(); n++) {
					//System.out.println("index:" + names.get(n));
					//String unquoted = names.get(n);
					//if(unquoted.startsWith("\"")&&unquoted.endsWith("\"")) unquoted = unquoted.substring(1, unquoted.length()-1);
					String quoted = names.get(n);
					
					f.setParameterIndex(names.get(n), cfun.getVariableIndex(quoted));
				}
				f.setCompleteFunSignatureInTable(true);

			   multiModel.funDB.addChangeFunction(indexFun, f);
			   indexFun++;
			   
			   /*if((MainGui.showAllAvailableFunctions || f.toShow()) && !listModel_FunctionToCompact.contains(f.getName())) { 
					listModel_FunctionToCompact.addElement(f.getName()); 
				}
					jListFunctionToCompact.revalidate();
					scrollPaneListFunctionToCompact.revalidate();
					tableFunctionsmodel.addRow(new Vector());
					indexFun++;
						if(!MainGui.importFromSBMLorCPS && (MainGui.showAllAvailableFunctions || f.toShow())) {
					
						tableFunctionsmodel.setValueAt_withoutUpdate(f.printCompleteSignature(), row, 1);
						tableFunctionsmodel.setValueAt_withoutUpdate(f.getExpandedEquation(new Vector()), row, 2);
						printedFunctions.add(row);
						if(f.getType() == CFunction.PreDefined) {
							tableFunctionsmodel.disableCell(row, Constants.FunctionsColumns.NAME.index);
							tableFunctionsmodel.disableCell(row, Constants.FunctionsColumns.EQUATION.index);
							tableFunctionsmodel.disableCell(row, Constants.FunctionsColumns.PARAMETER_ROLES.index);
							//tableFunctionsmodel.disableCell(row, Constants.FunctionsColumns.SIGNATURE.index);
						} else{
							tableFunctionsmodel.enableCell(row, Constants.FunctionsColumns.NAME.index);
							tableFunctionsmodel.enableCell(row, Constants.FunctionsColumns.EQUATION.index);
							tableFunctionsmodel.enableCell(row, Constants.FunctionsColumns.PARAMETER_ROLES.index);
							//tableFunctionsmodel.enableCell(row, Constants.FunctionsColumns.SIGNATURE.index);
						}
						row++;
					}
					else {
						predefined_not_loaded.add(f);
					}
					*/
			} catch (Exception ex) {
				if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) 	ex.printStackTrace();
				if(val.getObjectName().compareTo("Mass action (irreversible)")!=0 &&
						val.getObjectName().compareTo("Mass action (reversible)")!=0	) {
					System.out.println("FUNCTION NOT IMPORTED: " +val.getObjectName());
					System.out.println("Parse error in string: " +val.getInfix());
					if(MainGui.fromMainGuiTest) throw ex;
				}
				continue;
			}
			
		}
		
		
		for(int i = 0; i < multiModel.funDB.getNumUserDefinedFunctions(); i++) {
			//if(printedFunctions.contains(i)) continue;
			Function f = multiModel.funDB.getUserDefinedFunctionByIndex(i);
			if(f ==null) continue;
			tableFunctionsmodel.setValueAt_withoutUpdate(f.printCompleteSignature(), i-1, 1);
			tableFunctionsmodel.setValueAt_withoutUpdate(f.getExpandedEquation(new Vector()), i-1, 2);
			tableFunctionsmodel.enableCell(i-1, Constants.FunctionsColumns.NAME.index);
			tableFunctionsmodel.enableCell(i-1, Constants.FunctionsColumns.EQUATION.index);
			tableFunctionsmodel.enableCell(i-1, Constants.FunctionsColumns.PARAMETER_ROLES.index);
		}
		
		for(int i = 0; i < multiModel.funDB.getNumBuiltInFunctions(); i++) {
			Function f = multiModel.funDB.getBuiltInFunctionByIndex(i);
			if(f ==null) continue;
			tableBuiltInFunctionsmodel.setValueAt_withoutUpdate(f.printCompleteSignature(), i-1, 1);
			tableBuiltInFunctionsmodel.setValueAt_withoutUpdate(f.getExpandedEquation(new Vector()), i-1, 2);
			tableBuiltInFunctionsmodel.disableCell(i-1, Constants.FunctionsColumns.NAME.index);
			tableBuiltInFunctionsmodel.disableCell(i-1, Constants.FunctionsColumns.EQUATION.index);
			//tableBuiltInFunctionsmodel.enableCell(row, Constants.FunctionsColumns.PARAMETER_ROLES.index);
			//tableFunctionsmodel.enableCell(row, Constants.FunctionsColumns.SIGNATURE.index);
		}
        tableFunctionsmodel.addAddEmptyRow_Listener();
        return predefined_not_loaded;
        
       
	}
	
	private static void  loadBuiltInFunctionsTable() throws Exception {
		if(tableBuiltInFunctionsmodel== null) return;
		tableBuiltInFunctionsmodel.clearData();
		tableBuiltInFunctionsmodel.removeAddEmptyRow_Listener();
		
		CFunctionDB db = CCopasiRootContainer.getFunctionList();
		
     	Vector printedFunctions = new Vector();
		CFunctionVectorN funcs = db.loadedFunctions();
		
		
		int row = 0;
		int indexFun = 0;
		//System.out.println("tot function:" + funcs.size());
		for (int i = 0; i < funcs.size();i++)
		{
			//System.out.println("function "+i + " of "+funcs.size());	System.out.flush();
			CEvaluationTree val = null;
			val = db.findFunction(funcs.get(i).getObjectName());
					
			String pureNameFunc = CellParsers.cleanName(val.getObjectName());
			CFunction cfun = (CFunction)val;
			if(cfun.getType() != CFunction.PreDefined) continue;
			try {
				Function f = new Function(pureNameFunc); 
				f.setCompleteFunSignatureInTable(false);
				if(!MainGui.showAllAvailableFunctions) f.setShow(false);
				/*System.out.println("Analyzing... "+pureNameFunc);
				System.out.flush();
				*/
				f.setEquation(val.getInfix(), CFunction.PreDefined, 0);
				Vector<String> names = f.getParametersNames();
				CFunctionParameters variables = cfun.getVariables();
				for(int n = 0; n < names.size(); n++) {
					//String unquoted = names.get(n);
					//if(unquoted.startsWith("\"")&&unquoted.endsWith("\"")) unquoted = unquoted.substring(1, unquoted.length()-1);
					
					String quoted = names.get(n);
					long index = cfun.getVariableIndex(quoted);
					if(index ==-1) {
						String unquoted = quoted;
						if(unquoted.startsWith("\"")&&unquoted.endsWith("\"")) unquoted = unquoted.substring(1, unquoted.length()-1);
						index = cfun.getVariableIndex(unquoted);
					}
					CFunctionParameter param = (variables.getParameter(index));
					
					int type = param.getUsage(); 
					//PAR in native function can be a number --> VAR in my interpretation
					if(type==CFunctionParameter.PARAMETER) type = CFunctionParameter.VARIABLE;
					f.setParameterRole(names.get(n), type  );
				}
				for(int n = 0; n < names.size(); n++) {
					//System.out.println("index:" + names.get(n));
					//String unquoted = names.get(n);
					//if(unquoted.startsWith("\"")&&unquoted.endsWith("\"")) unquoted = unquoted.substring(1, unquoted.length()-1);
					String quoted = names.get(n);
					
					f.setParameterIndex(names.get(n), cfun.getVariableIndex(quoted));
				}
				f.setCompleteFunSignatureInTable(true);

			   multiModel.funDB.addChangeFunction(indexFun, f);
			   indexFun++;
			   
			
			} catch (Exception ex) {
				if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) 	ex.printStackTrace();
				if(val.getObjectName().compareTo("Mass action (irreversible)")!=0 &&
						val.getObjectName().compareTo("Mass action (reversible)")!=0	) {
					System.out.println("FUNCTION NOT IMPORTED: " +val.getObjectName());
					System.out.println("Parse error in string: " +val.getInfix());
					if(MainGui.fromMainGuiTest) throw ex;
				}
				continue;
			}
			
		}
		
		
		for(int i = 0; i < multiModel.funDB.getNumBuiltInFunctions(); i++) {
			Function f = multiModel.funDB.getBuiltInFunctionByIndex(i);
			if(f ==null) continue;
			tableBuiltInFunctionsmodel.setValueAt_withoutUpdate(f.printCompleteSignature(), i-1, 1);
			tableBuiltInFunctionsmodel.setValueAt_withoutUpdate(f.getExpandedEquation(new Vector()), i-1, 2);
			tableBuiltInFunctionsmodel.disableCell(i-1, Constants.FunctionsColumns.NAME.index);
			tableBuiltInFunctionsmodel.disableCell(i-1, Constants.FunctionsColumns.EQUATION.index);
			//tableBuiltInFunctionsmodel.enableCell(row, Constants.FunctionsColumns.PARAMETER_ROLES.index);
			//tableFunctionsmodel.enableCell(row, Constants.FunctionsColumns.SIGNATURE.index);
		}
        
		 
	}
	
	public void setUnits(int unitTime, int unitVolume, int unitQuantity) {
		multiModel.setUnits(unitTime,unitVolume,unitQuantity);
	}

	private void loadGlobalQTable() throws Exception {
		tableGlobalQmodel.clearData();
		tableGlobalQmodel.removeAddEmptyRow_Listener();
        
		Vector rows = multiModel.loadGlobalQTable();
		  
		for (int i = 0;i < rows.size();i++) {
			jTabGeneral.setSelectedIndex(Constants.TitlesTabs.GLOBALQ.index);
	          tableGlobalQmodel.addRow((Vector)rows.get(i));
		      updateModelFromTable(i,Constants.GlobalQColumns.NAME.index);
        }
        
        tableGlobalQmodel.addRow(new Vector());
        tableGlobalQmodel.addAddEmptyRow_Listener();
	}
	
	private void loadEventsTable() throws Exception {
		tableEventsmodel.clearData();
		tableEventsmodel.removeAddEmptyRow_Listener();
        
		Vector rows = multiModel.loadEventsTable();
	        
		for (int i = 0;i < rows.size();i++) {
			  jTabGeneral.setSelectedIndex(4);
	          tableEventsmodel.addRow((Vector)rows.get(i));
		      updateModelFromTable(i,Constants.EventsColumns.NAME.index);
        }
        
        tableEventsmodel.addRow(new Vector());
        tableEventsmodel.addAddEmptyRow_Listener();
	}
	
	private void loadSpeciesTable() throws Exception {
		tableSpeciesmodel.clearData();
		tableSpeciesmodel.removeAddEmptyRow_Listener();
		  
	    Vector rows_and_SBMLid_and_newNamesFromIssuesAtImport = multiModel.loadSpeciesTable();
	    
	    Vector rows = (Vector) rows_and_SBMLid_and_newNamesFromIssuesAtImport.get(0);
	    HashMap<Long, String> SBMLids = (HashMap<Long, String>) rows_and_SBMLid_and_newNamesFromIssuesAtImport.get(1);
	    Vector<String> newNamesFromIssuesAtImport = (Vector) rows_and_SBMLid_and_newNamesFromIssuesAtImport.get(2);
	    
	    for (int i = 0;i < rows.size();i++)
        {
	       jTabGeneral.setSelectedIndex(1);
	       tableSpeciesmodel.addRow((Vector)rows.get(i));
	    
	       
	       if(newNamesFromIssuesAtImport.size() > 0 && newNamesFromIssuesAtImport.contains(((Vector)rows.get(i)).get(Constants.SpeciesColumns.NAME.index-1).toString())){
           		DebugMessage dm = new DebugMessage();
				dm.setOrigin_table(Constants.TitlesTabs.SPECIES.description);
			    dm.setOrigin_col(Constants.ReactionsColumns.NAME.index);
			    dm.setOrigin_row(i+1);
				dm.setProblem("Conflicting names. The original species could not be properly imported and the a new name has been created for that species.");
			    dm.setPriority(DebugConstants.PriorityType.MINOR_IMPORT_ISSUES.priorityCode);
				MainGui.addDebugMessage_ifNotPresent(dm);
           }
	       
	       jTableSpecies.setColumnSelectionInterval(2, 2);
           updateModelFromTable(i,Constants.SpeciesColumns.NAME.index);
        }
	    jTabGeneral.setSelectedIndex(0);
	    
	    tableSpeciesmodel.addAddEmptyRow_Listener();
	    tableSpeciesmodel.addRow(new Vector());
	    
        multiModel.updateSBMLids(SBMLids);
	}
	
	private Vector<CFunction> loadReactionTable(Vector<String> reactionWithProblems) throws Exception {
		tableReactionmodel.clearData();
		
    
        tableReactionmodel.removeAddEmptyRow_Listener();
        Vector rows = multiModel.loadReactionTable();
        
      //the LAAAAAAAAAAAAAAAAAAAST ENTRY IS A VECTOR CONTAINING THE CFUNCTION PREDEFINED THAT NEED TO BE LOADED
          
        for (int i = 0;i < rows.size()-1;i++)
        {
        	jTabGeneral.setSelectedIndex(0);
            tableReactionmodel.addRow((Vector)rows.get(i));
            if(reactionWithProblems.size() > 0 && reactionWithProblems.contains(((Vector)rows.get(i)).get(Constants.ReactionsColumns.NAME.index-1).toString())){
            	DebugMessage dm = new DebugMessage();
 				dm.setOrigin_table(Constants.TitlesTabs.REACTIONS.description);
 			    dm.setOrigin_col(Constants.ReactionsColumns.NAME.index);
 			    dm.setOrigin_row(i+1);
 				dm.setProblem("Turned to Irreversible reaction. The original reversible reaction in the SBML could not be properly converted to 2 irreversible reactions.");
 			    dm.setPriority(DebugConstants.PriorityType.MINOR_IMPORT_ISSUES.priorityCode);
 				MainGui.addDebugMessage_ifNotPresent(dm);
            }
            if(!MainGui.importFromSBMLorCPS) updateModelFromTable(i, Constants.ReactionsColumns.NAME.index);
        }
       
        tableReactionmodel.addRow(new Vector());
        tableReactionmodel.addAddEmptyRow_Listener();
        
        return (Vector<CFunction>) rows.get(rows.size()-1);
	}
		
	private JPanel getJPanelModelProperties() {
		//if (jPanel == null) {
			GridLayout gridLayout = new GridLayout();
			gridLayout.setRows(4);
			jPanelModelProperties_1 = new JPanel();
			jPanelModelProperties_1.setLayout(gridLayout);
			gridLayout.setVgap(2);
			gridLayout.setHgap(3);
			jPanelModelProperties_1.add(getJPanelModelName(), null);
			jPanelModelProperties_1.add(getJPanelUnitMeasure(), null);
			jPanelModelProperties_1.add(getJCheckBoxExportConcentration());
			jPanelModelProperties_1.setBorder(new TitledBorder("Model Properties"));
			
			//jPanel.add(getJPanelUnitMeasure(), null);
			
		//}
		return jPanelModelProperties_1;
	}
	
	private JPanel getJPanelModelName() {
		jPanelModelName = new JPanel();
		BorderLayout layout = new BorderLayout();
		jPanelModelName.setLayout(layout);
		layout.setVgap(2);
		layout.setHgap(3);
		jPanelModelName.add(new JLabel("Model name "), BorderLayout.WEST);
		textFieldModelName = new JTextField();
		textFieldModelName.getDocument().addDocumentListener(new DocumentListener() {
			  public void changedUpdate(DocumentEvent e) {
			    updateModelName();
			  }
			  public void removeUpdate(DocumentEvent e) {
				  updateModelName();
			  }
			  public void insertUpdate(DocumentEvent e) {
				  updateModelName();
			  }

			  public void updateModelName() {
			    MainGui.modelName = textFieldModelName.getText();
			  }
			});

		Dimension dim = new Dimension(200,20);
		textFieldModelName.setMinimumSize(dim);
		textFieldModelName.setMaximumSize(dim);
		textFieldModelName.setPreferredSize(dim);
		textFieldModelName.setMargin(new Insets(3,3,3,3));
		textFieldModelName.setText(MainGui.modelName);
		jPanelModelName.add(textFieldModelName, BorderLayout.CENTER);
		
		return jPanelModelName;
	}

	
	private JPanel getJPanelUnitMeasure() {
		if (jPanelUnitMeasure == null) {
			jPanelUnitMeasure = new JPanel();
			comboBox_unitVolume = new JComboBox();
			comboBox_unitTime = new JComboBox();
			comboBox_unitQuantity = new JComboBox();
			
			for(int i = 0; i < Constants.volumeUnits.size(); i++) { 
				comboBox_unitVolume.addItem((String)Constants.volumeUnits.get(i));
			}
			jPanelUnitMeasure.add(new JLabel("Volume unit"));
			jPanelUnitMeasure.add(comboBox_unitVolume);
			
			for(int i = 0; i < Constants.timeUnits.size(); i++) { 
				comboBox_unitTime.addItem((String)Constants.timeUnits.get(i));
			}
			jPanelUnitMeasure.add(new JLabel("Time unit"));
			jPanelUnitMeasure.add(comboBox_unitTime);
			
			
			for(int i = 0; i < Constants.quantityUnits.size(); i++) { 
				comboBox_unitQuantity.addItem((String)Constants.quantityUnits.get(i));
			}
			jPanelUnitMeasure.add(new JLabel("Quantity unit"));
			jPanelUnitMeasure.add(comboBox_unitQuantity);
			
			
			comboBox_unitVolume.addActionListener( new  ActionListener() {
				public void actionPerformed(ActionEvent e) {
					 MainGui.volumeUnit = Constants.UnitTypeVolume.getCopasiTypeFromDescription((String)comboBox_unitVolume.getSelectedItem());
					}
			});
			comboBox_unitTime.addActionListener(new  ActionListener() {
				public void actionPerformed(ActionEvent e) {
					 MainGui.timeUnit = Constants.UnitTypeTime.getCopasiTypeFromDescription((String)comboBox_unitTime.getSelectedItem());
					}
			});
			comboBox_unitQuantity.addActionListener(new  ActionListener() {
				public void actionPerformed(ActionEvent e) {
					 MainGui.quantityUnit = Constants.UnitTypeQuantity.getCopasiTypeFromDescription((String)comboBox_unitQuantity.getSelectedItem());
					}
			});
			
			comboBox_unitVolume.setSelectedIndex(0);
			comboBox_unitTime.setSelectedIndex(0);
			comboBox_unitQuantity.setSelectedIndex(0);
			
			
		}
		return jPanelUnitMeasure;
	}

	
	private JCheckBox getJCheckBoxQuantityIsConcentration() {
		if (jCheckBoxQuantityIsConcentration == null) {
			jCheckBoxQuantityIsConcentration = new JCheckBox();
			jCheckBoxQuantityIsConcentration.setText("Quantity = Concentration (uncheck for Quantity = ParticleNumber)");
			jCheckBoxQuantityIsConcentration.setSelected(quantityIsConc);
			jCheckBoxQuantityIsConcentration.addChangeListener(new javax.swing.event.ChangeListener() {
				public void stateChanged(javax.swing.event.ChangeEvent e) {
					updateStatusQuantityIsConcentration();
				}
			});
		}
		return jCheckBoxQuantityIsConcentration;
	}
	
	private void updateStatusExportConcentration() {
		updateStatusExportConcentration(jCheckBoxExportConcentration.isSelected());
	}
	
	public void updateModelProperties() {
		jCheckBoxQuantityIsConcentration.setSelected(quantityIsConc);
		jCheckBoxExportConcentration.setSelected(exportConcentration);
		
		
		comboBox_unitVolume.setSelectedItem(Constants.UnitTypeVolume.getDescriptionFromCopasiType(volumeUnit));
		comboBox_unitTime.setSelectedItem(Constants.UnitTypeTime.getDescriptionFromCopasiType(timeUnit));
		comboBox_unitQuantity.setSelectedItem(Constants.UnitTypeQuantity.getDescriptionFromCopasiType(quantityUnit));
		textFieldModelName.setText(MainGui.modelName);
	}
	
	public void updateStatusExportConcentration(boolean val) {
		exportConcentration  = val;
	}
	
	private void updateStatusQuantityIsConcentration() {
		updateStatusQuantityIsConcentration(jCheckBoxQuantityIsConcentration.isSelected());
	}
	
	public void updateStatusQuantityIsConcentration(boolean val) {
		quantityIsConc  = val;
	}
	

	private JCheckBox getJCheckBoxExportConcentration() {
		if (jCheckBoxExportConcentration == null) {
			jCheckBoxExportConcentration = new JCheckBox();
			jCheckBoxExportConcentration.setText("Export Concentration (if no reference is specified)");
			jCheckBoxExportConcentration.setSelected(exportConcentration);
			jCheckBoxExportConcentration.addChangeListener(new javax.swing.event.ChangeListener() {
				public void stateChanged(javax.swing.event.ChangeEvent e) {
					updateStatusExportConcentration();
				}
			});
		}
		return jCheckBoxExportConcentration;
	}
	
	
	private JPanel getJPaneTreeDebugMessages() {
		if (jPanelTreeDebugMessages == null) {
			jPanelTreeDebugMessages = new TreeDebugMessages();
			
		}
		return jPanelTreeDebugMessages;
	}

	private JScrollPane getJScrollPane1() {
		if (jScrollPane1 == null) {
			jScrollPane1 = new JScrollPane();
			jScrollPane1.setViewportView(getJTextAreaDebugComplete());
		}
		return jScrollPane1;
	}



	private JTextArea getJTextAreaDebugComplete() {
		if (jTextAreaDebugComplete == null) {
			jTextAreaDebugComplete = new JTextArea();
			jTextAreaDebugComplete.setWrapStyleWord(true);
			jTextAreaDebugComplete.setLineWrap(true);
			jTextAreaDebugComplete.setEditable(false);
			jTextAreaDebugComplete.setFont(UIManager.getFont("Label.font"));
		}
		return jTextAreaDebugComplete;
	}

	private void loadRecentFiles() {
		
		BufferedReader fin;
		String strLine;
		if(file_RecentFiles.length() == 0) return;
			try {
				fin = new BufferedReader(new FileReader(file_RecentFiles));
				 while ((strLine = fin.readLine()) != null)   {
					 addRecents(new File(strLine));
				 }
				fin.close();
			}
		catch (Exception e) {
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
			recentFiles = new Vector(5);
		}
	}
	
	private void loadPreferencesFile() {
		BufferedReader fin;
		String strLine;
		if(file_preferences.length() == 0) return;
			try {
				fin = new BufferedReader(new FileReader(file_preferences));
				Vector pref = new Vector<String>();
				 while ((strLine = fin.readLine()) != null)   {
					 pref.add(strLine);
				 }
				 preferenceFrame.extractPreferences(pref);
				fin.close();
				//preferenceFrame.updateStatusAllAvailableFunctions();
				preferenceFrame.updateStatusAutocomplete();
				preferenceFrame.updateStatusDialogWindow();
			}
		catch (Exception e) {
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES)
				e.printStackTrace();
		}
	}

	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			getJTabGeneral();
			jContentPane.add(jTabGeneral, BorderLayout.CENTER);
			statusBar = new StatusBar();
			jContentPane.add(statusBar, BorderLayout.SOUTH);
			if(customFont!=null) jTabGeneral.setFont(customFont);	
			if(customFont!=null) statusBar.setFont(customFont);
		}
		return jContentPane;
	}

	
	class StatusBar extends JPanel {

		JLabel message = new JLabel();
		
		  public StatusBar() {
		    setLayout(new BorderLayout());
		    setPreferredSize(new Dimension(10, 23));

		    JPanel rightPanel = new JPanel(new BorderLayout());
		    rightPanel.add(new JLabel(new AngledLinesWindowsCornerIcon()), BorderLayout.SOUTH);
		    rightPanel.setOpaque(false);
		    message.setText("...");
		    add(rightPanel, BorderLayout.EAST);
		    add(message, BorderLayout.CENTER);
		    setBackground(SystemColor.control);
		  }
		  
		  public void setMessage(String newMessage) {
			  message.setText(newMessage);
			  revalidate();
		  }
		  
		  public String getMessage() {
			 return message.getText();
		  }

		  protected void paintComponent(Graphics g) {
		    super.paintComponent(g);

		    int y = 0;
		    g.setColor(new Color(156, 154, 140));
		    g.drawLine(0, y, getWidth(), y);
		    y++;
		    g.setColor(new Color(196, 194, 183));
		    g.drawLine(0, y, getWidth(), y);
		    y++;
		    g.setColor(new Color(218, 215, 201));
		    g.drawLine(0, y, getWidth(), y);
		    y++;
		    g.setColor(new Color(233, 231, 217));
		    g.drawLine(0, y, getWidth(), y);

		    y = getHeight() - 3;
		    g.setColor(new Color(233, 232, 218));
		    g.drawLine(0, y, getWidth(), y);
		    y++;
		    g.setColor(new Color(233, 231, 216));
		    g.drawLine(0, y, getWidth(), y);
		    y = getHeight() - 1;
		    g.setColor(new Color(221, 221, 220));
		    g.drawLine(0, y, getWidth(), y);

		  }

		}
	
	class AngledLinesWindowsCornerIcon implements Icon {
		  private Color WHITE_LINE_COLOR = new Color(255, 255, 255);

		  private Color GRAY_LINE_COLOR = new Color(172, 168, 153);
		  private int WIDTH = 13;

		  private static final int HEIGHT = 13;

		  public int getIconHeight() {    return WIDTH;  }

		  public int getIconWidth() {    return HEIGHT; 	}

		  public void paintIcon(Component c, Graphics g, int x, int y) {

		    g.setColor(WHITE_LINE_COLOR);
		    g.drawLine(0, 12, 12, 0);
		    g.drawLine(5, 12, 12, 5);
		    g.drawLine(10, 12, 12, 10);

		    g.setColor(GRAY_LINE_COLOR);
		    g.drawLine(1, 12, 12, 1);
		    g.drawLine(2, 12, 12, 2);
		    g.drawLine(3, 12, 12, 3);

		    g.drawLine(6, 12, 12, 6);
		    g.drawLine(7, 12, 12, 7);
		    g.drawLine(8, 12, 12, 8);

		    g.drawLine(11, 12, 12, 11);
		    g.drawLine(12, 12, 12, 12);

		  }
	}
	
	class RecentItemActionListener implements ActionListener {
		File filePath;
		
		RecentItemActionListener(File file) {
			filePath = file;
			
		}

	    public void actionPerformed(ActionEvent arg0) {
	    	String ext = null;
	        String s = filePath.getName();
	        int i = s.lastIndexOf('.');

	        if (i > 0 &&  i < s.length() - 1) {
	            ext = s.substring(i+1).toLowerCase();
	        }
	        try {
	        	if(ext.compareTo("cps")==0)

	        		loadCPS(filePath);

	        	else if(ext.compareTo("xml")==0) loadSBML(filePath);  
	        	else if(ext.compareTo("sbml")==0) loadSBML(filePath);  
	        } catch (Exception e) {
	        	if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
	        }
	   }
	}
	
	
	public MainGui() {
		super();
		try {
			initialize();
		} catch (Exception e) {
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
		}
		new CellParsers();
	
	}
	
	public JFrame getJFrame() {
         return frame;
    } 
	 
	private void initialize() throws Exception {
		multiModel = new MultiModel();
		
		frame = this;
		preferenceFrame = new PreferencesFrame(this);
		recordAutosave = new RecordAutosave(this);
		
		row_to_highlight = new Vector<Integer>();
		for(int i = 0; i < Constants.TitlesTabs.getNumTabs()+1; i++) {	row_to_highlight.add(new Integer(-1));	} //last entry is the index of the sub builtin table
		setSize(700, 539);
		setJMenuBar(getJJMenuBar());
		setContentPane(getJContentPane());
		setTitle(Constants.TOOL_NAME_FULL+" - version 0."+serialVersionUID);
		setLocationRelativeTo(null);
		
	 	
		//directoryRecentFiles = System.getProperty("user.home") + System.getProperty("file.separator") + "MultiStateGui_HISTORY.cfg";
		file_RecentFiles = Constants.RECENT_FILE_NAME;
		file_preferences = Constants.PREFERENCES_FILE_NAME;
		
		addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent evt) {
		        exit();
		    }
		});
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		loadRecentFiles();
		loadPreferencesFile();
		
		
		multiModel.setTableReactionModel(tableReactionmodel);
		
	
		 popupDebugMessageActions = new JPopupMenu();
		 ackMenuItem = new JMenuItem("Acknowledge!");
		 ackMenuItem.addActionListener(new ActionListener() {
			  public void actionPerformed(ActionEvent e) {
				  try {
					ackSelectedDebugMessage();
				} catch (Exception e1) {
					
					if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e1.printStackTrace();
				}
			  }
		  });
		 popupDebugMessageActions.add(ackMenuItem);
			
		
		 loadFunctionsTable();
		 fillCleanNamesPredefinedFunctions();
			MainGui.updateFunctionsView();
			ExportMultistateFormat.setMainGui(this);
			
			if(customFont!=null) {
			jTableCompartments.getTableHeader().setFont(customFont);
			jTableEvents.getTableHeader().setFont(customFont);
			jTableFunctions.getTableHeader().setFont(customFont);
			jTableGlobalQ.getTableHeader().setFont(customFont);
			jTableReactions.getTableHeader().setFont(customFont);
			jTableSpecies.getTableHeader().setFont(customFont);
			}
			
			
		loadLastAutosavedIfExists();	
			
	}
	
	
	
	
	private void loadLastAutosavedIfExists() {
		String dirName = recordAutosave.getPath();
		
		File dir = new File(dirName);
		File[] files = dir.listFiles(new FilenameFilter() { 
            public boolean accept(File dir, String filename)
                 {  return 
                		 filename.startsWith(Constants.AUTOSAVE_TMP_PREFIX) 
                		 && filename.endsWith(Constants.AUTOSAVE_TMP_SUFFIX+Constants.MULTISTATE_FILE_EXTENSION)
                		 && filename.compareTo(Constants.AUTOSAVE_TMP_PREFIX+Constants.AUTOSAVE_UNTITLED+Constants.AUTOSAVE_TMP_SUFFIX+Constants.MULTISTATE_FILE_EXTENSION)!=0;    		 }
		});

		if(files.length > 0) {
			
			Object[] options = {"Yes",
			                    "No"};
			int n = JOptionPane.showOptionDialog(frame,
			    "The previous session was not terminated properly. Do you want to recover the last available model?",
			    "Question",
			    JOptionPane.YES_NO_CANCEL_OPTION,
			    JOptionPane.QUESTION_MESSAGE,
			    null,
			    options,
			    options[0]);
			if(n==0) {//yes
				String fileName = files[0].getName();
				int from = fileName.indexOf(Constants.AUTOSAVE_TMP_PREFIX) + Constants.AUTOSAVE_TMP_PREFIX.length();
				int to = fileName.lastIndexOf(Constants.AUTOSAVE_TMP_SUFFIX);
				String fileWithoutPrefixSuffix =fileName.substring(from,to);
				inputFile = fileWithoutPrefixSuffix;
                donotCleanDebugMessages = true;
                importTablesMultistateFormat(files[0]);
                donotCleanDebugMessages = false;
                startAutosave();
         	} else if(n==1) { //no
				for(int i = 0; i < files.length; i++) {
					files[i].delete();
				}
				return;
			} 
		}

	}

	public static void ackSelectedDebugMessage() throws Exception {
		clear_debugMessages_relatedWith(toBeAck_debugMessage.getOrigin_table(), toBeAck_debugMessage.getPriority(), toBeAck_debugMessage.getOrigin_row(), toBeAck_debugMessage.getOrigin_col());
		old_acknowledged.add(toBeAck_debugMessage.getOrigin_table()+"@"+toBeAck_debugMessage.getPriority()+"_"+toBeAck_debugMessage.getOrigin_row()+"_"+toBeAck_debugMessage.getOrigin_col());
		int col = toBeAck_debugMessage.getOrigin_col();
		if(toBeAck_debugMessage.getPriority() == DebugConstants.PriorityType.DEFAULTS.priorityCode) {
			String descr = toBeAck_debugMessage.getOrigin_table();
			CustomJTable_MSMB table1 = null;
			Vector<Integer> columns = new Vector<>();
			if(descr.compareTo(Constants.TitlesTabs.SPECIES.description)==0) {	
				table1 = jTableSpecies;	
				if(col == Constants.SpeciesColumns.NAME.index){
					columns.add(Constants.SpeciesColumns.INITIAL_QUANTITY.index); 
					columns.add(Constants.SpeciesColumns.COMPARTMENT.index);
				} else { columns.add(col);}
			}

			else if(descr.compareTo(Constants.TitlesTabs.COMPARTMENTS.description)==0) {	
				table1 = jTableCompartments; 
				if(col == Constants.CompartmentsColumns.NAME.index){ 
					columns.add(Constants.CompartmentsColumns.INITIAL_SIZE.index); 
					columns.add(Constants.CompartmentsColumns.NAME.index);	
				} else { columns.add(col);}
			}
			else if(descr.compareTo(Constants.TitlesTabs.EVENTS.description)==0) {	table1 = jTableEvents;	}
			else if(descr.compareTo(Constants.TitlesTabs.GLOBALQ.description)==0) {	
				table1 = jTableGlobalQ; 
				if(col == Constants.GlobalQColumns.NAME.index) { columns.add(Constants.GlobalQColumns.VALUE.index); }
				else { columns.add(col);}
			}
			else if(descr.compareTo(Constants.TitlesTabs.REACTIONS.description)==0) {	table1 = jTableReactions;	}

			for(Integer cols : columns) {
				table1.cell_no_defaults(toBeAck_debugMessage.getOrigin_row()-1,cols);
			}
			table1.repaint();
		}
	
	}

	private void fillCleanNamesPredefinedFunctions() {
		CFunctionDB funDB_copasi = CCopasiRootContainer.getFunctionList();
		CFunctionVectorN vectorFun = funDB_copasi.loadedFunctions();
		long totalNumPredefindedFunCOPASI = (long) vectorFun.size();
	    cleanNamesPredefinedFunctions = new HashMap<String, String>();
	    
	    
	    for(int i = 0; i < totalNumPredefindedFunCOPASI; i++) {
	    	String original = vectorFun.get(i).getObjectName();
	    	String clean = CellParsers.cleanName(original);
	    	cleanNamesPredefinedFunctions.put(clean, original);
	    }
		
	}

	
	/**
	* Adds the specified path to the java library path
	*
	* @param pathToAdd the path to add
	* @throws Exception
	*/
	public static void addLibraryPath(String pathToAdd) throws Exception{
	    final Field usrPathsField = ClassLoader.class.getDeclaredField("usr_paths");
	    usrPathsField.setAccessible(true);
	 
	    //get array of paths
	    final String[] paths = (String[])usrPathsField.get(null);
	 
	    //check if the path to add is already present
	    for(String path : paths) {
	        if(path.equals(pathToAdd)) {
	            return;
	        }
	    }
	 
	    //add the new path
	    final String[] newPaths = Arrays.copyOf(paths, paths.length + 1);
	    newPaths[newPaths.length-1] = pathToAdd;
	    usrPathsField.set(null, newPaths);
	}
	
	public static void main(String[] args) {
		try {
		  addLibraryPath("..\\libs");
		  addLibraryPath("libs");
	      UIManager.setLookAndFeel(
	            UIManager.getSystemLookAndFeelClassName());
		  
		  final SplashScreen splash = SplashScreen.getSplashScreen();
		  if (splash == null) {
			  //System.out.println("SplashScreen.getSplashScreen() returned null");
		  }
		  else {
			  Graphics2D g = splash.createGraphics();
			  if (g == null) {
				//  System.out.println("g is null");
				  //return; 
			  }
			}
		  final MainGui thisClass = new MainGui();
		  thisClass.setVisible(true);
		  
		
	
		} catch(Exception ex) {
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) ex.printStackTrace();
			return;
		}
	}

	public static int getTotNumExpandedReactions(int row) throws Exception {//TO BE MODIFIED... NON DEVO GENERARLE TUTTE, MA SOL CONTARE GLI STATI
		String stringReactions = new String();
		String string_reaction = ((String)tableReactionmodel.getValueAt(row, 2));
		Vector metabolites = new Vector();
		try{ 
			metabolites = CellParsers.parseReaction(multiModel,string_reaction,row+1);
		} catch(Exception ex) {
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES)
				ex.printStackTrace();
			metabolites.add(new Vector());
			metabolites.add(new Vector());
			metabolites.add(new Vector());
		}

		
		Vector singleConfigurations = multiModel.expandReaction(metabolites,row);
		return singleConfigurations.size();
	}
	
	
	public static MutablePair<String, String> getExpandedMultistateReactions(int row, int from, int to) throws Exception {
		MutablePair<String, String> pair = new MutablePair<String, String>();
		Vector orderedReactions = new Vector<String>();
		String string_reaction = ((String)tableReactionmodel.getValueAt(row, 2));
		Vector metabolites = new Vector();
		try{ 
			metabolites = CellParsers.parseReaction(multiModel,string_reaction,row+1);
		} catch(Exception ex) {
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES)		ex.printStackTrace();
			metabolites.add(new Vector());
			metabolites.add(new Vector());
			metabolites.add(new Vector());
		}

		
		Vector singleConfigurations = multiModel.expandReaction(metabolites,row);	
		if(singleConfigurations.size()<1) return new MutablePair<String, String>();
		
		for(int j = 0; j < singleConfigurations.size(); j++) {	
			String stringReactions = new String();
			Vector expandedReaction = (Vector) singleConfigurations.get(j);
			Vector subs = (Vector)expandedReaction.get(0);
			Vector prod =(Vector)expandedReaction.get(1);
			Vector mod = (Vector)expandedReaction.get(2);
			for(int i = 0; i < subs.size(); i++) {
				stringReactions += " " + subs.get(i) + " +";
			}
			if(subs.size() >= 1) stringReactions = stringReactions.substring(0, stringReactions.length()-1);
			stringReactions += "->";
			for(int i = 0; i < prod.size(); i++) {
				stringReactions += " " + prod.get(i) + " +";
			}
			if(prod.size() >= 1) stringReactions = stringReactions.substring(0, stringReactions.length()-1);
			if(mod.size() > 0) stringReactions += ";";
			for(int i = 0; i < mod.size(); i++) {
				stringReactions += " " + mod.get(i) + " ,";
			}
			if(mod.size() > 1) stringReactions = stringReactions.substring(0, stringReactions.length()-1);
			//stringReactions += System.getProperty("line.separator");
			orderedReactions.add(stringReactions);
			
		}
		String stringReactions = new String();
		
		
		Collections.sort(orderedReactions, new AlphanumComparator());
		for(int i = 0; i < orderedReactions.size(); i++) {
			//TO CHANGE!!! HERE I ALWAYS TAKE ALL THE REACTIONS AND RETURN JUST A SUBSET BUT IT IS HIGHLY INEFFICIENT
			if(i<from-1 || i >= to) continue;
			stringReactions += orderedReactions.get(i) + System.getProperty("line.separator");
		}
		pair.left = stringReactions;
		
		String key = Constants.TitlesTabs.REACTIONS.description
				+"@"+DebugConstants.PriorityType.MISSING.priorityCode
				+"_"+(row+1)+"_"+Constants.ReactionsColumns.REACTION.index;
		
		DebugMessage dm = MainGui.debugMessages.get(key);
		if(dm!=null) {
			String missing = dm.getProblem();
			pair.right = missing.substring(missing.indexOf(":")+1);
		} else {
			pair.right = new String();
		}
		
		
		return pair;
	}

	public static void addDebugMessage_ifNotPresent(DebugMessage dm) {
		addDebugMessage_ifNotPresent(dm,true);
		try {
			recolorCell(dm,true);
		} catch (Exception e) {
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES)		e.printStackTrace();
		}
	}
	
	
	public static void addDebugMessage_ifNotPresent(DebugMessage dm, boolean concatenateErrorMessage) {
		String key = dm.getOrigin_table()+"@"+dm.getPriority()+"_"+dm.getOrigin_row()+"_"+dm.getOrigin_col();
		 if(!old_acknowledged.contains(key)){
			 String key_parsing = dm.getOrigin_table()+"@"+DebugConstants.PriorityType.PARSING.priorityCode+"_"+dm.getOrigin_row()+"_"+dm.getOrigin_col();
			 if(debugMessages.get(key) == null && debugMessages.get(key_parsing) == null) {
				 debugMessages.put(key, dm);
			 } else {
				 if(debugMessages.get(key_parsing) == null) {
					 DebugMessage old = debugMessages.get(key);
					 if(!old.getProblem().contains(dm.getProblem()) && concatenateErrorMessage) {
						 old.setProblem(old.getProblem()+System.getProperty("line.separator")+dm.getProblem());
					 } 
					 debugMessages.put(key, old);
				 } else {
					 try {
						clear_debugMessages_relatedWith(dm.getOrigin_table(), DebugConstants.PriorityType.PARSING.priorityCode, dm.getOrigin_row(), dm.getOrigin_col());
					} catch (Exception e) {
						e.printStackTrace();
					}
					 debugMessages.put(key, dm);
				 }
			 }
		 }
		 
		if(dm.getPriority() == DebugConstants.PriorityType.DEFAULTS.priorityCode) {
			String descr = dm.getOrigin_table();
			CustomJTable_MSMB table = null;
		Vector<Integer> columns = new Vector<>();
			if(descr.compareTo(Constants.TitlesTabs.SPECIES.description)==0) {	
				table = jTableSpecies;	
				columns.add(Constants.SpeciesColumns.INITIAL_QUANTITY.index); 
				columns.add(Constants.SpeciesColumns.COMPARTMENT.index);
			}

			else if(descr.compareTo(Constants.TitlesTabs.COMPARTMENTS.description)==0) {	
				table = jTableCompartments; 
				columns.add(Constants.CompartmentsColumns.INITIAL_SIZE.index); 
				columns.add(Constants.CompartmentsColumns.NAME.index);	
			}
			else if(descr.compareTo(Constants.TitlesTabs.EVENTS.description)==0) {	table = jTableEvents;	}
			else if(descr.compareTo(Constants.TitlesTabs.GLOBALQ.description)==0) {	table = jTableGlobalQ; columns.add(Constants.GlobalQColumns.VALUE.index);	}
			else if(descr.compareTo(Constants.TitlesTabs.REACTIONS.description)==0) {	table = jTableReactions;	}

			/*for(Integer cols : columns) {
				table.cell_has_defaults(dm.getOrigin_row()-1,cols);
			}*/
		}
		updateDebugTab();
	}

	
	
	public static Vector<DebugMessage> getDebugMessages(double partial_key) {
		Vector<DebugMessage> ret = new Vector<DebugMessage>();
		Iterator it = debugMessages.keySet().iterator();
		String partial_key_string = new Double(partial_key).toString();
		if(partial_key_string.endsWith(".0")) { partial_key_string = partial_key_string.substring(0, partial_key_string.indexOf(".0"));}
		while(it.hasNext()) {
			String key = (String) it.next();
			if(key.contains("@"+partial_key_string+".") || key.contains("@"+partial_key_string+"_")) ret.add(debugMessages.get(key));
		}
		return ret;
	}

	public int getNumComp() {		return multiModel.getNumComp();	}
	public int getNumSpecies() {	 return multiModel.getNumSpecies(); }
	public int getNumReactions() {	 return multiModel.getNumReactions();}
	public int getNumSpeciesExpanded() throws Exception {	 return multiModel.getNumSpeciesExpanded(); }
	public int getNumReactionsExpanded() {	 return multiModel.getNumReactionsExpanded();}
	public int getNumGlobalQ() {	return  multiModel.getNumGlobalQ();}
	public int getNumEvents() {	return multiModel.getNumEvents();}

	/*public static void setCheckBoxTrigger(int row) {
		System.out.println("setCheckBoxTrigger row:"+row);
	}*/

	public static void highlightElement_relatedWith(DebugMessage dm) {
		jTabGeneral.setSelectedIndex(Constants.TitlesTabs.getIndexFromDescription(dm.getOrigin_table()));
		MainGui.cell_to_highlight = new MutablePair(dm.getOrigin_row(),  dm.getOrigin_col());
		
		
		MainGui.row_to_highlight.set(Constants.TitlesTabs.getIndexFromDescription(dm.getOrigin_table()),-1);
		
		if(dm.getOrigin_table().compareTo(Constants.TitlesTabs.SPECIES.description)==0) { 
				jTableSpecies.setCell_to_highlight(new MutablePair(dm.getOrigin_row(),  dm.getOrigin_col()));
			   jTableSpecies.scrollRectToVisible(new Rectangle(jTableSpecies.getCellRect(dm.getOrigin_row()-1, 0, true)));  
 				MainGui.jTableSpecies.clearSelection();
	     		MainGui.jTableSpecies.revalidate();
	     	}
	     	else if(dm.getOrigin_table().compareTo(Constants.TitlesTabs.REACTIONS.description)==0) { 
	     		jTableReactions.scrollRectToVisible(new Rectangle(jTableReactions.getCellRect(dm.getOrigin_row()-1, 0, true)));  
 				MainGui.jTableReactions.clearSelection();
	     		MainGui.jTableReactions.revalidate();
	     	}
	     	else if(dm.getOrigin_table().compareTo(Constants.TitlesTabs.GLOBALQ.description)==0) { 
	     		jTableGlobalQ.scrollRectToVisible(new Rectangle(jTableGlobalQ.getCellRect(dm.getOrigin_row()-1, 0, true)));  
 				MainGui.jTableGlobalQ.clearSelection();
	     		MainGui.jTableGlobalQ.revalidate();
	     		}
	     	else if(dm.getOrigin_table().compareTo(Constants.TitlesTabs.FUNCTIONS.description)==0) {
	     		jTableFunctions.scrollRectToVisible(new Rectangle(jTableFunctions.getCellRect(dm.getOrigin_row()-1, 0, true)));  
 				MainGui.jTableFunctions.clearSelection();
	     		MainGui.jTableFunctions.revalidate();
	    	}
	     	else if(dm.getOrigin_table().compareTo(Constants.TitlesTabs.EVENTS.description)==0) { 
	     		jTableEvents.scrollRectToVisible(new Rectangle(jTableEvents.getCellRect(dm.getOrigin_row()-1, 0, true)));  
 				MainGui.jTableEvents.clearSelection();
	     		MainGui.jTableEvents.revalidate();
	     		}
	     	else if(dm.getOrigin_table().compareTo(Constants.TitlesTabs.COMPARTMENTS.description)==0) { 
	     		jTableCompartments.scrollRectToVisible(new Rectangle(jTableCompartments.getCellRect(dm.getOrigin_row()-1, 0, true)));  
 				MainGui.jTableCompartments.clearSelection();
	     		MainGui.jTableCompartments.revalidate();
	     	}
		
	
	}

	public static String getCellContent(FoundElement foundElement) {
		String ret = new String();
		CustomTableModel_MSMB tModel = null;
		
		String descr = foundElement.getTableDescription();
		     if(descr.compareTo(Constants.TitlesTabs.REACTIONS.description) == 0) { tModel = tableReactionmodel;	}
		else if(descr.compareTo(Constants.TitlesTabs.SPECIES.description) == 0) { tModel = tableSpeciesmodel;	}
		else if(descr.compareTo(Constants.TitlesTabs.COMPARTMENTS.description) == 0) { tModel = tableCompartmentsmodel;	}
		else if(descr.compareTo(Constants.TitlesTabs.EVENTS.description) == 0) { tModel = tableEventsmodel;	}
		else if(descr.compareTo(Constants.TitlesTabs.FUNCTIONS.description) == 0) { tModel = tableFunctionsmodel;	}
		else if(descr.compareTo(Constants.TitlesTabs.GLOBALQ.description) == 0) { tModel = tableGlobalQmodel;	}
		
		ret = (tModel.getValueAt(foundElement.getRow(), foundElement.getCol())).toString();
		return ret;
		
	}
	
	public static String getRowContent(FoundElement foundElement) {
		String ret = new String();
		CustomTableModel_MSMB tModel = null;
		Vector cols = new Vector();
		String descr = foundElement.getTableDescription();
		     if(descr.compareTo(Constants.TitlesTabs.REACTIONS.description) == 0) { tModel = tableReactionmodel; cols = Constants.reactions_columns;	}
		else if(descr.compareTo(Constants.TitlesTabs.SPECIES.description) == 0) { tModel = tableSpeciesmodel;cols = Constants.species_columns;	}
		else if(descr.compareTo(Constants.TitlesTabs.COMPARTMENTS.description) == 0) { tModel = tableCompartmentsmodel;	cols = Constants.compartments_columns;}
		else if(descr.compareTo(Constants.TitlesTabs.EVENTS.description) == 0) { tModel = tableEventsmodel;cols = Constants.events_columns;	}
		else if(descr.compareTo(Constants.TitlesTabs.FUNCTIONS.description) == 0) { tModel = tableFunctionsmodel;	cols = Constants.functions_columns;}
		else if(descr.compareTo(Constants.TitlesTabs.GLOBALQ.description) == 0) { tModel = tableGlobalQmodel;	cols = Constants.globalQ_columns;}
		int row = foundElement.getRow();
		for(int i = 0; i < cols.size(); i++) {
			  if(descr.compareTo(Constants.TitlesTabs.REACTIONS.description) == 0) {	ret += tModel.getValueAt(row, Constants.ReactionsColumns.getIndex((String) cols.get(i))).toString();}
		  else if(descr.compareTo(Constants.TitlesTabs.SPECIES.description) == 0) { 	ret += tModel.getValueAt(row, Constants.SpeciesColumns.getIndex((String) cols.get(i))).toString();	}
		  else if(descr.compareTo(Constants.TitlesTabs.COMPARTMENTS.description) == 0) {ret += tModel.getValueAt(row, Constants.CompartmentsColumns.getIndex((String) cols.get(i))).toString(); }
		  else if(descr.compareTo(Constants.TitlesTabs.EVENTS.description) == 0) { 		ret += tModel.getValueAt(row, Constants.EventsColumns.getIndex((String) cols.get(i))).toString();}
		  else if(descr.compareTo(Constants.TitlesTabs.FUNCTIONS.description) == 0) {	ret += tModel.getValueAt(row, Constants.FunctionsColumns.getIndex((String) cols.get(i))).toString(); }
		  else if(descr.compareTo(Constants.TitlesTabs.GLOBALQ.description) == 0) {		ret += tModel.getValueAt(row, Constants.GlobalQColumns.getIndex((String) cols.get(i))).toString();}
			  ret +="|";
		}
		ret = ret.substring(0, ret.length()-1);
		return ret;
	}
	
	public static CustomTableModel_MSMB getTableModelFromDescription(String descr) {
		CustomTableModel_MSMB tModel;
		if(descr==null) return null;
		if(descr.compareTo(Constants.TitlesTabs.REACTIONS.description) == 0) { tModel = tableReactionmodel;	}
		else if(descr.compareTo(Constants.TitlesTabs.SPECIES.description) == 0) { tModel = tableSpeciesmodel;	}
		else if(descr.compareTo(Constants.TitlesTabs.COMPARTMENTS.description) == 0) { tModel = tableCompartmentsmodel;	}
		else if(descr.compareTo(Constants.TitlesTabs.EVENTS.description) == 0) { tModel = tableEventsmodel;	}
		else if(descr.compareTo(Constants.TitlesTabs.FUNCTIONS.description) == 0) { tModel = tableFunctionsmodel;	}
		else if(descr.compareTo(Constants.TitlesTabs.GLOBALQ.description) == 0) { tModel = tableGlobalQmodel;	}
		else tModel = null;
		return tModel;
	}
	
	public static CustomJTable_MSMB getTableFromDescription(String descr) {
		CustomJTable_MSMB table;
		if(descr.compareTo(Constants.TitlesTabs.REACTIONS.description) == 0) { table = jTableReactions;	}
		else if(descr.compareTo(Constants.TitlesTabs.SPECIES.description) == 0) { table = jTableSpecies;}
		else if(descr.compareTo(Constants.TitlesTabs.COMPARTMENTS.description) == 0) { table = jTableCompartments;	}
		else if(descr.compareTo(Constants.TitlesTabs.EVENTS.description) == 0) { table = jTableEvents;	}
		else if(descr.compareTo(Constants.TitlesTabs.FUNCTIONS.description) == 0) { table = jTableFunctions;	}
		else if(descr.compareTo(Constants.TitlesTabs.GLOBALQ.description) == 0) { table = jTableGlobalQ;	}
		else table = null;
		return table;
	}

	/*public static CustomTableModel_MSMB getTableModelFromDescription(String descr) {
		CustomTableModel_MSMB tModel;
		if(descr.compareTo(Constants.TitlesTabs.REACTIONS.description) == 0) { tModel = tableReactionmodel;	}
		else if(descr.compareTo(Constants.TitlesTabs.SPECIES.description) == 0) { tModel = tableSpeciesmodel;	}
		else if(descr.compareTo(Constants.TitlesTabs.COMPARTMENTS.description) == 0) { tModel = tableCompartmentsmodel;	}
		else if(descr.compareTo(Constants.TitlesTabs.EVENTS.description) == 0) { tModel = tableEventsmodel;	}
		else if(descr.compareTo(Constants.TitlesTabs.FUNCTIONS.description) == 0) { tModel = tableFunctionsmodel;	}
		else if(descr.compareTo(Constants.TitlesTabs.GLOBALQ.description) == 0) { tModel = tableGlobalQmodel;	}
		else tModel = null;
		return tModel;
	}*/
	
	public static void applyRenaming(Vector collectedRenamingElements) {
		int original_focus = jTabGeneral.getSelectedIndex();
		for(int i = 0; i < collectedRenamingElements.size(); i++ ) {
			Vector el = (Vector) collectedRenamingElements.get(i);
			String newString = (String) el.get(0);
			FoundElement where = (FoundElement) el.get(1);
			CustomTableModel_MSMB tModel = getTableModelFromDescription(where.getTableDescription());
			if(tModel != null) {
				jTabGeneral.setSelectedIndex(Constants.TitlesTabs.getIndexFromDescription(where.getTableDescription()));
				tModel.setValueAt(newString, where.getRow(), where.getCol());
			} else {
				jTabGeneral.setSelectedIndex(Constants.TitlesTabs.SPECIES.index);
				tableSpeciesmodel.setValueAt(newString, where.getRow(), where.getCol());
			}
			
		}
		jTabGeneral.setSelectedIndex(original_focus);
		revalidateAllTables();
		
	}
	
	
	
	
	public static void undoRenaming(String inTable, int row, String toOriginal) {
		int original_focus = jTabGeneral.getSelectedIndex();
		CustomTableModel_MSMB tModel = getTableModelFromDescription(inTable);
		jTabGeneral.setSelectedIndex(Constants.TitlesTabs.getIndexFromDescription(inTable));
		int col = -1;
		if(inTable.compareTo(Constants.TitlesTabs.SPECIES.description)==0) {	col = Constants.SpeciesColumns.NAME.index;	}
		else if(inTable.compareTo(Constants.TitlesTabs.COMPARTMENTS.description)==0) {	col = Constants.CompartmentsColumns.NAME.index;		}
		else if(inTable.compareTo(Constants.TitlesTabs.EVENTS.description)==0) {	col = Constants.EventsColumns.NAME.index;	}
		else if(inTable.compareTo(Constants.TitlesTabs.GLOBALQ.description)==0) {	col = Constants.GlobalQColumns.NAME.index;		}
		else if(inTable.compareTo(Constants.TitlesTabs.REACTIONS.description)==0) { col = Constants.ReactionsColumns.NAME.index;		}
		if(col==-1) return;
		tModel.setValueAt(toOriginal, row, col);
		jTabGeneral.setSelectedIndex(original_focus);
		revalidateAllTables();
	}

	
	private static Vector<DebugMessage> getAllDebugMessagesDefaults() {
		Vector ret = new Vector<DebugMessage>();
		Iterator it = debugMessages.keySet().iterator();
		while(it.hasNext()){
			String key = (String) it.next();
			DebugMessage dm = (DebugMessage)(debugMessages.get(key));
	    	if(dm.getPriority() ==DebugConstants.PriorityType.DEFAULTS.priorityCode) {
	    		ret.add(dm);
	    	}
		}
		return ret;
	}
	
	private static void addAllDebugMessagesMessages(Vector<DebugMessage> m) {
		Iterator it = m.iterator();
		while(it.hasNext()){
			DebugMessage dm = (DebugMessage) it.next();
	    	addDebugMessage_ifNotPresent(dm);
		}
	}
	
	public static void revalidateExpressions(Vector collectedElements) {
		if(!validationsOn) return;
		boolean old = autocompleteWithDefaults;
		MainGui.autocompleteWithDefaults = false;
		
		int original_focus = jTabGeneral.getSelectedIndex();
		Vector<DebugMessage> def = getAllDebugMessagesDefaults();
		for(int i = 0; i < collectedElements.size(); i++ ) {
			FoundElement where = (FoundElement) collectedElements.get(i);
			
			//because I don't want to replace local name of variables... CHEEEEEEEEEEEECK if that is in conflict with actual renames of local variables or the rename of a function 
			if(where.getTableDescription().compareTo(Constants.TitlesTabs.FUNCTIONS.description)==0 && 
					where.getCol() == Constants.FunctionsColumns.NAME.index) continue; 
			
			CustomTableModel_MSMB tModel = getTableModelFromDescription(where.getTableDescription());
			if(tModel!=null) {
				jTabGeneral.setSelectedIndex(Constants.TitlesTabs.getIndexFromDescription(where.getTableDescription()));
				tModel.setValueAt(tModel.getValueAt(where.getRow(), where.getCol()), where.getRow(), where.getCol()); //reassign same value but cause an update and a revalidation
			} else {
				jTabGeneral.setSelectedIndex(Constants.TitlesTabs.SPECIES.index);
				tableSpeciesmodel.setValueAt(tableSpeciesmodel.getValueAt(where.getRow(), where.getCol()), where.getRow(), where.getCol()); //reassign same value but cause an update and a revalidation
			
			}
		}
		jTabGeneral.setSelectedIndex(original_focus);
		revalidateAllTables();
		addAllDebugMessagesMessages(def);
		MainGui.autocompleteWithDefaults = old;
		
	}

	public static void revalidateAllTables() {
	  jTableCompartments.revalidate();
	  jTableFunctions.revalidate();
	  jTableGlobalQ.revalidate();
	  jTableSpecies.revalidate();
	  jTableReactions.revalidate();
	}

	public static void updateFunctionsView() throws Exception {
		/*if(tableFunctionsmodel== null) return;
		Collection<Function> toShow = null;
		if(MainGui.showAllAvailableFunctions) toShow = multiModel.funDB.getAllFunctions(); 
		else toShow = multiModel.funDB.getUsedFunctions();

		tableFunctionsmodel.clearData();
		tableFunctionsmodel.enableAllCells();
		listModel_FunctionToCompact.clear();

		tableFunctionsmodel.removeAddEmptyRow_Listener();
		Iterator it = toShow.iterator();
		
		int row = 0;
		while(it.hasNext()){
			Function f = (Function) it.next();
			if(!listModel_FunctionToCompact.contains(f.getName())) listModel_FunctionToCompact.addElement(f.getName());
			tableFunctionsmodel.setValueAt_withoutUpdate(f.printCompleteSignature(), row, Constants.FunctionsColumns.NAME.index);
			tableFunctionsmodel.setValueAt_withoutUpdate(f.getExpandedEquation(new Vector()), row, Constants.FunctionsColumns.EQUATION.index);
			if(f.getType() == CFunction.PreDefined) {
				tableFunctionsmodel.disableCell(row, Constants.FunctionsColumns.NAME.index);
				tableFunctionsmodel.disableCell(row, Constants.FunctionsColumns.EQUATION.index);
				tableFunctionsmodel.disableCell(row, Constants.FunctionsColumns.PARAMETER_ROLES.index);
			} else{
				tableFunctionsmodel.enableCell(row, Constants.FunctionsColumns.NAME.index);
				tableFunctionsmodel.enableCell(row, Constants.FunctionsColumns.EQUATION.index);
				tableFunctionsmodel.enableCell(row, Constants.FunctionsColumns.PARAMETER_ROLES.index);
			}
			row++;
		}
		tableFunctionsmodel.addAddEmptyRow_Listener(); 
		tableFunctionsmodel.fireTableDataChanged();
		jTableFunctions.revalidate(); 
		jListFunctionToCompact.revalidate();
		
		return; */

	}

	public static boolean isCellWithMultipleView(String tableName, int row, int col) {
		CustomJTable_MSMB table = null;
		if(tableName.compareTo(Constants.TitlesTabs.SPECIES.description)==0) {			table = jTableSpecies;	}
		else if(tableName.compareTo(Constants.TitlesTabs.COMPARTMENTS.description)==0) {	table = jTableCompartments;		}
		else if(tableName.compareTo(Constants.TitlesTabs.EVENTS.description)==0) {		table = jTableEvents;	}
		else if(tableName.compareTo(Constants.TitlesTabs.GLOBALQ.description)==0) {		table = jTableGlobalQ;	}
		else if(tableName.compareTo(Constants.TitlesTabs.REACTIONS.description)==0) {		table = jTableReactions;	}
		else return false;
		table.setRowSelectionInterval(row, row);
		
		table.revalidate();

		
		if(col==0) return false;
		if(tableName.compareTo(Constants.TitlesTabs.REACTIONS.description)==0 && col==Constants.ReactionsColumns.KINETIC_LAW.index) return true;
		
		else if(tableName.compareTo(Constants.TitlesTabs.SPECIES.description)==0) {
			if(jTableSpecies.isCellWithError(row, col)) return false;
			if(col==Constants.SpeciesColumns.INITIAL_QUANTITY.index) return true;
			else if(col==Constants.SpeciesColumns.EXPRESSION.index) return true;
		}
			
		return false;
	}

	public static void setView(int viewIndex, String table, int row, int column) throws MySyntaxException {
		String viewString = new String();
		
		if(viewIndex == Constants.Views.CURRENT_AS_EDITABLE.index) {
			setCurrentAsEditable(table,row,column);
			viewIndex = Constants.Views.EDITABLE.index;
		}
		if(viewIndex == Constants.Views.EXPANDED.index ||  viewIndex == Constants.Views.EXPANDED_ALL.index 
			|| viewIndex == Constants.Views.COMPRESSED.index || viewIndex == Constants.Views.EDITABLE.index) {
			 viewString = getViewIn(table,row,column,viewIndex);
		 }
		
		 else if(viewIndex == Constants.Views.CUSTOM.index)  {
			 CustomViewDialog viewDialog = new CustomViewDialog(multiModel);
			 String editable = new String();
			 try{
				 editable = getViewIn(table,row,column,Constants.Views.EDITABLE.index);
				 viewDialog.setCompressedExpression(editable);
				 viewDialog.setVisible(true);
				 viewString = viewDialog.getReturnString();
				 viewDialog.dispose();
			 }catch (Exception e) {//no function to expand
				 //e.printStackTrace();
				 viewString = editable;
				 viewIndex = Constants.Views.EDITABLE.index;
			}
			 //String newName = viewDialog.getReturnString();
			 //POPUP FRAME TO SELECT THE FUNCTIONS THAT THE USER WANT TO SEE COMPRESSED... AND THE STRING IS GOING TO BE WHAT IT RETURNS
			// viewString = getViewIn(table,row,column,viewIndex);
		 }
		
		String editableView = getViewIn(table,row,column,Constants.Views.EDITABLE.index);
		boolean isEqual = checkIfEqualToEditable(viewString,editableView);
		
		
		if(table.compareTo(Constants.TitlesTabs.REACTIONS.description)==0) {
			if(!isEqual){
				tableReactionmodel.disableCell(row, column);
				tableReactionmodel.setValueAt(viewString, row, column);
			} else {
				tableReactionmodel.enableCell(row, column);
				tableReactionmodel.setValueAt(editableView, row, column);
			}
			
			jTableReactions.revalidate();
		} else if(table.compareTo(Constants.TitlesTabs.SPECIES.description)==0) {
			//if(viewIndex != Constants.Views.EDITABLE.index && viewIndex != Constants.Views.COMPRESSED.index) {
			
			if(!isEqual){
				tableSpeciesmodel.disableCell(row, column);
				tableSpeciesmodel.setValueAt(viewString, row, column);
			} else {
				tableSpeciesmodel.enableCell(row, column);
				tableSpeciesmodel.setValueAt(editableView, row, column);
			}
			
			jTableSpecies.revalidate();
			
			
		} else if(table.compareTo(Constants.TitlesTabs.GLOBALQ.description)==0) {
			tableGlobalQmodel.setValueAt(viewString, row, column);
			jTableGlobalQ.revalidate();
			
		}else if(table.compareTo(Constants.TitlesTabs.COMPARTMENTS.description)==0) {
			tableCompartmentsmodel.setValueAt(viewString, row, column);
			jTableCompartments.revalidate();
			
			
		} else if(table.compareTo(Constants.TitlesTabs.FUNCTIONS.description)==0) {
			tableFunctionsmodel.setValueAt(viewString, row, column);
			jTableFunctions.revalidate();
			
		} else if(table.compareTo(Constants.TitlesTabs.EVENTS.description)==0) {
			tableEventsmodel.setValueAt(viewString, row, column);
			jTableEvents.revalidate();
		}
		
		
		
	}
	
	private static boolean checkIfEqualToEditable(String viewToCheck,String editableView) {
		jListFunctionToCompact.setSelectionInterval(0, listModel_FunctionToCompact.size()-1);
		
		String editableFullBrackets = CellParsers.reprintExpression_brackets(editableView, true);
		String viewToCheckFullBrackets = CellParsers.reprintExpression_brackets(viewToCheck, true);
		
		if(editableFullBrackets.compareTo(viewToCheckFullBrackets)==0) {
			return true;
		} else {
			return false;
		}
	}

	private static void setCurrentAsEditable(String table, int row, int column) throws MySyntaxException {
		String editableString = new String();
		if(table.compareTo(Constants.TitlesTabs.REACTIONS.description)==0) {		
			editableString = (String) tableReactionmodel.getValueAt(row, column);
		} 
		else if(table.compareTo(Constants.TitlesTabs.SPECIES.description)==0) {		
			editableString =(String) tableSpeciesmodel.getValueAt(row, column);
			multiModel.setEditableExpression(editableString,row,column);
		}
		else if(table.compareTo(Constants.TitlesTabs.GLOBALQ.description)==0) { 	
			editableString = (String) tableGlobalQmodel.getValueAt(row, column);
			
		}
		else if(table.compareTo(Constants.TitlesTabs.COMPARTMENTS.description)==0) {
			editableString = (String) tableCompartmentsmodel.getValueAt(row, column);
			
		}
		else if(table.compareTo(Constants.TitlesTabs.FUNCTIONS.description)==0) { 	
			editableString = (String) tableFunctionsmodel.getValueAt(row, column);
			
		}
		else if(table.compareTo(Constants.TitlesTabs.EVENTS.description)==0) { 		
			editableString = (String) tableEventsmodel.getValueAt(row, column);
			
		}
	
		
	}

	public static String getViewIn(String table, int row, int column,int viewIndex) {
	
		String editableString = new String();
		editableString = multiModel.getEditableExpression(table, row, column);
		
		/*if(table.compareTo(Constants.TitlesTabs.REACTIONS.description)==0) { editableString = (String) tableReactionmodel.getValueAt(row, column);} 
		else if(table.compareTo(Constants.TitlesTabs.SPECIES.description)==0) {		editableString = multiModel.getEditableExpression(row, column);}
		else if(table.compareTo(Constants.TitlesTabs.GLOBALQ.description)==0) { 	editableString = (String) tableGlobalQmodel.getValueAt(row, column);}
		else if(table.compareTo(Constants.TitlesTabs.COMPARTMENTS.description)==0) {editableString = (String) tableCompartmentsmodel.getValueAt(row, column);}
		else if(table.compareTo(Constants.TitlesTabs.FUNCTIONS.description)==0) { 	editableString = (String) tableFunctionsmodel.getValueAt(row, column);}
		else if(table.compareTo(Constants.TitlesTabs.EVENTS.description)==0) { 		editableString = (String) tableEventsmodel.getValueAt(row, column);}
		*/
		/*if(viewIndex==Constants.Views.EDITABLE.index || viewIndex == Constants.Views.COMPRESSED.index) {
			jListFunctionToCompact.setSelectionInterval(0, listModel_FunctionToCompact.size()-1);
		} else*/ 
		String ret = editableString;
		
		if (viewIndex == Constants.Views.EXPANDED.index || viewIndex == Constants.Views.EXPANDED_ALL.index) {
			jListFunctionToCompact.clearSelection();
			boolean all = false;
			if(viewIndex == Constants.Views.EXPANDED_ALL.index) {
				all = true;
			}
			String prec = new String(editableString);
			do {
				try{
					InputStream is = new ByteArrayInputStream(prec.getBytes("UTF-8"));
					MR_Expression_Parser parser = new MR_Expression_Parser(is,"UTF-8");
					CompleteExpression root = parser.CompleteExpression();
					ExpressionVisitor vis = new ExpressionVisitor(jListFunctionToCompact.getSelectedValuesList(),multiModel,all);
					root.accept(vis);
					if(vis.getExceptions().size() == 0) {
						ret  = vis.getExpression();
						ret = CellParsers.reprintExpression_brackets(ret, MainGui.FULL_BRACKET_EXPRESSION);
					} else {
						throw vis.getExceptions().get(0);
					}
					
				}catch (Exception e) {
					if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES)
						e.printStackTrace();
				}
				if(prec.compareTo(ret)==0) break;
				prec = ret;
			} while(true);
			
			
		}
		return ret;
	}

	public static void updateDefaultValue(String description, int indexColumn ,String species_defaultInitialValue2) {
		if(multiModel == null) return;
		CustomTableModel_MSMB mod = null;
		CustomJTable_MSMB table = null;
		
		int tabToSelect = -1;
				
		int selectedTab = jTabGeneral.getSelectedIndex();
		addedByReaction = true;
		
		if(description.compareTo(Constants.TitlesTabs.SPECIES.description)==0) {	
			mod = tableSpeciesmodel; 
			table = jTableSpecies;
			tabToSelect = Constants.TitlesTabs.SPECIES.index;
		}
		else if(description.compareTo(Constants.TitlesTabs.COMPARTMENTS.description)==0) {	
			mod = tableCompartmentsmodel; 
			table = jTableCompartments;	
			tabToSelect = Constants.TitlesTabs.COMPARTMENTS.index;
		}
		else if(description.compareTo(Constants.TitlesTabs.EVENTS.description)==0) {	
			mod = tableEventsmodel;	 
			table = jTableEvents;
			tabToSelect = Constants.TitlesTabs.EVENTS.index;
		}
		else if(description.compareTo(Constants.TitlesTabs.GLOBALQ.description)==0) {	
			mod = tableGlobalQmodel; 
			table = jTableGlobalQ;
			tabToSelect = Constants.TitlesTabs.GLOBALQ.index;
		}
		else if(description.compareTo(Constants.TitlesTabs.REACTIONS.description)==0) {	
			mod = tableReactionmodel; 
			table = jTableReactions;
			tabToSelect = Constants.TitlesTabs.REACTIONS.index;
		}
		
		if(mod!= null) {
			jTabGeneral.setSelectedIndex(tabToSelect);
			table.setColumnSelectionInterval(indexColumn, indexColumn);
	     
			//TableColumn nameColumn = table.getColumnModel().getColumn(indexColumn);
	        //TableCellRenderer cellRender = nameColumn.getCellRenderer();
	     	//if(cellRender instanceof EditableCellRenderer_MSMB) {
	     		for(int row = 0; row < mod.getRowCount(); row++) {
	     			if(table.isCellWithDefaults(row,indexColumn))	 mod.setValueAt_withoutUpdate(species_defaultInitialValue2, row, indexColumn);
	     		}
	     	//}
	     	
			
	     	table.revalidate();
			
	    	jTabGeneral.setSelectedIndex(selectedTab);
		}
		
	}
	
	
	public static boolean isCellWithDefaultValue(String description, int row, int col) {
		CustomJTable_MSMB table = null;
		if(description.compareTo(Constants.TitlesTabs.SPECIES.description)==0) {			table = jTableSpecies;	}
		else if(description.compareTo(Constants.TitlesTabs.COMPARTMENTS.description)==0) {	table = jTableCompartments;		}
		else if(description.compareTo(Constants.TitlesTabs.EVENTS.description)==0) {		table = jTableEvents;	}
		else if(description.compareTo(Constants.TitlesTabs.GLOBALQ.description)==0) {		table = jTableGlobalQ;	}
		else if(description.compareTo(Constants.TitlesTabs.REACTIONS.description)==0) {		table = jTableReactions;	}
		else return false;
		
		table.setRowSelectionInterval(row, row);
		if(table.isCellWithError(row,col)) return false;
		return table.isCellWithDefaults(row,col);
		
     	//return false;
	}

	static HashMap<String,Integer> indexToDelete = new HashMap<String,Integer>();

	protected static boolean copiedSignature = false;
	public static int delayAutocompletion = Integer.MAX_VALUE;
	public static int autocompletionContext = -1;



	
	
	public static void applyDeleteActions(TreeTableNode root) {
		
				   
		Vector<FoundElementToDelete> elementToBeDeleted = extractElementsToBeDeleted(root); // devono essere in ordine di riga!
		int previousSelectedIndex = jTabGeneral.getSelectedIndex();
		Vector<FoundElementToDelete> otherActions = new Vector<FoundElementToDelete>();
		Vector<FoundElement> inconsistent = new Vector<FoundElement>();
			try {
			MainGui.donotCleanDebugMessages = true;
			
			for(int i = 0; i < elementToBeDeleted.size(); i++) {
					FoundElementToDelete element = elementToBeDeleted.get(i);
					int whichTab = Constants.TitlesTabs.getIndexFromDescription(element.getTableDescription());
					jTabGeneral.setSelectedIndex(whichTab);
					MainGui.row_to_highlight.set(whichTab, -1);
					Integer numberOfElementsAlreadyDeletedFromThisTable = indexToDelete.get(element.getTableDescription());
					if(numberOfElementsAlreadyDeletedFromThisTable == null) numberOfElementsAlreadyDeletedFromThisTable = new Integer(0);
					int currentIndexToDelete = element.getRow()-(numberOfElementsAlreadyDeletedFromThisTable);
					numberOfElementsAlreadyDeletedFromThisTable++;
					
					if(element.getActionToTake().compareTo(Constants.DeleteActions.DELETE.description)!= 0) {
						if(element.getActionToTake().compareTo(Constants.DeleteActions.INCONSISTENT.description)==0) {
							inconsistent.add(new FoundElement(element.getTableDescription(),currentIndexToDelete, element.getCol()));
						}
						else {
							FoundElementToDelete movedUp = new FoundElementToDelete(element);
							movedUp.setRow(currentIndexToDelete);
							otherActions.add(movedUp);
						}
						continue;
					}
				
					clear_debugMessages_relatedWith_row(currentIndexToDelete+1);
					decolorRow(currentIndexToDelete, element.getTableDescription());
					
					
 					moveUpRow_references_debugMessages(currentIndexToDelete+1);
 				
					switch(whichTab) {
						case 0:  multiModel.removeReaction(currentIndexToDelete);
								jTableReactions.revalidate();
								break;
						case 1: multiModel.removeSpecies(currentIndexToDelete); updateSpeciesTableFromMultiModel(); 
								break;
						case 2: multiModel.removeGlobalQ(currentIndexToDelete); updateGlobalQTableFromMultiModel();
								break;
						case 3: String name = FunctionsDB.extractJustName(multiModel.funDB.getUserDefinedFunctionByIndex(currentIndexToDelete+1).getName());
								multiModel.removeFunction(currentIndexToDelete); 
								updateFunctionsTableFromMultiModel();
								Vector<FoundElement> found = search(name);
								for(int j = 0; j < found.size(); j++) {
									DebugMessage dm = new DebugMessage();
									dm.setOrigin_table(found.get(j).getTableDescription());
									dm.setOrigin_col(found.get(j).getCol());
									dm.setOrigin_row(found.get(j).getRow()+1);
									dm.setProblem("Problem parsing element.\nFunction "+name+" is undefined.");
									dm.setPriority(DebugConstants.PriorityType.PARSING.priorityCode);
									MainGui.addDebugMessage_ifNotPresent(dm);
								}
								recolorCellsWithErrors();
								
								break;
						case 4: multiModel.removeEvent(currentIndexToDelete); updateEventTableFromMultiModel();
								break;
						case 5: multiModel.removeComp(currentIndexToDelete); updateCompartmentsTableFromMultiModel();
								break;
						default: System.out.println("Error?"); break;
					}
					indexToDelete.put(element.getTableDescription(),numberOfElementsAlreadyDeletedFromThisTable);
					
			}
			
			
			for(int i = 0; i < otherActions.size(); i++) {
				FoundElementToDelete element =  otherActions.get(i);
				if(element.getActionToTake().compareTo(Constants.DeleteActions.ASSIGN_NEW_VALUE.description) ==0) {
					applyDeleteAction_newValue(element);
					inconsistent.add(element);
				}
				
			}
			
			revalidateExpressions(inconsistent);
			
		} catch (Exception e) {
			//if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES)
			e.printStackTrace();
		} finally {
			MainGui.donotCleanDebugMessages = false;
			jTabGeneral.setSelectedIndex(1);
			jTabGeneral.setSelectedIndex(2);
			jTabGeneral.setSelectedIndex(previousSelectedIndex);
			
		}
		
	
		
	}

	private static void applyDeleteAction_newValue(FoundElementToDelete element) {
		int selectedTab = jTabGeneral.getSelectedIndex();
		addedByReaction = true;
		String description = element.getTableDescription();
		
		CustomTableModel_MSMB mod = null;
		CustomJTable_MSMB table = null;
		int tabToSelect = 0;
		
		if(description.compareTo(Constants.TitlesTabs.SPECIES.description)==0) {	
			mod = tableSpeciesmodel; 
			table = jTableSpecies;
			tabToSelect = Constants.TitlesTabs.SPECIES.index;
		}
		else if(description.compareTo(Constants.TitlesTabs.COMPARTMENTS.description)==0) {	
			mod = tableCompartmentsmodel; 
			table = jTableCompartments;	
			tabToSelect = Constants.TitlesTabs.COMPARTMENTS.index;
		}
		else if(description.compareTo(Constants.TitlesTabs.EVENTS.description)==0) {	
			mod = tableEventsmodel;	 
			table = jTableEvents;
			tabToSelect = Constants.TitlesTabs.EVENTS.index;
		}
		else if(description.compareTo(Constants.TitlesTabs.GLOBALQ.description)==0) {	
			mod = tableGlobalQmodel; 
			table = jTableGlobalQ;
			tabToSelect = Constants.TitlesTabs.GLOBALQ.index;
		}
		else if(description.compareTo(Constants.TitlesTabs.REACTIONS.description)==0) {	
			mod = tableReactionmodel; 
			table = jTableReactions;
			tabToSelect = Constants.TitlesTabs.REACTIONS.index;
		}
		
		if(mod!= null) {
			jTabGeneral.setSelectedIndex(tabToSelect);
			table.setColumnSelectionInterval(element.getCol(), element.getCol());
			//String newValue = RenamingFrame.replaceAllWords(mod.getValueAt(element.getRow(), element.getCol()).toString(),element.getOldValue(),element.getNewValue());
	     	String newValue2 = CellParsers.replaceVariableInExpression(mod.getValueAt(element.getRow(), element.getCol()).toString(),element.getOldValue(),element.getNewValue());
		 	mod.setValueAt_withoutUpdate(newValue2, element.getRow(), element.getCol());
	     	table.revalidate();
			jTabGeneral.setSelectedIndex(selectedTab);
		}
		
	}

	private static Vector<FoundElementToDelete> extractElementsToBeDeleted(TreeTableNode root) {
		Vector<FoundElementToDelete> ret = new Vector<FoundElementToDelete>();
		for(int i = 0; i < root.getChildCount(); i++) {
			DefaultMutableTreeTableNode child = (DefaultMutableTreeTableNode) root.getChildAt(i);
			ret.add((FoundElementToDelete) child.getUserObject());
			ret.addAll(extractElementsToBeDeleted(child));
		}
		
		Collections.sort(ret);
		
		return ret;
	}

	public static void clearCopasiFunctions() {
		clearCFunctionDB();
	}

	public HashMap<String, HashMap<String, String>> getMultistateInitials() {
		return multiModel.getMultistateInitials();
	}

	public String getAutosaveBaseName() {
		return this.inputFile ;
	}

	public void setFullBracketExpression(boolean fullBracketExpressions) {
		FULL_BRACKET_EXPRESSION = fullBracketExpressions;
	}

	public static Vector<Vector<String>> getSubstratesSelectedReaction() {
		
		Reaction r = multiModel.getReaction(MainGui.cellSelectedRow+1);
		Vector<Vector<String>> ret = new Vector<Vector<String>>();
		Vector<String> subs = r.getSubstrates(multiModel);
		for(int i = 0; i < subs.size(); i++) {
			Vector<String> element = new Vector<>();
			element.add(subs.get(i));
			element.add(null);
			ret.add(element);
		}
		return ret;
		
	}

	
	public static void updateAutocompletionContext(String current, int cursor) {
		  System.out.println("current string: " + current);
		  String newString = new String();
		  int paramNumber = 0; 
		  String type = null;
		  for(int i=cursor-1; i >=0; i--) {
			  //WRONG PROCESS IF INVOLVES NAMES WITH QUOTESSSSSSSSS AND , AND ( BETWEEN QUOTES
			  //I WILL FIX IT LATER
			  char character = current.charAt(i);
			  if(character!=',' && character!='(') {
				  character='a';
				  newString = character + newString;
			  } 
			  if(character==',') {
				  paramNumber++;
				  newString = character + newString;  
			  }
			  if(character=='(') {
				  String subString = current.substring(0,i);
				  subString += MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstantsNOQUOTES.MY_SPECIAL_EXTENSION);
				  InputStream is;
				try {
					is = new ByteArrayInputStream(subString.getBytes("UTF-8"));
				
				  MR_Expression_Parser parser = new MR_Expression_Parser(is,"UTF-8");
				  CompleteExpression root = parser.CompleteExpression();
				  GetElementBeforeSpecialExtension name = new GetElementBeforeSpecialExtension();
				  root.accept(name);
				  String funName = name.getElementName();
				  System.out.println("updateAutocompletionContext FUNCTION NAME: "+funName);
				  if(funName != null && funName.trim().length()>0) {
					  Function f = multiModel.getFunctionByName(funName);
					  System.out.println("updateAutocompletionContext paramNumber: "+paramNumber);
					  
					  if(f!= null) type = f.getParametersTypes().get(paramNumber);
					  
				  }
				} catch (Throwable e) {
					if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
				}
			  }
			  
		  }
		  
		  if(type!= null) {
			  MainGui.autocompletionContext = Constants.FunctionParamType.getCopasiTypeFromSignatureType(type);
		  } else {
			  MainGui.autocompletionContext = Constants.FunctionParamType.VARIABLE.copasiType;
		  }
		
		  System.out.println("updateAutocompletionContext autocompletionContext: "+Constants.FunctionParamType.getSignatureDescriptionFromCopasiType(autocompletionContext));
	}
	
	

	
	
}  


class CustomFileFilter extends javax.swing.filechooser.FileFilter {

	Vector extensions = new Vector();
	String description = new String();
	
	CustomFileFilter(Vector ext, String descr) {
		extensions.addAll(ext);
		description = descr;
	}
	
	public String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }

    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String ext = getExtension(f);
        if (ext != null) {
            if (extensions.contains(ext)) {
                    return true;
            } else {
                return false;
            }
        }

        return false;
    }

    public String getDescription() {
        return description;
    }
    
}


class ButtonRenderer extends JButton implements TableCellRenderer {
	 
	  ImageIcon icon = null;
	  
	  public ButtonRenderer(ImageIcon icon) {
		 if(icon != null) {
			 this.icon = icon;
		}
	    setOpaque(true);
	  }

	  public Component getTableCellRendererComponent(JTable table, Object value,
	      boolean isSelected, boolean hasFocus, int row, int column) {
	    if (isSelected) {
	      setForeground(table.getSelectionForeground());
	      setBackground(table.getSelectionBackground());
	    } else {
	      setForeground(table.getForeground());
	      setBackground(UIManager.getColor("Button.background"));
	    }
	    setIcon(icon);
	    setDisabledIcon(icon);
	    setPressedIcon(icon);
	    
	    return this;
	  }
}


class ButtonEditor_ExpandReactions extends DefaultCellEditor {
	  protected JButton button;
	  private boolean isPushed;
	  int row = -1;
	  static ImageIcon icon = null;
	  MainGui mainFrame = null;

	  public ButtonEditor_ExpandReactions(MainGui mainFrame) {
		  super(new JCheckBox());
		  this.mainFrame = mainFrame;
		  InputStream is = MainGui.class.getResourceAsStream("images/row.jpg");
		  try {
			icon = new ImageIcon( ImageIO.read(is));
		} catch (IOException e1) {
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e1.printStackTrace();
		}
		  button = new JButton();
		  button.setIcon(icon);
		  button.setDisabledIcon(icon);
		  button.setPressedIcon(icon);
		  button.setOpaque(true);
		  button.addActionListener(new ActionListener() {
				  public void actionPerformed(ActionEvent e) {
					  fireEditingStopped();
				  }
			  });
		 
	  }

	  public Component getTableCellEditorComponent(JTable table, Object value,
	      boolean isSelected, int row, int column) {
	    if (isSelected) {
	      button.setForeground(table.getSelectionForeground());
	      button.setBackground(table.getSelectionBackground());
	    } else {
	      button.setForeground(table.getForeground());
	      button.setBackground(table.getBackground());
	    }
	    this.row = row;
	    isPushed = true;
	    return button;
	  }
	  
	   public Object getCellEditorValue() {
	    if (isPushed) {
	    	MutablePair<String, String> pair = new MutablePair<String, String>();
			int nreac = 0;
			int steps = 20;
			String reactions = new String();
			String missing = new String();
			try {
				pair = MainGui.getExpandedMultistateReactions(row, 1, steps);
				reactions = pair.left;
				nreac = MainGui.getTotNumExpandedReactions(row);
				missing = pair.right;
				
			
	    	if(reactions.length() > 0) {
	    		//JOptionPane.showMessageDialog(mainFrame, reactions);
	    		ExpandedReactionsDialog frame = new ExpandedReactionsDialog();
	    		frame.setTotNumReact(nreac);
	    		frame.setShowedReactions(reactions, missing);
	    		frame.setLastReactionNumber(steps);
	    		frame.setNumberReactionShowed(steps);
	    		frame.setRow(row);
	    		frame.setVisible(true);
	    	}
			} catch (Exception e) {
				 isPushed = false;
				if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) 
					e.printStackTrace();
			}
	    }
	    isPushed = false;
	    return new String(button.getText());
	  }
	}




class ButtonEditor_ParameterRoles extends DefaultCellEditor {
	  protected JButton button;
	  int row = -1;

	  public ButtonEditor_ParameterRoles(ImageIcon icon, ActionListener action) {
		  super(new JCheckBox());
		
		  button = new JButton();
		  button.setIcon(icon);
		  button.setDisabledIcon(icon);
		  button.setPressedIcon(icon);
		  button.setOpaque(true);
		  button.addActionListener(action);
		 
	  }
	  
	  public ButtonEditor_ParameterRoles(ImageIcon icon, MouseListener action) {
		  super(new JCheckBox());
		
		  button = new JButton();
		  button.setIcon(icon);
		  button.setDisabledIcon(icon);
		  button.setPressedIcon(icon);
		  button.setOpaque(true);
		  button.addMouseListener(action);
		 
	  }

	  public Component getTableCellEditorComponent(JTable table, Object value,
	      boolean isSelected, int row, int column) {
	    if (isSelected) {
	      button.setForeground(table.getSelectionForeground());
	      button.setBackground(table.getSelectionBackground());
	    } else {
	      button.setForeground(table.getForeground());
	      button.setBackground(table.getBackground());
	    }
	    this.row = row;
	    //isPushed = true;
	    return button;
	  }
	  
	
	   protected void fireEditingStopped() {
		    return;
		  }

	}


class PrintAllTables implements Printable {
	int[] pageBreaks;  
	Vector<JTable> tables = new Vector<JTable>();
	
	public PrintAllTables(Vector tabs) {
		tables.addAll(tabs);
	}
	
	public int print(Graphics g, PageFormat pf, int pageIndex) throws PrinterException {

		Font font = new Font("Serif", Font.PLAIN, 10);
		FontMetrics metrics = g.getFontMetrics(font);
		int lineHeight = metrics.getHeight();

		if (pageBreaks == null) {
			int linesPerPage = (int)(pf.getImageableHeight()/lineHeight);
			int numBreaks = 1;
			pageBreaks = new int[numBreaks];
			for (int b=0; b<numBreaks; b++) {
				pageBreaks[b] = (b+1)*linesPerPage; 
			}
		}

		if (pageIndex > pageBreaks.length) {
			return NO_SUCH_PAGE;
		}

		tables.get(pageIndex).print(JTable.PrintMode.FIT_WIDTH);
		
		return PAGE_EXISTS;
	}

}

class TreeDebugMessages extends JPanel {
    TreeView treeView;
    JTextArea textArea;
    JList currentViewArea;
    DefaultListModel listModel;
    
    final static String newline = "\n";

    public TreeDebugMessages() {
    	 treeView = new TreeView();
    	 textArea = new JTextArea();
    	 listModel = new DefaultListModel();
    	 currentViewArea = new JList(listModel); //data has type Object[]
    	 
    	 currentViewArea.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    	 currentViewArea.setVisibleRowCount(-1);
    	 
    	 currentViewArea.addListSelectionListener(new ListSelectionListener() {
			@Override
			 public void valueChanged(ListSelectionEvent e) {
				JList lsm = (JList)e.getSource();

		       if (!lsm.isSelectionEmpty()) {
		            // Find out which indexes are selected.
		            int index = lsm.getMinSelectionIndex();
		            DebugMessage dm = (DebugMessage) listModel.get(index);
		            textArea.setText(dm.getCompleteDescription());
		            revalidate();
		                 
		        }
		      }
		});
    	 
    	 currentViewArea.addMouseListener(new MouseAdapter()
	        {
	            public void mouseClicked(MouseEvent e)
	            {
	                if (e.getComponent().isEnabled() && e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2)
	                {
	                	DebugMessage dm = (DebugMessage)currentViewArea.getSelectedValue();
	                	MainGui.highlightElement_relatedWith(dm);
	                } else {
	                	 if (e.getComponent().isEnabled() && e.getButton() == MouseEvent.BUTTON3){
	                		 int index = ((JList)(e.getComponent())).locationToIndex(new Point(e.getX(),e.getY()));
	                		 ((JList)(e.getComponent())).setSelectedIndex(index);
	                	 	 DebugMessage dm = (DebugMessage)currentViewArea.getSelectedValue();
	                	 	 if(dm.getPriority() != DebugConstants.PriorityType.DEFAULTS.priorityCode &&
	                	 			dm.getPriority() != DebugConstants.PriorityType.MINOR_EMPTY.priorityCode	 ) {
	                	 		 MainGui.ackMenuItem.setEnabled(false);
	                	 	 } else  {
	                	 		 MainGui.ackMenuItem.setEnabled(true);
	                	 		 MainGui.popupDebugMessageActions.show(e.getComponent(), e.getX(), e.getY());
	                	 		 MainGui.toBeAck_debugMessage = dm;
	                	 	 }
	                	}
	                }
	            }
	        });
	        
    	 
         treeView.setMinimumSize(new Dimension(200,190));
        
         textArea.setEditable(false);
         textArea.setWrapStyleWord(true);
         textArea.setFont(new Font("Arial", Font.PLAIN, 13));
         
         JScrollPane scrollPane = new JScrollPane(currentViewArea);
        
         scrollPane.setPreferredSize(new Dimension(150,150));
         currentViewArea.setPreferredSize(new Dimension(100,100));
         
         JScrollPane scrollPane2 = new JScrollPane(textArea);
         
    	JSplitPane jSplitPane = new JSplitPane();
    	JSplitPane jSplitPane_right = new JSplitPane();
		jSplitPane.setLeftComponent(treeView);
		jSplitPane.setRightComponent(jSplitPane_right);
		jSplitPane_right.setLeftComponent(scrollPane);
		jSplitPane_right.setDividerLocation(0.5);
		jSplitPane_right.setOneTouchExpandable(true);
		jSplitPane_right.setRightComponent(scrollPane2);
		jSplitPane_right.setOrientation(JSplitPane.VERTICAL_SPLIT);
		jSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		jSplitPane.setDividerLocation(0.65);
		jSplitPane.setOneTouchExpandable(true);

		this.setLayout(new BorderLayout());
		add(jSplitPane, BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
    }

    public void updateTreeView() {
    	treeView.updateTreeView();
    }

    class TreeView extends JScrollPane implements TreeExpansionListener {
        Dimension minSize = new Dimension(200, 200);
        JTree tree;
    
        public TreeView() {
            TreeNode rootNode = createNodes();
            tree = new JTree(rootNode);
            tree.addTreeExpansionListener(this);
            tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
            //Listen for when the selection changes.
            tree.addTreeSelectionListener(new TreeSelectionListener() {
				@Override
				public void valueChanged(TreeSelectionEvent arg0) {
					//Returns the last path element of the selection.
					DefaultMutableTreeNode node = (DefaultMutableTreeNode)  tree.getLastSelectedPathComponent();
					    if (node == null)	    return;

					    node.getUserObject();
					    //if (node.isLeaf()) {
					    //textArea.append("Selected: " + arg0.getPath().getLastPathComponent()+System.getProperty("line.separator"));
					    printSelectedDebugMessages((DefaultMutableTreeNode) arg0.getPath().getLastPathComponent());
				}

			});

            setViewportView(tree);
        }

        private TreeNode createNodes() {
            DefaultMutableTreeNode root;
           
            root = new DefaultMutableTreeNode("Messages");
         
            DefaultMutableTreeNode major = new DefaultMutableTreeNode(DebugConstants.PriorityType.MAJOR.description +" ("+ 
            														MainGui.getDebugMessages(DebugConstants.PriorityType.MAJOR.priorityCode).size() 
            														+")");
            root.add(major);
            
            DefaultMutableTreeNode pars = new DefaultMutableTreeNode(DebugConstants.PriorityType.PARSING.description +" ("+ 
					MainGui.getDebugMessages(DebugConstants.PriorityType.PARSING.priorityCode).size() 
					+")");
            major.add(pars);
            
            DefaultMutableTreeNode inc = new DefaultMutableTreeNode(DebugConstants.PriorityType.INCONSISTENCIES.description +" ("+ 
					MainGui.getDebugMessages(DebugConstants.PriorityType.INCONSISTENCIES.priorityCode).size() 
					+")");
            major.add(inc);
            
            DefaultMutableTreeNode miss = new DefaultMutableTreeNode(DebugConstants.PriorityType.MISSING.description +" ("+ 
					MainGui.getDebugMessages(DebugConstants.PriorityType.MISSING.priorityCode).size() 
					+")");
            major.add(miss);
            
            DefaultMutableTreeNode empty = new DefaultMutableTreeNode(DebugConstants.PriorityType.EMPTY.description +" ("+ 
					MainGui.getDebugMessages(DebugConstants.PriorityType.EMPTY.priorityCode).size() 
					+")");
            major.add(empty);
            
            DefaultMutableTreeNode def = new DefaultMutableTreeNode(DebugConstants.PriorityType.DEFAULTS.description +" ("+ 
					MainGui.getDebugMessages(DebugConstants.PriorityType.DEFAULTS.priorityCode).size() 
					+")");
            root.add(def);
           
            DefaultMutableTreeNode minor = new DefaultMutableTreeNode(DebugConstants.PriorityType.MINOR.description +" ("+ 
					MainGui.getDebugMessages(DebugConstants.PriorityType.MINOR.priorityCode).size() 
					+")");
            root.add(minor);
            

            DefaultMutableTreeNode min_importIssues = new DefaultMutableTreeNode(DebugConstants.PriorityType.MINOR_IMPORT_ISSUES.description +" ("+ 
					MainGui.getDebugMessages(DebugConstants.PriorityType.MINOR_IMPORT_ISSUES.priorityCode).size() 
					+")");
            minor.add(min_importIssues);
            
            DefaultMutableTreeNode min_missing = new DefaultMutableTreeNode(DebugConstants.PriorityType.MINOR_EMPTY.description +" ("+ 
					MainGui.getDebugMessages(DebugConstants.PriorityType.MINOR_EMPTY.priorityCode).size() 
					+")");
            minor.add(min_missing);
            
            
            
           /* DefaultMutableTreeNode sim = new DefaultMutableTreeNode(DebugConstants.PriorityType.SIMILARITY.description +" ("+ 
					MainGui.getDebugMessages(DebugConstants.PriorityType.SIMILARITY.priorityCode).size() 
					+")");
            minor.add(sim);
            */
            
            return root;
        }
    
        public Dimension getMinimumSize() {
            return minSize;
        }

        public Dimension getPreferredSize() {
            return minSize;
        }

        // Required by TreeExpansionListener interface.
        public void treeExpanded(TreeExpansionEvent e) {
           // saySomething("Tree-expanded event detected", e);
        }

        // Required by TreeExpansionListener interface.
        public void treeCollapsed(TreeExpansionEvent e) {
            //saySomething("Tree-collapsed event detected", e);
        }
        
        public void updateTreeView() {
        	textArea.setText("");
        	DefaultMutableTreeNode root = (DefaultMutableTreeNode)tree.getModel().getRoot();
            visitAllNodes(root);
        	DefaultMutableTreeNode sel = (DefaultMutableTreeNode)  tree.getLastSelectedPathComponent();
            if(sel != null)printSelectedDebugMessages(sel);
        }
        
        
        public void visitAllNodes(DefaultMutableTreeNode node) {
         Iterator it = MainGui.debugMessages.keySet().iterator();
         if(node.toString().indexOf("(") != -1) { //root has no number of messages and should not be processed
        	 String priorityDescr = node.toString().substring(0, node.toString().indexOf("(")-1);
        	 int count = 0;
        	 String partial_key_string = new Double(DebugConstants.PriorityType.getIndex(priorityDescr)).toString();
     		if(partial_key_string.endsWith(".0")) { partial_key_string = partial_key_string.substring(0, partial_key_string.indexOf(".0"));}
     		while(it.hasNext()) {
     			String key = (String) it.next();
     			if(key.contains("@"+partial_key_string+".")  || key.contains("@"+partial_key_string+"_")){
        	      			 count++; 
        		 } 
        	 }
        	 node.setUserObject(priorityDescr + " (" + count + ")");
         }
         if (node.getChildCount() >= 0) {
                for (Enumeration e=node.children(); e.hasMoreElements(); ) {
                	DefaultMutableTreeNode n = (DefaultMutableTreeNode)e.nextElement();
                    visitAllNodes(n);
                }
            }
        }
        
        
        private void printSelectedDebugMessages(DefaultMutableTreeNode selectedNode) {
        	Iterator it = MainGui.debugMessages.keySet().iterator();
        	listModel.clear();
            if(selectedNode.toString().indexOf("(") != -1) { //root has no number of messages and should not be processed
           	 String priorityDescr = selectedNode.toString().substring(0, selectedNode.toString().indexOf("(")-1);
           	 Vector<DebugMessage> sorted = new Vector<DebugMessage>();
           	 while(it.hasNext()) {
           		 String key = (String) it.next();
           		 if(key.contains("@"+DebugConstants.PriorityType.getIndex(priorityDescr)+"_")) {
           			sorted.add((DebugMessage)MainGui.debugMessages.get(key));
           			 
           		 } 
           	 }
           	 Collections.sort(sorted);
           	 for (DebugMessage x : sorted) {listModel.addElement(x);  
           	}
            currentViewArea.revalidate();
            textArea.setText("");
            /*MouseListener popupListener = new PopupListener();
            currentViewArea.addMouseListener(popupListener);*/
		}

    }
}
    
}

class PopupListener extends MouseAdapter {
    public void mousePressed(MouseEvent e) {
        maybeShowPopup(e);
    }

    public void mouseReleased(MouseEvent e) {
        maybeShowPopup(e);
    }

    private void maybeShowPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
            MainGui.popupDebugMessageActions.show(e.getComponent(),
                       e.getX(), e.getY());
        }
    }
}


class CustomDialogSignature extends JDialog
implements ActionListener,
PropertyChangeListener {
	private String typedText = null;
	private JTextField textField;

	private JOptionPane optionPane;

	private String btnString1 = "Copy to Clipboard";
	private String btnString2 = "Back";

	/** Creates the reusable dialog. */
	public CustomDialogSignature(Frame aFrame, String aWord) {
		super(aFrame, true);
	
		setTitle("Function signature");

		textField = new JTextField();
		textField.setEditable(false);
		textField.setEnabled(true);
		textField.setText(aWord);
		
		Object[] array = {textField};

		//Create an array specifying the number of dialog buttons
		//and their text.
		Object[] options = {btnString1, btnString2};

		//Create the JOptionPane.
		optionPane = new JOptionPane(array,
				JOptionPane.INFORMATION_MESSAGE,
				JOptionPane.YES_NO_OPTION,
				null,
				options,
				options[0]);

		//Make this dialog display it.
		setContentPane(optionPane);

		//Handle window closing correctly.
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				/*
				 * Instead of directly closing the window,
				 * we're going to change the JOptionPane's
				 * value property.
				 */
				optionPane.setValue(new Integer(
						JOptionPane.CLOSED_OPTION));
			}
		});

		//Ensure the text field always gets the first focus.
		addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent ce) {
				textField.requestFocusInWindow();
			}
		});

		//Register an event handler that puts the text into the option pane.
		textField.addActionListener(this);

		//Register an event handler that reacts to option pane state changes.
		optionPane.addPropertyChangeListener(this);
	}

	/** This method handles events for the text field. */
	public void actionPerformed(ActionEvent e) {
		optionPane.setValue(btnString1);
	}

	/** This method reacts to state changes in the option pane. */
	public void propertyChange(PropertyChangeEvent e) {
		String prop = e.getPropertyName();

		if (isVisible()
				&& (e.getSource() == optionPane)
				&& (JOptionPane.VALUE_PROPERTY.equals(prop) ||
						JOptionPane.INPUT_VALUE_PROPERTY.equals(prop))) {
			Object value = optionPane.getValue();

			if (value == JOptionPane.UNINITIALIZED_VALUE) {
				//ignore reset
				return;
			}

			//Reset the JOptionPane's value.
			//If you don't do this, then if the user
			//presses the same button next time, no
			//property change event will be fired.
			optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);

			if (btnString1.equals(value)) {
				typedText = textField.getText();
				Clipboard clipboard = getToolkit().getSystemClipboard();
				clipboard.setContents(new StringSelection(typedText), null);
				clearAndHide();
			} else { //user closed dialog or clicked cancel
				typedText = null;
				clearAndHide();
			}
		}
	}

	/** This method clears the dialog and hides it. */
	public void clearAndHide() {
		textField.setText(null);
		setVisible(false);
	}
}