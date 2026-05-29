package com.test.users.app.dto

data class UserFormData(

    val name: String,

    val email: String,

    val password: String?,

    val role: String
)