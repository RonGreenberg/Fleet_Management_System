package model;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Observable;
import java.util.concurrent.atomic.AtomicReference;

//communication with FlightGear
public class Model extends Observable {
	final static String ip = "127.0.0.1";
	final static int fgport = 5402;
	final static int myport = 5400;

    public static HashMap<String, Integer> var2Val = new HashMap<>(); //Updates 10 times in a second
    public static AtomicReference<String> CurrentCsvLine; //Updates 10 times in a second
    String flightName; // RECIVE FROM FG
    Socket fgSet; // SOCKET USED BY OUTTOFG
    PrintWriter outToFg;
    Server server; // OUTPUT SERVER
    public Model(String propertiesFileName) {
        openSetServer();
        server = new Server(myport, new FGClientHandler("F1"));
		server.start();
    }
    //============================================//
    private void openSetServer()
    {
        try {
            fgSet = new Socket(ip,fgport);
            outToFg = new PrintWriter(fgSet.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //============================================//
    public void setVal(String cmd)
    {
        outToFg.println(cmd);
    }
    //============================================//
    public void connectToFg() {
		
	}
    //============================================//
    public float getParam() {
    	
		return 0;
		
	}
    //============================================//
    public String getCurrentCsvLine() {
    	
		return null;
	}
    //============================================//
    public boolean getStatus() {
		return false;
    	
	}
    //============================================//
    public int getFlightDataStart() {
    	
		return 0;
	}
    //============================================//
    public String getFlightDataNextLine() {
    	
		return null;
	}
    //============================================//
    @Override
    protected void finalize()
    {
        try {
            fgSet.close();
            outToFg.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}