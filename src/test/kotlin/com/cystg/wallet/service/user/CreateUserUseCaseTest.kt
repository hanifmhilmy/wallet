package com.cystg.wallet.service.user

import com.cystg.wallet.service.common.ports.PasswordEncoderPort
import com.cystg.wallet.service.common.ports.User as AppUser
import com.cystg.wallet.service.common.ports.UserPersistencePort
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*

class CreateUserUseCaseTest {

    private val userPersistencePort = mock<UserPersistencePort>()
    private val passwordEncoderPort = mock<PasswordEncoderPort>()
    private val useCase = CreateUserUseCase(userPersistencePort, passwordEncoderPort)

    @Test
    fun `should create user successfully`() = runBlocking {
        val command = CreateUserUseCase.CreateUserCommand("test@example.com", "password123", "testuser")
        
        whenever(userPersistencePort.findByEmail(any())).thenReturn(null)
        whenever(passwordEncoderPort.encode(any())).thenReturn("hashed-password")

        val result = useCase.createUser(command)

        assertTrue(result.isRight())
        verify(userPersistencePort).save(any())
        Unit
    }

    @Test
    fun `should return error if email already exists`() = runBlocking {
        val command = CreateUserUseCase.CreateUserCommand("test@example.com", "password123", "testuser")
        val existingUser = mock<AppUser>()
        
        whenever(userPersistencePort.findByEmail("test@example.com")).thenReturn(existingUser)

        val result = useCase.createUser(command)

        assertTrue(result.isLeft())
        result.onLeft { 
            assertEquals("Email already registered", it.message)
            verify(userPersistencePort, never()).save(any())
        }
        Unit
    }

    @Test
    fun `should return error if username is blank`() = runBlocking {
        val command = CreateUserUseCase.CreateUserCommand("test@example.com", "password123", " ")
        
        val result = useCase.createUser(command)

        assertTrue(result.isLeft())
        result.onLeft { 
            assertEquals("Username must not be blank", it.message)
            verify(userPersistencePort, never()).save(any())
        }
        Unit
    }
}
