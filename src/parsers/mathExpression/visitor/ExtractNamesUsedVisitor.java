package parsers.mathExpression.visitor;
import model.Function;
import model.MultiModel;
import model.MultistateSpecies;
import model.Species;
import parsers.mathExpression.MR_Expression_ParserConstants;
import parsers.mathExpression.MR_Expression_ParserConstantsNOQUOTES;
import parsers.mathExpression.MR_Expression_Parser_ReducedParserException;
import parsers.mathExpression.syntaxtree.*;
import parsers.multistateSpecies.MR_MultistateSpecies_Parser;
import parsers.multistateSpecies.ParseException;
import parsers.multistateSpecies.syntaxtree.CompleteMultistateSpecies_Operator;
import parsers.multistateSpecies.visitor.MultistateSpeciesVisitor;

import gui.MainGui;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

import com.sun.tools.javac.code.Attribute.Array;

import utility.CellParsers;
import utility.Constants;

public class ExtractNamesUsedVisitor extends DepthFirstVoidVisitor {
		HashSet<String> names = new HashSet<String>();
		MultiModel multiModel = null;
		
	
	   public ExtractNamesUsedVisitor(MultiModel mm)  {
		   multiModel = mm;
	   }

		public Vector<String> getNamesUsed() {	
			Vector ret = new Vector<String>();
			ret.addAll(names);
			return ret;	
		}
		
	@Override
	public void visit(SpeciesReferenceOrFunctionCall_prefix n) {
		super.visit(n);
		String name = ToStringVisitor.toString(n.name.nodeChoice.choice);
		
		if(n.nodeOptional.present())  {
			NodeOptional nodeOptional = (NodeOptional) ((NodeSequence) n.nodeOptional.node).nodes.get(1);
			if(nodeOptional.node==null){
				
				//System.out.println("FUNCTION CALL (0): "+name);
			//	Function f = multiModel.getFunctionByName(name);
				
					Function f = null;
					try {
						f = multiModel.getFunctionByName(ToStringVisitor.toString(n.name.nodeChoice.choice));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(f==null) {	
						if(!isKeyword(ToStringVisitor.toString(n.name.nodeChoice.choice))) {
							names.add(ToStringVisitor.toString(n.name.nodeChoice.choice)); 
						}
					}
			}
			else {
				if(!isMultistateSitesList(nodeOptional.node)) {
					//System.out.println("FUNCTION CALL ("+getNumberArguments((ArgumentList)nodeOptional.node)+"): " +name);
					//Function f = multiModel.getFunctionByName(name);
					name = ToStringVisitor.toString(n.name.nodeChoice.choice);
					
					
						Function f = null;
						try {
							f = multiModel.getFunctionByName(name);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if(f==null){	
							if(!isKeyword(ToStringVisitor.toString(n.name.nodeChoice.choice))) {
								names.add(name); }
							}
						else{
							checkParameterUsage((ArgumentList)nodeOptional.node,f);
						}
				
				} else {
					//System.out.println("SPECIES: "+ToStringVisitor.toString(n)); // to print complete "multistate" definition
					if(CellParsers.isMultistateSpeciesName(name)) {
						try {
							names.add((new MultistateSpecies(multiModel, name).getSpeciesName()));
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else if(!isKeyword(name)) 	{
						names.add(name);
						}
					
					}
				}
			}
		 else {
			if(!isKeyword(name)) {
					names.add(name);
			}
		}
		super.visit(n);
	}
	  
	 
	private boolean isKeyword(String name) {
		if(name.toLowerCase().compareTo(Constants.NAN_STRING) == 0) return true;
		if(name.compareTo(MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstantsNOQUOTES.TIME)) == 0) return true;
		if(name.compareTo(MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstantsNOQUOTES.FLOOR)) == 0) return true;
		if(name.compareTo(MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstantsNOQUOTES.SQRT)) == 0) return true;
		if(name.compareTo(MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstantsNOQUOTES.EXP)) == 0) return true;
		if(name.compareTo(MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstantsNOQUOTES.LOG)) == 0) return true;
		return false;
	}

	

	private void checkParameterUsage(ArgumentList node, Function f) {
		Vector<String> types = f.getParametersTypes();
		int found = getNumberArguments(node);
		
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

			String element = ToStringVisitor.toString(elementNode);
			
			Vector<Integer> definedInTable = multiModel.getWhereNameIsUsed(element);
				if(definedInTable==null){//it means that is a number or an expression... and if the function requires something else than a variable, this is not allowed
					return;
				}
				else {names.add(element);}
			
			}
			
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

	private int getNumberArguments(ArgumentList node) {
		int size = ((NodeSequence)(node.nodeChoice.choice)).nodes.size()-1;
		INode element = ((NodeSequence)(node.nodeChoice.choice)).nodes.get(1);
		if(element instanceof NodeListOptional) {
			NodeListOptional optList = (NodeListOptional) element;
			size += optList.nodes.size();
		}
		return size;
		
	}



	
}
