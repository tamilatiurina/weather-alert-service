# Weather Alert Service

A Spring Boot application that provides weather alerts based on subscriptions. The project is containerized with Docker and uses an H2 database for persistence.

## Prerequisites

Before you start, ensure you have the following installed:

- **Java 21** (for building and running the application)
- **Docker** (to build and run the app in containers)
- **Git** (to clone the repository)
- **Gradle** (to build the application locally)

## Steps to Launch the Project

### 1. Clone the Repository

Clone the repository to your local machine:

```bash
git clone https://github.com/tamilatiurina/weather-alert-service.git
cd weather-alert-service
```

### 2. Create Environment Variables File

Create a .env file in the root of your project with these variables:

```bash
EMAIL_SENDER=your-email@example.com
EMAIL_PASSWORD=your-email-password
WEATHER_API_KEY=your-weather-api-key
```
Replace the placeholders with completing the next steps.

### 3. Create free Weather API

Go to https://www.weatherapi.com/ and register.

On the Dashboard/API look for the API key: ***********************

Copy this key and paste it in the place of ```your-weather-api-key``` in the ```WEATHER_API_KEY=your-weather-api-key``` in the  ```.env``` file.

### 4. Create gmail address

Create new gmail address or take the existed one.

Paste this address in the place of ```your-email@example.com``` in the ```EMAIL_SENDER=your-email@example.com``` in the  ```.env``` file.

### 5. Create App Password

Go to Google Account Security of the previously discussed email.

Under "Signing in to Google", click 2-Step Verification.

Follow the on-screen instructions to set up 2-Step Verification.

Open a web browser and go to https://myaccount.google.com.

In the left sidebar, click on Security.

Scroll down to the "Signing in to Google" section.

Under App passwords, click Generate app password. (You may need to sign in again for security purposes.)

In the drop-down menu, select the app and device for which you need the app password (Mail, Windows Computer).

After selecting the app and device, click Generate.

A 16-character code will be displayed on the screen. This is your App Password.

Copy this code and paste it in the place of ```your-email-password``` in the ```EMAIL_PASSWORD=your-email-password``` in the  ```.env``` file.

### 6. Set Up Docker 

Docker will handle setting up the environment and dependencies.

#### a. Build the Docker Image
To build the Docker image, run the following command:

```bash
docker-compose build --no-cache
```
#### b. Start the Application
After the image is built, start the application by running:

```bash
docker-compose up
```
This will start the application in a Docker container. By default, the service will be available on http://localhost:8080.

#### c. View Logs
To check the logs of the running container:

```bash
docker-compose logs -f
```

### 7. If Running Without Docker (Optional)

If you don't want to use Docker and prefer to run the application locally:

Build the project with Gradle:

```bash
gradle build
```

Run the project:

```bash
gradle bootRun
```
This will start the Spring Boot application, and you can access it on http://localhost:8080

### 8. Accessing the Application
Once the application is up, you can access it as follows:

- #### Weather Data API:
    ```GET http://localhost:8080/weather?city={city_name}```

    Example: ```GET http://localhost:8080/weather?city=Warsaw```

- #### Subscriptions API:
    ```POST http://localhost:8080/subscriptions```

- #### H2 Database Console (for development):
    The H2 database console is available at: http://localhost:8080/h2-console

    Use the default connection settings (password leave empty): 
    ```bash
    JDBC URL: jdbc:h2:file:/WeatherAlertService/db/subscriptions
    username: sa
    password:
    ```
### 9. Stopping the Application
To stop the application running in Docker press ```Ctrl+C```:

```bash
docker-compose down
```
### 10. Troubleshooting
- 403 Forbidden error:

    If you're getting a 403 Forbidden error, make sure the paths you want to access (such as /weather/** and /static/**) are properly configured in SecurityConfig.java.

- Missing CSS or other resources:

    Ensure that your static resources (like styles.css) are located in src/main/resources/static/ in your project.

### Configuration
- #### Database:

    The application uses an H2 database, which is created at runtime. If you'd like to change the database, modify the application.yml configuration.

- #### Cron Jobs:

    The application runs scheduled tasks (for example, to send weather alerts) defined using Spring's @Scheduled annotations. You can modify these in the relevant service classes.
