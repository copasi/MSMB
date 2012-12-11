/* Generated by JTB 1.4.4 */
package msmb.parsers.multistateSpecies.visitor;

import msmb.parsers.multistateSpecies.syntaxtree.*;
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
   * nodeListOptional -> ( PossibleExtensions() )*<br>
   * nodeToken -> < EOF ><br>
   *
   * @param n the node to visit
   * @param argu the user argument
   * @return the user return information
   */
  public R visit(final CompleteMultistateSpecies n, final A argu) {
    R nRes = null;
    final MultistateSpecies n0 = n.multistateSpecies;
    nRes = n0.accept(this, argu);
    final NodeListOptional n1 = n.nodeListOptional;
    if (n1.present()) {
      for (int i = 0; i < n1.size(); i++) {
        final INode n1Ei = n1.elementAt(i);
        nRes = n1Ei.accept(this, argu);
      }
    }
    final NodeToken n2 = n.nodeToken;
    nRes = n2.accept(this, argu);
    return nRes;
  }

  /**
   * Visits a {@link CompleteMultistateSpecies_Operator} node, whose children are the following :
   * <p>
   * multistateSpecies_Operator -> MultistateSpecies_Operator()<br>
   * nodeListOptional -> ( PossibleExtensions() )*<br>
   * nodeToken -> < EOF ><br>
   *
   * @param n the node to visit
   * @param argu the user argument
   * @return the user return information
   */
  public R visit(final CompleteMultistateSpecies_Operator n, final A argu) {
    R nRes = null;
    final MultistateSpecies_Operator n0 = n.multistateSpecies_Operator;
    nRes = n0.accept(this, argu);
    final NodeListOptional n1 = n.nodeListOptional;
    if (n1.present()) {
      for (int i = 0; i < n1.size(); i++) {
        final INode n1Ei = n1.elementAt(i);
        nRes = n1Ei.accept(this, argu);
      }
    }
    final NodeToken n2 = n.nodeToken;
    nRes = n2.accept(this, argu);
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
    final MultistateSpecies_SiteSingleElement_Range n0 = n.multistateSpecies_SiteSingleElement_Range;
    nRes = n0.accept(this, argu);
    final NodeToken n1 = n.nodeToken;
    nRes = n1.accept(this, argu);
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
    final MultistateSpecies_SiteSingleElement n0 = n.multistateSpecies_SiteSingleElement;
    nRes = n0.accept(this, argu);
    final NodeToken n1 = n.nodeToken;
    nRes = n1.accept(this, argu);
    return nRes;
  }

  /**
   * Visits a {@link PossibleExtensions} node, whose children are the following :
   * <p>
   * nodeChoice -> . %00 < EXTENSION_CONC ><br>
   * .......... .. | %01 < EXTENSION_COMPARTMENT ><br>
   * .......... .. | %02 < EXTENSION_PARTICLE ><br>
   * .......... .. | %03 < EXTENSION_TRANS ><br>
   * .......... .. | %04 < EXTENSION_INIT ><br>
   * .......... .. | %05 < EXTENSION_RATE ><br>
   * .......... .. | %06 < EXTENSION_SPECIES ><br>
   * .......... .. | %07 < EXTENSION_GLOBALQ ><br>
   * .......... .. | %08 < EXTENSION_FUNCTION ><br>
   * .......... .. | %09 < EXTENSION_REACTION ><br>
   * .......... .. | %10 < EXTENSION_FLUX ><br>
   *
   * @param n the node to visit
   * @param argu the user argument
   * @return the user return information
   */
  public R visit(final PossibleExtensions n, final A argu) {
    R nRes = null;
    final NodeChoice n0Ch = n.nodeChoice;
    final INode n0ChN = n0Ch.choice;
    switch (n0Ch.which) {
      case 0:
        nRes = n0ChN.accept(this, argu);
        break;
      case 1:
        nRes = n0ChN.accept(this, argu);
        break;
      case 2:
        nRes = n0ChN.accept(this, argu);
        break;
      case 3:
        nRes = n0ChN.accept(this, argu);
        break;
      case 4:
        nRes = n0ChN.accept(this, argu);
        break;
      case 5:
        nRes = n0ChN.accept(this, argu);
        break;
      case 6:
        nRes = n0ChN.accept(this, argu);
        break;
      case 7:
        nRes = n0ChN.accept(this, argu);
        break;
      case 8:
        nRes = n0ChN.accept(this, argu);
        break;
      case 9:
        nRes = n0ChN.accept(this, argu);
        break;
      case 10:
        nRes = n0ChN.accept(this, argu);
        break;
      default:
        // should not occur !!!
        break;
    }
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
    final MultistateSpecies_Name n0 = n.multistateSpecies_Name;
    nRes = n0.accept(this, argu);
    final NodeOptional n1 = n.nodeOptional;
    if (n1.present()) {
      final NodeSequence n1Seq = (NodeSequence) n1.node;
      final INode n1SeqA0 = n1Seq.elementAt(0);
      nRes = n1SeqA0.accept(this, argu);
      final INode n1SeqA1 = n1Seq.elementAt(1);
      nRes = n1SeqA1.accept(this, argu);
      final INode n1SeqA2 = n1Seq.elementAt(2);
      final NodeListOptional n1SeqA2Nlo = (NodeListOptional) n1SeqA2;
      if (n1SeqA2Nlo.present()) {
        for (int i = 0; i < n1SeqA2Nlo.size(); i++) {
          final INode n1SeqA2NloEi = n1SeqA2Nlo.elementAt(i);
          final NodeSequence n1SeqA2NloEiSeq = (NodeSequence) n1SeqA2NloEi;
          final INode n1SeqA2NloEiSeqA0 = n1SeqA2NloEiSeq.elementAt(0);
          nRes = n1SeqA2NloEiSeqA0.accept(this, argu);
          final INode n1SeqA2NloEiSeqA1 = n1SeqA2NloEiSeq.elementAt(1);
          nRes = n1SeqA2NloEiSeqA1.accept(this, argu);
        }
      }
      final INode n1SeqA3 = n1Seq.elementAt(3);
      nRes = n1SeqA3.accept(this, argu);
    }
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
    final MultistateSpecies_SiteName n0 = n.multistateSpecies_SiteName;
    nRes = n0.accept(this, argu);
    final NodeToken n1 = n.nodeToken;
    nRes = n1.accept(this, argu);
    final MultistateSpecies_SiteSingleElement n2 = n.multistateSpecies_SiteSingleElement;
    nRes = n2.accept(this, argu);
    final NodeListOptional n3 = n.nodeListOptional;
    if (n3.present()) {
      for (int i = 0; i < n3.size(); i++) {
        final INode n3Ei = n3.elementAt(i);
        final NodeSequence n3EiSeq = (NodeSequence) n3Ei;
        final INode n3EiSeqA0 = n3EiSeq.elementAt(0);
        nRes = n3EiSeqA0.accept(this, argu);
        final INode n3EiSeqA1 = n3EiSeq.elementAt(1);
        nRes = n3EiSeqA1.accept(this, argu);
      }
    }
    final NodeToken n4 = n.nodeToken1;
    nRes = n4.accept(this, argu);
    final NodeOptional n5 = n.nodeOptional;
    if (n5.present()) {
      nRes = n5.accept(this, argu);
    }
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
    final NodeChoice n0Ch = n.nodeChoice;
    final INode n0ChN = n0Ch.choice;
    switch (n0Ch.which) {
      case 0:
        nRes = n0ChN.accept(this, argu);
        break;
      case 1:
        final NodeList n0ChNLst = (NodeList) n0ChN;
        for (int i = 0; i < n0ChNLst.size(); i++) {
          final INode n0ChNLstEi = n0ChNLst.elementAt(i);
          final NodeChoice n0ChNLstEiCh = (NodeChoice) n0ChNLstEi;
          final INode n0ChNLstEiChN = n0ChNLstEiCh.choice;
          switch (n0ChNLstEiCh.which) {
            case 0:
              nRes = n0ChNLstEiChN.accept(this, argu);
              break;
            case 1:
              nRes = n0ChNLstEiChN.accept(this, argu);
              break;
            case 2:
              nRes = n0ChNLstEiChN.accept(this, argu);
              break;
            case 3:
              nRes = n0ChNLstEiChN.accept(this, argu);
              break;
            case 4:
              nRes = n0ChNLstEiChN.accept(this, argu);
              break;
            case 5:
              nRes = n0ChNLstEiChN.accept(this, argu);
              break;
            case 6:
              nRes = n0ChNLstEiChN.accept(this, argu);
              break;
            default:
              // should not occur !!!
              break;
          }
        }
        break;
      default:
        // should not occur !!!
        break;
    }
    return nRes;
  }

  /**
   * Visits a {@link MultistateSpecies_SiteSingleElement} node, whose children are the following :
   * <p>
   * nodeChoice -> ( %0 < STRING_LITERAL ><br>
   * .......... .. | %1 ( &0 < CIRCULAR_FLAG ><br>
   * .......... .. . .. | &1 < MULTI_IDENTIFIER ><br>
   * .......... .. . .. | &2 < NUMBER ><br>
   * .......... .. . .. | &3 < OPEN_R > )+ )<br>
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
    final NodeChoice n0 = n.nodeChoice;
    final INode n0N = n0.choice;
    switch (n0.which) {
      case 0:
        nRes = n0N.accept(this, argu);
        break;
      case 1:
        final NodeList n0NLst = (NodeList) n0N;
        for (int i = 0; i < n0NLst.size(); i++) {
          final INode n0NLstEi = n0NLst.elementAt(i);
          final NodeChoice n0NLstEiCh = (NodeChoice) n0NLstEi;
          final INode n0NLstEiChN = n0NLstEiCh.choice;
          switch (n0NLstEiCh.which) {
            case 0:
              nRes = n0NLstEiChN.accept(this, argu);
              break;
            case 1:
              nRes = n0NLstEiChN.accept(this, argu);
              break;
            case 2:
              nRes = n0NLstEiChN.accept(this, argu);
              break;
            case 3:
              nRes = n0NLstEiChN.accept(this, argu);
              break;
            default:
              // should not occur !!!
              break;
          }
        }
        break;
      default:
        // should not occur !!!
        break;
    }
    final NodeOptional n1 = n.nodeOptional;
    if (n1.present()) {
      final NodeSequence n1Seq = (NodeSequence) n1.node;
      final INode n1SeqA0 = n1Seq.elementAt(0);
      final NodeListOptional n1SeqA0Nlo = (NodeListOptional) n1SeqA0;
      if (n1SeqA0Nlo.present()) {
        for (int i = 0; i < n1SeqA0Nlo.size(); i++) {
          final INode n1SeqA0NloEi = n1SeqA0Nlo.elementAt(i);
          nRes = n1SeqA0NloEi.accept(this, argu);
        }
      }
      final INode n1SeqA1 = n1Seq.elementAt(1);
      nRes = n1SeqA1.accept(this, argu);
      final INode n1SeqA2 = n1Seq.elementAt(2);
      final NodeListOptional n1SeqA2Nlo = (NodeListOptional) n1SeqA2;
      if (n1SeqA2Nlo.present()) {
        for (int i = 0; i < n1SeqA2Nlo.size(); i++) {
          final INode n1SeqA2NloEi = n1SeqA2Nlo.elementAt(i);
          nRes = n1SeqA2NloEi.accept(this, argu);
        }
      }
      final INode n1SeqA3 = n1Seq.elementAt(3);
      final NodeChoice n1SeqA3Ch = (NodeChoice) n1SeqA3;
      final INode n1SeqA3ChN = n1SeqA3Ch.choice;
      switch (n1SeqA3Ch.which) {
        case 0:
          nRes = n1SeqA3ChN.accept(this, argu);
          break;
        case 1:
          final NodeList n1SeqA3ChNLst = (NodeList) n1SeqA3ChN;
          for (int i = 0; i < n1SeqA3ChNLst.size(); i++) {
            final INode n1SeqA3ChNLstEi = n1SeqA3ChNLst.elementAt(i);
            final NodeChoice n1SeqA3ChNLstEiCh = (NodeChoice) n1SeqA3ChNLstEi;
            final INode n1SeqA3ChNLstEiChN = n1SeqA3ChNLstEiCh.choice;
            switch (n1SeqA3ChNLstEiCh.which) {
              case 0:
                nRes = n1SeqA3ChNLstEiChN.accept(this, argu);
                break;
              case 1:
                nRes = n1SeqA3ChNLstEiChN.accept(this, argu);
                break;
              case 2:
                nRes = n1SeqA3ChNLstEiChN.accept(this, argu);
                break;
              default:
                // should not occur !!!
                break;
            }
          }
          break;
        default:
          // should not occur !!!
          break;
      }
    }
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
    final NodeToken n0 = n.nodeToken;
    nRes = n0.accept(this, argu);
    final NodeListOptional n1 = n.nodeListOptional;
    if (n1.present()) {
      for (int i = 0; i < n1.size(); i++) {
        final INode n1Ei = n1.elementAt(i);
        nRes = n1Ei.accept(this, argu);
      }
    }
    final NodeToken n2 = n.nodeToken1;
    nRes = n2.accept(this, argu);
    final NodeListOptional n3 = n.nodeListOptional1;
    if (n3.present()) {
      for (int i = 0; i < n3.size(); i++) {
        final INode n3Ei = n3.elementAt(i);
        nRes = n3Ei.accept(this, argu);
      }
    }
    final NodeToken n4 = n.nodeToken2;
    nRes = n4.accept(this, argu);
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
    final NodeChoice n0Ch = n.nodeChoice;
    final INode n0ChN = n0Ch.choice;
    switch (n0Ch.which) {
      case 0:
        nRes = n0ChN.accept(this, argu);
        break;
      case 1:
        final NodeList n0ChNLst = (NodeList) n0ChN;
        for (int i = 0; i < n0ChNLst.size(); i++) {
          final INode n0ChNLstEi = n0ChNLst.elementAt(i);
          final NodeChoice n0ChNLstEiCh = (NodeChoice) n0ChNLstEi;
          final INode n0ChNLstEiChN = n0ChNLstEiCh.choice;
          switch (n0ChNLstEiCh.which) {
            case 0:
              nRes = n0ChNLstEiChN.accept(this, argu);
              break;
            case 1:
              nRes = n0ChNLstEiChN.accept(this, argu);
              break;
            case 2:
              nRes = n0ChNLstEiChN.accept(this, argu);
              break;
            case 3:
              nRes = n0ChNLstEiChN.accept(this, argu);
              break;
            case 4:
              nRes = n0ChNLstEiChN.accept(this, argu);
              break;
            default:
              // should not occur !!!
              break;
          }
        }
        break;
      default:
        // should not occur !!!
        break;
    }
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
    final MultistateSpecies_Name n0 = n.multistateSpecies_Name;
    nRes = n0.accept(this, argu);
    final NodeOptional n1 = n.nodeOptional;
    if (n1.present()) {
      final NodeSequence n1Seq = (NodeSequence) n1.node;
      final INode n1SeqA0 = n1Seq.elementAt(0);
      nRes = n1SeqA0.accept(this, argu);
      final INode n1SeqA1 = n1Seq.elementAt(1);
      nRes = n1SeqA1.accept(this, argu);
      final INode n1SeqA2 = n1Seq.elementAt(2);
      final NodeListOptional n1SeqA2Nlo = (NodeListOptional) n1SeqA2;
      if (n1SeqA2Nlo.present()) {
        for (int i = 0; i < n1SeqA2Nlo.size(); i++) {
          final INode n1SeqA2NloEi = n1SeqA2Nlo.elementAt(i);
          final NodeSequence n1SeqA2NloEiSeq = (NodeSequence) n1SeqA2NloEi;
          final INode n1SeqA2NloEiSeqA0 = n1SeqA2NloEiSeq.elementAt(0);
          nRes = n1SeqA2NloEiSeqA0.accept(this, argu);
          final INode n1SeqA2NloEiSeqA1 = n1SeqA2NloEiSeq.elementAt(1);
          nRes = n1SeqA2NloEiSeqA1.accept(this, argu);
        }
      }
      final INode n1SeqA3 = n1Seq.elementAt(3);
      nRes = n1SeqA3.accept(this, argu);
    }
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
    final NodeChoice n0Ch = n.nodeChoice;
    final INode n0ChN = n0Ch.choice;
    switch (n0Ch.which) {
      case 0:
        final NodeSequence n0ChNSeq0 = (NodeSequence) n0ChN;
        final INode n0ChNSeq0A0 = n0ChNSeq0.elementAt(0);
        final NodeChoice n0ChNSeq0A0Ch = (NodeChoice) n0ChNSeq0A0;
        final INode n0ChNSeq0A0ChN = n0ChNSeq0A0Ch.choice;
        switch (n0ChNSeq0A0Ch.which) {
          case 0:
            nRes = n0ChNSeq0A0ChN.accept(this, argu);
            break;
          case 1:
            nRes = n0ChNSeq0A0ChN.accept(this, argu);
            break;
          default:
            // should not occur !!!
            break;
        }
        final INode n0ChNSeq0A1 = n0ChNSeq0.elementAt(1);
        nRes = n0ChNSeq0A1.accept(this, argu);
        final INode n0ChNSeq0A2 = n0ChNSeq0.elementAt(2);
        nRes = n0ChNSeq0A2.accept(this, argu);
        final INode n0ChNSeq0A3 = n0ChNSeq0.elementAt(3);
        nRes = n0ChNSeq0A3.accept(this, argu);
        break;
      case 1:
        final NodeSequence n0ChNSeq1 = (NodeSequence) n0ChN;
        final INode n0ChNSeq1A0 = n0ChNSeq1.elementAt(0);
        nRes = n0ChNSeq1A0.accept(this, argu);
        final INode n0ChNSeq1A1 = n0ChNSeq1.elementAt(1);
        final NodeOptional n0ChNSeq1A1Opt = (NodeOptional) n0ChNSeq1A1;
        if (n0ChNSeq1A1Opt.present()) {
          final NodeSequence n0ChNSeq1A1OptSeq = (NodeSequence) n0ChNSeq1A1Opt.node;
          final INode n0ChNSeq1A1OptSeqA0 = n0ChNSeq1A1OptSeq.elementAt(0);
          nRes = n0ChNSeq1A1OptSeqA0.accept(this, argu);
          final INode n0ChNSeq1A1OptSeqA1 = n0ChNSeq1A1OptSeq.elementAt(1);
          nRes = n0ChNSeq1A1OptSeqA1.accept(this, argu);
          final INode n0ChNSeq1A1OptSeqA2 = n0ChNSeq1A1OptSeq.elementAt(2);
          nRes = n0ChNSeq1A1OptSeqA2.accept(this, argu);
        }
        break;
      default:
        // should not occur !!!
        break;
    }
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
    final NodeChoice n0Ch = n.nodeChoice;
    final INode n0ChN = n0Ch.choice;
    switch (n0Ch.which) {
      case 0:
        nRes = n0ChN.accept(this, argu);
        break;
      case 1:
        final NodeList n0ChNLst = (NodeList) n0ChN;
        for (int i = 0; i < n0ChNLst.size(); i++) {
          final INode n0ChNLstEi = n0ChNLst.elementAt(i);
          final NodeChoice n0ChNLstEiCh = (NodeChoice) n0ChNLstEi;
          final INode n0ChNLstEiChN = n0ChNLstEiCh.choice;
          switch (n0ChNLstEiCh.which) {
            case 0:
              nRes = n0ChNLstEiChN.accept(this, argu);
              break;
            case 1:
              nRes = n0ChNLstEiChN.accept(this, argu);
              break;
            default:
              // should not occur !!!
              break;
          }
        }
        break;
      default:
        // should not occur !!!
        break;
    }
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
    final NodeChoice n0Ch = n.nodeChoice;
    final INode n0ChN = n0Ch.choice;
    switch (n0Ch.which) {
      case 0:
        nRes = n0ChN.accept(this, argu);
        break;
      case 1:
        final NodeList n0ChNLst = (NodeList) n0ChN;
        for (int i = 0; i < n0ChNLst.size(); i++) {
          final INode n0ChNLstEi = n0ChNLst.elementAt(i);
          final NodeChoice n0ChNLstEiCh = (NodeChoice) n0ChNLstEi;
          final INode n0ChNLstEiChN = n0ChNLstEiCh.choice;
          switch (n0ChNLstEiCh.which) {
            case 0:
              nRes = n0ChNLstEiChN.accept(this, argu);
              break;
            case 1:
              nRes = n0ChNLstEiChN.accept(this, argu);
              break;
            default:
              // should not occur !!!
              break;
          }
        }
        break;
      default:
        // should not occur !!!
        break;
    }
    return nRes;
  }

}
