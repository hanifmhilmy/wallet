package com.cystg.wallet.infrastructure.security

import com.cystg.wallet.service.common.ports.PasswordEncoderPort
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class BcryptPasswordEncoderAdapter(
    private val passwordEncoder: PasswordEncoder
) : PasswordEncoderPort {

    override fun encode(raw: String): String? {
        return passwordEncoder.encode(raw)
    }

    override fun matches(raw: String, encoded: String): Boolean {
        return passwordEncoder.matches(raw, encoded)
    }
}
