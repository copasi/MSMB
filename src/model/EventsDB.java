package model;

import java.util.Collection;
import java.util.HashMap;
import java.util.TreeMap;

public class EventsDB {
	TreeMap<Integer, Event> eventVector = new TreeMap<Integer, Event>();
	HashMap<String, Integer> eventIndexes = new HashMap<String, Integer>();
	
	
	public EventsDB() {
		eventVector = new TreeMap<Integer, Event>();
		eventIndexes = new HashMap<String, Integer>();
		eventVector.put(0,null);//rows starts at index 1
	}
	
	public void addChangeEvent(int row, Event f) {	//TO DOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO
		eventVector.put(row, f); 
		eventIndexes.put(f.getName(), eventVector.size());
	}
	
	public Collection<Event> getAllEvents() {
		return this.eventVector.values();
	}
	
	public boolean removeEvent(int toBeRemoved) {
		int size = eventVector.keySet().size();
		//globalQIndexes.remove(globalQVector.get(toBeRemoved+1).getName());
		for(int i = toBeRemoved+1; i < size; i++) {
			Event succ = eventVector.get(i+1);
			if(succ==null) {
				eventVector.remove(eventVector.size()-1);
				break; 
			}
			eventVector.put(i, succ);
			eventIndexes.put(succ.getName(), i);
			
		}
		return true;
}
	
}
