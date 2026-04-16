package com.cystg.wallet.infrastructure.persistence.cassandra

import com.cystg.wallet.application.common.ports.LedgerPersistencePort
import com.cystg.wallet.application.ledger.LedgerEntry

class CassandraLedgerAdapter : LedgerPersistencePort {
    override fun save(entries: List<LedgerEntry>): List<LedgerEntry> = entries
}
