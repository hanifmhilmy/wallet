package com.cystg.wallet.service.transaction

import com.cystg.wallet.service.common.ports.LedgerPersistencePort
import com.cystg.wallet.service.common.ports.TransactionPersistencePort
import com.cystg.wallet.domain.shared.DomainError
import arrow.core.Either
import java.util.UUID

class RecordTransactionUseCase(
    private val transactionPersistencePort: TransactionPersistencePort,
    private val ledgerPersistencePort: LedgerPersistencePort,
) {
    data class RecordTransactionCommand(
        val userId: UUID,
        val walletId: UUID,
        val accountId: UUID,
        val categoryId: UUID?,
        val amount: java.math.BigDecimal,
        val currency: String,
        val note: String?
    )
    
    data class RecordTransactionResult(
        val transactionId: UUID
    )
    
    fun record(command: RecordTransactionCommand): Either<DomainError, RecordTransactionResult> {
        // Implementation to be added
        error("not implemented")
    }
}