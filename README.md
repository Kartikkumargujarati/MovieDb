# Salesforce Challenge - Interview Challenge App from Salesforce
This is an Android application written in Kotlin built as part of my (Kartik Kumar Gujarati) interview process at Salesforce.
Per requirements, this app loads a list of movies and shows them in a list. Each list item is a cardview containing details about the movie like poster, title, year abd type. There is also a button to favorite and un-favorite a movie. Also supports pagination.

### App Architecture:
This application follows MVVM architecture pattern with View(Activity), ViewModel and Repository.

#### Extra Features:
1. Supports tablets
2. Securely storing API in a properties file rather than hard-coding it into the code.
3. Pagination for Movie Search

#### Libraries Used:
This app uses the latest AndroidX packages.

Architecture  component libraries used:
Lifecycle, ViewModel, Room and Kotlin Coroutines.

3rd Party libraries used:
* Retrofit - for Networking
* Glide - for Images

#### Notes:
* Built using Android Studio 3.5 beta 4.
* Assumed "imdbID"s are unique. Used them as primary keys for Room.
* The requirements ask to show "director" and "plot" data in the search result. However, the movies returned by "By Search" parameters does not include that data. So, showing the "director" and "plot" information in the details screen.
* Had to sign-up to get a new an API Key.

#### Could have done (given more time):
* Dependency Injection
* Re-use the login in repository by using more generics.
* Write test
* Move dimensions from layouts to a dimes file

#### Building the APK:
1. Navigate to the project's root directory from command line.
2. From command line,  run "./gradlew assembleDebug".
3. Once build is complete, an .apk is generated in the "app/build/outputs/apk/debug/app-debug.apk"
4. You can install the apk on to an Android device by running "adb install app-debug.apk" from the command line.

