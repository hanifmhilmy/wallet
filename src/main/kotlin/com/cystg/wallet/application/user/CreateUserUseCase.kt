package com.cystg.wallet.application.user

import com.cystg.wallet.application.common.ports.UserPersistencePort
import com.cystg.wallet.domain.shared.DomainError
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import java.util.UUID

class CreateUserUseCase(
    private val userPersistencePort: UserPersistencePort,
) {
    data class CreateUserCommand(
        val email: String,
        val password: String,
        val username: String
    )
    
    data class CreateUserResult(
        val userId: UUID
    )
    
    fun createUser(command: CreateUserCommand): Either<DomainError, CreateUserResult> {
        // Implementation to be added
        error("not implemented")
    }
}