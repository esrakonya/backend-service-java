# Use a lightweight JRE image for the runtime
FROM amazoncorretto:17-alpine
WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

WORKDIR /app

# Expect the JAR to be already built by Maven
# This copies the JAR from the module's target folder into the container
COPY target/*.jar app.jar

# Standard port (Internal to the container)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", \
            "-XX:+UseContainerSupport", \
            "-XX:MaxRAMPercentage=75.0", \
            "-Djava.security.egd=file:/dev/./urandom", \
            "-jar", "app.jar"]