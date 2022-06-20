package model;

import java.sql.Date;

public class PlaneData {
    private String planeID;
    private String model;
    private Date dateAdded;
    private double latitude;
    private double longitude;
    private double heading;
    private double altitude;
    private double airspeed;
    
    public PlaneData(String planeID, String model, Date dateAdded, double latitude, double longitude, double heading, double altitude,
            double airspeed) {
        this.planeID = planeID;
        this.model = model;
        this.dateAdded = dateAdded;
        this.latitude = latitude;
        this.longitude = longitude;
        this.heading = heading;
        this.altitude = altitude;
        this.airspeed = airspeed;
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

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getHeading() {
        return heading;
    }

    public double getAltitude() {
        return altitude;
    }

    public double getAirspeed() {
        return airspeed;
    }
    
}
