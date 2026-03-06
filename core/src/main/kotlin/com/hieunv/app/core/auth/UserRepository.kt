package com.hieunv.app.core.auth

interface UserRepository {
  fun findAll(): List<User>
}