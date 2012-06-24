/* Generated by JTB 1.4.4 */
package parsers.mathExpression.visitor;

import parsers.mathExpression.syntaxtree.*;
import java.util.*;

/**
 * Provides default methods which visit each node in the tree in depth-first order.<br>
 * In your "VoidArgu" visitors extend this class and override part or all of these methods.
 *
 * @param <A> The user argument type
 */
public class DepthFirstVoidArguVisitor<A> implements IVoidArguVisitor<A> {


  /*
   * Base nodes classes visit methods (to be overridden if necessary)
   */

  /**
   * Visits a {@link NodeChoice} node, passing it an argument.
   *
   * @param n the node to visit
   * @param argu the user argument
   */
  public void visit(final NodeChoice n, final A argu) {
    n.choice.accept(this, argu);
    return;
  }

  /**
   * Visits a {@link NodeList} node, passing it an argument.
   *
   * @param n the node to visit
   * @param argu the user argument
   */
  public void visit(final NodeList n, final A argu) {
    for (final Iterator<INode> e = n.elements(); e.hasNext();) {
      e.next().accept(this, argu);
    }
    return;
  }

  /**
   * Visits a {@link NodeListOptional} node, passing it an argument.
   *
   * @param n the node to visit
   * @param argu the user argument
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
   * Visits a {@link NodeOptional} node, passing it an argument.
   *
   * @param n the node to visit
   * @param argu the user argument
   */
  public void visit(final NodeOptional n, final A argu) {
    if (n.present()) {
      n.node.accept(this, argu);
      return;
    } else
    return;
  }

  /**
   * Visits a {@link NodeSequence} node, passing it an argument.
   *
   * @param n the node to visit
   * @param argu the user argument
   */
  public void visit(final NodeSequence n, final A argu) {
    for (final Iterator<INode> e = n.elements(); e.hasNext();) {
      e.next().accept(this, argu);
    }
    return;
  }

  /**
   * Visits a {@link NodeToken} node, passing it an argument.
   *
   * @param n the node to visit
   * @param argu the user argument
   */
  public void visit(final NodeToken n, @SuppressWarnings("unused") final A argu) {
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
   * @param argu the user argument
   */
  public void visit(final CompleteExpression n, final A argu) {
    // expression -> Expression()
    n.expression.accept(this, argu);
    // nodeToken -> < EOF >
    n.nodeToken.accept(this, argu);
  }

  /**
   * Visits a {@link SingleFunctionCall} node, whose children are the following :
   * <p>
   * name -> Name()<br>
   * nodeToken -> < LPAREN ><br>
   * argumentList -> ArgumentList()<br>
   * nodeToken1 -> < RPAREN ><br>
   * nodeToken2 -> < EOF ><br>
   *
   * @param n the node to visit
   * @param argu the user argument
   */
  public void visit(final SingleFunctionCall n, final A argu) {
    // name -> Name()
    n.name.accept(this, argu);
    // nodeToken -> < LPAREN >
    n.nodeToken.accept(this, argu);
    // argumentList -> ArgumentList()
    n.argumentList.accept(this, argu);
    // nodeToken1 -> < RPAREN >
    n.nodeToken1.accept(this, argu);
    // nodeToken2 -> < EOF >
    n.nodeToken2.accept(this, argu);
  }

  /**
   * Visits a {@link CompleteFunctionDeclaration} node, whose children are the following :
   * <p>
   * functionDeclarator -> FunctionDeclarator()<br>
   * nodeToken -> < EOF ><br>
   *
   * @param n the node to visit
   * @param argu the user argument
   */
  public void visit(final CompleteFunctionDeclaration n, final A argu) {
    // functionDeclarator -> FunctionDeclarator()
    n.functionDeclarator.accept(this, argu);
    // nodeToken -> < EOF >
    n.nodeToken.accept(this, argu);
  }

  /**
   * Visits a {@link FunctionDeclarator} node, whose children are the following :
   * <p>
   * nodeToken -> < IDENTIFIER ><br>
   * nodeOptional -> ( FormalParameters() )?<br>
   *
   * @param n the node to visit
   * @param argu the user argument
   */
  public void visit(final FunctionDeclarator n, final A argu) {
    // nodeToken -> < IDENTIFIER >
    n.nodeToken.accept(this, argu);
    // nodeOptional -> ( FormalParameters() )?
    n.nodeOptional.accept(this, argu);
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
   * @param argu the user argument
   */
  public void visit(final FormalParameters n, final A argu) {
    // nodeToken -> "("
    n.nodeToken.accept(this, argu);
    // nodeOptional -> [ #0 FormalParameter()
    // ............ .. . #1 ( $0 "," $1 FormalParameter() )* ]
    n.nodeOptional.accept(this, argu);
    // nodeToken1 -> ")"
    n.nodeToken1.accept(this, argu);
  }

  /**
   * Visits a {@link FormalParameter} node, whose children are the following :
   * <p>
   * primitiveType -> PrimitiveType()<br>
   * variableDeclaratorId -> VariableDeclaratorId()<br>
   *
   * @param n the node to visit
   * @param argu the user argument
   */
  public void visit(final FormalParameter n, final A argu) {
    // primitiveType -> PrimitiveType()
    n.primitiveType.accept(this, argu);
    // variableDeclaratorId -> VariableDeclaratorId()
    n.variableDeclaratorId.accept(this, argu);
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
   *
   * @param n the node to visit
   * @param argu the user argument
   */
  public void visit(final PrimitiveType n, final A argu) {
    // nodeChoice -> . %0 < TYPE_PAR >
    // .......... .. | %1 < TYPE_VAR >
    // .......... .. | %2 < TYPE_SUB >
    // .......... .. | %3 < TYPE_PROD >
    // .......... .. | %4 < TYPE_MOD >
    // .......... .. | %5 < TYPE_SITE >
    // .......... .. | %6 < TYPE_VOL >
    n.nodeChoice.accept(this, argu);
  }

  /**
   * Visits a {@link VariableDeclaratorId} node, whose children are the following :
   * <p>
   * nodeToken -> < IDENTIFIER ><br>
   *
   * @param n the node to visit
   * @param argu the user argument
   */
  public void visit(final VariableDeclaratorId n, final A argu) {
    // nodeToken -> < IDENTIFIER >
    n.nodeToken.accept(this, argu);
  }

  /**
   * Visits a {@link Expression} node, whose children are the following :
   * <p>
   * additiveExpression -> AdditiveExpression()<br>
   * nodeOptional -> ( #0 RelationalOperator() #1 Expression()<br>
   * ............ .. . #2 ( $0 LogicalOperator() $1 Expression() )* )?<br>
   *
   * @param n the node to visit
   * @param argu the user argument
   */
  public void visit(final Expression n, final A argu) {
    // additiveExpression -> AdditiveExpression()
    n.additiveExpression.accept(this, argu);
    // nodeOptional -> ( #0 RelationalOperator() #1 Expression()
    // ............ .. . #2 ( $0 LogicalOperator() $1 Expression() )* )?
    n.nodeOptional.accept(this, argu);
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
   * @param argu the user argument
   */
  public void visit(final RelationalOperator n, final A argu) {
    // nodeChoice -> . %0 ( &0 < ASSIGN >
    // .......... .. . .. | &1 < EQ > )
    // .......... .. | %1 < LT >
    // .......... .. | %2 < GT >
    // .......... .. | %3 < GEQ >
    // .......... .. | %4 < LEQ >
    n.nodeChoice.accept(this, argu);
  }

  /**
   * Visits a {@link LogicalOperator} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 < AND ><br>
   * .......... .. | %1 < OR ><br>
   *
   * @param n the node to visit
   * @param argu the user argument
   */
  public void visit(final LogicalOperator n, final A argu) {
    // nodeChoice -> . %0 < AND >
    // .......... .. | %1 < OR >
    n.nodeChoice.accept(this, argu);
  }

  /**
   * Visits a {@link AdditiveExpression} node, whose children are the following :
   * <p>
   * multiplicativeExpression -> MultiplicativeExpression()<br>
   * nodeListOptional -> ( #0 ( %0 < PLUS ><br>
   * ................ .. . .. | %1 < MINUS > ) #1 MultiplicativeExpression() )*<br>
   *
   * @param n the node to visit
   * @param argu the user argument
   */
  public void visit(final AdditiveExpression n, final A argu) {
    // multiplicativeExpression -> MultiplicativeExpression()
    n.multiplicativeExpression.accept(this, argu);
    // nodeListOptional -> ( #0 ( %0 < PLUS >
    // ................ .. . .. | %1 < MINUS > ) #1 MultiplicativeExpression() )*
    n.nodeListOptional.accept(this, argu);
  }

  /**
   * Visits a {@link MultiplicativeExpression} node, whose children are the following :
   * <p>
   * powerExpression -> PowerExpression()<br>
   * nodeListOptional -> ( #0 ( %0 < TIMES ><br>
   * ................ .. . .. | %1 < DIV > ) #1 PowerExpression() )*<br>
   *
   * @param n the node to visit
   * @param argu the user argument
   */
  public void visit(final MultiplicativeExpression n, final A argu) {
    // powerExpression -> PowerExpression()
    n.powerExpression.accept(this, argu);
    // nodeListOptional -> ( #0 ( %0 < TIMES >
    // ................ .. . .. | %1 < DIV > ) #1 PowerExpression() )*
    n.nodeListOptional.accept(this, argu);
  }

  /**
   * Visits a {@link PowerExpression} node, whose children are the following :
   * <p>
   * unaryExpression -> UnaryExpression()<br>
   * nodeListOptional -> ( #0 ( < CARET > ) #1 UnaryExpression() )*<br>
   *
   * @param n the node to visit
   * @param argu the user argument
   */
  public void visit(final PowerExpression n, final A argu) {
    // unaryExpression -> UnaryExpression()
    n.unaryExpression.accept(this, argu);
    // nodeListOptional -> ( #0 ( < CARET > ) #1 UnaryExpression() )*
    n.nodeListOptional.accept(this, argu);
  }

  /**
   * Visits a {@link UnaryExpression} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 #0 ( &0 < PLUS ><br>
   * .......... .. . .. .. | &1 < MINUS > ) #1 UnaryExpression()<br>
   * .......... .. | %1 UnaryExpressionNotPlusMinus()<br>
   *
   * @param n the node to visit
   * @param argu the user argument
   */
  public void visit(final UnaryExpression n, final A argu) {
    // nodeChoice -> . %0 #0 ( &0 < PLUS >
    // .......... .. . .. .. | &1 < MINUS > ) #1 UnaryExpression()
    // .......... .. | %1 UnaryExpressionNotPlusMinus()
    n.nodeChoice.accept(this, argu);
  }

  /**
   * Visits a {@link UnaryExpressionNotPlusMinus} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 #0 < BANG > #1 UnaryExpression()<br>
   * .......... .. | %1 PrimaryExpression()<br>
   *
   * @param n the node to visit
   * @param argu the user argument
   */
  public void visit(final UnaryExpressionNotPlusMinus n, final A argu) {
    // nodeChoice -> . %0 #0 < BANG > #1 UnaryExpression()
    // .......... .. | %1 PrimaryExpression()
    n.nodeChoice.accept(this, argu);
  }

  /**
   * Visits a {@link PrimaryExpression} node, whose children are the following :
   * <p>
   * primaryPrefix -> PrimaryPrefix()<br>
   *
   * @param n the node to visit
   * @param argu the user argument
   */
  public void visit(final PrimaryExpression n, final A argu) {
    // primaryPrefix -> PrimaryPrefix()
    n.primaryPrefix.accept(this, argu);
  }

  /**
   * Visits a {@link PrimaryPrefix} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 Literal()<br>
   * .......... .. | %1 #0 < LPAREN > #1 Expression() #2 < RPAREN ><br>
   * .......... .. | %2 SpeciesReferenceOrFunctionCall()<br>
   * .......... .. | %3 MultistateSum()<br>
   *
   * @param n the node to visit
   * @param argu the user argument
   */
  public void visit(final PrimaryPrefix n, final A argu) {
    // nodeChoice -> . %0 Literal()
    // .......... .. | %1 #0 < LPAREN > #1 Expression() #2 < RPAREN >
    // .......... .. | %2 SpeciesReferenceOrFunctionCall()
    // .......... .. | %3 MultistateSum()
    n.nodeChoice.accept(this, argu);
  }

  /**
   * Visits a {@link MultistateSum} node, whose children are the following :
   * <p>
   * nodeToken -> < SUM ><br>
   * nodeToken1 -> < LPAREN ><br>
   * argumentList -> ArgumentList()<br>
   * nodeToken2 -> < RPAREN ><br>
   *
   * @param n the node to visit
   * @param argu the user argument
   */
  public void visit(final MultistateSum n, final A argu) {
    // nodeToken -> < SUM >
    n.nodeToken.accept(this, argu);
    // nodeToken1 -> < LPAREN >
    n.nodeToken1.accept(this, argu);
    // argumentList -> ArgumentList()
    n.argumentList.accept(this, argu);
    // nodeToken2 -> < RPAREN >
    n.nodeToken2.accept(this, argu);
  }

  /**
   * Visits a {@link Name} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 #0 < IDENTIFIER ><br>
   * .......... .. . .. #1 ( PossibleExtensions() )?<br>
   * .......... .. | %1 < TIME ><br>
   * .......... .. | %2 < FLOOR ><br>
   * .......... .. | %3 < LOG ><br>
   * .......... .. | %4 < EXP ><br>
   * .......... .. | %5 < NAN ><br>
   *
   * @param n the node to visit
   * @param argu the user argument
   */
  public void visit(final Name n, final A argu) {
    // nodeChoice -> . %0 #0 < IDENTIFIER >
    // .......... .. . .. #1 ( PossibleExtensions() )?
    // .......... .. | %1 < TIME >
    // .......... .. | %2 < FLOOR >
    // .......... .. | %3 < LOG >
    // .......... .. | %4 < EXP >
    // .......... .. | %5 < NAN >
    n.nodeChoice.accept(this, argu);
  }

  /**
   * Visits a {@link SpeciesReferenceOrFunctionCall_prefix} node, whose children are the following :
   * <p>
   * name -> Name()<br>
   * nodeOptional -> [ #0 < LPAREN ><br>
   * ............ .. . #1 [ ArgumentList() ] #2 < RPAREN > ]<br>
   *
   * @param n the node to visit
   * @param argu the user argument
   */
  public void visit(final SpeciesReferenceOrFunctionCall_prefix n, final A argu) {
    // name -> Name()
    n.name.accept(this, argu);
    // nodeOptional -> [ #0 < LPAREN >
    // ............ .. . #1 [ ArgumentList() ] #2 < RPAREN > ]
    n.nodeOptional.accept(this, argu);
  }

  /**
   * Visits a {@link SpeciesReferenceOrFunctionCall} node, whose children are the following :
   * <p>
   * speciesReferenceOrFunctionCall_prefix -> SpeciesReferenceOrFunctionCall_prefix()<br>
   * nodeListOptional -> ( ( PossibleExtensions() ) )*<br>
   *
   * @param n the node to visit
   * @param argu the user argument
   */
  public void visit(final SpeciesReferenceOrFunctionCall n, final A argu) {
    // speciesReferenceOrFunctionCall_prefix -> SpeciesReferenceOrFunctionCall_prefix()
    n.speciesReferenceOrFunctionCall_prefix.accept(this, argu);
    // nodeListOptional -> ( ( PossibleExtensions() ) )*
    n.nodeListOptional.accept(this, argu);
  }

  /**
   * Visits a {@link PossibleExtensions} node, whose children are the following :
   * <p>
   * nodeChoice -> ( %0 < EXTENSION_CONC ><br>
   * .......... .. | %1 < EXTENSION_COMPARTMENT ><br>
   * .......... .. | %2 < EXTENSION_PARTICLE ><br>
   * .......... .. | %3 < EXTENSION_TRANS ><br>
   * .......... .. | %4 < EXTENSION_INIT ><br>
   * .......... .. | %5 < EXTENSION_RATE ><br>
   * .......... .. | %6 < EXTENSION_SPECIES ><br>
   * .......... .. | %7 < EXTENSION_GLOBALQ ><br>
   * .......... .. | %8 < MY_SPECIAL_EXTENSION > )<br>
   * nodeListOptional -> ( PossibleExtensions() )*<br>
   *
   * @param n the node to visit
   * @param argu the user argument
   */
  public void visit(final PossibleExtensions n, final A argu) {
    // nodeChoice -> ( %0 < EXTENSION_CONC >
    // .......... .. | %1 < EXTENSION_COMPARTMENT >
    // .......... .. | %2 < EXTENSION_PARTICLE >
    // .......... .. | %3 < EXTENSION_TRANS >
    // .......... .. | %4 < EXTENSION_INIT >
    // .......... .. | %5 < EXTENSION_RATE >
    // .......... .. | %6 < EXTENSION_SPECIES >
    // .......... .. | %7 < EXTENSION_GLOBALQ >
    // .......... .. | %8 < MY_SPECIAL_EXTENSION > )
    n.nodeChoice.accept(this, argu);
    // nodeListOptional -> ( PossibleExtensions() )*
    n.nodeListOptional.accept(this, argu);
  }

  /**
   * Visits a {@link Literal} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 < INTEGER_LITERAL ><br>
   * .......... .. | %1 < FLOATING_POINT_LITERAL ><br>
   * .......... .. | %2 BooleanLiteral()<br>
   *
   * @param n the node to visit
   * @param argu the user argument
   */
  public void visit(final Literal n, final A argu) {
    // nodeChoice -> . %0 < INTEGER_LITERAL >
    // .......... .. | %1 < FLOATING_POINT_LITERAL >
    // .......... .. | %2 BooleanLiteral()
    n.nodeChoice.accept(this, argu);
  }

  /**
   * Visits a {@link BooleanLiteral} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 < TRUE ><br>
   * .......... .. | %1 < FALSE ><br>
   *
   * @param n the node to visit
   * @param argu the user argument
   */
  public void visit(final BooleanLiteral n, final A argu) {
    // nodeChoice -> . %0 < TRUE >
    // .......... .. | %1 < FALSE >
    n.nodeChoice.accept(this, argu);
  }

  /**
   * Visits a {@link ArgumentList} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 MultistateSites_list()<br>
   * .......... .. | %1 #0 AdditiveExpression()<br>
   * .......... .. . .. #1 ( $0 < COMMA > $1 AdditiveExpression() )*<br>
   *
   * @param n the node to visit
   * @param argu the user argument
   */
  public void visit(final ArgumentList n, final A argu) {
    // nodeChoice -> . %0 MultistateSites_list()
    // .......... .. | %1 #0 AdditiveExpression()
    // .......... .. . .. #1 ( $0 < COMMA > $1 AdditiveExpression() )*
    n.nodeChoice.accept(this, argu);
  }

  /**
   * Visits a {@link MultistateSite} node, whose children are the following :
   * <p>
   * name -> Name()<br>
   * nodeToken -> < LBRACE ><br>
   * name1 -> Name()<br>
   * nodeToken1 -> < RBRACE ><br>
   *
   * @param n the node to visit
   * @param argu the user argument
   */
  public void visit(final MultistateSite n, final A argu) {
    // name -> Name()
    n.name.accept(this, argu);
    // nodeToken -> < LBRACE >
    n.nodeToken.accept(this, argu);
    // name1 -> Name()
    n.name1.accept(this, argu);
    // nodeToken1 -> < RBRACE >
    n.nodeToken1.accept(this, argu);
  }

  /**
   * Visits a {@link MultistateSites_list} node, whose children are the following :
   * <p>
   * multistateSite -> MultistateSite()<br>
   * nodeListOptional -> ( #0 < SEMICOLON > #1 MultistateSite() )*<br>
   *
   * @param n the node to visit
   * @param argu the user argument
   */
  public void visit(final MultistateSites_list n, final A argu) {
    // multistateSite -> MultistateSite()
    n.multistateSite.accept(this, argu);
    // nodeListOptional -> ( #0 < SEMICOLON > #1 MultistateSite() )*
    n.nodeListOptional.accept(this, argu);
  }

}
