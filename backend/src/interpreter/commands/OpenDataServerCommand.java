package interpreter.commands;

import interpreter.Interpreter;
import server.AgentServer;

public class OpenDataServerCommand implements Command {

    @Override
    public void execute(String[] args) {
        AgentServer.send(Interpreter.clientID, "openDataServer " + String.join(" ", args));
    }

}
