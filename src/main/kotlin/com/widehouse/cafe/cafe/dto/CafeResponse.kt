package com.widehouse.cafe.cafe.dto

import com.widehouse.cafe.cafe.model.Cafe
import com.widehouse.cafe.cafe.model.Category

class CafeResponse(
    val url: String,
    val name: String,
    val description: String,
    val category: Category
) {
    companion object {
        fun from(cafe: Cafe) =
            CafeResponse(
                cafe.url,
                cafe.name,
                cafe.description,
                cafe.category
            )
    }
}
