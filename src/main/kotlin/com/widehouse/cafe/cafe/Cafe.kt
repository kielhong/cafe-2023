package com.widehouse.cafe.cafe

import com.widehouse.cafe.cafe.dto.CafeRequest
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "cafe")
class Cafe(
    @Id
    val url: String,
    var name: String,
    var description: String,
    var categoryId: Long
) {
    fun update(request: CafeRequest) {
        name = request.name
        description = request.description
        categoryId = request.categoryId
    }
}
