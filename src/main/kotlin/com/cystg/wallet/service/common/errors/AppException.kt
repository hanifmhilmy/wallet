package com.cystg.wallet.service.common.errors

import org.springframework.http.HttpStatus

abstract class AppException(
    override val message: String,
    val status: HttpStatus,
    val code: String
) : RuntimeException(message)

class UnauthorizedException(message: String = "Unauthorized") : 
    AppException(message, HttpStatus.UNAUTHORIZED, "UNAUTHORIZED")

class NotFoundException(message: String) : 
    AppException(message, HttpStatus.NOT_FOUND, "NOT_FOUND")

class ConflictException(message: String) : 
    AppException(message, HttpStatus.CONFLICT, "CONFLICT")

class BadRequestException(message: String) : 
    AppException(message, HttpStatus.BAD_REQUEST, "BAD_REQUEST")
