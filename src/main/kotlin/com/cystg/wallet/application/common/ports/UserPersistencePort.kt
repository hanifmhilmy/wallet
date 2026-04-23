package com.cystg.wallet.application.common.ports

import java.util.UUID

interface UserPersistencePort {
    fun save(user: User)
    fun findById(id: UUID): User?
    fun findByEmail(email: String): User?
    fun deleteById(id: UUID)
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