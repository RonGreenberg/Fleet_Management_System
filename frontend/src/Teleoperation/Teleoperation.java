package Teleoperation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import model.BackendMethods;

public class Teleoperation {
    
    String fileName;
    
    @FXML
    TextArea textArea;
    @FXML
    Button btnRunScript;
    
    public void openScriptFile() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Open script file");
        fc.setInitialDirectory(new File("../"));
        File chosen = fc.showOpenDialog(null);
        if (chosen != null) {
            fileName = chosen.getAbsolutePath();
            try {
                List<String> lines = Files.readAllLines(chosen.toPath());
                textArea.setText(String.join(System.lineSeparator(), lines));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void btnRunScriptClick() {
        showAlert("hello!");
//        String message = "";
//        if (activePlanes.getValue() == null) {
//            message += "-Please choose an active plane ID.\n";
//        } 
//        if (fileName == null) {
//            message += "-Please choose a file to interpret.";
//        }
//        
//        if (message.equals("")) {
//            String res = BackendMethods.interpret(fileName, activePlanes.getValue());
//            if (!res.equals("ok")) {
//                if (res.equals("busy")) {
//                    showAlert("Interpreter is busy");
//                } else {
//                    showAlert(res);
//                }
//            }
//        } else {
//            showAlert(message);
//        }
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
