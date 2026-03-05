package com.hieunv.app.core.user

import java.time.LocalDateTime

data class User(
    val id: String,
    val username: String,
    var password: String,
    var enabled: Boolean,
    val createdAt: LocalDateTime,
    var updatedAt: LocalDateTime,
    var deletedAt: LocalDateTime?
)
