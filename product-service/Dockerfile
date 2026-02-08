FROM amazoncorretto:17-alpine
WORKDIR /app

# .original olanı değil, gerçek executable JAR'ı kopyalıyoruz
COPY target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]