package parsers.mathExpression.visitor;
import model.Function;
import model.MultiModel;
import model.MultistateSpecies;
import model.Species;
import parsers.mathExpression.MR_Expression_ParserConstants;
import parsers.mathExpression.MR_Expression_ParserConstantsNOQUOTES;
import parsers.mathExpression.syntaxtree.*;
import parsers.multistateSpecies.MR_MultistateSpecies_Parser;
import parsers.multistateSpecies.ParseException;
import parsers.multistateSpecies.syntaxtree.CompleteMultistateSpecies_Operator;
import parsers.multistateSpecies.visitor.MultistateSpeciesVisitor;

import gui.MainGui;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

import utility.Constants;

public class Look4UndefinedMisusedVisitor extends DepthFirstVoidVisitor {
		Vector<String> missing;
		Vector<String> misused;
		private String returnName;
		private boolean getSubName;
		MultiModel multiModel = null;
	
	   public Look4UndefinedMisusedVisitor(MultiModel mm)  {
		   missing = new Vector<String>();
		   misused = new Vector<String>();
		   multiModel = mm;
	   }

		public Vector<String> getMisusedElements() {	return misused;	}
		public Vector<String> getUndefinedElements() {	return missing;	}
		
		
		@Override
		public void visit(Name n) {
			returnName = ToStringVisitor.toString(n.nodeChoice.choice);
			if(n.nodeChoice.which ==0) {
				super.visit(n);
				if(getSubName) {
					NodeSequence nodes = (NodeSequence) n.nodeChoice.choice;
					returnName = ToStringVisitor.toString(nodes.nodes.get(0));
				}
			}
		}
		
		@Override
		public void visit(PossibleExtensions n) {
			getSubName = true;
		}
	
		
		
	@Override
	public void visit(SpeciesReferenceOrFunctionCall_prefix n) {
		super.visit(n);
		//String name = ToStringVisitor.toString(n.name.nodeChoice.choice);
		String name = returnName;
		
		if(n.nodeOptional.present())  {
			NodeOptional nodeOptional = (NodeOptional) ((NodeSequence) n.nodeOptional.node).nodes.get(1);
			if(nodeOptional.node==null){
				//System.out.println("FUNCTION CALL (0): "+name);
			//	Function f = multiModel.getFunctionByName(name);
				Function f = multiModel.getFunctionByName(ToStringVisitor.toString(n.name.nodeChoice.choice));
				if(f==null) {	if(!missing.contains(name))	missing.add(name); }
			}
			else {
				if(!isMultistateSitesList(nodeOptional.node)) {
					//System.out.println("FUNCTION CALL ("+getNumberArguments((ArgumentList)nodeOptional.node)+"): " +name);
					//Function f = multiModel.getFunctionByName(name);
					name = ToStringVisitor.toString(n.name.nodeChoice.choice);
					
					
						Function f = multiModel.getFunctionByName(name);
						if(f==null){	
							if(!missing.contains(name))	missing.add(name); }
						else{
							checkParameterUsage((ArgumentList)nodeOptional.node,f);
						}
				
				} else {
					//System.out.println("SPECIES: "+ToStringVisitor.toString(n)); // to print complete "multistate" definition
					if(isMultistateSpeciesDefined(n)) {
						//ok multistate species existing
					} else if(name.toLowerCase().compareTo(Constants.NAN_STRING) != 0
							&& multiModel.getWhereNameIsUsed(name)==null) {
						if(!misused.contains(ToStringVisitor.toString(n))&&!missing.contains(name))	missing.add(name);
					}
				}
			}
		} else {
			//System.out.println("SPECIES: "+ToStringVisitor.toString(n));
			if(name.toLowerCase().compareTo(Constants.NAN_STRING) != 0
					&& multiModel.getWhereNameIsUsed(name)==null) {
				if(!missing.contains(name))	
					missing.add(name);
			}
		}
		super.visit(n);
	}
	  
	 
	private boolean isMultistateSpeciesDefined(SpeciesReferenceOrFunctionCall_prefix n) {
		String element = ToStringVisitor.toString(n);
		 InputStream is = new ByteArrayInputStream(element.getBytes());
		 MR_MultistateSpecies_Parser react = new MR_MultistateSpecies_Parser(is);
		 try {
			CompleteMultistateSpecies_Operator start = react.CompleteMultistateSpecies_Operator();
			MultistateSpeciesVisitor v = new MultistateSpeciesVisitor(multiModel);
	
			start.accept(v);
			MultistateSpecies sp = (MultistateSpecies) multiModel.getSpecies(v.getSpeciesName());
		
			if(sp.containsSpecificConfiguration(element)) return true;
			else {
				misused.add(element);
				return false;
			}
			
			/*//to check if that is a state existing from the sp definition
			MultistateSpeciesVisitor v2 = new MultistateSpeciesVisitor(multiModel,sp);
			 start.accept(v2);
			 String exp = v2.getProductExpansion();
			 
			 
			 
			 if(exp != null && v.getExceptions().size() == 0) { return true; }
			 else {
				 return false;
			 }
			*/
		 } catch (Exception e) {
			//	e.printStackTrace();
				return false;
			}
	}


	private int indexSum = -1;
	Vector<SumExpansion> sumExpansion = new Vector<SumExpansion>();
	
	public Vector<SumExpansion> getSumExpansions() {
		return sumExpansion;
	}

	@Override
	public void visit(MultistateSum n) {
		checkParameterUsage_SUM(n.argumentList);
		System.out.println(sumExpansion);
	}
	
	
	private void checkParameterUsage_SUM(ArgumentList node) {
		indexSum++;
		int found = getNumberArguments(node);
		/*if(found something about the number of parameters????) {
			misused.add("\nFunction "+f.getName()+" should have "+types.size()+ " parameters and not "+ found + " as in "+f.getName() + "("+ToStringVisitor.toString(node)+")");
			return;
		}*/
		MultistateSpecies multi_sp = null;
		for(int i = 0; i < found;) {
			INode elementNode = null;
			if(i ==0) {
				elementNode = ((NodeSequence)(node.nodeChoice.choice)).nodes.get(0);
				String element = ToStringVisitor.toString(elementNode);
				Species s = multiModel.getSpecies(element);
				if(s == null || !(s instanceof MultistateSpecies)) {
					misused.add("\nFunction \"SUM\" should  be used only on multistate species. The first parameter "+element+" is not a multistate species name.");
					return;
				}
				multi_sp = (MultistateSpecies) s;
				i++;
			}
			else {
				INode element2 = ((NodeSequence)(node.nodeChoice.choice)).nodes.get(1);
				try{
					i = threeElements(element2, i,multi_sp);
				} catch(Exception ex) {
					try{
						i = onlyName(element2, i,multi_sp);
					} catch(Exception ex2) {
						sumExpansion.remove(indexSum);
						indexSum--;
						misused.add("\nFunction \"SUM\" is misformed");
						return;

					}
				}


			}

		}


		return;

	}

	private int onlyName(INode element2, int i, MultistateSpecies multi_sp) throws Exception {
		INode elementNode = null;
		if(element2 instanceof NodeListOptional) {
			NodeSequence seq = (NodeSequence) ((NodeListOptional) element2).nodes.get(i-1);
			elementNode = seq.nodes.get(1); // because the first should be the separator ,
		}
		String site_name = ToStringVisitor.toString(elementNode); // site name

		Vector<String> sites = multi_sp.getSiteStates_complete(site_name);

		if(sites == null) {
			misused.add("\nFunction \"SUM\" is misformed. The site "+site_name+" does not exist for species "+multi_sp.getDisplayedName()+".");
			throw new Exception();
		}
		//the name is ok
		i++;
		SumExpansion se = new SumExpansion(multi_sp.getDisplayedName());
		se.addSite(site_name);
		sumExpansion.set(indexSum,se);
		se.addStates(site_name, new Vector(sites));
		return i;
	}


	private int threeElements(INode element2, int i, MultistateSpecies multi_sp) throws Exception {
		INode elementNode = null;
		if(element2 instanceof NodeListOptional) {
			NodeSequence seq = (NodeSequence) ((NodeListOptional) element2).nodes.get(i-1);
			elementNode = seq.nodes.get(1); // because the first should be the separator ,
		}
		String site_name = ToStringVisitor.toString(elementNode); // site name

		Vector<String> sites = multi_sp.getSiteStates_complete(site_name);

		if(sites == null) {
			misused.add("\nFunction \"SUM\" is misformed. The site "+site_name+" does not exist for species "+multi_sp.getDisplayedName()+".");
			throw new Exception();
		}

		SumExpansion se = new SumExpansion(multi_sp.getDisplayedName());
		se.addSite(site_name);
		sumExpansion.add(indexSum,se);

		//the name is ok
		i++;
		if(element2 instanceof NodeListOptional) {
			NodeSequence seq = (NodeSequence) ((NodeListOptional) element2).nodes.get(i-1);
			elementNode = seq.nodes.get(1); // because the first should be the separator ,
		}
		String element = ToStringVisitor.toString(elementNode);
		if( !sites.contains(element)) {
			i--;
			misused.add("\nFunction \"SUM\" is misformed. The site "+site_name+" does not contain the state "+element+".");
			se.addStates(site_name, new Vector(sites));
			throw new Exception();
		}
		int from = sites.indexOf(element);
		//the first element is ok

		i++;

		if(element2 instanceof NodeListOptional) {
			NodeSequence seq = (NodeSequence) ((NodeListOptional) element2).nodes.get(i-1);
			elementNode = seq.nodes.get(1); // because the first should be the separator ,
		}
		element = ToStringVisitor.toString(elementNode);
		if( !sites.contains(element)) {
			i--;
			i--;
			misused.add("\nFunction \"SUM\" is misformed. The site "+site_name+" does not contain the state "+element+".");
			se.addStates(site_name, new Vector(sites));
			throw new Exception();

		}
		int to = sites.indexOf(element);

		if(to < from) {
			misused.add("\nFunction \"SUM\" is misformed. The site states are not in the right order");
			throw new Exception();
		}

		se.addStates(site_name, new Vector(sites.subList(from, to+1)));

		i++;
		return i;
	}




	private void checkParameterUsage(ArgumentList node, Function f) {
		Vector<String> types = f.getParametersTypes();
		int found = getNumberArguments(node);
		if(types.size() != found) {
			misused.add("\nFunction "+f.getName()+" should have "+types.size()+ " parameters and not "+ found + " as in "+f.getName() + "("+ToStringVisitor.toString(node)+")");
			return;
		}
		
		for(int i = 0; i < found; i++) {
			
			INode elementNode = null;
			if(i ==0) elementNode = ((NodeSequence)(node.nodeChoice.choice)).nodes.get(0);
			else {
				INode element2 = ((NodeSequence)(node.nodeChoice.choice)).nodes.get(1);
				if(element2 instanceof NodeListOptional) {
					NodeSequence seq = (NodeSequence) ((NodeListOptional) element2).nodes.get(i-1);
					elementNode = seq.nodes.get(1); // because the first should be the separator ,
				}
			
			}

			if(Constants.FunctionParamType.VARIABLE.signatureType.compareTo(types.get(i))==0) {
				continue; //I DON'T CHECK ANYTHING... and it's THE ONLY type that allows SOMETHING to BE AN EXPRESSION WITHOUT RAISING ANY PROBLEM
			}
			else {
				String element = ToStringVisitor.toString(elementNode);
				/*Integer definedInTable = multiModel.getWhereNameIsUsed(element);
				if(definedInTable==null) {//it means that is a number or an expression... and if the function requires something else than a variable, this is not allowed
					misused.add("Encountered \""+element+"\". Was expecting a "+types.get(i));
				} else {
					if (	Constants.FunctionParamType.SUBSTRATE.signatureType.compareTo(types.get(i))==0 ||
							Constants.FunctionParamType.PRODUCT.signatureType.compareTo(types.get(i))==0	||
							Constants.FunctionParamType.MODIFIER.signatureType.compareTo(types.get(i))==0) {
						if(definedInTable!=Constants.TitlesTabs.SPECIES.index) misused.add(element);
					} else if (Constants.FunctionParamType.PARAMETER.signatureType.compareTo(types.get(i))==0) {
						if(definedInTable!=Constants.TitlesTabs.GLOBALQ.index) misused.add(element);
						
					} else if (Constants.FunctionParamType.VOLUME.signatureType.compareTo(types.get(i))==0) {
						if(definedInTable!=Constants.TitlesTabs.COMPARTMENTS.index) misused.add(element);
					}
				}*/
				 //TOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO CHECK... IF A NAME IS USED IN MORE THAN ONE SOMEWHERE THERE SHOULD BE THE CHECK THAT A SUFFIX EXISTS.... PROBABLY NOT HERE...
				Vector<Integer> definedInTable = multiModel.getWhereNameIsUsed(element);
				if(definedInTable==null){//it means that is a number or an expression... and if the function requires something else than a variable, this is not allowed
					if(!missing.contains(element)) { // because if it is missing it is also misused, but the user should see only the missing error
						misused.add("Encountered \""+element+"\". Was expecting a "+types.get(i));
					}
				}
				else {if (	Constants.FunctionParamType.SUBSTRATE.signatureType.compareTo(types.get(i))==0 ||
						Constants.FunctionParamType.PRODUCT.signatureType.compareTo(types.get(i))==0	||
						Constants.FunctionParamType.MODIFIER.signatureType.compareTo(types.get(i))==0) {
					if(!definedInTable.contains(new Integer(Constants.TitlesTabs.SPECIES.index))) {
						if(!missing.contains(element)) { // because if it is missing it is also misused, but the user should see only the missing error
							misused.add(element);
						}
					}
				} else if (Constants.FunctionParamType.PARAMETER.signatureType.compareTo(types.get(i))==0) {
					if(!definedInTable.contains(new Integer(Constants.TitlesTabs.GLOBALQ.index))) {
						if(!missing.contains(element)) { // because if it is missing it is also misused, but the user should see only the missing error
							misused.add(element);
						}
					}
				} else if (Constants.FunctionParamType.VOLUME.signatureType.compareTo(types.get(i))==0) {
					if(!definedInTable.contains(new Integer(Constants.TitlesTabs.COMPARTMENTS.index))) { 
						if(!missing.contains(element)) { // because if it is missing it is also misused, but the user should see only the missing error
							misused.add(element);
						}
					}
				}}
			//}
			}

		}
		return;
		
	}

	private int getNumberArguments(ArgumentList node) {
		int size = ((NodeSequence)(node.nodeChoice.choice)).nodes.size()-1;
		INode element = ((NodeSequence)(node.nodeChoice.choice)).nodes.get(1);
		if(element instanceof NodeListOptional) {
			NodeListOptional optList = (NodeListOptional) element;
			size += optList.nodes.size();
		}
		return size;
		
	}

	boolean isMultistateSitesList(INode n) {
		 if(n instanceof ArgumentList) {
			 if(((ArgumentList)n).nodeChoice.which ==0){
				 return true;
			 }  else return false;
		 }
		 else {
			 System.out.println("ERROR!" + n.getClass());
			 return false;
		 }
	 }





	
}


class SumExpansion {
	Vector<String> sites = new Vector<String>();
	Vector<Vector<String>> sitesStates = new Vector<Vector<String>>();
	String species_name = new String();
	Vector<Species> elementsSum = null;
	
	
	public SumExpansion(String name) {
		species_name = name;
	}
	
	void addSite(String name) {
		if(!sites.contains(name)) sites.add(name);
	}
	
	void addStates(String siteName, Vector<String> states) {
		int ind = sites.indexOf(siteName);
		if(ind >=0) {
			sitesStates.add(ind, states);
		}
	}
	
	@Override
	public String toString() {
		/*String ret = sites.toString() + "\n";
		for(Vector<String> el : sitesStates) {
			ret += el.toString();
		}*/
		String ret = printCompleteSum();
		return  ret;
	}
	
	
	public Vector<Species> getSpeciesSum(){
		try {
			MultistateSpecies ms = new MultistateSpecies(null, new String(species_name));
			for(int i = 0; i < sites.size(); i++) {
				ms.addSite_vector( sites.get(i),sitesStates.get(i));
			}
			
			elementsSum = ms.getExpandedSpecies(null);
		} catch (Exception e) {
			
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
		}
		
		return elementsSum;
	}
	
	public String printCompleteSum() {
		String ret = new String();
		
		try {
			MultistateSpecies ms = new MultistateSpecies(null, new String(species_name));
			for(int i = 0; i < sites.size(); i++) {
				ms.addSite_vector( sites.get(i),sitesStates.get(i));
			}
			
			Vector<Species> spec = ms.getExpandedSpecies(null);
			for(int i = 0; i < spec.size()-1; i++) {
				Species s = spec.get(i);
				ret += s.getDisplayedName() + " + ";
			}
			ret += spec.get(spec.size()-1).getDisplayedName();
			
		} catch (Exception e) {
			
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
		}
		
		
		return ret;
	}
}
