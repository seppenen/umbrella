# 'Hello world' microservices

This project uses Docker compose, Jakarta EE, Spring Data JPA, Spring MVC, Lombok, and Java
SDK version 17 along with PostgreSQL database. This application focuses on creating endpoints for user registration.

## Usage

### Swagger UI

Access via `http://localhost:8080/swagger-ui/index.html`.

### Endpoint 1 ([POST] /register)

The endpoint accepts user registration data as the request body, transformed into a `UserRequestDto` object by Spring
MVC. It uses 'UserService' to register the user and returns a 'UserResponseDto' in a 'ResponseEntity'.

#### Example Request Body

`{... user details according to UserRequestDto}`

#### Example Response

`{... user details according to UserResponseDto}` with HTTP status code indicating the success status of the operation.

## Tasks

A set of tasks used in the build process of this Java-based microservices project, managed using docker-compose.

1. **clean-docker:** Stops and removes all Docker containers and images.
2. **empty-database:** Removes all data, deletes the postgres Docker container and creates a new postgres-db-service
   Docker container.
3. **create-database:** Creates a new postgres-db-service Docker container.
4. **up:** Builds the entire project.

## Set-Up

To get started with this project:

1. Make sure you have Java SDK 17 installed.
2. Import the project as a Maven/Gradle project into your preferred IDE.
3. Ensure the Lombok plugin is installed in your IDE.
4. Resolve all the dependencies.
5. Create a `.env` file in the root directory and add the environment variables:

   #### Global Environment Variables
    - ENVIRONMENT: dev (or prod)

   #### PostgreSQL Environment Variables
    - DB_IMAGE_VERSION: postgres:16.1
    - DB_PORT: 5432
    - DB_USER: [your-username]
    - DB_PASSWORD: [your-password]

6. Run `task up` to build the project.
7. Uncomment port 5005 in `docker-compose.yml` to enable remote debugging.
