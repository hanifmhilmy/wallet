package com.cystg.wallet.application.common.ports

interface AuthorizationPort {
    fun authorize(token: String, requiredRole: String? = null): TokenClaims
}
