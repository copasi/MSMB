package utility;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;

import org.lsmp.djep.xjep.XJep;
import org.nfunk.jep.Node;
import org.nfunk.jep.OperatorSet;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.SymbolTable;
import org.nfunk.jep.function.*;

import parsers.chemicalReaction.MR_ChemicalReaction_Parser;
import parsers.chemicalReaction.syntaxtree.CompleteReaction;
import parsers.chemicalReaction.visitor.ExtractSubProdModVisitor;
import parsers.mathExpression.MR_Expression_ParserConstantsNOQUOTES;
import parsers.mathExpression.MR_Expression_Parser_ReducedParserException;


import debugTab.*;
import model.*;
import parsers.mathExpression.syntaxtree.*;
import parsers.mathExpression.visitor.*;
import parsers.multistateSpecies.MR_MultistateSpecies_Parser;
import parsers.multistateSpecies.syntaxtree.CompleteMultistateSpecies;
import parsers.multistateSpecies.syntaxtree.CompleteMultistateSpecies_Operator;
import parsers.multistateSpecies.visitor.MultistateSpeciesVisitor;
import parsers.multistateSpecies.visitor.MultistateSpecies_UndefinedSitesVisitor;
import utility.Constants.BooleanType;
import gui.*;


public class CellParsers {
	
	public static XJep parser  = new XJep();
	//public static MR_grammar parser2 = null;
	
	private static HashMap<String, String> cleanedNames = new HashMap<String,String>();
	
	
	public CellParsers() { 
		
		parser.addStandardFunctions(); 
		parser.setAllowUndeclared(true);
		parser.addFunction("SUM",  new MyFunctionClass_DELETE_oldParser(-1));
		parser.addFunction("if", new If());
		parser.addFunction("floor", new MyFunctionClass_DELETE_oldParser(1));
		 
		
		
		/*if(parser2 == null) parser2 = new MR_grammar(new StringReader("a+b->c"));
	
		 try
	      {
	          MR_grammar.one_line();
	          System.out.println("oooooooooooooooooOK.");
	      }
	      catch (Exception e)
	      {
	      if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
	        System.out.println("NOK.");
	        System.out.println(e.getMessage());
	      }
	      catch (Error e)
	      {
	      if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
	        System.out.println("Oops.");
	        System.out.println(e.getMessage());
	      }*/
	}
	
	
	public void addSpecies(MultistateSpecies sp) { parser.addFunction(sp.getSpeciesName(), new MyFunctionClass_DELETE_oldParser(sp.getSitesNames().size()));}
	public void addFunction(Function f) { parser.addFunction(f.getName(), new MyFunctionClass_DELETE_oldParser(f.getParametersNames().size()));	}
	public void addVariable(String name) { parser.addVariable(name, 0.0); }
	public void addConstant(String name) { parser.addConstant(name, 0.0); }
	public Node parse(String expression) throws ParseException { 
		//System.out.println("CellParsers.parse TO ELIMINATE:"+expression);
		return parser.parse(expression);
	}
	public String toString(Node parsedExpression) { return 	parser.toString(parsedExpression); }
	public SymbolTable getSymbolTable() { return parser.getSymbolTable();	}
	public OperatorSet getOperatorSet() { return parser.getOperatorSet();	}
	public Node substitute(Node parsedExpression, String[] names,	Node[] substitutions) throws ParseException {
		return parser.substitute(parsedExpression, names, substitutions);
	}
	
	
	public static boolean isKeyword(String name) {
		
		
		if(name.compareTo(MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstantsNOQUOTES.NAN)) == 0) return true;
		
		if(name.compareTo(MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstantsNOQUOTES.PI)) == 0) return true;
		if(name.compareTo(MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstantsNOQUOTES.DELAY)) == 0) return true;
		if(name.compareTo(MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstantsNOQUOTES.CEIL)) == 0) return true;
		if(name.compareTo(MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstantsNOQUOTES.COS)) == 0) return true;
		if(name.compareTo(MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstantsNOQUOTES.ABS)) == 0) return true;
		if(name.compareTo(MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstantsNOQUOTES.LOG10)) == 0) return true;
			if(name.compareTo(MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstantsNOQUOTES.COSH)) == 0) return true;
		if(name.compareTo(MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstantsNOQUOTES.TAN)) == 0) return true;
		if(name.compareTo(MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstantsNOQUOTES.TANH)) == 0) return true;
		if(name.compareTo(MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstantsNOQUOTES.SIN)) == 0) return true;
		if(name.compareTo(MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstantsNOQUOTES.TYPE_MOD)) == 0) return true;
		if(name.compareTo(MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstantsNOQUOTES.TYPE_PAR)) == 0) return true;
		if(name.compareTo(MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstantsNOQUOTES.TYPE_PROD)) == 0) return true;
		if(name.compareTo(MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstantsNOQUOTES.TYPE_SITE)) == 0) return true;
		if(name.compareTo(MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstantsNOQUOTES.TYPE_SUB)) == 0) return true;
		if(name.compareTo(MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstantsNOQUOTES.TYPE_VAR)) == 0) return true;
		if(name.compareTo(MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstantsNOQUOTES.TYPE_VOL)) == 0) return true;
			
		if(name.compareTo(MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstantsNOQUOTES.TIME)) == 0) return true;
		if(name.compareTo(MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstantsNOQUOTES.FLOOR)) == 0) return true;
		if(name.compareTo(MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstantsNOQUOTES.SQRT)) == 0) return true;
		if(name.compareTo(MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstantsNOQUOTES.EXP)) == 0) return true;
		if(name.compareTo(MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstantsNOQUOTES.LOG)) == 0) return true;
		return false;
	}
	
	public static boolean isMultistateSpeciesName(String name) {
		if(MainGui.importFromSBMLorCPS) {
			//TO FIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIX
			//I WILL NEED TO CHECK THE ANNOTATION
			//AND IF THERE IS NO ANNOTATION, EVEN IF IT FOLLOWS OUR SYNTAX I SHOULD SAY IS NOT A MULTIASTATE SPECIES
			return false;
		}
		if(name.startsWith("\"")) return false;
		
		InputStream is;
		try {
			is = new ByteArrayInputStream(name.getBytes("UTF-8"));
			 MR_MultistateSpecies_Parser react = new MR_MultistateSpecies_Parser(is);
			 CompleteMultistateSpecies_Operator start = react.CompleteMultistateSpecies_Operator();
			 MultistateSpeciesVisitor v = new MultistateSpeciesVisitor(null);
			 start.accept(v);
			 return v.isRealMultiStateSpecies(); 
		} catch (Exception e1) {
			try {
				is = new ByteArrayInputStream(name.getBytes("UTF-8"));
			
				MR_MultistateSpecies_Parser react = new MR_MultistateSpecies_Parser(is);
			 CompleteMultistateSpecies start = react.CompleteMultistateSpecies();
			 MultistateSpeciesVisitor v = new MultistateSpeciesVisitor(null);
			 start.accept(v);
			 return v.isRealMultiStateSpecies(); 
			 
			} catch (Exception e) {
				//e.printStackTrace();
				return false;
			}
		}

	
	}

	
	public static String replaceVariableInExpression(String original, String find, String replacement) {
		
		try {
			

			//System.out.println(".................................");
			InputStream is = new ByteArrayInputStream(original.getBytes("UTF-8"));
			MR_Expression_Parser_ReducedParserException parser = new MR_Expression_Parser_ReducedParserException(is);
			CompleteExpression root = parser.CompleteExpression();
			MR_SubstitutionVisitor mySV= new MR_SubstitutionVisitor(find, replacement);
			root.accept(mySV);
			String newExpr = mySV.getNewExpression();
			//System.out.println(newExpr);
			InputStream is2 = new ByteArrayInputStream(newExpr.getBytes("UTF-8"));
			parser = new MR_Expression_Parser_ReducedParserException(is2);
			root = parser.CompleteExpression();
			String newExprParsed = ToStringVisitor.toString(root);
			
			//Node parsedExpression = CellParsers.parser.parse(newExprParsed);
			//newExprParsed = CellParsers.parser.toString(parsedExpression);
			
			//System.out.println(newExprParsed);
			//System.out.println(".................................");
			
			
			return newExprParsed;
			
		} catch (Exception e) {
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES)
				e.printStackTrace();
			}
			
		return null;
	 }

	public static Vector<Vector<String>> parseExpression_getUndefMisused_2(MultiModel m, String expression, String table_descr, String column_descr) throws MySyntaxException, Exception {
		 Vector ret = new Vector();
		 expression = expression.trim();
		 if(expression.length()==0) return ret;
		 int column_tab = -1;
			if(table_descr.compareTo(Constants.TitlesTabs.SPECIES.description)== 0) {
				column_tab = Constants.SpeciesColumns.getIndex(column_descr);
			} else if(table_descr.compareTo(Constants.TitlesTabs.GLOBALQ.description)== 0) {
				column_tab = Constants.GlobalQColumns.getIndex(column_descr);
			}  else if(table_descr.compareTo(Constants.TitlesTabs.COMPARTMENTS.description)== 0) {
				column_tab = Constants.CompartmentsColumns.getIndex(column_descr);
			}
		 try{
			
		    if(expression.length() >0) {
				  InputStream is = new ByteArrayInputStream(expression.getBytes("UTF-8"));
				  MR_Expression_Parser_ReducedParserException parser = new MR_Expression_Parser_ReducedParserException(is);
				  CompleteExpression root = parser.CompleteExpression();
				  Look4UndefinedMisusedVisitor undefVisitor = new Look4UndefinedMisusedVisitor(m);
				  root.accept(undefVisitor);
				  Vector<String> undef = undefVisitor.getUndefinedElements();
				  Vector<String> misused = undefVisitor.getMisusedElements();
				  
				  ret.add(undef);
				  ret.add(misused);
				  
				  if(undef.size() != 0 || misused.size() != 0) {
					    String message = new String();
						if(undef.size() >0) {
							 message += "The following elements are used but never declared: " + undef.toString();
						}
						if(misused.size() > 0) message += System.lineSeparator() + "The following elements are misused: " +misused.toString();
						throw new MySyntaxException(column_tab, message,table_descr);
				  } 
				  
				
			  }
		 } catch (parsers.mathExpression.ParseException e) {
			 
			 
			 //e.printStackTrace();
			 throw new MySyntaxException(column_tab, e.getMessage(),table_descr);
		}
		return ret;
	}
		    
		    
		    
	public static Vector<Vector<String>> parseExpression_getUndefMisused(MultiModel m, String expression, String table_descr, String column_descr) throws MySyntaxException {
		
		try {
			return parseExpression_getUndefMisused_2(m, expression,table_descr,column_descr);
		} catch (Exception e) {
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
			if(!(e instanceof MySyntaxException)){ 
				if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
			}
			else {
				throw (MySyntaxException)e;
			}
		}
		return null;
		
	/*	if(expression.length() == 0) return new Vector();
		Node parsedExpression = null;
		try {
			parsedExpression = FunctionsDB.parser.parse(expression);
			if(parsedExpression==null) throw new ParseException();
			ExpressionsVisitor visitor = new ExpressionsVisitor();
			Vector<Vector<String>> undef_misused = visitor.getBothUndefinedAndMisusedElements(parsedExpression);
			Vector<String> undef = undef_misused.get(0);
			Vector<String> misused = undef_misused.get(1);
			
			if(undef.size() != 0 || misused.size() != 0) {
				String message = new String();
				if(undef.size() >0) message += "The following elements are used but never declared: " + undef.toString();
				if(misused.size() > 0) message += System.lineSeparator() + "The following elements are misused: " +misused.toString();
				int column_tab = -1;
				if(table_descr.compareTo(Constants.TitlesTabs.SPECIES.description)== 0) {
					column_tab = Constants.SpeciesColumns.getIndex(column_descr);
				} else if(table_descr.compareTo(Constants.TitlesTabs.GLOBALQ.description)== 0) {
					column_tab = Constants.GlobalQColumns.getIndex(column_descr);
				}  else if(table_descr.compareTo(Constants.TitlesTabs.COMPARTMENTS.description)== 0) {
					column_tab = Constants.CompartmentsColumns.getIndex(column_descr);
				}
				throw new MySyntaxException(column_tab, message,table_descr);
			}
			return undef_misused;
				
		} catch (ParseException ex) {
			//ex.printStackTrace();
			//System.out.println("Problems with token:"+ex.currentToken);
			String message = new String(ex.getErrorInfo());
			if(ex.getErrorInfo().contains("Unexpected \",\"") || ex.getErrorInfo().contains("implicit multiplication")) {
				message += System.lineSeparator() + "Are you maybe trying to use a function that has not been defined?";
			}
			int column_tab = -1;
			if(table_descr.compareTo(Constants.TitlesTabs.SPECIES.description)== 0) {
				column_tab = Constants.SpeciesColumns.getIndex(column_descr);
			} else if(table_descr.compareTo(Constants.TitlesTabs.GLOBALQ.description)== 0) {
				column_tab = Constants.GlobalQColumns.getIndex(column_descr);
			}  else if(table_descr.compareTo(Constants.TitlesTabs.COMPARTMENTS.description)== 0) {
				column_tab = Constants.CompartmentsColumns.getIndex(column_descr);
			}
			throw new MySyntaxException(column_tab, message,table_descr );
		}*/
	}
	
/*	public static String cleanName_2(String objectName) {
		Object existing = cleanedNames.get(objectName);
		if(existing != null) return existing.toString();
		
		InputStream is;
		String ret = new String(objectName);
		try {
			is = new ByteArrayInputStream(new String("").getBytes("UTF-8"));
			MR_Expression_Parser parser = new MR_Expression_Parser(is);
			for(int i = 0; i < parser.escapable.length; i++) {
				ret = ret.replace(parser.escapable[i], '\\'+parser.escapable[i]);
			}
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}
		
		ret = StringEscapeUtils.unescapeJava(ret);
		cleanedNames.put(objectName, ret);
		return ret;
	}*/
	
	
	/*public static String oldCleanName (String objectName, boolean species){
		if(objectName.contains("(") && species) {
			//existing names with ( could not be compliant to multiremi syntax
			try {
				MultistateSpecies temp = new MultistateSpecies(objectName);
				return temp.printCompleteDefinition(); /// TOOOOOOOOOOOO CHECK
			} catch (Exception e) {
				if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
				objectName = objectName.replace("(","_");
				objectName = objectName.replace(")", "_");
				objectName = objectName.replace("{", "_");
				objectName = objectName.replace("}", "_");
				objectName = objectName.replace(":", "_");
				objectName = objectName.replace(",", "_");
			}
			
		}
		
		String ret = objectName.replaceAll(" ", "_");
		
		//because those names can be used in mathematical expressions
		ret =  ret.replace("-", "_");
		ret =  ret.replace("+", "_");
		ret =  ret.replace("*", "_");
		ret =  ret.replace("/", "_");
		ret =  ret.replace("'", "_pr");
		ret =  ret.replace("`", "_pr");
		ret =  ret.replace("&", "_");
				
		if(!species) {
			ret =  ret.replace("(", "_");
			ret =  ret.replace(")", "_");
		}
		return ret;
	}*/
	
	
	public static String cleanName(String objectName, boolean species) {
		//return cleanName_2(objectName);
		//return oldCleanName(objectName, species);
		
		if(!objectName.startsWith("\"")) {
			if(objectName.indexOf(' ')!=-1 ||
					objectName.indexOf('+')!=-1 ||
					objectName.indexOf('-')!=-1 ||
					objectName.indexOf('*')!=-1 ||
					objectName.indexOf('#')!=-1 ||
					objectName.indexOf('/')!=-1 ||
					objectName.indexOf('^')!=-1) {
				return new String("\""+objectName+"\"");
			}
			
			if(objectName.indexOf('\"')!=-1 ){
				return new String(objectName.replace("\"", "''"));
			}
			
			if(isKeyword(objectName) && objectName.compareTo(MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstantsNOQUOTES.TIME)) != 0) {
				return new String("\""+objectName+"\"");
			}
			if(!CellParsers.isMultistateSpeciesName(objectName) && 
					(objectName.indexOf('(')!=-1 
					|| objectName.indexOf(':')!=-1 
					|| objectName.indexOf(',')!=-1
					|| objectName.indexOf(';')!=-1
					|| objectName.indexOf('{')!=-1
					|| objectName.indexOf('}')!=-1)) {
				return new String("\""+objectName+"\"");
			}
			
			if(objectName.indexOf('.') != -1) {
				//TO CHECK IF IT'S OK WITH NAME WITH EXTENSIONS IN EXPRESSIONS
				return new String("\""+objectName+"\"");
			}
			
			else return objectName;
		}
		else return objectName;
	}
	
	public static String cleanName(String objectName) {
		return cleanName(objectName,false);
	}
	
	public static String parseMultistateSpecies_product(String multiStateProd, HashMap multiStateReact) {
			if(multiStateProd.contains("succ") || multiStateProd.contains("pred")) { 
				// o il nome di un sito specifico senza il nuovo stato... 
				//(cdh1[p]) significa che ci deve essere tra i reagenti... 
				//se invece e cdh1[q^20] allora puo non esserci tra i reagenti
				
				StringTokenizer st = new StringTokenizer(multiStateProd, "[];");
				String name = new String((String)st.nextToken());
				if(!multiStateReact.containsKey(name))	{ return new String(); }			
				
				while(st.hasMoreTokens()) {
				    		String site = (String)(st.nextToken()).trim();
				    		if(site.contains("^")) { continue; }
				    		int start = 0;
				    		int end = site.length();
				    		if(site.contains("succ") || site.contains("prec")) {
				    			start = site.indexOf("(") + 1;
				    			end = site.indexOf(")");
				    		}
				 			String name_site = site.substring(start, end);
			    			MultistateSpecies react = (MultistateSpecies)multiStateReact.get(name);
			    			if(!react.getSitesNames().contains(name_site)) { return new String(); }
			  }
				
				
				
				return multiStateProd;
			}
			
			return multiStateProd;
	}

	public static Vector parseMultistateSpecies_states(String states) {
		StringTokenizer st_states = new StringTokenizer(states, ",");
		Vector single_states = new Vector();
		boolean foundTrue = false;
		boolean foundFalse = false;
		while(st_states.hasMoreTokens()) {
				String s = (String)st_states.nextToken();
				if(BooleanType.isTrue(s)
						//s.compareTo(Constants.BooleanType.TRUE.description) ==0 
						//|| s.compareTo(Constants.BooleanType.TRUE_lower.description) ==0
						){
					foundTrue = true;
				}
				if(BooleanType.isFalse(s)
						//s.compareTo(Constants.BooleanType.FALSE.description) ==0 
						//|| s.compareTo(Constants.BooleanType.FALSE_lower.description) ==0
						){
					foundFalse = true;
				}
				single_states.add(s);
		}
		if((foundFalse||foundTrue) && single_states.size()!=2) {
			return null;
		}
		return single_states;
	}
	
	private static int findRealIndexModifiersSeparator(String reaction) {
		boolean multistate_started = false;
		boolean multistate_ended = false;
		boolean semicolon = false;
		
		for(int i = 0; i < reaction.length(); i++) {
			if(reaction.charAt(i) == '(') {
				multistate_started = true; continue;
			}
			
			if(reaction.charAt(i) == ';' && multistate_started==true) {
				semicolon = true; continue;
			} else {
				if(reaction.charAt(i) == ';' && multistate_started==false) {
					return i;
				}
			}
			if(reaction.charAt(i) == ')') {
				multistate_ended = true;
				multistate_started = false;
				semicolon = false;
			}
		}
		return reaction.length();
	} 
	
	private static Vector extractModifiers(MultiModel m,String modifiersList) throws Exception {
		Vector mod = new Vector();
		
		StringTokenizer st_modifiers = new StringTokenizer(modifiersList," ,");
		while(st_modifiers.hasMoreTokens()) {
					String species = (String)(st_modifiers.nextToken());
			    	String mod_to_add = new String(species);
			    	if(CellParsers.isMultistateSpeciesName(species)) {
			    		if(!species.contains(")")) {
			    			throw new Exception("Only single multisite species states can be used as modifier. No ranges (:) or list (,) operators are allowed");
			    		}
			    		MultistateSpecies r = new MultistateSpecies(m,species); //just to use the parser in the constructor
			    		mod_to_add = r.printCompleteDefinition();
			    		if(mod_to_add.trim().length() <= 0) throw new Exception("PROBLEMS PARSING MODIFIERS");
				 	}	
			    	mod.add(mod_to_add);
				}
			return mod;
		}

	public static Vector parseReaction_2(MultiModel m, String reaction_complete, int row) throws Exception {
		Vector subs_prod_mod = new Vector();
		
		
		
	//	MainGui.clear_debugMessages_relatedWith_table_col_priority(Constants.TitlesTabs.REACTIONS.description, Constants.ReactionsColumns.REACTION.index, DebugConstants.PriorityType.PARSING.priorityCode);
		
		try {
			InputStream is = new ByteArrayInputStream(reaction_complete.getBytes("UTF-8"));
			MR_ChemicalReaction_Parser react = new MR_ChemicalReaction_Parser(is);
		  	CompleteReaction start = react.CompleteReaction();
		  	ExtractSubProdModVisitor v = new ExtractSubProdModVisitor(m);
		    start.accept(v);
		    
		    if(v.getExceptions().size() != 0) {
		    	throw new Exception(v.getExceptions().get(0).getMessage());
		    }
		   
		    subs_prod_mod.addAll(v.getAll_asString());
		      
		} catch(Throwable ex) {
			//ex.printStackTrace();
			
			 DebugMessage dm = new DebugMessage();
			 dm.setOrigin_table(Constants.TitlesTabs.REACTIONS.description);
			 dm.setProblem("Reaction not following the correct syntax. Common causes: missing blank separator or quotes."+ex.getMessage());
			 dm.setOrigin_row(row+1);
			 dm.setOrigin_col(Constants.ReactionsColumns.REACTION.index);
			 dm.setPriority(DebugConstants.PriorityType.PARSING.priorityCode);
			 MainGui.addDebugMessage_ifNotPresent(dm);
			 throw new Exception("Problem parsing reaction");
		}

		
		return subs_prod_mod;
		
	}
	
	public static Vector parseReaction(MultiModel m, String reaction_complete, int row) throws Exception{
		return parseReaction_2(m, reaction_complete.trim(), row);
	}
	
	
	
	/*public static Vector parseReaction_old(String reaction_complete, int row) throws Exception{
		if(reaction_complete.trim().length() ==0) return new Vector();
				Vector subs_prod_mod = new Vector();
			
			Vector subs = new Vector();
			Vector prod = new Vector();
			Vector mod = new Vector();
			
			MainGui.clear_debugMessages_relatedWith_table_col_priority(Constants.TitlesTabs.REACTIONS.description, Constants.ReactionsColumns.REACTION.index, DebugConstants.PriorityType.PARSING.priorityCode);
			
			
		String modifiers = new String();
		
		int index_modifiers_list = reaction_complete.lastIndexOf(";");
		
		String reaction = new String();
		if(index_modifiers_list != -1) { //can contain modifiers
			//but ; is also used in multistate species.
			//so I have to split it manually and fill the modifiers appropriately
			int index_semicolon_modifier = findRealIndexModifiersSeparator(reaction_complete);
			reaction = reaction_complete.substring(0, index_semicolon_modifier);
			if(index_semicolon_modifier < reaction_complete.length()) {
				mod.addAll(extractModifiers(reaction_complete.substring(index_semicolon_modifier+1)));
			}
		} else {  //no modifiers
			reaction = new String(reaction_complete);
		}
		
		StringTokenizer st_reactants_products = new StringTokenizer(reaction.trim(), "->");
		
		if(!reaction.contains("->")) {
		    DebugMessage dm = new DebugMessage();
		    dm.setOrigin_table(Constants.TitlesTabs.REACTIONS.description);
		   dm.setProblem("Reaction not following the correct syntax: missing -> element");
		   dm.setOrigin_row(row);
		   dm.setOrigin_col(Constants.ReactionsColumns.REACTION.index);
			dm.setPriority(DebugConstants.PriorityType.PARSING.priorityCode);
			MainGui.addDebugMessage_ifNotPresent(dm);
			throw new Exception("Problem parsing reaction");
		}
		
		String reactants = new String();
		String products = new String();
		
		
		if(reaction.trim().indexOf("->") > 0) {
			reactants = (String)st_reactants_products.nextToken().trim();
		}
		
		if(st_reactants_products.hasMoreElements()) {
			products = (String)st_reactants_products.nextToken().trim();
		}
		
		StringTokenizer st_species_reactants = new StringTokenizer(reactants, " +*");
		
		HashMap multiStateReactants = new HashMap();
		
		int compacted_reactants_with_stoichiometry = 0;
		int compacted_products_with_stoichiometry = 0;
		
		double stoic = 1.0;
		
		while(st_species_reactants.hasMoreTokens()) {
		    	String species = (String)(st_species_reactants.nextToken());
		    	try{
		    		stoic = Double.parseDouble(species);
		    		species = (String)(st_species_reactants.nextToken());
		    	} catch(Exception ex) { //no stoichiometry in this species, just the classical one
		    		if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) ex.printStackTrace();
		    	} finally {
		    		String subs_to_add = new String(species);
			    	if(species.contains("(")) {
			    		MultistateSpecies r = new MultistateSpecies(species);
			    		multiStateReactants.put(r.getSpeciesName(), r);
			    		subs_to_add = r.printCompleteDefinition();
			    		if(subs_to_add.trim().length() <= 0) throw new Exception("PROBLEMS PARSING REACTANT");
				 	}	
			    	if(stoic != 1.0) {
			    		subs.add(new Double(stoic) + "*" + subs_to_add);
			    		compacted_reactants_with_stoichiometry++;
			    		stoic = 1.0;
			    	} else {
			    		subs.add(subs_to_add);
			    		compacted_reactants_with_stoichiometry++;
			    	}
			    	
			    	/*for(int i = 0; i < stoic; i++) {
			    		subs.add(subs_to_add);
			    		compacted_reactants_with_stoichiometry++;
			    	}*
			    	compacted_reactants_with_stoichiometry--;
		    	}
		    
		}
		
		stoic = 1.0;
		StringTokenizer st_species_product = new StringTokenizer(products, " +*");
		while(st_species_product.hasMoreTokens()) {
	    	String species = (String)(st_species_product.nextToken());
	    
	    	try{
	    		stoic = Double.parseDouble(species);
	    		species = (String)(st_species_product.nextToken());
	    	} catch(Exception ex) {  //no stoichiometry in this species, just the classical one
	    		if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) ex.printStackTrace();
	    	} finally {
	    		String prod_to_add = new String(species);
	    		/*if(species.contains("(")) {
	    		prod_to_add = parseMultistateSpecies_product(species, multiStateReactants);
	    		if(prod_to_add.trim().length() <= 0) throw new Exception("PROBLEMS PARSING PRODUCT");
	 	    	}*

	    		if(prod_to_add.length() > 0)	{ 
	    			/*for(int i = 0; i < stoic; i++) {
	    				prod.add(prod_to_add); 
	    				compacted_products_with_stoichiometry++;
	    			}
	    			compacted_products_with_stoichiometry--;*
	    			if(stoic != 1.0) {
	    				prod.add(new Double(stoic) + "*" + prod_to_add);
	    				compacted_products_with_stoichiometry++;
			    		stoic = 1.0;
			    	} else {
			    		prod.add(prod_to_add);
			    		compacted_products_with_stoichiometry++;
			    	}
	    			compacted_products_with_stoichiometry--;
	    		}
	    	}
    	}
		
		subs_prod_mod.add(subs);
		subs_prod_mod.add(prod);
		subs_prod_mod.add(mod);
		
		int count_plus_reactants = 0;
		int count_plus_products = 0;
		
		
		int lastIndex = 0;
		while(lastIndex != -1){
		       lastIndex = reactants.indexOf("+",lastIndex+1);
		       if( lastIndex != -1){  count_plus_reactants ++;	      }
		}
		
		lastIndex = 0;
		while(lastIndex != -1){
		       lastIndex = products.indexOf("+",lastIndex+1);
		       if( lastIndex != -1){  count_plus_products ++;	      }
		}
		
		
		if(count_plus_reactants != 0 && count_plus_reactants != subs.size()-1-compacted_reactants_with_stoichiometry ) {
		    DebugMessage dm = new DebugMessage();
			//dm.setOrigin_cause("Parsing reaction error");
			dm.setProblem("Reaction not following the correct syntax: too many + characters between the reactants");
			dm.setPriority(DebugConstants.PriorityType.MAJOR.priorityCode);
			MainGui.addDebugMessage_ifNotPresent(dm);
			throw new Exception("Problem parsing reaction");
		}
		
		if(count_plus_products != 0 && count_plus_products != prod.size()-1-compacted_products_with_stoichiometry ) {
		    DebugMessage dm = new DebugMessage();
			//dm.setOrigin_cause("Parsing reaction error");
			dm.setProblem("Reaction not following the correct syntax: too many + characters between the products");
			dm.setPriority(DebugConstants.PriorityType.MAJOR.priorityCode);
			MainGui.addDebugMessage_ifNotPresent(dm);
			throw new Exception("Problem parsing reaction");
		}
		
			
		return subs_prod_mod;
	}*/

	public static String cleanMathematicalExpression(String mathematicalExpression) {
		String ret = new String(mathematicalExpression);
		
		ret = ret.replace(" eq ", "==");
		ret = ret.replace(" ge ", ">=");
		ret = ret.replace(" le ", "<=");
		ret = ret.replace(" gt", ">");
		ret = ret.replace(" lt", "<");
		ret = ret.replace(" and ", "&&");
		ret = ret.replace(" or ", "||");
		
		try {
			InputStream is = new ByteArrayInputStream(mathematicalExpression.getBytes("UTF-8"));
			MR_Expression_Parser_ReducedParserException parser = new MR_Expression_Parser_ReducedParserException(is);
			CompleteExpression root = parser.CompleteExpression();
			QuoteKeywordInExpressionVisitor quoted = new QuoteKeywordInExpressionVisitor();
			root.accept(quoted);
			ret = quoted.getNewExpression();
			
		} catch (Exception e) {
			
		}
	/*	int index_open = mathematicalExpression.indexOf("\"");
		if(index_open == -1) return ret;
		
		ret = new String();
		int index_close = mathematicalExpression.indexOf("\"",index_open+1);
		
		
		ret += mathematicalExpression.substring(0,index_open);
		
		String cleanName = CellParsers.cleanName(mathematicalExpression.substring(index_open+1, index_close));

		ret += cleanName;
		
		ret += cleanMathematicalExpression(mathematicalExpression.substring(index_close+1));
		
		ret = ret.replace(" eq ", "==");
		ret = ret.replace(" ge ", ">=");
		ret = ret.replace(" le ", "<=");
		ret = ret.replace(" gt", ">");
		ret = ret.replace(" lt", "<");
		ret = ret.replace(" and ", "&&");
		ret = ret.replace(" or ", "||");
		
		
		*/
		
		
		
		return ret;
	}


	public static boolean isMultistateSpeciesName_withUndefinedStates(String name) {
		if(name.trim().length() ==0) return false;
		if(name.startsWith("\"")) return false;
		
		InputStream is;
		try {
			is = new ByteArrayInputStream(name.getBytes("UTF-8"));
			 MR_MultistateSpecies_Parser react = new MR_MultistateSpecies_Parser(is);
			 CompleteMultistateSpecies start = react.CompleteMultistateSpecies();
			 MultistateSpecies_UndefinedSitesVisitor v = new MultistateSpecies_UndefinedSitesVisitor(null);
			 start.accept(v);
			 return v.isMultistateSpeciesName_withUndefinedStates(); 
		} catch (Exception e1) {
			e1.printStackTrace();
			try {
				is = new ByteArrayInputStream(name.getBytes("UTF-8"));
			
				MR_MultistateSpecies_Parser react = new MR_MultistateSpecies_Parser(is);
			 CompleteMultistateSpecies_Operator start = react.CompleteMultistateSpecies_Operator();
			 MultistateSpecies_UndefinedSitesVisitor v = new MultistateSpecies_UndefinedSitesVisitor(null);
			 start.accept(v);
			 return v.isMultistateSpeciesName_withUndefinedStates(); 
			 
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}

	}
	
	
	
	

}
