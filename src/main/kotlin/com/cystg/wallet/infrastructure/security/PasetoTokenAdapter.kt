package com.cystg.wallet.infrastructure.security

import com.cystg.wallet.application.common.ports.AuthorizationPort
import com.cystg.wallet.application.common.ports.PasetoTokenPort
import com.cystg.wallet.application.common.ports.TokenClaims

class PasetoTokenAdapter(
    private val authorizationPort: AuthorizationPort,
) : PasetoTokenPort {
    override fun issue(subject: String, roles: Set<String>): String =
        listOf("paseto", subject, roles.joinToString(",")).joinToString(".")

    override fun verify(token: String): TokenClaims = authorizationPort.authorize(token)
}
