FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app
COPY . .

RUN ./gradlew build --no-daemon

EXPOSE 9090

CMD ["java", "-jar", "build/libs/backend-0.0.1-SNAPSHOT.jar"]