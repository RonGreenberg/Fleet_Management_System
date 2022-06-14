package view;

import java.nio.file.Paths;
import java.util.Observable;
import java.util.Scanner;

import model.Model;

public class View extends Observable{

	public String cmd;

	public void debug() {
		Thread t = new Thread(()->{
	        Scanner input = new Scanner(System.in);
	        while(true)
	        {
	            System.out.println("Please enter desired command to debug");
	            cmd = input.nextLine();
	            setChanged();
	            notifyObservers();
	        }
		});
		t.start();
	}
    public String getUserCommand()
    {
    	return cmd;
    }

    public void displayData() {
    	//showing data from flightGear - only for debugging purposes
    	
    }
}
