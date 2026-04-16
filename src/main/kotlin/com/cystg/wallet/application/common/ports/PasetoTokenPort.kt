package com.cystg.wallet.application.common.ports

interface PasetoTokenPort {
    fun issue(subject: String, roles: Set<String> = emptySet()): String
    fun verify(token: String): TokenClaims
}

data class TokenClaims(
    val subject: String,
    val roles: Set<String> = emptySet(),
)
