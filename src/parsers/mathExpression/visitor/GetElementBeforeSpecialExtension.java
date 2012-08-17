package parsers.mathExpression.visitor;
import java.util.ArrayList;
import java.util.Vector;

import parsers.mathExpression.MR_Expression_ParserConstants;
import parsers.mathExpression.MR_Expression_ParserConstantsNOQUOTES;
import parsers.mathExpression.syntaxtree.*;

public class GetElementBeforeSpecialExtension extends DepthFirstVoidVisitor {
		String returnName = new String();
		Vector<String> extensions = new Vector();
		
		public String getElementName() { return returnName; }	
	
	   public GetElementBeforeSpecialExtension()  {}
		
	   @Override
	   public void visit(Name n) {
		   super.visit(n);
		   if(n.nodeChoice.which ==0) {
			   NodeSequence nodes = (NodeSequence) n.nodeChoice.choice;
			   returnName = ToStringVisitor.toString(nodes.nodes.get(0));
			   NodeOptional listExtensions = (NodeOptional) nodes.nodes.get(1);
			   boolean foundMyExt = false;
			   if(listExtensions.present()) {

				   for(int i = 0; i < extensions.size(); i++) {

					   if( extensions.get(i).compareTo(MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.MY_SPECIAL_EXTENSION))==0) {

						   foundMyExt = true;
						   break;
					   } else{
						   returnName +=extensions.get(i);
					   }

				   }
			   }

			   if(!foundMyExt) {
				   returnName = null;
				   extensions.clear();
			   }
		   }
	   }
	
	@Override
	public void visit(PossibleExtensions n) {
		extensions.add(ToStringVisitor.toString(n.nodeChoice.choice));
		super.visit(n);
	}
	  




	
}
