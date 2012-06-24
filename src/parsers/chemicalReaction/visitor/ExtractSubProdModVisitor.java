package parsers.chemicalReaction.visitor;

import parsers.chemicalReaction.MR_ChemicalReaction_ParserConstants;
import parsers.chemicalReaction.syntaxtree.*;
import parsers.mathExpression.MR_Expression_ParserConstants;

import java.util.*;

import org.apache.commons.lang3.tuple.MutablePair;

import model.MultiModel;
import model.MultistateSpecies;



public class ExtractSubProdModVisitor extends DepthFirstVoidVisitor {
		Vector<MutablePair<Double, String>> substrates;
		Vector<MutablePair<Double, String>> products;
		Vector<MutablePair<Double, String>> modifiers;
		private int use_vector = 0;
		private Double stoichiometry = new Double(1.0);
		MultiModel multiModel = null;
		
	   public ExtractSubProdModVisitor(MultiModel m)  {
		   substrates = new Vector<MutablePair<Double, String>>();
		   products = new Vector<MutablePair<Double, String>>();
		   modifiers = new Vector<MutablePair<Double, String>>();
		   multiModel = m;
	   }

		Vector<Exception> exceptions = new Vector<>();
		 public Vector<Exception> getExceptions() { return exceptions; }
	   
		public Vector<Vector<MutablePair<Double,String>>> getAll() {	
			Vector ret = new Vector<Vector<MutablePair<Double,String>>> ();
			ret.add(getSubstrates());
			ret.add(getProducts());
			ret.add(getModifiers());
			return ret;	
		}
		
		public Vector<Vector<MutablePair<Double,String>>> getAll_asString() {	
			Vector ret = new Vector<Vector<String>> ();
			ret.add(getSubstrates_asString());
			ret.add(getProduct_asString());
			ret.add(getModifiers_asString());
			return ret;	
		}
		
		private Vector<MutablePair<Double,String>> getSubstrates_asString() {	
			Vector ret = new Vector<String>();
			for(MutablePair<Double, String> element  : substrates) {
				String name = new String();
				if(element.getLeft().doubleValue() != 1.0) name += element.getLeft().toString() + " * ";
				name += element.getRight();
				ret.add(name);
			}
			return ret;		
		}
		
		private Vector<MutablePair<Double,String>> getProduct_asString() {	
			Vector ret = new Vector<String>();
			for(MutablePair<Double, String> element  : products) {
				String name = new String();
				if(element.getLeft().doubleValue() != 1.0) name += element.getLeft().toString() + " * ";
				name += element.getRight();
				ret.add(name);
			}
			return ret;		
		}
		
		private Vector<MutablePair<Double,String>> getModifiers_asString() {	
			Vector ret = new Vector<String>();
			for(MutablePair<Double, String> element  : modifiers) {
				String name = new String();
				if(element.getLeft().doubleValue() != 1.0) name += element.getLeft().toString() + " * ";
				name += element.getRight();
				ret.add(name);
			}
			return ret;		
		}
		
		private Vector<MutablePair<Double,String>> getSubstrates() {	
			Vector ret = new Vector<MutablePair<Double,String>>();	ret.addAll(substrates);	return ret;		}
		
		private Vector<MutablePair<Double,String>> getProducts() {	
			Vector ret = new Vector<MutablePair<Double,String>>();	ret.addAll(products);	return ret;		}
		
		private Vector<MutablePair<Double,String>> getModifiers() {	
			Vector ret = new Vector<MutablePair<Double,String>>();	ret.addAll(modifiers);	return ret;		}
		
	
		
		@Override
	public void visit(Species n) {
		String name = new String(n.nodeToken.tokenImage);
		  int[] vectorExtensions = {
				  	MR_Expression_ParserConstants.EXTENSION_COMPARTMENT,
				  	MR_Expression_ParserConstants.EXTENSION_CONC,
				  	MR_Expression_ParserConstants.EXTENSION_GLOBALQ,
				  	MR_Expression_ParserConstants.EXTENSION_PARTICLE,
				  	MR_Expression_ParserConstants.EXTENSION_RATE,
				  	MR_Expression_ParserConstants.EXTENSION_SPECIES,
				  	MR_Expression_ParserConstants.EXTENSION_TRANS
		  };
		  for(int index : vectorExtensions) {
			  String noQuotes= new String(MR_Expression_ParserConstants.tokenImage[index]);
			  noQuotes = noQuotes.substring(1,noQuotes.length()-1);
		       if(name.toString().endsWith(noQuotes))  {
				     exceptions.add(new Exception("Error: The name cannot end with the reserved sequence "+noQuotes));
				     return;
			  }
		  }
		  
		  if(isMultistateSpecies(name)) {
			  MultistateSpecies r = null;
			  try {
				  r = new MultistateSpecies(multiModel,name);
			  } catch (Exception e) {
				  exceptions.add(new Exception("Error: "+e.getMessage()));
				  return;
			  }
			  name = r.printCompleteDefinition();
		  }

		  if(use_vector==0) {
			  substrates.add(new MutablePair(stoichiometry, name));
			  stoichiometry = new Double(1.0);
		  } else if(use_vector == 1){
			  products.add(new MutablePair(stoichiometry, name));
			  stoichiometry = new Double(1.0);
		  } else {
			  modifiers.add(new MutablePair(stoichiometry, name));
			  stoichiometry = new Double(1.0);
		  }
		  
		  super.visit(n);
		}
		
		private boolean isMultistateSpecies(String name) {
			return false;
		}

		@Override
		public void visit(ListModifiers n) {
			use_vector = 2;
			super.visit(n);
		}
		
		public void visit(NodeToken n)
		  {
		    
			String noQuotes = new String(MR_ChemicalReaction_ParserConstants.tokenImage[n.kind]);
			//noQuotes = noQuotes.substring(1,noQuotes.length()-1).trim();
			if(noQuotes.compareTo(MR_ChemicalReaction_ParserConstants.tokenImage[MR_ChemicalReaction_ParserConstants.ARROW])==0 ||
				noQuotes.compareTo(MR_ChemicalReaction_ParserConstants.tokenImage[MR_ChemicalReaction_ParserConstants.ARROW2])==0){
				use_vector = 1;
			}
			
		  }
		
		
		@Override
		public void visit(SpeciesWithCoeff n) {
			if(n.nodeOptional.present()) {
				Stoichiometry nodeOptional = (Stoichiometry) ((NodeSequence) n.nodeOptional.node).nodes.get(0);
				stoichiometry = new Double(ToStringVisitor.toString(nodeOptional));
			} 
			super.visit(n);
		}
		
		
		String justSpeciesName = new String();
		@Override
		public void visit(CompleteSpeciesWithCoefficient n) {
			justSpeciesName = new String(n.speciesWithCoeff.species.nodeToken.tokenImage);
			if(n.speciesWithCoeff.nodeOptional.present()) {
				Stoichiometry nodeOptional = (Stoichiometry) ((NodeSequence) n.speciesWithCoeff.nodeOptional.node).nodes.get(0);
				stoichiometry = new Double(ToStringVisitor.toString(nodeOptional));
			} 
		}
		
		public String getExtractedName_fromSpeciesWithCoefficient() {
			return  justSpeciesName;
		}
		
		public Double getExtractedCoeff_fromSpeciesWithCoefficient() {
			return  stoichiometry;
		}
		
		
/*	@Override
	public void visit(SpeciesReferenceOrFunctionCall_prefix n) {
		String name = ToStringVisitor.toString(n.name.nodeChoice.choice);
		if(n.nodeOptional.present())  {
			NodeOptional nodeOptional = (NodeOptional) ((NodeSequence) n.nodeOptional.node).nodes.get(1);
			if(nodeOptional.node==null){
				//System.out.println("FUNCTION CALL (0): "+name);
				Function f = MainGui.funDB.getFunctionByName(name);
				if(f==null) missing.add(name);
			}
			else {
				if(!isMultistateSitesList(nodeOptional.node)) {
					//System.out.println("FUNCTION CALL ("+getNumberArguments((ArgumentList)nodeOptional.node)+"): " +name);
					Function f = MainGui.funDB.getFunctionByName(name);
					if(f==null)	missing.add(name);
					else{
						checkParameterUsage((ArgumentList)nodeOptional.node,f);
						
					}

				} else {
					//System.out.println("SPECIES: "+ToStringVisitor.toString(n)); // to print complete "multistate" definition
					if(MultiModel.getWhereNameIsUsed(name)==null) missing.add(name);
				}
			}
		} else {
			//System.out.println("SPECIES: "+ToStringVisitor.toString(n));
			if(MultiModel.getWhereNameIsUsed(name)==null) missing.add(name);
		}
		super.visit(n);
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
				Integer definedInTable = MultiModel.getWhereNameIsUsed(element);
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
				}
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


*/


	
}
