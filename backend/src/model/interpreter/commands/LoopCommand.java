package model.interpreter.commands;

import java.util.Map;

import model.interpreter.Interpreter;

public class LoopCommand extends ConditionParser {
    
    @Override
    public void execute(String[] args) throws Exception {
        if (Interpreter.stop) {
            throw new Exception("Interpreter killed");
        }
        
        updateCondition(args);
        while (condition) {
            for (Map.Entry<Command, String[]> entry : commandsWithArgs.entrySet()) {
                entry.getKey().execute(entry.getValue());
            }
            updateCondition(args);
        }
    }
}
