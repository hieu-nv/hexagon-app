package com.hieunv.app.core.user

interface UserUseCase {
  fun findAll(): List<User>
}