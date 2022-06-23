package model;

import model.algorithms.TimeSeries;
import viewModel.FlightSettings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class SimulatorPlayer {
    private String ip;
    private long port;
    private DoubleProperty speed;
    private TimeSeries timeSeries;
    private DoubleProperty timeStamp;
    private FlightSettings flightSettings;
    private int maxlines;
    //private Socket socket;

    public SimulatorPlayer() {
        this.speed = new SimpleDoubleProperty(1.0);
    }

    public void setFlightSettings(FlightSettings fs) {
        this.flightSettings = fs;
        this.ip = fs.getSimulatorIp();
        this.port = fs.getSimulatorPort();
        this.timeStamp = new SimpleDoubleProperty();
    }

    public void setTimeSeries(TimeSeries ts) {
        this.timeSeries = ts;
        this.maxlines = this.timeSeries.data.size();
    }

    private ArrayList<String> castFloatArrayToString(float[] floats) {
        ArrayList<String> sVals = new ArrayList<>();
        for (int i = 0; i < floats.length; i++) {
            sVals.add("" + floats[i]);
        }
        return sVals;
    }

    public void play() {
        try {
            //PrintWriter out = new PrintWriter(socket.getOutputStream());

            double floatedMaxTime = (this.maxlines / 10 + ((double) this.maxlines % 10 / 10) + 0.1);

            while (this.timeStamp.getValue() < floatedMaxTime - 0.1) {
                int linuNum = (int) (this.timeStamp.getValue() * 10);
                float[] data = this.timeSeries.data.get(linuNum);
                String line = String.join(",", castFloatArrayToString(data));
                //out.println(line);
                //out.flush();
                Thread.sleep((long) (100 / this.speed.getValue()));
                this.timeStamp.setValue(this.timeStamp.getValue() + 0.1);
            }
            //out.close();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }


    public void createSocket() throws IOException {
        //socket = new Socket(this.ip, (int) this.port);
    }


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public long getPort() {
        return port;
    }

    public void setPort(long port) {
        this.port = port;
    }

    public Double getTimeStamp() {
        return timeStamp.get();
    }

    public DoubleProperty timeStampProperty() {
        return timeStamp;
    }

    public void setTimeStamp(Double timeStamp) {
        this.timeStamp.set(timeStamp);
    }

    public double getSpeed() {
        return speed.get();
    }

    public DoubleProperty speedProperty() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed.set(speed);
    }
}
