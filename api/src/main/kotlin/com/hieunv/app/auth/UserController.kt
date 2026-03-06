package com.hieunv.app.auth

import com.hieunv.app.core.auth.User
import com.hieunv.app.core.auth.UserUseCase
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users")
class UserController(
  private val userUseCase: UserUseCase
) {
  @GetMapping
  fun getAllUsers(): List<User> {
    return userUseCase.findAll()
  }
}