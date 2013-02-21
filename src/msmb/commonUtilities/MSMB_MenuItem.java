package msmb.commonUtilities;

import msmb.utility.Constants;

public enum MSMB_MenuItem
{
	FILE("File"),
		NEW("New"),
		OPEN("Open"),
			OPEN_CPS(".cps"),		
			OPEN_SBML(".sbml"),
			OPEN_MSMB(".msmb"),
		SAVE("Save"),
			SAVE_CPS("as .cps"),		
			SAVE_MSMB("as .msmb"),
		EXPORT("Export"),
			EXPORT_SBML("to .sbml"),
			EXPORT_XPP("to .xpp"),
		PRINT_TABLES_PDF("Print tables to PDF"),
		PREFERENCES("Preferences..."),
		RECENT("Recent files"),	
		EXIT("Exit"),	

	EDIT("Edit"),
		SHOW_EXPANDED_EXPR("Show expanded expression in table..."),
			REACTIONS(Constants.TitlesTabs.REACTIONS.description),
			SPECIES(Constants.TitlesTabs.SPECIES.description),
			GLOBALQ(Constants.TitlesTabs.GLOBALQ.description),
			COMPARTMENTS(Constants.TitlesTabs.COMPARTMENTS.description),
			EVENTS(Constants.TitlesTabs.EVENTS.description),
		ADD_REVERSE_REACTION("Add reverse reaction"),
		DELETE_ELEMENT("Delete element..."),
		VALIDATE("Validate model"),
		MULTISTATE_BUILDER("Multistate builder...");
	
	private final String menuString;
	
	private MSMB_MenuItem(String displayedString)	{		menuString = displayedString;	}
	
	public String getMenuString() { return menuString; }
	
	
	 public static MSMB_MenuItem getEnum(String value) {
		    if(value == null)      {
		    	throw new IllegalArgumentException();
		    }
		    
		    for(MSMB_MenuItem v : values()) {
	            if(value.compareTo(v.menuString)==0) {
	            	return v;
	            }
		    }
	        return null;
	    }
	
}
