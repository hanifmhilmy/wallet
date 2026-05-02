package com.cystg.wallet.infrastructure.persistence.postgres

import com.cystg.wallet.service.common.ports.User
import com.cystg.wallet.service.common.ports.UserStatus
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import java.util.UUID

class PostgresUserAdapterTest {

    private val userRepository = mock<UserRepository>()
    private val adapter = PostgresUserAdapter(userRepository)

    @Test
    fun `save should map and save user entity`(): Unit = runBlocking {
        val id = UUID.randomUUID()
        val user = User(id, "test@example.com", "user", "hash", UserStatus.ACTIVE)
        
        whenever(userRepository.save(any())).thenAnswer { it.arguments[0] as UserEntity }

        adapter.save(user)
        
        verify(userRepository).save(any())
    }

    @Test
    fun `findById should return null if not found`() = runBlocking {
        whenever(userRepository.findById(any<UUID>())).thenReturn(null)
        
        val result = adapter.findById(UUID.randomUUID())
        assertNull(result)
    }

    @Test
    fun `findById should return mapped user if found`() = runBlocking {
        val id = UUID.randomUUID()
        val entity = UserEntity(id, "test@example.com", "user", "hash", "ACTIVE")
        whenever(userRepository.findById(id)).thenReturn(entity)
        
        val result = adapter.findById(id)
        assertNotNull(result)
        assertEquals("test@example.com", result?.email)
    }

    @Test
    fun `findByEmail should return null if not found`() = runBlocking {
        whenever(userRepository.findByEmail("test@example.com")).thenReturn(null)
        
        val result = adapter.findByEmail("test@example.com")
        assertNull(result)
    }

    @Test
    fun `deleteById should call repository deleteById`() = runBlocking {
        val id = UUID.randomUUID()
        
        whenever(userRepository.deleteById(id)).thenReturn(Unit)
        
        adapter.deleteById(id)
        verify(userRepository).deleteById(id)
    }
}
