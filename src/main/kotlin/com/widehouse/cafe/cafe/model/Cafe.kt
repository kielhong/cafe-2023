package com.widehouse.cafe.cafe.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "cafe")
class Cafe(
    @Id
    val url: String,
    var name: String,
    var description: String,
    @Indexed
    var category: Category
) {
    fun update(name: String, description: String, category: Category) {
        this.name = name
        this.description = description
        this.category = category
    }
}
