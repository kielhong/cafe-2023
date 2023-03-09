package com.widehouse.cafe.cafe.dto

import com.widehouse.cafe.cafe.Cafe

data class CafeRequest(
    val url: String,
    val name: String,
    val description: String
) {
    fun toModel() =
        Cafe(
            this.url,
            this.name,
            this.description
        )
}
