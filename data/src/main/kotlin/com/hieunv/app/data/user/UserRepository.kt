package com.hieunv.app.data.user

import com.hieunv.app.core.user.UserEntity
import com.hieunv.app.core.user.UserRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : UserRepository {

    @Query("SELECT u.* FROM users AS u", nativeQuery = true)
    override fun findAll(): List<UserEntity>
}