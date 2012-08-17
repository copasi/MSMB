/* Generated by JTB 1.4.4 */
package parsers.multistateSpecies.visitor;

import parsers.multistateSpecies.syntaxtree.*;
import java.util.*;

/**
 * Provides default methods which visit each node in the tree in depth-first order.<br>
 * In your "RetArgu" visitors extend this class and override part or all of these methods.
 *
 * @param <R> The user return information type
 * @param <A> The user argument type
 */
public class DepthFirstRetArguVisitor<R, A> implements IRetArguVisitor<R, A> {


  /*
   * Base nodes classes visit methods (to be overridden if necessary)
   */

  /**
   * Visits a {@link NodeChoice} node, passing it an argument.
   *
   * @param n the node to visit
   * @param argu the user argument
   * @return the user return information
   */
  public R visit(final NodeChoice n, final A argu) {
    final R nRes = n.choice.accept(this, argu);
    return nRes;
  }

  /**
   * Visits a {@link NodeList} node, passing it an argument.
   *
   * @param n the node to visit
   * @param argu the user argument
   * @return the user return information
   */
  public R visit(final NodeList n, final A argu) {
    R nRes = null;
    for (final Iterator<INode> e = n.elements(); e.hasNext();) {
      @SuppressWarnings("unused")
      final R sRes = e.next().accept(this, argu);
    }
    return nRes;
  }

  /**
   * Visits a {@link NodeListOptional} node, passing it an argument.
   *
   * @param n the node to visit
   * @param argu the user argument
   * @return the user return information
   */
  public R visit(final NodeListOptional n, final A argu) {
    if (n.present()) {
      R nRes = null;
      for (final Iterator<INode> e = n.elements(); e.hasNext();) {
        @SuppressWarnings("unused")
        R sRes = e.next().accept(this, argu);
        }
      return nRes;
    } else
      return null;
  }

  /**
   * Visits a {@link NodeOptional} node, passing it an argument.
   *
   * @param n the node to visit
   * @param argu the user argument
   * @return the user return information
   */
  public R visit(final NodeOptional n, final A argu) {
    if (n.present()) {
      final R nRes = n.node.accept(this, argu);
      return nRes;
    } else
    return null;
  }

  /**
   * Visits a {@link NodeSequence} node, passing it an argument.
   *
   * @param n the node to visit
   * @param argu the user argument
   * @return the user return information
   */
  public R visit(final NodeSequence n, final A argu) {
    R nRes = null;
    for (final Iterator<INode> e = n.elements(); e.hasNext();) {
      @SuppressWarnings("unused")
      R subRet = e.next().accept(this, argu);
    }
    return nRes;
  }

  /**
   * Visits a {@link NodeToken} node, passing it an argument.
   *
   * @param n the node to visit
   * @param argu the user argument
   * @return the user return information
   */
  public R visit(final NodeToken n, @SuppressWarnings("unused") final A argu) {
    R nRes = null;
    @SuppressWarnings("unused")
    final String tkIm = n.tokenImage;
    return nRes;
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
   * @param argu the user argument
   * @return the user return information
   */
  public R visit(final CompleteMultistateSpecies n, final A argu) {
    R nRes = null;
    // multistateSpecies -> MultistateSpecies()
    n.multistateSpecies.accept(this, argu);
    // nodeToken -> < EOF >
    n.nodeToken.accept(this, argu);
    return nRes;
  }

  /**
   * Visits a {@link CompleteMultistateSpecies_Operator} node, whose children are the following :
   * <p>
   * multistateSpecies_Operator -> MultistateSpecies_Operator()<br>
   * nodeToken -> < EOF ><br>
   *
   * @param n the node to visit
   * @param argu the user argument
   * @return the user return information
   */
  public R visit(final CompleteMultistateSpecies_Operator n, final A argu) {
    R nRes = null;
    // multistateSpecies_Operator -> MultistateSpecies_Operator()
    n.multistateSpecies_Operator.accept(this, argu);
    // nodeToken -> < EOF >
    n.nodeToken.accept(this, argu);
    return nRes;
  }

  /**
   * Visits a {@link CompleteMultistateSpecies_Range} node, whose children are the following :
   * <p>
   * multistateSpecies_SiteSingleElement_Range -> MultistateSpecies_SiteSingleElement_Range()<br>
   * nodeToken -> < EOF ><br>
   *
   * @param n the node to visit
   * @param argu the user argument
   * @return the user return information
   */
  public R visit(final CompleteMultistateSpecies_Range n, final A argu) {
    R nRes = null;
    // multistateSpecies_SiteSingleElement_Range -> MultistateSpecies_SiteSingleElement_Range()
    n.multistateSpecies_SiteSingleElement_Range.accept(this, argu);
    // nodeToken -> < EOF >
    n.nodeToken.accept(this, argu);
    return nRes;
  }

  /**
   * Visits a {@link CompleteMultistateSpecies_RangeString} node, whose children are the following :
   * <p>
   * multistateSpecies_SiteSingleElement -> MultistateSpecies_SiteSingleElement()<br>
   * nodeToken -> < EOF ><br>
   *
   * @param n the node to visit
   * @param argu the user argument
   * @return the user return information
   */
  public R visit(final CompleteMultistateSpecies_RangeString n, final A argu) {
    R nRes = null;
    // multistateSpecies_SiteSingleElement -> MultistateSpecies_SiteSingleElement()
    n.multistateSpecies_SiteSingleElement.accept(this, argu);
    // nodeToken -> < EOF >
    n.nodeToken.accept(this, argu);
    return nRes;
  }

  /**
   * Visits a {@link MultistateSpecies} node, whose children are the following :
   * <p>
   * multistateSpecies_Name -> MultistateSpecies_Name()<br>
   * nodeOptional -> ( #0 < OPEN_R > #1 MultistateSpecies_SingleStateDefinition()<br>
   * ............ .. . #2 ( $0 < SITE_NAMES_SEPARATOR > $1 MultistateSpecies_SingleStateDefinition() )* #3 < CLOSED_R > )?<br>
   *
   * @param n the node to visit
   * @param argu the user argument
   * @return the user return information
   */
  public R visit(final MultistateSpecies n, final A argu) {
    R nRes = null;
    // multistateSpecies_Name -> MultistateSpecies_Name()
    n.multistateSpecies_Name.accept(this, argu);
    // nodeOptional -> ( #0 < OPEN_R > #1 MultistateSpecies_SingleStateDefinition()
    // ............ .. . #2 ( $0 < SITE_NAMES_SEPARATOR > $1 MultistateSpecies_SingleStateDefinition() )* #3 < CLOSED_R > )?
    n.nodeOptional.accept(this, argu);
    return nRes;
  }

  /**
   * Visits a {@link MultistateSpecies_SingleStateDefinition} node, whose children are the following :
   * <p>
   * multistateSpecies_SiteName -> MultistateSpecies_SiteName()<br>
   * nodeToken -> < OPEN_C ><br>
   * multistateSpecies_SiteSingleElement -> MultistateSpecies_SiteSingleElement()<br>
   * nodeListOptional -> ( #0 < SITE_STATES_SEPARATOR > #1 MultistateSpecies_SiteSingleElement() )*<br>
   * nodeToken1 -> < CLOSED_C ><br>
   * nodeOptional -> ( < CIRCULAR_FLAG > )?<br>
   *
   * @param n the node to visit
   * @param argu the user argument
   * @return the user return information
   */
  public R visit(final MultistateSpecies_SingleStateDefinition n, final A argu) {
    R nRes = null;
    // multistateSpecies_SiteName -> MultistateSpecies_SiteName()
    n.multistateSpecies_SiteName.accept(this, argu);
    // nodeToken -> < OPEN_C >
    n.nodeToken.accept(this, argu);
    // multistateSpecies_SiteSingleElement -> MultistateSpecies_SiteSingleElement()
    n.multistateSpecies_SiteSingleElement.accept(this, argu);
    // nodeListOptional -> ( #0 < SITE_STATES_SEPARATOR > #1 MultistateSpecies_SiteSingleElement() )*
    n.nodeListOptional.accept(this, argu);
    // nodeToken1 -> < CLOSED_C >
    n.nodeToken1.accept(this, argu);
    // nodeOptional -> ( < CIRCULAR_FLAG > )?
    n.nodeOptional.accept(this, argu);
    return nRes;
  }

  /**
   * Visits a {@link MultistateSpecies_Name} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 < STRING_LITERAL ><br>
   * .......... .. | %1 ( &0 < MULTI_IDENTIFIER ><br>
   * .......... .. . .. | &1 < CIRCULAR_FLAG ><br>
   * .......... .. . .. | &2 < CLOSED_R ><br>
   * .......... .. . .. | &3 < SITE_NAMES_SEPARATOR ><br>
   * .......... .. . .. | &4 < RANGE_SEPARATOR ><br>
   * .......... .. . .. | &5 < SITE_STATES_SEPARATOR ><br>
   * .......... .. . .. | &6 < CLOSED_C > )+<br>
   *
   * @param n the node to visit
   * @param argu the user argument
   * @return the user return information
   */
  public R visit(final MultistateSpecies_Name n, final A argu) {
    R nRes = null;
    // nodeChoice -> . %0 < STRING_LITERAL >
    // .......... .. | %1 ( &0 < MULTI_IDENTIFIER >
    // .......... .. . .. | &1 < CIRCULAR_FLAG >
    // .......... .. . .. | &2 < CLOSED_R >
    // .......... .. . .. | &3 < SITE_NAMES_SEPARATOR >
    // .......... .. . .. | &4 < RANGE_SEPARATOR >
    // .......... .. . .. | &5 < SITE_STATES_SEPARATOR >
    // .......... .. . .. | &6 < CLOSED_C > )+
    n.nodeChoice.accept(this, argu);
    return nRes;
  }

  /**
   * Visits a {@link MultistateSpecies_SiteSingleElement} node, whose children are the following :
   * <p>
   * nodeChoice -> ( %0 < STRING_LITERAL ><br>
   * .......... .. | %1 ( &0 < MULTI_IDENTIFIER ><br>
   * .......... .. . .. | &1 < NUMBER ><br>
   * .......... .. . .. | &2 < OPEN_R > )+ )<br>
   * nodeOptional -> ( #0 ( " " )* #1 < RANGE_SEPARATOR ><br>
   * ............ .. . #2 ( " " )*<br>
   * ............ .. . #3 ( %0 < STRING_LITERAL ><br>
   * ............ .. . .. | %1 ( &0 < MULTI_IDENTIFIER ><br>
   * ............ .. . .. . .. | &1 < NUMBER ><br>
   * ............ .. . .. . .. | &2 < OPEN_R > )+ ) )?<br>
   *
   * @param n the node to visit
   * @param argu the user argument
   * @return the user return information
   */
  public R visit(final MultistateSpecies_SiteSingleElement n, final A argu) {
    R nRes = null;
    // nodeChoice -> ( %0 < STRING_LITERAL >
    // .......... .. | %1 ( &0 < MULTI_IDENTIFIER >
    // .......... .. . .. | &1 < NUMBER >
    // .......... .. . .. | &2 < OPEN_R > )+ )
    n.nodeChoice.accept(this, argu);
    // nodeOptional -> ( #0 ( " " )* #1 < RANGE_SEPARATOR >
    // ............ .. . #2 ( " " )*
    // ............ .. . #3 ( %0 < STRING_LITERAL >
    // ............ .. . .. | %1 ( &0 < MULTI_IDENTIFIER >
    // ............ .. . .. . .. | &1 < NUMBER >
    // ............ .. . .. . .. | &2 < OPEN_R > )+ ) )?
    n.nodeOptional.accept(this, argu);
    return nRes;
  }

  /**
   * Visits a {@link MultistateSpecies_SiteSingleElement_Range} node, whose children are the following :
   * <p>
   * nodeToken -> < NUMBER ><br>
   * nodeListOptional -> ( " " )*<br>
   * nodeToken1 -> < RANGE_SEPARATOR ><br>
   * nodeListOptional1 -> ( " " )*<br>
   * nodeToken2 -> < NUMBER ><br>
   *
   * @param n the node to visit
   * @param argu the user argument
   * @return the user return information
   */
  public R visit(final MultistateSpecies_SiteSingleElement_Range n, final A argu) {
    R nRes = null;
    // nodeToken -> < NUMBER >
    n.nodeToken.accept(this, argu);
    // nodeListOptional -> ( " " )*
    n.nodeListOptional.accept(this, argu);
    // nodeToken1 -> < RANGE_SEPARATOR >
    n.nodeToken1.accept(this, argu);
    // nodeListOptional1 -> ( " " )*
    n.nodeListOptional1.accept(this, argu);
    // nodeToken2 -> < NUMBER >
    n.nodeToken2.accept(this, argu);
    return nRes;
  }

  /**
   * Visits a {@link MultistateSpecies_SiteName} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 < STRING_LITERAL ><br>
   * .......... .. | %1 ( &0 < MULTI_IDENTIFIER ><br>
   * .......... .. . .. | &1 < CLOSED_C ><br>
   * .......... .. . .. | &2 < OPEN_R ><br>
   * .......... .. . .. | &3 < CLOSED_R ><br>
   * .......... .. . .. | &4 < SITE_STATES_SEPARATOR > )+<br>
   *
   * @param n the node to visit
   * @param argu the user argument
   * @return the user return information
   */
  public R visit(final MultistateSpecies_SiteName n, final A argu) {
    R nRes = null;
    // nodeChoice -> . %0 < STRING_LITERAL >
    // .......... .. | %1 ( &0 < MULTI_IDENTIFIER >
    // .......... .. . .. | &1 < CLOSED_C >
    // .......... .. . .. | &2 < OPEN_R >
    // .......... .. . .. | &3 < CLOSED_R >
    // .......... .. . .. | &4 < SITE_STATES_SEPARATOR > )+
    n.nodeChoice.accept(this, argu);
    return nRes;
  }

  /**
   * Visits a {@link MultistateSpecies_Operator} node, whose children are the following :
   * <p>
   * multistateSpecies_Name -> MultistateSpecies_Name()<br>
   * nodeOptional -> ( #0 < OPEN_R > #1 MultistateSpecies_Operator_SingleSite()<br>
   * ............ .. . #2 ( $0 < SITE_NAMES_SEPARATOR > $1 MultistateSpecies_Operator_SingleSite() )* #3 < CLOSED_R > )?<br>
   *
   * @param n the node to visit
   * @param argu the user argument
   * @return the user return information
   */
  public R visit(final MultistateSpecies_Operator n, final A argu) {
    R nRes = null;
    // multistateSpecies_Name -> MultistateSpecies_Name()
    n.multistateSpecies_Name.accept(this, argu);
    // nodeOptional -> ( #0 < OPEN_R > #1 MultistateSpecies_Operator_SingleSite()
    // ............ .. . #2 ( $0 < SITE_NAMES_SEPARATOR > $1 MultistateSpecies_Operator_SingleSite() )* #3 < CLOSED_R > )?
    n.nodeOptional.accept(this, argu);
    return nRes;
  }

  /**
   * Visits a {@link MultistateSpecies_Operator_SingleSite} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 #0 ( &0 < SUCC ><br>
   * .......... .. . .. .. | &1 < PREC > ) #1 < OPEN_R > #2 MultistateSpecies_Operator_SiteName() #3 < CLOSED_R ><br>
   * .......... .. | %1 #0 MultistateSpecies_Operator_SiteName()<br>
   * .......... .. . .. #1 ( $0 < OPEN_C > $1 MultistateSpecies_Operator_SiteSingleState() $2 < CLOSED_C > )?<br>
   *
   * @param n the node to visit
   * @param argu the user argument
   * @return the user return information
   */
  public R visit(final MultistateSpecies_Operator_SingleSite n, final A argu) {
    R nRes = null;
    // nodeChoice -> . %0 #0 ( &0 < SUCC >
    // .......... .. . .. .. | &1 < PREC > ) #1 < OPEN_R > #2 MultistateSpecies_Operator_SiteName() #3 < CLOSED_R >
    // .......... .. | %1 #0 MultistateSpecies_Operator_SiteName()
    // .......... .. . .. #1 ( $0 < OPEN_C > $1 MultistateSpecies_Operator_SiteSingleState() $2 < CLOSED_C > )?
    n.nodeChoice.accept(this, argu);
    return nRes;
  }

  /**
   * Visits a {@link MultistateSpecies_Operator_SiteName} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 < STRING_LITERAL ><br>
   * .......... .. | %1 ( &0 < MULTI_IDENTIFIER ><br>
   * .......... .. . .. | &1 < NUMBER > )+<br>
   *
   * @param n the node to visit
   * @param argu the user argument
   * @return the user return information
   */
  public R visit(final MultistateSpecies_Operator_SiteName n, final A argu) {
    R nRes = null;
    // nodeChoice -> . %0 < STRING_LITERAL >
    // .......... .. | %1 ( &0 < MULTI_IDENTIFIER >
    // .......... .. . .. | &1 < NUMBER > )+
    n.nodeChoice.accept(this, argu);
    return nRes;
  }

  /**
   * Visits a {@link MultistateSpecies_Operator_SiteSingleState} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 < STRING_LITERAL ><br>
   * .......... .. | %1 ( &0 < MULTI_IDENTIFIER ><br>
   * .......... .. . .. | &1 < NUMBER > )+<br>
   *
   * @param n the node to visit
   * @param argu the user argument
   * @return the user return information
   */
  public R visit(final MultistateSpecies_Operator_SiteSingleState n, final A argu) {
    R nRes = null;
    // nodeChoice -> . %0 < STRING_LITERAL >
    // .......... .. | %1 ( &0 < MULTI_IDENTIFIER >
    // .......... .. . .. | &1 < NUMBER > )+
    n.nodeChoice.accept(this, argu);
    return nRes;
  }

}
