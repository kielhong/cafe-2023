package com.widehouse.cafe.cafe

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "cafe")
class Cafe(
    @Id
    val url: String,
    val name: String,
    val description: String
)
