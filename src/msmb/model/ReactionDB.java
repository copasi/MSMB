package msmb.model;

import msmb.gui.MainGui;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Vector;

import org.apache.commons.lang3.tuple.MutablePair;

import msmb.utility.CellParsers;
import msmb.utility.Constants;
import msmb.utility.MySyntaxException;

import msmb.parsers.mathExpression.MR_Expression_Parser;
import msmb.parsers.mathExpression.syntaxtree.CompleteExpression;
import msmb.parsers.mathExpression.visitor.Look4UndefinedMisusedVisitor;

public class ReactionDB {
	Vector<Reaction> reactionVector = new Vector<Reaction>();
	
	MultiModel multiModel = null;
	
	public ReactionDB(MultiModel mm) {
		reactionVector = new Vector<Reaction>();
		reactionVector.add(null);
		multiModel = mm;
	}
	
	
	public Reaction getReaction(int index) {
		if(index < 0 || index >= reactionVector.size()) {
			return null;
		}
		Reaction ret = reactionVector.get(index);
		return ret;
	}
	
	
	
	public int addChangeReaction(int index, String name,  String reaction, int type, String rateLaw, String notes) throws Exception {
		if(reaction.trim().length() == 0) return -1;
		
		Reaction oldR = null;
		if(index < reactionVector.size()) oldR = reactionVector.get(index);
				
		try{
			if(oldR == null) {//it is a new reaction
				Reaction c = new Reaction(name);
				c.setNotes(notes);
				c.setType(type);
				reactionVector.add(c); //take the place even if expressions contains error
				c.setReactionString(reaction);
				c.setRateLaw(multiModel,rateLaw);
				reactionVector.set(reactionVector.size()-1,c);
				multiModel.addNamedElement(c.getName(), Constants.TitlesTabs.REACTIONS.index);
				return reactionVector.size()-1;
			} else { //reaction already defined
				oldR.setNotes(notes);
				oldR.setType(type);
				oldR.setReactionString(reaction);
				reactionVector.set(index,oldR); //take the place even if expressions contains error
				oldR.setRateLaw(multiModel,rateLaw);
				reactionVector.set(index,oldR);
				multiModel.addNamedElement(name, Constants.TitlesTabs.REACTIONS.index);
				
				if(!MainGui.donotCleanDebugMessages) MainGui.clear_debugMessages_defaults_relatedWith(Constants.TitlesTabs.REACTIONS.description, index);
				return index;
			}
		} catch (MySyntaxException ex) {
			if(ex.getColumn()==Constants.ReactionsColumns.KINETIC_LAW.index && rateLaw.trim().length() >0) {
				addChangeReaction_withoutParsing(index, name,  reaction, type, rateLaw, notes);
				/*Vector<String> undef = null;
				if(rateLaw.length() >0) {
					  InputStream is = new ByteArrayInputStream(rateLaw.getBytes("UTF-8"));
					  MR_Expression_Parser parser = new MR_Expression_Parser(is,"UTF-8");
					  CompleteExpression root = parser.CompleteExpression();
					  Look4UndefinedMisusedVisitor undefVisitor = new Look4UndefinedMisusedVisitor(multiModel);
					  root.accept(undefVisitor);
					  undef = undefVisitor.getUndefinedElements();
				}
				if(undef != null){
					 if(undef.size()==1 && undef.get(0).compareTo(name)==0) { //just self reference in ode/expression and it is allowed
						return addChangeReaction_withoutParsing(index, name,  reaction, type, rateLaw, notes);
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
				*/
			}
			return -1; 
		}

	}
	
	public int addChangeReaction_withoutParsing(int index, String name,  String reaction, int type, String rateLaw, String notes) throws Exception {
		if(reaction.trim().length() == 0) return -1;
		
		Reaction oldR = reactionVector.get(index);
		if(oldR==null) { //it is a new reaction
			Reaction c = new Reaction(name);
			c.setRateLaw_withoutParsing(rateLaw);
			c.setReactionString(reaction);
			c.setNotes(notes);
			c.setType(type);
			reactionVector.add(c);
			return reactionVector.size()-1;
		} else { //reaction already defined
			Reaction c = reactionVector.get(index);
			c.setRateLaw_withoutParsing(rateLaw);
			c.setReactionString(reaction);
			c.setNotes(notes);
			c.setType(type);
			reactionVector.set(index, c);
			if(!MainGui.donotCleanDebugMessages) MainGui.clear_debugMessages_defaults_relatedWith(Constants.TitlesTabs.REACTIONS.description, index);
			return index;
		}
			
	}
	
	public void clear() {
		reactionVector.clear();
		reactionVector.add(null);
	}
	
	
	public String getEditableExpression(int row, int column) {
		Reaction r = this.reactionVector.get(row+1);
		String ret = null;
		if(	column == Constants.ReactionsColumns.KINETIC_LAW.index) {ret = r.getEditableRateLaw();}
		return ret;
	}

	public void updateReactionEditableView(int row, String editableView) {
		Reaction r = this.reactionVector.get(row+1);
		r.setRateLaw_withoutParsing(editableView);
		reactionVector.set(row+1, r);
	}


	public Vector<String> checkUsage(Vector<MutablePair<String, String>> usedAsElements, int row) {
		Vector misused = new Vector<String>();
		
		Reaction r = this.reactionVector.get(row+1);
		Vector<String> subs = r.getSubstrates(multiModel);
		for(int i = 0; i < usedAsElements.size(); i++) {
			MutablePair<String, String> element = usedAsElements.get(i);
			String name = element.left;
			String type = element.right;
			boolean ok = false;
			if(type.compareTo(Constants.FunctionParamType.SUBSTRATE.signatureType)==0) {
				for(int j = 0; j < subs.size(); j++){
					if(subs.get(j).compareTo(name)==0) {
						ok = true;
						break;
					}
				}
				if(!ok) misused.add(name);
			}
			//ADD PRODUCTS AND MODIFIERS
		}
		
		return misused;
	}
}
