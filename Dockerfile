FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY teacher-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "app.jar"]