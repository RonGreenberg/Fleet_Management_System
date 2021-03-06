package model.orm;

import java.sql.Date;

public class Airplane {
    private String planeID;
    private String model;
    private Date dateAdded;
    private String lastPosition;
    private Double lastHeading; // so that it can hold null values
    private Double lastAltitude; // so that it can hold null values

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

    public String getLastPosition() {
        return lastPosition;
    }

    public Double getLastHeading() {
        return lastHeading;
    }

    public Double getLastAltitude() {
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

    public void setLastPosition(String lastPosition) {
        this.lastPosition = lastPosition;
    }

    public void setLastHeading(Double lastHeading) {
        this.lastHeading = lastHeading;
    }

    public void setLastAltitude(Double lastAltitude) {
        this.lastAltitude = lastAltitude;
    }

    @Override
    public String toString() {
        // note that we're using toString() method of java.sql.Date and not java.util.date, which uses the yyyy-mm-dd format.
        return String.join(",", planeID, model, dateAdded.toString(), lastPosition, String.valueOf(lastHeading), String.valueOf(lastAltitude));
    }
}
