package controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import view.View;

public class Controller {
    
    private Map<String, Integer> activePlanes;
    private AgentServer agentServer;
    private FrontendServer frontendServer;
    private static Map<String, String> activeTasks;
    
    public Controller() {
        activePlanes = new HashMap<>();
        agentServer = new AgentServer(1000);
        frontendServer = new FrontendServer(2000, new FrontendHandler(this));
        activeTasks = new LinkedHashMap<>(); // so that we print threads in the order they were opened
    }
    
    public void start() {
        // runs the agent server in a background thread
        agentServer.start();
        System.out.println("Waiting for clients...");
        
        // runs the frontend server in a background thread
        frontendServer.start();
        System.out.println("Waiting for frontend...");
        
        Thread monitoringThread = new Thread(()->{
            addTask("Monitoring Thread for Agents");
            while (true) {
                Set<Integer> clients = agentServer.getConnectedClients();
                for (int clientID : clients) {
                    String details = AgentServer.send(clientID, "getStatus");
                    // checking if agent closed the connection abruptly
                    if (details == null) {
                        System.out.println("Client closed connection abruptly");
                        agentServer.removeClient(clientID);
                        activePlanes.values().remove(clientID); // uses iteration under the hood
                        continue;
                    }
                    
                    String planeID = details.split(",")[0];
                    if (!activePlanes.containsKey(planeID)) {
                        activePlanes.put(planeID, clientID);
                        System.out.println("Added new planeID: " + planeID);
                    }
                    String status = details.split(",")[1];
                    // check if status is finished, perform necessary operations when flight is finished
                }
                
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }   
            }
        });
        monitoringThread.start();
        
        try {
            monitoringThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public Set<String> getActivePlaneIDs() {
        return activePlanes.keySet();
    }
    
    public int getClientIDForPlane(String planeID) {
        return activePlanes.get(planeID);
    }
    
    public static void addTask(String description) {
        if (View.isActive) { // so that we don't maintain the active tasks if there's no view
            activeTasks.put(description, Thread.currentThread().getName());   
        }
    }
    
    public static void removeTask(String description) {
        if (View.isActive) { // so that we don't maintain the active tasks if there's no view
            activeTasks.remove(description);   
        }
    }
    
    public Map<String, String> getActiveTasks() {
        return activeTasks;
    }
}
