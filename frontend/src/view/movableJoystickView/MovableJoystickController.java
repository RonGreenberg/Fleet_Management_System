package view.movableJoystickView;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;

public class MovableJoystickController implements Initializable {
    @FXML
    Canvas joystick;
    @FXML
    Slider throttle;
    @FXML
    Slider rudder;
    //ViewModel vm;
    DoubleProperty aileron, elevators;
    
    boolean mousePushed;
    double jx, jy; // joystick x,y
    double mx, my; // canvas center coordinates
    
    public MovableJoystickController() {
        
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mousePushed = false;
        jx = 100;
        jy = 100;
        aileron = new SimpleDoubleProperty();
        elevators = new SimpleDoubleProperty();
    }
    
//  void init(ViewModel vm) {
//      this.vm = vm;
//      vm.throttle.bind(throttle.valueProperty());
//      vm.rudder.bind(rudder.valueProperty());
//      vm.aileron.bind(aileron);
//      vm.elevators.bind(elevators);
//  }
    
    public void init() {}
    
    public void paint() {
        GraphicsContext gc = joystick.getGraphicsContext2D();
        mx = joystick.getWidth() / 2;
        my = joystick.getHeight() / 2;
        gc.clearRect(0, 0, joystick.getWidth(), joystick.getHeight());
        gc.strokeOval(jx - 50, jy - 50, 100, 100);
        aileron.set((jx - mx) / mx); // normalized value between -1 and 1 (can actually be less than -1 if we go off the canvas and jx becomes negative)
        elevators.set((jy - my) / my); // normalized value between -1 and 1 (can actually be less than -1 if we go off the canvas and jy becomes negative)       
    }
    
    public void mouseDown(MouseEvent me) {
        if (!mousePushed) {
            mousePushed = true;
        }
    }
    
    public void mouseUp(MouseEvent me) {
        if (mousePushed) {
            mousePushed = false;
            // re-position joystick in the center of the canvas when releasing mouse
            jx = mx;
            jy = my;
            paint();
        }
    }
    
    public void mouseMove(MouseEvent me) {
        if (mousePushed) {
            jx = me.getX();
            jy = me.getY();
            paint();
        }
    }
}
