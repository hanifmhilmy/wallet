package com.cystg.wallet.service.auth

import com.cystg.wallet.service.common.ports.TokenStorePort
import com.cystg.wallet.domain.shared.DomainError
import arrow.core.Either
import arrow.core.right

class LogoutUseCase(
    private val tokenStorePort: TokenStorePort,
) {
    data class LogoutCommand(
        val token: String
    )
    
    suspend fun logout(command: LogoutCommand): Either<DomainError, Unit> {
        tokenStorePort.store(command.token, 86400) // Store for 1 day as blacklisted
        return Unit.right()
    }
}