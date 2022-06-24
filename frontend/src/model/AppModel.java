package model;


import model.algorithms.*;
import model.statlib.*;
import viewModel.*;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.scene.chart.XYChart;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AppModel {
    private FlightSettings flightSettings;
    private TimeSeries timeSeriesTrain;
    private TimeSeries timeSeriesAnomaly;
    private SimulatorPlayer sp;
    private FloatProperty timestamp;
    private int aileronIndex, throttleIndex, rudderIndex, elevatorIndex,
            yawIndex, pitchIndex, headingIndex, altitudeIndex, airspeedIndex, rollIndex;
    private DoubleProperty speed;
    private TimeSeriesAnomalyDetector anomalDetect;

    private HashMap<String, List<Integer>> mapAnomaly;

    public AppModel() {
        this.timestamp = new SimpleFloatProperty();
        this.sp = new SimulatorPlayer();
        this.speed = new SimpleDoubleProperty(1.0);
        sp.speedProperty().bind(this.speed);
    }

    public boolean isReady() {
        return (timeSeriesTrain != null && flightSettings != null && timeSeriesAnomaly != null);
    }


    public void addValueTilTime(String atribute, XYChart.Series s) {
        Platform.runLater(() -> {
            s.getData().clear();
            int time = (int) (this.timestamp.getValue() * 10);
            for (int i = 1; i <= time; i++) {
                double temp = timeSeriesAnomaly.getValAtSpecificTime(i, atribute);
                s.getData().add(new XYChart.Data(i, temp));
            }
        });

    }

    public void clearGraph(XYChart.Series s) {
        Platform.runLater(() -> {
            s.getData().clear();

        });

    }

    //for big graph
    public void addAnomalyValueAtTime(String atribute, XYChart.Series s) {
        Platform.runLater(() -> {
            float tempX, tempY;
            int time = (int) (this.timestamp.getValue() * 10);
            if (time != 0) {

                if (this.getAnomalDetect().getClass() == LinearRegression.class) {
                    tempX = this.timeSeriesAnomaly.getValAtSpecificTime(time, atribute);
                    String fe2 = ((LinearRegression) (this.anomalDetect)).getHashMap().get(atribute).feature2;
                    tempY = this.timeSeriesAnomaly.getValAtSpecificTime(time, fe2);
                    if (s.getData().size() >= 40)
                        s.getData().clear();
                    s.getData().add(new XYChart.Data(tempX, tempY));
                } else if (this.getAnomalDetect().getClass() == ZScore.class) {
                    //zscore
                    tempX = time;
                    tempY = ((ZScore) (this.anomalDetect)).getHashMap().get(atribute);
                    s.getData().add(new XYChart.Data(tempX, tempY));

                } else if (this.getAnomalDetect().getClass() == HybridAlgo.class) {
                    if (((HybridAlgo) (this.anomalDetect)).hashMapC.containsKey(atribute)) {
                        //circle
                        tempX = this.timeSeriesAnomaly.getValAtSpecificTime(time, atribute);
                        String fe2 = ((HybridAlgo) (this.anomalDetect)).hashMapC.get(atribute).feature2;
                        tempY = this.timeSeriesAnomaly.getValAtSpecificTime(time, fe2);
                        if (s.getData().size() >= 40)
                            s.getData().clear();
                        s.getData().add(new XYChart.Data(tempX, tempY));
                    } else if (((HybridAlgo) (this.anomalDetect)).hashMapL.containsKey(atribute)) {
                        //linear
                        tempX = this.timeSeriesAnomaly.getValAtSpecificTime(time, atribute);
                        String fe2 = ((HybridAlgo) (this.anomalDetect)).hashMapL.get(atribute).feature2;
                        tempY = this.timeSeriesAnomaly.getValAtSpecificTime(time, fe2);
                        if (s.getData().size() >= 50)
                            s.getData().clear();
                        s.getData().add(new XYChart.Data(tempX, tempY));
                    } else {
                        //zScore
                        tempX = time;
                        tempY = ((HybridAlgo) (this.anomalDetect)).hashMapZ.get(atribute);
                        s.getData().add(new XYChart.Data(tempX, tempY));
                    }
                }
                if (this.mapAnomaly.get(atribute) != null && this.mapAnomaly.get(atribute).contains(time)) {    //anomaly now!
                    s.setName("Anomaly Detected!!");

                } else {
                    s.setName("Not Anomaly");
                }


            }
        });

    }

    //for small graph
    public void addValueAtTime(String atribute, XYChart.Series s) {
        Platform.runLater(() -> {

            int time = (int) (this.timestamp.getValue() * 10);
            double temp = timeSeriesAnomaly.getValAtSpecificTime(time, atribute);
            s.getData().add(new XYChart.Data(time, temp));


        });

    }

    public void addLine(String atribute, XYChart.Series s) {
        Platform.runLater(() -> {
            float minX, maxX, minY, maxY;
            Line line;
            int time = (int) (this.timestamp.getValue() * 10);
            minX = this.timeSeriesTrain.getMinByFeature(atribute);
            maxX = this.timeSeriesTrain.getMaxByFeature(atribute);
            if (this.getAnomalDetect().getClass() == LinearRegression.class) {
                line = ((LinearRegression) (this.anomalDetect)).getHashMap().get(atribute).lin_reg;
            } else {
                line = ((HybridAlgo) (this.anomalDetect)).hashMapL.get(atribute).lin_reg;
            }
            minY = line.valueInTime(minX);
            maxY = line.valueInTime(maxX);
            if (Float.isNaN(minY))
                minY = 0;
            if (Float.isNaN(maxY))
                maxY = 0;
            if (maxY == 0 && maxX == 0) {
                maxX = 10;
            }
            s.getData().add(new XYChart.Data(minX, minY));
            s.getData().add(new XYChart.Data(maxX, maxY));
            s.setName("Regression Line");


        });

    }

    public void addZScoreLine(String atribute, XYChart.Series s) {
        Platform.runLater(() -> {
            float temp;
            int time = (int) (this.timestamp.getValue() * 10);
            if (this.getAnomalDetect().getClass() == ZScore.class)
                temp = ((ZScore) (this.anomalDetect)).getHashMap().get(atribute);
            else {
                //Hybrid
                temp = ((HybridAlgo) (this.anomalDetect)).hashMapZ.get(atribute);
            }
            s.getData().add(new XYChart.Data(1, temp));
            s.getData().add(new XYChart.Data(2000, temp));
            s.setName("Z-Score Line");
        });

    }

    public void paintHybrid(String atribute, XYChart.Series s) {
        Platform.runLater(() -> {

            float xcenter = ((HybridAlgo) this.anomalDetect).hashMapC.get(atribute).c.c.x;
            float ycenter = ((HybridAlgo) this.anomalDetect).hashMapC.get(atribute).c.c.y;
            double radius = ((HybridAlgo) this.anomalDetect).hashMapC.get(atribute).c.r;
            ArrayList<Point> points = new ArrayList<Point>();
            for (double angle = 0; angle < 360; angle += 0.5) {
                float x = (float) (radius * Math.cos(angle) + xcenter);
                float y = (float) (radius * Math.sin(angle) + ycenter);
                points.add(new Point(x, y));
            }

            for (Point point : points) {
                s.getData().add(new XYChart.Data(point.x, point.y));
            }
            s.setName("Circle");
        });
    }

    private String checkCsvFile(String path) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(path));
            String line = reader.readLine();
            int counter = 1;
            while (line != null) {
                String[] s;
                s = line.split(",");


                if (s.length != 42) {
                    return "flight csv row: " + counter + " expected to have 42 column";
                }
                // read next line
                counter++;
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            return e.getMessage();
        }

        return "OK"; // if all is good return OK
    }

    public FlightSettings getFlightSettings() {
        return flightSettings;
    }

    public void setFlightSettings(FlightSettings flightSettings) {
        this.flightSettings = flightSettings;
        this.sp.setFlightSettings(flightSettings);
        loadIndexes();
        this.timestamp.bindBidirectional(sp.timeStampProperty());
        this.timeSeriesTrain = new TimeSeries(flightSettings.getValidFlightPath());
    }


    private void loadIndexes() {
        this.aileronIndex = this.flightSettings.getFlightFeatureHashMap().get("aileron").getFeatureIndex();
        this.throttleIndex = this.flightSettings.getFlightFeatureHashMap().get("throttle").getFeatureIndex();
        this.rudderIndex = this.flightSettings.getFlightFeatureHashMap().get("rudder").getFeatureIndex();
        this.elevatorIndex = this.flightSettings.getFlightFeatureHashMap().get("elevator").getFeatureIndex();
        this.yawIndex = this.flightSettings.getFlightFeatureHashMap().get("yaw").getFeatureIndex();
        this.pitchIndex = this.flightSettings.getFlightFeatureHashMap().get("pitch").getFeatureIndex();
        this.headingIndex = this.flightSettings.getFlightFeatureHashMap().get("heading").getFeatureIndex();
        this.altitudeIndex = this.flightSettings.getFlightFeatureHashMap().get("altitude").getFeatureIndex();
        this.airspeedIndex = this.flightSettings.getFlightFeatureHashMap().get("airspeed").getFeatureIndex();
        this.rollIndex = this.flightSettings.getFlightFeatureHashMap().get("roll").getFeatureIndex();
    }


    public TimeSeries getTimeSeriesAnomaly() {
        return timeSeriesAnomaly;
    }

    public TimeSeries getTimeSeriesTrain() {
        return timeSeriesTrain;
    }

    public void setTimeSeriesTrain(String timeSeries) {
        this.timeSeriesTrain = new TimeSeries(timeSeries);

    }


    public String setTimeSeriesAnomaly(String timeSeries) {
        String val = checkCsvFile(timeSeries);
        if (val.equals("OK")) {
            this.timeSeriesAnomaly = new TimeSeries(timeSeries);
            this.sp.setTimeSeries(this.timeSeriesAnomaly);
        }

        return val;
    }


    public TimeSeriesAnomalyDetector getAnomalDetect() {
        return anomalDetect;
    }

    public void setAnomalDetect(TimeSeriesAnomalyDetector anomalDetect) {
        this.anomalDetect = anomalDetect;
        this.anomalDetect.learnNormal(timeSeriesTrain);
        
        // for the pre-selected algorithm, an anomaly time series does not yet exist. only when a flight is selected
        if (timeSeriesAnomaly != null) {
            this.mapAnomaly = this.anomalDetect.detect(timeSeriesAnomaly);
        }
    }

    public void play() {
        sp.play();
    }

    public float getTimestamp() {
        return timestamp.get();
    }

    public FloatProperty timestampProperty() {
        return timestamp;
    }

    public void setTimestamp(float timestamp) {
        this.timestamp.set(timestamp);
    }


    public SimulatorPlayer getSp() {
        return sp;
    }

    public void setSp(SimulatorPlayer sp) {
        this.sp = sp;
    }

    public int getAileronIndex() {
        return aileronIndex;
    }

    public void setAileronIndex(int aileronIndex) {
        this.aileronIndex = aileronIndex;
    }

    public int getThrottleIndex() {
        return throttleIndex;
    }

    public void setThrottleIndex(int throttleIndex) {
        this.throttleIndex = throttleIndex;
    }

    public int getRudderIndex() {
        return rudderIndex;
    }

    public void setRudderIndex(int rudderIndex) {
        this.rudderIndex = rudderIndex;
    }

    public int getElevatorIndex() {
        return elevatorIndex;
    }

    public void setElevatorIndex(int elevatorIndex) {
        this.elevatorIndex = elevatorIndex;
    }

    public int getYawIndex() {
        return yawIndex;
    }

    public void setYawIndex(int yawIndex) {
        this.yawIndex = yawIndex;
    }

    public int getPitchIndex() {
        return pitchIndex;
    }

    public void setPitchIndex(int pitchIndex) {
        this.pitchIndex = pitchIndex;
    }

    public int getHeadingIndex() {
        return headingIndex;
    }

    public void setHeadingIndex(int headingIndex) {
        this.headingIndex = headingIndex;
    }

    public int getAltitudeIndex() {
        return altitudeIndex;
    }

    public void setAltitudeIndex(int altitudeIndex) {
        this.altitudeIndex = altitudeIndex;
    }

    public int getAirspeedIndex() {
        return airspeedIndex;
    }

    public void setAirspeedIndex(int airspeedIndex) {
        this.airspeedIndex = airspeedIndex;
    }

    public int getRollIndex() {
        return rollIndex;
    }

    public void setRollIndex(int rollIndex) {
        this.rollIndex = rollIndex;
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
