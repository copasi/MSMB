/* Generated by JTB 1.4.7 */
package msmb.parsers.mathExpression.syntaxtree;

import msmb.parsers.mathExpression.visitor.*;

/**
 * JTB node class for the production ArgumentList_MultistateSum_Selectors:<br>
 * Corresponding grammar:<br>
 * nodeToken -> <SEMICOLON><br>
 * selector -> Selector()<br>
 * nodeListOptional -> ( #0 <SEMICOLON> #1 Selector() )*<br>
 */
public class ArgumentList_MultistateSum_Selectors implements INode {

  /** Child node 1 */
  public NodeToken nodeToken;

  /** Child node 2 */
  public Selector selector;

  /** Child node 3 */
  public NodeListOptional nodeListOptional;

  /** The serial version UID */
  private static final long serialVersionUID = 147L;

  /**
   * Constructs the node with all its children nodes.
   *
   * @param n0 - first child node
   * @param n1 - next child node
   * @param n2 - next child node
   */
  public ArgumentList_MultistateSum_Selectors(final NodeToken n0, final Selector n1, final NodeListOptional n2) {
    nodeToken = n0;
    selector = n1;
    nodeListOptional = n2;
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