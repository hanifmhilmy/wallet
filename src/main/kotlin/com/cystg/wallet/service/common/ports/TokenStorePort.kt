package com.cystg.wallet.service.common.ports

interface TokenStorePort {
    suspend fun store(token: String, ttlSeconds: Long)
    suspend fun exists(token: String): Boolean
    suspend fun delete(token: String)
}