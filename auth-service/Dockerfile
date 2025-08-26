# ---------- Build stage ----------
FROM maven:3.9.8-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
WORKDIR /app/auth-service
RUN mvn clean package -DskipTests

# ---------- Runtime stage ----------
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/auth-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
