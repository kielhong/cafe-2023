package com.widehouse.cafe.user.model

import org.springframework.data.mongodb.core.mapping.Document

@Document("user")
class User(
    val username: String,
    val password: String,
    var roles: Set<Role>
)
