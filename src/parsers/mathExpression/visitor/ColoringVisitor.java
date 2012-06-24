package parsers.mathExpression.visitor;

import parsers.mathExpression.syntaxtree.*;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.commons.lang3.tuple.MutablePair;


public class ColoringVisitor extends DepthFirstVoidVisitor {

	
	public ColoringVisitor()  {  }
	
	static Vector colors = new Vector<Color>();
	static Vector elements = new Vector<String>();
	
	@Override
	public void visit(final NodeToken n) {   
		elements.add(n.tokenImage);	
	}

	
	static List<Color> getUniqueColors(int amount) {
	    final int lowerLimit = 0x10;
	    final int upperLimit = 0xE0;    
	    final int colorStep = (int) ((upperLimit-lowerLimit)/Math.pow(amount,1f/3));

	    final List<Color> colors = new ArrayList<Color>(amount);

	    for (int R = lowerLimit;R < upperLimit; R+=colorStep)
	        for (int G = lowerLimit;G < upperLimit; G+=colorStep)
	            for (int B = lowerLimit;B < upperLimit; B+=colorStep) {
	                if (colors.size() >= amount) { //The calculated step is not very precise, so this safeguard is appropriate
	                    return colors;
	                } else {
	                    int color = (R<<16)+(G<<8)+(B);
	                    colors.add(new Color(color));
	                }               
	            }
	    return colors;
	}


	public static MutablePair<Vector<String>, Vector<Color>> color(INode element) {
		ByteArrayOutputStream string = new ByteArrayOutputStream();
		ColoringVisitor toString = new ColoringVisitor();
		toString.elements.clear();
		toString.colors.clear();
		
		element.accept(toString);
		
		MutablePair ret = new MutablePair<Vector<String>, Vector<Color>>();
		ret.setLeft(elements);
		
		colors.addAll(getUniqueColors(elements.size()));
		ret.setRight(colors);
		return ret;
	}
}
