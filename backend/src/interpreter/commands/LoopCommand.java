package interpreter.commands;

import java.util.Map;

import interpreter.Interpreter;
import interpreter.ProgramVar;

public class LoopCommand extends ConditionParser {
    
    @Override
    public void execute(String[] args) throws Exception {
        updateCondition(args);
        while (condition) {
            for (Map.Entry<Command, String[]> entry : commandsWithArgs.entrySet()) {
                entry.getKey().execute(entry.getValue());
            }
            double x = Interpreter.symbolTable.get("x").getValue();
            Interpreter.symbolTable.put("x", new ProgramVar(x + 1));
            updateCondition(args);
        }
    }
}
