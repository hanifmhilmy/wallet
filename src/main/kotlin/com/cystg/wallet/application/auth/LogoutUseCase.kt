package com.cystg.wallet.application.auth

import com.cystg.wallet.application.common.ports.TokenStorePort
import com.cystg.wallet.domain.shared.DomainError
import arrow.core.Either
import arrow.core.left
import arrow.core.right

class LogoutUseCase(
    private val tokenStorePort: TokenStorePort,
) {
    data class LogoutCommand(
        val token: String
    )
    
    fun logout(command: LogoutCommand): Either<DomainError, Unit> {
        tokenStorePort.delete(command.token)
        return Unit.right()
    }
}