package com.cystg.wallet.api.account

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/accounts")
class AccountController {
    
    @PostMapping
    fun createAccount(@RequestBody request: CreateAccountRequest) {
        // Implementation to be added
    }
}