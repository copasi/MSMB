package gui;

import java.awt.EventQueue;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class RecordAutosave {
		Timer myGlobalTimer = null;
		MainGui mainGui = null;
		private String path;
		private String baseName;
		
	public RecordAutosave(MainGui mainGui) {
		this.mainGui = mainGui;
	}
	
	
	public void startAutosave() {
		if(myGlobalTimer == null) myGlobalTimer = new Timer();
		path = mainGui.getAutosaveDirectory();
		baseName = mainGui.getAutosaveBaseName();
		final Runnable doUpdateCursor = new Runnable() {
				    public void run() {
			    	  try {
			    		  File outputfile = new File(path+"#"+baseName+"_autosaved.multis");
			    		  ExportMultistateFormat.setFile(outputfile);
				          ExportMultistateFormat.exportMultistateFormat(false);
				       
					} catch (Exception e) {
						
						e.printStackTrace();
					}

			    }
			};
			

			TimerTask updateCursorTask = new TimerTask() {
			    public void run() {
			    	EventQueue.invokeLater(doUpdateCursor);
			    }
			};
		myGlobalTimer.schedule(updateCursorTask, 0, mainGui.getAutosaveTime());
	}
	
	public void stopAutosave() {
		myGlobalTimer.cancel();
		myGlobalTimer = null;
	}

public String getPath() {
		
		return path;
	}
	
	
	
	
	

}
