package com.hieunv.app.data.user

import com.hieunv.app.core.user.UserEntity
import com.hieunv.app.core.user.UserRepository
import org.springframework.stereotype.Component

@Component
class UserRepositoryImpl(
    private val userJpaRepository: UserJpaRepository
) : UserRepository {

    override fun findAll(): List<UserEntity> {
        return userJpaRepository.findAll()
    }

}