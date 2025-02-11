FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app

COPY --from=build /app/target/*.jar /app/cinema-api.jar

EXPOSE 8080
CMD ["java", "-Dfile.encoding=UTF-8", "-jar", "/app/cinema-api.jar"]
