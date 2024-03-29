package com.widehouse.cafe.article

import com.widehouse.cafe.article.model.Article
import java.time.LocalDateTime

object ArticleFixture {
    fun create(id: Long = 1L, username: String = "user") =
        Article(id, "cafe", 1L, username, "subject$id", "content$id", LocalDateTime.now())
}
