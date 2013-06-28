/* Generated by JTB 1.4.7 */
package msmb.parsers.multistateSpecies.visitor;

import msmb.parsers.multistateSpecies.syntaxtree.*;
import java.util.*;

/**
 * Provides default methods which visit each node in the tree in depth-first order.<br>
 * In your "Ret" visitors extend this class and override part or all of these methods.
 *
 * @param <R> - The user return information type
 */
public class DepthFirstRetVisitor<R> implements IRetVisitor<R> {


  /*
   * Base nodes classes visit methods (to be overridden if necessary)
   */

  /**
   * Visits a {@link NodeChoice} node.
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final NodeChoice n) {
    /* You have to adapt which data is returned (result variables below are just examples) */
    final R nRes = n.choice.accept(this);
    return nRes;
  }

  /**
   * Visits a {@link NodeList} node.
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final NodeList n) {
    /* You have to adapt which data is returned (result variables below are just examples) */
    R nRes = null;
    for (final Iterator<INode> e = n.elements(); e.hasNext();) {
      @SuppressWarnings("unused")
      final R sRes = e.next().accept(this);
    }
    return nRes;
  }

  /**
   * Visits a {@link NodeListOptional} node.
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final NodeListOptional n) {
    /* You have to adapt which data is returned (result variables below are just examples) */
    if (n.present()) {
      R nRes = null;
      for (final Iterator<INode> e = n.elements(); e.hasNext();) {
        @SuppressWarnings("unused")
        R sRes = e.next().accept(this);
        }
      return nRes;
    } else
      return null;
  }

  /**
   * Visits a {@link NodeOptional} node.
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final NodeOptional n) {
    /* You have to adapt which data is returned (result variables below are just examples) */
    if (n.present()) {
      final R nRes = n.node.accept(this);
      return nRes;
    } else
      return null;
  }

  /**
   * Visits a {@link NodeSequence} node.
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final NodeSequence n) {
    /* You have to adapt which data is returned (result variables below are just examples) */
    R nRes = null;
    for (final Iterator<INode> e = n.elements(); e.hasNext();) {
      @SuppressWarnings("unused")
      R subRet = e.next().accept(this);
    }
    return nRes;
  }

  /**
   * Visits a {@link NodeTCF} node.
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final NodeTCF n) {
    /* You have to adapt which data is returned (result variables below are just examples) */
    R nRes = null;
    @SuppressWarnings("unused")
    final String tkIm = n.tokenImage;
    return nRes;
  }

  /**
   * Visits a {@link NodeToken} node.
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final NodeToken n) {
    /* You have to adapt which data is returned (result variables below are just examples) */
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
   * nodeToken -> <EOF><br>
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final CompleteMultistateSpecies n) {
    R nRes = null;
    // multistateSpecies -> MultistateSpecies()
    final MultistateSpecies n0 = n.multistateSpecies;
    nRes = n0.accept(this);
    // nodeListOptional -> ( PossibleExtensions() )*
    final NodeListOptional n1 = n.nodeListOptional;
    if (n1.present()) {
      for (int i = 0; i < n1.size(); i++) {
        final INode n1Mi = n1.elementAt(i);
        nRes = n1Mi.accept(this);
      }
    }
    // nodeToken -> <EOF>
    final NodeToken n2 = n.nodeToken;
    nRes = n2.accept(this);
    return nRes;
  }

  /**
   * Visits a {@link CompleteMultistateSpecies_Operator} node, whose children are the following :
   * <p>
   * multistateSpecies_Operator -> MultistateSpecies_Operator()<br>
   * nodeListOptional -> ( PossibleExtensions() )*<br>
   * nodeToken -> <EOF><br>
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final CompleteMultistateSpecies_Operator n) {
    R nRes = null;
    // multistateSpecies_Operator -> MultistateSpecies_Operator()
    final MultistateSpecies_Operator n0 = n.multistateSpecies_Operator;
    nRes = n0.accept(this);
    // nodeListOptional -> ( PossibleExtensions() )*
    final NodeListOptional n1 = n.nodeListOptional;
    if (n1.present()) {
      for (int i = 0; i < n1.size(); i++) {
        final INode n1Mi = n1.elementAt(i);
        nRes = n1Mi.accept(this);
      }
    }
    // nodeToken -> <EOF>
    final NodeToken n2 = n.nodeToken;
    nRes = n2.accept(this);
    return nRes;
  }

  /**
   * Visits a {@link CompleteMultistateSpecies_Range} node, whose children are the following :
   * <p>
   * multistateSpecies_SiteSingleElement_Range -> MultistateSpecies_SiteSingleElement_Range()<br>
   * nodeToken -> <EOF><br>
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final CompleteMultistateSpecies_Range n) {
    R nRes = null;
    // multistateSpecies_SiteSingleElement_Range -> MultistateSpecies_SiteSingleElement_Range()
    final MultistateSpecies_SiteSingleElement_Range n0 = n.multistateSpecies_SiteSingleElement_Range;
    nRes = n0.accept(this);
    // nodeToken -> <EOF>
    final NodeToken n1 = n.nodeToken;
    nRes = n1.accept(this);
    return nRes;
  }

  /**
   * Visits a {@link CompleteMultistateSpecies_RangeString} node, whose children are the following :
   * <p>
   * multistateSpecies_SiteSingleElement -> MultistateSpecies_SiteSingleElement()<br>
   * nodeToken -> <EOF><br>
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final CompleteMultistateSpecies_RangeString n) {
    R nRes = null;
    // multistateSpecies_SiteSingleElement -> MultistateSpecies_SiteSingleElement()
    final MultistateSpecies_SiteSingleElement n0 = n.multistateSpecies_SiteSingleElement;
    nRes = n0.accept(this);
    // nodeToken -> <EOF>
    final NodeToken n1 = n.nodeToken;
    nRes = n1.accept(this);
    return nRes;
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
   * @return the user return information
   */
  public R visit(final PossibleExtensions n) {
    R nRes = null;
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
        nRes = n0CH.accept(this);
        break;
      case 1:
        // %01 <EXTENSION_COMPARTMENT>
        nRes = n0CH.accept(this);
        break;
      case 2:
        // %02 <EXTENSION_PARTICLE>
        nRes = n0CH.accept(this);
        break;
      case 3:
        // %03 <EXTENSION_TRANS>
        nRes = n0CH.accept(this);
        break;
      case 4:
        // %04 <EXTENSION_INIT>
        nRes = n0CH.accept(this);
        break;
      case 5:
        // %05 <EXTENSION_RATE>
        nRes = n0CH.accept(this);
        break;
      case 6:
        // %06 <EXTENSION_SPECIES>
        nRes = n0CH.accept(this);
        break;
      case 7:
        // %07 <EXTENSION_GLOBALQ>
        nRes = n0CH.accept(this);
        break;
      case 8:
        // %08 <EXTENSION_FUNCTION>
        nRes = n0CH.accept(this);
        break;
      case 9:
        // %09 <EXTENSION_REACTION>
        nRes = n0CH.accept(this);
        break;
      case 10:
        // %10 <EXTENSION_FLUX>
        nRes = n0CH.accept(this);
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
   * nodeOptional -> ( #0 <OPEN_R> #1 MultistateSpecies_SingleStateDefinition()<br>
   * ............ .. . #2 ( $0 <SITE_NAMES_SEPARATOR> $1 MultistateSpecies_SingleStateDefinition() )*<br>
   * ............ .. . #3 <CLOSED_R> )?<br>
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final MultistateSpecies n) {
    R nRes = null;
    // multistateSpecies_Name -> MultistateSpecies_Name()
    final MultistateSpecies_Name n0 = n.multistateSpecies_Name;
    nRes = n0.accept(this);
    // nodeOptional -> ( #0 <OPEN_R> #1 MultistateSpecies_SingleStateDefinition()
    // ............ .. . #2 ( $0 <SITE_NAMES_SEPARATOR> $1 MultistateSpecies_SingleStateDefinition() )*
    // ............ .. . #3 <CLOSED_R> )?
    final NodeOptional n1 = n.nodeOptional;
    if (n1.present()) {
      final NodeSequence n1S0 = (NodeSequence) n1.node;
      // #0 <OPEN_R>
      final INode n1S0A0 = n1S0.elementAt(0);
      nRes = n1S0A0.accept(this);
      // #1 MultistateSpecies_SingleStateDefinition()
      final INode n1S0A1 = n1S0.elementAt(1);
      nRes = n1S0A1.accept(this);
      // #2 ( $0 <SITE_NAMES_SEPARATOR> $1 MultistateSpecies_SingleStateDefinition() )*
      final INode n1S0A2 = n1S0.elementAt(2);
      final NodeListOptional n1S0A2T = (NodeListOptional) n1S0A2;
      if (n1S0A2T.present()) {
        for (int i = 0; i < n1S0A2T.size(); i++) {
          final INode n1S0A2TMi = n1S0A2T.elementAt(i);
          final NodeSequence n1S0A2TMiS1 = (NodeSequence) n1S0A2TMi;
          // $0 <SITE_NAMES_SEPARATOR>
          final INode n1S0A2TMiS1A0 = n1S0A2TMiS1.elementAt(0);
          nRes = n1S0A2TMiS1A0.accept(this);
          // $1 MultistateSpecies_SingleStateDefinition()
          final INode n1S0A2TMiS1A1 = n1S0A2TMiS1.elementAt(1);
          nRes = n1S0A2TMiS1A1.accept(this);
        }
      }
      // #3 <CLOSED_R>
      final INode n1S0A3 = n1S0.elementAt(3);
      nRes = n1S0A3.accept(this);
    }
    return nRes;
  }

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
   * @return the user return information
   */
  public R visit(final MultistateSpecies_SingleStateDefinition n) {
    R nRes = null;
    // multistateSpecies_SiteName -> MultistateSpecies_SiteName()
    final MultistateSpecies_SiteName n0 = n.multistateSpecies_SiteName;
    nRes = n0.accept(this);
    // nodeOptional -> ( #0 <OPEN_C> #1 MultistateSpecies_SiteSingleElement()
    // ............ .. . #2 ( $0 <SITE_STATES_SEPARATOR> $1 MultistateSpecies_SiteSingleElement() )*
    // ............ .. . #3 <CLOSED_C>
    // ............ .. . #4 ( <CIRCULAR_FLAG> )? )?
    final NodeOptional n1 = n.nodeOptional;
    if (n1.present()) {
      final NodeSequence n1S0 = (NodeSequence) n1.node;
      // #0 <OPEN_C>
      final INode n1S0A0 = n1S0.elementAt(0);
      nRes = n1S0A0.accept(this);
      // #1 MultistateSpecies_SiteSingleElement()
      final INode n1S0A1 = n1S0.elementAt(1);
      nRes = n1S0A1.accept(this);
      // #2 ( $0 <SITE_STATES_SEPARATOR> $1 MultistateSpecies_SiteSingleElement() )*
      final INode n1S0A2 = n1S0.elementAt(2);
      final NodeListOptional n1S0A2T = (NodeListOptional) n1S0A2;
      if (n1S0A2T.present()) {
        for (int i = 0; i < n1S0A2T.size(); i++) {
          final INode n1S0A2TMi = n1S0A2T.elementAt(i);
          final NodeSequence n1S0A2TMiS1 = (NodeSequence) n1S0A2TMi;
          // $0 <SITE_STATES_SEPARATOR>
          final INode n1S0A2TMiS1A0 = n1S0A2TMiS1.elementAt(0);
          nRes = n1S0A2TMiS1A0.accept(this);
          // $1 MultistateSpecies_SiteSingleElement()
          final INode n1S0A2TMiS1A1 = n1S0A2TMiS1.elementAt(1);
          nRes = n1S0A2TMiS1A1.accept(this);
        }
      }
      // #3 <CLOSED_C>
      final INode n1S0A3 = n1S0.elementAt(3);
      nRes = n1S0A3.accept(this);
      // #4 ( <CIRCULAR_FLAG> )?
      final INode n1S0A4 = n1S0.elementAt(4);
      final NodeOptional n1S0A4P = (NodeOptional) n1S0A4;
      if (n1S0A4P.present()) {
        nRes = n1S0A4P.accept(this);
      }
    }
    return nRes;
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
   * @return the user return information
   */
  public R visit(final MultistateSpecies_Name n) {
    R nRes = null;
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
        nRes = n0CH.accept(this);
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
              nRes = n0CHLEiCH.accept(this);
              break;
            case 1:
              // &1 <CIRCULAR_FLAG>
              nRes = n0CHLEiCH.accept(this);
              break;
            case 2:
              // &2 <CLOSED_R>
              nRes = n0CHLEiCH.accept(this);
              break;
            case 3:
              // &3 <SITE_NAMES_SEPARATOR>
              nRes = n0CHLEiCH.accept(this);
              break;
            case 4:
              // &4 <RANGE_SEPARATOR>
              nRes = n0CHLEiCH.accept(this);
              break;
            case 5:
              // &5 <SITE_STATES_SEPARATOR>
              nRes = n0CHLEiCH.accept(this);
              break;
            case 6:
              // &6 <CLOSED_C>
              nRes = n0CHLEiCH.accept(this);
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
   * @return the user return information
   */
  public R visit(final MultistateSpecies_SiteSingleElement n) {
    R nRes = null;
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
        nRes = n0CH.accept(this);
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
              nRes = n0CHLEiCH.accept(this);
              break;
            case 1:
              // &1 <MULTI_IDENTIFIER>
              nRes = n0CHLEiCH.accept(this);
              break;
            case 2:
              // &2 <NUMBER>
              nRes = n0CHLEiCH.accept(this);
              break;
            case 3:
              // &3 <OPEN_R>
              nRes = n0CHLEiCH.accept(this);
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
          nRes = n1S0A0TMi.accept(this);
        }
      }
      // #1 <RANGE_SEPARATOR>
      final INode n1S0A1 = n1S0.elementAt(1);
      nRes = n1S0A1.accept(this);
      // #2 ( " " )*
      final INode n1S0A2 = n1S0.elementAt(2);
      final NodeListOptional n1S0A2T1 = (NodeListOptional) n1S0A2;
      if (n1S0A2T1.present()) {
        for (int i = 0; i < n1S0A2T1.size(); i++) {
          final INode n1S0A2T1Mi = n1S0A2T1.elementAt(i);
          nRes = n1S0A2T1Mi.accept(this);
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
          nRes = n1S0A3CH.accept(this);
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
                nRes = n1S0A3CHL1EiCH.accept(this);
                break;
              case 1:
                // &1 <NUMBER>
                nRes = n1S0A3CHL1EiCH.accept(this);
                break;
              case 2:
                // &2 <OPEN_R>
                nRes = n1S0A3CHL1EiCH.accept(this);
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
   * nodeToken -> <NUMBER><br>
   * nodeListOptional -> ( " " )*<br>
   * nodeToken1 -> <RANGE_SEPARATOR><br>
   * nodeListOptional1 -> ( " " )*<br>
   * nodeToken2 -> <NUMBER><br>
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final MultistateSpecies_SiteSingleElement_Range n) {
    R nRes = null;
    // nodeToken -> <NUMBER>
    final NodeToken n0 = n.nodeToken;
    nRes = n0.accept(this);
    // nodeListOptional -> ( " " )*
    final NodeListOptional n1 = n.nodeListOptional;
    if (n1.present()) {
      for (int i = 0; i < n1.size(); i++) {
        final INode n1Mi = n1.elementAt(i);
        nRes = n1Mi.accept(this);
      }
    }
    // nodeToken1 -> <RANGE_SEPARATOR>
    final NodeToken n2 = n.nodeToken1;
    nRes = n2.accept(this);
    // nodeListOptional1 -> ( " " )*
    final NodeListOptional n3 = n.nodeListOptional1;
    if (n3.present()) {
      for (int i = 0; i < n3.size(); i++) {
        final INode n3Mi = n3.elementAt(i);
        nRes = n3Mi.accept(this);
      }
    }
    // nodeToken2 -> <NUMBER>
    final NodeToken n4 = n.nodeToken2;
    nRes = n4.accept(this);
    return nRes;
  }

  /**
   * Visits a {@link MultistateSpecies_SiteName} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 <STRING_LITERAL><br>
   * .......... .. | %1 ( &0 <MULTI_IDENTIFIER><br>
   * .......... .. . .. | &1 <CLOSED_C><br>
   * .......... .. . .. | &2 <OPEN_R> )+<br>
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final MultistateSpecies_SiteName n) {
    R nRes = null;
    // nodeChoice -> . %0 <STRING_LITERAL>
    // .......... .. | %1 ( &0 <MULTI_IDENTIFIER>
    // .......... .. . .. | &1 <CLOSED_C>
    // .......... .. . .. | &2 <OPEN_R> )+
    final NodeChoice n0C = n.nodeChoice;
    final INode n0CH = n0C.choice;
    switch (n0C.which) {
      case 0:
        // %0 <STRING_LITERAL>
        nRes = n0CH.accept(this);
        break;
      case 1:
        // %1 ( &0 <MULTI_IDENTIFIER>
        // .. | &1 <CLOSED_C>
        // .. | &2 <OPEN_R> )+
        final NodeList n0CHL = (NodeList) n0CH;
        for (int i = 0; i < n0CHL.size(); i++) {
          final INode n0CHLEi = n0CHL.elementAt(i);
          final NodeChoice n0CHLEiC = (NodeChoice) n0CHLEi;
          final INode n0CHLEiCH = n0CHLEiC.choice;
          switch (n0CHLEiC.which) {
            case 0:
              // &0 <MULTI_IDENTIFIER>
              nRes = n0CHLEiCH.accept(this);
              break;
            case 1:
              // &1 <CLOSED_C>
              nRes = n0CHLEiCH.accept(this);
              break;
            case 2:
              // &2 <OPEN_R>
              nRes = n0CHLEiCH.accept(this);
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
   * nodeOptional -> ( #0 <OPEN_R> #1 MultistateSpecies_Operator_SingleSite()<br>
   * ............ .. . #2 ( $0 <SITE_NAMES_SEPARATOR> $1 MultistateSpecies_Operator_SingleSite() )*<br>
   * ............ .. . #3 <CLOSED_R> )?<br>
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final MultistateSpecies_Operator n) {
    R nRes = null;
    // multistateSpecies_Name -> MultistateSpecies_Name()
    final MultistateSpecies_Name n0 = n.multistateSpecies_Name;
    nRes = n0.accept(this);
    // nodeOptional -> ( #0 <OPEN_R> #1 MultistateSpecies_Operator_SingleSite()
    // ............ .. . #2 ( $0 <SITE_NAMES_SEPARATOR> $1 MultistateSpecies_Operator_SingleSite() )*
    // ............ .. . #3 <CLOSED_R> )?
    final NodeOptional n1 = n.nodeOptional;
    if (n1.present()) {
      final NodeSequence n1S0 = (NodeSequence) n1.node;
      // #0 <OPEN_R>
      final INode n1S0A0 = n1S0.elementAt(0);
      nRes = n1S0A0.accept(this);
      // #1 MultistateSpecies_Operator_SingleSite()
      final INode n1S0A1 = n1S0.elementAt(1);
      nRes = n1S0A1.accept(this);
      // #2 ( $0 <SITE_NAMES_SEPARATOR> $1 MultistateSpecies_Operator_SingleSite() )*
      final INode n1S0A2 = n1S0.elementAt(2);
      final NodeListOptional n1S0A2T = (NodeListOptional) n1S0A2;
      if (n1S0A2T.present()) {
        for (int i = 0; i < n1S0A2T.size(); i++) {
          final INode n1S0A2TMi = n1S0A2T.elementAt(i);
          final NodeSequence n1S0A2TMiS1 = (NodeSequence) n1S0A2TMi;
          // $0 <SITE_NAMES_SEPARATOR>
          final INode n1S0A2TMiS1A0 = n1S0A2TMiS1.elementAt(0);
          nRes = n1S0A2TMiS1A0.accept(this);
          // $1 MultistateSpecies_Operator_SingleSite()
          final INode n1S0A2TMiS1A1 = n1S0A2TMiS1.elementAt(1);
          nRes = n1S0A2TMiS1A1.accept(this);
        }
      }
      // #3 <CLOSED_R>
      final INode n1S0A3 = n1S0.elementAt(3);
      nRes = n1S0A3.accept(this);
    }
    return nRes;
  }

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
   * @return the user return information
   */
  public R visit(final MultistateSpecies_Operator_SingleSite n) {
    R nRes = null;
    // nodeChoice -> . %0 #0 ( &0 <SUCC>
    // .......... .. . .. .. | &1 <PREC> )
    // .......... .. . .. #1 <OPEN_R> #2 MultistateSpecies_Operator_SiteName() #3 <CLOSED_R>
    // .......... .. | %1 #0 MultistateSpecies_Operator_SiteName()
    // .......... .. . .. #1 ( &0 $0 "=" $1 MultistateSpecies_Operator_SiteTransferSelector()
    // .......... .. . .. .. | &1 ( $0 <OPEN_C> $1 MultistateSpecies_Operator_SiteSingleState() $2 <CLOSED_C> ) )?
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
            nRes = n0CHS00A0CH.accept(this);
            break;
          case 1:
            // &1 <PREC>
            nRes = n0CHS00A0CH.accept(this);
            break;
          default:
            // should not occur !!!
            break;
        }
        // #1 <OPEN_R>
        final INode n0CHS01A1 = n0CHS0.elementAt(1);
        nRes = n0CHS01A1.accept(this);
        // #2 MultistateSpecies_Operator_SiteName()
        final INode n0CHS01A2 = n0CHS0.elementAt(2);
        nRes = n0CHS01A2.accept(this);
        // #3 <CLOSED_R>
        final INode n0CHS01A3 = n0CHS0.elementAt(3);
        nRes = n0CHS01A3.accept(this);
        break;
      case 1:
        // %1 #0 MultistateSpecies_Operator_SiteName()
        // .. #1 ( &0 $0 "=" $1 MultistateSpecies_Operator_SiteTransferSelector()
        // .. .. | &1 ( $0 <OPEN_C> $1 MultistateSpecies_Operator_SiteSingleState() $2 <CLOSED_C> ) )?
        final NodeSequence n0CHS1 = (NodeSequence) n0CH;
        // #0 MultistateSpecies_Operator_SiteName()
        final INode n0CHS11A0 = n0CHS1.elementAt(0);
        nRes = n0CHS11A0.accept(this);
        // #1 ( &0 $0 "=" $1 MultistateSpecies_Operator_SiteTransferSelector()
        // .. | &1 ( $0 <OPEN_C> $1 MultistateSpecies_Operator_SiteSingleState() $2 <CLOSED_C> ) )?
        final INode n0CHS11A1 = n0CHS1.elementAt(1);
        final NodeOptional n0CHS11A1P = (NodeOptional) n0CHS11A1;
        if (n0CHS11A1P.present()) {
          final NodeChoice n0CHS11A1PC = (NodeChoice) n0CHS11A1P.node;
          final INode n0CHS11A1PCH = n0CHS11A1PC.choice;
          switch (n0CHS11A1PC.which) {
            case 0:
              // &0 $0 "=" $1 MultistateSpecies_Operator_SiteTransferSelector()
              final NodeSequence n0CHS11A1PCHS2 = (NodeSequence) n0CHS11A1PCH;
              // $0 "="
              final INode n0CHS11A1PCHS20A0 = n0CHS11A1PCHS2.elementAt(0);
              nRes = n0CHS11A1PCHS20A0.accept(this);
              // $1 MultistateSpecies_Operator_SiteTransferSelector()
              final INode n0CHS11A1PCHS20A1 = n0CHS11A1PCHS2.elementAt(1);
              nRes = n0CHS11A1PCHS20A1.accept(this);
              break;
            case 1:
              // &1 ( $0 <OPEN_C> $1 MultistateSpecies_Operator_SiteSingleState() $2 <CLOSED_C> )
              final NodeSequence n0CHS11A1PCHS3 = (NodeSequence) n0CHS11A1PCH;
              // $0 <OPEN_C>
              final INode n0CHS11A1PCHS3A0 = n0CHS11A1PCHS3.elementAt(0);
              nRes = n0CHS11A1PCHS3A0.accept(this);
              // $1 MultistateSpecies_Operator_SiteSingleState()
              final INode n0CHS11A1PCHS3A1 = n0CHS11A1PCHS3.elementAt(1);
              nRes = n0CHS11A1PCHS3A1.accept(this);
              // $2 <CLOSED_C>
              final INode n0CHS11A1PCHS3A2 = n0CHS11A1PCHS3.elementAt(2);
              nRes = n0CHS11A1PCHS3A2.accept(this);
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
   * Visits a {@link MultistateSpecies_Operator_SiteTransferSelector} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 #0 ( &0 <SUCC><br>
   * .......... .. . .. .. | &1 <PREC> )<br>
   * .......... .. . .. #1 <OPEN_R> #2 MultistateSpecies_Name() #3 "." #4 MultistateSpecies_Operator_SiteName() #5 <CLOSED_R><br>
   * .......... .. | %1 #0 MultistateSpecies_Name() #1 "." #2 MultistateSpecies_Operator_SiteName()<br>
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final MultistateSpecies_Operator_SiteTransferSelector n) {
    R nRes = null;
    // nodeChoice -> . %0 #0 ( &0 <SUCC>
    // .......... .. . .. .. | &1 <PREC> )
    // .......... .. . .. #1 <OPEN_R> #2 MultistateSpecies_Name() #3 "." #4 MultistateSpecies_Operator_SiteName() #5 <CLOSED_R>
    // .......... .. | %1 #0 MultistateSpecies_Name() #1 "." #2 MultistateSpecies_Operator_SiteName()
    final NodeChoice n0C = n.nodeChoice;
    final INode n0CH = n0C.choice;
    switch (n0C.which) {
      case 0:
        // %0 #0 ( &0 <SUCC>
        // .. .. | &1 <PREC> )
        // .. #1 <OPEN_R> #2 MultistateSpecies_Name() #3 "." #4 MultistateSpecies_Operator_SiteName() #5 <CLOSED_R>
        final NodeSequence n0CHS0 = (NodeSequence) n0CH;
        // #0 ( &0 <SUCC>
        // .. | &1 <PREC> )
        final INode n0CHS00A0 = n0CHS0.elementAt(0);
        final NodeChoice n0CHS00A0C = (NodeChoice) n0CHS00A0;
        final INode n0CHS00A0CH = n0CHS00A0C.choice;
        switch (n0CHS00A0C.which) {
          case 0:
            // &0 <SUCC>
            nRes = n0CHS00A0CH.accept(this);
            break;
          case 1:
            // &1 <PREC>
            nRes = n0CHS00A0CH.accept(this);
            break;
          default:
            // should not occur !!!
            break;
        }
        // #1 <OPEN_R>
        final INode n0CHS01A1 = n0CHS0.elementAt(1);
        nRes = n0CHS01A1.accept(this);
        // #2 MultistateSpecies_Name()
        final INode n0CHS01A2 = n0CHS0.elementAt(2);
        nRes = n0CHS01A2.accept(this);
        // #3 "."
        final INode n0CHS01A3 = n0CHS0.elementAt(3);
        nRes = n0CHS01A3.accept(this);
        // #4 MultistateSpecies_Operator_SiteName()
        final INode n0CHS01A4 = n0CHS0.elementAt(4);
        nRes = n0CHS01A4.accept(this);
        // #5 <CLOSED_R>
        final INode n0CHS01A5 = n0CHS0.elementAt(5);
        nRes = n0CHS01A5.accept(this);
        break;
      case 1:
        // %1 #0 MultistateSpecies_Name() #1 "." #2 MultistateSpecies_Operator_SiteName()
        final NodeSequence n0CHS1 = (NodeSequence) n0CH;
        // #0 MultistateSpecies_Name()
        final INode n0CHS11A0 = n0CHS1.elementAt(0);
        nRes = n0CHS11A0.accept(this);
        // #1 "."
        final INode n0CHS11A1 = n0CHS1.elementAt(1);
        nRes = n0CHS11A1.accept(this);
        // #2 MultistateSpecies_Operator_SiteName()
        final INode n0CHS11A2 = n0CHS1.elementAt(2);
        nRes = n0CHS11A2.accept(this);
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
   * nodeChoice -> . %0 <STRING_LITERAL><br>
   * .......... .. | %1 ( &0 <MULTI_IDENTIFIER><br>
   * .......... .. . .. | &1 <NUMBER> )+<br>
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final MultistateSpecies_Operator_SiteName n) {
    R nRes = null;
    // nodeChoice -> . %0 <STRING_LITERAL>
    // .......... .. | %1 ( &0 <MULTI_IDENTIFIER>
    // .......... .. . .. | &1 <NUMBER> )+
    final NodeChoice n0C = n.nodeChoice;
    final INode n0CH = n0C.choice;
    switch (n0C.which) {
      case 0:
        // %0 <STRING_LITERAL>
        nRes = n0CH.accept(this);
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
              nRes = n0CHLEiCH.accept(this);
              break;
            case 1:
              // &1 <NUMBER>
              nRes = n0CHLEiCH.accept(this);
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
   * multistateSpecies_SiteSingleElement -> MultistateSpecies_SiteSingleElement()<br>
   * nodeListOptional -> ( #0 <SITE_STATES_SEPARATOR> #1 MultistateSpecies_SiteSingleElement() )*<br>
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final MultistateSpecies_Operator_SiteSingleState n) {
    R nRes = null;
    // multistateSpecies_SiteSingleElement -> MultistateSpecies_SiteSingleElement()
    final MultistateSpecies_SiteSingleElement n0 = n.multistateSpecies_SiteSingleElement;
    nRes = n0.accept(this);
    // nodeListOptional -> ( #0 <SITE_STATES_SEPARATOR> #1 MultistateSpecies_SiteSingleElement() )*
    final NodeListOptional n1 = n.nodeListOptional;
    if (n1.present()) {
      for (int i = 0; i < n1.size(); i++) {
        final INode n1Mi = n1.elementAt(i);
        final NodeSequence n1MiS0 = (NodeSequence) n1Mi;
        // #0 <SITE_STATES_SEPARATOR>
        final INode n1MiS0A0 = n1MiS0.elementAt(0);
        nRes = n1MiS0A0.accept(this);
        // #1 MultistateSpecies_SiteSingleElement()
        final INode n1MiS0A1 = n1MiS0.elementAt(1);
        nRes = n1MiS0A1.accept(this);
      }
    }
    return nRes;
  }

}
