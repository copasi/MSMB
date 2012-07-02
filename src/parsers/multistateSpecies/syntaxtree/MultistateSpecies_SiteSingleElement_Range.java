/* Generated by JTB 1.4.4 */
package parsers.multistateSpecies.syntaxtree;

import parsers.multistateSpecies.visitor.*;

/**
 * JTB node class for the production MultistateSpecies_SiteSingleElement_Range:<br>
 * Corresponding grammar :<br>
 * nodeToken -> < NUMBER ><br>
 * nodeListOptional -> ( " " )*<br>
 * nodeToken1 -> < RANGE_SEPARATOR ><br>
 * nodeListOptional1 -> ( " " )*<br>
 * nodeToken2 -> < NUMBER ><br>
 */
public class MultistateSpecies_SiteSingleElement_Range implements INode {

  /** A child node */
  public NodeToken nodeToken;

  /** A child node */
  public NodeListOptional nodeListOptional;

  /** A child node */
  public NodeToken nodeToken1;

  /** A child node */
  public NodeListOptional nodeListOptional1;

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
  public MultistateSpecies_SiteSingleElement_Range(final NodeToken n0, final NodeListOptional n1, final NodeToken n2, final NodeListOptional n3, final NodeToken n4) {
    nodeToken = n0;
    nodeListOptional = n1;
    nodeToken1 = n2;
    nodeListOptional1 = n3;
    nodeToken2 = n4;
  }

  /**
   * Constructs the node with only its non NodeToken child node(s).
   *
   * @param n0 first child node
   * @param n1 next child node
   * @param n2 next child node
   * @param n3 next child node
   */
  public MultistateSpecies_SiteSingleElement_Range(final NodeToken n0, final NodeListOptional n1, final NodeListOptional n2, final NodeToken n3) {
    nodeToken = n0;
    nodeListOptional = n1;
    nodeToken1 = new NodeToken(":");
    nodeListOptional1 = n2;
    nodeToken2 = n3;
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
