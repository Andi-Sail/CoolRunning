# CoolRunning
Term Project for CNIT Fall-2019-CNIT-35500-001

## Source Files
### Activities layouts
The layouts for the activities for this project are in "/CoolRunning/app/src/main/res/layout/". 
The following files are used:
* activity_main.xml
  * The main activity is shown on startup and promps the user to enter the settings for the run and then start the rung
* activity_running.xml
  * The running activity is shown while the user is running. It shows current and target speed. It also shwos weather the user needs to slow down or speed up.
* activity_result.xml
  * After the user finishes running the result activity is shown with information about the time of the run and how good the user could stick to the target speed.
* activity_maps.xml
  * This activity displays a Google Maps fragment. On this map the tracked paht is displayed

### Activities Java
Each layout xml file has a correspoinding java file in "CoolRunning/app/src/main/java/ch/arebsame/coolrunning/".
* MainActivity.java
  * This file handles the interaction with the user unitl he starts running. It recieves the inital settings for the run and makes sure they are valid before the user starts running. When the user starts running the ActivityRunning is started
* ActivityRunning.java
  * From this activity the the services to monitor the speed are started. It periodically updates the user interface wheather the user is too fast or too slow according to the data of the services. When the user is finished running it stops the services and starts the ActivityResult
* ActivityResult
  This activity gets the values resultet from the run and displays them in the user interface. The user can choose to finish or display the map from the run.

### Services
While the user is running two services are used to obain new position and speed information, and to monitor the current speed and decide if the user is too fast or too slow. The java classes for the services are in "CoolRunning/app/src/main/java/ch/arebsame/coolrunning/"
* SpeedService.java
  *  This service accesses Androids localisation API using a LocationManager. It subscribes to position updates with the creteria to get a new possition every 0.1 seconds with no creteria on the distance to the last position.
  * This service is started as a Forground service and will display an notification once started. This is necessary the avoid androids limitation on localisation information when the app is in the background.
* SpeedMonitorService.java
  * This service compares the current speed to the target speed and decides if the user is too fast or too slow. It plays a sound if the user is wrong.

### other Java classes
The java classes used for this project are in "CoolRunning/app/src/main/java/ch/arebsame/coolrunning/"
The following files are used:
* CoolRunningCom.java
  * This is a static class used for communication between various activities and servides. This can be accessed to store or get current information about the apps status.
* GPXGenerator.java
  * This is used by the SpeedService to store the position information in a *.gpx file if the user wishes to save the track
* SpeedMonitor.java
  * This class is used by the SpeedMonitorService and compares the current to target speed and decies wheather the user is too fast or too slow.
* TargetSpeedUpdater.java
  * This class is used by the SpeedMonitorService. It starts a threaed that will periodically update the target speed according to the mode setting of the user.

### Enums
Severals emuns are used as common values for several definitions saved in "CoolRunning/app/src/main/java/ch/arebsame/coolrunning/".
* RunningError.java
  * This defines the states if the user is too fast, too slow or running correct.
* RunningMode.java
  * This defins the possible running modes for the user