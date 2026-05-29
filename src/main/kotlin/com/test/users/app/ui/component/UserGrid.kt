package com.test.users.app.ui.component

import com.test.users.app.dto.UserDto
import com.test.users.app.service.UserService
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.confirmdialog.ConfirmDialog
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.provider.CallbackDataProvider
import com.vaadin.flow.data.value.ValueChangeMode
import com.vaadin.flow.spring.security.AuthenticationContext
import org.springframework.data.domain.PageRequest
import org.springframework.security.core.context.SecurityContextHolder
import java.time.format.DateTimeFormatter

class UserGrid(
    private val userService: UserService,
    private val authenticationContext: AuthenticationContext
) : VerticalLayout() {

    private val grid = Grid(UserDto::class.java, false)
    private val searchField = TextField()
    private val admin = isAdmin()
    private val addButton = Button("Add User")
    private val logoutButton = Button("Logout")
    private val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")

    init {

        setSizeFull()
        configureSearch()
        configureGrid()
        configureDataProvider()

        if (admin) {
            configureAdminActions()
        }

        logoutButton.addClickListener {
            authenticationContext.logout()
        }
        add(
            HorizontalLayout(searchField, addButton, logoutButton),
            grid
        )
    }

    private fun configureSearch() {

        searchField.apply {
            placeholder = "Search by name or email"
            isClearButtonVisible = true
            valueChangeMode = ValueChangeMode.LAZY

            addValueChangeListener { grid.dataProvider.refreshAll() }
        }
    }

    private fun configureGrid() {

        grid.apply {

            setSizeFull()

            addColumn(UserDto::name)
                .setHeader("Name")
                .setSortable(true)

            addColumn(UserDto::email)
                .setHeader("Email")
                .setSortable(true)

            addColumn { it.createdAt.format(formatter) }
                .setHeader("Created At")
                .setSortable(true)

            addColumn { it.updatedAt.format(formatter) }
                .setHeader("Updated At")
                .setSortable(true)

            if (admin) {
                addComponentColumn { user ->
                    HorizontalLayout(
                        Button("Edit") { openEditDialog(user) },
                        Button("Delete") { openDeleteDialog(user) }
                    )
                }.setHeader("Actions")
            }
        }
    }

    private fun configureAdminActions() {

        addButton.addClickListener {
            openCreateDialog()
        }
    }

    private fun configureDataProvider() {

        val provider = CallbackDataProvider<UserDto, Void>(
            { query ->

                val search = searchField.value ?: ""

                val page = PageRequest.of(
                    query.offset / query.pageSize,
                    query.pageSize
                )

                userService.searchUsers(search, page).stream()
            },
            { _ ->

                val search = searchField.value ?: ""

                userService.searchUsers(
                    search,
                    PageRequest.of(0, 1)
                ).totalElements.toInt()
            }
        )
        grid.setDataProvider(provider)
    }

    private fun openCreateDialog() {
        UserFormDialog(
            onSave = { form ->
                userService.createUser(
                    name = form.name,
                    email = form.email,
                    password = form.password ?: "",
                    role = form.role
                )

                grid.dataProvider.refreshAll()
                Notification.show("User created")
            }
        ).open()
    }

    private fun openEditDialog(user: UserDto) {
        UserFormDialog(
            user = user,
            onSave = { form ->
                userService.updateUser(
                    id = user.id!!,
                    name = form.name,
                    email = form.email,
                    role = form.role
                )

                grid.dataProvider.refreshAll()
                Notification.show("User updated")
            }

        ).open()
    }

    private fun openDeleteDialog(user: UserDto) {
        ConfirmDialog(
            "Delete User",
            "Are you sure you want to delete ${user.name}?",
            "Delete",
            {
                userService.deleteUser(user.id!!)
                grid.dataProvider.refreshAll()
                Notification.show("User deleted")
            },
            "Cancel"
        ) {}.open()
    }

    private fun isAdmin(): Boolean {
        return SecurityContextHolder.getContext()
            .authentication
            .authorities
            .any { it.authority == "ROLE_ADMIN" }
    }
}