package com.cystg.wallet.domain.account

import com.cystg.wallet.domain.shared.Identifier
import java.util.UUID

data class Account(
    val id: Identifier,
    val userId: UUID?,
    val name: String,
    val type: AccountType,
    val currency: String,
    val isSystem: Boolean,
    val version: Long = 0,
)