package com.cystg.wallet.api.auth

import com.cystg.wallet.application.common.ports.PasetoTokenPort
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val pasetoTokenPort: PasetoTokenPort,
) {
    @PostMapping("/token")
    fun issue(@Valid @RequestBody request: IssueTokenRequest): IssueTokenResponse {
        return IssueTokenResponse(
            token = pasetoTokenPort.issue(
                subject = request.subject,
                roles = request.roles.toSet(),
            )
        )
    }
}

data class IssueTokenRequest(
    @field:NotBlank
    val subject: String,
    val roles: List<String> = emptyList(),
)

data class IssueTokenResponse(
    val token: String,
)
