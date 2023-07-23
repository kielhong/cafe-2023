package com.widehouse.cafe.user.service

import com.widehouse.cafe.user.model.UserRepository
import kotlinx.coroutines.runBlocking
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class AccountService(
    private val userRepository: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails? {
        return runBlocking {
            userRepository.findByUsername(username)?.let {
                User.withUsername(username)
                    .password(it.password)
                    .roles(*it.roles.map { role -> role.value }.toTypedArray())
                    .build()
            }
        }
    }
}
