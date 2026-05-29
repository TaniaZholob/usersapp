package com.test.users.app.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.Index
import java.time.LocalDateTime

@Entity
@Table(
    name = "users",
    indexes = [Index(name = "idx_users_name", columnList = "name"), Index(
        name = "idx_users_email",
        columnList = "email"
    ), Index(name = "idx_users_created_at", columnList = "created_at"), Index(
        name = "idx_users_updated_at",
        columnList = "updated_at"
    )]
)
class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
    @Column(nullable = false) var name: String,
    @Column(nullable = false, unique = true) var email: String,
    @Column(nullable = false) var password: String,
    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "role_id", nullable = false) var role: Role,
    @Column(name = "created_at", nullable = false, updatable = false) var createdAt: LocalDateTime? = null,
    @Column(name = "updated_at", nullable = false) var updatedAt: LocalDateTime? = null
)