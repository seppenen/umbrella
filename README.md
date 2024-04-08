# 'Hello world' Microservices Project

This demo application utilize Docker Compose, Java 17, Spring Boot 3.2, Spring Security 6, Spring Cloud, Spring Data, Spring MVC, Spring WebFlux and PostgreSQL database. It primarily focuses on managing entrepreneurial billing, invoicing, and reporting.



## Usage Guide
### Swagger UI
This can be accessed via `http://localhost:8080/swagger-ui/index.html`.

### Eureka
This can be accessed via `http://localhost:8761

## Build Tasks
A sequence of tasks is utilized for building this Java-based microservices project. The tasks are managed using `Taskfile`, with each task being prefixed by `task`, such as `task build` or `task deploy`. `Docker-compose` is used in some of these tasks to orchestrate the services.

1. **clean-docker:** Stops and deletes all Docker containers and images.
2. **empty-database:** Deletes all data in postgres Docker container and creates a new postgres-db-service Docker container.
3. **create-database:** Generates a new postgres-db-service Docker container.
4. **redeploy SERVICE={service-name}:** Redeploy a specific service.
5. **up:** Builds the entire project structure.

## How to Initiate the Project
Follow these steps to use this project:

1. Java SDK 17 has to be installed.
2. Import the project as a Maven/Gradle project in your preferred IDE.
3. Lombok plugin has to be installed in your IDE.
4. Resolve all the dependencies.
5. Create a `.env` file in the root directory and add the following environment variables:

   #### Given global environment variables:
   - ENVIRONMENT: dev (or prod)

   #### PostgreSQL environment variables:
   - DB_IMAGE_VERSION: postgres:16.1
   - DB_PORT: 5432
   - DB_USER: [your-username]
   - DB_PASSWORD: [your-password]
6. Build the project by running `task up`.
7. Uncomment port 5005 in `docker-compose.yml` to enable remote debugging.
