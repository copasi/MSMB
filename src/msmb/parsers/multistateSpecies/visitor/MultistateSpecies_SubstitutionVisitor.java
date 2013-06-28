package msmb.parsers.multistateSpecies.visitor;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;

import msmb.gui.MainGui;
import msmb.parsers.multistateSpecies.syntaxtree.*;
import msmb.utility.CellParsers;

public class MultistateSpecies_SubstitutionVisitor  extends DepthFirstVoidVisitor {
	String originalSpeciesFullDefinition = new String();
	String replacementSpeciesName = new String();
	HashMap<String, String> sitesName_originalReplacement = new HashMap<String, String> ();
	boolean replaceElementsAfterTransferAssign = false;
	PrintWriter out;
	ByteArrayOutputStream newMultistate = new ByteArrayOutputStream();
	 
	 public String getNewMultistate() {	
		return newMultistate.toString();
	 }
	 
   public MultistateSpecies_SubstitutionVisitor(String originalSpFull, String replacementSpName, HashMap<String, String> sitesName_origRepl)  {
	   this(originalSpFull,replacementSpName,sitesName_origRepl, false);
   }
   
    public MultistateSpecies_SubstitutionVisitor(String originalSpFull, String replacementSpName, HashMap<String, String> sitesName_origRepl, boolean replaceElementsAfterTransferAssign)  {
	   originalSpeciesFullDefinition = originalSpFull;
	   replacementSpeciesName = replacementSpName;
	   sitesName_originalReplacement.clear();
	   sitesName_originalReplacement.putAll(sitesName_origRepl);
	   out = new PrintWriter(newMultistate, true); 
	   this.replaceElementsAfterTransferAssign = replaceElementsAfterTransferAssign;
   }
   
    @Override
    public void visit(MultistateSpecies_Name n) {
    	String name = ToStringVisitor.toString(n);
    	
    	if(!replaceElementsAfterTransferAssign) {
	    	if(originalSpeciesFullDefinition!= null && name.compareTo(CellParsers.extractMultistateName(originalSpeciesFullDefinition))==0 &&
	    			replacementSpeciesName != null) {
	    		out.print(replacementSpeciesName);
	    	} else {
	    		out.print(name);
	    	}
	    	 out.flush();
    	}  else {
    		out.print(name);
    		out.flush();
    	}
    	
    }
    
    @Override
    public void visit(MultistateSpecies_SiteName n) {
    	String name = ToStringVisitor.toString(n);
    	if(!replaceElementsAfterTransferAssign) {
	    	if(sitesName_originalReplacement.containsKey(name)) {
	    		out.print(sitesName_originalReplacement.get(name));
	    	} else {
	    		out.print(name);
	    	}
	    	 out.flush();
    	}	 else {
    		out.print(name);
    		out.flush();
    	} 
    }
    
    
    @Override
    public void visit(MultistateSpecies_Operator_SiteTransferSelector n) {
    	if(!replaceElementsAfterTransferAssign) {
    		out.print(ToStringVisitor.toString(n));
    		out.flush();
    		return;
    	}
    	
    	String spFromName = new String();
    	String siteFromName = new String();
    	if(n.nodeChoice.which == 0){
			NodeSequence sequence = (NodeSequence)(n.nodeChoice.choice);
			out.println(ToStringVisitor.toString(sequence.nodes.get(0))); // print succ/pred
			out.println(ToStringVisitor.toString(sequence.nodes.get(1))); // print (
			spFromName = ToStringVisitor.toString(sequence.nodes.get(2));
			out.print(spFromName); // print species name
			out.println(ToStringVisitor.toString(sequence.nodes.get(3))); // print dot
			//print old name or new name
			siteFromName = ToStringVisitor.toString(sequence.nodes.get(4));
    		if(spFromName.compareTo(replacementSpeciesName) == 0) {
				if(sitesName_originalReplacement.containsKey(siteFromName)) {
		    		out.print(sitesName_originalReplacement.get(siteFromName));
		    	} else {
		    		out.print(siteFromName);
		    	}
		    	 out.flush();
			} else {
				out.print(siteFromName);
			}
    		out.println(ToStringVisitor.toString(sequence.nodes.get(5))); // print )
			
			 out.flush();
	    		
    	} else {
    			NodeSequence sequence = (NodeSequence)(n.nodeChoice.choice);
    			spFromName = ToStringVisitor.toString(sequence.nodes.get(0));
    			siteFromName = ToStringVisitor.toString(sequence.nodes.get(2));
    			out.print(spFromName); // print species name
    			out.println(ToStringVisitor.toString(sequence.nodes.get(1))); // print dot
    			
    			//print old name or new name
    			if(spFromName.compareTo(replacementSpeciesName) == 0) {
    				if(sitesName_originalReplacement.containsKey(siteFromName)) {
    		    		out.print(sitesName_originalReplacement.get(siteFromName));
    		    	} else {
    		    		out.print(siteFromName);
    		    	}
    		    	 out.flush();
    			} else {
    				out.print(siteFromName);
    			}
    			 out.flush();
    	}
    	
    	
    
    }
    	
    
    @Override
    public void visit(MultistateSpecies_Operator_SiteName n) {
    	String name = ToStringVisitor.toString(n);
    	if(!replaceElementsAfterTransferAssign) {
	    	if(sitesName_originalReplacement.containsKey(name)) {
	    		out.print(sitesName_originalReplacement.get(name));
	    	} else {
	    		out.print(name);
	    	}
	    	 out.flush();
    	} else {
	     		out.print(name);
	     		out.flush();
	     	} 
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

}
