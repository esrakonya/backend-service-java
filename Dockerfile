# Step 1: Build the application using Maven and Amazon Corretto 17
FROM maven:3.9.6-amazoncorretto-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Build the project and skip tests for speed during dockerization
RUN mvn clean package -DskipTests

# Step 2: Run the application using Amazon Corretto 17 (Headless JRE)
# This is highly optimized for ARM64 (Mac M3)
FROM amazoncorretto:17-alpine
WORKDIR /app
# Copy the generated JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]