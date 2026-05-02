package com.cystg.wallet.service.user

import com.cystg.wallet.service.common.ports.UserPersistencePort
import com.cystg.wallet.service.common.ports.PasswordEncoderPort
import com.cystg.wallet.domain.shared.DomainError
import arrow.core.Either
import arrow.core.left
import java.util.UUID

class CreateUserUseCase(
    private val userPersistencePort: UserPersistencePort,
    private val passwordEncoderPort: PasswordEncoderPort,
) {
    data class CreateUserCommand(
        val email: String,
        val password: String,
        val username: String
    )
    
    data class CreateUserResult(
        val userId: UUID
    )
    
    suspend fun createUser(command: CreateUserCommand): Either<DomainError, CreateUserResult> {
        val existingByEmail = userPersistencePort.findByEmail(command.email)
        if (existingByEmail != null) {
            return DomainError("Email already registered").left()
        }

        val passwordHash = passwordEncoderPort.encode(command.password)
        val userResult = com.cystg.wallet.domain.user.User.register(command.email, command.username, passwordHash)

        return userResult.map { domainUser ->
            val applicationUser = com.cystg.wallet.service.common.ports.User(
                id = domainUser.id.value,
                email = domainUser.email.value,
                username = domainUser.username,
                passwordHash = domainUser.passwordHash,
                status = com.cystg.wallet.service.common.ports.UserStatus.valueOf(domainUser.status.name)
            )
            userPersistencePort.save(applicationUser)
            CreateUserResult(applicationUser.id)
        }
    }
}