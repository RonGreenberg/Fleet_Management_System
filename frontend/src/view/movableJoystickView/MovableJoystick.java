package view.movableJoystickView;

import java.io.IOException;
import java.util.Objects;
import java.util.Observable;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import viewModel.AppViewModel;

public class MovableJoystick extends BorderPane {
	
	MovableJoystickController joystickController;
	
	public MovableJoystick() {
	    FXMLLoader fxl = new FXMLLoader();
        try {
            BorderPane joy = fxl.load(Objects.requireNonNull(getClass().getResource("MovableJoystick.fxml")).openStream());
            joystickController = fxl.getController();
            joystickController.init();
            joystickController.paint();
            this.getChildren().add(joy);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
