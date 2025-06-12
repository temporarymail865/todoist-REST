# Todoist REST API

This project is a Spring Boot application that exposes a REST interface for managing projects, tasks, labels and other entities similar to the Todoist service.

## Prerequisites

- Java 8 or newer must be available on the PATH.
- A PostgreSQL server running locally. The default configuration expects a database reachable at `jdbc:postgresql://localhost:5432/postgres` with username `postgres` and password `nitish` as defined in `src/main/resources/application.properties`:

```
server.port=8080
spring.datasource.driverClassName=org.postgresql.Driver
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=nitish
spring.jpa.hibernate.ddl-auto=update
```

Adjust these values if your database configuration differs.

## Building the project

Use the Maven wrapper included in the repository to compile the application. From the project root run:

```bash
./mvnw clean package
```

This will build the project and create `target/todoist-0.0.1-SNAPSHOT.jar`.

## Running the application

During development you can start the application directly using Spring Boot:

```bash
./mvnw spring-boot:run
```

After a successful build you can also run the generated jar:

```bash
java -jar target/todoist-0.0.1-SNAPSHOT.jar
```

The application listens on port `8080` by default.
