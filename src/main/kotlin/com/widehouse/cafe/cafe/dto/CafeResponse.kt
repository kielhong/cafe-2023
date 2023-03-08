package com.widehouse.cafe.cafe.dto

import com.widehouse.cafe.cafe.Cafe

class CafeResponse(
    val url: String,
    val name: String,
    val description: String
) {
    companion object {
        fun from(cafe: Cafe) =
            CafeResponse(
                cafe.url,
                cafe.name,
                cafe.description
            )
    }
}
