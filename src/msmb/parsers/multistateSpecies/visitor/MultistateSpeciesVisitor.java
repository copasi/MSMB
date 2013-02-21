package msmb.parsers.multistateSpecies.visitor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

import org.apache.commons.lang3.tuple.MutablePair;

import msmb.gui.MainGui;
import msmb.model.MultiModel;
import msmb.model.MultistateSpecies;
import msmb.model.Species;


import msmb.parsers.multistateSpecies.MR_MultistateSpecies_Parser;
import msmb.parsers.multistateSpecies.MR_MultistateSpecies_ParserConstants;
import msmb.parsers.multistateSpecies.MR_MultistateSpecies_ParserConstantsNOQUOTES;
import msmb.parsers.multistateSpecies.ParseException;
import msmb.parsers.multistateSpecies.syntaxtree.*;
import msmb.utility.CellParsers;

public class MultistateSpeciesVisitor extends DepthFirstVoidVisitor
{
	
	 private String speciesName;
 	 private HashMap<String,Vector<String>> site_states;
	 private HashMap<String, MutablePair<Integer, Integer>> pureRange_sites;
//	 private HashMap<String, MutablePair<String, String>> pureRangeString_sites;
	 private HashSet<String> circular_sites;
    Vector<Exception> exceptions = new Vector<>();
	public Vector<Exception> getExceptions() { return exceptions; }
	   
	 private String current_site = null;
	 private Vector<String> current_states = null;
	 private Integer current_high;
	 private Integer current_low;
	 private String current_highString;
	 private String current_lowString;
	
	 public String getSpeciesName() { return speciesName; }
	 public Vector<String> getSite_states(String site) {	return site_states.get(site);	}
	 public Set<String> getAllSites_names() {	return site_states.keySet();	}
	 public MutablePair<Integer, Integer> getPureRangeLimits(String site) { return pureRange_sites.get(site);	}
	 public MutablePair<String, String> getStringRangeLimits() { return new MutablePair<String, String>(current_lowString, current_highString) ;	}
		
	 private MultistateSpecies reactant = null;
	private String expandedForm = null;
	 private HashMap<String,String> current_site_nextState;
	MultiModel multiModel = null;
	private boolean enforceRangesNumeric;
	
	Vector <Species> singleStateMultiStateSpecies = new Vector<Species>(); //for the expansion
	
	public String getProductExpansion() {			return expandedForm;	}
	
	 public MultistateSpeciesVisitor(MultiModel mm) { multiModel = mm;	}
	 
	public MultistateSpeciesVisitor(MultiModel mm, Species multistate_reactant, Vector <Species> singleStateMultiStateSpecies) { //constructor used in the expansion
		 try {
			 multiModel = mm;	
			reactant = new MultistateSpecies(multiModel,multistate_reactant.getDisplayedName());
			this.singleStateMultiStateSpecies.addAll(singleStateMultiStateSpecies);
			} catch (Exception e) {
				e.printStackTrace();
		}
	}
		
	
	public MultistateSpeciesVisitor(MultiModel mm,	boolean isReactantReactionWithPossibleRanges) {
		 multiModel = mm;	
		 enforceRangesNumeric = !isReactantReactionWithPossibleRanges;
	}

	boolean isRealMultiStateSpecies = false;
	
	public boolean isRealMultiStateSpecies() {
		return isRealMultiStateSpecies;
	}
	@Override
	public void visit(CompleteMultistateSpecies_Operator n) {
		isRealMultiStateSpecies = n.multistateSpecies_Operator.nodeOptional.present();
		speciesName = ToStringVisitor.toString(n.multistateSpecies_Operator.multistateSpecies_Name.nodeChoice.choice);
		if(reactant!= null) {
			current_site_nextState = new HashMap<String,String>();
			super.visit(n);
			
			if(speciesName.compareTo(reactant.getSpeciesName())==0) {
				expandedForm  = buildExpandedForm();
				if(expandedForm == null) {
						exceptions.add(new Exception("SOMEEEEEEEEEEEETHING WRONG"));
				}
			} else {
				boolean okExpanded = false;
				for(int i = 0; i < singleStateMultiStateSpecies.size(); i++) {
					Species sp = singleStateMultiStateSpecies.get(i);
					if(CellParsers.isMultistateSpeciesName(sp.getDisplayedName())) {
						MultistateSpecies msp = null;
						try {
							msp = new MultistateSpecies(multiModel, sp.getDisplayedName());
						if(speciesName.compareTo(msp.getSpeciesName())==0) {
							//THE PROBLEM IS HERE... FIGURE IT OUT HOW TO MERGE THE CURRENT SPECIFICATION WITH THE ONE IN THE REACTANTS
							MultistateSpecies current = new MultistateSpecies(multiModel, ToStringVisitor.toString(n));
							current.mergeStatesWith_Minimum(msp);
							expandedForm = current.getDisplayedName();
							okExpanded = true;
							break;
						}
						} catch (Exception e) {
							if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES)	e.printStackTrace();
						}
						
					}
				}
				if(!okExpanded) expandedForm = speciesName;
			}
		}
	}
	
	@Override
	public void visit(CompleteMultistateSpecies n) {
		isRealMultiStateSpecies = n.multistateSpecies.nodeOptional.present();
		speciesName = ToStringVisitor.toString(n.multistateSpecies.multistateSpecies_Name.nodeChoice.choice);
		super.visit(n);
	}
	 
	@Override
	 public void visit(MultistateSpecies_Operator_SingleSite n) {
		 if(n.nodeChoice.which==0) {
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
	 
	 @SuppressWarnings("unchecked")
	private String buildExpandedForm() {
		 if(speciesName.compareTo(reactant.getSpeciesName())!= 0) {
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
						nextState = multi.getSucc(site,state);//,false);
					} else if(val.compareTo(MR_MultistateSpecies_ParserConstantsNOQUOTES.getTokenImage(MR_MultistateSpecies_ParserConstants.PREC))==0) {
						nextState = multi.getPrec(site,state);//,false);
					}/* else if(val.compareTo(MR_MultistateSpecies_ParserConstantsNOQUOTES.getTokenImage(MR_MultistateSpecies_ParserConstants.CIRC_R_SHIFT))==0) {
						nextState = multi.getSucc(site,state,true);
					} else if(val.compareTo(MR_MultistateSpecies_ParserConstantsNOQUOTES.getTokenImage(MR_MultistateSpecies_ParserConstants.CIRC_L_SHIFT))==0) {
						nextState = multi.getPrec(site,state,true);
					}*/ else {
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
	public void visit(msmb.parsers.multistateSpecies.syntaxtree.MultistateSpecies n) {
		 speciesName = ToStringVisitor.toString(n.multistateSpecies_Name.nodeChoice.choice);
		 site_states = new HashMap<String,Vector<String>>();
		 pureRange_sites = new HashMap<String, MutablePair<Integer, Integer>> ();
		 circular_sites = new HashSet<String> ();
		 super.visit(n);
		 if(enforceRangesNumeric) fill_pureRange_sites();
	}

	

	  @Override
	  public void visit(MultistateSpecies_SingleStateDefinition n) {
		  super.visit(n);
		  //System.out.println("current_site: "+current_site);
		  //System.out.println("current_states: "+current_states);
		  site_states.put(current_site, current_states);
		  if(n.nodeOptional.present()) {
			  circular_sites.add(current_site);
		  }
		  current_site = null;
		  current_states = null;
		  current_lowString = null;
		  current_highString = null;
		  
	  }
	  
	  
	  @Override
	  public void visit(MultistateSpecies_SiteName n) {
		  current_site  = new String(ToStringVisitor.toString(n.nodeChoice.choice));
		  super.visit(n);
		
	  }
	
	   public void visit(MultistateSpecies_SiteSingleElement n) {
		  if(current_states == null) current_states = new Vector<String>();
		  
		  if(n.nodeOptional.present() && current_lowString == null) {
			  current_lowString = ToStringVisitor.toString(n.nodeChoice.choice);
			  current_highString = ToStringVisitor.toString(((NodeSequence)(n.nodeOptional.node)).nodes.get(3));
			  current_states.add(current_lowString+":"+current_highString);
		  } else {
			  current_states.add(ToStringVisitor.toString(n.nodeChoice.choice));
		  }	  
		  
		  super.visit(n);
	  }
	   
	@Override
	public void visit(CompleteMultistateSpecies_RangeString n) {
		  try{
			  current_lowString = null;
		current_highString = null;
		
		if(n.multistateSpecies_SiteSingleElement.nodeOptional.present()) {
		  current_lowString = ToStringVisitor.toString(n.multistateSpecies_SiteSingleElement.nodeChoice);
		  current_highString = ToStringVisitor.toString(((NodeSequence)(n.multistateSpecies_SiteSingleElement.nodeOptional.node)).nodes.get(3));
		} else { 
			throw new ParseException(); // no range 
		}
		
	} catch(Exception ex) {
		  exceptions.add(ex);
	   }
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
					  if(states.contains(MR_MultistateSpecies_ParserConstantsNOQUOTES.getTokenImage(MR_MultistateSpecies_ParserConstantsNOQUOTES.RANGE_SEPARATOR))) {
						  
					  
					  try {  InputStream is = new ByteArrayInputStream(states.getBytes("UTF-8"));
					  		MR_MultistateSpecies_Parser react = new MR_MultistateSpecies_Parser(is);
					  		CompleteMultistateSpecies_Range range = react.CompleteMultistateSpecies_Range();
					  		range.accept(this);
					  		pureRange_sites.put(site, new MutablePair<Integer,Integer>(current_low,current_high));
					  } catch (Exception e) {
						  e.printStackTrace();
						  exceptions.add(new ParseException("Range for site "+site+" used with non-integer values ("+states+")"));
					}
				  	}
				  }
				}
			  
		  }
	  
	
	  
	public HashSet<String> getCircularSites() {
		//System.out.println("getCircularSites: " +circular_sites);
		return circular_sites;
	}
	
			  
	}
