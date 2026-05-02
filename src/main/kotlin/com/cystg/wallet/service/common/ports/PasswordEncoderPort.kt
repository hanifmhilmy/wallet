package com.cystg.wallet.service.common.ports

interface PasswordEncoderPort {
    fun encode(raw: String): String?
    fun matches(raw: String, encoded: String): Boolean
}
