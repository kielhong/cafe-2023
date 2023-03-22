package com.widehouse.cafe.article.service

import com.widehouse.cafe.article.ArticleRepository
import com.widehouse.cafe.article.model.Article
import com.widehouse.cafe.common.exception.DataNotFoundException
import org.springframework.stereotype.Service

@Service
class ArticleDomainService(
    private val articleRepository: ArticleRepository
) {
    suspend fun getArticle(articleId: Long): Article {
        return articleRepository.findById(articleId)
            ?: throw DataNotFoundException("$articleId not found")
    }
}