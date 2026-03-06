package com.hieunv.app.core.auth

class UserUseCaseImpl(
  private val userRepository: UserRepository
) : UserUseCase {
  override fun findAll(): List<User> {
    return userRepository.findAll()
  }
}