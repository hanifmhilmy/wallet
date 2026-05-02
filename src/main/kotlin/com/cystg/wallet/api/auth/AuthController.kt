package com.cystg.wallet.api.auth

import com.cystg.wallet.service.auth.LoginUseCase
import com.cystg.wallet.service.auth.LogoutUseCase
import com.cystg.wallet.service.common.errors.UnauthorizedException
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val loginUseCase: LoginUseCase,
    private val logoutUseCase: LogoutUseCase,
) {
    @PostMapping("/login")
    suspend fun login(@Valid @RequestBody request: LoginRequest): LoginResponse {
        val result = loginUseCase.login(LoginUseCase.LoginCommand(request.email, request.password))
        
        return result.fold(
            ifLeft = { throw UnauthorizedException(it.message) },
            ifRight = { LoginResponse(it.token) }
        )
    }

    @PostMapping("/logout")
    suspend fun logout(@Valid @RequestBody request: LogoutRequest) {
        val result = logoutUseCase.logout(LogoutUseCase.LogoutCommand(request.token))
        
        result.fold(
            ifLeft = { throw UnauthorizedException(it.message) },
            ifRight = { }
        )
    }
}

