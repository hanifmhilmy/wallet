package com.cystg.wallet.infrastructure.persistence.postgres

import com.cystg.wallet.service.common.ports.User
import com.cystg.wallet.service.common.ports.UserPersistencePort
import com.cystg.wallet.service.common.ports.UserStatus
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class PostgresUserAdapter(
    private val userRepository: UserRepository
) : UserPersistencePort {

    override suspend fun save(user: User) {
        val entity = UserEntity(
            id = user.id,
            email = user.email,
            username = user.username,
            password = user.passwordHash,
            status = user.status.name
        )
        userRepository.save(entity)
    }

    override suspend fun findById(id: UUID): User? {
        val entity = userRepository.findById(id) ?: return null
        return mapToDomain(entity)
    }

    override suspend fun findByEmail(email: String): User? {
        val entity = userRepository.findByEmail(email) ?: return null
        return mapToDomain(entity)
    }

    override suspend fun deleteById(id: UUID) {
        userRepository.deleteById(id)
    }

    private fun mapToDomain(entity: UserEntity): User {
        return User(
            id = entity.id,
            email = entity.email,
            username = entity.username,
            passwordHash = entity.password,
            status = UserStatus.valueOf(entity.status)
        )
    }
}
