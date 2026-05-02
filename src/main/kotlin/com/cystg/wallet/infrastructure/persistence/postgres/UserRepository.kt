package com.cystg.wallet.infrastructure.persistence.postgres

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.UUID

@Table("users")
data class UserEntity(
    @Id
    val id: UUID,
    val email: String,
    val username: String,
    val password: String, // Maps to db column password, mapped from Domain's passwordHash
    val status: String,
    val version: Long = 0
)

interface UserRepository : CoroutineCrudRepository<UserEntity, UUID> {
    suspend fun findByEmail(email: String): UserEntity?
}
