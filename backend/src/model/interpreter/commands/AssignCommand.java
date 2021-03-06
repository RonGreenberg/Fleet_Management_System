package model.interpreter.commands;

import java.util.Arrays;

import controller.AgentServer;
import model.interpreter.Interpreter;
import model.interpreter.ProgramVar;
import model.interpreter.expression.ExpressionEvaluator;

public class AssignCommand implements Command {

    /* Possible forms:
     * x = 1 (or x=1)
     * x = y + 1
     * x = bind "/controls/flight/speedbrake" 
     */
    @Override
    public void execute(String[] args) throws Exception {
        if (Interpreter.stop) {
            throw new Exception("Interpreter killed");
        }
        
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
            // changing the value only if there's an actual change (because it might require requesting from the agent...)
            if (var.getValue() != res) {
                var.setValue(res);
                if (var.isBoundToSim()) {
                    String response = AgentServer.send(Interpreter.clientID, "set " + var.getSimDir() + " " + res);
                    if (response == null) {
                        throw new Exception("Client disconnected during interpreter");
                    }
                    if (!response.equals("ok")) {
                        throw new Exception("could not set bound variable " + split[0]);
                    }
                }
            }
        } else {
            throw new Exception("unknown expression");
        }
    }

}
