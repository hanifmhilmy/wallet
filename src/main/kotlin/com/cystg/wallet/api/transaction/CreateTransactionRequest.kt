package com.cystg.wallet.api.transaction

import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.util.UUID

data class CreateTransactionRequest(
    @field:NotNull
    val userId: UUID,
    
    @field:NotNull
    val walletId: UUID,
    
    @field:NotNull
    val accountId: UUID,
    
    val categoryId: UUID? = null,
    
    @field:NotNull
    val amount: BigDecimal,
    
    val currency: String = "IDR",
    
    val note: String? = null
)