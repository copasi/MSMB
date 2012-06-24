package model;

import gui.MainGui;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Vector;

import parsers.mathExpression.syntaxtree.CompleteExpression;
import parsers.mathExpression.visitor.Look4UndefinedMisusedVisitor;

import org.nfunk.jep.Node;

import parsers.mathExpression.MR_Expression_Parser;

import utility.*;

public class GlobalQDB {
	
	TreeMap<Integer, GlobalQ> globalQVector = new TreeMap<Integer, GlobalQ>();
	HashMap<String, Integer> globalQIndexes = new HashMap<String, Integer>();
	MultiModel multiModel = null;
	
	public GlobalQDB(MultiModel mm) {
		globalQVector = new TreeMap<Integer, GlobalQ>();
		globalQIndexes = new HashMap<String, Integer>();
		globalQVector.put(0,null);//rows starts at index 1
		multiModel = mm;
	}
	
	public Vector<GlobalQ> getAllGlobalQ() {
		return new Vector(this.globalQVector.values());
	}
	
	public Vector<String> getAllNames() {
		Vector n = new Vector();
		for(int i = 0; i < globalQVector.size(); i++) {
			GlobalQ s = globalQVector.get(i);
			if(s!=null)n.add(s.getName());
		}
		return n;
	}
	
	
	public GlobalQ getGlobalQ(String name) {
		if(globalQIndexes.get(name)==null) return null;
		int ind = globalQIndexes.get(name).intValue();
		return getGlobalQ(ind);
	}
	
	
	public GlobalQ getGlobalQ(int index) {
		if(index < 0 || index >= globalQVector.size()) {
			return null;
		}
		GlobalQ ret = globalQVector.get(index);
		return ret;
	}
	
	public int addChangeGlobalQ(int index, String name,  String initialValue, String type, String expression, String notes) throws Exception {
		if(name.trim().length() == 0) return -1;
		Integer ind = globalQIndexes.get(name);
		

		if(index != -1) {
			GlobalQ old = globalQVector.get(index);
			if(old!= null) {
				multiModel.removeNamedElement(old.getName(), new Integer(Constants.TitlesTabs.GLOBALQ.index));
				globalQIndexes.remove(old.getName());
				CellParsers.parser.removeVariable(old.getName());
				
			}
		}
		
		if(ind != null && ind != index ) { // the name is already assigned to another species
			Throwable cause = new Throwable(name);
			throw new ClassNotFoundException("A globalQ already exists with the name "+name, cause);
		}
		
		
		try{
			if(ind == null) {//it is a new globalq
				GlobalQ c = new GlobalQ(name);
				c.setNotes(notes);
				c.setType(type);
				if(index==-1) ind = globalQVector.size();
				else ind = index;
				globalQIndexes.put(c.getName(), ind);
				globalQVector.put(ind,c); //take the place even if expressions contains error
				multiModel.addNamedElement(c.getName(), Constants.TitlesTabs.GLOBALQ.index);
				c.setExpression(multiModel,expression);
				c.setInitialValue(multiModel,initialValue);
				globalQVector.put(ind,c);
				return globalQVector.size()-1;
			} else { //globalQ already defined
				GlobalQ c = globalQVector.get(ind);
				globalQIndexes.put(name, ind);
				multiModel.addNamedElement(name, Constants.TitlesTabs.GLOBALQ.index);
				c.setExpression(multiModel,expression);
				c.setInitialValue(multiModel,initialValue);
				c.setNotes(notes);
				c.setType(type);
				globalQVector.put(ind,c);
				
				if(!MainGui.donotCleanDebugMessages) MainGui.clear_debugMessages_defaults_relatedWith(Constants.TitlesTabs.GLOBALQ.description, ind);
				return ind;
			}
		} catch (MySyntaxException ex) {
			if(ex.getColumn()==Constants.GlobalQColumns.EXPRESSION.index && expression.trim().length() >0) {
				Vector<String> undef = null;
				if(expression.length() >0) {
					  InputStream is = new ByteArrayInputStream(expression.getBytes("UTF-8"));
					  MR_Expression_Parser parser = new MR_Expression_Parser(is);
					  CompleteExpression root = parser.CompleteExpression();
					  Look4UndefinedMisusedVisitor undefVisitor = new Look4UndefinedMisusedVisitor(multiModel);
					  root.accept(undefVisitor);
					  undef = undefVisitor.getUndefinedElements();
				}
				if(undef != null){
					 if(undef.size()==1 && undef.get(0).compareTo(name)==0) { //just self reference in ode/expression and it is allowed
						return addChangeGlobalQ_withoutParsing(name,  initialValue, type, expression, notes);
					} 
					 
					 else {
						  for(int i = 0; i < undef.size(); i++) {
							 if(undef.get(i).compareTo(name)==0){
								 undef.remove(i);
								 break;
							 }
						 }
						 String message = "The following elements are used but never declared: " + undef.toString();
						 ex = new MySyntaxException(message, ex);
					 }
					throw ex;
				} 

			}
			return -1; 
		}

	}
	
	
	public int addChangeGlobalQ_withoutParsing(String name,  String initialValue, String type, String expression, String notes) throws Exception {
		if(name.trim().length() == 0) return -1;
		if(name.length() == 0) name = "default";
		if(!globalQIndexes.containsKey(name)) { //it is a new comp
			GlobalQ c = new GlobalQ(name);
			c.setExpression_withoutParsing(expression);
			c.setInitialValue(multiModel,initialValue);
			c.setNotes(notes);
			c.setType(type);
			globalQIndexes.put(c.getName(), globalQVector.size());
			multiModel.addNamedElement(c.getName(), Constants.TitlesTabs.GLOBALQ.index);
			globalQVector.put(globalQVector.size(),c);
			return globalQVector.size()-1;
		} else { //globalQ already defined
			Integer ind = globalQIndexes.get(name);
			GlobalQ c = globalQVector.get(ind);
			c.setExpression_withoutParsing(expression);
			c.setInitialValue(multiModel,initialValue);
			c.setNotes(notes);
			c.setType(type);
			globalQIndexes.put(name, ind);
			globalQVector.put(ind,c);
			multiModel.addNamedElement(name, Constants.TitlesTabs.GLOBALQ.index);
			if(!MainGui.donotCleanDebugMessages) MainGui.clear_debugMessages_defaults_relatedWith(Constants.TitlesTabs.GLOBALQ.description, ind);
			return ind;
		}
			
	}

	public boolean contains(String name) {
		return globalQIndexes.containsKey(name);
	}
	
	public int getIndex(String name) {
		return globalQIndexes.get(name);
	}

	public boolean removeGlobalQ(int toBeRemoved) {
			int size = globalQVector.keySet().size();
			globalQIndexes.remove(globalQVector.get(toBeRemoved+1).getName());
			multiModel.removeNamedElement(globalQVector.get(toBeRemoved+1).getName(),new Integer(Constants.TitlesTabs.GLOBALQ.index));
			for(int i = toBeRemoved+1; i < size; i++) {
				GlobalQ succ = globalQVector.get(i+1);
				if(succ==null) {
					globalQVector.remove(globalQVector.size()-1);
					break; 
				}
				globalQVector.put(i, succ);
				globalQIndexes.put(succ.getName(), i);
				
			}
			return true;
	}

}
