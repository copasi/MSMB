package  msmb.gui;

import java.io.*;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import org.apache.commons.lang3.tuple.MutablePair;

import msmb.debugTab.DebugMessage;

import msmb.model.Function;
import msmb.model.MultistateSpecies;
import msmb.model.Species;
import msmb.utility.CellParsers;
import msmb.utility.Constants;

class ExportMultistateFormat {

	private static File file = null;
	static MainGui  mainW;
	
	public static void setMainGui(MainGui mainGui){	mainW = mainGui; }
	
	public static void setFile(File f) {
		file = f;
		if(!file.getName().endsWith(Constants.FILE_EXTENSION_MSMB)) file = new File(file.getAbsoluteFile()+Constants.FILE_EXTENSION_MSMB);
	}
	
	public static void exportMultistateFormat(boolean withProgressBar) {
		try {
			if(withProgressBar && file!= null && mainW!=null) mainW.createAndShowProgressBarFrame(file.getName());
			
			ObjectOutput out = new ObjectOutputStream(new FileOutputStream(file,false));
			
			Vector<Vector<Vector<String>>> data = new Vector<Vector<Vector<String>>>();
			
			Vector<CustomTableModel_MSMB> tables = new Vector<CustomTableModel_MSMB>(Constants.TitlesTabs.getNumTabs());
			
			tables.setSize(Constants.TitlesTabs.getNumTabs());
			
			tables.set(Constants.TitlesTabs.REACTIONS.index, MainGui.tableReactionmodel);
			tables.set(Constants.TitlesTabs.SPECIES.index, MainGui.tableSpeciesmodel);
			tables.set(Constants.TitlesTabs.GLOBALQ.index, MainGui.tableGlobalQmodel);
			tables.set(Constants.TitlesTabs.FUNCTIONS.index, MainGui.tableFunctionsmodel);
			tables.set(Constants.TitlesTabs.EVENTS.index, MainGui.tableEventsmodel);
			tables.set(Constants.TitlesTabs.COMPARTMENTS.index, MainGui.tableCompartmentsmodel);
			
			for(int t = 0; t < tables.size(); t++) {
				if(mainW!=null)mainW.progress(10+t*10);
				Vector<Vector<String>> singleTable = new Vector<Vector<String>>();
				CustomTableModel_MSMB tablemodel = tables.get(t);
				if(tablemodel==null) continue;
				tablemodel.fireTableDataChanged();
				for(int i = 0; i < tablemodel.getRowCount()-1; i++) {
					Vector<String> row = new Vector<String>();
		    		for(int j = 0; j < tablemodel.getColumnCount(); j++) {
		    			if(tablemodel.getValueAt(i, j)==null) continue;
		    			String value = tablemodel.getValueAt(i, j).toString();
		    			if(tablemodel.getTableName().compareTo(Constants.TitlesTabs.FUNCTIONS.description)==0 && j == Constants.FunctionsColumns.EQUATION.index && value.trim().length() < 1) {
		    				System.err.println("Empty EQUATION field!"); 
		    				JOptionPane.showMessageDialog(new JButton(),"Empty EQUATION field at export!!", "Problem at export!", JOptionPane.ERROR_MESSAGE);
		    			}
		    			if(tablemodel.getTableName().compareTo(Constants.TitlesTabs.COMPARTMENTS.description)==0 && j == Constants.CompartmentsColumns.NAME.index && value.trim().length() == 0) {row=null; break;}
		    			if(tablemodel.getTableName().compareTo(Constants.TitlesTabs.EVENTS.description)==0 && j == Constants.EventsColumns.TRIGGER.index && value.trim().length() == 0) {row=null; break;}
		    			if(tablemodel.getTableName().compareTo(Constants.TitlesTabs.FUNCTIONS.description)==0 && j == Constants.FunctionsColumns.NAME.index && value.trim().length() == 0) {row=null; break;}
		    			if(tablemodel.getTableName().compareTo(Constants.TitlesTabs.GLOBALQ.description)==0 && j == Constants.GlobalQColumns.NAME.index && value.trim().length() == 0) {
		    				row=null; break;}
		    			if(tablemodel.getTableName().compareTo(Constants.TitlesTabs.REACTIONS.description)==0 && j == Constants.ReactionsColumns.REACTION.index && value.trim().length() == 0) {row=null; break;}
		    			if(tablemodel.getTableName().compareTo(Constants.TitlesTabs.SPECIES.description)==0 && j == Constants.SpeciesColumns.NAME.index && value.trim().length() == 0)  {row=null; break;}
				    	if(value.length() <=0) value = " ";
		    			row.add(value);
		    		}
		    		if(row != null) singleTable.add(new Vector<String>(row));
		    	}
				data.add(singleTable);
			}
			
			MutablePair<Vector<Vector<Vector<String>>>,HashMap<String, HashMap<String, String>>> tables_multistateInitials = new MutablePair<>();
			tables_multistateInitials.left = data;
			
			
			if(mainW!=null) {
				tables_multistateInitials.right = mainW.getMultistateInitials();
				mainW.progress(80);
			}
			
			
			out.writeObject(tables_multistateInitials);
			
			out.writeObject(MainGui.debugMessages);
			
			
			Vector<String> modelProperties = new Vector<String>();
			modelProperties.add(MainGui.modelName);
			modelProperties.add(new Integer(MainGui.volumeUnit).toString());
			modelProperties.add(new Integer(MainGui.timeUnit).toString());
			modelProperties.add(new Integer(MainGui.quantityUnit).toString());
			modelProperties.add(new Boolean(MainGui.exportConcentration).toString());
			modelProperties.add(new Boolean(MainGui.quantityIsConc).toString());
			
			out.writeObject(modelProperties);
			
			out.close();
			if(mainW!=null)mainW.progress(100);
		} catch (Exception e) {
			//if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) 
				e.printStackTrace();
		}
		
	}
	@SuppressWarnings("unchecked")
	public static HashMap<String, HashMap<String, String>> importMultistateFormat() {
		ObjectInputStream in;
		HashMap<String, HashMap<String, String>> multistateInitials = null;
		try {
			in = new ObjectInputStream(new FileInputStream(file));
			MutablePair<Vector<Vector<Vector<String>>>,HashMap<String, HashMap<String, String>>> tables_multistateInitials = (MutablePair<Vector<Vector<Vector<String>>>, HashMap<String, HashMap<String, String>>>) in.readObject();
			Vector<Vector<Vector<String>>>  tables = tables_multistateInitials.left;
			
			//Vector<Vector<Vector<String>>>  tables = (Vector<Vector<Vector<String>>>) in.readObject();
			Vector<Vector<String>> data = tables.get(Constants.TitlesTabs.REACTIONS.index);
			mainW.resetJTable(new Vector<Vector<String>>(data), Constants.TitlesTabs.REACTIONS.description);
			data = tables.get(Constants.TitlesTabs.SPECIES.index);
			mainW.resetJTable(new Vector<Vector<String>>(data), Constants.TitlesTabs.SPECIES.description);
			data = tables.get(Constants.TitlesTabs.COMPARTMENTS.index);
			mainW.resetJTable(new Vector<Vector<String>>(data), Constants.TitlesTabs.COMPARTMENTS.description);
			data = tables.get(Constants.TitlesTabs.EVENTS.index);
			mainW.resetJTable(new Vector<Vector<String>>(data), Constants.TitlesTabs.EVENTS.description);
			data = tables.get(Constants.TitlesTabs.FUNCTIONS.index);
			mainW.resetJTable(new Vector<Vector<String>>(data), Constants.TitlesTabs.FUNCTIONS.description);
			data = tables.get(Constants.TitlesTabs.GLOBALQ.index);
			mainW.resetJTable(new Vector<Vector<String>>(data), Constants.TitlesTabs.GLOBALQ.description);
			
			
			HashMap<String, DebugMessage> debugMessages = (HashMap<String, DebugMessage>) in.readObject();
			MainGui.debugMessages.putAll(debugMessages);
			
		
			
			multistateInitials = tables_multistateInitials.right;
			
					
			Vector<String> modelProperties = (Vector<String>)in.readObject();
			
			MainGui.modelName = modelProperties.get(0);
			MainGui.volumeUnit = Integer.parseInt(modelProperties.get(1));
			MainGui.timeUnit = Integer.parseInt(modelProperties.get(2));
			MainGui.quantityUnit = Integer.parseInt(modelProperties.get(3));
			MainGui.exportConcentration = Boolean.parseBoolean(modelProperties.get(4));
			MainGui.quantityIsConc = Boolean.parseBoolean(modelProperties.get(5));
			
			in.close();
		} catch (Exception e) {
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) 
				e.printStackTrace();
		}
		
		return multistateInitials;
		
	}
		
	public static void exportTxtTables(CustomTableModel_MSMB tableReactionmodel,CustomTableModel_MSMB tableSpeciesmodel, CustomTableModel_MSMB tableFunctionsmodel,
			CustomTableModel_MSMB tableGlobalQmodel, CustomTableModel_MSMB tableEventsmodel, CustomTableModel_MSMB tableCompartmentsmodel) {
		 try {
         	writeTable(tableReactionmodel, file.getAbsoluteFile()+".reactions.txt");
         	writeTableSpecies(tableSpeciesmodel, file.getAbsoluteFile()+".species.txt");
         	writeTableFunctions(tableFunctionsmodel, file.getAbsoluteFile()+".functions.txt");
         	writeTable(tableGlobalQmodel, file.getAbsoluteFile()+".globalQ.txt");
         	writeTable(tableEventsmodel, file.getAbsoluteFile()+".events.txt");
         	writeTable(tableCompartmentsmodel, file.getAbsoluteFile()+".compartments.txt");
         } catch (Exception e) {
        	 if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
		}
	}
	
	private static void writeTableSpecies(CustomTableModel_MSMB tableSpeciesmodel, String file) throws Exception{
		BufferedWriter buffout= new BufferedWriter(new FileWriter(file));
		PrintWriter out = new PrintWriter(buffout);
		int realRowCount = 1;
		for(int i = 0; i < tableSpeciesmodel.getRowCount()-1; i++) {
    		String row = new String();
    		String nameSpecie = tableSpeciesmodel.getValueAt(i, 1).toString();
    		if(!CellParsers.isMultistateSpeciesName(nameSpecie)) {
    			for(int j = 1; j < tableSpeciesmodel.getColumnCount(); j++) {
    				String value = tableSpeciesmodel.getValueAt(i, j).toString();
    				if(value.length() <=0) value = " ";
    				value = value.replace("\n", "\\n");
    				row += value + "\t";
    			}
    			row = realRowCount + "\t" + row;
        		if(row.length() >0) out.println(row);
    			realRowCount++;
    		} else {
    			MultistateSpecies ms = MainGui.multiModel.getMultistateSpecies(nameSpecie);
    			Vector<Species> single = ms.getExpandedSpecies(MainGui.multiModel);
    			for(int k = 0; k < single.size(); k++) {
    				Species sp = single.get(k);
    				row = 	sp.getDisplayedName() + "\t" +
    						//ms.getInitial_singleConfiguration(sp, false) + "\t" +
    						//ms.getInitial_singleConfiguration(sp, true) + "\t" +
    						ms.getInitial_singleConfiguration(sp) + "\t" +
    						Constants.SpeciesType.getDescriptionFromCopasiType(ms.getType()) + "\t" +
    						sp.getCompartment_listString() + "\t" +
    						sp.getExpression() + "\t" +
    						sp.getNotes();
    				row = realRowCount + "\t" + row;
    				out.println(row);
        			realRowCount++;
        		}
    		}
    	}
    	out.flush();
    	out.close();
	}
	
	private static void writeTable(CustomTableModel_MSMB tableReactionmodel, String file) throws IOException{
		BufferedWriter buffout= new BufferedWriter(new FileWriter(file));
		PrintWriter out = new PrintWriter(buffout);
		
		for(int i = 0; i < tableReactionmodel.getRowCount()-1; i++) {
    		for(int j = 0; j < tableReactionmodel.getColumnCount(); j++) {
    			String value = tableReactionmodel.getValueAt(i, j).toString();
    			if(value.length() <=0) value = " ";
    			out.print(value + "\t");
    		}
    		out.println("");
    	}
    	out.flush();
    	out.close();
	}
	
	//I have to export the complete signature for all the functions if I want to be able to
	//import from txt in a correct way!!!
	private static void writeTableFunctions(CustomTableModel_MSMB tableFunctionsmodel, String file) throws Exception{
		BufferedWriter buffout= new BufferedWriter(new FileWriter(file));
		PrintWriter out = new PrintWriter(buffout);
		
		for(int i = 0; i < tableFunctionsmodel.getRowCount()-1; i++) {
			String nameFunction = tableFunctionsmodel.getValueAt(i, 1).toString();
			Function f = MainGui.multiModel.funDB.getFunctionByName(nameFunction);
			out.print(tableFunctionsmodel.getValueAt(i, 0).toString()+"\t" +f.printCompleteSignature() + "\t");	
    		for(int j = 2; j < tableFunctionsmodel.getColumnCount(); j++) {
    			String value = tableFunctionsmodel.getValueAt(i, j).toString();
    			if(value.length() <=0) value = " ";
    			out.print(value + "\t");
    		}
    		out.println("");
    	}
    	out.flush();
    	out.close();
	}
}
