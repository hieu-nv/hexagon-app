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
├── api/                   # API Module
│   ├── src/main/kotlin/   # Kotlin source files
│   │   └── com/hieunv/app/
│   │       ├── config/    # Configuration classes
│   │       ├── controller/# REST controllers
│   │       └── dto/       # Data Transfer Objects
│   └── src/main/resources/# Configuration files
├── core/                  # Core Module
│   └── src/main/kotlin/
│       └── com/hieunv/app/core/
│           ├── entity/    # Domain entities
│           ├── repository/# Repository interfaces
│           ├── service/   # Service interfaces
│           └── user/      # User domain feature
├── data/                  # Data Module
│   └── src/main/kotlin/
│       └── com/hieunv/app/data/
│           ├── repository/# Repository implementations
│           └── user/      # User feature repositories
├── gradle/                # Gradle wrapper files
├── app.db                 # SQLite database file
├── build.gradle.kts       # Root build configuration
├── settings.gradle.kts    # Project settings
└── README.md              # This file
```

## Features

- Feature-based organization within modules for better maintainability
- Clear separation of concerns following hexagonal architecture principles
- REST API endpoints for data access
- SQLite database for persistence

## Development Guidelines

### Adding a New Feature

1. Define the domain model and interfaces in the `core` module
2. Implement the persistence layer in the `data` module
3. Create controllers and DTOs in the `api` module

### Package Structure

The codebase follows a feature-based organization within each module to improve maintainability.

## License

[MIT License](LICENSE)
