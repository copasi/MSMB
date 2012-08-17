/* Generated by JTB 1.4.4 */
package parsers.mathExpression.visitor;

import parsers.mathExpression.syntaxtree.*;

/**
 * All "VoidArgu" visitors must implement this interface.
 * @param <A> The user argument type
 */
public interface IVoidArguVisitor<A> {

  /*
   * Base "VoidArgu" visit methods
   */

  /**
   * Visits a {@link NodeList} node, passing it an argument.
   *
   * @param n the node to visit
   * @param argu the user argument
   */
  public void visit(final NodeList n, final A argu);

  /**
   * Visits a {@link NodeListOptional} node, passing it an argument.
   *
   * @param n the node to visit
   * @param argu the user argument
   */
  public void visit(final NodeListOptional n, final A argu);

  /**
   * Visits a {@link NodeOptional} node, passing it an argument.
   *
   * @param n the node to visit
   * @param argu the user argument
   */
  public void visit(final NodeOptional n, final A argu);


  /**
   * Visits a {@link NodeSequence} node, passing it an argument.
   *
   * @param n the node to visit
   * @param argu the user argument
   */
  public void visit(final NodeSequence n, final A argu);

  /**
   * Visits a {@link NodeToken} node, passing it an argument.
   *
   * @param n the node to visit
   * @param argu the user argument
   */
  public void visit(final NodeToken n, final A argu);

  /*
   * User grammar generated visit methods
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
  public void visit(final CompleteExpression n, final A argu);

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
  public void visit(final SingleFunctionCall n, final A argu);

  /**
   * Visits a {@link CompleteFunctionDeclaration} node, whose children are the following :
   * <p>
   * functionDeclarator -> FunctionDeclarator()<br>
   * nodeToken -> < EOF ><br>
   *
   * @param n the node to visit
   * @param argu the user argument
   */
  public void visit(final CompleteFunctionDeclaration n, final A argu);

  /**
   * Visits a {@link FunctionDeclarator} node, whose children are the following :
   * <p>
   * nodeToken -> < IDENTIFIER ><br>
   * nodeOptional -> ( FormalParameters() )?<br>
   *
   * @param n the node to visit
   * @param argu the user argument
   */
  public void visit(final FunctionDeclarator n, final A argu);

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
  public void visit(final FormalParameters n, final A argu);

  /**
   * Visits a {@link FormalParameter} node, whose children are the following :
   * <p>
   * primitiveType -> PrimitiveType()<br>
   * variableDeclaratorId -> VariableDeclaratorId()<br>
   *
   * @param n the node to visit
   * @param argu the user argument
   */
  public void visit(final FormalParameter n, final A argu);

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
  public void visit(final PrimitiveType n, final A argu);

  /**
   * Visits a {@link VariableDeclaratorId} node, whose children are the following :
   * <p>
   * nodeToken -> < IDENTIFIER ><br>
   *
   * @param n the node to visit
   * @param argu the user argument
   */
  public void visit(final VariableDeclaratorId n, final A argu);

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
  public void visit(final Expression n, final A argu);

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
  public void visit(final RelationalOperator n, final A argu);

  /**
   * Visits a {@link LogicalOperator} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 < AND ><br>
   * .......... .. | %1 < OR ><br>
   *
   * @param n the node to visit
   * @param argu the user argument
   */
  public void visit(final LogicalOperator n, final A argu);

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
  public void visit(final AdditiveExpression n, final A argu);

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
  public void visit(final MultiplicativeExpression n, final A argu);

  /**
   * Visits a {@link PowerExpression} node, whose children are the following :
   * <p>
   * unaryExpression -> UnaryExpression()<br>
   * nodeListOptional -> ( #0 ( < CARET > ) #1 UnaryExpression() )*<br>
   *
   * @param n the node to visit
   * @param argu the user argument
   */
  public void visit(final PowerExpression n, final A argu);

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
  public void visit(final UnaryExpression n, final A argu);

  /**
   * Visits a {@link UnaryExpressionNotPlusMinus} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 #0 < BANG > #1 UnaryExpression()<br>
   * .......... .. | %1 PrimaryExpression()<br>
   *
   * @param n the node to visit
   * @param argu the user argument
   */
  public void visit(final UnaryExpressionNotPlusMinus n, final A argu);

  /**
   * Visits a {@link PrimaryExpression} node, whose children are the following :
   * <p>
   * primaryPrefix -> PrimaryPrefix()<br>
   *
   * @param n the node to visit
   * @param argu the user argument
   */
  public void visit(final PrimaryExpression n, final A argu);

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
  public void visit(final PrimaryPrefix n, final A argu);

  /**
   * Visits a {@link MultistateSum} node, whose children are the following :
   * <p>
   * nodeToken -> < SUM ><br>
   * nodeToken1 -> < LPAREN ><br>
   * argumentList_MultistateSum -> ArgumentList_MultistateSum()<br>
   * nodeToken2 -> < RPAREN ><br>
   *
   * @param n the node to visit
   * @param argu the user argument
   */
  public void visit(final MultistateSum n, final A argu);

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
  public void visit(final Name n, final A argu);

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
  public void visit(final SpeciesReferenceOrFunctionCall_prefix n, final A argu);

  /**
   * Visits a {@link SpeciesReferenceOrFunctionCall} node, whose children are the following :
   * <p>
   * speciesReferenceOrFunctionCall_prefix -> SpeciesReferenceOrFunctionCall_prefix()<br>
   * nodeListOptional -> ( ( PossibleExtensions() ) )*<br>
   *
   * @param n the node to visit
   * @param argu the user argument
   */
  public void visit(final SpeciesReferenceOrFunctionCall n, final A argu);

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
   * @param argu the user argument
   */
  public void visit(final PossibleExtensions n, final A argu);

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
  public void visit(final Literal n, final A argu);

  /**
   * Visits a {@link BooleanLiteral} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 < TRUE ><br>
   * .......... .. | %1 < FALSE ><br>
   *
   * @param n the node to visit
   * @param argu the user argument
   */
  public void visit(final BooleanLiteral n, final A argu);

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
  public void visit(final ArgumentList n, final A argu);

  /**
   * Visits a {@link ArgumentList_MultistateSum} node, whose children are the following :
   * <p>
   * name -> Name()<br>
   * nodeOptional -> [ ArgumentList_MultistateSum_Selectors() ]<br>
   *
   * @param n the node to visit
   * @param argu the user argument
   */
  public void visit(final ArgumentList_MultistateSum n, final A argu);

  /**
   * Visits a {@link ArgumentList_MultistateSum_Selectors} node, whose children are the following :
   * <p>
   * nodeToken -> < SEMICOLON ><br>
   * selector -> Selector()<br>
   * nodeListOptional -> ( #0 < SEMICOLON > #1 Selector() )*<br>
   *
   * @param n the node to visit
   * @param argu the user argument
   */
  public void visit(final ArgumentList_MultistateSum_Selectors n, final A argu);

  /**
   * Visits a {@link Selector} node, whose children are the following :
   * <p>
   * name -> Name()<br>
   * nodeOptional -> [ %0 SiteSelector_postFix()<br>
   * ............ .. | %1 CoeffFunction_postFix() ]<br>
   *
   * @param n the node to visit
   * @param argu the user argument
   */
  public void visit(final Selector n, final A argu);

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
   * @param argu the user argument
   */
  public void visit(final SiteSelector_postFix n, final A argu);

  /**
   * Visits a {@link CoeffFunction_postFix} node, whose children are the following :
   * <p>
   * nodeToken -> < LPAREN ><br>
   * nodeOptional -> [ ArgumentList() ]<br>
   * nodeToken1 -> < RPAREN ><br>
   *
   * @param n the node to visit
   * @param argu the user argument
   */
  public void visit(final CoeffFunction_postFix n, final A argu);

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
   * @param argu the user argument
   */
  public void visit(final MultistateSite n, final A argu);

  /**
   * Visits a {@link MultistateSites_list} node, whose children are the following :
   * <p>
   * multistateSite -> MultistateSite()<br>
   * nodeListOptional -> ( #0 < SEMICOLON > #1 MultistateSite() )*<br>
   *
   * @param n the node to visit
   * @param argu the user argument
   */
  public void visit(final MultistateSites_list n, final A argu);

}
