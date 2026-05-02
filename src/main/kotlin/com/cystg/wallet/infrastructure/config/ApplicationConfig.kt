package com.cystg.wallet.infrastructure.config

import com.cystg.wallet.service.common.ports.AuthorizationPort
import com.cystg.wallet.service.common.ports.LedgerPersistencePort
import com.cystg.wallet.service.common.ports.PasetoTokenPort
import com.cystg.wallet.service.common.ports.TransactionPersistencePort
import com.cystg.wallet.service.transaction.RecordTransactionUseCase
import com.cystg.wallet.infrastructure.persistence.cassandra.CassandraLedgerAdapter
import com.cystg.wallet.infrastructure.persistence.postgres.PostgresTransactionAdapter
import com.cystg.wallet.infrastructure.security.PasetoAuthorizationAdapter
import com.cystg.wallet.infrastructure.security.PasetoTokenAdapter
import com.cystg.wallet.service.common.ports.PasswordEncoderPort
import com.cystg.wallet.service.common.ports.TokenStorePort
import com.cystg.wallet.service.common.ports.UserPersistencePort
import com.cystg.wallet.service.user.CreateUserUseCase
import com.cystg.wallet.service.auth.LoginUseCase
import com.cystg.wallet.service.auth.LogoutUseCase
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(PasetoProperties::class)
class ApplicationConfig {

    @Bean
    fun transactionPersistencePort(): TransactionPersistencePort = PostgresTransactionAdapter()

    @Bean
    fun ledgerPersistencePort(): LedgerPersistencePort = CassandraLedgerAdapter()

    @Bean
    fun pasetoTokenPort(pasetoProperties: PasetoProperties): PasetoTokenPort =
        PasetoTokenAdapter(
            secretKeyBase64 = pasetoProperties.secretKey,
            tokenTtlSeconds = pasetoProperties.tokenTtlSeconds,
        )

    @Bean
    fun authorizationPort(pasetoTokenPort: PasetoTokenPort): AuthorizationPort =
        PasetoAuthorizationAdapter(pasetoTokenPort)

    @Bean
    fun recordTransactionUseCase(
        transactionPersistencePort: TransactionPersistencePort,
        ledgerPersistencePort: LedgerPersistencePort,
    ): RecordTransactionUseCase = RecordTransactionUseCase(
        transactionPersistencePort = transactionPersistencePort,
        ledgerPersistencePort = ledgerPersistencePort,
    )

    @Bean
    fun createUserUseCase(
        userPersistencePort: UserPersistencePort,
        passwordEncoderPort: PasswordEncoderPort,
    ): CreateUserUseCase = CreateUserUseCase(userPersistencePort, passwordEncoderPort)

    @Bean
    fun loginUseCase(
        userPersistencePort: UserPersistencePort,
        passwordEncoderPort: PasswordEncoderPort,
        pasetoTokenPort: PasetoTokenPort,
    ): LoginUseCase = LoginUseCase(userPersistencePort, passwordEncoderPort, pasetoTokenPort)

    @Bean
    fun logoutUseCase(
        tokenStorePort: TokenStorePort,
    ): LogoutUseCase = LogoutUseCase(tokenStorePort)
}
