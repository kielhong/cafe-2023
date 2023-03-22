package com.widehouse.cafe.article.service

import com.widehouse.cafe.article.ArticleRepository
import com.widehouse.cafe.article.dto.ArticleRequest
import com.widehouse.cafe.article.dto.ArticleResponse
import com.widehouse.cafe.article.model.Article
import com.widehouse.cafe.board.BoardRepository
import com.widehouse.cafe.common.exception.DataNotFoundException
import com.widehouse.cafe.common.sequence.SequenceService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class ArticleService(
    private val articleDomainService: ArticleDomainService,
    private val articleRepository: ArticleRepository,
    private val boardRepository: BoardRepository,
    private val sequenceService: SequenceService
) {
    @Transactional
    suspend fun create(user: UserDetails, request: ArticleRequest): ArticleResponse {
        requireNotNull(boardRepository.findById(request.boardId))

        val id = sequenceService.generateSequence(Article.SEQUENCE_NAME)
        val article = Article(id, request.cafeUrl, request.boardId, user.username, request.subject, request.content, LocalDateTime.now())

        return articleDomainService.create(article)
            .run { ArticleResponse.from(this) }
    }

    @Transactional
    suspend fun update(articleId: Long, request: ArticleRequest): ArticleResponse {
        // TODO : user
        val article = articleRepository.findById(articleId)
            ?.let { Article(it.id, it.cafeUrl, request.boardId, "username", request.subject, request.content, it.createdAt) }
            ?: throw DataNotFoundException("$articleId not found exception")

        return articleRepository.save(article)
            .run {
                ArticleResponse.from(this)
            }
    }

    @Transactional
    suspend fun delete(articleId: Long) {
        articleDomainService.getArticleById(articleId)
            ?.let { articleDomainService.delete(it) }
            ?: throw DataNotFoundException("$articleId not found")
    }
}
