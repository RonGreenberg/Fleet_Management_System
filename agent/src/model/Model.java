package model;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Observable;
import java.util.concurrent.atomic.AtomicReference;

//communication with FlightGear
public class Model extends Observable {
	final static String ip = "127.0.0.1";
	final static int fgport = 5402;
	final static int myport = 5400;

    public static Map<String, Integer> var2Val = Collections.synchronizedMap(new LinkedHashMap<>()); //Updates 10 times in a second
    public static AtomicReference<String> CurrentCsvLine; //Updates 10 times in a second
    String flightName; // RECIVE FROM FG
    Socket fgSet; // SOCKET USED BY OUTTOFG
    PrintWriter outToFg;
    BufferedReader responseFromFg;
    Server server; // csv10times
    public Model(String propertiesFileName) {
    	connectToFg();
        server = new Server(myport, new FGClientHandler(flightName)); // open csv file updates server in other thread
		server.start();
    }
    //============================================//
    private void connectToFg() // connects command server and set flight name
    {
        try {
            fgSet = new Socket(ip,fgport);
            outToFg = new PrintWriter(fgSet.getOutputStream(), true);
            responseFromFg = new BufferedReader(new InputStreamReader(fgSet.getInputStream()));
            outToFg.println("get /sim/user/callsign");
            // have to add "--prop:/sim/user/callsign=<Something>" in fg additional settings
            flightName = responseFromFg.readLine().split("'")[1]; //gets Flight Name
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //============================================//
    public int setVar(String cmd)
    {
        outToFg.println(cmd);
		setChanged();
		notifyObservers();
		return 0;
    }
    //============================================//
    public float getParam() {
    	
		setChanged();
		notifyObservers();
		return 0;
	}
    //============================================//
    public String getCurrentCsvLine() {
    	
		setChanged();
		notifyObservers();
		return null;
	}
    //============================================//
    public boolean getStatus() {
    	
		setChanged();
		notifyObservers();
		return false;
	}
    //============================================//
    public int getFlightDataStart() {
    	
		setChanged();
		notifyObservers();
		return 0;
	}
    //============================================//
    public String getFlightDataNextLine() {
    	
		setChanged();
		notifyObservers();
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