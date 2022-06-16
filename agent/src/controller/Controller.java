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

public class Controller implements Observer
{
	//View view;
	Model model;
	
	BufferedReader inFromBE;
    PrintWriter outToBE;
    Socket socket;
    String backendIP = "79.179.43.111";
    int backendPort = 1000;
    
    public Controller() {
        model = new Model();
        
        try {
            socket = new Socket(backendIP, backendPort);
            inFromBE = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outToBE = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("Connected to backend!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void start() {
        String line;
        try {
            while (!(line = inFromBE.readLine()).equals("disconnect")) { // Backend disconnects the agent when the flight is finished
                System.out.println("Received: " + line);
                switch (line) {
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
//		if(o == view) {
//			String input = view.getUserCommand();
//		}
//		if(o == model) {
//			view.displayData();
//		}
	}
    
}