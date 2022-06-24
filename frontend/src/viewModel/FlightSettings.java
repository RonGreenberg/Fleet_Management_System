package viewModel;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class FlightSettings {
    private HashMap<String, FlightFeature> flightFeatureHashMap;
    private String validFlightPath;
    private String chosenAlgorithmPath;
    private String simulatorIp;
    private long simulatorPort;
    private double simulatorSpeed;
    private String settingsFile;

    private final List<String> settingsKeys = Arrays.asList("port", "ip", "featuresSettings",
            "chosenAlgorithmPath", "samplingRatePerSec", "trainFlightCsvPath");
    private final List<String> featureNames = Arrays.asList("aileron", "throttle", "elevator", "rudder", "heading", "yaw", "roll", "pitch", "airspeed", "altitude");

    public FlightSettings(String file) {
        this.settingsFile = file;
    }

    public void loadSettings() throws Exception {
        this.flightFeatureHashMap = new HashMap<>();

        Object obj = new JSONParser().parse(new FileReader(this.settingsFile));
        JSONObject jo = (JSONObject) obj;
        if (!jo.keySet().containsAll(this.settingsKeys)) {
            throw new Exception("Settings json File doesn't contain all keys");
        }


        this.simulatorIp = (String) jo.get("ip");
        this.chosenAlgorithmPath = (String) jo.get("chosenAlgorithmPath");
        this.validFlightPath = (String) jo.get("trainFlightCsvPath");
        this.simulatorPort = (long) jo.get("port");
        this.simulatorSpeed = (((Long) jo.get("samplingRatePerSec")).doubleValue()) / 10; // divide by 10 for x per sec

        if (!Files.exists(Paths.get(chosenAlgorithmPath))) {
            this.chosenAlgorithmPath = "bin/model/algorithms/ZScore.class"; // eclipse version (the settings file contains the IntelliJ version)
        }
        
        if (!(new File(this.validFlightPath).isFile())) {
            this.validFlightPath = "resources/reg_flight.csv"; // the path in the settings file might not need to contain the frontend parent folder
            if (!(new File(this.validFlightPath).isFile())) {
                throw new Exception("trainFlightCsvPath: " + this.validFlightPath + " does not exists");
            }
        }

        if (this.simulatorSpeed != 0.5 && this.simulatorSpeed != 1.0 && this.simulatorSpeed != 1.5 && this.simulatorSpeed != 2.0) {
            throw new Exception("samplingRatePerSec is Not Valid!\n Please set 5 / 10 / 15 / 20 as samplingRatePerSec");
        }

        JSONArray featuresSettings = (JSONArray) jo.get("featuresSettings");

        for (Object featuresSetting : featuresSettings) {
            JSONObject feature = (JSONObject) featuresSetting;
            String featureName = (String) feature.get("FeatureName");
            if (!featureNames.contains(featureName)) {
                throw new Exception("Feature Name " + featureName + " not found in settings.json file");
            }
            FlightFeature ff = new FlightFeature((String) feature.get("FeatureName"), ((Long) feature.get("min")).floatValue(),
                    ((Long) feature.get("max")).floatValue(), ((Long) feature.get("ColumnIndex")).intValue());
            flightFeatureHashMap.put(ff.getFeatureName(), ff);
        }

    }

    public HashMap<String, FlightFeature> getFlightFeatureHashMap() {
        return flightFeatureHashMap;
    }

    public String getSimulatorIp() {
        return simulatorIp;
    }

    public void setSimulatorIp(String simulatorIp) {
        this.simulatorIp = simulatorIp;
    }

    public double getSimulatorSpeed() {
        return simulatorSpeed;
    }

    public void setSimulatorSpeed(double simulatorSpeed) {
        this.simulatorSpeed = simulatorSpeed;
    }

    public long getSimulatorPort() {
        return simulatorPort;
    }

    public void setSimulatorPort(long simulatorPort) {
        this.simulatorPort = simulatorPort;
    }

    public String getSettingsFile() {
        return settingsFile;
    }

    public void setSettingsFile(String settingsFile) {
        this.settingsFile = settingsFile;
    }

    public String getValidFlightPath() {
        return validFlightPath;
    }

    public void setValidFlightPath(String validFlightPath) {
        this.validFlightPath = validFlightPath;
    }

    public String getChosenAlgorithmPath() {
        return chosenAlgorithmPath;
    }

    public void setChosenAlgorithmPath(String chosenAlgorithmPath) {
        this.chosenAlgorithmPath = chosenAlgorithmPath;
    }

}
