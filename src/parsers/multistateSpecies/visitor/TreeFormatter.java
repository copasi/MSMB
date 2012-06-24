/* Generated by JTB 1.4.4 */
package parsers.multistateSpecies.visitor;

import java.util.ArrayList;
import java.util.Iterator;

import parsers.multistateSpecies.syntaxtree.*;

/**
 * A skeleton output formatter for your language grammar.<br>
 * Using the add() method along with force(), indent(), and outdent(),<br>
 * you can easily specify how this visitor will format the given syntax tree.<br>
 * See the JTB documentation for more details.
 * <p>
 * Pass your syntax tree to this visitor, and then to the TreeDumper visitor<br>
 * in order to "pretty print" your tree.
 */
public class TreeFormatter extends DepthFirstVoidVisitor {

  /** The list of formatting commands */
  private final ArrayList<FormatCommand> cmdQueue = new ArrayList<FormatCommand>();
  /** True if line to be wrapped, false otherwise */
  private boolean lineWrap;
  /** The wrap width */
  private final int wrapWidth;
  /** The indentation amount */
  private final int indentAmt;
  /** The current line number */
  private int curLine = 1;
  /** The current column number */
  private int curColumn = 1;
  /** The current indentation */
  private int curIndent = 0;
  /** The default indentation */
  private static int INDENT_AMT = 2;

  /**
   * Constructor with a default indentation amount of {@link #INDENT_AMT} and no line-wrap.
   */
  public TreeFormatter() { this(INDENT_AMT, 0); }

  /**
   * Constructor using an indent amount and a line width used to wrap long lines.<br>
   * If a token's beginColumn value is greater than the specified wrapWidth,<br>
   * it will be moved to the next line andindented one extra level.<br>
   * To turn off line-wrapping, specify a wrapWidth of 0.
   *
   * @param aIndentAmt Amount of spaces per indentation level
   * @param aWrapWidth Wrap lines longer than wrapWidth. 0 for no wrap
   */
  public TreeFormatter(final int aIndentAmt, final int aWrapWidth) {
    this.indentAmt = aIndentAmt;
    this.wrapWidth = aWrapWidth;

    if (wrapWidth > 0)
       lineWrap = true;
    else
       lineWrap = false;
  }

  /**
   * Accepts a INodeList object.
   *
   * @param n the node list to process
   */
  protected void processList(final INodeList n) {
    processList(n, null);
  }

  /**
   * Accepts a INodeList object and performs a format command (if non null)<br>
   * between each node in the list (but not after the last node).
   *
   * @param n the node list to process
   * @param cmd the format command
   */
  protected void processList(final INodeList n, final FormatCommand cmd) {
    for (final Iterator<INode> e = n.elements(); e.hasNext();) {
       e.next().accept(this);
       if (cmd != null && e.hasNext())
        cmdQueue.add(cmd);
    }
  }

  /**
   * Inserts one line break and indents the next line to the current indentation level.<br>
   * Use "add(force());".
   *
   * @return the corresponding FormatCommand
   */
  protected FormatCommand force() { return force(1); }

  /**
   * Inserts a given number of line breaks and indents the next line to the current indentation level.<br>
   * Use "add(force(i));".
   *
   * @param i the number of line breaks
   * @return the corresponding FormatCommand
   */
  protected FormatCommand force(final int i) {
    return new FormatCommand(FormatCommand.FORCE, i);
  }

  /**
   * Increases the indentation level by one.<br>
   * Use "add(indent());".
   *
   * @return the corresponding FormatCommand
   */
  protected FormatCommand indent() { return indent(1); }

  /**
   * Increases the indentation level by a given number.<br>
   * Use "add(indent(i));".
   *
   * @param i the number of indentation levels to add
   * @return the corresponding FormatCommand
   */
  protected FormatCommand indent(final int i) {
    return new FormatCommand(FormatCommand.INDENT, i);
  }

  /**
   * Reduces the indentation level by one.<br>
   * Use "add(outdent());".
   *
   * @return the corresponding FormatCommand
   */
  protected FormatCommand outdent() { return outdent(1); }

  /**
   * Reduces the indentation level by a given number.<br>
   * Use "add(outdent(i));".
   *
   * @param i the number of indentation levels to substract
   * @return the corresponding FormatCommand
   */
  protected FormatCommand outdent(final int i) {
    return new FormatCommand(FormatCommand.OUTDENT, i);
  }

  /**
   * Adds one space between tokens.<br>
   * Use "add(space());".
   *
   * @return the corresponding FormatCommand
   */
  protected FormatCommand space() { return space(1); }

  /**
   * Adds a given number of spaces between tokens.<br>
   * Use "add(space(i));".
   *
   * @param i the number of spaces to add
   * @return the corresponding FormatCommand
   */
  protected FormatCommand space(final int i) {
    return new FormatCommand(FormatCommand.SPACE, i);
  }

  /**
   * Use this method to add FormatCommands to the command queue to be executed<br>
   * when the next token in the tree is visited.
   *
   * @param cmd the FormatCommand to be added
   */
  protected void add(final FormatCommand cmd) {
    cmdQueue.add(cmd);
  }

  /**
   * Executes the commands waiting in the command queue,<br>
   * then inserts the proper location information into the current NodeToken.
   * <p>
   * If there are any special tokens preceding this token,<br>
   * they will be given the current location information.<br>
   * The token will follow on the next line, at the proper indentation level.<br>
   * If this is not the behavior you want from special tokens,<br>
   * feel free to modify this method.
   */
  @Override
  public void visit(final NodeToken n) {
    for (Iterator<FormatCommand> e = cmdQueue.iterator(); e.hasNext();) {
      final FormatCommand cmd = e.next();
      switch (cmd.getCommand()) {
      case FormatCommand.FORCE :
        curLine += cmd.getNumCommands();
        curColumn = curIndent + 1;
        break;
      case FormatCommand.INDENT :
        curIndent += indentAmt * cmd.getNumCommands();
        break;
      case FormatCommand.OUTDENT :
        if (curIndent >= indentAmt)
        curIndent -= indentAmt * cmd.getNumCommands();
        break;
      case FormatCommand.SPACE :
        curColumn += cmd.getNumCommands();
        break;
      default :
        throw new TreeFormatterException("Invalid value in command queue.");
      }
    }

    cmdQueue.removeAll(cmdQueue);

    //
    // Handle all special tokens preceding this NodeToken
    //
    if (n.numSpecials() > 0)
      for (final Iterator<NodeToken> e = n.specialTokens.iterator(); e.hasNext();) {
       NodeToken special = e.next();

       //
       // Place the token
       // Move cursor to next line after the special token
       // Don't update curColumn - want to keep current indent level
       //
       placeToken(special, curLine, curColumn);
       curLine = special.endLine + 1;
      }

    placeToken(n, curLine, curColumn);
    curLine = n.endLine;
    curColumn = n.endColumn;
  }

  /**
   * Inserts token location (beginLine, beginColumn, endLine, endColumn)<br>
   * information into the NodeToken.<br>
   * Takes into account line-wrap. Does not update curLine and curColumn.
   *
   * @param n the NodeToken to insert
   * @param aLine the insertion line number
   * @param aColumn the insertion column number
   */
  private void placeToken(final NodeToken n, final int aLine, final int aColumn) {
    final int length = n.tokenImage.length();
    int line = aLine;
    int column = aColumn;

    //
    // Find beginning of token.  Only line-wrap for single-line tokens
    //
    if (!lineWrap || n.tokenImage.indexOf('\n') != -1 ||
       column + length <= wrapWidth)
       n.beginColumn = column;
    else {
       ++line;
       column = curIndent + indentAmt + 1;
       n.beginColumn = column;
    }

    n.beginLine = line;

    //
    // Find end of token; don't count '\n' if it's the last character
    //
    for (int i = 0; i < length; ++i) {
       if (n.tokenImage.charAt(i) == '\n' && i < length - 1) {
        ++line;
        column = 1;
       }
       else
        ++column;
    }

    n.endLine = line;
    n.endColumn = column;
  }

  //
  // User-generated visitor methods below
  //

  /**
   * multistateSpecies -> MultistateSpecies()<br>
   * nodeToken -> < EOF ><br>
   */
  @Override
  public void visit(final CompleteMultistateSpecies n) {
    n.multistateSpecies.accept(this);
    n.nodeToken.accept(this);
  }

  /**
   * multistateSpecies_Operator -> MultistateSpecies_Operator()<br>
   * nodeToken -> < EOF ><br>
   */
  @Override
  public void visit(final CompleteMultistateSpecies_Operator n) {
    n.multistateSpecies_Operator.accept(this);
    n.nodeToken.accept(this);
  }

  /**
   * multistateSpecies_SiteSingleElement_Range -> MultistateSpecies_SiteSingleElement_Range()<br>
   * nodeToken -> < EOF ><br>
   */
  @Override
  public void visit(final CompleteMultistateSpecies_Range n) {
    n.multistateSpecies_SiteSingleElement_Range.accept(this);
    n.nodeToken.accept(this);
  }

  /**
   * multistateSpecies_Name -> MultistateSpecies_Name()<br>
   * nodeOptional -> ( #0 < OPEN_R > #1 MultistateSpecies_SingleStateDefinition()<br>
   * ............ .. . #2 ( $0 < SITE_NAMES_SEPARATOR > $1 MultistateSpecies_SingleStateDefinition() )* #3 < CLOSED_R > )?<br>
   */
  @Override
  public void visit(final MultistateSpecies n) {
    n.multistateSpecies_Name.accept(this);
    if (n.nodeOptional.present()) {
      n.nodeOptional.accept(this);
    }
  }

  /**
   * multistateSpecies_SiteName -> MultistateSpecies_SiteName()<br>
   * nodeToken -> < OPEN_C ><br>
   * multistateSpecies_SiteSingleElement -> MultistateSpecies_SiteSingleElement()<br>
   * nodeListOptional -> ( #0 < SITE_STATES_SEPARATOR > #1 MultistateSpecies_SiteSingleElement() )*<br>
   * nodeToken1 -> < CLOSED_C ><br>
   */
  @Override
  public void visit(final MultistateSpecies_SingleStateDefinition n) {
    n.multistateSpecies_SiteName.accept(this);
    n.nodeToken.accept(this);
    n.multistateSpecies_SiteSingleElement.accept(this);
    if (n.nodeListOptional.present()) {
      processList(n.nodeListOptional);
    }
    n.nodeToken1.accept(this);
  }

  /**
   * nodeChoice -> . %0 < STRING_LITERAL ><br>
   * .......... .. | %1 ( &0 < MULTI_IDENTIFIER ><br>
   * .......... .. . .. | &1 < CLOSED_R ><br>
   * .......... .. . .. | &2 < SITE_NAMES_SEPARATOR ><br>
   * .......... .. . .. | &3 < RANGE_SEPARATOR ><br>
   * .......... .. . .. | &4 < SITE_STATES_SEPARATOR ><br>
   * .......... .. . .. | &5 < CLOSED_C > )+<br>
   */
  @Override
  public void visit(final MultistateSpecies_Name n) {
    n.nodeChoice.accept(this);
  }

  /**
   * nodeChoice -> . %0 < STRING_LITERAL ><br>
   * .......... .. | %1 MultistateSpecies_SiteSingleElement_Range()<br>
   * .......... .. | %2 ( &0 < MULTI_IDENTIFIER ><br>
   * .......... .. . .. | &1 < NUMBER ><br>
   * .......... .. . .. | &2 < OPEN_R ><br>
   * .......... .. . .. | &3 < RANGE_SEPARATOR > )+<br>
   */
  @Override
  public void visit(final MultistateSpecies_SiteSingleElement n) {
    n.nodeChoice.accept(this);
  }

  /**
   * nodeToken -> < NUMBER ><br>
   * nodeToken1 -> < RANGE_SEPARATOR ><br>
   * nodeToken2 -> < NUMBER ><br>
   */
  @Override
  public void visit(final MultistateSpecies_SiteSingleElement_Range n) {
    n.nodeToken.accept(this);
    n.nodeToken1.accept(this);
    n.nodeToken2.accept(this);
  }

  /**
   * nodeChoice -> . %0 < STRING_LITERAL ><br>
   * .......... .. | %1 ( &0 < MULTI_IDENTIFIER ><br>
   * .......... .. . .. | &1 < CLOSED_C ><br>
   * .......... .. . .. | &2 < OPEN_R ><br>
   * .......... .. . .. | &3 < CLOSED_R ><br>
   * .......... .. . .. | &4 < RANGE_SEPARATOR ><br>
   * .......... .. . .. | &5 < SITE_STATES_SEPARATOR > )+<br>
   */
  @Override
  public void visit(final MultistateSpecies_SiteName n) {
    n.nodeChoice.accept(this);
  }

  /**
   * multistateSpecies_Name -> MultistateSpecies_Name()<br>
   * nodeOptional -> ( #0 < OPEN_R > #1 MultistateSpecies_Operator_SingleSite()<br>
   * ............ .. . #2 ( $0 < SITE_NAMES_SEPARATOR > $1 MultistateSpecies_Operator_SingleSite() )* #3 < CLOSED_R > )?<br>
   */
  @Override
  public void visit(final MultistateSpecies_Operator n) {
    n.multistateSpecies_Name.accept(this);
    if (n.nodeOptional.present()) {
      n.nodeOptional.accept(this);
    }
  }

  /**
   * nodeChoice -> . %0 #0 MultistateSpecies_Operator_SiteName()<br>
   * .......... .. . .. #1 ( $0 < OPEN_C > $1 MultistateSpecies_Operator_SiteSingleState() $2 < CLOSED_C > )?<br>
   * .......... .. | %1 #0 ( &0 < SUCC ><br>
   * .......... .. . .. .. | &1 < PREC ><br>
   * .......... .. . .. .. | &2 < CIRC_L_SHIFT ><br>
   * .......... .. . .. .. | &3 < CIRC_R_SHIFT > ) #1 < OPEN_R > #2 MultistateSpecies_Operator_SiteName() #3 < CLOSED_R ><br>
   */
  @Override
  public void visit(final MultistateSpecies_Operator_SingleSite n) {
    n.nodeChoice.accept(this);
  }

  /**
   * nodeChoice -> . %0 < STRING_LITERAL ><br>
   * .......... .. | %1 ( &0 < MULTI_IDENTIFIER ><br>
   * .......... .. . .. | &1 < NUMBER ><br>
   * .......... .. . .. | &2 < OPEN_R ><br>
   * .......... .. . .. | &3 < RANGE_SEPARATOR > )+<br>
   */
  @Override
  public void visit(final MultistateSpecies_Operator_SiteName n) {
    n.nodeChoice.accept(this);
  }

  /**
   * nodeChoice -> . %0 < STRING_LITERAL ><br>
   * .......... .. | %1 ( &0 < MULTI_IDENTIFIER ><br>
   * .......... .. . .. | &1 < NUMBER ><br>
   * .......... .. . .. | &2 < OPEN_R ><br>
   * .......... .. . .. | &3 < RANGE_SEPARATOR > )+<br>
   */
  @Override
  public void visit(final MultistateSpecies_Operator_SiteSingleState n) {
    n.nodeChoice.accept(this);
  }

}

/**
 * Stores a format command.
 */
class FormatCommand {

  /** Line break format code */
  public static final int FORCE = 0;
  /** Indentation format code */
  public static final int INDENT = 1;
  /** Unindentation format code */
  public static final int OUTDENT = 2;
  /** Spacing format code */
  public static final int SPACE = 3;

  /** The format command code */
  private int command;
  /** The format command repetition number */
  private int numCommands;

  /**
   * Constructor with class members.
   *
   * @param aCmd the command code
   * @param aNumCmd the command repetition number
   */
  FormatCommand(final int aCmd, final int aNumCmd) {
    this.command = aCmd;
    this.numCommands = aNumCmd;
  }

  /**
   * @return the command code
   */
  public int getCommand()  { return command; }

  /**
   * @return the command repetition number
   */
  public int getNumCommands()  { return numCommands; }

  /**
   * Sets the command code.
   *
   * @param i the command code
   */
  public void setCommand(final int i)  { command = i; }

  /**
   * Sets the command repetition number.
   *
   * @param i the command repetition number
   */
  public void setNumCommands(final int i)  { numCommands = i; }

}

/**
 * The TreeFormatter exception class.
 */
class TreeFormatterException extends RuntimeException {

  /** The serial version uid */
  private static final long serialVersionUID = 1L;

  /**
   * Constructor with no message.
   */
  TreeFormatterException()  { super(); }

  /**
   * Constructor with a given message.
   *
   * @param s the exception message
   */
  TreeFormatterException(final String s)  { super(s); }

}
