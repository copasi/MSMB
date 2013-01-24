package msmb.parsers.chemicalReaction.visitor;

import msmb.parsers.chemicalReaction.syntaxtree.*;
import msmb.utility.CellParsers;
import msmb.gui.MainGui;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class SubstitutionVisitorReaction extends DepthFirstVoidVisitor {

		String originalName = new String();
		String replacementExpr = new String();
		PrintWriter out;
		ByteArrayOutputStream newExpression = new ByteArrayOutputStream();
		 
		 
		CompleteReaction newCompleteExpression = null;
	
	
	   public SubstitutionVisitorReaction(String originalVar, String replacementExpression)  {
		   originalName = originalVar;
		   replacementExpr = replacementExpression;
		   out = new PrintWriter(newExpression, true); 
	   }
	   
	   
	   public String getNewExpression() {	
		 	return newExpression.toString();
	   }
	   
	   NumberFormat formatter = new DecimalFormat("##########################.##########################");
		

		private void printToken(final String s) {	
			try{
				Double d = Double.parseDouble(s);
				out.print(formatter.format(d));
			} catch(Exception ex) { out.print(s);}
			finally { out.flush();}	
		}

	
		@Override
		public void visit(final NodeToken n) {   printToken(n.tokenImage); }

		@Override
		public void visit(Species n) {
			String name = ToStringVisitor.toString(n);
			if(name.compareTo(originalName)==0) printToken(replacementExpr);
			else printToken(name);
		// TODO Auto-generated method stub
		//super.visit(n);
		}
		
	/*	@Override
		public void visit(SpeciesReferenceOrFunctionCall_prefix n) {
			String name = ToStringVisitor.toString(n.name.nodeChoice.choice);
			if(n.nodeOptional.present())  {
				NodeOptional nodeOptional = (NodeOptional) ((NodeSequence) n.nodeOptional.node).nodes.get(1);
				if(nodeOptional.node==null){
					//System.out.println("FUNCTION CALL (0): "+name);
					if(name.compareTo(originalName)==0) printToken(replacementExpr);
					super.visit(n);
				}
				else {
					if(!isMultistateSitesList(nodeOptional.node)) {
						//System.out.println("FUNCTION CALL ("+getNumberArguments((ArgumentList)nodeOptional.node)+"): " +name);
						if(name.compareTo(originalName)==0) printToken(replacementExpr);
						super.visit(n.nodeOptional);
					} else {
						//System.out.println("SPECIES: "+ToStringVisitor.toString(n)); // to print complete "multistate" definition
						if(name.compareTo(originalName)==0) printToken("("+replacementExpr+")");
						else super.visit(n);
					}
				}
			} else {
				//System.out.println("SPECIES: "+ToStringVisitor.toString(n));
				if(name.compareTo(originalName)==0) printToken("("+replacementExpr+")");
				else super.visit(n);
			}

	}*/
	  
	 
	  
	 







	
}
