package com.cystg.wallet.domain.shared

import java.util.UUID

@JvmInline
value class Identifier(val value: UUID) {
    companion object {
        fun new(): Identifier = Identifier(UUID.randomUUID())
    }
}
