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
COPY --from=builder /app/target/*.jar auth-service.jar

# Expose the application port (adjust as needed)
EXPOSE 8081

# Command to run the application
ENTRYPOINT ["java", "-jar", "auth-service.jar"]




#FROM openjdk:17
#WORKDIR /app
#COPY target/auth-service-1.0.jar auth-service.jar
#EXPOSE 8081
#ENTRYPOINT ["java", "-jar", "auth-service.jar","-web -webAllowOthers -tcp -tcpAllowOthers -browser"]
# Start H2 in TCP mode
#CMD java -cp auth-service.jar org.h2.tools.Server -tcp -tcpAllowOthers -ifNotExists & \
#    java -jar auth-service.jar

