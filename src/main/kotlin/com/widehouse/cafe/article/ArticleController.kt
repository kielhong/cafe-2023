package com.widehouse.cafe.article

import com.widehouse.cafe.article.dto.ArticleRequest
import com.widehouse.cafe.article.dto.ArticleResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("articles")
class ArticleController(
    private val articleService: ArticleService
) {
    @PostMapping
    suspend fun create(@RequestBody request: ArticleRequest): ArticleResponse {
        return articleService.create(request)
    }
}
