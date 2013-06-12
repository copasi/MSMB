package msmb.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.lang3.tuple.MutablePair;

import com.google.common.collect.HashBiMap;

import msmb.utility.CellParsers;
import msmb.utility.Constants;

public class ComplexSpecies extends MultistateSpecies {

	Vector<String> components = new Vector<String>();
	Vector<String> siteNamesUsed = new Vector<String>();
	String complexName = new String();
	HashMap<String,Vector<MutablePair<String,String>>> components_multi = new HashMap<String,Vector<MutablePair<String,String>>>();
	HashMap<String,String> complex_sitesConfiguration = new HashMap<String, String>();
	HashBiMap<String, MutablePair<String, String>> complexSite_originSpecies_originSite = HashBiMap.create();
	private boolean returnOnlySpecies = false;

	
	public Vector<Vector<String>> getTrackedTriplets(String originSpeciesName) {
		Vector<Vector<String>> ret = new Vector<Vector<String>>();
		Iterator<String> it = complex_sitesConfiguration.keySet().iterator();
		while(it.hasNext()) {
			String complexSite = it.next();
			Vector<String> triplet = new Vector<String>();
			triplet.add(complexSite);
			MutablePair<String, String> originReference = complexSite_originSpecies_originSite.get(complexSite);
			triplet.add(originReference.right);
			triplet.add(complex_sitesConfiguration.get(complexSite));
			if(originSpeciesName.compareTo(originReference.left)==0) { ret.add(triplet);	}
		}
		return ret;
	}
	
	
	public ComplexSpecies(MultiModel m, String complete_string,
			boolean isReactantReactionWithPossibleRanges) throws Exception {
		super(m, complete_string, isReactantReactionWithPossibleRanges);
		setType(Constants.SpeciesType.COMPLEX.copasiType);
	}

	public ComplexSpecies(MultiModel m, String complete_string) throws Exception {
		super(m, complete_string);
		setType(Constants.SpeciesType.COMPLEX.copasiType);
	}
	
	public ComplexSpecies(String complete_string) throws Exception {
		super(null, complete_string);
		setType(Constants.SpeciesType.COMPLEX.copasiType);
		
		
	}

	//for now we don't want to allow multiple multistate species with the same name in a complex
	//We will add it later, once we implement the "tagging" in the reactions grammar
	public boolean containsComponent(Species componentSpecies) {
		String componentName = componentSpecies.getSpeciesName();
		for(String s: components) {
			if(s.compareTo(componentName)==0) return true;
		}
		return false;
	}
	
	public void addComponent() {
		
	}

	public void addAll_regularSpecies(List selectedValuesList) {
		components.addAll(selectedValuesList);
	}

	public Vector getSiteNamesUsed() {
		Vector<String> ret = new Vector<String>();
		ret.addAll(siteNamesUsed);
		return ret;
	}

	public void clearComponents() {
		components.clear();
		complex_sitesConfiguration.clear();
		siteNamesUsed.clear();
		components_multi.clear();
	}
	
	public String getDecomplexationReaction() {
		if(returnOnlySpecies) return null;
		
		String ret = new String();
		if(components_multi.size() ==0 && components.size()==0) return "";
		String fullComplexName = getFullComplexName() ;
		ret += fullComplexName;
		ret += " -> ";
		for(String el : components) {
			if(!(CellParsers.isMultistateSpeciesName(el))) ret += el + " + ";
		}
		
		Set<String> keys = components_multi.keySet();
		for(String k : keys) {
			ret += k +"(";
			Vector<MutablePair<String, String>> sitesList = components_multi.get(k);
			for(MutablePair<String, String> pair : sitesList){
					ret += pair.left;
					MutablePair toFind = new MutablePair<String, String>(k,pair.left);
					String siteComplex = complexSite_originSpecies_originSite.inverse().get(toFind);
					ret += "="+fullComplexName+"."+siteComplex;
					ret +=";";
			}
			ret = ret.substring(0, ret.length()-1);
			ret +=")" + " + ";
		}
		
		
		ret = ret.substring(0, ret.length()-3);
		
		
		return ret;

	}

	public String getComplexationReaction() {
		if(returnOnlySpecies) return null;
		
		String ret = new String();
		if(components_multi.size() ==0 && components.size()==0) return "";
		
		for(String el : components) {
			if(!(CellParsers.isMultistateSpeciesName(el))) ret += el + " + ";
		}
		
		Set<String> keys = components_multi.keySet();
		for(String k : keys) {
			ret += k +"(";
			Vector<MutablePair<String, String>> sitesList = components_multi.get(k);
			for(MutablePair<String, String> pair : sitesList){
					ret += pair.left;
					if(pair.right.length() > 0) {
						String siteConf = pair.right;
						if(!siteConf.startsWith("{")) {	siteConf = "{"+siteConf;	}
						if(!siteConf.endsWith("}")) {	siteConf += "}";	}
						ret += siteConf;
					}
					ret +=";";
			}
			ret = ret.substring(0, ret.length()-1);
			ret +=")" + " + ";
		}
		ret = ret.substring(0, ret.length()-3);
		
		ret += " -> "+getFullComplexName();
		
		if(complexSite_originSpecies_originSite.keySet().size() > 0) {
			ret += "(";
			Iterator<String> it = complexSite_originSpecies_originSite.keySet().iterator();
			while(it.hasNext()) {
				String key = it.next();
				MutablePair<String, String> pieces = complexSite_originSpecies_originSite.get(key);
				ret += key+"="+pieces.left+"."+pieces.right+";";
			}
			ret = ret.substring(0, ret.length()-1);
			ret += ")";
		}
		
		return ret;
	}
	
	private String getFullComplexName() {
		String completeDef = complexName;
		
		for(String el : components) {
			if(!(CellParsers.isMultistateSpeciesName(el))) completeDef += "_"+el;
		}
		
		if(complex_sitesConfiguration.keySet().size() > 0) {
			Set<String> keys = components_multi.keySet();
			for(String k : keys) {
				completeDef += "_"+k;
			}
		}
		return completeDef;
	}

	public String getFullComplexDefinition() {
		String completeDef = getFullComplexName();
		
		if(complex_sitesConfiguration.keySet().size() > 0) {
			completeDef += "(";
			for(String site : complex_sitesConfiguration.keySet()) {
				String siteConf = complex_sitesConfiguration.get(site);
				if(!siteConf.startsWith("{")) {	siteConf = "{"+siteConf;	}
				if(!siteConf.endsWith("}")) {	siteConf += "}";	}
				completeDef += site + siteConf +";";
			}
			completeDef = completeDef.substring(0,completeDef.length()-1);
			completeDef += ")";
		}
		
		return completeDef;
	}
	

	
	//first entry: the strings to make the species definition (site1{subRangeSelected};site2{subRangeSelected})
	//second entry(to build the reactions): a vector where each element is a vector of 3 elements:
	//								- complexSite name
	//								- multistateSpecies name origin
	//								- multistateSpecies site origin
	//third entry (to build the reaction): a hashmap with sites and state selected (note: here we can have the site states empty because it will be used in the reactants)
	//Returns: the string of the multistate species that is tracking, string to add in the components tree
	public String addMultistateElementsToComplex(Vector  elementsToAdd) {
		
		Vector<MutablePair<String, String>> for_definition = (Vector<MutablePair<String, String>>) elementsToAdd.get(0);
		Vector<Vector<String>> for_reactions = (Vector<Vector<String>>)elementsToAdd.get(1);
		HashMap<String,String> currentMultistateConfiguration = (HashMap<String, String>)elementsToAdd.get(2);
		
		
		for(MutablePair<String,String> def : for_definition) {
			complex_sitesConfiguration.put(def.left, def.right);
			siteNamesUsed.add(def.left);
		}
				
		String currentMultistateSpeciesName = new String();
		for(Vector<String> react : for_reactions) {
			complexSite_originSpecies_originSite.put(react.get(0), new MutablePair<String, String>(react.get(1),react.get(2)));
			currentMultistateSpeciesName = react.get(1);
		}
		
		Iterator<String> it = currentMultistateConfiguration.keySet().iterator();
		String sitesFinal = new String();
		
		Vector<MutablePair<String, String>> entry = new Vector<MutablePair<String, String>>();
		while(it.hasNext()) {
			String siteName = it.next();
			String siteStates = currentMultistateConfiguration.get(siteName);
			entry.add(new MutablePair<String, String>(siteName, siteStates));
			sitesFinal+=siteName;
			if(siteStates.length()> 0) {
				sitesFinal+=siteStates;
			}
			sitesFinal+=";";
		}
		sitesFinal = sitesFinal.substring(0, sitesFinal.length()-1);
		
		components_multi.put(currentMultistateSpeciesName, entry);
			
		
	//	System.out.println("complexSite_originSpecies_originSite: "+complexSite_originSpecies_originSite);
	//	System.out.println("components_multi: "+components_multi);
		
		String ret = currentMultistateSpeciesName + "("+sitesFinal+")";
		
		return ret;
	}

	public void removeComponentFromComplex(String removedComponent) {
		if(CellParsers.isMultistateSpeciesName(removedComponent)) {
			MultistateSpecies m;
			try {
				m = new MultistateSpecies(null, removedComponent);
				
				for(Object site : m.getSitesNames()) {
					MutablePair toFind = new MutablePair<String, String>(m.getSpeciesName(),site.toString());
					String siteComplex = complexSite_originSpecies_originSite.inverse().get(toFind);
					siteNamesUsed.remove(siteComplex);
					complexSite_originSpecies_originSite.remove(siteComplex);
					complex_sitesConfiguration.remove(siteComplex);
					
				}
				components.remove(m.getSpeciesName());
				components_multi.remove(m.getSpeciesName());
			} catch (Exception e) {
				//e.printStackTrace();
			}
			
		} 
		
		components.remove(removedComponent);
	}

	
	public void setComplexName(String text) {
		complexName = new String(text);
	}

	@Override
	public String toString() {
		return getFullComplexDefinition();
	}


	public void returnOnlySpecies(boolean b) {
		returnOnlySpecies = b;
	}
	
	@Override
	public String getSpeciesName() {
		return CellParsers.extractMultistateName(getFullComplexDefinition());
	}
	
	@Override
	public String getDisplayedName() {
		return getFullComplexDefinition();
	}

	
}
