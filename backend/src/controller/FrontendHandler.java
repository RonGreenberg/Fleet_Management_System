package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import model.interpreter.Interpreter;

public class FrontendHandler {
    
    BufferedReader in;
    PrintWriter out;
    Controller c;
    Interpreter interpreter; // using only one interpreter at a time
    
    public FrontendHandler(Controller c) {
        this.c = c;
        interpreter = new Interpreter();
    }
    
    public void handle(InputStream inFromFE, OutputStream outToFE) {
        try {
            in = new BufferedReader(new InputStreamReader(inFromFE));
            out = new PrintWriter(outToFE, true);
            
            String line;
            while (!(line = in.readLine()).equals(String.valueOf(Character.MIN_VALUE))) {
                String[] split = line.split(" ");
                
                switch (split[0]) {
                case "interpret":
                    out.println(interpret(split[1], split[2]));
                    break;
                case "getPlaneIDs":
                    out.println(getPlaneIDs(split[1]));
                    break;
                }
                out.println(String.valueOf(Character.MIN_VALUE));
            }
        } catch (Exception e) {}
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
        }   

        return "ok";
    }
    
    public String getPlaneIDs(String activeOrAll) {
        if (activeOrAll.equals("active")) {
            return String.join(",", c.getActivePlaneIDs());
        }
        return null; // we will change this to handle all planes using the DB 
    }
    
    public void close() {
        try {
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
