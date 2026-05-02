package com.cystg.wallet.infrastructure.persistence.postgres

import com.cystg.wallet.service.common.ports.Transaction
import com.cystg.wallet.service.common.ports.TransactionPersistencePort

class PostgresTransactionAdapter : TransactionPersistencePort {
    override fun save(transaction: Transaction): Transaction {
        TODO("Not yet implemented")
    }
}
