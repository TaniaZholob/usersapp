package com.test.users.app.repository

import com.test.users.app.entity.Role
import com.test.users.app.entity.RoleName
import com.test.users.app.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository : JpaRepository<Role, Long> {

     fun findByName(name: RoleName): Role?
}