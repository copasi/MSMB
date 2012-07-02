/* Generated by JTB 1.4.4 */
package parsers.mathExpression.visitor;

import parsers.mathExpression.syntaxtree.*;
import java.util.*;

/**
 * Provides default methods which visit each node in the tree in depth-first order.<br>
 * In your "Ret" visitors extend this class and override part or all of these methods.
 *
 * @param <R> The user return information type
 */
public class DepthFirstRetVisitor<R> implements IRetVisitor<R> {


  /*
   * Base nodes classes visit methods (to be overridden if necessary)
   */

  /**
   * Visits a {@link NodeChoice} node.
   *
   * @param n the node to visit
   * @return the user return information
   */
  public R visit(final NodeChoice n) {
    final R nRes = n.choice.accept(this);
    return nRes;
  }

  /**
   * Visits a {@link NodeList} node.
   *
   * @param n the node to visit
   * @return the user return information
   */
  public R visit(final NodeList n) {
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
   * @param n the node to visit
   * @return the user return information
   */
  public R visit(final NodeListOptional n) {
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
   * @param n the node to visit
   * @return the user return information
   */
  public R visit(final NodeOptional n) {
    if (n.present()) {
      final R nRes = n.node.accept(this);
      return nRes;
    } else
    return null;
  }

  /**
   * Visits a {@link NodeSequence} node.
   *
   * @param n the node to visit
   * @return the user return information
   */
  public R visit(final NodeSequence n) {
    R nRes = null;
    for (final Iterator<INode> e = n.elements(); e.hasNext();) {
      @SuppressWarnings("unused")
      R subRet = e.next().accept(this);
    }
    return nRes;
  }

  /**
   * Visits a {@link NodeToken} node.
   *
   * @param n the node to visit
   * @return the user return information
   */
  public R visit(final NodeToken n) {
    R nRes = null;
    @SuppressWarnings("unused")
    final String tkIm = n.tokenImage;
    return nRes;
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
   * @return the user return information
   */
  public R visit(final CompleteExpression n) {
    R nRes = null;
    // expression -> Expression()
    n.expression.accept(this);
    // nodeToken -> < EOF >
    n.nodeToken.accept(this);
    return nRes;
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
   * @return the user return information
   */
  public R visit(final SingleFunctionCall n) {
    R nRes = null;
    // name -> Name()
    n.name.accept(this);
    // nodeToken -> < LPAREN >
    n.nodeToken.accept(this);
    // argumentList -> ArgumentList()
    n.argumentList.accept(this);
    // nodeToken1 -> < RPAREN >
    n.nodeToken1.accept(this);
    // nodeToken2 -> < EOF >
    n.nodeToken2.accept(this);
    return nRes;
  }

  /**
   * Visits a {@link CompleteFunctionDeclaration} node, whose children are the following :
   * <p>
   * functionDeclarator -> FunctionDeclarator()<br>
   * nodeToken -> < EOF ><br>
   *
   * @param n the node to visit
   * @return the user return information
   */
  public R visit(final CompleteFunctionDeclaration n) {
    R nRes = null;
    // functionDeclarator -> FunctionDeclarator()
    n.functionDeclarator.accept(this);
    // nodeToken -> < EOF >
    n.nodeToken.accept(this);
    return nRes;
  }

  /**
   * Visits a {@link FunctionDeclarator} node, whose children are the following :
   * <p>
   * nodeToken -> < IDENTIFIER ><br>
   * nodeOptional -> ( FormalParameters() )?<br>
   *
   * @param n the node to visit
   * @return the user return information
   */
  public R visit(final FunctionDeclarator n) {
    R nRes = null;
    // nodeToken -> < IDENTIFIER >
    n.nodeToken.accept(this);
    // nodeOptional -> ( FormalParameters() )?
    n.nodeOptional.accept(this);
    return nRes;
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
   * @return the user return information
   */
  public R visit(final FormalParameters n) {
    R nRes = null;
    // nodeToken -> "("
    n.nodeToken.accept(this);
    // nodeOptional -> [ #0 FormalParameter()
    // ............ .. . #1 ( $0 "," $1 FormalParameter() )* ]
    n.nodeOptional.accept(this);
    // nodeToken1 -> ")"
    n.nodeToken1.accept(this);
    return nRes;
  }

  /**
   * Visits a {@link FormalParameter} node, whose children are the following :
   * <p>
   * primitiveType -> PrimitiveType()<br>
   * variableDeclaratorId -> VariableDeclaratorId()<br>
   *
   * @param n the node to visit
   * @return the user return information
   */
  public R visit(final FormalParameter n) {
    R nRes = null;
    // primitiveType -> PrimitiveType()
    n.primitiveType.accept(this);
    // variableDeclaratorId -> VariableDeclaratorId()
    n.variableDeclaratorId.accept(this);
    return nRes;
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
   * @return the user return information
   */
  public R visit(final PrimitiveType n) {
    R nRes = null;
    // nodeChoice -> . %0 < TYPE_PAR >
    // .......... .. | %1 < TYPE_VAR >
    // .......... .. | %2 < TYPE_SUB >
    // .......... .. | %3 < TYPE_PROD >
    // .......... .. | %4 < TYPE_MOD >
    // .......... .. | %5 < TYPE_SITE >
    // .......... .. | %6 < TYPE_VOL >
    n.nodeChoice.accept(this);
    return nRes;
  }

  /**
   * Visits a {@link VariableDeclaratorId} node, whose children are the following :
   * <p>
   * nodeToken -> < IDENTIFIER ><br>
   *
   * @param n the node to visit
   * @return the user return information
   */
  public R visit(final VariableDeclaratorId n) {
    R nRes = null;
    // nodeToken -> < IDENTIFIER >
    n.nodeToken.accept(this);
    return nRes;
  }

  /**
   * Visits a {@link Expression} node, whose children are the following :
   * <p>
   * additiveExpression -> AdditiveExpression()<br>
   * nodeOptional -> ( #0 RelationalOperator() #1 Expression()<br>
   * ............ .. . #2 ( $0 LogicalOperator() $1 Expression() )* )?<br>
   *
   * @param n the node to visit
   * @return the user return information
   */
  public R visit(final Expression n) {
    R nRes = null;
    // additiveExpression -> AdditiveExpression()
    n.additiveExpression.accept(this);
    // nodeOptional -> ( #0 RelationalOperator() #1 Expression()
    // ............ .. . #2 ( $0 LogicalOperator() $1 Expression() )* )?
    n.nodeOptional.accept(this);
    return nRes;
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
   * @return the user return information
   */
  public R visit(final RelationalOperator n) {
    R nRes = null;
    // nodeChoice -> . %0 ( &0 < ASSIGN >
    // .......... .. . .. | &1 < EQ > )
    // .......... .. | %1 < LT >
    // .......... .. | %2 < GT >
    // .......... .. | %3 < GEQ >
    // .......... .. | %4 < LEQ >
    n.nodeChoice.accept(this);
    return nRes;
  }

  /**
   * Visits a {@link LogicalOperator} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 < AND ><br>
   * .......... .. | %1 < OR ><br>
   *
   * @param n the node to visit
   * @return the user return information
   */
  public R visit(final LogicalOperator n) {
    R nRes = null;
    // nodeChoice -> . %0 < AND >
    // .......... .. | %1 < OR >
    n.nodeChoice.accept(this);
    return nRes;
  }

  /**
   * Visits a {@link AdditiveExpression} node, whose children are the following :
   * <p>
   * multiplicativeExpression -> MultiplicativeExpression()<br>
   * nodeListOptional -> ( #0 ( %0 < PLUS ><br>
   * ................ .. . .. | %1 < MINUS > ) #1 MultiplicativeExpression() )*<br>
   *
   * @param n the node to visit
   * @return the user return information
   */
  public R visit(final AdditiveExpression n) {
    R nRes = null;
    // multiplicativeExpression -> MultiplicativeExpression()
    n.multiplicativeExpression.accept(this);
    // nodeListOptional -> ( #0 ( %0 < PLUS >
    // ................ .. . .. | %1 < MINUS > ) #1 MultiplicativeExpression() )*
    n.nodeListOptional.accept(this);
    return nRes;
  }

  /**
   * Visits a {@link MultiplicativeExpression} node, whose children are the following :
   * <p>
   * powerExpression -> PowerExpression()<br>
   * nodeListOptional -> ( #0 ( %0 < TIMES ><br>
   * ................ .. . .. | %1 < DIV > ) #1 PowerExpression() )*<br>
   *
   * @param n the node to visit
   * @return the user return information
   */
  public R visit(final MultiplicativeExpression n) {
    R nRes = null;
    // powerExpression -> PowerExpression()
    n.powerExpression.accept(this);
    // nodeListOptional -> ( #0 ( %0 < TIMES >
    // ................ .. . .. | %1 < DIV > ) #1 PowerExpression() )*
    n.nodeListOptional.accept(this);
    return nRes;
  }

  /**
   * Visits a {@link PowerExpression} node, whose children are the following :
   * <p>
   * unaryExpression -> UnaryExpression()<br>
   * nodeListOptional -> ( #0 ( < CARET > ) #1 UnaryExpression() )*<br>
   *
   * @param n the node to visit
   * @return the user return information
   */
  public R visit(final PowerExpression n) {
    R nRes = null;
    // unaryExpression -> UnaryExpression()
    n.unaryExpression.accept(this);
    // nodeListOptional -> ( #0 ( < CARET > ) #1 UnaryExpression() )*
    n.nodeListOptional.accept(this);
    return nRes;
  }

  /**
   * Visits a {@link UnaryExpression} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 #0 ( &0 < PLUS ><br>
   * .......... .. . .. .. | &1 < MINUS > ) #1 UnaryExpression()<br>
   * .......... .. | %1 UnaryExpressionNotPlusMinus()<br>
   *
   * @param n the node to visit
   * @return the user return information
   */
  public R visit(final UnaryExpression n) {
    R nRes = null;
    // nodeChoice -> . %0 #0 ( &0 < PLUS >
    // .......... .. . .. .. | &1 < MINUS > ) #1 UnaryExpression()
    // .......... .. | %1 UnaryExpressionNotPlusMinus()
    n.nodeChoice.accept(this);
    return nRes;
  }

  /**
   * Visits a {@link UnaryExpressionNotPlusMinus} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 #0 < BANG > #1 UnaryExpression()<br>
   * .......... .. | %1 PrimaryExpression()<br>
   *
   * @param n the node to visit
   * @return the user return information
   */
  public R visit(final UnaryExpressionNotPlusMinus n) {
    R nRes = null;
    // nodeChoice -> . %0 #0 < BANG > #1 UnaryExpression()
    // .......... .. | %1 PrimaryExpression()
    n.nodeChoice.accept(this);
    return nRes;
  }

  /**
   * Visits a {@link PrimaryExpression} node, whose children are the following :
   * <p>
   * primaryPrefix -> PrimaryPrefix()<br>
   *
   * @param n the node to visit
   * @return the user return information
   */
  public R visit(final PrimaryExpression n) {
    R nRes = null;
    // primaryPrefix -> PrimaryPrefix()
    n.primaryPrefix.accept(this);
    return nRes;
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
   * @return the user return information
   */
  public R visit(final PrimaryPrefix n) {
    R nRes = null;
    // nodeChoice -> . %0 Literal()
    // .......... .. | %1 #0 < LPAREN > #1 Expression() #2 < RPAREN >
    // .......... .. | %2 SpeciesReferenceOrFunctionCall()
    // .......... .. | %3 MultistateSum()
    n.nodeChoice.accept(this);
    return nRes;
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
   * @return the user return information
   */
  public R visit(final MultistateSum n) {
    R nRes = null;
    // nodeToken -> < SUM >
    n.nodeToken.accept(this);
    // nodeToken1 -> < LPAREN >
    n.nodeToken1.accept(this);
    // argumentList -> ArgumentList()
    n.argumentList.accept(this);
    // nodeToken2 -> < RPAREN >
    n.nodeToken2.accept(this);
    return nRes;
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
   * @return the user return information
   */
  public R visit(final Name n) {
    R nRes = null;
    // nodeChoice -> . %0 #0 < IDENTIFIER >
    // .......... .. . .. #1 ( PossibleExtensions() )?
    // .......... .. | %1 < TIME >
    // .......... .. | %2 < FLOOR >
    // .......... .. | %3 < LOG >
    // .......... .. | %4 < EXP >
    // .......... .. | %5 < NAN >
    n.nodeChoice.accept(this);
    return nRes;
  }

  /**
   * Visits a {@link SpeciesReferenceOrFunctionCall_prefix} node, whose children are the following :
   * <p>
   * name -> Name()<br>
   * nodeOptional -> [ #0 < LPAREN ><br>
   * ............ .. . #1 [ ArgumentList() ] #2 < RPAREN > ]<br>
   *
   * @param n the node to visit
   * @return the user return information
   */
  public R visit(final SpeciesReferenceOrFunctionCall_prefix n) {
    R nRes = null;
    // name -> Name()
    n.name.accept(this);
    // nodeOptional -> [ #0 < LPAREN >
    // ............ .. . #1 [ ArgumentList() ] #2 < RPAREN > ]
    n.nodeOptional.accept(this);
    return nRes;
  }

  /**
   * Visits a {@link SpeciesReferenceOrFunctionCall} node, whose children are the following :
   * <p>
   * speciesReferenceOrFunctionCall_prefix -> SpeciesReferenceOrFunctionCall_prefix()<br>
   * nodeListOptional -> ( ( PossibleExtensions() ) )*<br>
   *
   * @param n the node to visit
   * @return the user return information
   */
  public R visit(final SpeciesReferenceOrFunctionCall n) {
    R nRes = null;
    // speciesReferenceOrFunctionCall_prefix -> SpeciesReferenceOrFunctionCall_prefix()
    n.speciesReferenceOrFunctionCall_prefix.accept(this);
    // nodeListOptional -> ( ( PossibleExtensions() ) )*
    n.nodeListOptional.accept(this);
    return nRes;
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
   * @return the user return information
   */
  public R visit(final PossibleExtensions n) {
    R nRes = null;
    // nodeChoice -> ( %0 < EXTENSION_CONC >
    // .......... .. | %1 < EXTENSION_COMPARTMENT >
    // .......... .. | %2 < EXTENSION_PARTICLE >
    // .......... .. | %3 < EXTENSION_TRANS >
    // .......... .. | %4 < EXTENSION_INIT >
    // .......... .. | %5 < EXTENSION_RATE >
    // .......... .. | %6 < EXTENSION_SPECIES >
    // .......... .. | %7 < EXTENSION_GLOBALQ >
    // .......... .. | %8 < MY_SPECIAL_EXTENSION > )
    n.nodeChoice.accept(this);
    // nodeListOptional -> ( PossibleExtensions() )*
    n.nodeListOptional.accept(this);
    return nRes;
  }

  /**
   * Visits a {@link Literal} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 < INTEGER_LITERAL ><br>
   * .......... .. | %1 < FLOATING_POINT_LITERAL ><br>
   * .......... .. | %2 BooleanLiteral()<br>
   *
   * @param n the node to visit
   * @return the user return information
   */
  public R visit(final Literal n) {
    R nRes = null;
    // nodeChoice -> . %0 < INTEGER_LITERAL >
    // .......... .. | %1 < FLOATING_POINT_LITERAL >
    // .......... .. | %2 BooleanLiteral()
    n.nodeChoice.accept(this);
    return nRes;
  }

  /**
   * Visits a {@link BooleanLiteral} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 < TRUE ><br>
   * .......... .. | %1 < FALSE ><br>
   *
   * @param n the node to visit
   * @return the user return information
   */
  public R visit(final BooleanLiteral n) {
    R nRes = null;
    // nodeChoice -> . %0 < TRUE >
    // .......... .. | %1 < FALSE >
    n.nodeChoice.accept(this);
    return nRes;
  }

  /**
   * Visits a {@link ArgumentList} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 MultistateSites_list()<br>
   * .......... .. | %1 #0 AdditiveExpression()<br>
   * .......... .. . .. #1 ( $0 < COMMA > $1 AdditiveExpression() )*<br>
   *
   * @param n the node to visit
   * @return the user return information
   */
  public R visit(final ArgumentList n) {
    R nRes = null;
    // nodeChoice -> . %0 MultistateSites_list()
    // .......... .. | %1 #0 AdditiveExpression()
    // .......... .. . .. #1 ( $0 < COMMA > $1 AdditiveExpression() )*
    n.nodeChoice.accept(this);
    return nRes;
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
   * @return the user return information
   */
  public R visit(final MultistateSite n) {
    R nRes = null;
    // name -> Name()
    n.name.accept(this);
    // nodeToken -> < LBRACE >
    n.nodeToken.accept(this);
    // nodeChoice -> ( %0 Name()
    // .......... .. | %1 Literal() )
    n.nodeChoice.accept(this);
    // nodeToken1 -> < RBRACE >
    n.nodeToken1.accept(this);
    return nRes;
  }

  /**
   * Visits a {@link MultistateSites_list} node, whose children are the following :
   * <p>
   * multistateSite -> MultistateSite()<br>
   * nodeListOptional -> ( #0 < SEMICOLON > #1 MultistateSite() )*<br>
   *
   * @param n the node to visit
   * @return the user return information
   */
  public R visit(final MultistateSites_list n) {
    R nRes = null;
    // multistateSite -> MultistateSite()
    n.multistateSite.accept(this);
    // nodeListOptional -> ( #0 < SEMICOLON > #1 MultistateSite() )*
    n.nodeListOptional.accept(this);
    return nRes;
  }

}
