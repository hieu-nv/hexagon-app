package com.hieunv.app.data.user

import com.hieunv.app.core.user.User
import com.hieunv.app.core.user.UserRepository
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