package gui;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import model.MultiModel;

import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.DefaultCompletionProvider;

import parsers.mathExpression.MR_Expression_Parser;
import parsers.mathExpression.MR_Expression_ParserConstantsNOQUOTES;
import parsers.mathExpression.syntaxtree.CompleteExpression;
import parsers.mathExpression.visitor.GetElementBeforeSpecialExtension;
import utility.AutocompleteDB;

public class AutoCompleteKeyLister implements KeyListener {
	AutoCompletion autoCompletion = null;
	AutocompleteDB autocomDB = null;
	boolean isInitialExpression = false;
	MultiModel multiModel = null;
	
	public AutoCompleteKeyLister(AutoCompletion ac, MultiModel mm, boolean isInitialExpr) { 
		this.autoCompletion = ac; 
		multiModel = mm;
		autocomDB = new AutocompleteDB(multiModel);
		isInitialExpression = isInitialExpr;
		int mask = InputEvent.CTRL_MASK;
		ac.setTriggerKey(KeyStroke.getKeyStroke(KeyEvent.VK_O, mask));
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if ((e.getModifiers() & InputEvent.CTRL_MASK) != 0 && (keyCode == KeyEvent.VK_O) ) {
			JTextField source = (JTextField)e.getSource();
			String testo = source.getText();
			int caret = source.getCaretPosition();
			if(caret>=0) {
				char prev_char = ' ';
				if(caret > 0) prev_char = testo.charAt(caret-1);
				   String prevText = testo.substring(0,caret);
					char trigger = '.';
					if(prev_char==' ') trigger = '%';
					if(!prevText.endsWith(new String()+trigger)) {
						String lastPart = null;
						if(testo.length() >= caret+1) lastPart=testo.substring(caret+1,testo.length());
						String complete = prevText + trigger;
						if(lastPart!=null) complete+= lastPart;
						source.setText(complete);
					}
					int numOfPrevQuotes = prevText.split("\"").length-1; //if this is odd it means that the dot is between quotes so no autocompletion
					if(numOfPrevQuotes%2==0) {
						DefaultCompletionProvider provider = null;
						try {
							String lastElement = getLastElement(prevText);
							provider =  autocomDB.getDefaultCompletionProvider(trigger,lastElement,isInitialExpression); 
						} catch (Exception ex) {
							//ex.printStackTrace();
							//provider = autocomDB.getDefaultCompletionProvider(trigger); 
							provider = null;
							JOptionPane.showMessageDialog(null, "Parsing error in the expression. \nNo contextual autocompletion available at this point.", "No contextual autocompletion", JOptionPane.ERROR_MESSAGE);
						} finally {
							if(provider!= null) { 
								autoCompletion.setCompletionProvider(provider);
								autoCompletion.doCompletion();
							}
						}
				} 
		} 
		}
		
	}
	
	
	private String getLastElement(String subString) throws Exception {
		boolean endWithDot = false;
		if(subString.endsWith(".")) {
			endWithDot = true;
			subString = subString.substring(0,subString.length()-1);
		}
		String ret = new String();
		subString += MR_Expression_ParserConstantsNOQUOTES.getTokenImage(MR_Expression_ParserConstantsNOQUOTES.MY_SPECIAL_EXTENSION);
		InputStream is = new ByteArrayInputStream(subString.getBytes("UTF-8"));
		MR_Expression_Parser parser = new MR_Expression_Parser(is);
		CompleteExpression root = parser.CompleteExpression();
		GetElementBeforeSpecialExtension name = new GetElementBeforeSpecialExtension();
		root.accept(name);
		ret  = name.getElementName();
		if(endWithDot) ret += ".";
		return ret;
	}
	
	
	
	@Override
	public void keyPressed(KeyEvent e) {}
	@Override
	public void keyTyped(KeyEvent e) {}
}