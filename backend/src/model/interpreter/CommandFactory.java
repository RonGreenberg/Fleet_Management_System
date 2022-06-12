package model.interpreter;

import java.util.HashMap;

import model.interpreter.commands.Command;

public class CommandFactory {
    
    private interface Creator {
        Command create();
    }
    
    HashMap<String, Creator> map;
    
    public CommandFactory() {
        map = new HashMap<>();
    }
    
    public void insertCommand(String key, Class<? extends Command> c) {
        map.put(key, new Creator() {
            @Override
            public Command create() {
                try {
                    return c.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }
    
    public Command getNewCommand(String key) {
        if (map.containsKey(key)) {
            return map.get(key).create();
        }
        return null;
    }
}
