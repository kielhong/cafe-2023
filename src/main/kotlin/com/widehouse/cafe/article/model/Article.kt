package com.widehouse.cafe.article.model

import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document("article")
class Article(
    val id: Long,
    val cafeUrl: String,
    val boardId: Long,
    val username: String,
    val subject: String,
    val content: String,
    val createdAt: LocalDateTime
) {
    companion object {
        @Transient
        const val SEQUENCE_NAME = "article_sequence"
    }
}
