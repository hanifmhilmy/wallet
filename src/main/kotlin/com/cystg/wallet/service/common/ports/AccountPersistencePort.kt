package com.cystg.wallet.service.common.ports

import java.util.UUID

interface AccountPersistencePort {
    fun save(account: Account)
    fun findById(id: UUID): Account?
    fun findByUserIdAndName(userId: UUID, name: String): Account?
    fun findByUserId(userId: UUID): List<Account>
}

data class Account(
    val id: UUID,
    val userId: UUID?,
    val name: String,
    val type: AccountType,
    val currency: String,
    val isSystem: Boolean
)

enum class AccountType {
    ASSET,
    INCOME,
    EXPENSE,
    LIABILITY
}