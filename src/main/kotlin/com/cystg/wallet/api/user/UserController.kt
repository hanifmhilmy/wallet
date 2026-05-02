package com.cystg.wallet.api.user

import com.cystg.wallet.service.common.errors.BadRequestException
import com.cystg.wallet.service.user.CreateUserUseCase
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val createUserUseCase: CreateUserUseCase
) {
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun createUser(@Valid @RequestBody request: CreateUserRequest): UserResponse {
        val command = CreateUserUseCase.CreateUserCommand(
            email = request.email,
            password = request.password,
            username = request.username
        )
        
        val result = createUserUseCase.createUser(command)
        
        return result.fold(
            ifLeft = { throw BadRequestException(it.message) },
            ifRight = { 
                UserResponse(
                    id = it.userId.toString(),
                    email = request.email,
                    username = request.username
                )
            }
        )
    }
}