package com.test.users.app.controller

import com.test.users.app.entity.Role
import com.test.users.app.entity.RoleName
import com.test.users.app.repository.RoleRepository
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/roles")
class RoleController(
    private val roleRepository: RoleRepository
) {

    @GetMapping
    fun getAllRoles(): List<Role> {
        return roleRepository.findAll()
    }

    @GetMapping("/{name}")
    fun getRoleByName(@PathVariable name: String): Role? {
        return try {
            roleRepository.findByName(RoleName.valueOf(name.uppercase()))
        } catch (e: IllegalArgumentException) {
            null
        }
    }
}
