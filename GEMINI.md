# Project Overview

This is an Android application for booking hotel rooms. It is built with Java and uses Firebase for backend services, including authentication, database, and storage. The UI is built with XML layouts and follows the Material Design guidelines.

## Building and Running

To build and run this project, you will need Android Studio.

1.  **Clone the repository:**
    ```bash
    git clone <repository-url>
    ```
2.  **Open the project in Android Studio.**
3.  **Connect a device or start an emulator.**
4.  **Run the app.**

The project uses Gradle for building. The main build configuration files are `build.gradle` and `app/build.gradle`.

## Development Conventions

*   **Language:** Java
*   **UI:** XML layouts
*   **Backend:** Firebase (Authentication, Firestore, Storage)
*   **Image Loading:** Glide
*   **Code Style:** The code follows standard Java and Android conventions.

## Key Files

*   `app/src/main/java/com/example/hotelbookingapp/activities/WelcomeActivity.java`: The initial screen of the app, providing options to log in or register.
*   `app/src/main/java/com/example/hotelbookingapp/activities/LoginActivity.java`: Handles user authentication using Firebase Auth.
*   `app/src/main/java/com/example/hotelbookingapp/activities/RegisterActivity.java`: Handles new user registration.
*   `app/src/main/java/com/example/hotelbookingapp/activities/ProfileActivity.java`: Displays the user's profile information.
*   `app/build.gradle`: Contains the project's dependencies and build settings.
*   `app/src/main/AndroidManifest.xml`: Describes the app's components and permissions.
*   `readme.md`: Provides a detailed overview of the project (in Vietnamese).
