package com.cystg.wallet.api.user

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users")
class UserController {
    
    @PostMapping
    fun createUser(@RequestBody request: CreateUserRequest) {
        // Implementation to be added
    }
}