/* Generated by JTB 1.4.7 */
package msmb.parsers.multistateSpecies.syntaxtree;

import msmb.parsers.multistateSpecies.visitor.*;

/**
 * JTB node class for the production MultistateSpecies_SiteSingleElement:<br>
 * Corresponding grammar:<br>
 * nodeChoice -> ( %0 <STRING_LITERAL><br>
 * .......... .. | %1 ( &0 <CIRCULAR_FLAG><br>
 * .......... .. . .. | &1 <MULTI_IDENTIFIER><br>
 * .......... .. . .. | &2 <NUMBER><br>
 * .......... .. . .. | &3 <OPEN_R><br>
 * .......... .. . .. | &4 <MATH_ELEMENT> )+ )<br>
 * nodeOptional -> ( #0 ( " " )*<br>
 * ............ .. . #1 <RANGE_SEPARATOR><br>
 * ............ .. . #2 ( " " )*<br>
 * ............ .. . #3 ( %0 <STRING_LITERAL><br>
 * ............ .. . .. | %1 ( &0 <MULTI_IDENTIFIER><br>
 * ............ .. . .. . .. | &1 <CIRCULAR_FLAG><br>
 * ............ .. . .. . .. | &2 <NUMBER><br>
 * ............ .. . .. . .. | &3 <OPEN_R><br>
 * ............ .. . .. . .. | &4 <MATH_ELEMENT><br>
 * ............ .. . .. . .. | &5 <CLOSED_R> )+ ) )?<br>
 */
public class MultistateSpecies_SiteSingleElement implements INode {

  /** Child node 1 */
  public NodeChoice nodeChoice;

  /** Child node 2 */
  public NodeOptional nodeOptional;

  /** The serial version UID */
  private static final long serialVersionUID = 147L;

  /**
   * Constructs the node with all its children nodes.
   *
   * @param n0 - first child node
   * @param n1 - next child node
   */
  public MultistateSpecies_SiteSingleElement(final NodeChoice n0, final NodeOptional n1) {
    nodeChoice = n0;
    nodeOptional = n1;
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
