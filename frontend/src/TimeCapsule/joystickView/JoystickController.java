package TimeCapsule.joystickView;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Slider;

import java.net.URL;
import java.util.ResourceBundle;

public class JoystickController implements Initializable {

    @FXML
    private Slider throttle;// zir y
    @FXML
    private Slider rudder;//zir x
    @FXML
    private Canvas joy;

    private FloatProperty aileron, elevator;
    private FloatProperty minThrottle, maxThrottle, minRudder, maxRudder;
    private FloatProperty minElevator, maxElevator, minAileron, maxAileron;
    private FloatProperty centerCircle;

    public JoystickController() {
        //do not write here!!!
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //get the setting value and initialize
        minThrottle = new SimpleFloatProperty(-1);
        maxThrottle = new SimpleFloatProperty(1);
        minRudder = new SimpleFloatProperty(-1);
        maxRudder = new SimpleFloatProperty(1);
        centerCircle = new SimpleFloatProperty((float) (0));
        minElevator = new SimpleFloatProperty(-1);
        maxElevator = new SimpleFloatProperty(1);
        minAileron = new SimpleFloatProperty(-1);
        maxAileron = new SimpleFloatProperty(1);

        this.throttle.setMajorTickUnit(1000);
        this.rudder.setMajorTickUnit(1000);

        //end
        this.throttle.setMin(minThrottle.getValue());
        this.throttle.setMax(maxThrottle.getValue());
        this.rudder.setMin(minRudder.getValue());
        this.rudder.setMax(maxRudder.getValue());

        this.throttle.setValue((maxThrottle.getValue() - minThrottle.getValue()) / 2);
        this.rudder.setValue((maxRudder.getValue() - minRudder.getValue()) / 2);
        this.aileron = new SimpleFloatProperty();
        this.aileron.set(0);
        this.elevator = new SimpleFloatProperty();
        this.elevator.set(0);

        addListeners();

    }

    public void addListeners(){
        minRudder.addListener(v ->   {   this.rudder.setMin(this.minRudder.getValue());});
        maxThrottle.addListener(v -> {   this.throttle.setMax(this.maxThrottle.getValue());});
        maxRudder.addListener(v ->   {   this.rudder.setMax(this.maxRudder.getValue());});
        minRudder.addListener(v ->   {   this.rudder.setMin(this.minRudder.getValue());});
        /*maxAileron.addListener(v-> paint());
        minAileron.addListener(v-> paint());
        maxElevator.addListener(v-> paint());
        minElevator.addListener(v-> paint());*/
        
        this.elevator.addListener(v -> paint());
        this.aileron.addListener(v -> paint());
    }


    public void paint() {
        GraphicsContext gc = joy.getGraphicsContext2D();
        gc.clearRect(0, 0, joy.getWidth(), joy.getHeight());
        gc.strokeRect(0, 0, joy.getWidth(), joy.getHeight());
        gc.strokeOval(this.getAli() - 30, this.getEle() - 30, 60, 60);


    }

    public float getEle() {
        //get the value noramllaiz after get the canvas size
    	float len = Math.abs(maxElevator.getValue() - minElevator.getValue());
    	float temp = Math.abs(this.elevator.getValue() - minElevator.getValue());
    	float ans=(float) ((temp / len) * joy.getHeight());
        return ans;
    }


    public float getAli() {
        
        float len = Math.abs(maxAileron.getValue() - minAileron.getValue());///TODO****
    	float temp = Math.abs(this.aileron.getValue() - minAileron.getValue());
    	float ans=(float) ((temp / len) * joy.getWidth());
        return ans;

    }

    public Slider getThrottle() {
        return throttle;
    }

    public void setThrottle(Slider throttle) {
        this.throttle = throttle;
    }

    public Slider getRudder() {
        return rudder;
    }

    public void setRudder(Slider rader) {
        this.rudder = rader;
    }

    public Canvas getJoy() {
        return joy;
    }

    public void setJoy(Canvas joy) {
        this.joy = joy;
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

    public float getCenterCircle() {
        return centerCircle.get();
    }

    public FloatProperty centerCircleProperty() {
        return centerCircle;
    }

    public void setCenterCircle(float centerCircle) {
        this.centerCircle.set(centerCircle);
    }
}
