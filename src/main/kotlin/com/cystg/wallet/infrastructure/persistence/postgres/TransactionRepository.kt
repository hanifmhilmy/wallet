package com.cystg.wallet.infrastructure.persistence.postgres

import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

data class TransactionRow(
    val id: UUID,
    val userId: UUID,
    val walletId: UUID,
    val accountId: UUID,
    val categoryId: UUID?,
    val amount: BigDecimal,
    val currency: String,
    val note: String?,
    val transactedAt: Instant,
)
