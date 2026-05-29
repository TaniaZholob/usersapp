package com.test.users.app.exception

class UserNotFoundException : RuntimeException {

    constructor(id: Long) : super("User with id '$id' not found")
    constructor(email: String) : super("User with email '$email' not found")

}