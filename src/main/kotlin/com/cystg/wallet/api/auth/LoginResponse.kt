package com.cystg.wallet.api.auth

import jakarta.validation.constraints.NotBlank

data class LoginResponse(
    @field:NotBlank
    val token: String
)