package interpreter.test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import interpreter.Interpreter;
import interpreter.commands.Command;
import interpreter.commands.DefineVarCommand;
import interpreter.commands.PrintCommand;

public class Test {

    public static void main(String[] args) {
//        try {
//            List<String> lines = Files.readAllLines(Paths.get("resources/script.txt"));
//            System.out.println("read file");
//            Interpreter i = new Interpreter();
//            i.interpret(lines);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        Interpreter i = new Interpreter();
        List<String> lines = new ArrayList<>();
        lines.add("while 20*x <= 50*3");
        lines.add("{");
        lines.add(" while x < 3");
        lines.add(" {");
        lines.add(" ");
        lines.add("     print x");
        lines.add("     sleep 1000");
        lines.add(" }");
        lines.add("}");
        lines.add("print x");
        i.interpret(lines);
    }

}
