FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY build.gradle settings.gradle ./

RUN ./gradlew build --no-daemon

EXPOSE 9090

CMD ["java", "-jar", "build/libs/backend-0.0.1-SNAPSHOT.jar"]