package com.cystg.wallet.application.ledger

import arrow.core.Either
import com.cystg.wallet.domain.shared.DomainError
import java.math.BigDecimal
import java.util.UUID

interface RecordTransactionUseCase {
    fun record(command: RecordTransactionCommand): Either<DomainError, RecordTransactionResult>
}

data class RecordTransactionCommand(
    val userId: UUID,
    val walletId: UUID,
    val accountId: UUID,
    val categoryId: UUID?,
    val amount: BigDecimal,
    val currency: String,
    val note: String?,
)

data class RecordTransactionResult(
    val transactionId: UUID,
)
