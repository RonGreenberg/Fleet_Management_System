package interpreter.commands;

public interface Command {
    void execute(String[] args) throws Exception;
}
