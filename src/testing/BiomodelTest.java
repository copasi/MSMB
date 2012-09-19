package testing;
import gui.MainGui;
import uk.ac.ebi.biomodels.ws.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import org.COPASI.*;


public class BiomodelTest {
	static boolean deleteFilesAfterProcessing = false;
	static 	String subdirectoryTests = "tests/BiomodelTests/";
	static String nameFileLogReport = subdirectoryTests+"MSMB_simulationDiff_report.txt";
	static String progressLog = subdirectoryTests+"MSMB_progressLog.txt";
	static Vector<String> failedTests = new Vector<String>();
	static boolean simulateAll = true;
	static Integer indexToSimulate = new Integer(0);
	
	//IDs with delay --> NO SIMULATION ALLOWED 
	public static final Vector<String> id_delay;
	static {
		id_delay = new Vector<String>();
		id_delay.add("BIOMD0000000024");
		id_delay.add("BIOMD0000000025");
		id_delay.add("BIOMD0000000034");
		id_delay.add("BIOMD0000000154");
		id_delay.add("BIOMD0000000155");
		id_delay.add("BIOMD0000000177"); //simulations not possible because exception numerical instability 
		id_delay.add("BIOMD0000000196");
		id_delay.add("BIOMD0000000297"); // simulations not possible because: Simultaneous event assignments encountered. The simulation cannot continue.
	}				  
	
	
	//IDs with delay --> NO SIMULATION ALLOWED 
	public static final Vector<String> models_with_known_problems;
		static {
			models_with_known_problems = new Vector<String>();
			models_with_known_problems.add("BIOMD0000000293");
			models_with_known_problems.add("BIOMD0000000388");
			models_with_known_problems.add("BIOMD0000000322");
			models_with_known_problems.add("BIOMD0000000410");
			models_with_known_problems.add("BIOMD0000000411");
			models_with_known_problems.add("BIOMD0000000415"); 
			models_with_known_problems.add("BIOMD0000000112");
			models_with_known_problems.add("BIOMD0000000174");
			models_with_known_problems.add("BIOMD0000000055");
			models_with_known_problems.add("BIOMD0000000214");
			models_with_known_problems.add("BIOMD0000000234");
			models_with_known_problems.add("BIOMD0000000295");
			models_with_known_problems.add("BIOMD0000000412");
			models_with_known_problems.add("BIOMD0000000350");
			models_with_known_problems.add("BIOMD0000000143");
			models_with_known_problems.add("BIOMD0000000226");
			models_with_known_problems.add("BIOMD0000000227");
			models_with_known_problems.add("BIOMD0000000396");
			models_with_known_problems.add("BIOMD0000000397");
			models_with_known_problems.add("BIOMD0000000266");
			models_with_known_problems.add("BIOMD0000000255");
			models_with_known_problems.add("BIOMD0000000422");
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
		System.out.println("... See files "+nameFileLogReport+ " and "+progressLog + " for progress reports ...");
		System.out.println("...");
		
		System.exit(0);
	}
		public void runTest(){
		try{
			MainGui m = new MainGui();
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
	    
	    	for(int i = 0; i < indices.size(); i++) {
				Integer index = indices.get(i);
				if(index == 0) break;
				String sbmlID = curatedIDs.get(index-1);
				if(models_with_known_problems.contains(sbmlID)){
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
				MainGui.cleanUpModel();
				MainGui.clearCopasiFunctions();
				MainGui.fromMainGuiTest = true;
				File sbmlFile = new File(subdirectoryTests+sbmlID+".xml");
				File cpsFile = new File(subdirectoryTests+sbmlID+"_MSMB.cps");
				try{
					m.loadSBML(sbmlFile);
					m.updateStatusQuantityIsConcentration(true);
					m.updateStatusExportConcentration(true);//it does not matter because all the expression are expanded so the default choice is never used
					m.saveCPS(cpsFile,false);
					
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
								outProgressLog.println(sbmlID + ", parseErrors , "+ formatDate.format(new Date()));
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
					if (!success) throw new IllegalArgumentException("Delete: deletion sbmlFile failed");
					success = cpsFile.delete();
					if (!success) throw new IllegalArgumentException("Delete: deletion cpsFile failed");
				}
				System.gc();
		
			
		}
			outProgressLog.flush();
			outProgressLog.close();
			
		}	catch(Exception ex) {
				
				ex.printStackTrace();
			}
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
					out.println("StartIF, IDNumber, MaxPercentageError,  SimulatedFrom, Simulated to, NumberSteps, MaxPercentageErrorOnSpecies");
					out.flush();
					out.println();
					out.close();
				}
			}catch(Exception ex) {
				ex.printStackTrace();
			}
			

			
		}

	
}
