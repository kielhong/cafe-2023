package com.widehouse.cafe.cafe.model

import org.springframework.data.mongodb.core.mapping.Document

@Document("category")
class Category(
    val id: Long,
    val name: String
)
