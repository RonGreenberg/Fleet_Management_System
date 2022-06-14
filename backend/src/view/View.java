package view;

import java.util.Map;

public class View {
    
    public static boolean isActive = false;
    
    public View() {
        isActive = true;
    }
    
    public void printTasks(Map<String, String> tasks) {
        System.out.println("\nActive Tasks:");
        for (Map.Entry<String, String> task : tasks.entrySet()) {
            System.out.println(task.getValue() + ": " + task.getKey()); // thread name and description
        }
    }
}
