# Dependency Inversion Principle: The Foundation of Sustainable Architecture

*Reading time: approximately 10 minutes*

## Introduction

In the field of software development, creating sustainable and maintainable code is always a challenge. The Dependency Inversion Principle (DIP) - one of the five SOLID principles - provides a solid foundation to address this challenge. This article will explain DIP in detail and illustrate how to apply it at the architectural level through a real project using Hexagonal Architecture (also known as Ports and Adapters).

## What is the Dependency Inversion Principle?

The Dependency Inversion Principle was proposed by Robert C. Martin and is stated as follows:

1. **High-level modules should not depend on low-level modules. Both should depend on abstractions.**
2. **Abstractions should not depend on details. Details should depend on abstractions.**

Simply put, DIP promotes "inverting" the traditional dependency flow in an application. Instead of core modules (like business logic) directly depending on technical modules (like databases or UI), we define interfaces in the core module and require technical modules to follow these interfaces.

## Why is DIP Important?

DIP brings many practical benefits:

1. **Loose coupling**: Modules are not tightly bound to each other, making it easy to change one part without affecting the entire system.

2. **Resilience**: When requirements change, only implementations need to be modified without changing the core business logic.

3. **Testability**: Dependencies can be easily mocked through interfaces to test business logic.

4. **Extensibility**: New implementations for existing interfaces can be added without modifying existing code.

5. **Maintainability**: Code becomes more understandable and maintainable thanks to clear separation of concerns.

## DIP at the Architectural Level: Hexagonal Architecture

Hexagonal Architecture is an architectural pattern proposed by Alistair Cockburn, applying DIP at the architectural level. In this architecture:

- **Domain Core**: Contains business logic and defines interfaces (ports) that other modules need to follow.
- **Adapters**: Implement interfaces defined by the domain core.

## Comparing Traditional Layered Architecture and Hexagonal Architecture

### Traditional Layered Architecture

Traditional layered architecture is typically organized in layers as follows:

1. **Presentation Layer (UI Layer)**: Handles user interactions
2. **Business Logic Layer**: Processes business logic
3. **Data Access Layer**: Manages database interactions

In this architecture, each layer depends on the layer below it, creating a one-way dependency flow from top to bottom. The main issues with this architecture:

- **One-way dependency**: Business Logic Layer directly depends on Data Access Layer, making it difficult to change the data storage method.
- **Complex testing**: Testing the Business Logic Layer requires mocking or creating an actual Data Access Layer.
- **Difficulty with changes**: Changes to a lower layer can affect all layers above it.

### Hexagonal Architecture (Ports and Adapters)

Hexagonal Architecture solves these problems by applying DIP at the architectural level:

1. **Domain Core (Hexagon)**: At the center, contains business logic and defines interfaces (ports) that it needs.
2. **Ports**: Interfaces defined by the Domain Core, specifying how to communicate with the outside world.
3. **Adapters**: Specific implementations of ports, connecting the Domain Core with external technical details.

Advantages of this architecture:

- **Separation of concerns**: Domain Core knows nothing about external technical details.
- **Inverted dependencies**: Technical details depend on the Domain Core, not the other way around.
- **Flexibility**: Easily change or add new adapters without affecting the Domain Core.
- **Simple testing**: Domain Core can be tested independently of adapters.

### Difference in Dependency Flow

The most important difference between the two architectures is the dependency flow:

- **Traditional Layered Architecture**:
  ```
  UI → Business Logic → Data Access
  ```
  Business Logic depends on Data Access.

- **Hexagonal Architecture**:
  ```
  UI Adapter → Domain Core ← Data Access Adapter
  ```
  Both UI Adapter and Data Access Adapter depend on the Domain Core.

This is the clearest manifestation of the Dependency Inversion Principle at the architectural level.

## Sample Project Analysis

Our project is organized into the following modules:

```
hexagon-app/
├── api/                # API Module - Handles HTTP requests
├── core/               # Core Module - Contains business logic and interfaces
├── data/               # Data Module - Implements repositories for database
└── gw/                 # Gateway Module - Connects to external services
```

### 1. Core Module - The Center of the Architecture

The core module defines entities and interfaces without depending on any specific framework or technology. This is an excellent illustration of the first principle of DIP: "High-level modules should not depend on low-level modules."

For example, in the core module, we have the `UserRepository` interface:

```kotlin
// core/src/main/kotlin/com/hieunv/app/core/user/UserRepository.kt
@NoRepositoryBean
interface UserRepository : JpaRepository<UserEntity, UUID> {
    override fun findAll(): List<UserEntity>
}
```

This interface defines what the core module needs from a repository storing user information without caring about how it's implemented. This is an example of the "abstraction" that DIP refers to.

### 2. Data Module - Repository Implementation

The data module provides implementations for interfaces defined in the core module:

```kotlin
// data/src/main/kotlin/com/hieunv/app/data/user/UserRepository.kt
@Repository
interface UserRepository : UserRepository {
    @Query("SELECT u.* FROM users AS u", nativeQuery = true)
    override fun findAll(): List<UserEntity>
}
```

Here, the implementation of `UserRepository` inherits from the interface defined in the core module and provides specific details about how to query data from the database. This illustrates the second principle of DIP: "Details should depend on abstractions."

### 3. Relationship Between Modules

In traditional architecture, the dependency flow is typically:
```
API → Business Logic → Data Access
```

Meaning business logic depends on data access, and API depends on business logic.

However, with DIP, the dependency flow is inverted:
```
API → Business Logic ← Data Access
```

Business logic defines interfaces, and data access must follow these interfaces. This "inverts" the normal dependency flow.

In our project, the core module does not depend on the data module. Instead, the data module depends on the core module by implementing interfaces defined in the core module.

### 4. Practical Benefits

This approach brings clear benefits:

1. **Replaceability**: We can easily change the implementation of `UserRepository` (e.g., from MySQL to MongoDB) without modifying the business logic.

2. **Simple testing**: Business logic can be tested by mocking repository interfaces, without needing an actual database.

3. **Architectural clarity**: Easy to understand the boundaries between layers and the responsibilities of each module.

## Applying DIP in Your Project

To effectively apply DIP in your project, follow these principles:

1. **Clearly identify the core domain**: Understand the core business logic of the application and separate it from technical details.

2. **Define interfaces in the core domain**: These interfaces represent what the core domain needs from the outside world.

3. **Implement interfaces in external modules**: Technical modules (database, UI, external services) must follow interfaces defined by the core domain.

4. **Use Dependency Injection**: Use DI to "inject" implementations into the core domain at runtime.

5. **Organize project structure logically**: Clearly reflect the separation between layers and dependency flow.

## Conclusion

The Dependency Inversion Principle is not just a design principle, but also the foundation for sustainable and flexible software architecture. By applying DIP at the architectural level as in Hexagonal Architecture, we can build applications that are highly adaptable to change, easy to maintain, and test.

Our sample project illustrates how DIP can be effectively applied in practice. By separating business logic from technical details and inverting the dependency flow, we create a robust architecture that can withstand the inevitable changes in the software development process.

Investing time to apply DIP may require more initial effort, but the long-term benefits in terms of maintainability and extensibility will prove it to be the right decision.
