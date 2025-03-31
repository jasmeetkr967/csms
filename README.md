Overview

The CSMS (Charging Station Management System) Authorization & Transaction System is a microservices-based application built using Spring Boot. It manages authorization requests from electric vehicle drivers, processes them asynchronously using Apache Kafka, and provides a response based on predefined authorization rules.

Details:
It consists of 2 independent microservices: transaction-service, auth-service, and a simple Java project, docker-setup, to maintain an independent structure.
If needed, we can move them to separate repos, I am using the same repo for easy sharing.

Features

1. Authorization Service: Determines driver authorization status.
2. Transaction Service: Handles authorization requests and integrates with Kafka for asynchronous processing.
3. Kafka Integration: Used for decoupled and scalable event-driven communication.
4. Unit & Integration Tests: Implemented using JUnit, Mockito, and Spring Boot Test.
5. Docker Support: Simplifies deployment and execution.
6. Maven: To run and build the project

Setup Guide

Prerequisites

Ensure you have the following installed:

1. Java 17 or later
2. Apache Kafka (running locally or in Docker)
3. Docker
4. Maven (for dependency management and building the project)
5. Lombok
6. Eclipse or any other IDE
7. Postman

Steps to Run the Services

1. Clone the Repository
2. If Lombok is not configured on your local machine, you will get issues due to the Lombok library. Refer to the following link to resolve 
   that
   https://www.baeldung.com/lombok-ide
3. Go to the docker-setup project in the terminal and run docker compose up --build

API Endpoints

Authorization Service
POST /auth/validate - Validate driver authorization


Testing:
1. Run mvn test to run test cases of individual  services.
2. H2 console db configuration to check data
   You can access the h2- database using the following link
   http://localhost:8081/h2-console/
   Enter the jdbc url as dbc:h2:mem:testdb
   username:sa , password: 
   and login.
 To insert more data, you can manually run insert queries or configure in DataLoader class
   
3. Testing in Postman 
 URL: http://localhost:8080/transaction/authorize (POST)
 request body raw type json
 {
  "stationUuid": "25aac66b-6051-478a-95e2-6d3aa343b025",
  "driverIdentifier": { "id": "valid-id-12345678901234567890" }
 }
  and run
4. CURL command testing. Run the following curl command to test the application
   curl --location 'http://localhost:8080/transaction/authorize' \
--header 'Content-Type: application/json' \
--data '{
  "stationUuid": "25aac66b-6051-478a-95e2-6d3aa343b025",
  "driverIdentifier": { "id": "valid-id-12345678901234567890" }
}'      
   
Improvement Scope

1. Security Enhancements
   Implement JWT-based authentication for API access.
   Introduce role-based access control (RBAC) to restrict actions.
   Validate Station Ids
2. Testing Enhancements
   Increase Test Cases coverage by adding consumer and producer classes test cases and Integration Testing.
   We can refer to the following link for Testing Kafka and Spring Boot
   https://www.baeldung.com/spring-boot-kafka-testing

4. Scalability & Performance Improvements
   Kafka Consumer Optimization: Implement batching and consumer parallelization.
   Database Integration with a better database and structure 
   Load Testing: Perform stress tests to analyze system behavior.

5. Additional Features
   API Gateway: Integrating API gateway for Load balancing and URL redirection
   Open AI and Swagger Implementation: To get a UI view of the API and the structure
   Audit Logging: Maintain logs for all authorization requests.
   Retry Mechanism: Implement a retry mechanism for Kafka messages.
   Dashboard Integration: Develop a UI for monitoring authorization requests using tools like Prometheus and Grafana

6. Deployment Enhancements
   Kubernetes Support: Deploy microservices on a Kubernetes cluster.
   CI/CD Pipeline: Automate builds and deployments using GitHub Actions.

Running Multiple Instances of the APIs:

In the current example, since auth-service and transaction-service both act as producers as well as consumers, so will have to consider partitioning while increasing the number of partitions.

Since, we are already using same group-id we won't have need any changes producer perspective as all the producers will publish message to the same group. For consumers, we will have to keep in mind the following rule:

The number of topic partitions is the upper limit for the parallelism of your Kafka Streams application and the number of running instances of your application.

Since, we are using Driver's Identity Id as key. The Key ensures that a particular driver's message goes to the same partition.
Key ensures message ordering and plays a key-role in parallelism.

If we are increasing the number of instances we can increase the concurrency level for fine tuning as follows:

We can increase the number of partitions dynamically as follows:
kafka-topics --bootstrap-server localhost:9092 --alter --topic authorization-requests --partitions 4

We can also configure concurrency level in properties as follows:

spring:
  kafka:
    consumer:
      group-id: authorization-group
      auto-offset-reset: earliest
    listener:
      concurrency: n  # Number of concurrent consumers (should match partitions)

The auto-offset-reset is set to earliest to ensure no messages are lost if a new consumer joins.












   



