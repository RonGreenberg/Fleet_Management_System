package interpreter.commands;

public interface Command {
    void execute(String[] args) throws Exception;
//    default int execute(String[] args) {
//        System.out.println(this.getClass().getSimpleName() + " " + String.join(",", args));
//        return 0;
//    }
}
