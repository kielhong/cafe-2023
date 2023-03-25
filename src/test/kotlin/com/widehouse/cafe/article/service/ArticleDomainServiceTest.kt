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

        val article = ArticleFixture.create()

        beforeEach {
            MockKAnnotations.init(this)

            service = ArticleDomainService(articleRepository)
        }

        afterEach {
            clearAllMocks()
        }

        "getArticle by id then return Article" {
            coEvery { articleRepository.findById(any()) } returns article
            // when
            val result = service.getArticleById(1L)
            // then
            result shouldBe article
        }

        "create Article" {
            coEvery { articleRepository.save(any()) } returnsArgument 0
            // when
            service.create(article)
            // then
            coVerify { articleRepository.save(article) }
        }

        "update Article" {
            coEvery { articleRepository.save(any()) } returnsArgument 0
            // when
            service.update(article)
            // then
            coVerify { articleRepository.save(article) }
        }

        "delete Article" {
            coEvery { articleRepository.delete(any()) } just Runs
            // when
            service.delete(article)
            // then
            coVerify { articleRepository.delete(article) }
        }
    }
}
