# Understanding Hexagonal Architecture Through a Practical Application

## Introduction

In today's modern software development world, designing applications with scalability, maintainability, and flexibility has become more important than ever. One of the most notable architectural patterns is **Hexagonal Architecture** (also known as Ports and Adapters), introduced by Alistair Cockburn in 2005.

This article explains how Hexagonal Architecture works through a practical example - a Spring Boot application built with Kotlin. We'll examine code organization, design principles, and the benefits of adopting this architecture.

## What is Hexagonal Architecture?

Hexagonal Architecture is an architectural pattern that separates the core business logic of an application from external technical details such as databases, user interfaces, or external services. It's visualized as a hexagon with:

- **Core Domain** at the center: containing business rules and logic
- **Ports**: interfaces allowing communication with the application
- **Adapters**: specific implementations of ports, connecting the domain with the outside world

![Hexagonal Architecture](https://herbertograca.com/wp-content/uploads/2018/11/070-explicit-architecture-svg.png?w=1100)

## Feature-Based Organization in Hexagonal Architecture

A key strength of Hexagonal Architecture is how it supports feature-based organization of code. Rather than organizing by technical layers (controllers, services, repositories), we organize primarily by business features or domains.

Our sample project structure illustrates this approach:

```
hexagon-app/
├── api/                                # API Module
│   └── src/main/kotlin/com/hieunv/app/
│       ├── App.kt                      # Main application class
│       └── user/                       # User feature REST controllers
├── core/                               # Core Module
│   └── src/main/kotlin/com/hieunv/app/core/
│       ├── entity/                     # Domain entities
│       └── user/                       # User domain model and interfaces
├── data/                               # Data Module
│   └── src/main/kotlin/com/hieunv/app/data/
│       └── user/                       # User repository implementations
```

Notice how the "user" feature is represented across all three modules, maintaining the hexagonal boundaries while keeping related functionality grouped by feature.

## Detailed Analysis of Each Module

### 1. Core Module - The Heart of the Architecture

The Core module is the heart of the application, containing all business logic and domain rules. Most importantly, **this module has no dependencies on any external frameworks or libraries**.

Let's look at how we define a user entity:

```kotlin
// core/src/main/kotlin/com/hieunv/app/core/user/UserEntity.kt
package com.hieunv.app.core.user

data class UserEntity(
    val id: Long? = null,
    val username: String,
    val email: String,
    val fullName: String
)
```

Note that this class is a pure POJO (Plain Old Java Object), with no JPA annotations or any framework dependencies.

Next, we define ports as interfaces:

```kotlin
// core/src/main/kotlin/com/hieunv/app/core/user/UserRepository.kt
package com.hieunv.app.core.user

interface UserRepository {
    fun findAll(): List<UserEntity>
    fun findById(id: Long): UserEntity?
    fun save(user: UserEntity): UserEntity
    fun deleteById(id: Long)
}
```

```kotlin
// core/src/main/kotlin/com/hieunv/app/core/user/UserService.kt
package com.hieunv.app.core.user

interface UserService {
    fun getAllUsers(): List<UserEntity>
    fun getUserById(id: Long): UserEntity?
    fun createUser(user: UserEntity): UserEntity
    fun updateUser(id: Long, user: UserEntity): UserEntity
    fun deleteUser(id: Long)
}
```

And the service implementation:

```kotlin
// core/src/main/kotlin/com/hieunv/app/core/user/UserServiceImpl.kt
package com.hieunv.app.core.user

class UserServiceImpl(private val userRepository: UserRepository) : UserService {
    override fun getAllUsers(): List<UserEntity> = userRepository.findAll()
    
    override fun getUserById(id: Long): UserEntity? = userRepository.findById(id)
    
    override fun createUser(user: UserEntity): UserEntity = userRepository.save(user)
    
    override fun updateUser(id: Long, user: UserEntity): UserEntity {
        val existingUser = userRepository.findById(id) 
            ?: throw IllegalArgumentException("User not found")
        return userRepository.save(user.copy(id = id))
    }
    
    override fun deleteUser(id: Long) = userRepository.deleteById(id)
}
```

### 2. Data Module - Secondary Adapter

The Data module is responsible for implementing repository interfaces defined in the core module. This module connects the domain to the database.

```kotlin
// data/src/main/kotlin/com/hieunv/app/data/user/JpaUserRepository.kt
package com.hieunv.app.data.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface JpaUserRepository : JpaRepository<UserJpaEntity, Long>
```

```kotlin
// data/src/main/kotlin/com/hieunv/app/data/user/UserJpaEntity.kt
package com.hieunv.app.data.user

import jakarta.persistence.*

@Entity
@Table(name = "users")
data class UserJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    
    @Column(nullable = false, unique = true)
    val username: String,
    
    @Column(nullable = false, unique = true)
    val email: String,
    
    @Column(name = "full_name", nullable = false)
    val fullName: String
)
```

```kotlin
// data/src/main/kotlin/com/hieunv/app/data/user/UserRepositoryImpl.kt
package com.hieunv.app.data.user

import com.hieunv.app.core.user.UserEntity
import com.hieunv.app.core.user.UserRepository
import org.springframework.stereotype.Component

@Component
class UserRepositoryImpl(private val jpaUserRepository: JpaUserRepository) : UserRepository {
    override fun findAll(): List<UserEntity> =
        jpaUserRepository.findAll().map { it.toDomain() }
    
    override fun findById(id: Long): UserEntity? =
        jpaUserRepository.findById(id).orElse(null)?.toDomain()
    
    override fun save(user: UserEntity): UserEntity =
        jpaUserRepository.save(user.toJpaEntity()).toDomain()
    
    override fun deleteById(id: Long) =
        jpaUserRepository.deleteById(id)
        
    // Extension functions for mapping
    private fun UserJpaEntity.toDomain() = UserEntity(
        id = this.id,
        username = this.username,
        email = this.email,
        fullName = this.fullName
    )
    
    private fun UserEntity.toJpaEntity() = UserJpaEntity(
        id = this.id,
        username = this.username,
        email = this.email,
        fullName = this.fullName
    )
}
```

### 3. API Module - Primary Adapter

The API module contains controllers and DTOs to handle HTTP requests and responses. This module connects the domain to users.

```kotlin
// api/src/main/kotlin/com/hieunv/app/user/UserDto.kt
package com.hieunv.app.user

data class UserDto(
    val id: Long? = null,
    val username: String,
    val email: String,
    val fullName: String
)
```

```kotlin
// api/src/main/kotlin/com/hieunv/app/user/UserController.kt
package com.hieunv.app.user

import com.hieunv.app.core.user.UserEntity
import com.hieunv.app.core.user.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {

    @GetMapping
    fun getAllUsers(): List<UserDto> =
        userService.getAllUsers().map { it.toDto() }
    
    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): ResponseEntity<UserDto> {
        val user = userService.getUserById(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(user.toDto())
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(@RequestBody userDto: UserDto): UserDto =
        userService.createUser(userDto.toDomain()).toDto()
    
    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: Long, @RequestBody userDto: UserDto): UserDto =
        userService.updateUser(id, userDto.toDomain()).toDto()
    
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteUser(@PathVariable id: Long) =
        userService.deleteUser(id)
        
    // Extension functions for mapping
    private fun UserEntity.toDto() = UserDto(
        id = this.id,
        username = this.username,
        email = this.email,
        fullName = this.fullName
    )
    
    private fun UserDto.toDomain() = UserEntity(
        id = this.id,
        username = this.username,
        email = this.email,
        fullName = this.fullName
    )
}
```

## Benefits of Feature-Based Organization in Hexagonal Architecture

Traditional layered architectures typically organize code by technical concerns (controllers, services, repositories). However, with a feature-based approach in Hexagonal Architecture, we gain several advantages:

1. **Better Discoverability**: All code related to a specific feature is grouped together within each module, making it easier to find and understand
2. **Improved Maintainability**: Changes to a feature typically affect related files, which are now located closer together
3. **Enhanced Ownership**: Teams can take ownership of entire features across architectural boundaries
4. **Easier Onboarding**: New developers can understand features holistically rather than having to jump between technical layers
5. **Reduced Coupling**: Features are isolated from each other, minimizing cross-feature dependencies

## Data Flow in Hexagonal Architecture

Let's follow the flow of an HTTP request from start to finish:

1. **HTTP Request** -> REST Controller in API Module (Primary Adapter)
2. Controller converts DTO to Entity and calls the Service interface
3. **Service** in Core Module processes business logic
4. Service calls Repository interface to retrieve/save data
5. **Repository Implementation** in Data Module (Secondary Adapter) performs database queries
6. Data is returned in reverse order: Repository -> Service -> Controller -> HTTP Response

## Benefits of Hexagonal Architecture

1. **High Maintainability**: Business logic is separated from technical details, making it easier to understand and maintain
2. **Technology Agnosticism**: You can change database, UI, or framework without affecting the domain
3. **Excellent Testability**: The domain can be tested independently, without setting up database or UI
4. **Parallel Development**: Teams can work in parallel on different parts of the application
5. **Flexibility**: Easy to add new adapters for new technologies or communication channels

## When to Use Hexagonal Architecture

Hexagonal Architecture is especially useful for:

- Complex applications with many business rules
- Long-lived projects that must be easy to maintain
- Systems with multiple interfaces (REST API, GraphQL, UI...)
- When you want to ensure domain logic isn't polluted by technical details

However, for smaller applications or prototypes, this architectural pattern might be overkill.

## Conclusion

Hexagonal Architecture provides a structured approach to organizing code, separating the core domain from technical details. Our sample project with three clear modules (api, core, data) demonstrates how to implement this architectural pattern in practice, with an emphasis on feature-based organization.

The main benefits are maintainability, adaptability to change, and good testability. However, it requires some initial effort to set up the project structure and adhere to the principles of the architecture.

If you're developing a complex application with high requirements for maintenance and scalability, and you value organizing code by features rather than technical layers, Hexagonal Architecture is definitely worth considering.

---

*References:*
1. [Alistair Cockburn - Hexagonal Architecture](https://alistair.cockburn.us/hexagonal-architecture/)
2. [DDD, Hexagonal, Onion, Clean, CQRS, … How I put it all together](https://herbertograca.com/2017/11/16/explicit-architecture-01-ddd-hexagonal-onion-clean-cqrs-how-i-put-it-all-together/)
3. [Spring Boot Documentation](https://spring.io/projects/spring-boot)
4. [Kotlin Documentation](https://kotlinlang.org/docs/home.html)
