package com.hieunv.app.user

import com.hieunv.app.core.user.User
import com.hieunv.app.core.user.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users")
class UserController(
  private val userService: UserService
) {
  @GetMapping
  fun getAllUsers(): List<User> {
    return userService.findAll()
  }
}