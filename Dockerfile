FROM ubuntu:latest
LABEL authors="vladimirocheret"

# Step 1: Build the JAR using Maven in a temporary container
# Step 1: Build the JAR using Maven 3.9.4 and Java 17 in a temporary container
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app

# Copy the pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the rest of the project files and build the JAR
COPY src ./src
# Ensure to specify the 'package' goal to build the JAR
RUN mvn clean package -DskipTests

# Step 2: Use a smaller OpenJDK 17 image to run the Spring Boot application
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/SparkUp-0.0.1-SNAPSHOT.jar /app/SparkUp-0.0.1-SNAPSHOT.jar

# Expose port 8080
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "SparkUp-0.0.1-SNAPSHOT.jar"]