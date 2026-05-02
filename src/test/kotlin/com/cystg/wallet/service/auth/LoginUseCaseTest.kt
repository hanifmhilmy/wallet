package com.cystg.wallet.service.auth

import com.cystg.wallet.service.common.ports.PasswordEncoderPort
import com.cystg.wallet.service.common.ports.PasetoTokenPort
import com.cystg.wallet.service.common.ports.User
import com.cystg.wallet.service.common.ports.UserPersistencePort
import com.cystg.wallet.service.common.ports.UserStatus
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import java.util.UUID

class LoginUseCaseTest {

    private val userPersistencePort = mock<UserPersistencePort>()
    private val passwordEncoderPort = mock<PasswordEncoderPort>()
    private val pasetoTokenPort = mock<PasetoTokenPort>()
    private val useCase = LoginUseCase(userPersistencePort, passwordEncoderPort, pasetoTokenPort)

    @Test
    fun `should login successfully and return token`() = runBlocking {
        val command = LoginUseCase.LoginCommand("test@example.com", "password123")
        val userId = UUID.randomUUID()
        val user = User(userId, "test@example.com", "testuser", "hashed-password", UserStatus.ACTIVE)
        
        whenever(userPersistencePort.findByEmail("test@example.com")).thenReturn(user)
        whenever(passwordEncoderPort.matches("password123", "hashed-password")).thenReturn(true)
        whenever(pasetoTokenPort.issue(any(), any())).thenReturn("paseto-token")

        val result = useCase.login(command)

        assertTrue(result.isRight())
        result.onRight {
            assertEquals("paseto-token", it.token)
        }
        Unit
    }

    @Test
    fun `should return error if user not found`() = runBlocking {
        val command = LoginUseCase.LoginCommand("test@example.com", "password123")
        
        whenever(userPersistencePort.findByEmail("test@example.com")).thenReturn(null)

        val result = useCase.login(command)

        assertTrue(result.isLeft())
        result.onLeft {
            assertEquals("Invalid credentials", it.message)
            verify(pasetoTokenPort, never()).issue(any(), any())
        }
        Unit
    }

    @Test
    fun `should return error if password does not match`() = runBlocking {
        val command = LoginUseCase.LoginCommand("test@example.com", "wrongpassword")
        val userId = UUID.randomUUID()
        val user = User(userId, "test@example.com", "testuser", "hashed-password", UserStatus.ACTIVE)
        
        whenever(userPersistencePort.findByEmail("test@example.com")).thenReturn(user)
        whenever(passwordEncoderPort.matches("wrongpassword", "hashed-password")).thenReturn(false)

        val result = useCase.login(command)

        assertTrue(result.isLeft())
        result.onLeft {
            assertEquals("Invalid credentials", it.message)
            verify(pasetoTokenPort, never()).issue(any(), any())
        }
        Unit
    }
}
