package com.widehouse.cafe.article.service

import com.widehouse.cafe.article.ArticleFixture
import com.widehouse.cafe.article.ArticleRepository
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.just

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
            val result = service.getArticleById(1L)
            // then
            result shouldBe article
        }

        "delete Article" {
            val article = ArticleFixture.create()
            coEvery { articleRepository.delete(any()) } just Runs
            // when
            service.delete(article)
            // then
            coVerify { articleRepository.delete(article) }
        }
    }
}
