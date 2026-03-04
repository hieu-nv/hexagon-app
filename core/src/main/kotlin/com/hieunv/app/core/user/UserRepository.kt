package com.hieunv.app.core.user

import org.springframework.stereotype.Component

interface UserRepository {
    fun findAll(): List<UserEntity>
}