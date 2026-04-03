# Runtime Stage (Optimized for ARM64/Mac M3)
FROM amazoncorretto:17-alpine

# Security: Create and use a non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

WORKDIR /app

# Copy the pre-built JAR from GitHub Actions build stage
COPY target/*.jar app.jar

EXPOSE 8080

# JVM Container optimizations for memory and CPU awareness
ENTRYPOINT ["java", \
            "-XX:+UseContainerSupport", \
            "-XX:MaxRAMPercentage=75.0", \
            "-Djava.security.egd=file:/dev/./urandom", \
            "-jar", "app.jar"]