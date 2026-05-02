package com.cystg.wallet.service.user

import com.cystg.wallet.service.common.ports.UserPersistencePort
import com.cystg.wallet.domain.shared.DomainError
import arrow.core.Either
import arrow.core.right
import java.util.UUID

class DeleteUserUseCase(
    private val userPersistencePort: UserPersistencePort,
) {
    data class DeleteUserCommand(
        val userId: UUID
    )
    
    fun deleteUser(command: DeleteUserCommand): Either<DomainError, Unit> {
        // Implementation to be added
        return Unit.right()
    }
}