package view.menuBarView;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import model.BackendMethods;

import java.io.File;

public class MenuBarController {
    @FXML
    private MenuItem jsonSettings;
    @FXML
    private MenuItem csvFile;
    @FXML
    private MenuItem algoChoose;
    @FXML
    private ComboBox<String> planeChoose, flightList;
    @FXML
    private Button planeChooseBtn;

    private StringProperty sSettingFile;
    private StringProperty sCsvFile;
    private StringProperty sAlgoFile;

    // Add a public no-args constructor
    public MenuBarController() {
    }

    @FXML
    private void initialize() {
        //
        String [] planeId=  BackendMethods.getPlaneIDs("all");
        planeChoose.setItems(FXCollections.observableArrayList(planeId));
        sSettingFile = new SimpleStringProperty("");
        sCsvFile = new SimpleStringProperty("");
        sAlgoFile = new SimpleStringProperty("");

    }

    @FXML
    void xmlFileChooser(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new ExtensionFilter("json files", "*.json"));
        File f = fc.showOpenDialog(null);
        if (f != null) {
            sSettingFile.setValue(f.getPath());
        }
    }

    @FXML
    void csvFileChooser(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new ExtensionFilter(" csv files", "*.csv"));
        File f = fc.showOpenDialog(null);
        if (f != null) {
            sCsvFile.setValue(f.getPath());
        }
    }

    @FXML
    void algoFileChooser(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new ExtensionFilter("Anomaly detector files", "*.class"));
        File f = fc.showOpenDialog(null);
        if (f != null) {
            sAlgoFile.setValue(f.getPath());
        }
    }
    @FXML
    void selectPlane(ActionEvent event) {
      if(!planeChoose.getValue().equals("Choose Plane")  )
      {
          String [] flightIDs=  BackendMethods.getFlightIDs(planeChoose.getValue());
          flightList.setItems(FXCollections.observableArrayList(flightIDs));
          flightList.setDisable(false);
      }
    }

    public MenuItem getJsonSettings() {
        return jsonSettings;
    }

    public void setJsonSettings(MenuItem jsonSettings) {
        this.jsonSettings = jsonSettings;
    }

    public MenuItem getCsvFile() {
        return csvFile;
    }

    public void setCsvFile(MenuItem csvFile) {
        this.csvFile = csvFile;
    }

    public MenuItem getAlgoChoose() {
        return algoChoose;
    }

    public void setAlgoChoose(MenuItem algoChoose) {
        this.algoChoose = algoChoose;
    }

    public StringProperty getsSettingFile() {
        return sSettingFile;
    }

    public void setsSettingFile(StringProperty sSettingFile) {
        this.sSettingFile = sSettingFile;
    }

    public StringProperty getsCsvFile() {
        return sCsvFile;
    }

    public void setsCsvFile(StringProperty sCsvFile) {
        this.sCsvFile = sCsvFile;
    }

    public StringProperty getsAlgoFile() {
        return sAlgoFile;
    }

    public void setsAlgoFile(StringProperty sAlgoFile) {
        this.sAlgoFile = sAlgoFile;
    }


}
