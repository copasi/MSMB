package parsers.mathExpression.visitor;

import parsers.mathExpression.MR_Expression_Parser;
import parsers.mathExpression.MR_Expression_ParserConstants;
import parsers.mathExpression.MR_Expression_ParserConstantsNOQUOTES;
import parsers.mathExpression.MR_Expression_Parser_ReducedParserException;
import parsers.mathExpression.syntaxtree.*;
import parsers.multistateSpecies.MR_MultistateSpecies_Parser;
import parsers.multistateSpecies.syntaxtree.CompleteMultistateSpecies_Operator;
import parsers.multistateSpecies.visitor.MultistateSpeciesVisitor;
import utility.Constants;

import gui.MainGui;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.Vector;

import model.Function;
import model.MultiModel;
import model.MultistateSpecies;
import model.Species;

import org.COPASI.CCompartment;
import org.COPASI.CCopasiObjectName;
import org.COPASI.CMetab;
import org.COPASI.CModel;
import org.COPASI.CModelValue;
import org.nfunk.jep.ParseException;

import debugTab.DebugConstants;
import debugTab.DebugMessage;
import debugTab.SimilarityStrings;


public class CopasiVisitor extends DepthFirstVoidVisitor {

	private PrintWriter out;
	  private CModel model = null;
	  private boolean conc = false;
	  private boolean isInitialExpression = false;
	  Vector<Exception> exceptions = new Vector<>();
		 public Vector<Exception> getExceptions() { return exceptions; }
		
	  private String copasiExpression = new String();
	  public String getCopasiExpression() {return copasiExpression;}
	  MultiModel multiModel = null;
	  
	public CopasiVisitor(CModel model, MultiModel mm, boolean conc, boolean isInitialExpression2)  { 
		  this.model = model;
		  this.conc = conc;
		  this.isInitialExpression = isInitialExpression2;
		  multiModel = mm;
	}

	@Override
	public void visit(NodeToken n) {
		copasiExpression+=n.tokenImage;
		super.visit(n);
	}
	
	@Override
	public void visit(MultistateSum n) {
		if(isSumMultistate(ToStringVisitor.toString(n))) {
			printSumMultistate(ToStringVisitor.toString(n));
		}
	}
	
	boolean nodeIsAFunctionCall = false;
	@Override
	public void visit(SpeciesReferenceOrFunctionCall_prefix n) {
		String name = ToStringVisitor.toString(n.name.nodeChoice.choice);
		String fun = new String();
		if(n.nodeOptional.present())  {
			NodeOptional nodeOptional = (NodeOptional) ((NodeSequence) n.nodeOptional.node).nodes.get(1);
			if(nodeOptional.node==null){
				//System.out.println("FUNCTION CALL ("0");
				nodeIsAFunctionCall = true;
			}
			else {
				if(!isMultistateSitesList(nodeOptional.node)) {
					//System.out.println("FUNCTION CALL ("+getNumberArguments((ArgumentList)nodeOptional.node)+"): " +name);
					nodeIsAFunctionCall = true;
				} else {
					//System.out.println("SPECIES: "+ToStringVisitor.toString(n)); // to print complete "multistate" definition
					nodeIsAFunctionCall = false;
					
				
				}
			}
		} else {
			//System.out.println("SPECIES: "+ToStringVisitor.toString(n));
			nodeIsAFunctionCall = false;
		}
		
	}
	  
	@Override
	public void visit(SpeciesReferenceOrFunctionCall n) {
		try {
			super.visit(n);
			String element = ToStringVisitor.toString(n);
			if(nodeIsAFunctionCall) {
				
					String funName  = new String();

					try {
						InputStream is = new ByteArrayInputStream(element.getBytes("UTF-8"));
						MR_Expression_Parser parser = new MR_Expression_Parser(is);
						CompleteExpression root;
						root = parser.CompleteExpression();
						GetFunctionNameVisitor name = new GetFunctionNameVisitor();
						root.accept(name);
						funName  = name.getFunctionName();
						if(funName.length()==0) return;
						Function f = multiModel.getFunctionByName(funName);
						copasiExpression += funName+"(";
					} catch(Exception ex) {
						if(funName.compareTo(MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.FLOOR))==0 ||
								funName.compareTo(MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.SQRT))==0 ||
								funName.compareTo(MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXP))==0 ||
								funName.compareTo(MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.LOG))==0) {
							copasiExpression += funName+"(";
						}
						else {
							if(!isMultistateSpeciesDefined(ToStringVisitor.toString(n.speciesReferenceOrFunctionCall_prefix))) {
								throw ex;
							}

						}
					}


					InputStream is2 = new ByteArrayInputStream(element.getBytes("UTF-8"));
					MR_Expression_Parser parser2 = new MR_Expression_Parser(is2);
					CompleteExpression root2 = parser2.CompleteExpression();
					GetFunctionParametersVisitor v = new GetFunctionParametersVisitor();
					root2.accept(v);
					Vector<String> parametersActuals = v.getActualParameters();

					for(int i = 0; i < parametersActuals.size(); i++) {
						InputStream isR = new ByteArrayInputStream(parametersActuals.get(i).getBytes("UTF-8"));
						MR_Expression_Parser parserR = new MR_Expression_Parser(isR);
						CompleteExpression rootR = parserR.CompleteExpression();
						CopasiVisitor vis = new CopasiVisitor(model,multiModel,conc,isInitialExpression);
						rootR.accept(vis);
						if(vis.getExceptions().size() == 0) {
							String copasiExpr  = vis.getCopasiExpression();
							copasiExpression += copasiExpr+",";
						} else {
							this.exceptions.addAll(vis.exceptions);
						}
					}
					copasiExpression = copasiExpression.substring(0, copasiExpression.length()-1);
					copasiExpression += ")";
				
			} else {
				generateCopasiElement(element);
			}
		} catch (Exception e) {
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES)
				e.printStackTrace();
		}
	}
	
	
	
	private void printSumMultistate(String element) {
		
		 try {
			  InputStream is = new ByteArrayInputStream(element.getBytes("UTF-8"));
			  MR_Expression_Parser_ReducedParserException parser = new MR_Expression_Parser_ReducedParserException(is);
			  CompleteExpression root = parser.CompleteExpression();
			  Look4UndefinedMisusedVisitor multistateSum = new Look4UndefinedMisusedVisitor(multiModel);
			  root.accept(multistateSum);
			  Vector<SumExpansion> sum = multistateSum.getSumExpansions();
			 for(int i = 0; i < sum.size(); i++) {
				  copasiExpression += "(";
				 SumExpansion el = sum.get(i);
				  Vector<Species> species = el.getSpeciesSum();
				  for(int j =0; j < species.size()-1; j++){
					  generateCopasiElement(species.get(j).getDisplayedName());
					  copasiExpression += " + ";
				}
				  generateCopasiElement(species.get(species.size()-1).getDisplayedName());
				  copasiExpression += ")";
			  }
			
			  
		  }catch (Exception e) {
			 return ;
		}
		return ;
	}

	private boolean isSumMultistate(String element) {
		 try {
			  InputStream is = new ByteArrayInputStream(element.getBytes("UTF-8"));
			  MR_Expression_Parser_ReducedParserException parser = new MR_Expression_Parser_ReducedParserException(is);
			  CompleteExpression root = parser.CompleteExpression();
			  Look4UndefinedMisusedVisitor multistateSum = new Look4UndefinedMisusedVisitor(multiModel);
			  root.accept(multistateSum);
			  //Vector<SumExpansion> sum = multistateSum.getSumExpansions();
			  return true;
		  }catch (Exception e) {
			 return false;
		}
	}

	private boolean isMultistateSpeciesDefined(String element) {
		 InputStream is = new ByteArrayInputStream(element.getBytes());
		 MR_MultistateSpecies_Parser react = new MR_MultistateSpecies_Parser(is);
		 try {
			CompleteMultistateSpecies_Operator start = react.CompleteMultistateSpecies_Operator();
			MultistateSpeciesVisitor v = new MultistateSpeciesVisitor(multiModel);
	
			start.accept(v);
			MultistateSpecies sp = (MultistateSpecies) multiModel.getSpecies(v.getSpeciesName());
		
			if(sp.containsSpecificConfiguration(element)) return true;
			else {
				exceptions.add(new ParseException("Model yet not complete. Element "+element+" not found"));
				return false;
			}
		
		 } catch (Exception e) {
			//	e.printStackTrace();
				return false;
		}
	}
	
	public void generateCopasiElement(String element) {
		//String element = ToStringVisitor.toString(n);
		String element_copasiTerm = new String();
		if(element.compareTo(Constants.TIME_STRING) ==0) {
			element_copasiTerm = model.getObject(new CCopasiObjectName("Reference=Time")).getCN().getString();
		} 
		else  if(!isMultistateSpeciesDefined(element)) {
			
			int index = -1;
			GetElementWithExtensions name = null;
			try{
				InputStream is = new ByteArrayInputStream(element.getBytes("UTF-8"));
				MR_Expression_Parser parser = new MR_Expression_Parser(is);
				CompleteExpression root = parser.CompleteExpression();
				name = new GetElementWithExtensions();
				root.accept(name);
			}catch (Exception e) {
				if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
				return;
				
			}
				String element_substring = name.getElementName();
				Vector<String> extensions = name.getExtensions();
				String element_kind_quantifier = getKindQuantifier(extensions);
				String element_timing_quantifier = getTimingQuantifier(extensions);
				String element_quantity_quantifier = getQuantityQuantifier(extensions);
			
			//String element_substring = new String(element);
			//String element_timing_quantifier = null;//endWithTimingQuantifier(element);
			//String element_quantity_quantifier = null;//endWithQuantityQuantifier(element);
		/*	if(element_quantity_quantifier!= null) {
				element_substring = element.substring(0, element.length()-2);
				element_timing_quantifier = endWithTimingQuantifier(element_substring);
				if(element_timing_quantifier!=null) {
					element_substring = element_substring.substring(0, element_substring.length()-2);
				}
				System.out.println(element_substring);
			} else {
				if(element_timing_quantifier!=null) {
					element_substring = element.substring(0, element.length()-2);
					element_quantity_quantifier = endWithQuantityQuantifier(element_substring);
					if(element_quantity_quantifier!= null) {
						element_substring = element_substring.substring(0, element_substring.length()-2);
					}
					System.out.println(element_substring);
				}
			}*/
			
			try {
				if(element_kind_quantifier == null || element_kind_quantifier.compareTo("."+MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_SPECIES))==0) {
					index = this.findMetabolite(element_substring,false);
				}
			} catch (Exception e) {
				if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
				index = -1;
			}
			if(index!= -1) { //species
				CMetab metab = model.getMetabolite(index);
				if(element_quantity_quantifier == null && element_timing_quantifier == null) {
					if(!conc) element_copasiTerm = metab.getObject(new CCopasiObjectName("Reference=ParticleNumber")).getCN().getString();
					else element_copasiTerm = metab.getObject(new CCopasiObjectName("Reference=Concentration")).getCN().getString();
				} else {
					if(element_timing_quantifier == null) {
						if (element_quantity_quantifier.compareTo(".p")==0) element_copasiTerm = metab.getObject(new CCopasiObjectName("Reference=ParticleNumber")).getCN().getString();
						else if(element_quantity_quantifier.compareTo(".c")==0) element_copasiTerm = metab.getObject(new CCopasiObjectName("Reference=Concentration")).getCN().getString();
					}else if(element_quantity_quantifier == null) {
						if(element_timing_quantifier.compareTo(".i")==0 && conc) element_copasiTerm = metab.getObject(new CCopasiObjectName("Reference=InitialConcentration")).getCN().getString();
						else if(element_timing_quantifier.compareTo(".t")==0 && conc) element_copasiTerm = metab.getObject(new CCopasiObjectName("Reference=Concentration")).getCN().getString();
						else if(element_timing_quantifier.compareTo(".r")==0 && conc) element_copasiTerm = metab.getObject(new CCopasiObjectName("Reference=Rate")).getCN().getString();
						else if(element_timing_quantifier.compareTo(".i")==0 && !conc) element_copasiTerm = metab.getObject(new CCopasiObjectName("Reference=InitialParticleNumber")).getCN().getString();
						else if(element_timing_quantifier.compareTo(".t")==0 && !conc) element_copasiTerm = metab.getObject(new CCopasiObjectName("Reference=ParticleNumber")).getCN().getString();
						else if(element_timing_quantifier.compareTo(".r")==0 && !conc) element_copasiTerm = metab.getObject(new CCopasiObjectName("Reference=ParticleNumberRate")).getCN().getString();
					}
					else if(element_timing_quantifier.compareTo(".i")==0 && element_quantity_quantifier.compareTo(".c")==0) element_copasiTerm = metab.getObject(new CCopasiObjectName("Reference=InitialConcentration")).getCN().getString();
					else if(element_timing_quantifier.compareTo(".i")==0 && element_quantity_quantifier.compareTo(".p")==0) element_copasiTerm = metab.getObject(new CCopasiObjectName("Reference=InitialParticleNumber")).getCN().getString();
					else if(element_timing_quantifier.compareTo(".t")==0 && element_quantity_quantifier.compareTo(".p")==0) element_copasiTerm = metab.getObject(new CCopasiObjectName("Reference=ParticleNumber")).getCN().getString();
					else if(element_timing_quantifier.compareTo(".t")==0 && element_quantity_quantifier.compareTo(".c")==0) element_copasiTerm = metab.getObject(new CCopasiObjectName("Reference=Concentration")).getCN().getString();
					else if(element_timing_quantifier.compareTo(".r")==0 && element_quantity_quantifier.compareTo(".p")==0) element_copasiTerm = metab.getObject(new CCopasiObjectName("Reference=ParticleNumberRate")).getCN().getString();
					else if(element_timing_quantifier.compareTo(".r")==0 && element_quantity_quantifier.compareTo(".c")==0) element_copasiTerm = metab.getObject(new CCopasiObjectName("Reference=Rate")).getCN().getString();
					
				}
			} else {
				//MAYBE ALSO THE REST CAN HAVE A DISTINCTION BETWEEN TRANSIENT .t AND .i INITIAL (CHEEEEEEEEEEEEECK)
				
				//for now just species are allowed
				if(element_kind_quantifier == null || element_kind_quantifier.compareTo("."+MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_GLOBALQ))==0) {
						index = this.findGlobalQ(element,false);
				}
				if(index!= -1) { //parameter
					CModelValue m = model.getModelValue(index);
					if(!isInitialExpression) {
						element_copasiTerm = m.getObject(new CCopasiObjectName("Reference=Value")).getCN().getString();
					} else {
						element_copasiTerm = m.getObject(new CCopasiObjectName("Reference=InitialValue")).getCN().getString();
					}
				} else { //compartment?
					if(element_kind_quantifier == null || element_kind_quantifier.compareTo("."+MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_COMPARTMENT))==0) {
						index = this.findCompartment(element,false);
					}
					if(index != -1) {
						CCompartment comp = model.getCompartment(index);
						if(!isInitialExpression) {
							element_copasiTerm = comp.getObject(new CCopasiObjectName("Reference=Volume")).getCN().getString();
						} else {
							element_copasiTerm = comp.getObject(new CCopasiObjectName("Reference=InitialVolume")).getCN().getString();
										
						}
					} 
				}
			}
		} else {
			try {
				InputStream is = new ByteArrayInputStream(element.getBytes());
				MR_MultistateSpecies_Parser react = new MR_MultistateSpecies_Parser(is);
				CompleteMultistateSpecies_Operator start;
				start = react.CompleteMultistateSpecies_Operator();
				MultistateSpeciesVisitor v = new MultistateSpeciesVisitor(multiModel);

				start.accept(v);
				MultistateSpecies sp = (MultistateSpecies) multiModel.getSpecies(v.getSpeciesName());
				
				
				int index = this.findMetabolite(element,false);
				
				//TOOOOO DOOOOOO
				
				String element_kind_quantifier = null; //getKindQuantifier(extensions);
				String element_timing_quantifier = null;//getTimingQuantifier(extensions);
				String element_quantity_quantifier = null; //getQuantityQuantifier(extensions);
			
				if(index!= -1) { //species
					CMetab metab = model.getMetabolite(index);
					if(element_quantity_quantifier == null && element_timing_quantifier == null) {
						if(!conc) element_copasiTerm = metab.getObject(new CCopasiObjectName("Reference=ParticleNumber")).getCN().getString();
						else element_copasiTerm = metab.getObject(new CCopasiObjectName("Reference=Concentration")).getCN().getString();
					} else {
						if(element_timing_quantifier == null) {
							if (element_quantity_quantifier.compareTo(".p")==0) element_copasiTerm = metab.getObject(new CCopasiObjectName("Reference=ParticleNumber")).getCN().getString();
							else if(element_quantity_quantifier.compareTo(".c")==0) element_copasiTerm = metab.getObject(new CCopasiObjectName("Reference=Concentration")).getCN().getString();
						}else if(element_quantity_quantifier == null) {
							if(element_timing_quantifier.compareTo(".i")==0 && conc) element_copasiTerm = metab.getObject(new CCopasiObjectName("Reference=InitialConcentration")).getCN().getString();
							else if(element_timing_quantifier.compareTo(".t")==0 && conc) element_copasiTerm = metab.getObject(new CCopasiObjectName("Reference=Concentration")).getCN().getString();
							else if(element_timing_quantifier.compareTo(".r")==0 && conc) element_copasiTerm = metab.getObject(new CCopasiObjectName("Reference=Rate")).getCN().getString();
							else if(element_timing_quantifier.compareTo(".i")==0 && !conc) element_copasiTerm = metab.getObject(new CCopasiObjectName("Reference=InitialParticleNumber")).getCN().getString();
							else if(element_timing_quantifier.compareTo(".t")==0 && !conc) element_copasiTerm = metab.getObject(new CCopasiObjectName("Reference=ParticleNumber")).getCN().getString();
							else if(element_timing_quantifier.compareTo(".r")==0 && !conc) element_copasiTerm = metab.getObject(new CCopasiObjectName("Reference=ParticleNumberRate")).getCN().getString();
						}
						else if(element_timing_quantifier.compareTo(".i")==0 && element_quantity_quantifier.compareTo(".c")==0) element_copasiTerm = metab.getObject(new CCopasiObjectName("Reference=InitialConcentration")).getCN().getString();
						else if(element_timing_quantifier.compareTo(".i")==0 && element_quantity_quantifier.compareTo(".p")==0) element_copasiTerm = metab.getObject(new CCopasiObjectName("Reference=InitialParticleNumber")).getCN().getString();
						else if(element_timing_quantifier.compareTo(".t")==0 && element_quantity_quantifier.compareTo(".p")==0) element_copasiTerm = metab.getObject(new CCopasiObjectName("Reference=ParticleNumber")).getCN().getString();
						else if(element_timing_quantifier.compareTo(".t")==0 && element_quantity_quantifier.compareTo(".c")==0) element_copasiTerm = metab.getObject(new CCopasiObjectName("Reference=Concentration")).getCN().getString();
						else if(element_timing_quantifier.compareTo(".r")==0 && element_quantity_quantifier.compareTo(".p")==0) element_copasiTerm = metab.getObject(new CCopasiObjectName("Reference=ParticleNumberRate")).getCN().getString();
						else if(element_timing_quantifier.compareTo(".r")==0 && element_quantity_quantifier.compareTo(".c")==0) element_copasiTerm = metab.getObject(new CCopasiObjectName("Reference=Rate")).getCN().getString();
						
					}
				} else {
					throw new Exception("problem exporting multistate species");
				}
				
				
			} catch (Exception e) {

				e.printStackTrace();
				exceptions.add(e);
			}
		
			
		}
		
		
		
		if(element_copasiTerm.length() == 0) exceptions.add(new ParseException("Model yet not complete. Element "+element+" not found"));
		copasiExpression+="<"+element_copasiTerm+">";
	}

	private String getKindQuantifier(Vector<String> extensions) {
		for(int i = 0; i < extensions.size(); i++) {
			String ext = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_COMPARTMENT);
			if(extensions.get(i).compareTo(ext)==0) {	return "."+ext;	} 
			ext = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_SPECIES);
			if(extensions.get(i).compareTo(ext)==0) {	return "."+ext;	} 
			ext = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_GLOBALQ);
			if(extensions.get(i).compareTo(ext)==0) {	return "."+ext;	} 
		}
		return null;
	}

	private String getQuantityQuantifier(Vector<String> extensions) {
		for(int i = 0; i < extensions.size(); i++) {
				String ext = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_PARTICLE);
				if(extensions.get(i).compareTo(ext)==0) {	return "."+ext;	} 
				ext = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_CONC);
				if(extensions.get(i).compareTo(ext)==0) {	return "."+ext;	} 
			}
			return null;
	}

	private String getTimingQuantifier(Vector<String> extensions) {
		for(int i = 0; i < extensions.size(); i++) {
			String ext = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_INIT);
			if(extensions.get(i).compareTo(ext)==0) {	return "."+ext;	} 
			ext = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_RATE);
			if(extensions.get(i).compareTo(ext)==0) {	return "."+ext;	} 
			ext = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_TRANS);
			if(extensions.get(i).compareTo(ext)==0) {	return "."+ext;	} 
		}
		return null;
	}

	private int findCompartment(String name, boolean key) {
		if(name.startsWith("\"")) {	name = name.substring(1);	}
		if(name.endsWith("\"")) { name = name.substring(0,name.length()-1); }
		int i, iMax =(int) model.getCompartments().size();
        for (i = 0;i < iMax;++i)
        {
            CCompartment comp = model.getCompartment(i);
            assert comp != null;
            String current = new String();
            if(!key) current = comp.getObjectName();
            else current = comp.getKey();
            if(name.compareTo(current) == 0) return i;
        }
        
        return -1;
	}
	
	private int findGlobalQ(String name, boolean key) {
		if(name.startsWith("\"")) {	name = name.substring(1);	}
		if(name.endsWith("\"")) { name = name.substring(0,name.length()-1); }
		
		int i, iMax =(int) model.getModelValues().size();
        for (i = 0;i < iMax;++i)
        {
            CModelValue m = model.getModelValue(i);
            String current = new String();
            if(!key) current = m.getObjectName();
            else current = m.getKey();
            if(name.compareTo(current) == 0) return i;
        }
        
        return -1;
	}
	
	
	private int findMetabolite(String name, boolean key) throws Exception {
			if(name.startsWith("\"")) {	name = name.substring(1);	}
			if(name.endsWith("\"")) { name = name.substring(0,name.length()-1); }
			
			if(name.contains("(")) {
				MultistateSpecies ms = new MultistateSpecies(multiModel,name);
				name = ms.printCompleteDefinition(); 
				//because in "name" the order of the sites can be different from the order used for defining the metabolite species.
				//Building the multistateSpecies and printing again its complete definition will make the two definitions identical w.r.t. the order
			}
			
			int i, iMax =(int) model.getMetabolites().size();
			
	        for (i = 0;i < iMax;++i)
	        {
	            CMetab metab = model.getMetabolite(i);
	            assert metab != null;
	            if(!key) {
		            String current = metab.getObjectName();
		            if(name.compareTo(current) == 0) return i;
	            } else {
	            	String current = metab.getKey();
	            	if(name.compareTo(current) == 0) return i;
	            }
	        }
	        
	        return -1;
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
	
	/*public MultistateSpecies extract_object_of_SUM(String element) throws Exception {
		*String weightFunctionString = extract_weightFunction_in_SUM(element);
		if(weightFunctionString.length() > 0) {
			element = element.substring(0,element.length()- weightFunctionString.length()-2);
		} *
		
		StringTokenizer sum_st = new StringTokenizer(element,"(,)");
		sum_st.nextToken(); //SUM
		String multistate_species_name = sum_st.nextToken();
		//controllare che esista
		String site = new String();
		HashMap<String, Vector<Integer>> sitesSum = new HashMap<String, Vector<Integer>>();
		try {
			while(sum_st.hasMoreTokens()) {
				site = sum_st.nextToken();
				Vector<Integer> limits = new Vector<Integer>();
				try{
					String lower_bound = sum_st.nextToken();
					String upper_bound = sum_st.nextToken();
					limits.add(new Double(Double.parseDouble(lower_bound)).intValue());
					limits.add(new Double(Double.parseDouble(upper_bound)).intValue());
				} catch (NoSuchElementException ex){ //there are no lower-upper bounds --> all the site states
					if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) ex.printStackTrace();
					MultistateSpecies ms = (MultistateSpecies) MultiModel.speciesDB.getSpecies(multistate_species_name);
					Vector states = ms.getSiteStates_complete(site);
					String lower_bound = (String) states.get(0);
					String upper_bound = (String)states.get(states.size()-1);
					limits.add(new Double(Double.parseDouble(lower_bound)).intValue());
					limits.add(new Double(Double.parseDouble(upper_bound)).intValue());
				}
				sitesSum.put(site, limits);
			}
		} catch(NumberFormatException numberEx) { 
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) numberEx.printStackTrace();
			throw new NumberFormatException("Only numerical states can be used as indexes in SUM");
		}
		
		//DEFINISCO NUOVA MULTISTATE CON SOLO I RANGE INDICATI SOPRA E STAMPO EXPANDED NELLA SOMMA
		MultistateSpecies ms = (MultistateSpecies) MultiModel.speciesDB.getSpecies(multistate_species_name);
		String complete_string = new String();
		Iterator all_sites = ms.getSitesNames().iterator();
		
		while(all_sites.hasNext()) {
			String name = (String) all_sites.next();
			complete_string += name+"{";
			if(sitesSum.containsKey(name)) {
				//CHEEEEEEEEEEEEEEEEECK IF LOWER E UPPER SONO COERENTI CON LA DEFINIZIONE DEL SITO
				//E CHE IL SITO SIA DEFINITO CON UN RANGE!!! ALTRIMENTI COMPLETE_STRING ORA E' SBAGLIATA
				int lower = sitesSum.get(name).get(0);
				int upper = sitesSum.get(name).get(1);
				for(int i = lower; i < upper; i++) {
					complete_string += i+",";
				}
				complete_string += upper+"}";
			} else {
				Iterator it = ms.getSiteStates_complete(name).iterator();
				while(it.hasNext()) {
					complete_string += it.next()+",";
				}
				complete_string = complete_string.substring(0,complete_string.length()-1);
			}
			complete_string += ";";
		}
		
		complete_string = complete_string.substring(0,complete_string.length()-1);
		
		complete_string = multistate_species_name+"("+ complete_string + ")";
		
		MultistateSpecies reduced = new MultistateSpecies(complete_string);
		return reduced;
	}*/
}
