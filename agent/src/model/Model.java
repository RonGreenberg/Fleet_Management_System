package model;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Observable;

//communication with FlightGear
public class Model extends Observable {
	final String ip = "127.0.0.1";
	final int port = 5402;
	
	Thread setThread, getThread;
    HashMap<String, Integer> var2Val; //Updates 10 times in a second
    HashMap<String, String> props;
    String flightName;
    Socket fgSet, fgGet;
    PrintWriter outToFg;
    public Model(String propertiesFileName) {
        flightName = "F1";
        openFlightCsvFile();
        openSetServer();
        updateFlightCsv();
        setThread.start();
        getThread.start();
    }
    //============================================//
    private void openSetServer()
    {
    	setThread = new Thread(()->{
	        try {
	            fgSet = new Socket(ip,port);
	            outToFg = new PrintWriter(fgSet.getOutputStream(), true);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		});
    }
    //============================================//
    private void openFlightCsvFile()
    {
        File flight = new File(flightName + ".csv");
        try {
            flight.createNewFile();
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
    private void updateFlightCsv()
    {
    	//open thread that open a server that get information from flight gear 10 times per second
		getThread = new Thread(()->{
			try {
				Socket fgGet=new Socket(ip, port);
				BufferedReader inFromfg=new BufferedReader(new InputStreamReader(fgGet.getInputStream()));
				PrintWriter csv=new PrintWriter(new FileWriter(flightName + ".csv"),true);//csv will get closed at the end of the program by the gc dont worry about it
				String line;
				while(true) {
					line = inFromfg.readLine();
					csv.println(line);
					Thread.sleep(100);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});

    }
    //============================================//
    @Override
    protected void finalize()
    {
        try {
            File flight = new File(flightName);
            flight.delete();
            fgSet.close();
            outToFg.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}