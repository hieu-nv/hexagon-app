package com.hieunv.app.core.user

import org.springframework.stereotype.Component

@Component
class UserServiceImpl(
  private val userRepository: UserRepository
) : UserService {
  override fun findAll(): List<User> {
    return userRepository.findAll()
  }
}