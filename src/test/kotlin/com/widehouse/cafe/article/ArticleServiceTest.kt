package com.widehouse.cafe.article

import com.widehouse.cafe.article.dto.ArticleRequestFixture
import com.widehouse.cafe.article.model.Article
import com.widehouse.cafe.common.sequence.SequenceService
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK

class ArticleServiceTest : StringSpec() {
    private lateinit var service: ArticleService

    @MockK
    private lateinit var articleRepository: ArticleRepository

    @MockK
    private lateinit var sequenceService: SequenceService

    init {
        coroutineTestScope = true

        beforeEach {
            MockKAnnotations.init(this)

            service = ArticleService(articleRepository, sequenceService)
            coEvery { sequenceService.generateSequence(Article.SEQUENCE_NAME) } returns 1L
        }

        "create Article" {
            // given
            coEvery { articleRepository.save(any()) } returnsArgument 0
            // when
            val request = ArticleRequestFixture.create()
            val result = service.create(request)
            // then
            result.id shouldBe 1L
            result.cafeUrl shouldBe request.cafeUrl
            result.boardId shouldBe request.boardId
            result.subject shouldBe request.subject
            result.content shouldBe request.content
            result.createdAt.shouldNotBeNull()
        }
    }
}
