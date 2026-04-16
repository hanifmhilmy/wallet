package com.cystg.wallet.domain.shared

@JvmInline
value class EmailAddress private constructor(val value: String) {
    companion object {
        private val pattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()

        fun of(raw: String): Result<EmailAddress> =
            if (raw.matches(pattern)) Result.success(EmailAddress(raw))
            else Result.failure(IllegalArgumentException("Invalid email address"))
    }
}
