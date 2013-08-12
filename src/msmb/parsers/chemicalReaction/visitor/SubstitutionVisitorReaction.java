package msmb.parsers.chemicalReaction.visitor;

import msmb.parsers.chemicalReaction.syntaxtree.*;
import msmb.utility.CellParsers;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import org.apache.commons.lang3.tuple.MutablePair;

public class SubstitutionVisitorReaction extends DepthFirstVoidVisitor {

		String originalName = new String();
		String replacementExpr = new String();
		private HashMap<String, String> aliases;
		
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
		public void visit(CompleteReaction n) {
			String reaction_string = ToStringVisitor.toString(n);
			try {
				aliases = CellParsers.getAllAliases(reaction_string);
			} catch (Exception e) {
				e.printStackTrace();
			}
			super.visit(n);
		}
		
		@Override
		public void visit(Species n) {
			String name = ToStringVisitor.toString(n);
			if(name.compareTo(originalName)==0) printToken(replacementExpr);
			else {
				MutablePair<String, String> aliasPair = CellParsers.extractAlias(name);
				if(aliasPair.left != null) {
					name = aliasPair.right;
					out.print(aliasPair.left+"=");
				}
				if(CellParsers.isMultistateSpeciesName(name)) {
					printNewMultistate(name, originalName, replacementExpr);
				}
				else printToken(name);
			}
		}

		
		private void printNewMultistate(String currentSpecies, String originalName,	String replacementExpr) {
			String onlyNameOriginal = CellParsers.extractMultistateName(originalName);
			String  onlyNameReplacementExpr = CellParsers.extractMultistateName(replacementExpr);
			String onlyNameCurrentSpecies = CellParsers.extractMultistateName(currentSpecies);
						
			try{
				if(onlyNameCurrentSpecies.compareTo(onlyNameOriginal) == 0) {
					out.print(currentSpecies.replaceFirst(onlyNameOriginal, onlyNameReplacementExpr));
				} else {
					out.print(CellParsers.replaceSpeciesName_AfterTransferAssignment(currentSpecies, onlyNameOriginal, replacementExpr, aliases));
					//out.print(currentSpecies);
				}
			} catch(Exception ex) { 
				//ex.printStackTrace();
				out.print(currentSpecies);
				
			}
			finally { out.flush();}	
			}
		
}
	
