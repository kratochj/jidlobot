FROM maven:3.9.4-amazoncorretto-21 AS build

LABEL org.opencontainers.image.description="This project is a Slack bot built with Java using Spring Boot and Bolt for Java SDK. The bot connects to Slack via Socket Mode, allowing it to run behind a firewall without exposing a public URL. It listens for mentions and responds with daily menu information or help messages. The bot also includes a health check endpoint implemented with Spring Boot Actuator."

# Set the working directory
WORKDIR /app

# Copy the pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the source code
COPY src ./src

# Package the application
RUN mvn package -DskipTests

# Stage 2: Run the Spring Boot app
FROM openjdk:21

# Set the working directory
WORKDIR /app

# Copy the jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
