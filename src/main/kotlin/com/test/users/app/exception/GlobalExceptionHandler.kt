package com.test.users.app.exception

import com.vaadin.flow.component.notification.Notification
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException::class)
    fun handleUserAlreadyExists(
        ex: UserAlreadyExistsException
    ) {
        Notification.show(
            ex.message ?: "User already exists"
        )
    }

    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFound(
        ex: UserNotFoundException
    ) {
        Notification.show(
            ex.message ?: "User not found"
        )
    }

    @ExceptionHandler(RoleNotFoundException::class)
    fun handleRoleNotFound(
        ex: RoleNotFoundException
    ) {
        Notification.show(
            ex.message ?: "Role not found"
        )
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDatabaseError() {
        Notification.show(
            "Database error occurred"
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericError(
        ex: Exception
    ) {
        ex.printStackTrace()

        Notification.show(
            "Unexpected error occurred"
        )
    }
}