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

    HashMap<String, Integer> var2Val; //Updates 10 times in a second
    HashMap<String, String> props;
    String flightName;
    Socket fgSet;
    PrintWriter outToFg;
    Server server;
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