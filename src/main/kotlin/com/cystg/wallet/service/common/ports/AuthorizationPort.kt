package com.cystg.wallet.service.common.ports

interface AuthorizationPort {
    fun authorize(token: String, requiredRole: String? = null): TokenClaims
}
