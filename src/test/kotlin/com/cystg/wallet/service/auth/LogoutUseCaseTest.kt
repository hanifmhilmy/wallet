package com.cystg.wallet.service.auth

import com.cystg.wallet.service.common.ports.TokenStorePort
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class LogoutUseCaseTest {

    private val tokenStorePort = mock(TokenStorePort::class.java)
    private val useCase = LogoutUseCase(tokenStorePort)

    @Test
    fun `should logout successfully by deleting token`() = runBlocking {
        val command = LogoutUseCase.LogoutCommand("some-token")
        
        val result = useCase.logout(command)

        assertTrue(result.isRight())
        verify(tokenStorePort).store("some-token", 86400)
    }
}
