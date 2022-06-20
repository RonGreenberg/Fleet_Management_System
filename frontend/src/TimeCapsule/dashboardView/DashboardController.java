package TimeCapsule.dashboardView;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.fxml.*;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class DashboardController implements Initializable {

    @FXML
    private Text roll;
    @FXML
    private Text yaw;
    @FXML
    private Text pitch;
    @FXML
    private Text altitude;
    @FXML
    private ImageView heading;
    @FXML
    private ImageView airspeed;
    
    private FloatProperty dpHeading,dpAirspeed , dpRoll, dpYaw,dpPitch,dpAltitude;

    // Add a public no-args constructor
    public DashboardController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	//get the setting value and initialize

    	dpHeading = new SimpleFloatProperty(0);
    	dpAirspeed = new SimpleFloatProperty(0);
    	dpRoll = new SimpleFloatProperty(0);
    	dpYaw = new SimpleFloatProperty(0);
    	dpPitch = new SimpleFloatProperty(0);
    	dpAltitude = new SimpleFloatProperty(0);
    	
    	//end
    	dpRoll.addListener( v->updateText());
    	dpYaw.addListener( v->updateText());
    	dpPitch.addListener( v->updateText());
    	dpAltitude.addListener( v->updateText());
    	
    	dpHeading.addListener(v->updateImages());
    	dpAirspeed.addListener(v->updateImages());
      
    	
    }
    public void updateImages() {
    	 heading.setRotate(dpHeading.getValue());
        if(dpAirspeed.getValue()<40) {
            airspeed.setRotate(dpAirspeed.getValue());
        }
        else if( dpAirspeed.getValue()<=60) {
            airspeed.setRotate(dpAirspeed.getValue()+20);
        }
        else if( dpAirspeed.getValue()<=80) {
            airspeed.setRotate(dpAirspeed.getValue()+30);
        }
        else if( dpAirspeed.getValue()<=100) {
            airspeed.setRotate(dpAirspeed.getValue()+50);
        }
        else if( dpAirspeed.getValue()<=120) {
            airspeed.setRotate(dpAirspeed.getValue()+70);
        }
        else if( dpAirspeed.getValue()<=140) {
            airspeed.setRotate(dpAirspeed.getValue()+80);
        }
        else if( dpAirspeed.getValue()<=160) {
            airspeed.setRotate(dpAirspeed.getValue()+90);
        }
        else if( dpAirspeed.getValue()<=180) {
            airspeed.setRotate(dpAirspeed.getValue()+95);
        }
        else if( dpAirspeed.getValue()==200) {
            airspeed.setRotate(dpAirspeed.getValue()+100);
        }
    }
    public void updateText() {
    	 roll.setText(dpRoll.getValue().toString());
         yaw.setText( dpYaw.getValue().toString() );
         pitch.setText( dpPitch.getValue().toString());
         altitude.setText(dpAltitude.getValue().toString());
    }


    public Text getRoll() {
        return roll;
    }

    public void setRoll(Text roll) {
        this.roll = roll;
    }

    public Text getYaw() {
        return yaw;
    }

    public void setYaw(Text yaw) {
        this.yaw = yaw;
    }

    public Text getPitch() {
        return pitch;
    }

    public void setPitch(Text pitch) {
        this.pitch = pitch;
    }

    public Text getAltitude() {
        return altitude;
    }

    public void setAltitude(Text altitude) {
        this.altitude = altitude;
    }

    public ImageView getHeading() {
        return heading;
    }

    public void setHeading(ImageView heading) {
        this.heading = heading;
    }

    public ImageView getAirspeed() {
        return airspeed;
    }

    public void setAirspeed(ImageView airspeed) {
        this.airspeed = airspeed;
    }


    public float getDpHeading() {
        return dpHeading.get();
    }

    public FloatProperty dpHeadingProperty() {
        return dpHeading;
    }

    public void setDpHeading(float dpHeading) {
        this.dpHeading.set(dpHeading);
    }

    public float getDpAirspeed() {
        return dpAirspeed.get();
    }

    public FloatProperty dpAirspeedProperty() {
        return dpAirspeed;
    }

    public void setDpAirspeed(float dpAirspeed) {
        this.dpAirspeed.set(dpAirspeed);
    }

    public float getDpRoll() {
        return dpRoll.get();
    }

    public FloatProperty dpRollProperty() {
        return dpRoll;
    }

    public void setDpRoll(float dpRoll) {
        this.dpRoll.set(dpRoll);
    }

    public float getDpYaw() {
        return dpYaw.get();
    }

    public FloatProperty dpYawProperty() {
        return dpYaw;
    }

    public void setDpYaw(float dpYaw) {
        this.dpYaw.set(dpYaw);
    }

    public float getDpPitch() {
        return dpPitch.get();
    }

    public FloatProperty dpPitchProperty() {
        return dpPitch;
    }

    public void setDpPitch(float dpPitch) {
        this.dpPitch.set(dpPitch);
    }

    public float getDpAltitude() {
        return dpAltitude.get();
    }

    public FloatProperty dpAltitudeProperty() {
        return dpAltitude;
    }

    public void setDpAltitude(float dpAltitude) {
        this.dpAltitude.set(dpAltitude);
    }
}
