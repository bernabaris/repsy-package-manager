FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean install -DskipTests
FROM openjdk:21-slim
WORKDIR /app
COPY --from=build /app/target/repsy-app-0.0.1-SNAPSHOT.jar repsy-app-0.0.1-SNAPSHOT.jar

CMD ["java", "-jar", "repsy-app-0.0.1-SNAPSHOT.jar"]

EXPOSE 8084