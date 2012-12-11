package msmb.gui;

import javax.swing.JMenuBar;
import javax.swing.JTabbedPane;

public interface MSMB_Interface {
	public JTabbedPane getMSMB_MainTabPanel();
	public JMenuBar getMSMB_MenuBar();
	
	public void importFromCopasiKey(String copasiKey)  throws Exception;
	public int getMSMB_numSpecies();
	
	
}
