package debugTab;

import gui.CustomTableModel;
import gui.MainGui;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Vector;

import model.Compartment;
import model.Function;
import model.MultiModel;
import model.MultistateSpecies;
import model.Species;

import org.COPASI.CFunctionParameter;

import parsers.mathExpression.MR_Expression_Parser;
import parsers.mathExpression.syntaxtree.CompleteExpression;
import parsers.mathExpression.visitor.Look4UndefinedMisusedVisitor;
import utility.CellParsers;
import utility.Constants;
import utility.Constants.TitlesTabs;

public class ConsistencyChecks {

	
	private static final HashSet<Integer> optionalColumns_Reactions;
	static {
		optionalColumns_Reactions = new HashSet<Integer>();
		optionalColumns_Reactions.add(Constants.ReactionsColumns.NAME.index);
		optionalColumns_Reactions.add(Constants.ReactionsColumns.EXPANDED.index);
		optionalColumns_Reactions.add(Constants.ReactionsColumns.NOTES.index);
	}
	
	private static final HashSet<Integer> suggestedColumns_Reactions;
	static {
		suggestedColumns_Reactions = new HashSet<Integer>();
		suggestedColumns_Reactions.add(Constants.ReactionsColumns.KINETIC_LAW.index);
		suggestedColumns_Reactions.add(Constants.ReactionsColumns.TYPE.index);
	}
	
	private static final HashSet<Integer> optionalColumns_Species;
	static {
		optionalColumns_Species = new HashSet<Integer>();
		optionalColumns_Species.add(Constants.SpeciesColumns.EXPRESSION.index);
		optionalColumns_Species.add(Constants.SpeciesColumns.NOTES.index);
	}
	
	private static final HashSet<Integer> optionalColumns_GlobalQ;
	static {
		optionalColumns_GlobalQ = new HashSet<Integer>();
		optionalColumns_GlobalQ.add(Constants.GlobalQColumns.EXPRESSION.index);
		optionalColumns_GlobalQ.add(Constants.GlobalQColumns.NOTES.index);
	}
	
	private static final HashSet<Integer> optionalColumns_Functions;
	static {
		optionalColumns_Functions = new HashSet<Integer>();
		optionalColumns_Functions.add(Constants.FunctionsColumns.PARAMETER_ROLES.index);
		//optionalColumns_Functions.add(Constants.FunctionsColumns.SIGNATURE.index);
		optionalColumns_Functions.add(Constants.FunctionsColumns.NOTES.index);
	}
	
	private static final HashSet<Integer> optionalColumns_Compartments;
	static {
		optionalColumns_Compartments = new HashSet<Integer>();
		optionalColumns_Compartments.add(Constants.CompartmentsColumns.EXPRESSION.index);
		optionalColumns_Compartments.add(Constants.CompartmentsColumns.NOTES.index);
	}
	
	private static final HashSet<Integer> optionalColumns_Events;
	static {
		optionalColumns_Events = new HashSet<Integer>();
		optionalColumns_Events.add(Constants.EventsColumns.NAME.index);
		optionalColumns_Events.add(Constants.EventsColumns.DELAY.index);
		optionalColumns_Events.add(Constants.EventsColumns.NOTES.index);
	}
	
	public static HashSet<String> emptyFields = new HashSet<String>();
	public static HashSet<String> emptyNonMandatoryFields = new HashSet<String>();
	
	public static void put_EmptyFields(String table_name, Integer row, Integer col) {
			String key = table_name+"_"+row+"_"+col;
			boolean addedInEmptyFields = false;
			boolean addedInEmptyNonMandatoryFields = false;
			if(table_name.compareTo(Constants.TitlesTabs.REACTIONS.description) == 0) {
				if(!optionalColumns_Reactions.contains(col)) {	
					if(suggestedColumns_Reactions.contains(col)){
						addedInEmptyNonMandatoryFields = true;
						emptyNonMandatoryFields.add(key);
					}
					else  {
						emptyFields.add(key); 
						addedInEmptyFields = true;
					}
				}
			
			} else if(table_name.compareTo(Constants.TitlesTabs.SPECIES.description) == 0) {
				if(!optionalColumns_Species.contains(col)) {	emptyFields.add(key); addedInEmptyFields = true;} 
			}else if(table_name.compareTo(Constants.TitlesTabs.COMPARTMENTS.description) == 0) {
				if(!optionalColumns_Compartments.contains(col)) {	emptyFields.add(key); addedInEmptyFields = true;}
			}else if(table_name.compareTo(Constants.TitlesTabs.EVENTS.description) == 0) {
				if(!optionalColumns_Events.contains(col)) {	emptyFields.add(key); addedInEmptyFields = true;}
			}else if(table_name.compareTo(Constants.TitlesTabs.FUNCTIONS.description) == 0) {
				if(!optionalColumns_Functions.contains(col)) {	emptyFields.add(key); addedInEmptyFields = true;}
			}else if(table_name.compareTo(Constants.TitlesTabs.GLOBALQ.description) == 0) {
				if(!optionalColumns_GlobalQ.contains(col)) {	emptyFields.add(key); addedInEmptyFields = true;}
			}
			
			if(addedInEmptyFields) {
				DebugMessage dm = new DebugMessage();
				dm.setOrigin_table(table_name);
				dm.setProblem("Empty mandatory field");
				dm.setPriority(DebugConstants.PriorityType.EMPTY.priorityCode);
				dm.setOrigin_col(col);
				dm.setOrigin_row(row);
				MainGui.addDebugMessage_ifNotPresent(dm);
			} 
			if(addedInEmptyNonMandatoryFields) {
				DebugMessage dm = new DebugMessage();
				dm.setOrigin_table(table_name);
				dm.setProblem("Empty important non-mandatory field");
				dm.setPriority(DebugConstants.PriorityType.MINOR_EMPTY.priorityCode);
				dm.setOrigin_col(col);
				dm.setOrigin_row(row);
				MainGui.addDebugMessage_ifNotPresent(dm);
			}
	}
	
	public static void remove_EmptyFields(String table_name, Integer row, Integer col) throws Exception {
		emptyFields.remove(table_name+"_"+row+"_"+col);
		emptyNonMandatoryFields.remove(table_name+"_"+row+"_"+col);
		MainGui.clear_debugMessages_relatedWith(table_name,DebugConstants.PriorityType.EMPTY.priorityCode,row,col);
		MainGui.clear_debugMessages_relatedWith(table_name,DebugConstants.PriorityType.MINOR_EMPTY.priorityCode,row,col);
	}
	
	
	
	public static Vector<String>  all_parameters_in_functionCalls_exist(int whichTable, MultiModel multiModel, String functionCall, int rowIndex)throws Exception {
		Vector<?> paramMapping = new Vector<Object>();
		if(whichTable == TitlesTabs.REACTIONS.index) {
			paramMapping = (Vector<?>) multiModel.funDB.getMapping(rowIndex);
		} else if(whichTable == TitlesTabs.SPECIES.index) {
			paramMapping = 	multiModel.funDB.get_mappings_speciesExpression(rowIndex, functionCall);
		}
		return all_parameters_in_functionCalls_exist(multiModel,rowIndex,paramMapping);
		
	}
	
	
	private static Vector<String> all_parameters_in_functionCalls_exist(MultiModel multiModel, int rowIndex, Vector<?> paramMapping) throws Exception {
			Vector<String> missingParameters = new Vector<String>();
			if(paramMapping == null) return missingParameters;
			Function f = (Function)paramMapping.get(0);
			Vector<Integer> paramRoles =  f.getParametersTypes_CFunctionParameter();
			Vector<Integer> roleVector = new Vector<Integer>();
			roleVector.add(CFunctionParameter.PARAMETER);
			roleVector.add(CFunctionParameter.SUBSTRATE);
			roleVector.add(CFunctionParameter.PRODUCT);
			roleVector.add(CFunctionParameter.MODIFIER);
			roleVector.add(CFunctionParameter.VOLUME);
			roleVector.add(Constants.SITE_FOR_WEIGHT_IN_SUM);
			roleVector.add(Constants.ROLE_EXPRESSION);
			
			for(int iii = 1, jjj = 0; iii < paramMapping.size(); iii=iii+2,jjj++) {
					//String parameterNameInFunction = (String)paramMapping.get(iii);
					String actualModelParameter = (String)paramMapping.get(iii+1);
					int role = paramRoles.get(jjj);
					boolean checkAllRoles = false;
					int indexRole = 0;
					if(role==CFunctionParameter.VARIABLE) { checkAllRoles  = true; role= (Integer) roleVector.get(indexRole);}
					do{
						try {
							Vector<String> undef = null;
							switch(role) {
							case CFunctionParameter.PARAMETER:     
								try{
									Double.parseDouble(actualModelParameter);
									checkAllRoles = false;
								} catch(NumberFormatException ex) { //the parameter is not a number but a globalQ
									if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) ex.printStackTrace();
									String value = multiModel.getGlobalQ(actualModelParameter);
									if(value == null) {
										throw new NullPointerException();
									}
									checkAllRoles = false;
								}
								break;
							case CFunctionParameter.SUBSTRATE: 
							case CFunctionParameter.PRODUCT:  
							case CFunctionParameter.MODIFIER:   
								Species sp = multiModel.getSpecies(actualModelParameter);
								if(sp == null) throw new NullPointerException();
								checkAllRoles = false;
								break;
							case CFunctionParameter.VOLUME:    
								Compartment c = multiModel.getComp(actualModelParameter);
								if(c == null) throw new NullPointerException();
								checkAllRoles = false;
								break;
							case Constants.ROLE_EXPRESSION:  
								if(actualModelParameter.length() >0) {
									  InputStream is = new ByteArrayInputStream(actualModelParameter.getBytes("UTF-8"));
									  MR_Expression_Parser parser = new MR_Expression_Parser(is);
									  CompleteExpression root = parser.CompleteExpression();
									  Look4UndefinedMisusedVisitor undefVisitor = new Look4UndefinedMisusedVisitor(multiModel);
									  root.accept(undefVisitor);
									  undef = undefVisitor.getUndefinedElements();
								}
								if(undef == null || undef.size() ==0)	{checkAllRoles = false; }
								else throw new NullPointerException();
								break;
							case CFunctionParameter.TIME:    
								checkAllRoles = false;
								break;
							/*case Constants.SITE_FOR_WEIGHT_IN_SUM:
								System.out.println("TOOOOOOOO DO Constants.SITE_FOR_WEIGHT_IN_SUM");
								break;*/
							default: 
								//System.out.println("missing parameter role in function, for actual value " + actualModelParameter);
								throw new NullPointerException();
							}
						} catch(Exception ex) {
							if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) ex.printStackTrace();
							if(checkAllRoles==false) throw ex;
							indexRole++;
							try {
								role = (Integer) roleVector.get(indexRole);
							} catch(Exception ex2) {
								if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) ex2.printStackTrace();
								DebugMessage dm = new DebugMessage();
								 dm.setOrigin_table(Constants.TitlesTabs.REACTIONS.description);
								 dm.setProblem("Missing element definition: " + actualModelParameter);
								 dm.setPriority(DebugConstants.PriorityType.MISSING.priorityCode);
								 dm.setOrigin_col(Constants.ReactionsColumns.KINETIC_LAW.index);
								 dm.setOrigin_row(rowIndex+1);
								 MainGui.addDebugMessage_ifNotPresent(dm);
								//System.out.println("Missing parameter definition: "+actualModelParameter);
								MainGui.updateDebugTab();
								missingParameters.add(actualModelParameter);
								break;
							}
						}
					}while(checkAllRoles || indexRole >=roleVector.size());
				}
				 
				return missingParameters;
	}
	
	
	//FOR NOW I'M USING THE TABLE REACTION MODEL BUT NEED TO BE CHANGED ONCE THERE WILL BE
	//A DATA STRUCTURE FOR THE REACTIONS IN THE MULTIMODEL
	public static boolean all_elements_in_reaction_exist(MultiModel multiModel, CustomTableModel tableReactionmodel) throws Exception{
		Vector<String> missingSpecies = new Vector<String>();
		
		if(tableReactionmodel!= null ) {
			for(int i = 0; i < tableReactionmodel.getRowCount()-1 ; i++ ) {
				String string_reaction = ((String)tableReactionmodel.getValueAt(i, 2)).trim();
				if(string_reaction.trim().length() <= 0) continue;

				MainGui.clear_debugMessages_relatedWith(Constants.TitlesTabs.REACTIONS.description, DebugConstants.PriorityType.MISSING.priorityCode, i, Constants.ReactionsColumns.REACTION.index);
				MainGui.clear_debugMessages_relatedWith(Constants.TitlesTabs.REACTIONS.description, DebugConstants.PriorityType.MISSING.priorityCode, i, Constants.ReactionsColumns.KINETIC_LAW.index);
				MainGui.clear_debugMessages_relatedWith(Constants.TitlesTabs.REACTIONS.description, DebugConstants.PriorityType.PARSING.priorityCode, i, Constants.ReactionsColumns.KINETIC_LAW.index);
				MainGui.clear_debugMessages_relatedWith(Constants.TitlesTabs.REACTIONS.description, DebugConstants.PriorityType.PARSING.priorityCode, i, Constants.ReactionsColumns.REACTION.index);
			
				//missingSpecies.addAll(all_parameters_in_functionCalls_exist(multiModel, (String)tableReactionmodel.getValueAt(i, Constants.ReactionsColumns.KINETIC_LAW.index), i));
				missingSpecies.addAll(all_parameters_in_functionCalls_exist(Constants.TitlesTabs.REACTIONS.index, multiModel, (String)tableReactionmodel.getValueAt(i, Constants.ReactionsColumns.KINETIC_LAW.index), i));
				
						
				//boolean parseErrors = false;
				Vector<?> metabolites = new Vector<Object>();
				Vector<?> singleConfigurations = new Vector<Object>();
				try{ 
					metabolites = CellParsers.parseReaction(multiModel,string_reaction,i+1);
					singleConfigurations = multiModel.expandReaction(metabolites,i);
				} catch(Exception ex) {
					//if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) 
						ex.printStackTrace();
					 DebugMessage dm = new DebugMessage();
					 dm.setOrigin_table(Constants.TitlesTabs.REACTIONS.description);
					dm.setProblem("Problem parsing the reaction string. " + ex.getMessage());
				    dm.setPriority(DebugConstants.PriorityType.PARSING.priorityCode);
					 dm.setOrigin_col(Constants.ReactionsColumns.REACTION.index);
					 dm.setOrigin_row(i+1);
					 MainGui.addDebugMessage_ifNotPresent(dm,false);
					//parseErrors = true;
					continue;
				}

				
				for(int j = 0; j < singleConfigurations.size(); j++) {
					Vector<?> expandedReaction = (Vector<?>) singleConfigurations.get(j);

					Vector<?> subs = (Vector<?>)expandedReaction.get(0);
					Vector<?> prod =(Vector<?>)expandedReaction.get(1);
					Vector<?> mod = (Vector<?>)expandedReaction.get(2);

					for(int i1 = 0; i1 < subs.size(); i1++) {
						String s = (String)subs.get(i1);
						s = multiModel.extractName(s);
						if(!multiModel.containsSpecies(s))  {
							 DebugMessage dm = new DebugMessage();
							 dm.setOrigin_table(Constants.TitlesTabs.REACTIONS.description);
							 dm.setProblem("Missing species definition: " + s);
							 dm.setPriority(DebugConstants.PriorityType.MISSING.priorityCode);
							 dm.setOrigin_col(Constants.ReactionsColumns.REACTION.index);
							 dm.setOrigin_row(i+1);
							 MainGui.addDebugMessage_ifNotPresent(dm);
							missingSpecies.add(s);
						} else {
							Species sp = multiModel.getSpecies(s);
							if(sp instanceof MultistateSpecies) {
								MultistateSpecies m = (MultistateSpecies)sp;
								if(!m.containsSpecificConfiguration(s)) {
									DebugMessage dm = new DebugMessage();
									 dm.setOrigin_table(Constants.TitlesTabs.REACTIONS.description);
									 dm.setProblem("Missing species definition: " + s);
									 dm.setPriority(DebugConstants.PriorityType.MISSING.priorityCode);
									 dm.setOrigin_col(Constants.ReactionsColumns.REACTION.index);
									 dm.setOrigin_row(i+1);
									 MainGui.addDebugMessage_ifNotPresent(dm);
									missingSpecies.add(s);
								}
							}
						}
					}
					
					for(int i1 = 0; i1 < prod.size(); i1++) {
						String s = (String)prod.get(i1);
						s = multiModel.extractName(s);
						if(!multiModel.containsSpecies(s))  {
							DebugMessage dm = new DebugMessage();
							 dm.setOrigin_table(Constants.TitlesTabs.REACTIONS.description);
							 dm.setProblem("Missing species definition: " + s);
							 dm.setPriority(DebugConstants.PriorityType.MISSING.priorityCode);
							 dm.setOrigin_col(Constants.ReactionsColumns.REACTION.index);
							 dm.setOrigin_row(i+1);
							 MainGui.addDebugMessage_ifNotPresent(dm);
							missingSpecies.add(s);
						} 
					}
					
					for(int i1 = 0; i1 < mod.size(); i1++) {
						String s = (String)mod.get(i1);
						s = multiModel.extractName(s);
						if(!multiModel.containsSpecies(s))  {
							DebugMessage dm = new DebugMessage();
							 dm.setOrigin_table(Constants.TitlesTabs.REACTIONS.description);
							 dm.setProblem("Missing species definition: " + s);
							 dm.setPriority(DebugConstants.PriorityType.MISSING.priorityCode);
							 dm.setOrigin_col(Constants.ReactionsColumns.REACTION.index);
							 dm.setOrigin_row(i+1);
							 MainGui.addDebugMessage_ifNotPresent(dm);
							missingSpecies.add(s);
						} 
					}
					
					
				}
			}
			
			if(missingSpecies.size() >0) {
				throw new Exception("Error during validation: the following species are not defined "+missingSpecies);
			} 
		}
		
		return true;
	}

	
		
	public static Vector<Object> missing_fields() {
		return new Vector<Object>(Arrays.asList(emptyFields.toArray()));
	}
	
	public static Vector<Object> missing_nonMandatory_fields() {
		return new Vector<Object>(Arrays.asList(emptyNonMandatoryFields.toArray()));
	}

}
