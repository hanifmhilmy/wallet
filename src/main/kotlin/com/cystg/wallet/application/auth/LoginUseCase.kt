package com.cystg.wallet.application.auth

import com.cystg.wallet.application.common.ports.AuthorizationPort
import com.cystg.wallet.application.common.ports.PasetoTokenPort
import com.cystg.wallet.application.common.ports.TokenStorePort
import com.cystg.wallet.application.common.ports.UserPersistencePort
import com.cystg.wallet.domain.shared.DomainError
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import java.util.UUID

class LoginUseCase(
    private val userPersistencePort: UserPersistencePort,
    private val authorizationPort: AuthorizationPort,
    private val pasetoTokenPort: PasetoTokenPort,
    private val tokenStorePort: TokenStorePort,
) {
    data class LoginCommand(
        val email: String,
        val password: String
    )
    
    data class LoginResult(
        val token: String
    )
    
    fun login(command: LoginCommand): Either<DomainError, LoginResult> {
        // Implementation to be added
        error("not implemented")
    }
}