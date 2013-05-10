package msmb.model;

import msmb.debugTab.DebugMessage;
import msmb.gui.MainGui;

import java.util.Vector;

import msmb.utility.CellParsers;
import msmb.utility.Constants;
import msmb.utility.MySyntaxException;

import org.COPASI.*;



public class GlobalQ {
	String name = new String();
	int type = CModelValue.FIXED;
	
	String initialValue = MainGui.globalQ_defaultValue_for_dialog_window;
	String expression = new String();
	String notes = new String();
	
	private String editableValue = new String();
	private String editableExpression = expression;
	public String getEditableValue() {	return editableValue; }
	public String getEditableExpression() {	return editableExpression; }
	
	public String getName() {		return name;	}
	public void setName(String name) {		this.name = name;	}
	
	public int getType() {	return type;	}

	public void setType(String type_string) {
		this.type = Constants.GlobalQType.getCopasiTypeFromDescription(type_string);
	}
	
	public String getNotes() {		return notes;	}
	public void setNotes(String notes) {		this.notes = notes;	}

	public String getInitialValue() {		return initialValue;	}
	
	public void setInitialValue(MultiModel m, String initialValue) throws Throwable {	

		if(initialValue.length()==0) return;
		this.initialValue = initialValue;	
		try{
			Double.parseDouble(initialValue);
			return;
		} catch (Throwable e) {// not a number, expression... so let's try to parse it
			
			try {
				Vector<Vector<String>> undef_misused = CellParsers.parseExpression_getUndefMisused(m,initialValue, Constants.TitlesTabs.GLOBALQ.description,Constants.GlobalQColumns.VALUE.description);
			} catch (Throwable ex) {
				throw ex;
			}
		}
		 finally {
			 editableValue = getInitialValue();
		}
			
	}
	
	
	public String getExpression() {		return expression;	}
	
	//public void setExpression(String expression) {		this.expression = expression;	}
	
	public void setExpression(MultiModel m, String expr) throws Throwable {	
		if(expr.compareTo(Constants.NOT_EDITABLE_VIEW) == 0) return;
		if(expr.trim().length() == 0) return ;
		try {
			this.expression = expr;	
			 CellParsers.parseExpression_getUndefMisused(m,expression, Constants.TitlesTabs.GLOBALQ.description,Constants.GlobalQColumns.EXPRESSION.description);
		
				boolean oldImportFromSBMLorCPS = MainGui.importFromSBMLorCPS;
				MainGui.importFromSBMLorCPS = false;
				this.expression = m.reprintExpression_forceCompressionElements(expression,Constants.TitlesTabs.GLOBALQ.description,Constants.GlobalQColumns.EXPRESSION.description);
				MainGui.importFromSBMLorCPS = oldImportFromSBMLorCPS;
		} catch (Throwable ex) {
			throw ex;
		}
		finally {
			editableExpression = expr;
		}
	}
	


	public GlobalQ() { name = "";}
	public GlobalQ(String name) { setName(name);}
	
	
	
	public Vector<Object> getAllFields() {
		Vector<Object> r = new Vector<Object>();
		r.add(this.getName());
		r.add(this.getInitialValue());
		r.add(this.getType());
		r.add(this.getExpression());
		r.add(this.getNotes());
		return r;
	}
	
	public void setExpression_withoutParsing(String expression2) {
		this.expression = expression2;
	}
}
