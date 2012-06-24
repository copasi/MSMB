package model;

import gui.MainGui;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import org.apache.commons.lang3.tuple.MutablePair;

import parsers.multistateSpecies.MR_MultistateSpecies_Parser;
import parsers.multistateSpecies.MR_MultistateSpecies_ParserConstants;
import parsers.multistateSpecies.syntaxtree.CompleteMultistateSpecies;
import parsers.multistateSpecies.visitor.MultistateSpeciesVisitor;

import com.google.common.collect.Sets;
//import com.sun.tools.javac.util.Pair;

import utility.*;
import utility.Constants.BooleanType;
import utility.Constants.SiteType;

public class MultistateSpecies extends Species {
	
	private TreeMap sites = new TreeMap();
	private HashMap<String, SiteType> sites_type = new HashMap<String, SiteType>();
	MultiModel multiModel = null;
	
	private HashMap<String, String> initialQuantities = new HashMap<String, String>();
	public HashMap<String, String> getInitialQuantity_multi() {	return this.initialQuantities; }
	public void setInitialQuantity(HashMap<String,String> initials) { 	this.initialQuantities.putAll(initials);	}
		
	@Override public String getDisplayedName() { return printCompleteDefinition(); }
	
	public String getInitial_singleConfiguration(Vector<Vector> sites_value) {
		String key = this.name + "(";
		for(int i = 0; i < sites_value.size(); i = i + 2) {
			key += sites_value.get(i) + "{" + sites_value.get(i+1) + "}";
			key += ";";
		}
		key = key.substring(0, key.length()-1);
		key += ")";
		return this.initialQuantities.get(key);
	}
	
	
	public String getInitial_singleConfiguration(Species sp) {
		if(!this.initialQuantities.containsKey(sp.getDisplayedName())) return new String(MainGui.species_defaultInitialValue);
		return this.initialQuantities.get(sp.getDisplayedName());
		
	}
	

	public void newParser(String complete_string) throws MySyntaxException {
		 try {
			 InputStream is = new ByteArrayInputStream(complete_string.getBytes("UTF-8"));
			 MR_MultistateSpecies_Parser react = new MR_MultistateSpecies_Parser(is);
			 CompleteMultistateSpecies start = react.CompleteMultistateSpecies();
			 MultistateSpeciesVisitor v = new MultistateSpeciesVisitor(multiModel);
			 start.accept(v);
			 if(v.getExceptions().size()>0) throw v.getExceptions().get(0);
			 this.setName(v.getSpeciesName());
			  initialQuantities = new HashMap<String, String>();
			  Iterator<String> it = v.getAllSites_names().iterator();
			  while(it.hasNext()) {
				  String site_name = it.next();
				  MutablePair<Integer, Integer> pureRangeSite = v.getPureRangeLimits(site_name);
				  if(pureRangeSite!= null) {
					   this.addSite_range(site_name, pureRangeSite.getLeft(), pureRangeSite.getRight());
				  } else {
					  Vector<String> single_states = v.getSite_states(site_name);
					  this.addSite_vector(site_name, single_states);
				  }
			  }
			  try {
				  this.setCompartment(multiModel,Constants.DEFAULT_COMPARTMENT_NAME);
			  } catch(MySyntaxException ex) {
				//do nothing?
			 }
			  
		 } catch(MySyntaxException ex) {
			// ex.printStackTrace();
			throw new MySyntaxException(ex.getColumn(),"Problem parsing species:"+ex.getMessage(), Constants.TitlesTabs.SPECIES.description);

		 } catch (Exception e) {
			throw new MySyntaxException(Constants.SpeciesColumns.NAME.index,"Problem parsing species: "+e.getMessage(), Constants.TitlesTabs.SPECIES.description);

		}

	}
	
	public MultistateSpecies(MultiModel m, String complete_string) throws Exception { 
		if(complete_string.length() == 0) return;
		multiModel = m;
		newParser(complete_string);
	}
	
	
	
	public void addSite_range(String name, int start, int end) {
		Vector st = new Vector();
		for(int i = start; i <= end; i++) { st.add(Integer.toString(i)); }
		sites.put(name, st);
		sites_type.put(name, Constants.SiteType.RANGE);
	}
	
	public void addSite_vector(String name, Vector states) throws Exception {
		Iterator it = states.iterator();
		boolean foundTrue = false, foundFalse = false;
		while(it.hasNext()) {
			String s = (String)it.next();
			if(BooleanType.isTrue(s)){
				foundTrue = true;
			}
			if(BooleanType.isFalse(s)){
				foundFalse = true;
			}
		}
		if((foundFalse||foundTrue) && states.size()>2) {
			throw new Exception("Boolean keyword used in a list different from \""+BooleanType.TRUE.description+","+BooleanType.FALSE.description+"\"");
		}
		
		sites.put(name, states);
		if(foundFalse||foundTrue)
		{
			//sites_boolean.add(name);
			sites_type.put(name, Constants.SiteType.BOOLEAN);
		}
		else sites_type.put(name, Constants.SiteType.LIST);
		//sites_fromRanges.remove(name);
		//sites_start_indexes.put(name, new Integer(1));
	}
	
	public void addSite_string(String name, String states) throws Exception {
		Vector parsedStates = CellParsers.parseMultistateSpecies_states(states);
		sites.put(name, parsedStates);
		if(parsedStates.size()==2 && 
			//	( (BooleanType.isTrue(((String)parsedStates.get(0))) || ((String)parsedStates.get(0)).compareTo(BooleanType.TRUE_lower.description)==0)
				( BooleanType.isTrue((String)parsedStates.get(0))
				&& BooleanType.isFalse((String)parsedStates.get(1)) )
						)
				
		{
			//sites_boolean.add(name);
			sites_type.put(name, Constants.SiteType.BOOLEAN);
		} else {
			//sites_boolean.remove(name);
		}
		//sites_fromRanges.remove(name);
		//sites_start_indexes.put(name, new Integer(1));
	}
	
	public void deleteSite(String name) {
		sites.remove(name);
		sites_type.remove(name);
		//sites_fromRanges.remove(name);
		//sites_boolean.remove(name);
		//sites_start_indexes.remove(name);
	}
	
	
	public void addNewState(String name, String state) {
		Vector old = (Vector)sites.get(name);
		old.add(state);
		sites.put(name, old);
	}
	
	public Set getSitesNames() {	return this.sites.keySet();	}
	
	public Vector getSiteStates_complete(String site_name) {//always unfolded
		return (Vector)sites.get(site_name);
	}
	
	public Vector getSiteStates(String site_name) {//compacted if possible
		Vector start_end_list = new Vector();
		String start = new String("0");
		String end = new String("0");
		String list = new String("");
		
		Vector states = (Vector)sites.get(site_name);
		//if(this.sites_fromRanges.contains(site_name)) {
		if(this.sites_type.get(site_name)== SiteType.RANGE) {
			start = (String)states.get(0);
			end = (String)states.get(states.size()-1);
		} else {
			String separator = MR_MultistateSpecies_ParserConstants.tokenImage[MR_MultistateSpecies_Parser.SITE_STATES_SEPARATOR];
			separator = separator.substring(1,separator.length()-1);
			//if(!sites_boolean.contains(site_name)) {
				for(int i = 0; i < states.size()-1 ;i++) {
					list += (String)states.get(i)+ separator;
				}
				list +=(String)states.get(states.size()-1);
		/*	} else {
				list += Constants.BooleanType.TRUE.description + "," + Constants.BooleanType.FALSE.description;
			}*/
		}
		
		start_end_list.add(start);
		start_end_list.add(end);
		start_end_list.add(list);
		//start_end_list.add(new Boolean(sites_boolean.contains(site_name)));
		start_end_list.add(new Boolean(sites_type.get(site_name)==SiteType.BOOLEAN));
		return start_end_list;
	}
	
	public String printSite(String name) {
		return printSite(name, false);
	}
	
	private String printSite(String name, boolean alphabetical
		//	,boolean withStartIndex
			) {
		if(name.contains("succ") || name.contains("prec")) return name;
		
		String r = new String();
		Vector values = new Vector();
		values.addAll((Vector)sites.get(name));
		if(alphabetical) Collections.sort(values);
		if(values == null) return r;
		
		r+= name+"{";
		SiteType ty = this.sites_type.get(name);
		//if(sites_fromRanges.contains(name)) {.
		if(ty == SiteType.RANGE){
			r+=values.get(0) + ":" + values.get(values.size()-1);
		//} else if(sites_boolean.contains(name)) {
		} else if(ty == SiteType.BOOLEAN) {
			r+= Constants.BooleanType.TRUE.description + "," + Constants.BooleanType.FALSE.description;
	    }else {
			for(int i = 0; i < values.size()-1; i++) {
				r+= values.get(i) + ",";
			}
			r+= values.get(values.size()-1);
		}
		
		
		r+="}";
		return r;
	}

	public MultistateSpecies mergeStatesWith(MultistateSpecies newSpecies, boolean fromMultistateBuilder) throws Exception {
		if(this.printCompleteDefinition().compareTo(newSpecies.printCompleteDefinition()) == 0) {
			return this;
		}

		MultistateSpecies merged = new MultistateSpecies(multiModel,this.getDisplayedName());
		merged.setName(newSpecies.getSpeciesName());
		Iterator site_it2 = newSpecies.sites.keySet().iterator();
		while (site_it2.hasNext()) {  
			String key = site_it2.next().toString();  
			Vector states = (Vector)newSpecies.sites.get(key);
			Vector statesOld = (Vector)merged.sites.get(key);
			if(this.sites_type.get(key) != merged.sites_type.get(key)) throw new MySyntaxException(Constants.SpeciesColumns.NAME.index, "Inconsistent site type during merging.", Constants.TitlesTabs.SPECIES.description);
			if(this.sites_type.get(key) == SiteType.BOOLEAN) {
				continue;
			} else if(this.sites_type.get(key) == SiteType.RANGE) {
				HashSet statesWithoutDuplicates = new HashSet();
			    if(states != null) statesWithoutDuplicates.addAll(states);
			    if(statesOld != null) statesWithoutDuplicates.addAll(statesOld);
			    Vector ordered = new Vector(Arrays.asList(statesWithoutDuplicates.toArray()));
			    for(int i = 0; i < ordered.size(); i++) { //are they all numbers?
			    	try {
			    		Integer.parseInt((String)ordered.get(i));	    	 
			    	} catch(Exception ex) {
			    		if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) 
			    			ex.printStackTrace();
			    		throw new MySyntaxException(Constants.SpeciesColumns.NAME.index, "Site type range with states no numbers", Constants.TitlesTabs.SPECIES.description);
			    	}
			    }
			   
			    Collections.sort(ordered, new AlphanumComparator());
				  
				merged.sites.put(key, ordered);	
				continue;
			} else {
				 Vector all = new Vector();
				 if(statesOld != null) all.addAll(statesOld);
				HashSet statesAlreadyAdded = new HashSet();
				 if(statesOld != null) statesAlreadyAdded.addAll(statesOld);
				if(states!= null) {
					for(int i = 0; i < states.size(); i++) {
							if(!statesAlreadyAdded.contains(states.get(i))) all.add(states.get(i));
					}
				}
				merged.sites.put(key, all);
			}
		
		}  

		merged.setInitialQuantity(this.getInitialQuantity_multi());
		merged.setInitialQuantity(newSpecies.getInitialQuantity_multi());

		merged.setType(this.getType());
		merged.setCompartment(multiModel,this.getCompartment());

		return merged;
	}
	
	
	
	public String printCompleteDefinition() {
		String r = this.name; 
		if(getSitesNames().size() <= 0) return r;
		r += "(";
		 Iterator iterator = getSitesNames().iterator();  
		 int i = 0;
		 while (iterator.hasNext()) {  
		       String pr = printSite((String)iterator.next());//, true);  
		       r+=pr+";";
	    }
		 r = r.substring(0,r.length()-1) + ")";
		return r;
	}
	
	public Vector<Species> getExpandedSpecies(MultiModel m) throws Exception {
		Vector<Species> ret = new Vector<Species>();
		
		Set keySet = this.sites.keySet();
		
		 List<String> keys = new ArrayList<String>(keySet);
		 List<Set<String>> values = new Vector<Set<String>>();
		 Iterator sites_iterator = keySet.iterator();
		while (sites_iterator.hasNext()) {  
			String name = sites_iterator.next().toString();  
		    Vector<String> states = (Vector<String>)this.sites.get(name);
		 	Set<String> values_site = Sets.newLinkedHashSet(states);
		     values.add(values_site);
		}
		   		    
		Set<List<String>> product = Sets.cartesianProduct(values);
		
	    
		for (List<String> v : product) {
			   Vector<String> site_value = new Vector<String>();
			   for (int i = 0; i < keys.size(); ++i) {
		    	    String key = (String) keys.get(i);
		            String value = v.get(i);
		            site_value.add(key);
		            site_value.add(value);
		        }
		        Species singleConf = createSingleConfigurationState(m,site_value);
		        singleConf.setCompartment(multiModel,this.getCompartment());
		        singleConf.setType(Constants.SpeciesType.REACTIONS.copasiType);
		        ret.add(singleConf);
		    }
		return ret;
	}

	private Species createSingleConfigurationState(MultiModel m,Vector<String> site_value) throws Exception {
		String name = new String(this.getSpeciesName());
		name += "(";
		for(int i = 0; i < site_value.size(); i=i+2) {
			name+= site_value.get(i) + "{"+ site_value.get(i+1)+"}"; 
			name+=";";
		}
		name = name.substring(0, name.length()-1);
		name += ")";
		
		Species ret = new Species();
		ret.setName(name);
		ret.setCompartment(multiModel,this.getCompartment());
		ret.setInitialQuantity(m,new String(MainGui.species_defaultInitialValue));
		
		ret.setType(this.getType());
		
		return ret;
	}

	public Vector<String> getExpandedSites() {
		
		if(this.sites.size() ==0) return new Vector(); 
			
		Vector<String> ret = new Vector<String>();
		
		 List<String> keys = new ArrayList<String>(this.sites.keySet());
		 List<Set<String>> values = new Vector<Set<String>>();
		Iterator sites_iterator = this.sites.keySet().iterator();
		while (sites_iterator.hasNext()) {  
			String name = sites_iterator.next().toString();  
		    Vector<String> states = (Vector<String>)this.sites.get(name);
		 	Set<String> values_site = Sets.newLinkedHashSet(states);
		    values.add(values_site);
		}
		   		    
	    Set<List<String>> product = Sets.cartesianProduct(values);
	
	    for (List<String> v : product) {
			   String singleConf = new String();
		       
				for (int i = 0; i < keys.size(); ++i) {
		    	    String key = (String) keys.get(i);
		            String value = v.get(i);
		            singleConf += key + "{";
		            singleConf += value + "};";
		        }
				singleConf = singleConf.substring(0, singleConf.length()-1);
		        ret.add(singleConf);
		    }
		return ret;
	}
	
	
	public Vector<Vector> getExpandedVectors() {
		
		if(this.sites.size() ==0) return new Vector(); 
			
		Vector<Vector> ret = new Vector<Vector>();
		
		 List<String> keys = new ArrayList<String>(this.sites.keySet());
		 List<Set<String>> values = new Vector<Set<String>>();
		 Iterator sites_iterator = this.sites.keySet().iterator();
		while (sites_iterator.hasNext()) {  
			String name = sites_iterator.next().toString();  
		    Vector states = (Vector)this.sites.get(name);
		 	Set<String> values_site = Sets.newLinkedHashSet(states);
		    values.add(values_site);
		}
		   		    
	    Set<List<String>> product = Sets.cartesianProduct(values);
	    
		for (List<String> v : product) {
			  
			   Vector singleConf = new Vector();
		       
				for (int i = 0; i < keys.size(); ++i) {
		    	    String key = (String) keys.get(i);
		            String value = v.get(i);
		            singleConf.add(key);
		            singleConf.add(value);
		        }
				 ret.add(singleConf);
		    }
		return ret;
	}


	
	public String getSucc(String site, String state, boolean circular) {
		Vector states = (Vector) sites.get(site);
		int i = 0;
		for( i = 0; i < states.size()-1; i++) {
	    	 if(((String)states.get(i)).compareTo(state) == 0) {
	    		 return (String)states.get(i+1);
	    	 }	    	 
	     }
		if(!circular) return null;
		return (String)states.get(0);
	}
	
	public String getPrec(String site, String state, boolean circular) {
		Vector states = (Vector) sites.get(site);
		for(int i = 1; i < states.size(); i++) {
	    	 if(((String)states.get(i)).compareTo(state) == 0) {
	    		 return (String)states.get(i-1);
	    	 }	    	 
	    	 
	     }
//		if(sites_fromRanges.contains(site)) return null;
		if(!circular) return null;
		return (String)states.get(states.size()-1);
	}
	
	
	public void mergeStatesWith_Minimum(MultistateSpecies existing) {
		this.setType(existing.getType());
		if(existing.printCompleteDefinition().compareTo(this.printCompleteDefinition()) == 0) {
			return;
		}
		
		
	    Iterator iterator = this.sites.keySet().iterator();  
	    
	    while (iterator.hasNext()) {  
	       String key = iterator.next().toString();  
	       Vector states = new Vector();
	       states.addAll((Vector)this.sites.get(key));
	       //if(sites_type.get(key)==SiteType.RANGE) Collections.sort(states, new AlphanumComparator());
	       this.sites.put(key, states);
	    }  
	    
    
		Iterator site_it2 = existing.sites.keySet().iterator();
		while (site_it2.hasNext()) {  
		       String key = site_it2.next().toString();  
		       if(this.sites.containsKey(key)) { continue;}
		       Vector states = (Vector)existing.sites.get(key);
		       HashSet statesWithoutDuplicates = new HashSet();
		       statesWithoutDuplicates.addAll(states);
		       Vector ordered = new Vector(Arrays.asList(statesWithoutDuplicates.toArray()));
		       this.sites.put(key, ordered);
		 }  
		
		
		Iterator sites = this.sites.keySet().iterator();
		while (sites.hasNext()) {  
			 String key = sites.next().toString();  
			 boolean allNumbers = true;
		     Vector states = (Vector)this.sites.get(key);
		     
		     for(int i = 0; i < states.size(); i++) { //are they all numbers?
		    	 try {
		    		 Integer.parseInt((String)states.get(i));	    	 
		    	 } catch(Exception ex) {
		    		 if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) ex.printStackTrace();
		    		 allNumbers = false;
		    		 break;
		    	 }
		     }
		     
		     if(allNumbers) {
			     int startRange = Integer.parseInt((String)states.get(0));
			     int endRange = Integer.parseInt((String)states.get(states.size()-1));
			     
			     if((endRange-startRange)==states.size()-1) { //the range is complete
			    	 //this.sites_fromRanges.add(key);
			    	 this.sites_type.put(key, SiteType.RANGE);
			     }
		     } else {
		    	 this.sites_type.put(key, SiteType.LIST);
		     }
		    	 
		     
		}

	
		return;
	}

	public boolean containsSpecificConfiguration(String s) throws Exception {
		MultistateSpecies single = new MultistateSpecies(multiModel,s);
		String configuration = single.getExpandedSites().get(0);
		Vector<String> current = this.getExpandedSites();
		for(int i = 0; i < current.size(); i++) {
			String site = current.get(i);
			if(site.compareTo(configuration)==0) return true;
		}
		return false;
	}
	
	public String printCompleteDefinition(boolean b) {
		String r = this.name; 
		if(getSitesNames().size() <= 0) return r;
		r += "(";
		 Iterator iterator = getSitesNames().iterator();  
		 int i = 0;
		 while (iterator.hasNext()) {  
		       String pr = printSite((String)iterator.next(),b);//, true);  
		       r+=pr+";";
	    }
		 r = r.substring(0,r.length()-1) + ")";
		return r;
	}


	
	
}


class CartesianIterator implements Iterator<Object[]> {

	private final Iterable[] iterables;
	private final Iterator[] iterators;
	private Object[] values;
	private int size;
	private boolean empty;

	/**
	 * Constructor
	 * @param iterables array of Iterables being the source for the Cartesian product.
	 */
	public CartesianIterator(Iterable ...iterables) {
		this.size = iterables.length;
		this.iterables = iterables;
		this.iterators = new Iterator[size];
		
		// Initialize iterators
		for (int i = 0; i < size; i++) {
			iterators[i] = iterables[i].iterator();
			// If one of the iterators is empty then the whole Cartesian product is empty
			if (!iterators[i].hasNext()) {
				empty = true;
				break;
			}
		}
		
		// Initialize the tuple of the iteration values except the last one
		if (!empty) {
			values = new Object[size];
			for (int i = 0; i < size-1; i++) setNextValue(i);
		}
	}

	@Override
	public boolean hasNext() {
		if (empty) return false;
		for (int i = 0; i < size; i++)
			if (iterators[i].hasNext())
				return true;
		return false;
	}

	@Override
	public Object[] next() {
		// Find first in reverse order iterator the has a next element
		int cursor;
		for (cursor = size-1; cursor >= 0; cursor--)
			if (iterators[cursor].hasNext()) break;

		// Initialize iterators next from the current one  
		for (int i = cursor+1; i < size; i++) iterators[i] = iterables[i].iterator();
		
		// Get the next value from the current iterator and all the next ones  
		for (int i = cursor; i < size; i++) setNextValue(i);

		return values.clone();
	}

	/**
	 * Gets the next value provided there is one from the iterator at the given index. 
	 * @param index
	 */
	private void setNextValue(int index) {
		Iterator it = iterators[index];
		if (it.hasNext())
			values[index] = it.next();
	}

	@Override
	public void remove() { throw new UnsupportedOperationException(); }
}
