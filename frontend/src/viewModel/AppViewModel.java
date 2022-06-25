package viewModel;

import model.AppModel;
import model.algorithms.HybridAlgo;
import model.algorithms.LinearRegression;
import model.algorithms.TimeSeries;
import model.algorithms.TimeSeriesAnomalyDetector;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;


public class AppViewModel {

    private AppModel appModel;

    private DoubleProperty timeStamp;
    private StringProperty algoFile, csvFile, settingFile;
    private ListProperty<String> listView;
    private FloatProperty altitude, yaw, roll, pitch, airspeed, heading;
    private FloatProperty aileron, elevator, rudder, throttle, centerCircle;
    private FloatProperty minThrottle, maxThrottle, minRudder, maxRudder;
    private FloatProperty minElevator, maxElevator, minAileron, maxAileron;
    private DoubleProperty maxTimeLine;

    private StringProperty nameofFeatureA, nameofFeatureB, nameFromList;
    private StringProperty spLabelCoralFeatureA, spLabelCoralFeatureB, spAnomalyClass;
    private DoubleProperty speed;

    Thread startThread;

    public AppViewModel(AppModel am) {
        //joystick
        initJoyStickProperties();
        //dashbord
        initDashBoardProperties();
        //graph
        graphInit();

        //menu button
        algoFile = new SimpleStringProperty();

        csvFile = new SimpleStringProperty();
        settingFile = new SimpleStringProperty();

        csvFile.addListener(v -> {
            createTimeSeries();
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setHeaderText("Performing Anomaly Detection...");
            alert.setTitle("Please Wait");
            alert.getDialogPane().lookupButton(ButtonType.OK).setDisable(true); // disabling OK button
            //alert.setOnCloseRequest(e->e.consume()); // preventing closing the alert with X
            alert.show();
            Platform.runLater(()-> {
                appModel.setMapAnomaly(appModel.getAnomalDetect().detect(appModel.getTimeSeriesAnomaly())); // performing anomaly detection on the csv file of the selected flight)
                alert.close();
            });
        });
        settingFile.addListener(v -> createSettings());

        algoFile.addListener(v -> loadAlgo());

        //list
        ObservableList<String> observableList = FXCollections.observableArrayList();
        this.listView = new SimpleListProperty<>(observableList);

        this.speed = new SimpleDoubleProperty(1.0);
        this.maxTimeLine = new SimpleDoubleProperty();
        this.timeStamp = new SimpleDoubleProperty();
        this.appModel = am;
        this.appModel.speedProperty().bind(this.speed);
        this.timeStamp.bindBidirectional(am.timestampProperty());
        this.timeStamp.addListener(v -> updateParams());
        
        String path = "frontend/resources/settings.json";
        if (!Files.exists(Paths.get(path))) {
            path = "resources/settings.json"; // the path in the settings file might not need to contain the frontend parent folder
        }
        settingFile.setValue(path);
        algoFile.setValue(am.getFlightSettings().getChosenAlgorithmPath()); // pre-selecting an algorithm
    }


    private void graphInit() {

        this.nameofFeatureB = new SimpleStringProperty();
        this.nameofFeatureA = new SimpleStringProperty();
        this.spAnomalyClass = new SimpleStringProperty();
        nameFromList = new SimpleStringProperty();
        nameFromList.addListener(v ->
        {
            nameofFeatureA.setValue(nameFromList.getValue());
            if (appModel.getAnomalDetect() != null && appModel.getAnomalDetect().getClass() == LinearRegression.class) {
                String str = ((LinearRegression) appModel.getAnomalDetect()).getHashMap().get(this.nameofFeatureA.getValue()).feature2;
                if (str != null)
                    nameofFeatureB.setValue(str);
            } else if (appModel.getAnomalDetect() != null && appModel.getAnomalDetect().getClass() == HybridAlgo.class) {
                if (((HybridAlgo) appModel.getAnomalDetect()).hashMapL.containsKey(nameFromList.getValue())) {
                    String str = ((HybridAlgo) appModel.getAnomalDetect()).hashMapL.get(this.nameofFeatureA.getValue()).feature2;
                    nameofFeatureB.setValue(str);
                } else if (((HybridAlgo) appModel.getAnomalDetect()).hashMapC.containsKey(nameFromList.getValue())) {
                    String str = ((HybridAlgo) appModel.getAnomalDetect()).hashMapC.get(this.nameofFeatureA.getValue()).feature2;
                    nameofFeatureB.setValue(str);
                } else {
                    //in hybrid but zScore
                    nameofFeatureB.setValue("");
                }

            } else {
                //zscore
                nameofFeatureB.setValue("");
            }
        });


        spLabelCoralFeatureA = new SimpleStringProperty("");
        spLabelCoralFeatureB = new SimpleStringProperty("");
        spAnomalyClass = new SimpleStringProperty("");
    }


    private void updateParams() {
        if (this.timeStamp.getValue() == 0) {
            resetFlightProp();

        } else {
            int time = (int) (this.timeStamp.getValue() * 10);

            this.yaw.setValue(this.appModel.getTimeSeriesAnomaly().getValAtSpecificTime(time, this.appModel.getYawIndex()));
            this.pitch.setValue(this.appModel.getTimeSeriesAnomaly().getValAtSpecificTime(time, this.appModel.getPitchIndex()));
            this.roll.setValue(this.appModel.getTimeSeriesAnomaly().getValAtSpecificTime(time, this.appModel.getRollIndex()));
            this.heading.setValue(180 - this.appModel.getTimeSeriesAnomaly().getValAtSpecificTime(time, this.appModel.getHeadingIndex()));
            this.altitude.setValue(this.appModel.getTimeSeriesAnomaly().getValAtSpecificTime(time, this.appModel.getAltitudeIndex()));
            this.airspeed.setValue(this.appModel.getTimeSeriesAnomaly().getValAtSpecificTime(time, this.appModel.getAirspeedIndex()));
            this.throttle.setValue(this.appModel.getTimeSeriesAnomaly().getValAtSpecificTime(time, this.appModel.getThrottleIndex()));
            this.rudder.setValue(this.appModel.getTimeSeriesAnomaly().getValAtSpecificTime(time, this.appModel.getRudderIndex()));
            this.aileron.setValue(this.appModel.getTimeSeriesAnomaly().getValAtSpecificTime(time, this.appModel.getAileronIndex()));
            this.elevator.setValue(this.appModel.getTimeSeriesAnomaly().getValAtSpecificTime(time, this.appModel.getElevatorIndex()));
        }
    }

    public void changeList( FlightSettings fs){
        TimeSeries temp= new TimeSeries(fs.getValidFlightPath());
    this.listView.addAll(temp.namesOfFeatures);
}

    private void initJoyStickProperties() {
        this.aileron = new SimpleFloatProperty();
        this.elevator = new SimpleFloatProperty();
        this.throttle = new SimpleFloatProperty();
        this.rudder = new SimpleFloatProperty();
        this.centerCircle = new SimpleFloatProperty();
        this.minThrottle = new SimpleFloatProperty();
        this.maxThrottle = new SimpleFloatProperty();
        this.minRudder = new SimpleFloatProperty();
        this.maxRudder = new SimpleFloatProperty();
        this.minElevator = new SimpleFloatProperty();
        this.maxElevator = new SimpleFloatProperty();
        this.minAileron = new SimpleFloatProperty();
        this.maxAileron = new SimpleFloatProperty();
    }

    public boolean isInflight() {
        //return (this.startThread != null && this.startThread.isAlive());
return true;
    }

    private void initDashBoardProperties() {
        //dashboard
        this.airspeed = new SimpleFloatProperty();
        this.heading = new SimpleFloatProperty();
        this.yaw = new SimpleFloatProperty();
        this.pitch = new SimpleFloatProperty();
        this.roll = new SimpleFloatProperty();
        this.altitude = new SimpleFloatProperty();
    }

    private void loadAlgo() {
        try {
            File f = new File(this.getAlgoFile().getValue());
            String s = f.getName();
            String path = "file://" + f.toURL();
            URL[] urls = new URL[1];
            urls[0] = new URL(path);
            int ch = 0;
            String name = "";
            while (s.charAt(ch) != '.') {
                name += s.charAt(ch);
                ch++;
            }
            System.out.println("algo");
            this.spAnomalyClass.setValue(name + " Algorithm.");
            URLClassLoader urlClassLoader = URLClassLoader.newInstance(urls);
            Class<?> c = urlClassLoader.loadClass("model.algorithms." + name);
            TimeSeriesAnomalyDetector ad = (TimeSeriesAnomalyDetector) c.newInstance();
            appModel.setAnomalDetect(ad);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {

            e.printStackTrace();
            System.out.println(e);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    private void createSettings() {
       resetFlightProp();
        FlightSettings fs = new FlightSettings(settingFile.getValue());

        try {
            fs.loadSettings();
            appModel.setFlightSettings(fs);
            this.maxAileron.setValue(fs.getFlightFeatureHashMap().get("aileron").getMax());
            this.minAileron.setValue(fs.getFlightFeatureHashMap().get("aileron").getMin());
            this.maxElevator.setValue(fs.getFlightFeatureHashMap().get("elevator").getMax());
            this.minElevator.setValue(fs.getFlightFeatureHashMap().get("elevator").getMin());
            this.maxThrottle.setValue(fs.getFlightFeatureHashMap().get("throttle").getMax());
            this.minThrottle.setValue(fs.getFlightFeatureHashMap().get("throttle").getMin());
            this.maxRudder.setValue(fs.getFlightFeatureHashMap().get("rudder").getMax());
            this.minRudder.setValue(fs.getFlightFeatureHashMap().get("rudder").getMin());
            this.speed.setValue(fs.getSimulatorSpeed());
            changeList(fs);
            //myGoodAlert("Settings.json");
        } catch (Exception exception) {
            myErrorAlert("Choose Flight Settings.json file ERROR", exception.toString());
        }
    }


    private void createTimeSeries() {
        // call pause
        String s = this.csvFile.getValue();
        String check = this.appModel.setTimeSeriesAnomaly(s);
        if (check.equals("OK")) {
            int dataSize = this.appModel.getTimeSeriesAnomaly().data.size();
            this.listView.clear();
            this.listView.addAll(appModel.getTimeSeriesAnomaly().namesOfFeatures);
            this.maxTimeLine.setValue((dataSize / 10 + ((double) dataSize % 10 / 10) + 0.1));
            resetFlightProp();
            //myGoodAlert("csv flight");
        } else {
            myErrorAlert("Choose Flight Csv file ERROR", check);
        }

    }

    private void myErrorAlert(String title, String text) {
        Alert a1 = new Alert(Alert.AlertType.ERROR, text, ButtonType.CLOSE);
        a1.setTitle(title);
        a1.show();
    }

    private void myGoodAlert(String fileName) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Managed To Load " + fileName + " File", ButtonType.OK);
        alert.show();
    }


    public void resetFlightProp() {
        //joystick
        this.timeStamp.set(0);

        this.aileron.set(centerCircle.getValue());
        this.elevator.set(centerCircle.getValue());
        this.throttle.set((minThrottle.getValue() + maxThrottle.getValue()) / 2);
        this.rudder.set((minRudder.getValue() + maxRudder.getValue()) / 2);

        //dashboard

        this.airspeed.set(0);
        this.heading.set(0);
        this.yaw.set(0);
        this.pitch.set(0);
        this.roll.set(0);
        this.altitude.set(0);
        this.speed.set(1);

    }


    public void setTimeSeries(String timeSeries) {
        this.appModel.setTimeSeriesAnomaly((timeSeries));
    }


    public void play() {
        if (!appModel.isReady()) {
            myErrorAlert("Start ERROR", "Flight is missing at least 1 of the followings:\n1) Settings.json file\n2) flight.csv file");
        } else if (this.startThread != null && this.startThread.isAlive()) {
            myErrorAlert("Start Error", "Flight is Running\n" +
                    "please press 'Pause' Or 'Stop' flight and run again");
        } else {
            try {
                this.appModel.getSp().createSocket();
                this.startThread = new Thread(() -> {
                    this.appModel.play();
                });
                this.startThread.start();
            } catch (Exception e) {
                myErrorAlert("Start ERROR", e.getMessage() + "\nPlease make sure simulator is set\n" +
                        "IP: " + this.appModel.getSp().getIp() + "\nPORT: " + this.appModel.getSp().getPort() + "\n*" +
                        "Parameters are taken from the settings file given by you");
            }
        }
    }


    public void pause() {
        if (this.startThread != null)
            this.startThread.interrupt();
    }

    public void runNext(double value) {
        double newTimeStamp = (this.timeStamp.getValue() + value) >= this.maxTimeLine.getValue() ?
                this.maxTimeLine.getValue() : this.timeStamp.getValue() + value;
        setTimeStamp(newTimeStamp);

    }

    public void runBack(double value) {
        double val = this.timeStamp.getValue() - value;
        if (val <= 0) {
            setTimeStamp(0);
        } else {
            setTimeStamp(val);
        }

    }

    public void stop() {
        if (this.startThread != null)
            this.startThread.interrupt();
        resetFlightProp();
    }


    public AppModel getAppModel() {
        return appModel;
    }

    public void setAppModel(AppModel appModel) {
        this.appModel = appModel;
    }

    public float getAileron() {
        return aileron.get();
    }

    public FloatProperty aileronProperty() {
        return aileron;
    }

    public void setAileron(float aileron) {
        this.aileron.set(aileron);
    }

    public float getElevator() {
        return elevator.get();
    }

    public FloatProperty elevatorProperty() {
        return elevator;
    }

    public void setElevator(float elevator) {
        this.elevator.set(elevator);

    }

    public float getRudder() {
        return rudder.get();
    }

    public FloatProperty rudderProperty() {
        return rudder;
    }

    public void setRudder(float rudder) {
        this.rudder.set(rudder);
    }

    public float getThrottle() {
        return throttle.get();
    }

    public FloatProperty throttleProperty() {
        return throttle;
    }

    public void setThrottle(float throttle) {
        this.throttle.set(throttle);
    }

    public float getAltitude() {
        return altitude.get();
    }

    public FloatProperty altitudeProperty() {
        return altitude;
    }

    public void setAltitude(float altitude) {
        this.altitude.set(altitude);
    }

    public float getAirspeed() {
        return airspeed.get();
    }

    public FloatProperty airspeedProperty() {
        return airspeed;
    }

    public void setAirspeed(float airspeed) {
        this.airspeed.set(airspeed);
    }

    public float getHeading() {
        return heading.get();
    }

    public FloatProperty headingProperty() {
        return heading;
    }

    public void setHeading(float heading) {
        this.heading.set(heading);
    }

    public float getYaw() {
        return yaw.get();
    }

    public FloatProperty yawProperty() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw.set(yaw);
    }

    public double getRoll() {
        return roll.get();
    }

    public FloatProperty rollProperty() {
        return roll;
    }

    public void setRoll(float roll) {
        this.roll.set(roll);
    }

    public float getPitch() {
        return pitch.get();
    }

    public FloatProperty pitchProperty() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch.set(pitch);
    }

    public double getTimeStamp() {
        return timeStamp.get();
    }

    public DoubleProperty timeStampProperty() {
        return timeStamp;
    }

    public void setTimeStamp(double timeStamp) {
        this.timeStamp.set(timeStamp);
    }

    public StringProperty getAlgoFile() {
        return algoFile;
    }


    public void setAlgoFile(StringProperty algoFile) {
        this.algoFile = algoFile;
    }


    public StringProperty getCsvFile() {
        return csvFile;
    }


    public void setCsvFile(StringProperty csvFile) {
        this.csvFile = csvFile;
    }


    public StringProperty getSettingFile() {
        return settingFile;
    }


    public void setSettingFile(StringProperty settingFile) {
        this.settingFile = settingFile;
    }


    public ListProperty<String> getListView() {
        return listView;
    }

    public void setListView(SimpleListProperty<String> listView) {
        this.listView = listView;
    }


    public float getMinThrottle() {
        return minThrottle.get();
    }

    public FloatProperty minThrottleProperty() {
        return minThrottle;
    }

    public void setMinThrottle(float minThrottle) {
        this.minThrottle.set(minThrottle);
    }

    public float getMaxThrottle() {
        return maxThrottle.get();
    }

    public FloatProperty maxThrottleProperty() {
        return maxThrottle;
    }

    public void setMaxThrottle(float maxThrottle) {
        this.maxThrottle.set(maxThrottle);
    }

    public float getMinRudder() {
        return minRudder.get();
    }

    public FloatProperty minRudderProperty() {
        return minRudder;
    }

    public void setMinRudder(float minRudder) {
        this.minRudder.set(minRudder);
    }

    public float getMaxRudder() {
        return maxRudder.get();
    }

    public FloatProperty maxRudderProperty() {
        return maxRudder;
    }

    public void setMaxRudder(float maxRudder) {
        this.maxRudder.set(maxRudder);
    }

    public float getMinElevator() {
        return minElevator.get();
    }

    public FloatProperty minElevatorProperty() {
        return minElevator;
    }

    public void setMinElevator(float minElevator) {
        this.minElevator.set(minElevator);
    }

    public float getMaxElevator() {
        return maxElevator.get();
    }

    public FloatProperty maxElevatorProperty() {
        return maxElevator;
    }

    public void setMaxElevator(float maxElevator) {
        this.maxElevator.set(maxElevator);
    }

    public float getMinAileron() {
        return minAileron.get();
    }

    public FloatProperty minAileronProperty() {
        return minAileron;
    }

    public void setMinAileron(float minAileron) {
        this.minAileron.set(minAileron);
    }

    public float getMaxAileron() {
        return maxAileron.get();
    }

    public FloatProperty maxAileronProperty() {
        return maxAileron;
    }

    public void setMaxAileron(float maxAileron) {
        this.maxAileron.set(maxAileron);
    }

    public float getCenterCircle() {
        return centerCircle.get();
    }

    public FloatProperty centerCircleProperty() {
        return centerCircle;
    }

    public void setCenterCircle(float centerCircle) {
        this.centerCircle.set(centerCircle);
    }

    public double getMaxTimeLine() {
        return maxTimeLine.get();
    }

    public DoubleProperty maxTimeLineProperty() {
        return maxTimeLine;
    }


    public StringProperty getNameofFeatureA() {
        return nameofFeatureA;
    }


    public void setNameofFeatureA(StringProperty nameofFeatureA) {
        this.nameofFeatureA = nameofFeatureA;
    }


    public StringProperty getNameofFeatureB() {
        return nameofFeatureB;
    }


    public void setNameofFeatureB(StringProperty nameofFeatureB) {
        this.nameofFeatureB = nameofFeatureB;
    }

    public void setMaxTimeLine(double maxTimeLine) {
        this.maxTimeLine.set(maxTimeLine);
    }


    public StringProperty getSpAnomalyClassProperty() {
        return spAnomalyClass;
    }

    public void setSpAnomalyClass(String spAnomalyClass) {
        this.spAnomalyClass.set(spAnomalyClass);
    }


    public StringProperty getNameFromList() {
        return nameFromList;
    }


    public void setNameFromList(StringProperty nameFromList) {
        this.nameFromList = nameFromList;
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

    public StringProperty getSpLabelCoralFeatureA() {
        return spLabelCoralFeatureA;
    }

    public void setSpLabelCoralFeatureA(String spLabelCoralFeatureA) {
        this.spLabelCoralFeatureA.set(spLabelCoralFeatureA);
    }


    public StringProperty getSpLabelCoralFeatureB() {
        return spLabelCoralFeatureB;
    }

    public void setSpLabelCoralFeatureB(String spLabelCoralFeatureB) {
        this.spLabelCoralFeatureB.set(spLabelCoralFeatureB);
    }
}
