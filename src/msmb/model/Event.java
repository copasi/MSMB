package msmb.model;

import msmb.gui.MainGui;

import java.util.*;

public class Event {
	private String name = new String();
	String trigger = new String();
	Vector<String> actions = new Vector<String>();
	String notes = new String();
	String delay = new String();
	boolean delayAfterCalculation = true;
	boolean shortCut_recalculateConcentration_changingVolume = false;
	int expandActionVolume = -1;
	
	public String getDelay() {	return delay;}
	public void setDelay(String delay) {	this.delay = delay.trim();	}
	
	public boolean isDelayAfterCalculation() {		return delayAfterCalculation;	}
	public void setDelayAfterCalculation(boolean delayAfterCalculation) {		this.delayAfterCalculation = delayAfterCalculation;	}
	public void setExpandActionVolume(int expandExtension) { this.expandActionVolume = expandExtension; }
	public int getExpandActionVolume() { return this.expandActionVolume; }
	
	
	public Event(String name) {
		//if(name.length()<=0) name = "event";
		this.name = name;
		
		if(MainGui.importFromTables) shortCut_recalculateConcentration_changingVolume = true;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		//if(name.length()<=0) name = "";
		this.name = name;
	}
	public String getTrigger() {
		return trigger;
	}
	public void setTrigger(String trigger) {
		this.trigger = trigger;
	}
	public Vector<String> getActions() {
		return actions;
	}
	
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	public void setActions(String actions) {
		StringTokenizer st = new StringTokenizer(actions, ";");
		while(st.hasMoreTokens())  {
			this.actions.add(st.nextToken().trim());
		}
	}
}
