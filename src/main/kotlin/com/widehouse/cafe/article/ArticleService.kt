package com.widehouse.cafe.article

import com.widehouse.cafe.article.dto.ArticleRequest
import com.widehouse.cafe.article.dto.ArticleResponse
import com.widehouse.cafe.article.model.Article
import com.widehouse.cafe.board.BoardRepository
import com.widehouse.cafe.common.exception.DataNotFoundException
import com.widehouse.cafe.common.exception.ForbiddenException
import com.widehouse.cafe.common.sequence.SequenceService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
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
    suspend fun delete(user: UserDetails, articleId: Long) {
        val article = articleRepository.findById(articleId)
            ?: throw DataNotFoundException("$articleId not found exception")
        if (article.username != user.username) {
            throw ForbiddenException("article delete forbidden")
        }

        articleRepository.delete(article)
    }

    /**
     * 참조 : https://github.com/RobertHeim/spring-security-bug-preauth-coroutines
     */
    @PreAuthorize("#article[0].username == authentication.principal.username")
    suspend fun test(article: Article) {
        val authentication: Authentication = SecurityContextHolder.getContext().authentication

        println("article.username = ${article.username}")
        println("authentication.principal = ${authentication.principal}")
    }
}
