package com.test.users.app.repository

import com.test.users.app.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(username: String): User?
    fun findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
        name: String,
        email: String,
        pageable: Pageable
    ): Page<User>
}