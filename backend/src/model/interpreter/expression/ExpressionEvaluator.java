package model.interpreter.expression;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class ExpressionEvaluator {
    
    public static double evaluate(String exp) {
        Stack<Expression> s = new Stack<>(); // stack to store Expressions (either Number or BinaryExpression)
        
        Queue<String> q = infixToPostfix(exp);
        
        /* Reading from left to right, because we need to build the expression
         * from the inside out. Each time we encounter an operator, we can immediately
         * apply it on the two previous operands (that will be stored in the stack in advance)
         * and push the resulting expression to the stack so it will be popped at some point and
         * get wrapped by an outer expression.
         */
        for (String token : q) {
            if (isDouble(token)) {
                s.push(new Number(Double.parseDouble(token)));
            } else {
                Expression right = s.pop(); // the right operand is the first one to be popped from the stack
                Expression left = s.pop();

                switch (token) {
                    case "+":
                        s.push(new Plus(left, right));
                        break;
                    case "-":
                        s.push(new Minus(left, right));
                        break;
                    case "*":
                        s.push(new Mul(left, right));
                        break;
                    case "/":
                        s.push(new Div(left, right));
                        break;
                }
            }
        }
        
        /* The stack is guaranteed to have only the fully formed expression
         * at the end, so we can calculate its value (rounding to 3 digits
         * after the decimal point).
         */
        return Math.floor(s.pop().calculate() * 1000) / 1000; 
    }
    
    // An implementation of the Shunting Yard algorithm
    private static Queue<String> infixToPostfix(String exp) {
        Queue<String> q = new LinkedList<>();
        Stack<String> s = new Stack<>();
        
        exp = exp.replaceAll("\\s+", ""); // removing all whitespaces
        
        /* Replacing all negative numbers with 0- (checking for two situations:
         * a negative number in the beginning, for which the expression starts with
         * a negative sign, or a negative number within the expression, which must be
         * preceded with a left parenthesis, assuming valid input).
         */
        exp = exp.replaceAll("(^-)|((?<=[(])-)", "0-");
        
        exp = exp.replaceAll("--", "+"); // replacing all occurrences of minus-minus with a plus
        
        /* Reading each individual character can be problematic when it comes to numbers
         * that may consist of more than one digit or even have a decimal point.
         * So we can use a regular expression to split the given expression into
         * separate tokens, which will allow us to easily distinguish between numbers,
         * operators and parenthesis. The expression basically matches any delimiters
         * that come before or after an operator/parenthesis (positive lookahead/lookbehind).
         * This way, each operand string in the resulting array can contain the full multi-digit
         * or floating-point numbers.
         */
        String[] tokens = exp.split("(?<=[-+*/()])|(?=[-+*/()])");
        
        for (String token : tokens) {
            if (isDouble(token)) {
                q.add(token);
            } else {
                switch (token) {
                    case "*":
                    case "/":
                        // popping operators with greater than or equal precedence (can only be equal in this case)
                        while (!s.empty() && s.peek().matches("^[*/]$")) {
                            q.add(s.pop());
                        }
                        s.push(token);
                        break;
                    case "(":
                        s.push(token);
                        break;
                    case "+":
                    case "-":
                        // popping operators with greater than or equal precedence (can only be greater in this case)
                        while (!s.empty() && !s.peek().equals("(")) { // popping only operators
                            q.add(s.pop());
                        }
                        s.push(token);
                        break;
                    case ")":
                        // popping until left parenthesis is encountered
                        while (!s.empty() && !s.peek().equals("(")) {
                            q.add(s.pop());
                        }
                        s.pop(); // popping the left parenthesis
                        break;
                }
            }
        }
        
        // popping all remaining operators
        while (!s.empty()) {
            q.add(s.pop());
        }
        
        return q;
    }
    
    public static Double tryEvaluate(String exp) {
        try {
            double res = evaluate(exp);
            return res;
        } catch (Exception e) {
            return null;
        }
    }
    
    private static boolean isDouble(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
