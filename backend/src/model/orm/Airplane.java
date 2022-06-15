package model.orm;

import java.util.Date;
import java.util.Set;

public class Airplane {
    private String planeID;
    private String model;
    private java.util.Date dateAdded;
    private float lastHeading;
    private float lastAltitude;
    private Set<Flight> flights;

    public Airplane() {

    }

    public String getPlaneID() {
        return planeID;
    }

    public String getModel() {
        return model;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public float getLastHeading() {
        return lastHeading;
    }

    public float getLastAltitude() {
        return lastAltitude;
    }

    public void setPlaneID(String planeID) {
        this.planeID = planeID;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public void setLastHeading(float lastHeading) {
        this.lastHeading = lastHeading;
    }

    public void setLastAltitude(float lastAltitude) {
        this.lastAltitude = lastAltitude;
    }

    public void setFlights(Set<Flight> flights) {
        this.flights = flights;
    }

    public Set<Flight> getFlights() {
        return flights;
    }
}
