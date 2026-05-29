package com.test.users.app.exception

class UserAlreadyExistsException(email: String) :
    RuntimeException("User with email '$email' already exists")