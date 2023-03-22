package com.widehouse.cafe.article.service

import com.widehouse.cafe.article.ArticleFixture
import com.widehouse.cafe.article.ArticleRepository
import com.widehouse.cafe.article.model.Article
import com.widehouse.cafe.board.model.BoardFixture
import com.widehouse.cafe.common.exception.DataNotFoundException
import com.widehouse.cafe.user.Role
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails

class ArticleDomainServiceTest : StringSpec() {
    private lateinit var service: ArticleDomainService

    @MockK
    private lateinit var articleRepository: ArticleRepository

    init {
        isolationMode = IsolationMode.InstancePerLeaf
        coroutineTestScope = true

        beforeEach {
            MockKAnnotations.init(this)

            service = ArticleDomainService(articleRepository)
        //            coEvery { sequenceService.generateSequence(Article.SEQUENCE_NAME) } returns 1L
        }

        afterEach {
            clearAllMocks()
        }

        "getArticle by id then return Article" {
            val article = ArticleFixture.create()
            coEvery { articleRepository.findById(any()) } returns article
            // when
            val result = service.getArticle(1L)
            // then
            result shouldBe article
        }

        "getArticle by id given not exist article then return Article" {
            coEvery { articleRepository.findById(any()) } returns null
            // when
            shouldThrow<DataNotFoundException> {
                service.getArticle(1L)
            }
        }
    }
}