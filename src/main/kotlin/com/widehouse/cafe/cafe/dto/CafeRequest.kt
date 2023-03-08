package com.widehouse.cafe.cafe.dto

import com.widehouse.cafe.cafe.Cafe

data class CafeRequest(
    val url: String,
    val name: String,
    val description: String
) {
    companion object {
        fun toModel(cafeRequest: CafeRequest) =
            Cafe(
                cafeRequest.url,
                cafeRequest.name,
                cafeRequest.description
            )
    }
}
