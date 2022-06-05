package interpreter.commands;

import interpreter.Interpreter;
import server.AgentServer;

public class ConnectCommand implements Command {

    @Override
    public void execute(String[] args) {
        AgentServer.send(Interpreter.clientID, "connect " + String.join(" ", args));
    }

}
