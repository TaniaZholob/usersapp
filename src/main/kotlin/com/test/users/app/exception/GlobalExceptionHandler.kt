package com.test.users.app.exception

import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(UserAlreadyExistsException::class)
    fun handleUserAlreadyExists(
        ex: UserAlreadyExistsException
    ): ResponseEntity<ApiError> {

        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(ApiError(ex.message ?: "User already exists"))
    }

    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFound(
        ex: UserNotFoundException
    ): ResponseEntity<ApiError> {

        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ApiError(ex.message ?: "User not found"))
    }

    @ExceptionHandler(RoleNotFoundException::class)
    fun handleRoleNotFound(
        ex: RoleNotFoundException
    ): ResponseEntity<ApiError> {

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiError(ex.message ?: "Role not found"))
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDatabaseError(
        ex: DataIntegrityViolationException
    ): ResponseEntity<ApiError> {

        logger.error("Database error", ex)

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiError("Database error occurred"))
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericError(
        ex: Exception
    ): ResponseEntity<ApiError> {
        logger.error("Unexpected error", ex)
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiError("Unexpected error occurred"))
    }
}

data class ApiError(
    val message: String
)