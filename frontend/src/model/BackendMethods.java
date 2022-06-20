package model;

import java.sql.Date;
import java.util.EnumMap;
import java.util.Map;

public class BackendMethods {
    
    public static String interpret(String filename, String planeID) {
        return BackendClient.send("interpret " + filename + " " + planeID);
    }
    
    public static String[] getPlaneIDs(String activeOrAll) {
        String[] res = BackendClient.send("getPlaneIDs " + activeOrAll).split(",");
        return res;
    }
    
    public static String[] getFlightIDs(String planeID) {
        String[] res = BackendClient.send("getFlightIDs " + planeID).split(",");
        return res;
    }
    
    public static PlaneData getPlaneData(String planeID) {
    	String[] fields = BackendClient.send("getPlaneData " + planeID).split(",");
    	String[] coords = fields[3].split(";");
    	return new PlaneData(fields[0], fields[1], Date.valueOf(fields[2]), Double.parseDouble(coords[0]), Double.parseDouble(coords[1]),
    	        Double.parseDouble(fields[4]), Double.parseDouble(fields[5]), Double.parseDouble(fields[6]));
    }
    
    public static String[] getFlightDetails(int flightID) {
        return BackendClient.send("getFlightDetails " + flightID).split(",");
    }
    
    public static boolean isPlaneActive(String planeID) {
    	return Boolean.parseBoolean(BackendClient.send("isPlaneActive " + planeID));
    }
}
