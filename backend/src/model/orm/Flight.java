package model.orm;

public class Flight {
    private int flightID;
    private String planeID;
    private String csvFileName;

    public Flight() {

    }

    public void setFlightID(int flightID) {
        this.flightID = flightID;
    }

    public void setCsvFileName(String csvFileName) {
        this.csvFileName = csvFileName;
    }

    public void setPlaneID(String planeID) {
        this.planeID = planeID;
    }

    public int getFlightID() {
        return flightID;
    }

    public String getCsvFileName() {
        return csvFileName;
    }

    public String getPlaneID() {
        return planeID;
    }
}
