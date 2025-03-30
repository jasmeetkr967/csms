# Updated Dockerfile with an official OpenJDK-based image

# Use a valid Maven image with OpenJDK 17
FROM maven:3.9.4-eclipse-temurin-17 AS builder
WORKDIR /app

# Copy project files and build
COPY . .
RUN mvn clean package -DskipTests

# Use an official OpenJDK image for runtime
FROM openjdk:17-jdk AS runtime
WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /app/target/*.jar transaction-service.jar

# Expose the application port (adjust as needed)
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "transaction-service.jar"]



#FROM openjdk:17
#WORKDIR /app
#COPY target/transaction-service-1.0.jar transaction-service.jar
#EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "transaction-service.jar"]