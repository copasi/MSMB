package msmb.model;

import java.io.UnsupportedEncodingException;
import java.util.Vector;

import msmb.utility.*;


public class Species  {
	String name = new String();
	Vector<String> initialQuantity = new Vector<String>(); 
	Vector<String> compartment = new Vector<String>(); 
	int type = Constants.SpeciesType.REACTIONS.copasiType;
	String expression = new String();
	private String editableInitialQuantity = new String();
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
		} finally {
			editableExpression = expr;
		}
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
	
	public Species() { initialQuantity.clear(); initialQuantity.add(new String()); }
	public Species(String name) { this.setName(name);  initialQuantity.clear(); initialQuantity.add(new String()); }
	
	public Vector getAllFields() { /// should have the field ordered according to the columns in the tables
		Vector r = new Vector();
		r.add(this.getDisplayedName());
		r.add(this.getInitialQuantity_listString());
		r.add(this.getType());
		r.add(this.getCompartment_listString());
		r.add(this.getNotes());
		r.add(this.getSBMLid());
		return r;
	}
	
	public String getSpeciesName() { return name; }
	public String getDisplayedName() { 	return getSpeciesName();	} //multistate species override this method
	public String getName() { return getDisplayedName(); }
	
	public void setName(String newName) {
		try {
		this.name = new String(newName.getBytes("UTF-8"),"UTF-8");
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}	}
	
	public Vector<String> getInitialQuantity() {	
		return initialQuantity; 
	}
	
	public String getInitialQuantity_listString() {		
		String ret = new String();
		if(initialQuantity.size() > 0) {
			for(int i = 0; i< initialQuantity.size()-1; i++) {
				ret += initialQuantity.get(i)+", ";
			}
			ret += initialQuantity.get(initialQuantity.size()-1);
		}
		return ret; 
	}
	
	
	public void setInitialQuantity(MultiModel m, String initialQ) throws MySyntaxException {	
		if(m==null) return;
		if(initialQ.compareTo(Constants.NOT_EDITABLE_VIEW) == 0) return;
		if(initialQ.length()==0) return;
		
		try{
			Double.parseDouble(initialQ);
			this.initialQuantity.clear();	
			this.initialQuantity.add(initialQ);
		} catch (Exception e) {// not a number, expression... so let's try to parse it
			try {
				CellParsers.parseExpression_getUndefMisused(m,initialQ, Constants.TitlesTabs.SPECIES.description,Constants.SpeciesColumns.INITIAL_QUANTITY.description);
				this.initialQuantity.clear();	
				this.initialQuantity.add(initialQ);
			} catch (Exception ex) {
				//throw ex;
				Vector<String> elements = new Vector<String>();
				try {
					elements = CellParsers.extractElementsInList(m,initialQ, Constants.TitlesTabs.SPECIES.description, Constants.SpeciesColumns.INITIAL_QUANTITY.description);
				} catch (MySyntaxException e1) {
					e1.printStackTrace();
				}
				this.initialQuantity.clear();	
				
				for(int i = 0; i < elements.size(); i++) {
					this.initialQuantity.add(elements.get(i));
				}
			}
		} finally {

			editableInitialQuantity = getInitialQuantity_listString();
		}
		
	
		
		
		
	}
	
	public int getType() {
			return type;
	}
	
	public void setType(int CMetab_Type) {	
		this.type = CMetab_Type;
	}
	
	public String getCompartment_listString() {		
		String ret = new String();
		if(compartment.size() > 0) {
			for(int i = 0; i< compartment.size()-1; i++) {
				ret += compartment.get(i)+", ";
			}
			ret += compartment.get(compartment.size()-1);
		}
		return ret; 
	}
	
	public Vector<String> getCompartments() {		
		return this.compartment; 
	}
	
	public void setCompartment(MultiModel m, String compartment) throws MySyntaxException {	
		if(compartment == null) return;
		if(m==null) return;
		if(compartment.compareTo(Constants.NOT_EDITABLE_VIEW) == 0) return;
		if(compartment.length()==0) return;
		try {
			//Vector<Vector<String>> undef_misused = CellParsers.parseExpression_getUndefMisused(m,compartment, Constants.TitlesTabs.SPECIES.description,Constants.SpeciesColumns.COMPARTMENT.description);
			Vector<Vector<String>> undef_misused = CellParsers.parseListExpression_getUndefMisused(m,compartment, Constants.TitlesTabs.SPECIES.description,Constants.SpeciesColumns.COMPARTMENT.description);
			
		
		} catch (Exception ex) {
			/*if(m.getComp(compartment)!=null) {
				if(!this.compartment.contains(compartment))	this.compartment.add(compartment);	
			}*/
			throw ex;	
		}	
		
		Vector<String> names = new Vector<String>();
		try {
			names = CellParsers.extractNamesInList(m,compartment, Constants.TitlesTabs.SPECIES.description, Constants.SpeciesColumns.COMPARTMENT.description);
		} catch (MySyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(int i = 0; i < names.size(); i++) {
			if(m.getComp(names.get(i))!=null) {
				if(!this.compartment.contains(names.get(i))) this.compartment.add(names.get(i));	
			} else {
				throw new MySyntaxException(Constants.SpeciesColumns.COMPARTMENT.index, "Compartment  \""+ names.get(i)+"\" is not defined.", Constants.TitlesTabs.SPECIES.description);
			}
		}
		
		
	}

	public String getNotes() {		return notes;	}
	public void setNotes(String notes) {		this.notes = notes;	}
	
	
	
	public String getValueOfSite(String siteName) {
		if(!CellParsers.isMultistateSpeciesName(this.getDisplayedName())) {
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




	public boolean alreadyInComp(String cmpName) {
	   for(int i = 0; i < compartment.size(); i++) {
		   String current = compartment.get(i);
		   if(current.compareTo(cmpName)==0) return true;
	   }
		return false;
	}





	
}