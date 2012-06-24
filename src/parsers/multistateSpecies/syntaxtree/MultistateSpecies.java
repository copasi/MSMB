/* Generated by JTB 1.4.4 */
package parsers.multistateSpecies.syntaxtree;

import parsers.multistateSpecies.visitor.*;

/**
 * JTB node class for the production MultistateSpecies:<br>
 * Corresponding grammar :<br>
 * multistateSpecies_Name -> MultistateSpecies_Name()<br>
 * nodeOptional -> ( #0 < OPEN_R > #1 MultistateSpecies_SingleStateDefinition()<br>
 * ............ .. . #2 ( $0 < SITE_NAMES_SEPARATOR > $1 MultistateSpecies_SingleStateDefinition() )* #3 < CLOSED_R > )?<br>
 */
public class MultistateSpecies implements INode {

  /** A child node */
  public MultistateSpecies_Name multistateSpecies_Name;

  /** A child node */
  public NodeOptional nodeOptional;

  /** The serial version uid */
  private static final long serialVersionUID = 144L;

  /**
   * Constructs the node with all its children nodes.
   *
   * @param n0 first child node
   * @param n1 next child node
   */
  public MultistateSpecies(final MultistateSpecies_Name n0, final NodeOptional n1) {
    multistateSpecies_Name = n0;
    nodeOptional = n1;
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
