package msmb.model;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Vector;

import msmb.gui.MainGui;
import msmb.parsers.chemicalReaction.MR_ChemicalReaction_Parser;
import msmb.parsers.chemicalReaction.syntaxtree.CompleteReaction;
import msmb.parsers.chemicalReaction.visitor.ExtractSubProdModVisitor;
import msmb.utility.CellParsers;
import msmb.utility.Constants;
import msmb.utility.MySyntaxException;


public class Reaction {
	String name = new String();
	String notes = new String();
	String SBMLid = new String();
	int type = Constants.ReactionType.MASS_ACTION.copasiType;
	String rateLaw = new String();
	private String editableRateLaw = rateLaw;
	String reactionString = new String();
	
	
	public Vector<String> getSubstrates(MultiModel m) {
		Vector ret = new Vector<>();
		Vector metabolites;
		try {
			metabolites = getSubProdMod(m);
		} catch (Throwable e) {
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
			return null;
		}
		Vector subs = (Vector)metabolites.get(0);
		 ret.addAll(subs);
		return ret;
	 
	}
	
	public Vector<String> getProducts(MultiModel m) {
		Vector ret = new Vector<>();
		Vector metabolites;
		try {
			metabolites = getSubProdMod(m);
		} catch (Throwable e) {
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
			return null;
		}
		Vector prod = (Vector)metabolites.get(1);
		 ret.addAll(prod);
		return ret;
	 
	}
	
	
	public Vector<String> getModifiers(MultiModel m) {
		Vector ret = new Vector<>();
		Vector metabolites;
		try {
			metabolites = getSubProdMod(m);
		} catch (Throwable e) {
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
			return null;
		}
		Vector mod = (Vector)metabolites.get(2);
		 ret.addAll(mod);
		return ret;
	 
	}
	
	private Vector getSubProdMod(MultiModel m) throws Throwable {
		Vector metabolites = new Vector<>();

		InputStream is = new ByteArrayInputStream(reactionString.getBytes("UTF-8"));
		MR_ChemicalReaction_Parser react = new MR_ChemicalReaction_Parser(is,"UTF-8");
		CompleteReaction start = react.CompleteReaction();
		ExtractSubProdModVisitor v = new ExtractSubProdModVisitor(m);
		start.accept(v);

		if(v.getExceptions().size() != 0) {
			throw new Exception(v.getExceptions().get(0).getMessage());
		}

		metabolites.addAll(v.getAll_asString());
		
		return metabolites;
	}


	public String getRateLaw() { 	return rateLaw.trim();	}
		
	public void setRateLaw(MultiModel m, String expr) throws MySyntaxException {	
		if(expr.compareTo(Constants.NOT_EDITABLE_VIEW) == 0) return;
		this.rateLaw = expr;	
		if(expr.length() == 0) return ;
		try {
			CellParsers.parseExpression_getUndefMisused(m,rateLaw, Constants.TitlesTabs.REACTIONS.description,Constants.ReactionsColumns.KINETIC_LAW.description);
		} catch (Exception ex) {
			throw ex;
		}
		editableRateLaw = expr;
	}

	public void setRateLaw_withoutParsing(String expression) {	this.rateLaw = expression; editableRateLaw = expression;	}

	
	public String getEditableRateLaw() {	return editableRateLaw; }
	
	
	public String getSBMLid() {return SBMLid;}
	public void setSBMLid(String sBMLid) {	SBMLid = sBMLid;}
	
	public Reaction() {}
	public Reaction(String name) { this.setName(name);}
	
	public Vector getAllFields() { 
		Vector r = new Vector();
		r.add(this.getName());
		r.add(this.getReactionString());
		r.add(this.getType());
		r.add(this.getRateLaw());
		r.add(this.getNotes());
		r.add(this.getSBMLid());
		return r;
	}
	
	public String getReactionString() {	return reactionString;	}
	public void setReactionString(String r) {	reactionString = new String(r);	}
	


	public String getName() { return name; }
	public void setName(String name) {	this.name = name;	}
	public int getType() {	return type;	}
	public void setType(int CMetab_Type) {		this.type = CMetab_Type;	}
	public String getNotes() {		return notes;	}
	public void setNotes(String notes) {		this.notes = notes;	}
	
	

	public void setEditableRateLaw(MultiModel m,String editableString) throws MySyntaxException {
		setRateLaw(m,editableString);		
	}



	



}
