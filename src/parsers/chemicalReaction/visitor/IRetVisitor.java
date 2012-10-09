/* Generated by JTB 1.4.4 */
package parsers.chemicalReaction.visitor;

import parsers.chemicalReaction.syntaxtree.*;

/**
 * All "Ret" visitors must implement this interface.
 * @param <R> The user return information type
 */
public interface IRetVisitor<R> {

  /*
   * Base "Ret" visit methods
   */

  /**
   * Visits a {@link NodeList} node.
   *
   * @param n the node to visit
   * @return the user return information
   */
  public R visit(final NodeList n);

  /**
   * Visits a {@link NodeListOptional} node.
   *
   * @param n the node to visit
   * @return the user return information
   */
  public R visit(final NodeListOptional n);

  /**
   * Visits a {@link NodeOptional} node.
   *
   * @param n the node to visit
   * @return the user return information
   */
  public R visit(final NodeOptional n);

  /**
   * Visits a {@link NodeSequence} node.
   *
   * @param n the node to visit
   * @return the user return information
   */
  public R visit(final NodeSequence n);

  /**
   * Visits a {@link NodeToken} node.
   *
   * @param n the node to visit
   * @return the user return information
   */
  public R visit(final NodeToken n);

  /*
   * User grammar generated visit methods
   */

  /**
   * Visits a {@link CompleteReaction} node, whose children are the following :
   * <p>
   * reaction -> Reaction()<br>
   * nodeToken -> < EOF ><br>
   *
   * @param n the node to visit
   * @return the user return information
   */
  public R visit(final CompleteReaction n);

  /**
   * Visits a {@link CompleteSpeciesWithCoefficient} node, whose children are the following :
   * <p>
   * speciesWithCoeff -> SpeciesWithCoeff()<br>
   * nodeToken -> < EOF ><br>
   *
   * @param n the node to visit
   * @return the user return information
   */
  public R visit(final CompleteSpeciesWithCoefficient n);

  /**
   * Visits a {@link Reaction} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 #0 ( AdditiveExpression() )?<br>
   * .......... .. . .. #1 ( Blank() )* #2 < ARROW ><br>
   * .......... .. . .. #3 ( $0 ( " " )<br>
   * .......... .. . .. .. . $1 ( Blank() )*<br>
   * .......... .. . .. .. . $2 ( AdditiveExpression() )? )*<br>
   * .......... .. . .. #4 ( $0 ( Blank() )* $1 ";"<br>
   * .......... .. . .. .. . $2 ( Blank() )* $3 ListModifiers() )?<br>
   * .......... .. | %1 #0 < ARROW2 ><br>
   * .......... .. . .. #1 ( Blank() )*<br>
   * .......... .. . .. #2 ( AdditiveExpression() )?<br>
   * .......... .. . .. #3 ( $0 ( Blank() )* $1 ";"<br>
   * .......... .. . .. .. . $2 ( Blank() )* $3 ListModifiers() )?<br>
   *
   * @param n the node to visit
   * @return the user return information
   */
  public R visit(final Reaction n);

  /**
   * Visits a {@link AdditiveExpression} node, whose children are the following :
   * <p>
   * speciesWithCoeff -> SpeciesWithCoeff()<br>
   * nodeListOptional -> ( #0 ( Blank() )* #1 " + "<br>
   * ................ .. . #2 ( Blank() )* #3 SpeciesWithCoeff() )*<br>
   *
   * @param n the node to visit
   * @return the user return information
   */
  public R visit(final AdditiveExpression n);

  /**
   * Visits a {@link SpeciesWithCoeff} node, whose children are the following :
   * <p>
   * nodeOptional -> ( #0 Stoichiometry()<br>
   * ............ .. . #1 ( Blank() )* #2 " * "<br>
   * ............ .. . #3 ( Blank() )* )?<br>
   * species -> Species()<br>
   *
   * @param n the node to visit
   * @return the user return information
   */
  public R visit(final SpeciesWithCoeff n);

  /**
   * Visits a {@link Blank} node, whose children are the following :
   * <p>
   * nodeToken -> " "<br>
   *
   * @param n the node to visit
   * @return the user return information
   */
  public R visit(final Blank n);

  /**
   * Visits a {@link ListModifiers} node, whose children are the following :
   * <p>
   * species -> Species()<br>
   * nodeListOptional -> ( #0 ( Blank() )+ #1 Species() )*<br>
   *
   * @param n the node to visit
   * @return the user return information
   */
  public R visit(final ListModifiers n);

  /**
   * Visits a {@link Species} node, whose children are the following :
   * <p>
   * nodeToken -> < IDENTIFIER ><br>
   * nodeListOptional -> ( < IDENTIFIER > )*<br>
   *
   * @param n the node to visit
   * @return the user return information
   */
  public R visit(final Species n);

  /**
   * Visits a {@link Stoichiometry} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 < INTEGER_LITERAL ><br>
   * .......... .. | %1 < FLOATING_POINT_LITERAL ><br>
   *
   * @param n the node to visit
   * @return the user return information
   */
  public R visit(final Stoichiometry n);

}
