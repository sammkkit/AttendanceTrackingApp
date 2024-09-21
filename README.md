# Automatic Attendance Manager

The Automatic Attendance Manager app streamlines and automates attendance tracking for employees based on geolocation. It allows users to check in and check out based on their proximity to the office location, and calculates total working hours for the day. The app is built with Jetpack Compose, Firebase Authentication, and Google Maps.

## Features

- **Authentication**: Sign up and log in using Firebase Authentication.
- **Office Location Setup**: Users can select their office location on a map (using Google Maps) during the sign-up process.
- **Geolocation-Based Attendance**: The app periodically fetches the user's location and calculates whether they are inside or outside the office location.
- **Automatic Check-in/Check-out**: Users' attendance is recorded automatically based on their proximity to the office.
- **Work Hours Calculation**: Total working hours for the day are calculated based on check-ins and check-outs.
- **Manual Location Setup**: Users can manually set up their office location on a map.
- **Notification System**: The app sends notifications to remind users to check in or out.

## Tech Stack

- **Frontend**: Jetpack Compose for UI
- **Backend**: Firebase (Authentication, Realtime Database)
- **Geolocation**: Google Maps, Fused Location Provider
- **Background Processing**: WorkManager for periodic location updates
- **Dependency Injection**: Hilt
- **Architecture**: Clean architecture with separation into Data, Domain, and Presentation layers

## Screenshots

_Add screenshots here_

## Installation

1. Clone the repository:
    ```bash
    git clone https://github.com/your-repo/attendance-manager.git
    ```
2. Open the project in Android Studio.
3. Set up your Firebase project:
    - Go to the [Firebase Console](https://console.firebase.google.com/).
    - Create a new project.
    - Enable Firebase Authentication and Realtime Database.
    - Download the `google-services.json` file and place it in the `app` directory.
4. Add your Google Maps API key:
    - Get your API key from the [Google Cloud Console](https://console.cloud.google.com/).
    - Add the API key to the `local.properties` file:
      ```
      MAPS_API_KEY=your-api-key
      ```
5. Build and run the project on your Android device or emulator.

## Firebase Setup

- Enable Firebase Authentication (Email/Password).
- Set up the Firebase Realtime Database to store user data and attendance records.

## Usage

1. **Sign Up**: New users can sign up by entering their name, email, password, and selecting their office location on a map.
2. **Location Selection**: During sign-up, users select their office location on a map using Google Maps. The location is saved to Firebase.
3. **Attendance Tracking**: The app periodically checks the user's current location and records check-in and check-out times when they enter or leave the office radius.
4. **Working Hours**: Users can view their total working hours for the day, calculated based on check-ins and check-outs.

## Architecture

The project follows the MVVM Architecture principle.

## Dependencies

- Jetpack Compose
- Firebase Authentication
- Firebase Realtime Database
- Google Maps
- Fused Location Provider API
- WorkManager
- Hilt (for Dependency Injection)

## Future Improvements

- Add support for multiple office locations.
- Generate attendance reports for each user.
- Implement an admin panel for managing users and attendance records.
- Add manual check-in/check-out functionality.
- Implement a dark mode.

## Contributing

Contributions are welcome! If you'd like to contribute, please fork the repository and submit a pull request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.



