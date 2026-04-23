package com.cystg.wallet.application.common.ports

interface TokenStorePort {
    fun store(token: String, ttlSeconds: Long)
    fun exists(token: String): Boolean
    fun delete(token: String)
}