package utility;

import java.util.Stack;

import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;
import org.nfunk.jep.function.PostfixMathCommandI;

public class MyFunctionClass_DELETE_oldParser extends PostfixMathCommand {

		public MyFunctionClass_DELETE_oldParser(int par) {
			numberOfParameters = par;
		}
	
		/**
		 * Runs the square root operation on the inStack. The parameter is popped
		 * off the <code>inStack</code>, and the square root of it's value is 
		 * pushed back to the top of <code>inStack</code>.
		 */
		public void run(Stack inStack) throws ParseException {

			// check the stack
			checkStack(inStack);

			// get the parameter from the stack
			Object param = inStack.pop();

			// check whether the argument is of the right type
			if (param instanceof Double) {
				// calculate the result
				double r = ((Double)param).doubleValue() / 2;
				// push the result on the inStack
				inStack.push(new Double(r));
			} else {
				throw new ParseException("Invalid parameter type");
			}
		}
	}		


		

