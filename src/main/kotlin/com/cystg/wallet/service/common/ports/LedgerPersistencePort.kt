package com.cystg.wallet.service.common.ports

import java.util.UUID

interface LedgerPersistencePort {
    fun save(entries: List<LedgerEntry>): List<LedgerEntry>
}

data class LedgerEntry(
    val id: UUID,
    val transactionId: UUID,
    val accountId: UUID,
    val entryType: LedgerEntryType,
    val amount: java.math.BigDecimal,
    val currency: String,
    val createdAt: java.time.Instant,
)

enum class LedgerEntryType {
    DEBIT,
    CREDIT,
}