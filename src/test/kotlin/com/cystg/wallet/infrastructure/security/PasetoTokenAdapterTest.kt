package com.cystg.wallet.infrastructure.security

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.security.SecureRandom
import java.util.Base64
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class PasetoTokenAdapterTest {

    // Generate a random 32-byte key, base64-encoded (same format as application.properties)
    private val secretKeyBase64: String = Base64.getEncoder().encodeToString(
        ByteArray(32).also { SecureRandom().nextBytes(it) }
    )

    private val adapter = PasetoTokenAdapter(
        secretKeyBase64 = secretKeyBase64,
        tokenTtlSeconds = 3600L,
    )

    @Test
    fun `issue returns a valid v4 local token`() {
        val token = adapter.issue(subject = "user-123", roles = setOf("USER"))

        assertNotNull(token)
        assertTrue(token.startsWith("v4.local."), "Token should start with v4.local. prefix, got: $token")
    }

    @Test
    fun `verify round-trip returns correct subject and roles`() {
        val subject = "user-abc"
        val roles = setOf("USER", "ADMIN")

        val token = adapter.issue(subject = subject, roles = roles)
        val claims = adapter.verify(token)

        assertEquals(subject, claims.subject)
        assertEquals(roles, claims.roles)
    }

    @Test
    fun `verify with tampered token throws exception`() {
        val token = adapter.issue(subject = "user-x", roles = emptySet())
        val tampered = token.dropLast(4) + "XXXX"

        assertThrows<Exception> {
            adapter.verify(tampered)
        }
    }

    @Test
    fun `verify expired token throws exception`() {
        val expiredAdapter = PasetoTokenAdapter(
            secretKeyBase64 = secretKeyBase64,
            tokenTtlSeconds = -1L, // already expired
        )

        val token = expiredAdapter.issue(subject = "user-y", roles = emptySet())

        assertThrows<Exception> {
            adapter.verify(token) // verified by non-expired adapter but exp is in the past
        }
    }

    @Test
    fun `issue with empty roles produces token verifiable with empty roles`() {
        val token = adapter.issue(subject = "user-no-roles")
        val claims = adapter.verify(token)

        assertTrue(claims.roles.isEmpty())
    }
}
