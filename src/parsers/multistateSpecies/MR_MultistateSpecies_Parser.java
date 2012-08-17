/* Generated By:JavaCC: Do not edit this line. MR_MultistateSpecies_Parser.java */
package parsers.multistateSpecies;

import parsers.multistateSpecies.syntaxtree.*;
import parsers.multistateSpecies.visitor.*;
import java.io.*;
import parsers.mathExpression.MR_Expression_ParserConstants;
import parsers.multistateSpecies.syntaxtree.*;


public class MR_MultistateSpecies_Parser implements MR_MultistateSpecies_ParserConstants {

  public static void main(String args[]) {
    System.out.println("MR_MultistateSpecies_Parser...");
    try {
      String expression = new String("Cdh1(P{1:10})");
      InputStream is = new ByteArrayInputStream(expression.getBytes("UTF-8"));
      MR_MultistateSpecies_Parser react = new MR_MultistateSpecies_Parser(is);
      CompleteMultistateSpecies start = react.CompleteMultistateSpecies();
      DepthFirstVoidVisitor v = new MyVisitor();
      start.accept(v);
      System.out.println("...................................");
      expression = new String("c");
      is = new ByteArrayInputStream(expression.getBytes("UTF-8"));
      react = new MR_MultistateSpecies_Parser(is);
      start = react.CompleteMultistateSpecies();
      v = new MyVisitor();
      start.accept(v);
      System.out.println("...................................");
      expression = new String("Cdh1");
      is = new ByteArrayInputStream(expression.getBytes("UTF-8"));
      react = new MR_MultistateSpecies_Parser(is);
      start = react.CompleteMultistateSpecies();
      v = new MyVisitor();
      start.accept(v);
      System.out.println("...................................");
      expression = new String("Cdh1(p{1})");
      is = new ByteArrayInputStream(expression.getBytes("UTF-8"));
      react = new MR_MultistateSpecies_Parser(is);
      start = react.CompleteMultistateSpecies();
      v = new MyVisitor();
      start.accept(v);
      System.out.println("...................................");
      expression = new String("Cdh1(p{1};q{4})");
      is = new ByteArrayInputStream(expression.getBytes("UTF-8"));
      react = new MR_MultistateSpecies_Parser(is);
      start = react.CompleteMultistateSpecies();
      v = new MyVisitor();
      start.accept(v);
      System.out.println("...................................");
      expression = new String("Cdh1(P{1,2,ciao})");
      is = new ByteArrayInputStream(expression.getBytes("UTF-8"));
      react = new MR_MultistateSpecies_Parser(is);
      start = react.CompleteMultistateSpecies();
      v = new MyVisitor();
      start.accept(v);
      System.out.println("...................................");
      expression = new String("Cdh1(P{1:4,6,ciao})");
      is = new ByteArrayInputStream(expression.getBytes("UTF-8"));
      react = new MR_MultistateSpecies_Parser(is);
      start = react.CompleteMultistateSpecies();
      v = new MyVisitor();
      start.accept(v);
      System.out.println("...................................");
      expression = new String("Cdh1(P{1:4,6,PRED,\u005c"pred\u005c"})");
      is = new ByteArrayInputStream(expression.getBytes("UTF-8"));
      react = new MR_MultistateSpecies_Parser(is);
      start = react.CompleteMultistateSpecies();
      v = new MyVisitor();
      start.accept(v);
      System.out.println("...................................");
      System.out.println(".......OPERATORS.....................");
      expression = new String("Cdh1(pred(p);pred(q))");
      is = new ByteArrayInputStream(expression.getBytes("UTF-8"));
      react = new MR_MultistateSpecies_Parser(is);
      CompleteMultistateSpecies_Operator op = react.CompleteMultistateSpecies_Operator();
      v = new MyVisitor();
      op.accept(v);
      System.out.println("...................................");
      expression = new String("Cdh1(succ(p))");
      is = new ByteArrayInputStream(expression.getBytes("UTF-8"));
      react = new MR_MultistateSpecies_Parser(is);
      op = react.CompleteMultistateSpecies_Operator();
      v = new MyVisitor();
      op.accept(v);
      System.out.println("...................................");
      expression = new String("Cdh1(succ(\u005c"Time\u005c");pred(q))");
      is = new ByteArrayInputStream(expression.getBytes("UTF-8"));
      react = new MR_MultistateSpecies_Parser(is);
      op = react.CompleteMultistateSpecies_Operator();
      v = new MyVisitor();
      op.accept(v);
      System.out.println("...................................");
      expression = new String("Cdh1(p{0:10}c)");
      is = new ByteArrayInputStream(expression.getBytes("UTF-8"));
      react = new MR_MultistateSpecies_Parser(is);
      start = react.CompleteMultistateSpecies();
      v = new MyVisitor();
      start.accept(v);
      System.out.println("...................................");
      expression = new String("Cdh1(p{0:10}c;q{true,false})");
      is = new ByteArrayInputStream(expression.getBytes("UTF-8"));
      react = new MR_MultistateSpecies_Parser(is);
      start = react.CompleteMultistateSpecies();
      v = new MyVisitor();
      start.accept(v);
      System.out.println("...................................");
    }
    catch (Exception e) {
      System.out.println("Oops.");
      System.out.println(e.getMessage());
    }
  }

  final public CompleteMultistateSpecies CompleteMultistateSpecies() throws ParseException {
  // --- JTB generated node declarations ---
  MultistateSpecies n0 = null;
  NodeToken n1 = null;
  Token n2 = null;
    n0 = MultistateSpecies();
    n2 = jj_consume_token(0);
    n2.beginColumn++;
    n2.endColumn++;
    { n1 = JTBToolkit.makeNodeToken(n2); }
    {if (true) return new CompleteMultistateSpecies(n0, n1);}
    throw new Error("Missing return statement in function");
  }

  final public CompleteMultistateSpecies_Operator CompleteMultistateSpecies_Operator() throws ParseException, ParseException {
  // --- JTB generated node declarations ---
  MultistateSpecies_Operator n0 = null;
  NodeToken n1 = null;
  Token n2 = null;
    n0 = MultistateSpecies_Operator();
    n2 = jj_consume_token(0);
    n2.beginColumn++;
    n2.endColumn++;
    { n1 = JTBToolkit.makeNodeToken(n2); }
    {if (true) return new CompleteMultistateSpecies_Operator(n0, n1);}
    throw new Error("Missing return statement in function");
  }

  final public CompleteMultistateSpecies_Range CompleteMultistateSpecies_Range() throws ParseException, ParseException {
  // --- JTB generated node declarations ---
  MultistateSpecies_SiteSingleElement_Range n0 = null;
  NodeToken n1 = null;
  Token n2 = null;
    n0 = MultistateSpecies_SiteSingleElement_Range();
    n2 = jj_consume_token(0);
    n2.beginColumn++;
    n2.endColumn++;
    { n1 = JTBToolkit.makeNodeToken(n2); }
    {if (true) return new CompleteMultistateSpecies_Range(n0, n1);}
    throw new Error("Missing return statement in function");
  }

  final public CompleteMultistateSpecies_RangeString CompleteMultistateSpecies_RangeString() throws ParseException, ParseException {
  // --- JTB generated node declarations ---
  MultistateSpecies_SiteSingleElement n0 = null;
  NodeToken n1 = null;
  Token n2 = null;
    n0 = MultistateSpecies_SiteSingleElement();
    n2 = jj_consume_token(0);
    n2.beginColumn++;
    n2.endColumn++;
    { n1 = JTBToolkit.makeNodeToken(n2); }
    {if (true) return new CompleteMultistateSpecies_RangeString(n0, n1);}
    throw new Error("Missing return statement in function");
  }

  final public MultistateSpecies MultistateSpecies() throws ParseException, ParseException {
  // --- JTB generated node declarations ---
  MultistateSpecies_Name n0 = null;
  NodeOptional n1 = new NodeOptional();
  NodeSequence n2 = null;
  NodeToken n3 = null;
  Token n4 = null;
  MultistateSpecies_SingleStateDefinition n5 = null;
  NodeListOptional n6 = null;
  NodeSequence n7 = null;
  NodeToken n8 = null;
  Token n9 = null;
  MultistateSpecies_SingleStateDefinition n10 = null;
  NodeToken n11 = null;
  Token n12 = null;
    n0 = MultistateSpecies_Name();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case OPEN_R:
      n6 = new NodeListOptional();
      n2 = new NodeSequence(4);
      n4 = jj_consume_token(OPEN_R);
      n3 = JTBToolkit.makeNodeToken(n4);
      n2.addNode(n3);
      n5 = MultistateSpecies_SingleStateDefinition();
      n2.addNode(n5);
      label_1:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case SITE_NAMES_SEPARATOR:
          ;
          break;
        default:
          jj_la1[0] = jj_gen;
          break label_1;
        }
        n7 = new NodeSequence(2);
        n9 = jj_consume_token(SITE_NAMES_SEPARATOR);
        n8 = JTBToolkit.makeNodeToken(n9);
        n7.addNode(n8);
        n10 = MultistateSpecies_SingleStateDefinition();
        n7.addNode(n10);
        n6.addNode(n7);
      }
      n6.nodes.trimToSize();
      n2.addNode(n6);
      n12 = jj_consume_token(CLOSED_R);
      n11 = JTBToolkit.makeNodeToken(n12);
      n2.addNode(n11);
      n1.addNode(n2);
      break;
    default:
      jj_la1[1] = jj_gen;
      ;
    }
    {if (true) return new MultistateSpecies(n0, n1);}
    throw new Error("Missing return statement in function");
  }

  final public MultistateSpecies_SingleStateDefinition MultistateSpecies_SingleStateDefinition() throws ParseException, ParseException {
  // --- JTB generated node declarations ---
  MultistateSpecies_SiteName n0 = null;
  NodeToken n1 = null;
  Token n2 = null;
  MultistateSpecies_SiteSingleElement n3 = null;
  NodeListOptional n4 = new NodeListOptional();
  NodeSequence n5 = null;
  NodeToken n6 = null;
  Token n7 = null;
  MultistateSpecies_SiteSingleElement n8 = null;
  NodeToken n9 = null;
  Token n10 = null;
  NodeOptional n11 = new NodeOptional();
  NodeToken n12 = null;
  Token n13 = null;
    n0 = MultistateSpecies_SiteName();
    n2 = jj_consume_token(OPEN_C);
    n1 = JTBToolkit.makeNodeToken(n2);
    n3 = MultistateSpecies_SiteSingleElement();
    label_2:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case SITE_STATES_SEPARATOR:
        ;
        break;
      default:
        jj_la1[2] = jj_gen;
        break label_2;
      }
      n5 = new NodeSequence(2);
      n7 = jj_consume_token(SITE_STATES_SEPARATOR);
      n6 = JTBToolkit.makeNodeToken(n7);
      n5.addNode(n6);
      n8 = MultistateSpecies_SiteSingleElement();
      n5.addNode(n8);
      n4.addNode(n5);
    }
    n4.nodes.trimToSize();
    n10 = jj_consume_token(CLOSED_C);
    n9 = JTBToolkit.makeNodeToken(n10);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case CIRCULAR_FLAG:
      n13 = jj_consume_token(CIRCULAR_FLAG);
      n12 = JTBToolkit.makeNodeToken(n13);
      n11.addNode(n12);
      break;
    default:
      jj_la1[3] = jj_gen;
      ;
    }
    {if (true) return new MultistateSpecies_SingleStateDefinition(n0, n1, n3, n4, n9, n11);}
    throw new Error("Missing return statement in function");
  }

  final public MultistateSpecies_Name MultistateSpecies_Name() throws ParseException, ParseException {
  // --- JTB generated node declarations ---
  NodeChoice n0 = null;
  NodeToken n1 = null;
  Token n2 = null;
  NodeList n3 = new NodeList();
  NodeChoice n4 = null;
  NodeToken n5 = null;
  Token n6 = null;
  NodeToken n7 = null;
  Token n8 = null;
  NodeToken n9 = null;
  Token n10 = null;
  NodeToken n11 = null;
  Token n12 = null;
  NodeToken n13 = null;
  Token n14 = null;
  NodeToken n15 = null;
  Token n16 = null;
  NodeToken n17 = null;
  Token n18 = null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case STRING_LITERAL:
      n2 = jj_consume_token(STRING_LITERAL);
      n1 = JTBToolkit.makeNodeToken(n2);
      n0 = new NodeChoice(n1, 0, 2);
      break;
    case CIRCULAR_FLAG:
    case CLOSED_R:
    case CLOSED_C:
    case RANGE_SEPARATOR:
    case SITE_NAMES_SEPARATOR:
    case SITE_STATES_SEPARATOR:
    case MULTI_IDENTIFIER:
      label_3:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case MULTI_IDENTIFIER:
          n6 = jj_consume_token(MULTI_IDENTIFIER);
          n5 = JTBToolkit.makeNodeToken(n6);
          n4 = new NodeChoice(n5, 0, 7);
          break;
        case CIRCULAR_FLAG:
          n8 = jj_consume_token(CIRCULAR_FLAG);
          n7 = JTBToolkit.makeNodeToken(n8);
          n4 = new NodeChoice(n7, 1, 7);
          break;
        case CLOSED_R:
          n10 = jj_consume_token(CLOSED_R);
          n9 = JTBToolkit.makeNodeToken(n10);
          n4 = new NodeChoice(n9, 2, 7);
          break;
        case SITE_NAMES_SEPARATOR:
          n12 = jj_consume_token(SITE_NAMES_SEPARATOR);
          n11 = JTBToolkit.makeNodeToken(n12);
          n4 = new NodeChoice(n11, 3, 7);
          break;
        case RANGE_SEPARATOR:
          n14 = jj_consume_token(RANGE_SEPARATOR);
          n13 = JTBToolkit.makeNodeToken(n14);
          n4 = new NodeChoice(n13, 4, 7);
          break;
        case SITE_STATES_SEPARATOR:
          n16 = jj_consume_token(SITE_STATES_SEPARATOR);
          n15 = JTBToolkit.makeNodeToken(n16);
          n4 = new NodeChoice(n15, 5, 7);
          break;
        case CLOSED_C:
          n18 = jj_consume_token(CLOSED_C);
          n17 = JTBToolkit.makeNodeToken(n18);
          n4 = new NodeChoice(n17, 6, 7);
          break;
        default:
          jj_la1[4] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
        n3.addNode(n4);
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case CIRCULAR_FLAG:
        case CLOSED_R:
        case CLOSED_C:
        case RANGE_SEPARATOR:
        case SITE_NAMES_SEPARATOR:
        case SITE_STATES_SEPARATOR:
        case MULTI_IDENTIFIER:
          ;
          break;
        default:
          jj_la1[5] = jj_gen;
          break label_3;
        }
      }
      n3.nodes.trimToSize();
      n0 = new NodeChoice(n3, 1, 2);
      break;
    default:
      jj_la1[6] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    {if (true) return new MultistateSpecies_Name(n0);}
    throw new Error("Missing return statement in function");
  }

  final public MultistateSpecies_SiteSingleElement MultistateSpecies_SiteSingleElement() throws ParseException, ParseException {
  // --- JTB generated node declarations ---
  NodeChoice n0 = null;
  NodeToken n1 = null;
  Token n2 = null;
  NodeList n3 = new NodeList();
  NodeChoice n4 = null;
  NodeToken n5 = null;
  Token n6 = null;
  NodeToken n7 = null;
  Token n8 = null;
  NodeToken n9 = null;
  Token n10 = null;
  NodeOptional n11 = new NodeOptional();
  NodeSequence n12 = null;
  NodeListOptional n13 = null;
  NodeToken n14 = null;
  Token n15 = null;
  NodeToken n16 = null;
  Token n17 = null;
  NodeListOptional n18 = null;
  NodeToken n19 = null;
  Token n20 = null;
  NodeChoice n21 = null;
  NodeToken n22 = null;
  Token n23 = null;
  NodeList n24 = null;
  NodeChoice n25 = null;
  NodeToken n26 = null;
  Token n27 = null;
  NodeToken n28 = null;
  Token n29 = null;
  NodeToken n30 = null;
  Token n31 = null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case STRING_LITERAL:
      n2 = jj_consume_token(STRING_LITERAL);
        n1 = JTBToolkit.makeNodeToken(n2);
        n0 = new NodeChoice(n1, 0, 2);
      break;
    case OPEN_R:
    case NUMBER:
    case MULTI_IDENTIFIER:
      label_4:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case MULTI_IDENTIFIER:
          n6 = jj_consume_token(MULTI_IDENTIFIER);
            n5 = JTBToolkit.makeNodeToken(n6);
            n4 = new NodeChoice(n5, 0, 3);
          break;
        case NUMBER:
          n8 = jj_consume_token(NUMBER);
            n7 = JTBToolkit.makeNodeToken(n8);
            n4 = new NodeChoice(n7, 1, 3);
          break;
        case OPEN_R:
          n10 = jj_consume_token(OPEN_R);
            n9 = JTBToolkit.makeNodeToken(n10);
            n4 = new NodeChoice(n9, 2, 3);
          break;
        default:
          jj_la1[7] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
          n3.addNode(n4);
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case OPEN_R:
        case NUMBER:
        case MULTI_IDENTIFIER:
          ;
          break;
        default:
          jj_la1[8] = jj_gen;
          break label_4;
        }
      }
        n3.nodes.trimToSize();
        n0 = new NodeChoice(n3, 1, 2);
      break;
    default:
      jj_la1[9] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case BLANK:
    case RANGE_SEPARATOR:
      n13 = new NodeListOptional();
      n18 = new NodeListOptional();
      n24 = new NodeList();
      n12 = new NodeSequence(4);
      label_5:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case BLANK:
          ;
          break;
        default:
          jj_la1[10] = jj_gen;
          break label_5;
        }
        n15 = jj_consume_token(BLANK);
        n14 = JTBToolkit.makeNodeToken(n15);
        n13.addNode(n14);
      }
      n13.nodes.trimToSize();
      n12.addNode(n13);
      n17 = jj_consume_token(RANGE_SEPARATOR);
      n16 = JTBToolkit.makeNodeToken(n17);
      n12.addNode(n16);
      label_6:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case BLANK:
          ;
          break;
        default:
          jj_la1[11] = jj_gen;
          break label_6;
        }
        n20 = jj_consume_token(BLANK);
        n19 = JTBToolkit.makeNodeToken(n20);
        n18.addNode(n19);
      }
      n18.nodes.trimToSize();
      n12.addNode(n18);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case STRING_LITERAL:
        n23 = jj_consume_token(STRING_LITERAL);
          n22 = JTBToolkit.makeNodeToken(n23);
          n21 = new NodeChoice(n22, 0, 2);
        break;
      case OPEN_R:
      case NUMBER:
      case MULTI_IDENTIFIER:
        label_7:
        while (true) {
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case MULTI_IDENTIFIER:
            n27 = jj_consume_token(MULTI_IDENTIFIER);
              n26 = JTBToolkit.makeNodeToken(n27);
              n25 = new NodeChoice(n26, 0, 3);
            break;
          case NUMBER:
            n29 = jj_consume_token(NUMBER);
              n28 = JTBToolkit.makeNodeToken(n29);
              n25 = new NodeChoice(n28, 1, 3);
            break;
          case OPEN_R:
            n31 = jj_consume_token(OPEN_R);
              n30 = JTBToolkit.makeNodeToken(n31);
              n25 = new NodeChoice(n30, 2, 3);
            break;
          default:
            jj_la1[12] = jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
          }
            n24.addNode(n25);
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case OPEN_R:
          case NUMBER:
          case MULTI_IDENTIFIER:
            ;
            break;
          default:
            jj_la1[13] = jj_gen;
            break label_7;
          }
        }
          n24.nodes.trimToSize();
          n21 = new NodeChoice(n24, 1, 2);
        break;
      default:
        jj_la1[14] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      n12.addNode(n21);
      n11.addNode(n12);
      break;
    default:
      jj_la1[15] = jj_gen;
      ;
    }
    {if (true) return new MultistateSpecies_SiteSingleElement(n0, n11);}
    throw new Error("Missing return statement in function");
  }

  final public MultistateSpecies_SiteSingleElement_Range MultistateSpecies_SiteSingleElement_Range() throws ParseException, ParseException {
  // --- JTB generated node declarations ---
  NodeToken n0 = null;
  Token n1 = null;
  NodeListOptional n2 = new NodeListOptional();
  NodeToken n3 = null;
  Token n4 = null;
  NodeToken n5 = null;
  Token n6 = null;
  NodeListOptional n7 = new NodeListOptional();
  NodeToken n8 = null;
  Token n9 = null;
  NodeToken n10 = null;
  Token n11 = null;
    n1 = jj_consume_token(NUMBER);
    n0 = JTBToolkit.makeNodeToken(n1);
    label_8:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case BLANK:
        ;
        break;
      default:
        jj_la1[16] = jj_gen;
        break label_8;
      }
      n4 = jj_consume_token(BLANK);
      n3 = JTBToolkit.makeNodeToken(n4);
      n2.addNode(n3);
    }
    n2.nodes.trimToSize();
    n6 = jj_consume_token(RANGE_SEPARATOR);
    n5 = JTBToolkit.makeNodeToken(n6);
    label_9:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case BLANK:
        ;
        break;
      default:
        jj_la1[17] = jj_gen;
        break label_9;
      }
      n9 = jj_consume_token(BLANK);
      n8 = JTBToolkit.makeNodeToken(n9);
      n7.addNode(n8);
    }
    n7.nodes.trimToSize();
    n11 = jj_consume_token(NUMBER);
    n10 = JTBToolkit.makeNodeToken(n11);
    {if (true) return new MultistateSpecies_SiteSingleElement_Range(n0, n2, n5, n7, n10);}
    throw new Error("Missing return statement in function");
  }

  final public MultistateSpecies_SiteName MultistateSpecies_SiteName() throws ParseException, ParseException {
  // --- JTB generated node declarations ---
  NodeChoice n0 = null;
  NodeToken n1 = null;
  Token n2 = null;
  NodeList n3 = new NodeList();
  NodeChoice n4 = null;
  NodeToken n5 = null;
  Token n6 = null;
  NodeToken n7 = null;
  Token n8 = null;
  NodeToken n9 = null;
  Token n10 = null;
  NodeToken n11 = null;
  Token n12 = null;
  NodeToken n13 = null;
  Token n14 = null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case STRING_LITERAL:
      n2 = jj_consume_token(STRING_LITERAL);
      n1 = JTBToolkit.makeNodeToken(n2);
      n0 = new NodeChoice(n1, 0, 2);
      break;
    case OPEN_R:
    case CLOSED_R:
    case CLOSED_C:
    case SITE_STATES_SEPARATOR:
    case MULTI_IDENTIFIER:
      label_10:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case MULTI_IDENTIFIER:
          n6 = jj_consume_token(MULTI_IDENTIFIER);
          n5 = JTBToolkit.makeNodeToken(n6);
          n4 = new NodeChoice(n5, 0, 5);
          break;
        case CLOSED_C:
          n8 = jj_consume_token(CLOSED_C);
          n7 = JTBToolkit.makeNodeToken(n8);
          n4 = new NodeChoice(n7, 1, 5);
          break;
        case OPEN_R:
          n10 = jj_consume_token(OPEN_R);
          n9 = JTBToolkit.makeNodeToken(n10);
          n4 = new NodeChoice(n9, 2, 5);
          break;
        case CLOSED_R:
          n12 = jj_consume_token(CLOSED_R);
          n11 = JTBToolkit.makeNodeToken(n12);
          n4 = new NodeChoice(n11, 3, 5);
          break;
        case SITE_STATES_SEPARATOR:
          n14 = jj_consume_token(SITE_STATES_SEPARATOR);
          n13 = JTBToolkit.makeNodeToken(n14);
          n4 = new NodeChoice(n13, 4, 5);
          break;
        default:
          jj_la1[18] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
        n3.addNode(n4);
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case OPEN_R:
        case CLOSED_R:
        case CLOSED_C:
        case SITE_STATES_SEPARATOR:
        case MULTI_IDENTIFIER:
          ;
          break;
        default:
          jj_la1[19] = jj_gen;
          break label_10;
        }
      }
      n3.nodes.trimToSize();
      n0 = new NodeChoice(n3, 1, 2);
      break;
    default:
      jj_la1[20] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    {if (true) return new MultistateSpecies_SiteName(n0);}
    throw new Error("Missing return statement in function");
  }

  final public MultistateSpecies_Operator MultistateSpecies_Operator() throws ParseException, ParseException {
  // --- JTB generated node declarations ---
  MultistateSpecies_Name n0 = null;
  NodeOptional n1 = new NodeOptional();
  NodeSequence n2 = null;
  NodeToken n3 = null;
  Token n4 = null;
  MultistateSpecies_Operator_SingleSite n5 = null;
  NodeListOptional n6 = null;
  NodeSequence n7 = null;
  NodeToken n8 = null;
  Token n9 = null;
  MultistateSpecies_Operator_SingleSite n10 = null;
  NodeToken n11 = null;
  Token n12 = null;
    n0 = MultistateSpecies_Name();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case OPEN_R:
      n6 = new NodeListOptional();
      n2 = new NodeSequence(4);
      n4 = jj_consume_token(OPEN_R);
      n3 = JTBToolkit.makeNodeToken(n4);
      n2.addNode(n3);
      n5 = MultistateSpecies_Operator_SingleSite();
      n2.addNode(n5);
      label_11:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case SITE_NAMES_SEPARATOR:
          ;
          break;
        default:
          jj_la1[21] = jj_gen;
          break label_11;
        }
        n7 = new NodeSequence(2);
        n9 = jj_consume_token(SITE_NAMES_SEPARATOR);
        n8 = JTBToolkit.makeNodeToken(n9);
        n7.addNode(n8);
        n10 = MultistateSpecies_Operator_SingleSite();
        n7.addNode(n10);
        n6.addNode(n7);
      }
      n6.nodes.trimToSize();
      n2.addNode(n6);
      n12 = jj_consume_token(CLOSED_R);
      n11 = JTBToolkit.makeNodeToken(n12);
      n2.addNode(n11);
      n1.addNode(n2);
      break;
    default:
      jj_la1[22] = jj_gen;
      ;
    }
    {if (true) return new MultistateSpecies_Operator(n0, n1);}
    throw new Error("Missing return statement in function");
  }

  final public MultistateSpecies_Operator_SingleSite MultistateSpecies_Operator_SingleSite() throws ParseException, ParseException {
  // --- JTB generated node declarations ---
  NodeChoice n0 = null;
  NodeSequence n1 = null;
  NodeChoice n2 = null;
  NodeToken n3 = null;
  Token n4 = null;
  NodeToken n5 = null;
  Token n6 = null;
  NodeToken n7 = null;
  Token n8 = null;
  MultistateSpecies_Operator_SiteName n9 = null;
  NodeToken n10 = null;
  Token n11 = null;
  NodeSequence n12 = null;
  MultistateSpecies_Operator_SiteName n13 = null;
  NodeOptional n14 = new NodeOptional();
  NodeSequence n15 = null;
  NodeToken n16 = null;
  Token n17 = null;
  MultistateSpecies_Operator_SiteSingleState n18 = null;
  NodeToken n19 = null;
  Token n20 = null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case SUCC:
    case PREC:
      n1 = new NodeSequence(4);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case SUCC:
        n4 = jj_consume_token(SUCC);
          n3 = JTBToolkit.makeNodeToken(n4);
          n2 = new NodeChoice(n3, 0, 2);
        break;
      case PREC:
        n6 = jj_consume_token(PREC);
          n5 = JTBToolkit.makeNodeToken(n6);
          n2 = new NodeChoice(n5, 1, 2);
        break;
      default:
        jj_la1[23] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      n1.addNode(n2);
      n8 = jj_consume_token(OPEN_R);
      n7 = JTBToolkit.makeNodeToken(n8);
      n1.addNode(n7);
      n9 = MultistateSpecies_Operator_SiteName();
      n1.addNode(n9);
      n11 = jj_consume_token(CLOSED_R);
      n10 = JTBToolkit.makeNodeToken(n11);
      n1.addNode(n10);
      n0 = new NodeChoice(n1, 0, 2);
      break;
    case STRING_LITERAL:
    case NUMBER:
    case MULTI_IDENTIFIER:
      n12 = new NodeSequence(2);
      n13 = MultistateSpecies_Operator_SiteName();
      n12.addNode(n13);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case OPEN_C:
        n15 = new NodeSequence(3);
        n17 = jj_consume_token(OPEN_C);
        n16 = JTBToolkit.makeNodeToken(n17);
        n15.addNode(n16);
        n18 = MultistateSpecies_Operator_SiteSingleState();
        n15.addNode(n18);
        n20 = jj_consume_token(CLOSED_C);
        n19 = JTBToolkit.makeNodeToken(n20);
        n15.addNode(n19);
        n14.addNode(n15);
        break;
      default:
        jj_la1[24] = jj_gen;
        ;
      }
      n12.addNode(n14);
      n0 = new NodeChoice(n12, 1, 2);
      break;
    default:
      jj_la1[25] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    {if (true) return new MultistateSpecies_Operator_SingleSite(n0);}
    throw new Error("Missing return statement in function");
  }

  final public MultistateSpecies_Operator_SiteName MultistateSpecies_Operator_SiteName() throws ParseException, ParseException {
  // --- JTB generated node declarations ---
  NodeChoice n0 = null;
  NodeToken n1 = null;
  Token n2 = null;
  NodeList n3 = new NodeList();
  NodeChoice n4 = null;
  NodeToken n5 = null;
  Token n6 = null;
  NodeToken n7 = null;
  Token n8 = null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case STRING_LITERAL:
      n2 = jj_consume_token(STRING_LITERAL);
      n1 = JTBToolkit.makeNodeToken(n2);
      n0 = new NodeChoice(n1, 0, 2);
      break;
    case NUMBER:
    case MULTI_IDENTIFIER:
      label_12:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case MULTI_IDENTIFIER:
          n6 = jj_consume_token(MULTI_IDENTIFIER);
          n5 = JTBToolkit.makeNodeToken(n6);
          n4 = new NodeChoice(n5, 0, 2);
          break;
        case NUMBER:
          n8 = jj_consume_token(NUMBER);
          n7 = JTBToolkit.makeNodeToken(n8);
          n4 = new NodeChoice(n7, 1, 2);
          break;
        default:
          jj_la1[26] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
        n3.addNode(n4);
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case NUMBER:
        case MULTI_IDENTIFIER:
          ;
          break;
        default:
          jj_la1[27] = jj_gen;
          break label_12;
        }
      }
      n3.nodes.trimToSize();
      n0 = new NodeChoice(n3, 1, 2);
      break;
    default:
      jj_la1[28] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    {if (true) return new MultistateSpecies_Operator_SiteName(n0);}
    throw new Error("Missing return statement in function");
  }

  final public MultistateSpecies_Operator_SiteSingleState MultistateSpecies_Operator_SiteSingleState() throws ParseException, ParseException {
  // --- JTB generated node declarations ---
  NodeChoice n0 = null;
  NodeToken n1 = null;
  Token n2 = null;
  NodeList n3 = new NodeList();
  NodeChoice n4 = null;
  NodeToken n5 = null;
  Token n6 = null;
  NodeToken n7 = null;
  Token n8 = null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case STRING_LITERAL:
      n2 = jj_consume_token(STRING_LITERAL);
      n1 = JTBToolkit.makeNodeToken(n2);
      n0 = new NodeChoice(n1, 0, 2);
      break;
    case NUMBER:
    case MULTI_IDENTIFIER:
      label_13:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case MULTI_IDENTIFIER:
          n6 = jj_consume_token(MULTI_IDENTIFIER);
          n5 = JTBToolkit.makeNodeToken(n6);
          n4 = new NodeChoice(n5, 0, 2);
          break;
        case NUMBER:
          n8 = jj_consume_token(NUMBER);
          n7 = JTBToolkit.makeNodeToken(n8);
          n4 = new NodeChoice(n7, 1, 2);
          break;
        default:
          jj_la1[29] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
        n3.addNode(n4);
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case NUMBER:
        case MULTI_IDENTIFIER:
          ;
          break;
        default:
          jj_la1[30] = jj_gen;
          break label_13;
        }
      }
      n3.nodes.trimToSize();
      n0 = new NodeChoice(n3, 1, 2);
      break;
    default:
      jj_la1[31] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    {if (true) return new MultistateSpecies_Operator_SiteSingleState(n0);}
    throw new Error("Missing return statement in function");
  }

  /** Generated Token Manager. */
  public MR_MultistateSpecies_ParserTokenManager token_source;
  SimpleCharStream jj_input_stream;
  /** Current token. */
  public Token token;
  /** Next token. */
  public Token jj_nt;
  private int jj_ntk;
  private int jj_gen;
  final private int[] jj_la1 = new int[32];
  static private int[] jj_la1_0;
  static {
      jj_la1_init_0();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x100000,0x8000,0x200000,0x2000,0x13d2000,0x13d2000,0x17d2000,0x1808000,0x1808000,0x1c08000,0x4000,0x4000,0x1808000,0x1808000,0x1c08000,0x84000,0x4000,0x4000,0x1258000,0x1258000,0x1658000,0x100000,0x8000,0x1800,0x20000,0x1c01800,0x1800000,0x1800000,0x1c00000,0x1800000,0x1800000,0x1c00000,};
   }

  /** Constructor with InputStream. */
  public MR_MultistateSpecies_Parser(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public MR_MultistateSpecies_Parser(java.io.InputStream stream, String encoding) {
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new MR_MultistateSpecies_ParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 32; i++) jj_la1[i] = -1;
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
    for (int i = 0; i < 32; i++) jj_la1[i] = -1;
  }

  /** Constructor. */
  public MR_MultistateSpecies_Parser(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new MR_MultistateSpecies_ParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 32; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 32; i++) jj_la1[i] = -1;
  }

  /** Constructor with generated Token Manager. */
  public MR_MultistateSpecies_Parser(MR_MultistateSpecies_ParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 32; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(MR_MultistateSpecies_ParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 32; i++) jj_la1[i] = -1;
  }

  private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
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

  /** Generate ParseException. */
  public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[26];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 32; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 26; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
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

}


class MyVisitor extends DepthFirstVoidVisitor {

  public void visit(NodeToken n) {
    System.out.println("visit " + MR_MultistateSpecies_ParserConstants.tokenImage[n.kind] + "-->" + n.tokenImage);
  }

  @Override public void visit(MultistateSpecies n) {
    System.out.println("MultistateSpecies");
    super.visit(n);
  }
}

class JTBToolkit {

  static NodeToken makeNodeToken(final Token t) {
    return new NodeToken(t.image.intern(), t.kind, t.beginLine, t.beginColumn, t.endLine, t.endColumn);
  }
}
