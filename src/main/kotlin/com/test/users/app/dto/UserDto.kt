package com.test.users.app.dto

import java.time.LocalDateTime

data class UserDto(
    val id: Long?,
    val name: String,
    val email: String,
    val role: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
