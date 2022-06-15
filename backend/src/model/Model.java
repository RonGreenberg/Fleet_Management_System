package model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import controller.AgentServer;
import controller.Controller;
import model.interpreter.Interpreter;

public class Model {

    Interpreter interpreter; // using only one interpreter at a time
    Controller c;
    
    public Model(Controller c) {
        interpreter = new Interpreter();
        this.c = c;
    }
    
    public String setVar(String varName, String value, String planeID) {
        int clientID = c.getClientIDForPlane(planeID);
        String res = AgentServer.send(clientID, "set " + varName + " " + value);
        if (res == null) {
            return "Client disconnected during set request";
        }
        return res;
    }
    
    public String getVar(String varName, String planeID) {
        int clientID = c.getClientIDForPlane(planeID);
        String res = AgentServer.send(clientID, "get " + varName);
        if (res == null) {
            return "Client disconnected during get request";
        }
        return res;
    }
    
    // this function exits immediately
    public String interpret(String filename, String planeID) {
        if (interpreter.getStatus() == 1) {
            return "busy";
        }
        
        try {
            List<String> lines = Files.readAllLines(Paths.get(filename));
            interpreter.interpret(lines, c.getClientIDForPlane(planeID)); // interprets in a background thread
        } catch (IOException e) {
            e.printStackTrace();
            return "Script not found";
        }

        return "ok";
    }
    
    public String getPlaneIDs(String activeOrAll) {
        if (activeOrAll.equals("active")) {
            return String.join(",", c.getActivePlaneIDs());
        }
        
        //return String.join(",", QueriesUtil.getAllPlaneIDs());
        return null; 
    }
    
    public String[] getDetailsForMap(String planeID) {
        int clientID = c.getClientIDForPlane(planeID);
        String latitude = AgentServer.send(clientID, "get /position/latitude-deg");
        String longitude = AgentServer.send(clientID, "get /position/longitude-deg");
        String position = latitude + ";" + longitude;
        String heading = AgentServer.send(clientID, "get /instrumentation/heading-indicator/offset-deg");
        String altitudeFt = AgentServer.send(clientID, "get /instrumentation/altimeter/indicated-altitude-ft"); // frontend should convert to kft
        String airspeed = AgentServer.send(clientID, "get /instrumentation/airspeed-indicator/indicated-speed-kt");
        return new String[] { position, heading, altitudeFt, airspeed };
    }
    
    public String getPlaneData(String planeID) {
        //Airplane plane = QueriesUtil.getPlaneData(planeID);
        if (c.isPlaneActive(planeID)) {
            //return String.join(",", planeID, plane.getModel(), plane.getDateAdded(), getDetailsForMap(planeID));
            return null;
        } else {
            // use Airplane's toString() method that will return only values separated by commas, and separates latitude and logitude using semi-colon
            // append 0 at the end to indicate airspeed of 0
            return null;
        }
    }
    
    public String getFlightIDs(String planeID) {
        // fetch from DB, return as comma-separated values
        return null;
    }
    
    public String getFlightDetails(int flightID) {
        // fetch from DB, return as comma-separated values
        return null;
    }
    
    public String getAndSaveFlightFile(String planeID) {
        String datetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy_HH.mm"));
        String fileName = "flight_files/" + planeID + "_" + datetime + ".csv";
        File f = new File(fileName);
        
        System.out.println("Receiving flight data...");
        int clientID = c.getClientIDForPlane(planeID);
        AgentServer.send(clientID, "getFlightDataStart");
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(f));
            String response;
            do {
                response = AgentServer.send(clientID, "getFlightDataNextLine");
                if (response != null) {
                    writer.println(response);
                }
            } while (response != null);
            writer.close();
            System.out.println("Finished receiving flight data!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return fileName;
    }
}
