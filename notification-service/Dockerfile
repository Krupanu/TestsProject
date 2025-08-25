FROM eclipse-temurin:17-jdk as build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN ./mvnw -q -v || true
RUN apt-get update && apt-get install -y maven
RUN mvn -q -DskipTests package

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","/app/app.jar","--spring.profiles.active=docker"]
