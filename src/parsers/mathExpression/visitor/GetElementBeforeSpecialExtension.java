package parsers.mathExpression.visitor;
import java.util.ArrayList;

import parsers.mathExpression.MR_Expression_ParserConstants;
import parsers.mathExpression.MR_Expression_ParserConstantsNOQUOTES;
import parsers.mathExpression.syntaxtree.*;

public class GetElementBeforeSpecialExtension extends DepthFirstVoidVisitor {
		String returnName = new String();
		String firstExtension = null;
		boolean getName = false;
		public String getElementName() { return returnName; }	
	
	   public GetElementBeforeSpecialExtension()  {}
		
	@Override
	public void visit(Name n) {
		if(n.nodeChoice.which ==0) {
			super.visit(n);
			if(getName) {
				NodeSequence nodes = (NodeSequence) n.nodeChoice.choice;
				returnName = ToStringVisitor.toString(nodes.nodes.get(0));
				if(firstExtension.compareTo(MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_COMPARTMENT))==0
				   | firstExtension.compareTo(MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_GLOBALQ))==0
				   | firstExtension.compareTo(MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_SPECIES))==0
					)
						returnName += firstExtension;
				}
			}
	}
	
	@Override
	public void visit(PossibleExtensions n) {
		if( ToStringVisitor.toString(n.nodeChoice.choice).compareTo(MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.MY_SPECIAL_EXTENSION))==0) {
			getName = true;
		}
		if(firstExtension == null)firstExtension = ToStringVisitor.toString(n.nodeChoice.choice);
		super.visit(n);
	}
	  




	
}
