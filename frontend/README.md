# Frontend
## Team
- [Guy Levy](https://github.com/guylevy2307)
- [Itamar Azmoni](https://github.com/Itamar-Azmoni)
- [Paz Haimovich](https://github.com/pazhaimovich)
## Architecture
![frontend_architecture](https://user-images.githubusercontent.com/89278943/175931293-65043821-d702-46e6-b57c-d1542f8dbc5c.png)
The frontend is a desktop application, consisting of the following tabs:  
1) **Fleet Overview:** Includes a live map of the airplanes and some graphs with statistics about the fleet.  
2) **Monitoring:** Can monitor an active plane with a dashboard and enables the user to select a flight property and receive a graph showing its value change over time. It also contains a graph for visualizing anomaly detection.  
3) **Teleoperation:** Enables the user to remotely control a plane using a joystick or an auto-pilot script. Contains a dashboard as well.  
4) **Time Capsule:** Provides an option to replay a recording of a past flight, includes a timeline and playback buttons, and all the controls from the Monitoring tab. We can connect to a local FlightGear instance, to actually see the plane fly accordingly.  
**Design Patterns used:** MVVM, Dependency Injection, Facade