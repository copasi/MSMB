package msmb.testing;
import msmb.gui.MainGui;
import uk.ac.ebi.biomodels.ws.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import org.COPASI.*;


public class BiomodelTest {
	static boolean deleteFilesAfterProcessing = true;
	static boolean collectStatisticsMode = false;
	static 	String subdirectoryTests = "tests/BiomodelTests/";
	static String collectStatisticsFile = new String(subdirectoryTests+"BioModels_statistics.txt");
	
	static String nameFileLogReport = subdirectoryTests+"MSMB_simulationDiff_report.txt";
	static String progressLog = subdirectoryTests+"MSMB_progressLog.txt";
	static Vector<String> failedTests = new Vector<String>();
	static boolean simulateAll = true;
	static Integer indexToSimulate = new Integer(0);
	
	static boolean FULL_BRACKET_EXPRESSION = true;
	
	//IDs with delay --> NO SIMULATION ALLOWED 
	public static final Vector<String> id_delay;
	static {
		id_delay = new Vector<String>();
		id_delay.add("BIOMD0000000024");
		id_delay.add("BIOMD0000000025");
		id_delay.add("BIOMD0000000034");
		id_delay.add("BIOMD0000000154");
		id_delay.add("BIOMD0000000155");
		id_delay.add("BIOMD0000000196");
		id_delay.add("BIOMD0000000297"); // simulations not possible because: Simultaneous event assignments encountered. The simulation cannot continue.
	}				  
	
	
	public static final Vector<String> models_with_known_problems;
	static {
		models_with_known_problems = new Vector<String>();
		models_with_known_problems.add("BIOMD0000000248");//convert to irreversible not correct because a global quantity was referring to the flux of a reversible reaction, error not catched
		models_with_known_problems.add("BIOMD0000000408");//not all the  reversible reactions that can be split because they use functions with parameter and the - is in the parameters, so is wrong to change them manually as irreversible
														  //if I make them all irreversible is ok at export, but I get an error in simulation (simultaneous events) that is probably caused by that forced to be irreversible change
		models_with_known_problems.add("BIOMD0000000411");//variable not assigned in GUI

		models_with_known_problems.add("BIOMD0000000056"); //BUG REPORTED TO FRANK - unsupported annotations
		models_with_known_problems.add("BIOMD0000000169");//BUG REPORTED TO FRANK - unsupported annotations but somehow broken
	}
		
		
	
	
	public static void main(String[] args) {
		try {
			String rscriptPath = new String("C:\\Program Files\\R\\R-2.13.2\\bin\\x64\\RScript");
			RunSimulation.setRScriptPath(rscriptPath);
			if(args.length > 0) { 
				for(int i = 0; i<args.length; i++) {
					String current = args[i];

					if(current.compareTo("-del")==0) {	deleteFilesAfterProcessing = true;	}
					else if(current.compareTo("-o")==0){ 
						String subDir = args[i+1];
						if(!subDir.endsWith("/")) subDir += "/";
						File dir=new File(subDir);
						if(!dir.exists()){
							boolean result = dir.mkdir();  
						    if(!result){    
						    	System.out.println("Unable to create the specified output directory.");
						    	System.exit(1);
						     }

						}
						subdirectoryTests = subDir;
						nameFileLogReport = subdirectoryTests+"MSMB_simulationDiff_report.txt";
						progressLog = subdirectoryTests+"MSMB_progressLog.txt";
						i++;
					} else if(current.compareTo("-all")==0) { simulateAll = true; 
					} else if(current.compareTo("-single")==0) { 
						simulateAll = false;
						indexToSimulate = new Integer(Integer.parseInt(args[i+1]));
						i++;
					} else if(current.compareTo("-rscript")==0){ 
						String rscript = args[i+1];
						File file=new File(rscript);
						if(!file.exists()){
							System.out.println("Unable to find the RScript path.");
						    System.exit(1);
					    } else {
					    	RunSimulation.setRScriptPath(rscript);
					    }
					}

				}
			} else {
				File dir=new File(subdirectoryTests);
				if(!dir.exists()){
					boolean result = dir.mkdir();  
				    if(!result){    
				    	System.out.println("...");
				    	System.out.println("Unable to create the output directory "+subdirectoryTests+".");
				    	System.out.println("Please provide an output directory path with the option -o.");
				    	System.out.println("...");
				    	System.exit(1);
				     }

				}
				
				
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		System.out.println("... start BiomodelTest ...");
		BiomodelTest test = new BiomodelTest();
		test.runTest();
		System.out.println("... end BiomodelTest test ...");
		System.out.println("... FAILED SIMULATION TESTS :"+ failedTests);
		
		
		System.out.println("... See files "+nameFileLogReport+ " and "+progressLog + " for progress reports ...");
		System.out.println("...");
		
		System.exit(0);
	}



	
	
	public void runTest(){
		try{
			//System.setOut(new PrintStream(new File(subdirectoryTests+"SystemOutput.txt")));

			if(collectStatisticsMode) initializeCollectStatistics();
			
			MainGui m = new MainGui();
			m.setFullBracketExpression(FULL_BRACKET_EXPRESSION);
			BioModelsWSClient client = new BioModelsWSClient();
			System.out.println("... WS retrieval of all curated IDs ...");
			System.out.flush();

			String[] curated = client.getAllCuratedModelsId();
			System.out.println("... total curated IDs: "+curated.length+" ...");
			System.out.println("... done ...");
			System.out.flush();
			ArrayList<String> curatedIDs = new ArrayList<String>();
			curatedIDs.addAll(Arrays.asList(curated));
			Collections.sort(curatedIDs);

			ArrayList<Integer> indices = new ArrayList<Integer>();
			if(simulateAll) {
				//LOAD THE LAST START OF progressLog
				int idBeforeCrash = loadIdBeforeCrash_fromProgressLog();
				for(int i = idBeforeCrash; i <= curatedIDs.size(); i++) { indices.add(i); }
			} else {
				indices.add(indexToSimulate);
				if(indexToSimulate <=0 || indexToSimulate > curatedIDs.size()) {
					throw new FileNotFoundException("The model ID "+indexToSimulate+ " is not available as curated model in Biomodel!");
				}
			}					
			generateLogReport();

			FileOutputStream buffoutProgressLog= new FileOutputStream(progressLog,true);
			PrintWriter outProgressLog = new PrintWriter(new OutputStreamWriter(buffoutProgressLog,"UTF-8"));
			SimpleDateFormat formatDate = new SimpleDateFormat("HH:mm:ss:SS");

		/*indices.clear();
			//indices.add(new Integer(39)); //1% should be ok
			//indices.add(new Integer(206));//1% should be ok
			//indices.add(new Integer(18)); //high% e-021
			//indices.add(new Integer(85));//high% e-028
			
		//	indices.add(new Integer(3));
			indices.add(new Integer(18));
			indices.add(new Integer(51));
			indices.add(new Integer(61));
			indices.add(new Integer(63));*/
				
					

			for(int i = 0; i < indices.size(); i++) {
				Integer index = indices.get(i);
				if(index == 0) continue;
				String sbmlID = curatedIDs.get(index-1);
				if(models_with_known_problems.contains(sbmlID)&&!collectStatisticsMode){
					System.out.println("Model with known problems. We are working to solve them :)");
					outProgressLog.println(sbmlID + ", parseErrors , "+ formatDate.format(new Date()));
					outProgressLog.flush();
					continue;
				}


				FileOutputStream buffout= new FileOutputStream(subdirectoryTests+sbmlID+".xml");
				PrintWriter out = new PrintWriter(new OutputStreamWriter(buffout,"UTF-8"));
				System.out.println("... WS retrieval of "+sbmlID+" ...");
				System.out.flush();
				out.println(client.getModelSBMLById(sbmlID));
				out.flush();
				out.close();


				outProgressLog.println(sbmlID + ", start , "+ formatDate.format(new Date()));
				outProgressLog.flush();
				System.out.println("... Analyzing "+sbmlID+"...");
				System.out.flush();
				
				if(collectStatisticsMode) {
					collectStatistics(sbmlID);
					continue;
				}
				
				
				MainGui.cleanUpModel();
				MainGui.clearCopasiFunctions();
				MainGui.fromMainGuiTest = true;
				File sbmlFile = new File(subdirectoryTests+sbmlID+".xml");
				File cpsFile = new File(subdirectoryTests+sbmlID+"_MSMB.cps");
				try{
					m.loadSBML(sbmlFile);
					m.updateStatusQuantityIsConcentration(true);
					m.updateStatusExportConcentration(true);//it does not matter because all the expression are expanded so the default choice is never used
					System.out.println("... Model loaded, now saveCPS ...");
					System.out.flush();
					m.saveCPS(cpsFile,false);
					System.out.println("... Model saved, now simulations ...");
					System.out.flush();
					outProgressLog.println(sbmlID + ", exported , "+ formatDate.format(new Date()));
					outProgressLog.flush();

					RunSimulation.RunSimulations_CPSfromMSMB_OriginalSBML(
							cpsFile.getAbsolutePath(),
							sbmlFile.getAbsolutePath(), 
							2000, 200,new File(nameFileLogReport).getAbsolutePath());


					outProgressLog.println(sbmlID + ", simulated , "+ formatDate.format(new Date()));
					outProgressLog.flush();


				} catch(Throwable t){
					if(t instanceof FileNotFoundException) {
						throw t;
					}
					else if(t instanceof Exception) {
						Exception ex = (Exception)t;
						if(!id_delay.contains(sbmlID))	{
							if(ex.getCause()!=null && ex.getCause().getMessage() != null) {
								if(ex.getCause().getMessage().compareTo("convert2nonReversible")==0) {
									outProgressLog.println(sbmlID + ", convert2nonReversible , "+ formatDate.format(new Date()));
									outProgressLog.flush();
								} else {
									outProgressLog.println(sbmlID + ", parseErrors , "+ formatDate.format(new Date()));
									outProgressLog.flush();
								}
							} else {
								ex.printStackTrace();
								outProgressLog.println(sbmlID + ", otherSimulationError , "+ formatDate.format(new Date()));
								outProgressLog.flush();
							}
							failedTests.add(sbmlID);
						}
						else {
							outProgressLog.println(sbmlID + ", delayAndSimilar , "+ formatDate.format(new Date()) );
							outProgressLog.flush();

						}
					}
					continue;
				}


				if(deleteFilesAfterProcessing) {
					boolean success = sbmlFile.delete();
					if (!success) System.err.println("Delete: deletion sbmlFile failed");
					success = cpsFile.delete();
					if (!success) System.err.println("Delete: deletion cpsFile failed");
				}
				System.gc();


			}
			outProgressLog.flush();
			outProgressLog.close();

		}	catch(Exception ex) {

			ex.printStackTrace();
		}
	}
	
	private void initializeCollectStatistics() {
		File file = new File(collectStatisticsFile);
		
		try {
			FileOutputStream buffout2 = new FileOutputStream(file.getAbsolutePath(),false);

			PrintWriter out2 = new PrintWriter(new OutputStreamWriter(buffout2,"UTF-8"));
			out2.print("ModelID");
			out2.print(", ");
			out2.print("Species");
			out2.print(", ");
			out2.print("Compartments");
			out2.print(", ");
			out2.print("Reactions");
			out2.print(", ");
			out2.print("GlobalQuantities");
			out2.print(", ");
			out2.print("Events");
			out2.println("");
			out2.flush();
			out2.close();
			out2.flush();
			out2.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void collectStatistics(String sbmlID) {
		try{
			File file = new File(collectStatisticsFile);

			FileOutputStream buffout2= new FileOutputStream(file.getAbsolutePath(),true);
			PrintWriter out2 = new PrintWriter(new OutputStreamWriter(buffout2,"UTF-8"));

			File sbmlFile = new File(subdirectoryTests+sbmlID+".xml");

			CCopasiRootContainer.removeDatamodelWithIndex(0);
			CCopasiDataModel dataModel = CCopasiRootContainer.addDatamodel();
			dataModel.importSBML(sbmlFile.getAbsolutePath());


			CModel model = dataModel.getModel();
			boolean converted = model.convert2NonReversible();
			if(!converted) {
				int i = 0;
				int index = -1;
				while(CCopasiMessage.size() > 0) {
					CCopasiMessage message = CCopasiMessage.getFirstMessage();
					if(message.getType()==CCopasiMessage.ERROR) {
						String text = message.getText();
						if(text.contains("could not be split into two irreversible")) {
							StringTokenizer st = new StringTokenizer(text, "'");
							st.nextToken();
							String reactionName = st.nextToken();

							index = findReaction(reactionName,dataModel);
							System.out.println("reversible that could not be split index = "+index);
							if(index != -1) {
								CReaction r = model.getReaction(index);
								r.setReversible(false);
								model.compile();
							}
							index = -1;

						}

					}
					i++;
				}

				converted = model.convert2NonReversible();
			}

			out2.print(sbmlID);
			out2.print(", ");
			out2.print(model.getMetabolites().size());
			out2.print(", ");
			out2.print(model.getCompartments().size());
			out2.print(", ");
			out2.print(model.getReactions().size());
			out2.print(", ");
			out2.print(model.getModelValues().size());
			out2.print(", ");
			out2.print(model.getEvents().size());
			out2.println("");
			out2.flush();
			out2.close();
			if(deleteFilesAfterProcessing) {
				boolean success = sbmlFile.delete();
				if (!success) throw new IllegalArgumentException("Delete: deletion sbmlFile failed");
				}
			System.gc();
		} catch (Exception ex){
			ex.printStackTrace();
		}
	}
	
	private int findReaction(String name, CCopasiDataModel copasiDataModel) {
		if(name.startsWith("\"")&&name.endsWith("\"")) {	name = name.substring(1,name.length()-1); }
		CModel model = copasiDataModel.getModel();
		int i, iMax =(int) model.getReactions().size();
        for (i = 0;i < iMax;++i)
        {
            CReaction r = model.getReaction(i);
            assert r != null;
            String current = new String();
            current = r.getObjectName();
            if(current.startsWith("\"")&&current.endsWith("\"")) {	current = current.substring(1,current.length()-1); }
    		if(name.compareTo(current) == 0) return i;
        }
        
        return -1;
	}


	private int loadIdBeforeCrash_fromProgressLog() {
			try {
				File file = new File(progressLog);
				if(!file.exists()) return 1;
		        BufferedReader reader = new BufferedReader(new FileReader(file));
		        String line = new String();
		        String analyzingBioModel = new String();
		        int index = 0;
		        String tmpTextFile = new String();
		        String textFile = new String();
		        while((line = reader.readLine()) != null) {
		        		if(line.trim().length()==0) continue; 
		        		if(line.indexOf(",")==-1) continue;
		        		if(line.indexOf(".")!=-1 || line.indexOf("=")!=-1|| line.indexOf("reaching")!=-1 || line.indexOf("-")!=-1) continue;
		        		String tmp = line.substring(0,line.indexOf(","));
		        		if(tmp.compareTo(analyzingBioModel)!=0) {
		        			index++;
		        			if(index != new Integer(tmp.substring(12,15))) {
		        				index--;
		        				break;
		        			}
		        			analyzingBioModel = new String(tmp);
		        			tmpTextFile += line+"\n";
		        			textFile += tmpTextFile;
		        			tmpTextFile = new String();
		        		} else {
		        			tmpTextFile += line+"\n";
		        		}
				 }
		        if(tmpTextFile.length()==0) {
		        	if(textFile.lastIndexOf("\n")!= -1) {
		        		int lastUsefulNewLine = textFile.substring(0,textFile.lastIndexOf("\n")).lastIndexOf("\n");
		        		textFile = textFile.substring(0, lastUsefulNewLine);
		        	}
		        }
				 reader.close();
		         
		     	FileOutputStream buffout2= new FileOutputStream(file.getAbsolutePath(),false);
				PrintWriter out2 = new PrintWriter(new OutputStreamWriter(buffout2,"UTF-8"));
				out2.println(textFile);
				out2.flush();
				out2.close();
		         
		         return index;
				} catch (Exception e) {
					e.printStackTrace();
					return -1;
				}
			
		}
	
		
		
		private void generateLogReport() {
			//file collecting maxPercentageError and similar info
			try {
				File file = new File(nameFileLogReport);
				if(!file.exists()) {
					FileOutputStream buffout= new FileOutputStream(file.getAbsolutePath());
					PrintWriter out = new PrintWriter(new OutputStreamWriter(buffout,"UTF-8"));
					out.println("StartIF, IDNumber, MaxPercentageError, MaxAbsoluteError,  SimulatedFrom, Simulated to, NumberSteps, MaxPercentageErrorOnSpecies");
					out.flush();
					out.println();
					out.close();
				}
			}catch(Exception ex) {
				ex.printStackTrace();
			}
			

			
		}

	
}
