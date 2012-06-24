package parsers.mathExpression.visitor;
import java.util.ArrayList;
import java.util.Vector;

import parsers.mathExpression.MR_Expression_ParserConstants;
import parsers.mathExpression.MR_Expression_ParserConstantsNOQUOTES;
import parsers.mathExpression.syntaxtree.*;

public class GetElementWithExtensions extends DepthFirstVoidVisitor {
		String returnName = new String();
		Vector<String> extensions = new Vector<String>();
		boolean getName = false;
		
		public String getElementName() { return returnName; }	
		public Vector<String> getExtensions() { return extensions; }	
		
	   public GetElementWithExtensions()  {}
		
	@Override
	public void visit(Name n) {
		if(n.nodeChoice.which ==0) {
			super.visit(n);
			NodeSequence nodes = (NodeSequence) n.nodeChoice.choice;
			returnName = ToStringVisitor.toString(nodes.nodes.get(0));
		}
	}
	
	@Override
	public void visit(PossibleExtensions n) {
		extensions.add(ToStringVisitor.toString(n.nodeChoice.choice));
		super.visit(n);
	}
	  




	
}
