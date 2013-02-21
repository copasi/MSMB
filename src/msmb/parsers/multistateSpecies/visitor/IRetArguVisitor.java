/* Generated by JTB 1.4.7 */
package msmb.parsers.multistateSpecies.visitor;

import msmb.parsers.multistateSpecies.syntaxtree.*;

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
   * Visits a {@link CompleteMultistateSpecies} node, whose children are the following :
   * <p>
   * multistateSpecies -> MultistateSpecies()<br>
   * nodeListOptional -> ( PossibleExtensions() )*<br>
   * nodeToken -> <EOF><br>
   *
   * @param n - the node to visit
   * @param argu - the user argument
   * @return the user return information
   */
  public R visit(final CompleteMultistateSpecies n, final A argu);

  /**
   * Visits a {@link CompleteMultistateSpecies_Operator} node, whose children are the following :
   * <p>
   * multistateSpecies_Operator -> MultistateSpecies_Operator()<br>
   * nodeListOptional -> ( PossibleExtensions() )*<br>
   * nodeToken -> <EOF><br>
   *
   * @param n - the node to visit
   * @param argu - the user argument
   * @return the user return information
   */
  public R visit(final CompleteMultistateSpecies_Operator n, final A argu);

  /**
   * Visits a {@link CompleteMultistateSpecies_Range} node, whose children are the following :
   * <p>
   * multistateSpecies_SiteSingleElement_Range -> MultistateSpecies_SiteSingleElement_Range()<br>
   * nodeToken -> <EOF><br>
   *
   * @param n - the node to visit
   * @param argu - the user argument
   * @return the user return information
   */
  public R visit(final CompleteMultistateSpecies_Range n, final A argu);

  /**
   * Visits a {@link CompleteMultistateSpecies_RangeString} node, whose children are the following :
   * <p>
   * multistateSpecies_SiteSingleElement -> MultistateSpecies_SiteSingleElement()<br>
   * nodeToken -> <EOF><br>
   *
   * @param n - the node to visit
   * @param argu - the user argument
   * @return the user return information
   */
  public R visit(final CompleteMultistateSpecies_RangeString n, final A argu);

  /**
   * Visits a {@link PossibleExtensions} node, whose children are the following :
   * <p>
   * nodeChoice -> . %00 <EXTENSION_CONC><br>
   * .......... .. | %01 <EXTENSION_COMPARTMENT><br>
   * .......... .. | %02 <EXTENSION_PARTICLE><br>
   * .......... .. | %03 <EXTENSION_TRANS><br>
   * .......... .. | %04 <EXTENSION_INIT><br>
   * .......... .. | %05 <EXTENSION_RATE><br>
   * .......... .. | %06 <EXTENSION_SPECIES><br>
   * .......... .. | %07 <EXTENSION_GLOBALQ><br>
   * .......... .. | %08 <EXTENSION_FUNCTION><br>
   * .......... .. | %09 <EXTENSION_REACTION><br>
   * .......... .. | %10 <EXTENSION_FLUX><br>
   *
   * @param n - the node to visit
   * @param argu - the user argument
   * @return the user return information
   */
  public R visit(final PossibleExtensions n, final A argu);

  /**
   * Visits a {@link MultistateSpecies} node, whose children are the following :
   * <p>
   * multistateSpecies_Name -> MultistateSpecies_Name()<br>
   * nodeOptional -> ( #0 <OPEN_R> #1 MultistateSpecies_SingleStateDefinition()<br>
   * ............ .. . #2 ( $0 <SITE_NAMES_SEPARATOR> $1 MultistateSpecies_SingleStateDefinition() )*<br>
   * ............ .. . #3 <CLOSED_R> )?<br>
   *
   * @param n - the node to visit
   * @param argu - the user argument
   * @return the user return information
   */
  public R visit(final MultistateSpecies n, final A argu);

  /**
   * Visits a {@link MultistateSpecies_SingleStateDefinition} node, whose children are the following :
   * <p>
   * multistateSpecies_SiteName -> MultistateSpecies_SiteName()<br>
   * nodeToken -> <OPEN_C><br>
   * multistateSpecies_SiteSingleElement -> MultistateSpecies_SiteSingleElement()<br>
   * nodeListOptional -> ( #0 <SITE_STATES_SEPARATOR> #1 MultistateSpecies_SiteSingleElement() )*<br>
   * nodeToken1 -> <CLOSED_C><br>
   * nodeOptional -> ( <CIRCULAR_FLAG> )?<br>
   *
   * @param n - the node to visit
   * @param argu - the user argument
   * @return the user return information
   */
  public R visit(final MultistateSpecies_SingleStateDefinition n, final A argu);

  /**
   * Visits a {@link MultistateSpecies_Name} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 <STRING_LITERAL><br>
   * .......... .. | %1 ( &0 <MULTI_IDENTIFIER><br>
   * .......... .. . .. | &1 <CIRCULAR_FLAG><br>
   * .......... .. . .. | &2 <CLOSED_R><br>
   * .......... .. . .. | &3 <SITE_NAMES_SEPARATOR><br>
   * .......... .. . .. | &4 <RANGE_SEPARATOR><br>
   * .......... .. . .. | &5 <SITE_STATES_SEPARATOR><br>
   * .......... .. . .. | &6 <CLOSED_C> )+<br>
   *
   * @param n - the node to visit
   * @param argu - the user argument
   * @return the user return information
   */
  public R visit(final MultistateSpecies_Name n, final A argu);

  /**
   * Visits a {@link MultistateSpecies_SiteSingleElement} node, whose children are the following :
   * <p>
   * nodeChoice -> ( %0 <STRING_LITERAL><br>
   * .......... .. | %1 ( &0 <CIRCULAR_FLAG><br>
   * .......... .. . .. | &1 <MULTI_IDENTIFIER><br>
   * .......... .. . .. | &2 <NUMBER><br>
   * .......... .. . .. | &3 <OPEN_R> )+ )<br>
   * nodeOptional -> ( #0 ( " " )*<br>
   * ............ .. . #1 <RANGE_SEPARATOR><br>
   * ............ .. . #2 ( " " )*<br>
   * ............ .. . #3 ( %0 <STRING_LITERAL><br>
   * ............ .. . .. | %1 ( &0 <MULTI_IDENTIFIER><br>
   * ............ .. . .. . .. | &1 <NUMBER><br>
   * ............ .. . .. . .. | &2 <OPEN_R> )+ ) )?<br>
   *
   * @param n - the node to visit
   * @param argu - the user argument
   * @return the user return information
   */
  public R visit(final MultistateSpecies_SiteSingleElement n, final A argu);

  /**
   * Visits a {@link MultistateSpecies_SiteSingleElement_Range} node, whose children are the following :
   * <p>
   * nodeToken -> <NUMBER><br>
   * nodeListOptional -> ( " " )*<br>
   * nodeToken1 -> <RANGE_SEPARATOR><br>
   * nodeListOptional1 -> ( " " )*<br>
   * nodeToken2 -> <NUMBER><br>
   *
   * @param n - the node to visit
   * @param argu - the user argument
   * @return the user return information
   */
  public R visit(final MultistateSpecies_SiteSingleElement_Range n, final A argu);

  /**
   * Visits a {@link MultistateSpecies_SiteName} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 <STRING_LITERAL><br>
   * .......... .. | %1 ( &0 <MULTI_IDENTIFIER><br>
   * .......... .. . .. | &1 <CLOSED_C><br>
   * .......... .. . .. | &2 <OPEN_R><br>
   * .......... .. . .. | &3 <CLOSED_R><br>
   * .......... .. . .. | &4 <SITE_STATES_SEPARATOR> )+<br>
   *
   * @param n - the node to visit
   * @param argu - the user argument
   * @return the user return information
   */
  public R visit(final MultistateSpecies_SiteName n, final A argu);

  /**
   * Visits a {@link MultistateSpecies_Operator} node, whose children are the following :
   * <p>
   * multistateSpecies_Name -> MultistateSpecies_Name()<br>
   * nodeOptional -> ( #0 <OPEN_R> #1 MultistateSpecies_Operator_SingleSite()<br>
   * ............ .. . #2 ( $0 <SITE_NAMES_SEPARATOR> $1 MultistateSpecies_Operator_SingleSite() )*<br>
   * ............ .. . #3 <CLOSED_R> )?<br>
   *
   * @param n - the node to visit
   * @param argu - the user argument
   * @return the user return information
   */
  public R visit(final MultistateSpecies_Operator n, final A argu);

  /**
   * Visits a {@link MultistateSpecies_Operator_SingleSite} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 #0 ( &0 <SUCC><br>
   * .......... .. . .. .. | &1 <PREC> )<br>
   * .......... .. . .. #1 <OPEN_R> #2 MultistateSpecies_Operator_SiteName() #3 <CLOSED_R><br>
   * .......... .. | %1 #0 MultistateSpecies_Operator_SiteName()<br>
   * .......... .. . .. #1 ( $0 <OPEN_C> $1 MultistateSpecies_Operator_SiteSingleState() $2 <CLOSED_C> )?<br>
   *
   * @param n - the node to visit
   * @param argu - the user argument
   * @return the user return information
   */
  public R visit(final MultistateSpecies_Operator_SingleSite n, final A argu);

  /**
   * Visits a {@link MultistateSpecies_Operator_SiteName} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 <STRING_LITERAL><br>
   * .......... .. | %1 ( &0 <MULTI_IDENTIFIER><br>
   * .......... .. . .. | &1 <NUMBER> )+<br>
   *
   * @param n - the node to visit
   * @param argu - the user argument
   * @return the user return information
   */
  public R visit(final MultistateSpecies_Operator_SiteName n, final A argu);

  /**
   * Visits a {@link MultistateSpecies_Operator_SiteSingleState} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 <STRING_LITERAL><br>
   * .......... .. | %1 ( &0 <MULTI_IDENTIFIER><br>
   * .......... .. . .. | &1 <NUMBER> )+<br>
   *
   * @param n - the node to visit
   * @param argu - the user argument
   * @return the user return information
   */
  public R visit(final MultistateSpecies_Operator_SiteSingleState n, final A argu);

}