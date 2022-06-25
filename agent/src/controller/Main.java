package controller;

import view.View;

public class Main {

	public static void main(String[] args) {
		Controller c = new Controller();
		
		if (args.length > 0 && args[0].equals("verbose")) {
		    View v = new View();
		}
		c.start();
	}

}
