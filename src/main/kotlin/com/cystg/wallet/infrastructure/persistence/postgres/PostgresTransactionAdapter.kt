package com.cystg.wallet.infrastructure.persistence.postgres

import com.cystg.wallet.application.common.ports.TransactionPersistencePort
import com.cystg.wallet.application.ledger.Transaction

class PostgresTransactionAdapter : TransactionPersistencePort {
    override fun save(transaction: Transaction): Transaction = transaction
}
