# OpenMovieDB
This app loads a list of movies and shows them in a list. Each list item is a cardview containing details about the movie like poster, title, year abd type. There is also a button to favorite and un-favorite a movie. Also supports pagination.

### App Architecture:
This application follows MVVM architecture pattern with View(Activity), ViewModel and Repository.

#### Extra Features:
1. Supports tablets
2. Securely storing API in a properties file rather than hard-coding it into the code.
3. Pagination for Movie Search

#### Libraries Used:
This app uses the latest AndroidX packages.

Architecture  component libraries used:
Navigation, Lifecycle, ViewModel, Room and Kotlin Coroutines.

3rd Party libraries used:
* Retrofit - for Networking
* Glide - for Images

#### Notes:
* Built using Android Studio 3.5 beta 4.
* Assumed "imdbID"s are unique. Used them as primary keys for Room.

#### Building the APK:
1. Navigate to the project's root directory from command line.
2. From command line,  run "./gradlew assembleDebug".
3. Once build is complete, an .apk is generated in the "app/build/outputs/apk/debug/app-debug.apk"
4. You can install the apk on to an Android device by running "adb install app-debug.apk" from the command line.

