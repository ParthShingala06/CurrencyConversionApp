# Use an official OpenJDK runtime as a parent image
FROM openjdk:18-jdk-slim

MAINTAINER https://parthshingala06.github.io/

# Set the working directory inside the container
WORKDIR /app

# Copy the application's JAR file to the container
COPY target/Rate-Edge-0.0.1.jar app.jar

# Expose the port thata the application will run on
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
