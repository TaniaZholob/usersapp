package com.test.users.app.controller

import com.test.users.app.dto.UserDto
import com.test.users.app.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class DashboardController(
    private val userService: UserService
) {

    @GetMapping
    fun getAllUsers(): List<UserDto> {
        return userService.getAllUsers()
    }
}