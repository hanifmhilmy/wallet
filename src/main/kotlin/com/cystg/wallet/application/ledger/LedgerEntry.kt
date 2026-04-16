package com.cystg.wallet.application.ledger

import java.math.BigDecimal
import java.util.UUID

data class LedgerEntry(
    val id: UUID,
    val transactionId: UUID,
    val accountId: UUID,
    val entryType: LedgerEntryType,
    val amount: BigDecimal,
    val currency: String,
)
