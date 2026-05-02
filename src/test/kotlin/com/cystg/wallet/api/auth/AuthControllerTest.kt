package com.cystg.wallet.api.auth

import com.cystg.wallet.service.auth.LoginUseCase
import com.cystg.wallet.service.auth.LogoutUseCase
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

@WebFluxTest(AuthController::class)
class AuthControllerTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @MockitoBean
    private lateinit var loginUseCase: LoginUseCase
    
    @MockitoBean
    private lateinit var logoutUseCase: LogoutUseCase

    @MockitoBean
    private lateinit var pasetoTokenPort: PasetoTokenPort

    @MockitoBean
    private lateinit var tokenStorePort: TokenStorePort

    @Test
    fun `should return token on successful login`() = runBlocking {
        val request = LoginRequest("test@example.com", "password123")
        
        `when`(loginUseCase.login(LoginUseCase.LoginCommand(request.email, request.password)))
            .thenReturn(LoginUseCase.LoginResult("valid-token").right())

        webTestClient.post()
            .uri("/api/v1/auth/login")
            .bodyValue(request)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.token").isEqualTo("valid-token")
        Unit
    }

    @Test
    fun `should return 401 on invalid credentials`() = runBlocking {
        val request = LoginRequest("test@example.com", "wrong")
        
        `when`(loginUseCase.login(LoginUseCase.LoginCommand(request.email, request.password)))
            .thenReturn(DomainError("Invalid credentials").left())

        webTestClient.post()
            .uri("/api/v1/auth/login")
            .bodyValue(request)
            .exchange()
            .expectStatus().isUnauthorized
        Unit
    }

    @Test
    fun `should return 200 on successful logout`() = runBlocking {
        val request = LogoutRequest("valid-token")
        
        `when`(logoutUseCase.logout(LogoutUseCase.LogoutCommand(request.token)))
            .thenReturn(Unit.right())

        webTestClient.post()
            .uri("/api/v1/auth/logout")
            .bodyValue(request)
            .exchange()
            .expectStatus().isOk
        Unit
    }
}
