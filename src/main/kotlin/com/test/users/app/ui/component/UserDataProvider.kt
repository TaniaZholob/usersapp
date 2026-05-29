package com.test.users.app.ui.dataprovider

import com.test.users.app.dto.UserDto
import com.test.users.app.service.UserService
import com.test.users.app.ui.common.SafeExecutor
import com.vaadin.flow.data.provider.CallbackDataProvider
import com.vaadin.flow.data.provider.QuerySortOrder
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort

class UserDataProvider(
    private val userService: UserService,
    private val searchProvider: () -> String
) {

    fun build(): CallbackDataProvider<UserDto, Void> {

        return CallbackDataProvider(
            { query ->
                val sort = convertVaadinSortToSpringSort(query.sortOrders)
                val pageRequest = PageRequest.of(
                    query.offset / query.pageSize,
                    query.pageSize,
                    sort
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

    private fun convertVaadinSortToSpringSort(
        sortOrders: List<QuerySortOrder>
    ): Sort {

        val orders = sortOrders.map { sortOrder ->
            val direction = when (sortOrder.direction) {
                com.vaadin.flow.data.provider.SortDirection.ASCENDING -> Sort.Direction.ASC
                else -> Sort.Direction.DESC
            }
            Sort.Order(direction, sortOrder.sorted)
        }

        return Sort.by(orders)
    }
}