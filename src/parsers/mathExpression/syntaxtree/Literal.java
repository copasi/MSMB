/* Generated by JTB 1.4.4 */
package parsers.mathExpression.syntaxtree;

import parsers.mathExpression.visitor.*;

/**
 * JTB node class for the production Literal:<br>
 * Corresponding grammar :<br>
 * nodeChoice -> . %0 < INTEGER_LITERAL ><br>
 * .......... .. | %1 < FLOATING_POINT_LITERAL ><br>
 * .......... .. | %2 BooleanLiteral()<br>
 */
public class Literal implements INode {

  /** A child node */
  public NodeChoice nodeChoice;

  /** The serial version uid */
  private static final long serialVersionUID = 144L;

  /**
   * Constructs the node with its child node.
   *
   * @param n0 the child node
   */
  public Literal(final NodeChoice n0) {
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