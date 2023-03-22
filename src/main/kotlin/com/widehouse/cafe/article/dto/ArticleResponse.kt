package com.widehouse.cafe.article.dto

import com.widehouse.cafe.article.model.Article
import java.time.LocalDateTime

data class ArticleResponse(
    val id: Long,
    val cafeUrl: String,
    val boardId: Long,
    val subject: String,
    val content: String,
    val username: String,
    val createdAt: LocalDateTime
) {
    companion object {
        fun from(article: Article) =
            ArticleResponse(
                article.id,
                article.cafeUrl,
                article.boardId,
                article.subject,
                article.content,
                article.username,
                article.createdAt
            )
    }
}
