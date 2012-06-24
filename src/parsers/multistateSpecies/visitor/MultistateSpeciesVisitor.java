package parsers.multistateSpecies.visitor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

import org.apache.commons.lang3.tuple.MutablePair;

import model.MultiModel;
import model.MultistateSpecies;
import model.Species;


import parsers.multistateSpecies.MR_MultistateSpecies_Parser;
import parsers.multistateSpecies.MR_MultistateSpecies_ParserConstants;
import parsers.multistateSpecies.MR_MultistateSpecies_ParserConstantsNOQUOTES;
import parsers.multistateSpecies.syntaxtree.*;

public class MultistateSpeciesVisitor extends DepthFirstVoidVisitor
{
	
	 private String speciesName;
 	 private HashMap<String,Vector<String>> site_states;
	 private HashMap<String, MutablePair<Integer, Integer>> pureRange_sites;
     Vector<Exception> exceptions = new Vector<>();
	public Vector<Exception> getExceptions() { return exceptions; }
	   
	 private String current_site = null;
	 private Vector<String> current_states = null;
	 private Integer current_high;
	 private Integer current_low;
	
	 public String getSpeciesName() { return speciesName; }
	 public Vector<String> getSite_states(String site) {	return site_states.get(site);	}
	 public Set<String> getAllSites_names() {	return site_states.keySet();	}
	 public MutablePair<Integer, Integer> getPureRangeLimits(String site) { return pureRange_sites.get(site);	}
	
	 private MultistateSpecies reactant = null;
	private String expandedForm = null;
	 private HashMap<String,String> current_site_nextState;
	MultiModel multiModel = null;
	 public String getProductExpansion() {			return expandedForm;	}
	
	 public MultistateSpeciesVisitor(MultiModel mm) { multiModel = mm;	}
	 
	public MultistateSpeciesVisitor(MultiModel mm, Species multistate_reactant) { //constructor used in the expansion
		 try {
			 multiModel = mm;	
			reactant = new MultistateSpecies(multiModel,multistate_reactant.getDisplayedName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	
	
	
	@Override
	public void visit(CompleteMultistateSpecies_Operator n) {
		speciesName = new String(ToStringVisitor.toString(n.multistateSpecies_Operator.multistateSpecies_Name.nodeChoice.choice));
		if(reactant!= null) {
			current_site_nextState = new HashMap<String,String>();
			super.visit(n);
			
			if(speciesName.compareTo(reactant.getSpeciesName())==0) {
				expandedForm  = buildExpandedForm();
				if(expandedForm == null) {
						exceptions.add(new Exception("SOMEEEEEEEEEEEETHING WRONG"));
				}
			} else {
				expandedForm = speciesName;
			}
		}
	}
	
	 
	@Override
	 public void visit(MultistateSpecies_Operator_SingleSite n) {
		 if(n.nodeChoice.which==1) {
			 NodeSequence seq = (NodeSequence)(n.nodeChoice.choice);
			 String operator = ToStringVisitor.toString(seq.nodes.get(0));
			 String siteName = ToStringVisitor.toString(seq.nodes.get(2));
			 current_site_nextState.put(siteName, operator);
		 } else {
			 NodeSequence seq = (NodeSequence)(n.nodeChoice.choice);
			 String siteName = ToStringVisitor.toString(seq.nodes.get(0));
			 
			 if(seq.nodes.size()>1) {
				 NodeOptional opt = (NodeOptional)(seq.nodes.get(1));
				 NodeSequence seq2 = (NodeSequence) (opt.node);
				 if(seq2!=null) {	
					 	String state = ToStringVisitor.toString(seq2.nodes.get(1));
				 		current_site_nextState.put(siteName, state);
				 }
			 }
		 }
	 }
	 
	 private String buildExpandedForm() {
		 if(speciesName.compareTo(reactant.getSpeciesName())!= 0) {
			 System.out.println("quiiiiiiii "+speciesName);
			 return speciesName;
		 }
		 String ret = new String();
		 MultistateSpecies multi = (MultistateSpecies) multiModel.getSpecies(speciesName);
		 ret+= speciesName + "(";
		// HashMap product_sites = new HashMap();
			
		//  current_site_operator.keySet().iterator();
		 Iterator<String> it = reactant.getSitesNames().iterator();
		 while(it.hasNext()) {
				String site = it.next();
				String state=(String) reactant.getSiteStates_complete(site).get(0);
				if(!current_site_nextState.containsKey(site)) {
					ret += site + "{" + state + "};";
				} else {
					String nextState = null;
					String val = current_site_nextState.get(site);
					if(val.compareTo(MR_MultistateSpecies_ParserConstantsNOQUOTES.getTokenImage(MR_MultistateSpecies_ParserConstants.SUCC))==0) {
						nextState = multi.getSucc(site,state,false);
					} else if(val.compareTo(MR_MultistateSpecies_ParserConstantsNOQUOTES.getTokenImage(MR_MultistateSpecies_ParserConstants.PREC))==0) {
						nextState = multi.getPrec(site,state,false);
					} else if(val.compareTo(MR_MultistateSpecies_ParserConstantsNOQUOTES.getTokenImage(MR_MultistateSpecies_ParserConstants.CIRC_R_SHIFT))==0) {
						nextState = multi.getSucc(site,state,true);
					} else if(val.compareTo(MR_MultistateSpecies_ParserConstantsNOQUOTES.getTokenImage(MR_MultistateSpecies_ParserConstants.CIRC_L_SHIFT))==0) {
						nextState = multi.getPrec(site,state,true);
					} else {
						nextState = val;
					}
					if(nextState==null) return null;
					ret += site + "{" + nextState + "};";
					
				}
				
			}
		ret = ret.substring(0, ret.length()-1);
		ret += ")";	
		return ret;
	}
	 
		
	 
	 
	public void visit(NodeToken n)
	  {
	    //System.out.println("visit " + MR_MultistateSpecies_ParserConstants.tokenImage[n.kind] + "-->" + n.tokenImage);
	  }
	
	@Override
	public void visit(parsers.multistateSpecies.syntaxtree.MultistateSpecies n) {
		 speciesName = new String(ToStringVisitor.toString(n.multistateSpecies_Name.nodeChoice.choice));
		 site_states = new HashMap<String,Vector<String>>();
		 pureRange_sites = new HashMap<String, MutablePair<Integer, Integer>> ();
		 super.visit(n);
		 fill_pureRange_sites();
	}

	

	  @Override
	  public void visit(MultistateSpecies_SingleStateDefinition n) {
		  super.visit(n);
		  //System.out.println("current_site: "+current_site);
		  //System.out.println("current_states: "+current_states);
		  site_states.put(current_site, current_states);
		  current_site = null;
		  current_states = null;
	  }
	  
	  
	  @Override
	  public void visit(MultistateSpecies_SiteName n) {
		  current_site  = new String(ToStringVisitor.toString(n.nodeChoice.choice));
		  super.visit(n);
		
	  }
	
	   public void visit(MultistateSpecies_SiteSingleElement n) {
		  if(current_states == null) current_states = new Vector();
		  current_states.add(ToStringVisitor.toString(n.nodeChoice.choice));
		  super.visit(n);
	  }
	  
	  @Override
		public void visit(MultistateSpecies_SiteSingleElement_Range n) {
		  try{
			  current_low = new Integer(n.nodeToken.tokenImage);
			  current_high = new Integer(n.nodeToken2.tokenImage);
			  //System.out.println("low = "+current_low+"; high = "+current_high);
			  if(current_low>=current_high) throw new Exception("Error in \""+ToStringVisitor.toString(n)+"\": lower bound should be < upper bound.");
		  } catch(Exception ex) {
			   ex.printStackTrace();
			   exceptions.add(ex);
		   }
		  super.visit(n); 
	    
		}

	  
	  void fill_pureRange_sites() {
		 
			  Iterator<String> it = site_states.keySet().iterator();
			  while(it.hasNext()) {
				  String site = it.next();
				  if(site_states.get(site).size() == 1) {
					  String states = site_states.get(site).get(0);
					  try {  InputStream is = new ByteArrayInputStream(states.getBytes("UTF-8"));
					  		MR_MultistateSpecies_Parser react = new MR_MultistateSpecies_Parser(is);
					  		CompleteMultistateSpecies_Range range = react.CompleteMultistateSpecies_Range();
					  		range.accept(this);
					  		pureRange_sites.put(site, new MutablePair<Integer,Integer>(current_low,current_high));
					  } catch (Exception e) {
						 // e.printStackTrace();
					}
				  }
				}
		  }
	
			  
	}
