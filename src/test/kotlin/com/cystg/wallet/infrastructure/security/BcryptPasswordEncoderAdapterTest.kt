package com.cystg.wallet.infrastructure.security

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import org.springframework.security.crypto.password.PasswordEncoder

class BcryptPasswordEncoderAdapterTest {

    private val passwordEncoder = mock<PasswordEncoder>()
    private val adapter = BcryptPasswordEncoderAdapter(passwordEncoder)

    @Test
    fun `encode should delegate to passwordEncoder`() {
        val raw = "my-password"
        val encoded = "encoded-password"
        whenever(passwordEncoder.encode(raw)).thenReturn(encoded)

        val result = adapter.encode(raw)

        assertEquals(encoded, result)
    }

    @Test
    fun `matches should delegate to passwordEncoder`() {
        val raw = "my-password"
        val encoded = "encoded-password"
        whenever(passwordEncoder.matches(raw, encoded)).thenReturn(true)

        val result = adapter.matches(raw, encoded)

        assertTrue(result)
    }
}
