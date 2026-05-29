package com.test.users.app.service

import com.test.users.app.dto.UserDto
import com.test.users.app.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
) {

    fun getAllUsers(): List<UserDto> {
        return userRepository.findAll()
            .map { user ->
                UserDto(
                    id = user.id,
                    name = user.name,
                    email = user.email,
                    role = user.role.name.name,
                    createdAt = user.createdAt!!,
                    updatedAt = user.updatedAt!!
                )
            }
    }
}