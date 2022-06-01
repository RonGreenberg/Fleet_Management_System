package interpreter;

import java.util.HashMap;

import interpreter.commands.Command;
import interpreter.commands.IfCommand;
import interpreter.commands.LoopCommand;

public class CommandFactory {
    
    private interface Creator {
        Command create();
    }
    
    HashMap<String, Creator> map;
    
    public CommandFactory() {
        map = new HashMap<>();
        // these commands have to be recreated each time because they contain data members
        map.put("while", ()->new LoopCommand());
        map.put("if", ()->new IfCommand());
    }
    
    public Command getNewCommand(String key) {
        if (map.containsKey(key)) {
            return map.get(key).create();
        }
        return null;
    }
}
