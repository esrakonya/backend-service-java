# Use a lightweight JRE image for the runtime
FROM amazoncorretto:17-alpine
WORKDIR /app

# Senior Tip: We expect the JAR to be already built by Maven
# This copies the JAR from the module's target folder into the container
COPY target/*.jar app.jar

# Standard port (Internal to the container)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]