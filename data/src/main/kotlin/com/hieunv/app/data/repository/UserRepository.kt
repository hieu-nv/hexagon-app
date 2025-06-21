package com.hieunv.app.data.repository

import com.hieunv.app.core.entity.UserEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : com.hieunv.app.core.repository.UserRepository {

    @Query("SELECT u.* FROM users AS u", nativeQuery = true)
    override fun findAll(): List<UserEntity>
}
