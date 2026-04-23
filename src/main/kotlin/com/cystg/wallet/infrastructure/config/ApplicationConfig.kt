package com.cystg.wallet.infrastructure.config

import com.cystg.wallet.application.common.ports.AuthorizationPort
import com.cystg.wallet.application.common.ports.LedgerPersistencePort
import com.cystg.wallet.application.common.ports.PasetoTokenPort
import com.cystg.wallet.application.common.ports.TransactionPersistencePort
import com.cystg.wallet.application.transaction.RecordTransactionUseCase
import com.cystg.wallet.infrastructure.persistence.cassandra.CassandraLedgerAdapter
import com.cystg.wallet.infrastructure.persistence.postgres.PostgresTransactionAdapter
import com.cystg.wallet.infrastructure.security.PasetoAuthorizationAdapter
import com.cystg.wallet.infrastructure.security.PasetoTokenAdapter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ApplicationConfig {
    @Bean
    fun transactionPersistencePort(): TransactionPersistencePort = PostgresTransactionAdapter()

    @Bean
    fun ledgerPersistencePort(): LedgerPersistencePort = CassandraLedgerAdapter()

    @Bean
    fun authorizationPort(): AuthorizationPort = PasetoAuthorizationAdapter()

    @Bean
    fun pasetoTokenPort(authorizationPort: AuthorizationPort): PasetoTokenPort =
        PasetoTokenAdapter(authorizationPort)

    @Bean
    fun recordTransactionUseCase(
        transactionPersistencePort: TransactionPersistencePort,
        ledgerPersistencePort: LedgerPersistencePort,
    ): RecordTransactionUseCase = RecordTransactionUseCase(
        transactionPersistencePort = transactionPersistencePort,
        ledgerPersistencePort = ledgerPersistencePort,
    )
}
