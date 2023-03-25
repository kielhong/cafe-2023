package com.widehouse.cafe.article.service

import com.widehouse.cafe.article.ArticleRepository
import com.widehouse.cafe.article.model.Article
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service

@Service
class ArticleDomainService(
    private val articleRepository: ArticleRepository
) {
    suspend fun getArticleById(articleId: Long): Article? {
        return articleRepository.findById(articleId)
    }

    suspend fun create(article: Article): Article {
        return articleRepository.save(article)
    }

    @PreAuthorize("#article[0].username == authentication.principal.username")
    suspend fun update(article: Article): Article {
        return articleRepository.save(article)
    }

    @PreAuthorize("#article[0].username == authentication.principal.username")
    suspend fun delete(article: Article) {
        articleRepository.delete(article)
    }
}
