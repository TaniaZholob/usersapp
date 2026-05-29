package com.test.users.app.repository

import com.test.users.app.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    @EntityGraph(attributePaths = ["role"])
    override fun findAll(pageable: Pageable): Page<User>

    @EntityGraph(attributePaths = ["role"])
    fun findByEmail(username: String): User?

    @EntityGraph(attributePaths = ["role"])
    fun findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
        name: String,
        email: String,
        pageable: Pageable
    ): Page<User>
}