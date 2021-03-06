package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

import model.Model;
import view.View;

public class Controller implements Observer {
	Model model;
	
	BufferedReader inFromBE;
    PrintWriter outToBE;
    Socket socket;
    String backendIP = "10.0.0.111";
    int backendPort = 1000;
    
    public Controller() {
        model = new Model();
        model.addObserver(this);
        System.out.println("Preparing to connect to backend...");
        
        try {
            Thread.sleep(10000);
            socket = new Socket(backendIP, backendPort);
            inFromBE = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outToBE = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("Connected to backend!");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public void start() {
        String line;
        try {
            while (!(line = inFromBE.readLine()).equals("disconnect")) { // Backend disconnects the agent when the flight is finished
                String cmd = line.split(" ")[0];
                if (cmd.equals("getFlightDataNextLine")) {
                    System.out.print(".");
                } else if (View.isActive) {
                    System.out.println("Received: " + line); // printing verbose output, including the command parameters
                } else {
                    System.out.println("Received: " + cmd); // printing only the command itself
                }
                
                switch (cmd) {
                case "set": // format: set [var_name] [value] (can be sent directly to FG)
                    outToBE.println(model.setVar(line));
                    break;
                case "get": // format: get [var_name] (can be sent directly to FG)
                    outToBE.println(model.getParam(line));
                    break;
                case "getFlightParamsLine":
                    outToBE.println(model.getFlightParamsLine());
                    break;
                case "getFlightDataStart":
                    outToBE.println(model.getFlightDataStart());
                    break;
                case "getFlightDataNextLine":
                    String fileLine = model.getFlightDataNextLine();
                    if (fileLine != null) { // if end of file has been reached, we don't send anything. The backend will detect a null response and finish the file transfer
                        outToBE.println(fileLine);
                    }
                    break;
                case "getStatus":
                    outToBE.println(model.getStatus());
                    break;
                }
                outToBE.println(Character.MIN_VALUE); // sending the \0 character to indicate end of response
            }			
            Runtime.getRuntime().exec("taskkill /F /IM fgfs.exe"); // killing FlightGear
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            inFromBE.close();
            outToBE.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
	@Override
	public void update(Observable o, Object arg) {
		if (View.isActive && arg != null) {
			System.out.println(arg);
		}
	}
    
}