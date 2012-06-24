/* Generated by JTB 1.4.4 */
package parsers.multistateSpecies.syntaxtree;

import parsers.multistateSpecies.visitor.*;

/**
 * JTB node class for the production MultistateSpecies_SiteName:<br>
 * Corresponding grammar :<br>
 * nodeChoice -> . %0 < STRING_LITERAL ><br>
 * .......... .. | %1 ( &0 < MULTI_IDENTIFIER ><br>
 * .......... .. . .. | &1 < CLOSED_C ><br>
 * .......... .. . .. | &2 < OPEN_R ><br>
 * .......... .. . .. | &3 < CLOSED_R ><br>
 * .......... .. . .. | &4 < RANGE_SEPARATOR ><br>
 * .......... .. . .. | &5 < SITE_STATES_SEPARATOR > )+<br>
 */
public class MultistateSpecies_SiteName implements INode {

  /** A child node */
  public NodeChoice nodeChoice;

  /** The serial version uid */
  private static final long serialVersionUID = 144L;

  /**
   * Constructs the node with its child node.
   *
   * @param n0 the child node
   */
  public MultistateSpecies_SiteName(final NodeChoice n0) {
    nodeChoice = n0;
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