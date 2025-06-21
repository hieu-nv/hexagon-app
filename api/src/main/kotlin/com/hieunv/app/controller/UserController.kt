package com.hieunv.app.controller

import com.hieunv.app.core.entity.UserEntity
import com.hieunv.app.core.repository.UserRepository
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