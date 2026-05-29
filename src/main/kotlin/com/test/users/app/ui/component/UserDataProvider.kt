package com.test.users.app.ui.dataprovider

import com.test.users.app.dto.UserDto
import com.test.users.app.service.UserService
import com.test.users.app.ui.common.SafeExecutor
import com.vaadin.flow.data.provider.CallbackDataProvider
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest

class UserDataProvider(
    private val userService: UserService,
    private val searchProvider: () -> String
) {

    fun build(): CallbackDataProvider<UserDto, Void> {

        return CallbackDataProvider(
            { query ->
                val pageRequest = PageRequest.of(
                    query.offset / query.pageSize,
                    query.pageSize
                )

                val page = SafeExecutor.nullable {
                    userService.searchUsers(
                        searchProvider(),
                        pageRequest
                    )
                }

                (page ?: Page.empty()).stream()
            },
            {

                SafeExecutor.get(
                    action = {
                        userService.searchUsers(
                            searchProvider(),
                            PageRequest.of(0, 1)
                        ).totalElements.toInt()
                    },
                    fallback = 0
                )
            }
        )
    }
}