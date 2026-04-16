package com.cystg.wallet.infrastructure.persistence.cassandra

import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

data class LedgerEntryRow(
    val id: UUID,
    val transactionId: UUID,
    val accountId: UUID,
    val entryType: String,
    val amount: BigDecimal,
    val currency: String,
    val createdAt: Instant,
)
