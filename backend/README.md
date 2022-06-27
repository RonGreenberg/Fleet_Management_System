# Backend
## Team
- [Ron Greenberg](https://github.com/RonGreenberg)
- [Sheer Shveka](https://github.com/SheerShveka)
## Architecture
![backend_architecture](https://user-images.githubusercontent.com/89278943/175929429-722c4a84-2a28-4437-82c6-e8b49a444496.png)
The backend contains a database for storing data about airplanes and flights. It also contains the Interpreter for the auto-pilot scripting language.  
The backend is built in MVC architecture: The model contains the logic related to DB communication and the Interpreter. The controller is responsible for maintaining a server for all agents and another server for the Frontend. The view is optional, and provides a CLI that prints the currently active tasks/threads every few seconds, for debugging purposes.  
**Design Patterns used:** MVC, Factory, Command, Composite, Singleton