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

import model.Model;
import model.interpreter.Interpreter;

public class FrontendHandler {
    
    BufferedReader in;
    PrintWriter out;
    Model m;
    
    public FrontendHandler(Model m) {
        this.m = m;
    }
    
    public void handle(InputStream inFromFE, OutputStream outToFE) {
        try {
            in = new BufferedReader(new InputStreamReader(inFromFE));
            out = new PrintWriter(outToFE, true);
            
            String line;
            while (!(line = in.readLine()).equals(String.valueOf(Character.MIN_VALUE))) {
                String[] split = line.split(" ");
                
                switch (split[0]) {
                case "set": // format: set [var_name] [value] [callsign]
                    out.println(m.setVar(split[1], split[2], split[3]));
                    break;
                case "get": // format: get [var_name] [callsign]
                    out.println(m.getVar(split[1], split[2]));
                    break;
                case "interpret": // format: interpret [script_filename] [callsign]
                    out.println(m.interpret(split[1], split[2]));
                    break;
                case "getPlaneIDs": // either: getPlaneIDs active or getPlaneIDs all
                    out.println(m.getPlaneIDs(split[1]));
                    break;
                case "getPlaneData": // format: getPlaneData [callsign]
                    out.println(m.getPlaneData(split[1]));
                    break;
                case "getFlightIDs": // format: getFlightIDs [callsign]
                    out.println(m.getFlightIDs(split[1]));
                    break;
                case "getFlightDetails": // format: getFlightDetails [flightID]
                    out.println(m.getFlightDetails(Integer.parseInt(split[1])));
                    break;
                }
                out.println(String.valueOf(Character.MIN_VALUE));
            }
        } catch (Exception e) {}
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
