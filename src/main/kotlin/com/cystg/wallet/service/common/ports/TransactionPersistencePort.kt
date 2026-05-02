package com.cystg.wallet.service.common.ports

import java.util.UUID

interface TransactionPersistencePort {
    fun save(transaction: Transaction): Transaction
}

data class Transaction(
    val id: UUID,
    val userId: UUID,
    val categoryId: UUID?,
    val amount: java.math.BigDecimal,
    val currency: String,
    val type: TransactionType,
    val status: TransactionStatus,
    val note: String?,
    val transactedAt: java.time.Instant,
)

enum class TransactionType {
    INCOME,
    EXPENSE,
    TRANSFER
}

enum class TransactionStatus {
    PENDING,
    COMPLETED,
    CANCELLED
}