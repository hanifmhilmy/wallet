package com.cystg.wallet.application.common.ports

import java.util.UUID

interface AuthorizationPort {
    fun authorize(token: String, requiredRole: String? = null): TokenClaims
}
