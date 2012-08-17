package utility;

import java.util.HashMap;
import java.util.Vector;

import model.MultiModel;

import org.fife.ui.autocomplete.BasicCompletion;
import org.fife.ui.autocomplete.DefaultCompletionProvider;

import parsers.mathExpression.MR_Expression_ParserConstants;
import parsers.mathExpression.MR_Expression_ParserConstantsNOQUOTES;

public class AutocompleteDB {
	
	private final String initial = new String("_INITIAL");
	private final String constantString = new String("_CONST");
	private final String all = new String("_ALL");
	private final String conflictString = new String("_CONFLICT");
	MultiModel multiModel = null;
	
	HashMap<String, Vector<String>> completionOptions = new HashMap<String, Vector<String>>();
	HashMap<String, String> optionShortDescription = new HashMap<String, String>();
	HashMap<String, String> optionSummary = new HashMap<String, String>();
	
	public AutocompleteDB(MultiModel mm) {
		multiModel = mm;
		fillShortDescription();
		fillSummary();
		
		Vector constants = new Vector();
		String completion = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.CONST_AVOGADRO);
		constants.add(completion);
		completion = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.CONST_QUANTITY_CONV_FACTOR);
		constants.add(completion);
		completion = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.CONST_MODEL_TIME);
		constants.add(completion);
		completion = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.CONST_MODEL_TIME_INITIAL);
		constants.add(completion);
		
		String condition = constantString;
		Vector element = new Vector();	
		element.addAll(constants);
		completionOptions.put(condition, element);
	
		
		
		condition = conflictString+"_C_G_S";
		element = new Vector();	
		completion = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_COMPARTMENT);
		element.add(completion);
		completion = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_GLOBALQ);
		element.add(completion);
		completion = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_SPECIES);
		element.add(completion);
		completionOptions.put(condition, element);
		
		condition = conflictString+"_C_G";
		element = new Vector();	
		completion = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_COMPARTMENT);
		element.add(completion);
		completion = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_GLOBALQ);
		element.add(completion);
		completionOptions.put(condition, element);
		
		
		
		condition = conflictString+"_C_S";
		element = new Vector();	
		completion = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_COMPARTMENT);
		element.add(completion);
		completion = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_SPECIES);
		element.add(completion);
		completionOptions.put(condition, element);
		
		condition = conflictString+"_G_S";
		element = new Vector();	
		completion = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_GLOBALQ);
		element.add(completion);
		completion = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_SPECIES);
		element.add(completion);
		completionOptions.put(condition, element);
		
		condition = all;
		element = new Vector();	
		element.addAll(constants);
		completion = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_COMPARTMENT);
		element.add(completion);
		completion = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_SPECIES);
		element.add(completion);
		completion = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_CONC);
		element.add(completion);
		completion = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_PARTICLE);
		element.add(completion);	
		completion = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_TRANS);
		element.add(completion);	
		completion = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_RATE);
		element.add(completion);	
		completion = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_INIT);
		element.add(completion);	
		completion = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_GLOBALQ);
		element.add(completion);
		completionOptions.put(condition, element);
		
		
		condition = Constants.TitlesTabs.SPECIES.description + initial;
		element = new Vector();	
		 completion = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_SPECIES);
		element.add(completion);
		completion = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_CONC);
		completion += MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_INIT);
		element.add(completion);
		completion = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_PARTICLE);
		completion += MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_INIT);
		element.add(completion);	
		completionOptions.put(condition, element);
		
		
		condition = Constants.TitlesTabs.SPECIES.description;
		element = new Vector();	
		completion = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_SPECIES);
		element.add(completion);
		completion = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_CONC);
		element.add(completion);
		completion = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_PARTICLE);
		element.add(completion);	
		completion = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_TRANS);
		element.add(completion);	
		completion = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_RATE);
		element.add(completion);	
		completion = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_INIT);
		element.add(completion);	
		completionOptions.put(condition, element);
		
		condition = Constants.TitlesTabs.COMPARTMENTS.description + initial;
		element = new Vector();	
		completion = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_COMPARTMENT);
		element.add(completion);
		completion = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_INIT);
		element.add(completion);	
		completionOptions.put(condition, element);
		
		condition = Constants.TitlesTabs.COMPARTMENTS.description;
		element = new Vector();	
		completion = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_COMPARTMENT);
		element.add(completion);
		completion = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_TRANS);
		element.add(completion);	
		completion = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_RATE);
		element.add(completion);	
		completion = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_INIT);
		element.add(completion);	
		completionOptions.put(condition, element);
		
		
		
		condition = Constants.TitlesTabs.GLOBALQ.description + initial;
		element = new Vector();	
		completion = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_GLOBALQ);
		element.add(completion);
		completion = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_INIT);
		element.add(completion);	
		completionOptions.put(condition, element);
		
		
		condition = Constants.TitlesTabs.GLOBALQ.description;
		element = new Vector();	
		completion = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_GLOBALQ);
		element.add(completion);
		completion = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_TRANS);
		element.add(completion);	
		completion = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_RATE);
		element.add(completion);	
		completion = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_INIT);
		element.add(completion);	
		completionOptions.put(condition, element);
		
	}
	
	
	
	private void fillSummary() {
		String key = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_SPECIES);
		String value = "Extension to indicate that the name belongs to the <b>Species</b> list. <br> It is <i>necessary</i> if the same name is used for different entities (e.g. Compartments, Global Quantites)";
		optionSummary.put(key, value);

		key = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_COMPARTMENT);
		value = "Extension to indicate that the name belongs to the <b>Compartment</b> list. <br> It is <i>necessary</i> if the same name is used for different entities (e.g. Species, Global Quantites)";
		optionSummary.put(key, value);
	
		key = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_GLOBALQ);
		value = "Extension to indicate that the name belongs to the <b>Global Quantity</b> list. <br> It is <i>necessary</i> if the same name is used for different entities (e.g. Compartments, Species)";
		optionSummary.put(key, value);
		
		key = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_CONC);
		value = "Extension to refer to the <b>Concentration value</b> of a Species.";
		optionSummary.put(key, value);
		
		key = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_PARTICLE);
		value = "Extension to refer to the <b>Particle Number value</b> of a Species.";
		optionSummary.put(key, value);
		
		key = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_TRANS);
		value = "Extension to refer to a <b>Transient value</b> of an entity.";
		optionSummary.put(key, value);
	
		key = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_RATE);
		value = "Extension to refer to the <b>Rate</b> of an entity.";
		optionSummary.put(key, value);
	
		key = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_INIT);
		value = "Extension to refer to an <b>Initial value</b> of an entity.";
		optionSummary.put(key, value);
		
		key = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.CONST_AVOGADRO);
		value = "Constant referring to the <b>Avogadro Number</b>.";
		optionSummary.put(key, value);
		
		key = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.CONST_MODEL_TIME);
		value = "Constant referring to the <b>Model time</b>.";
		optionSummary.put(key, value);
		
		key = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.CONST_MODEL_TIME_INITIAL);
		value = "Constant referring to the <b>Model initial time</b>.";
		optionSummary.put(key, value);
		
		key = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.CONST_QUANTITY_CONV_FACTOR);
		value = "Constant referring to the <b>Quantity conversion factor</b>.";
		optionSummary.put(key, value);
	}



	private void fillShortDescription() {
		
		String key = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_SPECIES);
		String value = "Species extension";
		optionShortDescription.put(key, value);
	
		key = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_COMPARTMENT);
		value = "Compartment extension";
		optionShortDescription.put(key, value);
	
		key = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_GLOBALQ);
		value = "Global quantity extension";
		optionShortDescription.put(key, value);
		
		key = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_CONC);
		value = "Concentration value";
		optionShortDescription.put(key, value);
		
		key = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_PARTICLE);
		value = "Particle Number value";
		optionShortDescription.put(key, value);
		
		key = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_TRANS);
		value = "Transient value";
		optionShortDescription.put(key, value);
	
		key = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_RATE);
		value = "Rate value";
		optionShortDescription.put(key, value);
	
		key = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_INIT);
		value = "Initial value";
		optionShortDescription.put(key, value);
	
	}



	public DefaultCompletionProvider getDefaultCompletionProvider(char trigger, String element, boolean isInitialExpression) {
		boolean endWithDot = false;
		
		
		System.out.println("element before " +element);
		if(element.endsWith(".")) {
			endWithDot = true;
			element = element.substring(0,element.length()-1);
		}
		System.out.println("element after " +element);
		
		if(element.length() ==0) {
			if(trigger == '%') {
				DefaultCompletionProvider ret = new DefaultCompletionProvider();
				Vector<String> completions = completionOptions.get(constantString);
				if(completions!= null) {
					for(int i = 0; i<completions.size(); i++) {
						if(completions.get(i).startsWith(Character.toString(trigger))) {
							String shortD = optionShortDescription.get(completions.get(i));
							String summary = optionSummary.get(completions.get(i));
							ret.addCompletion(new BasicCompletion(ret, completions.get(i).substring(1)+" ",shortD, summary)); // substring because I have to get rid of the first char that has already been typed to trigger this
						}
					}
				}
				return ret;
			}
		}
		
		Vector<Integer> definedInTables = null;
		
		String suffix = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_COMPARTMENT);
		if(element.endsWith(suffix)) {
			element = element.substring(0, suffix.length()-2);
			definedInTables = new Vector<Integer>();
			definedInTables.add(new Integer(Constants.TitlesTabs.COMPARTMENTS.index));
		} else {
			suffix = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_SPECIES);
			if(element.endsWith(suffix)) {
				element = element.substring(0, suffix.length()-2);
				definedInTables = new Vector<Integer>();
				definedInTables.add(new Integer(Constants.TitlesTabs.SPECIES.index));
			} else {
				suffix = MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstants.EXTENSION_GLOBALQ);
				if(element.endsWith(suffix)) {
					element = element.substring(0, suffix.length()-2);
					definedInTables = new Vector<Integer>();
					definedInTables.add(new Integer(Constants.TitlesTabs.GLOBALQ.index));
				} 
			}
		}
		
		System.out.println("definedInTables: before " +definedInTables);
		System.out.println("element " +element);
		
		if(definedInTables==null) {
			definedInTables = multiModel.getWhereNameIsUsed(element);	
		}
		
		Vector<String> completions = new Vector<String>();
		System.out.println("definedInTables: " +definedInTables);
		if(definedInTables.size() == 1) {
			String where = new String();
			if(definedInTables.get(0).intValue()==Constants.TitlesTabs.SPECIES.index) where =  Constants.TitlesTabs.SPECIES.description;
			else if(definedInTables.get(0).intValue()==Constants.TitlesTabs.COMPARTMENTS.index) where =  Constants.TitlesTabs.COMPARTMENTS.description;
			else if(definedInTables.get(0).intValue()==Constants.TitlesTabs.GLOBALQ.index) where =  Constants.TitlesTabs.GLOBALQ.description;
			if(isInitialExpression) where += initial;
			completions.addAll(completionOptions.get(where));
		}
		else {
			String conflicts = conflictString;
			boolean confl_S = false;
			boolean confl_G = false;
			boolean confl_C = false;
			
			for(int i = 0; i < definedInTables.size(); i++) {
				if(definedInTables.get(i).intValue() == Constants.TitlesTabs.SPECIES.index) confl_S = true;
				if(definedInTables.get(i).intValue() == Constants.TitlesTabs.COMPARTMENTS.index) confl_C = true;
				if(definedInTables.get(i).intValue() == Constants.TitlesTabs.GLOBALQ.index) confl_G = true;
			}
			
			if(confl_C) conflicts += "_C";
			if(confl_G) conflicts += "_G";
			if(confl_S) conflicts += "_S";
			
			completions.addAll(completionOptions.get(conflicts));
			System.out.println("completionOptions: " +completionOptions);
			System.out.println("conflicts: " +conflicts);
		}
		
	
		
		
		DefaultCompletionProvider ret = new DefaultCompletionProvider();
			for(int i = 0; i<completions.size(); i++) {
				if(completions.get(i).startsWith(Character.toString(trigger))) {
					String shortD = optionShortDescription.get(completions.get(i));
					String summary = optionSummary.get(completions.get(i));
					String tmp_compl = completions.get(i);
					//if(excludeFirstChar)
						tmp_compl= tmp_compl.substring(1); // substring because I have to get rid of the first char that has already been typed to trigger this
					ret.addCompletion(new BasicCompletion(ret, tmp_compl+" ",shortD, summary)); 
					System.out.println("addedCompl.= "+ tmp_compl);
				}
			}
			return ret;

		
	}

	/*public DefaultCompletionProvider getDefaultCompletionProvider(char trigger) {
	
			DefaultCompletionProvider ret = new DefaultCompletionProvider();
			Vector<String> completions = completionOptions.get(all);
			if(completions!= null) {
				for(int i = 0; i<completions.size(); i++) {
					if(completions.get(i).startsWith(Character.toString(trigger))) {
						String shortD = optionShortDescription.get(completions.get(i));
						String summary = noContextualAutocomplete + "<br><br>" +optionSummary.get(completions.get(i));
						ret.addCompletion(new BasicCompletion(ret, completions.get(i).substring(1)+" ", shortD, summary)); // substring because I have to get rid of the first char that has already been typed to trigger this
					}
				}
			}
			return ret;
		
	}*/

}
