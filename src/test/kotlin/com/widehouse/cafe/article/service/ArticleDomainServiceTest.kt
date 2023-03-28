package com.widehouse.cafe.article.service

import com.widehouse.cafe.article.ArticleFixture
import com.widehouse.cafe.article.model.ArticleRepository
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk

class ArticleDomainServiceTest : StringSpec() {
    private val articleRepository = mockk<ArticleRepository>()

    private val service = ArticleDomainService(articleRepository)

    init {
        isolationMode = IsolationMode.InstancePerLeaf
        coroutineTestScope = true

        val article = ArticleFixture.create()

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
