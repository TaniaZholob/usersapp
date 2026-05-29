package com.test.users.app.ui.component

import com.test.users.app.dto.UserDto
import com.test.users.app.service.UserService
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.orderedlayout.VerticalLayout

class UserGrid(
    private val userService: UserService
) : VerticalLayout() {

    private val grid = Grid(UserDto::class.java, false)

    init {
        setSizeFull()

        grid.apply {
            setSizeFull()

            addColumn(UserDto::name)
                .setHeader("Name")
                .setSortable(true)

            addColumn(UserDto::email)
                .setHeader("Email")
                .setSortable(true)

            addColumn { it.createdAt.toString() }
                .setHeader("Created At")
                .setSortable(true)

            addColumn { it.updatedAt.toString() }
                .setHeader("Updated At")
                .setSortable(true)

            setItems(userService.getAllUsers())
        }

        add(grid)
    }
}