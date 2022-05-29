package model;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Observable;

//communication with FlightGear
public class Model extends Observable {

    HashMap<String, String> props;
    Socket fg;
    PrintWriter outToFg;
    public Model(String propertiesFileName) {
        props = new HashMap<>();
        try {
            BufferedReader in = new BufferedReader(new FileReader(propertiesFileName));
            String line;
            while ((line=in.readLine()) != null) {
                String[] sp = line.split(",");
                props.put(sp[0],sp[1]);
            }
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            int port = Integer.parseInt(props.get("port"));
            fg = new Socket(props.get("ip"),port);
            outToFg = new PrintWriter(fg.getOutputStream());
            //connected to flighGear :)




            //==========================//

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /*
    aileron,set /controls/flight/aileron
    elevators,set /controls/flight/elevator
    rudder,set /controls/flight/rudder
    throttle,set /controls/engines/current-engine/throttle
     */

    //set instructions
    public void setAileron(double x) {
        outToFg.println(props.get("aileron") + " " + x);
        outToFg.flush();
    }
    public void setElevator(double x) {
        outToFg.println(props.get("elevators") + " " + x);
        outToFg.flush();
    }
    public void setRudder(double x) {
        outToFg.println(props.get("rudder") + " " + x);
        outToFg.flush();
    }
    public void setThrottle(double x) {
        System.out.println("in");
        outToFg.println(props.get("throttle") + " " + x);
        outToFg.flush();

    }
    //============================================//
    @Override
    protected void finalize()
    {
        try {
            fg.close();
            outToFg.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}