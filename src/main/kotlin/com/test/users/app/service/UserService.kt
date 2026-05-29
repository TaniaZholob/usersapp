package com.test.users.app.service

import com.test.users.app.dto.UserDto
import com.test.users.app.entity.RoleName
import com.test.users.app.entity.User
import com.test.users.app.exception.RoleNotFoundException
import com.test.users.app.exception.UserAlreadyExistsException
import com.test.users.app.exception.UserNotFoundException
import com.test.users.app.repository.RoleRepository
import com.test.users.app.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class UserService(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val passwordEncoder: PasswordEncoder
) {

    fun searchUsers(
        search: String,
        pageable: Pageable
    ): Page<UserDto> {

        return if (search.isBlank()) {

            userRepository.findAll(pageable)
                .map(::mapToDto)

        } else {

            userRepository
                .findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                    search,
                    search,
                    pageable
                )
                .map(::mapToDto)
        }
    }

    @Transactional
    fun createUser(
        name: String,
        email: String,
        password: String,
        role: String
    ) {

        val existingUser = userRepository.findByEmail(email)

        if (existingUser != null) {
            throw UserAlreadyExistsException(email)
        }
        val roleValue = RoleName.valueOf(role.uppercase())
        val userRole = roleRepository.findByName(roleValue) ?: throw RoleNotFoundException(roleValue.name)

        val now = LocalDateTime.now()

        val user = User(
            name = name,
            email = email,
            password = passwordEncoder.encode(password),
            role = userRole,
            createdAt = now,
            updatedAt = now
        )

        userRepository.save(user)
    }

    @Transactional
    fun updateUser(
        id: Long,
        name: String,
        email: String,
        role: String
    ) {

        val user = userRepository.findById(id)
            .orElseThrow {
                UserNotFoundException(id)
            }

        val existingUser = userRepository.findByEmail(email)

        if (existingUser != null && existingUser.id != id) {
            throw UserAlreadyExistsException(email)
        }
        val roleValue = RoleName.valueOf(role.uppercase())
        val userRole = roleRepository.findByName(roleValue) ?: throw RoleNotFoundException(roleValue.name)

        user.name = name
        user.email = email
        user.role = userRole

        userRepository.save(user)
    }

    @Transactional
    fun deleteUser(id: Long) {

        val user = userRepository.findById(id)
            .orElseThrow {
                UserNotFoundException(id)
            }

        userRepository.delete(user)
    }

    private fun mapToDto(user: User): UserDto {

        return UserDto(
            id = user.id,
            name = user.name,
            email = user.email,
            role = user.role.name.name,
            createdAt = user.createdAt ?: LocalDateTime.now(),
            updatedAt = user.updatedAt ?: LocalDateTime.now()
        )
    }
}