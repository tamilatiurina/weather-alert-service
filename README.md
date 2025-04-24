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

## Architecture

This project follows a modular, layered architecture. Below is a breakdown of the key architectural components:

### 1. Spring Boot Framework
The project is built on Spring Boot, providing the environment for creating web applications and RESTful APIs.

### 2. Layers in the Application
The project follows the Layered Architecture pattern with the following layers:

#### a. Controller Layer
Responsibility: This layer handles incoming HTTP requests and routes them to the appropriate service. It's where the API endpoints are defined.

Technology: Spring MVC is used to expose REST API endpoints. For example, endpoints like /subscriptions handle user subscription requests.

#### b. Service Layer
Responsibility: The Service Layer contains the core business logic of the application and directly interacts with the database using EntityManager instead of using a separate Repository Layer. This layer handles CRUD operations and additional business logic, such as subscribing a user, updating a subscription, and notifying users about weather updates.

Technology:

- EntityManager is used for database interaction, which is part of JPA (Java Persistence API).

- Transactional Management: The @Transactional annotation ensures that database operations are executed within a transaction, ensuring consistency.

- Service Methods: Methods like subscribe(), addSubscription(), and updateLastNotified() interact with the database and business logic.

#### c. Model Layer
Responsibility: The Model or DTO Layer defines the shape of the data that is transferred between the different layers of the application. 
Technology:

DTOs are plain Java objects with fields corresponding to the data you want to transfer.

Model classes ```NotificationDTO``` and ```SubcriptionDTO``` also correspond to JPA Entities for database interaction, though DTOs are often decoupled from database entities to avoid exposing internal database structure.

#### d. Configeration Layer
Responsibility: The Configuration Layer is used to manage periodic tasks like sending email and for API access control

Technology:

Spring's @Configuration classes to define beans, schedules.

### 3. External Integration
#### a. Weather API
Responsibility: The project integrates with an external Weather API to fetch current weather information for a subscribed city.

Technology: The weather data is fetched in JSON format from an external weather service.

#### b. Email Notification
Responsibility: The project sends email notifications to users with weather notification for they had subscribed.

Technology: Spring Boot's Spring Mail module is used to send emails to users using SMTP. The email content is dynamically generated using Thymeleaf templates.

### 4. Scheduled Tasks
Responsibility: The application periodically checks the weather for subscribed users and sends notifications based on a predefined schedule (every day at 7.00 AM).

Technology: Spring Scheduler is used to schedule and automate the weather checking tasks.

### 5. Database
Responsibility: The H2 database is used to persist user subscriptions and notification history.

Technology: The project uses Spring Data JPA for persistence with an in-memory H2 database for simplicity.

## Justification of Architectural Decisions

1. **Spring Boot Framework**: Selected for rapid development and built-in features for REST API creation, scheduling, and email handling.

2. **H2 Database**: Used for simplicity and fast prototyping, with the option to migrate to a more robust database in the future.

3. **Email Notifications**: Email is a reliable, non-intrusive method for weather alerts, and Spring Mail simplifies the integration.

4. **Spring Scheduler**: Automates daily weather checks, making the system reliable and easy to manage with minimal configuration.

5. **RESTful API**: Provides flexibility, scalability, and simple integration with other systems.

6. **DTOs**: Decouples internal data from API exposure, improving maintainability and security.

7. **External Weather API**: Provides real-time, accurate weather data, ensuring scalability and reliability.

8. **Basic Authentication**: Provides initial security with room for expansion to more advanced security mechanisms as the system grows.

## Workflow logic

#### 1. Weather Data Retrieval

  **External Weather API Integration**:
    The service communicates with an external weather data source to retrieve real-time weather conditions for cities. For this project, weather data includes parameters such as temperature, humidity and sky conditions.

  **API Endpoint**:
    A RESTful API endpoint ```GET /weather?city={city_name}``` is exposed for clients to request current weather conditions for a specified city. The city name is passed as a query parameter in the request.

Example:

```bash
GET /weather?city=Kyiv
```
The system returns the current weather data for the requested city. If the city is not found or if there is an issue with the data source, the error page with error message and button "Go Back" is returned.

#### 2. Subscription Management

   **Subscription Endpoint**:
      The service allows users to subscribe for weather notifications through the POST /subscriptions endpoint. Subscriptions include:

- *User Email*: The contact information of the user who wants to receive notifications.

- *City*: The city for which weather conditions will be checked.

- *Conditions*: Specific weather conditions ("temperature" stands for temperature below 0°C or "rain" stands for it's raining) that, when met, will trigger the notification.

Example:

```
{
"email": "user@example.com",
"city": "Kyiv",
"conditions": "temperature"
}
```
Validation:

The system ensures that the city exists and that the email is valid and that all three attributes are specified.

#### 3. Weather Condition Monitoring and Notification
**Scheduled Monitoring**:

Every day at 7.00 AM the service performs an automatic check of the weather conditions for all cities with active subscriptions. This is achieved using Spring’s @Scheduled annotation, which triggers the condition checks via a cron job. 

**Conditions Matching**:
During each scheduled check, the service compares the current weather data for each city with the conditions defined in the subscription. If the conditions are met, a notification is triggered. If the condition is "temperature" and the weather report indicates temperature below 0, or if the condition is "rain" and the weather report indicates sky description which contains a word "rain",  a notification is generated.

**Notification Delivery**:
Notifications are sent to users who have subscribed to relevant conditions. The notification method in this case is via email.

#### ! THE NOTIFICATIONS IN EMAILS APPEAR IN SPAM FOLDER !

## Error Handling

The system gracefully handles errors such as:
- Invalid city name: Returns the error page with an error message and button "Go back" to return to the weather page and input valid city.
- External Weather API failure: Returns the error page which was described above.
- Email failure: Logs the issue and notifies the admin for manual intervention if the email could not be sent.

Error responses are accompanied by appropriate HTTP status codes (e.g., `400 Bad Request`, `500 Internal Server Error`).

## Testing Strategy

The project includes unit tests for the core services such as:
- Subscription creation and validation
- Weather condition checking logic
- Email notification delivery

Mockito is used to mock external dependencies in unit tests.


## Project Dependencies
- *Spring Boot Starter Mail*: For sending email notifications to users.

- *Spring Boot Starter Data JPA*: For interaction with the H2 database.

- *Spring Boot Starter Thymeleaf*: For rendering HTML templates.

- *Spring Boot Starter Web*: For building REST APIs to interact with the weather subscription service.

- *Spring Boot Starter Security*: For securing the API endpoints and basic authorization.

- *Spring Boot Starter Test*: For testing the application.

- *H2 Database*: An in-memory database used for storing user subscriptions.

- *Mockito*: For mocking dependencies in unit tests.

- *JSON Library*: org.json:json:20210307 for parsing JSON responses from external weather APIs.

