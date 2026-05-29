package com.test.users.app.controller

import com.test.users.app.dto.UserDto
import com.test.users.app.dto.UserFormData
import com.test.users.app.service.UserService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(@Valid @RequestBody formData: UserFormData) {
        userService.createUser(formData)
    }

    @PutMapping("/{id}")
    fun updateUser(
        @PathVariable id: Long,
        @Valid @RequestBody formData: UserFormData
    ) {
        userService.updateUser(id, formData)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteUser(@PathVariable id: Long) {
        userService.deleteUser(id)
    }
}