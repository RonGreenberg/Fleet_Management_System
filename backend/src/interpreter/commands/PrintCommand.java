package interpreter.commands;

import interpreter.Interpreter;
import interpreter.expression.ExpressionEvaluator;

public class PrintCommand implements Command {

    @Override
    public void execute(String[] args) throws Exception {
        String arg = String.join("", args);
        if (arg.startsWith("\"") && arg.endsWith("\"")) {
            System.out.println(arg.substring(1, arg.length() - 1));
        } else {
            Double res = ExpressionEvaluator.tryEvaluate(Interpreter.replaceVarsWithValue(arg));
            if (res != null) {
                System.out.println(res);   
            } else {
                throw new Exception("unknown expression");
            }
        }
    }

}
