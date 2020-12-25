# Noeto

<img src="/screenshots/icon.png" height="200px"/> <br>
A Note taking android application with all minimal features to be a handy tool.

**The application has following minimal features implemented**

- Login using google account
- Create notes
- Edit Notes
- Delete Notes
- Archive and Un-Archive notes by swiping recyclerview items left or right
- Logout from app


##### How to build ?
Create a [Firebase Project](https://firebase.google.com/docs/android/setup) and download the google_services.json file Put that file into the app folder and you are good to go

Open terminal and type the below command to generate debug build below are commands for Linux and Mac OS users
```
./gradlew assembleDebug
```

To build and install the app in running emulator
```
./gradlew installDebug
```

##### Or Just download the apk file in from [here](/builds/app-debug.apk)

##### How its being developed?:

All the android new Best Practises have been implemented:
- Android **navigational Component** for navigation between fragment and achieving a single activity based application
- **Room database** which is ORM over the sqlite Database
- **MVVM** architecture with Repository Pattern and storing fragment states while orientation changes
- Using **Dagger** for dependency Injection
- Simplicity and easy design of the app
- Android View Binding to avoid monotonous findViewById call and run-time exceptions and crashes.


##### Some Screenshots:

<div style="display:flex | space-evenly;" >
    <img src="/screenshots/1.png" height="400px" />
    <img src="/screenshots/2.png" height="400px" />
</div>
<div>
<br>
<div style="display:flex | space-evenly;" >
    <img src="/screenshots/3.png" height="400px" />
    <img src="/screenshots/4.png" height="400px" />
</div>

