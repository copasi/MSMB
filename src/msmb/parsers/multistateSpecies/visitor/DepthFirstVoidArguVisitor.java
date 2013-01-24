/* Generated by JTB 1.4.7 */
package msmb.parsers.multistateSpecies.visitor;

import msmb.parsers.multistateSpecies.syntaxtree.*;
import java.util.*;

/**
 * Provides default methods which visit each node in the tree in depth-first order.<br>
 * In your "VoidArgu" visitors extend this class and override part or all of these methods.
 *
 * @param <A> - The user argument type
 */
public class DepthFirstVoidArguVisitor<A> implements IVoidArguVisitor<A> {


  /*
   * Base nodes classes visit methods (to be overridden if necessary)
   */

  /**
   * Visits a {@link NodeChoice} node.
   *
   * @param n - the node to visit
   * @param argu - the user argument
   */
  public void visit(final NodeChoice n, final A argu) {
    n.choice.accept(this, argu);
    return;
  }

  /**
   * Visits a {@link NodeList} node.
   *
   * @param n - the node to visit
   * @param argu - the user argument
   */
  public void visit(final NodeList n, final A argu) {
    for (final Iterator<INode> e = n.elements(); e.hasNext();) {
      e.next().accept(this, argu);
    }
    return;
  }

  /**
   * Visits a {@link NodeListOptional} node.
   *
   * @param n - the node to visit
   * @param argu - the user argument
   */
  public void visit(final NodeListOptional n, final A argu) {
    if (n.present()) {
      for (final Iterator<INode> e = n.elements(); e.hasNext();) {
        e.next().accept(this, argu);
        }
      return;
    } else
      return;
  }

  /**
   * Visits a {@link NodeOptional} node.
   *
   * @param n - the node to visit
   * @param argu - the user argument
   */
  public void visit(final NodeOptional n, final A argu) {
    if (n.present()) {
      n.node.accept(this, argu);
      return;
    } else
      return;
  }

  /**
   * Visits a {@link NodeSequence} node.
   *
   * @param n - the node to visit
   * @param argu - the user argument
   */
  public void visit(final NodeSequence n, final A argu) {
    for (final Iterator<INode> e = n.elements(); e.hasNext();) {
      e.next().accept(this, argu);
    }
    return;
  }

  /**
   * Visits a {@link NodeTCF} node.
   *
   * @param n - the node to visit
   * @param argu - the user argument
   */
  public void visit(final NodeTCF n, final A argu) {
    @SuppressWarnings("unused")
    final String tkIm = n.tokenImage;
    return;
  }

  /**
   * Visits a {@link NodeToken} node.
   *
   * @param n - the node to visit
   * @param argu - the user argument
   */
  public void visit(final NodeToken n, final A argu) {
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
   * nodeListOptional -> ( PossibleExtensions() )*<br>
   * nodeToken -> <EOF><br>
   *
   * @param n - the node to visit
   * @param argu - the user argument
   */
  public void visit(final CompleteMultistateSpecies n, final A argu) {
    // multistateSpecies -> MultistateSpecies()
    final MultistateSpecies n0 = n.multistateSpecies;
    n0.accept(this, argu);
    // nodeListOptional -> ( PossibleExtensions() )*
    final NodeListOptional n1 = n.nodeListOptional;
    if (n1.present()) {
      for (int i = 0; i < n1.size(); i++) {
        final INode n1Mi = n1.elementAt(i);
        n1Mi.accept(this, argu);
      }
    }
    // nodeToken -> <EOF>
    final NodeToken n2 = n.nodeToken;
    n2.accept(this, argu);
  }

  /**
   * Visits a {@link CompleteMultistateSpecies_Operator} node, whose children are the following :
   * <p>
   * multistateSpecies_Operator -> MultistateSpecies_Operator()<br>
   * nodeListOptional -> ( PossibleExtensions() )*<br>
   * nodeToken -> <EOF><br>
   *
   * @param n - the node to visit
   * @param argu - the user argument
   */
  public void visit(final CompleteMultistateSpecies_Operator n, final A argu) {
    // multistateSpecies_Operator -> MultistateSpecies_Operator()
    final MultistateSpecies_Operator n0 = n.multistateSpecies_Operator;
    n0.accept(this, argu);
    // nodeListOptional -> ( PossibleExtensions() )*
    final NodeListOptional n1 = n.nodeListOptional;
    if (n1.present()) {
      for (int i = 0; i < n1.size(); i++) {
        final INode n1Mi = n1.elementAt(i);
        n1Mi.accept(this, argu);
      }
    }
    // nodeToken -> <EOF>
    final NodeToken n2 = n.nodeToken;
    n2.accept(this, argu);
  }

  /**
   * Visits a {@link CompleteMultistateSpecies_Range} node, whose children are the following :
   * <p>
   * multistateSpecies_SiteSingleElement_Range -> MultistateSpecies_SiteSingleElement_Range()<br>
   * nodeToken -> <EOF><br>
   *
   * @param n - the node to visit
   * @param argu - the user argument
   */
  public void visit(final CompleteMultistateSpecies_Range n, final A argu) {
    // multistateSpecies_SiteSingleElement_Range -> MultistateSpecies_SiteSingleElement_Range()
    final MultistateSpecies_SiteSingleElement_Range n0 = n.multistateSpecies_SiteSingleElement_Range;
    n0.accept(this, argu);
    // nodeToken -> <EOF>
    final NodeToken n1 = n.nodeToken;
    n1.accept(this, argu);
  }

  /**
   * Visits a {@link CompleteMultistateSpecies_RangeString} node, whose children are the following :
   * <p>
   * multistateSpecies_SiteSingleElement -> MultistateSpecies_SiteSingleElement()<br>
   * nodeToken -> <EOF><br>
   *
   * @param n - the node to visit
   * @param argu - the user argument
   */
  public void visit(final CompleteMultistateSpecies_RangeString n, final A argu) {
    // multistateSpecies_SiteSingleElement -> MultistateSpecies_SiteSingleElement()
    final MultistateSpecies_SiteSingleElement n0 = n.multistateSpecies_SiteSingleElement;
    n0.accept(this, argu);
    // nodeToken -> <EOF>
    final NodeToken n1 = n.nodeToken;
    n1.accept(this, argu);
  }

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
   */
  public void visit(final PossibleExtensions n, final A argu) {
    // nodeChoice -> . %00 <EXTENSION_CONC>
    // .......... .. | %01 <EXTENSION_COMPARTMENT>
    // .......... .. | %02 <EXTENSION_PARTICLE>
    // .......... .. | %03 <EXTENSION_TRANS>
    // .......... .. | %04 <EXTENSION_INIT>
    // .......... .. | %05 <EXTENSION_RATE>
    // .......... .. | %06 <EXTENSION_SPECIES>
    // .......... .. | %07 <EXTENSION_GLOBALQ>
    // .......... .. | %08 <EXTENSION_FUNCTION>
    // .......... .. | %09 <EXTENSION_REACTION>
    // .......... .. | %10 <EXTENSION_FLUX>
    final NodeChoice n0C = n.nodeChoice;
    final INode n0CH = n0C.choice;
    switch (n0C.which) {
      case 0:
        // %00 <EXTENSION_CONC>
        n0CH.accept(this, argu);
        break;
      case 1:
        // %01 <EXTENSION_COMPARTMENT>
        n0CH.accept(this, argu);
        break;
      case 2:
        // %02 <EXTENSION_PARTICLE>
        n0CH.accept(this, argu);
        break;
      case 3:
        // %03 <EXTENSION_TRANS>
        n0CH.accept(this, argu);
        break;
      case 4:
        // %04 <EXTENSION_INIT>
        n0CH.accept(this, argu);
        break;
      case 5:
        // %05 <EXTENSION_RATE>
        n0CH.accept(this, argu);
        break;
      case 6:
        // %06 <EXTENSION_SPECIES>
        n0CH.accept(this, argu);
        break;
      case 7:
        // %07 <EXTENSION_GLOBALQ>
        n0CH.accept(this, argu);
        break;
      case 8:
        // %08 <EXTENSION_FUNCTION>
        n0CH.accept(this, argu);
        break;
      case 9:
        // %09 <EXTENSION_REACTION>
        n0CH.accept(this, argu);
        break;
      case 10:
        // %10 <EXTENSION_FLUX>
        n0CH.accept(this, argu);
        break;
      default:
        // should not occur !!!
        break;
    }
  }

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
   */
  public void visit(final MultistateSpecies n, final A argu) {
    // multistateSpecies_Name -> MultistateSpecies_Name()
    final MultistateSpecies_Name n0 = n.multistateSpecies_Name;
    n0.accept(this, argu);
    // nodeOptional -> ( #0 <OPEN_R> #1 MultistateSpecies_SingleStateDefinition()
    // ............ .. . #2 ( $0 <SITE_NAMES_SEPARATOR> $1 MultistateSpecies_SingleStateDefinition() )*
    // ............ .. . #3 <CLOSED_R> )?
    final NodeOptional n1 = n.nodeOptional;
    if (n1.present()) {
      final NodeSequence n1S0 = (NodeSequence) n1.node;
      // #0 <OPEN_R>
      final INode n1S0A0 = n1S0.elementAt(0);
      n1S0A0.accept(this, argu);
      // #1 MultistateSpecies_SingleStateDefinition()
      final INode n1S0A1 = n1S0.elementAt(1);
      n1S0A1.accept(this, argu);
      // #2 ( $0 <SITE_NAMES_SEPARATOR> $1 MultistateSpecies_SingleStateDefinition() )*
      final INode n1S0A2 = n1S0.elementAt(2);
      final NodeListOptional n1S0A2T = (NodeListOptional) n1S0A2;
      if (n1S0A2T.present()) {
        for (int i = 0; i < n1S0A2T.size(); i++) {
          final INode n1S0A2TMi = n1S0A2T.elementAt(i);
          final NodeSequence n1S0A2TMiS1 = (NodeSequence) n1S0A2TMi;
          // $0 <SITE_NAMES_SEPARATOR>
          final INode n1S0A2TMiS1A0 = n1S0A2TMiS1.elementAt(0);
          n1S0A2TMiS1A0.accept(this, argu);
          // $1 MultistateSpecies_SingleStateDefinition()
          final INode n1S0A2TMiS1A1 = n1S0A2TMiS1.elementAt(1);
          n1S0A2TMiS1A1.accept(this, argu);
        }
      }
      // #3 <CLOSED_R>
      final INode n1S0A3 = n1S0.elementAt(3);
      n1S0A3.accept(this, argu);
    }
  }

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
   */
  public void visit(final MultistateSpecies_SingleStateDefinition n, final A argu) {
    // multistateSpecies_SiteName -> MultistateSpecies_SiteName()
    final MultistateSpecies_SiteName n0 = n.multistateSpecies_SiteName;
    n0.accept(this, argu);
    // nodeToken -> <OPEN_C>
    final NodeToken n1 = n.nodeToken;
    n1.accept(this, argu);
    // multistateSpecies_SiteSingleElement -> MultistateSpecies_SiteSingleElement()
    final MultistateSpecies_SiteSingleElement n2 = n.multistateSpecies_SiteSingleElement;
    n2.accept(this, argu);
    // nodeListOptional -> ( #0 <SITE_STATES_SEPARATOR> #1 MultistateSpecies_SiteSingleElement() )*
    final NodeListOptional n3 = n.nodeListOptional;
    if (n3.present()) {
      for (int i = 0; i < n3.size(); i++) {
        final INode n3Mi = n3.elementAt(i);
        final NodeSequence n3MiS0 = (NodeSequence) n3Mi;
        // #0 <SITE_STATES_SEPARATOR>
        final INode n3MiS0A0 = n3MiS0.elementAt(0);
        n3MiS0A0.accept(this, argu);
        // #1 MultistateSpecies_SiteSingleElement()
        final INode n3MiS0A1 = n3MiS0.elementAt(1);
        n3MiS0A1.accept(this, argu);
      }
    }
    // nodeToken1 -> <CLOSED_C>
    final NodeToken n4 = n.nodeToken1;
    n4.accept(this, argu);
    // nodeOptional -> ( <CIRCULAR_FLAG> )?
    final NodeOptional n5 = n.nodeOptional;
    if (n5.present()) {
      n5.accept(this, argu);
    }
  }

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
   */
  public void visit(final MultistateSpecies_Name n, final A argu) {
    // nodeChoice -> . %0 <STRING_LITERAL>
    // .......... .. | %1 ( &0 <MULTI_IDENTIFIER>
    // .......... .. . .. | &1 <CIRCULAR_FLAG>
    // .......... .. . .. | &2 <CLOSED_R>
    // .......... .. . .. | &3 <SITE_NAMES_SEPARATOR>
    // .......... .. . .. | &4 <RANGE_SEPARATOR>
    // .......... .. . .. | &5 <SITE_STATES_SEPARATOR>
    // .......... .. . .. | &6 <CLOSED_C> )+
    final NodeChoice n0C = n.nodeChoice;
    final INode n0CH = n0C.choice;
    switch (n0C.which) {
      case 0:
        // %0 <STRING_LITERAL>
        n0CH.accept(this, argu);
        break;
      case 1:
        // %1 ( &0 <MULTI_IDENTIFIER>
        // .. | &1 <CIRCULAR_FLAG>
        // .. | &2 <CLOSED_R>
        // .. | &3 <SITE_NAMES_SEPARATOR>
        // .. | &4 <RANGE_SEPARATOR>
        // .. | &5 <SITE_STATES_SEPARATOR>
        // .. | &6 <CLOSED_C> )+
        final NodeList n0CHL = (NodeList) n0CH;
        for (int i = 0; i < n0CHL.size(); i++) {
          final INode n0CHLEi = n0CHL.elementAt(i);
          final NodeChoice n0CHLEiC = (NodeChoice) n0CHLEi;
          final INode n0CHLEiCH = n0CHLEiC.choice;
          switch (n0CHLEiC.which) {
            case 0:
              // &0 <MULTI_IDENTIFIER>
              n0CHLEiCH.accept(this, argu);
              break;
            case 1:
              // &1 <CIRCULAR_FLAG>
              n0CHLEiCH.accept(this, argu);
              break;
            case 2:
              // &2 <CLOSED_R>
              n0CHLEiCH.accept(this, argu);
              break;
            case 3:
              // &3 <SITE_NAMES_SEPARATOR>
              n0CHLEiCH.accept(this, argu);
              break;
            case 4:
              // &4 <RANGE_SEPARATOR>
              n0CHLEiCH.accept(this, argu);
              break;
            case 5:
              // &5 <SITE_STATES_SEPARATOR>
              n0CHLEiCH.accept(this, argu);
              break;
            case 6:
              // &6 <CLOSED_C>
              n0CHLEiCH.accept(this, argu);
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
   */
  public void visit(final MultistateSpecies_SiteSingleElement n, final A argu) {
    // nodeChoice -> ( %0 <STRING_LITERAL>
    // .......... .. | %1 ( &0 <CIRCULAR_FLAG>
    // .......... .. . .. | &1 <MULTI_IDENTIFIER>
    // .......... .. . .. | &2 <NUMBER>
    // .......... .. . .. | &3 <OPEN_R> )+ )
    final NodeChoice n0 = n.nodeChoice;
    final NodeChoice n0C = n0;
    final INode n0CH = n0C.choice;
    switch (n0C.which) {
      case 0:
        // %0 <STRING_LITERAL>
        n0CH.accept(this, argu);
        break;
      case 1:
        // %1 ( &0 <CIRCULAR_FLAG>
        // .. | &1 <MULTI_IDENTIFIER>
        // .. | &2 <NUMBER>
        // .. | &3 <OPEN_R> )+
        final NodeList n0CHL = (NodeList) n0CH;
        for (int i = 0; i < n0CHL.size(); i++) {
          final INode n0CHLEi = n0CHL.elementAt(i);
          final NodeChoice n0CHLEiC = (NodeChoice) n0CHLEi;
          final INode n0CHLEiCH = n0CHLEiC.choice;
          switch (n0CHLEiC.which) {
            case 0:
              // &0 <CIRCULAR_FLAG>
              n0CHLEiCH.accept(this, argu);
              break;
            case 1:
              // &1 <MULTI_IDENTIFIER>
              n0CHLEiCH.accept(this, argu);
              break;
            case 2:
              // &2 <NUMBER>
              n0CHLEiCH.accept(this, argu);
              break;
            case 3:
              // &3 <OPEN_R>
              n0CHLEiCH.accept(this, argu);
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
    // nodeOptional -> ( #0 ( " " )*
    // ............ .. . #1 <RANGE_SEPARATOR>
    // ............ .. . #2 ( " " )*
    // ............ .. . #3 ( %0 <STRING_LITERAL>
    // ............ .. . .. | %1 ( &0 <MULTI_IDENTIFIER>
    // ............ .. . .. . .. | &1 <NUMBER>
    // ............ .. . .. . .. | &2 <OPEN_R> )+ ) )?
    final NodeOptional n1 = n.nodeOptional;
    if (n1.present()) {
      final NodeSequence n1S0 = (NodeSequence) n1.node;
      // #0 ( " " )*
      final INode n1S0A0 = n1S0.elementAt(0);
      final NodeListOptional n1S0A0T = (NodeListOptional) n1S0A0;
      if (n1S0A0T.present()) {
        for (int i = 0; i < n1S0A0T.size(); i++) {
          final INode n1S0A0TMi = n1S0A0T.elementAt(i);
          n1S0A0TMi.accept(this, argu);
        }
      }
      // #1 <RANGE_SEPARATOR>
      final INode n1S0A1 = n1S0.elementAt(1);
      n1S0A1.accept(this, argu);
      // #2 ( " " )*
      final INode n1S0A2 = n1S0.elementAt(2);
      final NodeListOptional n1S0A2T1 = (NodeListOptional) n1S0A2;
      if (n1S0A2T1.present()) {
        for (int i = 0; i < n1S0A2T1.size(); i++) {
          final INode n1S0A2T1Mi = n1S0A2T1.elementAt(i);
          n1S0A2T1Mi.accept(this, argu);
        }
      }
      // #3 ( %0 <STRING_LITERAL>
      // .. | %1 ( &0 <MULTI_IDENTIFIER>
      // .. .. | &1 <NUMBER>
      // .. .. | &2 <OPEN_R> )+ )
      final INode n1S0A3 = n1S0.elementAt(3);
      final NodeChoice n1S0A3C = (NodeChoice) n1S0A3;
      final INode n1S0A3CH = n1S0A3C.choice;
      switch (n1S0A3C.which) {
        case 0:
          // %0 <STRING_LITERAL>
          n1S0A3CH.accept(this, argu);
          break;
        case 1:
          // %1 ( &0 <MULTI_IDENTIFIER>
          // .. | &1 <NUMBER>
          // .. | &2 <OPEN_R> )+
          final NodeList n1S0A3CHL1 = (NodeList) n1S0A3CH;
          for (int i = 0; i < n1S0A3CHL1.size(); i++) {
            final INode n1S0A3CHL1Ei = n1S0A3CHL1.elementAt(i);
            final NodeChoice n1S0A3CHL1EiC = (NodeChoice) n1S0A3CHL1Ei;
            final INode n1S0A3CHL1EiCH = n1S0A3CHL1EiC.choice;
            switch (n1S0A3CHL1EiC.which) {
              case 0:
                // &0 <MULTI_IDENTIFIER>
                n1S0A3CHL1EiCH.accept(this, argu);
                break;
              case 1:
                // &1 <NUMBER>
                n1S0A3CHL1EiCH.accept(this, argu);
                break;
              case 2:
                // &2 <OPEN_R>
                n1S0A3CHL1EiCH.accept(this, argu);
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
  }

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
   */
  public void visit(final MultistateSpecies_SiteSingleElement_Range n, final A argu) {
    // nodeToken -> <NUMBER>
    final NodeToken n0 = n.nodeToken;
    n0.accept(this, argu);
    // nodeListOptional -> ( " " )*
    final NodeListOptional n1 = n.nodeListOptional;
    if (n1.present()) {
      for (int i = 0; i < n1.size(); i++) {
        final INode n1Mi = n1.elementAt(i);
        n1Mi.accept(this, argu);
      }
    }
    // nodeToken1 -> <RANGE_SEPARATOR>
    final NodeToken n2 = n.nodeToken1;
    n2.accept(this, argu);
    // nodeListOptional1 -> ( " " )*
    final NodeListOptional n3 = n.nodeListOptional1;
    if (n3.present()) {
      for (int i = 0; i < n3.size(); i++) {
        final INode n3Mi = n3.elementAt(i);
        n3Mi.accept(this, argu);
      }
    }
    // nodeToken2 -> <NUMBER>
    final NodeToken n4 = n.nodeToken2;
    n4.accept(this, argu);
  }

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
   */
  public void visit(final MultistateSpecies_SiteName n, final A argu) {
    // nodeChoice -> . %0 <STRING_LITERAL>
    // .......... .. | %1 ( &0 <MULTI_IDENTIFIER>
    // .......... .. . .. | &1 <CLOSED_C>
    // .......... .. . .. | &2 <OPEN_R>
    // .......... .. . .. | &3 <CLOSED_R>
    // .......... .. . .. | &4 <SITE_STATES_SEPARATOR> )+
    final NodeChoice n0C = n.nodeChoice;
    final INode n0CH = n0C.choice;
    switch (n0C.which) {
      case 0:
        // %0 <STRING_LITERAL>
        n0CH.accept(this, argu);
        break;
      case 1:
        // %1 ( &0 <MULTI_IDENTIFIER>
        // .. | &1 <CLOSED_C>
        // .. | &2 <OPEN_R>
        // .. | &3 <CLOSED_R>
        // .. | &4 <SITE_STATES_SEPARATOR> )+
        final NodeList n0CHL = (NodeList) n0CH;
        for (int i = 0; i < n0CHL.size(); i++) {
          final INode n0CHLEi = n0CHL.elementAt(i);
          final NodeChoice n0CHLEiC = (NodeChoice) n0CHLEi;
          final INode n0CHLEiCH = n0CHLEiC.choice;
          switch (n0CHLEiC.which) {
            case 0:
              // &0 <MULTI_IDENTIFIER>
              n0CHLEiCH.accept(this, argu);
              break;
            case 1:
              // &1 <CLOSED_C>
              n0CHLEiCH.accept(this, argu);
              break;
            case 2:
              // &2 <OPEN_R>
              n0CHLEiCH.accept(this, argu);
              break;
            case 3:
              // &3 <CLOSED_R>
              n0CHLEiCH.accept(this, argu);
              break;
            case 4:
              // &4 <SITE_STATES_SEPARATOR>
              n0CHLEiCH.accept(this, argu);
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
   */
  public void visit(final MultistateSpecies_Operator n, final A argu) {
    // multistateSpecies_Name -> MultistateSpecies_Name()
    final MultistateSpecies_Name n0 = n.multistateSpecies_Name;
    n0.accept(this, argu);
    // nodeOptional -> ( #0 <OPEN_R> #1 MultistateSpecies_Operator_SingleSite()
    // ............ .. . #2 ( $0 <SITE_NAMES_SEPARATOR> $1 MultistateSpecies_Operator_SingleSite() )*
    // ............ .. . #3 <CLOSED_R> )?
    final NodeOptional n1 = n.nodeOptional;
    if (n1.present()) {
      final NodeSequence n1S0 = (NodeSequence) n1.node;
      // #0 <OPEN_R>
      final INode n1S0A0 = n1S0.elementAt(0);
      n1S0A0.accept(this, argu);
      // #1 MultistateSpecies_Operator_SingleSite()
      final INode n1S0A1 = n1S0.elementAt(1);
      n1S0A1.accept(this, argu);
      // #2 ( $0 <SITE_NAMES_SEPARATOR> $1 MultistateSpecies_Operator_SingleSite() )*
      final INode n1S0A2 = n1S0.elementAt(2);
      final NodeListOptional n1S0A2T = (NodeListOptional) n1S0A2;
      if (n1S0A2T.present()) {
        for (int i = 0; i < n1S0A2T.size(); i++) {
          final INode n1S0A2TMi = n1S0A2T.elementAt(i);
          final NodeSequence n1S0A2TMiS1 = (NodeSequence) n1S0A2TMi;
          // $0 <SITE_NAMES_SEPARATOR>
          final INode n1S0A2TMiS1A0 = n1S0A2TMiS1.elementAt(0);
          n1S0A2TMiS1A0.accept(this, argu);
          // $1 MultistateSpecies_Operator_SingleSite()
          final INode n1S0A2TMiS1A1 = n1S0A2TMiS1.elementAt(1);
          n1S0A2TMiS1A1.accept(this, argu);
        }
      }
      // #3 <CLOSED_R>
      final INode n1S0A3 = n1S0.elementAt(3);
      n1S0A3.accept(this, argu);
    }
  }

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
   */
  public void visit(final MultistateSpecies_Operator_SingleSite n, final A argu) {
    // nodeChoice -> . %0 #0 ( &0 <SUCC>
    // .......... .. . .. .. | &1 <PREC> )
    // .......... .. . .. #1 <OPEN_R> #2 MultistateSpecies_Operator_SiteName() #3 <CLOSED_R>
    // .......... .. | %1 #0 MultistateSpecies_Operator_SiteName()
    // .......... .. . .. #1 ( $0 <OPEN_C> $1 MultistateSpecies_Operator_SiteSingleState() $2 <CLOSED_C> )?
    final NodeChoice n0C = n.nodeChoice;
    final INode n0CH = n0C.choice;
    switch (n0C.which) {
      case 0:
        // %0 #0 ( &0 <SUCC>
        // .. .. | &1 <PREC> )
        // .. #1 <OPEN_R> #2 MultistateSpecies_Operator_SiteName() #3 <CLOSED_R>
        final NodeSequence n0CHS0 = (NodeSequence) n0CH;
        // #0 ( &0 <SUCC>
        // .. | &1 <PREC> )
        final INode n0CHS00A0 = n0CHS0.elementAt(0);
        final NodeChoice n0CHS00A0C = (NodeChoice) n0CHS00A0;
        final INode n0CHS00A0CH = n0CHS00A0C.choice;
        switch (n0CHS00A0C.which) {
          case 0:
            // &0 <SUCC>
            n0CHS00A0CH.accept(this, argu);
            break;
          case 1:
            // &1 <PREC>
            n0CHS00A0CH.accept(this, argu);
            break;
          default:
            // should not occur !!!
            break;
        }
        // #1 <OPEN_R>
        final INode n0CHS01A1 = n0CHS0.elementAt(1);
        n0CHS01A1.accept(this, argu);
        // #2 MultistateSpecies_Operator_SiteName()
        final INode n0CHS01A2 = n0CHS0.elementAt(2);
        n0CHS01A2.accept(this, argu);
        // #3 <CLOSED_R>
        final INode n0CHS01A3 = n0CHS0.elementAt(3);
        n0CHS01A3.accept(this, argu);
        break;
      case 1:
        // %1 #0 MultistateSpecies_Operator_SiteName()
        // .. #1 ( $0 <OPEN_C> $1 MultistateSpecies_Operator_SiteSingleState() $2 <CLOSED_C> )?
        final NodeSequence n0CHS1 = (NodeSequence) n0CH;
        // #0 MultistateSpecies_Operator_SiteName()
        final INode n0CHS11A0 = n0CHS1.elementAt(0);
        n0CHS11A0.accept(this, argu);
        // #1 ( $0 <OPEN_C> $1 MultistateSpecies_Operator_SiteSingleState() $2 <CLOSED_C> )?
        final INode n0CHS11A1 = n0CHS1.elementAt(1);
        final NodeOptional n0CHS11A1P = (NodeOptional) n0CHS11A1;
        if (n0CHS11A1P.present()) {
          final NodeSequence n0CHS11A1PS2 = (NodeSequence) n0CHS11A1P.node;
          // $0 <OPEN_C>
          final INode n0CHS11A1PS2A0 = n0CHS11A1PS2.elementAt(0);
          n0CHS11A1PS2A0.accept(this, argu);
          // $1 MultistateSpecies_Operator_SiteSingleState()
          final INode n0CHS11A1PS2A1 = n0CHS11A1PS2.elementAt(1);
          n0CHS11A1PS2A1.accept(this, argu);
          // $2 <CLOSED_C>
          final INode n0CHS11A1PS2A2 = n0CHS11A1PS2.elementAt(2);
          n0CHS11A1PS2A2.accept(this, argu);
        }
        break;
      default:
        // should not occur !!!
        break;
    }
  }

  /**
   * Visits a {@link MultistateSpecies_Operator_SiteName} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 <STRING_LITERAL><br>
   * .......... .. | %1 ( &0 <MULTI_IDENTIFIER><br>
   * .......... .. . .. | &1 <NUMBER> )+<br>
   *
   * @param n - the node to visit
   * @param argu - the user argument
   */
  public void visit(final MultistateSpecies_Operator_SiteName n, final A argu) {
    // nodeChoice -> . %0 <STRING_LITERAL>
    // .......... .. | %1 ( &0 <MULTI_IDENTIFIER>
    // .......... .. . .. | &1 <NUMBER> )+
    final NodeChoice n0C = n.nodeChoice;
    final INode n0CH = n0C.choice;
    switch (n0C.which) {
      case 0:
        // %0 <STRING_LITERAL>
        n0CH.accept(this, argu);
        break;
      case 1:
        // %1 ( &0 <MULTI_IDENTIFIER>
        // .. | &1 <NUMBER> )+
        final NodeList n0CHL = (NodeList) n0CH;
        for (int i = 0; i < n0CHL.size(); i++) {
          final INode n0CHLEi = n0CHL.elementAt(i);
          final NodeChoice n0CHLEiC = (NodeChoice) n0CHLEi;
          final INode n0CHLEiCH = n0CHLEiC.choice;
          switch (n0CHLEiC.which) {
            case 0:
              // &0 <MULTI_IDENTIFIER>
              n0CHLEiCH.accept(this, argu);
              break;
            case 1:
              // &1 <NUMBER>
              n0CHLEiCH.accept(this, argu);
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

  /**
   * Visits a {@link MultistateSpecies_Operator_SiteSingleState} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 <STRING_LITERAL><br>
   * .......... .. | %1 ( &0 <MULTI_IDENTIFIER><br>
   * .......... .. . .. | &1 <NUMBER> )+<br>
   *
   * @param n - the node to visit
   * @param argu - the user argument
   */
  public void visit(final MultistateSpecies_Operator_SiteSingleState n, final A argu) {
    // nodeChoice -> . %0 <STRING_LITERAL>
    // .......... .. | %1 ( &0 <MULTI_IDENTIFIER>
    // .......... .. . .. | &1 <NUMBER> )+
    final NodeChoice n0C = n.nodeChoice;
    final INode n0CH = n0C.choice;
    switch (n0C.which) {
      case 0:
        // %0 <STRING_LITERAL>
        n0CH.accept(this, argu);
        break;
      case 1:
        // %1 ( &0 <MULTI_IDENTIFIER>
        // .. | &1 <NUMBER> )+
        final NodeList n0CHL = (NodeList) n0CH;
        for (int i = 0; i < n0CHL.size(); i++) {
          final INode n0CHLEi = n0CHL.elementAt(i);
          final NodeChoice n0CHLEiC = (NodeChoice) n0CHLEi;
          final INode n0CHLEiCH = n0CHLEiC.choice;
          switch (n0CHLEiC.which) {
            case 0:
              // &0 <MULTI_IDENTIFIER>
              n0CHLEiCH.accept(this, argu);
              break;
            case 1:
              // &1 <NUMBER>
              n0CHLEiCH.accept(this, argu);
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

}
