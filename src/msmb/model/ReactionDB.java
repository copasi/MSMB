package msmb.model;

import msmb.debugTab.FoundElement;
import msmb.gui.MainGui;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Vector;

import org.apache.commons.lang3.tuple.MutablePair;

import msmb.parsers.multistateSpecies.MR_MultistateSpecies_Parser;
import msmb.parsers.multistateSpecies.syntaxtree.CompleteMultistateSpecies_Operator;
import msmb.parsers.multistateSpecies.visitor.MultistateSpeciesVisitor;
import msmb.utility.CellParsers;
import msmb.utility.Constants;
import msmb.utility.MySyntaxException;

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
	
	
	
	public int addChangeReaction(int index, String name,  String reaction, int type, String rateLaw, String notes) throws Throwable {
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
						 String message = "Missing element definition: " + undef.toString();
						 ex = new MySyntaxException(message, ex);
					 }
					throw ex;
				} 
				*/
			}
			return -1; 
		}

	}
	
	public int addChangeReaction_withoutParsing(int index, String name,  String reaction, int type, String rateLaw, String notes) throws Throwable {
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
		if(row >= reactionVector.size()) return new String();
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
		Vector<String> prod = r.getProducts(multiModel);
		Vector<String> mod = r.getModifiers(multiModel);
		for(int i = 0; i < usedAsElements.size(); i++) {
			MutablePair<String, String> element = usedAsElements.get(i);
			String name = element.left;
			String type = element.right;
			boolean ok = false;
			if(type.compareTo(Constants.FunctionParamType.SUBSTRATE.signatureType)==0) {
				for(int j = 0; j < subs.size(); j++){
					String element1 = subs.get(j);
					element1 = multiModel.extractName(element1);
					String element1_justName_ifMultistate = element1;
					if(CellParsers.isMultistateSpeciesName(element1)) {
						element1_justName_ifMultistate = CellParsers.extractMultistateName(element1);
					}
					if(element1_justName_ifMultistate.compareTo(name)==0 || CellParsers.compareMultistateSpecies(multiModel,element1,name)) {
						ok = true;
						break;
					}
				}
				if(!ok) misused.add(name);
			} else if(type.compareTo(Constants.FunctionParamType.PRODUCT.signatureType)==0) {
				for(int j = 0; j < prod.size(); j++){
					String element1 = prod.get(j);
					element1 = multiModel.extractName(element1);
					String element1_justName_ifMultistate = element1;
					if(CellParsers.isMultistateSpeciesName(element1)) {
						element1_justName_ifMultistate = CellParsers.extractMultistateName(element1);
					}
					if(element1_justName_ifMultistate.compareTo(name)==0 || CellParsers.compareMultistateSpecies(multiModel,element1,name)) {
						ok = true;
						break;
					}
				}
				if(!ok) misused.add(name);
			}else if(type.compareTo(Constants.FunctionParamType.MODIFIER.signatureType)==0) {
				for(int j = 0; j < mod.size(); j++){
					String element1 = mod.get(j);
					element1 = multiModel.extractName(element1);
					String element1_justName_ifMultistate = element1;
					if(CellParsers.isMultistateSpeciesName(element1)) {
						element1_justName_ifMultistate = CellParsers.extractMultistateName(element1);
					}
					if(element1_justName_ifMultistate.compareTo(name)==0 || CellParsers.compareMultistateSpecies(multiModel,element1,name)) {
						ok = true;
						break;
					}
				}
				if(!ok) misused.add(name);
			}else if(type.compareTo(Constants.FunctionParamType.SITE.signatureType)==0) {
				for(int j = 0; j < subs.size(); j++){
					String element1 = subs.get(j);
					element1 = multiModel.extractName(element1);
					String element1_justName_ifMultistate = element1;
					if(CellParsers.isMultistateSpeciesName(element1)) {
						element1_justName_ifMultistate = CellParsers.extractMultistateName(element1);
					}
					MultistateSpecies sp = (MultistateSpecies) multiModel.getSpecies(element1_justName_ifMultistate);
					if(sp.getSitesNames().contains(name)) {
						ok = true;
						break;
					}
				}
				if(!ok) misused.add(name);
			}
		}
		
		return misused;
	}
	
	public Integer getReactionIndex(String name) {
		return -1;
	}
		
}
