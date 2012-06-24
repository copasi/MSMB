/* Generated by JTB 1.4.4 */
package parsers.mathExpression.syntaxtree;

import parsers.mathExpression.visitor.*;

/**
 * JTB node class for the production SingleFunctionCall:<br>
 * Corresponding grammar :<br>
 * name -> Name()<br>
 * nodeToken -> < LPAREN ><br>
 * argumentList -> ArgumentList()<br>
 * nodeToken1 -> < RPAREN ><br>
 * nodeToken2 -> < EOF ><br>
 */
public class SingleFunctionCall implements INode {

  /** A child node */
  public Name name;

  /** A child node */
  public NodeToken nodeToken;

  /** A child node */
  public ArgumentList argumentList;

  /** A child node */
  public NodeToken nodeToken1;

  /** A child node */
  public NodeToken nodeToken2;

  /** The serial version uid */
  private static final long serialVersionUID = 144L;

  /**
   * Constructs the node with all its children nodes.
   *
   * @param n0 first child node
   * @param n1 next child node
   * @param n2 next child node
   * @param n3 next child node
   * @param n4 next child node
   */
  public SingleFunctionCall(final Name n0, final NodeToken n1, final ArgumentList n2, final NodeToken n3, final NodeToken n4) {
    name = n0;
    nodeToken = n1;
    argumentList = n2;
    nodeToken1 = n3;
    nodeToken2 = n4;
  }

  /**
   * Constructs the node with only its non NodeToken child node(s).
   *
   * @param n0 first child node
   * @param n1 next child node
   */
  public SingleFunctionCall(final Name n0, final ArgumentList n1) {
    name = n0;
    nodeToken = new NodeToken("(");
    argumentList = n1;
    nodeToken1 = new NodeToken(")");
    nodeToken2 = new NodeToken("");
  }

  /**
   * Accepts the IRetArguVisitor visitor.
   *
   * @param <R> the user return type
   * @param <A> the user argument type
   * @param vis the visitor
   * @param argu a user chosen argument
   * @return a user chosen return information
   */
  public <R, A> R accept(final IRetArguVisitor<R, A> vis, final A argu) {
    return vis.visit(this, argu);
  }

  /**
   * Accepts the IRetVisitor visitor.
   *
   * @param <R> the user return type
   * @param vis the visitor
   * @return a user chosen return information
   */
  public <R> R accept(final IRetVisitor<R> vis) {
    return vis.visit(this);
  }

  /**
   * Accepts the IVoidArguVisitor visitor.
   *
   * @param <A> the user argument type
   * @param vis the visitor
   * @param argu a user chosen argument
   */
  public <A> void accept(final IVoidArguVisitor<A> vis, final A argu) {
    vis.visit(this, argu);
  }

  /**
   * Accepts the IVoidVisitor visitor.
   *
   * @param vis the visitor
   */
  public void accept(final IVoidVisitor vis) {
    vis.visit(this);
  }

}
