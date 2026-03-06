package com.hieunv.app.data.auth

import com.hieunv.app.core.auth.User
import com.hieunv.app.core.auth.UserRepository
import org.springframework.stereotype.Component

@Component
class UserRepositoryImpl(
    private val userJpaRepository: UserJpaRepository
) : UserRepository {

    override fun findAll(): List<User> {
        return userJpaRepository.findAll().map { it.toDomain() }
    }
}

private fun UserEntity.toDomain() = User(
    id = id,
    username = username,
    password = password,
    enabled = enabled,
    createdAt = createdAt,
    updatedAt = updatedAt,
    deletedAt = deletedAt
)