package msmb.commonUtilities;

import java.util.Vector;

import javax.swing.JMenuBar;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeListener;

import msmb.model.Compartment;
import msmb.model.GlobalQ;
import msmb.model.Species;

import java.awt.*;

public interface MSMB_Interface {
	public JTabbedPane getMSMB_MainTabPanel();
	public JMenuBar getMSMB_MenuBar();
	
	public void loadFromCopasiKey(String copasiKey)  throws Exception;
	public boolean saveToCopasiKey(String copasiKey) ;
	
	public void loadFromMSMB(byte[] msmbByteArray);
	public byte[]  saveToMSMB();
	
	
	public Vector<String> getMSMB_listOfSpecies();
	public Vector<String> getMSMB_listOfGlobalQuantities();
	public Vector<String> getMSMB_listOfCompartments();

	
	public Species getMSMB_getSpecies(String name);
	public GlobalQ getMSMB_getGlobalQuantity(String name);
	public Compartment getMSMB_getCompartment(String name);
	

	public Font getCustomFont();
//	public void addChangeListener(ChangeListener c, Class cl);
	public void addChangeListener(ChangeListener c, MSMB_Element element);	
}
