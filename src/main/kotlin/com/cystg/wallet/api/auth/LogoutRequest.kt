package com.cystg.wallet.api.auth

import jakarta.validation.constraints.NotBlank

data class LogoutRequest(
    @field:NotBlank
    val token: String
)