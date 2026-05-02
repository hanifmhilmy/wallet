package com.cystg.wallet.infrastructure.security

import com.cystg.wallet.service.common.ports.PasetoTokenPort
import com.cystg.wallet.service.common.ports.TokenClaims
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.github.oshai.kotlinlogging.KotlinLogging
import org.paseto4j.commons.SecretKey
import org.paseto4j.commons.Version
import org.paseto4j.version4.Paseto
import java.time.Instant
import java.util.Base64

private val log = KotlinLogging.logger {}

/**
 * Real PASETO v4.local (symmetric encryption) implementation.
 *
 * Token payload schema:
 * {
 *   "sub": "<user-id>",
 *   "roles": ["USER"],
 *   "exp": "<ISO-8601 expiry>"
 * }
 *
 * The secret key must be a Base64-encoded 32-byte value (256-bit) configured via `paseto.secret-key`.
 */
class PasetoTokenAdapter(
    secretKeyBase64: String,
    private val tokenTtlSeconds: Long,
) : PasetoTokenPort {

    private val mapper = jacksonObjectMapper()

    /** paseto4j SecretKey for v4.local — wraps the raw 32-byte key bytes. */
    private val secretKey: SecretKey = SecretKey(
        Base64.getDecoder().decode(secretKeyBase64),
        Version.V4,
    )

    override fun issue(subject: String, roles: Set<String>): String {
        val expiry = Instant.now().plusSeconds(tokenTtlSeconds).toString()
        val payload = mapper.writeValueAsString(
            mapOf(
                "sub" to subject,
                "roles" to roles,
                "exp" to expiry,
            )
        )
        return Paseto.encrypt(secretKey, payload, "")
    }

    override fun verify(token: String): TokenClaims {
        val plaintext = try {
            Paseto.decrypt(secretKey, token, "")
        } catch (e: Exception) {
            log.warn { "PASETO decryption failed: ${e.message}" }
            throw IllegalArgumentException("Invalid or malformed token", e)
        }

        val claims: Map<String, Any> = mapper.readValue(plaintext)

        val exp = claims["exp"] as? String
            ?: throw IllegalArgumentException("Token missing 'exp' claim")

        if (Instant.parse(exp).isBefore(Instant.now())) {
            throw IllegalArgumentException("Token has expired")
        }

        val subject = claims["sub"] as? String
            ?: throw IllegalArgumentException("Token missing 'sub' claim")

        @Suppress("UNCHECKED_CAST")
        val roles = (claims["roles"] as? List<String>)?.toSet() ?: emptySet()

        return TokenClaims(subject = subject, roles = roles)
    }
}
