package view;

import java.nio.file.Paths;
import java.util.Observable;
import java.util.Scanner;

import model.Model;

public class View extends Observable{
	public Input in;
	
	public class Input{
		public int choice;
		public double val;
		public Input(int choice, double val) {
			this.choice = choice;
			this.val = val;
		}
	}
	public void debug() {
		Thread t = new Thread(()->{
	        Scanner input = new Scanner(System.in);
	        while(true)
	        {
	            System.out.println("Hello please enter what to change");
	            System.out.println("1 - aileron\n" +
	                    "2 - elevators\n" +
	                    "3 - rudders\n" +
	                    "4 - throttles");
	            int choice = Integer.parseInt(input.nextLine());
	            if(choice == 0)
	            	return;
	            System.out.println("Enter value to set");
	            double val = Double.parseDouble(input.nextLine());
	            in = new Input(choice, val);
	            setChanged();
	            notifyObservers();
	            //we may need to wait till getUserCommand is used - meanwhile please don't spam:)
	        }
		});
		t.start();
	}
    public Input getUserCommand()
    {
    	return in;
    }

    public void displayData() {
    	//showing data from flightGear - only for debugging purposes
    	
    }
}
