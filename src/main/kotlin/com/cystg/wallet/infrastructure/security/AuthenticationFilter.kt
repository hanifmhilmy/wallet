package com.cystg.wallet.infrastructure.security

import com.cystg.wallet.service.common.errors.UnauthorizedException
import com.cystg.wallet.service.common.ports.PasetoTokenPort
import com.cystg.wallet.service.common.ports.TokenStorePort
import kotlinx.coroutines.reactor.mono
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Component
class AuthenticationFilter(
    private val pasetoTokenPort: PasetoTokenPort,
    private val tokenStorePort: TokenStorePort
) : WebFilter {

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val authHeader = exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION)
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return chain.filter(exchange)
        }

        val token = authHeader.substring(7)
        
        return mono {
            val isBlacklisted = tokenStorePort.exists(token)
            if (isBlacklisted) {
                throw UnauthorizedException("Token has been revoked")
            }
            
            try {
                val claims = pasetoTokenPort.verify(token)
                val authorities = claims.roles.map { SimpleGrantedAuthority(it) }
                val auth = UsernamePasswordAuthenticationToken(claims.subject, null, authorities)
                SecurityContextImpl(auth)
            } catch (e: Exception) {
                throw UnauthorizedException("Invalid token")
            }
        }.flatMap { securityContext ->
            chain.filter(exchange)
                .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)))
        }
    }
}
