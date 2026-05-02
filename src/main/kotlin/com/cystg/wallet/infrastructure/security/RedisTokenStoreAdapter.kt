package com.cystg.wallet.infrastructure.security

import com.cystg.wallet.service.common.ports.TokenStorePort
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class RedisTokenStoreAdapter(
    private val redisTemplate: ReactiveRedisTemplate<String, String>
) : TokenStorePort {

    override suspend fun store(token: String, ttlSeconds: Long) {
        redisTemplate.opsForValue()
            .set(token, "valid", Duration.ofSeconds(ttlSeconds))
            .awaitSingleOrNull()
    }

    override suspend fun exists(token: String): Boolean {
        return redisTemplate.hasKey(token).awaitSingleOrNull() ?: false
    }

    override suspend fun delete(token: String) {
        redisTemplate.delete(token).awaitSingleOrNull()
    }
}
