/* Generated by JTB 1.4.4 */
package parsers.mathExpression.syntaxtree;

import parsers.mathExpression.visitor.*;

/**
 * JTB node class for the production PossibleExtensions:<br>
 * Corresponding grammar :<br>
 * nodeChoice -> ( %0 < EXTENSION_CONC ><br>
 * .......... .. | %1 < EXTENSION_COMPARTMENT ><br>
 * .......... .. | %2 < EXTENSION_PARTICLE ><br>
 * .......... .. | %3 < EXTENSION_TRANS ><br>
 * .......... .. | %4 < EXTENSION_INIT ><br>
 * .......... .. | %5 < EXTENSION_RATE ><br>
 * .......... .. | %6 < EXTENSION_SPECIES ><br>
 * .......... .. | %7 < EXTENSION_GLOBALQ ><br>
 * .......... .. | %8 < MY_SPECIAL_EXTENSION > )<br>
 * nodeListOptional -> ( PossibleExtensions() )*<br>
 */
public class PossibleExtensions implements INode {

  /** A child node */
  public NodeChoice nodeChoice;

  /** A child node */
  public NodeListOptional nodeListOptional;

  /** The serial version uid */
  private static final long serialVersionUID = 144L;

  /**
   * Constructs the node with all its children nodes.
   *
   * @param n0 first child node
   * @param n1 next child node
   */
  public PossibleExtensions(final NodeChoice n0, final NodeListOptional n1) {
    nodeChoice = n0;
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
