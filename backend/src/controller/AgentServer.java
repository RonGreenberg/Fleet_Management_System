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
        Controller.addTask("Agent Server");
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
            Controller.removeTask("Agent Server");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /* Should be synchronized because it might be called simultaneously
     * from two threads, for example: the monitoring thread and the interpreter
     * thread. In case the same client sends a response to one thread and
     * almost immediately after sends a response to the other thread, the
     * first thread might receive that response and treat it as an additional
     * line for its own response.
     */
    public synchronized static String send(int clientID, String message) {
        Socket client = connectedClients.get(clientID);
        if (client == null) {
            System.out.println("Client unreachable");
            return null;
        }
        
        try {
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            out.println(message);
            
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            String response = null, line;
            while (!(line = in.readLine()).equals(String.valueOf(Character.MIN_VALUE))) { // the \0 character indicates end of response from agent
                if (response == null) {
                    response = "";
                }
                response += line + System.lineSeparator();
            }
            if (response != null) {
                response = response.trim(); // remove unnecessary line separator at the end   
            }
            return response;
        } catch (Exception e) {
            return null;
        }
    }
    
    public Set<Integer> getConnectedClients() {
        return connectedClients.keySet();
    }
    
    public void removeClient(int clientID) {
        connectedClients.remove(clientID);
    }
    
    public synchronized void disconnect(int clientID) {
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
