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
import java.util.Scanner;

import model.Server.ClientHandler;

public class FGClientHandler implements ClientHandler{
	PrintWriter csv;
    //String fileName;


    //============================================//
    
	@Override
	public void handleClient(InputStream inFromClient) {
		new Thread(()->{
			Scanner s = new Scanner(System.in);
			String input;
			do {
				input = s.next();
			}while(!input.equals("stop"));
			s.close();
			try {
				inFromClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start();
		BufferedReader inFromFg = new BufferedReader(new InputStreamReader(inFromClient));
		while(Model.fileName == null) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		File flight = new File(Model.fileName);
		try {
			csv=new PrintWriter(new FileWriter(flight),true);
			String line;
			while((line=inFromFg.readLine()) != null) {
				csv.println(line);
				Model.currentLine.compareAndSet(Model.currentLine.get(), line);
			}
		} catch (IOException e) {System.out.println("finished");}
		finally {			
			Model.status = "finished";
			csv.close();
		}
	}
	
    //============================================//

	
}
