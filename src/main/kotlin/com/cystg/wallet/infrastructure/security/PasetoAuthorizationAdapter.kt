package com.cystg.wallet.infrastructure.security

import com.cystg.wallet.service.common.ports.AuthorizationPort
import com.cystg.wallet.service.common.ports.PasetoTokenPort
import com.cystg.wallet.service.common.ports.TokenClaims

/**
 * Delegates token authorization to [PasetoTokenPort].
 * Verifies the token cryptographically, then optionally asserts that
 * the required role is present in the token's claims.
 */
class PasetoAuthorizationAdapter(
    private val pasetoTokenPort: PasetoTokenPort,
) : AuthorizationPort {

    override fun authorize(token: String, requiredRole: String?): TokenClaims {
        val claims = pasetoTokenPort.verify(token)

        if (requiredRole != null && requiredRole !in claims.roles) {
            error("Forbidden: required role '$requiredRole' not present")
        }

        return claims
    }
}
