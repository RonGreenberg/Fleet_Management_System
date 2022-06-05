package model;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Observable;

//communication with FlightGear
public class Model extends Observable {
	final static String ip = "127.0.0.1";
	final static int fgport = 5402;
	final static int myport = 5400;
	PrintWriter csv;
	File flight;
	Thread getThread;
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
        getThread.start();
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
    private void openFlightCsvFile()
    {
        flight = new File(flightName + ".csv");
        try {
            flight.createNewFile();
            csv=new PrintWriter(new FileWriter(flight),true);
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
//		getThread = new Thread(()->{
//			try {
//				Socket fgGet=new Socket(ip, fgport);
//				BufferedReader inFromfg=new BufferedReader(new InputStreamReader(fgGet.getInputStream()));
//				String line;
//				while(true) {
//					line = inFromfg.readLine();
//					csv.println(line);
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		});

    }
    //============================================//
    @Override
    protected void finalize()
    {
        try {
            flight.delete();
            fgSet.close();
            outToFg.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}