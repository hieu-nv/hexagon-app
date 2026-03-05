package com.hieunv.app.user

import java.time.LocalDateTime

data class UserDto(
    val id: String,
    val username: String,
    val enabled: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
