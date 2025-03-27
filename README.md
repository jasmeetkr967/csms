Overview

The CSMS (Charging Station Management System) Authorization & Transaction System is a microservices-based application built using Spring Boot. It manages authorization requests from electric vehicle drivers, processes them asynchronously using Apache Kafka, and provides a response based on predefined authorization rules.

Details:
It consists of 2 independent microservices: transaction-service, auth-service, and a simple Java project, docker-setup, to maintain an independent structure.
If needed, we can move them to separate repos, I am using the same repo for the ease of sharing.

Features

Authorization Service: Determines driver authorization status.

Transaction Service: Handles authorization requests and integrates with Kafka for asynchronous processing.

Kafka Integration: Used for decoupled and scalable event-driven communication.

Unit & Integration Tests: Implemented using JUnit, Mockito, and Spring Boot Test.

Docker Support: Simplifies deployment and execution.

Maven: To run and build the project

Setup Guide

Prerequisites

Ensure you have the following installed:

Java 17 or later

Apache Kafka (running locally or in Docker)

Docker (optional for running the services in containers)

Maven (for dependency management and building the project)

Lombok

Eclipse or any other IDE

Postman

Steps to Run the Services

1. Clone the Repository
2. You might get issues due to Lombok library, if it is not configured on your local machine.Refer the following link to resolve that
   https://www.baeldung.com/lombok-ide
3. Run mvn clean package on projects individually.
4. Go to the docker-setup project in the terminal and run docker compose up --build


API Endpoints

Authorization Service

POST /auth/validate - Validate driver authorization

Testing Steps:

After the application is up.
You can access the h2- database using the following link
http://localhost:8081/h2-console/
Enter the jdbc url as dbc:h2:mem:testdb
and username:sa 
and keep password empty and login.

Run the following query

INSERT INTO DRIVER_AUTHORIZATIONS  (IDENTIFIER , ALLOWED) 
VALUES 
('valid-id-12345678901234567890', true),
('rejected-id-12345678901234567890', false),
('unknown-id-12345678901234567890', false);

After this is done, you can test the application using Postman
URL: http://localhost:8080/transaction/authorize (POST)
request body raw type json
{
  "stationUuid": "25aac66b-6051-478a-95e2-6d3aa343b025",
  "driverIdentifier": { "id": "valid-id-12345678901234567890" }
}
and run

It should give valid response







   



