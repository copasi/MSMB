/* Generated by JTB 1.4.7 */
package msmb.parsers.multistateSpecies.visitor;

import msmb.parsers.multistateSpecies.syntaxtree.*;

/**
 * All "Void" visitors must implement this interface.
 */
public interface IVoidVisitor {

  /*
   * Base "Void" visit methods
   */

  /**
   * Visits a {@link NodeChoice} node.
   *
   * @param n - the node to visit
   */
  public void visit(final NodeChoice n);

  /**
   * Visits a {@link NodeList} node.
   *
   * @param n - the node to visit
   */
  public void visit(final NodeList n);

  /**
   * Visits a {@link NodeListOptional} node.
   *
   * @param n - the node to visit
   */
  public void visit(final NodeListOptional n);

  /**
   * Visits a {@link NodeOptional} node.
   *
   * @param n - the node to visit
   */
  public void visit(final NodeOptional n);

  /**
   * Visits a {@link NodeSequence} node.
   *
   * @param n - the node to visit
   */
  public void visit(final NodeSequence n);

  /**
   * Visits a {@link NodeTCF} node.
   *
   * @param n - the node to visit
   */
  public void visit(final NodeTCF n);

  /**
   * Visits a {@link NodeToken} node.
   *
   * @param n - the node to visit
   */
  public void visit(final NodeToken n);

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
   */
  public void visit(final CompleteMultistateSpecies n);

  /**
   * Visits a {@link CompleteMultistateSpecies_Operator} node, whose children are the following :
   * <p>
   * multistateSpecies_Operator -> MultistateSpecies_Operator()<br>
   * nodeListOptional -> ( PossibleExtensions() )*<br>
   * nodeToken -> <EOF><br>
   *
   * @param n - the node to visit
   */
  public void visit(final CompleteMultistateSpecies_Operator n);

  /**
   * Visits a {@link CompleteMultistateSpecies_Range} node, whose children are the following :
   * <p>
   * multistateSpecies_SiteSingleElement_Range -> MultistateSpecies_SiteSingleElement_Range()<br>
   * nodeToken -> <EOF><br>
   *
   * @param n - the node to visit
   */
  public void visit(final CompleteMultistateSpecies_Range n);

  /**
   * Visits a {@link CompleteMultistateSpecies_RangeString} node, whose children are the following :
   * <p>
   * multistateSpecies_SiteSingleElement -> MultistateSpecies_SiteSingleElement()<br>
   * nodeToken -> <EOF><br>
   *
   * @param n - the node to visit
   */
  public void visit(final CompleteMultistateSpecies_RangeString n);

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
   */
  public void visit(final PossibleExtensions n);

  /**
   * Visits a {@link MultistateSpecies} node, whose children are the following :
   * <p>
   * multistateSpecies_Name -> MultistateSpecies_Name()<br>
   * nodeOptional -> ( #0 <OPEN_R> #1 MultistateSpecies_SingleStateDefinition()<br>
   * ............ .. . #2 ( $0 <SITE_NAMES_SEPARATOR> $1 MultistateSpecies_SingleStateDefinition() )*<br>
   * ............ .. . #3 <CLOSED_R> )?<br>
   *
   * @param n - the node to visit
   */
  public void visit(final MultistateSpecies n);

  /**
   * Visits a {@link MultistateSpecies_SingleStateDefinition} node, whose children are the following :
   * <p>
   * multistateSpecies_SiteName -> MultistateSpecies_SiteName()<br>
   * nodeOptional -> ( #0 <OPEN_C> #1 MultistateSpecies_SiteSingleElement()<br>
   * ............ .. . #2 ( $0 <SITE_STATES_SEPARATOR> $1 MultistateSpecies_SiteSingleElement() )*<br>
   * ............ .. . #3 <CLOSED_C><br>
   * ............ .. . #4 ( <CIRCULAR_FLAG> )? )?<br>
   *
   * @param n - the node to visit
   */
  public void visit(final MultistateSpecies_SingleStateDefinition n);

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
   */
  public void visit(final MultistateSpecies_Name n);

  /**
   * Visits a {@link MultistateSpecies_SiteSingleElement} node, whose children are the following :
   * <p>
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
   *
   * @param n - the node to visit
   */
  public void visit(final MultistateSpecies_SiteSingleElement n);

  /**
   * Visits a {@link MultistateSpecies_SiteSingleElement_Range} node, whose children are the following :
   * <p>
   * multistateSpecies_SiteSingleElement_Range_Limits -> MultistateSpecies_SiteSingleElement_Range_Limits()<br>
   * nodeListOptional -> ( " " )*<br>
   * nodeToken -> <RANGE_SEPARATOR><br>
   * nodeListOptional1 -> ( " " )*<br>
   * multistateSpecies_SiteSingleElement_Range_Limits1 -> MultistateSpecies_SiteSingleElement_Range_Limits()<br>
   *
   * @param n - the node to visit
   */
  public void visit(final MultistateSpecies_SiteSingleElement_Range n);

  /**
   * Visits a {@link MultistateSpecies_SiteSingleElement_Range_Limits} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 <NUMBER><br>
   * .......... .. | %1 <STRING_LITERAL><br>
   * .......... .. | %2 <CIRCULAR_FLAG><br>
   * .......... .. | %3 ( &0 " "<br>
   * .......... .. . .. | &1 <OPEN_R><br>
   * .......... .. . .. | &2 <CLOSED_R><br>
   * .......... .. . .. | &3 <MATH_ELEMENT><br>
   * .......... .. . .. | &4 <NUMBER><br>
   * .......... .. . .. | &5 <STRING_LITERAL><br>
   * .......... .. . .. | &6 <MULTI_IDENTIFIER> )+<br>
   *
   * @param n - the node to visit
   */
  public void visit(final MultistateSpecies_SiteSingleElement_Range_Limits n);

  /**
   * Visits a {@link MultistateSpecies_SiteName} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 <STRING_LITERAL><br>
   * .......... .. | %1 ( &0 <MULTI_IDENTIFIER><br>
   * .......... .. . .. | &1 <CLOSED_C><br>
   * .......... .. . .. | &2 <OPEN_R> )+<br>
   *
   * @param n - the node to visit
   */
  public void visit(final MultistateSpecies_SiteName n);

  /**
   * Visits a {@link MultistateSpecies_Operator} node, whose children are the following :
   * <p>
   * multistateSpecies_Name -> MultistateSpecies_Name()<br>
   * nodeOptional -> ( #0 <OPEN_R> #1 MultistateSpecies_Operator_SingleSite()<br>
   * ............ .. . #2 ( $0 <SITE_NAMES_SEPARATOR> $1 MultistateSpecies_Operator_SingleSite() )*<br>
   * ............ .. . #3 <CLOSED_R> )?<br>
   *
   * @param n - the node to visit
   */
  public void visit(final MultistateSpecies_Operator n);

  /**
   * Visits a {@link MultistateSpecies_Operator_SingleSite} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 #0 ( &0 <SUCC><br>
   * .......... .. . .. .. | &1 <PREC> )<br>
   * .......... .. . .. #1 <OPEN_R> #2 MultistateSpecies_Operator_SiteName() #3 <CLOSED_R><br>
   * .......... .. | %1 #0 MultistateSpecies_Operator_SiteName()<br>
   * .......... .. . .. #1 ( &0 $0 "=" $1 MultistateSpecies_Operator_SiteTransferSelector()<br>
   * .......... .. . .. .. | &1 ( $0 <OPEN_C> $1 MultistateSpecies_Operator_SiteSingleState() $2 <CLOSED_C> ) )?<br>
   *
   * @param n - the node to visit
   */
  public void visit(final MultistateSpecies_Operator_SingleSite n);

  /**
   * Visits a {@link MultistateSpecies_Operator_SiteTransferSelector} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 #0 ( &0 <SUCC><br>
   * .......... .. . .. .. | &1 <PREC> )<br>
   * .......... .. . .. #1 <OPEN_R> #2 MultistateSpecies_Name() #3 "." #4 MultistateSpecies_Operator_SiteName() #5 <CLOSED_R><br>
   * .......... .. | %1 #0 MultistateSpecies_Name() #1 "." #2 MultistateSpecies_Operator_SiteName()<br>
   *
   * @param n - the node to visit
   */
  public void visit(final MultistateSpecies_Operator_SiteTransferSelector n);

  /**
   * Visits a {@link MultistateSpecies_Operator_SiteName} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 <STRING_LITERAL><br>
   * .......... .. | %1 ( &0 <MULTI_IDENTIFIER><br>
   * .......... .. . .. | &1 <NUMBER> )+<br>
   *
   * @param n - the node to visit
   */
  public void visit(final MultistateSpecies_Operator_SiteName n);

  /**
   * Visits a {@link MultistateSpecies_Operator_SiteSingleState} node, whose children are the following :
   * <p>
   * multistateSpecies_SiteSingleElement -> MultistateSpecies_SiteSingleElement()<br>
   * nodeListOptional -> ( #0 <SITE_STATES_SEPARATOR> #1 MultistateSpecies_SiteSingleElement() )*<br>
   *
   * @param n - the node to visit
   */
  public void visit(final MultistateSpecies_Operator_SiteSingleState n);

}
