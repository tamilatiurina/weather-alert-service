FROM eclipse-temurin:21-jdk

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} app.jar

ENV WEATHER_API_KEY=changeme
ENV EMAIL_SENDER=changeme@gmail.com
ENV EMAIL_PASSWORD=changeme_password

ENTRYPOINT ["java", "-jar", "app.jar"]