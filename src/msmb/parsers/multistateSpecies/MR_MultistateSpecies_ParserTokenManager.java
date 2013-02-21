/* Generated By:JavaCC: Do not edit this line. MR_MultistateSpecies_ParserTokenManager.java */
package msmb.parsers.multistateSpecies;
import msmb.parsers.multistateSpecies.syntaxtree.*;
import msmb.parsers.multistateSpecies.visitor.*;
import java.io.*;
import msmb.parsers.mathExpression.MR_Expression_ParserConstants;
import msmb.parsers.multistateSpecies.syntaxtree.*;

/** Token Manager. */
public class MR_MultistateSpecies_ParserTokenManager implements MR_MultistateSpecies_ParserConstants
{

  /** Debug output. */
  public  java.io.PrintStream debugStream = System.out;
  /** Set debug output. */
  public  void setDebugStream(java.io.PrintStream ds) { debugStream = ds; }
private final int jjStopStringLiteralDfa_0(int pos, long active0)
{
   switch (pos)
   {
      case 0:
         if ((active0 & 0x2001fc0L) != 0L)
         {
            jjmatchedKind = 24;
            return 25;
         }
         if ((active0 & 0x2000L) != 0L)
            return 25;
         return -1;
      case 1:
         if ((active0 & 0x1fc0L) != 0L)
         {
            if (jjmatchedPos != 1)
            {
               jjmatchedKind = 24;
               jjmatchedPos = 1;
            }
            return 25;
         }
         return -1;
      case 2:
         if ((active0 & 0x1b40L) != 0L)
         {
            jjmatchedKind = 24;
            jjmatchedPos = 2;
            return 25;
         }
         if ((active0 & 0x480L) != 0L)
            return 25;
         return -1;
      case 3:
         if ((active0 & 0x100L) != 0L)
         {
            jjmatchedKind = 24;
            jjmatchedPos = 3;
            return 25;
         }
         if ((active0 & 0x1a40L) != 0L)
            return 25;
         return -1;
      default :
         return -1;
   }
}
private final int jjStartNfa_0(int pos, long active0)
{
   return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
}
private int jjStopAtPos(int pos, int kind)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   return pos + 1;
}
private int jjMoveStringLiteralDfa0_0()
{
   switch(curChar)
   {
      case 32:
         return jjStopAtPos(0, 14);
      case 40:
         return jjStopAtPos(0, 15);
      case 41:
         return jjStopAtPos(0, 16);
      case 44:
         return jjStopAtPos(0, 21);
      case 46:
         return jjMoveStringLiteralDfa1_0(0x1ffc000000L);
      case 58:
         return jjStopAtPos(0, 19);
      case 59:
         return jjStopAtPos(0, 20);
      case 63:
         return jjStartNfaWithStates_0(0, 24, 25);
      case 83:
         return jjMoveStringLiteralDfa1_0(0x80L);
      case 84:
         return jjMoveStringLiteralDfa1_0(0x40L);
      case 99:
         return jjStartNfaWithStates_0(0, 13, 25);
      case 102:
         return jjMoveStringLiteralDfa1_0(0x100L);
      case 108:
         return jjMoveStringLiteralDfa1_0(0x400L);
      case 112:
         return jjMoveStringLiteralDfa1_0(0x1000L);
      case 115:
         return jjMoveStringLiteralDfa1_0(0xa00L);
      case 123:
         return jjStopAtPos(0, 17);
      case 125:
         return jjStopAtPos(0, 18);
      default :
         return jjMoveNfa_0(0, 0);
   }
}
private int jjMoveStringLiteralDfa1_0(long active0)
{
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(0, active0);
      return 1;
   }
   switch(curChar)
   {
      case 85:
         return jjMoveStringLiteralDfa2_0(active0, 0x80L);
      case 99:
         if ((active0 & 0x4000000L) != 0L)
         {
            jjmatchedKind = 26;
            jjmatchedPos = 1;
         }
         return jjMoveStringLiteralDfa2_0(active0, 0x200000000L);
      case 102:
         if ((active0 & 0x1000000000L) != 0L)
         {
            jjmatchedKind = 36;
            jjmatchedPos = 1;
         }
         return jjMoveStringLiteralDfa2_0(active0, 0x800000000L);
      case 103:
         return jjMoveStringLiteralDfa2_0(active0, 0x100000000L);
      case 105:
         if ((active0 & 0x20000000L) != 0L)
            return jjStopAtPos(1, 29);
         return jjMoveStringLiteralDfa2_0(active0, 0x40L);
      case 108:
         return jjMoveStringLiteralDfa2_0(active0, 0x100L);
      case 111:
         return jjMoveStringLiteralDfa2_0(active0, 0x400L);
      case 112:
         if ((active0 & 0x8000000L) != 0L)
            return jjStopAtPos(1, 27);
         break;
      case 113:
         return jjMoveStringLiteralDfa2_0(active0, 0x200L);
      case 114:
         if ((active0 & 0x40000000L) != 0L)
         {
            jjmatchedKind = 30;
            jjmatchedPos = 1;
         }
         return jjMoveStringLiteralDfa2_0(active0, 0x400001000L);
      case 115:
         return jjMoveStringLiteralDfa2_0(active0, 0x80000000L);
      case 116:
         if ((active0 & 0x10000000L) != 0L)
            return jjStopAtPos(1, 28);
         break;
      case 117:
         return jjMoveStringLiteralDfa2_0(active0, 0x800L);
      default :
         break;
   }
   return jjStartNfa_0(0, active0);
}
private int jjMoveStringLiteralDfa2_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(0, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(1, active0);
      return 2;
   }
   switch(curChar)
   {
      case 77:
         if ((active0 & 0x80L) != 0L)
            return jjStartNfaWithStates_0(2, 7, 25);
         break;
      case 99:
         return jjMoveStringLiteralDfa3_0(active0, 0x400000800L);
      case 101:
         return jjMoveStringLiteralDfa3_0(active0, 0x1000L);
      case 103:
         if ((active0 & 0x400L) != 0L)
            return jjStartNfaWithStates_0(2, 10, 25);
         break;
      case 108:
         return jjMoveStringLiteralDfa3_0(active0, 0x100000000L);
      case 109:
         return jjMoveStringLiteralDfa3_0(active0, 0x200000040L);
      case 110:
         return jjMoveStringLiteralDfa3_0(active0, 0x800000000L);
      case 111:
         return jjMoveStringLiteralDfa3_0(active0, 0x100L);
      case 112:
         return jjMoveStringLiteralDfa3_0(active0, 0x80000000L);
      case 114:
         return jjMoveStringLiteralDfa3_0(active0, 0x200L);
      default :
         break;
   }
   return jjStartNfa_0(1, active0);
}
private int jjMoveStringLiteralDfa3_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(1, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(2, active0);
      return 3;
   }
   switch(curChar)
   {
      case 99:
         if ((active0 & 0x800L) != 0L)
            return jjStartNfaWithStates_0(3, 11, 25);
         else if ((active0 & 0x80000000L) != 0L)
            return jjStopAtPos(3, 31);
         else if ((active0 & 0x800000000L) != 0L)
            return jjStopAtPos(3, 35);
         break;
      case 100:
         if ((active0 & 0x1000L) != 0L)
            return jjStartNfaWithStates_0(3, 12, 25);
         break;
      case 101:
         if ((active0 & 0x40L) != 0L)
            return jjStartNfaWithStates_0(3, 6, 25);
         break;
      case 111:
         return jjMoveStringLiteralDfa4_0(active0, 0x100L);
      case 112:
         if ((active0 & 0x200000000L) != 0L)
            return jjStopAtPos(3, 33);
         break;
      case 113:
         if ((active0 & 0x100000000L) != 0L)
            return jjStopAtPos(3, 32);
         break;
      case 116:
         if ((active0 & 0x200L) != 0L)
            return jjStartNfaWithStates_0(3, 9, 25);
         else if ((active0 & 0x400000000L) != 0L)
            return jjStopAtPos(3, 34);
         break;
      default :
         break;
   }
   return jjStartNfa_0(2, active0);
}
private int jjMoveStringLiteralDfa4_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(2, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(3, active0);
      return 4;
   }
   switch(curChar)
   {
      case 114:
         if ((active0 & 0x100L) != 0L)
            return jjStartNfaWithStates_0(4, 8, 25);
         break;
      default :
         break;
   }
   return jjStartNfa_0(3, active0);
}
private int jjStartNfaWithStates_0(int pos, int kind, int state)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) { return pos + 1; }
   return jjMoveNfa_0(state, pos + 1);
}
static final long[] jjbitVec0 = {
   0x0L, 0x0L, 0xffffffffffffffffL, 0xffffffffffffffffL
};
static final long[] jjbitVec1 = {
   0x0L, 0xffff000000000000L, 0xffffffffffffffffL, 0xffffffffffffffffL
};
private int jjMoveNfa_0(int startState, int curPos)
{
   int startsAt = 0;
   jjnewStateCnt = 25;
   int i = 1;
   jjstateSet[0] = startState;
   int kind = 0x7fffffff;
   for (;;)
   {
      if (++jjround == 0x7fffffff)
         ReInitRounds();
      if (curChar < 64)
      {
         long l = 1L << curChar;
         do
         {
            switch(jjstateSet[--i])
            {
               case 25:
                  if ((0xd000acfa00000000L & l) != 0L)
                  {
                     if (kind > 24)
                        kind = 24;
                     jjCheckNAddTwoStates(9, 10);
                  }
                  else if ((0x3ff000000000000L & l) != 0L)
                  {
                     if (kind > 24)
                        kind = 24;
                     jjCheckNAddTwoStates(9, 10);
                  }
                  break;
               case 0:
                  if ((0xd000acfa00000000L & l) != 0L)
                  {
                     if (kind > 24)
                        kind = 24;
                     jjCheckNAddTwoStates(9, 10);
                  }
                  else if ((0x3ff000000000000L & l) != 0L)
                  {
                     if (kind > 23)
                        kind = 23;
                     jjCheckNAddStates(0, 2);
                  }
                  else if (curChar == 34)
                     jjCheckNAddStates(3, 5);
                  if (curChar == 47)
                     jjAddStates(6, 7);
                  break;
               case 1:
                  if ((0xfffffffbffffdbffL & l) != 0L)
                     jjCheckNAddStates(3, 5);
                  break;
               case 3:
                  if ((0x8400000000L & l) != 0L)
                     jjCheckNAddStates(3, 5);
                  break;
               case 4:
                  if (curChar == 34 && kind > 22)
                     kind = 22;
                  break;
               case 5:
                  if ((0xff000000000000L & l) != 0L)
                     jjCheckNAddStates(8, 11);
                  break;
               case 6:
                  if ((0xff000000000000L & l) != 0L)
                     jjCheckNAddStates(3, 5);
                  break;
               case 7:
                  if ((0xf000000000000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 8;
                  break;
               case 8:
                  if ((0xff000000000000L & l) != 0L)
                     jjCheckNAdd(6);
                  break;
               case 9:
                  if ((0xd000acfa00000000L & l) == 0L)
                     break;
                  if (kind > 24)
                     kind = 24;
                  jjCheckNAddTwoStates(9, 10);
                  break;
               case 10:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 24)
                     kind = 24;
                  jjCheckNAddTwoStates(9, 10);
                  break;
               case 11:
                  if (curChar == 47)
                     jjAddStates(6, 7);
                  break;
               case 12:
                  if (curChar == 47)
                     jjCheckNAddStates(12, 14);
                  break;
               case 13:
                  if ((0xffffffffffffdbffL & l) != 0L)
                     jjCheckNAddStates(12, 14);
                  break;
               case 14:
                  if ((0x2400L & l) != 0L && kind > 4)
                     kind = 4;
                  break;
               case 15:
                  if (curChar == 10 && kind > 4)
                     kind = 4;
                  break;
               case 16:
                  if (curChar == 13)
                     jjstateSet[jjnewStateCnt++] = 15;
                  break;
               case 17:
                  if (curChar == 42)
                     jjCheckNAddTwoStates(18, 19);
                  break;
               case 18:
                  if ((0xfffffbffffffffffL & l) != 0L)
                     jjCheckNAddTwoStates(18, 19);
                  break;
               case 19:
                  if (curChar == 42)
                     jjAddStates(15, 16);
                  break;
               case 20:
                  if ((0xffff7fffffffffffL & l) != 0L)
                     jjCheckNAddTwoStates(21, 19);
                  break;
               case 21:
                  if ((0xfffffbffffffffffL & l) != 0L)
                     jjCheckNAddTwoStates(21, 19);
                  break;
               case 22:
                  if (curChar == 47 && kind > 5)
                     kind = 5;
                  break;
               case 23:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 23)
                     kind = 23;
                  jjCheckNAddStates(0, 2);
                  break;
               case 24:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 23)
                     kind = 23;
                  jjCheckNAdd(24);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else if (curChar < 128)
      {
         long l = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               case 25:
               case 9:
                  if ((0x57ffffffefffffffL & l) == 0L)
                     break;
                  if (kind > 24)
                     kind = 24;
                  jjCheckNAddTwoStates(9, 10);
                  break;
               case 0:
                  if ((0x57ffffffefffffffL & l) == 0L)
                     break;
                  if (kind > 24)
                     kind = 24;
                  jjCheckNAddTwoStates(9, 10);
                  break;
               case 1:
                  if ((0xffffffffefffffffL & l) != 0L)
                     jjCheckNAddStates(3, 5);
                  break;
               case 2:
                  if (curChar == 92)
                     jjAddStates(17, 19);
                  break;
               case 3:
                  if ((0x14404410000000L & l) != 0L)
                     jjCheckNAddStates(3, 5);
                  break;
               case 13:
                  jjAddStates(12, 14);
                  break;
               case 18:
                  jjCheckNAddTwoStates(18, 19);
                  break;
               case 20:
               case 21:
                  jjCheckNAddTwoStates(21, 19);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else
      {
         int hiByte = (int)(curChar >> 8);
         int i1 = hiByte >> 6;
         long l1 = 1L << (hiByte & 077);
         int i2 = (curChar & 0xff) >> 6;
         long l2 = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               case 25:
               case 9:
                  if (!jjCanMove_1(hiByte, i1, i2, l1, l2))
                     break;
                  if (kind > 24)
                     kind = 24;
                  jjCheckNAddTwoStates(9, 10);
                  break;
               case 0:
                  if (!jjCanMove_1(hiByte, i1, i2, l1, l2))
                     break;
                  if (kind > 24)
                     kind = 24;
                  jjCheckNAddTwoStates(9, 10);
                  break;
               case 1:
                  if (jjCanMove_0(hiByte, i1, i2, l1, l2))
                     jjAddStates(3, 5);
                  break;
               case 13:
                  if (jjCanMove_2(hiByte, i1, i2, l1, l2))
                     jjAddStates(12, 14);
                  break;
               case 18:
                  if (jjCanMove_2(hiByte, i1, i2, l1, l2))
                     jjCheckNAddTwoStates(18, 19);
                  break;
               case 20:
               case 21:
                  if (jjCanMove_2(hiByte, i1, i2, l1, l2))
                     jjCheckNAddTwoStates(21, 19);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      if (kind != 0x7fffffff)
      {
         jjmatchedKind = kind;
         jjmatchedPos = curPos;
         kind = 0x7fffffff;
      }
      ++curPos;
      if ((i = jjnewStateCnt) == (startsAt = 25 - (jjnewStateCnt = startsAt)))
         return curPos;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
   }
}
static final int[] jjnextStates = {
   24, 9, 10, 1, 2, 4, 12, 17, 1, 2, 6, 4, 13, 14, 16, 20, 
   22, 3, 5, 7, 
};
private static final boolean jjCanMove_0(int hiByte, int i1, int i2, long l1, long l2)
{
   switch(hiByte)
   {
      case 0:
         return ((jjbitVec0[i2] & l2) != 0L);
      case 3:
         return ((jjbitVec1[i2] & l2) != 0L);
      default :
         return false;
   }
}
private static final boolean jjCanMove_1(int hiByte, int i1, int i2, long l1, long l2)
{
   switch(hiByte)
   {
      case 3:
         return ((jjbitVec1[i2] & l2) != 0L);
      default :
         return false;
   }
}
private static final boolean jjCanMove_2(int hiByte, int i1, int i2, long l1, long l2)
{
   switch(hiByte)
   {
      case 0:
         return ((jjbitVec0[i2] & l2) != 0L);
      default :
         return false;
   }
}

/** Token literal values. */
public static final String[] jjstrLiteralImages = {
"", null, null, null, null, null, "\124\151\155\145", "\123\125\115", 
"\146\154\157\157\162", "\163\161\162\164", "\154\157\147", "\163\165\143\143", "\160\162\145\144", 
"\143", "\40", "\50", "\51", "\173", "\175", "\72", "\73", "\54", null, null, null, 
"\77", "\56\143", "\56\160", "\56\164", "\56\151", "\56\162", "\56\163\160\143", 
"\56\147\154\161", "\56\143\155\160", "\56\162\143\164", "\56\146\156\143", "\56\146", };

/** Lexer state names. */
public static final String[] lexStateNames = {
   "DEFAULT",
};
static final long[] jjtoToken = {
   0x1fffffffc1L, 
};
static final long[] jjtoSkip = {
   0x3eL, 
};
protected SimpleCharStream input_stream;
private final int[] jjrounds = new int[25];
private final int[] jjstateSet = new int[50];
protected char curChar;
/** Constructor. */
public MR_MultistateSpecies_ParserTokenManager(SimpleCharStream stream){
   if (SimpleCharStream.staticFlag)
      throw new Error("ERROR: Cannot use a static CharStream class with a non-static lexical analyzer.");
   input_stream = stream;
}

/** Constructor. */
public MR_MultistateSpecies_ParserTokenManager(SimpleCharStream stream, int lexState){
   this(stream);
   SwitchTo(lexState);
}

/** Reinitialise parser. */
public void ReInit(SimpleCharStream stream)
{
   jjmatchedPos = jjnewStateCnt = 0;
   curLexState = defaultLexState;
   input_stream = stream;
   ReInitRounds();
}
private void ReInitRounds()
{
   int i;
   jjround = 0x80000001;
   for (i = 25; i-- > 0;)
      jjrounds[i] = 0x80000000;
}

/** Reinitialise parser. */
public void ReInit(SimpleCharStream stream, int lexState)
{
   ReInit(stream);
   SwitchTo(lexState);
}

/** Switch to specified lex state. */
public void SwitchTo(int lexState)
{
   if (lexState >= 1 || lexState < 0)
      throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", TokenMgrError.INVALID_LEXICAL_STATE);
   else
      curLexState = lexState;
}

protected Token jjFillToken()
{
   final Token t;
   final String curTokenImage;
   final int beginLine;
   final int endLine;
   final int beginColumn;
   final int endColumn;
   String im = jjstrLiteralImages[jjmatchedKind];
   curTokenImage = (im == null) ? input_stream.GetImage() : im;
   beginLine = input_stream.getBeginLine();
   beginColumn = input_stream.getBeginColumn();
   endLine = input_stream.getEndLine();
   endColumn = input_stream.getEndColumn();
   t = Token.newToken(jjmatchedKind, curTokenImage);

   t.beginLine = beginLine;
   t.endLine = endLine;
   t.beginColumn = beginColumn;
   t.endColumn = endColumn;

   return t;
}

int curLexState = 0;
int defaultLexState = 0;
int jjnewStateCnt;
int jjround;
int jjmatchedPos;
int jjmatchedKind;

/** Get the next Token. */
public Token getNextToken() 
{
  Token matchedToken;
  int curPos = 0;

  EOFLoop :
  for (;;)
  {
   try
   {
      curChar = input_stream.BeginToken();
   }
   catch(java.io.IOException e)
   {
      jjmatchedKind = 0;
      matchedToken = jjFillToken();
      return matchedToken;
   }

   try { input_stream.backup(0);
      while (curChar <= 13 && (0x2600L & (1L << curChar)) != 0L)
         curChar = input_stream.BeginToken();
   }
   catch (java.io.IOException e1) { continue EOFLoop; }
   jjmatchedKind = 0x7fffffff;
   jjmatchedPos = 0;
   curPos = jjMoveStringLiteralDfa0_0();
   if (jjmatchedKind != 0x7fffffff)
   {
      if (jjmatchedPos + 1 < curPos)
         input_stream.backup(curPos - jjmatchedPos - 1);
      if ((jjtoToken[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L)
      {
         matchedToken = jjFillToken();
         return matchedToken;
      }
      else
      {
         continue EOFLoop;
      }
   }
   int error_line = input_stream.getEndLine();
   int error_column = input_stream.getEndColumn();
   String error_after = null;
   boolean EOFSeen = false;
   try { input_stream.readChar(); input_stream.backup(1); }
   catch (java.io.IOException e1) {
      EOFSeen = true;
      error_after = curPos <= 1 ? "" : input_stream.GetImage();
      if (curChar == '\n' || curChar == '\r') {
         error_line++;
         error_column = 0;
      }
      else
         error_column++;
   }
   if (!EOFSeen) {
      input_stream.backup(1);
      error_after = curPos <= 1 ? "" : input_stream.GetImage();
   }
   throw new TokenMgrError(EOFSeen, curLexState, error_line, error_column, error_after, curChar, TokenMgrError.LEXICAL_ERROR);
  }
}

private void jjCheckNAdd(int state)
{
   if (jjrounds[state] != jjround)
   {
      jjstateSet[jjnewStateCnt++] = state;
      jjrounds[state] = jjround;
   }
}
private void jjAddStates(int start, int end)
{
   do {
      jjstateSet[jjnewStateCnt++] = jjnextStates[start];
   } while (start++ != end);
}
private void jjCheckNAddTwoStates(int state1, int state2)
{
   jjCheckNAdd(state1);
   jjCheckNAdd(state2);
}

private void jjCheckNAddStates(int start, int end)
{
   do {
      jjCheckNAdd(jjnextStates[start]);
   } while (start++ != end);
}

}