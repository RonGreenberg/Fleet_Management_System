package model;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

import model.Server.ClientHandler;

public class FGClientHandler implements ClientHandler{
	Model m;
	public FGClientHandler(Model m) {
		this.m = m;
	}
	@Override
	public void handleClient(InputStream inFromClient) {
		BufferedReader inFromfg=new BufferedReader(new InputStreamReader(inFromClient));
		try {
			String line;
			while(true) {
				line = inFromfg.readLine();
				m.csv.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
