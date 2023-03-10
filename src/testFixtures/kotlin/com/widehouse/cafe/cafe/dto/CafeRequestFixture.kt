package com.widehouse.cafe.cafe.dto

class CafeRequestFixture {
    companion object {
        fun create() =
            CafeRequest("test", "test", "desc", 1L)
    }
}
