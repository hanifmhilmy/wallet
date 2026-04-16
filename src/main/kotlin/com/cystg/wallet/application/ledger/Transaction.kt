package com.cystg.wallet.application.ledger

import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

data class Transaction(
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
