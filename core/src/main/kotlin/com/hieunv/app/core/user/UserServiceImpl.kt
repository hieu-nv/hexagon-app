package com.hieunv.app.core.user

import org.springframework.stereotype.Component

@Component
class UserServiceImpl(
  private val userRepository: UserRepository
) : UserService {
  override fun findAll(): List<UserEntity> {
    return userRepository.findAll()
  }
}