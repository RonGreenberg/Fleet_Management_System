package model;

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
    
    public static String[] getPlaneData(String planeID) {
    	return BackendClient.send("getPlaneData " + planeID).split(",");
    }
    
    public static String getFlightDetails(int flightID) {
        return BackendClient.send("getFlightDetails " + flightID);    
    }
}
