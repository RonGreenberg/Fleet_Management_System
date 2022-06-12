package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AgentServer {
    
    int port;
    boolean stop;
    static Map<Integer, Socket> connectedClients;
    
    public AgentServer(int port) {
        connectedClients = new HashMap<>();
        this.port = port;
    }
    
    public void start() {
        stop = false;
        new Thread(()->startServer()).start();
    }
    
    private void startServer() {
        try {
            ServerSocket server = new ServerSocket(port);
            server.setSoTimeout(1000);
            while (!stop) {
                try {
                    Socket client = server.accept();
                    connectedClients.put(client.getPort(), client);
                } catch (SocketTimeoutException e) {}
            }
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static String send(int clientID, String message) {
        Socket client = connectedClients.get(clientID);
        try {
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            out.println(message);
            
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
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
            connectedClients.remove(clientID); // removing client from map if connection was lost
            return null;
        }
    }
    
    public Set<Integer> getConnectedClients() {
        return connectedClients.keySet();
    }
    
    public void disconnect(int clientID) {
        Socket client = connectedClients.get(clientID);
        try {
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            out.println("disconnect");
            Thread.sleep(10);
            out.close();
            connectedClients.remove(clientID);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public void stop() {
        stop = true;
    }
}
