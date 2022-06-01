package interpreter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import interpreter.commands.AssignCommand;
import interpreter.commands.BindCommand;
import interpreter.commands.Command;
import interpreter.commands.ConditionParser;
import interpreter.commands.ConnectCommand;
import interpreter.commands.DefineVarCommand;
import interpreter.commands.FuncCommand;
import interpreter.commands.IfCommand;
import interpreter.commands.LoopCommand;
import interpreter.commands.OpenDataServerCommand;
import interpreter.commands.PrintCommand;
import interpreter.commands.SleepCommand;

/* ASSUMPTIONS:
 * 1. Curly brackets for if conditions and while loops can be opened in the
 *    same line or in a new line consisting of just the opening bracket.
 *    Closing brackets must populate an entire separate line.
 * 2. In conditions of if or while, there must be whitespaces between the
 *    two operands and the operator. In addition, there must be no whitespaces
 *    within the operands themselves (if they are expressions).
 *    In the print and sleep commands, however, which both take only one
 *    argument, whitespaces can be handled.
 */
public class Interpreter {
    
    private static Map<String, Command> cmdsMap;
    private static CommandFactory cmdsFac;
    public static Map<String, ProgramVar> symbolTable;
    private int currentIndex; // global index of the current line in the program
    
    // should run only once, when the class is loaded
    static {
        cmdsFac = new CommandFactory();
        cmdsMap = new HashMap<>();
        cmdsMap.put("openDataServer", new OpenDataServerCommand());
        cmdsMap.put("connect", new ConnectCommand());
        cmdsMap.put("var", new DefineVarCommand());
        cmdsMap.put("bind", new BindCommand());
        cmdsMap.put("=", new AssignCommand());
        cmdsMap.put("print", new PrintCommand());
        cmdsMap.put("sleep", new SleepCommand());    
    }
    
    public Interpreter() {
        symbolTable = new HashMap<>(); // the symbol table should be re-initialized for every new interpreter
        symbolTable.put("x", new ProgramVar(1));
        currentIndex = 0;
    }
    
    public List<String[]> lexer(List<String> lines) {
        //lines.removeIf(line->line.trim().isEmpty()); // removing empty or whitespace lines
        //String content = String.join("\n", lines);
        return lines.stream().map(s->s.trim()).filter(s->!s.isEmpty())
                .map(s->s.split("\\s+")).collect(Collectors.toList());
    }
    
    public static String replaceVarsWithValue(String arg) {
        String newArg = arg;
        for (Map.Entry<String, ProgramVar> entry : symbolTable.entrySet()) {
            String var = entry.getKey();
            ProgramVar val = entry.getValue();
            if (arg.contains(var)) {
                newArg = newArg.replace(var, String.valueOf(val.getValue()));
            }
        }
        return newArg;
    }
    
    public Command getCommandFromLine(String[] line) throws Exception {
        // first alternative: from hashmap
        if (cmdsMap.containsKey(line[0])) {
            return cmdsMap.get(line[0]);
        }
        
        // second alternative: from factory
        Command c = cmdsFac.getNewCommand(line[0]);
        if (c != null) {
            return c;
        }
        
        throw new Exception("Illegal command");
    }
    
    public String[] getArgsFromLine(String[] line) {
        return Arrays.asList(line).subList(1, line.length).toArray(new String[0]);
    }
    
    public void handleConditions(List<String[]> lines, Command conditionCmd, String[] cmdArgs) throws Exception {
        String[] line = lines.get(currentIndex);
        while (!Arrays.asList(line).contains("}")) {
            String[] args = getArgsFromLine(line);
            Command c = getCommandFromLine(line);
            if (c instanceof ConditionParser) {
                if (String.join("", line).endsWith("{")) {
                    currentIndex++;
                } else {
                    currentIndex++;
                    line = lines.get(currentIndex);
                    if (String.join("", line).equals("{")) {
                        currentIndex++;
                    }
                }
                handleConditions(lines, c, args); // nested condition
            }
            
            ((ConditionParser) conditionCmd).addCommand(c, args);
            currentIndex++;
            line = lines.get(currentIndex);
        }
    }
    
    public void parser(List<String[]> lines) throws Exception {
        currentIndex = 0;
        while (currentIndex < lines.size()) {
            String[] line = lines.get(currentIndex);
            String[] args = getArgsFromLine(line);
            Command c = getCommandFromLine(line);
            if (c instanceof ConditionParser) { // "while" or "if"
                if (String.join("", line).endsWith("{")) {
                    currentIndex++;
                } else {
                    currentIndex++;
                    line = lines.get(currentIndex);
                    if (String.join("", line).equals("{")) {
                        currentIndex++;
                    }
                }
                
                handleConditions(lines, c, args);
                c.execute(args);
            } else if (c instanceof FuncCommand) {
//                index++;
//                line = lines.get(index);
//                while (!Arrays.asList(line).contains("}")) {
//                    ((ConditionParser) c).addCommand(getCommandFromLine(line), getArgsFromLine(line));
//                    index++;
//                    line = lines.get(index);
//                }
//                c.execute(args);
            } else {
                c.execute(args);
            }
            
            currentIndex++;
        }
    }
    
    public void interpret(List<String> lines) {
        try {
            parser(lexer(lines));
        } catch (Exception e) {
            System.out.println("Exception thrown at line " + currentIndex);
            e.printStackTrace();
        }
    }
    
}
