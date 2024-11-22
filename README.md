# 👟 Wanderlust - The Traveller's App

<a href="https://github.com/harismuneer"><img alt="views" title="Github views" src="https://komarev.com/ghpvc/?username=harismuneer&style=flat-square" width="125"/></a>
[![Open Source Love svg1](https://badges.frapsoft.com/os/v1/open-source.svg?v=103)](#)
[![GitHub Forks](https://img.shields.io/github/forks/harismuneer/Wanderlust-The-Travellers-App.svg?style=social&label=Fork&maxAge=2592000)](https://www.github.com/harismuneer/Wanderlust-The-Travellers-App/fork)
[![GitHub Issues](https://img.shields.io/github/issues/harismuneer/Wanderlust-The-Travellers-App.svg?style=flat&label=Issues&maxAge=2592000)](https://www.github.com/harismuneer/Wanderlust-The-Travellers-App/issues)
[![contributions welcome](https://img.shields.io/badge/contributions-welcome-brightgreen.svg?style=flat&label=Contributions&colorA=red&colorB=black	)](#)


## Project Description
Wanderlust is an **Android app** that allows its users to keep track of their journey experiences by allowing them to save their journeys in the form of a trip diary and have all their travel experiences saved at one place. Its more of a **Check-in** application. In order to spice things up, we gave this app a **social platform** feel by allowing the user to add friends and share his profile containing journeys count and friends count with each other. 

## Tools and APIs Used
* Android Studio is used to develop the whole android application in Java
* Google Maps API
* Facebook and Twitter SDKs (for fetching and uploading data on these platforms)
* Google Places API
* Firebase Authentication
* Cloud Messaging using Google Firebase
* Firebase Storage 
* Firebase Analytics including Crashlytics, Performance Monitoring
* Google Admob APIs
* Firebase Realtime Database


## Screenshots of Interfaces and their Description

<p align="middle">
  <img src="../master/images/s1.png"/>
 </p>
 
 <p align="middle">
  <img src="../master/images/s2.png"/>
 </p>

## Functionalities

Wanderlust uses **Firebase Phone Number Authentication** which verifies a user’s phone number by sending a code to it. The user is then authenticated to use the app.

Our app also has a **Journeys Map** which has journeys as labels placed on locations which have been visited.

The app incorporates a **Step Count** feature which allows the travellers to count the number of steps they took while being on a journey. It also incorporates a **Compass using Magnetic Sensors** which proves helpful for travellers while travelling.

In order to make the app usable for a wide range of audiences, our app supports **language support in Urdu**. We have also added **Text-to-Speech** feature by reading the journey title and description for users with some disabilities. Moreover, our app can also be used using **Voice Commands**. The user speaks the command and our app recognizes the command e.g Exit App will close the app. 

Wanderlust seamlessly incorporates **Social Media Integration using Facebook and Twitter APIs**. This allows users to share their journey experiences on these social platforms. 

Moreover, the app also incorporates **Google Admob** with customized ads for targeting travellers. We have also integrated **Firebase Analytics** which allows us to have deeper insights into how a user uses our apps so that we can use those insights and improve our app.

Wanderlust was designed with **major focus on HCI**. It incorporates **Ben Shneiderman’s 'Golden Rules of Interface Design'** including

* Consistency
* Design Dialogs to Yield Closure
* Offer Informative Feedback
* Prevention of Errors
* Keep Users in Control
* Reduce Cognitive Load
* Universal Usability

Moreover the **modern UI/UX guidelines for Android App Design** have been followed including:

* Visibility (making features visible to user, used Bottom Navigation Bar)
* Splash Screens
* Audio Feedback using Text to Speech
* Enhanced Accessibility Using Google Maps

The attached presentation ‘[Wanderlust-Rules](../master/Wanderlust-Rules.pptx)’ explains how our app utilized these HCI rules in our interfaces.

## How to Run

An apk file named [Wanderlust.apk](../master/Wanderlust.apk) is provided which can be installed on an Android Phone. 

In order to have a look at the code files and understand the working, simply download this repository and open Android Studio and browse to the downloaded project and open it. It will load the project files and the code will be ready to run. Before running the app, use your Google Account to register this Application on Firebase Console, with any name you want. Then using that Google Account, login to Android Studio. Then you can run the project. If you face any issues with the Firebase Database, you can ping me up.

<br>
<hr>

## Author
You can get in touch with me on my LinkedIn Profile: [![LinkedIn Link](https://img.shields.io/badge/Connect-harismuneer-blue.svg?logo=linkedin&longCache=true&style=social&label=Follow)](https://www.linkedin.com/in/harismuneer)

You can also follow my GitHub Profile to stay updated about my latest projects: [![GitHub Follow](https://img.shields.io/badge/Connect-harismuneer-blue.svg?logo=Github&longCache=true&style=social&label=Follow)](https://github.com/harismuneer)

If you liked the repo then kindly support it by giving it a star ⭐ and share in your circles so more people can benefit from the effort.

## Contributions Welcome
[![GitHub Issues](https://img.shields.io/github/issues/harismuneer/Wanderlust-The-Travellers-App.svg?style=flat&label=Issues&maxAge=2592000)](https://www.github.com/harismuneer/Wanderlust-The-Travellers-App/issues)

If you find any bugs, have suggestions, or face issues:

- Open an Issue in the Issues Tab to discuss them.
- Submit a Pull Request to propose fixes or improvements.
- Review Pull Requests from other contributors to help maintain the project's quality and progress.

This project thrives on community collaboration! Members are encouraged to take the initiative, support one another, and actively engage in all aspects of the project. Whether it’s debugging, fixing issues, or brainstorming new ideas, your contributions are what keep this project moving forward.

With modern AI tools like ChatGPT, solving challenges and contributing effectively is easier than ever. Let’s work together to make this project the best it can be! 🚀

## License
[![MIT](https://img.shields.io/cocoapods/l/AFNetworking.svg?style=style&label=License&maxAge=2592000)](../master/LICENSE)

Copyright (c) 2018-present, harismuneer, HassaanElahi, FarhanShoukat       

<!-- PROFILE_INTRO_START -->
<!-- PROFILE_INTRO_END -->
