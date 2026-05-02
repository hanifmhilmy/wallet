package com.cystg.wallet.service.common.ports

import java.util.UUID

interface UserPersistencePort {
    suspend fun save(user: User)
    suspend fun findById(id: UUID): User?
    suspend fun findByEmail(email: String): User?
    suspend fun deleteById(id: UUID)
}

data class User(
    val id: UUID,
    val email: String,
    val username: String,
    val passwordHash: String,
    val status: UserStatus
)

enum class UserStatus {
    ACTIVE,
    INACTIVE,
    SUSPENDED
}