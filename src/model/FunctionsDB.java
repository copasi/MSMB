package model;

import gui.MainGui;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

import parsers.mathExpression.syntaxtree.CompleteExpression;
import parsers.mathExpression.syntaxtree.CompleteFunctionDeclaration;
import parsers.mathExpression.visitor.GetFunctionNameVisitor;
import parsers.mathExpression.visitor.Look4UndefinedMisusedVisitor;
import parsers.mathExpression.visitor.RateLawMappingVisitor;

import org.COPASI.CFunction;
import parsers.mathExpression.MR_Expression_Parser;
import parsers.mathExpression.MR_Expression_Parser_ReducedParserException;

import debugTab.DebugConstants;
import debugTab.DebugMessage;

import sun.misc.Cleaner;
import utility.CellParsers;
import utility.Constants;
import utility.MyChangeNotAllowedException;
import utility.MySyntaxException;


public class FunctionsDB {
	MultiModel multiModel = null;
	public FunctionsDB(MultiModel mm) {
		multiModel = mm;
	}
	
	public static CellParsers parser  = new CellParsers();
	
	HashMap<Integer,Function> userDefinedFun = new HashMap<Integer, Function>(); 
	public HashMap<Integer, Vector> mappings = new HashMap<Integer, Vector>();
	public HashMap<String, HashSet<Integer>> whereFuncIsUsed = new HashMap<String, HashSet<Integer>>();
	
	HashMap<String, Vector> mappings_weight_globalQ_withSUM = new HashMap<String, Vector>();
	HashMap<String, Vector> mappings_speciesExpression = new HashMap<String, Vector>();
	
	HashMap<String, Vector> mappings_weight_subFunctions_Functions = new HashMap<String, Vector>(); 
	
	
	
	public void addChangeFunction(int row, Function f) throws Exception {
	
		int existing = getIndex(f.getName());
		if(existing != -1) {
			row = existing;
		}
		
		userDefinedFun.put(row, f); 
	//	System.out.println("in addChangeFunction: "+userDefinedFun);
		
		parser.addFunction(f);
		
		//recalculate mappings
		if(whereFuncIsUsed.get(f.getName())!=null) {
			HashSet<Integer> used = whereFuncIsUsed.get(f.getName());
			Iterator<Integer> it = used.iterator();
			
			while(it.hasNext()) {
				int row_reaction = it.next();
				Vector mapping_vector = mappings.get(row_reaction);
				if(mapping_vector == null) continue;
				Vector new_mapping_vector = new Vector();
				Vector<String> param_names = f.getParametersNames();
			//	Vector<String> param_roles = f.getParametersTypes();
				int j = 0;
				new_mapping_vector.add(f);
				for(int i = 1; i < mapping_vector.size(); i=i+2, j++) {
					new_mapping_vector.add(param_names.get(j));
					new_mapping_vector.add(mapping_vector.get(i+1));
					
				}
				mappings.put(row_reaction, new_mapping_vector);
				if(!mapping_vector.equals(new_mapping_vector)) {
					DebugMessage dm = new DebugMessage();
					dm.setOrigin_table(Constants.TitlesTabs.REACTIONS.description);
					dm.setProblem("The signature of function " + f.getName() + " has been modified. \nThe new mapping of reaction at row " + row+ " is " + new_mapping_vector.subList(1, new_mapping_vector.size()));
					dm.setPriority(DebugConstants.PriorityType.MINOR.priorityCode);
					dm.setOrigin_col(Constants.ReactionsColumns.KINETIC_LAW.index);
					dm.setOrigin_row(row_reaction+1);
					MainGui.addDebugMessage_ifNotPresent(dm);
				} else {
					try {
						MainGui.clear_debugMessages_relatedWith(Constants.TitlesTabs.REACTIONS.description, DebugConstants.PriorityType.MINOR.priorityCode, row_reaction+1, Constants.ReactionsColumns.KINETIC_LAW.index);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} else {
			try {
				MainGui.clear_debugMessages_relatedWith_table_col_priority(Constants.TitlesTabs.REACTIONS.description, 
						Constants.ReactionsColumns.KINETIC_LAW.index,DebugConstants.PriorityType.MINOR.priorityCode);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public int getFunctionIndex(Function f){
		int ret = -1;
		if(f==null) return ret;
		Iterator<Integer> it = userDefinedFun.keySet().iterator();
		while(it.hasNext()) {
			Integer index = it.next();
			if(f.getName().compareTo(userDefinedFun.get(index).getName()) == 0) {
				ret = index.intValue();
				break;
			}
		}
		return ret;
	}

	public static String extractJustName(String functionNamePlusPossibleParameters) throws Exception {
		String ret = new String();
		InputStream is;
		try {
			is = new ByteArrayInputStream(functionNamePlusPossibleParameters.getBytes("UTF-8"));
	
		  MR_Expression_Parser parser = new MR_Expression_Parser(is);
		  CompleteFunctionDeclaration root = parser.CompleteFunctionDeclaration();
		  GetFunctionNameVisitor name = new GetFunctionNameVisitor();
		  root.accept(name);
		  ret = name.getFunctionName();
		  
		} catch (Exception e) {
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
			throw e;
		}
		return ret;
	}
	public Function getFunctionByName(String name) throws Exception {
		
		if(name.endsWith(")")) name = extractJustName(name);
		Iterator<Integer> it = userDefinedFun.keySet().iterator();
		while(it.hasNext()){
			int index = it.next();
			Function f = (Function)userDefinedFun.get(index);
			if(f.getName().compareTo(name) == 0) {
				return f;
			}
		}
		return null;
	}
	
	
	
	public Vector<String> addMapping(int row, String equation, String type) throws Exception {
		Vector<String> ret = new Vector<String>();
		if(equation.length() ==0) return ret;
		//System.out.println(equation);
		try {
				 
				InputStream is = new ByteArrayInputStream(equation.getBytes("UTF-8"));
				MR_Expression_Parser_ReducedParserException parser = new MR_Expression_Parser_ReducedParserException(is);
				CompleteExpression root = parser.CompleteExpression();
				Look4UndefinedMisusedVisitor undefVisitor = new Look4UndefinedMisusedVisitor(multiModel);
				root.accept(undefVisitor);
				Vector<String> undef = undefVisitor.getUndefinedElements();
				Vector<String> misused = undefVisitor.getMisusedElements();
				  
				 
				 if(misused.size() != 0) {
					    String message = new String();
						if(misused.size() > 0) message += "The following elements are misused: " +misused.toString();
						throw new MySyntaxException(Constants.ReactionsColumns.KINETIC_LAW.index, message,Constants.TitlesTabs.REACTIONS.description);
				  }
		   Vector<String> PARs = new Vector();
			if(type.compareTo(Constants.ReactionType.MASS_ACTION.description)!=0) {
				InputStream is1 = new ByteArrayInputStream(equation.getBytes("UTF-8"));
				MR_Expression_Parser parser1 = new MR_Expression_Parser(is1);
				CompleteExpression root1 = parser1.CompleteExpression();
				RateLawMappingVisitor vis = new RateLawMappingVisitor(multiModel,row, equation);
				root1.accept(vis);
				PARs = vis.getGlobalQ_PARtype();
			} else {
				PARs.addAll(undef);// the single parameter of a mass action kinetic (added 
			}
			
			String message = new String("The following elements are used but never declared: " );
			boolean found_non_PAR_missing = false;
			for(int i = 0; i <undef.size();i++ ) {
				String undef_maybePar = undef.get(i);
				if(PARs.contains(undef_maybePar)){
					ret.add(undef_maybePar);
				}
				else {
					found_non_PAR_missing = true;
					    message += undef_maybePar +" ";
				  }
			}
			if(found_non_PAR_missing) throw new MySyntaxException(Constants.ReactionsColumns.KINETIC_LAW.index, message,Constants.TitlesTabs.REACTIONS.description);
			/*else if(found_non_PAR_missing && MainGui.autocompleteWithDefaults) {
				return ret;
			}*/
		} catch (Exception e) {
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
			throw e;
		}
		
		  
	
		return ret;
	}
	
	public Vector getMapping(int row) {
		return mappings.get(row);
	}

	public int getNumFunctions() {
		return this.userDefinedFun.size();
	}
	
	public void addMapping_weight_globalQ_withSUM(int row, MultistateSpecies sp, String weight_functionCall) throws Exception {
		int openBr = weight_functionCall.indexOf("(");
		while(openBr!=-1) {
			if(weight_functionCall.charAt(openBr-1)=='\\') { 
				openBr=weight_functionCall.indexOf("(",openBr+1);	
			} else {
				break ;
			}
		}		
		int closedBr = weight_functionCall.lastIndexOf(")");
		
		if(openBr==-1 || closedBr == -1) return;
		
		String funName = weight_functionCall.substring(0,openBr);
		
		Function f = this.getFunctionByName(funName);
		
		String param = weight_functionCall.substring(openBr+1, closedBr);
		StringTokenizer st = new StringTokenizer(param, " ,");
		Vector mapping_vector = new Vector();
		mapping_vector.add(f);
		Vector<String> param_names = f.getParametersNames();
		int i = 0;
		while(st.hasMoreTokens()) {
			String actualValue = st.nextToken();
			mapping_vector.add(param_names.get(i));
			mapping_vector.add(actualValue);
			i++;
		}
		mappings_weight_globalQ_withSUM.put(new String(row+"_"+sp.getSpeciesName()+"_"+funName), mapping_vector);
		return;
	}
	
	
	
	public void addMapping_speciesExpression(int row, String functionCall) throws Exception {
		int openBr = functionCall.indexOf("(");
		while(openBr!=-1) {
			if(functionCall.charAt(openBr-1)=='\\') { 
				openBr=functionCall.indexOf("(",openBr+1);	
			} else {
				break ;
			}
		}		
		int closedBr = functionCall.lastIndexOf(")");
		
		if(openBr==-1 || closedBr == -1) return;
		
		String funName = functionCall.substring(0,openBr);
		
		Function f = this.getFunctionByName(funName);
		
		String param = functionCall.substring(openBr+1, closedBr);
		StringTokenizer st = new StringTokenizer(param, " ,");
		Vector mapping_vector = new Vector();
		mapping_vector.add(f);
		Vector<String> param_names = f.getParametersNames();
		int i = 0;
		while(st.hasMoreTokens()) {
			String actualValue = st.nextToken();
			mapping_vector.add(param_names.get(i));
			mapping_vector.add(actualValue);
			i++;
		}
		mappings_speciesExpression.put(new String(row+"_"+funName), mapping_vector);
		return;
	}
	
	public Vector get_mappings_speciesExpression(int row, String functionCall) {
		int openBr = functionCall.indexOf("(");
		int closedBr = functionCall.lastIndexOf(")");
		while(openBr!=-1) {
			if(functionCall.charAt(openBr-1)=='\\') { 
				openBr=functionCall.indexOf("(",openBr+1);	
			} else {
				break ;
			}
		}		
		if(openBr==-1 || closedBr == -1) return new Vector();
		
		String funName = functionCall.substring(0,openBr);
		return mappings_speciesExpression.get(new String(row+"_"+funName));
	}
	
	public Vector get_mappings_weight_globalQ_withSUM(int row, MultistateSpecies sp, String weight_functionCall) {
		int openBr = weight_functionCall.indexOf("(");
		int closedBr = weight_functionCall.lastIndexOf(")");
		
		if(openBr==-1 || closedBr == -1) return new Vector();
		
		String funName = weight_functionCall.substring(0,openBr);
		return mappings_weight_globalQ_withSUM.get(new String(row+"_"+sp.getSpeciesName()+"_"+funName));
	}
		
	
	
	
	
	public void addMapping_subFunctions_Functions(int row, String functionCall, int index_subfunction) throws Exception {
		int openBr = functionCall.indexOf("(");
		while(openBr!=-1) {
			if(functionCall.charAt(openBr-1)=='\\') { 
				openBr=functionCall.indexOf("(",openBr+1);	
			} else {
				break ;
			}
		}		
		int closedBr = functionCall.lastIndexOf(")");
		
		if(openBr==-1 || closedBr == -1) return;
		
		String funName = functionCall.substring(0,openBr);
		
		Function f = this.getFunctionByName(funName);
		
		String param = functionCall.substring(openBr+1, closedBr);
		StringTokenizer st = new StringTokenizer(param, " ,"); //JUST ONE LEVEL: FUNCTION = 1 + SUBFUNCTION(1,2) ... no FUNCTION = 1 + SUBFUNCTION(SUBSUBFUNCTION(1,2))
		Vector mapping_vector = new Vector();
		mapping_vector.add(f);
		Vector<String> param_names = f.getParametersNames();
		int i = 0;
		while(st.hasMoreTokens()) {
			String actualValue = st.nextToken();
			mapping_vector.add(param_names.get(i));
			mapping_vector.add(actualValue);
			i++;
		}
		mappings_weight_subFunctions_Functions.put(new String(row+"_"+funName+"_"+index_subfunction), mapping_vector);
		return;
	}
	
	public Vector get_mappings_subFunctions_Functions(int row, String subfunctionCall, int index_subfunction) {
		int openBr = subfunctionCall.indexOf("(");
		int closedBr = subfunctionCall.lastIndexOf(")");
		
		if(openBr==-1 || closedBr == -1) return new Vector();
		
		String funName = subfunctionCall.substring(0,openBr);
		return mappings_weight_subFunctions_Functions.get(new String(row+"_"+funName+"_"+index_subfunction));
	}

	public int getIndex(String funName) throws Exception {
		
		funName = extractJustName(funName);
		Iterator<Integer> it = userDefinedFun.keySet().iterator();
		while(it.hasNext()) {
			Integer index = it.next();
			Function f = userDefinedFun.get(index);
			if(f.getName().compareTo(funName)==0) return index;
		}
		return -1;
	}

	public Function getFunctionByIndex(int i) {
		return userDefinedFun.get(new Integer(i));
	}

	
	public Collection<Function> getAllFunctions() {
		List<Function> mapValues = new ArrayList<Function>(this.userDefinedFun.values());
		Collections.sort(mapValues);
		return mapValues;
	}
	
	
	public Collection<Function> getUsedFunctions() {
		Collection<Function> ret = new Vector<Function>();
		HashSet<String> printed = new HashSet<String>();
		
		
		Iterator it2 = this.userDefinedFun.values().iterator();
		while(it2.hasNext()){
			Function f = (Function) it2.next();
			if(printed.contains(f.getName())) continue;
			if(f.getType() == CFunction.PreDefined && f.toShow()==false) continue;
			ret.add(f);
			printed.add(f.getName());
		}
		
		
		
		return ret;
	}
	
	
	
}
