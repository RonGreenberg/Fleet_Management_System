package model.interpreter;

public class ProgramVar {
    private double value;
    private String simDir;
    
    public ProgramVar(double value) {
        this.value = value;
        this.simDir = "";
    }
    
    public ProgramVar(double value, String simDir) {
        this.value = value;
        this.simDir = simDir;
    }
    
    public void setValue(double value) {
        this.value = value;
    }
    
    public double getValue() {
        return value;
    }
    
    public void setSimDir(String simDir) {
        this.simDir = simDir;
    }
    
    public String getSimDir() {
        return simDir;
    }
    
    public boolean isBoundToSim() {
        return !simDir.isEmpty();
    }
}
