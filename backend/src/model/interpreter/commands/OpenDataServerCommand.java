package model.interpreter.commands;

import controller.AgentServer;
import model.interpreter.Interpreter;

public class OpenDataServerCommand implements Command {

    @Override
    public void execute(String[] args) throws Exception {
        if (Interpreter.stop) {
            throw new Exception("Interpreter killed");
        }
        
        AgentServer.send(Interpreter.clientID, "openDataServer " + String.join(" ", args));
    }

}
