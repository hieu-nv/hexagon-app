package com.hieunv.app.core.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean
import java.util.UUID

@NoRepositoryBean
interface UserRepository : JpaRepository<UserEntity, UUID> {
    override fun findAll(): List<UserEntity>
}