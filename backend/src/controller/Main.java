package controller;

import view.View;

public class Main {

    public static void main(String[] args) {
        Controller c = new Controller();
        
        if (args.length > 0 && args[0].equals("print_active_tasks")) {
            System.out.println("Initializing View");
            View v = new View();
            Controller.addTask("Main Thread");
            new Thread(()->{
                Controller.addTask("View");
                while (true) {
                    v.printTasks(c.getActiveTasks());
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }   
                }
            }).start();
        }
        
        c.start();
        Controller.removeTask("Main Thread");
    }

}
