package gui;

import java.io.*;
import java.util.HashMap;
import java.util.Vector;

import org.apache.commons.lang3.tuple.MutablePair;

import debugTab.DebugMessage;

import model.Function;
import model.MultistateSpecies;
import model.Species;
import utility.Constants;

class ExportMultistateFormat {

	private static File file = null;
	public final static String MULTISTATE_SUFFIX = ".multis";
	static MainGui  mainW;
	
	public static void setMainGui(MainGui mainGui){	mainW = mainGui; }
	
	public static void setFile(File f) {
		file = f;
		if(!file.getName().endsWith(MULTISTATE_SUFFIX)) file = new File(file.getAbsoluteFile()+MULTISTATE_SUFFIX);
	}
	
	public static void exportMultistateFormat(boolean withProgressBar) {
		try {
			if(withProgressBar && file!= null) mainW.createAndShowProgressBarFrame(file.getName());
			
			ObjectOutput out = new ObjectOutputStream(new FileOutputStream(file,false));
			
			Vector<Vector<Vector<String>>> data = new Vector<Vector<Vector<String>>>();
			
			Vector<CustomTableModel> tables = new Vector<CustomTableModel>(Constants.TitlesTabs.getNumTabs());
			
			tables.setSize(Constants.TitlesTabs.getNumTabs());
			
			tables.set(Constants.TitlesTabs.REACTIONS.index, MainGui.tableReactionmodel);
			tables.set(Constants.TitlesTabs.SPECIES.index, MainGui.tableSpeciesmodel);
			tables.set(Constants.TitlesTabs.GLOBALQ.index, MainGui.tableGlobalQmodel);
			tables.set(Constants.TitlesTabs.FUNCTIONS.index, MainGui.tableFunctionsmodel);
			tables.set(Constants.TitlesTabs.EVENTS.index, MainGui.tableEventsmodel);
			tables.set(Constants.TitlesTabs.COMPARTMENTS.index, MainGui.tableCompartmentsmodel);
			
			for(int t = 0; t < tables.size(); t++) {
				mainW.progress(10+t*10);
				Vector<Vector<String>> singleTable = new Vector<Vector<String>>();
				CustomTableModel tablemodel = tables.get(t);
				if(tablemodel==null) continue;
				for(int i = 0; i < tablemodel.getRowCount()-1; i++) {
					Vector<String> row = new Vector<String>();
		    		for(int j = 0; j < tablemodel.getColumnCount(); j++) {
		    			String value = tablemodel.getValueAt(i, j).toString();
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
			
			
			tables_multistateInitials.right = mainW.getMultistateInitials();
			mainW.progress(80);
			
			
			out.writeObject(tables_multistateInitials);
			
			out.writeObject(MainGui.debugMessages);
			
			out.close();
			mainW.progress(100);
		} catch (Exception e) {
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
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
			in.close();
		} catch (Exception e) {
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
		}
		
		return multistateInitials;
		
	}
		
	public static void exportTxtTables(CustomTableModel tableReactionmodel,CustomTableModel tableSpeciesmodel, CustomTableModel tableFunctionsmodel,
			CustomTableModel tableGlobalQmodel, CustomTableModel tableEventsmodel, CustomTableModel tableCompartmentsmodel) {
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
	
	private static void writeTableSpecies(CustomTableModel tablemodel, String file) throws Exception{
		BufferedWriter buffout= new BufferedWriter(new FileWriter(file));
		PrintWriter out = new PrintWriter(buffout);
		int realRowCount = 1;
		for(int i = 0; i < tablemodel.getRowCount()-1; i++) {
    		String row = new String();
    		String nameSpecie = tablemodel.getValueAt(i, 1).toString();
    		if(!nameSpecie.contains("(")) {
    			for(int j = 1; j < tablemodel.getColumnCount(); j++) {
    				String value = tablemodel.getValueAt(i, j).toString();
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
    						sp.getCompartment() + "\t" +
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
	
	private static void writeTable(CustomTableModel tablemodel, String file) throws IOException{
		BufferedWriter buffout= new BufferedWriter(new FileWriter(file));
		PrintWriter out = new PrintWriter(buffout);
		
		for(int i = 0; i < tablemodel.getRowCount()-1; i++) {
    		for(int j = 0; j < tablemodel.getColumnCount(); j++) {
    			String value = tablemodel.getValueAt(i, j).toString();
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
	private static void writeTableFunctions(CustomTableModel tablemodel, String file) throws IOException{
		BufferedWriter buffout= new BufferedWriter(new FileWriter(file));
		PrintWriter out = new PrintWriter(buffout);
		
		for(int i = 0; i < tablemodel.getRowCount()-1; i++) {
			String nameFunction = tablemodel.getValueAt(i, 1).toString();
			Function f = MainGui.multiModel.funDB.getFunctionByName(nameFunction);
			out.print(tablemodel.getValueAt(i, 0).toString()+"\t" +f.printCompleteSignature() + "\t");	
    		for(int j = 2; j < tablemodel.getColumnCount(); j++) {
    			String value = tablemodel.getValueAt(i, j).toString();
    			if(value.length() <=0) value = " ";
    			out.print(value + "\t");
    		}
    		out.println("");
    	}
    	out.flush();
    	out.close();
	}
}
