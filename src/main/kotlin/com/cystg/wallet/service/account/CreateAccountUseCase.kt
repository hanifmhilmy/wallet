package com.cystg.wallet.service.account

import com.cystg.wallet.service.common.ports.AccountPersistencePort
import com.cystg.wallet.domain.shared.DomainError
import arrow.core.Either
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