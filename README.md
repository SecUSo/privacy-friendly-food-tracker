# Privacy Friendly Food Tracker

Privacy Friendly Food Tracker allows you to track your daily calorie consumption. 

To do this, you can enter all your consumed food and drink as well as its weight. The app will then calculate total daily caloric consumption.
 
The app also allows an analysis of calorie consumption over a larger timeframe such as weeks or months.

This app belongs to the group “Privacy Friendly Apps” developed by the research group SECUSO at Karlsruhe Institute of Technology. You can find more information at [the SECUSO website](secuso.org/pfa).

You can find this app on the Google Play store and F-Droid.

## Download and more Information

Further development requires Android Studio, we recommend to use at least version 3.1.1
 
### API Reference

Minimum SDK: 21
Target SDK: 27 

## Contribution Guide

Generally you are very welcome to contribute new functionality to this application or report/fix any bugs that you find.

### Bug reports
In your bug report, be sure to include:

- The state the app was in when the bug occurred
- What you expected to happen
- What actually happened
- Any error messages that were shown to you

### Contributing new functionality
To contribute any new functionality, be it new functions or something like translations, just open a Pull Request and one of the contributors will get back to you as soon as possible.
#### How to build
This app uses Gradle for dependency management. To get started, simply clone this repository and run `gradle build`, Gradle will manage the rest for you.
#### Code structure
- *privacy-friendly-food-tracker/app/src/main/java/org/secuso/privacyfriendlyfoodtracker/*: This is where the main functionality of the app is stored. It has several subfolders, the important ones are listed below:
  - */activities/*: This is where all activities are stored. This is generally what you see on screen.
  - */activities/adapters/*: These are all adapters that are needed by several classes such as Fragments. This also contains the database façade which is used to talk to the (encrypted) database.
  - */database/*: This is where the database structure and the database functions are stored. The database uses Room to create an encrypted database with a key that is generated via the KeyGenerationActivity in /activities. The database uses a Data Access Object pattern to retrieve entries.
  - */network/*: This is the package that is used to call the Openfoodfact API. It is only used in the search function.
  - */customviews/*: This is for all views that are custom created for this application.
  
### Unwanted Changes

The Privacy Friendly Apps are a group of Android apps that are optimized regarding the user's privacy. Therefore, Pull Requests that contain the following functionality will be rejected:

- Analytics or advertisement frameworks
- User tracking (e.g. sending of data to a third party)
- Any that use of libraries that do not comply the license of the corresponding Privacy Friendly App (GPLv3 or Apache2).
- Unnecessary use of Android permissions. If new functionality is added that requires the usage of an Android permission you should clearly explain the Pull Request why this permission is required.



## License

Privacy Friendly App Example is licensed under the GPLv3.
Copyright (C) 2016-2018  Karola Marky

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program. If not, see <http://www.gnu.org/licenses/>.

The icons used in the nagivation drawer are licensed under the [CC BY 2.5](http://creativecommons.org/licenses/by/2.5/). In addition to them the app uses icons from [Google Design Material Icons](https://design.google.com/icons/index.html) licensed under Apache License Version 2.0. All other images (the logo of Privacy Friendly Apps, the SECUSO logo and the app logo) copyright [Karlsruhe Institute of Technology](www.kit.edu) (2018-2019).

## Contributors

App-Icon: <br />
Markus Hau<br />

Github-Users: <br />
Yonjuni (Karola Marky)<br />
Kamuno (Christopher Beckmann) <br />
cubi3192 (André Lutz) <br />
simonre (Simon Reinkemeier)





