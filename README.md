# Project Overview

This project is a Spring Boot application that implements a web-based chess game. It appears to be the "complete" version of a tutorial or example project, demonstrating how to handle form submissions and build a functional chess application.

## Features

Key features of this application include:

*   Starting a new chess game.
*   Making moves during the game.
*   Handling pawn promotion.
*   API documentation available via Swagger.

## Technologies Used

This project utilizes the following technologies:

*   Java
*   Spring Boot
*   Maven (for build and dependency management)
*   Thymeleaf (for server-side templating)
*   Spring Web (for building web applications)
*   Spring Data MongoDB (for database interaction)
*   H2 Database (as an in-memory database)
*   Swagger (for API documentation)

## Prerequisites

To build and run this project locally, you will need:

*   JDK 1.8 or newer.
*   Apache Maven.

## How to Build and Run

1.  Navigate to the `complete/` directory of the project:
    ```bash
    cd complete
    ```
2.  Run the application using the Maven wrapper:
    *   On macOS/Linux:
        ```bash
        ./mvnw spring-boot:run
        ```
    *   On Windows:
        ```bash
        mvnw.cmd spring-boot:run
        ```
3.  Once the application starts, it will be accessible at [http://localhost:8080](http://localhost:8080).

## How to Play

1.  Open your web browser and go to [http://localhost:8080](http://localhost:8080).
2.  You should see an option to start a new game. You may be asked for your name and desired difficulty.
3.  Follow the on-screen instructions to make moves. This typically involves selecting a piece and its destination square, or inputting moves in a text format (e.g., "e2e4").
4.  If a pawn reaches the opposite end of the board, you will be prompted for pawn promotion.

## API Documentation

This project uses Swagger to provide API documentation. Once the application is running, you can access the Swagger UI at:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

This interface allows you to explore the available API endpoints and test them.

## Contributing

Contributions are welcome! Please see the [CONTRIBUTING.adoc](CONTRIBUTING.adoc) file for guidelines, including the requirement to sign the Contributor License Agreement.
