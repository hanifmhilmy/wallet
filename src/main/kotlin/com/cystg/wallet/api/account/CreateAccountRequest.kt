package com.cystg.wallet.api.account

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateAccountRequest(
    @field:NotBlank
    @field:Size(min = 3, max = 100)
    val name: String,
    
    val currency: String = "IDR"
)