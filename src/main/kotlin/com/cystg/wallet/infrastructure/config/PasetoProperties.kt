package com.cystg.wallet.infrastructure.config

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * Binds `paseto.*` from application.properties.
 *
 * secretKey: Base64-encoded 32-byte symmetric key used for v4.local signing.
 * tokenTtlSeconds: How long an issued token is valid before it is considered expired.
 */

@ConfigurationProperties(prefix = "paseto")
data class PasetoProperties(
    val secretKey: String,
    val tokenTtlSeconds: Long = 3600L,
)
