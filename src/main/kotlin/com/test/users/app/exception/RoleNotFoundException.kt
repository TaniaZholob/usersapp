package com.test.users.app.exception

class RoleNotFoundException(role: String) :
    RuntimeException("Role '$role' not found")