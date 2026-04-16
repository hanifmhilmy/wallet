package com.cystg.wallet.application.common.ports

import com.cystg.wallet.application.ledger.Transaction

interface TransactionPersistencePort {
    fun save(transaction: Transaction): Transaction
}
