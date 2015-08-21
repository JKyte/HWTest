This project can be directly imported into eclipse via the following steps

## Importing into Eclipse IDE
1. Open Eclipse
2. In the "Package Explorer" view, right click and select the "Import" option
3. Select the "Git" -> "Projects from Git" option
4. Select the URI option
5. Paste in the kytebot URI --> "https://github.com/JKyte/kytebot.git"
6. Select the branch you want to clone (default is "master")
7. Eclipse should import everything and you're ready to begin contributing to the kytebot project

## Running the bot (from Eclipse)
1. Open the kytebot project in Eclipse
2. Open src/main/java/master/KickOffMaster.java
3. Edit the variables for 'nick', 'passwd', '#startchan', and 'irc.server.net'
4. Run the class as a Java application


## Current Sprint Goals (V0.4 sprint)
1. Expand unit testing of core code
2. Add logic for kytebot commands


## Wishlist of Features
1. Alerts based on IRC messages
2. Trusted users, trusted user actions
3. Framework for automatic replies/kytebot commands
4. Mavenize to ease build process
5. Use JavaFX for UI