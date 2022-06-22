package view.application;
import model.AppModel;
import model.BackendClient;
import view.movableJoystickView.MovableJoystick;
import viewModel.AppViewModel;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
        	BackendClient client = new BackendClient();
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("Main.fxml"));
            AnchorPane root = (AnchorPane)fxml.load();
//            AppModel m = new AppModel();
//            AppViewModel vm = new AppViewModel(m);
//            GUIController view = fxml.getController();
//            view.init(vm);

//            FXMLLoader fxl = new FXMLLoader(); // creating a separate object so we can use it for getController()
//            BorderPane root = fxl.load(getClass().getResource("MovableJoystick.fxml").openStream());
//            MovableJoystick wc = fxl.getController(); // View
//            
//            wc.init();
//            wc.paint();
            
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
