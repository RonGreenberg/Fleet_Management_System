package view;
//check
import model.Model;
import controller.Controller;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		View v = new View();//in the future v will be bound to the frontend
		Model m = new Model(Paths.get("src\\model\\props.txt").toAbsolutePath().toString());//started with agent\\ but it was already in agent in my perspective. if its a problem write here and ill find a way to fix it
		Controller c = new Controller(v,m);
		v.addObserver(c);
		m.addObserver(c);
		v.debug();
	}
}
