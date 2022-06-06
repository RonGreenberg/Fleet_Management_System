package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import model.Server.ClientHandler;

public class FGClientHandler implements ClientHandler{
	PrintWriter csv;
    String flightName;

	public FGClientHandler() {
	    flightName = "F1";
        openFlightCsvFile();
	}
	
    //============================================//

	private void openFlightCsvFile() {
        File flight = new File(flightName + ".csv");
        try {
            flight.createNewFile();
            csv=new PrintWriter(new FileWriter(flight),true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //============================================//
    
	@Override
	public void handleClient(InputStream inFromClient) {
		BufferedReader inFromfg=new BufferedReader(new InputStreamReader(inFromClient));
		try {
			String line;
			while((line=inFromfg.readLine()) != null)
				csv.println(line);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    //============================================//

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		csv.close();
	}
	
}
