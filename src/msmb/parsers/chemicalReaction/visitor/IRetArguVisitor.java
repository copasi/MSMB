/* Generated by JTB 1.4.7 */
package msmb.parsers.chemicalReaction.visitor;

import msmb.parsers.chemicalReaction.syntaxtree.*;

/**
 * All "RetArgu" visitors must implement this interface.
 * @param <R> - The user return information type
 * @param <A> - The user argument type
 */
public interface IRetArguVisitor<R, A> {

  /*
   * Base "RetArgu" visit methods
   */

  /**
   * Visits a {@link NodeChoice} node, passing it an argument.
   *
   * @param n - the node to visit
   * @param argu - the user argument
   * @return the user return information
   */
  public R visit(final NodeChoice n, final A argu);

  /**
   * Visits a {@link NodeList} node, passing it an argument.
   *
   * @param n - the node to visit
   * @param argu - the user argument
   * @return the user return information
   */
  public R visit(final NodeList n, final A argu);

  /**
   * Visits a {@link NodeListOptional} node, passing it an argument.
   *
   * @param n - the node to visit
   * @param argu - the user argument
   * @return the user return information
   */
  public R visit(final NodeListOptional n, final A argu);

  /**
   * Visits a {@link NodeOptional} node, passing it an argument.
   *
   * @param n - the node to visit
   * @param argu - the user argument
   * @return the user return information
   */
  public R visit(final NodeOptional n, final A argu);

  /**
   * Visits a {@link NodeSequence} node, passing it an argument.
   *
   * @param n - the node to visit
   * @param argu - the user argument
   * @return the user return information
   */
  public R visit(final NodeSequence n, final A argu);

  /**
   * Visits a {@link NodeTCF} node, passing it an argument.
   *
   * @param n - the node to visit
   * @param argu - the user argument
   * @return the user return information
   */
  public R visit(final NodeTCF n, final A argu);

  /**
   * Visits a {@link NodeToken} node, passing it an argument.
   *
   * @param n - the node to visit
   * @param argu - the user argument
   * @return the user return information
   */
  public R visit(final NodeToken n, final A argu);

  /*
   * User grammar generated visit methods
   */

  /**
   * Visits a {@link CompleteReaction} node, whose children are the following :
   * <p>
   * reaction -> Reaction()<br>
   * nodeToken -> <EOF><br>
   *
   * @param n - the node to visit
   * @param argu - the user argument
   * @return the user return information
   */
  public R visit(final CompleteReaction n, final A argu);

  /**
   * Visits a {@link CompleteSpeciesWithCoefficient} node, whose children are the following :
   * <p>
   * speciesWithCoeff -> SpeciesWithCoeff()<br>
   * nodeToken -> <EOF><br>
   *
   * @param n - the node to visit
   * @param argu - the user argument
   * @return the user return information
   */
  public R visit(final CompleteSpeciesWithCoefficient n, final A argu);

  /**
   * Visits a {@link Reaction} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 #0 ( AdditiveExpression() )?<br>
   * .......... .. . .. #1 ( Blank() )*<br>
   * .......... .. . .. #2 <ARROW><br>
   * .......... .. . .. #3 ( $0 ( " " )<br>
   * .......... .. . .. .. . $1 ( Blank() )*<br>
   * .......... .. . .. .. . $2 ( AdditiveExpression() )? )*<br>
   * .......... .. . .. #4 ( $0 ( Blank() )*<br>
   * .......... .. . .. .. . $1 ";"<br>
   * .......... .. . .. .. . $2 ( Blank() )*<br>
   * .......... .. . .. .. . $3 ListModifiers() )?<br>
   * .......... .. | %1 #0 <ARROW2><br>
   * .......... .. . .. #1 ( Blank() )*<br>
   * .......... .. . .. #2 ( AdditiveExpression() )?<br>
   * .......... .. . .. #3 ( $0 ( Blank() )*<br>
   * .......... .. . .. .. . $1 ";"<br>
   * .......... .. . .. .. . $2 ( Blank() )*<br>
   * .......... .. . .. .. . $3 ListModifiers() )?<br>
   *
   * @param n - the node to visit
   * @param argu - the user argument
   * @return the user return information
   */
  public R visit(final Reaction n, final A argu);

  /**
   * Visits a {@link AdditiveExpression} node, whose children are the following :
   * <p>
   * speciesWithCoeff -> SpeciesWithCoeff()<br>
   * nodeListOptional -> ( #0 ( Blank() )*<br>
   * ................ .. . #1 " + "<br>
   * ................ .. . #2 ( Blank() )*<br>
   * ................ .. . #3 SpeciesWithCoeff() )*<br>
   *
   * @param n - the node to visit
   * @param argu - the user argument
   * @return the user return information
   */
  public R visit(final AdditiveExpression n, final A argu);

  /**
   * Visits a {@link SpeciesWithCoeff} node, whose children are the following :
   * <p>
   * nodeOptional -> ( #0 Stoichiometry()<br>
   * ............ .. . #1 ( Blank() )*<br>
   * ............ .. . #2 " * "<br>
   * ............ .. . #3 ( Blank() )* )?<br>
   * species -> Species()<br>
   *
   * @param n - the node to visit
   * @param argu - the user argument
   * @return the user return information
   */
  public R visit(final SpeciesWithCoeff n, final A argu);

  /**
   * Visits a {@link Blank} node, whose children are the following :
   * <p>
   * nodeToken -> " "<br>
   *
   * @param n - the node to visit
   * @param argu - the user argument
   * @return the user return information
   */
  public R visit(final Blank n, final A argu);

  /**
   * Visits a {@link ListModifiers} node, whose children are the following :
   * <p>
   * species -> Species()<br>
   * nodeListOptional -> ( #0 ( Blank() )+<br>
   * ................ .. . #1 Species() )*<br>
   *
   * @param n - the node to visit
   * @param argu - the user argument
   * @return the user return information
   */
  public R visit(final ListModifiers n, final A argu);

  /**
   * Visits a {@link Species} node, whose children are the following :
   * <p>
   * nodeToken -> <IDENTIFIER><br>
   * nodeListOptional -> ( <IDENTIFIER> )*<br>
   *
   * @param n - the node to visit
   * @param argu - the user argument
   * @return the user return information
   */
  public R visit(final Species n, final A argu);

  /**
   * Visits a {@link Stoichiometry} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 <INTEGER_LITERAL><br>
   * .......... .. | %1 <FLOATING_POINT_LITERAL><br>
   *
   * @param n - the node to visit
   * @param argu - the user argument
   * @return the user return information
   */
  public R visit(final Stoichiometry n, final A argu);

}
