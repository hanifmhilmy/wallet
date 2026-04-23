package com.cystg.wallet.api.user

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class UserResponse(
    @field:NotBlank
    val id: String,
    
    @field:Email
    @field:NotBlank
    val email: String,
    
    @field:NotBlank
    val username: String
)