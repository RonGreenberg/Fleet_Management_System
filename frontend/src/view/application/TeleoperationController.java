package view.application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import model.BackendMethods;
import view.dashboardView.Dashboard;
import view.movableJoystickView.MovableJoystick;

public class TeleoperationController {
    
    @FXML
    TextArea textArea;
    @FXML
    Button btnRunScript;
    @FXML
    MovableJoystick joystick;
    @FXML
    ComboBox<String> activePlanes;
    @FXML
    Dashboard dashboard;
    float altitude, pitch, airspeed, heading, yaw, roll;
    
    @FXML
    private void initialize() {
        // updating dashboard every second
        Timeline timeLine = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateDashboard()));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }
    
    private void updateDashboard() {
        // doing something only if the Teleoperation tab is currently in focus
        if (!((TabPane)textArea.getScene().lookup("#tabs")).getSelectionModel().getSelectedItem().getId().equals("teleoperation")) {
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
        
//        if (activePlane == null || !BackendMethods.isPlaneActive(activePlane)) {
//            return;
//        }
//        
//        altitude = Float.parseFloat(BackendMethods.getCmd("/instrumentation/altimeter/indicated-altitude-ft", activePlane));
//        pitch = Float.parseFloat(BackendMethods.getCmd("/instrumentation/attitude-indicator/indicated-pitch-deg", activePlane));
//        airspeed = Float.parseFloat(BackendMethods.getCmd("/instrumentation/airspeed-indicator/indicated-speed-kt", activePlane));
//        heading = Float.parseFloat(BackendMethods.getCmd("/instrumentation/heading-indicator/indicated-heading-deg", activePlane));
//        yaw = Float.parseFloat(BackendMethods.getCmd("/orientation/side-slip-deg", activePlane));
//        roll = Float.parseFloat(BackendMethods.getCmd("/instrumentation/attitude-indicator/indicated-roll-deg", activePlane));
//        System.out.println(BackendMethods.getFlightParamsLine(activePlane));
//        System.out.println("altitude=" + altitude); // index: 25
//        System.out.println("pitch=" + pitch); // index: 27
//        System.out.println("airspeed=" + airspeed); // 24
//        System.out.println("heading=" + heading); // 36
//        System.out.println("yaw=" + yaw); // 20
//        System.out.println("roll=" + roll); // 28
//        dashboard.setAltitude(altitude);
//        dashboard.setPitch(pitch);
//        dashboard.setAirspeed(airspeed);
//        dashboard.setHeading(heading);
//        dashboard.setYaw(yaw);
//        dashboard.setRoll(roll);
    }
    
    public void openScriptFile() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Open script file");
        fc.setInitialDirectory(new File("../"));
        File chosen = fc.showOpenDialog(null);
        if (chosen != null) {
            try {
                List<String> lines = Files.readAllLines(chosen.toPath());
                textArea.setText(String.join(System.lineSeparator(), lines));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void btnRunScriptClick() {
        String message = "";
        if (activePlanes.getValue() == null) {
            message += "-Please choose an active plane ID.\n";
        }
        if (textArea.getText().trim().isEmpty()) {
            message += "-Please enter a script in the text area or upload a text file containing a script.";
        }
        
        if (message.isEmpty()) {
            // writing the script to a file
            String fileName = "resources/flight_scripts/" + activePlanes.getValue() + ".txt";
            String absolutePath = "";
            try {
                File f = new File(fileName);
                absolutePath = f.getAbsolutePath();
                PrintWriter out = new PrintWriter(new FileWriter(new File(fileName)), true);
                String[] lines = textArea.getText().split(System.lineSeparator());
                for (String line : lines) {
                    out.println(line);
                }
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            String res = BackendMethods.interpret(absolutePath, activePlanes.getValue());
            if (!res.equals("ok")) {
                if (res.equals("busy")) {
                    showAlert("Interpreter is busy");
                } else {
                    showAlert(res);
                }
            }
        } else {
            showAlert(message);
        }
    }
    
    public void cmbActivePlanesOnShowing() {
        String[] res = BackendMethods.getPlaneIDs("active");
        activePlanes.setItems(FXCollections.observableArrayList(res));
    }
    
    public void cmbActivePlanesOnAction() {
        if (activePlanes.getValue() == null) {
            return;
        }
        joystick.setPlaneID(activePlanes.getValue());
    }
    
    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(message);
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
               System.out.println("OK");
            }
        });
    }
}
