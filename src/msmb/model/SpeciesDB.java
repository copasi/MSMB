package msmb.model;

import msmb.commonUtilities.ChangedElement;
import msmb.commonUtilities.MSMB_Element;
import msmb.commonUtilities.MSMB_InterfaceChange;
import msmb.gui.MainGui;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

import javax.mail.internet.NewsAddress;

import msmb.utility.*;

import msmb.parsers.mathExpression.MR_Expression_Parser_ReducedParserException;
import msmb.parsers.mathExpression.syntaxtree.CompleteExpression;
import msmb.parsers.mathExpression.visitor.Look4UndefinedMisusedVisitor;
import msmb.parsers.multistateSpecies.MR_MultistateSpecies_Parser;
import msmb.parsers.multistateSpecies.syntaxtree.CompleteMultistateSpecies_Operator;
import msmb.parsers.multistateSpecies.visitor.MultistateSpeciesVisitor;


public class SpeciesDB {
	Vector< Species> invisibleSpeciesVector = new Vector<Species>();
	TreeMap<Integer, Species> speciesVector = new TreeMap<Integer, Species>();
	HashMap<String, Integer> speciesIndexes = new HashMap<String, Integer>();
	MultiModel multiModel = null;
	
	public SpeciesDB(MultiModel mm) {
		speciesIndexes = new HashMap<String, Integer>();
		speciesVector = new TreeMap<Integer, Species>();
		speciesVector.put(0,null);
		multiModel = mm;
	}
	
	public int addChangeSpecies(int index, String sbmlID, String name, HashMap<String, String> initialQuant, int type, String compartment, String expression, boolean fromMultistateBuilder, String notes, boolean autoMergeSpecies,boolean parseExpression) throws Throwable {
	
		Integer ind;
		if(CellParsers.isMultistateSpeciesName(name)) {
			ind = speciesIndexes.get(new MultistateSpecies(multiModel,name).getSpeciesName());
		} else {
			ind = speciesIndexes.get(name);
		}
		/*if(name.contains("(")) { 
			ind = speciesIndexes.get(name.substring(0, name.indexOf("(")));
		} else {
			ind = speciesIndexes.get(name);
		}*/
			
		if(ind != null && ind != index && !CellParsers.isMultistateSpeciesName(name)) { 
			// the name is already assigned to another species
			//the case of multistate is handled later because it can be the case of multiple states that need to be merged
			Species old = speciesVector.get(ind);
			
			if(old.getCompartment_listString().compareTo(compartment)==0 || compartment.length()== 0) {
				Throwable cause = new Throwable(name);
				throw new ClassNotFoundException("A species already exists with that name", cause);
			}
		}
		
		if(index != -1) {
			Species old = speciesVector.get(index);
			if(old!= null) {
				multiModel.removeNamedElement(old.getSpeciesName(), new Integer(Constants.TitlesTabs.SPECIES.index));
				speciesIndexes.remove(old.getSpeciesName());
			}
		}
		
		try{
			//if(name.contains("(")) {
			if(CellParsers.isMultistateSpeciesName(name)) {
				if(ind == null) {
					MultistateSpecies s = new MultistateSpecies(multiModel,name);
					s.setInitialQuantity(initialQuant);
					s.setCompartment(multiModel,compartment);
					if(parseExpression == true) s.setExpression(multiModel,expression);
					else s.setExpression_withoutParsing(expression);
					s.setType(Constants.SpeciesType.MULTISTATE.copasiType);
					s.setNotes(notes);
					if(index == -1) { index = speciesVector.size(); }
					speciesIndexes.put(s.getSpeciesName(), index);
					speciesVector.put(index,s);
					multiModel.addNamedElement(s.getSpeciesName(), Constants.TitlesTabs.SPECIES.index);
					return index;
				} 
				
				Species old = speciesVector.get(ind);
				if(old instanceof MultistateSpecies) {
					MultistateSpecies m_old = (MultistateSpecies) old;
					MultistateSpecies m_new = null;
					try{				
						m_new = new MultistateSpecies(multiModel,name);
					} catch (Exception e) {
						Species s = new Species("\""+name+"\"");
						s.setCompartment(multiModel,compartment);
						s.setType(type);
						s.setSBMLid(sbmlID);
						s.setNotes(notes);
						try{
							s.setInitialQuantity(multiModel,initialQuant.get(name));
							if(parseExpression== true) s.setExpression(multiModel,expression);
							else s.setExpression_withoutParsing(expression);
						
						} catch(Exception ex) {
							//System.out.println("problem parsing qualcosa");
						}
						speciesIndexes.put(s.getSpeciesName(), speciesVector.size());
						multiModel.addNamedElement(s.getSpeciesName(), Constants.TitlesTabs.SPECIES.index);
						speciesVector.put(speciesVector.size(),s);	
						return speciesVector.size()-1;
					}
					
					if(!autoMergeSpecies && m_new.printCompleteDefinition(true).compareTo(m_old.printCompleteDefinition(true))!=0 && ind!=index) {
						Throwable cause = new Throwable(m_old.printCompleteDefinition());
						throw new MyInconsistencyException(ind,Constants.SpeciesColumns.NAME.index, "A multistate species with that name already exist.",cause);
					}
					
					speciesIndexes.remove(m_old.getSpeciesName()); // because the merge can change the name and I want to clear previous names associations
					multiModel.removeNamedElement(m_old.getSpeciesName(), new Integer(Constants.TitlesTabs.SPECIES.index));
					m_old.setCompartment(multiModel,compartment);
					m_old.setType(Constants.SpeciesType.MULTISTATE.copasiType);
					m_old.setNotes(notes);
					m_new.setInitialQuantity(initialQuant);
					if(index == -1 || MainGui.importFromTables) {//it's update from reaction so I want to merge with the existing
						m_old = m_old.mergeStatesWith(m_new,fromMultistateBuilder);
					} else { // it is an update from the species, so I can safely take whatever is the new species as overwriting the old one
						m_old = m_new;
					}
					m_old.setExpression(multiModel,expression);
					if(parseExpression == true) m_old.setExpression(multiModel,expression);
					else m_old.setExpression_withoutParsing(expression);
					//speciesVector.set(ind, m_old);
					speciesVector.put(ind,m_old);
					speciesIndexes.put(m_old.getSpeciesName(), ind);
					multiModel.addNamedElement(m_old.getSpeciesName(), Constants.TitlesTabs.SPECIES.index);
					if(!MainGui.donotCleanDebugMessages) MainGui.clear_debugMessages_defaults_relatedWith(Constants.TitlesTabs.SPECIES.description, ind);
					return -ind;
				} else {
					if(old instanceof Species) {
						Throwable cause = new Throwable(name);
						throw new ClassNotFoundException("A non-multistate species already exists with that name", cause);
					}
					else throw new Exception("problem addChangeSpecies");
				}
			}
			
			if(!speciesIndexes.containsKey(name) && 
					(index >= speciesVector.size() || index == -1)) { //it is a new species
				if(CellParsers.isMultistateSpeciesName(name)) { 
					MultistateSpecies s = new MultistateSpecies(multiModel,name);
					s.setCompartment(multiModel,compartment);
					s.setType(Constants.SpeciesType.MULTISTATE.copasiType);
					s.setNotes(notes);
					if(parseExpression== true) s.setExpression(multiModel,expression);
					else s.setExpression_withoutParsing(expression);
					
					s.setInitialQuantity(initialQuant);
					//speciesVector.add(s);
					//speciesIndexes.put(s.getSpeciesName(), speciesVector.size()-1);
					speciesIndexes.put(s.getSpeciesName(), speciesVector.size());
					multiModel.addNamedElement(s.getSpeciesName(), Constants.TitlesTabs.SPECIES.index);
					speciesVector.put(speciesVector.size(),s);
				} else {
					MSMB_InterfaceChange changeToReport_IntfSpecies = new MSMB_InterfaceChange(MSMB_Element.SPECIES);
					changeToReport_IntfSpecies.setElementBefore(null);
					changeToReport_IntfSpecies.setElementAfter(new ChangedElement(name, MSMB_Element.SPECIES));
					MainGui.setChangeToReport(changeToReport_IntfSpecies);
					
					Species s = new Species(name);
					s.setCompartment(multiModel,compartment);
					s.setType(type);
					s.setSBMLid(sbmlID);
					s.setNotes(notes);
					try{
						s.setInitialQuantity(multiModel,initialQuant.get(name));
						if(parseExpression== true) s.setExpression(multiModel,expression);
						else s.setExpression_withoutParsing(expression);
					
					} catch(Exception ex) {
						//System.out.println("problem parsing qualcosa");
					}
					
					speciesIndexes.put(s.getSpeciesName(), speciesVector.size());
					multiModel.addNamedElement(s.getSpeciesName(), Constants.TitlesTabs.SPECIES.index);
					speciesVector.put(speciesVector.size(),s);	
				}
				
				
				return speciesVector.size()-1;
				
			} else { //species already defined
				//if(!name.contains("(")) { 
				
				
				
				if(!CellParsers.isMultistateSpeciesName(name)) {
					if(index >= speciesVector.size() || index == -1) { 
						if(compartment.length() > 0 && speciesVector.get(ind).getCompartment_listString().length() > 0 && speciesVector.get(ind).getCompartment_listString().compareTo(compartment)==0) {
						// the user is trying to define a regular species with a name of already existing multistate species
							Throwable cause = new Throwable(name);
							throw new ClassNotFoundException("A species already exists with that name", cause);
						}
					}
					
					Species s = speciesVector.get(index);
					String oldName = s.getDisplayedName();
					if(oldName.compareTo(name)!=0) {
						MSMB_InterfaceChange changeToReport_IntfSpecies = new MSMB_InterfaceChange(MSMB_Element.SPECIES);
						changeToReport_IntfSpecies.setElementBefore(new ChangedElement(oldName,MSMB_Element.SPECIES));
						changeToReport_IntfSpecies.setElementAfter(new ChangedElement(name,MSMB_Element.SPECIES));
						MainGui.setChangeToReport(changeToReport_IntfSpecies);
					}
					s.setName(name);
					s.setCompartment(multiModel,compartment);
					s.setType(type);
					s.setNotes(notes);
					s.setInitialQuantity(multiModel,initialQuant.get(name));
					if(parseExpression == true) s.setExpression(multiModel,expression);
					else s.setExpression_withoutParsing(expression);
				
					speciesIndexes.put(name, index);
					speciesVector.put(index,s);
					multiModel.addNamedElement(name, Constants.TitlesTabs.SPECIES.index);
					if(oldName.compareTo(name)!=0) {
						speciesIndexes.remove(oldName);
					}
					if(!MainGui.donotCleanDebugMessages) MainGui.clear_debugMessages_defaults_relatedWith(Constants.TitlesTabs.SPECIES.description, index);
					return index;	//o 000000?
				} 
				
				
			}
			return 0;
		} catch (MySyntaxException ex) {
			
			multiModel.addNamedElement(name, Constants.TitlesTabs.SPECIES.index);
			speciesIndexes.put(name, index);
			if(ex.getColumn()==Constants.SpeciesColumns.EXPRESSION.index && expression.trim().length() >0) {
				Vector<String> undef = null;
				if(expression.length() >0) {
					  InputStream is = new ByteArrayInputStream(expression.getBytes("UTF-8"));
					  MR_Expression_Parser_ReducedParserException parser = new MR_Expression_Parser_ReducedParserException(is,"UTF-8");
					  try {
						  CompleteExpression root = parser.CompleteExpression(); 
						  Look4UndefinedMisusedVisitor undefVisitor = new Look4UndefinedMisusedVisitor(multiModel);
						  root.accept(undefVisitor);
						  undef = undefVisitor.getUndefinedElements();
					  }catch (Exception e) {
						addChangeSpecies(index, sbmlID, name, initialQuant,  type, compartment, expression, fromMultistateBuilder, notes, autoMergeSpecies,false);
						throw ex;
					}
					  
				}
				if(undef != null){
					 if( undef.size() ==0 || (undef.size()==1 && undef.get(0).compareTo(name)==0)) { //just self reference in ode/expression and it is allowed
						return addChangeSpecies(index, sbmlID, name, initialQuant,  type, compartment, expression, fromMultistateBuilder, notes, autoMergeSpecies,false);
					}
					throw ex;
				} 

			}
			throw ex; 
		}

			
			
			
	}
	
	
	public Vector<Species> getAllSpecies() {
		return new Vector(this.speciesVector.values());
	}
	
	public Vector<String> getAllNames() {
		Vector n = new Vector();
		for(int i = 0; i < speciesVector.size(); i++) {
			Species s = speciesVector.get(i);
			if(s!=null)n.add(s.getSpeciesName());
		}
		return n;
	}
	
	public Vector<String> getAllInvisibleNames() {
		Vector n = new Vector();
		for(int i = 0; i < invisibleSpeciesVector.size(); i++) {
			Species s = invisibleSpeciesVector.get(i);
			if(s!=null)n.add(s.getSpeciesName());
		}
		return n;
	}
	
	public Vector<Species> getAllInvisibleSpecies() {
		Vector n = new Vector();
		n.addAll(this.invisibleSpeciesVector);
		return n;
	}
	
	

	public int getNumSpeciesExpanded() throws Throwable {
		Vector n = new Vector();
		int counter = 0;
		for(int i = 0; i < speciesVector.size(); i++) {
			Species s = speciesVector.get(i);
			if(s!=null) {
				if(s instanceof MultistateSpecies) {
					MultistateSpecies m = (MultistateSpecies) s;
					counter += m.getExpandedSpecies(multiModel).size();
				} else {
					counter++;
				}
			}
		}
		return counter;
	}
	
	public Species getSpecies(String name) {
		String real_name = new String(name);
		
		if(CellParsers.isMultistateSpeciesName(name)) {
			real_name = CellParsers.extractMultistateName(name);
		}
		if(speciesIndexes.get(real_name)==null) return null;
		int ind = speciesIndexes.get(real_name).intValue();
		return getSpecies(ind);
	}
	
	public Species getSpecies(int index) {
		if(index < 0 ) {
			return null;
		}
		Species ret = speciesVector.get(index);
		return ret;
	}

	
	public boolean removeSpecies(int toBeRemoved){
		int size = speciesVector.keySet().size();
		if(toBeRemoved+1>=size) return true;
		speciesIndexes.remove(speciesVector.get(toBeRemoved+1).getSpeciesName());
		multiModel.removeNamedElement(speciesVector.get(toBeRemoved+1).getSpeciesName(), new Integer(Constants.TitlesTabs.SPECIES.index));
		for(int i = toBeRemoved+1; i < size; i++) {
			Species succ = speciesVector.get(i+1);
			if(succ==null) {
				speciesVector.remove(speciesVector.size()-1);
				break; 
			}
			speciesVector.put(i, succ);
			speciesIndexes.put(succ.getSpeciesName(), i);
		}
		return true;
	}
	
	public boolean removeInvisibleSpecies(String name){
		return invisibleSpeciesVector.remove(name);
	}
	
	public void removeSpecies(Vector species_default_for_dialog_window) throws Exception {
			HashSet index_to_delete = new HashSet();
			for(int i = 0; i < species_default_for_dialog_window.size(); i++) {
					String sp = (String) species_default_for_dialog_window.get(i);
					if(this.speciesIndexes.containsKey(sp)) {
						index_to_delete.add(this.speciesIndexes.get(sp));
						this.speciesIndexes.remove(sp);
						multiModel.removeNamedElement(sp, new Integer(Constants.TitlesTabs.SPECIES.index));
					}
			}
			//Vector newSpeciesVector = new Vector();
			TreeMap<Integer, Species> newSpeciesVector = new TreeMap<Integer, Species>();
			for(int i = 0; i < this.speciesVector.size(); i++) {
				if(!index_to_delete.contains(i)) {
					Species sp = speciesVector.get(i);
					//newSpeciesVector.add(sp);
					if(sp!=null) {
						this.speciesIndexes.put(sp.getSpeciesName(), newSpeciesVector.size());
						//MultiModel.addNamedElement(sp.getSpeciesName(), Constants.TitlesTabs.SPECIES.index);
					}
					newSpeciesVector.put(newSpeciesVector.size(),sp);
					//if(sp!=null) this.speciesIndexes.put(sp.getSpeciesName(), newSpeciesVector.size()-1);
				}
			}
			this.speciesVector.clear();
			this.speciesVector.putAll(newSpeciesVector);
	}

	public void compressSpecies() throws Throwable {
		Vector<Species> original = new Vector(this.getAllSpecies());
		multiModel.removeAllNamedElement(speciesIndexes.keySet());
		this.speciesIndexes.clear();
		this.speciesVector.clear();
		speciesVector.put(0,null);
		
		for(int i = 0; i < original.size(); i++) {
			Species current = original.get(i);
			if(current!= null) {
			/*	if(initialAmounts.containsKey(current.getDisplayedName())) {
					this.addChangeSpecies(current.getDisplayedName(),new Vector(Arrays.asList(0.0)),new Vector(Arrays.asList(0.0)), false);
				} else {
					this.addChangeSpecies(current.getDisplayedName(),new Vector(Arrays.asList(0.0)),new Vector(Arrays.asList(0.0)), false);
				}*/
				
				if(current instanceof MultistateSpecies) {
					MultistateSpecies multiCurrent = (MultistateSpecies) current;
				
					//this.addChangeSpecies(multiCurrent.getDisplayedName(),multiCurrent.getInitialConcentration_multi(),multiCurrent.getInitialAmount_multi(), multiCurrent.getType(), multiCurrent.getCompartment(), multiCurrent.getExpression(), false);
					this.addChangeSpecies(-1,new String(),multiCurrent.getDisplayedName(),multiCurrent.getInitialQuantity_multi(), multiCurrent.getType(), multiCurrent.getCompartment_listString(), multiCurrent.getExpression(), false, multiCurrent.getNotes(),true,false);
				} else {
					HashMap<String, String> entry_quantity = new HashMap<String, String>();
					entry_quantity.put(current.getDisplayedName(),current.getInitialQuantity_listString());
					this.addChangeSpecies(i,current.getSBMLid(),current.getDisplayedName(),entry_quantity, current.getType(), current.getCompartment_listString(), current.getExpression(), false, current.getNotes(),true,false);
					
								
				}
			}
		}
		
	}

	public int getSizeSpeciesDB() {
		return this.speciesVector.size();
	}


	

	public boolean containsSpecies(String speciesName, boolean isFromReaction) {
		String justName = new String();
		try{
			MultistateSpecies m = new MultistateSpecies(multiModel,speciesName,isFromReaction);
			justName = m.getSpeciesName();
		} catch (Exception e) {
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
			//this function can be used also to check if Cdh1(++(p)) exist... so I have to analyze name with operators too.
			 InputStream is;
			try {
				is = new ByteArrayInputStream(speciesName.getBytes("UTF-8"));
				 MR_MultistateSpecies_Parser react = new MR_MultistateSpecies_Parser(is,"UTF-8");
				 CompleteMultistateSpecies_Operator start = react.CompleteMultistateSpecies_Operator();
				 MultistateSpeciesVisitor v = new MultistateSpeciesVisitor(multiModel);
				 start.accept(v);
				 justName = v.getSpeciesName();
			} catch (Exception e1) {
				if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e1.printStackTrace();
			}
			
		} 
		
		
		
		return this.speciesIndexes.containsKey(justName);
		
	}

	public boolean containsSpecies(String speciesName) {
		return containsSpecies(speciesName,false);
	}


	public void updateSBMLids(HashMap<Long, String> SBMLids) {
		Iterator it = SBMLids.keySet().iterator();
		while(it.hasNext()){
			Long index =(Long) it.next();
			String sbmlid = SBMLids.get(index);
			if(sbmlid.length()==0) continue;
			Species sp = speciesVector.get(index.intValue());
			if(sp!=null) sp.setSBMLid(sbmlid); 
			speciesVector.put(index.intValue(), sp);
		}
	}


	public void clearDataOldMultistateSpecies(String name) {
		Integer index = this.speciesIndexes.get(name);
		if(index != null) this.speciesVector.remove(index);
		this.speciesIndexes.remove(name);
		multiModel.removeNamedElement(name, new Integer(Constants.TitlesTabs.SPECIES.index));
	}


	public String getEditableExpression(int row, int column) {
		Species sp = this.speciesVector.get(row+1);
		String ret = null;
		if(sp == null) return null;
		if(	column == Constants.SpeciesColumns.INITIAL_QUANTITY.index) {ret = sp.getEditableInitialQuantity();}
		if (column == Constants.SpeciesColumns.EXPRESSION.index) { ret = sp.getEditableExpression();}
		return ret;
	}


	public void setEditableExpression(String editableString, int row, int column) throws Throwable {
		Species sp = this.speciesVector.get(row+1);
		if(column == Constants.SpeciesColumns.INITIAL_QUANTITY.index) { sp.setEditableInitialQuantity(multiModel,editableString);}
		if (column == Constants.SpeciesColumns.EXPRESSION.index) { sp.setEditableExpression(multiModel,editableString);}
	}


	public Vector getAllMultistateSpeciesExpanded() throws Throwable {
		Vector n = new Vector();
		for(int i = 0; i < speciesVector.size(); i++) {
			Species s = speciesVector.get(i);
			if(s!=null) {
				if(s instanceof MultistateSpecies) {
					MultistateSpecies m = (MultistateSpecies) s;
					try {
						Vector<Species> exp = m.getExpandedSpecies(multiModel);
						for(int j = 0; j < exp.size(); j++) {
							n.add(((Species)exp.get(j)).getDisplayedName() + m.getInitial_singleConfiguration(exp.get(j)));
						}
					} catch (MySyntaxException e) {
						if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES)
							e.printStackTrace();
					}
					
				}
			}
		}
		return n;
	}


	public HashMap<String, HashMap<String, String>> getMultistateInitials() {
		HashMap<String, HashMap<String, String>> ret = new HashMap<>();
		
		for(int i = 0; i < speciesVector.size(); i++) {
			Species s = speciesVector.get(i);
			if(s!=null) {
				if(s instanceof MultistateSpecies) {
					MultistateSpecies m = (MultistateSpecies) s;
					ret.put(m.getSpeciesName(), m.getInitialQuantity_multi());
				}
			}
		}
		return ret;
	}


	public void setMultistateInitials(HashMap<String, HashMap<String, String>> multistateInitials) {
	   for(int i = 0; i < speciesVector.size(); i++) {
			Species s = speciesVector.get(i);
			if(s!=null) {
				if(s instanceof MultistateSpecies) {
					MultistateSpecies m = (MultistateSpecies) s;
					String spName= m.getSpeciesName();
					m.setInitialQuantity(multistateInitials.get(spName));
				}
			}
		}
	
	}

	public Integer getSpeciesIndex(String name) {
		String speciesName = new String();
		
		if(CellParsers.isMultistateSpeciesName(name)) {
			speciesName = CellParsers.extractMultistateName(name);
		}

		return speciesIndexes.get(speciesName);
	}

	public Vector<MultistateSpecies> getAllMultistateSpecies() {
		Vector<MultistateSpecies> ret = new Vector();
		for(int i = 0; i < speciesVector.size(); i++) {
			Species s = speciesVector.get(i);
			if(s!=null) {
				if(s instanceof MultistateSpecies) {
					MultistateSpecies m = (MultistateSpecies) s;
					ret.add(m);
					
				}
			}
		}
		return ret;
	}

	public void clear() {
		speciesVector.clear();
		speciesIndexes.clear();
	}

	public void addCompartmentToSpecies(String name, String cmpName) {
		Integer ind = this.getSpeciesIndex(name);
		Species s = this.speciesVector.get(ind);
		try {
			s.setCompartment(multiModel, cmpName);
		} catch (MySyntaxException e) {
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
		}
		this.speciesVector.put(ind, s);		
	}

	public boolean isSpeciesWithMultipleCompartment(String cleanName) {
		Species sp = this.getSpecies(cleanName);
		if(sp== null) return false;
		Vector<String> c = sp.getCompartments();
		if(c== null || c.size()<=1) return false;
		else return true;
	}

	public void addInvisibleSpecies(String name, String initialQuantity,
			String compartment) throws Exception {
		
		if(this.speciesIndexes.get(name)!=null) throw new Exception("Problem adding invisible Species: name conflicting with a visible species");
		
		Species invisible = new Species(name);
		
		try {
			invisible.setCompartment(multiModel, compartment);
			invisible.setInitialQuantity(multiModel, initialQuantity);
		} catch (Throwable e) {
			throw new Exception("Problem adding invisible Species: problem parsing compartment or initialQuantity");
		}
		
		invisible.setInvisible(true);
		
		this.invisibleSpeciesVector.add(invisible);

		return;
		
	}
}
