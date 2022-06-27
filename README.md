# Fleet Management System - PTM2 Project  
![image](https://user-images.githubusercontent.com/89278943/175933508-e03b7df9-d12f-4e1b-bee1-69dbfd34111b.png)
## Collaborators
- [Ron Greenberg](https://github.com/RonGreenberg)
- [Aviv Keinan](https://github.com/avivk9)
- [Sheer Shveka](https://github.com/SheerShveka)
- [Paz Haimovich](https://github.com/pazhaimovich)
- [Yonatan Birman](https://github.com/yonatan1710)
- [Guy Levy](https://github.com/guylevy2307)
- [Itamar Azmoni](https://github.com/Itamar-Azmoni)
## Video Links
- [**Main Demo Video**](https://youtu.be/ctTXNjZxCc4)
- [**Agent Video**](https://youtu.be/n_ZONatYvXc)
- [**Backend Video**](https://youtu.be/yWrjw7Z6XQg)
- [**Frontend Video**](https://youtu.be/sMqKhha4m-M)
## Project Description
This is our final project in Advanced Software Development course at the College of Management, conducted by Dr. Eliahu Khalastchi.  
We developed a system that can manage and control a fleet of aircrafts remotely. Each aircraft is represented by an **Agent** running on a separate machine, which uses the FlightGear Flight Simulator to simulate flights. All agents connect to the central **Backend** component, which can send them commands, receive data and store it in a database. The backend can also handle the **Frontend**, which is a desktop application consisting of multiple tabs for different purposes:  
1) The user can view the airplanes and flights on a live map.  
2) The user can view statistics about the fleet.  
3) The user can monitor a specific plane's dashboard and other properties mid-flight.  
4) The user can remotely control a specific plane using a joystick or an auto-pilot script, written in a user-defined programming language.  
5) The user can replay a recording of a past flight.  
6) The application can detect anomalies in flight properties, both in a live flight and an offline recording, using different detection algorithms.
## Architecture
![agent](https://user-images.githubusercontent.com/89278943/167743108-7f6f4432-08a3-465e-b814-1f1fe924b901.jpg)  
## Differences between design and implementation
Thanks to good planning and designing in advance, there were very few changes between the design and the implementation. For example, we planned that the agent would maintain a mapping from each simulator variable to its current value so that we can query it any time, but we realized it would be a lot simpler to send a get request directly to FlightGear. Another example is that we planned to implement the communication with the Google Maps API by ourselves for the map in Fleet Overview, but we found a JavaFX library that provides a map component and does it for us.