package interpreter.commands;

import java.util.List;

public class FuncCommand implements Command {
    private List<Command> commands;

    @Override
    public void execute(String[] args) throws Exception {
        for (Command c : commands) {
            c.execute(args);
        }
    }
}
