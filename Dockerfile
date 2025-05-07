# Use an official Maven image to build the app
FROM maven:3.8.6-openjdk-11 as build

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml and install dependencies
COPY pom.xml .

# Download all dependencies (for faster builds)
RUN mvn dependency:go-offline

# Copy the rest of the application code
COPY src /app

# Build the application
RUN mvn clean package -DskipTests

# Use an official OpenJDK runtime as the base image for running the app
FROM openjdk:11-jre-slim

# Set the working directory in the container
WORKDIR /app

# Copy the jar file from the build stage
COPY --from=build /app/target/*.jar /app/petclinic.jar

# Expose the port that the app will run on
EXPOSE 8080

# Command to run the Spring Boot application
CMD ["java", "-jar", "petclinic.jar"]
