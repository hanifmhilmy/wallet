package com.cystg.wallet.infrastructure.persistence.cassandra

import com.cystg.wallet.application.common.ports.LedgerEntry
import com.cystg.wallet.application.common.ports.LedgerPersistencePort

class CassandraLedgerAdapter : LedgerPersistencePort {
    override fun save(entries: List<LedgerEntry>): List<LedgerEntry> {
        TODO("Not yet implemented")
    }
}
