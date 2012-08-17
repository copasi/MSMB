/* Generated by JTB 1.4.4 */
package parsers.mathExpression.syntaxtree;

import parsers.mathExpression.visitor.*;

/**
 * JTB node class for the production ArgumentList_MultistateSum_Selectors:<br>
 * Corresponding grammar :<br>
 * nodeToken -> < SEMICOLON ><br>
 * selector -> Selector()<br>
 * nodeListOptional -> ( #0 < SEMICOLON > #1 Selector() )*<br>
 */
public class ArgumentList_MultistateSum_Selectors implements INode {

  /** A child node */
  public NodeToken nodeToken;

  /** A child node */
  public Selector selector;

  /** A child node */
  public NodeListOptional nodeListOptional;

  /** The serial version uid */
  private static final long serialVersionUID = 144L;

  /**
   * Constructs the node with all its children nodes.
   *
   * @param n0 first child node
   * @param n1 next child node
   * @param n2 next child node
   */
  public ArgumentList_MultistateSum_Selectors(final NodeToken n0, final Selector n1, final NodeListOptional n2) {
    nodeToken = n0;
    selector = n1;
    nodeListOptional = n2;
  }

  /**
   * Constructs the node with only its non NodeToken child node(s).
   *
   * @param n0 first child node
   * @param n1 next child node
   */
  public ArgumentList_MultistateSum_Selectors(final Selector n0, final NodeListOptional n1) {
    nodeToken = new NodeToken(";");
    selector = n0;
    nodeListOptional = n1;
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
