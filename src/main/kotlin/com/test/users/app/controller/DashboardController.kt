package com.test.users.app.controller

import com.test.users.app.dto.UserDto
import com.test.users.app.service.UserService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class DashboardController(
    private val userService: UserService
) {

    @GetMapping
    fun searchUsers(
        @RequestParam(required = false, defaultValue = "") search: String,
        pageable: Pageable
    ): Page<UserDto> {
        return userService.searchUsers(search, pageable)
    }
}