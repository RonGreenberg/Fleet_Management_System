package model.interpreter.commands;

import model.interpreter.Interpreter;
import model.interpreter.expression.ExpressionEvaluator;

public class SleepCommand implements Command {
    
    @Override
    public void execute(String[] args) throws Exception {
        String arg = String.join("", args);
        Double res = ExpressionEvaluator.tryEvaluate(Interpreter.replaceVarsWithValue(arg));
        if (res == null) {
            throw new Exception("unknown expression");   
        }
        
        try {
            //System.out.println("start sleep");
            Thread.sleep(res.longValue());
            //System.out.println("finish sleep");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
