package model.interpreter.commands;

import java.util.Map;

public class LoopCommand extends ConditionParser {
    
    @Override
    public void execute(String[] args) throws Exception {
        updateCondition(args);
        while (condition) {
            for (Map.Entry<Command, String[]> entry : commandsWithArgs.entrySet()) {
                entry.getKey().execute(entry.getValue());
            }
            updateCondition(args);
        }
    }
}
