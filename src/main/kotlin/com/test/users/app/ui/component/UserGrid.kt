package com.test.users.app.ui.component

import com.test.users.app.dto.UserDto
import com.test.users.app.service.UserService
import com.test.users.app.ui.common.SafeExecutor
import com.test.users.app.ui.dataprovider.UserDataProvider
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.confirmdialog.ConfirmDialog
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.value.ValueChangeMode
import com.vaadin.flow.spring.security.AuthenticationContext
import org.springframework.security.core.userdetails.UserDetails
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
        configureActions()

        val toolbar = if (admin) {
            HorizontalLayout(searchField, addButton, logoutButton)
        } else {
            HorizontalLayout(searchField, logoutButton)
        }

        add(toolbar, grid)
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

    private fun configureDataProvider() {

        val provider = UserDataProvider(
            userService = userService,
            searchProvider = { searchField.value ?: "" }
        ).build()

        grid.setDataProvider(provider)
    }

    private fun configureActions() {

        if (admin) {
            addButton.addClickListener {
                openCreateDialog()
            }
        }

        logoutButton.addClickListener {
            val ui = UI.getCurrent()
            authenticationContext.logout()
            ui?.page?.setLocation("/login")
        }
    }

    private fun openCreateDialog() {
        lateinit var dialog: UserFormDialog
        dialog = UserFormDialog(
            onSave = { form ->
                addButton.isEnabled = false
                SafeExecutor.run {
                    try {
                        userService.createUser(
                            name = form.name,
                            email = form.email,
                            password = form.password ?: "",
                            role = form.role
                        )

                        grid.dataProvider.refreshAll()
                        Notification.show("User created")
                        dialog.close()
                    } finally {
                        addButton.isEnabled = true
                    }
                }
            }
        )
        dialog.open()
    }

    private fun openEditDialog(user: UserDto) {
        lateinit var dialog: UserFormDialog
        dialog = UserFormDialog(
            user = user,
            onSave = { form ->
                addButton.isEnabled = false
                SafeExecutor.run {
                    try {
                        userService.updateUser(
                            id = user.id!!,
                            name = form.name,
                            email = form.email,
                            role = form.role
                        )

                        grid.dataProvider.refreshAll()
                        Notification.show("User updated")
                        dialog.close()
                    } finally {
                        addButton.isEnabled = true
                    }
                }
            }
        )
        dialog.open()
    }

    private fun openDeleteDialog(user: UserDto) {
        val dialog = ConfirmDialog()
        dialog.setHeader("Delete User")
        dialog.setText("Are you sure you want to delete ${user.name}?")
        dialog.setCancelable(true)
        dialog.setConfirmText("Delete")
        dialog.setCancelText("Cancel")
        dialog.addConfirmListener {
            addButton.isEnabled = false
            SafeExecutor.run {
                try {
                    userService.deleteUser(user.id!!)
                    grid.dataProvider.refreshAll()
                    Notification.show("User deleted")
                } finally {
                    addButton.isEnabled = true
                }
            }
        }
        dialog.open()
    }

    private fun isAdmin(): Boolean {
        return authenticationContext.getAuthenticatedUser(UserDetails::class.java).map { user ->
                user.authorities.any {
                    it.authority == "ROLE_ADMIN"
                }
            }
            .orElse(false)
    }
}