# CoolRunning
This readme describes the Term Project for CNIT Fall-2019-CNIT-35500-001 at Purdue Polytechnic.

## License
This project is open source and available under MIT License at: https://github.com/Andi-Sail/CoolRunning

## Brief Description
Cool Running is a speed tracking mobile application for android devices that tracks the speed of the user while either walking, running, or riding on a vehicle. If the runner’s speed is out of the selected range of the speed, then the program will send the alarm to the runner indicating the status.

### Running Modes
The following four running programs will be implemented:
* Interval
  * The target speed alternates between fast and slow at a constant time interval.
* Increasing speed
  * The target speed begins at a start value and always increase after a time interval. The goal for the user is to keep up as long as possible.
* Constant speed
  * The target speed is set to a constant value and stays on that value for the full run.
* Random speed
  * The target speed changes at a constant time interval to a new random speed. That way the user can practice to adapt to different speeds.

For each program the difficulty level determines the various target speed and time intervals. These numbers will be evaluated during testing to see in a real life scenario what numbers will make the most sense.

# How to compile and run
## User
This app can be installed on any android phone with the given APK: CoolRunning/app/release/app-release.apk
Simply copy the APK to your phone and install it.
Minimum Required Android version: 8.0 (Oreo, SDK-Version 26)
Required Permissions:
* fine location
* coarse location
* foreground service
* read external storage
* write external storage

## Developer
This project can be opened in Android Studio and compiled.

### Google Maps API-Key
The device for development and/or the key to sign  the APK need to be registered for the given API key. Non Team members will need to create a new key and replace the existing one.
Go to https://developers.google.com/maps/documentation/android-sdk/get-api-key for further information

### Custom target speed
The target speeds are set to preferences of the developers. They can be adjusted to every individuals preferences in TargetSpeedUpdater.java

# Implementation
This App consists of four activities and two services. One of the services is run as a foreground service.

## Cross Communication
For several threads, services and the user interface to communicate there is the "CoolRunningCom" class. This class provides static access to all the necessary data. This data can always be accessed and is used to asynchronous store and read data. To avoid reentrancy problems the data is encapsulated in static synchronized methods.

## Source Files
### Activities layouts
The layouts for the activities for this project are in "/CoolRunning/app/src/main/res/layout/". 
The following files are used:
* activity main.xml
  * The main activity is shown on startup and prompts the user to enter the settings for the run and then start the rung
* activity running.xml
  * The running activity is shown while the user is running. It shows current and target speed. It also shows weather the user needs to slow down or speed up.
* activity result.xml
  * After the user finishes running the result activity is shown with information about the time of the run and how good the user could stick to the target speed.
* activity maps.xml
  * This activity displays a Google Maps fragment. On this map the tracked path is displayed

### Activities Java
Each layout xml file has a corresponding java file in "CoolRunning/app/src/main/java/ch/arebsame/coolrunning/".
* MainActivity.java
  * This file handles the interaction with the user until he starts running. It receives the initial settings for the run and makes sure they are valid before the user starts running. When the user starts running the ActivityRunning is started
* ActivityRunning.java
  * From this activity the the services to monitor the speed are started. It periodically updates the user interface weather the user is too fast or too slow according to the data of the services. When the user is finished running it stops the services and starts the ActivityResult
* ActivityResult
  This activity gets the values resulted from the run and displays them in the user interface. The user can choose to finish or display the map from the run.

### Services
While the user is running two services are used to obtain new position and speed information, and to monitor the current speed and decide if the user is too fast or too slow. The java classes for the services are in "CoolRunning/app/src/main/java/ch/arebsame/coolrunning/"
* SpeedService.java
  *  This service accesses Androids localization API using a LocationManager. It subscribes to position updates with the criteria to get a new position every 0.1 seconds with no criteria on the distance to the last position.
  * This service is started as a foreground service and will display an notification once started. This is necessary the avoid androids limitation on localization information when the app is in the background.
* SpeedMonitorService.java
  * This service compares the current speed to the target speed and decides if the user is too fast or too slow. It plays a sound if the user is wrong.

### other Java classes
The java classes used for this project are in "CoolRunning/app/src/main/java/ch/arebsame/coolrunning/"
The following files are used:
* CoolRunningCom.java
  * This is a static class used for communication between various activities and services. This can be accessed to store or get current information about the apps status.
* GPXGenerator.java
  * This is used by the SpeedService to store the position information in a *.gpx file if the user wishes to save the track
* SpeedMonitor.java
  * This class is used by the SpeedMonitorService and compares the current to target speed and deices weather the user is too fast or too slow.
* TargetSpeedUpdater.java
  * This class is used by the SpeedMonitorService. It starts a thread that will periodically update the target speed according to the mode setting of the user.

### Enums
Several emuns are used as common values for several definitions saved in "CoolRunning/app/src/main/java/ch/arebsame/coolrunning/".
* RunningError.java
  * This defines the states if the user is too fast, too slow or running correct.
* RunningMode.java
  * This defines the possible running modes for the user