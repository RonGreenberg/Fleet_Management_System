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
import model.AppModel;
import model.BackendMethods;
import viewModel.AppViewModel;

public class MovableJoystick extends BorderPane {
	
	MovableJoystickController joystickController;
	DoubleProperty aileron, elevator, throttle, rudder;
	String planeID;
	
	public MovableJoystick() {
	    FXMLLoader fxl = new FXMLLoader();
        try {
            BorderPane joy = fxl.load(Objects.requireNonNull(getClass().getResource("MovableJoystick.fxml")).openStream());
            joystickController = fxl.getController();
            this.getChildren().add(joy);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        aileron = new SimpleDoubleProperty();
        elevator = new SimpleDoubleProperty();
        throttle = new SimpleDoubleProperty();
        rudder = new SimpleDoubleProperty();
        
        // adding listeners to send commands whenever the joystick/sliders are moved
        aileron.addListener((o,ov,nv)->BackendMethods.setCmd("/controls/flight/aileron", nv.doubleValue(), planeID));
        elevator.addListener((o,ov,nv)->BackendMethods.setCmd("/controls/flight/elevator", nv.doubleValue(), planeID));
        throttle.addListener((o,ov,nv)->BackendMethods.setCmd("/controls/engines/current-engine/throttle", nv.doubleValue(), planeID));
        rudder.addListener((o,ov,nv)->BackendMethods.setCmd("/controls/flight/rudder", nv.doubleValue(), planeID));
        joystickController.init(this);
        joystickController.paint();
	}
	
	public void setPlaneID(String planeID) {
	    this.planeID = planeID;
	}
}
