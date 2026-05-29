package com.test.users.app.config

import com.test.users.app.ui.view.LoginView
import com.vaadin.flow.spring.security.VaadinWebSecurity
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class SecurityConfig : VaadinWebSecurity() {

    override fun configure(http: HttpSecurity) {

        http
            .formLogin {
                it.loginPage("/login")
                    .defaultSuccessUrl("/", true)
            }
            .logout {
                it.logoutUrl("/logout")
                    .logoutSuccessUrl("/login")
            }

        super.configure(http)

        setLoginView(http, LoginView::class.java)
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}