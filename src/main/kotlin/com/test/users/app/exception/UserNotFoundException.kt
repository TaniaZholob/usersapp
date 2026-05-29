package com.test.users.app.exception

class UserNotFoundException(id: Long) :
    RuntimeException("User with id '$id' not found")