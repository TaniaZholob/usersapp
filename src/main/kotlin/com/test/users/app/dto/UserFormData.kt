package com.test.users.app.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UserFormData(

    @field:NotBlank(message = "Name is required")
    @field:Size(
        min = 2,
        max = 100,
        message = "Name must be between 2 and 100 characters"
    )
    val name: String,

    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Invalid email format")
    val email: String,

    @field:Size(
        min = 8,
        message = "Password must be at least 8 characters"
    )
    val password: String?,

    @field:NotBlank(message = "Role is required")
    val role: String
)