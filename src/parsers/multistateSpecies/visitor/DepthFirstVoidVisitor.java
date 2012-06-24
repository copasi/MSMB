/* Generated by JTB 1.4.4 */
package parsers.multistateSpecies.visitor;

import parsers.multistateSpecies.syntaxtree.*;
import java.util.*;

/**
 * Provides default methods which visit each node in the tree in depth-first order.<br>
 * In your "Void" visitors extend this class and override part or all of these methods.
 *
 */
public class DepthFirstVoidVisitor implements IVoidVisitor {


  /*
   * Base nodes classes visit methods (to be overridden if necessary)
   */

  /**
   * Visits a {@link NodeChoice} node.
   *
   * @param n the node to visit
   */
  public void visit(final NodeChoice n) {
    n.choice.accept(this);
    return;
  }

  /**
   * Visits a {@link NodeList} node.
   *
   * @param n the node to visit
   */
  public void visit(final NodeList n) {
    for (final Iterator<INode> e = n.elements(); e.hasNext();) {
      e.next().accept(this);
    }
    return;
  }

  /**
   * Visits a {@link NodeListOptional} node.
   *
   * @param n the node to visit
   */
  public void visit(final NodeListOptional n) {
    if (n.present()) {
      for (final Iterator<INode> e = n.elements(); e.hasNext();) {
        e.next().accept(this);
        }
      return;
    } else
      return;
  }

  /**
   * Visits a {@link NodeOptional} node.
   *
   * @param n the node to visit
   */
  public void visit(final NodeOptional n) {
    if (n.present()) {
      n.node.accept(this);
      return;
    } else
    return;
  }

  /**
   * Visits a {@link NodeSequence} node.
   *
   * @param n the node to visit
   */
  public void visit(final NodeSequence n) {
    for (final Iterator<INode> e = n.elements(); e.hasNext();) {
      e.next().accept(this);
    }
    return;
  }

  /**
   * Visits a {@link NodeToken} node.
   *
   * @param n the node to visit
   */
  public void visit(final NodeToken n) {
    @SuppressWarnings("unused")
    final String tkIm = n.tokenImage;
    return;
  }

  /*
   * User grammar generated visit methods (to be overridden if necessary)
   */

  /**
   * Visits a {@link CompleteMultistateSpecies} node, whose children are the following :
   * <p>
   * multistateSpecies -> MultistateSpecies()<br>
   * nodeToken -> < EOF ><br>
   *
   * @param n the node to visit
   */
  public void visit(final CompleteMultistateSpecies n) {
    // multistateSpecies -> MultistateSpecies()
    n.multistateSpecies.accept(this);
    // nodeToken -> < EOF >
    n.nodeToken.accept(this);
  }

  /**
   * Visits a {@link CompleteMultistateSpecies_Operator} node, whose children are the following :
   * <p>
   * multistateSpecies_Operator -> MultistateSpecies_Operator()<br>
   * nodeToken -> < EOF ><br>
   *
   * @param n the node to visit
   */
  public void visit(final CompleteMultistateSpecies_Operator n) {
    // multistateSpecies_Operator -> MultistateSpecies_Operator()
    n.multistateSpecies_Operator.accept(this);
    // nodeToken -> < EOF >
    n.nodeToken.accept(this);
  }

  /**
   * Visits a {@link CompleteMultistateSpecies_Range} node, whose children are the following :
   * <p>
   * multistateSpecies_SiteSingleElement_Range -> MultistateSpecies_SiteSingleElement_Range()<br>
   * nodeToken -> < EOF ><br>
   *
   * @param n the node to visit
   */
  public void visit(final CompleteMultistateSpecies_Range n) {
    // multistateSpecies_SiteSingleElement_Range -> MultistateSpecies_SiteSingleElement_Range()
    n.multistateSpecies_SiteSingleElement_Range.accept(this);
    // nodeToken -> < EOF >
    n.nodeToken.accept(this);
  }

  /**
   * Visits a {@link MultistateSpecies} node, whose children are the following :
   * <p>
   * multistateSpecies_Name -> MultistateSpecies_Name()<br>
   * nodeOptional -> ( #0 < OPEN_R > #1 MultistateSpecies_SingleStateDefinition()<br>
   * ............ .. . #2 ( $0 < SITE_NAMES_SEPARATOR > $1 MultistateSpecies_SingleStateDefinition() )* #3 < CLOSED_R > )?<br>
   *
   * @param n the node to visit
   */
  public void visit(final MultistateSpecies n) {
    // multistateSpecies_Name -> MultistateSpecies_Name()
    n.multistateSpecies_Name.accept(this);
    // nodeOptional -> ( #0 < OPEN_R > #1 MultistateSpecies_SingleStateDefinition()
    // ............ .. . #2 ( $0 < SITE_NAMES_SEPARATOR > $1 MultistateSpecies_SingleStateDefinition() )* #3 < CLOSED_R > )?
    n.nodeOptional.accept(this);
  }

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
   */
  public void visit(final MultistateSpecies_SingleStateDefinition n) {
    // multistateSpecies_SiteName -> MultistateSpecies_SiteName()
    n.multistateSpecies_SiteName.accept(this);
    // nodeToken -> < OPEN_C >
    n.nodeToken.accept(this);
    // multistateSpecies_SiteSingleElement -> MultistateSpecies_SiteSingleElement()
    n.multistateSpecies_SiteSingleElement.accept(this);
    // nodeListOptional -> ( #0 < SITE_STATES_SEPARATOR > #1 MultistateSpecies_SiteSingleElement() )*
    n.nodeListOptional.accept(this);
    // nodeToken1 -> < CLOSED_C >
    n.nodeToken1.accept(this);
  }

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
   */
  public void visit(final MultistateSpecies_Name n) {
    // nodeChoice -> . %0 < STRING_LITERAL >
    // .......... .. | %1 ( &0 < MULTI_IDENTIFIER >
    // .......... .. . .. | &1 < CLOSED_R >
    // .......... .. . .. | &2 < SITE_NAMES_SEPARATOR >
    // .......... .. . .. | &3 < RANGE_SEPARATOR >
    // .......... .. . .. | &4 < SITE_STATES_SEPARATOR >
    // .......... .. . .. | &5 < CLOSED_C > )+
    n.nodeChoice.accept(this);
  }

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
   */
  public void visit(final MultistateSpecies_SiteSingleElement n) {
    // nodeChoice -> . %0 < STRING_LITERAL >
    // .......... .. | %1 MultistateSpecies_SiteSingleElement_Range()
    // .......... .. | %2 ( &0 < MULTI_IDENTIFIER >
    // .......... .. . .. | &1 < NUMBER >
    // .......... .. . .. | &2 < OPEN_R >
    // .......... .. . .. | &3 < RANGE_SEPARATOR > )+
    n.nodeChoice.accept(this);
  }

  /**
   * Visits a {@link MultistateSpecies_SiteSingleElement_Range} node, whose children are the following :
   * <p>
   * nodeToken -> < NUMBER ><br>
   * nodeToken1 -> < RANGE_SEPARATOR ><br>
   * nodeToken2 -> < NUMBER ><br>
   *
   * @param n the node to visit
   */
  public void visit(final MultistateSpecies_SiteSingleElement_Range n) {
    // nodeToken -> < NUMBER >
    n.nodeToken.accept(this);
    // nodeToken1 -> < RANGE_SEPARATOR >
    n.nodeToken1.accept(this);
    // nodeToken2 -> < NUMBER >
    n.nodeToken2.accept(this);
  }

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
   */
  public void visit(final MultistateSpecies_SiteName n) {
    // nodeChoice -> . %0 < STRING_LITERAL >
    // .......... .. | %1 ( &0 < MULTI_IDENTIFIER >
    // .......... .. . .. | &1 < CLOSED_C >
    // .......... .. . .. | &2 < OPEN_R >
    // .......... .. . .. | &3 < CLOSED_R >
    // .......... .. . .. | &4 < RANGE_SEPARATOR >
    // .......... .. . .. | &5 < SITE_STATES_SEPARATOR > )+
    n.nodeChoice.accept(this);
  }

  /**
   * Visits a {@link MultistateSpecies_Operator} node, whose children are the following :
   * <p>
   * multistateSpecies_Name -> MultistateSpecies_Name()<br>
   * nodeOptional -> ( #0 < OPEN_R > #1 MultistateSpecies_Operator_SingleSite()<br>
   * ............ .. . #2 ( $0 < SITE_NAMES_SEPARATOR > $1 MultistateSpecies_Operator_SingleSite() )* #3 < CLOSED_R > )?<br>
   *
   * @param n the node to visit
   */
  public void visit(final MultistateSpecies_Operator n) {
    // multistateSpecies_Name -> MultistateSpecies_Name()
    n.multistateSpecies_Name.accept(this);
    // nodeOptional -> ( #0 < OPEN_R > #1 MultistateSpecies_Operator_SingleSite()
    // ............ .. . #2 ( $0 < SITE_NAMES_SEPARATOR > $1 MultistateSpecies_Operator_SingleSite() )* #3 < CLOSED_R > )?
    n.nodeOptional.accept(this);
  }

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
   */
  public void visit(final MultistateSpecies_Operator_SingleSite n) {
    // nodeChoice -> . %0 #0 MultistateSpecies_Operator_SiteName()
    // .......... .. . .. #1 ( $0 < OPEN_C > $1 MultistateSpecies_Operator_SiteSingleState() $2 < CLOSED_C > )?
    // .......... .. | %1 #0 ( &0 < SUCC >
    // .......... .. . .. .. | &1 < PREC >
    // .......... .. . .. .. | &2 < CIRC_L_SHIFT >
    // .......... .. . .. .. | &3 < CIRC_R_SHIFT > ) #1 < OPEN_R > #2 MultistateSpecies_Operator_SiteName() #3 < CLOSED_R >
    n.nodeChoice.accept(this);
  }

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
   */
  public void visit(final MultistateSpecies_Operator_SiteName n) {
    // nodeChoice -> . %0 < STRING_LITERAL >
    // .......... .. | %1 ( &0 < MULTI_IDENTIFIER >
    // .......... .. . .. | &1 < NUMBER >
    // .......... .. . .. | &2 < OPEN_R >
    // .......... .. . .. | &3 < RANGE_SEPARATOR > )+
    n.nodeChoice.accept(this);
  }

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
   */
  public void visit(final MultistateSpecies_Operator_SiteSingleState n) {
    // nodeChoice -> . %0 < STRING_LITERAL >
    // .......... .. | %1 ( &0 < MULTI_IDENTIFIER >
    // .......... .. . .. | &1 < NUMBER >
    // .......... .. . .. | &2 < OPEN_R >
    // .......... .. . .. | &3 < RANGE_SEPARATOR > )+
    n.nodeChoice.accept(this);
  }

}
