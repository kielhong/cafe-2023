package com.widehouse.cafe.config

import com.widehouse.cafe.user.model.Role
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.invoke
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
@EnableWebFluxSecurity
@EnableMethodSecurity(prePostEnabled = true)
class SecurityConfig {
    @Bean
    fun springSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http {
            authorizeExchange {
                authorize("/articles/**", authenticated)
                authorize(anyExchange, permitAll)
            }
            csrf { disable() }
            httpBasic { }
            formLogin { disable() }
        }
    }

    /**
     * TODO : refactoring.
     */
    @Bean
    fun userDetailsService(): ReactiveUserDetailsService {
        val userDetails = User.withUsername("user")
            .password("password")
            .roles(Role.USER.value)
            .build()
        return MapReactiveUserDetailsService(userDetails)
    }
}
