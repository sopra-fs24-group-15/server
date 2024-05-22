# MemeBattle

## Introduction
Create the best Meme and win the game!

We were motivated to create MemeBattle to build a community-centric, interactive, creative and entertaining online game.

### Technologies used

For the MemeBattle Webapplication we used the following technologies:

- Frontend Development: HMTL, CSS, React, Imgflip API
- Backend Development: Java, Springboot, RESTful API, Imgflip API
- Deployment and Hosting: Google Cloud and Docker
- Testing: Mockito and JUnit Tests
- CI/CD: GitHub Actions

### High-level Components
1. #### Lobby: 
    ###### Role
    The Lobby component serves as the central hub where users can gather, interact, and play a game. It manages the overall flow of the user experience and allows the LobbyOwner to change the GameSettings. It also allows every User to change his profilepicture and create memes.
    ##### Correlation
    The Lobby links all other components together. It ensures seamless transition and interaction between them.
    ##### Reference
    The main file for the Lobby component can be found at 'src/main/java/ch.uzh.ifi.hase.soprafs24/entity/Lobby' 
2. #### User:
    ##### Role
    The User components handles the creation of a UserProfile and all related aspects to it. This includes user authentication, so that there cannot be username duplicates. Additionally, it also handles profilepictures and saves them for later display in the ranking screen.
    ##### Correlation
    The User Component communicates with the Lobby to display User informations and with the Meme Component to save the votes a User has earned for their created meme.  
    ##### Reference
    The main class for the User component is located in 'src/main/java/ch.uzh.ifi.hase.soprafs24/entity/User'
    
3. #### Meme: 
    ##### Role
    The Meme component is responsible for the creation and displaying of memes within the application. It handles all the meme-related functionalities such as creating a meme.
    ##### Correlation
    The Meme component interacts directly with the Lobby and the User component. It provides memes that users can interact with in the Lobby. It also tracks the votes from each user through the User component.
    ##### Reference
    the main functions for the Meme component are defined in 'src/main/java/ch.uzh.ifi.hase.soprafs24/controller/MemeController'


### Launch & Deployment
Make sure you have the required Software installed:
for our project this includes Java 17 (for Windows, also make sure that your **JAVA_HOME** environment variable is set to the correct version of Java).

The following extensions can help you get started more easily:
-   `vmware.vscode-spring-boot`
-   `vscjava.vscode-spring-initializr`
-   `vscjava.vscode-spring-boot-dashboard`
-   `vscjava.vscode-java-pack`

**Note:** You'll need to build the project first with Gradle, just click on the `build` command in the _Gradle Tasks_ extension. Then check the _Spring Boot Dashboard_ extension if it already shows `soprafs24` and hit the play button to start the server. If it doesn't show up, restart VS Code and check again.


Then start by cloning the Repository:

```bash
git clone ....
```

To build the application you can use the local Gradle Wrapper:

-   macOS: `./gradlew`
-   Linux: `./gradlew`
-   Windows: `./gradlew.bat`

to build it use the following:

```bash
./gradlew build
```

and then to run it:
```bash
./gradlew bootRun
```

You can verify that the server is running by visiting **localhost:8080** in your browser.

##### development mode
You can start the backend in development mode, this will automatically trigger a new build and reload the application once the content of a file has been changed.

Start two terminal windows and run:

```bash
./gradlew build --continuous
```

and in the other one:

```bash
./gradlew bootRun
```

If you want to avoid running all tests with every change, use the following command instead:

```bash
./gradlew build --continuous -xtest
```
### Roadmap
MemeBattle currently consists of two GameModes. There are many more possible GameModes to implement. This could for example be a  Gamemode where Users can choose Pictures from their own gallery as the Template or where users could write a Topic themselves.
Furthermore, ....

### Authors and acknowledgment
Authors: Jana Muheim, Marc Huber, Marc Amsler, Christof Steiner, Gian Seifert

Acknowledgment: We want to thank the UZH and Prof. Fritz for offering the SoPra module. Without this module MemeBattle most likely would'nt exist. Furthermore, we want to say a big thanks to our tutor Miro Vannini. With his important and well-thought inputs MemeBattle wouldn't be what it is today. 

### License

GNU GPLv3 (?)


