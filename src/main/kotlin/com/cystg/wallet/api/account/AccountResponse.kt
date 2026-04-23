package com.cystg.wallet.api.account

import jakarta.validation.constraints.NotBlank

data class AccountResponse(
    @field:NotBlank
    val id: String,
    
    @field:NotBlank
    val name: String,
    
    val currency: String = "IDR"
)