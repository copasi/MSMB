package model;

import java.util.Vector;
import utility.*;


public class Species {
	String name = new String();
	String initialQuantity = new String();
	private String editableInitialQuantity = initialQuantity;
	int type = Constants.SpeciesType.REACTIONS.copasiType;
	String compartment = new String(); 
	String expression = new String();
	private String editableExpression = expression;
	public String getExpression() { 	return expression.trim();	}
	
	

	
	public void setExpression(MultiModel m, String expr) throws MySyntaxException {	
		if(expr.compareTo(Constants.NOT_EDITABLE_VIEW) == 0) return;
		if(expr.length() == 0) return ;
		try {
			this.expression = expr;	
			CellParsers.parseExpression_getUndefMisused(m,expression, Constants.TitlesTabs.SPECIES.description,Constants.SpeciesColumns.EXPRESSION.description);
		} catch (Exception ex) {
			throw ex;
		}
			editableExpression = expr;

	}

	
	public String getEditableInitialQuantity() {	return editableInitialQuantity; }
	public String getEditableExpression() {	return editableExpression; }
	
	String notes = new String();
	String SBMLid = new String();

	public String getSBMLid() {
		return SBMLid;
	}
	public void setSBMLid(String sBMLid) {
		SBMLid = sBMLid;
	}
	
	public Species() {}
	public Species(String name) { this.setName(name);}
	
	public Vector getAllFields() { /// should have the field ordered according to the columns in the tables
		Vector r = new Vector();
		r.add(this.getDisplayedName());
		r.add(this.getInitialQuantity());
		r.add(this.getType());
		r.add(this.getCompartment());
		r.add(this.getNotes());
		r.add(this.getSBMLid());
		return r;
	}
	
	public String getSpeciesName() { return name; }
	public String getDisplayedName() { 	return getSpeciesName();	}
	public void setName(String name) {	this.name = name;	}
	
	public String getInitialQuantity() {	return initialQuantity; }
	
	
	public void setInitialQuantity(MultiModel m, String initialQ) throws MySyntaxException {	
		if(m==null) return;
		if(initialQ.compareTo(Constants.NOT_EDITABLE_VIEW) == 0) return;
		if(initialQ.length()==0) return;
		this.initialQuantity = initialQ;	
		try{
			Double.parseDouble(initialQ);
		
		} catch (Exception e) {// not a number, expression... so let's try to parse it
			try {
				CellParsers.parseExpression_getUndefMisused(m,initialQuantity, Constants.TitlesTabs.SPECIES.description,Constants.SpeciesColumns.INITIAL_QUANTITY.description);
			} catch (Exception ex) {
				throw ex;
			}
		}
		
			editableInitialQuantity = initialQ;
	
		
	}
	
	public int getType() {
			return type;
	}
	
	public void setType(int CMetab_Type) {	
		this.type = CMetab_Type;
	}
	
	public String getCompartment() {		return compartment; }
	
	public void setCompartment(MultiModel m, String compartment) throws MySyntaxException {	
		if(compartment == null) return;
		if(m==null) return;
		if(compartment.compareTo(Constants.NOT_EDITABLE_VIEW) == 0) return;
		if(compartment.length()==0) return;
		try {
			Vector<Vector<String>> undef_misused = CellParsers.parseExpression_getUndefMisused(m,compartment, Constants.TitlesTabs.SPECIES.description,Constants.SpeciesColumns.COMPARTMENT.description);
		} catch (Exception ex) {
			if(m.getComp(compartment)!=null) {
				this.compartment = compartment;	
			}
			throw ex;	
		}	
		
		if(m.getComp(compartment)!=null) {
			this.compartment = compartment;	
		} else {
			throw new MySyntaxException(Constants.SpeciesColumns.COMPARTMENT.index, "Compartment  \""+ compartment+"\" is not defined.", Constants.TitlesTabs.SPECIES.description);
		}
	}

	public String getNotes() {		return notes;	}
	public void setNotes(String notes) {		this.notes = notes;	}
	
	
	
	public String getValueOfSite(String siteName) {
		if(!this.getDisplayedName().contains("(")) {
			return new String();
		} 
		
		String completeName = this.getDisplayedName();
		int index_site = completeName.indexOf(siteName+"{");
		String sub = completeName.substring(index_site+siteName.length()+1);
		
		int index_end_species = sub.indexOf(")");
		int index_semicolon = sub.indexOf(";");
		String value = new String();
		if(index_semicolon != -1) {
			value = sub.substring(0,index_semicolon);
		} else {
			value = sub.substring(0, index_end_species);
		}
		return value;
	}




	public void setEditableInitialQuantity(MultiModel m, String editableString) throws MySyntaxException {
		setInitialQuantity(m,editableString);
	}

	public void setEditableExpression(MultiModel m,String editableString) throws MySyntaxException {
		setExpression(m,editableString);		
	}



	public void setExpression_withoutParsing(String expression) {
		this.expression = expression;
	}





	
}
