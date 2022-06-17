package model.interpreter.commands;

import java.util.Map;

import model.interpreter.Interpreter;

public class IfCommand extends ConditionParser {

    @Override
    public void execute(String[] args) throws Exception {
        if (Interpreter.stop) {
            throw new Exception("Interpreter killed");
        }
        
        updateCondition(args);
        if (condition) {
            for (Map.Entry<Command, String[]> entry : commandsWithArgs.entrySet()) {
                entry.getKey().execute(entry.getValue());
            }
        }
    }
}
