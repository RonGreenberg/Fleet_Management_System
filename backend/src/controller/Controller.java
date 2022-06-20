package controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import model.Model;
import model.orm.Flight;
import model.orm.HibernateUtil;
import model.orm.QueriesUtil;
import view.View;

public class Controller {
    
    private Map<String, Integer> activePlanes;
    private AgentServer agentServer;
    private FrontendServer frontendServer;
    private static Map<String, String> activeTasks;
    private Model model;
    private int agentServerPort = 1000;
    private int frontendServerPort = 2000;
    
    public Controller() {
        model = new Model(this);
        activePlanes = new HashMap<>();
        agentServer = new AgentServer(agentServerPort);
        frontendServer = new FrontendServer(frontendServerPort, new FrontendHandler(model));
        activeTasks = new LinkedHashMap<>(); // so that we print threads in the order they were opened
    }
    
    public void start() {
        // runs the agent server in a background thread
        agentServer.start();
        System.out.println("Waiting for clients...");
        
        // runs the frontend server in a background thread
        frontendServer.start();
        System.out.println("Waiting for frontend...");
        
        HibernateUtil util = new HibernateUtil(); // just to open the session factory when the application starts
        
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
                        break; // breaking out of the loop, because if this was the last client, there could be all sorts of problems with the loop now...
                    }
                    
                    String planeID = details.split(",")[0];
                    if (!activePlanes.containsKey(planeID)) {
                        activePlanes.put(planeID, clientID);
                        System.out.println("Added new planeID: " + planeID);
                    }
                    
                    String status = details.split(",")[1];
                    if (status.equals("finished")) {
                        model.stopInterpreter(); // killing the interpreter in case it is still running
                        activePlanes.remove(planeID); // immediately removing from active planes so that it would be reflected in the frontend (and it won't have to wait until the file transfer finishes)
                        
                        String[] planeDetails = model.getDetailsForMap(planeID);
                        Double lastHeading = Double.parseDouble(planeDetails[1]);
                        Double lastAltitude = Double.parseDouble(planeDetails[2]);
                        QueriesUtil.updatePlaneDetails(planeID, planeDetails[0], lastHeading, lastAltitude); // airspeed is not stored in DB
                        
                        String fileName = model.getAndSaveFlightFile(planeID);
                        Flight flight = new Flight();
                        flight.setPlaneID(planeID);
                        flight.setCsvFileName(fileName);
                        QueriesUtil.saveFlight(flight);
                        System.out.println("Saved flight in database!");
                        
                        agentServer.disconnect(clientID);
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
