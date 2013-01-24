/* Generated by JTB 1.4.7 */
package msmb.parsers.multistateSpecies.syntaxtree;

import msmb.parsers.multistateSpecies.visitor.*;

/**
 * JTB node class for the production CompleteMultistateSpecies_RangeString:<br>
 * Corresponding grammar:<br>
 * multistateSpecies_SiteSingleElement -> MultistateSpecies_SiteSingleElement()<br>
 * nodeToken -> <EOF><br>
 */
public class CompleteMultistateSpecies_RangeString implements INode {

  /** Child node 1 */
  public MultistateSpecies_SiteSingleElement multistateSpecies_SiteSingleElement;

  /** Child node 2 */
  public NodeToken nodeToken;

  /** The serial version UID */
  private static final long serialVersionUID = 147L;

  /**
   * Constructs the node with all its children nodes.
   *
   * @param n0 - first child node
   * @param n1 - next child node
   */
  public CompleteMultistateSpecies_RangeString(final MultistateSpecies_SiteSingleElement n0, final NodeToken n1) {
    multistateSpecies_SiteSingleElement = n0;
    nodeToken = n1;
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
