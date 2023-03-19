package com.widehouse.cafe.article

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
    private val articleRepository: ArticleRepository,
    private val boardRepository: BoardRepository,
    private val sequenceService: SequenceService
) {
    @Transactional
    suspend fun create(user: UserDetails, request: ArticleRequest): ArticleResponse {
        requireNotNull(boardRepository.findById(request.boardId))

        val article = sequenceService.generateSequence(Article.SEQUENCE_NAME)
            .run {
                Article(this, request.cafeUrl, request.boardId, user.username, request.subject, request.content, LocalDateTime.now())
            }

        return articleRepository.save(article)
            .run {
                ArticleResponse.from(this)
            }
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
        articleRepository.findById(articleId)
            ?: throw DataNotFoundException("$articleId not found exception")

        articleRepository.deleteById(articleId)
    }
}
