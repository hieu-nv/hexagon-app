package com.hieunv.app.core.auth

interface UserUseCase {
  fun findAll(): List<User>
}