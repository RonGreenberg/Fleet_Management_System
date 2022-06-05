package model;

import java.io.*;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Observable;

//communication with FlightGear
public class Model extends Observable {

    HashMap<String, Integer> var2Val; //Updates 10 times in a second
    HashMap<String, String> props;
    String flightName;
    Socket fg;
    PrintWriter outToFg;
    public Model(String propertiesFileName) {
        flightName = "F1";
        openSetServer("127.0.0.1", 5402);
        openFlightCsvFile();
    }
    //============================================//
    private void openSetServer(String ip, int port)
    {
        try {
            fg = new Socket(ip,port);
            outToFg = new PrintWriter(fg.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    private void updateFlightCsv(Socket s)
    {

    }
    //============================================//
    @Override
    protected void finalize()
    {
        try {
            File flight = new File(flightName);
            flight.delete();
            fg.close();
            outToFg.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}