package view;
//check
import model.Model;
import controller.Controller;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		View v = new View();//in the future v will be bound to the frontend
		Model m = new Model("props.txt");
		System.out.println("check");
		Controller c = new Controller(v,m);
		v.addObserver(c);
		m.addObserver(c);
		v.debug();
	}
}