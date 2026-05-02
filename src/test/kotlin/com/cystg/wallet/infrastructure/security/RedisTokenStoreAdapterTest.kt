package com.cystg.wallet.infrastructure.security

import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.core.ReactiveValueOperations
import reactor.core.publisher.Mono
import java.time.Duration

@ExtendWith(MockitoExtension::class)
class RedisTokenStoreAdapterTest {

    @Mock
    private lateinit var redisTemplate: ReactiveRedisTemplate<String, String>

    @Mock
    private lateinit var valueOperations: ReactiveValueOperations<String, String>

    @Test
    fun `store should save token with expiry`(): Unit = runBlocking {
        `when`(redisTemplate.opsForValue()).thenReturn(valueOperations)
        `when`(valueOperations.set(anyString(), anyString(), any(Duration::class.java)))
            .thenReturn(Mono.just(true))

        val adapter = RedisTokenStoreAdapter(redisTemplate)
        adapter.store("test-token", 3600)

        verify(valueOperations).set("test-token", "valid", Duration.ofSeconds(3600))
    }

    @Test
    fun `exists should return true if token is found`() = runBlocking {
        `when`(redisTemplate.hasKey("test-token")).thenReturn(Mono.just(true))

        val adapter = RedisTokenStoreAdapter(redisTemplate)
        val exists = adapter.exists("test-token")

        assertTrue(exists)
    }

    @Test
    fun `delete should remove token`(): Unit = runBlocking {
        `when`(redisTemplate.delete("test-token")).thenReturn(Mono.just(1L))

        val adapter = RedisTokenStoreAdapter(redisTemplate)
        adapter.delete("test-token")

        verify(redisTemplate).delete("test-token")
    }
}
