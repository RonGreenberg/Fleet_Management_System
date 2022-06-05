package interpreter.commands;

import interpreter.Interpreter;
import interpreter.ProgramVar;

public class BindCommand implements Command {

    // args are sent in the form of: varName, simVarName
    @Override
    public void execute(String[] args) {
        String simDir = args[1].substring(1, args[1].length() - 1); // stripping double quotes
        ProgramVar var = Interpreter.simVarsSymTable.get(simDir); // getting the object associated with this simulator variable
        Interpreter.programSymTable.put(args[0], var); // adding the same object to the program symbol table
    }

}
