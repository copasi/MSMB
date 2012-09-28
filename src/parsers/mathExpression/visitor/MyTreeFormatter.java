package parsers.mathExpression.visitor;

import parsers.mathExpression.MR_Expression_ParserConstants;
import parsers.mathExpression.syntaxtree.NodeToken;

public class MyTreeFormatter extends TreeFormatter {
	
	 public MyTreeFormatter(int i, int j) {
		super(i,j);
	}

	public void visit(final NodeToken n) {
		 //System.out.println(MR_Expression_ParserConstants.tokenImage[n.kind]);
		 super.visit(n);
	 }
}
