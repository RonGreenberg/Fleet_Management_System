package interpreter.commands;

import interpreter.expression.Expression;

public class CommandExpression implements Expression {

    private Command c;
    private String[] args;
    
    @Override
    public double calculate() {
        try {
            c.execute(args);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

}
