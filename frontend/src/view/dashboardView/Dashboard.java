package view.dashboardView;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.util.Objects;

public class Dashboard extends AnchorPane {

    private DashboardController dashboardController;
    private FloatProperty altitude, yaw, pitch, roll;
    private FloatProperty airspeed, heading;


    public Dashboard() {
        super();
        try {

            FXMLLoader fxl = new FXMLLoader();
            AnchorPane dash = fxl.load(Objects.requireNonNull(getClass().getResource("Dashboard.fxml")).openStream());
            dashboardController = fxl.getController();
            initProperties();
            bindProperties();

            this.getChildren().add(dash);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void initProperties() {
        roll = new SimpleFloatProperty(dashboardController.dpRollProperty().getValue());
        pitch = new SimpleFloatProperty(dashboardController.dpPitchProperty().getValue());
        heading = new SimpleFloatProperty(dashboardController.dpHeadingProperty().getValue());
        yaw = new SimpleFloatProperty(dashboardController.dpYawProperty().getValue());
        airspeed = new SimpleFloatProperty(dashboardController.dpAirspeedProperty().getValue());
        altitude = new SimpleFloatProperty(dashboardController.dpAltitudeProperty().getValue());


    }

    private void bindProperties() {
        dashboardController.dpAirspeedProperty().bind(airspeed);
        dashboardController.dpHeadingProperty().bind(heading);
        dashboardController.dpYawProperty().bind(yaw);
        dashboardController.dpRollProperty().bind(roll);
        dashboardController.dpPitchProperty().bind(pitch);
        dashboardController.dpAltitudeProperty().bind(altitude);


    }

    public DashboardController getDashboardController() {
        return dashboardController;
    }

    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }


    public float getAltitude() {
        return altitude.get();
    }

    public FloatProperty altitudeProperty() {
        return altitude;
    }

    public void setAltitude(float altitude) {
        this.altitude.set(altitude);
    }

    public float getYaw() {
        return yaw.get();
    }

    public FloatProperty yawProperty() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw.set(yaw);
    }

    public float getPitch() {
        return pitch.get();
    }

    public FloatProperty pitchProperty() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch.set(pitch);
    }

    public float getRoll() {
        return roll.get();
    }

    public FloatProperty rollProperty() {
        return roll;
    }

    public void setRoll(float roll) {
        this.roll.set(roll);
    }

    public float getAirspeed() {
        return airspeed.get();
    }

    public FloatProperty airspeedProperty() {
        return airspeed;
    }

    public void setAirspeed(float airspeed) {
        this.airspeed.set(airspeed);
    }

    public float getHeading() {
        return heading.get();
    }

    public FloatProperty headingProperty() {
        return heading;
    }

    public void setHeading(float heading) {
        this.heading.set(heading);
    }
}
