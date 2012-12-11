/* Generated by JTB 1.4.4 */
package msmb.parsers.mathExpression.visitor;

import msmb.parsers.mathExpression.syntaxtree.*;
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
   * Visits a {@link CompleteExpression} node, whose children are the following :
   * <p>
   * expression -> Expression()<br>
   * nodeToken -> < EOF ><br>
   *
   * @param n the node to visit
   */
  public void visit(final CompleteExpression n) {
    final Expression n0 = n.expression;
    n0.accept(this);
    final NodeToken n1 = n.nodeToken;
    n1.accept(this);
  }

  /**
   * Visits a {@link CompleteListOfExpression} node, whose children are the following :
   * <p>
   * expression -> Expression()<br>
   * nodeListOptional -> ( #0 < COMMA > #1 Expression() )*<br>
   * nodeToken -> < EOF ><br>
   *
   * @param n the node to visit
   */
  public void visit(final CompleteListOfExpression n) {
    final Expression n0 = n.expression;
    n0.accept(this);
    final NodeListOptional n1 = n.nodeListOptional;
    if (n1.present()) {
      for (int i = 0; i < n1.size(); i++) {
        final INode n1Ei = n1.elementAt(i);
        final NodeSequence n1EiSeq = (NodeSequence) n1Ei;
        final INode n1EiSeqA0 = n1EiSeq.elementAt(0);
        n1EiSeqA0.accept(this);
        final INode n1EiSeqA1 = n1EiSeq.elementAt(1);
        n1EiSeqA1.accept(this);
      }
    }
    final NodeToken n2 = n.nodeToken;
    n2.accept(this);
  }

  /**
   * Visits a {@link SingleFunctionCall} node, whose children are the following :
   * <p>
   * name -> Name()<br>
   * nodeToken -> < LPAREN ><br>
   * nodeOptional -> ( ArgumentList() )?<br>
   * nodeToken1 -> < RPAREN ><br>
   * nodeToken2 -> < EOF ><br>
   *
   * @param n the node to visit
   */
  public void visit(final SingleFunctionCall n) {
    final Name n0 = n.name;
    n0.accept(this);
    final NodeToken n1 = n.nodeToken;
    n1.accept(this);
    final NodeOptional n2 = n.nodeOptional;
    if (n2.present()) {
      n2.accept(this);
    }
    final NodeToken n3 = n.nodeToken1;
    n3.accept(this);
    final NodeToken n4 = n.nodeToken2;
    n4.accept(this);
  }

  /**
   * Visits a {@link CompleteFunctionDeclaration} node, whose children are the following :
   * <p>
   * functionDeclarator -> FunctionDeclarator()<br>
   * nodeToken -> < EOF ><br>
   *
   * @param n the node to visit
   */
  public void visit(final CompleteFunctionDeclaration n) {
    final FunctionDeclarator n0 = n.functionDeclarator;
    n0.accept(this);
    final NodeToken n1 = n.nodeToken;
    n1.accept(this);
  }

  /**
   * Visits a {@link FunctionDeclarator} node, whose children are the following :
   * <p>
   * nodeToken -> < IDENTIFIER ><br>
   * nodeOptional -> ( FormalParameters() )?<br>
   *
   * @param n the node to visit
   */
  public void visit(final FunctionDeclarator n) {
    final NodeToken n0 = n.nodeToken;
    n0.accept(this);
    final NodeOptional n1 = n.nodeOptional;
    if (n1.present()) {
      n1.accept(this);
    }
  }

  /**
   * Visits a {@link FormalParameters} node, whose children are the following :
   * <p>
   * nodeToken -> "("<br>
   * nodeOptional -> [ #0 FormalParameter()<br>
   * ............ .. . #1 ( $0 "," $1 FormalParameter() )* ]<br>
   * nodeToken1 -> ")"<br>
   *
   * @param n the node to visit
   */
  public void visit(final FormalParameters n) {
    final NodeToken n0 = n.nodeToken;
    n0.accept(this);
    final NodeOptional n1 = n.nodeOptional;
    if (n1.present()) {
      final NodeSequence n1Seq = (NodeSequence) n1.node;
      final INode n1SeqA0 = n1Seq.elementAt(0);
      n1SeqA0.accept(this);
      final INode n1SeqA1 = n1Seq.elementAt(1);
      final NodeListOptional n1SeqA1Nlo = (NodeListOptional) n1SeqA1;
      if (n1SeqA1Nlo.present()) {
        for (int i = 0; i < n1SeqA1Nlo.size(); i++) {
          final INode n1SeqA1NloEi = n1SeqA1Nlo.elementAt(i);
          final NodeSequence n1SeqA1NloEiSeq = (NodeSequence) n1SeqA1NloEi;
          final INode n1SeqA1NloEiSeqA0 = n1SeqA1NloEiSeq.elementAt(0);
          n1SeqA1NloEiSeqA0.accept(this);
          final INode n1SeqA1NloEiSeqA1 = n1SeqA1NloEiSeq.elementAt(1);
          n1SeqA1NloEiSeqA1.accept(this);
        }
      }
    }
    final NodeToken n2 = n.nodeToken1;
    n2.accept(this);
  }

  /**
   * Visits a {@link FormalParameter} node, whose children are the following :
   * <p>
   * primitiveType -> PrimitiveType()<br>
   * variableDeclaratorId -> VariableDeclaratorId()<br>
   *
   * @param n the node to visit
   */
  public void visit(final FormalParameter n) {
    final PrimitiveType n0 = n.primitiveType;
    n0.accept(this);
    final VariableDeclaratorId n1 = n.variableDeclaratorId;
    n1.accept(this);
  }

  /**
   * Visits a {@link PrimitiveType} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 < TYPE_PAR ><br>
   * .......... .. | %1 < TYPE_VAR ><br>
   * .......... .. | %2 < TYPE_SUB ><br>
   * .......... .. | %3 < TYPE_PROD ><br>
   * .......... .. | %4 < TYPE_MOD ><br>
   * .......... .. | %5 < TYPE_SITE ><br>
   * .......... .. | %6 < TYPE_VOL ><br>
   * .......... .. | %7 < TYPE_TIME ><br>
   *
   * @param n the node to visit
   */
  public void visit(final PrimitiveType n) {
    final NodeChoice n0Ch = n.nodeChoice;
    final INode n0ChN = n0Ch.choice;
    switch (n0Ch.which) {
      case 0:
        n0ChN.accept(this);
        break;
      case 1:
        n0ChN.accept(this);
        break;
      case 2:
        n0ChN.accept(this);
        break;
      case 3:
        n0ChN.accept(this);
        break;
      case 4:
        n0ChN.accept(this);
        break;
      case 5:
        n0ChN.accept(this);
        break;
      case 6:
        n0ChN.accept(this);
        break;
      case 7:
        n0ChN.accept(this);
        break;
      default:
        // should not occur !!!
        break;
    }
  }

  /**
   * Visits a {@link VariableDeclaratorId} node, whose children are the following :
   * <p>
   * nodeToken -> < IDENTIFIER ><br>
   *
   * @param n the node to visit
   */
  public void visit(final VariableDeclaratorId n) {
    final NodeToken n0 = n.nodeToken;
    n0.accept(this);
  }

  /**
   * Visits a {@link IfExpression} node, whose children are the following :
   * <p>
   * nodeToken -> < IF ><br>
   * nodeToken1 -> < LPAREN ><br>
   * expression -> Expression()<br>
   * nodeToken2 -> < COMMA ><br>
   * expression1 -> Expression()<br>
   * nodeOptional -> ( #0 < COMMA > #1 Expression() )?<br>
   * nodeToken3 -> < RPAREN ><br>
   *
   * @param n the node to visit
   */
  public void visit(final IfExpression n) {
    final NodeToken n0 = n.nodeToken;
    n0.accept(this);
    final NodeToken n1 = n.nodeToken1;
    n1.accept(this);
    final Expression n2 = n.expression;
    n2.accept(this);
    final NodeToken n3 = n.nodeToken2;
    n3.accept(this);
    final Expression n4 = n.expression1;
    n4.accept(this);
    final NodeOptional n5 = n.nodeOptional;
    if (n5.present()) {
      final NodeSequence n5Seq = (NodeSequence) n5.node;
      final INode n5SeqA0 = n5Seq.elementAt(0);
      n5SeqA0.accept(this);
      final INode n5SeqA1 = n5Seq.elementAt(1);
      n5SeqA1.accept(this);
    }
    final NodeToken n6 = n.nodeToken3;
    n6.accept(this);
  }

  /**
   * Visits a {@link LeftExpression} node, whose children are the following :
   * <p>
   * additiveExpression -> AdditiveExpression()<br>
   * nodeOptional -> ( #0 RelationalOperator() #1 Expression() )?<br>
   *
   * @param n the node to visit
   */
  public void visit(final LeftExpression n) {
    final AdditiveExpression n0 = n.additiveExpression;
    n0.accept(this);
    final NodeOptional n1 = n.nodeOptional;
    if (n1.present()) {
      final NodeSequence n1Seq = (NodeSequence) n1.node;
      final INode n1SeqA0 = n1Seq.elementAt(0);
      n1SeqA0.accept(this);
      final INode n1SeqA1 = n1Seq.elementAt(1);
      n1SeqA1.accept(this);
    }
  }

  /**
   * Visits a {@link Expression} node, whose children are the following :
   * <p>
   * leftExpression -> LeftExpression()<br>
   * nodeListOptional -> ( #0 LogicalOperator() #1 Expression() )*<br>
   *
   * @param n the node to visit
   */
  public void visit(final Expression n) {
    final LeftExpression n0 = n.leftExpression;
    n0.accept(this);
    final NodeListOptional n1 = n.nodeListOptional;
    if (n1.present()) {
      for (int i = 0; i < n1.size(); i++) {
        final INode n1Ei = n1.elementAt(i);
        final NodeSequence n1EiSeq = (NodeSequence) n1Ei;
        final INode n1EiSeqA0 = n1EiSeq.elementAt(0);
        n1EiSeqA0.accept(this);
        final INode n1EiSeqA1 = n1EiSeq.elementAt(1);
        n1EiSeqA1.accept(this);
      }
    }
  }

  /**
   * Visits a {@link RelationalOperator} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 ( &0 < ASSIGN ><br>
   * .......... .. . .. | &1 < EQ > )<br>
   * .......... .. | %1 < LT ><br>
   * .......... .. | %2 < GT ><br>
   * .......... .. | %3 < GEQ ><br>
   * .......... .. | %4 < LEQ ><br>
   *
   * @param n the node to visit
   */
  public void visit(final RelationalOperator n) {
    final NodeChoice n0Ch = n.nodeChoice;
    final INode n0ChN = n0Ch.choice;
    switch (n0Ch.which) {
      case 0:
        final NodeChoice n0ChNCh = (NodeChoice) n0ChN;
        final INode n0ChNChN = n0ChNCh.choice;
        switch (n0ChNCh.which) {
          case 0:
            n0ChNChN.accept(this);
            break;
          case 1:
            n0ChNChN.accept(this);
            break;
          default:
            // should not occur !!!
            break;
        }
        break;
      case 1:
        n0ChN.accept(this);
        break;
      case 2:
        n0ChN.accept(this);
        break;
      case 3:
        n0ChN.accept(this);
        break;
      case 4:
        n0ChN.accept(this);
        break;
      default:
        // should not occur !!!
        break;
    }
  }

  /**
   * Visits a {@link LogicalOperator} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 < AND ><br>
   * .......... .. | %1 < OR ><br>
   * .......... .. | %2 < XOR ><br>
   *
   * @param n the node to visit
   */
  public void visit(final LogicalOperator n) {
    final NodeChoice n0Ch = n.nodeChoice;
    final INode n0ChN = n0Ch.choice;
    switch (n0Ch.which) {
      case 0:
        n0ChN.accept(this);
        break;
      case 1:
        n0ChN.accept(this);
        break;
      case 2:
        n0ChN.accept(this);
        break;
      default:
        // should not occur !!!
        break;
    }
  }

  /**
   * Visits a {@link AdditiveExpression} node, whose children are the following :
   * <p>
   * multiplicativeExpression -> MultiplicativeExpression()<br>
   * nodeListOptional -> ( #0 ( %0 < PLUS ><br>
   * ................ .. . .. | %1 < MINUS > ) #1 MultiplicativeExpression() )*<br>
   *
   * @param n the node to visit
   */
  public void visit(final AdditiveExpression n) {
    final MultiplicativeExpression n0 = n.multiplicativeExpression;
    n0.accept(this);
    final NodeListOptional n1 = n.nodeListOptional;
    if (n1.present()) {
      for (int i = 0; i < n1.size(); i++) {
        final INode n1Ei = n1.elementAt(i);
        final NodeSequence n1EiSeq = (NodeSequence) n1Ei;
        final INode n1EiSeqA0 = n1EiSeq.elementAt(0);
        final NodeChoice n1EiSeqA0Ch = (NodeChoice) n1EiSeqA0;
        final INode n1EiSeqA0ChN = n1EiSeqA0Ch.choice;
        switch (n1EiSeqA0Ch.which) {
          case 0:
            n1EiSeqA0ChN.accept(this);
            break;
          case 1:
            n1EiSeqA0ChN.accept(this);
            break;
          default:
            // should not occur !!!
            break;
        }
        final INode n1EiSeqA1 = n1EiSeq.elementAt(1);
        n1EiSeqA1.accept(this);
      }
    }
  }

  /**
   * Visits a {@link MultiplicativeExpression} node, whose children are the following :
   * <p>
   * powerExpression -> PowerExpression()<br>
   * nodeListOptional -> ( #0 ( %0 < TIMES ><br>
   * ................ .. . .. | %1 < DIV > ) #1 PowerExpression() )*<br>
   *
   * @param n the node to visit
   */
  public void visit(final MultiplicativeExpression n) {
    final PowerExpression n0 = n.powerExpression;
    n0.accept(this);
    final NodeListOptional n1 = n.nodeListOptional;
    if (n1.present()) {
      for (int i = 0; i < n1.size(); i++) {
        final INode n1Ei = n1.elementAt(i);
        final NodeSequence n1EiSeq = (NodeSequence) n1Ei;
        final INode n1EiSeqA0 = n1EiSeq.elementAt(0);
        final NodeChoice n1EiSeqA0Ch = (NodeChoice) n1EiSeqA0;
        final INode n1EiSeqA0ChN = n1EiSeqA0Ch.choice;
        switch (n1EiSeqA0Ch.which) {
          case 0:
            n1EiSeqA0ChN.accept(this);
            break;
          case 1:
            n1EiSeqA0ChN.accept(this);
            break;
          default:
            // should not occur !!!
            break;
        }
        final INode n1EiSeqA1 = n1EiSeq.elementAt(1);
        n1EiSeqA1.accept(this);
      }
    }
  }

  /**
   * Visits a {@link PowerExpression} node, whose children are the following :
   * <p>
   * unaryExpression -> UnaryExpression()<br>
   * nodeListOptional -> ( #0 ( < CARET > ) #1 UnaryExpression() )*<br>
   *
   * @param n the node to visit
   */
  public void visit(final PowerExpression n) {
    final UnaryExpression n0 = n.unaryExpression;
    n0.accept(this);
    final NodeListOptional n1 = n.nodeListOptional;
    if (n1.present()) {
      for (int i = 0; i < n1.size(); i++) {
        final INode n1Ei = n1.elementAt(i);
        final NodeSequence n1EiSeq = (NodeSequence) n1Ei;
        final INode n1EiSeqA0 = n1EiSeq.elementAt(0);
        n1EiSeqA0.accept(this);
        final INode n1EiSeqA1 = n1EiSeq.elementAt(1);
        n1EiSeqA1.accept(this);
      }
    }
  }

  /**
   * Visits a {@link UnaryExpression} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 #0 ( &0 < PLUS ><br>
   * .......... .. . .. .. | &1 < MINUS > ) #1 UnaryExpression()<br>
   * .......... .. | %1 UnaryExpressionNotPlusMinus()<br>
   *
   * @param n the node to visit
   */
  public void visit(final UnaryExpression n) {
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
            n0ChNSeq0A0ChN.accept(this);
            break;
          case 1:
            n0ChNSeq0A0ChN.accept(this);
            break;
          default:
            // should not occur !!!
            break;
        }
        final INode n0ChNSeq0A1 = n0ChNSeq0.elementAt(1);
        n0ChNSeq0A1.accept(this);
        break;
      case 1:
        n0ChN.accept(this);
        break;
      default:
        // should not occur !!!
        break;
    }
  }

  /**
   * Visits a {@link UnaryExpressionNotPlusMinus} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 #0 < BANG > #1 UnaryExpression()<br>
   * .......... .. | %1 PrimaryExpression()<br>
   *
   * @param n the node to visit
   */
  public void visit(final UnaryExpressionNotPlusMinus n) {
    final NodeChoice n0Ch = n.nodeChoice;
    final INode n0ChN = n0Ch.choice;
    switch (n0Ch.which) {
      case 0:
        final NodeSequence n0ChNSeq0 = (NodeSequence) n0ChN;
        final INode n0ChNSeq0A0 = n0ChNSeq0.elementAt(0);
        n0ChNSeq0A0.accept(this);
        final INode n0ChNSeq0A1 = n0ChNSeq0.elementAt(1);
        n0ChNSeq0A1.accept(this);
        break;
      case 1:
        n0ChN.accept(this);
        break;
      default:
        // should not occur !!!
        break;
    }
  }

  /**
   * Visits a {@link PrimaryExpression} node, whose children are the following :
   * <p>
   * primaryPrefix -> PrimaryPrefix()<br>
   *
   * @param n the node to visit
   */
  public void visit(final PrimaryExpression n) {
    final PrimaryPrefix n0 = n.primaryPrefix;
    n0.accept(this);
  }

  /**
   * Visits a {@link PrimaryPrefix} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 Literal()<br>
   * .......... .. | %1 #0 < LPAREN > #1 Expression() #2 < RPAREN ><br>
   * .......... .. | %2 SpeciesReferenceOrFunctionCall()<br>
   * .......... .. | %3 MultistateSum()<br>
   * .......... .. | %4 IfExpression()<br>
   * .......... .. | %5 < CONST_MODEL_TIME ><br>
   * .......... .. | %6 < CONST_AVOGADRO ><br>
   * .......... .. | %7 < CONST_QUANTITY_CONV_FACTOR ><br>
   * .......... .. | %8 < CONST_MODEL_TIME_INITIAL ><br>
   *
   * @param n the node to visit
   */
  public void visit(final PrimaryPrefix n) {
    final NodeChoice n0Ch = n.nodeChoice;
    final INode n0ChN = n0Ch.choice;
    switch (n0Ch.which) {
      case 0:
        n0ChN.accept(this);
        break;
      case 1:
        final NodeSequence n0ChNSeq1 = (NodeSequence) n0ChN;
        final INode n0ChNSeq1A0 = n0ChNSeq1.elementAt(0);
        n0ChNSeq1A0.accept(this);
        final INode n0ChNSeq1A1 = n0ChNSeq1.elementAt(1);
        n0ChNSeq1A1.accept(this);
        final INode n0ChNSeq1A2 = n0ChNSeq1.elementAt(2);
        n0ChNSeq1A2.accept(this);
        break;
      case 2:
        n0ChN.accept(this);
        break;
      case 3:
        n0ChN.accept(this);
        break;
      case 4:
        n0ChN.accept(this);
        break;
      case 5:
        n0ChN.accept(this);
        break;
      case 6:
        n0ChN.accept(this);
        break;
      case 7:
        n0ChN.accept(this);
        break;
      case 8:
        n0ChN.accept(this);
        break;
      default:
        // should not occur !!!
        break;
    }
  }

  /**
   * Visits a {@link MultistateSum} node, whose children are the following :
   * <p>
   * nodeToken -> < SUM ><br>
   * nodeToken1 -> < LPAREN ><br>
   * argumentList_MultistateSum -> ArgumentList_MultistateSum()<br>
   * nodeToken2 -> < RPAREN ><br>
   *
   * @param n the node to visit
   */
  public void visit(final MultistateSum n) {
    final NodeToken n0 = n.nodeToken;
    n0.accept(this);
    final NodeToken n1 = n.nodeToken1;
    n1.accept(this);
    final ArgumentList_MultistateSum n2 = n.argumentList_MultistateSum;
    n2.accept(this);
    final NodeToken n3 = n.nodeToken2;
    n3.accept(this);
  }

  /**
   * Visits a {@link Name} node, whose children are the following :
   * <p>
   * nodeChoice -> . %00 < IDENTIFIER ><br>
   * .......... .. | %01 PrimitiveType()<br>
   * .......... .. | %02 < PI ><br>
   * .......... .. | %03 < TIME ><br>
   * .......... .. | %04 < FLOOR ><br>
   * .......... .. | %05 < DELAY ><br>
   * .......... .. | %06 < CEIL ><br>
   * .......... .. | %07 < TAN ><br>
   * .......... .. | %08 < TANH ><br>
   * .......... .. | %09 < COSH ><br>
   * .......... .. | %10 < LOG10 ><br>
   * .......... .. | %11 < ABS ><br>
   * .......... .. | %12 < COS ><br>
   * .......... .. | %13 < SIN ><br>
   * .......... .. | %14 < LOG ><br>
   * .......... .. | %15 < EXP ><br>
   * .......... .. | %16 < NAN ><br>
   *
   * @param n the node to visit
   */
  public void visit(final Name n) {
    final NodeChoice n0Ch = n.nodeChoice;
    final INode n0ChN = n0Ch.choice;
    switch (n0Ch.which) {
      case 0:
        n0ChN.accept(this);
        break;
      case 1:
        n0ChN.accept(this);
        break;
      case 2:
        n0ChN.accept(this);
        break;
      case 3:
        n0ChN.accept(this);
        break;
      case 4:
        n0ChN.accept(this);
        break;
      case 5:
        n0ChN.accept(this);
        break;
      case 6:
        n0ChN.accept(this);
        break;
      case 7:
        n0ChN.accept(this);
        break;
      case 8:
        n0ChN.accept(this);
        break;
      case 9:
        n0ChN.accept(this);
        break;
      case 10:
        n0ChN.accept(this);
        break;
      case 11:
        n0ChN.accept(this);
        break;
      case 12:
        n0ChN.accept(this);
        break;
      case 13:
        n0ChN.accept(this);
        break;
      case 14:
        n0ChN.accept(this);
        break;
      case 15:
        n0ChN.accept(this);
        break;
      case 16:
        n0ChN.accept(this);
        break;
      default:
        // should not occur !!!
        break;
    }
  }

  /**
   * Visits a {@link SpeciesReferenceOrFunctionCall_prefix} node, whose children are the following :
   * <p>
   * name -> Name()<br>
   * nodeOptional -> [ #0 < LPAREN ><br>
   * ............ .. . #1 [ ArgumentList() ] #2 < RPAREN > ]<br>
   *
   * @param n the node to visit
   */
  public void visit(final SpeciesReferenceOrFunctionCall_prefix n) {
    final Name n0 = n.name;
    n0.accept(this);
    final NodeOptional n1 = n.nodeOptional;
    if (n1.present()) {
      final NodeSequence n1Seq = (NodeSequence) n1.node;
      final INode n1SeqA0 = n1Seq.elementAt(0);
      n1SeqA0.accept(this);
      final INode n1SeqA1 = n1Seq.elementAt(1);
      final NodeOptional n1SeqA1Opt = (NodeOptional) n1SeqA1;
      if (n1SeqA1Opt.present()) {
        n1SeqA1Opt.accept(this);
      }
      final INode n1SeqA2 = n1Seq.elementAt(2);
      n1SeqA2.accept(this);
    }
  }

  /**
   * Visits a {@link SpeciesReferenceOrFunctionCall} node, whose children are the following :
   * <p>
   * speciesReferenceOrFunctionCall_prefix -> SpeciesReferenceOrFunctionCall_prefix()<br>
   * nodeListOptional -> ( ( PossibleExtensions() ) )*<br>
   *
   * @param n the node to visit
   */
  public void visit(final SpeciesReferenceOrFunctionCall n) {
    final SpeciesReferenceOrFunctionCall_prefix n0 = n.speciesReferenceOrFunctionCall_prefix;
    n0.accept(this);
    final NodeListOptional n1 = n.nodeListOptional;
    if (n1.present()) {
      for (int i = 0; i < n1.size(); i++) {
        final INode n1Ei = n1.elementAt(i);
        n1Ei.accept(this);
      }
    }
  }

  /**
   * Visits a {@link PossibleExtensions} node, whose children are the following :
   * <p>
   * nodeChoice -> ( %00 < EXTENSION_CONC ><br>
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
   * .......... .. | %11 < MY_SPECIAL_EXTENSION > )<br>
   * nodeListOptional -> ( PossibleExtensions() )*<br>
   *
   * @param n the node to visit
   */
  public void visit(final PossibleExtensions n) {
    final NodeChoice n0 = n.nodeChoice;
    final INode n0N = n0.choice;
    switch (n0.which) {
      case 0:
        n0N.accept(this);
        break;
      case 1:
        n0N.accept(this);
        break;
      case 2:
        n0N.accept(this);
        break;
      case 3:
        n0N.accept(this);
        break;
      case 4:
        n0N.accept(this);
        break;
      case 5:
        n0N.accept(this);
        break;
      case 6:
        n0N.accept(this);
        break;
      case 7:
        n0N.accept(this);
        break;
      case 8:
        n0N.accept(this);
        break;
      case 9:
        n0N.accept(this);
        break;
      case 10:
        n0N.accept(this);
        break;
      case 11:
        n0N.accept(this);
        break;
      default:
        // should not occur !!!
        break;
    }
    final NodeListOptional n1 = n.nodeListOptional;
    if (n1.present()) {
      for (int i = 0; i < n1.size(); i++) {
        final INode n1Ei = n1.elementAt(i);
        n1Ei.accept(this);
      }
    }
  }

  /**
   * Visits a {@link Literal} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 < INTEGER_LITERAL ><br>
   * .......... .. | %1 < FLOATING_POINT_LITERAL ><br>
   * .......... .. | %2 BooleanLiteral()<br>
   *
   * @param n the node to visit
   */
  public void visit(final Literal n) {
    final NodeChoice n0Ch = n.nodeChoice;
    final INode n0ChN = n0Ch.choice;
    switch (n0Ch.which) {
      case 0:
        n0ChN.accept(this);
        break;
      case 1:
        n0ChN.accept(this);
        break;
      case 2:
        n0ChN.accept(this);
        break;
      default:
        // should not occur !!!
        break;
    }
  }

  /**
   * Visits a {@link BooleanLiteral} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 < TRUE ><br>
   * .......... .. | %1 < FALSE ><br>
   *
   * @param n the node to visit
   */
  public void visit(final BooleanLiteral n) {
    final NodeChoice n0Ch = n.nodeChoice;
    final INode n0ChN = n0Ch.choice;
    switch (n0Ch.which) {
      case 0:
        n0ChN.accept(this);
        break;
      case 1:
        n0ChN.accept(this);
        break;
      default:
        // should not occur !!!
        break;
    }
  }

  /**
   * Visits a {@link ArgumentList} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 MultistateSites_list()<br>
   * .......... .. | %1 #0 AdditiveExpression()<br>
   * .......... .. . .. #1 ( $0 < COMMA > $1 AdditiveExpression() )*<br>
   *
   * @param n the node to visit
   */
  public void visit(final ArgumentList n) {
    final NodeChoice n0Ch = n.nodeChoice;
    final INode n0ChN = n0Ch.choice;
    switch (n0Ch.which) {
      case 0:
        n0ChN.accept(this);
        break;
      case 1:
        final NodeSequence n0ChNSeq1 = (NodeSequence) n0ChN;
        final INode n0ChNSeq1A0 = n0ChNSeq1.elementAt(0);
        n0ChNSeq1A0.accept(this);
        final INode n0ChNSeq1A1 = n0ChNSeq1.elementAt(1);
        final NodeListOptional n0ChNSeq1A1Nlo = (NodeListOptional) n0ChNSeq1A1;
        if (n0ChNSeq1A1Nlo.present()) {
          for (int i = 0; i < n0ChNSeq1A1Nlo.size(); i++) {
            final INode n0ChNSeq1A1NloEi = n0ChNSeq1A1Nlo.elementAt(i);
            final NodeSequence n0ChNSeq1A1NloEiSeq = (NodeSequence) n0ChNSeq1A1NloEi;
            final INode n0ChNSeq1A1NloEiSeqA0 = n0ChNSeq1A1NloEiSeq.elementAt(0);
            n0ChNSeq1A1NloEiSeqA0.accept(this);
            final INode n0ChNSeq1A1NloEiSeqA1 = n0ChNSeq1A1NloEiSeq.elementAt(1);
            n0ChNSeq1A1NloEiSeqA1.accept(this);
          }
        }
        break;
      default:
        // should not occur !!!
        break;
    }
  }

  /**
   * Visits a {@link ArgumentList_MultistateSum} node, whose children are the following :
   * <p>
   * name -> Name()<br>
   * nodeOptional -> [ ArgumentList_MultistateSum_Selectors() ]<br>
   *
   * @param n the node to visit
   */
  public void visit(final ArgumentList_MultistateSum n) {
    final Name n0 = n.name;
    n0.accept(this);
    final NodeOptional n1 = n.nodeOptional;
    if (n1.present()) {
      n1.accept(this);
    }
  }

  /**
   * Visits a {@link ArgumentList_MultistateSum_Selectors} node, whose children are the following :
   * <p>
   * nodeToken -> < SEMICOLON ><br>
   * selector -> Selector()<br>
   * nodeListOptional -> ( #0 < SEMICOLON > #1 Selector() )*<br>
   *
   * @param n the node to visit
   */
  public void visit(final ArgumentList_MultistateSum_Selectors n) {
    final NodeToken n0 = n.nodeToken;
    n0.accept(this);
    final Selector n1 = n.selector;
    n1.accept(this);
    final NodeListOptional n2 = n.nodeListOptional;
    if (n2.present()) {
      for (int i = 0; i < n2.size(); i++) {
        final INode n2Ei = n2.elementAt(i);
        final NodeSequence n2EiSeq = (NodeSequence) n2Ei;
        final INode n2EiSeqA0 = n2EiSeq.elementAt(0);
        n2EiSeqA0.accept(this);
        final INode n2EiSeqA1 = n2EiSeq.elementAt(1);
        n2EiSeqA1.accept(this);
      }
    }
  }

  /**
   * Visits a {@link Selector} node, whose children are the following :
   * <p>
   * name -> Name()<br>
   * nodeOptional -> [ %0 SiteSelector_postFix()<br>
   * ............ .. | %1 CoeffFunction_postFix() ]<br>
   *
   * @param n the node to visit
   */
  public void visit(final Selector n) {
    final Name n0 = n.name;
    n0.accept(this);
    final NodeOptional n1 = n.nodeOptional;
    if (n1.present()) {
      final NodeChoice n1Ch = (NodeChoice) n1.node;
      final INode n1ChN = n1Ch.choice;
      switch (n1Ch.which) {
        case 0:
          n1ChN.accept(this);
          break;
        case 1:
          n1ChN.accept(this);
          break;
        default:
          // should not occur !!!
          break;
      }
    }
  }

  /**
   * Visits a {@link SiteSelector_postFix} node, whose children are the following :
   * <p>
   * nodeToken -> < LBRACE ><br>
   * nodeChoice -> ( %0 Name()<br>
   * .......... .. | %1 Literal() )<br>
   * nodeOptional -> ( %0 ( #0 < COMMA ><br>
   * ............ .. . #1 ( &0 Name()<br>
   * ............ .. . .. | &1 Literal() ) )+<br>
   * ............ .. | %1 ( #0 < COLON ><br>
   * ............ .. . .. . #1 ( &0 Name()<br>
   * ............ .. . .. . .. | &1 Literal() ) ) )?<br>
   * nodeToken1 -> < RBRACE ><br>
   *
   * @param n the node to visit
   */
  public void visit(final SiteSelector_postFix n) {
    final NodeToken n0 = n.nodeToken;
    n0.accept(this);
    final NodeChoice n1 = n.nodeChoice;
    final INode n1N = n1.choice;
    switch (n1.which) {
      case 0:
        n1N.accept(this);
        break;
      case 1:
        n1N.accept(this);
        break;
      default:
        // should not occur !!!
        break;
    }
    final NodeOptional n2 = n.nodeOptional;
    if (n2.present()) {
      final NodeChoice n2Ch = (NodeChoice) n2.node;
      final INode n2ChN = n2Ch.choice;
      switch (n2Ch.which) {
        case 0:
          final NodeList n2ChNLst = (NodeList) n2ChN;
          for (int i = 0; i < n2ChNLst.size(); i++) {
            final INode n2ChNLstEi = n2ChNLst.elementAt(i);
            final NodeSequence n2ChNLstEiSeq = (NodeSequence) n2ChNLstEi;
            final INode n2ChNLstEiSeqA0 = n2ChNLstEiSeq.elementAt(0);
            n2ChNLstEiSeqA0.accept(this);
            final INode n2ChNLstEiSeqA1 = n2ChNLstEiSeq.elementAt(1);
            final NodeChoice n2ChNLstEiSeqA1Ch = (NodeChoice) n2ChNLstEiSeqA1;
            final INode n2ChNLstEiSeqA1ChN = n2ChNLstEiSeqA1Ch.choice;
            switch (n2ChNLstEiSeqA1Ch.which) {
              case 0:
                n2ChNLstEiSeqA1ChN.accept(this);
                break;
              case 1:
                n2ChNLstEiSeqA1ChN.accept(this);
                break;
              default:
                // should not occur !!!
                break;
            }
          }
          break;
        case 1:
          final NodeSequence n2ChNSeq = (NodeSequence) n2ChN;
          final INode n2ChNSeqA0 = n2ChNSeq.elementAt(0);
          n2ChNSeqA0.accept(this);
          final INode n2ChNSeqA1 = n2ChNSeq.elementAt(1);
          final NodeChoice n2ChNSeqA1Ch = (NodeChoice) n2ChNSeqA1;
          final INode n2ChNSeqA1ChN = n2ChNSeqA1Ch.choice;
          switch (n2ChNSeqA1Ch.which) {
            case 0:
              n2ChNSeqA1ChN.accept(this);
              break;
            case 1:
              n2ChNSeqA1ChN.accept(this);
              break;
            default:
              // should not occur !!!
              break;
          }
          break;
        default:
          // should not occur !!!
          break;
      }
    }
    final NodeToken n3 = n.nodeToken1;
    n3.accept(this);
  }

  /**
   * Visits a {@link CoeffFunction_postFix} node, whose children are the following :
   * <p>
   * nodeToken -> < LPAREN ><br>
   * nodeOptional -> [ ArgumentList() ]<br>
   * nodeToken1 -> < RPAREN ><br>
   *
   * @param n the node to visit
   */
  public void visit(final CoeffFunction_postFix n) {
    final NodeToken n0 = n.nodeToken;
    n0.accept(this);
    final NodeOptional n1 = n.nodeOptional;
    if (n1.present()) {
      n1.accept(this);
    }
    final NodeToken n2 = n.nodeToken1;
    n2.accept(this);
  }

  /**
   * Visits a {@link MultistateSite} node, whose children are the following :
   * <p>
   * name -> Name()<br>
   * nodeToken -> < LBRACE ><br>
   * nodeChoice -> ( %0 Name()<br>
   * .......... .. | %1 Literal() )<br>
   * nodeToken1 -> < RBRACE ><br>
   *
   * @param n the node to visit
   */
  public void visit(final MultistateSite n) {
    final Name n0 = n.name;
    n0.accept(this);
    final NodeToken n1 = n.nodeToken;
    n1.accept(this);
    final NodeChoice n2 = n.nodeChoice;
    final INode n2N = n2.choice;
    switch (n2.which) {
      case 0:
        n2N.accept(this);
        break;
      case 1:
        n2N.accept(this);
        break;
      default:
        // should not occur !!!
        break;
    }
    final NodeToken n3 = n.nodeToken1;
    n3.accept(this);
  }

  /**
   * Visits a {@link MultistateSites_list} node, whose children are the following :
   * <p>
   * multistateSite -> MultistateSite()<br>
   * nodeListOptional -> ( #0 < SEMICOLON > #1 MultistateSite() )*<br>
   *
   * @param n the node to visit
   */
  public void visit(final MultistateSites_list n) {
    final MultistateSite n0 = n.multistateSite;
    n0.accept(this);
    final NodeListOptional n1 = n.nodeListOptional;
    if (n1.present()) {
      for (int i = 0; i < n1.size(); i++) {
        final INode n1Ei = n1.elementAt(i);
        final NodeSequence n1EiSeq = (NodeSequence) n1Ei;
        final INode n1EiSeqA0 = n1EiSeq.elementAt(0);
        n1EiSeqA0.accept(this);
        final INode n1EiSeqA1 = n1EiSeq.elementAt(1);
        n1EiSeqA1.accept(this);
      }
    }
  }

}
