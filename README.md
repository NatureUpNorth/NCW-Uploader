# North Country Wild Uploader
This is the repository that contains the uploader built in Java used for game camera images taken by North Country Community members. The src folder contains all of the files needed for building each part of the uploader. 

## Outline
- Intro Page: Goes over the uploader, contact information, and contributors.
- Login: Gives the option to sign in with display name and password, sign up, or reset your password.
- Image File Uploader: You pick your organization affiliation and upload your images.
- Deployment Info: Insert your dates and location of the game camera (Latitude and Longitude).
- Habitat: Type of location and if the location was urbanized.
- Comments and Concerns: Add any other information.
- Bottom Line: Upload images or select the help button.

## Uploader Main File
Implements the main class for the NCW uploader providing a user-friendly interface. It uses Java Swing to create the GUI with frames, panels, buttons, and dialog boxes. The class extends JFrame serving as the window for the application and implements ActionListener for button clicks. It uses several panels including:
TitlePanel: The welcome screen with a start button.
LoginPanel: Handles user login with a display name and password.
InputPanel: Collects image-related inputs and provides options like upload and help.
UploadPanel: Manages the actual image upload process.
ThankYouPanel: Displays a thank-you message after successful uploads.
In addition, it uses userAuthentication to verify credentials.
