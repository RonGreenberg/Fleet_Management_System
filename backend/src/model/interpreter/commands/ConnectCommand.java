package model.interpreter.commands;

import controller.AgentServer;
import model.interpreter.Interpreter;

public class ConnectCommand implements Command {

    @Override
    public void execute(String[] args) {
        AgentServer.send(Interpreter.clientID, "connect " + String.join(" ", args));
    }

}
