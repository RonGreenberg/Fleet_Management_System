package controller;

import java.util.Observable;
import java.util.Observer;

import model.Model;
import view.View;
import view.View.Input;

public class Controller implements Observer
{
	View view;
	Model model;
	
	public Controller(View view, Model model) {
		this.view = view;
		this.model = model;
	}

	@Override
	public void update(Observable o, Object arg) {
		if(o == view) {
			Input input = view.getUserCommand();
			if(input.choice == 1)
                model.setAileron(input.val);
            else if(input.choice == 2)
                model.setElevator(input.val);
            else if(input.choice == 3)
                model.setRudder(input.val);
            else if(input.choice == 4)
                model.setThrottle(input.val);
            else {
            	System.out.println("value is not valid");
            	}
		}
		if(o == model) {
			//getting data from flightGear
			view.displayData();
		}
	}

}