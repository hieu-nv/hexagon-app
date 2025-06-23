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
│       ├── user/                       # User feature REST controllers
│       └── pokemon/                    # Pokemon feature REST controllers
├── core/                               # Core Module
│   └── src/main/kotlin/com/hieunv/app/core/
│       ├── entity/                     # Domain entities
│       ├── user/                       # User domain model and interfaces
│       └── pokemon/                    # Pokemon domain model and interfaces
├── data/                               # Data Module
│   └── src/main/kotlin/com/hieunv/app/data/
│       └── user/                       # User repository implementations
├── gw/                                 # Gateway Module
│   └── src/main/kotlin/com/hieunv/gw/
│       ├── client/                     # API client implementations
│       └── pokemon/                    # Pokemon gateway implementations
```

Notice how each feature is represented across multiple modules, maintaining the hexagonal boundaries while keeping related functionality grouped by feature.

## Detailed Analysis of Each Module

### 1. Core Module - The Heart of the Architecture

The Core module is the heart of the application, containing business logic and domain entities. In our implementation, we have JPA annotations in our core module for simplicity.

Let's look at our base entity class and user entity:

```kotlin
// core/src/main/kotlin/com/hieunv/app/core/entity/SystemEntity.kt
package com.hieunv.app.core.entity

import jakarta.persistence.Column
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import java.time.LocalDateTime
import java.util.UUID

@MappedSuperclass
open class SystemEntity(
    @Id val id: String = UUID.randomUUID().toString(),

    @Column(nullable = false, name = "created_at") val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false, name = "updated_at") var updatedAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false, name = "deleted_at") var deletedAt: LocalDateTime = LocalDateTime.now(),
)
```

```kotlin
// core/src/main/kotlin/com/hieunv/app/core/user/UserEntity.kt
package com.hieunv.app.core.user

import com.hieunv.app.core.entity.SystemEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "users")
class UserEntity(
    @Column(nullable = false, unique = true) val username: String = "",

    @Column(nullable = false) var password: String = "",

    @Column(nullable = false) var enabled: Boolean = true,

    ) : SystemEntity()
```

Next, we define the repository interface:

```kotlin
// core/src/main/kotlin/com/hieunv/app/core/user/UserRepository.kt
package com.hieunv.app.core.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean
import java.util.UUID

@NoRepositoryBean
interface UserRepository : JpaRepository<UserEntity, UUID> {
    override fun findAll(): List<UserEntity>
}
```

### 2. Data Module - Secondary Adapter

The Data module is responsible for implementing repository interfaces defined in the core module. This module connects the domain to the database.

```kotlin
// data/src/main/kotlin/com/hieunv/app/data/user/UserRepository.kt
package com.hieunv.app.data.user

import com.hieunv.app.core.user.UserEntity
import com.hieunv.app.core.user.UserRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : UserRepository {

    @Query("SELECT u.* FROM users AS u", nativeQuery = true)
    override fun findAll(): List<UserEntity>
}
```

### 3. API Module - Primary Adapter

The API module contains controllers that handle HTTP requests and responses, acting as the primary adapter in our hexagonal architecture. This module connects external users to our domain.

```kotlin
// api/src/main/kotlin/com/hieunv/app/user/UserController.kt
package com.hieunv.app.user

import com.hieunv.app.core.user.UserEntity
import com.hieunv.app.core.user.UserRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userRepository: UserRepository
) {
    @GetMapping
    fun getAllUsers(): List<UserEntity> {
        val users = userRepository.findAll()
        return users
    }
}
```

### 4. Gateway Module - Secondary Adapter for External Services

The Gateway module is responsible for implementing interfaces defined in the core module that interact with external services or APIs. This module connects the domain to external systems.

Let's look at how we interact with an external Pokemon API:

```kotlin
// core/src/main/kotlin/com/hieunv/app/core/pokemon/Pokemon.kt
package com.hieunv.app.core.pokemon

/**
 * Data class representing a Pokémon.
 */
data class Pokemon(
    val name: String = "", 
    val url: String = ""
)
```

```kotlin
// core/src/main/kotlin/com/hieunv/app/core/pokemon/PokemonGateway.kt
package com.hieunv.app.core.pokemon

interface PokemonGateway {
    /**
     * Fetches a list of Pokémon.
     *
     * @param limit the maximum number of Pokémon to return
     * @param offset the offset for pagination
     * @return a list of Pokémon
     */
    fun fetchPokemonList(limit: Int, offset: Int): List<Pokemon?>?

    /**
     * Fetches details of a specific Pokémon by its ID.
     *
     * @param id the ID of the Pokémon
     * @return details of the specified Pokémon
     */
    fun fetchPokemonById(id: Int): Pokemon?
}
```

For handling API responses with nested structures, we created a wrapper class:

```kotlin
// gw/src/main/kotlin/com/hieunv/gw/client/Poke.kt
package com.hieunv.gw.client

/**
 * Generic wrapper class for PokeAPI responses with pagination.
 */
data class Poke<T>(
    val count: Int = 0,
    val next: String? = null,
    val previous: String? = null,
    val results: List<T> = emptyList()
)
```

We implemented a generic API client to handle HTTP requests:

```kotlin
// gw/src/main/kotlin/com/hieunv/gw/client/PokeClient.kt
package com.hieunv.gw.client

import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class PokeClient(private val restTemplate: RestTemplate) {
    /**
     * Generic GET method with explicit ParameterizedTypeReference for handling nested generic types
     */
    fun <T> get(url: String, responseType: ParameterizedTypeReference<T>): T? {
        return try {
            val response = restTemplate.exchange(url, HttpMethod.GET, null, responseType)
            response.body
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
```

Finally, we implemented the Pokemon gateway interface:

```kotlin
// gw/src/main/kotlin/com/hieunv/gw/pokemon/PokemonGateway.kt
package com.hieunv.gw.pokemon

import com.hieunv.app.core.pokemon.Pokemon
import com.hieunv.gw.client.PokeClient
import com.hieunv.gw.client.Poke
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Service

@Service
class PokemonGateway(private val pokeClient: PokeClient) : com.hieunv.app.core.pokemon.PokemonGateway {
    override fun fetchPokemonList(limit: Int, offset: Int): List<Pokemon?>? {
        val responseType = object : ParameterizedTypeReference<Poke<Map<String, Any>>>() {}
        return pokeClient.get("https://pokeapi.co/api/v2/pokemon?limit=$limit&offset=$offset", responseType)
            ?.results
            ?.map { map -> 
                mapToPokemon(map)
            }
    }

    private fun mapToPokemon(map: Map<String, Any>?): Pokemon? {
        if (map == null) return null
        
        val name = map["name"] as? String ?: ""
        val url = map["url"] as? String ?: ""
        return Pokemon(name, url)
    }

    override fun fetchPokemonById(id: Int): Pokemon? {
        // Implementation for fetching a specific Pokemon
        TODO("Not yet implemented")
    }
}
```

In the API module, we created a controller to expose Pokemon data:

```kotlin
// api/src/main/kotlin/com/hieunv/app/pokemon/PokemonController.kt
package com.hieunv.app.pokemon

import com.hieunv.app.core.pokemon.Pokemon
import com.hieunv.app.core.pokemon.PokemonGateway
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/pokemon")
class PokemonController(
    private val pokemonGateway: PokemonGateway
) {
    @GetMapping
    fun getPokemonList(
        @RequestParam(defaultValue = "20") limit: Int,
        @RequestParam(defaultValue = "0") offset: Int
    ): List<Pokemon> {
        return pokemonGateway.fetchPokemonList(limit, offset)?.filterNotNull() ?: emptyList()
    }

    @GetMapping("/{id}")
    fun getPokemonById(@PathVariable id: Int): Pokemon? {
        return pokemonGateway.fetchPokemonById(id)
    }
}
```

## Data Flow with External Services in Hexagonal Architecture

Let's follow the flow of an HTTP request from start to finish, including external API integration:

1. **HTTP Request** -> REST Controller in API Module (Primary Adapter)
2. Controller converts DTO to Entity and calls the Service interface
3. **Service** in Core Module processes business logic
4. Service calls Repository interface to retrieve/save data
5. **Repository Implementation** in Data Module (Secondary Adapter) performs database queries
6. For external API calls, the Service uses the Gateway interface
7. **Gateway Implementation** in Gateway Module (Secondary Adapter) interacts with the external API
8. Data is returned in reverse order: Repository/Gateway -> Service -> Controller -> HTTP Response

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
