package interpreter.commands;

import interpreter.Interpreter;
import interpreter.ProgramVar;

public class DefineVarCommand implements Command {

    /* Possible forms:
     * var x (default value is 0)
     * var x = 1
     * var x = y + 1
     * var brakes = bind "/controls/flight/speedbrake" 
     */
    @Override
    public void execute(String[] args) throws Exception {
        String arg = String.join("", args);
        String[] split = arg.split("="); // split[0] should contain variable name, split[1] contains value or bind
        
        if (split[0].length() == 0) {
            throw new Exception("empty variable name");
        }
        
        if (Interpreter.programSymTable.containsKey(split[0])) {
            throw new Exception("cannot redeclare variable");
        }
        
        Interpreter.programSymTable.put(split[0], new ProgramVar(0.0)); // initializing with 0 by default
        if (split.length > 1) { // checking if there is also an assignment
            Command c = new AssignCommand(); // will also take care of binding if necessary
            c.execute(args);
        }
    }

}
