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

	String callsign; // received from FG
    public static String status = "flying"; // should be updated to "finished" in FGClientHandler when the flight is finished
    BufferedReader fileReader = null; // for reading the csv file when the backend asks for it
    public static AtomicReference<String> currentLine = new AtomicReference<>(""); //Updates 10 times in a second
    public static String fileName;
    Socket fgSet; // SOCKET USED BY OUTTOFG
    PrintWriter outToFg;
    BufferedReader responseFromFg;
    Server server; // csv10times
    public Model() {
    	server = new Server(myport, new FGClientHandler()); // open csv file updates server in other thread
    	server.start();
    	connectToFg();
    	fileName = "flightsFiles/" + callsign + ".csv";
    }
    //============================================//
    private void connectToFg() // connects command server and set flight name
    {
        while (true) {
        	System.out.println("trying to connect to fg");
            try {
                fgSet = new Socket(ip, fgport);
                System.out.println("Connected to FG!");
                outToFg = new PrintWriter(fgSet.getOutputStream(), true);
    			responseFromFg = new BufferedReader(new InputStreamReader(fgSet.getInputStream())); // wrap a new BufferedReader on the same FG socket for get requests
                outToFg.println("data"); // switching FG to data mode to receive raw values
                callsign = getParam("get /sim/user/callsign"); // gets plane name (have to add "--prop:/sim/user/callsign=<Something>" in fg additional settings)
                System.out.println("Callsign: " + callsign);
                return;
            } catch (IOException e) {
                //e.printStackTrace();
            }
            try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
    }
    //============================================//
    public String setVar(String cmd)
    {
        outToFg.println(cmd);
		setChanged();
		notifyObservers();
		return "ok";
    }
    //============================================//
    public String getParam(String getCmd) {
		String str = null;

        try {
        	outToFg.println(getCmd); // send get cmd to FG
			str = responseFromFg.readLine().trim();
		} catch (IOException e) {
			e.printStackTrace();
		}
		setChanged();
		notifyObservers("Returning: " + str);
		return str;
	}
    //============================================//
    public String getFlightParamsLine() {
		return currentLine.get();
	}
    //============================================//
    public String getStatus() {
        // return format: [callsign],[status]
		setChanged();
		notifyObservers("Returning: " + callsign + "," + status);
		return callsign + "," + status;
	}
    //============================================//
    public String getFlightDataStart() {
    	try {
            fileReader = new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
		setChanged();
		notifyObservers();
		return "ok";
	}
    //============================================//
    public String getFlightDataNextLine() {
    	String line = null;
        try {
            line = fileReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        if (line == null) {
            try {
                fileReader.close(); // closing the reader if end of file has been reached
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
		setChanged();
		notifyObservers();
		return line;
	}
    //============================================//
    @Override
    protected void finalize()
    {
        try {
        	responseFromFg.close();
            outToFg.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}