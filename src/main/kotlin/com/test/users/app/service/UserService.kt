package com.test.users.app.service

import com.test.users.app.dto.UserDto
import com.test.users.app.dto.UserFormData
import com.test.users.app.entity.RoleName
import com.test.users.app.entity.User
import com.test.users.app.exception.RoleNotFoundException
import com.test.users.app.exception.UserAlreadyExistsException
import com.test.users.app.exception.UserNotFoundException
import com.test.users.app.repository.RoleRepository
import com.test.users.app.repository.UserRepository
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated

@Validated
@Service
class UserService(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val passwordEncoder: PasswordEncoder
) {

    @Transactional(readOnly = true)
    fun searchUsers(
        search: String,
        pageable: Pageable
    ): Page<UserDto> {

        return if (search.isBlank()) {
            userRepository.findAll(pageable).map(::mapToDto)
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
    fun createUser(@Valid formData: UserFormData) {

        validateEmailUniqueness(email = formData.email)
        val userRole = getRole(formData.role)

        val user = User(
            name = formData.name,
            email = formData.email,
            password = passwordEncoder.encode(formData.password ?: ""),
            role = userRole
        )
        userRepository.save(user)
    }

    @Transactional
    fun updateUser(
        id: Long,
        @Valid formData: UserFormData
    ) {
        val user = userRepository.findById(id)
            .orElseThrow { UserNotFoundException(id) }

        validateEmailUniqueness(email = formData.email, currentUserId = id)

        user.name = formData.name
        user.email = formData.email
        user.role = getRole(formData.role)
    }

    @Transactional
    fun deleteUser(id: Long) {
        val user = userRepository.findById(id)
            .orElseThrow { UserNotFoundException(id) }
        userRepository.delete(user)
    }

    private fun validateEmailUniqueness(
        email: String,
        currentUserId: Long? = null
    ) {
        val existingUser = userRepository.findByEmail(email)
        if (existingUser != null &&
            existingUser.id != currentUserId) {
            throw UserAlreadyExistsException(email)
        }
    }

    private fun getRole(role: String) =
        roleRepository.findByName(
            parseRole(role)
        ) ?: throw RoleNotFoundException(role)

    private fun parseRole(role: String): RoleName {
        return runCatching {
            RoleName.valueOf(role.uppercase())
        }.getOrElse {
            throw RoleNotFoundException(role)
        }
    }

    private fun mapToDto(user: User): UserDto {
        return UserDto(
            id = user.id,
            name = user.name,
            email = user.email,
            role = user.role.name.name,
            createdAt = requireNotNull(user.createdAt),
            updatedAt = requireNotNull(user.updatedAt)
        )
    }
}