package com.cystg.wallet.api.user

import com.cystg.wallet.service.user.CreateUserUseCase
import com.cystg.wallet.domain.shared.DomainError
import arrow.core.left
import arrow.core.right
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest
import com.cystg.wallet.service.common.ports.PasetoTokenPort
import com.cystg.wallet.service.common.ports.TokenStorePort
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.reactive.server.WebTestClient
import java.util.UUID

@WebFluxTest(UserController::class)
class UserControllerTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @MockitoBean
    private lateinit var createUserUseCase: CreateUserUseCase

    @MockitoBean
    private lateinit var pasetoTokenPort: PasetoTokenPort

    @MockitoBean
    private lateinit var tokenStorePort: TokenStorePort

    @Test
    fun `should return 201 created when user is registered`() = runBlocking {
        val request = CreateUserRequest("test@example.com", "password123", "testuser")
        val userId = UUID.randomUUID()
        
        `when`(createUserUseCase.createUser(CreateUserUseCase.CreateUserCommand(
            email = request.email,
            password = request.password,
            username = request.username
        ))).thenReturn(CreateUserUseCase.CreateUserResult(userId).right())

        webTestClient.post()
            .uri("/api/v1/users")
            .bodyValue(request)
            .exchange()
            .expectStatus().isCreated
            .expectBody()
            .jsonPath("$.id").isEqualTo(userId.toString())
            .jsonPath("$.email").isEqualTo(request.email)
            .jsonPath("$.username").isEqualTo(request.username)
        Unit
    }

    @Test
    fun `should return 400 bad request when email is duplicate`() = runBlocking {
        val request = CreateUserRequest("test@example.com", "password123", "testuser")
        
        `when`(createUserUseCase.createUser(CreateUserUseCase.CreateUserCommand(
            email = request.email,
            password = request.password,
            username = request.username
        ))).thenReturn(DomainError("Email already registered").left())

        webTestClient.post()
            .uri("/api/v1/users")
            .bodyValue(request)
            .exchange()
            .expectStatus().isBadRequest
        Unit
    }
}
