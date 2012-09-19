/* Generated By:JavaCC: Do not edit this line. MR_Expression_ParserConstants.java */
package parsers.mathExpression;


/**
 * Token literal values and constants.
 * Generated by org.javacc.parser.OtherFilesGen#start()
 */
public interface MR_Expression_ParserConstants {

  /** End of File. */
  int EOF = 0;
  /** RegularExpression Id. */
  int IF = 7;
  /** RegularExpression Id. */
  int THEN = 8;
  /** RegularExpression Id. */
  int ELSE = 9;
  /** RegularExpression Id. */
  int FALSE = 10;
  /** RegularExpression Id. */
  int TRUE = 11;
  /** RegularExpression Id. */
  int TRUE_1 = 12;
  /** RegularExpression Id. */
  int TRUE_2 = 13;
  /** RegularExpression Id. */
  int TRUE_3 = 14;
  /** RegularExpression Id. */
  int FALSE_1 = 15;
  /** RegularExpression Id. */
  int FALSE_2 = 16;
  /** RegularExpression Id. */
  int FALSE_3 = 17;
  /** RegularExpression Id. */
  int TIME = 18;
  /** RegularExpression Id. */
  int TYPE_PAR = 19;
  /** RegularExpression Id. */
  int TYPE_VAR = 20;
  /** RegularExpression Id. */
  int TYPE_SUB = 21;
  /** RegularExpression Id. */
  int TYPE_PROD = 22;
  /** RegularExpression Id. */
  int TYPE_MOD = 23;
  /** RegularExpression Id. */
  int TYPE_SITE = 24;
  /** RegularExpression Id. */
  int TYPE_VOL = 25;
  /** RegularExpression Id. */
  int EXTENSION_CONC = 26;
  /** RegularExpression Id. */
  int EXTENSION_PARTICLE = 27;
  /** RegularExpression Id. */
  int EXTENSION_TRANS = 28;
  /** RegularExpression Id. */
  int EXTENSION_INIT = 29;
  /** RegularExpression Id. */
  int EXTENSION_RATE = 30;
  /** RegularExpression Id. */
  int EXTENSION_SPECIES = 31;
  /** RegularExpression Id. */
  int EXTENSION_GLOBALQ = 32;
  /** RegularExpression Id. */
  int EXTENSION_COMPARTMENT = 33;
  /** RegularExpression Id. */
  int EXTENSION_REACTION = 34;
  /** RegularExpression Id. */
  int EXTENSION_FUNCTION = 35;
  /** RegularExpression Id. */
  int EXTENSION_FLUX = 36;
  /** RegularExpression Id. */
  int MY_SPECIAL_EXTENSION = 37;
  /** RegularExpression Id. */
  int SUM = 38;
  /** RegularExpression Id. */
  int DELAY = 39;
  /** RegularExpression Id. */
  int FLOOR = 40;
  /** RegularExpression Id. */
  int CEIL = 41;
  /** RegularExpression Id. */
  int COS = 42;
  /** RegularExpression Id. */
  int SIN = 43;
  /** RegularExpression Id. */
  int TAN = 44;
  /** RegularExpression Id. */
  int TANH = 45;
  /** RegularExpression Id. */
  int COSH = 46;
  /** RegularExpression Id. */
  int SQRT = 47;
  /** RegularExpression Id. */
  int EXP = 48;
  /** RegularExpression Id. */
  int LOG = 49;
  /** RegularExpression Id. */
  int LOG10 = 50;
  /** RegularExpression Id. */
  int ABS = 51;
  /** RegularExpression Id. */
  int PI = 52;
  /** RegularExpression Id. */
  int NAN = 53;
  /** RegularExpression Id. */
  int CONST_AVOGADRO = 54;
  /** RegularExpression Id. */
  int CONST_QUANTITY_CONV_FACTOR = 55;
  /** RegularExpression Id. */
  int CONST_MODEL_TIME = 56;
  /** RegularExpression Id. */
  int CONST_MODEL_TIME_INITIAL = 57;
  /** RegularExpression Id. */
  int INTEGER_LITERAL = 58;
  /** RegularExpression Id. */
  int FLOATING_POINT_LITERAL = 59;
  /** RegularExpression Id. */
  int EXPONENT = 60;
  /** RegularExpression Id. */
  int LPAREN = 61;
  /** RegularExpression Id. */
  int RPAREN = 62;
  /** RegularExpression Id. */
  int LBRACE = 63;
  /** RegularExpression Id. */
  int RBRACE = 64;
  /** RegularExpression Id. */
  int LBRACKET = 65;
  /** RegularExpression Id. */
  int RBRACKET = 66;
  /** RegularExpression Id. */
  int SEMICOLON = 67;
  /** RegularExpression Id. */
  int COMMA = 68;
  /** RegularExpression Id. */
  int COLON = 69;
  /** RegularExpression Id. */
  int DOT = 70;
  /** RegularExpression Id. */
  int PLUS = 71;
  /** RegularExpression Id. */
  int MINUS = 72;
  /** RegularExpression Id. */
  int TIMES = 73;
  /** RegularExpression Id. */
  int DIV = 74;
  /** RegularExpression Id. */
  int ASSIGN = 75;
  /** RegularExpression Id. */
  int GT = 76;
  /** RegularExpression Id. */
  int LT = 77;
  /** RegularExpression Id. */
  int BANG = 78;
  /** RegularExpression Id. */
  int CARET = 79;
  /** RegularExpression Id. */
  int PERC = 80;
  /** RegularExpression Id. */
  int EQ = 81;
  /** RegularExpression Id. */
  int LEQ = 82;
  /** RegularExpression Id. */
  int GEQ = 83;
  /** RegularExpression Id. */
  int NE = 84;
  /** RegularExpression Id. */
  int OR = 85;
  /** RegularExpression Id. */
  int XOR = 86;
  /** RegularExpression Id. */
  int AND = 87;
  /** RegularExpression Id. */
  int IDENTIFIER = 88;
  /** RegularExpression Id. */
  int LETTER = 89;
  /** RegularExpression Id. */
  int DIGIT = 90;
  /** RegularExpression Id. */
  int STRING_LITERAL = 91;

  /** Lexical state. */
  int DEFAULT = 0;

  /** Literal token values. */
  String[] tokenImage = {
    "<EOF>",
    "\" \"",
    "\"\\t\"",
    "\"\\n\"",
    "\"\\r\"",
    "<token of kind 5>",
    "<token of kind 6>",
    "\"if\"",
    "\"then\"",
    "\"else\"",
    "<FALSE>",
    "<TRUE>",
    "\"true\"",
    "\"True\"",
    "\"TRUE\"",
    "\"false\"",
    "\"False\"",
    "\"FALSE\"",
    "\"Time\"",
    "\"GLQ\"",
    "\"VAR\"",
    "\"SUB\"",
    "\"PROD\"",
    "\"MOD\"",
    "\"SITE\"",
    "\"VOL\"",
    "\".c\"",
    "\".p\"",
    "\".t\"",
    "\".i\"",
    "\".r\"",
    "\".spc\"",
    "\".glq\"",
    "\".cmp\"",
    "\".rct\"",
    "\".fnc\"",
    "\".f\"",
    "\".*MY*SPECIAL*EXTENSION\"",
    "\"SUM\"",
    "\"delay\"",
    "\"floor\"",
    "\"ceil\"",
    "\"cos\"",
    "\"sin\"",
    "\"tan\"",
    "\"tanh\"",
    "\"cosh\"",
    "\"sqrt\"",
    "\"exp\"",
    "\"log\"",
    "\"log10\"",
    "\"abs\"",
    "\"PI\"",
    "<NAN>",
    "\"%NA%\"",
    "\"%QFC%\"",
    "\"%ModTime%\"",
    "\"%ModTime.i%\"",
    "<INTEGER_LITERAL>",
    "<FLOATING_POINT_LITERAL>",
    "<EXPONENT>",
    "\"(\"",
    "\")\"",
    "\"{\"",
    "\"}\"",
    "\"[\"",
    "\"]\"",
    "\";\"",
    "\",\"",
    "\":\"",
    "\".\"",
    "\"+\"",
    "\"-\"",
    "\"*\"",
    "\"/\"",
    "\"=\"",
    "\">\"",
    "\"<\"",
    "\"!\"",
    "\"^\"",
    "\"%\"",
    "\"==\"",
    "\"<=\"",
    "\">=\"",
    "\"!=\"",
    "\"||\"",
    "\"xor\"",
    "\"&&\"",
    "<IDENTIFIER>",
    "<LETTER>",
    "<DIGIT>",
    "<STRING_LITERAL>",
  };

}
