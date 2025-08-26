FROM maven:3.9.8-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
WORKDIR /app/notification-service
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/notification-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
