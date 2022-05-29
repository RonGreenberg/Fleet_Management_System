package view;

import model.Model;

import java.util.Scanner;

public class Main {
    public static void main(String[] args)
    {
        Model m = new Model("C:\\Users\\Aviv\\IdeaProjects\\Fleet_Management_System\\agent\\src\\model\\props.txt");
        Scanner input = new Scanner(System.in);
        while(true)
        {
            System.out.println("Hello please enter what to change");
            System.out.println("1 - aileron\n" +
                    "2 - elevator\n" +
                    "3 - rudder\n" +
                    "4 - throttle");
            int chose = Integer.parseInt(input.nextLine());
            System.out.println("enter value to set");
            double val = Double.parseDouble(input.nextLine());
            if(chose == 1)
                m.setAileron(val);
            if(chose == 2)
                m.setElevator(val);
            if(chose == 3)
                m.setRudder(val);
            if(chose == 4)
                m.setThrottle(val);
        }
    }
}
