package model.interpreter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import controller.AgentServer;
import model.interpreter.commands.AssignCommand;
import model.interpreter.commands.Command;
import model.interpreter.commands.ConditionParser;
import model.interpreter.commands.ConnectCommand;
import model.interpreter.commands.DefineVarCommand;
import model.interpreter.commands.IfCommand;
import model.interpreter.commands.LoopCommand;
import model.interpreter.commands.OpenDataServerCommand;
import model.interpreter.commands.PrintCommand;
import model.interpreter.commands.SleepCommand;

/* ASSUMPTIONS:
 * 1. Curly brackets for if conditions and while loops can be opened in the
 *    same line or in a new line consisting of just the opening bracket.
 *    Closing brackets must populate an entire separate line.
 * 2. In conditions of if or while, there must be whitespaces between the
 *    two operands and the operator. In addition, there must be no whitespaces
 *    within the operands themselves (if they are expressions).
 *    In the print and sleep commands, however, which both take only one
 *    argument, whitespaces can be handled.
 * 3. Conditions of if or while can only have one comparison, without brackets.
 *    Logical operators like AND, OR are not supported.
 *    Supported operators: <,>,<=,>=,==,!=
 * 4. Declaring variables inside a while loop is not allowed.
 * 5. Nested loops and conditions are supported.
 * 6. Bind statements must occur on a line with an assignment operator,
 *    whether it declares a new variable or changes the value of an existing
 *    one.
 * 7. Refer to script.txt as the standard for how a script should look.
 */
public class Interpreter {
    
    private static CommandFactory cmdsFac;
    public static Map<String, ProgramVar> programSymTable; // symbol table that maps program variable names to values
    public static Map<String, ProgramVar> simVarsSymTable; // symbol table that maps simulator variable names to values
    private int currentIndex; // global index of the current line in the program
    private static String simVarsFileName = "resources/flightgear_vars.txt"; // file from which to load simulator var names
    public static int clientID; // ID of the client (agent) the interpreter currently communicates with
    private int status = 0; // ready
    static Object o = new Object(); // object for synchronized lock
    
    // static initialization block. similar to constructor but runs only once - when the class is loaded
    static {
        /* We use a command factory because we must generate a new command
         * reference every time we need a command. In the while and if commands,
         * for example, we must have a new reference because each instance
         * contains data members for the condition and the commands in the
         * condition block, that must be different between each while and
         * if commands. For the rest of the commands, if they happen to be
         * inside an if or while, they are saved in a map whose key is the
         * command reference. If there are multiple commands of the same type
         * inside the body of the same if/while, their references must be
         * different so they can be actually added to the map.
         */
        cmdsFac = new CommandFactory();
        cmdsFac.insertCommand("openDataServer", OpenDataServerCommand.class);
        cmdsFac.insertCommand("connect", ConnectCommand.class);
        cmdsFac.insertCommand("var", DefineVarCommand.class);
        cmdsFac.insertCommand("while", LoopCommand.class);
        cmdsFac.insertCommand("if", IfCommand.class);
        cmdsFac.insertCommand("print", PrintCommand.class);
        cmdsFac.insertCommand("sleep", SleepCommand.class);
        
        // reading the simulator variables from a file
        simVarsSymTable = new HashMap<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(simVarsFileName));
            String line;
            while ((line = reader.readLine()) != null) {
                simVarsSymTable.put(line, new ProgramVar(0.0, line));
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /* This function splits each line by whitespaces. It also removes
     * leading/trailing whitespaces and filters empty lines.
     */
    private List<String[]> lexer(List<String> lines) {
        return lines.stream().map(s->s.trim()).filter(s->!s.isEmpty())
                .map(s->s.split("\\s+")).collect(Collectors.toList());
    }
    
    /* This function is used by several commands in their execute() method
     * in order to replace variable names contained in expressions used
     * in the script (e.g. - roll / 70), before we send the expression to be
     * evaluated by shunting yard. It simply iterates over the program symbol
     * table and replaces each occurrence of a variable in the expression,
     * by its respective value.
     */
    public static String replaceVarsWithValue(String arg) {
        // get updated value if bound to simulator variable
        String newArg = arg;
        for (Map.Entry<String, ProgramVar> entry : programSymTable.entrySet()) {
            String varName = entry.getKey();
            ProgramVar var = entry.getValue();
            if (arg.contains(varName)) {
                // if the variable is bound, we also get its updated value from the simulator, just when we need it
                if (var.isBoundToSim()) {
                    String simDir = var.getSimDir();
                    String res = AgentServer.send(Interpreter.clientID, "get " + simDir);
                    var.setValue(Double.parseDouble(res));
                }
                newArg = newArg.replace(varName, String.valueOf(var.getValue()));
            }
        }
        return newArg;
    }
    
    // This 
    private String[] getArgsFromLine(String[] line) {
        return Arrays.asList(line).subList(1, line.length).toArray(new String[0]);
    }
    
    private void handleConditions(List<String[]> lines, ConditionParser conditionCmd, String[] cmdArgs) throws Exception {
        String[] line = lines.get(currentIndex);
        // handling appropriate line skips to pass the opening curly bracket
        if (String.join("", line).endsWith("{")) {
            currentIndex++;
        } else {
            currentIndex++;
            line = lines.get(currentIndex);
            if (String.join("", line).equals("{")) {
                currentIndex++;
            }
        }
        
        line = lines.get(currentIndex);
        while (!Arrays.asList(line).contains("}")) {
            String[] args = getArgsFromLine(line);
            Command c = cmdsFac.getNewCommand(line[0]);
            
            if (c == null) {
                // checking if it might be an assign command
                if (String.join("", line).contains("=")) {
                   c = new AssignCommand();
                   args = line; // we want to send the entire line
                } else {
                   throw new Exception("Illegal command");   
                }
            } else if (c instanceof ConditionParser) {
                handleConditions(lines, (ConditionParser)c, args); // nested condition
            }
            
            ((ConditionParser) conditionCmd).addCommand(c, args);
            currentIndex++;
            line = lines.get(currentIndex);
        }
    }
    
    private void parser(List<String[]> lines) throws Exception {
        currentIndex = 0;
        while (currentIndex < lines.size()) {
            String[] line = lines.get(currentIndex);
            String[] args = getArgsFromLine(line);
            Command c = cmdsFac.getNewCommand(line[0]);
            
            if (c == null) {
                // checking if it might be an assign command
                if (String.join("", line).contains("=")) {
                   c = new AssignCommand();
                   args = line; // we want to send the entire line
                } else {
                   throw new Exception("Illegal command");   
                }
            } else if (c instanceof ConditionParser) { // "while" or "if"
                handleConditions(lines, (ConditionParser)c, args);
            }
            
            c.execute(args);
            currentIndex++;
        }
    }
    
    public void interpret(List<String> lines, int clientID) {
        status = 1;
        new Thread(()->{
            synchronized(o) {
                programSymTable = new HashMap<>(); // the symbol table should be re-initialized for every new interpret call
                currentIndex = 0;
                Interpreter.clientID = clientID;
                // initialize separate thread for getting flight data
                try {
                    parser(lexer(lines));
                } catch (Exception e) {
                    System.out.println("Exception thrown at line " + currentIndex);
                    e.printStackTrace();
                }
                status = 0;
                // stop getFlightData thread
                //System.out.println(Interpreter.simVarsSymTable.get("/controls/flight/speedbrake").getValue());
            }
        }).start();
    }
    
    public int getStatus() {
        return status;
    }
    
}
