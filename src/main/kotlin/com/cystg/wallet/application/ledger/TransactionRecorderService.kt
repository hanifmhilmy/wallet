package com.cystg.wallet.application.ledger

import arrow.core.Either
import com.cystg.wallet.application.common.ports.AuthorizationPort
import com.cystg.wallet.application.common.ports.LedgerPersistencePort
import com.cystg.wallet.application.common.ports.TransactionPersistencePort
import com.cystg.wallet.domain.shared.DomainError
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

class TransactionRecorderService(
    private val transactionPersistencePort: TransactionPersistencePort,
    private val ledgerPersistencePort: LedgerPersistencePort,
    private val authorizationPort: AuthorizationPort,
) : RecordTransactionUseCase {
    override fun record(command: RecordTransactionCommand): Either<DomainError, RecordTransactionResult> {
        if (command.amount <= BigDecimal.ZERO) return Either.Left(DomainError("Amount must be greater than zero"))
        if (command.currency.length != 3) return Either.Left(DomainError("Currency must be a 3-letter code"))

        val transaction = Transaction(
            id = UUID.randomUUID(),
            userId = command.userId,
            walletId = command.walletId,
            accountId = command.accountId,
            categoryId = command.categoryId,
            amount = command.amount,
            currency = command.currency,
            note = command.note,
            transactedAt = Instant.now(),
        )

        val entries = listOf(
            LedgerEntry(
                id = UUID.randomUUID(),
                transactionId = transaction.id,
                accountId = command.accountId,
                entryType = LedgerEntryType.DEBIT,
                amount = command.amount,
                currency = command.currency,
            ),
            LedgerEntry(
                id = UUID.randomUUID(),
                transactionId = transaction.id,
                accountId = command.walletId,
                entryType = LedgerEntryType.CREDIT,
                amount = command.amount,
                currency = command.currency,
            ),
        )

        runCatching { authorizationPort.authorize(command.note.orEmpty()) }
            .getOrElse { return Either.Left(DomainError(it.message ?: "Unauthorized")) }

        runCatching { transactionPersistencePort.save(transaction) }
            .getOrElse { return Either.Left(DomainError(it.message ?: "Failed to persist transaction")) }

        runCatching { ledgerPersistencePort.save(entries) }
            .getOrElse { return Either.Left(DomainError(it.message ?: "Failed to persist ledger entries")) }

        return Either.Right(RecordTransactionResult(transactionId = transaction.id))
    }
}
