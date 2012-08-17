package utility;

import model.Function;
import model.MultiModel;
import org.lsmp.djep.xjep.*;
import org.nfunk.jep.*;
import org.nfunk.jep.function.Exp;
import org.nfunk.jep.function.If;
import gui.MainGui;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.FieldPosition;


/**
 * Prints an expression in xppaut ODE terms
 */
public class OdeExpressionVisitor_DELETE_oldParser extends ErrorCatchingVisitor
{

  protected StringBuffer sb;
  protected int mode=0;
  public void setMode(int mode) {
	this.mode = mode;
}

private Hashtable specialRules = new Hashtable();
   private boolean conc = false;
  public static final int FULL_BRACKET = 1;
 // int index_reaction = 0;
  List functionToCompact = null;
  Vector<String> functionsInTheExpression = new Vector<String>();
  List listCompactTerms = null;
   
  MultiModel multiModel = null;
  
  public Vector<String> getFunctionsInTheExpression() {	return functionsInTheExpression;}

/** Creates a visitor to create and print string representations of an expression tree. 
 * @param listFunctionToCompact **/

  public OdeExpressionVisitor_DELETE_oldParser(//int i, 
		  List listFunctionToCompact, MultiModel mm)  { 
	 // index_reaction = i;
	 this.functionToCompact = listFunctionToCompact;
	 NumberFormat formatter = new DecimalFormat("##########################.##########################");
	 setNumberFormat(formatter);
	 multiModel = mm;
  }
  
  public OdeExpressionVisitor_DELETE_oldParser(boolean specificCompactTerms, 
		  List listCompactTerms)  { 
	 // index_reaction = i;
	 this.functionToCompact = null;
	 this.listCompactTerms = listCompactTerms;
	 NumberFormat formatter = new DecimalFormat("##########################.##########################");
	 setNumberFormat(formatter);
  }

  public void print(Node node, PrintStream out)
  {
	 int maxLen = -1;
	sb = new StringBuffer();
	acceptCatchingErrors(node,null);
	if(maxLen == -1)
		out.print(sb);
	else
	{
		while(true)	{
			if(sb.length() < maxLen) {
				out.print(sb);
				return;
			}
			int pos = maxLen-2;
			for(int i=maxLen-2;i>=0;--i) {
				char c = sb.charAt(i);
				if(c == '+' || c == '-' || c == '*' || c == '/'){
					pos = i; break;
				}
			}
			
			out.println(sb.substring(0,pos+1));
			sb.delete(0,pos+1);
		}
	}
  }

  /** Prints on System.out. */
  public void print(Node node) { print(node,System.out); }
    
  /** Prints the tree descending from node with a newline at end. **/

  public void println(Node node,PrintStream out)
  {
	print(node,out);
	out.println("");
  }

  /** Prints on System.out. */
  public void println(Node node) { println(node,System.out); }

  /** returns a String representation of the equation. */
  
  public String toString(Node node)
  {
	sb = new StringBuffer();
	acceptCatchingErrors(node,null);
	return sb.toString();
  }
  
	/**
	 * This interface specifies the method needed to implement a special print rule.
	 * A special rule must implement the append method, which should
	 * call pv.append to add data to the output. For example
	 * <pre>
	 * 	pv.addSpecialRule(Operator.OP_LIST,new PrintVisitor.PrintRulesI()
	 *	{
	 *  	public void append(Node node,PrintVisitor pv) throws ParseException
	 *		{
	 *			pv.append("[");
	 *			for(int i=0;i<node.jjtGetNumChildren();++i)
	 *			{
	 *				if(i>0) pv.append(",");
	 *				node.jjtGetChild(i).jjtAccept(pv, null);
	 *			}
	 *			pv.append("]");
	 *		}});
 	 * </pre>
	 * @author Rich Morris
	 * Created on 21-Feb-2004
	 */
  public interface PrintRulesI
  {
  	/** The method called to append data for the rule. **/
  	public void append(Node node,OdeExpressionVisitor_DELETE_oldParser odeExpressionVisitor) throws ParseException;
  }

  /** Add a string to buffer. Classes implementing PrintRulesI 
   * should call this add the */
  public void append(String s) { sb.append(s); }

  /** Adds a special print rule to be added for a given operator. 
   * TODO Allow special rules for other functions, i.e. not operators. */
  public void addSpecialRule(Operator op,PrintRulesI rules)
  {
  	specialRules.put(op,rules);
  }

/***************** visitor methods ********************************/

	/** print the node with no brackets. */
	private void printNoBrackets(Node node) throws ParseException
	{
		node.jjtAccept(this,null);
	}
	
	/** print a node surrounded by brackets. */
	private void printBrackets(Node node) throws ParseException
	{
		sb.append("(");
		printNoBrackets(node);
		sb.append(")");
	}
	
	/** print a unary operator. */
	private Object visitUnary(ASTFunNode node, Object data) throws ParseException
	{
		Node rhs = node.jjtGetChild(0);
	
		// now print the node
		sb.append(node.getOperator().getSymbol());
		// now the rhs
		if(rhs instanceof ASTFunNode && ((ASTFunNode) rhs).isOperator())
			printBrackets(rhs);	// -(-3) -(1+2) or !(-3)
		else
			printNoBrackets(rhs);
		
		return data;
	}
	
	
	private boolean testLeft(XOperator top,Node lhs)
	{
		if((mode & FULL_BRACKET)!= 0)
		{
			return true;
		}
		else if(lhs instanceof ASTFunNode && ((ASTFunNode) lhs).isOperator())
		{
			XOperator lhsop = (XOperator) ((ASTFunNode) lhs).getOperator();
			if(top == lhsop)
			{
				if(top.getBinding() == XOperator.LEFT	// (1-2)-3 -> 1-2-3
					&& top.isAssociative() )
						return false;
				else if(top.useBindingForPrint())
						return false;
				else
						return true;				// (1=2)=3 -> (1=2)=3
			}
			else if(top.getPrecedence() == lhsop.getPrecedence())
			{
				if(lhsop.getBinding() == XOperator.LEFT && lhsop.isAssociative())
						return false;
				else if(lhsop.useBindingForPrint())
						return false;
				else	return true;
			} 				// (1=2)=3 -> (1=2)=3
				
			else if(top.getPrecedence() > lhsop.getPrecedence()) // (1*2)+3
						return false;
			else
						return true;
		}
		else
			return false;
	
	}
	
	private boolean testMid(XOperator top,Node rhs)
	{
		if((mode & FULL_BRACKET)!= 0)
		{
			return true;
		}
		else if(rhs instanceof ASTFunNode && ((ASTFunNode) rhs).isOperator())
		{
			XOperator rhsop = (XOperator) ((ASTFunNode) rhs).getOperator();
			if(top == rhsop)
			{
				return false;
			}
			else if(top.getPrecedence() == rhsop.getPrecedence())
			{
				return false;	// a+(b-c) -> a+b-c
			}
			else if(top.getPrecedence() > rhsop.getPrecedence()) // 1+(2*3) -> 1+2*3
						return false;
			else
						return true;
		}
		else
			return false;
	}
	
	private boolean testRight(XOperator top,Node rhs)
	{
		if((mode & FULL_BRACKET)!= 0)
		{
			return true;
		}
		else if(rhs instanceof ASTFunNode && ((ASTFunNode) rhs).isOperator())
		{
			XOperator rhsop = (XOperator) ((ASTFunNode) rhs).getOperator();
			if(top == rhsop)
			{
				if(top.getBinding() == XOperator.RIGHT	// 1=(2=3) -> 1=2=3
					|| top.isAssociative() )			// 1+(2-3) -> 1+2-3
						return false;
				return true;				// 1-(2+3) -> 1-(2-3)
			}
			else if(top.getPrecedence() == rhsop.getPrecedence())
			{
				if(top.getBinding() == XOperator.LEFT && top.isAssociative() )			// 1+(2-3) -> 1+2-3)
					return false;	// a+(b-c) -> a+b-c
				return true;		// a-(b+c) -> a-(b+c)
			}
			else if(top.getPrecedence() > rhsop.getPrecedence()) // 1+(2*3) -> 1+2*3
						return false;
			else
						return true;
		}
		else
			return false;
	}
	
	private Object visitNaryBinary(ASTFunNode node,XOperator op) throws ParseException
	{
		int n = node.jjtGetNumChildren();
		for(int i=0;i<n;++i)
		{
			if(i>0) sb.append(op.getSymbol());
			
			Node arg = node.jjtGetChild(i);
			if(testMid(op,arg))
				printBrackets(arg);
			else
				printNoBrackets(arg);
		}
		return null;
	}
	
	public Object visit(ASTFunNode node, Object data) throws ParseException
	{
		
		if(!node.isOperator()) return visitFun(node);
		if(node instanceof PrintRulesI)
		{
			((PrintRulesI) node).append(node,this);
			return null;
		}
		if(node.getOperator()==null)
		{
			throw new ParseException("Null operator in print for "+node);
		}
		if(specialRules.containsKey(node.getOperator()))
		{
			((PrintRulesI) specialRules.get(node.getOperator())).append(node,this);
			return null;
		}
		/*if(node.getPFMC() instanceof org.nfunk.jep.function.List)
		{	
			append("[");
				for(int i=0;i<node.jjtGetNumChildren();++i)
				{
					if(i>0) append(",");
					node.jjtGetChild(i).jjtAccept(this, null);
				}
				append("]");
			return null;
		}*/
			
		if(((XOperator) node.getOperator()).isUnary())
			return visitUnary(node,data);
	
		if(((XOperator) node.getOperator()).isBinary())
		{
			XOperator top = (XOperator) node.getOperator();
			if(node.jjtGetNumChildren()!=2)
				return visitNaryBinary(node,top);
			Node lhs = node.jjtGetChild(0);
			Node rhs = node.jjtGetChild(1);
		
			if(testLeft(top,lhs))
				printBrackets(lhs);
			else
				printNoBrackets(lhs);
			
			// now print the node
			sb.append(node.getOperator().getSymbol());
			// now the rhs
	
			if(testRight(top,rhs))
				printBrackets(rhs);
			else
				printNoBrackets(rhs);
	
		}
		return null;
	}

	
	
	
	/** prints a standard function: fun(arg,arg) */
	private Object visitFun(ASTFunNode node) throws ParseException
	{
		
		String fun = node.getName();
		if(node.getPFMC() instanceof If){
			sb.append("if(");
			Object child = node.jjtGetChild(0).jjtAccept(this, null);
			if(child!= null)sb.append(child);
			sb.append(") then (");
			child = node.jjtGetChild(1).jjtAccept(this, null);
			if(child!= null)sb.append(child);
			sb.append(") else (");
			child = node.jjtGetChild(2).jjtAccept(this, null);
			if(child!= null)sb.append(child);
			sb.append(")");
			return null;
		} 
		else if(node.getPFMC() instanceof Exp){
			sb.append("exp(");
			Object child = node.jjtGetChild(0).jjtAccept(this, null);
			if(child!= null)sb.append(child);
			sb.append(")");
			return null;
		} else if(node.getPFMC() instanceof MyFunctionClass_DELETE_oldParser){
			if (node.jjtGetNumChildren()==1 && node.getName().compareTo("floor")==0) {
				sb.append("floor(");
				Object child = node.jjtGetChild(0).jjtAccept(this, null);
				if(child!= null)sb.append(child+")");
				return null;
			}
		}
			
		Function f = null;
		try {
			f = multiModel.getFunctionByName(fun);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Vector actuals = new Vector();
		PrintVisitor pv = new PrintVisitor();
		NumberFormat formatter = new DecimalFormat("##########################.##########################");
		pv.setNumberFormat(formatter);
		for(int i=0;i<node.jjtGetNumChildren();++i)
		{
			//node.jjtGetChild(i).jjtAccept(this, null);
			String child = pv.toString(node.jjtGetChild(i));
			actuals.add(child);
		}
		
		String r = new String();
		if(functionToCompact==null) {
			if(!listCompactTerms.contains(pv.toString(node))) {
				sb.append(pv.toString(node));
			} else {
				try {
					sb.append("(");
					r = f.getExpandedEquation(actuals);
					Node content = CellParsers.parser.parse(r);
					Object n = content.jjtAccept(this, null);
					if(n!= null)sb.append(n);
					sb.append(")");
				} catch (Exception e) {
					if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
				}
			}
			return null;
		}
		if(functionToCompact.contains(f.getName())) {
			String element = f.getName()+"(";
			for(int i = 0; i < actuals.size()-1; i++){
				element+= actuals.get(i);
				element+= ",";
			}
			element+= actuals.get(actuals.size()-1);
			element+= ")";
			sb.append(element);
			functionsInTheExpression.add(element);
		} else {
			try {
				sb.append("(");
				r = f.getExpandedEquation(actuals);
				Node content = CellParsers.parser.parse(r);
				Object n = content.jjtAccept(this, null);
				if(n!= null)sb.append(n);
				sb.append(")");
			} catch (Exception e) {
				if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
			}
		}
		return null;
	}

	public Object visit(ASTVarNode node, Object data) throws ParseException  {
		String element = node.getName();
		sb.append(element);
		return data;
	}

			
		
	public Object visit(ASTConstant node, Object data) {
		Object val = node.getValue();
		formatValue(val,sb);
		return data;
	}

	private FieldPosition fp = new FieldPosition(NumberFormat.FRACTION_FIELD);

	/** Appends a formatted versions of val to the string buffer.
	 * 
	 * @param val The value to format
	 * @param sb1  The StingBuffer to append to
	 */
	public void formatValue(Object val,StringBuffer sb1)
	{
		if(format != null)
		{
			if(val instanceof Number)
				format.format(val,sb1,fp);
			else
				sb1.append(val);
		}
		else {
			BigDecimal obj_0 = new BigDecimal(val.toString());
			sb1.append(obj_0.stripTrailingZeros());
		}
	}
	
	/** Returns a formated version of the value. */
	public String formatValue(Object val)
	{
	  	StringBuffer sb2 = new StringBuffer();
	  	formatValue(val,sb2);
	  	return sb2.toString();
	}
	/**
	 * Return the current print mode.
	 */
	public int getMode() {
		return mode;
	}
	public boolean getMode(int testmode) {
		return( (this.mode | testmode ) == testmode); 
	}
	
	/** The NumberFormat object used to print numbers. */
	protected NumberFormat format;
	public void setNumberFormat(NumberFormat format)
	{
		this.format = format;
	}

	
}