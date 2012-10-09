package model;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.*;

import org.COPASI.*;
import org.apache.commons.lang3.tuple.MutablePair;
import org.nfunk.jep.ParseException;
import parsers.chemicalReaction.MR_ChemicalReaction_Parser;
import parsers.chemicalReaction.syntaxtree.CompleteSpeciesWithCoefficient;
import parsers.chemicalReaction.visitor.ExtractSubProdModVisitor;
import parsers.mathExpression.MR_Expression_Parser;
import parsers.mathExpression.MR_Expression_ParserConstants;
import parsers.mathExpression.MR_Expression_ParserConstantsNOQUOTES;
import parsers.mathExpression.syntaxtree.CompleteExpression;
import parsers.mathExpression.syntaxtree.SingleFunctionCall;
import parsers.mathExpression.visitor.CopasiVisitor;
import parsers.mathExpression.visitor.ExpressionVisitor;
import parsers.mathExpression.visitor.GetFunctionNameVisitor;
import parsers.mathExpression.visitor.GetUsedVariablesInEquation;
import parsers.multistateSpecies.MR_MultistateSpecies_Parser;
import parsers.multistateSpecies.MR_MultistateSpecies_ParserConstantsNOQUOTES;
import parsers.multistateSpecies.syntaxtree.CompleteMultistateSpecies;
import parsers.multistateSpecies.syntaxtree.CompleteMultistateSpecies_Operator;
import parsers.multistateSpecies.visitor.MultistateSpeciesVisitor;

import debugTab.*;
import gui.*;
import utility.*;

public class MultiModel {
	
	private HashMap<String, Vector<Integer>> allNamedElements = new HashMap<String, Vector<Integer>>(); // name + list of indices of tables where the name is already defined
	public Vector<Integer> getWhereNameIsUsed(String name) { 
		if(CellParsers.isMultistateSpeciesName(name)) {
			try {
				name = new MultistateSpecies(this,name).getSpeciesName();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Vector ret = allNamedElements.get(name);
		if(ret!= null && ret.size() > 0) return ret;
		return null;
	}
	
	public void addNamedElement(String name, Integer tableIndex) throws Exception { 
		if(name.trim().length()==0) return;
		CellParsers.parser.addVariable(name, 0.0);
	
		Vector<Integer> where = getWhereNameIsUsed(name);
		if(where != null) { 
			
			if(where.contains(tableIndex) && tableIndex != Constants.TitlesTabs.SPECIES.index) throw new Exception(); //already defined in that table
		} else {
			where = new Vector<Integer>();
		}
		where.add(tableIndex);
		allNamedElements.put(name, where);
		}
	
	
	public void removeNamedElement(String name, Integer fromTable) {
			if(name.trim().length()==0) return; 
			Vector<Integer> where = getWhereNameIsUsed(name);
			if(where!=null) {
				where.removeElement(fromTable);
				allNamedElements.put(name, where);
			}
	}
	
	public void removeAllNamedElement(Collection<String> names) {
		Iterator it = names.iterator();
		while(it.hasNext()){		
				String name = (String) it.next();
				allNamedElements.remove(name);
		}
}
	
	public int getNumSpecies() { return speciesDB.getAllNames().size();  }
	public int getNumSpeciesExpanded() throws Exception { return speciesDB.getNumSpeciesExpanded();  }
	public int getNumComp() { return compDB.getAllNames().size(); }
	public int getNumGlobalQ() { return globalqDB.getAllNames().size(); }
	public int getNumEvents() { return eventsDB.getAllEvents().size()-1;} // because the first for row 0 is null 
	//public int getNumReactions() { return reactionsDB.???.size(); }
	//public int getNumReactionsExpanded() { return reactionsDB.???.size(); }
	
	public Vector<Compartment> getAllCompartments() { return compDB.getAllCompartments(); }
	//!!!!!!!!!!!!TO BE DELETED WHEN A REACTIONSDB IS IMPLEMENTED !!!!!!!!!!!!!!!!!!
	CustomTableModel tableReactionmodel;
	public void setTableReactionModel(CustomTableModel t) {	tableReactionmodel = t;	}
	public int getNumReactions(){
		int counter = 0;
		for(int i = 0; i < tableReactionmodel.getRowCount()-1 ; i++ ) {
			String string_reaction = ((String)tableReactionmodel.getValueAt(i, Constants.ReactionsColumns.REACTION.index));
			if(string_reaction.trim().length() <= 0) continue;
			counter++;
		}
		return counter;
	}
	
	public int getNumReactionsExpanded() { return this.counterExpandedReactions; }
	private int counterExpandedReactions;
	//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	
		
	private SpeciesDB speciesDB = new SpeciesDB(this);
	private CompartmentsDB compDB = new CompartmentsDB(this);
	private GlobalQDB globalqDB = new GlobalQDB(this); 
	private EventsDB eventsDB = new EventsDB();
	public FunctionsDB funDB = new FunctionsDB(this);
	
	//ObjectStdVector changedObjects = new ObjectStdVector();
	
	public CCopasiDataModel copasiDataModel;
	private String copasiDataModelSBML_ID = new String("");
	
	//public static long indexCopasiDataModel = -1;
	
	public MultiModel() {}
	
	public void addGlobalQ(int index,String name, String value, String type, String expression, String notes) throws Exception {
		this.globalqDB.addChangeGlobalQ(index,name, value, type, expression, notes);
	}
	
	public String getGlobalQ(String name) {
		if(this.globalqDB.getGlobalQ(name) == null) return null;
		return this.globalqDB.getGlobalQ(name).getInitialValue();
	}
	
	public MultistateSpecies getMultistateSpecies(String name) {
		try {
			MultistateSpecies ret = (MultistateSpecies)this.speciesDB.getSpecies(name);
			return ret;
		} catch(Exception ex) { //problem retrieving the species
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) ex.printStackTrace();
			return null; 
		}	
	}
	
	public boolean isGlobalQAlreadyDefined(String name) {
		return this.globalqDB.contains(name);
	}
	
	public int getSizeSpeciesDB() {
		return this.speciesDB.getSizeSpeciesDB();
	}
	

	
	public String mergeMultistateSpecies(int indexOld, MultistateSpecies newSp) throws Exception {
		int ind = -(speciesDB.addChangeSpecies(-1,new String(),newSp.printCompleteDefinition(),newSp.getInitialQuantity_multi(),newSp.getType(), newSp.getCompartment_listString(), newSp.getExpression(),false, new String(),true,true));
		return ((MultistateSpecies)speciesDB.getSpecies(ind)).printCompleteDefinition();
	}

	//RESETS COMPLETELY THE SPECIES WITH THE NEW NAME/SITES
	public void modifyMultistateSpecies(MultistateSpecies species, boolean fromMultistateBuilder,int row, boolean autoMergeSpecies) throws Exception {
		String name = species.printCompleteDefinition();
		
		int n = this.speciesDB.addChangeSpecies(row+1, new String(), name,species.getInitialQuantity_multi(), species.getType(), species.getCompartment_listString(), species.getExpression(),fromMultistateBuilder, new String(),autoMergeSpecies,true);
	}
	
	public int findCompartment(String name) {
		return this.findCompartment(name, false);
	}
	
	private int findCompartment_key(String name) {
		return this.findCompartment(name, true);
	}
	
	public int findCompartment(String name, boolean key) {
		if(name.startsWith("\"")&&name.endsWith("\"")) {	name = name.substring(1,name.length()-1); }
		CModel model = copasiDataModel.getModel();
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
	
	public int findGlobalQ(String name) {
		return this.findGlobalQ(name, false);
	}
	
	private int findGlobalQ_key(String name) {
		return this.findGlobalQ(name, true);
	}
	
	
	public int findGlobalQ(String name, boolean key) {
		if(name.startsWith("\"")&&name.endsWith("\"")) {	name = name.substring(1,name.length()-1); }
		CModel model = copasiDataModel.getModel();
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
	
	private int findEvent(String name) {
		 
		CModel model = copasiDataModel.getModel();
		int i, iMax =(int) model.getEvents().size();
        for (i = 0;i < iMax;++i)
        {
            if(name.compareTo(model.getEvents().get(i).getObjectName()) == 0) return i;
        }
        
        return -1;
	}
	
	
	private int findReaction(String name) {
		 
		CModel model = copasiDataModel.getModel();
		int i, iMax =(int) model.getReactions().size();
        for (i = 0;i < iMax;++i)
        {
            CReaction m = model.getReaction(i);
            String current = new String();
            current = m.getObjectName();
            if(name.compareTo(current) == 0) return i;
        }
        
        return -1;
	}
	
	private int findLocalParameter_key(String name, CCopasiParameterGroup group) {
		 
		for (int i = 0; i < group.size(); i++)
        {
            CCopasiParameter par = group.getParameter(i);
            String current = par.getKey();
            if(name.compareTo(current) == 0) return i;
        }
        
        return -1;
	}
	
	
	public int findMetabolite(String name, String cmp) throws Exception {
		return findMetabolite(name,cmp, false);
	}
	
	private int findMetabolite_key(String name) throws Exception {
		return findMetabolite(name,null, true);
	}
	
	
	
	
	private int findMetabolite(String name, String cmp, boolean key) throws Exception {
		
		if(CellParsers.isMultistateSpeciesName(name)) {
			MultistateSpecies ms = new MultistateSpecies(this,name);
			name = ms.printCompleteDefinition(); 
			//because in "name" the order of the sites can be different from the order used for defining the metabolite species.
			//Building the multistateSpecies and printing again its complete definition will make the two definitions identical w.r.t. the order
		}
		
		if(name.startsWith("\"")&&name.endsWith("\"")) {	name = name.substring(1,name.length()-1); }
		if(cmp!= null && cmp.startsWith("\"")&&cmp.endsWith("\"")) {	cmp = cmp.substring(1,cmp.length()-1); }
			
		CModel model = copasiDataModel.getModel();
		int i, iMax =(int) model.getMetabolites().size();
		
        for (i = 0;i < iMax;++i)
        {
            CMetab metab = model.getMetabolite(i);
            assert metab != null;
            if(!key) {
	            String current = metab.getObjectName();
	          /*  int level_similarity = 1;
	            if(!current.contains("(") && !name.contains("(") &&
	            	SimilarityStrings.damlev(current, name)!= 0 &&
	                SimilarityStrings.damlev(current, name) <= level_similarity) {
	         		DebugMessage dm = new DebugMessage();
	        	//	dm.setOrigin_cause("Similarity strings <= "+level_similarity);
	        		dm.setOrigin_table(Constants.TitlesTabs.SPECIES.description);
	        		dm.setProblem("Species "+ name + " and " + current + " have a degree of similarity lower than "+level_similarity+"\n. Two species have been added but maybe a misstype has occurred in the definition of the two species.");
	        		dm.setPriority(DebugConstants.PriorityType.SIMILARITY.priorityCode);
	        		dm.setOrigin_col(1);
	        		//MainGui.debugMessages.add(dm);
	        	}*/
	            
	            if(name.compareTo(current) == 0) {
	            	if(cmp!= null) { 
	            		if(metab.getCompartment().getObjectName().compareTo(cmp)==0 ) return i;
	            		else continue;
	            	}
	            	else return i;
	            }
            } else {
            	String current = metab.getKey();
            	
            	if(name.compareTo(current) == 0) return i;
            }
        }
        
        return -1;
	}
	
	private int findMetabolite_sbmlID(String id) {
		
		CModel model = copasiDataModel.getModel();
		int i, iMax =(int) model.getMetabolites().size();
		
        for (i = 0;i < iMax;++i)
        {
            CMetab metab = model.getMetabolite(i);
            String current = metab.getSBMLId();
	 
	        if(id.compareTo(current) == 0) return i;
           
        }
        
        return -1;
	}
	
	

	
	
	public String saveCPS(File file, 
						CustomTableModel tableReactionmodel, ProgressBarFrame progressBarFrame) throws Exception {
		/* Vector warnings = checkConsistency();
		
		 
		 if(warnings.size() > 0) {
			 //FINESTRA CON ELENCO WARNINGS E CHIEDI SE VUOLE ESPORTARE CMQ O VUOLE TORNARE INDIETRO E CORREGGERLI
			//TOOOOOOOOOOOO BEEEEEEEEEEEEE FIXED
		 }*/
		
		 progress(progressBarFrame,Constants.ProgressBar.LOADING_CPS.progress);
			
		 fillCopasiDataModel(tableReactionmodel, progressBarFrame);
		 System.out.println("created"); System.out.flush();
		 			
		 
		 progress(progressBarFrame,Constants.ProgressBar.COMPILING_CPS.progress);
			
		 this.copasiDataModel.getModel().compile();
		 System.out.println("compiled"); System.out.flush();
		 progress(progressBarFrame,Constants.ProgressBar.UPDATING_CPS.progress);
		 //this.copasiDataModel.getModel().updateInitialValues(changedObjects);
		// System.out.println("updated");System.out.flush();
		 progress(progressBarFrame,Constants.ProgressBar.SAVING_CPS.progress);
		 if(file != null) {
			 //Serialize4Debug.writeCopasiStateSummary(copasiDataModel.getModel(), "EVENTS");
			 
			 this.copasiDataModel.saveModel(file.getAbsolutePath(), true);
			 System.out.println("saved");System.out.flush();
			 progress(progressBarFrame,Constants.ProgressBar.COMPILING_CPS.progress);
			 
			 //addAnnotations(file);
			 
			 clearCopasiDataModel();
			 System.out.println("cleared");System.out.flush();
			 progress(progressBarFrame,Constants.ProgressBar.END.progress);
			 return null;
		 } else {
			 progress(progressBarFrame,100);
				
				return this.copasiDataModel.getModel().getSBMLId();
		 }
		
	}
	
	
	public String exportXPP(File file, CustomTableModel tableReactionmodel, ProgressBarFrame progressBarFrame) throws Exception {
				/* Vector warnings = checkConsistency();
		
		
		if(warnings.size() > 0) {
		 //FINESTRA CON ELENCO WARNINGS E CHIEDI SE VUOLE ESPORTARE CMQ O VUOLE TORNARE INDIETRO E CORREGGERLI
		//TOOOOOOOOOOOO BEEEEEEEEEEEEE FIXED
		}*/

		progress(progressBarFrame,Constants.ProgressBar.LOADING_CPS.progress);

		fillCopasiDataModel(tableReactionmodel, progressBarFrame);

		progress(progressBarFrame,Constants.ProgressBar.COMPILING_CPS.progress);

		this.copasiDataModel.getModel().compile();
		System.out.println("compiled");
		System.out.flush();
		progress(progressBarFrame,Constants.ProgressBar.UPDATING_CPS.progress);
		System.out.println("updated");System.out.flush();
		progress(progressBarFrame,Constants.ProgressBar.SAVING_CPS.progress);
		if(file != null) {
			
			System.out.println("exported");System.out.flush();
			String math = this.copasiDataModel.exportMathModelToString("XPPAUT (*.ode)");
			
			BufferedWriter buffout= new BufferedWriter(new FileWriter(file,true));
			PrintWriter out = new PrintWriter(buffout);
			out.println(math);
			
			out.flush();
			out.close();
			progress(progressBarFrame,Constants.ProgressBar.COMPILING_CPS.progress);
			System.out.println("saved");System.out.flush();
			
			//addAnnotations(file);

			clearCopasiDataModel();
			System.out.println("cleared");System.out.flush();
			progress(progressBarFrame,Constants.ProgressBar.END.progress);
			return null;
		} else {
			progress(progressBarFrame,100);

			return this.copasiDataModel.getModel().getSBMLId();
		}

	}
	
	
	private void addAnnotations(File file) {
		try {
			Vector names = this.speciesDB.getAllMultistateSpeciesExpanded();
			String completeFile = new String();
			BufferedReader buffin;
				buffin = new BufferedReader(new FileReader(file.getAbsolutePath()));

			String line = new String();

			while((line = buffin.readLine()) != null) {
				completeFile += line + System.getProperty("line.separator");
				if(line.contains("<species") || line.contains("<Metabolite ")) {

					StringTokenizer st = new StringTokenizer(line," =");
					while(st.hasMoreTokens()) {
						String element = st.nextToken();
						if(element.compareTo("name") == 0) {
							String name = st.nextToken();
							if(names.contains(name.substring(1,name.length()-1))) {
								completeFile += Constants.MULTISTATE_ANNOTATION;
							}
							break;
						}
					}
				} 
			}
			buffin.close();

			BufferedWriter	out = new BufferedWriter(new FileWriter(file.getAbsolutePath()));
			out.write(completeFile);
			out.flush();
			out.close();

		} catch (Exception e) {
			
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
		}
		
	}
	
	
	
	private void progress(ProgressBarFrame progressBarFrame, int i) throws InterruptedException {
		if(progressBarFrame !=null) {
				synchronized (progressBarFrame) {
		 			progressBarFrame.progress(i);
		 			progressBarFrame.notifyAll();
			 	}
		 	}
		 return;
	}
	
	
	private void fillCopasiDataModel(CustomTableModel tableReactionmodel, ProgressBarFrame progressBarFrame) throws Exception {

		//RIEMPIRE PRIMA I COMPARTMENT E POI LE SPECIE ALTRIMENTI NON AGGIUNGE I METABOLITESSSSSSS

		clearCopasiDataModel();
		CModel model = copasiDataModel.getModel(); 
		model.getEvents().cleanup(); // IF I DON'T DO THIS, THE COPASI SAVEMODEL IS NOT WORKING (NO EXCEPTIONS BUT SIMPLY STUCK)
				
		
		if(progressBarFrame!=null) progress(progressBarFrame,Constants.ProgressBar.LOADING_GLOBQ__CPS.progress);
		fillCopasiDataModel_globalQ_fixed();

		if(progressBarFrame!=null) progress(progressBarFrame,Constants.ProgressBar.LOADING_COMP_CPS.progress);
		fillCopasiDataModel_compartments();
		CFunctionDB copasiFunDB = null;
		try {
			if(progressBarFrame!=null) progress(progressBarFrame,Constants.ProgressBar.LOADING_SPECIES_CPS.progress);
			Vector<Species> allSpecies = this.speciesDB.getAllSpecies();
			Vector remainingSpecies = fillCopasiDataModel_species(allSpecies);


			if(progressBarFrame!=null) progress(progressBarFrame,Constants.ProgressBar.LOADING_FUNCTIONS_CPS.progress);
			copasiFunDB = fillCopasiDataModel_functions();
			//System.out.println("after fillCopasiDataModel_functions:" +funDB.loadedFunctions().size());

			Vector<GlobalQ> allGlobalQ= this.globalqDB.getAllGlobalQ();
			Vector remainingGlobalQ = fillCopasiDataModel_globalQ_assignment_ode(allGlobalQ);

			int remainingSp = remainingSpecies.size();
			int remainingGlQ = remainingGlobalQ.size();
			int counter = 10;

			while(remainingSpecies.size()!=0 || remainingGlobalQ.size() != 0) {
				if(counter == 0) {
					if(progressBarFrame!=null) progress(progressBarFrame,Constants.ProgressBar.END.progress);
					throw new Exception("Problems exporting "+remainingSpecies.size()+" species and " + remainingGlobalQ.size() +" parameters");
				}
				remainingSpecies = fillCopasiDataModel_species(remainingSpecies);
				remainingGlobalQ = fillCopasiDataModel_globalQ_assignment_ode(remainingGlobalQ);
				if(remainingSpecies.size() == remainingSp || remainingGlobalQ.size() == remainingGlQ) {
					counter--;
				} else {
					remainingSp = remainingSpecies.size();
					remainingGlQ = remainingGlobalQ.size();
				}

			}
		} catch(Exception ex) {
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES)	ex.printStackTrace();
		}
		
		//CFunctionVectorN allFunctions = funDB.loadedFunctions();
		
		if(progressBarFrame!=null) progress(progressBarFrame,Constants.ProgressBar.LOADING_REACTIONS_CPS.progress);
		counterExpandedReactions = 0;
		model.getReactions().cleanup();
		if(tableReactionmodel!= null) {
			//--REACTIONS------------------------
			//TAKE REACTIONS FROM THE DB not from the GUIIIIIIIIIIIIII
			//TOOOOOOOOOOOO BEEEEEEEEEEEEE FIXED
			for(int i = 0; i < tableReactionmodel.getRowCount() ; i++ ) {
				//System.out.println("Exporting reaction " + i + " of "+ tableReactionmodel.getRowCount());
				String string_reaction = ((String)tableReactionmodel.getValueAt(i, 2));
				if(string_reaction.trim().length() <= 0) continue;


				boolean parseErrors = false;
				Vector metabolites = new Vector();
				try{ 
					metabolites = CellParsers.parseReaction(this,string_reaction,i+1);
				} catch(Exception ex) {
					if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) ex.printStackTrace();
					parseErrors = true;
					metabolites.add(new Vector());
					metabolites.add(new Vector());
					metabolites.add(new Vector());
				}

				if(parseErrors) continue;


				Vector singleConfigurations = expandReaction(metabolites,i);
				CReaction reaction;
				for(int j = 0; j < singleConfigurations.size(); j++) {
					Vector expandedReaction = (Vector) singleConfigurations.get(j);

					String reaction_name = ((String)tableReactionmodel.getValueAt(i, 1)).trim();
					if(singleConfigurations.size() > 1) {
						reaction_name = (i+1)+"_"+(j+1)+"_"+ reaction_name;
					} 
					if(reaction_name.length() ==0) {
						reaction_name = (i+1)+"_"+(j+1)+"_"+ reaction_name;
					}
					reaction = model.createReaction(reaction_name);
					counterExpandedReactions++;
					if(reaction == null) { 
						reaction = model.getReaction(this.findReaction(reaction_name)); 
						reaction.cleanup();
						counterExpandedReactions--;
					}
					reaction.setReversible(false);
					reaction.setNotes((String)tableReactionmodel.getValueAt(i, Constants.ReactionsColumns.NOTES.index));
				
					String rateLaw = ((String)tableReactionmodel.getValueAt(i, Constants.ReactionsColumns.KINETIC_LAW.index)).trim();
					String kineticType = ((String)tableReactionmodel.getValueAt(i, Constants.ReactionsColumns.TYPE.index)).trim();
					reaction = generateKineticLaw(reaction,i, expandedReaction, rateLaw, kineticType, model);
					
				}
					
				
			}
		}
		if(progressBarFrame!=null) progress(progressBarFrame,Constants.ProgressBar.LOADING_EVENTS_CPS.progress);
		
		fillCopasiDataModel_events();
		
		model.setTimeUnit(MainGui.timeUnit);
		model.setVolumeUnit(MainGui.volumeUnit);
		model.setQuantityUnit(MainGui.quantityUnit);
		if(MainGui.modelName.trim().length() ==0) MainGui.modelName = Constants.DEFAULT_MODEL_NAME;
		model.setObjectName(MainGui.modelName);
		
		
		
	}
		
	public Vector check_ifSingleFunctionCallAndSingleParameters(boolean massAction, int reaction_row, String reactionName, String equation) {
		Vector newEquation_listOfNewNames = new Vector();
		Vector listOfNewNames = new Vector();
		
		if(!massAction) {
			String funName  = new String();
			try{
				InputStream is = new ByteArrayInputStream(equation.getBytes("UTF-8"));
				MR_Expression_Parser parser = new MR_Expression_Parser(is);
				SingleFunctionCall root = parser.SingleFunctionCall();
				GetFunctionNameVisitor vis = new GetFunctionNameVisitor();
				root.accept(vis);
				funName  = vis.getFunctionName();
				//singleFunctionCall = true;
				newEquation_listOfNewNames.add("");
				newEquation_listOfNewNames.add(new Vector());
				return newEquation_listOfNewNames;
			} catch (Exception e) {
				e.printStackTrace();
				//singleFunctionCall = false;
				
				MutablePair<String, String> pair = add_function_4_reaction_funDB(reactionName, reaction_row, equation);
				listOfNewNames.add(pair.right);
				newEquation_listOfNewNames.add(pair.left);
				newEquation_listOfNewNames.add(listOfNewNames);
				return newEquation_listOfNewNames;
			} 
		}
		else {
			
			try {
				InputStream is = new ByteArrayInputStream(equation.getBytes("UTF-8"));
				MR_Expression_Parser parser = new MR_Expression_Parser(is);
				CompleteExpression root;
				
					root = parser.CompleteExpression();
				
	  			GetUsedVariablesInEquation v = new GetUsedVariablesInEquation();
	  			root.accept(v);
	  			if(v.getNames().size()==0) {
	  				newEquation_listOfNewNames.add("");
					newEquation_listOfNewNames.add(new Vector());
					return newEquation_listOfNewNames;
	  			}
	  		
	  			if(v.isComplexExpression()) {
	  				MutablePair<String, String> pair = add_globalQ_4_reaction_globalQDB(reactionName, reaction_row, equation);
					listOfNewNames.add(pair.right);
					newEquation_listOfNewNames.add(pair.left);
					newEquation_listOfNewNames.add(listOfNewNames);
					return newEquation_listOfNewNames;
				} else {
					return null;
				}
			} catch (Exception e) {
				return newEquation_listOfNewNames;
			}
		}
			
	}
	
	private String getName_function_4_reaction(String reactionName, int reaction_row) {
		 String name = reactionName;
		 if(name.length()==0) { name = Integer.toString(reaction_row); }
		 
		 name = Constants.PREFIX_FUN_4_REACTION_NAME +name;
		 return name;
	}
	
	private String getName_globalQ_4_reaction(String reactionName, int reaction_row) {
		 String name = reactionName;
		 if(name.length()==0) { name = Integer.toString(reaction_row); }
		 
		 name = Constants.PREFIX_GLQ_4_REACTION_NAME +name;
		 return name;
	}
	
	private MutablePair<String,String> add_globalQ_4_reaction_globalQDB(String reactionName, int reaction_row, String rateLaw) {
		 MutablePair<String,String> functionCall_globalQDef = new MutablePair<>();
		
		String name = getName_globalQ_4_reaction(reactionName, reaction_row);
		
		  try {
				String globalQ = name;
				  
				functionCall_globalQDef.left = globalQ;
				functionCall_globalQDef.right = globalQ + " = " + rateLaw;
				
				
				updateGlobalQ(getNumGlobalQ()+1, globalQ, "0.0", Constants.GlobalQType.ASSIGNMENT.description, rateLaw, new String());
				
		} catch (Exception e) {
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES)
			e.printStackTrace();
		}
	    return functionCall_globalQDef;
	}
	
	private MutablePair<String,String> add_function_4_reaction_funDB(String reactionName, int reaction_row, String rateLaw) {
		 MutablePair<String,String> functionCall_functionDef = new MutablePair<>();
		
		String name = getName_function_4_reaction(reactionName, reaction_row);
		
		  try {
				Function f = new Function(name); 
				f.setCompleteFunSignatureInTable(false);
				f.setEquation(rateLaw, CFunction.UserDefined, 0);
				Vector<String> names = f.getParametersNames();
				
				for(int n = 0; n < names.size(); n++) {
					//WROOOOOOOOOOOOOOOOONG
					//I have to look if the parameter in the equation is used as actual value for another function call and then pick that type
					//e.g. test(SUB a, SUB b) a + b
					// test2(?? a, ?? b, VAR k1) test(a,b) + k1 --> a and b should be assigned as SUB... and if not an error should be raised to the user
					f.setParameterRole(names.get(n), CFunctionParameter.VARIABLE  );
				}
				
				for(int n = 0; n < names.size(); n++) {
					f.setParameterIndex(names.get(n), n);
				}
				f.setCompleteFunSignatureInTable(true);
				
				String functionCall = f.getCompactedEquation(names);
				  
				  
				functionCall_functionDef.left = functionCall;
				functionCall_functionDef.right = f.printCompleteSignature() + " = " + f.getExpandedEquation(names);
				//if(cfun.getType() != CFunction.PreDefined)  {
				funDB.addChangeFunction(funDB.userDefinedFun.size(), f);	
				funDB.addMapping(reaction_row, functionCall, Constants.ReactionType.USER_DEFINED.description);
				
				
		} catch (Exception e) {
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES)
			e.printStackTrace();
		}
		 
		  
		    return functionCall_functionDef;
		
	}
	
	private MutablePair<String,String> add_function_4_reaction(CFunctionDB copasiFunDB, CReaction reaction, int i, String rateLaw) {
		
		 String name = reaction.getSBMLId();
		 
		 if(name.length()==0) { name = Integer.toString(i+1); }
		 
		 name = "function_4_reaction_"+name;
		
		   CFunction function = (CFunction)copasiFunDB.createFunction(name,CEvaluationTree.UserDefined);
			 if(function == null) {
				 function =(CFunction)copasiFunDB.findFunction(name);
			 }
			 
			 rateLaw = rateLaw.replace("'", "_pr "); // variable in infix in copasi cannot contain '
			 
			 
		   function.setInfix(rateLaw);
		  function.setReversible(COPASI.TriFalse);
		 
		
		  
		  MutablePair<String, String> funName_newRateLawExpression = new MutablePair<String, String>();
		  
		  funName_newRateLawExpression.left = name;
		  
		  CFunctionParameters variables = function.getVariables();
		  String functionCall = new String(name + "(");
		  int j = 0;
		  for(; j < variables.size(); j++) {
			   if(j==(-1 & 0xffffffffL)) break;
			    CFunctionParameter parame = variables.getParameter(j);
			    //TOOOOOOOOOOO FIX BECAUSE IF I HAVE SUBSTRATES, MODIFIER OR PRODUCTS I HAVE TO SET THE PROPER USAGE
			    parame.setUsage(CFunctionParameter.PARAMETER);
			    functionCall += parame.getObjectName() + ",";
		  }
		  functionCall = functionCall.substring(0,functionCall.length()-1);
		  functionCall += ")";
		  

		  functionCall = functionCall.replace("_pr","'");
		  funName_newRateLawExpression.right = functionCall;
		  
	//	  System.out.println("in add_function_4_reaction functionCall = "+functionCall);
		  
		  
		  try {
				Function f = new Function(name); 
				f.setCompleteFunSignatureInTable(false);
				f.setEquation(function.getInfix(), CFunction.UserDefined, 0);
				Vector<String> names = f.getParametersNames();
				
				for(int n = 0; n < names.size(); n++) {
					int type = (variables.getParameter(function.getVariableIndex(names.get(n)))).getUsage(); 
					if(type==CFunctionParameter.PARAMETER) type = CFunctionParameter.VARIABLE;
					f.setParameterRole(names.get(n), type  );
				}
				for(int n = 0; n < names.size(); n++) {
					f.setParameterIndex(names.get(n), function.getVariableIndex(names.get(n)));
				}
				f.setCompleteFunSignatureInTable(true);
				f.setShow(true);
				funDB.addChangeFunction(funDB.userDefinedFun.size(), f);	
				funDB.addMapping(i, functionCall, Constants.ReactionType.USER_DEFINED.description);
				
		} catch (Exception e) {
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES)
			e.printStackTrace();
		}
		   return funName_newRateLawExpression;
		
	}
	
	

	CModelValue add_globalQ_4_function(CModel model, CReaction reaction, int index_reaction, int index_param, String actualModelParameterExpression) {
		String name = reaction.getSBMLId();
		 
		 if(name.length()==0) { name = Integer.toString(index_reaction+1); }
		 
		 name = "glq_4_reac_"+name+"_param_"+Integer.toString(index_param+1);
		
		
		CModelValue modelValue = model.createModelValue(name, 0.0);	
		if(modelValue == null) { modelValue = model.getModelValue(name);		}
		
		modelValue.setStatus(CModelValue.ASSIGNMENT);
		
		try {
			String expr = buildCopasiExpression(actualModelParameterExpression,false,false);
			modelValue.setExpression(expr);

		} catch (Exception e) {
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
		}
	
		//CCopasiObject object = modelValue.getObject(new CCopasiObjectName("Reference=InitialValue"));
		//changedObjects.add(object);
		model.compileIfNecessary();
		
		return modelValue;
	}
		
	
	
	
	private CReaction generateKineticLaw(CReaction reaction, int i, Vector expandedReaction, String rateLaw, String kineticType, CModel model) throws Exception {
		
		CChemEq chemEq = reaction.getChemEq();
		chemEq.cleanup();
	
		Vector subs = (Vector)expandedReaction.get(0);
		Vector prod =(Vector)expandedReaction.get(1);
		Vector mod = (Vector)expandedReaction.get(2);
		
		double multiplicity = 1.0;
		for(int i1 = 0; i1 < subs.size(); i1++) {
			String s = (String)subs.get(i1);
			multiplicity = extractMultiplicity(s);
			CMetab metab;
			int index_metab = -1;
			String cmp = CellParsers.extractCompartmentLabel(s);
			if(cmp.length() > 0 ) {
				String justName = CellParsers.extractMultistateName(extractName(s)); //extractName separates just the multiplicity
				index_metab = this.findMetabolite(justName, cmp);
			} else {
				String onlyName = extractName(s);
				index_metab = this.findMetabolite(onlyName,null);
			}
			
			if(index_metab==-1) {
				continue; 
			} else {
				metab = model.getMetabolite(index_metab);
			}
			chemEq.addMetabolite(metab.getKey(), multiplicity, CChemEq.SUBSTRATE);
		}

		for(int i1 = 0; i1 < prod.size(); i1++) {
			String s = (String)prod.get(i1);
			CMetab metab;
			multiplicity = extractMultiplicity(s);
			int index_metab = -1;
			String cmp = CellParsers.extractCompartmentLabel(s);
			if(cmp.length() > 0 ) {
				String justName = CellParsers.extractMultistateName(extractName(s)); //extractName separates just the multiplicity
				index_metab = this.findMetabolite(justName, cmp);
			} else {
				String onlyName = extractName(s);
				index_metab = this.findMetabolite(onlyName,null);
			}
			if(index_metab==-1) {
				continue;
			} else {
				metab = model.getMetabolite(index_metab);
			}
			chemEq.addMetabolite(metab.getKey(), multiplicity, CChemEq.PRODUCT);
		}

		for(int i1 = 0; i1 < mod.size(); i1++) {
			String s = (String)mod.get(i1);
			CMetab metab;
			int index_metab = -1;
			String cmp = CellParsers.extractCompartmentLabel(s);
			if(cmp.length() > 0 ) {
				String justName = CellParsers.extractMultistateName(extractName(s)); //extractName separates just the multiplicity
				index_metab = this.findMetabolite(justName, cmp);
			} else {
				String onlyName = extractName(s);
				index_metab = this.findMetabolite(onlyName,null);
			}
			multiplicity = extractMultiplicity(s);
			s = extractName(s);
			if(index_metab==-1) {
				continue;
			} else {
				metab = model.getMetabolite(index_metab);
			}
			chemEq.addMetabolite(metab.getKey(), multiplicity, CChemEq.MODIFIER);
		}
		

		if(rateLaw.trim().length() == 0 || kineticType.length() ==0) {
			return reaction; // copasi undefined reaction rate law
		}
		
		
		boolean foundUserDefFunction = false;
		boolean foundPredefinedFunction = false;
		boolean foundMassAction = false;
		

		if(kineticType.compareTo(Constants.ReactionType.USER_DEFINED.description)==0) { foundUserDefFunction = true; }
		else if(kineticType.compareTo(Constants.ReactionType.PRE_DEFINED.description)==0) { foundPredefinedFunction = true; }
		else if(kineticType.compareTo(Constants.ReactionType.MASS_ACTION.description)==0) {  foundMassAction = true;    	 }

		CFunctionDB copasiFunDB = CCopasiRootContainer.getFunctionList();
		boolean singleFunctionCall = false;
		
		if(foundPredefinedFunction) {
			CFunction val = (CFunction) copasiFunDB.findFunction(MainGui.cleanNamesPredefinedFunctions.get(rateLaw.substring(0,rateLaw.lastIndexOf("("))));
			reaction.setFunction(val);
		}
		else if (foundMassAction) {
			CFunction val = (CFunction)copasiFunDB.findFunction(this.getCompleteNameSuitableFunction("Mass action",subs.size(), prod.size()));
			reaction.setFunction(val);
		}
		else if(foundUserDefFunction) {
			String funName  = new String();
			try{
				InputStream is = new ByteArrayInputStream(rateLaw.getBytes("UTF-8"));
				MR_Expression_Parser parser = new MR_Expression_Parser(is);
				SingleFunctionCall root = parser.SingleFunctionCall();
				GetFunctionNameVisitor vis = new GetFunctionNameVisitor();
				root.accept(vis);
				funName  = vis.getFunctionName();
				singleFunctionCall = true;
			} catch (Exception e) {
				singleFunctionCall = false;
				//e.printStackTrace();
			}
			
			if(singleFunctionCall) {
				if(MainGui.cleanNamesPredefinedFunctions.containsKey(funName)) {
					funName = MainGui.cleanNamesPredefinedFunctions.get(funName);
				}
				
				CFunction val = (CFunction)copasiFunDB.findFunction(funName);
				reaction.setFunction(val);
			} else {
				MutablePair<String, String> pair = add_function_4_reaction(copasiFunDB, reaction, i, rateLaw);
				funName = pair.left;
				CFunction val = (CFunction)copasiFunDB.findFunction(funName);
				reaction.setFunction(val);
			}
		}
		
		
		
		//System.out.println("Exporting "+rateLaw);
		//System.out.flush();
		if(foundUserDefFunction || foundPredefinedFunction) {
			Vector paramMapping = new Vector();
			paramMapping = (Vector) funDB.getMapping(i);
			Function f = (Function)paramMapping.get(0);
			
			Vector<Integer> paramRoles =  f.getParametersTypes_CFunctionParameter();
			Vector roleVector = new Vector();
			roleVector.add(CFunctionParameter.PARAMETER);
			roleVector.add(CFunctionParameter.SUBSTRATE);
			roleVector.add(CFunctionParameter.PRODUCT);
			roleVector.add(CFunctionParameter.MODIFIER);
			roleVector.add(CFunctionParameter.VOLUME);
			roleVector.add(Constants.ROLE_EXPRESSION);
			
			CFunction chosenFun = reaction.getFunction();
			
			for(int iii = 1, jjj = 0; iii < paramMapping.size(); iii=iii+2,jjj++) {
				String parameterNameInFunction = (String)paramMapping.get(iii);
				String actualModelParameter = (String)paramMapping.get(iii+1);
				Vector expandedVectorElements = new Vector();
				int role = paramRoles.get(jjj);
				boolean checkAllRoles = false;
				int indexRole = 0;
				
				if(role==CFunctionParameter.VARIABLE) { checkAllRoles  = true; role= (Integer) roleVector.get(indexRole);}
				do{
					try {
						switch(role) {
						case CFunctionParameter.PARAMETER:     
							try{
								Double parValue = Double.parseDouble(actualModelParameter);
								reaction.setParameterValue(parameterNameInFunction,parValue.doubleValue());
								checkAllRoles = false;
							} catch(NumberFormatException ex) { //the parameter is not a number but a globalQ
								if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) ex.printStackTrace();
								if(actualModelParameter.startsWith("\"")&&actualModelParameter.endsWith("\"")) actualModelParameter = actualModelParameter.substring(1, actualModelParameter.length()-1);
								int index = this.findGlobalQ(actualModelParameter);
								if(index == -1) throw new NullPointerException();
								CModelValue modelValue = model.getModelValue(actualModelParameter);
								reaction.setParameterMapping(findParamIndex_InCFunction(chosenFun, parameterNameInFunction), modelValue.getKey());
								checkAllRoles = false;
							}
							break;
						case Constants.ROLE_EXPRESSION:
							CModelValue modelValue = add_globalQ_4_function(model, reaction, i, jjj, actualModelParameter);
							reaction.setParameterMapping(findParamIndex_InCFunction(chosenFun, parameterNameInFunction), modelValue.getKey());
							checkAllRoles = false;
							break;
						case CFunctionParameter.SUBSTRATE: 
							
							expandedVectorElements.addAll(subs);
							int index_metab = this.findMetabolite(actualModelParameter,null);
							
							if(index_metab == -1) {
								for(int g = 0; g < expandedVectorElements.size(); g++) {
									String current = (String)(expandedVectorElements.get(g));
									if(current.contains(actualModelParameter)) {
										index_metab = this.findMetabolite(current,null);
									}
								}
							}
							if(index_metab == -1) { //it's not a normal multistate species but the one with multiple compartment
								if(CellParsers.isMultistateSpeciesName(actualModelParameter)) {
									String cmp = CellParsers.extractCompartmentLabel(actualModelParameter);
									String justName = CellParsers.extractMultistateName(actualModelParameter);
									index_metab = this.findMetabolite(justName, cmp);
								}
								
							}
							
							if(index_metab == -1) throw new NullPointerException();
							CMetab metab = model.getMetabolite(index_metab);
							if(actualModelParameter.compareTo("\"SUB\"")==0) {
								System.out.println("SUUUB");
							}
							reaction.setParameterMapping(findParamIndex_InCFunction(chosenFun, parameterNameInFunction), metab.getKey());
							checkAllRoles = false;
							break;
						case CFunctionParameter.PRODUCT:  
							expandedVectorElements.addAll(prod); 
							index_metab = this.findMetabolite(actualModelParameter,null);
							if(index_metab == -1) {
								for(int g = 0; g < expandedVectorElements.size(); g++) {
									String current = (String)(expandedVectorElements.get(g));
									if(current.contains(actualModelParameter)) {
										index_metab = this.findMetabolite(current,null);
									}
								}
							}
							if(index_metab == -1) { //it's not a normal multistate species but the one with multiple compartment
								if(CellParsers.isMultistateSpeciesName(actualModelParameter)) {
									String cmp = CellParsers.extractCompartmentLabel(actualModelParameter);
									String justName = CellParsers.extractMultistateName(actualModelParameter);
									index_metab = this.findMetabolite(justName, cmp);
								}
								
							}
							
							if(index_metab == -1) throw new NullPointerException();
							metab = model.getMetabolite(index_metab);
							reaction.setParameterMapping(findParamIndex_InCFunction(chosenFun, parameterNameInFunction), metab.getKey());
							checkAllRoles = false;
							break;
						case CFunctionParameter.MODIFIER:   
							expandedVectorElements.addAll(mod);
							index_metab = this.findMetabolite(actualModelParameter,null);
							if(index_metab == -1) {
								for(int g = 0; g < expandedVectorElements.size(); g++) {
									String current = (String)(expandedVectorElements.get(g));
									if(current.contains(actualModelParameter)) {
										index_metab = this.findMetabolite(current,null);
									}
								}
							}
							if(index_metab == -1) { //it's not a normal multistate species but the one with multiple compartment
								if(CellParsers.isMultistateSpeciesName(actualModelParameter)) {
									String cmp = CellParsers.extractCompartmentLabel(actualModelParameter);
									String justName = CellParsers.extractMultistateName(actualModelParameter);
									index_metab = this.findMetabolite(justName, cmp);
								}
								
							}
							if(index_metab == -1) throw new NullPointerException();
							metab = model.getMetabolite(index_metab);
							reaction.setParameterMapping(findParamIndex_InCFunction(chosenFun, parameterNameInFunction), metab.getKey());
							checkAllRoles = false;
							break;
						case CFunctionParameter.VOLUME:    
							int index = this.findCompartment(actualModelParameter);
							if(index == -1) throw new NullPointerException();
							CCompartment comp = model.getCompartment(index);
							reaction.setParameterMapping(findParamIndex_InCFunction(chosenFun, parameterNameInFunction), comp.getKey());
							checkAllRoles = false;
							break;
						case CFunctionParameter.TIME:    
							reaction.setParameterMapping(findParamIndex_InCFunction(chosenFun, parameterNameInFunction), model.getKey());
							break;
						default:
							System.out.println("missing parameter role in function, "+chosenFun.getObjectName()+" for actual value " + actualModelParameter);
						}
					} catch(Exception ex) {
						if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) ex.printStackTrace();
						if(checkAllRoles==false) throw ex;
						indexRole++;
						try {
							role = (Integer) roleVector.get(indexRole);
						} catch(Exception ex2) {
							if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) ex2.printStackTrace();
							
							break;
						}
					}
				}while(checkAllRoles || indexRole >=roleVector.size());
			}
		}



		if(foundMassAction) {
			try {
				Double parValue = Double.parseDouble(rateLaw);
				reaction.setParameterValue(reaction.getParameters().getName(0),parValue,true);
			} catch(NumberFormatException ex) { //the parameter is not a number but a globalQ
				if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) ex.printStackTrace();
				CModelValue modelValue = model.getModelValue(rateLaw);
				reaction.setParameterMapping(0, modelValue.getKey());
			}
			
			
		} 
		


		//Vector mod_indexes = new Vector();
		//Vector sub_indexes = new Vector();
		//Vector prod_indexes = new Vector();
		int volume_index = -1;
	
		CEvaluationTree f = copasiFunDB.findFunction(reaction.getFunction().getObjectName());
		
		//TOOOOOOOOOOOOOOOOOOOOO BE FIXED (CHECK IF IT EXISTS IN THE MULTIREMI DATA STRUCTURE FOR USERDEFINED FUNCTIONS)
	/*	if(f instanceof CFunction && !reaction.getFunction().getObjectName().contains("action") &&
				!reaction.getFunction().getObjectName().contains("flux")) {
			CFunctionParameters variables = ((CFunction) f).getVariables();
			for(int r = 0; r < variables.size(); r++) {
				CFunctionParameter p = variables.getParameter(r);
				if(p.getUsage() == CFunctionParameter.MODIFIER) { 
					mod_indexes.add(r);}
				if(p.getUsage() == CFunctionParameter.SUBSTRATE) { 
					sub_indexes.add(r);
				}
				
				if(p.getUsage() == CFunctionParameter.PRODUCT) { 
					prod_indexes.add(r);
				}

				if(p.getUsage() == CFunctionParameter.VOLUME) {
					volume_index = r;
				}

			}
		}*/

		if(foundMassAction) {
			for(int r = 0; r < mod.size(); r++ ) {
				String s = (String)mod.get(r);
				s = extractName(s);
				CMetab metab;
				int index_metab = this.findMetabolite(s,null);
				metab = model.getMetabolite(index_metab);
				//if(mod_indexes.size() > 0 ) reaction.setParameterMapping((Integer) mod_indexes.get(r),  metab.getKey());
				//else 
				reaction.addParameterMapping("modifier",  metab.getKey());
			}
	
			
			
			for(int r = 0; r < subs.size(); r++ ) {
				String s = (String)subs.get(r);
				multiplicity = extractMultiplicity(s);
				s = extractName(s);
				CMetab metab;
				int index_metab = this.findMetabolite(s,null);
				
				if(index_metab == -1) { //it's not a normal multistate species but the one with multiple compartment
					if(CellParsers.isMultistateSpeciesName(s)) {
						String cmp = CellParsers.extractCompartmentLabel(s);
						String justName = CellParsers.extractMultistateName(s);
						index_metab = this.findMetabolite(justName, cmp);
					}
					
				}
				metab = model.getMetabolite(index_metab);
				/*if(sub_indexes.size() > 0 ) {
					reaction.setParameterMapping((Integer) sub_indexes.get(r),  metab.getKey());
				}
				else {
				*/	
					if(multiplicity == Math.floor(multiplicity)){
						for(int i11 = 0; i11 < Math.floor(multiplicity); i11++) {
							reaction.addParameterMapping("substrate",  metab.getKey());
						}
					}
					else {
						reaction.addParameterMapping("substrate",  metab.getKey());
					}	
				//}
			}
			//if(volume_index != -1 ) reaction.setParameterMapping(volume_index,  model.getCompartment(0).getKey());
	
	
			for(int r = 0; r < prod.size(); r++ ) {
				String s = (String)prod.get(r);
				s = extractName(s);
				CMetab metab;
				int index_metab = this.findMetabolite(s,null);
				if(index_metab == -1) { //it's not a normal multistate species but the one with multiple compartment
					if(CellParsers.isMultistateSpeciesName(s)) {
						String cmp = CellParsers.extractCompartmentLabel(s);
						String justName = CellParsers.extractMultistateName(s);
						index_metab = this.findMetabolite(justName, cmp);
					}
					
				}
				metab = model.getMetabolite(index_metab);
				/*if(prod_indexes.size() > 0 ) { 
					reaction.setParameterMapping((Integer) prod_indexes.get(r),  metab.getKey());
				} else */
				reaction.addParameterMapping("product",  metab.getKey());
	
			}
		}
		return reaction;
	}
	
	

	public String extractName(String s) {
			
		String ret = null;
		try{
			InputStream is = new ByteArrayInputStream(s.getBytes("UTF-8"));
			MR_ChemicalReaction_Parser react = new MR_ChemicalReaction_Parser(is);
			CompleteSpeciesWithCoefficient start = react.CompleteSpeciesWithCoefficient();
			ExtractSubProdModVisitor v = new ExtractSubProdModVisitor(this);
		    start.accept(v);
			ret = v.getExtractedName_fromSpeciesWithCoefficient();
		}catch (Exception e) {
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES)
			e.printStackTrace();
		}
		return ret;
	}

	public double extractMultiplicity(String s) {
		
		Double ret = new Double(1.0);
		try{
			InputStream is = new ByteArrayInputStream(s.getBytes("UTF-8"));
			MR_ChemicalReaction_Parser react = new MR_ChemicalReaction_Parser(is);
			CompleteSpeciesWithCoefficient start = react.CompleteSpeciesWithCoefficient();
			ExtractSubProdModVisitor v = new ExtractSubProdModVisitor(this);
		    start.accept(v);
			ret = v.getExtractedCoeff_fromSpeciesWithCoefficient();
		}catch (Exception e) {
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES)
			e.printStackTrace();
		}
		return ret;
	}
	
	public void cleanUpModel() {
		if(copasiDataModel==null) return;
		CModel model = this.copasiDataModel.getModel();
		if(model!=null) model.cleanup();
		
		
		allNamedElements.clear();
		speciesDB.clear();
		compDB.clear();
		eventsDB.clear();
		funDB.clear();
		globalqDB.clear();
		
		
		System.gc();
	}
	
	static int speciesToBeExported = 0;
	
	private Vector<Species> fillCopasiDataModel_species(Vector<Species> species) throws Exception {
		if(species.size() == 0) return new Vector<Species> ();
		
		speciesToBeExported = species.size();
		CCopasiObject object = null;
		
		CModel model = this.copasiDataModel.getModel();
		Vector<Species> species_with_expression_not_added = new Vector<Species>();
		
		for(int i = 0; i < species.size(); i++) {
			Species s = species.get(i);
			if(s!= null){
				if(s instanceof Species && !(s instanceof MultistateSpecies)) {
					String name = s.getDisplayedName();
					if(name.trim().length()== 0) continue;
					String compartmentList = s.getCompartment_listString();
					//FOR NOW ALL HAVE THE SAME INITIAL CONCENTRATION/EXPRESSION... NEEEEEEEEEED TO BE FIIIIIIIIIIIIXED
					Vector<String> compNames = CellParsers.extractListElements(this,compartmentList, Constants.TitlesTabs.SPECIES.description, Constants.SpeciesColumns.COMPARTMENT.description);
					
					
					for(int ii = 0; ii < compNames.size(); ii++) {
						//double initial_conc = s.getInitialConcentration().doubleValue();
						String comp = compNames.get(ii);
						String initialExpression = new String();
						double initial_conc = 0.0;
						if(MainGui.quantityIsConc) {
							try{
								initial_conc = Double.parseDouble(s.getInitialQuantity());
							} catch(Exception ex) { //E' UN'ESPRESSIONE METTERE 0 QUI E INITIAL EXPRESSION COME EXPRESSION
								if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) ex.printStackTrace();
								initialExpression = s.getInitialQuantity();
							}
						}

						int type = s.getType();
						CMetab m;
						int index_metab = this.findMetabolite(name,comp);
						if(index_metab == -1) m = model.createMetabolite(name, comp , initial_conc,  type);
						else m = model.getMetabolite(index_metab);

						String sbmlID = s.getSBMLid().trim();
						if(sbmlID.length()==0)  sbmlID = m.getKey();
						m.setSBMLId(sbmlID);

						if(s.getExpression().trim().length() > 0) {
							try {
								String expr = buildCopasiExpression(s.getExpression(),true,false);
								//System.out.println("Expression: "+expr);

								if(expr.length()==0) throw new Exception("Problems exporting the expression "+s.getExpression());
								m.setExpression(expr);
								//m.setInitialExpression(expr);
							} catch (Exception e) {
								if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
								species_with_expression_not_added.add(s);
							}
						}

						if(!MainGui.quantityIsConc)		{
							try{
								m.setInitialValue(Double.parseDouble(s.getInitialQuantity()));
							} catch(Exception ex) { //E' UN'ESPRESSIONE METTERE 0 QUI E INITIAL EXPRESSION COME EXPRESSION
								if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) ex.printStackTrace();
								initialExpression = s.getInitialQuantity();
							}
						}
						if(initialExpression.length()>0 ) {
							if(initialExpression.compareTo(MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstantsNOQUOTES.NAN)) != 0)
								m.setInitialExpression(buildCopasiExpression(initialExpression, false,true));
							else m.setInitialExpression("nan");
						} else {
							m.setInitialConcentration(initial_conc);
						}					
						//if(!MainGui.quantityIsConc) object = m.getObject(new CCopasiObjectName("Reference=InitialParticleNumber"));
						//else object = m.getObject(new CCopasiObjectName("Reference=InitialConcentration"));
						m.setNotes(s.getNotes());
					}
					//changedObjects.add(object);
				} else {

					MultistateSpecies multi = (MultistateSpecies) s;

					Vector<Species> expanded = multi.getExpandedSpecies(this);
					//expanded = this.speciesDB.assignInitals_expandedMultistateVector(expanded);

					//CCompartment compartment = model.createCompartment(multi.getCompartment());

					for(int j = 0; j < expanded.size(); j++) {
						Species single = expanded.get(j);
						String name = single.getDisplayedName();
						String comp = single.getCompartment_listString();
						double initial_conc = 0.0;
						if(MainGui.quantityIsConc) {
							try{
								String init_q = multi.getInitial_singleConfiguration(single);
								if(init_q!= null) {
									initial_conc = Double.parseDouble(init_q);
								}
							} catch(Exception ex) {//FOOOOOOOOOOOOR NOW NO EXPRESSION IN THE MULTISTATE SPECIES
								if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) ex.printStackTrace();
								throw ex;
							} 
						}
						
						int type = single.getType();

						CMetab m;
						int index_metab = this.findMetabolite(name,null);
						if(index_metab == -1) m = model.createMetabolite(name, comp , initial_conc,  type);
						else m = model.getMetabolite(index_metab);

						if(MainGui.quantityIsConc) { 
							m.setInitialConcentration(initial_conc); 
							//object = m.getObject(new CCopasiObjectName("Reference=InitialConcentration"));
						}
						
						m.setMiriamAnnotation(Constants.MULTISTATE_ANNOTATION, Constants.MULTISTATE_ANNOTATION,  Constants.MULTISTATE_ANNOTATION);
						double initial_amount = 0.0;
						if(!MainGui.quantityIsConc)		{
							try{
								String init_q = multi.getInitial_singleConfiguration(single);
								if(init_q!= null) {
									initial_amount = Double.parseDouble(init_q);
								}
							} catch(Exception ex) { //FOOOOOOOOOOOOR NOW NO EXPRESSION IN THE MULTISTATE SPECIES
								if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) ex.printStackTrace();
								throw ex;
							}
							m.setInitialValue(initial_amount);
							//object = m.getObject(new CCopasiObjectName("Reference=InitialParticleNumber"));
							
						}
						
						
						try {
							String expr = buildCopasiExpression(s.getExpression(),true,false);
							m.setExpression(expr);
						} catch (Exception e) {
							if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
							species_with_expression_not_added.add(single);
						}
						
						m.setNotes(s.getNotes());
						//changedObjects.add(object);
					}
				}
				
				
			}

		}
		
		
		if(species_with_expression_not_added.size() < speciesToBeExported) return fillCopasiDataModel_species(species_with_expression_not_added);
		return species_with_expression_not_added;
		
	}

	private String getCompleteNameSuitableFunction(String string, int numSub, int numProd) {
		CFunctionDB funDB = CCopasiRootContainer.getFunctionList();
		CFunctionStdVector suitableFunctions = funDB.suitableFunctions(numSub, numProd, COPASI.TriFalse);
		int i1 = 0;
		int iMax=(int)suitableFunctions.size();

		for (i1=0;i1<iMax;i1++) {
			if (suitableFunctions.get(i1).getObjectName().indexOf(string) != -1)    { 
				return   suitableFunctions.get(i1).getObjectName();
			}
		}
		return null;
	}

	private int findParamIndex_InCFunction(CFunction chosenFun, String param) {
		CFunctionParameters var = chosenFun.getVariables();
		
		if(param.startsWith("\"")&param.endsWith("\"")) param = param.substring(1,param.length()-1);
		for(int i = 0; i < var.size(); i++) {
			CFunctionParameter par = var.getParameter(i);
			if(par.getObjectName().compareTo(param) == 0) return i; 
		}
		return -1;
	}
	 
	
	private CFunctionDB fillCopasiDataModel_functions() {
		CFunctionDB funDB_copasi = CCopasiRootContainer.getFunctionList();
		//System.out.println("size 1: " + funDB_copasi.loadedFunctions().size());
		
		Collection<Function> defFunctions = funDB.getAllFunctions();
		 Iterator it2 = defFunctions.iterator();
		 while(it2.hasNext()) {
			 Function fun = (Function)it2.next();
			// try {
				 CFunction ev =(CFunction)funDB_copasi.findFunction(fun.name);
				 if(ev == null) {
		   //throw new Exception(""); //System.out.println(ev);
			 //} catch(Exception ex) {
				 //if I get an exception it means that the function is not there so I have to add
				 //the function in the database. If the function exist no exception are thrown so
				 //I can simply go on because the function is already there
			  if(fun.getType()==CFunction.UserDefined ||
				   (fun.getType() == CFunction.PreDefined && 
				      (fun.name.contains(CellParsers.cleanName(Constants.DEFAULT_SUFFIX_COPASI_BACKWARD_REACTION))
				    	||fun.name.contains(CellParsers.cleanName(Constants.DEFAULT_SUFFIX_COPASI_FORWARD_REACTION)) )
				    )
				 ) {
				  //CFunction cf = new CFunction(fun.getCopasiFunction());
				  //funDB_copasi.add(cf,true);
				  
				  CFunction function = (CFunction)funDB_copasi.createFunction(fun.getName(),CEvaluationTree.UserDefined);
				  boolean result = function.setInfix(fun.equation);
				  function.setReversible(COPASI.TriFalse);
				 
				 // function.setNotes(fun.getNotes());
				  CFunctionParameters variables = function.getVariables();
				 
				  
				  Vector orderedParameters = fun.getParametersNames();
				  for(int ord = 0; ord < orderedParameters.size(); ord++) {
					  String nextParam = (String) orderedParameters.get(ord);
					  long index = function.getVariableIndex(nextParam);

					  if(index==(-1 & 0xffffffffL)) break;
					  if(ord!=index) {
						  variables.swap(index, ord);
						  index = function.getVariableIndex(nextParam);
					  }
					
					  CFunctionParameter parame = variables.getParameter(index);
				
					  Integer usage = fun.parametesRoles.get(nextParam);
					  if(usage == CFunctionParameter.VARIABLE) usage = CFunctionParameter.PARAMETER;
					  if(usage == Constants.SITE_FOR_WEIGHT_IN_SUM) usage = CFunctionParameter.PARAMETER;
					  parame.setUsage(usage);
				 }
				  
			  }
			 }
		 }
		 
		
		 return funDB_copasi;
	}
	
	private void fillCopasiDataModel_globalQ_fixed() {
		CModel model = this.copasiDataModel.getModel();
		
		Iterator it = this.globalqDB.getAllNames().iterator();
	
		while(it.hasNext()) {
			String name = (String) it.next();
			GlobalQ g = this.globalqDB.getGlobalQ(name);
			if(g.type != CModelValue.FIXED) { 
				continue; 
			}
			
			CModelValue modelValue = model.createModelValue(name, new Double(0.0));	
			if(modelValue == null) {
				modelValue = model.getModelValue(name);
			}
			
			try{
				Double value = Double.parseDouble(g.getInitialValue());
				modelValue.setInitialValue(value);
			}catch(Exception ex) {
				if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) ex.printStackTrace();
				if(g.getInitialValue().compareTo(MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstantsNOQUOTES.NAN)) == 0) {
					modelValue.setInitialExpression("nan");
				} else {
					modelValue.setInitialExpression(g.getInitialValue());
				}
			}	
			
			modelValue.setNotes(g.getNotes());
			//CCopasiObject object = modelValue.getObject(new CCopasiObjectName("Reference=InitialValue"));
			//changedObjects.add(object);
		}
		
		model.compileIfNecessary();
		
		
	
	}
	
	private void fillCopasiDataModel_events() throws Exception {
		CModel model = this.copasiDataModel.getModel();
		Iterator it = this.eventsDB.getAllEvents().iterator();
		int index_event = 0;
		while(it.hasNext()) {
		
			Event ev = (Event) it.next();
			if(ev== null) { continue;}
			
			if(ev.getTrigger().trim().length() == 0) continue;
			
			String trig = parseExpressionGlobalQ(ev.getTrigger(),MainGui.exportConcentration,false,false);
			
			String name = ev.getName();
			
			int index = this.findEvent(name);
			CEvent event = null;
			if(index != -1) event = model.getEvent(index);
			else {
				index_event++;
				if(name.trim().length()==0) name = "event_"+index_event;
				event = model.createEvent(name);
			}
			
			event.setTriggerExpression(trig);
			Vector<String> actions = buildCopasiExpressionAssignment(ev.getActions(), ev.getExpandActionVolume());
			
			for(int i = 0; i < actions.size(); i=i+2) {
				CEventAssignment assign = event.createAssignment();
				assign.setTargetKey(actions.get(i));
				assign.setExpression(actions.get(i+1));
			}
			
			String delayAssignment = ev.getDelay();
			if(delayAssignment.length() >0) {
				event.setDelayAssignment(!ev.isDelayAfterCalculation());
				event.setDelayExpression(buildCopasiExpression(delayAssignment, false,false));
			}
			//index_event++;
			event.setNotes(ev.getNotes());
		}
		model.compile();
		
	}
	
	private void fillCopasiDataModel_compartments() throws Exception {
		CModel model = this.copasiDataModel.getModel();
		
		Iterator it = this.compDB.getAllNames().iterator();
		while(it.hasNext()) {
			String name = (String) it.next();
			Compartment c = this.compDB.getComp(name);
			CCompartment comp = null;
			try{
				Double size = Double.parseDouble(c.getInitialVolume());
				comp = model.createCompartment(name, size);
			} catch(Exception ex) {
				if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) ex.printStackTrace();
				comp = model.createCompartment(name, 1.0);
				comp.setInitialExpression(c.getInitialVolume());
			}
			
			if(comp == null) { comp = model.getCompartment(this.findCompartment(name)); }
			if(c.getType()!=CCompartment.FIXED) {
				comp.setStatus(c.getType());
				String expr = buildCopasiExpression(c.getExpression(),false,false);
				comp.setExpression(expr);
				try{
					Double size = Double.parseDouble(c.getInitialVolume());
					comp.setInitialValue(size);
				} catch(Exception ex) {
					if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) ex.printStackTrace();
					comp = model.createCompartment(name, 1.0);
					comp.setInitialExpression(buildCopasiExpression(c.getInitialVolume(),false,true));
				}
			}
			
			comp.setNotes(c.getNotes());
			//CCopasiObject object = comp.getObject(new CCopasiObjectName("Reference=InitialVolume"));
			//changedObjects.add(object);
		}
		

		model.compileIfNecessary();
	}
	
	static int globalQToBeExported = 0;
	
	private Vector<GlobalQ> fillCopasiDataModel_globalQ_assignment_ode(Vector<GlobalQ> globalQ) throws Exception {
		CModel model = this.copasiDataModel.getModel();
		
		globalQToBeExported = globalQ.size();
		Iterator it = globalQ.iterator();
		
		Vector<GlobalQ> globalQ_with_expression_not_added = new Vector<GlobalQ>();
		
		while(it.hasNext()) {
			
			GlobalQ g = (GlobalQ) it.next();
			if(g == null || g.getName().trim().length() == 0) {	continue;	}
			if(g.getType() == CModelValue.FIXED) { 
				continue; 
			}
			if(g.getName().compareTo("F")==0){
				System.out.println("qui");
			}
			CModelValue modelValue = null;
			try{
				Double value = Double.parseDouble(g.getInitialValue());
				modelValue = model.createModelValue(g.getName(), value);	
			}catch(Exception ex) {
				if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) ex.printStackTrace();
				modelValue = model.createModelValue(g.getName(), 0.0);
				
				modelValue.setInitialExpression(buildCopasiExpression(g.getInitialValue(),false,true));
			}	
			
		
			if(modelValue == null) { modelValue = model.getModelValue(g.getName());		}
			
			modelValue.setStatus(g.getType());
			
			try {
				String expr = buildCopasiExpression(g.getExpression(),false,false);
				modelValue.setExpression(expr);

			} catch (Exception e) {
				if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
				globalQ_with_expression_not_added.add(g);
			}
		
			modelValue.setNotes(g.getNotes());
			//CCopasiObject object = modelValue.getObject(new CCopasiObjectName("Reference=InitialValue"));
			//changedObjects.add(object);
			model.compileIfNecessary();
		  
		}
		
	
		if(globalQ_with_expression_not_added.size() < globalQToBeExported) fillCopasiDataModel_globalQ_assignment_ode(globalQ_with_expression_not_added);
		return globalQ_with_expression_not_added;
		
	}
	
	
	public String extract_weightFunction_in_SUM(String element) {
		int ind_LastBracket= element.lastIndexOf("(");
		String weightFunctionString = new String();
		if(ind_LastBracket != element.indexOf("(")) {
			int ind_previousComma = (element.substring(0,ind_LastBracket)).lastIndexOf(",");
			weightFunctionString = element.substring(ind_previousComma+1,element.lastIndexOf(")"));
			
		}
		return weightFunctionString;
	}
	
	public MultistateSpecies extract_object_of_SUM(String element) throws Exception {
		String weightFunctionString = extract_weightFunction_in_SUM(element);
		if(weightFunctionString.length() > 0) {
			element = element.substring(0,element.length()- weightFunctionString.length()-2);
		} 
		
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
					limits.add(Integer.parseInt(lower_bound));
					limits.add(Integer.parseInt(upper_bound));
				} catch (NoSuchElementException ex){ //there are no lower-upper bounds --> all the site states
					if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) ex.printStackTrace();
					MultistateSpecies ms = (MultistateSpecies) this.speciesDB.getSpecies(multistate_species_name);
					Vector states = ms.getSiteStates_complete(site);
					String lower_bound = (String) states.get(0);
					String upper_bound = (String)states.get(states.size()-1);
					limits.add(Integer.parseInt(lower_bound));
					limits.add(Integer.parseInt(upper_bound));
				}
				sitesSum.put(site, limits);
			}
		} catch(NumberFormatException numberEx) { 
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) numberEx.printStackTrace();
			throw new NumberFormatException("Only numerical states can be used as indexes in SUM");
		}
		
		//DEFINISCO NUOVA MULTISTATE CON SOLO I RANGE INDICATI SOPRA E STAMPO EXPANDED NELLA SOMMA
		MultistateSpecies ms = (MultistateSpecies) this.speciesDB.getSpecies(multistate_species_name);
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
		
		MultistateSpecies reduced = new MultistateSpecies(this,complete_string);
		return reduced;
	}

	static int testcount = 0;
	
	
	public String parseExpressionGlobalQ(String expression, boolean conc, boolean expressionInSpecies, boolean isInitialExpression) throws Exception {
		if(expression.length() == 0) return new String(); 
		
		
		this.copasiDataModel.getModel().compile();
		//this.copasiDataModel.getModel().updateInitialValues(changedObjects);
		 
		CModel model = this.copasiDataModel.getModel();
		
		InputStream is = new ByteArrayInputStream(expression.getBytes("UTF-8"));
		MR_Expression_Parser parser = new MR_Expression_Parser(is);
		CompleteExpression root = parser.CompleteExpression();
			CopasiVisitor vis = new CopasiVisitor(model,this,conc,isInitialExpression);
		try{
			root.accept(vis);
		} catch (Exception e) {
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
		}
		if(vis.getExceptions().size() == 0) {
			String copasiExpr  = vis.getCopasiExpression();
			return copasiExpr;
		} else {
			throw vis.getExceptions().get(0);
		}
		
		
		
	}

	
	
	
	
	private Vector<String> expand_element_weightFunctionCall(int row, MultistateSpecies reduced, String weightFunctionCall, boolean conc) throws Exception {
		Vector<String> ret = new Vector<String>();
		if(weightFunctionCall.length() == 0) return ret;
		Vector<Species> single_sp = reduced.getExpandedSpecies(this);
		
	    Vector paramMapping = (Vector) funDB.get_mappings_weight_globalQ_withSUM(row, reduced, weightFunctionCall);
		Function f = (Function)paramMapping.get(0);
		Vector<Integer> paramRoles =  f.getParametersTypes_CFunctionParameter();
		
		
	    CModel model = this.copasiDataModel.getModel();
	   
	    for(int i = 0; i < single_sp.size(); i++) {
	    	Vector parameterValues_couple = new Vector();
				
			for(int iii = 1, jjj = 0; iii < paramMapping.size(); iii=iii+2,jjj++) {
				String paramNameInFunction = (String) paramMapping.get(iii);
				String actualValue = (String) paramMapping.get(iii+1);
				switch(paramRoles.get(jjj)) {
					case CFunctionParameter.PARAMETER:     
						try{
							Double parValue = Double.parseDouble(actualValue);
							parameterValues_couple.add(parValue);
						} catch(NumberFormatException ex) { //the parameter is not a number but a globalQ
							if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) ex.printStackTrace();
							parameterValues_couple.add(actualValue);
						}
						break;
					case Constants.SITE_FOR_WEIGHT_IN_SUM:
						Species single = single_sp.get(i);
						String val = single.getValueOfSite(actualValue);
						try{
							Double siteValue = Double.parseDouble(val);
							parameterValues_couple.add(val);
						} catch(NumberFormatException ex) { //the parameter is not a number but a globalQ
							if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) ex.printStackTrace();
							throw new Exception("ONLY NUMERICAL SITES CAN BE USED IN WEIGHT FUNCTIONS");
						}
						break;
					default: throw new Exception("WRONG PARAMETER TYPES IN SUM!!!!!");
				}
				
				
			}
			String final_expression = parseExpressionGlobalQ(f.getExpandedEquation(parameterValues_couple), conc, false,false);
					
			ret.add(final_expression);
			
		}
		return ret;
	}

	

	private Vector<String> parseExpressionAssignment(Vector<String> expression, int expansionActionWithVolume) throws Exception {
		 this.copasiDataModel.getModel().compile();
		// this.copasiDataModel.getModel().updateInitialValues(changedObjects);
		 
		Vector<String> target_assignment = new Vector<String>();
		Vector<Vector<String>> originalValue_copasiTerm = new Vector<Vector<String>>();
		
		Vector<CCompartment> compartment_modified = new Vector<CCompartment>();
		
		for(int i = 0; i < expression.size(); i++) {

			StringTokenizer st = new StringTokenizer(expression.get(i), "=", false);
			
			String target = st.nextToken().trim();
			String assignment = st.nextToken().trim(); 
			
			CModel model = this.copasiDataModel.getModel();
			Vector<String> couple = new Vector<String>();
			
	   		if(CellParsers.isMultistateSpeciesName(target)) { //is a species and is multistate
			    			//should be a single state... no : or , allowed...
			    	if(target.contains(":") || target.contains(",")) throw new Exception("Reference of multistate species in expression can only refer to a single state");
			    	MultistateSpecies m = new MultistateSpecies(this,target);
			    	String complete_name = m.printCompleteDefinition();
			    		
			    	//int index_metab = this.findMetabolite(complete_name,null);
			    	int index_metab = -1;
			    	
			    	String cmp = CellParsers.extractCompartmentLabel(complete_name);
					if(cmp.length() > 0 ) {
						String justName = CellParsers.extractMultistateName(complete_name); //extractName separates just the multiplicity
						index_metab = this.findMetabolite(justName, cmp);
					} else {
					
						index_metab = this.findMetabolite(complete_name,null);
					}
					
					CMetab metab = model.getMetabolite(index_metab);
							
					String element_copasiTerm = metab.getKey();
					//metab.getObject(new CCopasiObjectName("Reference=Concentration")).getCN().getString();
					//element_copasiTerm="<"+element_copasiTerm+">";
					couple.add(target);
				    couple.add(element_copasiTerm);
				    originalValue_copasiTerm.add(couple);
			   } else {
				   //int index = this.findMetabolite(target,null);
				   int index = -1;
			    	String cmp = CellParsers.extractCompartmentLabel(target);
					if(cmp.length() > 0 ) {
						String justName = CellParsers.extractMultistateName(target); //extractName separates just the multiplicity
						index = this.findMetabolite(justName, cmp);
					} else {
						
						index = this.findMetabolite(target,null);
					}
					String element_copasiTerm = new String();
					if(index!= -1) { //species
							CMetab metab = model.getMetabolite(index);
							element_copasiTerm = metab.getKey();//metab.getObject(new CCopasiObjectName("Reference=Concentration")).getCN().getString();
					} else { 
							index = this.findGlobalQ(target);
							if(index!= -1) { //parameter
								CModelValue m = model.getModelValue(index);
								element_copasiTerm = m.getKey();//m.getObject(new CCopasiObjectName("Reference=Value")).getCN().getString();
							} else { //compartment
								index = this.findCompartment(target);
								CCompartment comp = model.getCompartment(index);
								compartment_modified.add(comp);
								element_copasiTerm = comp.getKey();//comp.getObject(new CCopasiObjectName("Reference=Volume")).getCN().getString();
							}
					}
					//element_copasiTerm="<"+element_copasiTerm+">";
					couple.add(target);
					couple.add(element_copasiTerm);
					originalValue_copasiTerm.add(couple);
				}
	   		
	   			String final_target = new String();
			
				for(int ii = 0; ii < originalValue_copasiTerm.size(); ii++ ) {
					Vector<String> entry = originalValue_copasiTerm.get(ii);
					String newValue = entry.get(1);
					target = target.replace(entry.get(0), newValue);
					
					int end = target.indexOf(newValue)+newValue.length();
						
					final_target += target.substring(0,end);
					if(end != target.length()) target = target.substring(end);
				}
				
				target_assignment.add(final_target);
				target_assignment.add(parseExpressionGlobalQ(assignment, MainGui.exportConcentration, false,false)); 
				// always concentration because the events hold the particle number automatically
				originalValue_copasiTerm.clear();
		}
		//---CONTROLLARE SE C'E' EXPANSION... SE SONO FALSI LASCIARE SOLO LE AZIONI CHE CI SONO
		
		if(expansionActionWithVolume!= -1) {
			for(int i = 0; i < compartment_modified.size(); i++) {
				CCompartment comp = compartment_modified.get(i);
				MetabVectorNS species = comp.getMetabolites();
				for(int j = 0; j < species.size(); j++) {
					org.COPASI.CCopasiObject sp = (org.COPASI.CCopasiObject) species.get(j);
					CCopasiObjectName objectName = null;
					if(expansionActionWithVolume == MR_Expression_ParserConstants.EXTENSION_CONC) { 
						objectName = new CCopasiObjectName("Reference=Concentration");
					}
					else { 
						objectName = new CCopasiObjectName("Reference=ParticleNumber"); 
						}
					String recalculate_conc = "<"+sp.getObject(objectName).getCN().getString()+">";
					target_assignment.add(sp.getKey());
					target_assignment.add(recalculate_conc);
				}
			}
		}
		return target_assignment;
			
	}
	
	private String buildCopasiExpression(String expression, boolean expressionInSpecies, boolean isInitialExpression) throws Exception {
		return parseExpressionGlobalQ(expression, MainGui.exportConcentration, expressionInSpecies,isInitialExpression); 
	}
	
	private Vector<String> buildCopasiExpressionAssignment(Vector<String> expression, int expansionActionWithVolume) throws Exception {
		return parseExpressionAssignment(expression, expansionActionWithVolume);
	}

	
	
	public Vector expandReaction(Vector metabolites, int row) throws Exception {
		Vector ret = new Vector();
	//	PROBLEMA IN QUESTOOOOOOOOOOOOOOOOOOOOOO METODO
	//	SBF(p{0};b{free}) + Whi5(p{0:2};b{free}) -> SBF(b{bound}) + Whi5(b{bound})
	//	REAZIONE 52
		Vector subs = (Vector)metabolites.get(0);
	    Vector prod =(Vector)metabolites.get(1);
		Vector mod = (Vector)metabolites.get(2);
		
		Vector multistate_reactants_expanded = new Vector();
		Vector non_multistate_reactants = new Vector();
		Vector multistate_reactants_single_states = new Vector();
		
		for(int i = 0; i < subs.size(); i++) {
			String species = (String) subs.get(i);
			if(CellParsers.isMultistateSpeciesName(species)) {
				Vector current_expanded = getExpandedStatesReactant(species);
				if(current_expanded.size() == 1) { 
					//there is a multistate reactant but with no ranges, so it's like a single species
					//except that it can still change it's state!! 
					non_multistate_reactants.add(species);
					multistate_reactants_single_states.addAll(current_expanded);
				} else {
					multistate_reactants_expanded.addAll(current_expanded);
				}
			} else {
				non_multistate_reactants.add(species);
			}
		}
		
		
	
		
		
	
		for(int i = 0; i < multistate_reactants_expanded.size(); i++) {
			Vector subs_exp = new Vector();
			subs_exp.addAll(non_multistate_reactants);
			Species sp = ((Species)multistate_reactants_expanded.get(i));
			subs_exp.add(sp.getDisplayedName());
			
			Vector single_reaction = new Vector();
			single_reaction.add(subs_exp);
			
			Vector prod_expanded = new Vector();
			for(int j = 0; j < prod.size(); j++) {
				 String pr = (String) prod.get(j);
				 InputStream is = new ByteArrayInputStream(pr.getBytes("UTF-8"));
				 MR_MultistateSpecies_Parser react = new MR_MultistateSpecies_Parser(is);
				 CompleteMultistateSpecies_Operator start = react.CompleteMultistateSpecies_Operator();
				 MultistateSpeciesVisitor v = new MultistateSpeciesVisitor(this,sp,multistate_reactants_single_states);
				 start.accept(v);
				 String exp = v.getProductExpansion();
				 if(exp != null && v.getExceptions().size() == 0) { prod_expanded.add(exp);}
				 else {
						DebugMessage dm = new DebugMessage();
						dm.setOrigin_table(Constants.TitlesTabs.REACTIONS.description);
					    dm.setOrigin_col(Constants.ReactionsColumns.REACTION.index);
					    dm.setOrigin_row(row+1);
						dm.setProblem("Undefined multistate state: "+sp.getDisplayedName() + " -> ??"+ pr+"??");
						dm.setPriority(DebugConstants.PriorityType.MISSING.priorityCode);
						MainGui.addDebugMessage_ifNotPresent(dm);
						MainGui.updateDebugTab();						
					}
				
				
			}
			
				
			
			
			single_reaction.add(prod_expanded);
			single_reaction.add(mod);
			
			if(prod_expanded.size() == prod.size()) ret.add(single_reaction);
		}
		
	
		
		if(multistate_reactants_expanded.size() == 0) {
			Vector single_reaction = new Vector();
			single_reaction.add(non_multistate_reactants);
			single_reaction.add(prod);
			single_reaction.add(mod);
			ret.add(single_reaction);
		}
		
		return ret;
	}
	

	private Vector getExpandedStatesReactant(String species) throws Exception {
	/*	Vector<Species> ret = new Vector<Species>();
		
		
		 try {
			 ret = existing.getExpandedSpecies_Minimum(this,species);
		
		 
		 } catch(Exception ex){
				throw new ParseException("Model yet not complete. Element "+species+" not found");
			}
		
			return ret;*/
		Vector<Species> ret = new Vector<Species>();
		MultistateSpecies temp = new MultistateSpecies(this,species,true);
		
		 InputStream is = new ByteArrayInputStream(species.getBytes());
		 MR_MultistateSpecies_Parser react = new MR_MultistateSpecies_Parser(is);
		 CompleteMultistateSpecies start = react.CompleteMultistateSpecies();
		 MultistateSpeciesVisitor v = new MultistateSpeciesVisitor(this);
	 	 start.accept(v);
	 	 Species existingSp = this.getSpecies(v.getSpeciesName());
	 	 if(existingSp instanceof MultistateSpecies)  {
	 		 MultistateSpecies existingMulti = (MultistateSpecies) existingSp;
			temp.mergeStatesWith_Minimum(existingMulti);
		 } else { 
			 //is multistate because of just compartment so the "temp" should be ok...
		 }
		 
		return temp.getExpandedSpecies(this);

	}

	
	
	/*private void checkSimilarityName(String name, Integer nrow) {
		double level_similarity = 1.0;
	
		Vector<String> names = this.speciesDB.getAllNames();
		
       	for(int i = 0; i < names.size(); i++) {
       		String current = names.get(i);
       	
       		if(debugTab.SimilarityStrings.damlev(current, name)!= 0 &&
       		   debugTab.SimilarityStrings.damlev(current, name) <= level_similarity) {
       			DebugMessage dm = new DebugMessage();
       			//dm.setOrigin_cause("Similarity strings <= "+level_similarity);
       			dm.setOrigin_table(Constants.TitlesTabs.SPECIES.description);
       			dm.setProblem("Species "+ name + " and " + current + " have a degree of similarity lower than "+level_similarity+"\n. Two species have been added but maybe a misstype has occurred in the definition of the two species.");
       			dm.setPriority(DebugConstants.PriorityType.SIMILARITY.priorityCode);
       			dm.setOrigin_col(1);
       			dm.setOrigin_row(nrow);
       			//MainGui.debugMessages.add(dm);
       		}
       	}
		
	}*/
	
	
	public void clearCopasiDataModel() {
		
		if(CCopasiRootContainer.getDatamodelList().size()==0) {
			copasiDataModel = CCopasiRootContainer.addDatamodel();
			copasiDataModelSBML_ID = new String(copasiDataModel.getModel().getSBMLId());
		}
		
		loadCopasiDataModel_fromSBMLid(copasiDataModelSBML_ID);
		

	
		 
	}
	
	public void setUnits(int unitTime, int unitVolume, int unitQuantity) {
		if(copasiDataModel!=null) {
			CModel model = copasiDataModel.getModel(); 
			model.setTimeUnit(unitTime);
			model.setVolumeUnit(unitVolume);
			model.setQuantityUnit(unitQuantity);
		}
		MainGui.timeUnit = unitTime;
		MainGui.volumeUnit = unitVolume;
		MainGui.quantityUnit = unitQuantity;
		 
	}
	
	
	
	public void clear() {
		//if(!clearJustTables) 
		clearCopasiDataModel();
		speciesDB = new SpeciesDB(this);
		globalqDB = new GlobalQDB(this);
		compDB = new CompartmentsDB(this);
		eventsDB = new EventsDB();
		funDB = new FunctionsDB(this);
		allNamedElements.clear();
	}
	
	public void loadCPS(File f) {
		
		if(CCopasiRootContainer.getDatamodelList().size() ==0) {
			copasiDataModel = CCopasiRootContainer.addDatamodel();
		
			copasiDataModelSBML_ID = new String(copasiDataModel.getModel().getSBMLId());
		}
		else {
			loadCopasiDataModel_fromSBMLid(copasiDataModelSBML_ID);
		}
		
	     try{
	    	 copasiDataModel.loadModel(f.getAbsolutePath());
	     }
	     catch (java.lang.Exception ex)
       {
           System.err.println("Error while loading the model from file named \"" + f.getAbsolutePath() + "\".");
           if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES)    ex.printStackTrace();
           return;
       }
	   
	 }
	
	
	private void loadCopasiDataModel_fromSBMLid(String copasiDataModelSBML_ID) {
		if(copasiDataModelSBML_ID== null){
			return;
		}
		 for (long s = 0; s < CCopasiRootContainer.getDatamodelList().size(); s++) 
		   {
			 
			 copasiDataModel = CCopasiRootContainer.get(s); //CCopasiRootContainer.getDatamodel(s);// 
			 CModel model = copasiDataModel.getModel();
			 if (model.getSBMLId().compareTo(copasiDataModelSBML_ID) == 0)	 {
				 break;
			 }
		   }
	}

	public CModel getCurrentCModel() {
		if(copasiDataModel == null) return null;
		return copasiDataModel.getModel();
	}
	
	
	public void modifySBMLid(String newSBML_ID) {
		copasiDataModelSBML_ID = new String(newSBML_ID);
	}
	
	
	public void createNewModel_withSBMLID(String newSBML_ID) {
		copasiDataModelSBML_ID = new String(newSBML_ID);
		copasiDataModel = CCopasiRootContainer.addDatamodel();
		copasiDataModel.getModel().setSBMLId(copasiDataModelSBML_ID);
		copasiDataModel.getModel().setTimeUnit(CModel.s);
		copasiDataModel.getModel().setVolumeUnit(CModel.fl);
		copasiDataModel.getModel().setQuantityUnit(CModel.number);
	}
	
	
	public void loadCPS(String cps_SBML_ID) {
		
		copasiDataModelSBML_ID = new String(cps_SBML_ID);
		
		loadCopasiDataModel_fromSBMLid(copasiDataModelSBML_ID);
	
    }
	
	
	public void loadSBML(File f) {
	
		if(CCopasiRootContainer.getDatamodelList().size() ==0) {
			copasiDataModel = CCopasiRootContainer.addDatamodel();
			copasiDataModelSBML_ID = new String(copasiDataModel.getModel().getSBMLId());
		}
		else {
			loadCopasiDataModel_fromSBMLid(copasiDataModelSBML_ID);
		}
		
		
		try{
	    	 copasiDataModel.importSBML(f.getAbsolutePath());
	     }
	     catch (java.lang.Exception ex)
      {
          System.err.println("Error while loading the model from file named \"" + f.getAbsolutePath() + "\".");
          if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES)           ex.printStackTrace();
          return;
      }
		
	}
	
	
	public Vector loadGlobalQTable() {
		if(copasiDataModel == null) return new Vector();
		CModel model = copasiDataModel.getModel();
		if(model == null) return  new Vector();
		
		Vector rows = new Vector();
		    
        int iMax = (int)model.getModelValues().size();
        
        for (int i = 0;i < iMax;i++)
        {
        	//System.out.println("global q "+i + " of "+iMax);
			//System.out.flush();
        	Vector row = new Vector();
    		CModelValue val = model.getModelValue(i);
            
    		String newName = CellParsers.cleanName(val.getObjectName());
            row.add(newName);
            if(newName!= val.getObjectName()) val.setObjectName(newName);//SAVE SOMEWHERE THE OLD ONE!!
//------------------------------------------------------------------
            row.add(val.getInitialValue());
//------------------------------------------------------------------
            row.add(Constants.GlobalQType.getDescriptionFromCopasiType(val.getStatus()));
//------------------------------------------------------------------
            if(		val.getStatus() == CModelValue.ASSIGNMENT || 
            		val.getStatus() == CModelValue.ODE) {
                  	row.add(CellParsers.cleanMathematicalExpression(this.buildMRExpression_fromCopasiExpr(val.getExpression())));
            } else row.add("");
//------------------------------------------------------------------
            row.add(val.getNotes());
           rows.add(row);
        }
        
        return rows;
		
	}
	

	
	public Vector loadEventsTable() {
		if(copasiDataModel == null) return new Vector();
		CModel model = copasiDataModel.getModel();
		if(model == null) return  new Vector();
		
		Vector rows = new Vector();
		    
        int iMax = (int)model.getEvents().size();
        
        
        for (int i=0; i<iMax; i++)
        {
        	Vector row = new Vector();
        	CEvent val = model.getEvent(i);
    		
            row.add(val.getObjectName());
//------------------------------------------------------------------
            row.add(this.buildMRExpression_fromCopasiExpr(val.getTriggerExpression()));
//------------------------------------------------------------------
            row.add(this.buildMRAssigments_fromCopasiExpr(val));
//------------------------------------------------------------------
            row.add(this.buildMRExpression_fromCopasiExpr(val.getDelayExpression()));
//------------------------------------------------------------------
            row.add(new Boolean(!val.getDelayAssignment()).toString()); 
//------------------------------------------------------------------
            rows.add(row);
        }
        
        return rows;
		
	}

	
	public Vector loadSpeciesTable() {
		if(copasiDataModel == null) return new Vector();
		CModel model = copasiDataModel.getModel();
		if(model == null) return  new Vector();
		MainGui.loadedExisting = true;
		MainGui.timeUnit = model.getTimeUnitEnum();
		MainGui.volumeUnit = model.getVolumeUnitEnum();
		MainGui.quantityUnit = model.getQuantityUnitEnum();
		MainGui.modelName = model.getObjectName();
		
		
		
		model.setTimeUnit(Constants.UnitTypeTime.DIMENSIONLESS_TIME.copasiType);
		model.setVolumeUnit(Constants.UnitTypeVolume.DIMENSIONLESS_VOL.copasiType);
		model.setQuantityUnit(Constants.UnitTypeQuantity.DIMENSIONLESS_QUANTITY.copasiType);
		
		Vector rows = new Vector();
		HashMap<Long, String> SBML_IDS = new HashMap<Long, String>();
		
        int iMax = (int)model.getMetabolites().size();
        HashMap<String, Integer> namesCollected_index = new HashMap<String, Integer>();
        for (int i = 0;i < iMax;i++)
        {
        	//System.out.println("species "+i + " of "+iMax);
			//System.out.flush();
			
        	Vector row = new Vector();
            CMetab metab = model.getMetabolite(i);

            String cleanName = CellParsers.cleanName(metab.getObjectName(),true);
            
            if(!namesCollected_index.containsKey(cleanName)) {
                if(metab.getMiriamAnnotation().compareTo(Constants.MULTISTATE_ANNOTATION) == 0) {
	            	System.out.println(metab.getObjectName() +" is a real multistate species from MultiReMI");
	            };
	            
	            row.add(cleanName);
	            SBML_IDS.put((long) i+1,metab.getSBMLId());
	      
	            if(MainGui.quantityIsConc) row.add(new Double(metab.getInitialConcentration()).toString());
	           else row.add(new Double(metab.getInitialValue()).toString());
	           
	           if(metab.getInitialExpression().trim().length()>0) {
	        	   row.set(row.size()-1, buildMRExpression_fromCopasiExpr(metab.getInitialExpression()));
	           }
	            
	            row.add(Constants.SpeciesType.getDescriptionFromCopasiType(metab.getStatus()));
	            
	            
	            String cleanNameComp = CellParsers.cleanName(metab.getCompartment().getObjectName(),false);
	            row.add(cleanNameComp);
	            
	          
	            
	            if(metab.getStatus() == CMetab.ASSIGNMENT || metab.getStatus() == CMetab.ODE) {
	            	row.add(CellParsers.cleanMathematicalExpression(this.buildMRExpression_fromCopasiExpr(metab.getExpression())));
	            } else row.add("");
	            
	            
	       	
	            row.add(metab.getNotes());
	            namesCollected_index.put(cleanName, i);
	           rows.add(row);
        	} else { //the species already exist in another compartment, I have to add the compartment and other data to the same row as the previously added one
        		Integer index = namesCollected_index.get(cleanName);
        		row = (Vector) rows.get(index);
        		
        		//SBML_IDS.put((long) i+1,metab.getSBMLId());
        		//if(MainGui.quantityIsConc) row.add(new Double(metab.getInitialConcentration()).toString());
 	            //else row.add(new Double(metab.getInitialValue()).toString());
 	            //if(metab.getInitialExpression().trim().length()>0) {
 	        	 //  row.set(row.size()-1, buildMRExpression_fromCopasiExpr(metab.getInitialExpression()));
 	           	//}
 	            //row.add(Constants.SpeciesType.getDescriptionFromCopasiType(metab.getStatus()));
 	            
 	            String cleanNameComp = CellParsers.cleanName(metab.getCompartment().getObjectName(),false);
 	            String oldComp = (String) row.get(Constants.SpeciesColumns.COMPARTMENT.index-1);
 	            
 	            row.set(Constants.SpeciesColumns.COMPARTMENT.index-1, oldComp+", "+cleanNameComp);
 	            
 	           // if(metab.getStatus() == CMetab.ASSIGNMENT || metab.getStatus() == CMetab.ODE) {
 	          //  	row.add(CellParsers.cleanMathematicalExpression(this.buildMRExpression_fromCopasiExpr(metab.getExpression())));
 	          //  } else row.add("");
 	            rows.set(index, row);
        	}
        }
        
        Vector ret = new Vector();
        ret.add(rows);
        ret.add(SBML_IDS);
        return ret;
	}
	
	//LAAAAAAAAAAAAAAAAAAAST ENTRY IS A VECTOR CONTAINING THE CFUNCTION PREDEFINED THAT NEED TO BE LOADED
	public Vector loadReactionTable() throws Exception {
		if(copasiDataModel == null) return new Vector();
		CModel model = copasiDataModel.getModel();
		if(model == null) return  new Vector();
				
		Vector rows = new Vector();
		Vector row = new Vector();
        Vector<CFunction> predefined_to_be_loaded = new Vector<CFunction>();
        
      
		int iMax = (int)model.getReactions().size();
        
        for (int i = 0;i < iMax;i++)
        {
        	//System.out.println("reaction "+i + " of "+iMax);
			//System.out.flush();
            CReaction reaction = model.getReaction(i);
            if(reaction.isReversible()) {
            	System.out.println("!!!!!!-----REVERSIBLE REACTION NOT IMPORTED-----");
            	System.out.println(reaction.getObjectName());
               	System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
               	continue;
            }
            
            
            row.add(reaction.getObjectName());
//------------------------------------------------------------------
            CChemEq eq = reaction.getChemEq();

            //--undefined reaction
            if(eq.getSubstrates().size() == 0 && eq.getProducts().size() == 0) {	 
            	rows.add(new Vector(row));
            	row.clear(); continue;    
            }
            //--  

            String r_string = new String();
            long nsub = eq.getSubstrates().size();
            for(int ii = 0; ii < nsub; ii++) {
            	CChemEqElement sub = eq.getSubstrate(ii);
            	Double mult = sub.getMultiplicity();
            	if(mult!=1.0) {r_string += mult.doubleValue() + " * ";}
            	String cleanName = CellParsers.cleanName(sub.getMetabolite().getObjectName(),true);
            	if(isSpeciesWithMultipleCompartment(cleanName)) {
            		cleanName = addCompartmentLabel(cleanName, sub.getMetabolite().getCompartment().getObjectName());
            	}
            	r_string += cleanName  + " + ";
            }
            if(r_string.length() > 3) r_string = r_string.substring(0, r_string.length()-3);

            r_string += " -> ";

            long nprod = eq.getProducts().size();
            boolean products = false;
            for(int ii = 0; ii < nprod; ii++) {
            	products = true;
            	CChemEqElement sp = eq.getProduct(ii);
            	Double mult = sp.getMultiplicity();
            	if(mult!=1.0) {r_string += mult.doubleValue() + " * ";}
            	String cleanName = CellParsers.cleanName(sp.getMetabolite().getObjectName(),true);
            	if(isSpeciesWithMultipleCompartment(cleanName)) {
            		cleanName = addCompartmentLabel(cleanName, sp.getMetabolite().getCompartment().getObjectName());
            	}
            	r_string += cleanName + " + ";
            }
            if(r_string.length() > 3 && products== true) r_string = r_string.substring(0, r_string.length()-3);


            long nmod = eq.getModifiers().size();
            boolean modifiers = false;
            if(nmod != 0) {
            	r_string += "; ";
            }
            for(int ii = 0; ii < nmod; ii++) {
            	modifiers = true;
            	CChemEqElement sp = eq.getModifier(ii);
            	String cleanName = CellParsers.cleanName(sp.getMetabolite().getObjectName(),true);
            	if(isSpeciesWithMultipleCompartment(cleanName)) {
            		cleanName = addCompartmentLabel(cleanName, sp.getMetabolite().getCompartment().getObjectName());
            	}
            	r_string +=  cleanName + " ";
            }
            if(r_string.length() > 3 && modifiers== true) r_string = r_string.substring(0, r_string.length()-1);

            row.add(r_string.trim());
            if(reaction.getFunction().getObjectName().contains("undefined")) { 
            	rows.add(new Vector(row));
            	row.clear();
            	continue;
            }
//------------------------------------------------------------------
           row.add(Constants.ReactionType.getDescriptionFromCopasiType(reaction.getFunction().getType()));
//------------------------------------------------------------------
           row.add(buildMRKineticLaw_fromCopasiFunction(reaction));
           
         
          
           if(reaction.getFunction().getType() == CFunction.PreDefined ||
        		   reaction.getFunction().getObjectName().contains(Constants.DEFAULT_SUFFIX_COPASI_BACKWARD_REACTION) ||
        		   reaction.getFunction().getObjectName().contains(Constants.DEFAULT_SUFFIX_COPASI_FORWARD_REACTION)) {
        	   predefined_to_be_loaded.add(reaction.getFunction());
           }
           row.add("");
           row.add(reaction.getNotes());
                      
           rows.add(new Vector(row));
            
           row.clear();
        }
        
       
        compressReactions();
        
        rows.add(predefined_to_be_loaded);
        return rows;
 	}
	
	private String addCompartmentLabel(String cleanName, String objectName) {
		return CellParsers.addCompartmentLabel(cleanName, objectName);
	}

	private boolean isSpeciesWithMultipleCompartment(String cleanName) {
		return  this.speciesDB.isSpeciesWithMultipleCompartment(cleanName);
	}

	private String buildMRKineticLaw_fromCopasiFunction(CReaction reaction) throws Exception {
			return buildMRKineticLaw_fromCopasiFunction(reaction, false);
	}
	
	private String buildMRKineticLaw_fromCopasiFunction(CReaction reaction, boolean isBackward) throws Exception {
		CModel model = copasiDataModel.getModel();
		String ret = new String();
		if(reaction.getFunction().getType() == CFunction.MassAction) {
			int index = findGlobalQ_key(reaction.getParameterMapping(reaction.getParameters().getName(0)).get(0));
			if(index != -1) {
				CModelValue val = model.getModelValue(index);
				ret +=CellParsers.cleanName(val.getObjectName());
			} else {
				ret += reaction.getParameters().getParameter(0).getDblValue();
			}
			
			return ret;
		}
		
		
		
		ret += CellParsers.cleanName(reaction.getFunction().getObjectName());
		if(isBackward) ret += Constants.DEFAULT_SUFFIX_BACKWARD_REACTION;
		ret += "(";
		VectorOfStringVectors param = reaction.getParameterMappings();
		CCopasiParameterGroup group = reaction.getParameters(); //local constants
		
		for(int i = 0; i < param.size(); i++) {
			String currentKey = param.get(i).get(0);
			CCopasiObject val = null;
			if(currentKey.contains("Value")) {
				int index = findGlobalQ_key(currentKey);
				if(index != -1) {
					val = model.getModelValue(index);
					ret += CellParsers.cleanName(val.getObjectName()) + ",";
				} else {
					ret += reaction.getParameters().getParameter(i).getDblValue() + ",";
				}
			} else if(currentKey.contains("Metabolite")){
				int index = findMetabolite_key(currentKey);
				val = model.getMetabolite(index);
				String complete_name_species = val.getObjectName();
				if(CellParsers.isMultistateSpeciesName(complete_name_species)) {
					//complete_name_species = complete_name_species.substring(0,complete_name_species.indexOf("("));
					complete_name_species = extractName(complete_name_species);
				}
				String element = CellParsers.cleanName(complete_name_species);
				if(isSpeciesWithMultipleCompartment(element)) {
					element = addCompartmentLabel(element, model.getMetabolite(index).getCompartment().getObjectName());
            	}
				ret +=  element + ",";
			} else if(currentKey.contains("Compartment")) {
				int index = findCompartment_key(currentKey);
				val = model.getCompartment(index);
				ret += CellParsers.cleanName(val.getObjectName()) + ",";
			} else if(currentKey.contains("Parameter")) { 
				int index = findLocalParameter_key(currentKey, group);
				CCopasiParameter parameter = group.getParameter(index);
				ret += parameter.getDblValue() + ",";
			}else if(currentKey.contains("Model")) { //model time
				ret += MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstantsNOQUOTES.CONST_MODEL_TIME) + ",";
			}
			else {
				System.out.println("PARAM NOT IMPORTED: "+currentKey);
			}
			
			
		}
		if(param.size()>0) ret = ret.substring(0, ret.length()-1);
		ret += ")";
		return ret;
	}

		
	public Vector loadCompartmentsTable() {
		if(copasiDataModel == null) return new Vector();
		CModel model = copasiDataModel.getModel();
		if(model == null) return  new Vector();
				
		
		Vector rows = new Vector();
		
		int iMax = (int)model.getCompartments().size();
        
        for (int i = 0;i < iMax;i++)
        {
        	Vector row = new Vector();
         	CCompartment val = model.getCompartment(i);
         	String newName = CellParsers.cleanName(val.getObjectName());
            if(newName!= val.getObjectName()) val.setObjectName(newName);
            
            row.add(newName);
//------------------------------------------------------------------
           row.add(Constants.CompartmentsType.getDescriptionFromCopasiType(val.getStatus()));
//------------------------------------------------------------------
            row.add(new String(Double.toString(val.getInitialValue())));
//------------------------------------------------------------------
            row.add(buildMRExpression_fromCopasiExpr(val.getExpression()));
//------------------------------------------------------------------
            row.add(val.getNotes());
            rows.add(row);
        }
        
       return rows;
		
	}
	
	
	
	private String buildMRExpression_fromCopasiExpr(String expression) {
		//example = <CN=Root,Model=New Model,Vector=Values[alpha],Reference=Value>*<CN=Root,Model=New Model,Vector=Compartments[cell],Reference=Volume>
		
		String ret = new String();
		if(expression.trim().length() ==0) return ret;
		
		expression = expression.replace(" < ", " lt ");
		expression = expression.replace(" > ", " gt ");
		expression = expression.replace(" <= ", " le ");
		expression = expression.replace(" >= ", " ge ");
		expression = expression.replace(" == ", " eq ");
		expression = expression.replace(" && ", " and ");
		expression = expression.replace(" || ", " or ");
		 
			
		StringTokenizer st_elem = new StringTokenizer(expression, "<>");
		while(st_elem.hasMoreTokens()) {
			String elem = st_elem.nextToken();
			elem = elem.replace("\\,", "\\*MY*COMMA");
			int whichElement = -1;
			if(elem.contains("[")) {
				StringTokenizer st_vector = new StringTokenizer(elem,",");
				String real_elem = new String();
				String real_elem2 = new String();
				int open_br = 0;
				int closed_br = 0;
				boolean first = true;
				
				while(st_vector.hasMoreTokens()) {
					//for Metabolites there are 2 "Vector=[]": the first is the compartment the second the metabolite... 
					String sub_elem = st_vector.nextToken();
					//for Metabolites there are 2 "Vector=[]": the first is the compartment the second the metabolite... 
					if(sub_elem.contains("Vector") && !first) {
						real_elem2 = new String(sub_elem);
						real_elem2 = real_elem2.replace('\\', '\u00A3');
						real_elem2 = real_elem2.replaceAll("\u00A3", "");
						open_br = real_elem2.indexOf("[");
						closed_br = real_elem2.lastIndexOf("]");
						if(real_elem2.contains("Metabolite")) whichElement = Constants.TitlesTabs.SPECIES.index;
						real_elem2 = real_elem2.substring(open_br+1,closed_br);
						real_elem2 = real_elem2.replace("*MY*COMMA", ",");
						
						if(whichElement == Constants.TitlesTabs.SPECIES.index) real_elem2 = CellParsers.cleanName(real_elem2,true);
						else real_elem2 = CellParsers.cleanName(real_elem2);
						
						real_elem = addCompartmentLabel(real_elem2, real_elem);
					}
					
					if(sub_elem.contains("Vector") && first) {
						real_elem = new String(sub_elem);
						real_elem = real_elem.replace('\\', '\u00A3');
						real_elem = real_elem.replaceAll("\u00A3", "");
						open_br = real_elem.indexOf("[");
						closed_br = real_elem.lastIndexOf("]");
						if(real_elem.contains("Metabolite")) whichElement = Constants.TitlesTabs.SPECIES.index;
						else if(real_elem.contains("Values")) whichElement = Constants.TitlesTabs.GLOBALQ.index;
						else if(real_elem.contains("Compartments")) whichElement = Constants.TitlesTabs.COMPARTMENTS.index;
						real_elem = real_elem.substring(open_br+1,closed_br);
						real_elem = real_elem.replace("*MY*COMMA", ",");
						if(whichElement == Constants.TitlesTabs.SPECIES.index) real_elem = CellParsers.cleanName(real_elem,true);
						else real_elem = CellParsers.cleanName(real_elem);
						first = false;
					}
				}
				
					
				st_vector = new StringTokenizer(elem,",");
				elem = elem.replace("\\,", "\\*MY*COMMA");
				String ref = new String();
				while(st_vector.hasMoreTokens()) {
					 ref = st_vector.nextToken();
					if(ref.contains("Reference")) {
							ref = ref.substring(ref.indexOf("=")+1);
							if(whichElement == Constants.TitlesTabs.SPECIES.index) {
								real_elem += MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstantsNOQUOTES.EXTENSION_SPECIES);
								if(ref.compareTo("Concentration")==0) real_elem +=  MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstantsNOQUOTES.EXTENSION_CONC);
								else if(ref.compareTo("ParticleNumber")==0) real_elem += MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstantsNOQUOTES.EXTENSION_PARTICLE);
								else if(ref.compareTo("Rate")==0) real_elem += MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstantsNOQUOTES.EXTENSION_RATE);
								else if(ref.compareTo("InitialConcentration")==0) real_elem += MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstantsNOQUOTES.EXTENSION_CONC)+MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstantsNOQUOTES.EXTENSION_INIT);
								else if(ref.compareTo("InitialParticleNumber")==0) real_elem += MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstantsNOQUOTES.EXTENSION_PARTICLE)+MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstantsNOQUOTES.EXTENSION_INIT);
							} else 	if(whichElement == Constants.TitlesTabs.GLOBALQ.index) {
								real_elem += MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstantsNOQUOTES.EXTENSION_GLOBALQ);
								if(ref.compareTo("Value")==0) real_elem += "";
								else if(ref.compareTo("InitialValue")==0) real_elem += MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstantsNOQUOTES.EXTENSION_INIT);
								else if(ref.compareTo("Rate")==0) real_elem += MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstantsNOQUOTES.EXTENSION_RATE);
							} else 	if(whichElement == Constants.TitlesTabs.COMPARTMENTS.index) {
								real_elem += MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstantsNOQUOTES.EXTENSION_COMPARTMENT);
								if(ref.compareTo("Volume")==0) real_elem += "";
								else if(ref.compareTo("InitialVolume")==0) real_elem += MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstantsNOQUOTES.EXTENSION_INIT);
								else if(ref.compareTo("Rate")==0) real_elem += MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstantsNOQUOTES.EXTENSION_RATE);
							}
					}		
				}
				real_elem = real_elem.replace("*MY*COMMA", ",");
				
				ret += real_elem;
				
				
		
			} else {
				if(elem.contains(Constants.COPASI_STRING_TIME)){
					ret+= MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstantsNOQUOTES.TIME);
				}
				else ret += elem; //operator or parenthesis
			}
		} 
		
		ret = ret.replace(" lt ", " < ");
		ret = ret.replace(" gt ", " > ");
		ret = ret.replace(" le ", " <= ");
		ret = ret.replace(" ge ", " >= ");
		ret = ret.replace(" eq ", " == ");
		ret = ret.replace(" and ", " && ");
		ret = ret.replace(" or ", " || ");
		
		
		try {
			ret = reprintExpression(ret, false);
		} catch (Exception e) {
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
		}
		
		return ret;
	}
	
	
	
	public String reprintExpression(String expr, boolean forceExpansion_elements) throws Exception {
		String ret = new String();
		try{
			
			InputStream is = new ByteArrayInputStream(expr.getBytes("UTF-8"));
			MR_Expression_Parser parser = new MR_Expression_Parser(is);
			CompleteExpression root = parser.CompleteExpression();
			MainGui.jListFunctionToCompact.setSelectionInterval(0, MainGui.listModel_FunctionToCompact.size()-1);
			
			ExpressionVisitor vis = new ExpressionVisitor(MainGui.jListFunctionToCompact.getSelectedValuesList(),this,forceExpansion_elements);
			
			root.accept(vis);
			if(vis.getExceptions().size() == 0) {
				ret  = vis.getExpression();
			} else {
				throw vis.getExceptions().get(0);
			}
		} catch (Exception e) {
			throw e;
		}
		return ret;
	}
	
	
	private String buildMRAssigments_fromCopasiExpr(CEvent event) {
		
		String ret = new String();
		CModel model = copasiDataModel.getModel();
		long assignSize = event.getAssignments().size();
		for(int i = 0; i < assignSize; i++) {
			CEventAssignment element = (CEventAssignment) event.getAssignment(i);
			try { //IT CAN BE SOMETHING DIFFERENT FROM A METAAAAAAAAAAAAAAAAAAAAAAAAAAAABOLITE
					//TO BE FIIIIIIIIIIIIIIIIIIIIIIIIIIIIXED
				
				int index = this.findMetabolite(element.getTargetKey(),null,true);
				
				if(index!= -1) { //species
		    		ret += CellParsers.cleanName(model.getMetabolite(index).getObjectName(),true);
		    		
		    	} else { 
						index = this.findGlobalQ(element.getTargetKey(),true);
						if(index!= -1) { //parameter
							ret += model.getModelValue(index).getObjectName();
						} else { //compartment
							index = this.findCompartment(element.getTargetKey(),true);
							ret += model.getCompartment(index).getObjectName();
						}
				}
			} catch (Exception e) {
				if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
			}
			ret += "="+this.buildMRExpression_fromCopasiExpr(element.getExpression());
			ret += "; ";
		}
		
		ret = ret.substring(0, ret.length()-2);
		return ret;
	}

	private void compressReactions() {
		return;
	}
	
	public void compressSpecies() throws Exception {
		this.speciesDB.compressSpecies();
		
		for(int i = 1; i < this.speciesDB.getSizeSpeciesDB(); i++) {
			Species sp = speciesDB.getSpecies(i);
			this.updateSpecies(i, sp.getDisplayedName(), 
									//sp.getInitialConcentration(), 
									//sp.getInitialAmount(), 
									sp.getInitialQuantity(), 
									(String)Constants.SpeciesType.getDescriptionFromCopasiType(sp.getType()),
									sp.getCompartment_listString(),
									sp.getExpression(), false, sp.getNotes(),true);
		}
	
	}
		
	public void removeSpecies(Vector species_default_for_dialog_window) throws Exception {
			this.speciesDB.removeSpecies(species_default_for_dialog_window);
	}

	public boolean updateGlobalQ(Integer nrow, String name, String value, String type, String expression, String notes) throws Exception {
		try{
			this.addGlobalQ(nrow,name, value, type, expression, notes);
		} catch(MySyntaxException ex) {
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) ex.printStackTrace();
		    DebugMessage dm = new DebugMessage();
			dm.setOrigin_table(Constants.TitlesTabs.GLOBALQ.description);
		    dm.setOrigin_col(ex.getColumn());
		    dm.setOrigin_row(nrow);
			dm.setProblem(ex.getMessage());
		    dm.setPriority(DebugConstants.PriorityType.PARSING.priorityCode);
			MainGui.addDebugMessage_ifNotPresent(dm);
			throw ex;
		}
		
		if(expression.compareTo(MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstantsNOQUOTES.NAN)) == 0) {
			  DebugMessage dm = new DebugMessage();
				dm.setOrigin_table(Constants.TitlesTabs.GLOBALQ.description);
			    dm.setOrigin_col(Constants.GlobalQColumns.EXPRESSION.index);
			    dm.setOrigin_row(nrow);
				dm.setProblem("NaN value - will be exported as (undefined) in the SBML/Copasi model");
			    dm.setPriority(DebugConstants.PriorityType.MINOR.priorityCode);
				MainGui.addDebugMessage_ifNotPresent(dm);
		} else {
			MainGui.clear_debugMessages_relatedWith_table_col_priority(Constants.TitlesTabs.GLOBALQ.description, Constants.GlobalQColumns.EXPRESSION.index, DebugConstants.PriorityType.MINOR.priorityCode);
		}
		
		
		if(name.compareTo(MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstantsNOQUOTES.NAN)) == 0) {
			  DebugMessage dm = new DebugMessage();
				dm.setOrigin_table(Constants.TitlesTabs.GLOBALQ.description);
			    dm.setOrigin_col(Constants.GlobalQColumns.VALUE.index);
			    dm.setOrigin_row(nrow);
				dm.setProblem("NaN value - will be exported as (undefined) in the SBML/Copasi model");
			    dm.setPriority(DebugConstants.PriorityType.MINOR.priorityCode);
				MainGui.addDebugMessage_ifNotPresent(dm);
		}else {
			MainGui.clear_debugMessages_relatedWith_table_col_priority(Constants.TitlesTabs.GLOBALQ.description, Constants.GlobalQColumns.VALUE.index, DebugConstants.PriorityType.MINOR.priorityCode);
		}
		
		
		return true;
	}

	public Vector<Species> getAllSpecies() {
		return new Vector(this.speciesDB.getAllSpecies());
	}

	
	public Vector<Vector> updateSpecies(Integer nrow, String name, String initialQ, String type, String compartment, String expression, boolean autoFill, String notes, boolean autoMergeSpecies) throws Exception{
		Exception exToThrow = null;
		boolean parseErrors = false;
		try{ 
			
			HashMap<String, String> entry_q = new HashMap<String, String>();
			entry_q.put(name,initialQ);
			this.speciesDB.addChangeSpecies(nrow,new String(),name,entry_q,Constants.SpeciesType.getCopasiTypeFromDescription(type),compartment,expression,false,notes,autoMergeSpecies,true);
		
			if(CellParsers.isMultistateSpeciesName_withUndefinedStates(name)) {
				   DebugMessage dm = new DebugMessage();
					dm.setOrigin_table(Constants.TitlesTabs.SPECIES.description);
				    dm.setOrigin_col(Constants.SpeciesColumns.NAME.index);
				    dm.setOrigin_row(nrow);
					dm.setProblem("Multistate species with undefined states");
				    dm.setPriority(DebugConstants.PriorityType.PARSING.priorityCode);
					MainGui.addDebugMessage_ifNotPresent(dm);
				}
			
		} catch(MyInconsistencyException ex) {
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) ex.printStackTrace();
			exToThrow = ex;
		}catch(MySyntaxException ex) {
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) ex.printStackTrace();
		    DebugMessage dm = new DebugMessage();
			dm.setOrigin_table(ex.getTable());
		    dm.setOrigin_col(ex.getColumn());
		    dm.setOrigin_row(nrow);
			dm.setProblem(ex.getMessage());
		    dm.setPriority(DebugConstants.PriorityType.PARSING.priorityCode);
			MainGui.addDebugMessage_ifNotPresent(dm);
			parseErrors = true;
			//MainGui.donotCleanDebugMessages = true;
			exToThrow = ex;
		}
		
		if(expression.compareTo(MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstantsNOQUOTES.NAN)) == 0) {
			  DebugMessage dm = new DebugMessage();
				dm.setOrigin_table(Constants.TitlesTabs.SPECIES.description);
			    dm.setOrigin_col(Constants.SpeciesColumns.EXPRESSION.index);
			    dm.setOrigin_row(nrow);
				dm.setProblem("NaN value - will be exported as (undefined) in the SBML/Copasi model");
			    dm.setPriority(DebugConstants.PriorityType.MINOR.priorityCode);
				MainGui.addDebugMessage_ifNotPresent(dm);
		} else {
			MainGui.clear_debugMessages_relatedWith_table_col_priority(Constants.TitlesTabs.SPECIES.description, Constants.SpeciesColumns.EXPRESSION.index, DebugConstants.PriorityType.MINOR.priorityCode);
		}
		
		if(initialQ.compareTo(MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstantsNOQUOTES.NAN)) == 0) {
			  DebugMessage dm = new DebugMessage();
				dm.setOrigin_table(Constants.TitlesTabs.SPECIES.description);
			    dm.setOrigin_col(Constants.SpeciesColumns.INITIAL_QUANTITY.index);
			    dm.setOrigin_row(nrow);
				dm.setProblem("NaN value - will be exported as (undefined) in the SBML/Copasi model");
			    dm.setPriority(DebugConstants.PriorityType.MINOR.priorityCode);
				MainGui.addDebugMessage_ifNotPresent(dm);
		}else {
			MainGui.clear_debugMessages_relatedWith_table_col_priority(Constants.TitlesTabs.SPECIES.description, Constants.SpeciesColumns.INITIAL_QUANTITY.index, DebugConstants.PriorityType.MINOR.priorityCode);
		}
		
		Vector rows = new Vector();
		
		Species sp = this.speciesDB.getSpecies(name);
		
		try{
			sp.setCompartment(this,compartment);
			sp.setInitialQuantity(this,initialQ);
			sp.setType(Constants.SpeciesType.getCopasiTypeFromDescription(type));
		
			//checkSimilarityName(name, nrow);
		} catch(MySyntaxException ex) { // something undefined in the initial quantity/expression of the compartment... but I have to move on with the species definition anyway
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) ex.printStackTrace();
		    DebugMessage dm = new DebugMessage();
			dm.setOrigin_table(ex.getTable());
		    dm.setOrigin_col(ex.getColumn());
		    dm.setOrigin_row(nrow);
			dm.setProblem(ex.getMessage());
		    dm.setPriority(DebugConstants.PriorityType.PARSING.priorityCode);
			MainGui.addDebugMessage_ifNotPresent(dm);

			//if(exToThrow!=null) throw exToThrow;
			return rows;
		}
		//if(exToThrow!=null) throw exToThrow;
		return rows;
	 }
	
	public Vector<String> getUndefinedSpeciesInReaction(Integer nrow, String reaction_string) {
		Vector ret = new Vector<String>();
		Vector metabolites = new Vector();
		try{ 
			
			metabolites = CellParsers.parseReaction(this,reaction_string,nrow-1);
		} catch(Exception ex) {
			return ret;
		}
		Vector subs = (Vector)metabolites.get(0);
		Vector prod =(Vector)metabolites.get(1);
		Vector mod = (Vector)metabolites.get(2);
		
		for(int i = 0; i < subs.size(); i++){
			String sp = (String) subs.get(i);
			
			sp = extractName(sp);
			Species s = speciesDB.getSpecies(sp);
			if(s==null) {
				if(CellParsers.isMultistateSpeciesName(sp)) {
					String autocompletedMultistate = generateAutocompleteMultistateName(sp);
					ret.add(autocompletedMultistate);
				} else {
					ret.add(sp);
				}
			}
		}
		
		for(int i = 0; i < prod.size(); i++){
			String sp = (String) prod.get(i);
			sp = extractName(sp);
			Species s = speciesDB.getSpecies(sp);
			 if(s==null) {
				if(CellParsers.isMultistateSpeciesName(sp)) {
					String autocompletedMultistate = generateAutocompleteMultistateName(sp);
					ret.add(autocompletedMultistate);
				} else {
					ret.add(sp);
				}
			}
		}
		for(int i = 0; i < mod.size(); i++){
			String sp = (String) mod.get(i);
			sp = extractName(sp);
			Species s = speciesDB.getSpecies(sp);
			if(s==null) {
				if(CellParsers.isMultistateSpeciesName(sp)) {
					String autocompletedMultistate = generateAutocompleteMultistateName(sp);
					ret.add(autocompletedMultistate);
				} else {
					ret.add(sp);
				}
			}
		}
		return ret;
	}
	
	
	private String generateAutocompleteMultistateName(String sp) {
		String autocompletedMultistate = new String();
		try {
			MultistateSpecies m = new MultistateSpecies(this, sp, true);
			Set sites = m.getSitesNames();
			autocompletedMultistate += m.getSpeciesName();
			autocompletedMultistate += MR_MultistateSpecies_ParserConstantsNOQUOTES.getTokenImage(MR_MultistateSpecies_ParserConstantsNOQUOTES.OPEN_R);
			Iterator itSites = sites.iterator();
			while(itSites.hasNext()) {
				String site = itSites.next().toString();
				autocompletedMultistate += site 
											+ MR_MultistateSpecies_ParserConstantsNOQUOTES.getTokenImage(MR_MultistateSpecies_ParserConstantsNOQUOTES.OPEN_C)
											+ "?"
											+ MR_MultistateSpecies_ParserConstantsNOQUOTES.getTokenImage(MR_MultistateSpecies_ParserConstantsNOQUOTES.CLOSED_C);
				autocompletedMultistate += MR_MultistateSpecies_ParserConstantsNOQUOTES.getTokenImage(MR_MultistateSpecies_ParserConstantsNOQUOTES.SITE_NAMES_SEPARATOR);
			}
			if(sites.size()>0) {
				autocompletedMultistate = autocompletedMultistate.substring(0,autocompletedMultistate.length()-1);
			}
			autocompletedMultistate += MR_MultistateSpecies_ParserConstantsNOQUOTES.getTokenImage(MR_MultistateSpecies_ParserConstantsNOQUOTES.CLOSED_R);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return autocompletedMultistate;
	}

	public Vector<Vector> updateReaction(Integer nrow,	String name, String reaction_string, String type, String equation, String exp,	String notes, boolean autocompleteWithDefaults, boolean actionInColumnName) throws Exception{
		Vector<Vector> table_rows = new Vector<Vector>();
		int prev_error_messages = MainGui.debugMessages.size();

		//MainGui.clear_debugMessages_relatedWith(nrow);
		MainGui.species_default_for_dialog_window.clear();

		boolean parseErrors = false;
		Vector metabolites = new Vector();
		try{ 
			
			metabolites = CellParsers.parseReaction(this,reaction_string,nrow-1);
		} catch(Exception ex) {
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) ex.printStackTrace();
			parseErrors = true;
			metabolites.add(new Vector());
			metabolites.add(new Vector());
			metabolites.add(new Vector());
		}

		

		if(parseErrors) return null;

		if (autocompleteWithDefaults && actionInColumnName) {
			Vector subs = (Vector)metabolites.get(0);
			Vector prod =(Vector)metabolites.get(1);
			Vector mod = (Vector)metabolites.get(2);

			Vector<Integer> indexes_row_species = new Vector<Integer>();

			for(int i = 0; i < subs.size(); i++) {
				String s = (String)subs.get(i);
				s = extractName(s);
				if(this.speciesDB.containsSpecies(s,true)) {continue;}
				String speciesName = new String();
				if(CellParsers.isMultistateSpeciesName(s)) {
					speciesName = generateAutocompleteMultistateName(s);
				} else {
					speciesName = s;
				}
				
				HashMap<String, String> entry_q = new HashMap<String, String>();
				entry_q.put(s,MainGui.species_defaultInitialValue);
				
				indexes_row_species.add(speciesDB.addChangeSpecies(-1,new String(),speciesName,entry_q,CMetab.REACTIONS, MainGui.compartment_default_for_dialog_window, new String(),false,"",true,true));
				/*if(CellParsers.isMultistateSpeciesName_withUndefinedStates(name)) {
					throw new MySyntaxException(Constants.SpeciesColumns.NAME.index, 
												"Multistate species with undefined states", 
												Constants.TitlesTabs.SPECIES.description);
					
				}*/
				if(CellParsers.isMultistateSpeciesName_withUndefinedStates(name)) {
				   DebugMessage dm = new DebugMessage();
					dm.setOrigin_table(Constants.TitlesTabs.SPECIES.description);
				    dm.setOrigin_col(Constants.SpeciesColumns.NAME.index);
				    dm.setOrigin_row(nrow);
					dm.setProblem("Multistate species with undefined states");
				    dm.setPriority(DebugConstants.PriorityType.PARSING.priorityCode);
					MainGui.addDebugMessage_ifNotPresent(dm);
				}
					
			}

			for(int i = 0; i < prod.size(); i++) {
				String s = (String)prod.get(i);
				s = extractName(s);
				if(//!s.contains("(") && 
						this.speciesDB.containsSpecies(s)) {continue;};
				
				HashMap<String, String> entry_q = new HashMap<String, String>();
				entry_q.put(s,MainGui.species_defaultInitialValue);
			indexes_row_species.add(speciesDB.addChangeSpecies(-1,new String(),s,entry_q, CMetab.REACTIONS,MainGui.compartment_default_for_dialog_window, new String(),false,"",true,true));
			
			}


			for(int i = 0; i < mod.size(); i++) {
				String s = (String)mod.get(i);
				s = extractName(s);
				if(this.speciesDB.containsSpecies(s)) {continue;};
				
				HashMap<String, String> entry_q = new HashMap<String, String>();
				entry_q.put(s,MainGui.species_defaultInitialValue);
				indexes_row_species.add(speciesDB.addChangeSpecies(-1,new String(),s,entry_q,CMetab.REACTIONS,MainGui.compartment_default_for_dialog_window, new String(),false,"",true,true));
			
			}

			for(int j = 0; j < indexes_row_species.size(); j++) {
				int species_row_index = Math.abs(indexes_row_species.get(j));
				if(species_row_index!= 0) {
					Species sp = speciesDB.getSpecies(species_row_index);
					Vector row = new Vector();
					row.add(species_row_index);
					row.addAll(sp.getAllFields());
					table_rows.add(row);
			
					MainGui.species_default_for_dialog_window.add(sp.getDisplayedName());
					//this.checkSimilarityName(sp.getSpeciesName(), species_row_index);
				}
			}
			
		}
		
		return table_rows;
	 }
		
	public int updateCompartment(Integer nrow, String name, String type,
			String initial, String expression, String notes) throws Exception {
		
		try{
			return this.compDB.addChangeComp(nrow, CellParsers.cleanName(name), type, initial, expression, notes);
		} catch(MySyntaxException ex) {
				if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) ex.printStackTrace();
			    DebugMessage dm = new DebugMessage();
				dm.setOrigin_table(Constants.TitlesTabs.COMPARTMENTS.description);
			    dm.setOrigin_col(ex.getColumn());
			    dm.setOrigin_row(nrow);
				dm.setProblem(ex.getMessage());
			    dm.setPriority(DebugConstants.PriorityType.PARSING.priorityCode);
				MainGui.addDebugMessage_ifNotPresent(dm);
				throw ex;
			}
		
	}

	public boolean updateEvent(Integer nrow, String name, String trigger, String actions, String delay, boolean delayAfterCalculation, String notes, int expandActionVolume) {
		
		Event event = new Event(name);
		event.setTrigger(trigger);
		event.setActions(actions);
		event.setNotes(notes);
		event.setDelay(delay);
		event.setDelayAfterCalculation(delayAfterCalculation);
		event.setExpandActionVolume(expandActionVolume);
		//CHEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEECK AND RETURN SOMETHING RELATED TO THE PARSING OF ACTION OF TRIGGER
		
		eventsDB.addChangeEvent(nrow, event);
		return true;
	}


	public String getSBMLid() {
		return this.copasiDataModelSBML_ID;
	}
	
	
	
	public String getDifferentialEquations(List listFunctionToCompact) throws Exception {
		 // Check if species:
	      // i)   is a constant
	      // ii)   is set by a rule
	      // if none of the above calculate the differential equation
		String ret = new String();
		Collection<Function> defFunctions = funDB.getAllFunctions();
		Vector<String> funcs = new Vector<String>();
		Iterator it2 = defFunctions.iterator();
		while(it2.hasNext()) {
			Function fun = (Function)it2.next();
			if(listFunctionToCompact.contains(fun.getName())) {
				String expression = fun.getExpandedEquation(new Vector());
				if(expression.length()>0) {
					funcs.add(fun.getCompactedEquation(new Vector())+"="+expression);
				}
			}
		}
		Collections.sort(funcs);
		/*for(int i = 0; i < funcs.size();i++) {
			System.out.println(funcs.get(i));
		}*/
		
		HashSet<String> notOdes = new HashSet<String>();
		Vector<Species> all = speciesDB.getAllSpecies();
		Vector<String> odes = new Vector<String>();
		
		for(int i = 1; i < all.size();i++) {
			Species s = all.get(i);
			if(s instanceof MultistateSpecies) {
				ret = "Multi-State species in the model: equation generation not supported.\n\n"+
					  "The generation of the underlying ODE for multistate systems is coming soon!\n\n"+
					  "For now you can export the model to COPASI and see the ODE system there! :)";
				return ret;
			}
			String expression = s.getExpression();
			if(expression.length()>0) {
				CellParsers.parser.parseExpression(expression);
				OdeExpressionVisitor_DELETE_oldParser visitor = new OdeExpressionVisitor_DELETE_oldParser(///i,
						listFunctionToCompact, this);
				if(CellParsers.parser.getErrorInfo()!= null) {
					throw new Exception(CellParsers.parser.getErrorInfo());
				}
				String expr = visitor.toString(CellParsers.parser.getTopNode());
				odes.add("d"+s.getSpeciesName()+"/dt="+expr);
				notOdes.add(s.getSpeciesName());
			}
			if(s.getType()== Constants.SpeciesType.FIXED.copasiType) {
				odes.add("d"+s.getSpeciesName()+"/dt=0");
				notOdes.add(s.getSpeciesName());
			}
			
		}
		Collections.sort(odes);
		if(odes.size() >0){
			ret+= "---- Determined by assignment rules ----------------" + System.getProperty("line.separator");
		}
		for(int i = 0; i < odes.size();i++) {
			 ret+= odes.get(i) + System.getProperty("line.separator");
		}
		if(odes.size() >0){
			ret+= "----------------------------------------------------" + System.getProperty("line.separator");
		}
		
		
	    // Go through all the reactions in the model and append to diffEquation
	    int numberOfReactions = this.getNumReactions();
	    String diffEquation = new String("");
	    TreeMap variableToEquationMap = new TreeMap();
	    for (int i = 0; i < numberOfReactions; i++) {
	    	String reaction_string = ((String) tableReactionmodel.getValueAt(i, Constants.ReactionsColumns.REACTION.index)).trim();
	        if(reaction_string.trim().length() == 0) continue; 
	    	Vector metabolites = CellParsers.parseReaction(this,reaction_string,i+1);
	    	Vector subs = (Vector)metabolites.get(0);
			Vector prod =(Vector)metabolites.get(1);
			Vector mod = (Vector)metabolites.get(2);
			
	    	//String infixFunction = convertMathToInfix (reaction.getKineticLaw ().getMath (), false);
	    	//String currentEquation = getEquation (reaction.getKineticLaw ().getMath (), infixFunction);
	        //Matcher getSpeciesName = getSpeciesFromFunction.matcher (infixFunction);
			String currentEquation = ((String) tableReactionmodel.getValueAt(i, Constants.ReactionsColumns.KINETIC_LAW.index)).trim();
			String currentEq_expanded = expandFunctionCalls(currentEquation,i,listFunctionToCompact);
	         for (int j = 0; j < subs.size(); j++) {
	        	String stoichStr = "";
	            diffEquation = "";
	            String sp = (String) subs.get(j);
	            if(notOdes.contains(sp)) {continue;}
	            if((String)variableToEquationMap.get(sp)==null) {
	            	 variableToEquationMap.put (sp, "");
	            }
	            diffEquation += " -";
	            
	            
	          //   String reactantCompartment = (String)speciesToCompartmentMap.get (reactantId);
	          //  stoich = reactant.getStoichiometry ();
	          //  if (stoich != 1.0)
	           //    stoichStr = stoich + " * ";
	            diffEquation += " ("  + stoichStr + currentEq_expanded + ") ";
	           /* if (getSpeciesName.matches ()) { 
	               Species speciesInFunction = (Species)model.findElementWithId (getSpeciesName.group (1), Model.SPECIES);
	               String speciesInFunctionCompartment = "";
	               if (speciesInFunction != null)
	                  speciesInFunctionCompartment = (String)speciesToCompartmentMap.get (speciesInFunction.getId ());
	               // Check whether species in kinetic law is in a different comparment.
	               // If this is the case then adjust its concentration by dividing comparment sizes together.
	               // C/dt = N * Vn/Vc    -> C species in comparment cytoplasm, N species in comparment nucleus
	               //                     -> Vc cytoplasm volume, Vn nucleus volume 
	               if (!reactantCompartment.equals (speciesInFunctionCompartment)) 
	                  diffEquation += " *(" + speciesInFunctionCompartment + "/" + reactantCompartment + ")";
	            }
	            variableToEquationMap.put (reactantId, (String)variableToEquationMap.get (reactantId) + replaceIdWithName (diffEquation));*/
	            variableToEquationMap.put (sp, (String)variableToEquationMap.get(sp) + diffEquation); 
	         }
	         for (int j = 0; j < prod.size(); j++) {
		            String stoichStr = "";
		            diffEquation = "";
		            String sp = (String) prod.get(j);
		            if(notOdes.contains(sp)) {continue;}
			        if((String)variableToEquationMap.get(sp)==null) {
		            	 variableToEquationMap.put (sp, "");
		            } else {
		            	 diffEquation += "+";
		            }
		          //   String reactantCompartment = (String)speciesToCompartmentMap.get (reactantId);
		          //  stoich = reactant.getStoichiometry ();
		          //  if (stoich != 1.0)
		           //    stoichStr = stoich + " * ";
		            diffEquation += " ("  + stoichStr + currentEq_expanded + ") ";
		           /* if (getSpeciesName.matches ()) { 
		               Species speciesInFunction = (Species)model.findElementWithId (getSpeciesName.group (1), Model.SPECIES);
		               String speciesInFunctionCompartment = "";
		               if (speciesInFunction != null)
		                  speciesInFunctionCompartment = (String)speciesToCompartmentMap.get (speciesInFunction.getId ());
		               // Check whether species in kinetic law is in a different comparment.
		               // If this is the case then adjust its concentration by dividing comparment sizes together.
		               // C/dt = N * Vn/Vc    -> C species in comparment cytoplasm, N species in comparment nucleus
		               //                     -> Vc cytoplasm volume, Vn nucleus volume 
		               if (!reactantCompartment.equals (speciesInFunctionCompartment)) 
		                  diffEquation += " *(" + speciesInFunctionCompartment + "/" + reactantCompartment + ")";
		            }
		            variableToEquationMap.put (reactantId, (String)variableToEquationMap.get (reactantId) + replaceIdWithName (diffEquation));*/
		            variableToEquationMap.put (sp, (String)variableToEquationMap.get(sp) + diffEquation); 
	         	}
	    }
	    
	    Set key = variableToEquationMap.keySet();
	    Iterator it = key.iterator();
	 	while(it.hasNext()) {
	 		String elem = (String) it.next();
			ret += "d"+elem+"/dt="+variableToEquationMap.get(elem) + System.getProperty("line.separator");
		}
		return ret;
		
	}
	
		
	private String expandFunctionCalls(String currentEquation, int i, 
			List listFunctionToCompact) throws Exception {
		boolean foundUserDefFunction = false;
		boolean foundPredefinedFunction= false;
		boolean foundMassAction= false;
		String ret = new String();
		
		
		if(((String)tableReactionmodel.getValueAt(i, Constants.ReactionsColumns.TYPE.index)).compareTo(Constants.ReactionType.USER_DEFINED.description)==0) { foundUserDefFunction = true; }
		else if(((String)tableReactionmodel.getValueAt(i, Constants.ReactionsColumns.TYPE.index)).compareTo(Constants.ReactionType.PRE_DEFINED.description)==0) { foundPredefinedFunction = true; }
		else if(((String)tableReactionmodel.getValueAt(i, Constants.ReactionsColumns.TYPE.index)).compareTo(Constants.ReactionType.MASS_ACTION.description)==0) {  foundMassAction = true;    	 }

		/*CellParsers.parser.parseExpression(currentEquation);
		OdeExpressionVisitor_DELETE_oldParser visitor = new OdeExpressionVisitor_DELETE_oldParser(//i,
				listFunctionToCompact,this);
		if(CellParsers.parser.getErrorInfo()!= null) {
			System.out.println(CellParsers.parser.getErrorInfo());
			throw new Exception(CellParsers.parser.getErrorInfo());
		}
		String expr = visitor.toString(CellParsers.parser.getTopNode());*/
		
		
		//if(visitor.getError()== null)	{
			
		
		String expr =new String();
		if(listFunctionToCompact.size()!=0) {
			if(listFunctionToCompact.size() == funDB.getAllFunctions().size()) {
					expr = MainGui.getViewIn(Constants.TitlesTabs.REACTIONS.description,i,Constants.ReactionsColumns.KINETIC_LAW.index,Constants.Views.COMPRESSED.index);
				}
			else {
				expr = tableReactionmodel.getValueAt(i, Constants.ReactionsColumns.KINETIC_LAW.index).toString();
				do {
					String tmp = new String();
					try{
						InputStream is = new ByteArrayInputStream(expr.getBytes("UTF-8"));
						MR_Expression_Parser parser = new MR_Expression_Parser(is);
						CompleteExpression root = parser.CompleteExpression();
						ExpressionVisitor vis = new ExpressionVisitor(listFunctionToCompact,this,false);
						root.accept(vis);
						if(vis.getExceptions().size() == 0) {
							tmp  = vis.getExpression();
							//System.out.println("qua - "+tmp);
						} else {
							throw vis.getExceptions().get(0);
						}

					}catch (Exception e) {
						if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
					}
					if(expr.compareTo(tmp)==0) break;
					expr = tmp;
			} while(true);
			}
		} else {
			expr = MainGui.getViewIn(Constants.TitlesTabs.REACTIONS.description,i,Constants.ReactionsColumns.KINETIC_LAW.index,Constants.Views.EXPANDED.index);
		}
		 
		if(expr.length() > 0) {
			if(foundMassAction) {
				Vector metabolites = CellParsers.parseReaction(this,(String) tableReactionmodel.getValueAt(i,Constants.ReactionsColumns.REACTION.index),i+1);
				Vector subs = (Vector) metabolites.get(0);
				ret+=expr+"*(";
				
				for(int j = 0; j < subs.size(); j++) {
					String sp = (String) subs.get(j);
					ret+=sp+"*";
				}
				ret = ret.substring(0,ret.length()-1);
				ret+=")";
			} else {
				ret+=expr;
			}
		}
		else { throw new ParseException("Parsing problem generating the ODE system.");}
		
		
		return ret;
	}
	
	public void updateSBMLids(HashMap<Long, String> SBMLids) {
		this.speciesDB.updateSBMLids(SBMLids);
	}
	
	public Vector addSpeciesFromSBMLidList(List<String> metaboliteSBMLidInCopasiDataModel) throws Exception{
		CModel model = copasiDataModel.getModel();
		Vector rows = new Vector();
		HashMap<Long, String> SBML_IDS = new HashMap<Long, String>();
		
		for(int i = 0; i < metaboliteSBMLidInCopasiDataModel.size(); i++) {
			String key = metaboliteSBMLidInCopasiDataModel.get(i);
			Vector row = new Vector();
            CMetab metab = model.getMetabolite(this.findMetabolite_sbmlID(key));
            
            String cleanName = CellParsers.cleanName(metab.getObjectName(),true);
            if(cleanName.compareTo(metab.getObjectName())!=0) {
            	metab.setObjectName(cleanName);
            }
            row.add(metab.getObjectName());
            SBML_IDS.put((long) (this.speciesDB.getNumSpeciesExpanded()+i+1), metab.getSBMLId());
            if(MainGui.quantityIsConc) row.add(new Double(metab.getInitialConcentration()).toString());
            else row.add(new Double(metab.getInitialValue()).toString());
           
           if(metab.getInitialExpression().trim().length()>0) {
        	   row.set(row.size()-1, buildMRExpression_fromCopasiExpr(metab.getInitialExpression()));
           }
            
            row.add(Constants.SpeciesType.getDescriptionFromCopasiType(metab.getStatus()));
            
            row.add(metab.getCompartment().getObjectName());
            
            if(metab.getStatus() == CMetab.ASSIGNMENT || metab.getStatus() == CMetab.ODE) {
            	row.add(CellParsers.cleanMathematicalExpression(this.buildMRExpression_fromCopasiExpr(metab.getExpression())));
            }
            
           rows.add(row);
		}
		Vector ret = new Vector();
        ret.add(rows);
        ret.add(SBML_IDS);
        return ret;
	}
	
	
	int dummySpecies = 0;
	
	public List<String> addDummySpecies() {
		Vector<String>  ret = new Vector<String>();
		CModel m = this.copasiDataModel.getModel();
		CCompartment comp = m.createCompartment("cell");
		CMetab metab = m.createMetabolite("Dummy_"+dummySpecies, "cell");
		metab.setSBMLId(metab.getObjectDisplayName());
		ret.add(metab.getSBMLId());
		dummySpecies++;
		return ret;
	}
	
	
	public void removeComp(int[] selected) {
		for(int i = 0; i < selected.length; i++) {
			this.compDB.removeComp(selected[i]-i);
		}
	}
	
	public void removeSpecies(int[] selected) {
		for(int i = 0; i < selected.length; i++) {
			this.speciesDB.removeSpecies(selected[i]-i);
		}
	}
	public void clearDataOldMultistateSpecies(String valueAt) {
		String name = valueAt.substring(0,valueAt.indexOf("("));
		speciesDB.clearDataOldMultistateSpecies(name);
		
	}
	
	public boolean convert2nonReversible() {
		CModel model = copasiDataModel.getModel();
		if(model == null) return  false;
		
		 boolean converted = model.convert2NonReversible();
	        
	        if(!converted) {
	        	System.out.println("!!!!!!----- PROBLEMS IN convert2NonReversible-----");
	           	System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
	           	return false;
	        }
	       return converted;
		
	}
	
	public void removeSpecies(int atIndex) throws Exception {
		this.speciesDB.removeSpecies(atIndex);
	}
	
	public void removeComp(int atIndex) throws Exception {
		this.compDB.removeComp(atIndex);
	}
	
	public void removeGlobalQ(int atIndex) throws Exception {
		this.globalqDB.removeGlobalQ(atIndex);
	}
	
	public void removeEvent(int atIndex) throws Exception {
		this.eventsDB.removeEvent(atIndex);
	}
	
	public void removeReaction(int atIndex) throws Exception {
		
		tableReactionmodel.removeRow(atIndex);
		tableReactionmodel.fireTableDataChanged();
		MainGui.tableReactionmodel.removeRow(atIndex); //TO BE DELETED ONCE REACTION DATA STRUCTURE
		MainGui.tableReactionmodel.fireTableDataChanged();//TO BE DELETED ONCE REACTION DATA STRUCTURE
	}
	
	public Vector<GlobalQ> getAllGlobalQ() {
		return new Vector(this.globalqDB.getAllGlobalQ());
	}
	
	public Vector<Event> getAllEvents() {
		return new Vector(this.eventsDB.getAllEvents());
	}
	

	public boolean containsSpecies(String name) {
		return speciesDB.containsSpecies(name);
	}

	public String getEditableExpression(int row, int column) {
		return speciesDB.getEditableExpression(row, column);
	}

	public void setEditableExpression(String editableString, int row, int column) throws MySyntaxException {
		speciesDB.setEditableExpression(editableString, row, column);
	}

	public Species getSpecies(String name) {
		return speciesDB.getSpecies(name);
	}

	public Compartment getComp(String name) {
		return compDB.getComp(name);
	}

	public Function getFunctionByName(String fun) throws Exception {
		return funDB.getFunctionByName(fun);
	}

	public HashSet<Integer> getWhereFuncIsUsed(String funName) {
		return funDB.whereFuncIsUsed.get(funName);
		
	}
	
	public void setWhereFuncIsUsed(String funName, HashSet<Integer> h) {
		funDB.whereFuncIsUsed.put(funName,h);
		
	}
	
	public void addMapping(int i, Vector v) {
		funDB.mappings.put(new Integer(i),v);
		
	}

	public HashMap<String, HashMap<String, String>> getMultistateInitials() {
		return speciesDB.getMultistateInitials();
	}

	public void setMultistateInitials(
			HashMap<String, HashMap<String, String>> multistateInitials) {
		if(multistateInitials!= null) speciesDB.setMultistateInitials(multistateInitials);
	}

	public Integer getSpeciesIndex(String name) {
		
		return speciesDB.getSpeciesIndex(name);
	}

	public Vector<MultistateSpecies> getAllMultistateSpecies() {
		return speciesDB.getAllMultistateSpecies();
	}

	public void addCompartmentToSpecies(String name, String cmpName) {
		speciesDB.addCompartmentToSpecies(name, cmpName);
		
	}
	
}
