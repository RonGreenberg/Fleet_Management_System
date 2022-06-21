package Teleoperation.joystickView;


import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;

import java.util.Objects;


public class Joystick extends GridPane {

    JoystickController joystickController;
    private FloatProperty throttle;
    private FloatProperty rudder;
    private FloatProperty aileron;
    private FloatProperty elevator;
    private FloatProperty centerCircle;
    private FloatProperty minThrottle, maxThrottle, minRudder, maxRudder;
    private FloatProperty minElevator, maxElevator, minAileron, maxAileron;

    public Joystick() {
        // TODO Auto-generated constructor stub
        super();
        try {

            FXMLLoader fxl = new FXMLLoader();
            GridPane joy = fxl.load(Objects.requireNonNull(getClass().getResource("Joystick.fxml")).openStream());
            joystickController = fxl.getController();
            initProperties();
            bindProperties();

            this.getChildren().add(joy);

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void bindProperties(){
        joystickController.getThrottle().valueProperty().bind(throttle);
        joystickController.getRudder().valueProperty().bind(rudder);
        joystickController.aileronProperty().bind(aileron);
        joystickController.elevatorProperty().bind(elevator);

        joystickController.centerCircleProperty().bind(centerCircle);

        //min max values
        joystickController.minThrottleProperty().bind(minThrottle);
        joystickController.maxThrottleProperty().bind(maxThrottle);
        joystickController.minRudderProperty().bind(minRudder);
        joystickController.maxRudderProperty().bind(maxRudder);
        joystickController.minElevatorProperty().bind(minElevator);
        joystickController.maxElevatorProperty().bind(maxElevator);
        joystickController.minAileronProperty().bind(minAileron);
        joystickController.maxAileronProperty().bind(maxAileron);
    }

    public void initProperties(){
        throttle = new SimpleFloatProperty((float) joystickController.getThrottle().getValue());
        rudder = new SimpleFloatProperty((float) joystickController.getRudder().getValue());
        aileron = new SimpleFloatProperty(joystickController.aileronProperty().getValue());
        elevator = new SimpleFloatProperty(joystickController.elevatorProperty().getValue());

        centerCircle = new SimpleFloatProperty(joystickController.centerCircleProperty().getValue());

        minThrottle = new SimpleFloatProperty(joystickController.minThrottleProperty().getValue());
        maxThrottle = new SimpleFloatProperty(joystickController.maxThrottleProperty().getValue());
        minRudder = new SimpleFloatProperty(joystickController.minRudderProperty().getValue());
        maxRudder = new SimpleFloatProperty(joystickController.maxRudderProperty().getValue());
        minElevator = new SimpleFloatProperty(joystickController.minElevatorProperty().getValue());
        maxElevator = new SimpleFloatProperty(joystickController.maxElevatorProperty().getValue());
        minAileron = new SimpleFloatProperty(joystickController.minAileronProperty().getValue());
        maxAileron = new SimpleFloatProperty(joystickController.maxAileronProperty().getValue());
    }


    public JoystickController getJoystickController() {
        return joystickController;
    }

    public void setJoystickController(JoystickController joystickController) {
        this.joystickController = joystickController;
    }


    public float getThrottle() {
        return throttle.get();
    }

    public FloatProperty throttleProperty() {
        return throttle;
    }

    public void setThrottle(float throttle) {
        this.throttle.set(throttle);
    }

    public float getRudder() {
        return rudder.get();
    }

    public FloatProperty rudderProperty() {
        return rudder;
    }

    public void setRudder(float rudder) {
        this.rudder.set(rudder);
    }

    public float getAileron() {
        return aileron.get();
    }

    public FloatProperty aileronProperty() {
        return aileron;
    }

    public void setAileron(float aileron) {
        this.aileron.set(aileron);
    }

    public float getElevator() {
        return elevator.get();
    }

    public FloatProperty elevatorProperty() {
        return elevator;
    }

    public void setElevator(float elevator) {
        this.elevator.set(elevator);
    }

    public float getCenterCircle() {
        return centerCircle.get();
    }

    public FloatProperty centerCircleProperty() {
        return centerCircle;
    }

    public void setCenterCircle(float centerCircle) {
        this.centerCircle.set(centerCircle);
    }

    public float getMinThrottle() {
        return minThrottle.get();
    }

    public FloatProperty minThrottleProperty() {
        return minThrottle;
    }

    public void setMinThrottle(float minThrottle) {
        this.minThrottle.set(minThrottle);
    }

    public float getMaxThrottle() {
        return maxThrottle.get();
    }

    public FloatProperty maxThrottleProperty() {
        return maxThrottle;
    }

    public void setMaxThrottle(float maxThrottle) {
        this.maxThrottle.set(maxThrottle);
    }

    public float getMinRudder() {
        return minRudder.get();
    }

    public FloatProperty minRudderProperty() {
        return minRudder;
    }

    public void setMinRudder(float minRudder) {
        this.minRudder.set(minRudder);
    }

    public float getMaxRudder() {
        return maxRudder.get();
    }

    public FloatProperty maxRudderProperty() {
        return maxRudder;
    }

    public void setMaxRudder(float maxRudder) {
        this.maxRudder.set(maxRudder);
    }

    public float getMinElevator() {
        return minElevator.get();
    }

    public FloatProperty minElevatorProperty() {
        return minElevator;
    }

    public void setMinElevator(float minElevator) {
        this.minElevator.set(minElevator);
    }

    public float getMaxElevator() {
        return maxElevator.get();
    }

    public FloatProperty maxElevatorProperty() {
        return maxElevator;
    }

    public void setMaxElevator(float maxElevator) {
        this.maxElevator.set(maxElevator);
    }

    public float getMinAileron() {
        return minAileron.get();
    }

    public FloatProperty minAileronProperty() {
        return minAileron;
    }

    public void setMinAileron(float minAileron) {
        this.minAileron.set(minAileron);
    }

    public float getMaxAileron() {
        return maxAileron.get();
    }

    public FloatProperty maxAileronProperty() {
        return maxAileron;
    }

    public void setMaxAileron(float maxAileron) {
        this.maxAileron.set(maxAileron);
    }
}
