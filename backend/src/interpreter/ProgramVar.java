package interpreter;

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
    
    public double getValue() {
        return value;
    }
}
