package controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import model.Model;
import view.View;

public class Controller {
    
    private Map<String, Integer> activePlanes;
    private AgentServer agentServer;
    private FrontendServer frontendServer;
    private static Map<String, String> activeTasks;
    private Model model;
    
    public Controller() {
        model = new Model(this);
        activePlanes = new HashMap<>();
        agentServer = new AgentServer(1000);
        frontendServer = new FrontendServer(2000, new FrontendHandler(model));
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
                    if (status.equals("finished")) {
                        String[] planeDetails = model.getDetailsForMap(planeID);
                        float lastHeading = Float.parseFloat(planeDetails[1]);
                        float lastAltitude = Float.parseFloat(planeDetails[2]);
                        //QueriesUtil.updatePlaneDetails(planeDetails[0], lastHeading, lastAltitude); // airspeed is not stored in DB
                        
                        String fileName = model.getAndSaveFlightFile(planeID);
                        //Flight flight = new Flight();
                        //flight.setPlaneID(planeID);
                        //flight.setCsvFileName(fileName);
                        //QueriesUtil.saveFlight(flight);
                        System.out.println("Saved flight in database!");
                        
                        agentServer.disconnect(clientID);
                        activePlanes.remove(planeID);
                        System.out.println("Disconnected plane: " + planeID);
                    }
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
    
    public boolean isPlaneActive(String planeID) {
        return activePlanes.containsKey(planeID);
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
