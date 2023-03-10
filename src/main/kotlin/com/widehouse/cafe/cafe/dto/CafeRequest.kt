package com.widehouse.cafe.cafe.dto

data class CafeRequest(
    val url: String,
    val name: String,
    val description: String,
    val categoryId: Long
)
