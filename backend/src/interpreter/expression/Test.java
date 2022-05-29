package interpreter.expression;

public class Test {

    public static void main(String[] args) {
        System.out.println(ExpressionEvaluator.evaluate("3+5"));
        System.out.println(ExpressionEvaluator.evaluate("3.5+5.5"));
        System.out.println(ExpressionEvaluator.evaluate("(3+5)*4-12"));
        System.out.println(ExpressionEvaluator.evaluate("7.5*(4.3+(6-12.3))/(0-3.0)"));
        System.out.println(ExpressionEvaluator.evaluate("5+2/(3-8)*5*2"));
        System.out.println(ExpressionEvaluator.evaluate("-1+3*(1-8)-(-3)"));
    }

}
