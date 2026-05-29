package com.test.users.app.ui.view

import com.test.users.app.ui.component.UserGrid
import com.github.mvysny.karibudsl.v10.h2
import com.test.users.app.service.UserService
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import com.vaadin.flow.spring.security.AuthenticationContext
import jakarta.annotation.security.RolesAllowed

@Route("")
@PageTitle("Dashboard")
@RolesAllowed("USER", "ADMIN")
class DashboardView(
private val userService: UserService,
private val authenticationContext: AuthenticationContext
)  : VerticalLayout() {

    init {
        setSizeFull()
        isPadding = true
        isSpacing = true

        h2("Dashboard")

        add(
            UserGrid(userService, authenticationContext)
        )
    }
}