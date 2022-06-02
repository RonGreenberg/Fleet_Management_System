package interpreter.commands;

import java.util.LinkedHashMap;
import java.util.Map;

import interpreter.Interpreter;
import interpreter.expression.ExpressionEvaluator;

public class ConditionParser implements Command {

    protected boolean condition;
    protected Map<Command, String[]> commandsWithArgs;
    
    public ConditionParser() {
        // we must make sure that the commands run in the same order as when they were inserted to the map
        commandsWithArgs = new LinkedHashMap<Command, String[]>();
    }
    
    public void addCommand(Command c, String[] args) {
        commandsWithArgs.put(c, args);
    }
    
    public void updateCondition(String[] args) {
        String operand1 = args[0];
        String operator = args[1];
        String operand2 = args[2];
        double val1 = ExpressionEvaluator.evaluate(Interpreter.replaceVarsWithValue(operand1));
        double val2 = ExpressionEvaluator.evaluate(Interpreter.replaceVarsWithValue(operand2));
        
        switch (operator) {
        case "<":
            condition = (val1 < val2);
            break;
        case ">":
            condition = (val1 > val2);
            break;
        case "<=":
            condition = (val1 <= val2);
            break;
        case ">=":
            condition = (val1 >= val2);
            break;
        case "==":
            condition = (val1 == val2);
            break;
        case "!=":
            condition = (val1 != val2);
            break;
        }
    }
    
    @Override
    public void execute(String[] args) throws Exception {}

}
