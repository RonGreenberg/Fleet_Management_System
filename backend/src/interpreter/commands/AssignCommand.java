package interpreter.commands;

import java.util.Arrays;

import interpreter.Interpreter;
import interpreter.ProgramVar;
import interpreter.expression.ExpressionEvaluator;

public class AssignCommand implements Command {

    /* Possible forms:
     * x = 1 (or x=1)
     * x = y + 1
     * x = bind "/controls/flight/speedbrake" 
     */
    @Override
    public void execute(String[] args) throws Exception {
        String arg = String.join("", args);
        String[] split = arg.split("=");
        
        if (split[0].length() == 0) {
            throw new Exception("empty variable name");
        }
        
        if (!Interpreter.programSymTable.containsKey(split[0])) {
            throw new Exception("variable " + split[0] + " was not defined");
        }
        
        // handling bind
        if (split[1].startsWith("bind")) {
            Command c = new BindCommand();
            String[] bindArgs = {split[0], args[Arrays.asList(args).indexOf("bind") + 1]}; // var name and simulator var name
            c.execute(bindArgs);
            return;
        }
        
        // simple assignment
        Double res = ExpressionEvaluator.tryEvaluate(Interpreter.replaceVarsWithValue(split[1]));
        if (res != null) {
            ProgramVar var = Interpreter.programSymTable.get(split[0]);
            var.setValue(res);
            if (var.isBoundToSim()) {
                // call protocol send
            }
        } else {
            throw new Exception("unknown expression");
        }
    }

}
