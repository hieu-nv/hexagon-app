package com.hieunv.app.data.user

import com.hieunv.app.core.user.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserJpaRepository : JpaRepository<UserEntity, UUID> {
    override fun findAll(): List<UserEntity>
}
