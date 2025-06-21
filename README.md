# Hexagonal Architecture App

A modern backend application built using Hexagonal Architecture (also known as Ports and Adapters) with Spring Boot and Kotlin.

## Project Overview

This project demonstrates the implementation of a hexagonal architecture pattern in a Spring Boot application. The architecture separates the core business logic from external concerns, making the system more maintainable, testable, and adaptable to change.

## Architecture

The application follows a modular structure with three main components:

### 1. API Module (`api`)
- Contains the REST controllers and web-related configurations
- Acts as the entry point for HTTP requests
- Depends on the core and data modules
- Implements the primary/driving adapters

### 2. Core Module (`core`)
- Contains the domain models, business logic, and service interfaces
- Defines the application's use cases and business rules
- Has no dependencies on frameworks or external systems
- Defines ports (interfaces) that other modules implement

### 3. Data Module (`data`)
- Contains the data access implementations
- Implements the secondary/driven adapters for persistence
- Provides concrete implementations of the repository interfaces defined in the core module

## Technology Stack

- **Language**: Kotlin 1.9.25
- **Framework**: Spring Boot 3.5.0
- **Build Tool**: Gradle with Kotlin DSL
- **Java Version**: 21
- **Database**: SQLite
- **Persistence**: Spring Data JPA
- **API Documentation**: SpringDoc OpenAPI

## Getting Started

### Prerequisites

- JDK 21 or later
- Gradle 8.x or later (or use the included Gradle wrapper)

### Building the Application

```bash
./gradlew build
```

### Running the Application

```bash
./gradlew bootRun
```

The application will start on http://localhost:8080 by default.

## Project Structure

```
hexagon-app/
├── api/                                # API Module
│   ├── src/
│   │   ├── main/
│   │   │   ├── kotlin/
│   │   │   │   └── com/hieunv/app/
│   │   │   │       ├── App.kt         # Main application class
│   │   │   │       └── user/          # User feature REST controllers
│   │   │   └── resources/
│   │   │       ├── application.yml    # Application configuration
│   │   │       ├── data.sql           # Sample data
│   │   │       └── schema.sql         # Database schema
│   │   └── test/                      # API tests
│   └── build.gradle.kts               # API module build configuration
├── core/                              # Core Module
│   ├── src/
│   │   ├── main/
│   │   │   ├── kotlin/
│   │   │   │   └── com/hieunv/app/core/
│   │   │   │       ├── entity/        # Domain entities
│   │   │   │       └── user/          # User domain model and interfaces
│   │   │   └── resources/
│   │   └── test/                      # Core tests
│   └── build.gradle.kts               # Core module build configuration
├── data/                              # Data Module
│   ├── src/
│   │   ├── main/
│   │   │   ├── kotlin/
│   │   │   │   └── com/hieunv/app/data/
│   │   │   │       └── user/          # User repository implementations
│   │   │   └── resources/
│   │   └── test/                      # Data tests
│   └── build.gradle.kts               # Data module build configuration
├── gradle/                            # Gradle wrapper files
├── app.db                             # SQLite database file
├── build.gradle.kts                   # Root build configuration
├── settings.gradle.kts                # Project settings
└── README.md                          # This file
```

## Features

- Feature-based organization within modules for better maintainability
- Clear separation of concerns following hexagonal architecture principles
- REST API endpoints for data access
- SQLite database for persistence

## Development Guidelines

### Adding a New Feature

When adding a new feature to the application, follow these steps to maintain the hexagonal architecture:

1. **Define the domain model in the core module**
   - Create entity classes in `core/src/main/kotlin/com/hieunv/app/core/{feature}`
   - Define repository interfaces with required methods

2. **Implement the business logic in the core module**
   - Create service interfaces that define the use cases
   - Implement service classes that contain the business logic

3. **Implement the repository in the data module**
   - Create repository implementations in `data/src/main/kotlin/com/hieunv/app/data/{feature}`
   - Ensure they implement the interfaces defined in the core module

4. **Create the API endpoints in the api module**
   - Define DTOs for request/response objects
   - Create controllers that use the service interfaces
   - Map between domain entities and DTOs

### Coding Standards

- Follow Kotlin coding conventions
- Use meaningful names for classes, methods, and variables
- Write unit tests for all business logic
- Use dependency injection for loose coupling
- Document public APIs with KDoc comments

## Testing

The project includes several types of tests:

### Unit Tests
- Located in the `src/test` directory of each module
- Focus on testing individual components in isolation
- Use JUnit 5 and MockK for mocking

### Integration Tests
- Located in the `api` module's `src/test` directory
- Test the interaction between components
- Use Spring Boot Test for integration testing

### Running Tests

```bash
# Run all tests
./gradlew test

# Run tests for a specific module
./gradlew :core:test
./gradlew :data:test
./gradlew :api:test
```

## API Documentation

The API documentation is automatically generated using SpringDoc OpenAPI and can be accessed at:

```
http://localhost:8080/swagger-ui.html
```

This provides an interactive UI to explore and test the available endpoints.

## Database

The application uses SQLite for simplicity. The database file is located at `app.db` in the root directory.

### Database Schema

The database schema is automatically created on application startup using the `schema.sql` file in the `api/src/main/resources` directory.

### Sample Data

Sample data is loaded from the `data.sql` file in the `api/src/main/resources` directory.

## Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Make your changes
4. Commit your changes (`git commit -m 'Add some amazing feature'`)
5. Push to the branch (`git push origin feature/amazing-feature`)
6. Open a Pull Request

### Commit Guidelines

- Use descriptive commit messages that explain the changes made
- Reference issue numbers in commit messages when applicable
- Keep commits focused on a single change

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Kotlin](https://kotlinlang.org/)
- [Hexagonal Architecture](https://alistair.cockburn.us/hexagonal-architecture/)

---

Last updated: June 21, 2025
