package com.cystg.wallet.application.common.ports

import com.cystg.wallet.application.ledger.LedgerEntry

interface LedgerPersistencePort {
    fun save(entries: List<LedgerEntry>): List<LedgerEntry>
}
