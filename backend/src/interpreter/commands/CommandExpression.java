package interpreter.commands;

import interpreter.expression.Expression;

public class CommandExpression implements Expression {

    private Command c;
    
    @Override
    public double calculate() {
        c.execute(null);
        return 0;
    }

}
