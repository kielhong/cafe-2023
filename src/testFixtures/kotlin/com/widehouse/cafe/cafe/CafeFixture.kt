package com.widehouse.cafe.cafe

class CafeFixture {
    companion object {
        fun create() =
            Cafe("test", "test", "desc", 1L)
        fun from(index: Int, categoryId: Long = 1L) =
            Cafe("test$index", "test$index", "desc$index", categoryId)
    }
}
