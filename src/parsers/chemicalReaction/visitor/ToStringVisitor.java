package parsers.chemicalReaction.visitor;

import parsers.chemicalReaction.syntaxtree.*;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Iterator;


public class ToStringVisitor extends DepthFirstVoidVisitor {

	private PrintWriter out;
	public ToStringVisitor(final OutputStream o)  { out = new PrintWriter(o, true); }
	public void flushWriter()  { out.flush(); }

	@Override
	public void visit(final NodeToken n) {   printToken(n.tokenImage); }

	private void printToken(final String s) {	out.print(s);	out.flush();	}

	public static String toString(INode element) {
		ByteArrayOutputStream string = new ByteArrayOutputStream();
		ToStringVisitor toString = new ToStringVisitor(string);
		element.accept(toString);
		return string.toString();
	}
}
