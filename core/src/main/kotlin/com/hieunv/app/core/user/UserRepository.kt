package com.hieunv.app.core.user

interface UserRepository {
  fun findAll(): List<User>
}