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
import model.orm.Airplane;
import model.orm.Flight;
import model.orm.QueriesUtil;

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
    
    public String getFlightParamsLine(String planeID) {
        int clientID = c.getClientIDForPlane(planeID);
        String res = AgentServer.send(clientID, "getFlightParamsLine");
        if (res == null) {
            return "Client disconnected during get line request";
        }
        return res;
    }
    
    // this function exits immediately
    public String interpret(String filename, String planeID) {
        if (interpreter.getStatus() == 1) {
            return "busy";
        }
        
        try {
            Interpreter.stop = false; // in case someone killed the interpreter earlier, we want to be able to start it again...
            List<String> lines = Files.readAllLines(Paths.get(filename));
            interpreter.interpret(lines, c.getClientIDForPlane(planeID)); // interprets in a background thread
        } catch (IOException e) {
            e.printStackTrace();
            return "Script not found";
        }

        return "ok";
    }
    
    public void stopInterpreter() {
        Interpreter.stop = true;
    }
    
    public String getPlaneIDs(String activeOrAll) {
        if (activeOrAll.equals("active")) {
            return String.join(",", c.getActivePlaneIDs());
        }
        
        return String.join(",", QueriesUtil.getAllPlaneIDs()); // returning all planes otherwise
    }
    
    public String isPlaneActive(String planeID) {
        return c.isPlaneActive(planeID) ? "true" : "false";
    }
    
    public String[] getDetailsForMap(String planeID) {
        int clientID = c.getClientIDForPlane(planeID);
        String latitude = AgentServer.send(clientID, "get /position/latitude-deg");
        String longitude = AgentServer.send(clientID, "get /position/longitude-deg");
        String position = latitude + ";" + longitude;
        String heading = AgentServer.send(clientID, "get /instrumentation/heading-indicator/indicated-heading-deg");
        String altitudeFt = AgentServer.send(clientID, "get /instrumentation/altimeter/indicated-altitude-ft"); // frontend should convert to kft
        String airspeed = AgentServer.send(clientID, "get /instrumentation/airspeed-indicator/indicated-speed-kt");
        return new String[] { position, heading, altitudeFt, airspeed };
    }
    
    public String getPlaneData(String planeID) {
        Airplane plane = QueriesUtil.getPlaneData(planeID);
        if (c.isPlaneActive(planeID)) {
            // getting up-to-date details for the live map, and returning them along with the constant ones
            return planeID + "," + plane.getModel() + "," + plane.getDateAdded().toString() + "," + String.join(",", getDetailsForMap(planeID));
        } else {
            return plane.toString() + ",0.0"; // appending 0 at the end to indicate airspeed of 0
        }
    }
    
    public String getFlightIDs(String planeID) {
        List<Integer> flightIDs = QueriesUtil.getFlightIDs(planeID);
        StringBuilder sb = new StringBuilder();
        for (int flightID : flightIDs) {
            sb.append(flightID + ",");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1); // removing the last unnecessary "," (only if it was actually added - we might have never entered the loop if the list is empty)   
        }
        
        return sb.toString();
    }
    
    public String getFlightDetails(int flightID) {
        Flight f = QueriesUtil.getFlightDetails(flightID);
        return f.toString();
    }
    
    public String getAndSaveFlightFile(String planeID) {
        String datetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy_HH.mm"));
        String fileName = "flight_files/" + planeID + "_" + datetime + ".csv";
        File f = new File(fileName);
        
        System.out.println("Receiving flight data...");
        int clientID = c.getClientIDForPlane(planeID);
        AgentServer.send(clientID, "getFlightDataStart");
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(f), true);
            String response;
            do {
                response = AgentServer.send(clientID, "getFlightDataNextLine");
                if (response != null) {
                    writer.println(response); // there will be an extra blank line at the end of the file, which the frontend should handle
                }
            } while (response != null);
            writer.close();
            System.out.println("Finished receiving flight data!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return f.getAbsolutePath();
    }
}
