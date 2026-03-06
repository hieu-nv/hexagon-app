package com.hieunv.app.data.auth

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserJpaRepository : JpaRepository<UserEntity, String> {
    override fun findAll(): List<UserEntity>
}
