package view.menuBarView;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import model.AppModel;
import model.BackendMethods;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MenuBarController {
   /* @FXML
    private MenuItem jsonSettings;
    @FXML
    private MenuItem csvFile;

    */
    @FXML
    private MenuItem algoChoose;
    @FXML
    private ComboBox<String> planeChoose, flightList;


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
        File recordsDir = new File(System.getProperty("user.home"), ".out/production/frontend/model/algorithms");
        if (! recordsDir.exists()) {
            recordsDir.mkdirs();
        }

        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(recordsDir);
        fc.getExtensionFilters().add(new ExtensionFilter(" csv files", "*.csv"));
        File f = fc.showOpenDialog(null);
        if (f != null) {
            sCsvFile.setValue(f.getPath());
        }
    }

    @FXML
    void algoFileChooser(ActionEvent event) {
        // IntelliJ
        String currentPath = Paths.get(".").toAbsolutePath().normalize().toString()+"/out/production/frontend/model/algorithms";
        if (!Files.exists(Paths.get(currentPath))) {
            // Eclipse
            currentPath = Paths.get(".").toAbsolutePath().normalize().toString() + "/bin/model/algorithms";
        }
        
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File(currentPath));
        fc.getExtensionFilters().add(new ExtensionFilter("Anomaly detector class", "*.class"));
        File f = fc.showOpenDialog(null);
        if (f != null) {
            sAlgoFile.setValue(f.getPath());
        }
    }

    @FXML
    void selectPlane(ActionEvent event) {
        if(!planeChoose.getValue().equals("Choose Plane")  )
        {
            flightList.setDisable(false);
            String [] flightIDs=  BackendMethods.getFlightIDs(planeChoose.getValue());
            //EventHandler<ActionEvent> handler = flightList.getOnAction();
            //flightList.removeEventHandler(ActionEvent.ANY, handler); // temporarily removing event handler
            flightList.setItems(FXCollections.observableArrayList(flightIDs));
            flightList.valueProperty().set(null); // clearing selection
            //flightList.setOnAction(handler); // re-adding the handler
        }
        else
        {
            flightList.setDisable(true);
            flightList.getItems().clear();
        }
    }
    @FXML
    void selectFlight(ActionEvent event) {
        /* The backend stores the absolute path of the flight files in the DB. Since the paths do not match between different computers,
         * Guy chose to open a directory in the frontend, store the files there, change the path in his local DB instance to store only the
         * filename, and assume that the files will be located in the directory he opened. But new flights will still be saved with the
         * absolute path. I (Ron) prefer using the absolute path in my PC, so in order for it to work on both PCs, we will first construct
         * a path consisting of only the filename itself from the DB, prefixed with Guy's directory. If the resulting path does not exist,
         * we'll use the original path we took from the DB, assuming it is absolute (Ron's case).
         */
        if (flightList.getValue() == null) {
            return;
        }
        String dbPath = BackendMethods.getFlightDetails(Integer.parseInt(flightList.getValue()))[2];
        String filename = Paths.get(dbPath).getFileName().toString(); // retrieves only the filename, whether it is stored in absolute or relative format
        String path="frontend/resources/flight_csv/"+filename;
        if (!Files.exists(Paths.get(path).getFileName())) {
            path = dbPath;
        }
        sCsvFile.setValue( path)  ;
    }
   /* public MenuItem getJsonSettings() {
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
*/
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
