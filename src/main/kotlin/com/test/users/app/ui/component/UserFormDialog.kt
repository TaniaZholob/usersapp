package com.test.users.app.ui.component

import com.test.users.app.dto.UserDto
import com.test.users.app.dto.UserFormData
import com.test.users.app.entity.RoleName
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.EmailField
import com.vaadin.flow.component.textfield.PasswordField
import com.vaadin.flow.component.textfield.TextField

class UserFormDialog(
    private val onSave: (UserFormData) -> Unit,
    user: UserDto? = null
) : Dialog() {

    private val nameField = TextField("Name")
    private val emailField = EmailField("Email")
    private val passwordField = PasswordField("Password")
    private val roleField = ComboBox<String>("Role")

    init {

        width = "400px"
        roleField.setItems(
            RoleName.ADMIN.name,
            RoleName.USER.name
        )

        roleField.isRequired = true

        if (user != null) {
            nameField.value = user.name
            emailField.value = user.email
            roleField.value = user.role
        } else {
            passwordField.isRequired = true
        }

        val formLayout = FormLayout(
            nameField,
            emailField,
            roleField,
            passwordField
        )

        val saveButton = Button("Save") {

            if (nameField.value.isBlank() ||
                emailField.value.isBlank() ||
                roleField.value.isNullOrBlank()
            ) {
                Notification.show("Please fill all required fields")
                return@Button
            }

            if (user == null && passwordField.value.isBlank()) {
                Notification.show("Password is required")
                return@Button
            }

            onSave(
                UserFormData(
                    name = nameField.value.trim(),
                    email = emailField.value.trim(),
                    password = if (user == null) passwordField.value else null,
                    role = roleField.value
                )
            )

            close()
        }

        val cancelButton = Button("Cancel") {
            close()
        }

        add(
            VerticalLayout(
                formLayout,
                HorizontalLayout(saveButton, cancelButton)
            )
        )
    }
}