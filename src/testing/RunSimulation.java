package testing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.channels.FileChannel;

import org.COPASI.*;

import rcaller.RCaller;


public class RunSimulation {
	
	static CCopasiDataModel dataModel = CCopasiRootContainer.addDatamodel();
	private static long steps;
	private static double maxTime;
	
	private static String RSCRIPT_PATH = new String();
	
	public static void setRScriptPath(String s) { RSCRIPT_PATH = new String(s);}
			
	public static void RunSimulations_CPSfromMSMB_OriginalSBML(String filename_CPS, String filename_SBML, long nsteps, int time, String nameFileLogReport) throws Exception {
		 steps  = nsteps;
		 maxTime = time;

		 dataModel.loadModel(filename_CPS);
		 createTimeSeriesReport(filename_CPS);

		 dataModel.importSBML(filename_SBML);
		 createTimeSeriesReport(filename_SBML);

		 
		 replaceCharactersWithNaN(filename_CPS,filename_SBML);
		 
		 compareGeneratedTXTWithCorrectTXT(filename_CPS, filename_SBML, nameFileLogReport);
	 }
	 
	 
	 private static void replaceCharactersWithNaN(String filename_CPS,	String filename_SBML) {
		 
		 try{
			File file = new File(filename_CPS.substring(0,filename_CPS.lastIndexOf("."))+".txt");
	         BufferedReader reader = new BufferedReader(new FileReader(file));
	         String line = "", oldtext = "";
	         while((line = reader.readLine()) != null)
	            {
	             oldtext += line + "\n";
	         }
	         reader.close();
	        
	         String newtext = oldtext.replaceAll("-1.#IND", "NaN");
	         newtext=newtext.replace("\"", "''");
	         newtext=newtext.replaceAll(", ", "MYCOMMA ");
	         newtext=newtext.replaceAll(",", "_");
	         newtext=newtext.replaceAll("MYCOMMA", ",");
	         
	         
	         FileWriter writer = new FileWriter(filename_CPS.substring(0,filename_CPS.lastIndexOf("."))+".txt");
	         writer.write(newtext);
	         writer.close();
	         
	     	File file2 = new File(filename_SBML.substring(0,filename_SBML.lastIndexOf("."))+".txt");
	         BufferedReader reader2 = new BufferedReader(new FileReader(file2));
	         String line2 = "", oldtext2 = "";
	         while((line2 = reader2.readLine()) != null)
	            {
	             oldtext2 += line2 + "\n";
	         }
	         reader2.close();
	        
	         String newtext2 = oldtext2.replaceAll("-1.#IND", "NaN");
	         newtext2=newtext2.replace("\"", "''");
	         newtext2=newtext2.replaceAll(", ", "MYCOMMA ");
	         newtext2=newtext2.replaceAll(",", "_");
	         newtext2=newtext2.replaceAll("MYCOMMA", ",");
	         
	         FileWriter writer2 = new FileWriter(filename_SBML.substring(0,filename_SBML.lastIndexOf("."))+".txt");
	         writer2.write(newtext2);
	         writer2.close();

		 } catch (Exception e) {
		e.printStackTrace();
	}
			
			
	}


	public static void compareGeneratedTXTWithCorrectTXT(String fileName1, String fileName2, String nameFileLogReport) throws IOException {
	    	RCaller caller = new RCaller();
	    	caller.cleanRCode();
	    	caller.setRscriptExecutable(RSCRIPT_PATH);
	    	
	    	fileName1 = fileName1.substring(0,fileName1.lastIndexOf("."))+".txt";
	    	fileName2 = fileName2.substring(0,fileName2.lastIndexOf("."))+".txt";
	    	fileName2 = fileName2.substring(0,fileName2.lastIndexOf("."))+".txt";
	    	fileName1 = fileName1.replace('\\','/');
	    	fileName2 = fileName2.replace('\\','/');
	    	nameFileLogReport = nameFileLogReport.replace('\\','/');
	    	String baseName = fileName2.substring(fileName2.lastIndexOf("/")+1,fileName2.lastIndexOf("."));
	    	caller.addRCode("file1 = \""+fileName1+"\"");
	    	caller.addRCode("file2 = \""+fileName2+"\"");
	    	caller.addRCode("baseName = \""+baseName+"\"");
	    	caller.addRCode("logFileName = \""+nameFileLogReport+"\"");
	    	
	    	File file = caller.startPlot();  
	       	caller.addRCode(RCode_generatePlotsFromTXTs);
	    	caller.endPlot();
	        caller.runOnly();
	        copyFile(file, new File((new File(fileName1).getParent())+System.getProperty("file.separator")+baseName+".png")); 
	   }
		
	  public static void copyFile(File sourceFile, File destFile) throws IOException {
		    if(!destFile.exists()) {
		        destFile.createNewFile();
		    }

		    FileChannel source = null;
		    FileChannel destination = null;

		    try {
		        source = new FileInputStream(sourceFile).getChannel();
		        destination = new FileOutputStream(destFile).getChannel();
		        destination.transferFrom(source, 0, source.size());
		    }
		    finally {
		        if(source != null) {
		            source.close();
		        }
		        if(destination != null) {
		            destination.close();
		        }
		    }
		    
		    
		}

	 public static void createTimeSeriesReport(String fileName) throws Exception {
		 CModel model = dataModel.getModel();

		 // create a report with the correct filename and all the species against
		 // time.
		 CReportDefinitionVector reports = dataModel.getReportDefinitionList();
		 // create a new report definition object
		 CReportDefinition report = reports.createReportDefinition("Report", "Output for timecourse");
		 // set the task type for the report definition to timecourse
		 report.setTaskType(CCopasiTask.timeCourse);
		 // we don't want a table
		 report.setIsTable(false);
		 // the entries in the output should be seperated by a ", "
		 report.setSeparator(new CCopasiReportSeparator(", "));

		 // we need a handle to the header and the body
		 // the header will display the ids of the metabolites and "time" for
		 // the first column
		 // the body will contain the actual timecourse data
		 ReportItemVector header = report.getHeaderAddr();
		 ReportItemVector body = report.getBodyAddr();

		 body.add(new CRegisteredObjectName(model.getObject(new CCopasiObjectName("Reference=Time")).getCN().getString()));
		 body.add(new CRegisteredObjectName(report.getSeparator().getCN().getString()));
		 header.add(new CRegisteredObjectName(new CCopasiStaticString("time").getCN().getString()));
		 header.add(new CRegisteredObjectName(report.getSeparator().getCN().getString()));

		 int i, iMax =(int) model.getMetabolites().size();
		 for (i = 0;i < iMax;++i)
		 {
			 CMetab metab = model.getMetabolite(i);
			 assert metab != null;
			 body.add(new CRegisteredObjectName(metab.getObject(new CCopasiObjectName("Reference=Concentration")).getCN().getString()));
			 
			 String name = metab.getObjectName();
			 String separator = report.getSeparator().getObjectName().toString().trim();
			 if(name.contains(separator)) {
				 header.add(new CRegisteredObjectName(new CCopasiStaticString("\""+metab.getObjectName()).getCN().getString()+"\""));
			 } else {
				 header.add(new CRegisteredObjectName(new CCopasiStaticString(metab.getObjectName()).getCN().getString()));
			 }
			 
			 // after each entry, we need a separator
			 if(i!=iMax-1)
			 {
				 body.add(new CRegisteredObjectName(report.getSeparator().getCN().getString()));
				 header.add(new CRegisteredObjectName(report.getSeparator().getCN().getString()));
			 }

			 
		 }
		 body.add(new CRegisteredObjectName(report.getSeparator().getCN().getString()));
		 header.add(new CRegisteredObjectName(report.getSeparator().getCN().getString()));
		 
		 iMax =(int) model.getModelValues().size();
		 for (i = 0;i < iMax;++i)
		 {
			 CModelValue modelValue = model.getModelValue(i);
			 assert modelValue != null;
			 
				 body.add(new CRegisteredObjectName(modelValue.getObject(new CCopasiObjectName("Reference=Value")).getCN().getString()));
				 // add the corresponding id to the header
				 header.add(new CRegisteredObjectName(new CCopasiStaticString(modelValue.getObjectName()).getCN().getString()));
				 // after each entry, we need a separator
				 if(i!=iMax-1)
				 {
					 body.add(new CRegisteredObjectName(report.getSeparator().getCN().getString()));
					 header.add(new CRegisteredObjectName(report.getSeparator().getCN().getString()));
				 }
		 }


		 // get the trajectory task object
		 CTrajectoryTask trajectoryTask = (CTrajectoryTask)dataModel.getTask("Time-Course");
		 assert trajectoryTask != null;
		 // if there isn't one
		 if (trajectoryTask == null)
		 {
			 // create a new one
			 trajectoryTask = new CTrajectoryTask();

			 // add the new time course task to the task list
			 // this method makes sure that the object is now owned 
			 // by the list and that it does not get deleted by SWIG
			 dataModel.getTaskList().addAndOwn(trajectoryTask);
		 }

		 // run a deterministic time course
		 trajectoryTask.setMethodType(CCopasiMethod.deterministic);

		 // pass a pointer of the model to the problem
		 trajectoryTask.getProblem().setModel(dataModel.getModel());

		 // actiavate the task so that it will be run when the model is saved
		 // and passed to CopasiSE
		 trajectoryTask.setScheduled(true);

		 // set the report for the task
		 trajectoryTask.getReport().setReportDefinition(report);
		 // set the output filename
		 trajectoryTask.getReport().setTarget(fileName.substring(0,fileName.lastIndexOf("."))+".txt");
		 // don't append output if the file exists, but overwrite the file
		 trajectoryTask.getReport().setAppend(false);
		 
		 // get the problem for the task to set some parameters
		 CTrajectoryProblem problem = (CTrajectoryProblem)trajectoryTask.getProblem();

		 // simulate 100 steps
		 problem.setStepNumber(steps);
		 // start at time 0
		 dataModel.getModel().setInitialTime(0.0);
		 // simulate a duration of 10 time units
		 problem.setDuration(maxTime);
		 // tell the problem to actually generate time series data
		 problem.setTimeSeriesRequested(true);

		 // set some parameters for the LSODA method through the method
		 CTrajectoryMethod method = (CTrajectoryMethod)trajectoryTask.getMethod();

		 CCopasiParameter parameter = method.getParameter("Absolute Tolerance");
		 assert parameter != null;
		 assert parameter.getType() == CCopasiParameter.DOUBLE;
		 parameter.setDblValue(1.0e-12);

		 boolean result=true;
		 try
		 {
			 // now we run the actual trajectory
			 result=trajectoryTask.process(true);
		 }
		 catch (Exception ex)
		 {
				fileName = fileName.replace('\\','/');
		    	String baseName = fileName.substring(fileName.lastIndexOf("/")+1,fileName.lastIndexOf("_"));
		    
			 if(!BiomodelTest.id_delay.contains(baseName)) ex.printStackTrace();
			// System.err.println( "Error. Running the time course simulation failed." );
			 // check if there are additional error messages
			 if (CCopasiMessage.size() > 0)
			 {
				 // print the messages in chronological order
				// System.err.println(CCopasiMessage.getAllMessageText(true));
			 }
			 throw ex;
		 }
		 if(result==false)
		 {
			 //System.err.println( "An error occured while running the time course simulation." );
			 // check if there are additional error messages
			 if (CCopasiMessage.size() > 0)
			 {
				 // print the messages in chronological order
				 //System.err.println(CCopasiMessage.getAllMessageText(true));
			 }
			 throw new Exception("An error occured while running the time course simulation.");
		 }
		
		
	 }
	 
	 
	 
	 
	 static String RCode_generatePlotsFromTXTs = new String(
			    "table1 = read.csv(file1, header = TRUE, sep = \",\", quote=\"\\\"\", dec=\".\",fill = FALSE)"+System.getProperty("line.separator")+
			    "\n"+System.getProperty("line.separator")+
				"table2 = read.csv(file2, header = TRUE, sep = \",\", quote=\"\\\"\", dec=\".\",fill = FALSE)"+System.getProperty("line.separator")+
				"\n"+System.getProperty("line.separator")+
				"\n"+	System.getProperty("line.separator")+			
				"coordinates = which(abs(table1-table2)==max(abs(table1-table2),na.rm = TRUE),arr.ind=TRUE)"+System.getProperty("line.separator")+
				"both=coordinates[1,]"+System.getProperty("line.separator")+
				"row1=both[1]"+System.getProperty("line.separator")+
				"col1=both[2]"+System.getProperty("line.separator")+
				"max_percentage_error = 100*abs(table1[row1,col1]-table2[row1,col1])/(max(abs(table1[row1,col1]),abs(table2[row1,col1])))"+System.getProperty("line.separator")+
				"if(length(which((table1 == table2)==FALSE))>0) {"+System.getProperty("line.separator")+
				"	subtitle = (paste(colnames(table1)[col1],table1[row1,col1],table2[row1,col1], \"  percentageError=\", max_percentage_error))"+System.getProperty("line.separator")+
				"} else {"+System.getProperty("line.separator")+
				"	subtitle =\".\"	"+System.getProperty("line.separator")+
				"}"+System.getProperty("line.separator")+
				"title1=baseName"+System.getProperty("line.separator")+
				"plot(table1[,1],table1[,2], type=\"l\",xlab=\"\",ylab=\"\", ylim=c(min(0,min(table1[,2:ncol(table1)],na.rm = TRUE)),max(table1[,2:ncol(table1)],na.rm = TRUE)))"+System.getProperty("line.separator")+
				"title(main=title1, col.main=\"red\",  sub=subtitle, col.sub=\"blue\") "+System.getProperty("line.separator")+
				"for(i in 3:ncol(table1)) {"+System.getProperty("line.separator")+
				"	lines(table1[,1], table1[,i])"+System.getProperty("line.separator")+
				"}"+System.getProperty("line.separator")+
				"for(i in 2:ncol(table2)) {"+System.getProperty("line.separator")+
					"lines(table2[,1], table2[,i],col=2)"+System.getProperty("line.separator")+
				"}"+System.getProperty("line.separator")+
				"nameSpeciesWithError = colnames(table1)[col1]"+System.getProperty("line.separator")+
				"if(is.na(max_percentage_error)) {" +System.getProperty("line.separator")+
				"	max_percentage_error = 0"+System.getProperty("line.separator")+
				"   nameSpeciesWithError = \"none\""+System.getProperty("line.separator")+
				"}"+System.getProperty("line.separator")+
				"logData = c(\"BIOMD0000000\", "+System.getProperty("line.separator")+
				"		substr(file2, nchar(file2)-6, nchar(file2)-4), "+System.getProperty("line.separator")+
				"		max_percentage_error,"+System.getProperty("line.separator")+
				"		table1[1,1], "+System.getProperty("line.separator")+
				"		table1[nrow(table1),1],"+System.getProperty("line.separator")+
				"		nrow(table1), "+System.getProperty("line.separator")+
				"		nameSpeciesWithError)"+System.getProperty("line.separator")+
				"write.table(sep = \",\", append=TRUE, t(logData), quote = FALSE, file=logFileName, row.names=FALSE, col.names=FALSE)"+System.getProperty("line.separator")+
				"\n"
			);
}
