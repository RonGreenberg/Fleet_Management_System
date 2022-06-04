package interpreter.test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import interpreter.Interpreter;

public class Test {

    // requires removing all lines related to the AgentServer in Interpreter.java
    public static void main(String[] args) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("resources/my_test_script.txt"));
            Interpreter i = new Interpreter();
            i.interpret(lines, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
//        Interpreter i = new Interpreter();
//        List<String> lines = new ArrayList<>();
//        lines.add("var x = bind \"/controls/flight/speedbrake\"");
//        lines.add("x = 1");
//        lines.add("while 20*x <= 50*3");
//        lines.add("{");
//        lines.add(" print x");
//        lines.add(" while x < 3");
//        lines.add(" {");
//        lines.add(" ");
//        lines.add("     print x");
//        lines.add("     sleep 1000");
//        lines.add("     x =x+ 1");
//        lines.add(" }");
//        lines.add(" print x");
//        lines.add(" x= x +1");
//        lines.add("}");
//        lines.add("print x");
//        lines.add("print \"done\"");
//        i.interpret(lines, 0);
        //i.interpret(lines);
    }

}
