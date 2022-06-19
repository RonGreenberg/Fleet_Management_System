package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class BackendClient {
    
    static BufferedReader in;
    static PrintWriter out;
    Socket socket;
    
    public BackendClient() {
        try {
            socket = new Socket("localhost", 2000);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("Connected to backend!");
        } catch (IOException e) {
            //e.printStackTrace();
        	System.out.println("Could not connect to backend");
        	
        }
    }
    
    public static synchronized String send(String message) {
        try {
            out.println(message);
            
            String response = null, line;
            while (!(line = in.readLine()).equals(String.valueOf(Character.MIN_VALUE))) {
                if (response == null) {
                    response = "";
                }
                response += line + System.lineSeparator();
            }
            if (response != null) {
                response = response.trim(); // remove unnecessary line separator at the end   
            }
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
