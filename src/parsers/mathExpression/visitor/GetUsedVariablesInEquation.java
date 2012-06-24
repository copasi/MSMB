package parsers.mathExpression.visitor;
import parsers.mathExpression.syntaxtree.*;

import java.util.*;

public class GetUsedVariablesInEquation extends DepthFirstVoidVisitor {
	 TreeMap<String, Comparable> names;
	
	   public GetUsedVariablesInEquation()  {
		   names = new  TreeMap<String, Comparable>();
	   }

		public TreeMap<String, Comparable> getNames() {	return names;	}
		
		
	@Override
	public void visit(SpeciesReferenceOrFunctionCall_prefix n) {
		String name = ToStringVisitor.toString(n.name.nodeChoice.choice);
		if(n.nodeOptional.present())  {
			NodeOptional nodeOptional = (NodeOptional) ((NodeSequence) n.nodeOptional.node).nodes.get(1);
			if(nodeOptional.node==null){
				//System.out.println("FUNCTION CALL (0): "+name);
			}
			else {
				if(!isMultistateSitesList(nodeOptional.node)) {
					//System.out.println("FUNCTION CALL ("+getNumberArguments((ArgumentList)nodeOptional.node)+"): " +name);
				} else {
					//System.out.println("SPECIES: "+ToStringVisitor.toString(n)); // to print complete "multistate" definition
					//names.put(namePar,index); // order as they appear
					names.put(ToStringVisitor.toString(n),ToStringVisitor.toString(n)); // order alphabetical
					
				}
			}
		} else {
			//System.out.println("SPECIES: "+ToStringVisitor.toString(n));
			names.put(ToStringVisitor.toString(n),ToStringVisitor.toString(n));
		}
		super.visit(n);
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
