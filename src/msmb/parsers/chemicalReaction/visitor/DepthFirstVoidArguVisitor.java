/* Generated by JTB 1.4.7 */
package msmb.parsers.chemicalReaction.visitor;

import msmb.parsers.chemicalReaction.syntaxtree.*;
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
   * Visits a {@link CompleteReaction} node, whose children are the following :
   * <p>
   * reaction -> Reaction()<br>
   * nodeToken -> <EOF><br>
   *
   * @param n - the node to visit
   * @param argu - the user argument
   */
  public void visit(final CompleteReaction n, final A argu) {
    // reaction -> Reaction()
    final Reaction n0 = n.reaction;
    n0.accept(this, argu);
    // nodeToken -> <EOF>
    final NodeToken n1 = n.nodeToken;
    n1.accept(this, argu);
  }

  /**
   * Visits a {@link CompleteSpeciesWithCoefficient} node, whose children are the following :
   * <p>
   * speciesWithCoeff -> SpeciesWithCoeff()<br>
   * nodeToken -> <EOF><br>
   *
   * @param n - the node to visit
   * @param argu - the user argument
   */
  public void visit(final CompleteSpeciesWithCoefficient n, final A argu) {
    // speciesWithCoeff -> SpeciesWithCoeff()
    final SpeciesWithCoeff n0 = n.speciesWithCoeff;
    n0.accept(this, argu);
    // nodeToken -> <EOF>
    final NodeToken n1 = n.nodeToken;
    n1.accept(this, argu);
  }

  /**
   * Visits a {@link Reaction} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 #0 ( AdditiveExpression() )?<br>
   * .......... .. . .. #1 ( Blank() )*<br>
   * .......... .. . .. #2 ( $0 <ARROW><br>
   * .......... .. . .. .. . $1 ( ?0 ( " " )<br>
   * .......... .. . .. .. . .. . ?1 ( Blank() )*<br>
   * .......... .. . .. .. . .. . ?2 ( AdditiveExpression() )? )* )<br>
   * .......... .. . .. #3 ( $0 ( Blank() )*<br>
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
   */
  public void visit(final Reaction n, final A argu) {
    // nodeChoice -> . %0 #0 ( AdditiveExpression() )?
    // .......... .. . .. #1 ( Blank() )*
    // .......... .. . .. #2 ( $0 <ARROW>
    // .......... .. . .. .. . $1 ( ?0 ( " " )
    // .......... .. . .. .. . .. . ?1 ( Blank() )*
    // .......... .. . .. .. . .. . ?2 ( AdditiveExpression() )? )* )
    // .......... .. . .. #3 ( $0 ( Blank() )*
    // .......... .. . .. .. . $1 ";"
    // .......... .. . .. .. . $2 ( Blank() )*
    // .......... .. . .. .. . $3 ListModifiers() )?
    // .......... .. | %1 #0 <ARROW2>
    // .......... .. . .. #1 ( Blank() )*
    // .......... .. . .. #2 ( AdditiveExpression() )?
    // .......... .. . .. #3 ( $0 ( Blank() )*
    // .......... .. . .. .. . $1 ";"
    // .......... .. . .. .. . $2 ( Blank() )*
    // .......... .. . .. .. . $3 ListModifiers() )?
    final NodeChoice n0C = n.nodeChoice;
    final INode n0CH = n0C.choice;
    switch (n0C.which) {
      case 0:
        // %0 #0 ( AdditiveExpression() )?
        // .. #1 ( Blank() )*
        // .. #2 ( $0 <ARROW>
        // .. .. . $1 ( ?0 ( " " )
        // .. .. . .. . ?1 ( Blank() )*
        // .. .. . .. . ?2 ( AdditiveExpression() )? )* )
        // .. #3 ( $0 ( Blank() )*
        // .. .. . $1 ";"
        // .. .. . $2 ( Blank() )*
        // .. .. . $3 ListModifiers() )?
        final NodeSequence n0CHS0 = (NodeSequence) n0CH;
        // #0 ( AdditiveExpression() )?
        final INode n0CHS00A0 = n0CHS0.elementAt(0);
        final NodeOptional n0CHS00A0P = (NodeOptional) n0CHS00A0;
        if (n0CHS00A0P.present()) {
          n0CHS00A0P.accept(this, argu);
        }
        // #1 ( Blank() )*
        final INode n0CHS0A1 = n0CHS0.elementAt(1);
        final NodeListOptional n0CHS0A1T = (NodeListOptional) n0CHS0A1;
        if (n0CHS0A1T.present()) {
          for (int i = 0; i < n0CHS0A1T.size(); i++) {
            final INode n0CHS0A1TMi = n0CHS0A1T.elementAt(i);
            n0CHS0A1TMi.accept(this, argu);
          }
        }
        // #2 ( $0 <ARROW>
        // .. . $1 ( ?0 ( " " )
        // .. . .. . ?1 ( Blank() )*
        // .. . .. . ?2 ( AdditiveExpression() )? )* )
        final INode n0CHS0A2 = n0CHS0.elementAt(2);
        final NodeSequence n0CHS0A2S1 = (NodeSequence) n0CHS0A2;
        // $0 <ARROW>
        final INode n0CHS0A2S1A0 = n0CHS0A2S1.elementAt(0);
        n0CHS0A2S1A0.accept(this, argu);
        // $1 ( ?0 ( " " )
        // .. . ?1 ( Blank() )*
        // .. . ?2 ( AdditiveExpression() )? )*
        final INode n0CHS0A2S1A1 = n0CHS0A2S1.elementAt(1);
        final NodeListOptional n0CHS0A2S1A1T1 = (NodeListOptional) n0CHS0A2S1A1;
        if (n0CHS0A2S1A1T1.present()) {
          for (int i = 0; i < n0CHS0A2S1A1T1.size(); i++) {
            final INode n0CHS0A2S1A1T1Mi = n0CHS0A2S1A1T1.elementAt(i);
            final NodeSequence n0CHS0A2S1A1T1MiS2 = (NodeSequence) n0CHS0A2S1A1T1Mi;
            // ?0 ( " " )
            final INode n0CHS0A2S1A1T1MiS2A0 = n0CHS0A2S1A1T1MiS2.elementAt(0);
            n0CHS0A2S1A1T1MiS2A0.accept(this, argu);
            // ?1 ( Blank() )*
            final INode n0CHS0A2S1A1T1MiS2A1 = n0CHS0A2S1A1T1MiS2.elementAt(1);
            final NodeListOptional n0CHS0A2S1A1T1MiS2A1T2 = (NodeListOptional) n0CHS0A2S1A1T1MiS2A1;
            if (n0CHS0A2S1A1T1MiS2A1T2.present()) {
              for (int i1 = 0; i1 < n0CHS0A2S1A1T1MiS2A1T2.size(); i1++) {
                final INode n0CHS0A2S1A1T1MiS2A1T2Mi = n0CHS0A2S1A1T1MiS2A1T2.elementAt(i1);
                n0CHS0A2S1A1T1MiS2A1T2Mi.accept(this, argu);
              }
            }
            // ?2 ( AdditiveExpression() )?
            final INode n0CHS0A2S1A1T1MiS2A2 = n0CHS0A2S1A1T1MiS2.elementAt(2);
            final NodeOptional n0CHS0A2S1A1T1MiS2A2P1 = (NodeOptional) n0CHS0A2S1A1T1MiS2A2;
            if (n0CHS0A2S1A1T1MiS2A2P1.present()) {
              n0CHS0A2S1A1T1MiS2A2P1.accept(this, argu);
            }
          }
        }
        // #3 ( $0 ( Blank() )*
        // .. . $1 ";"
        // .. . $2 ( Blank() )*
        // .. . $3 ListModifiers() )?
        final INode n0CHS0A3 = n0CHS0.elementAt(3);
        final NodeOptional n0CHS0A3P2 = (NodeOptional) n0CHS0A3;
        if (n0CHS0A3P2.present()) {
          final NodeSequence n0CHS0A3P2S1 = (NodeSequence) n0CHS0A3P2.node;
          // $0 ( Blank() )*
          final INode n0CHS0A3P2S1A0 = n0CHS0A3P2S1.elementAt(0);
          final NodeListOptional n0CHS0A3P2S1A0T3 = (NodeListOptional) n0CHS0A3P2S1A0;
          if (n0CHS0A3P2S1A0T3.present()) {
            for (int i = 0; i < n0CHS0A3P2S1A0T3.size(); i++) {
              final INode n0CHS0A3P2S1A0T3Mi = n0CHS0A3P2S1A0T3.elementAt(i);
              n0CHS0A3P2S1A0T3Mi.accept(this, argu);
            }
          }
          // $1 ";"
          final INode n0CHS0A3P2S1A1 = n0CHS0A3P2S1.elementAt(1);
          n0CHS0A3P2S1A1.accept(this, argu);
          // $2 ( Blank() )*
          final INode n0CHS0A3P2S1A2 = n0CHS0A3P2S1.elementAt(2);
          final NodeListOptional n0CHS0A3P2S1A2T4 = (NodeListOptional) n0CHS0A3P2S1A2;
          if (n0CHS0A3P2S1A2T4.present()) {
            for (int i = 0; i < n0CHS0A3P2S1A2T4.size(); i++) {
              final INode n0CHS0A3P2S1A2T4Mi = n0CHS0A3P2S1A2T4.elementAt(i);
              n0CHS0A3P2S1A2T4Mi.accept(this, argu);
            }
          }
          // $3 ListModifiers()
          final INode n0CHS0A3P2S1A3 = n0CHS0A3P2S1.elementAt(3);
          n0CHS0A3P2S1A3.accept(this, argu);
        }
        break;
      case 1:
        // %1 #0 <ARROW2>
        // .. #1 ( Blank() )*
        // .. #2 ( AdditiveExpression() )?
        // .. #3 ( $0 ( Blank() )*
        // .. .. . $1 ";"
        // .. .. . $2 ( Blank() )*
        // .. .. . $3 ListModifiers() )?
        final NodeSequence n0CHS1 = (NodeSequence) n0CH;
        // #0 <ARROW2>
        final INode n0CHS11A0 = n0CHS1.elementAt(0);
        n0CHS11A0.accept(this, argu);
        // #1 ( Blank() )*
        final INode n0CHS11A1 = n0CHS1.elementAt(1);
        final NodeListOptional n0CHS11A1T5 = (NodeListOptional) n0CHS11A1;
        if (n0CHS11A1T5.present()) {
          for (int i = 0; i < n0CHS11A1T5.size(); i++) {
            final INode n0CHS11A1T5Mi = n0CHS11A1T5.elementAt(i);
            n0CHS11A1T5Mi.accept(this, argu);
          }
        }
        // #2 ( AdditiveExpression() )?
        final INode n0CHS1A2 = n0CHS1.elementAt(2);
        final NodeOptional n0CHS1A2P3 = (NodeOptional) n0CHS1A2;
        if (n0CHS1A2P3.present()) {
          n0CHS1A2P3.accept(this, argu);
        }
        // #3 ( $0 ( Blank() )*
        // .. . $1 ";"
        // .. . $2 ( Blank() )*
        // .. . $3 ListModifiers() )?
        final INode n0CHS1A3 = n0CHS1.elementAt(3);
        final NodeOptional n0CHS1A3P4 = (NodeOptional) n0CHS1A3;
        if (n0CHS1A3P4.present()) {
          final NodeSequence n0CHS1A3P4S2 = (NodeSequence) n0CHS1A3P4.node;
          // $0 ( Blank() )*
          final INode n0CHS1A3P4S2A0 = n0CHS1A3P4S2.elementAt(0);
          final NodeListOptional n0CHS1A3P4S2A0T6 = (NodeListOptional) n0CHS1A3P4S2A0;
          if (n0CHS1A3P4S2A0T6.present()) {
            for (int i = 0; i < n0CHS1A3P4S2A0T6.size(); i++) {
              final INode n0CHS1A3P4S2A0T6Mi = n0CHS1A3P4S2A0T6.elementAt(i);
              n0CHS1A3P4S2A0T6Mi.accept(this, argu);
            }
          }
          // $1 ";"
          final INode n0CHS1A3P4S2A1 = n0CHS1A3P4S2.elementAt(1);
          n0CHS1A3P4S2A1.accept(this, argu);
          // $2 ( Blank() )*
          final INode n0CHS1A3P4S2A2 = n0CHS1A3P4S2.elementAt(2);
          final NodeListOptional n0CHS1A3P4S2A2T7 = (NodeListOptional) n0CHS1A3P4S2A2;
          if (n0CHS1A3P4S2A2T7.present()) {
            for (int i = 0; i < n0CHS1A3P4S2A2T7.size(); i++) {
              final INode n0CHS1A3P4S2A2T7Mi = n0CHS1A3P4S2A2T7.elementAt(i);
              n0CHS1A3P4S2A2T7Mi.accept(this, argu);
            }
          }
          // $3 ListModifiers()
          final INode n0CHS1A3P4S2A3 = n0CHS1A3P4S2.elementAt(3);
          n0CHS1A3P4S2A3.accept(this, argu);
        }
        break;
      default:
        // should not occur !!!
        break;
    }
  }

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
   */
  public void visit(final AdditiveExpression n, final A argu) {
    // speciesWithCoeff -> SpeciesWithCoeff()
    final SpeciesWithCoeff n0 = n.speciesWithCoeff;
    n0.accept(this, argu);
    // nodeListOptional -> ( #0 ( Blank() )*
    // ................ .. . #1 " + "
    // ................ .. . #2 ( Blank() )*
    // ................ .. . #3 SpeciesWithCoeff() )*
    final NodeListOptional n1 = n.nodeListOptional;
    if (n1.present()) {
      for (int i = 0; i < n1.size(); i++) {
        final INode n1Mi = n1.elementAt(i);
        final NodeSequence n1MiS0 = (NodeSequence) n1Mi;
        // #0 ( Blank() )*
        final INode n1MiS0A0 = n1MiS0.elementAt(0);
        final NodeListOptional n1MiS0A0T = (NodeListOptional) n1MiS0A0;
        if (n1MiS0A0T.present()) {
          for (int i1 = 0; i1 < n1MiS0A0T.size(); i1++) {
            final INode n1MiS0A0TMi = n1MiS0A0T.elementAt(i1);
            n1MiS0A0TMi.accept(this, argu);
          }
        }
        // #1 " + "
        final INode n1MiS0A1 = n1MiS0.elementAt(1);
        n1MiS0A1.accept(this, argu);
        // #2 ( Blank() )*
        final INode n1MiS0A2 = n1MiS0.elementAt(2);
        final NodeListOptional n1MiS0A2T1 = (NodeListOptional) n1MiS0A2;
        if (n1MiS0A2T1.present()) {
          for (int i1 = 0; i1 < n1MiS0A2T1.size(); i1++) {
            final INode n1MiS0A2T1Mi = n1MiS0A2T1.elementAt(i1);
            n1MiS0A2T1Mi.accept(this, argu);
          }
        }
        // #3 SpeciesWithCoeff()
        final INode n1MiS0A3 = n1MiS0.elementAt(3);
        n1MiS0A3.accept(this, argu);
      }
    }
  }

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
   */
  public void visit(final SpeciesWithCoeff n, final A argu) {
    // nodeOptional -> ( #0 Stoichiometry()
    // ............ .. . #1 ( Blank() )*
    // ............ .. . #2 " * "
    // ............ .. . #3 ( Blank() )* )?
    final NodeOptional n0 = n.nodeOptional;
    if (n0.present()) {
      final NodeSequence n0S0 = (NodeSequence) n0.node;
      // #0 Stoichiometry()
      final INode n0S0A0 = n0S0.elementAt(0);
      n0S0A0.accept(this, argu);
      // #1 ( Blank() )*
      final INode n0S0A1 = n0S0.elementAt(1);
      final NodeListOptional n0S0A1T = (NodeListOptional) n0S0A1;
      if (n0S0A1T.present()) {
        for (int i = 0; i < n0S0A1T.size(); i++) {
          final INode n0S0A1TMi = n0S0A1T.elementAt(i);
          n0S0A1TMi.accept(this, argu);
        }
      }
      // #2 " * "
      final INode n0S0A2 = n0S0.elementAt(2);
      n0S0A2.accept(this, argu);
      // #3 ( Blank() )*
      final INode n0S0A3 = n0S0.elementAt(3);
      final NodeListOptional n0S0A3T1 = (NodeListOptional) n0S0A3;
      if (n0S0A3T1.present()) {
        for (int i = 0; i < n0S0A3T1.size(); i++) {
          final INode n0S0A3T1Mi = n0S0A3T1.elementAt(i);
          n0S0A3T1Mi.accept(this, argu);
        }
      }
    }
    // species -> Species()
    final Species n1 = n.species;
    n1.accept(this, argu);
  }

  /**
   * Visits a {@link Blank} node, whose children are the following :
   * <p>
   * nodeToken -> " "<br>
   *
   * @param n - the node to visit
   * @param argu - the user argument
   */
  public void visit(final Blank n, final A argu) {
    // nodeToken -> " "
    final NodeToken n0 = n.nodeToken;
    n0.accept(this, argu);
  }

  /**
   * Visits a {@link ListModifiers} node, whose children are the following :
   * <p>
   * species -> Species()<br>
   * nodeListOptional -> ( #0 ( Blank() )+<br>
   * ................ .. . #1 Species() )*<br>
   *
   * @param n - the node to visit
   * @param argu - the user argument
   */
  public void visit(final ListModifiers n, final A argu) {
    // species -> Species()
    final Species n0 = n.species;
    n0.accept(this, argu);
    // nodeListOptional -> ( #0 ( Blank() )+
    // ................ .. . #1 Species() )*
    final NodeListOptional n1 = n.nodeListOptional;
    if (n1.present()) {
      for (int i = 0; i < n1.size(); i++) {
        final INode n1Mi = n1.elementAt(i);
        final NodeSequence n1MiS0 = (NodeSequence) n1Mi;
        // #0 ( Blank() )+
        final INode n1MiS0A0 = n1MiS0.elementAt(0);
        final NodeList n1MiS0A0L = (NodeList) n1MiS0A0;
        for (int i1 = 0; i1 < n1MiS0A0L.size(); i1++) {
          final INode n1MiS0A0LEi = n1MiS0A0L.elementAt(i1);
          n1MiS0A0LEi.accept(this, argu);
        }
        // #1 Species()
        final INode n1MiS0A1 = n1MiS0.elementAt(1);
        n1MiS0A1.accept(this, argu);
      }
    }
  }

  /**
   * Visits a {@link Species} node, whose children are the following :
   * <p>
   * nodeToken -> <IDENTIFIER><br>
   * nodeListOptional -> ( <IDENTIFIER> )*<br>
   *
   * @param n - the node to visit
   * @param argu - the user argument
   */
  public void visit(final Species n, final A argu) {
    // nodeToken -> <IDENTIFIER>
    final NodeToken n0 = n.nodeToken;
    n0.accept(this, argu);
    // nodeListOptional -> ( <IDENTIFIER> )*
    final NodeListOptional n1 = n.nodeListOptional;
    if (n1.present()) {
      for (int i = 0; i < n1.size(); i++) {
        final INode n1Mi = n1.elementAt(i);
        n1Mi.accept(this, argu);
      }
    }
  }

  /**
   * Visits a {@link Stoichiometry} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 <INTEGER_LITERAL><br>
   * .......... .. | %1 <FLOATING_POINT_LITERAL><br>
   *
   * @param n - the node to visit
   * @param argu - the user argument
   */
  public void visit(final Stoichiometry n, final A argu) {
    // nodeChoice -> . %0 <INTEGER_LITERAL>
    // .......... .. | %1 <FLOATING_POINT_LITERAL>
    final NodeChoice n0C = n.nodeChoice;
    final INode n0CH = n0C.choice;
    switch (n0C.which) {
      case 0:
        // %0 <INTEGER_LITERAL>
        n0CH.accept(this, argu);
        break;
      case 1:
        // %1 <FLOATING_POINT_LITERAL>
        n0CH.accept(this, argu);
        break;
      default:
        // should not occur !!!
        break;
    }
  }

}
