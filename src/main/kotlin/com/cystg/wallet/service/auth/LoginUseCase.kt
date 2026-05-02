package com.cystg.wallet.service.auth

import com.cystg.wallet.service.common.ports.PasswordEncoderPort
import com.cystg.wallet.service.common.ports.PasetoTokenPort
import com.cystg.wallet.service.common.ports.UserPersistencePort
import com.cystg.wallet.domain.shared.DomainError
import arrow.core.Either
import arrow.core.left
import arrow.core.right

class LoginUseCase(
    private val userPersistencePort: UserPersistencePort,
    private val passwordEncoderPort: PasswordEncoderPort,
    private val pasetoTokenPort: PasetoTokenPort,
) {
    data class LoginCommand(
        val email: String,
        val password: String
    )
    
    data class LoginResult(
        val token: String
    )
    
    suspend fun login(command: LoginCommand): Either<DomainError, LoginResult> {
        val user = userPersistencePort.findByEmail(command.email)
            ?: return DomainError("Invalid credentials").left()

        if (!passwordEncoderPort.matches(command.password, user.passwordHash)) {
            return DomainError("Invalid credentials").left()
        }

        val token = pasetoTokenPort.issue(user.id.toString(), emptySet())
        
        return LoginResult(token).right()
    }
}