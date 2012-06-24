/* Generated by JTB 1.4.4 */
package parsers.multistateSpecies.syntaxtree;

import parsers.multistateSpecies.visitor.IRetArguVisitor;
import parsers.multistateSpecies.visitor.IRetVisitor;
import parsers.multistateSpecies.visitor.IVoidArguVisitor;
import parsers.multistateSpecies.visitor.IVoidVisitor;

/**
 * The interface which {@link NodeList}, {@link NodeListOptional} and {@link NodeSequence} must implement.
 */
public interface INodeList extends INode {

  /**
   * Adds a node to the list.
   *
   * @param n the node to add
   */
  public void addNode(final INode n);

  /**
   * @param i the element index
   * @return the element at the given index
   */
  public INode elementAt(int i);

  /**
   * @return the iterator on the node list
   */
  public java.util.Iterator<INode> elements();

  /**
   * @return the list size
   */
  public int size();

  /**
   * Accepts a {@link IRetArguVisitor} visitor with user Return and Argument data.
   *
   * @param <R> the user Return type
   * @param <A> the user Argument type
   * @param vis the visitor
   * @param argu the user Argument data
   * @return the user Return data
   */
  public <R, A> R accept(final IRetArguVisitor<R, A> vis, final A argu);

  /**
   * Accepts a {@link IRetVisitor} visitor with user Return data.
   *
   * @param <R> the user Return type
   * @param vis the visitor
   * @return the user Return data
   */
  public <R> R accept(final IRetVisitor<R> vis);

  /**
   * Accepts a {@link IVoidArguVisitor} visitor with user Argument data.
   *
   * @param <A> the user Argument type
   * @param vis the visitor
   * @param argu the user Argument data
   */
  public <A> void accept(final IVoidArguVisitor<A> vis, final A argu);

  /**
   * Accepts a {@link IVoidVisitor} visitor with no user Return nor Argument data.
   *
   * @param vis the visitor
   */
  public void accept(final IVoidVisitor vis);

}
