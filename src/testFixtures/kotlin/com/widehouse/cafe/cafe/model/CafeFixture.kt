package com.widehouse.cafe.cafe.model

class CafeFixture {
    companion object {
        fun create() =
            Cafe("test", "test", "desc", Category(1L, ""))
        fun from(index: Int, category: Category = Category(1L, "")) =
            Cafe("test$index", "test$index", "desc$index", category)
    }
}
