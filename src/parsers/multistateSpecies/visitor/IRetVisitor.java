/* Generated by JTB 1.4.4 */
package parsers.multistateSpecies.visitor;

import parsers.multistateSpecies.syntaxtree.*;

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
   * Visits a {@link CompleteMultistateSpecies} node, whose children are the following :
   * <p>
   * multistateSpecies -> MultistateSpecies()<br>
   * nodeToken -> < EOF ><br>
   *
   * @param n the node to visit
   * @return the user return information
   */
  public R visit(final CompleteMultistateSpecies n);

  /**
   * Visits a {@link CompleteMultistateSpecies_Operator} node, whose children are the following :
   * <p>
   * multistateSpecies_Operator -> MultistateSpecies_Operator()<br>
   * nodeToken -> < EOF ><br>
   *
   * @param n the node to visit
   * @return the user return information
   */
  public R visit(final CompleteMultistateSpecies_Operator n);

  /**
   * Visits a {@link CompleteMultistateSpecies_Range} node, whose children are the following :
   * <p>
   * multistateSpecies_SiteSingleElement_Range -> MultistateSpecies_SiteSingleElement_Range()<br>
   * nodeToken -> < EOF ><br>
   *
   * @param n the node to visit
   * @return the user return information
   */
  public R visit(final CompleteMultistateSpecies_Range n);

  /**
   * Visits a {@link MultistateSpecies} node, whose children are the following :
   * <p>
   * multistateSpecies_Name -> MultistateSpecies_Name()<br>
   * nodeOptional -> ( #0 < OPEN_R > #1 MultistateSpecies_SingleStateDefinition()<br>
   * ............ .. . #2 ( $0 < SITE_NAMES_SEPARATOR > $1 MultistateSpecies_SingleStateDefinition() )* #3 < CLOSED_R > )?<br>
   *
   * @param n the node to visit
   * @return the user return information
   */
  public R visit(final MultistateSpecies n);

  /**
   * Visits a {@link MultistateSpecies_SingleStateDefinition} node, whose children are the following :
   * <p>
   * multistateSpecies_SiteName -> MultistateSpecies_SiteName()<br>
   * nodeToken -> < OPEN_C ><br>
   * multistateSpecies_SiteSingleElement -> MultistateSpecies_SiteSingleElement()<br>
   * nodeListOptional -> ( #0 < SITE_STATES_SEPARATOR > #1 MultistateSpecies_SiteSingleElement() )*<br>
   * nodeToken1 -> < CLOSED_C ><br>
   *
   * @param n the node to visit
   * @return the user return information
   */
  public R visit(final MultistateSpecies_SingleStateDefinition n);

  /**
   * Visits a {@link MultistateSpecies_Name} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 < STRING_LITERAL ><br>
   * .......... .. | %1 ( &0 < MULTI_IDENTIFIER ><br>
   * .......... .. . .. | &1 < CLOSED_R ><br>
   * .......... .. . .. | &2 < SITE_NAMES_SEPARATOR ><br>
   * .......... .. . .. | &3 < RANGE_SEPARATOR ><br>
   * .......... .. . .. | &4 < SITE_STATES_SEPARATOR ><br>
   * .......... .. . .. | &5 < CLOSED_C > )+<br>
   *
   * @param n the node to visit
   * @return the user return information
   */
  public R visit(final MultistateSpecies_Name n);

  /**
   * Visits a {@link MultistateSpecies_SiteSingleElement} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 < STRING_LITERAL ><br>
   * .......... .. | %1 MultistateSpecies_SiteSingleElement_Range()<br>
   * .......... .. | %2 ( &0 < MULTI_IDENTIFIER ><br>
   * .......... .. . .. | &1 < NUMBER ><br>
   * .......... .. . .. | &2 < OPEN_R ><br>
   * .......... .. . .. | &3 < RANGE_SEPARATOR > )+<br>
   *
   * @param n the node to visit
   * @return the user return information
   */
  public R visit(final MultistateSpecies_SiteSingleElement n);

  /**
   * Visits a {@link MultistateSpecies_SiteSingleElement_Range} node, whose children are the following :
   * <p>
   * nodeToken -> < NUMBER ><br>
   * nodeToken1 -> < RANGE_SEPARATOR ><br>
   * nodeToken2 -> < NUMBER ><br>
   *
   * @param n the node to visit
   * @return the user return information
   */
  public R visit(final MultistateSpecies_SiteSingleElement_Range n);

  /**
   * Visits a {@link MultistateSpecies_SiteName} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 < STRING_LITERAL ><br>
   * .......... .. | %1 ( &0 < MULTI_IDENTIFIER ><br>
   * .......... .. . .. | &1 < CLOSED_C ><br>
   * .......... .. . .. | &2 < OPEN_R ><br>
   * .......... .. . .. | &3 < CLOSED_R ><br>
   * .......... .. . .. | &4 < RANGE_SEPARATOR ><br>
   * .......... .. . .. | &5 < SITE_STATES_SEPARATOR > )+<br>
   *
   * @param n the node to visit
   * @return the user return information
   */
  public R visit(final MultistateSpecies_SiteName n);

  /**
   * Visits a {@link MultistateSpecies_Operator} node, whose children are the following :
   * <p>
   * multistateSpecies_Name -> MultistateSpecies_Name()<br>
   * nodeOptional -> ( #0 < OPEN_R > #1 MultistateSpecies_Operator_SingleSite()<br>
   * ............ .. . #2 ( $0 < SITE_NAMES_SEPARATOR > $1 MultistateSpecies_Operator_SingleSite() )* #3 < CLOSED_R > )?<br>
   *
   * @param n the node to visit
   * @return the user return information
   */
  public R visit(final MultistateSpecies_Operator n);

  /**
   * Visits a {@link MultistateSpecies_Operator_SingleSite} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 #0 MultistateSpecies_Operator_SiteName()<br>
   * .......... .. . .. #1 ( $0 < OPEN_C > $1 MultistateSpecies_Operator_SiteSingleState() $2 < CLOSED_C > )?<br>
   * .......... .. | %1 #0 ( &0 < SUCC ><br>
   * .......... .. . .. .. | &1 < PREC ><br>
   * .......... .. . .. .. | &2 < CIRC_L_SHIFT ><br>
   * .......... .. . .. .. | &3 < CIRC_R_SHIFT > ) #1 < OPEN_R > #2 MultistateSpecies_Operator_SiteName() #3 < CLOSED_R ><br>
   *
   * @param n the node to visit
   * @return the user return information
   */
  public R visit(final MultistateSpecies_Operator_SingleSite n);

  /**
   * Visits a {@link MultistateSpecies_Operator_SiteName} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 < STRING_LITERAL ><br>
   * .......... .. | %1 ( &0 < MULTI_IDENTIFIER ><br>
   * .......... .. . .. | &1 < NUMBER ><br>
   * .......... .. . .. | &2 < OPEN_R ><br>
   * .......... .. . .. | &3 < RANGE_SEPARATOR > )+<br>
   *
   * @param n the node to visit
   * @return the user return information
   */
  public R visit(final MultistateSpecies_Operator_SiteName n);

  /**
   * Visits a {@link MultistateSpecies_Operator_SiteSingleState} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 < STRING_LITERAL ><br>
   * .......... .. | %1 ( &0 < MULTI_IDENTIFIER ><br>
   * .......... .. . .. | &1 < NUMBER ><br>
   * .......... .. . .. | &2 < OPEN_R ><br>
   * .......... .. . .. | &3 < RANGE_SEPARATOR > )+<br>
   *
   * @param n the node to visit
   * @return the user return information
   */
  public R visit(final MultistateSpecies_Operator_SiteSingleState n);

}
