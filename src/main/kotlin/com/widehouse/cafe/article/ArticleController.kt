package com.widehouse.cafe.article

import com.widehouse.cafe.article.dto.ArticleRequest
import com.widehouse.cafe.article.dto.ArticleResponse
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("articles")
class ArticleController(
    private val articleService: ArticleService
) {
    @PostMapping
    suspend fun create(
        principal: Principal,
        @RequestBody request: ArticleRequest
    ): ArticleResponse {
        return articleService.create(request)
    }

    @PutMapping("{articleId}")
    suspend fun update(
        @PathVariable articleId: Long,
        @RequestBody request: ArticleRequest
    ): ArticleResponse {
        return articleService.update(articleId, request)
    }

    @DeleteMapping("{articleId}")
    suspend fun delete(
        @PathVariable articleId: Long
    ) {
        return articleService.delete(articleId)
    }
}
