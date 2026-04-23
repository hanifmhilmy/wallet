package com.cystg.wallet.application.account

import com.cystg.wallet.application.common.ports.AccountPersistencePort
import com.cystg.wallet.domain.shared.DomainError
import arrow.core.Either
import arrow.core.left
import arrow.core.leftNel
import arrow.core.raise.Raise
import arrow.core.raise.either
import arrow.core.right
import java.util.UUID

class CreateAccountUseCase(
    private val accountPersistencePort: AccountPersistencePort,
) {
    data class CreateAccountCommand(
        val userId: UUID,
        val name: String,
        val currency: String = "IDR"
    )
    
    data class CreateAccountResult(
        val accountId: UUID
    )
    
    fun createAccount(command: CreateAccountCommand): Either<DomainError, CreateAccountResult> {
        // Implementation to be added
        error("not implemented")
    }
}