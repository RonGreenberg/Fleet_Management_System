package view;
//check
import model.Model;

import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    public static void main(String[] args)
    {
        Model m = new Model(Paths.get("agent\\src\\model\\props.txt").toAbsolutePath().toString());
        Scanner input = new Scanner(System.in);
        while(true)
        {
            System.out.println("Hello please enter what to change");
            System.out.println("1 - aileron\n" +
                    "2 - elevators\n" +
                    "3 - rudders\n" +
                    "4 - throttles");
            int chose = Integer.parseInt(input.nextLine());
            System.out.println("Enter value to set");
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
