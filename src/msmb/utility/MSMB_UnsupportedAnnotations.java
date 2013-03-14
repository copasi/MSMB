package msmb.utility;

public class MSMB_UnsupportedAnnotations {

	public static String xmlns = "http://jigcell.cs.vt.edu";
	public static enum MSMB_UnsupportedAnnotations_type {
		GLQ_PARAMETER_TYPE_IN_CFUNCTION,
		REACTION_EMPTY_NAME;		
	}
	
	
	String name = new String();
	String annotation = new String();
	
	public MSMB_UnsupportedAnnotations(MSMB_UnsupportedAnnotations_type type, String value) {
		switch(type) {
			case GLQ_PARAMETER_TYPE_IN_CFUNCTION:
				name = xmlns+"_"+value;
				annotation =  "<"+MSMB_xml_annotation.GLQ_TYPE.description+" xmlns=\""+xmlns+"\" value=\""+value+"\"/>";
				break;
			case REACTION_EMPTY_NAME:
				name = xmlns+"_reactionNameEmpty";
				annotation =  "<"+MSMB_xml_annotation.REACTION_EMPTY.description+" xmlns=\""+xmlns+"\" value=\"\"/>";
				break;
			default:
				System.err.println("Not an available MSMB annotation");
				break;
		}
		
	}
	
	public String getName() { return name;  }
	public String getAnnotation() { return annotation; }
	
	

	
	enum MSMB_xml_annotation {
		   GLQ_TYPE("msmb_GLQtype"),
		   REACTION_EMPTY("msmb_reactionEmpty");
		          
		   public final String description;
		   
		   MSMB_xml_annotation(String descr) {
		              this.description = descr;
		    }
	}




	public static boolean is_GlqParTypeInFun(String annotation) {
		return annotation.contains(MSMB_xml_annotation.GLQ_TYPE.description);
	}
	
	public static boolean is_ReactionEmptyName(String annotation) {
		return annotation.contains(MSMB_xml_annotation.REACTION_EMPTY.description);
	}

	public static String extractParName(String annotation) {
		int valStart = annotation.indexOf("value=") + (new String("value=")).length()+1;
		int valEnd = annotation.indexOf('"',valStart);
		return annotation.substring(valStart,valEnd);
	}

	
}





