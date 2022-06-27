# Agent
## Team
- [Yonatan Birman](https://github.com/yonatan1710)
- [Aviv Keinan](https://github.com/avivk9)
## Architecture
![agent_architecture](https://user-images.githubusercontent.com/89278943/175927691-b0384d16-70a9-4438-b10a-d6ae263d2e82.png)

Each agent connects to a FlightGear instance. The agent was built in MVC architecture, so it contains a model, a view and a controller.  
The model communicates with FlightGear and can send command and receive flight data in CSV format simultaneously.  
The controller is responsible for communication with the Backend. The view is optional, and provides a CLI with verbose output, for debugging purposes.  
**Design Patterns used:** MVC, Observer, Dependency Injection.