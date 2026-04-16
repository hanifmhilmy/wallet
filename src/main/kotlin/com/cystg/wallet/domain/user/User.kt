package com.cystg.wallet.domain.user

import arrow.core.Either
import com.cystg.wallet.domain.shared.DomainError
import com.cystg.wallet.domain.shared.EmailAddress
import com.cystg.wallet.domain.shared.Identifier

data class User(
    val id: Identifier,
    val email: EmailAddress,
    val username: String,
    val passwordHash: String,
    val status: UserStatus = UserStatus.ACTIVE,
    val version: Long = 0,
) {
    companion object {
        fun register(rawEmail: String, username: String, passwordHash: String): Either<DomainError, User> {
            if (username.isBlank()) return Either.Left(DomainError("Username must not be blank"))
            if (passwordHash.isBlank()) return Either.Left(DomainError("Password hash must not be blank"))

            return EmailAddress.of(rawEmail).fold(
                onSuccess = {
                    Either.Right(
                        User(
                            id = Identifier.new(),
                            email = it,
                            username = username,
                            passwordHash = passwordHash,
                        )
                    )
                },
                onFailure = { Either.Left(DomainError(it.message ?: "Invalid email address")) },
            )
        }
    }
}
