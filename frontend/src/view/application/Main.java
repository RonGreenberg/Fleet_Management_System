package view.application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.BorderPane;
import model.AppModel;
import model.BackendClient;
import model.algorithms.TimeSeries;
import viewModel.AppViewModel;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
        	BackendClient client = new BackendClient();
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("Main.fxml"));
            AnchorPane root = (AnchorPane)fxml.load();
            FXMLLoader fxml1 = new FXMLLoader(getClass().getResource("TimeCapsule.fxml"));
            fxml1.load();
           AppModel m = new AppModel();
           AppViewModel vm = new AppViewModel(m);
           TimeCapsuleController view = fxml1.getController();
           view.init(vm);


            
            Scene scene = new Scene(root, 1500, 800);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
