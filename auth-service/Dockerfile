# Use a lightweight JRE image for the runtime
FROM amazoncorretto:17-alpine

# Create a non-privileged user to run the application
# This follows the 'Principle of Least Privilege' (PoLP)
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring


WORKDIR /app

# Copy the pre-built JAR from the module's target directory
# (Built by GitHub Actions/Maven before docker build)
COPY target/*.jar app.jar

# Standard port (Internal to the container)
EXPOSE 8080

ENTRYPOINT ["java", \
            "-XX:+UseContainerSupport", \
            "-XX:MaxRAMPercentage=75.0", \
            "-Djava.security.egd=file:/dev/./urandom", \
            "-jar", "app.jar"]