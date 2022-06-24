package view.application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import view.dashboardView.Dashboard;
import view.graphView.Graph;
import view.joystickView.Joystick;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.util.Duration;
import model.AppModel;
import model.BackendMethods;

public class MonitoringController {
    
    /* TODO:
     * -Create a separate instance for anomaly detection.
     * -Feed it with hybrid algorithm by default (remove select algorithm menu).
     * -Define an anomaly detection cycle:
     *  1) Initialization: reset line counter, open a csv file for writing with a constant name, write a properties list as its first line.
     *  2) For each new line read from the backend and sent to the dashboard and joystick, increment counter by 1.
     *  3) If line counter equals the size of the anomaly detection cycle, close the csv file and call detect() on that file.
     *     Try to append the anomaly map and not create a new one each cycle.
     *  4) Add data point of previous csv anomaly map to graphs.
     *  5) Repeat.
     *  NOTE: understand how to update the anomaly detection graphs (different than the time capsule because time is linear, always advances).
     */
    
    AppModel model;
    
    @FXML
    private ListView<String> featureList;
    @FXML
    private Joystick joystick;
    @FXML
    private Dashboard dashboard;
    @FXML
    private Graph graph;
    @FXML
    private ComboBox<String> activePlanes;
    @FXML
    private MenuItem algoChoose;
    
    String regCsvFileName = "resources/reg_flight.csv";
    
    @FXML
    private void initialize() {
        model = new AppModel();
        model.setTimeSeriesTrain(regCsvFileName);
        featureList.setItems(FXCollections.observableArrayList(model.getTimeSeriesTrain().namesOfFeatures));
        
        Timeline timeLine = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateDashboardAndJoystick()));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }
    
    private void updateDashboardAndJoystick() {
        if (!((TabPane)activePlanes.getScene().lookup("#tabs")).getSelectionModel().getSelectedItem().getId().equals("monitoring")) {
            return;
        }
        
        String activePlane = activePlanes.getValue();
        if (activePlane == null || activePlane.isEmpty()) {
            return;
        }
        
        String line = BackendMethods.getFlightParamsLine(activePlane);
        if (line == null || line.isEmpty() || line.equals("Client disconnected")) {
            return;
        }
        
        String[] values = line.split(",");
        dashboard.setAltitude(Float.parseFloat(values[25]));
        dashboard.setPitch(Float.parseFloat(values[27]));
        dashboard.setAirspeed(Float.parseFloat(values[24]));
        dashboard.setHeading(Float.parseFloat(values[36]));
        dashboard.setYaw(Float.parseFloat(values[20]));
        dashboard.setRoll(Float.parseFloat(values[28]));
        joystick.setAileron(Float.parseFloat(values[0]));
        joystick.setElevator(Float.parseFloat(values[1]));
        joystick.setRudder(Float.parseFloat(values[2]));
        joystick.setThrottle(Float.parseFloat(values[6]));
    }

    public void cmbActivePlanesOnShowing() {
        String[] res = BackendMethods.getPlaneIDs("active");
        activePlanes.setItems(FXCollections.observableArrayList(res));
    }
    
    public void cmbActivePlanesOnAction() {
        if (activePlanes.getValue() == null || activePlanes.getValue().isEmpty()) {
            return;
        }
        featureList.setDisable(false);
    }
    
    public void algoFileChooser() {
        
    }
}
