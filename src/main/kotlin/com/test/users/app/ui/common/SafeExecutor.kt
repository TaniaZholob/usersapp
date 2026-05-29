package com.test.users.app.ui.common

import com.test.users.app.exception.RoleNotFoundException
import com.test.users.app.exception.UserAlreadyExistsException
import com.test.users.app.exception.UserNotFoundException
import com.vaadin.flow.component.notification.Notification
import jakarta.validation.ConstraintViolationException
import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException

object SafeExecutor {

    private val logger = LoggerFactory.getLogger(SafeExecutor::class.java)

    fun run(action: () -> Unit) {
        try {
            action()
        } catch (e: Exception) {
            handleException(e)
        }
    }

    fun <T> get(
        fallback: T,
        action: () -> T
    ): T {
        return try {
            action()
        } catch (e: Exception) {
            handleException(e)
            fallback
        }
    }

    fun <T> nullable(action: () -> T): T? {
        return try {
            action()
        } catch (e: Exception) {
            handleException(e)
            null
        }
    }

    private fun handleException(e: Exception) {
        when (e) {

            is ConstraintViolationException -> {
                showNotification(e.constraintViolations.firstOrNull()?.message ?: "Validation failed")
            }

            is UserAlreadyExistsException -> {
                showNotification("A user with this email already exists")
            }

            is UserNotFoundException -> {
                showNotification("User not found")
            }

            is RoleNotFoundException -> {
                showNotification("Invalid role selected")
            }

            is DataIntegrityViolationException -> {
                logger.error("Database error", e)
                showNotification("Database error occurred. Please try again.")
            }

            is IllegalArgumentException -> {
                logger.error("Invalid argument", e)
                showNotification("Invalid input provided")
            }

            else -> {
                logger.error("Unexpected error", e)
                showNotification("An unexpected error occurred")
            }
        }
    }

    private fun showNotification(message: String) {
        Notification.show(message, 5000, Notification.Position.MIDDLE)
    }
}