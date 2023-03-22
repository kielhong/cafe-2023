package com.widehouse.cafe.article

import com.widehouse.cafe.article.dto.ArticleRequest
import com.widehouse.cafe.article.dto.ArticleResponse
import com.widehouse.cafe.article.service.ArticleService
import org.springframework.security.core.userdetails.UserDetailsService
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
    private val articleService: ArticleService,
    private val userDetailsService: UserDetailsService
) {
    @PostMapping
    suspend fun create(
        principal: Principal,
        @RequestBody request: ArticleRequest
    ): ArticleResponse {
        val user = userDetailsService.loadUserByUsername(principal.name)
        return articleService.create(user, request)
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
        principal: Principal,
        @PathVariable articleId: Long
    ) {
        return articleService.delete(articleId)
    }
}
