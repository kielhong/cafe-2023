package com.widehouse.cafe.user.model

import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface UserRepository : CoroutineCrudRepository<User, String> {
    suspend fun findByUsername(username: String): User?
}
