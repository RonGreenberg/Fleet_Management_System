package TimeCapsule.timeLineView;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.text.Text;

import java.util.HashMap;

public class TimeLineController {
    @FXML
    private Slider time;
    @FXML
    private Text textTimeStamp;
    @FXML
    private Button back;
    @FXML
    private Button stop;
    @FXML
    private Button play;
    @FXML
    private Button pause;
    @FXML
    private Button next;
    @FXML
    private ComboBox<String> speed;

    private final double nextValue = 15;
    private final double backValue = 15;
    private DoubleProperty maxTimeLine;

    private DoubleProperty dSpeed;

    // Add a public no-args constructor
    public TimeLineController() {
    }

    @FXML
    private void initialize() {
        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "x0.5",
                        "x1.0",
                        "x1.5",
                        "x2.0"
                );

        this.maxTimeLine = new SimpleDoubleProperty(0);
        this.dSpeed = new SimpleDoubleProperty();
        this.dSpeed.addListener(v -> {
            String value = "x1.0";
            if (this.dSpeed.getValue() == 1.0) {
                value = "x1.0";
            } else if
            (this.dSpeed.getValue() == 1.5) {
                value = "x1.5";
            } else if
            (this.dSpeed.getValue() == 0.5) {
                value = "x0.5";
            } else if (this.dSpeed.getValue() == 2.0) {
                value = "x2.0";
            }
            this.speed.setValue(value);
        });
        this.speed.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            String s =  this.speed.getValue().replace("x", "");
            double nv = Double.parseDouble(s);
            this.dSpeed.setValue(nv);
        });

        speed.getItems().addAll(options);
        speed.getSelectionModel().select(1);
    }

    public Slider getTime() {
        return time;
    }

    public void setTime(Slider time) {
        this.time = time;
    }

    public double getNextValue() {
        return this.nextValue;
    }

    public double getBackValue() {
        return this.backValue;
    }

    public Text getTextTimeStamp() {
        return textTimeStamp;
    }

    public void setTextTimeStamp(Text textTimeStamp) {
        this.textTimeStamp = textTimeStamp;
    }

    public Button getBack() {
        return back;
    }

    public void setBack(Button back) {
        this.back = back;
    }

    public Button getStop() {
        return stop;
    }

    public void setStop(Button stop) {
        this.stop = stop;
    }

    public Button getPlay() {
        return play;
    }

    public void setPlay(Button play) {
        this.play = play;
    }

    public Button getPause() {
        return pause;
    }

    public void setPause(Button pause) {
        this.pause = pause;
    }

    public Button getNext() {
        return next;
    }

    public void setNext(Button next) {
        this.next = next;
    }

    public ComboBox<String> getSpeed() {
        return speed;
    }

    public void setSpeed(ComboBox<String> speed) {
        this.speed = speed;
    }

    public double getMaxTimeLine() {
        return maxTimeLine.get();
    }

    public DoubleProperty maxTimeLineProperty() {
        return maxTimeLine;
    }

    public void setMaxTimeLine(double maxTimeLine) {
        this.maxTimeLine.set(maxTimeLine);
    }

    public double getdSpeed() {
        return dSpeed.get();
    }

    public DoubleProperty dSpeedProperty() {
        return dSpeed;
    }

    public void setdSpeed(double dSpeed) {
        this.dSpeed.set(dSpeed);
    }


}
