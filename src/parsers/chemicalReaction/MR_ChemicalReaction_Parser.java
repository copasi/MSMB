/* Generated By:JavaCC: Do not edit this line. MR_ChemicalReaction_Parser.java */
package parsers.chemicalReaction;

import parsers.chemicalReaction.syntaxtree.*;
import parsers.chemicalReaction.visitor.*;
import java.io.*;
import parsers.mathExpression.MR_Expression_ParserConstants;
import parsers.chemicalReaction.syntaxtree.*;


public class MR_ChemicalReaction_Parser implements MR_ChemicalReaction_ParserConstants {

  public static void main(String args[]) {
    System.out.println("Nothing MR_ChemicalReaction_Parser...");
    try {
      String expression = new String("A + B -> C; MOD  ldsdfa;sf");
      InputStream is = new ByteArrayInputStream(expression.getBytes("UTF-8"));
      MR_ChemicalReaction_Parser react = new MR_ChemicalReaction_Parser(is);
      CompleteReaction start = react.CompleteReaction();
      DepthFirstVoidVisitor v = new MyVisitor();
      start.accept(v);
      System.out.println("...................................");
      expression = new String("Cdh1(p{1:10}) -> Cdh1(SDFGSDFGSDF(p))");
      is = new ByteArrayInputStream(expression.getBytes("UTF-8"));
      react = new MR_ChemicalReaction_Parser(is);
      start = react.CompleteReaction();
      v = new MyVisitor();
      start.accept(v);
      System.out.println("...................................");
      expression = new String("cdhNOTbool(p{\u005c"TRUE\u005c",\u005c"FALSE\u005c",somethingElse})  -> cdhNOTbool(succ(p))");
      is = new ByteArrayInputStream(expression.getBytes("UTF-8"));
      react = new MR_ChemicalReaction_Parser(is);
      start = react.CompleteReaction();
      v = new MyVisitor();
      start.accept(v);
      System.out.println("...................................");
      expression = new String("cdhNOTbool(p{\u005c"TRUE\u005c" , \u005c"FALSE\u005c", somethingElse})  -> cdhNOTbool(succ(p))");
      is = new ByteArrayInputStream(expression.getBytes("UTF-8"));
      react = new MR_ChemicalReaction_Parser(is);
      start = react.CompleteReaction();
      v = new MyVisitor();
      start.accept(v);
      System.out.println("...................................");
    }
    catch (Exception e) {
      System.out.println("Oops.");
      System.out.println(e.getMessage());
    }
  }

/*****************************************
 * THE MULTIREMI CHEMICAL REACTION GRAMMAR STARTS HERE *
 *****************************************/
  final public CompleteReaction CompleteReaction() throws ParseException {
  // --- JTB generated node declarations ---
  Reaction n0 = null;
  NodeToken n1 = null;
  Token n2 = null;
    n0 = Reaction();
    n2 = jj_consume_token(0);
    n2.beginColumn++;
    n2.endColumn++;
    { n1 = JTBToolkit.makeNodeToken(n2); }
    {if (true) return new CompleteReaction(n0, n1);}
    throw new Error("Missing return statement in function");
  }

  final public CompleteSpeciesWithCoefficient CompleteSpeciesWithCoefficient() throws ParseException {
  // --- JTB generated node declarations ---
  SpeciesWithCoeff n0 = null;
  NodeToken n1 = null;
  Token n2 = null;
    n0 = SpeciesWithCoeff();
    n2 = jj_consume_token(0);
    n2.beginColumn++;
    n2.endColumn++;
    { n1 = JTBToolkit.makeNodeToken(n2); }
    {if (true) return new CompleteSpeciesWithCoefficient(n0, n1);}
    throw new Error("Missing return statement in function");
  }

  final public Reaction Reaction() throws ParseException {
  // --- JTB generated node declarations ---
  NodeChoice n0 = null;
  NodeSequence n1 = null;
  NodeList n2 = new NodeList();
  AdditiveExpression n3 = null;
  NodeListOptional n4 = new NodeListOptional();
  Blank n5 = null;
  NodeToken n6 = null;
  Token n7 = null;
  NodeListOptional n8 = new NodeListOptional();
  NodeSequence n9 = null;
  NodeSequence n10 = null;
  NodeToken n11 = null;
  Token n12 = null;
  NodeListOptional n13 = null;
  Blank n14 = null;
  NodeList n15 = null;
  AdditiveExpression n16 = null;
  NodeOptional n17 = new NodeOptional();
  NodeSequence n18 = null;
  NodeListOptional n19 = null;
  Blank n20 = null;
  NodeToken n21 = null;
  Token n22 = null;
  NodeListOptional n23 = null;
  Blank n24 = null;
  ListModifiers n25 = null;
  NodeSequence n26 = null;
  NodeToken n27 = null;
  Token n28 = null;
  NodeListOptional n29 = new NodeListOptional();
  Blank n30 = null;
  NodeList n31 = new NodeList();
  AdditiveExpression n32 = null;
  NodeOptional n33 = new NodeOptional();
  NodeSequence n34 = null;
  NodeListOptional n35 = null;
  Blank n36 = null;
  NodeToken n37 = null;
  Token n38 = null;
  NodeListOptional n39 = null;
  Blank n40 = null;
  ListModifiers n41 = null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case INTEGER_LITERAL:
    case FLOATING_POINT_LITERAL:
    case IDENTIFIER:
      n1 = new NodeSequence(5);
      label_1:
      while (true) {
        n3 = AdditiveExpression();
        n2.addNode(n3);
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case INTEGER_LITERAL:
        case FLOATING_POINT_LITERAL:
        case IDENTIFIER:
          ;
          break;
        default:
          jj_la1[0] = jj_gen;
          break label_1;
        }
      }
      n2.nodes.trimToSize();
      n1.addNode(n2);
      label_2:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case 23:
          ;
          break;
        default:
          jj_la1[1] = jj_gen;
          break label_2;
        }
        n5 = Blank();
        n4.addNode(n5);
      }
      n4.nodes.trimToSize();
      n1.addNode(n4);
      n7 = jj_consume_token(ARROW);
      n6 = JTBToolkit.makeNodeToken(n7);
      n1.addNode(n6);
      label_3:
      while (true) {
        if (jj_2_1(2)) {
          ;
        } else {
          break label_3;
        }
        n13 = new NodeListOptional();
        n15 = new NodeList();
        n9 = new NodeSequence(3);
          n10 = new NodeSequence(1);
        n12 = jj_consume_token(23);
          n11 = JTBToolkit.makeNodeToken(n12);
          n10.addNode(n11);
        n9.addNode(n10);
        label_4:
        while (true) {
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case 23:
            ;
            break;
          default:
            jj_la1[2] = jj_gen;
            break label_4;
          }
          n14 = Blank();
          n13.addNode(n14);
        }
        n13.nodes.trimToSize();
        n9.addNode(n13);
        label_5:
        while (true) {
          n16 = AdditiveExpression();
          n15.addNode(n16);
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case INTEGER_LITERAL:
          case FLOATING_POINT_LITERAL:
          case IDENTIFIER:
            ;
            break;
          default:
            jj_la1[3] = jj_gen;
            break label_5;
          }
        }
        n15.nodes.trimToSize();
        n9.addNode(n15);
        n8.addNode(n9);
      }
      n8.nodes.trimToSize();
      n1.addNode(n8);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 23:
      case 24:
        n19 = new NodeListOptional();
        n23 = new NodeListOptional();
        n18 = new NodeSequence(4);
        label_6:
        while (true) {
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case 23:
            ;
            break;
          default:
            jj_la1[4] = jj_gen;
            break label_6;
          }
          n20 = Blank();
          n19.addNode(n20);
        }
        n19.nodes.trimToSize();
        n18.addNode(n19);
        n22 = jj_consume_token(24);
        n21 = JTBToolkit.makeNodeToken(n22);
        n18.addNode(n21);
        label_7:
        while (true) {
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case 23:
            ;
            break;
          default:
            jj_la1[5] = jj_gen;
            break label_7;
          }
          n24 = Blank();
          n23.addNode(n24);
        }
        n23.nodes.trimToSize();
        n18.addNode(n23);
        n25 = ListModifiers();
        n18.addNode(n25);
        n17.addNode(n18);
        break;
      default:
        jj_la1[6] = jj_gen;
        ;
      }
      n1.addNode(n17);
      n0 = new NodeChoice(n1, 0, 2);
      break;
    case ARROW2:
      n26 = new NodeSequence(4);
      n28 = jj_consume_token(ARROW2);
      n27 = JTBToolkit.makeNodeToken(n28);
      n26.addNode(n27);
      label_8:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case 23:
          ;
          break;
        default:
          jj_la1[7] = jj_gen;
          break label_8;
        }
        n30 = Blank();
        n29.addNode(n30);
      }
      n29.nodes.trimToSize();
      n26.addNode(n29);
      label_9:
      while (true) {
        n32 = AdditiveExpression();
        n31.addNode(n32);
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case INTEGER_LITERAL:
        case FLOATING_POINT_LITERAL:
        case IDENTIFIER:
          ;
          break;
        default:
          jj_la1[8] = jj_gen;
          break label_9;
        }
      }
      n31.nodes.trimToSize();
      n26.addNode(n31);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 23:
      case 24:
        n35 = new NodeListOptional();
        n39 = new NodeListOptional();
        n34 = new NodeSequence(4);
        label_10:
        while (true) {
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case 23:
            ;
            break;
          default:
            jj_la1[9] = jj_gen;
            break label_10;
          }
          n36 = Blank();
          n35.addNode(n36);
        }
        n35.nodes.trimToSize();
        n34.addNode(n35);
        n38 = jj_consume_token(24);
        n37 = JTBToolkit.makeNodeToken(n38);
        n34.addNode(n37);
        label_11:
        while (true) {
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case 23:
            ;
            break;
          default:
            jj_la1[10] = jj_gen;
            break label_11;
          }
          n40 = Blank();
          n39.addNode(n40);
        }
        n39.nodes.trimToSize();
        n34.addNode(n39);
        n41 = ListModifiers();
        n34.addNode(n41);
        n33.addNode(n34);
        break;
      default:
        jj_la1[11] = jj_gen;
        ;
      }
      n26.addNode(n33);
      n0 = new NodeChoice(n26, 1, 2);
      break;
    default:
      jj_la1[12] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    {if (true) return new Reaction(n0);}
    throw new Error("Missing return statement in function");
  }

  final public AdditiveExpression AdditiveExpression() throws ParseException {
  // --- JTB generated node declarations ---
  SpeciesWithCoeff n0 = null;
  NodeListOptional n1 = new NodeListOptional();
  NodeSequence n2 = null;
  NodeListOptional n3 = null;
  Blank n4 = null;
  NodeToken n5 = null;
  Token n6 = null;
  NodeListOptional n7 = null;
  Blank n8 = null;
  SpeciesWithCoeff n9 = null;
    n0 = SpeciesWithCoeff();
    label_12:
    while (true) {
      if (jj_2_2(2)) {
        ;
      } else {
        break label_12;
      }
      n3 = new NodeListOptional();
      n7 = new NodeListOptional();
      n2 = new NodeSequence(4);
      label_13:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case 23:
          ;
          break;
        default:
          jj_la1[13] = jj_gen;
          break label_13;
        }
        n4 = Blank();
        n3.addNode(n4);
      }
      n3.nodes.trimToSize();
      n2.addNode(n3);
      n6 = jj_consume_token(25);
      n5 = JTBToolkit.makeNodeToken(n6);
      n2.addNode(n5);
      label_14:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case 23:
          ;
          break;
        default:
          jj_la1[14] = jj_gen;
          break label_14;
        }
        n8 = Blank();
        n7.addNode(n8);
      }
      n7.nodes.trimToSize();
      n2.addNode(n7);
      n9 = SpeciesWithCoeff();
      n2.addNode(n9);
      n1.addNode(n2);
    }
    n1.nodes.trimToSize();
    {if (true) return new AdditiveExpression(n0, n1);}
    throw new Error("Missing return statement in function");
  }

  final public SpeciesWithCoeff SpeciesWithCoeff() throws ParseException {
  // --- JTB generated node declarations ---
  NodeOptional n0 = new NodeOptional();
  NodeSequence n1 = null;
  Stoichiometry n2 = null;
  NodeListOptional n3 = null;
  Blank n4 = null;
  NodeToken n5 = null;
  Token n6 = null;
  NodeListOptional n7 = null;
  Blank n8 = null;
  Species n9 = null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case INTEGER_LITERAL:
    case FLOATING_POINT_LITERAL:
      n3 = new NodeListOptional();
      n7 = new NodeListOptional();
      n1 = new NodeSequence(4);
      n2 = Stoichiometry();
      n1.addNode(n2);
      label_15:
      while (true) {
        if (jj_2_3(2)) {
          ;
        } else {
          break label_15;
        }
        n4 = Blank();
        n3.addNode(n4);
      }
      n3.nodes.trimToSize();
      n1.addNode(n3);
      n6 = jj_consume_token(26);
      n5 = JTBToolkit.makeNodeToken(n6);
      n1.addNode(n5);
      label_16:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case 23:
          ;
          break;
        default:
          jj_la1[15] = jj_gen;
          break label_16;
        }
        n8 = Blank();
        n7.addNode(n8);
      }
      n7.nodes.trimToSize();
      n1.addNode(n7);
      n0.addNode(n1);
      break;
    default:
      jj_la1[16] = jj_gen;
      ;
    }
    n9 = Species();
    {if (true) return new SpeciesWithCoeff(n0, n9);}
    throw new Error("Missing return statement in function");
  }

  final public Blank Blank() throws ParseException {
  // --- JTB generated node declarations ---
  NodeToken n0 = null;
  Token n1 = null;
    n1 = jj_consume_token(23);
    n0 = JTBToolkit.makeNodeToken(n1);
    {if (true) return new Blank(n0);}
    throw new Error("Missing return statement in function");
  }

  final public ListModifiers ListModifiers() throws ParseException {
  // --- JTB generated node declarations ---
  Species n0 = null;
  NodeListOptional n1 = new NodeListOptional();
  NodeSequence n2 = null;
  NodeList n3 = null;
  Blank n4 = null;
  Species n5 = null;
    n0 = Species();
    label_17:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 23:
        ;
        break;
      default:
        jj_la1[17] = jj_gen;
        break label_17;
      }
      n3 = new NodeList();
      n2 = new NodeSequence(2);
      label_18:
      while (true) {
        n4 = Blank();
        n3.addNode(n4);
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case 23:
          ;
          break;
        default:
          jj_la1[18] = jj_gen;
          break label_18;
        }
      }
      n3.nodes.trimToSize();
      n2.addNode(n3);
      n5 = Species();
      n2.addNode(n5);
      n1.addNode(n2);
    }
    n1.nodes.trimToSize();
    {if (true) return new ListModifiers(n0, n1);}
    throw new Error("Missing return statement in function");
  }

  final public Species Species() throws ParseException, ParseException {
  // --- JTB generated node declarations ---
  NodeToken n0 = null;
  Token n1 = null;
    n1 = jj_consume_token(IDENTIFIER);
    n0 = JTBToolkit.makeNodeToken(n1);
    {if (true) return new Species(n0);}
    throw new Error("Missing return statement in function");
  }

  final public Stoichiometry Stoichiometry() throws ParseException {
  // --- JTB generated node declarations ---
  NodeChoice n0 = null;
  NodeToken n1 = null;
  Token n2 = null;
  NodeToken n3 = null;
  Token n4 = null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case INTEGER_LITERAL:
      n2 = jj_consume_token(INTEGER_LITERAL);
      n1 = JTBToolkit.makeNodeToken(n2);
      n0 = new NodeChoice(n1, 0, 2);
      break;
    case FLOATING_POINT_LITERAL:
      n4 = jj_consume_token(FLOATING_POINT_LITERAL);
      n3 = JTBToolkit.makeNodeToken(n4);
      n0 = new NodeChoice(n3, 1, 2);
      break;
    default:
      jj_la1[19] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    {if (true) return new Stoichiometry(n0);}
    throw new Error("Missing return statement in function");
  }

  private boolean jj_2_1(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_1(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(0, xla); }
  }

  private boolean jj_2_2(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_2(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(1, xla); }
  }

  private boolean jj_2_3(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_3(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(2, xla); }
  }

  private boolean jj_3R_21() {
    if (jj_3R_24()) return true;
    return false;
  }

  private boolean jj_3R_24() {
    if (jj_scan_token(23)) return true;
    return false;
  }

  private boolean jj_3_3() {
    if (jj_3R_24()) return true;
    return false;
  }

  private boolean jj_3R_27() {
    if (jj_scan_token(IDENTIFIER)) return true;
    return false;
  }

  private boolean jj_3_1() {
    if (jj_scan_token(23)) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_19()) { jj_scanpos = xsp; break; }
    }
    if (jj_3R_20()) return true;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_20()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  private boolean jj_3R_30() {
    if (jj_scan_token(FLOATING_POINT_LITERAL)) return true;
    return false;
  }

  private boolean jj_3_2() {
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_21()) { jj_scanpos = xsp; break; }
    }
    if (jj_scan_token(25)) return true;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_22()) { jj_scanpos = xsp; break; }
    }
    if (jj_3R_23()) return true;
    return false;
  }

  private boolean jj_3R_20() {
    if (jj_3R_25()) return true;
    return false;
  }

  private boolean jj_3R_26() {
    if (jj_3R_28()) return true;
    return false;
  }

  private boolean jj_3R_29() {
    if (jj_scan_token(INTEGER_LITERAL)) return true;
    return false;
  }

  private boolean jj_3R_25() {
    if (jj_3R_23()) return true;
    return false;
  }

  private boolean jj_3R_23() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_26()) jj_scanpos = xsp;
    if (jj_3R_27()) return true;
    return false;
  }

  private boolean jj_3R_28() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_29()) {
    jj_scanpos = xsp;
    if (jj_3R_30()) return true;
    }
    return false;
  }

  private boolean jj_3R_19() {
    if (jj_3R_24()) return true;
    return false;
  }

  private boolean jj_3R_22() {
    if (jj_3R_24()) return true;
    return false;
  }

  /** Generated Token Manager. */
  public MR_ChemicalReaction_ParserTokenManager token_source;
  SimpleCharStream jj_input_stream;
  /** Current token. */
  public Token token;
  /** Next token. */
  public Token jj_nt;
  private int jj_ntk;
  private Token jj_scanpos, jj_lastpos;
  private int jj_la;
  private int jj_gen;
  final private int[] jj_la1 = new int[20];
  static private int[] jj_la1_0;
  static {
      jj_la1_init_0();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x21800,0x800000,0x800000,0x21800,0x800000,0x800000,0x1800000,0x800000,0x21800,0x800000,0x800000,0x1800000,0x421800,0x800000,0x800000,0x800000,0x1800,0x800000,0x800000,0x1800,};
   }
  final private JJCalls[] jj_2_rtns = new JJCalls[3];
  private boolean jj_rescan = false;
  private int jj_gc = 0;

  /** Constructor with InputStream. */
  public MR_ChemicalReaction_Parser(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public MR_ChemicalReaction_Parser(java.io.InputStream stream, String encoding) {
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new MR_ChemicalReaction_ParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 20; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 20; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Constructor. */
  public MR_ChemicalReaction_Parser(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new MR_ChemicalReaction_ParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 20; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 20; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Constructor with generated Token Manager. */
  public MR_ChemicalReaction_Parser(MR_ChemicalReaction_ParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 20; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(MR_ChemicalReaction_ParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 20; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      if (++jj_gc > 100) {
        jj_gc = 0;
        for (int i = 0; i < jj_2_rtns.length; i++) {
          JJCalls c = jj_2_rtns[i];
          while (c != null) {
            if (c.gen < jj_gen) c.first = null;
            c = c.next;
          }
        }
      }
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }

  static private final class LookaheadSuccess extends java.lang.Error { }
  final private LookaheadSuccess jj_ls = new LookaheadSuccess();
  private boolean jj_scan_token(int kind) {
    if (jj_scanpos == jj_lastpos) {
      jj_la--;
      if (jj_scanpos.next == null) {
        jj_lastpos = jj_scanpos = jj_scanpos.next = token_source.getNextToken();
      } else {
        jj_lastpos = jj_scanpos = jj_scanpos.next;
      }
    } else {
      jj_scanpos = jj_scanpos.next;
    }
    if (jj_rescan) {
      int i = 0; Token tok = token;
      while (tok != null && tok != jj_scanpos) { i++; tok = tok.next; }
      if (tok != null) jj_add_error_token(kind, i);
    }
    if (jj_scanpos.kind != kind) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) throw jj_ls;
    return false;
  }


/** Get the next Token. */
  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  private int[] jj_expentry;
  private int jj_kind = -1;
  private int[] jj_lasttokens = new int[100];
  private int jj_endpos;

  private void jj_add_error_token(int kind, int pos) {
    if (pos >= 100) return;
    if (pos == jj_endpos + 1) {
      jj_lasttokens[jj_endpos++] = kind;
    } else if (jj_endpos != 0) {
      jj_expentry = new int[jj_endpos];
      for (int i = 0; i < jj_endpos; i++) {
        jj_expentry[i] = jj_lasttokens[i];
      }
      jj_entries_loop: for (java.util.Iterator<?> it = jj_expentries.iterator(); it.hasNext();) {
        int[] oldentry = (int[])(it.next());
        if (oldentry.length == jj_expentry.length) {
          for (int i = 0; i < jj_expentry.length; i++) {
            if (oldentry[i] != jj_expentry[i]) {
              continue jj_entries_loop;
            }
          }
          jj_expentries.add(jj_expentry);
          break jj_entries_loop;
        }
      }
      if (pos != 0) jj_lasttokens[(jj_endpos = pos) - 1] = kind;
    }
  }

  /** Generate ParseException. */
  public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[27];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 20; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 27; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    jj_endpos = 0;
    jj_rescan_token();
    jj_add_error_token(0, 0);
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  final public void enable_tracing() {
  }

  /** Disable tracing. */
  final public void disable_tracing() {
  }

  private void jj_rescan_token() {
    jj_rescan = true;
    for (int i = 0; i < 3; i++) {
    try {
      JJCalls p = jj_2_rtns[i];
      do {
        if (p.gen > jj_gen) {
          jj_la = p.arg; jj_lastpos = jj_scanpos = p.first;
          switch (i) {
            case 0: jj_3_1(); break;
            case 1: jj_3_2(); break;
            case 2: jj_3_3(); break;
          }
        }
        p = p.next;
      } while (p != null);
      } catch(LookaheadSuccess ls) { }
    }
    jj_rescan = false;
  }

  private void jj_save(int index, int xla) {
    JJCalls p = jj_2_rtns[index];
    while (p.gen > jj_gen) {
      if (p.next == null) { p = p.next = new JJCalls(); break; }
      p = p.next;
    }
    p.gen = jj_gen + xla - jj_la; p.first = token; p.arg = xla;
  }

  static final class JJCalls {
    int gen;
    Token first;
    int arg;
    JJCalls next;
  }

}


class MyVisitor extends DepthFirstVoidVisitor {

  public void visit(NodeToken n) {
    System.out.println("visit " + MR_ChemicalReaction_ParserConstants.tokenImage[n.kind] + "-->" + n.tokenImage);
  }

  @Override public void visit(Species n) {
    String noQuotes = new String(MR_Expression_ParserConstants.tokenImage[MR_Expression_ParserConstants.EXTENSION_CONC]);
    noQuotes = noQuotes.substring(1, noQuotes.length() - 1);
    if (n.nodeToken.tokenImage.toString().endsWith(noQuotes)) {
      System.out.println("sbagliaaaaaaaaaaato");
    }
    super.visit(n);
  }
}

class JTBToolkit {

  static NodeToken makeNodeToken(final Token t) {
    return new NodeToken(t.image.intern(), t.kind, t.beginLine, t.beginColumn, t.endLine, t.endColumn);
  }
}
