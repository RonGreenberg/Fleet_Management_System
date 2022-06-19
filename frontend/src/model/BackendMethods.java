package model;

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
    
    public static Map<PlaneData, String> getPlaneData(String planeID) {
    	String[] fields = BackendClient.send("getPlaneData " + planeID).split(",");
    	
    	Map<PlaneData, String> map = new EnumMap<>(PlaneData.class);
    	for (int i = 0; i < fields.length; i++) {
    	    map.put(PlaneData.values()[i], fields[i]);
    	}
    	return map;
    }
    
    public static String getFlightDetails(int flightID) {
        return BackendClient.send("getFlightDetails " + flightID);    
    }
    
    public static boolean isPlaneActive(String planeID)
    {
    	return Boolean.parseBoolean(BackendClient.send("isPlaneActive " + planeID));
    }
}
