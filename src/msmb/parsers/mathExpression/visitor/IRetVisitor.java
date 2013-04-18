/* Generated by JTB 1.4.7 */
package msmb.parsers.mathExpression.visitor;

import msmb.parsers.mathExpression.syntaxtree.*;

/**
 * All "Ret" visitors must implement this interface.
 * @param <R> - The user return information type
 */
public interface IRetVisitor<R> {

  /*
   * Base "Ret" visit methods
   */

  /**
   * Visits a {@link NodeChoice} node.
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final NodeChoice n);

  /**
   * Visits a {@link NodeList} node.
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final NodeList n);

  /**
   * Visits a {@link NodeListOptional} node.
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final NodeListOptional n);

  /**
   * Visits a {@link NodeOptional} node.
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final NodeOptional n);

  /**
   * Visits a {@link NodeSequence} node.
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final NodeSequence n);

  /**
   * Visits a {@link NodeTCF} node.
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final NodeTCF n);

  /**
   * Visits a {@link NodeToken} node.
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final NodeToken n);

  /*
   * User grammar generated visit methods
   */

  /**
   * Visits a {@link CompleteExpression} node, whose children are the following :
   * <p>
   * expression -> Expression()<br>
   * nodeToken -> <EOF><br>
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final CompleteExpression n);

  /**
   * Visits a {@link CompleteListOfExpression} node, whose children are the following :
   * <p>
   * expression -> Expression()<br>
   * nodeListOptional -> ( #0 <COMMA> #1 Expression() )*<br>
   * nodeToken -> <EOF><br>
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final CompleteListOfExpression n);

  /**
   * Visits a {@link CompleteListOfExpression_Events} node, whose children are the following :
   * <p>
   * expression -> Expression()<br>
   * nodeListOptional -> ( #0 <SEMICOLON> #1 Expression() )*<br>
   * nodeToken -> <EOF><br>
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final CompleteListOfExpression_Events n);

  /**
   * Visits a {@link SingleFunctionCall} node, whose children are the following :
   * <p>
   * name -> Name()<br>
   * nodeToken -> <LPAREN><br>
   * nodeOptional -> ( ArgumentList() )?<br>
   * nodeToken1 -> <RPAREN><br>
   * nodeToken2 -> <EOF><br>
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final SingleFunctionCall n);

  /**
   * Visits a {@link CompleteFunctionDeclaration} node, whose children are the following :
   * <p>
   * functionDeclarator -> FunctionDeclarator()<br>
   * nodeToken -> <EOF><br>
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final CompleteFunctionDeclaration n);

  /**
   * Visits a {@link FunctionDeclarator} node, whose children are the following :
   * <p>
   * nodeToken -> <IDENTIFIER><br>
   * nodeOptional -> ( FormalParameters() )?<br>
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final FunctionDeclarator n);

  /**
   * Visits a {@link FormalParameters} node, whose children are the following :
   * <p>
   * nodeToken -> "("<br>
   * nodeOptional -> [ #0 FormalParameter()<br>
   * ............ .. . #1 ( $0 "," $1 FormalParameter() )* ]<br>
   * nodeToken1 -> ")"<br>
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final FormalParameters n);

  /**
   * Visits a {@link FormalParameter} node, whose children are the following :
   * <p>
   * primitiveType -> PrimitiveType()<br>
   * variableDeclaratorId -> VariableDeclaratorId()<br>
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final FormalParameter n);

  /**
   * Visits a {@link PrimitiveType} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 <TYPE_PAR><br>
   * .......... .. | %1 <TYPE_VAR><br>
   * .......... .. | %2 <TYPE_SUB><br>
   * .......... .. | %3 <TYPE_PROD><br>
   * .......... .. | %4 <TYPE_MOD><br>
   * .......... .. | %5 <TYPE_SITE><br>
   * .......... .. | %6 <TYPE_VOL><br>
   * .......... .. | %7 <TYPE_TIME><br>
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final PrimitiveType n);

  /**
   * Visits a {@link VariableDeclaratorId} node, whose children are the following :
   * <p>
   * nodeToken -> <IDENTIFIER><br>
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final VariableDeclaratorId n);

  /**
   * Visits a {@link IfExpression} node, whose children are the following :
   * <p>
   * nodeToken -> <IF><br>
   * nodeToken1 -> <LPAREN><br>
   * expression -> Expression()<br>
   * nodeToken2 -> <COMMA><br>
   * expression1 -> Expression()<br>
   * nodeOptional -> ( #0 <COMMA> #1 Expression() )?<br>
   * nodeToken3 -> <RPAREN><br>
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final IfExpression n);

  /**
   * Visits a {@link LeftExpression} node, whose children are the following :
   * <p>
   * additiveExpression -> AdditiveExpression()<br>
   * nodeOptional -> ( #0 RelationalOperator() #1 Expression() )?<br>
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final LeftExpression n);

  /**
   * Visits a {@link Expression} node, whose children are the following :
   * <p>
   * leftExpression -> LeftExpression()<br>
   * nodeListOptional -> ( #0 LogicalOperator() #1 Expression() )*<br>
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final Expression n);

  /**
   * Visits a {@link RelationalOperator} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 ( &0 <ASSIGN><br>
   * .......... .. . .. | &1 <EQ> )<br>
   * .......... .. | %1 <LT><br>
   * .......... .. | %2 <GT><br>
   * .......... .. | %3 <GEQ><br>
   * .......... .. | %4 <LEQ><br>
   * .......... .. | %5 <NE><br>
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final RelationalOperator n);

  /**
   * Visits a {@link LogicalOperator} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 <AND><br>
   * .......... .. | %1 <OR><br>
   * .......... .. | %2 <XOR><br>
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final LogicalOperator n);

  /**
   * Visits a {@link AdditiveExpression} node, whose children are the following :
   * <p>
   * multiplicativeExpression -> MultiplicativeExpression()<br>
   * nodeListOptional -> ( #0 ( %0 <PLUS><br>
   * ................ .. . .. | %1 <MINUS> )<br>
   * ................ .. . #1 MultiplicativeExpression() )*<br>
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final AdditiveExpression n);

  /**
   * Visits a {@link MultiplicativeExpression} node, whose children are the following :
   * <p>
   * powerExpression -> PowerExpression()<br>
   * nodeListOptional -> ( #0 ( %0 <TIMES><br>
   * ................ .. . .. | %1 <DIV> )<br>
   * ................ .. . #1 PowerExpression() )*<br>
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final MultiplicativeExpression n);

  /**
   * Visits a {@link PowerExpression} node, whose children are the following :
   * <p>
   * unaryExpression -> UnaryExpression()<br>
   * nodeListOptional -> ( #0 ( <CARET> )<br>
   * ................ .. . #1 UnaryExpression() )*<br>
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final PowerExpression n);

  /**
   * Visits a {@link UnaryExpression} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 #0 ( &0 <PLUS><br>
   * .......... .. . .. .. | &1 <MINUS> )<br>
   * .......... .. . .. #1 UnaryExpression()<br>
   * .......... .. | %1 UnaryExpressionNotPlusMinus()<br>
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final UnaryExpression n);

  /**
   * Visits a {@link UnaryExpressionNotPlusMinus} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 #0 <BANG> #1 UnaryExpression()<br>
   * .......... .. | %1 PrimaryExpression()<br>
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final UnaryExpressionNotPlusMinus n);

  /**
   * Visits a {@link PrimaryExpression} node, whose children are the following :
   * <p>
   * primaryPrefix -> PrimaryPrefix()<br>
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final PrimaryExpression n);

  /**
   * Visits a {@link PrimaryPrefix} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 Literal()<br>
   * .......... .. | %1 #0 <LPAREN> #1 Expression() #2 <RPAREN><br>
   * .......... .. | %2 SpeciesReferenceOrFunctionCall()<br>
   * .......... .. | %3 MultistateSum()<br>
   * .......... .. | %4 IfExpression()<br>
   * .......... .. | %5 <CONST_MODEL_TIME><br>
   * .......... .. | %6 <CONST_QUANTITY_CONV_FACTOR><br>
   * .......... .. | %7 <CONST_MODEL_TIME_INITIAL><br>
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final PrimaryPrefix n);

  /**
   * Visits a {@link MultistateSum} node, whose children are the following :
   * <p>
   * nodeToken -> <SUM><br>
   * nodeToken1 -> <LPAREN><br>
   * argumentList_MultistateSum -> ArgumentList_MultistateSum()<br>
   * nodeToken2 -> <RPAREN><br>
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final MultistateSum n);

  /**
   * Visits a {@link Name} node, whose children are the following :
   * <p>
   * nodeChoice -> . %00 <IDENTIFIER><br>
   * .......... .. | %01 PrimitiveType()<br>
   * .......... .. | %02 <PI><br>
   * .......... .. | %03 <EXPONENTIALE><br>
   * .......... .. | %04 <CONST_AVOGADRO><br>
   * .......... .. | %05 <TIME><br>
   * .......... .. | %06 <FLOOR><br>
   * .......... .. | %07 <DELAY><br>
   * .......... .. | %08 <CEIL><br>
   * .......... .. | %09 <FACTORIAL><br>
   * .......... .. | %10 <ASIN><br>
   * .......... .. | %11 <ACOS><br>
   * .......... .. | %12 <ATAN><br>
   * .......... .. | %13 <TAN><br>
   * .......... .. | %14 <TANH><br>
   * .......... .. | %15 <COSH><br>
   * .......... .. | %16 <LOG10><br>
   * .......... .. | %17 <ABS><br>
   * .......... .. | %18 <COS><br>
   * .......... .. | %19 <SIN><br>
   * .......... .. | %20 <SEC><br>
   * .......... .. | %21 <CSC><br>
   * .......... .. | %22 <COT><br>
   * .......... .. | %23 <SINH><br>
   * .......... .. | %24 <ARCSEC><br>
   * .......... .. | %25 <ARCCSC><br>
   * .......... .. | %26 <ARCCOT><br>
   * .......... .. | %27 <ARCSINH><br>
   * .......... .. | %28 <ARCCOSH><br>
   * .......... .. | %29 <ARCTANH><br>
   * .......... .. | %30 <ARCSECH><br>
   * .......... .. | %31 <ARCCSCH><br>
   * .......... .. | %32 <ARCCOTH><br>
   * .......... .. | %33 <MIN><br>
   * .......... .. | %34 <MAX><br>
   * .......... .. | %35 <LOG><br>
   * .......... .. | %36 <EXP><br>
   * .......... .. | %37 <NAN1><br>
   * .......... .. | %38 <NAN2><br>
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final Name n);

  /**
   * Visits a {@link SpeciesReferenceOrFunctionCall_prefix} node, whose children are the following :
   * <p>
   * name -> Name()<br>
   * nodeOptional -> [ #0 <LPAREN><br>
   * ............ .. . #1 [ ArgumentList() ]<br>
   * ............ .. . #2 <RPAREN> ]<br>
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final SpeciesReferenceOrFunctionCall_prefix n);

  /**
   * Visits a {@link SpeciesReferenceOrFunctionCall} node, whose children are the following :
   * <p>
   * speciesReferenceOrFunctionCall_prefix -> SpeciesReferenceOrFunctionCall_prefix()<br>
   * nodeListOptional -> ( ( PossibleExtensions() ) )*<br>
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final SpeciesReferenceOrFunctionCall n);

  /**
   * Visits a {@link PossibleExtensions} node, whose children are the following :
   * <p>
   * nodeChoice -> ( %00 <EXTENSION_CONC><br>
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
   * .......... .. | %11 <MY_SPECIAL_EXTENSION> )<br>
   * nodeListOptional -> ( PossibleExtensions() )*<br>
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final PossibleExtensions n);

  /**
   * Visits a {@link Literal} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 <INTEGER_LITERAL><br>
   * .......... .. | %1 <FLOATING_POINT_LITERAL><br>
   * .......... .. | %2 BooleanLiteral()<br>
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final Literal n);

  /**
   * Visits a {@link BooleanLiteral} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 <TRUE><br>
   * .......... .. | %1 <FALSE><br>
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final BooleanLiteral n);

  /**
   * Visits a {@link ArgumentList} node, whose children are the following :
   * <p>
   * nodeChoice -> . %0 MultistateSites_list()<br>
   * .......... .. | %1 #0 AdditiveExpression()<br>
   * .......... .. . .. #1 ( $0 <COMMA> $1 AdditiveExpression() )*<br>
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final ArgumentList n);

  /**
   * Visits a {@link ArgumentList_MultistateSum} node, whose children are the following :
   * <p>
   * name -> Name()<br>
   * nodeOptional -> [ ArgumentList_MultistateSum_Selectors() ]<br>
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final ArgumentList_MultistateSum n);

  /**
   * Visits a {@link ArgumentList_MultistateSum_Selectors} node, whose children are the following :
   * <p>
   * nodeToken -> <SEMICOLON><br>
   * selector -> Selector()<br>
   * nodeListOptional -> ( #0 <SEMICOLON> #1 Selector() )*<br>
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final ArgumentList_MultistateSum_Selectors n);

  /**
   * Visits a {@link Selector} node, whose children are the following :
   * <p>
   * name -> Name()<br>
   * nodeOptional -> [ %0 SiteSelector_postFix()<br>
   * ............ .. | %1 CoeffFunction_postFix() ]<br>
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final Selector n);

  /**
   * Visits a {@link SiteSelector_postFix} node, whose children are the following :
   * <p>
   * nodeToken -> <LBRACE><br>
   * nodeChoice -> ( %0 Name()<br>
   * .......... .. | %1 Literal() )<br>
   * nodeOptional -> ( %0 ( #0 <COMMA><br>
   * ............ .. . .. . #1 ( &0 Name()<br>
   * ............ .. . .. . .. | &1 Literal() ) )+<br>
   * ............ .. | %1 ( #0 <COLON><br>
   * ............ .. . .. . #1 ( &0 Name()<br>
   * ............ .. . .. . .. | &1 Literal() ) ) )?<br>
   * nodeToken1 -> <RBRACE><br>
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final SiteSelector_postFix n);

  /**
   * Visits a {@link CoeffFunction_postFix} node, whose children are the following :
   * <p>
   * nodeToken -> <LPAREN><br>
   * nodeOptional -> [ ArgumentList() ]<br>
   * nodeToken1 -> <RPAREN><br>
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final CoeffFunction_postFix n);

  /**
   * Visits a {@link MultistateSite} node, whose children are the following :
   * <p>
   * name -> Name()<br>
   * nodeToken -> <LBRACE><br>
   * nodeChoice -> ( %0 Name()<br>
   * .......... .. | %1 Literal() )<br>
   * nodeToken1 -> <RBRACE><br>
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final MultistateSite n);

  /**
   * Visits a {@link MultistateSites_list} node, whose children are the following :
   * <p>
   * multistateSite -> MultistateSite()<br>
   * nodeListOptional -> ( #0 <SEMICOLON> #1 MultistateSite() )*<br>
   *
   * @param n - the node to visit
   * @return the user return information
   */
  public R visit(final MultistateSites_list n);

}
