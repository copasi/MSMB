/* Generated by JTB 1.4.7 */
package msmb.parsers.mathExpression.syntaxtree;

import msmb.parsers.mathExpression.visitor.*;

/**
 * JTB node class for the production MultistateSite:<br>
 * Corresponding grammar:<br>
 * name -> Name()<br>
 * nodeToken -> <LBRACE><br>
 * nodeChoice -> ( %0 Name()<br>
 * .......... .. | %1 Literal() )<br>
 * nodeToken1 -> <RBRACE><br>
 */
public class MultistateSite implements INode {

  /** Child node 1 */
  public Name name;

  /** Child node 2 */
  public NodeToken nodeToken;

  /** Child node 3 */
  public NodeChoice nodeChoice;

  /** Child node 4 */
  public NodeToken nodeToken1;

  /** The serial version UID */
  private static final long serialVersionUID = 147L;

  /**
   * Constructs the node with all its children nodes.
   *
   * @param n0 - first child node
   * @param n1 - next child node
   * @param n2 - next child node
   * @param n3 - next child node
   */
  public MultistateSite(final Name n0, final NodeToken n1, final NodeChoice n2, final NodeToken n3) {
    name = n0;
    nodeToken = n1;
    nodeChoice = n2;
    nodeToken1 = n3;
  }

  /**
   * Accepts the IRetArguVisitor visitor.
   *
   * @param <R> the user return type
   * @param <A> the user argument type
   * @param vis - the visitor
   * @param argu - a user chosen argument
   * @return a user chosen return information
   */
  public <R, A> R accept(final IRetArguVisitor<R, A> vis, final A argu) {
    return vis.visit(this, argu);
  }

  /**
   * Accepts the IRetVisitor visitor.
   *
   * @param <R> the user return type
   * @param vis - the visitor
   * @return a user chosen return information
   */
  public <R> R accept(final IRetVisitor<R> vis) {
    return vis.visit(this);
  }

  /**
   * Accepts the IVoidArguVisitor visitor.
   *
   * @param <A> the user argument type
   * @param vis - the visitor
   * @param argu - a user chosen argument
   */
  public <A> void accept(final IVoidArguVisitor<A> vis, final A argu) {
    vis.visit(this, argu);
  }

  /**
   * Accepts the IVoidVisitor visitor.
   *
   * @param vis - the visitor
   */
  public void accept(final IVoidVisitor vis) {
    vis.visit(this);
  }

}
