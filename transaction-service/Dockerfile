FROM openjdk:17
WORKDIR /app
COPY target/transaction-service-1.0.jar transaction-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "transaction-service.jar"]