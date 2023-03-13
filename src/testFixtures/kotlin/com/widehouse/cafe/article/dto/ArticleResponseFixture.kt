package com.widehouse.cafe.article.dto

import java.time.LocalDateTime

object ArticleResponseFixture {
    fun create() =
        ArticleResponse(1L, "cafe", 1L, "subject", "content", LocalDateTime.now())
}
