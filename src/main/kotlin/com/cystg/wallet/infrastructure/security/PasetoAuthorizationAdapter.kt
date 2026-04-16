package com.cystg.wallet.infrastructure.security

import com.cystg.wallet.application.common.ports.AuthorizationPort
import com.cystg.wallet.application.common.ports.TokenClaims

class PasetoAuthorizationAdapter : AuthorizationPort {
    override fun authorize(token: String, requiredRole: String?): TokenClaims {
        val claims = token.let { raw ->
            val parts = raw.split('.')
            require(parts.size >= 2) { "Invalid token" }
            TokenClaims(
                subject = parts[1],
                roles = parts.getOrNull(2)
                    ?.split(',')
                    ?.filter { it.isNotBlank() }
                    ?.toSet()
                    ?: emptySet(),
            )
        }

        if (requiredRole != null && requiredRole !in claims.roles) {
            error("Forbidden")
        }

        return claims
    }
}
