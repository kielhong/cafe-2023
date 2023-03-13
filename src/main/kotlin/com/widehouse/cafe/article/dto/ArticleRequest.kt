package com.widehouse.cafe.article.dto

data class ArticleRequest(
    val cafeUrl: String,
    val boardId: Long,
    val subject: String,
    val content: String
)
