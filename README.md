# 'Hello world' Microservices Project

This application employs Docker Compose, Jakarta EE, Spring Data JPA, Spring MVC, Lombok, Java SDK version 17, and PostgreSQL database. It primarily focuses on managing entrepreneurial data and user registration.

## Usage Guide
### Swagger UI
This can be accessed via `http://localhost:8080/swagger-ui/index.html`.

### User Registration Endpoint ([POST] /register)
This endpoint incorporates the user's registration data, which is passed through the request body and subsequently transformed into a `UserRequestDto` object by Spring MVC. User registration is then performed with the help of 'UserService' and a 'UserResponseDto' is generated, which returns using 'ResponseEntity'.

#### Sample Request Body
`{...UserRequestDto's corresponding details}`

#### Sample Response
`{...UserResponseDto's corresponding details}`; the HTTP status code indicates whether the registration process was successful.

### Endpoint to Fetch All Users ([GET] /api/v1/getAllUsers)
This endpoint fetches a list of all users from the UserService.

### Endpoint to Delete Entrepreneur ([DELETE] /api/v1/entrepreneurs/{id})
This endpoint deletes an entrepreneur using their ID.

### Endpoint to Fetch an Entrepreneur ([GET] /api/v1/entrepreneurs/{id})
This endpoint fetches details of a specific entrepreneur using their ID.

### Endpoint to Fetch All Entrepreneurs ([GET] /api/v1/entrepreneurs)
This endpoint fetches details of all entrepreneurs.

### Endpoint to Create New Entrepreneur ([POST] /api/v1/entrepreneurs)
This endpoint creates a new entrepreneur.

### Endpoint to Check Application Health ([GET] /api/v1/health)
This endpoint checks and returns the current health status of the application.

## Build Tasks
A sequence of tasks is utilized for building this Java-based microservices project. The tasks are managed using docker-compose:

1. **clean-docker:** Stops and deletes all Docker containers and images.
2. **empty-database:** Deletes all data in postgres Docker container and creates a new postgres-db-service Docker container.
3. **create-database:** Generates a new postgres-db-service Docker container.
4. **up:** Builds the entire project structure.

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
