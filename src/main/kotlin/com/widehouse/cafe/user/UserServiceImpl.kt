package com.widehouse.cafe.user

import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserServiceImpl : UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        return User.withUsername(username)
            .password("user")
            .roles(Role.USER.value)
            .build()
    }
}
