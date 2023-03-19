package com.widehouse.cafe.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.invoke
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
@EnableWebFluxSecurity
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
            .password("user")
            .roles("USER")
            .build()
        return MapReactiveUserDetailsService(userDetails)
    }
}
