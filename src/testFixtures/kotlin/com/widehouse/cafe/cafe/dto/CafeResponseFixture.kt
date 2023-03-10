package com.widehouse.cafe.cafe.dto

import com.widehouse.cafe.cafe.model.Category

class CafeResponseFixture {
    companion object {
        fun create(url: String = "test", name: String = "test", description: String = "desc", categoryId: Long = 1L) =
            CafeResponse(url, name, description, Category(categoryId, ""))

        fun from(index: Int, categoryId: Long = 1L) =
            create("test$index", "test$index", "desc$index", categoryId)
    }
}
